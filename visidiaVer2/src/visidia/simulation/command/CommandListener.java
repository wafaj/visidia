package visidia.simulation.command;

import java.util.EventListener;

import visidia.graph.Edge;
import visidia.graph.Vertex;
import visidia.misc.property.VisidiaProperty;
import visidia.simulation.evtack.VisidiaEvent;
import visidia.simulation.process.ProcessType;
import visidia.simulation.process.edgestate.EdgeState;
import visidia.simulation.process.messages.Message;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving command events.
 * The class that is interested in processing a command
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addCommandListener<code> method. When
 * the command event occurs, that object's appropriate
 * method is invoked.
 */
public interface CommandListener extends EventListener {

	/**
	 * Message sent.
	 * 
	 * @param senderId the sender id
	 * @param receiverId the receiver id
	 * @param msg the message
	 * @param event the event
	 */
	void messageSent(int senderId, int receiverId, Message msg, VisidiaEvent event);

	/**
	 * Edge state changed.
	 * 
	 * @param newEdgeState the new edge state
	 * @param v1 the first vertex
	 * @param v2 the second vertex
	 */
	void edgeStateChanged(EdgeState newEdgeState, Vertex v1, Vertex v2);

	/**
	 * Node property changed.
	 * 
	 * @param vertexId the vertex id
	 * @param property the property
	 */
	void nodePropertyChanged(int vertexId, VisidiaProperty property);

	/**
	 * Edge property changed.
	 * 
	 * @param edge the edge
	 * @param property the property
	 */
	void edgePropertyChanged(Edge edge, VisidiaProperty property);

	/**
	 * Simulation terminated.
	 */
	void simulationTerminated();

	/**
	 * Agent moved.
	 * 
	 * @param agentId the agent id
	 * @param origin the origin
	 * @param destination the destination
	 * @param event the event
	 */
	void agentMoved(int agentId, Vertex origin, Vertex destination, VisidiaEvent event);

	/**
	 * Agent dead.
	 * 
	 * @param agentId the agent id
	 * @param event the event
	 */
	void agentDead(int agentId, VisidiaEvent event);
	
	/**
	 * Pulse changed.
	 * 
	 * @param sender the sender
	 * @param pulse the pulse
	 */
	void pulseChanged(ProcessType sender, int pulse);

}
