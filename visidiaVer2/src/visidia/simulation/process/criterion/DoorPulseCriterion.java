package visidia.simulation.process.criterion;

import visidia.simulation.process.messages.Door;
import visidia.simulation.process.messages.Message;
import visidia.simulation.process.messages.MessagePacket;

/**
 * allows to retrieve a message from a node queue according to the pulse and/or
 * door number. This class is used in the SyncAlgorithm to filter messages and
 * allows the user to get messages according to a given pulse and/or door.
 */
public class DoorPulseCriterion implements Criterion {

	private int pulse;

	private Door door;

	/**
	 * the door and the pulse are ignored. matches any messagePacket.
	 * 
	 */

	public DoorPulseCriterion() {
		this.pulse = -1;
		this.door = null;
	}

	/**
	 * the door is ignored. matches any messagePacket sent at the given pulse.
	 * 
	 */
	public DoorPulseCriterion(int pulse) {
		this.pulse = pulse;
	}

	/**
	 * matches any messagePacket sent at the given pulse and arrived at the
	 * given port.
	 * 
	 */
	public DoorPulseCriterion(int door, int pulse) {
		this.pulse = pulse;
		this.door = new Door(door);
	}

	/**
	 * return true if the object o is instance of a MessagePacket object and if
	 * the door and pulse of the MessagePacket object o match respectively the
	 * door and pulse of this object, otherwise return false. If the door (resp.
	 * the pulse) of the criterion is not specified (null) then the message
	 * packet door (resp. pulse) number is not taken into account.
	 * 
	 */
	public boolean isMatchedBy(Object o) {
		if (!(o instanceof MessagePacket)) {
			return false;
		}

		if ((this.pulse == -1) && (this.door == null)) {
			return true;
		} else if (this.pulse == -1) {
			/* Message msg = */((MessagePacket) o).message();
			int d = ((MessagePacket) o).receiverDoor();
			if (this.door.getNum() == d) {
				return true;
			} else {
				return false;
			}
		} else if (this.door == null) {
			Message msg = ((MessagePacket) o).message();
			int p = msg.getMsgClock();
			if (this.pulse == p) {
				return true;
			}
			return false;
		} else {
			Message msg = ((MessagePacket) o).message();
			int p = msg.getMsgClock();
			int d = ((MessagePacket) o).receiverDoor();
			if ((this.door.getNum() == d) && (this.pulse == p)) {
				return true;
			}
			return false;
		}
	}

	public int getPulse() {
		if (this.pulse == -1) {
			return -1;
		}

		return this.pulse;
	}

	public int getDoor() {
		if (this.door == null) {
			return -1;
		}
		return this.door.getNum();
	}

	/**
	 * set the door in the criterion. This enables to create a criterion where
	 * the pulse is ignored (use the default constructor and then set the door).
	 * This is never done in the SyncAlgorithm class but the user may use it.
	 * 
	 */
	public void setDoor(int door) {
		if (this.door == null) {
			this.door = new Door(door);
		} else {
			this.door.setNum(door);
		}
	}

	public void setPulse(int pulse) {
		this.pulse = pulse;
	}

	public boolean pulseIsNull() {
		return this.pulse == -1;
	}

	public boolean doorIsNull() {
		return this.door == null;
	}

}
