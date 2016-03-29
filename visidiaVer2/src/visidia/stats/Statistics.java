package visidia.stats;

import java.util.Hashtable;
import java.util.Set;
import javax.swing.event.EventListenerList;

// TODO: Auto-generated Javadoc
/**
 * This class consists in a collection of objects associated with their occurrence.
 */
public class Statistics {

	/** The stat listeners. */
	private final EventListenerList statListeners = new EventListenerList(); 

	/** The table. */
	private Hashtable<Object, Long> table;

	/**
	 * Instantiates a new statistics.
	 */
	public Statistics() {
		this.table = new Hashtable<Object, Long>();
	}

	/**
	 * Gets the occurrences of object.
	 * 
	 * @param o the object
	 * 
	 * @return the occurrences of
	 */
	public long getOccurrencesOf(Object o) {
		Long occurrences = this.table.get(o);

		if (occurrences == null) {
			return 0;
		} else {
			return occurrences.intValue();
		}
	}

	/**
	 * Add the current value of the Bag corresponding to the object 'o'
	 * with the value 'occurrences'.
	 * 
	 * @param o the object
	 * @param occurrences the occurrences
	 */
	public void add(Object o, long occurrences) {
		long newOccurrences = this.getOccurrencesOf(o) + occurrences;

		this.table.put(o, new Long(newOccurrences));
		updateStats();
	}

	/**
	 * Increments the current value of the Bag corresponding to the object 'o'.
	 * If there is no object, create one.
	 * 
	 * @param o the object
	 */
	public void add(Object o) {
		this.add(o, 1);
	}

	/**
	 * Compare the current value of the Bag corresponding to the object 'o'
	 * with the value 'occurrences' and replace the value of the Bag with
	 * the minimum of both values.
	 * 
	 * @param o the object
	 * @param occurrences the occurrences
	 */
	public void min(Object o, long occurrences) {
		if (occurrences < this.getOccurrencesOf(o)){
			this.table.put(o, new Long(occurrences));
			updateStats();
		}	
	}

	/**
	 * Compare the current value of the Bag corresponding to the object 'o'
	 * with the value 'occurrences' and replace the value of the Bag with
	 * the maximum of both values.
	 * 
	 * @param o the object
	 * @param occurrences the occurrences
	 */
	public void max(Object o, long occurrences){
		if (occurrences > this.getOccurrencesOf(o)){
			this.table.put(o, new Long(occurrences));
			updateStats();
		}	
	}

	/**
	 * Replace the current value of the Bag corresponding to the object 'o'
	 * with the value 'occurrences'.
	 * 
	 * @param o the object
	 * @param occurrences the occurrences
	 */
	public void replace (Object o, long occurrences){
		this.table.put(o, new Long(occurrences));
		updateStats();
	}


	/**
	 * As hash table.
	 * 
	 * @return the hashtable< object, long>
	 */
	public Hashtable<Object, Long> asHashTable() {
		return this.table;
	}

	/**
	 * Key set.
	 * 
	 * @return the set< object>
	 */
	public Set<Object> keySet() {
		return this.table.keySet();
	}

	private void updateStats() {
		StatListener[] listeners = this.getStatListeners();

		for (StatListener listener : listeners) {
			listener.updatedStats(this);
		}
	}
	
	/**
	 * Adds a stat listener.
	 * 
	 * @param listener the listener
	 */
	public void addStatListener(StatListener listener) {
		statListeners.add(StatListener.class, listener);
	}

	/**
	 * Removes a stat listener.
	 * 
	 * @param listener the listener
	 */
	public void removeStatListener(StatListener listener) {
		statListeners.remove(StatListener.class, listener);
	}

	/**
	 * Gets the stat listeners.
	 * 
	 * @return the stat listeners
	 */
	public StatListener[] getStatListeners() {
		return statListeners.getListeners(StatListener.class);
	}

}
