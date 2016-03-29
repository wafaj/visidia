package visidia.gui.graphview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Hashtable;

import visidia.graph.Sensor;
import visidia.graph.SensorGraph;
import visidia.graph.SupportVertex;
import visidia.gui.graphview.ViewableItem.ItemState;
import visidia.misc.VisidiaSettings;

// TODO: Auto-generated Javadoc
/**
 * SensorGraphView is the class for decorations on graph view relative to mobile sensors.
 */
public class SensorGraphView extends GraphView {
	
	private static final long serialVersionUID = -4723882014684404887L;

	/** The support graph view. */
	private GraphView supportGraphView;

	/** The top left point. */
	private Point topLeftPoint = new Point(0, 0);
	
	/** The bottom right point. */
	private Point bottomRightPoint = new Point(0, 0);
	
	/**
	 * Instantiates a new graph view for sensors.
	 * 
	 * @param supportGraphView the graph support view; if null use a regular grid as graph support
	 */
	public SensorGraphView(GraphView supportGraphView) {
		super();

		// create a regular grid if supportGraphView is null
		if (supportGraphView == null) this.supportGraphView = buildRegularGrid();
		else this.supportGraphView = setSupportGraphView(supportGraphView);
	}

	/**
	 * Gets the top left point.
	 * 
	 * @return the top left point
	 */
	public Point getTopLeftPoint() {
		return topLeftPoint;
	}	

	/**
	 * Gets the bottom right point.
	 * 
	 * @return the bottom right point
	 */
	public Point getBottomRightPoint() {
		return bottomRightPoint;
	}
	
	/**
	 * Builds the regular grid.
	 * 
	 * @return the graph view
	 */
	private GraphView buildRegularGrid() {
		VisidiaSettings settings = VisidiaSettings.getInstance();
		final int margin = 50;

		int nbVerticesX = settings.getInt(VisidiaSettings.Constants.SENSOR_NB_VERTICES_X);
		int nbVerticesY = settings.getInt(VisidiaSettings.Constants.SENSOR_NB_VERTICES_Y);
		int vertexGapX = settings.getInt(VisidiaSettings.Constants.SENSOR_VERTEX_GAP_X);
		int vertexGapY = settings.getInt(VisidiaSettings.Constants.SENSOR_VERTEX_GAP_Y);
		
		GraphView gridView = new GraphView();
		topLeftPoint.x = margin;
		topLeftPoint.y = margin;
		bottomRightPoint.x = margin + (nbVerticesX-1) * vertexGapX;
		bottomRightPoint.y = margin + (nbVerticesY-1) * vertexGapY;

		VertexView[][] vertices = new VertexView[nbVerticesX][nbVerticesY];

		// create vertices
		for (int i = 0; i < nbVerticesX; ++i) {
			int posx = margin + i*vertexGapX;
			for (int j = 0; j < nbVerticesY; ++j) {
				int posy = margin + j*vertexGapY;
				SupportVertex supportVertex = new SupportVertex(j + i*nbVerticesY);
				gridView.graph.addVertex(supportVertex);
				vertices[i][j] = gridView.createVertexView(supportVertex, posx, posy);
			}
		}

		// create edges
		for (int i = 0; i < nbVerticesX; ++i) {
			for (int j = 0; j < nbVerticesY; ++j) {
				if (i != 0) gridView.createEdgeAndView(vertices[i-1][j], vertices[i][j], false);
				if (j != 0) gridView.createEdgeAndView(vertices[i][j-1], vertices[i][j], false);
			}
		}

		return gridView;
	}

