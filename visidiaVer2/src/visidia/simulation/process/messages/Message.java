package visidia.simulation.process.messages;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * This is the abstract base class representing the messages exchanged between the network nodes.
 * 
 * To avoid cross-references (different nodes owning a reference to the same Message), messages
 * must be cloned before being added to the receiver queue. 
 */
public abstract class Message implements Cloneable, Serializable {

	private static final long serialVersionUID = 1366472112822681655L;

	/** This field represents the time by which the message has been sent. The user can ignore it if the algorithm he is writing is asynchronous.
	 * When writing an asynchronous algorithm, this field is deeply used by the simulator. On the other hand, It can be set by hand by the user
	 * who is programming an algorithm (he must use the set and get method). It also allows to simulate the Lamport clock as well when writing an
	 * algorithm which class base is LamportAlgorithm */
	private int clock;

	/** Each message has a type. The programmer may specify the type of the messages he creates. By default the type is defaultMessageType. */
	private MessageType type = MessageType.defaultMessageType;

	/** The visualization. */
	private boolean visualization = true;

	/**
	 * This method allows the user to set the time at which the message is sent.
	 * 
	 * @param value the value
	 */
	public void setMsgClock(int value) {
		this.clock = value;
	}

	/**
	 * This method allows the user to get the time at which the message has been sent.
	 * 
	 * @return the msg clock
	 */
	public int getMsgClock() {
		return this.clock;
	}

	/**
	 * This method sets the type of a message. This is only for the purpose of
	 * visualization. Nevertheless, the user can create new message types and
	 * use them in his algorithms. The default message type is
	 * DefaultMessageType (red, to be visualized, name = default).
	 * 
	 * @param type the type
	 */
	public void setType(MessageType type) {
		this.type = type;
	}

	/**
	 * gets the message type. If the user has set a message type T then the
	 * method returns this Type T. Otherwise if no Type has been set, then the
	 * default message type is DefaultMessageType.
	 * 
	 * @return the type
	 */
	public MessageType getType() {
		return this.type;
	}

	/**
	 * Each message has a data which can be accessed by the getData() method.
	 * 
	 * @return the data
	 */
	public abstract Object getData();

	/**
	 * Returns a string representation of the message. This is used for the
	 * visualization. For the visualization, it is important to provide a nice
	 * implementation for the this.toString() method.
	 * 
	 * @return the string
	 */
	public abstract String toString();

	/**
	 * Checks if the message is to be visualized.
	 * 
	 * @return true, if the message is to be visualized; false otherwise.
	 */
	public boolean getVisualization() {
		return this.visualization;
	}

	/**
	 * Sets if the message is to be visualized.
	 * 
	 * @param s true if the message is to be visualized; false otherwise
	 */
	public void setVisualization(boolean s) {
		this.visualization = s;
	}

	/**
	 * Gives a copy (a clone) of this object.
	 * 
	 * @return the object
	 */
	public abstract Object clone();

}
