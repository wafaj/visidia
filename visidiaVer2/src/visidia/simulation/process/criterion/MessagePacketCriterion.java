package visidia.simulation.process.criterion;

import visidia.simulation.process.messages.MessagePacket;

// TODO: Auto-generated Javadoc
/**
 * MessagePacketCriterion is a message criterion wrapper that handles a message
 * packet, extracts its message which is then tested by a MessageCriterion.
 */
public class MessagePacketCriterion implements Criterion {

	/** The message criterion. */
	private MessageCriterion mc;

	/**
	 * Instantiates a new message packet criterion.
	 * 
	 * @param mc the message criterion
	 */
	public MessagePacketCriterion(MessageCriterion mc) {
		this.mc = mc;
	}

	/**
	 * Instantiates a new message packet criterion.
	 */
	MessagePacketCriterion() {
		this(null);
	}

	/**
	 * Returns true if the object is a MessagePacket, and contains a Message which satisfies the message criterion.
	 * 
	 * @param o the object
	 * 
	 * @return true, if criterion is satisfied.
	 * 
	 * @see visidia.simulation.process.criterion.Criterion#isMatchedBy(java.lang.Object)
	 */
	public boolean isMatchedBy(Object o) {
		if (this.mc == null) {
			return false;
		}

		if (!(o instanceof MessagePacket)) {
			return false;
		}

		MessagePacket mp = (MessagePacket) o;

		return this.mc.isMatchedBy(mp.message());
	}

}
