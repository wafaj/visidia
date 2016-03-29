package visidia.simulation.process;

// TODO: Auto-generated Javadoc
/**
 * This class handles exception for agent movers.
 */
public class MoveException extends Exception {

	private static final long serialVersionUID = 3971941989929504145L;

	/** The Constant NoDoorFound. */
	public static final int NoDoorFound = 1;

	/** The Constant SwitchedOffVertex. */
	public static final int SwitchedOffVertex = 2;

	/** The type. */
	private int type = 0;

	/**
	 * Instantiates a new move exception.
	 * 
	 * @param t the t
	 */
	public MoveException(int t) {
		this.type = t;
	}

	/**
	 * Gets the movement type exception.
	 * 
	 * @return the movement type exception
	 */
	public int getMovementTypeException() {
		return this.type;
	}

}

