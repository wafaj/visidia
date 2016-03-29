package visidia.examples.agent.mover;

import java.util.Random;

import visidia.simulation.process.MoveException;
import visidia.simulation.process.agent.Agent;

// TODO: Auto-generated Javadoc
/**
 * Provides a random walk for an Agent. An Agent will move to a random door of the vertex half of the time.
 */
public class RandomWalk extends RandomAgentMover {

	private static final long serialVersionUID = 3764880841291326591L;

	/**
	 * Instantiates a new random walk.
	 * 
	 * @param ag the agent
	 */
	public RandomWalk(Agent ag) {
		super(ag);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.AgentMover#move()
	 */
	public final void move() throws InterruptedException, MoveException {
		Random rand = new Random();
		if (0 == rand.nextInt(2)) {
			super.move();
		}
	}
}
