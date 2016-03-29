package visidia.gui.window;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import java.util.Hashtable;

import visidia.gui.graphview.EdgeView;
import visidia.gui.graphview.GraphItemView;
import visidia.gui.graphview.GraphView;
import visidia.gui.graphview.VertexView;
import visidia.gui.undo.MoveVertexEdit;
import visidia.misc.VisidiaSettings;

// TODO: Auto-generated Javadoc
/**
 * GraphPanelEdition extends GraphPanel, adding functionalities for graph edition.
 */
public class GraphPanelEdition extends GraphPanel implements MouseListener, MouseMotionListener, KeyListener {

	private static final long serialVersionUID = -1124143402957904492L;

	/** Temporary storage of a screen position (x coordinate). */
	private int xOld;

	/** Temporary storage of a screen position (y coordinate). */
	private int yOld;

	/** When the mouse is over a vertex, distance (along x coordinate) of cursor to the vertex center. */
	private int dx;

	/** When the mouse is over a vertex, distance (along y coordinate) of cursor to the vertex center. */
	private int dy;

	/** Indicates if a newly created vertex is being dragged and dropped. */
	private boolean dragAndDropNewVertex = false;

	/** Indicates if the whole graph is being dragged and dropped. */
	private boolean dragAndDropGraph = false;

	/** Indicates if an already existing vertex is being dragged and dropped. */
	private boolean	dragAndDropExistingVertex = false;

	/** Indicates if the current selection is being dragged and dropped. */
	private boolean	dragAndDropSelection = false;

	/** The initial position of a dragged and dropped vertex. */
	private Point dragAndDropVertexPos = new Point(0, 0);

	/** The initial position (x coordinate) of dragged and dropped selection/graph. */
	private int xOrigin;

	/** The initial position (y coordinate) of dragged and dropped selection/graph. */
	private int yOrigin;

	/** The clicked object. */
	private GraphItemView clickedObject = null;

	/** The initial position (x coordinate) of rectangular selection. */
	private int selectedAreaX0;

	/** The initial position (y coordinate) of rectangular selection. */
	private int selectedAreaY0;

	/**
	 * Instantiates a new graph panel in edition mode.
	 * 
	 * @param graphView the graph view
	 */
	public GraphPanelEdition(GraphView graphView, boolean displayGraph) {
		super(graphView, GraphPanel.DrawingPanelMode.EDITION, displayGraph);
		this.setDisplayGraph(displayGraph);
	}

	/* (non-Javadoc)
	 * @see visidia.gui.window.GraphPanel#setDisplayGraph(boolean)
	 */
	public void setDisplayGraph(boolean displayGraph) {
		drawingPanel.removeMouseListener(this);
		drawingPanel.removeMouseMotionListener(this);
		drawingPanel.removeKeyListener(this);

		if (displayGraph) {
			drawingPanel.addMouseListener(this);
			drawingPanel.addMouseMotionListener(this);
			drawingPanel.addKeyListener(this);
		}
		
		super.setDisplayGraph(displayGraph);
	}

	/**
	 * Undo last operation.
	 */
	public void undo() {
		if (!displayGraph) return;
		graphView.undo();
		drawingPanel.repaint();
	}

	/**
	 * Redo last undo'ed operation.
	 */
	public void redo() {
		if (!displayGraph) return;
		graphView.redo();
		drawingPanel.repaint();
	}

	/**
	 * Removes the selection.
	 */
	public void removeSelection() {
		if (selection == null || selection.isEmpty()) return;
		graphView.getUndoStack().beginGroup("Remove selection");
		Enumeration<GraphItemView> items = selection.elements();
		while (items.hasMoreElements()) {
			GraphItemView item = items.nextElement();
			graphView.removeGraphItemAndView(item);
		}
		graphView.getUndoStack().endGroup();
		selection.clear();
		repaint();
	}

