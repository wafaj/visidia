package visidia.examples.algo.sensor.mover;

import java.util.Random;

import visidia.graph.Sensor;
import visidia.graph.SupportVertex;
import visidia.simulation.process.MoveException;
import visidia.simulation.process.SensorMover;

// TODO: Auto-generated Javadoc
/**
 * Provides a random walk for a Sensor. A Sensor will move to a random neighbor of the vertex.
 */
public class OneRandomStep extends SensorMover {

	private static final long serialVersionUID = 5295052506350439785L;

	/** The random object. */
	private Random rand = new Random();

	/**
	 * Instantiates a new mover.
	 */
	public OneRandomStep() {
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.SensorMover#findWay(visidia.graph.Sensor)
	 */
	@Override
	public SupportVertex findWay(Sensor sensor) throws MoveException {
		SupportVertex vertexFrom = sensor.getSupportVertex();

		int len = vertexFrom.degree();
		int next = rand.nextInt(len);
		
		return (SupportVertex) vertexFrom.getNeighborByDoor(next);
	}

}
