package visidia.simulation.process.agent;

import java.io.Serializable;
import java.util.Enumeration;

import visidia.graph.Vertex;
import visidia.simulation.process.MoveException;

// TODO: Auto-generated Javadoc
/**
 * Abstract class providing different moving types for the agents. You should
 * subclass this class to create your own style of move.
 */
public abstract class AgentMover implements Serializable {
	
	private static final long serialVersionUID = 111876839741834937L;

	/** Agent associated to the mover. */
	private Agent agent = null;

	/**
	 * Instantiates a new agent mover.
	 */
	public AgentMover() {
	}
	
	/**
	 * Creates a new agent mover.
	 * 
	 * @param ag agent associated to this mover.
	 */
	public AgentMover(Agent ag) {
		this.agent = ag;
	}

	/**
	 * Returns the agent associated to this mover.
	 * 
	 * @return the agent
	 */
	protected final Agent agent() {
		return this.agent;
	}

	/**
	 * Sets the agent.
	 * 
	 * @param ag the new agent
	 */
	public void setAgent(Agent ag) {
		this.agent = ag;
	}

	/**
	 * Moves the agent to the next door.
	 * 
	 * @throws InterruptedException the interrupted exception
	 * @throws MoveException the move exception
	 */
	public void move() throws InterruptedException, MoveException {
		this.move(this.findNextDoor());
	}

	/**
	 * Moves the agent to a specified door.
	 * 
	 * @param door Door to which move.
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public final void move(int door) throws InterruptedException {
		this.agent.moveToDoor(door);
	}

	/**
	 * Returns the door to which the agent will go. This method needs to be
	 * specialized in the sub-classes.
	 * 
	 * @return the door
	 * 
	 * @throws MoveException the move exception
	 */
	public abstract int findNextDoor() throws MoveException;

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return "No description for this agent mover.";
	}
	
	/**
	 * Tests if the door is open, and if the vertex is accessible.
	 * 
	 * @param door the door
	 * @param vertex the vertex
	 * 
	 * @return true, if checks if is open door
	 */
	public Boolean isOpenDoor(int door, Vertex vertex){

		Enumeration<Vertex> e = vertex.getNeighbors();
		int i = 0;
		while (e.hasMoreElements()) {
			Vertex v = (Vertex) e.nextElement();
			if (i==door && v.isSwitchedOn() && vertex.isSwitchedOn()) {
				return true;
			}
			i++;
		}
		return false;
	}

}