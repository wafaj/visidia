package visidia.simulation.process.messages;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * This class encapsulated a message in transit on the network.
 */
public class MessagePacket implements Serializable {

	private static final long serialVersionUID = 852544806867096751L;

	/** The sender id. */
	protected Integer srcId = null;

	/** The receiver id. */
	protected Integer destId = null;

	/** The message. */
	protected Message mesg = null;

	/** The sender door. */
	protected int srcDoor = -1;

	/** The receiver door. */
	protected int destDoor = -1;

	/**
	 * Instantiates a new message packet.
	 * 
	 * @param senderId the sender id
	 * @param srcDoor the sender door
	 * @param receiverId the receiver id
	 * @param destDoor the receiver door
	 * @param msg the message
	 */
	public MessagePacket(Integer senderId, int srcDoor, Integer receiverId,	int destDoor, Message msg) {
		this.srcId = new Integer(senderId.intValue());
		this.destId = new Integer(receiverId.intValue());
		this.mesg = msg;
		this.srcDoor = srcDoor;
		this.destDoor = destDoor;
	}
	
	/**
	 * Gets the receiver door.
	 * 
	 * @return the receiver door
	 */
	public int receiverDoor() {
		return this.destDoor;
	}

	/**
	 * Gets the message.
	 * 
	 * @return the message
	 */
	public Message message() {
		return this.mesg;
	}
	
}
