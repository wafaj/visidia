package visidia.gui.graphview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import visidia.graph.Graph;
import visidia.graph.Vertex;
import visidia.gui.graphview.ViewableItem.ItemState;
import visidia.gui.undo.AddGraphItemEdit;
import visidia.gui.undo.ChangeOrientation;
import visidia.gui.undo.RemoveGraphItemEdit;
import visidia.gui.undo.UndoStack;
import visidia.gui.window.VisidiaPanel;
import visidia.graph.Edge;
import visidia.misc.property.PropertyTable;

// TODO: Auto-generated Javadoc
/**
 * The GraphView class manages graph visualization. It uses a factory to build views of the graph elements.
 * GraphView offers an undo/redo system on several graph operations: edge/vertex creation and removal, merging of vertices.  
 */
public class GraphView implements Serializable {

	private static final long serialVersionUID = -4423613024856999280L;

	/** The factory. */
	transient private GraphViewFactory factory;

	/** The graph. */
	protected Graph graph;

	/** The vertex items. */
	protected Vector<VertexView> vertexItems;

	/** The edge items. */
	protected Vector<EdgeView> edgeItems;

	/** The decorative items represent viewable items excluding graph edges and vertices. */
	protected Vector<ViewableItem> decorativeItems = new Vector<ViewableItem>();

	/** The recoverable items in an internal variable involved in the undo/redo system. */
	private Vector<GraphItemView> recoverableItems = null;

	/** The undo stack. */
	transient private UndoStack undoStack = null;

	/**
	 * Instantiates a new graph and a new view on this graph.
	 */
	public GraphView() {
		factory = VisidiaPanel.getFactory();
		graph = new Graph();
		vertexItems = new Vector<VertexView>();
		edgeItems = new Vector<EdgeView>();
		recoverableItems = new Vector<GraphItemView>();
		undoStack = new UndoStack();
	}
	
	/**
	 * Creates a "clean" copy of graphview, removing all disabled items from the graph.
	 * Does not copy decorative items.
	 * 
	 * @return the new graph view
	 */
	public GraphView cleanCopy() {
		GraphView gv = new GraphView();

		// maintains a link between old vertex (current graphview) and new vertex (graphview copy)
		// the hashtable stores <old vertex, new vertex> entries.
		Hashtable<VertexView, VertexView> theVertices = new Hashtable<VertexView, VertexView>();
		
		Enumeration<VertexView> vItems = vertexItems.elements();
		while (vItems.hasMoreElements()) {
			VertexView view = vItems.nextElement();
			if (view.getState() != ItemState.DELETED) {
				Point pos = view.getPosition();
				VertexView newView = gv.createVertexAndView(pos.x, pos.y); // will assign new vertex ids
				newView.vertex.setProperties((PropertyTable) view.vertex.clone());
				theVertices.put(view, newView);
			}
		}

		Enumeration<EdgeView> eItems = edgeItems.elements();
		while (eItems.hasMoreElements()) {
			EdgeView view = eItems.nextElement();
			if (view.getState() != ItemState.DELETED) {
				VertexView origin = theVertices.get(view.getOrigin());
				VertexView destination = theVertices.get(view.getDestination());
				EdgeView newView = gv.createEdgeAndView(origin, destination, view.getEdge().isOriented());
				newView.getEdge().setProperties((PropertyTable) view.getEdge().clone());
				newView.mark(view.isMarked());
			}
		}

		gv.recoverableItems.clear();
		gv.undoStack.clear();
		
		return gv;
	}

	/**
	 * Adds a decorative item.
	 * 
	 * @param item the item
	 */
	public void addDecorativeItem(ViewableItem item) {
		if (! decorativeItems.contains(item))
			decorativeItems.add(item);
	}

	/**
	 * Removes a decorative item.
	 * 
	 * @param item the item
	 */
	public void removeDecorativeItem(ViewableItem item) {
		decorativeItems.remove(item);
	}

	/**
	 * Reset items properties.
	 */
	public void resetItemsProperties() {
		// reset nodes
		Enumeration<VertexView> vItems = vertexItems.elements();
		while (vItems.hasMoreElements()) {
			VertexView view = vItems.nextElement();
			view.vertex.resetProperties();
			view.resetState();
		}

		// reset edges
		Enumeration<EdgeView> eItems = edgeItems.elements();
		while (eItems.hasMoreElements()) {
			EdgeView view = eItems.nextElement();
			view.getEdge().resetProperties();
			view.resetState();
		}

		Enumeration<ViewableItem> items = decorativeItems.elements();
		while (items.hasMoreElements())
			items.nextElement().resetState();
		decorativeItems.clear();
		decorativeItems = new Vector<ViewableItem>();
	}
	
