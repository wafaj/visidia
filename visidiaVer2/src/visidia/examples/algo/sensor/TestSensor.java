package visidia.examples.algo.sensor;

import visidia.misc.SynchronizedRandom;
import visidia.simulation.process.algorithm.SensorSyncAlgorithm;
import visidia.simulation.process.messages.StringMessage;

// TODO: Auto-generated Javadoc
/**
 * This class is an algorithm for sensor simulation which send a
 * message "Hello" to a neighbor. The neighbor is chosen randomly.
 */
public class TestSensor extends SensorSyncAlgorithm {

	private static final long serialVersionUID = -82360189770773378L;

	/* (non-Javadoc)
	 * @see visidia.simulation.process.Algorithm#init()
	 */
	public void init() {
		this.move(this.getId());

		for (int i = 1; i <= 10 * this.getId(); i++) {
			if (getArity() != 0){ 
				this.sendTo(this.chooseNeigbour(), new StringMessage("Hello"));
			}	
			this.move(this.getId());
			try {
				Thread.sleep(600);
			} catch (Exception e) {
			}
		}
		this.moveAfterEnd(this.getId());
	}

	/**
	 * Randomly chooses a neighbor.
	 * 
	 * @return the neighbor
	 */
	private int chooseNeigbour() {
		return Math.abs((SynchronizedRandom.nextInt())) % this.getArity();
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.Algorithm#clone()
	 */
	public Object clone() {
		return new TestSensor();
	}

}