	/**
	 * Duplicate the selection.
	 */
	public void duplicateSelection() {
		if (selection == null || selection.isEmpty()) return;
		Hashtable<Integer, VertexView> table = new Hashtable<Integer, VertexView>();
		Selection copied = new Selection();
		graphView.getUndoStack().beginGroup("Duplicate selection");

		// copy vertices
		Enumeration<GraphItemView> items = selection.elements();
		while (items.hasMoreElements()) {
			GraphItemView item = items.nextElement();
			if (item instanceof VertexView) {
				Point pos = ((VertexView) item).getPosition();
				int offset = (int) (3 * ((VertexView) item).getBorderSize());
				VertexView copy = graphView.createVertexAndView(pos.x + offset, pos.y + offset);
				table.put(((VertexView) item).getVertex().getId(), copy);
				copied.addElement(copy);
			}
		}

		// copy edges
		items = selection.elements();
		while (items.hasMoreElements()) {
			GraphItemView item = items.nextElement();
			if (item instanceof EdgeView) {
				EdgeView view = (EdgeView) item;
				VertexView origin = table.get(view.getOrigin().getVertex().getId());
				VertexView destination = table.get(view.getDestination().getVertex().getId());
				EdgeView copy = graphView.createEdgeAndView(origin, destination, view.getEdge().isOriented());
				copied.addElement(copy);
			}
		}

		// change selection
		selection.clear();
		items = copied.elements();
		while (items.hasMoreElements()) {
			GraphItemView item = items.nextElement();
			selection.addElement(item);
		}

		graphView.getUndoStack().endGroup();
		repaint();
	}

	/**
	 * Complete the graph (connect each vertex to all other).
	 */
	public void completeGraph() {
		int nbVertices = graphView.getNbVertices();
		VertexView[] vertices = new VertexView[nbVertices];
		Enumeration<VertexView> items = graphView.getVertexItems();
		int n = 0;
		while (items.hasMoreElements()) vertices[n++] = items.nextElement();

		graphView.getUndoStack().beginGroup("Complete graph");
		for (int i = 0; i < nbVertices-1; ++i) {
			for (int j = i+1; j < nbVertices; ++j) {
				if (graphView.getEdgeView(vertices[i].getVertex(), vertices[j].getVertex()) == null)
					graphView.createEdgeAndView(vertices[i], vertices[j], false);
			}
		}

		graphView.getUndoStack().endGroup();
		repaint();
	}

