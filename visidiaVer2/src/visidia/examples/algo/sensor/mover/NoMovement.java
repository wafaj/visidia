package visidia.examples.algo.sensor.mover;

import visidia.graph.Sensor;
import visidia.graph.SupportVertex;
import visidia.simulation.process.MoveException;
import visidia.simulation.process.SensorMover;

// TODO: Auto-generated Javadoc
/**
 * Provides a mover... for sensors which do not move!
 */
public class NoMovement extends SensorMover {

	private static final long serialVersionUID = 6632159282422639225L;

	/**
	 * Instantiates a new "mover".
	 */
	public NoMovement() {
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.SensorMover#findWay(visidia.graph.Sensor)
	 */
	@Override
	public SupportVertex findWay(Sensor sensor) throws MoveException {
		return sensor.getSupportVertex();
	}

}