	/**
	 * Undo last operation.
	 */
	public void undo() {
		undoStack.undo();
	}

	/**
	 * Redo last undo'ed operation.
	 */
	public void redo() {
		undoStack.redo();
	}

	/**
	 * Gets the graph.
	 * 
	 * @return the graph
	 */
	public Graph getGraph() {
		return graph;
	}

	/**
	 * Gets the undo stack.
	 * 
	 * @return the undo stack
	 */
	public UndoStack getUndoStack() {
		return undoStack;
	}

	/**
	 * Restore the view's previous state.
	 * 
	 * @param view the view
	 */
	public void restoreView(GraphItemView view) {
		if (!recoverableItems.contains(view)) return;
		recoverableItems.remove(view);
		view.activate();

		if (view instanceof VertexView)
			vertexItems.add((VertexView) view);
		else if (view instanceof EdgeView){
			edgeItems.add((EdgeView) view);
			((EdgeView) view).getOrigin().addEdge((EdgeView) view);
			((EdgeView) view).getDestination().addEdge((EdgeView) view);
			
		}
	}

	/**
	 * Removes the item view.
	 * 
	 * @param view the view
	 */
	public void removeView(GraphItemView view) {
		recoverableItems.add(view);
		view.delete();

		if (view instanceof VertexView){
			Enumeration<EdgeView> edges = ((VertexView)view).getEdges();
			Vector<EdgeView> edgesToBeRemoved = new Vector<EdgeView>(10, 10);
			while (edges.hasMoreElements()) {
				edgesToBeRemoved.add(edges.nextElement());			
			}
			for (EdgeView e:edgesToBeRemoved)
				removeEdgeAndView(e);
			vertexItems.remove(view);
		}
			
		else if (view instanceof EdgeView){
			((EdgeView)view).getOrigin().removeEdge(((EdgeView)view));
			((EdgeView)view).getDestination().removeEdge(((EdgeView)view));
			((EdgeView)view).getOrigin().getVertex().unlink(((EdgeView)view).getDestination().getVertex());
			edgeItems.remove(view);
		}
	}

	/**
	 * Creates a vertex and its view.
	 * 
	 * @param posx the x position of vertex on screen
	 * @param posy the y position of vertex on screen
	 * 
	 * @return the vertex view
	 */
	public VertexView createVertexAndView(int posx, int posy) {
		Vertex v = graph.createVertex();
		return createVertexView(v, posx, posy);
	}

	/**
	 * Creates a vertex view.
	 * 
	 * @param vertex the vertex associated to the view
	 * @param posx the x position of vertex on screen
	 * @param posy the y position of vertex on screen
	 * 
	 * @return the vertex view
	 */
	public VertexView createVertexView(Vertex vertex, int posx, int posy) {
		VertexView view = factory.makeVertexView(vertex);
		view.setPosition(posx, posy);
		vertexItems.add(view);
		undoStack.addUndoInfo(new AddGraphItemEdit(view, this));

		return view;
	}

	/**
	 * Creates an edge and its view.
	 * 
	 * @param v1 the origin vertex of edge
	 * @param v2 the destination vertex of edge
	 * @param oriented the orientation status
	 * 
	 * @return the edge view
	 */
	public EdgeView createEdgeAndView(VertexView v1, VertexView v2, boolean oriented) {
		Vertex vtx1 = v1.getVertex();
		Vertex vtx2 = v2.getVertex();
		
		Enumeration<EdgeView> e = edgeItems.elements();
		while (e.hasMoreElements()) {
			EdgeView view = (EdgeView) e.nextElement();
			if(view.getOrigin().equals(v2) && view.getDestination().equals(v1))// && view.getEdge().isOriented())
					undoStack.addUndoInfo(new ChangeOrientation(view, false));
		}
		if (vtx1.equals(vtx2))
			return null;
		Edge edge = vtx1.linkTo(vtx2, oriented);
		if(edge == null)
			return null;
		EdgeView view = factory.makeEdgeView(edge);
		view.setOrigin(v1);
		view.setDestination(v2);
		v1.addEdge(view);
		v2.addEdge(view);
		edgeItems.add(view);
		undoStack.addUndoInfo(new AddGraphItemEdit(view, this));

		return view;
	}

