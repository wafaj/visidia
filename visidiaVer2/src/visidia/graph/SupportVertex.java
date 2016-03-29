package visidia.graph;

import java.util.Enumeration;
import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * A SupportVertex in an element of a graph on which sensors move.
 */
public class SupportVertex extends Vertex {

	private static final long serialVersionUID = 183244550797376453L;

	/** The hosted sensors. */
	private Vector<Sensor> hostedSensors = new Vector<Sensor>();

	/**
	 * Instantiates a new support vertex.
	 * 
	 * @param id the id
	 */
	public SupportVertex(int id) {
		super(id);
	}

	/* (non-Javadoc)
	 * @see visidia.graph.Vertex#clone()
	 */
	public Object clone() {
		SupportVertex copy = (SupportVertex) super.clone();
		copy.hostedSensors = (Vector<Sensor>) hostedSensors.clone();
		return copy;
	}
	
	/**
	 * Adds the sensor.
	 * 
	 * @param sensor the sensor
	 */
	public void addSensor(Sensor sensor) {
		hostedSensors.add(sensor);
	}

	/**
	 * Removes the sensor.
	 * 
	 * @param sensor the sensor
	 */
	public void removeSensor(Sensor sensor) {
		hostedSensors.remove(sensor);
	}

	/**
	 * Checks if is empty.
	 * 
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return hostedSensors.isEmpty();
	}

	/**
	 * Gets the hosted sensors.
	 * 
	 * @return the hosted sensors
	 */
	public Enumeration<Sensor> getHostedSensors() {
		return hostedSensors.elements();
	}

	/**
	 * Checks if is the last sensor.
	 * 
	 * @param sensor the sensor
	 * 
	 * @return true, if is the last sensor
	 */
	public boolean isTheLastSensor(Sensor sensor) {
		return (sensor == this.hostedSensors.lastElement());
	}

	/**
	 * Gets the number of hosted sensors.
	 * 
	 * @return the number of hosted sensors
	 */
	public int getNbHostedSensors() {
		return hostedSensors.size();
	}

}
