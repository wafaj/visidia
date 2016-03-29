package visidia.simulation.evtack;

import java.util.Hashtable;

// TODO: Auto-generated Javadoc
/**
 * The Locker class manages locks, and stores them in a hash table.
 * A lock is an object associated to a unique id.
 */
public class Locker {

	/** The locks. */
	private Hashtable<Long, Object> locks;

	/** The number of locks. */
	private long numberOfLocks = 0;

	/** The instance. */
	private static Locker instance = new Locker();

	/**
	 * Instantiates a new locker.
	 */
	private Locker() {
		locks = new Hashtable<Long, Object>();
	}

	/**
	 * Gets the single instance of Locker.
	 * 
	 * @return single instance of Locker
	 */
	public static Locker getInstance() {
		return instance;
	}

	/**
	 * Creates a lock, and puts it it the hash table.
	 * 
	 * @return the lock id
	 */
	public synchronized Long createLock() {
		Long key = new Long(numberOfLocks++);
		Object lock = new Object();
		locks.put(key, lock);
		return key;
	}

	/**
	 * Removes the lock from the hash table.
	 * 
	 * @param lockId the lock id
	 * 
	 * @return the lock object
	 */
	public synchronized Object removeLock(Long lockId) {
		return locks.remove(lockId);
	}

	/**
	 * Gets the lock.
	 * 
	 * @param lockId the lock id
	 * 
	 * @return the lock
	 */
	public synchronized Object getLock(Long lockId) {
		return locks.get(lockId);
	}
}
