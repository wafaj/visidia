package visidia.simulation.process;

import java.io.Serializable;

import visidia.graph.Sensor;
import visidia.graph.SupportVertex;

// TODO: Auto-generated Javadoc
/**
 * Abstract class providing different moving types for the sensors.
 * You should subclass this class to create your own style of move.
 */
public abstract class SensorMover implements Serializable {

	private static final long serialVersionUID = 6268259620756169472L;

	/**
	 * Instantiates a new sensor mover.
	 */
	public SensorMover() {
	}

	/**
	 * Moves the sensor to the next vertex using the sensor's local mover.
	 * 
	 * @param sensor the sensor
	 * 
	 * @throws InterruptedException the interrupted exception
	 * @throws MoveException the move exception
	 */
	public void move(Sensor sensor) throws InterruptedException, MoveException {
		this.move(sensor, this.findWay(sensor));
	}

	/**
	 * Moves the sensor to a specified vertex neighbor.
	 * 
	 * @param sensor the sensor
	 * @param vertexTo the vertex to
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public final void move(Sensor sensor, SupportVertex vertexTo) throws InterruptedException {
		SupportVertex vertexFrom = sensor.getSupportVertex();
		vertexFrom.removeSensor(sensor);
		vertexTo.addSensor(sensor);
		sensor.setSupportVertex(vertexTo);
	}

	/**
	 * Returns the ID of the vertex neighbor to which the sensor will go. This method needs to be
	 * specialized in the sub-classes.
	 * 
	 * @param sensor the sensor
	 * 
	 * @return the support vertex
	 * 
	 * @throws MoveException the move exception
	 */
	public abstract SupportVertex findWay(Sensor sensor) throws MoveException;

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return "No description for this sensor mover.";
	}
}