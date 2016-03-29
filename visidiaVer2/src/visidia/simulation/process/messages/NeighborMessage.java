package visidia.simulation.process.messages;

import visidia.rule.Neighbor;

// TODO: Auto-generated Javadoc
/**
 * This class represents information (label, mark) about a neighbor.
 */
public class NeighborMessage extends Message {

	private static final long serialVersionUID = 3426417069667843580L;

	/** The mark. */
	private boolean mark = false;

	/** The label. */
	private String label = "";

	/**
	 * Instantiates a new neighbor message.
	 * 
	 * @param l the label
	 * @param b the mark
	 */
	public NeighborMessage(String l, boolean b) {
		this.mark = b;
		this.label = l;
	}

	/**
	 * Instantiates a new neighbor message.
	 * 
	 * @param n the neighbor
	 */
	public NeighborMessage(Neighbor n) {
		this.mark = n.mark();
		this.label = n.state();
	}

	/**
	 * Instantiates a new neighbor message.
	 * 
	 * @param n the neighbor
	 * @param t the message type
	 */
	public NeighborMessage(Neighbor n, MessageType t) {
		this(n);
		this.setType(t);
	}

	/**
	 * Mark.
	 * 
	 * @return true, if successful
	 */
	public boolean mark() {
		return this.mark;
	}

	/**
	 * Label.
	 * 
	 * @return the string
	 */
	public String label() {
		return this.label;
	}

	/**
	 * Gets the neighbor.
	 * 
	 * @return the neighbor
	 */
	public Neighbor getNeighbour() {
		return new Neighbor(this.label, this.mark);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.messages.Message#clone()
	 */
	@Override
	public Object clone() {
		NeighborMessage n = new NeighborMessage(this.label(), this.mark());
		n.setType(this.getType());
		return n;
	}

	/**
	 * the returned object is a clone of this message.
	 * 
	 * @return the data
	 */
	@Override
	public Object getData() {
		return new Neighbor(this.label, this.mark);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.messages.Message#toString()
	 */
	@Override
	public String toString() {
		if (this.mark()) {
			return "-X-" + this.label();
		}
		return "---" + this.label();
	}

}