	/**
	 * Draws the graph.
	 * 
	 * @param g the Graphics context
	 */
	public void drawGraph(Graphics g) {
		g.setColor(Color.black);
		
		// draw edges
		Enumeration<EdgeView> edges = edgeItems.elements();
		while (edges.hasMoreElements()) edges.nextElement().draw(g);

		// draw vertices
		Enumeration<VertexView> vertices = vertexItems.elements();
		while (vertices.hasMoreElements()) vertices.nextElement().draw(g);

		// draw decorations
		Enumeration<ViewableItem> items = decorativeItems.elements();
		while (items.hasMoreElements())	items.nextElement().draw(g);
	}

	/**
	 * Removes the edge and its view.
	 * 
	 * @param edgeView the edge view
	 */
	public void removeEdgeAndView(EdgeView edgeView) {
		undoStack.addUndoInfo(new RemoveGraphItemEdit(edgeView, this));
		removeView(edgeView);
	}

	/**
	 * Removes the vertex and its view.
	 * 
	 * @param vtxView the vertex view
	 */
	public void removeVertexAndView(VertexView vtxView) {
		undoStack.addUndoInfo(new RemoveGraphItemEdit(vtxView, this));
				removeView(vtxView);
	}

	/**
	 * Removes the graph item and its view.
	 * 
	 * @param itemView the item view
	 */
	public void removeGraphItemAndView(GraphItemView itemView) {
		if (itemView == null) return;
		if (itemView instanceof VertexView) removeVertexAndView((VertexView) itemView);
		else if (itemView instanceof EdgeView) removeEdgeAndView((EdgeView) itemView);
	}
	
	/**
	 * Gets the view of the vertex at position (x,y) on screen; return null if no vertex can be found at this position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * 
	 * @return the vertex at position (x,y)
	 */
	public VertexView getVertexAtPosition(int x, int y) {		
		return getVertexAtPosition(x, y, null);
	}

	/**
	 * Gets the view of the vertex at position (x,y) on screen, excluding vertex notThisOne; return null if no vertex can be found at this position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * @param notThisOne the vertex not to consider
	 * 
	 * @return the vertex at position (x,y)
	 */
	public VertexView getVertexAtPosition(int x, int y, VertexView notThisOne) {
		Enumeration<VertexView> vertices = vertexItems.elements();
		while (vertices.hasMoreElements()) {
			VertexView view = vertices.nextElement();
			if (view.containsPoint(x, y) && !view.equals(notThisOne))
				return view; 
		}

		return null;
	}

	/**
	 * Gets the view of the item (vertex or edge) at position (x,y) on screen,; return null if no vertex can be found at this position.
	 * 
	 * @param x the x position
	 * @param y the y position
	 * 
	 * @return the item at position (x,y)
	 */
	public GraphItemView getItemAtPosition(int x, int y) {
		VertexView vertexView = getVertexAtPosition(x, y);
		if (vertexView != null) return vertexView;

		Enumeration<EdgeView> edges = edgeItems.elements();
		while (edges.hasMoreElements()) {
			EdgeView view = edges.nextElement();
			if (view.containsPoint(x, y))
				return view;
		}

		return null;
	}

	/**
	 * Merge vertices.
	 * 
	 * @param view1 the view1
	 * @param view2 the view2
	 */
	public void mergeVertices(VertexView view1, VertexView view2) {
		undoStack.beginGroup("Merge vertices");
		Enumeration<EdgeView> e = view2.getEdges();
		while (e.hasMoreElements()){	
			EdgeView edgeview = (EdgeView) e.nextElement();
			if(edgeview.getOrigin().equals(view2) && !edgeview.getDestination().equals(view1))
				createEdgeAndView(view1, edgeview.getDestination(), edgeview.getEdge().isOriented());
			else if (edgeview.getDestination().equals(view2) && !edgeview.getOrigin().equals(view1))
				createEdgeAndView(edgeview.getOrigin(), view1, edgeview.getEdge().isOriented());
			else {
				removeEdgeAndView(edgeview);
			
			}
			
		}
	/*	Vector<EdgeView> edgesToBeRemoved = new Vector<EdgeView>(10, 10);
		Enumeration<EdgeView> e = edgeItems.elements();
		while (e.hasMoreElements()) {
			EdgeView view = (EdgeView) e.nextElement();
			VertexView origin = view.getOrigin();
			VertexView destination = view.getDestination();
			Edge edge;
			if (origin.equals(view2)) {
				if (destination.equals(view1)) edgesToBeRemoved.add(view);
				else {
					if (destination.getVertex().getEdge(view1.getVertex()) != null)
						edgesToBeRemoved.add(view);
					view.setOrigin(view1);
					view1.addEdge(view);
					undoStack.addUndoInfo(new ChangeEdgeExtremitiesEdit(view, origin, destination));
				}
			} else if (destination.equals(view2)) {
				if (origin.equals(view1)) edgesToBeRemoved.add(view);
				else {
					if (origin.getVertex().getEdge(view1.getVertex()) != null)
						edgesToBeRemoved.add(view);
						
					view.setDestination(view1);
					view1.addEdge(view);
					
					undoStack.addUndoInfo(new ChangeEdgeExtremitiesEdit(view, origin, destination));
				}
			}
		}

		Enumeration<EdgeView> edges = edgesToBeRemoved.elements();
		while (edges.hasMoreElements()) removeEdgeAndView(edges.nextElement());
*/
		//view1.getVertex().merge(view2.getVertex());
				
		removeVertexAndView(view2);		

		undoStack.endGroup();
	}