	/**
	 * Select all items.
	 */
	public void selectAll() {
		selection.clear();

		Enumeration<GraphItemView> items = graphView.getGraphItems();
		while (items.hasMoreElements()) {
			GraphItemView item = items.nextElement();
			selection.addElement(item);
		}
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {	
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {	
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		int x = (int) (e.getX() / drawingPanel.getZoomFactor());
		int y = (int) (e.getY() / drawingPanel.getZoomFactor());

		switch (e.getModifiers()) {
		case InputEvent.BUTTON1_MASK:
			addVertex(x, y);
			break;

		case InputEvent.BUTTON2_MASK:
		case (InputEvent.BUTTON1_MASK | InputEvent.ALT_MASK):
			dragAndDropObject(x, y);
		break;

		case InputEvent.BUTTON3_MASK:
			selectObject(x, y);
			break;

		default:		
		}	
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {	
		int x = (int) (e.getX() / drawingPanel.getZoomFactor());
		int y = (int) (e.getY() / drawingPanel.getZoomFactor());

		if (dragAndDropNewVertex) {
			// test if clickedObject (moving vertex) is on top of another vertex
			VertexView view = graphView.getVertexAtPosition(x, y, (VertexView) clickedObject);
			if (view != null) graphView.mergeVertices(view, (VertexView) clickedObject);
			else setVertexPosition(x, y);
			graphView.getUndoStack().endGroup();
			dragAndDropNewVertex = false;
		} else if (dragAndDropExistingVertex) {
			// test if clickedObject (moving vertex) is on top of another vertex
			VertexView view = graphView.getVertexAtPosition(x, y, (VertexView) clickedObject);

			if (view == null) {
				graphView.getUndoStack().addUndoInfo(new MoveVertexEdit((VertexView) clickedObject, x-dragAndDropVertexPos.x, y-dragAndDropVertexPos.y));
				setVertexPosition(x, y);
			} else {
				graphView.getUndoStack().beginGroup("Move vertex and merge");
				graphView.getUndoStack().addUndoInfo(new MoveVertexEdit((VertexView) clickedObject, x-dragAndDropVertexPos.x, y-dragAndDropVertexPos.y));
				graphView.mergeVertices(view, (VertexView) clickedObject);
				graphView.getUndoStack().endGroup();
			}

			dragAndDropExistingVertex = false;
		} else if (dragAndDropSelection) {
			graphView.getUndoStack().beginGroup("Move selection");
			Enumeration<GraphItemView> items = selection.elements();
			while (items.hasMoreElements()) {
				GraphItemView item = items.nextElement();
				if (item instanceof VertexView) {
					graphView.getUndoStack().addUndoInfo(new MoveVertexEdit((VertexView) item, x-xOrigin, y-yOrigin));
					Point pos = ((VertexView) item).getPosition();
					setVertexPosition(pos.x, pos.y);
				}
			}			
			graphView.getUndoStack().endGroup();
			dragAndDropSelection = false;
		} else if (dragAndDropGraph) {
			graphView.getUndoStack().beginGroup("Move graph");
			Enumeration<VertexView> vertices = graphView.getVertexItems();
			while (vertices.hasMoreElements()) {
				graphView.getUndoStack().addUndoInfo(new MoveVertexEdit(vertices.nextElement(), x-xOrigin, y-yOrigin));
			}
			graphView.getUndoStack().endGroup();
			dragAndDropGraph = false;
			int deltaX = x-xOrigin;
			int deltaY = y-yOrigin;
			vertexMinPos.x += deltaX;
			vertexMinPos.y += deltaY;
			vertexMaxPos.x += deltaX;
			vertexMaxPos.y += deltaY;
			updateDrawingPanelSize();
		} else if (selectedArea != null) {
			Enumeration<GraphItemView> items = graphView.getItemsInsideRegion(selectedArea);

			while (items.hasMoreElements()) {
				GraphItemView item = items.nextElement();
				selection.addElement(item);
			}
			selectedArea = null;
		}

		clickedObject = null;
		drawingPanel.repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
		int x = (int) (e.getX() / drawingPanel.getZoomFactor());
		int y = (int) (e.getY() / drawingPanel.getZoomFactor());

		switch (e.getModifiers()) {
		case InputEvent.BUTTON1_MASK:
			addEdge(x, y);
			break;

		case InputEvent.BUTTON2_MASK:
		case (InputEvent.BUTTON1_MASK | InputEvent.ALT_MASK):
			moveObject(x, y);
		break;

		case InputEvent.BUTTON3_MASK:
		case (InputEvent.BUTTON3_MASK | InputEvent.SHIFT_MASK):
			selectRectangularArea(x, y);
		break;

		default:
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent e) {	
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	public void keyPressed(KeyEvent e) {
		
		switch (e.getKeyCode()) {
		// Delete
		case KeyEvent.VK_DELETE:
		case KeyEvent.VK_BACK_SPACE:
			deleteSelectedItems();
			break;

		default:
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	public void keyReleased(KeyEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	public void keyTyped(KeyEvent e) {
	}

	/**
	 * Adds a vertex on screen.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	protected void addVertex(int x, int y) {
		this.xOld = x;
		this.yOld = y;

		clickedObject = graphView.getVertexAtPosition(x, y);
		if (clickedObject == null) {
			clickedObject = graphView.createVertexAndView(x, y);
			drawingPanel.repaint();
		}
		setVertexPosition(x, y);
	}

	/**
	 * Drag and drop object.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	private void dragAndDropObject(int x, int y) {
		this.xOld = x;
		this.yOld = y;

		clickedObject = graphView.getVertexAtPosition(x, y);

		if (clickedObject == null) {
			dragAndDropGraph = true;
			xOrigin = x;
			yOrigin = y;
		} else {
			if (selection.contains(clickedObject)) {
				dragAndDropSelection = true;
				xOrigin = x;
				yOrigin = y;
			} else if (clickedObject instanceof VertexView) {
				dragAndDropExistingVertex = true;
				/* When a vertex is dragged, its position to cursor must be accurate.
				 * The offset between cursor and vertex position is thus stored.
				 */
				dragAndDropVertexPos = ((VertexView) clickedObject).getPosition();

				dx = dragAndDropVertexPos.x - x;
				dy = dragAndDropVertexPos.y - y;
			}
		}
	}

	/**
	 * Select an object on screen.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	private void selectObject(int x, int y) {
		xOld = x;
		yOld = y;

		clickedObject = graphView.getItemAtPosition(x, y);

		if (clickedObject == null) {
			if (!selection.isEmpty()) {
				selection.clear();
				drawingPanel.repaint(0);
			}
			selectedArea = new Rectangle(x, y, 1, 1);
			selectedAreaX0 = x;
			selectedAreaY0 = y;
		} else {
			if (selection.contains(clickedObject)) selection.removeElement(clickedObject);
			else selection.addElement(clickedObject);

			drawingPanel.repaint();
		}
	}

	/**
	 * Adds the edge.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	private void addEdge(int x, int y) {
		if (clickedObject != null) {			
			if (dragAndDropNewVertex) {
				moveVertex(x, y);
			} else {
				if (clickedObject.containsPoint(x, y)) {
					return;
				} else {
					graphView.getUndoStack().beginGroup("Create Vertex and Edge");
					VertexView vertexUnderMouseOld = (VertexView) clickedObject;
					clickedObject = graphView.createVertexAndView(x, y);
					VertexView draggedAndDroppedVertex = (VertexView) clickedObject;
					EdgeView floatingEdge = graphView.createEdgeAndView(vertexUnderMouseOld, draggedAndDroppedVertex, VisidiaSettings.getInstance().getBoolean(VisidiaSettings.Constants.DIRECTED_GRAPH));

					dragAndDropNewVertex = true;
					dx = dy = 0;
				}
			}
			drawingPanel.repaint();
		}
	}

	/**
	 * Move object.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	private void moveObject(int x, int y) {
		if (clickedObject != null) {
			// move selection
			if (!selection.isEmpty() && selection.contains(clickedObject)) {
				Enumeration<GraphItemView> items = selection.elements();
				while (items.hasMoreElements()) {
					GraphItemView item = items.nextElement();
					if (item instanceof VertexView) {
						((VertexView) item).move(x-xOld, y-yOld);
					}
				}
				xOld = x;
				yOld = y;
			} else if (dragAndDropNewVertex || dragAndDropExistingVertex) {
				moveVertex(x, y);
			}
		} else {
			// move the whole graph
			Enumeration<VertexView> vertices = graphView.getVertexItems();
			while (vertices.hasMoreElements()) {
				vertices.nextElement().move(x-xOld, y-yOld);
			}
			xOld = x;
			yOld = y;
		}
		drawingPanel.repaint();
	}

	/**
	 * Select rectangular area.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	private void selectRectangularArea(int x, int y) {	
		if (selectedArea != null) {
			if (x > selectedAreaX0) {
				selectedArea.x = selectedAreaX0;
				selectedArea.width = x - selectedArea.x;
			} else {
				selectedArea.x = x;
				selectedArea.width = selectedAreaX0 - x;
			}

			if (y > selectedAreaY0) {
				selectedArea.y = selectedAreaY0;
				selectedArea.height = y - selectedArea.y;
			} else {
				selectedArea.y = y;
				selectedArea.height = selectedAreaY0 - y;
			}

			drawingPanel.repaint();
		}
	}

	/**
	 * Move vertex.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	private void moveVertex(int x, int y) {
		// test if clickedObject (moving vertex) is on top of another vertex
		VertexView view = graphView.getVertexAtPosition(x, y, (VertexView) clickedObject);

		if (view == null) {
			// no: we only move the vertex
			((VertexView) clickedObject).setPosition(x+dx, y+dy);
			xOld = x;
			yOld = y;
		} else {
			// yes: the moving vertex is attracted
			xOld = view.getPosition().x;
			yOld = view.getPosition().y;
			((VertexView) clickedObject).setPosition(xOld, yOld);
		}
	}

	/**
	 * Delete selected items.
	 */
	private void deleteSelectedItems() {
		graphView.getUndoStack().beginGroup("Remove selection");
		Enumeration<GraphItemView> items = selection.elements();
		while (items.hasMoreElements()) {
			GraphItemView item = items.nextElement();
			if (item instanceof VertexView) {
				graphView.removeVertexAndView((VertexView) item);
			} else if (item instanceof EdgeView) {
				graphView.removeEdgeAndView((EdgeView) item);
			}
		}

		graphView.getUndoStack().endGroup();
		setGraphBounds();
		drawingPanel.repaint();
	}

}
