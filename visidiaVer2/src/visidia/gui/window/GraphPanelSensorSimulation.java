package visidia.gui.window;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Vector;

import visidia.graph.SupportVertex;
import visidia.graph.Vertex;
import visidia.gui.graphview.EdgeView;
import visidia.gui.graphview.MovableSensor;
import visidia.gui.graphview.SensorGraphView;
import visidia.gui.graphview.VertexView;
import visidia.gui.graphview.ViewableItem;
import visidia.simulation.command.SensorCommandListener;
import visidia.simulation.evtack.VisidiaEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphPanelSensorSimulation manages simulation functionalities for sensors.
 */
public class GraphPanelSensorSimulation extends GraphPanelSimulation implements SensorCommandListener {

	private static final long serialVersionUID = 2072632427075596450L;

	/** The sensor graph view. */
	SensorGraphView sensorGraphView = null;
	
	/** The initial configuration. */
	SensorGraphView initialConfiguration = null;

	/** The display sensor number. */
	boolean displaySensorNumber = false;

	/** The sensor number display vector. */
	private Vector<SensorNumberDisplay> sensorNumberDisplayVector = new Vector<SensorNumberDisplay>();

	/**
	 * The Class SensorNumberDisplay.
	 */
	class SensorNumberDisplay extends ViewableItem {

		/** The location. */
		private Point location;
		
		/** The number. */
		private String number;

		/**
		 * Instantiates a new sensor number display.
		 * 
		 * @param location the location
		 * @param number the number
		 */
		public SensorNumberDisplay(Point location, String number) {
			this.location = location;
			this.number = number;
		}

		/* (non-Javadoc)
		 * @see visidia.gui.graphview.ViewableItem#draw(java.awt.Graphics)
		 */
		public void draw(Graphics g) {
			g.setColor(Color.red);
			int stringSize = (int) (g.getFontMetrics().getStringBounds(this.number, g).getWidth());
			// the number is display on top of the sensor
			g.drawString(this.number, this.location.x - stringSize / 2, this.location.y + 5);
		}
	}

	/**
	 * Instantiates a new graph panel sensor simulation.
	 * 
	 * @param graphView the graph view
	 * @param simulationMode the simulation mode
	 * @param parent the parent
	 */
	protected GraphPanelSensorSimulation(SensorGraphView graphView, int simulationMode, VisidiaPanel parent) {
		super(graphView, simulationMode, parent);
		this.sensorGraphView = graphView;
		this.initialConfiguration = graphView.cleanCopy();
		this.vertexMinPos = new Point(graphView.getTopLeftPoint());
		this.vertexMaxPos = new Point(graphView.getBottomRightPoint());
		this.updateDrawingPanelSize();
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.CommandListener#sensorMoved(int, visidia.graph.Vertex, visidia.graph.Vertex, visidia.simulation.evtack.VisidiaEvent)
	 */
	public void sensorMoved(int sensorId, SupportVertex origin, SupportVertex destination, VisidiaEvent event) {
		synchronized (this.concurrentObject) {
			Point a = sensorGraphView.getSupportGraphView().getVertexView(origin.getId()).getPosition();
			Point b = sensorGraphView.getSupportGraphView().getVertexView(destination.getId()).getPosition();
			VertexView sensorView = (VertexView) sensorGraphView.getVertexView(sensorId);
			MovableSensor item = new MovableSensor(sensorView, a, b, this.displacementStep, event);
			this.animatedItems.add(item);
			sensorGraphView.addDecorativeItem(item);
			repaint();
		}
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.SensorCommandListener#edgeAdded(visidia.graph.Vertex, visidia.graph.Vertex)
	 */
	public void edgeAdded(Vertex origin, Vertex destination) {
		VertexView viewOrigin = sensorGraphView.getVertexView(origin.getId());
		VertexView viewDestination = sensorGraphView.getVertexView(destination.getId());
		sensorGraphView.createEdgeAndView(viewOrigin, viewDestination, false);
		repaint();
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.SensorCommandListener#edgeRemoved(visidia.graph.Vertex, visidia.graph.Vertex)
	 */
	public void edgeRemoved(Vertex origin, Vertex destination) {
		EdgeView edgeView = sensorGraphView.getEdgeView(origin, destination);
		if (edgeView != null) sensorGraphView.removeEdgeAndView(edgeView);
		repaint();
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.SensorCommandListener#sensorNumberSet(visidia.graph.SupportVertex)
	 */
	public void sensorNumberSet(SupportVertex supportVertex) {
		Point location = sensorGraphView.getSupportGraphView().getVertexView(supportVertex.getId()).getPosition();
		SensorNumberDisplay snd = new SensorNumberDisplay(location, new Integer(supportVertex.getNbHostedSensors()).toString());
		sensorNumberDisplayVector.add(snd);
		this.graphView.addDecorativeItem(snd);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.SensorCommandListener#sensorNumberDisplayed(boolean)
	 */
	public void sensorNumberDisplayed(boolean display) {
		if (display) {
			displaySensorNumber = true;
		} else {
			synchronized (this.concurrentObject) {
				displaySensorNumber = false;
				int size = this.sensorNumberDisplayVector.size();
				for (int i = 0; i < size; ++i) {
					SensorNumberDisplay sensorNumberDisplay = this.sensorNumberDisplayVector.elementAt(i);
					this.graphView.removeDecorativeItem(sensorNumberDisplay);
				}
				sensorNumberDisplayVector = new Vector<SensorNumberDisplay>();
				repaint();
			}
		}
	}

	/* (non-Javadoc)
	 * @see visidia.gui.window.GraphPanel#paintComponent(java.awt.Graphics)
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		synchronized (this.concurrentObject) {
			if (this.displaySensorNumber) {
				int size = this.sensorNumberDisplayVector.size();
				for (int i = 0; i < size; ++i) {
					SensorNumberDisplay sensorNumberDisplay = this.sensorNumberDisplayVector.elementAt(i);
					sensorNumberDisplay.draw(g);
				}
			}
		}
		drawingPanel.repaint();
	}

}