	/**
	 * Gets the items inside region.
	 * 
	 * @param region the region
	 * 
	 * @return the items inside region
	 */
	public Enumeration<GraphItemView> getItemsInsideRegion(Rectangle region) {
		int x1 = region.x;
		int y1 = region.y;
		int x2 = region.x + region.width;
		int y2 = region.y + region.height;
		Vector<GraphItemView> items = new Vector<GraphItemView>();
		Enumeration<VertexView> e = vertexItems.elements();
		while (e.hasMoreElements()) {
			VertexView view = (VertexView) e.nextElement();
			if (view.isInsideRegion(x1, y1, x2, y2)) {
				items.addElement(view);
			}
		}
		Enumeration<EdgeView> e2 = edgeItems.elements();
		while (e2.hasMoreElements()) {
			EdgeView view = (EdgeView) e2.nextElement();
			if (view.isInsideRegion(x1, y1, x2, y2)) {
				items.addElement(view);
			}
		}
		return items.elements();
	}

	/**
	 * Gets the vertex items.
	 * 
	 * @return the vertex items
	 */
	public Enumeration<VertexView> getVertexItems() {
		return vertexItems.elements();
	}

	/**
	 * Gets the edge items.
	 * 
	 * @return the edge items
	 */
	public Enumeration<EdgeView> getEdgeItems() {
		return edgeItems.elements();
	}

	/**
	 * Gets the graph items (both vertices and edges).
	 * 
	 * @return the graph items
	 */
	public Enumeration<GraphItemView> getGraphItems() {
		Vector<GraphItemView> graphItems = new Vector<GraphItemView>(vertexItems);
		graphItems.addAll(edgeItems);		
		return graphItems.elements();
	}

	/**
	 * Gets the number of graph items (vertices and edges).
	 * 
	 * @return the number of graph items
	 */
	public int getNbGraphItems() {
		return vertexItems.size() + edgeItems.size();
	}
	
	/**
	 * Gets the view of vertex defined by this id.
	 * 
	 * @param id the vertex id
	 * @return the vertex view
	 */
	public VertexView getVertexView(int id) {
		Enumeration<VertexView> vertices = vertexItems.elements();
		while (vertices.hasMoreElements()) {
			VertexView v = vertices.nextElement();
			if (v.getVertex().getId() == id) return v;
		}
		
		return null;
	}

	/**
	 * Gets the view of edge which extremities are the vertices v1 and v2.
	 * 
	 * @param v1 the first vertex
	 * @param v2 the second vertex
	 * @return the edge view, or null if no edge exists between vertices
	 */
	public EdgeView getEdgeView(Vertex v1, Vertex v2) {
		Enumeration<EdgeView> edges = edgeItems.elements();
		while (edges.hasMoreElements()) {
			EdgeView view = edges.nextElement();
			Edge edge = view.getEdge();
			if (edge.isConnectedTo(v1) && edge.isConnectedTo(v2)) return view;
		}
		
		return null;
	}
	
	/**
	 * Gets the number of vertices.
	 * 
	 * @return the number of vertices
	 */
	public int getNbVertices() {
		return vertexItems.size();
	}

	/**
	 * Gets the number of edges.
	 * 
	 * @return the number of edges
	 */
	public int getNbEdges() {
		return edgeItems.size();
	}
	
}
