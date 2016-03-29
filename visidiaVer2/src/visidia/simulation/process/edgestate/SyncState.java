package visidia.simulation.process.edgestate;

// TODO: Auto-generated Javadoc
/**
 * This class represents an edge in the state "synchronized".
 */
public class SyncState extends EdgeState {

	private static final long serialVersionUID = 81983529038716965L;

	/** The synchronized state. */
	boolean isSynchro;

	/**
	 * Instantiates a new sync state.
	 * 
	 * @param b the synchronized state
	 */
	public SyncState(boolean b) {
		this.isSynchro = b;
	}

	/**
	 * Checks if is synchronized.
	 * 
	 * @return true, if is synchronized
	 */
	public boolean isSynchronized() {
		return this.isSynchro;
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.edgestate.EdgeState#clone()
	 */
	@Override
	public Object clone() {
		return new SyncState(this.isSynchro);
	}

}