	/**
	 * Sets the support graph view.
	 * 
	 * @param supportGraphView the support graph view
	 * 
	 * @return the graph view
	 */
	private GraphView setSupportGraphView(GraphView supportGraphView) {
		GraphView gridView = new GraphView();
		Hashtable<VertexView, VertexView> table = new Hashtable<VertexView, VertexView>(); 
		topLeftPoint.x = Integer.MAX_VALUE;

		// create vertices
		Enumeration<VertexView> vertices = supportGraphView.getVertexItems();
		while (vertices.hasMoreElements()) {
			VertexView view = vertices.nextElement();
			Point pos = view.getPosition();
			SupportVertex supportVertex = new SupportVertex(view.getVertex().getId());
			gridView.graph.addVertex(supportVertex);
			VertexView newView = gridView.createVertexView(supportVertex, pos.x, pos.y);
			table.put(view, newView);
			
			if (topLeftPoint.x == Integer.MAX_VALUE) {
				topLeftPoint.x = pos.x;
				topLeftPoint.y = pos.y;
				bottomRightPoint.x = pos.x;
				bottomRightPoint.y = pos.y;
			} else {
				if (pos.x < topLeftPoint.x) topLeftPoint.x = pos.x;
				else if (pos.x > bottomRightPoint.x) bottomRightPoint.x = pos.x;
				
				if (pos.y < topLeftPoint.y) topLeftPoint.y = pos.y;
				else if (pos.y > bottomRightPoint.y) bottomRightPoint.y = pos.y;
			}
		}

		// create edges
		Enumeration<EdgeView> edges = supportGraphView.getEdgeItems();
		while (edges.hasMoreElements()) {
			EdgeView view = edges.nextElement();
			VertexView newOrigin = table.get(view.origin);
			VertexView newDestination = table.get(view.destination);
			gridView.createEdgeAndView(newOrigin, newDestination, false);
		}
		
		return gridView;
	}

	/**
	 * Gets the support graph view.
	 * 
	 * @return the support graph view
	 */
	public GraphView getSupportGraphView() {
		return supportGraphView;
	}

	/**
	 * Draws the graph.
	 * 
	 * @param g the Graphics context
	 */
	public void drawGraph(Graphics g) {
		// draw edges of support graph
		Enumeration<EdgeView> edges = supportGraphView.edgeItems.elements();
		g.setColor(Color.lightGray);
		boolean directedgraph = VisidiaSettings.getInstance().getBoolean(VisidiaSettings.Constants.DIRECTED_GRAPH);
		VisidiaSettings.getInstance().set(VisidiaSettings.Constants.DIRECTED_GRAPH, false);
		while (edges.hasMoreElements()) edges.nextElement().draw(g);

		VisidiaSettings.getInstance().set(VisidiaSettings.Constants.DIRECTED_GRAPH, directedgraph);
		// draw links between sensors
		edges = edgeItems.elements();
		g.setColor(Color.black);
		while (edges.hasMoreElements()) edges.nextElement().draw(g);
		
		// draw sensors
		Enumeration<VertexView> vertices = vertexItems.elements();
		while (vertices.hasMoreElements()) vertices.nextElement().draw(g);

		// draw decorations
		Enumeration<ViewableItem> items = decorativeItems.elements();
		while (items.hasMoreElements())	items.nextElement().draw(g);
	}

	/**
	 * Creates a "clean" copy of SensorGraphView (vertices and support graph), removing all disabled items from the graph.
	 * Does not copy decorative items.
	 * 
	 * @return the new sensor graph view
	 */
	public SensorGraphView cleanCopy() {
		SensorGraphView sgv = new SensorGraphView(this.supportGraphView);
		SensorGraph sensorGraph = new SensorGraph(this.supportGraphView.getGraph());
		sgv.graph = sensorGraph;
		sgv.topLeftPoint = new Point(this.topLeftPoint);
		sgv.bottomRightPoint = new Point(this.bottomRightPoint);
		
		Enumeration<VertexView> vItems = vertexItems.elements();
		while (vItems.hasMoreElements()) {
			VertexView view = vItems.nextElement();
			if (view.getState() != ItemState.DELETED) {
				Point pos = view.getPosition();				
				VertexView support = sgv.supportGraphView.getVertexAtPosition(pos.x, pos.y);
				if (support != null) {
					Sensor sensor = sensorGraph.addSensorOnVertex(support.getVertex());
					if (sensor != null)
						sgv.createVertexView(sensor, pos.x, pos.y);
				}
			}
		}

		return sgv;
	}
	
}
