package visidia.misc.property;

import java.io.IOException;
import java.io.Serializable;

import visidia.simulation.SimulationConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class VisidiaProperty.
 */
public class VisidiaProperty implements Serializable, Cloneable {

	private static final long serialVersionUID = -3012974689210207673L;

	/**
	 * The Class Tag.
	 */
	public class Tag {
		
		/** The Constant DISPLAYABLE_PROPERTY. */
		public final static int DISPLAYABLE_PROPERTY = 1; // displayable and not displayed
		
		/** The Constant DISPLAYED_PROPERTY. */
		public final static int DISPLAYED_PROPERTY = 2; // displayable and displayed
		
		/** The Constant USER_PROPERTY. */
		public final static int USER_PROPERTY = 3; // property defined by user (and not displayable)
		
		/** The Constant PERSISTENT_PROPERTY. */
		public final static int PERSISTENT_PROPERTY = 4; // property defined by ViSiDiA (and not displayable)
	}
	
	/** The key. */
	private Object key;
	
	/** The value. */
	private Object value;
	
	/** The tag. */
	private int tag;
	
	/**
	 * Instantiates a new visidia property.
	 */
	VisidiaProperty() {
		this.key = new Object();
		this.value = new Object();
		this.tag = 0;
	}
	
	/**
	 * Instantiates a new visidia property.
	 *
	 * @param key the key
	 * @param value the value
	 * @param tag the tag
	 */
	public VisidiaProperty(Object key, Object value, int tag) {
		this.key = key;
		this.value = value;
		this.tag = tag;
	}

	/**
	 * Gets the key.
	 *
	 * @return the key
	 */
	public Object getKey() {
		return key;
	}
	
	/**
	 * Gets the value.
	 *
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}
	
	/**
	 * Gets the tag.
	 *
	 * @return the tag
	 */
	public int getTag() {
		return tag;
	}

	/**
	 * Write object.
	 *
	 * @param out the out
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(key);
		out.writeObject(value);
		out.writeInt(tag);
	}

	/**
	 * Read object.
	 *
	 * @param in the in
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException the class not found exception
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		key = (String) in.readObject();
		value = in.readObject();
		tag = in.readInt();
	}

	/**
	 * Gets the property tag from status.
	 *
	 * @param status the status
	 * @return the property tag from status
	 */
	public static int getPropertyTagFromStatus(int status) {
		int tag = VisidiaProperty.Tag.USER_PROPERTY;
		if (status == SimulationConstants.PropertyStatus.DISPLAYABLE) tag = VisidiaProperty.Tag.DISPLAYABLE_PROPERTY;
		else if (status == SimulationConstants.PropertyStatus.DISPLAYED) tag = VisidiaProperty.Tag.DISPLAYED_PROPERTY;
		
		return tag;
	}
	
}
