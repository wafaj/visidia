package visidia.simulation.process;

import visidia.graph.Vertex;
import visidia.simulation.command.MoveAgentCommand;
import visidia.simulation.process.agent.Agent;
import visidia.simulation.server.Server;
import visidia.stats.AgentSleepStat;

// TODO: Auto-generated Javadoc
/**
 * This class represents a communication process based on mobile agents.
 */
public class AgentProcess extends ProcessType {

	/** The agent. */
	private Agent agent = null;

	// An agent operates when it arrived at a vertex. We thus consider that an agent, if on a vertex, is always on the destination vertex.
	/** The origin. */
	private Vertex origin = null;
	
	/** The destination. */
	private Vertex destination = null;

	/**
	 * Instantiates a new agent process.
	 * 
	 * @param server the server
	 * @param vertex the vertex
	 * @param id the id
	 */
	public AgentProcess(Server server, Vertex vertex, int id) {
		super(id, server);
		this.origin = vertex;
		this.destination = vertex;
	}

	/**
	 * Gets the agent.
	 * 
	 * @return the agent
	 */
	public Agent getAgent() {
		return agent;
	}

	/**
	 * Sets the agent.
	 * 
	 * @param agent the new agent
	 */
	public void setAgent(Agent agent) {
		this.agent = agent;
		this.agent.setAgentProcess(this);
	}

	/**
	 * Gets the origin vertex.
	 * 
	 * @return the origin vertex
	 */
	public Vertex getOriginVertex() {
		return origin;
	}

	/**
	 * Gets the destination vertex.
	 * 
	 * @return the destination vertex
	 */
	public Vertex getDestinationVertex() {
		return destination;
	}

	/**
	 * Sets the origin vertex.
	 * 
	 * @param origin the new origin vertex
	 */
	public void setOriginVertex(Vertex origin) {
		this.origin = origin;
	}

	/**
	 * Sets the destination vertex.
	 * 
	 * @param destination the new destination vertex
	 */
	public void setDestinationVertex(Vertex destination) { 
		this.destination = destination;
	}

	/**
	 * Dead agent.
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public void deadAgent() throws InterruptedException {
		server.getConsole().deadAgent(this);
	}

	/**
	 * New pulse.
	 * 
	 * @param pulse the pulse
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	/*
	public void newPulse(int pulse) throws InterruptedException {
		NewPulseCommand cmd = new NewPulseCommand(this, pulse);
		server.sendToConsole(cmd);
	}
*/
	/**
	 * Moves an Agent to a specified door.
	 * 
	 * @param ag the Agent you want to move
	 * @param door the door to which you want to move the Agent
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public void moveAgentTo(Agent ag, int door) throws InterruptedException {
		if ((door < 0) || (door >= destination.degree())) {
			throw new IllegalArgumentException("This door doesn't exist !");
		}

		Vertex vertexFrom = destination;
		Vertex vertexTo = vertexFrom.getNeighborByDoor(door);

		setOriginVertex(vertexFrom);
		setDestinationVertex(vertexTo);

		//server.sendToConsole(new MoveAgentCommand(this, vertexFrom, vertexTo));
		server.sendToConsole(new MoveAgentCommand(this.getId(), vertexFrom.getId(), vertexTo.getId()));

		//if (!data.vertex.getVisualization()) ag.processingAgentWhenSwitchingOff();
	}

	/**
	 * Makes the specified agent fall asleep for a given amount of milliseconds.
	 * 
	 * @param ag Agent given to fall asleep
	 * @param milliseconds the milliseconds
	 * @throws InterruptedException 
	 */
	public void sleep(Agent ag, int milliseconds) throws InterruptedException {
		Thread.sleep(milliseconds);
		this.getServer().getConsole().getStats().add(new AgentSleepStat(ag.getClass(), id), milliseconds);
	}

}
