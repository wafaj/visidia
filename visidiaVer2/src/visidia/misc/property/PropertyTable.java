package visidia.misc.property;

import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import visidia.simulation.SimulationAbortError;

// TODO: Auto-generated Javadoc
/**
 * A property table stores properties under names like a Hashtable. The difference
 * with the hashtable is given by default values which are not stored to save space.
 */
public class PropertyTable implements Serializable, Cloneable {

	private static final long serialVersionUID = 5363356107992818176L;

	/** The values. */
	private Hashtable<Object, VisidiaProperty> values;

	/** The defaults. */
	private Hashtable<Object, VisidiaProperty> defaults;

	/** The lock owner. */
	transient private Object lockOwner = null;

	/** The lock object. */
	transient private Object concurrentObject = null;

	/** The Constant nullObject, used to virtually store "null" in the values hash table. */
	transient private final static VisidiaProperty nullProperty = new VisidiaProperty();

	/**
	 * Using this constructor, there will be no default values. The property table will be used just like a Hashtable.
	 */
	public PropertyTable() {
		this(new Hashtable<Object, VisidiaProperty>());
	}

	/**
	 * Constructs a new property table with default values.
	 * 
	 * @param defaults Default properties that will be used when nobody has modified them.
	 */
	public PropertyTable(Hashtable<Object, VisidiaProperty> defaults) {
		this(defaults, new Hashtable<Object, VisidiaProperty>());
	}

	/**
	 * Constructs a new property table with default and specifics values.
	 * 
	 * @param def Default properties that will be used when nobody has modified them.
	 * @param properties Specifics properties that will be used for this property table.
	 */
	public PropertyTable(Hashtable<Object, VisidiaProperty> def, Hashtable<Object, VisidiaProperty> properties) {
		if (def == null) def = new Hashtable<Object, VisidiaProperty>();
		if (properties == null) properties = new Hashtable<Object, VisidiaProperty>();
		this.defaults = def;
		this.values = properties;
		this.concurrentObject = new Object();
	}

	/**
	 * Sets the properties.
	 *
	 * @param properties the new properties
	 */
	public void setProperties(PropertyTable properties) {
		this.values.clear();
		this.values = (Hashtable<Object, VisidiaProperty>) properties.values.clone();
	}
	
	/**
	 * Write object.
	 * 
	 * @param out the out
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}
	
	/**
	 * Read object.
	 * 
	 * @param in the in
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException the class not found exception
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.concurrentObject = new Object();
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		PropertyTable prop = null;
		try {
			prop = (PropertyTable) super.clone();
			prop.lockOwner = null;
			prop.concurrentObject = new Object();
			prop.defaults = (Hashtable<Object, VisidiaProperty>) this.defaults.clone();
			prop.values = (Hashtable<Object, VisidiaProperty>) this.values.clone();
		} catch (CloneNotSupportedException e) {
			System.out.println(e.getMessage());
		}
		
		return prop;
	}
	
	/**
	 * Returns a Set view of mappings contained in this property table.
	 * 
	 * @return the set view of the mappings contains in this table
	 */
	public Set<Map.Entry<Object, VisidiaProperty>> entrySet() {
		return ((Hashtable<Object, VisidiaProperty>) values.clone()).entrySet();
	}
	
	/**
	 * Returns the property associated with the key. If the key can't be found, returns null.
	 * 
	 * @param key the key
	 * 
	 * @return the property
	 */
	public VisidiaProperty getVisidiaProperty(Object key) {
		if (this.values.containsKey(key)) {
			synchronized (concurrentObject) {
				VisidiaProperty prop = this.values.get(key);
				if(prop == null) return null;
				return (prop.equals(nullProperty) ? null : prop);
			}
		} else if (this.defaults.containsKey(key)) {
			return this.defaults.get(key);
		} else {
			return null;
		}
	}
	
	/**
	 * Returns the value associated with the key. If the key can't be found, returns null.
	 * 
	 * @param key the key
	 * 
	 * @return the property
	 */
	public Object getValueOf(Object key){
		
		if (this.getVisidiaProperty(key) == null) 
			return null;
		
		return this.getVisidiaProperty(key).getValue();
	}
	

