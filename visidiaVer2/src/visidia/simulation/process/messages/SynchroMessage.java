package visidia.simulation.process.messages;

// TODO: Auto-generated Javadoc
/**
 * This class represents a message containing an integer.
 */
public class SynchroMessage extends Message {

	private static final long serialVersionUID = -2494451028518372924L;

	public static final int NO = 0;
	public static final int OK = 1;
	public static final int MAX = 65535;
	public static final int TERM = -1;
	public static final int GTERM = -3;

	/** The message data. */
	Integer data;

	/**
	 * Instantiates a new integer message.
	 * 
	 * @param data the data
	 */
	public SynchroMessage(Integer data) {
		this.data = new Integer(data.intValue());
	}

	/**
	 * Instantiates a new integer message.
	 * 
	 * @param data the data
	 */
	public SynchroMessage(int data) {
		this.data = new Integer(data);
	}

	/**
	 * Instantiates a new integer message.
	 * 
	 * @param data the data
	 * @param type the type
	 */
	public SynchroMessage(Integer data, MessageType type) {
		this.data = new Integer(data.intValue());
		this.setType(type);
	}

	/**
	 * Instantiates a new integer message.
	 * 
	 * @param data the data
	 * @param type the type
	 */
	public SynchroMessage(int data, MessageType type) {
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
		return new SynchroMessage(this.data, this.getType());
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.messages.Message#toString()
	 */
	@Override
	public String toString() {
		if (this.data == SynchroMessage.OK)
			return "\u263B";
		if (this.data == SynchroMessage.NO)
			return "\u2639";
		if (this.data == SynchroMessage.TERM || this.data == SynchroMessage.GTERM)
			return "\u263D";
		if (this.data >= SynchroMessage.OK)
			return Integer.toString(data);
		return "?";
	}

}
