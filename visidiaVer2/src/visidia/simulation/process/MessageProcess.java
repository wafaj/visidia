package visidia.simulation.process;

import visidia.graph.Vertex;
import visidia.misc.property.VisidiaProperty;
import visidia.simulation.command.ChangeNodePropertyCommand;
import visidia.simulation.command.SendMessageCommand;
import visidia.simulation.evtack.VQueue;
import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.process.criterion.Criterion;
import visidia.simulation.process.messages.Door;
import visidia.simulation.process.messages.Message;
import visidia.simulation.process.messages.MessagePacket;
import visidia.simulation.server.Server;

// TODO: Auto-generated Javadoc
/**
 * This class represents a communication process based on message passing.
 */
public class MessageProcess extends ProcessType {

	/** The vertex. */
	private Vertex vertex = null;
	
	/** The algorithm. */
	private Algorithm algorithm = null;

	/** The message queue. */
	protected VQueue msgVQueue = new VQueue();

	/**
	 * Instantiates a new message process.
	 * 
	 * @param server the server
	 * @param vertex the vertex
	 */
	public MessageProcess(Server server, Vertex vertex) {
		super(vertex.getId(), server);
		this.vertex = vertex;
	}

	/**
	 * Gets the algorithm.
	 * 
	 * @return the algorithm
	 */
	public Algorithm getAlgorithm() {
		return algorithm;
	}

	/**
	 * Sets the algorithm.
	 * 
	 * @param algorithm the new algorithm
	 */
	public void setAlgorithm(Algorithm algorithm) {
		this.algorithm = algorithm;
		this.algorithm.setMessageProcess(this);
	}

	/**
	 * Gets the vertex.
	 * 
	 * @return the vertex
	 */
	public Vertex getVertex() {
		return vertex;
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.ProcessType#start()
	 */
	public void start() {
		super.start();
	}

	/**
	 * Sends a message.
	 * 
	 * @param door the door
	 * @param msg the message
	 * 
	 * @return true, if successful
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public boolean sendMessageTo(int door, Message msg) throws InterruptedException {		
		int senderId = this.vertex.getId();
		Vertex receiver = this.vertex.getNeighborByDoor(door);
		int receiverId = receiver.getId();
		//msg.setVisualization((this.getDraw(senderId) || this.getDraw(receiverId)));
		msg.setVisualization(true);

		int receiveDoor = receiver.getDoorTo(this.vertex);
		MessagePacket msgPacket = new MessagePacket(senderId, door, receiverId, receiveDoor, msg);

		SendMessageCommand cmd = new SendMessageCommand(this.getId(), receiverId, msgPacket);
		server.sendToConsole(cmd);
		return true;
	}

	/**
	 * Gets the next message received on the specified door (if any) corresponding to the criterion (if any).
	 * 
	 * @param c the criterion
	 * @param door the door
	 * 
	 * @return the next message
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public Message getNextMessage(Door door, Criterion c) throws InterruptedException {
		MessagePacket msgPacket = null;
		if (c != null) msgPacket = (MessagePacket) msgVQueue.get(c);
		else msgPacket = (MessagePacket) msgVQueue.get();
		 
		if (door != null) door.setNum(msgPacket.receiverDoor());

		return msgPacket.message();
	}

	/**
	 * Gets the next message packet matching the criterion. Does not block until a message has arrived.
	 * 
	 * @param c the criterion
	 * 
	 * @return the next message packet, or null if no message has arrived
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public MessagePacket getNextMessagePacketNoWait(Criterion c) throws InterruptedException {
		return (MessagePacket) msgVQueue.getNoWait(c);
	}
	
	/**
	 * Puts message on the queue.
	 * 
	 * @param msgPacket the message packet
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public void putMessage(MessagePacket msgPacket) throws InterruptedException {
		msgVQueue.put(msgPacket);
	}

	/**
	 * Terminated algorithm.
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public void terminatedAlgorithm() throws InterruptedException {
		server.getConsole().terminatedAlgorithm(this);
	}

	/**
	 * Tests if the message VQueue is empty.
	 * 
	 * @param c the criterion
	 * 
	 * @return true, if the queue is empty
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public boolean emptyVQueue(Criterion c) throws InterruptedException {
		return (c != null) ? !(msgVQueue.contains(c)) : msgVQueue.isEmpty();
	}

	/**
	 * Initializes the node property. Do not use this method in algorithm implementation!
	 * 
	 * @param property the property
	 */
	public void initNodeProperty(VisidiaProperty property) {
		this.vertex.setProperty(property);
	}

	/**
	 * Sets the node property.
	 * 
	 * @param nodeId the node id
	 * @param property the property
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public void setNodeProperty(int nodeId, VisidiaProperty property) throws InterruptedException {
		synchronized (vertex) {
			ChangeNodePropertyCommand cmd = new ChangeNodePropertyCommand(nodeId, property);
			server.sendToConsole(cmd);
		}
	}
	
	/**
	 * Gets the node property.
	 * 
	 * @param key the key
	 * 
	 * @return the node property value
	 */
	public Object getNodeProperty(String key) {
		synchronized (vertex) {
			VisidiaProperty prop = vertex.getVisidiaProperty(key);
			return (prop == null ? null : prop.getValue());
		}
	}
	
}