	/**
	 * Sets a property: a value and the corresponding key.
	 * 
	 * @param property the property
	 * 
     * @return     the previous property of the specified key in this hashtable,
     *             or <code>null</code> if it did not have one.
	 */
	public VisidiaProperty setVisidiaProperty(VisidiaProperty property) {
		synchronized (concurrentObject) {
			if (property == null) return null;
			Object key = property.getKey();
			return this.values.put(key, property);
		}
	}

	/**
	 * Sets a property: a value and the corresponding key.
	 * 
	 * @param key the key to add
	 * @param value the value associated with the key
	 * @param tag the VisidiaProperty.Tag value
	 * 
	 */
	public void setValue(Object key, Object value, int tag){
		VisidiaProperty property = new VisidiaProperty(key, value, tag);
		this.setVisidiaProperty(property);
	}
	
	/**
	 * Sets a property: a value and the corresponding key.
	 * 
	 * @param key the key to add
	 * @param value the value associated with the key
	 * @param tag the VisidiaProperty.Tag value
	 * 
	 */
	public void setValue(Object key, Object value){
		VisidiaProperty property = new VisidiaProperty(key, value, VisidiaProperty.Tag.USER_PROPERTY);
		this.setVisidiaProperty(property);
	}
	
	/**
	 * Checks if the property associated with the key is persistent (it cannot be removed).
	 * 
	 * @param key the key
	 * 
	 * @return true, if persistent property (default is false)
	 */
	public boolean isPersistentProperty(Object key) {
		VisidiaProperty prop = getVisidiaProperty(key);
		if (prop == null) return false;
		if (prop.getTag() == VisidiaProperty.Tag.PERSISTENT_PROPERTY) return true;
		return false;
	}
	
	/**
	 * Removes the property.
	 * 
	 * @param key the key
	 * 
	 * @return the property
	 */
	public VisidiaProperty removeProperty(Object key) {
		synchronized (concurrentObject) {
			if (isPersistentProperty(key)) return null;
			return this.values.remove(key);
		}
	}

	/**
	 * Reset properties.
	 */
	public void resetProperties() {
		synchronized (concurrentObject) {
			values.clear();
		}
	}

	/**
	 * Gets the property keys.
	 * 
	 * @return the property keys
	 */
	public Set<Object> getPropertyKeys() {
		Hashtable<Object, VisidiaProperty> common = (Hashtable<Object, VisidiaProperty>) this.defaults.clone();
		synchronized (concurrentObject) {
			common.putAll(this.values);
		}

		return common.keySet();
	}

	/**
	 * Contains element.
	 * 
	 * @param key the key
	 * 
	 * @return true, if successful
	 */
	public boolean containsElement(Object key) {
		synchronized (concurrentObject) {
			return (this.values.containsKey(key) || this.defaults.containsKey(key));
		}
	}

	/**
	 * Gets the lock owner.
	 * 
	 * @return the lock owner
	 */
	public synchronized Object getLockOwner() {
		return lockOwner;
	}

	/**
	 * Locked.
	 * 
	 * @return true, if successful
	 */
	public synchronized boolean locked() {
		return (lockOwner != null);
	}
	
	/**
	 * Lock properties. If already locked by lockOwner itself, does nothing.
	 * If already locked by anyone else, wait until the owner unlocks them.
	 * 
	 * @param requester the requester
	 */
	public synchronized void lockProperties(Object requester) {
		if (this.lockOwner != requester) {
			while (this.lockOwner != null) {
				try {
					this.wait();
				} catch (InterruptedException e) {
					throw new SimulationAbortError(e);
				}
			}
			this.lockOwner = requester;
		}
	}

	/**
	 * Unlock properties only if lockOwner has locked the properties itself.
	 * 
	 * @param requester the requester
	 */
	public synchronized void unlockProperties(Object requester) {
		if (this.lockOwner != null && this.lockOwner == requester) {
			this.lockOwner = null;
			this.notifyAll();
		}
	}

}

