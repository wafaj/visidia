package visidia.simulation.process.messages;

import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * This class represents a message containing a set of information which can be of different type.
 */
public class VectorMessage extends Message {

	private static final long serialVersionUID = -8777922385637169162L;

	/** The message data. */
	private Vector data;

	/**
	 * Instantiates a new vector message.
	 * 
	 * @param v the vector
	 */
	public VectorMessage(Vector v) {
		this.data = v;
	}

	/**
	 * Instantiates a new vector message.
	 * 
	 * @param v the vector
	 * @param type the type
	 */
	public VectorMessage(Vector v, MessageType type) {
		this.setType(type);
		this.data = v;
	}

	/**
	 * Gets the data.
	 * 
	 * @return the vector
	 */
	public Vector data() {
		return (Vector) this.data.clone();
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.messages.Message#getData()
	 */
	@Override
	public Object getData() {
		return this.data.clone();
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.messages.Message#clone()
	 */
	@Override
	public Object clone() {
		return new VectorMessage((Vector) this.data.clone(), this.getType());
	}

	/**
	 * The Vector Message representation is : "< + element0 +
	 * element1 + ... + elementN + >"
	 * 
	 * @return the string
	 */
	@Override
	public String toString() {
		/*
		 * Ancien code if(data.elementAt(0) instanceof Boolean) return "<FIN>";
		 * 
		 * String id = ((Integer)data.elementAt(0)).toString(); String num
		 * =((Integer)data.elementAt(1)).toString();
		 * 
		 * return "<" + id + ">";
		 */

		String result = "<";
		int size = this.data.size();
		if (size == 1) {
			result += this.data.elementAt(0) + ">";
		} else {
			result += this.data.elementAt(0);
			for (int i = 1; i < this.data.size(); i++) {
				result += ";" + (this.data.elementAt(i)).toString();
			}
			result += ">";
		}
		return result;
	}

}
