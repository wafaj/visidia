package visidia.simulation.process.criterion;

import visidia.simulation.process.messages.MessagePacket;

// TODO: Auto-generated Javadoc
/**
 * DoorCriterion is used to select a message packet according to its incoming door.
 */
public class DoorCriterion implements Criterion {

	/** The wanted door. */
	private int wantedDoor;

	/**
	 * Instantiates a new door criterion.
	 * 
	 * @param wantedDoor the wanted door
	 */
	public DoorCriterion(int wantedDoor) {
		this.wantedDoor = wantedDoor;
	}

	/**
	 * Returns true if object is a MessagePacket coming from the expected door.
	 * 
	 * @param o the object
	 * 
	 * @return true, if criterion is satisfied.
	 * 
	 * @see visidia.simulation.process.criterion.Criterion#isMatchedBy(java.lang.Object)
	 */
	public boolean isMatchedBy(Object o) {
		if (!(o instanceof MessagePacket)) {
			return false;
		}

		MessagePacket mesgPacket = (MessagePacket) o;
		int door = mesgPacket.receiverDoor();

		return door == this.wantedDoor;
	}

}
