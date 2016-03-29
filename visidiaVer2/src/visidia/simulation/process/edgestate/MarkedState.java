package visidia.simulation.process.edgestate;

// TODO: Auto-generated Javadoc
/**
 * This class represents an edge in the state "marked".
 */
public class MarkedState extends EdgeState {

	private static final long serialVersionUID = 507144533818612148L;

	/** The marked state. */
	boolean isMarked;

	/**
	 * Instantiates a new marked state.
	 * 
	 * @param b the marked state
	 */
	public MarkedState(boolean b) {
		this.isMarked = b;
	}

	/**
	 * Checks if is marked.
	 * 
	 * @return true, if is marked
	 */
	public boolean isMarked() {
		return this.isMarked;
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.edgestate.EdgeState#clone()
	 */
	@Override
	public Object clone() {
		return new MarkedState(this.isMarked);
	}

}
