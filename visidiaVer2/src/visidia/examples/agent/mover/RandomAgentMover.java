package visidia.examples.agent.mover;

import java.util.Random;

import visidia.graph.Vertex;
import visidia.simulation.process.MoveException;
import visidia.simulation.process.agent.Agent;
import visidia.simulation.process.agent.AgentMover;

// TODO: Auto-generated Javadoc
/**
 * Provides a random move for an Agent. On a vertex, the agent goes to a random door.
 */
public class RandomAgentMover extends AgentMover {

	private static final long serialVersionUID = 73927463611767349L;

	/** The random. */
	private Random rand = new Random();

	/**
	 * Instantiates a new random agent mover.
	 */
	public RandomAgentMover() {	
	}
	
	/**
	 * Instantiates a new random agent mover.
	 * 
	 * @param ag the agent
	 */
	public RandomAgentMover(Agent ag) {
		super(ag);
	}

	/* (non-Javadoc)
	 * @see visidia.agent.mover.AgentMover#findNextDoor()
	 */
	public int findNextDoor() throws MoveException {
		int arity;
		arity = this.agent().getArity();
		int door;
		while(arity == 0){
			try {
				arity = this.agent().getArity();
				Thread.sleep(100);
			}
			catch (InterruptedException e) {}
		}

		Vertex vertex = this.agent().getDestinationVertex();
		if (!vertex.isSwitchedOn()) {
			throw new MoveException(MoveException.SwitchedOffVertex);
		} else {
			door = this.rand.nextInt(arity);
			while((!isOpenDoor(door, vertex))){
				arity = this.agent().getArity();
				door = this.rand.nextInt(arity);
			}
		}
		return door;
	}

}
