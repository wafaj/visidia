package visidia.simulation.process.messages;

// TODO: Auto-generated Javadoc
/**
 * This class represents a node's door. A door can be a reception door or a sending
 * door. This class is used in the MessagePacket class.
 */
public class Door {
	
	/** The door number. */
	private int doorNum;

	/**
	 * Instantiates a new door.
	 */
	public Door() {
		this(0);
	}

	/**
	 * Instantiates a new door.
	 * 
	 * @param num the door number
	 */
	public Door(int num) {
		this.doorNum = num;
	}

	/**
	 * Gets the door number.
	 * 
	 * @return the door number
	 */
	public int getNum() {
		return this.doorNum;
	}

	/**
	 * Sets the door number.
	 * 
	 * @param num the new door number
	 */
	public void setNum(int num) {
		this.doorNum = num;
	}

}
