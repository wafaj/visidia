package visidia.graph;

import java.util.Enumeration;
import java.util.Hashtable;

import visidia.io.SensorMoverIO;
import visidia.misc.ClassIdentifier;
import visidia.misc.VisidiaSettings;
import visidia.simulation.process.SensorMover;

// TODO: Auto-generated Javadoc
/**
 * A sensor is a kind of graph whose vertices move along another graph edges.
 */
public class SensorGraph extends Graph {

	private static final long serialVersionUID = 2055892991379228841L;

	/** The global sensor mover. */
	private SensorMover globalSensorMover = null;

	/** The support vertices. */
	private Hashtable<Integer, SupportVertex> supportVertices = new Hashtable<Integer, SupportVertex>();

	/** The support graph. */
	private Graph supportGraph = null;
	
	/**
	 * Instantiates a new sensor graph.
	 * 
	 * @param supportGraph the support graph
	 */
	public SensorGraph(Graph supportGraph) {
		this(supportGraph, null);
	}

	/* (non-Javadoc)
	 * @see visidia.graph.Graph#clone()
	 */
	public Object clone() {
		SensorGraph copy = (SensorGraph) super.clone();
		copy.globalSensorMover = globalSensorMover;
		copy.supportVertices = (Hashtable<Integer, SupportVertex>) supportVertices.clone();
		copy.supportGraph = (Graph) supportGraph.clone();
		
		return copy;
	}
	
	/**
	 * Instantiates a new sensor graph.
	 * 
	 * @param supportGraph the support graph
	 * @param sensorMover the sensor mover
	 */
	public SensorGraph(Graph supportGraph, SensorMover sensorMover) {
		this.supportGraph = supportGraph;
		this.globalSensorMover = sensorMover;

		if (globalSensorMover == null) {
			ClassIdentifier classId = (ClassIdentifier) VisidiaSettings.getInstance().getObject(VisidiaSettings.Constants.VISIDIA_GLOBAL_SENSOR_MOVER);
			classId.setInstanceType(SensorMover.class);
			SensorMoverIO moverIO = new SensorMoverIO(classId);
			SensorMover mover = moverIO.load();
			setGlobalSensorMover(mover);
		}
		
		Enumeration<Vertex> vertices = supportGraph.getVertices();
		while (vertices.hasMoreElements()) {
			SupportVertex vertex = (SupportVertex) vertices.nextElement();
			SupportVertex supportVertex = new SupportVertex(vertex.getId());
			supportVertices.put(vertex.getId(), supportVertex);
		}

		vertices = supportGraph.getVertices();
		while (vertices.hasMoreElements()) {
			Vertex vertex = vertices.nextElement();
			SupportVertex supportVertex = supportVertices.get(vertex.getId());

			Enumeration<Vertex> neighbors = vertex.getNeighbors();
			while (neighbors.hasMoreElements()) {
				Vertex neighbor = neighbors.nextElement();
				supportVertex.neighbors.add(supportVertices.get(neighbor.getId()));
			}
		}
	}

	/**
	 * Sets the global sensor mover.
	 * 
	 * @param mover the new global sensor mover
	 */
	public void setGlobalSensorMover(SensorMover mover) {
		this.globalSensorMover = mover;
	}

	/**
	 * Gets the support graph.
	 * 
	 * @return the support graph
	 */
	public Graph getSupportGraph() {
		return supportGraph;
	}
	
	/**
	 * Gets the sensor communication distance.
	 * 
	 * @return the sensor communication distance
	 */
	public int getSensorCommunicationDistance() {
		return VisidiaSettings.getInstance().getInt(VisidiaSettings.Constants.SENSOR_COMMUNICATION_DISTANCE);
	}

	/**
	 * Gets the global sensor mover.
	 * 
	 * @return the global sensor mover
	 */
	public SensorMover getGlobalSensorMover() {
		return globalSensorMover;
	}

	/**
	 * Adds the sensor on vertex.
	 * 
	 * @param vertex the vertex
	 * 
	 * @return the sensor
	 */
	public Sensor addSensorOnVertex(Vertex vertex) {
		SupportVertex supportVertex = supportVertices.get(vertex.getId());
		if (supportVertex != null) {
			Sensor sensor = new Sensor(numberOfCreatedVertices++);
			sensor.setSupportVertex(supportVertex);
			vertices.add(sensor);

			supportVertex.addSensor(sensor);
			return sensor;
		}

		return null;
	}

}
