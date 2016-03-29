package visidia.simulation.command;

import visidia.graph.SupportVertex;
import visidia.graph.Vertex;
import visidia.simulation.evtack.VisidiaEvent;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving command events relative to sensors.
 * The class that is interested in processing a sensorCommand
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addCommandListener<code> method. When
 * the command event occurs, that object's appropriate
 * method is invoked.
 */
public interface SensorCommandListener extends CommandListener {

	/**
	 * Sensor moved.
	 * 
	 * @param sensorId the sensor id
	 * @param origin the origin
	 * @param destination the destination
	 * @param event the event
	 */
	void sensorMoved(int sensorId, SupportVertex origin, SupportVertex destination, VisidiaEvent event);

	/**
	 * Edge added.
	 * 
	 * @param origin the origin
	 * @param destination the destination
	 */
	void edgeAdded(Vertex origin, Vertex destination);

	/**
	 * Edge removed.
	 * 
	 * @param origin the origin
	 * @param destination the destination
	 */
	void edgeRemoved(Vertex origin, Vertex destination);

	/**
	 * Sensor number displayed.
	 * 
	 * @param display the display
	 */
	void sensorNumberDisplayed(boolean display);

	/**
	 * Sensor number set.
	 * 
	 * @param supportVertex the support vertex
	 */
	void sensorNumberSet(SupportVertex supportVertex);
	
}
