package visidia.simulation.process.messages;

// TODO: Auto-generated Javadoc
/**
 * This class represents a message containing an integer.
 */
public class DuelMessage extends Message {

	private static final long serialVersionUID = -2494451028518372924L;
	
	/** The message data. */
	Integer data;

	/**
	 * Instantiates a new integer message.
	 * 
	 * @param data the data
	 */
	public DuelMessage(Integer data) {
		this.data = new Integer(data.intValue());
	}

	/**
	 * Instantiates a new integer message.
	 * 
	 * @param data the data
	 */
	public DuelMessage(int data) {
		this.data = new Integer(data);
	}

	/**
	 * Instantiates a new integer message.
	 * 
	 * @param data the data
	 * @param type the type
	 */
	public DuelMessage(Integer data, MessageType type) {
		this.data = new Integer(data.intValue());
		this.setType(type);
	}

	/**
	 * Instantiates a new integer message.
	 * 
	 * @param data the data
	 * @param type the type
	 */
	public DuelMessage(int data, MessageType type) {
		this.data = new Integer(data);
		this.setType(type);
	}

	/**
	 * Return an int representation of data.
	 * 
	 * @return the int representation
	 */
	public int value() {
		return this.data.intValue();
	}

	/**
	 * Same as getData().
	 */
	public Integer data() {
		return new Integer(this.data.intValue());
	}

	/**
	 * Returns an Integer representation of data using : new Integer(data).
	 * 
	 * @return the Integer representation
	 */
	@Override
	public Integer getData() {
		return new Integer(this.data.intValue());
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.messages.Message#clone()
	 */
	@Override
	public Object clone() {
		return new DuelMessage(this.data, this.getType());
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.messages.Message#toString()
	 */
	@Override
	public String toString() {
		return  "\u2694";
	}

}
