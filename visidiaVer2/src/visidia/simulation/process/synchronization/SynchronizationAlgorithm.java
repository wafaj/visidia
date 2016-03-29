package visidia.simulation.process.synchronization;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Random;

import visidia.simulation.SimulationConstants;
import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.process.edgestate.SyncState;
import visidia.simulation.process.messages.MessageType;

// TODO: Auto-generated Javadoc
/**
 * all synchronization algorithms should extend this class.
 */
public abstract class SynchronizationAlgorithm extends Algorithm {

	private static final long serialVersionUID = 1051404022115088336L;

	/** The answer. */
	protected int answer[];

	/** The synchronization object. */
	protected SynchronizationObject synob = null;

	/**
	 * Instantiates a new synchronization algorithm.
	 */
	public SynchronizationAlgorithm() {
		super();
	}

	/**
	 * Instantiates a new synchronization algorithm.
	 * 
	 * @param algo the algorithm
	 */
	public SynchronizationAlgorithm(SynchronizationAlgorithm algo) {
		if (algo.synob != null) this.synob = (SynchronizationObject) algo.synob.clone();
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.Algorithm#clone()
	 */
	abstract public Object clone();

	/**
	 * Try synchronize.
	 */
	public abstract void trySynchronize();

	/**
	 * Gets the synchronization object.
	 * 
	 * @return the synchronization object
	 */
	public SynchronizationObject getSynchronizationObject() {
		return synob;
	}

	/**
	 * Sets the synchronization object.
	 * 
	 * @param synObj the new synchronization object
	 */
	public void setSynchronizationObject(SynchronizationObject synObj) {
		this.synob = synObj;
	}

	/**
	 * Randomly gets a connected door.
	 * 
	 * @return the connected door
	 */
	protected final int getRandomConnectedDoor() {
		Random r = new Random();
		int value = 0;
		for (int k = 0; k < 5; k++) {
			value = Math.abs(r.nextInt() % this.getArity());
		}

		return value;
	}

	/**
	 * Gets the list types.
	 * 
	 * @return the list types
	 */
	public Collection<MessageType> getListTypes() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(SimulationConstants.Messages.SYNC);
		typesList.add(SimulationConstants.Messages.TERM);
		return typesList;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "Abstract Synchro";
	}

	/**
	 * Breaks synchronization.
	 */
	public void breakSynchro() {
		for (int door = 0; door < this.getArity(); door++) {
			this.setDoorState(new SyncState(false), door);
		}
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.process.Algorithm#init()
	 */
	@Override
	public void init() {
	}

}
