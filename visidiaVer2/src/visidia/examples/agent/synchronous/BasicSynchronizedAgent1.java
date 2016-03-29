package visidia.examples.agent.synchronous;

import visidia.simulation.process.agent.SynchronousAgent;

/**
 * I'm one of the synchronized agents. I was developed to show how to write
 * agents that are waiting each other.
 * <p>
 * 
 * I wait 1 second between each move and I move 10 times before dying.
 */
public class BasicSynchronizedAgent1 extends SynchronousAgent {

	private static final long serialVersionUID = 5978027990167408136L;

	@Override
	public Object clone() {
		return new BasicSynchronizedAgent1();
	}

	@Override
	protected void init() {
		this.setAgentMover("RandomAgentMover");

		for (int i = 0; i < 10; ++i) {
			this.sleep(1000);

			/**
			 * nextPulse() is the method to use when you finish your work. It
			 * waits for the other synchronized agents to do the same
			 * nextPulse(). When all are done, nextPulse() returns and the next
			 * action is executed.
			 */
			this.nextPulse();
			this.move();
		}
	}

}
