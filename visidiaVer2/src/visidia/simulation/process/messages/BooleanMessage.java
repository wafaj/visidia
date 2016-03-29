package visidia.simulation.process.messages;

// TODO: Auto-generated Javadoc
/**
 * This class represents a message containing a boolean.
 */
public class BooleanMessage extends Message {

	private static final long serialVersionUID = -7269286917512500998L;
	
	/** The data. */
	boolean data;

	/**
	 * Instantiates a new boolean message.
	 * 
	 * @param value the value
	 */
	public BooleanMessage(boolean value) {
		this.data = value;
	}

	/**
	 * Instantiates a new boolean message.
	 * 
	 * @param data the data
	 * @param type the type
	 */
	public BooleanMessage(boolean data, MessageType type) {
		this.data = data;
		this.setType(type);
	}

	/**
	 * Value.
	 * 
	 * @return true, if successful
	 */
	public boolean value() {
		return this.data;
	}

	/**
	 * Data.
	 * 
	 * @return true, if successful
	 */
	public boolean data() {
		return this.data;
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.messages.Message#clone()
	 */
	@Override
	public Object clone() {
		return new BooleanMessage(this.data, this.getType());
	}

	/**
	 * the returned object is a new Boolean initialized with the value of data.
	 * 
	 * @return the data
	 */
	@Override
	public Object getData() {
		return new Boolean(this.data);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.messages.Message#toString()
	 */
	@Override
	public String toString() {
		return (new Boolean(this.data)).toString();
	}

}
