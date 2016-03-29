package visidia.simulation.process.messages;

// TODO: Auto-generated Javadoc
/**
 * This class represents a message containing a string.
 */
public class StringMessage extends Message {

	private static final long serialVersionUID = -1960138092850273437L;

	/** The message data. */
	String data;

	/**
	 * Instantiates a new string message.
	 * 
	 * @param data the data
	 */
	public StringMessage(String data) {
		this.data = new String(data);
	}

	/**
	 * Instantiates a new string message.
	 * 
	 * @param data the data
	 * @param type the type
	 */
	public StringMessage(String data, MessageType type) {
		this.setType(type);
		this.data = new String(data);
	}

	/**
	 * Same as getData().
	 */
	public String data() {
		return new String(this.data);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.messages.Message#clone()
	 */
	@Override
	public Object clone() {
		return new StringMessage(this.data, this.getType());
	}

	/**
	 * Returns a String representation of data.
	 * 
	 * @return the String representation
	 */
	@Override
	public Object getData() {
		return new String(this.data);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.messages.Message#toString()
	 */
	@Override
	public String toString() {
		return this.data;
	}

}
