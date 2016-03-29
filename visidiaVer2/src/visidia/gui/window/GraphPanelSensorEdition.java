package visidia.gui.window;

import java.awt.Point;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Random;

import visidia.gui.graphview.GraphItemView;
import visidia.gui.graphview.GraphView;
import visidia.gui.graphview.SensorGraphView;
import visidia.gui.graphview.VertexView;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphPanelSensorEdition manages edition functionalities for sensors.
 */
public class GraphPanelSensorEdition extends GraphPanelEdition {

	private static final long serialVersionUID = -1427440674952475873L;

	/** The sensor edition view. */
	SensorGraphView sensorEditionView;

	/**
	 * Instantiates a new graph panel sensor edition.
	 * 
	 * @param sensorEditionView the sensor edition view
	 * @param displayGraph the display graph
	 */
	public GraphPanelSensorEdition(SensorGraphView sensorEditionView, boolean displayGraph) {
		super(sensorEditionView, displayGraph);
		this.sensorEditionView = sensorEditionView;
		this.vertexMinPos = new Point(sensorEditionView.getTopLeftPoint());
		this.vertexMaxPos = new Point(sensorEditionView.getBottomRightPoint());
		this.updateDrawingPanelSize();
	}

	/* (non-Javadoc)
	 * @see visidia.gui.window.GraphPanelEdition#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		int x = (int) (e.getX() / drawingPanel.getZoomFactor());
		int y = (int) (e.getY() / drawingPanel.getZoomFactor());

		switch (e.getModifiers()) {
		case InputEvent.BUTTON1_MASK:
			GraphItemView clickedObject = sensorEditionView.getSupportGraphView().getVertexAtPosition(x, y);
			if (clickedObject != null && clickedObject instanceof VertexView) {
				// set sensor position exactly on a node of support graph
				Point pos = ((VertexView) clickedObject).getPosition();
				addVertex(pos.x, pos.y);
			}
			break;
		}
	}

	/* (non-Javadoc)
	 * @see visidia.gui.window.GraphPanelEdition#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {		
	}

	/* (non-Javadoc)
	 * @see visidia.gui.window.GraphPanelEdition#mouseDragged(java.awt.event.MouseEvent)
	 */
	public void mouseDragged(MouseEvent e) {
	}

	/**
	 * Randomly place sensors.
	 * 
	 * @param nbSensors the number of sensors
	 */
	public void randomlyPlaceSensors(int nbSensors) {
		GraphView supportView = sensorEditionView.getSupportGraphView();
		int nbSupportVertices = supportView.getNbVertices();
		
		int nbSensorsAlreadyPlaced = sensorEditionView.getNbVertices();		
		nbSupportVertices -= nbSensorsAlreadyPlaced;		
		if (nbSensors > nbSupportVertices) nbSensors = nbSupportVertices;
		
		// get vertex ids
		int[] vertexIds = new int[nbSupportVertices];
		Enumeration<VertexView> vertices = supportView.getVertexItems();
		int cpt = 0;
		while (vertices.hasMoreElements()) {
			VertexView v = vertices.nextElement();
			Point pos = v.getPosition();
			if (sensorEditionView.getVertexAtPosition(pos.x, pos.y) == null)
				vertexIds[cpt++] = v.getVertex().getId();
		}
		
		// Ramndom placement
		boolean[] mark = new boolean[nbSupportVertices];
		for (int i = 0; i < nbSupportVertices; ++i) mark[i] = false;
		
		Random r = new Random();
		while (nbSensors > 0) {
			int n = r.nextInt(nbSupportVertices);
			if (mark[n] == true) continue;
			mark[n] = true;
			nbSensors --;
			VertexView view = supportView.getVertexView(vertexIds[n]);
			Point pos = view.getPosition();
			addVertex(pos.x, pos.y);
		}
	}

}
