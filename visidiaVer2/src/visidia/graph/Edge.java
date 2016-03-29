package visidia.graph;

import java.util.Hashtable;

import visidia.misc.property.PropertyTable;
import visidia.misc.property.VisidiaProperty;

// TODO: Auto-generated Javadoc
/**
 * The Class Edge represents a graph edge as a link between two vertices.
 */
public class Edge extends PropertyTable {

	private static final long serialVersionUID = 510297057659565688L;

	/** The Constant PROPERTY_LABEL_TEXT. */
	transient private static final String PROPERTY_LABEL_TEXT = "label";
	
	/** The Constant PROPERTY_WEIGHT_TEXT. */
	transient private static final String PROPERTY_WEIGHT_TEXT = "weight";
	
	/** The Constant PROPERTY_ORIENTED_TEXT. */
	transient private static final String PROPERTY_ORIENTED_TEXT = "oriented";

	/** The edge origin. */
	private Vertex origin;
	
	/** The edge destination. */
	private Vertex destination;
	
	/**
	 * Instantiates a new edge.
	 * 
	 * @param origin the origin
	 * @param destination the destination
	 * @param oriented the orientation status
	 */
	Edge(Vertex origin, Vertex destination, boolean oriented) {
		super(null, new Hashtable<Object, VisidiaProperty>());
		this.origin = origin;
		this.destination = destination;
		setOriented(oriented);
		setLabel("");
		setWeight(1);
	}

	/* (non-Javadoc)
	 * @see visidia.misc.property.PropertyTable#clone()
	 */
	public Object clone() {
		Edge copy = (Edge) super.clone();
		copy.origin = origin;
		copy.destination = destination;
		copy.setOriented(isOriented());
		copy.setLabel(getLabel());
		copy.setWeight(getWeight());
		
		return copy;
	}
	
	/**
	 * Switch origin and destination vertices.
	 */
	public void switchOriginAndDestination() {
		Vertex v = origin;
		origin = destination;
		destination = v;
	}
	
	/**
	 * Checks if this edge is connected to the given vertex.
	 * 
	 * @param v the vertex to test connection with.
	 * 
	 * @return true, if this edge is connected to v
	 */
	public boolean isConnectedTo(Vertex v) {
		return (origin.equals(v) || destination.equals(v));
	}
	
	/**
	 * Removes the edge, specifying to its origin and destination that they are unlinked.
	 */
	public void remove() {
		origin.unlink(destination);
	}

	/**
	 * Gets the origin.
	 * 
	 * @return the origin
	 */
	public Vertex getOrigin() {
		return origin;
	}
	
	/**
	 * Sets the origin.
	 * 
	 * @param the origin
	 */
	public void setOrigin(Vertex origin) {
		this.origin = origin;
	}

	/**
	 * Gets the destination.
	 * 
	 * @return the destination
	 */
	public Vertex getDestination() {
		return destination;
	}

	/**
	 * Sets the destination.
	 * 
	 * @param the destination
	 */
	public void setDestination(Vertex destination) {
		this.destination = destination;
	}
	
	/* (non-Javadoc)
	 * @see visidia.misc.property.PropertyTable#resetProperties()
	 */
	public void resetProperties() {
		Boolean b = isOriented();
		super.resetProperties();
		setOriented(b);
		setLabel("");
		setWeight(1);
	}
	
	/**
	 * Sets a property.
	 * 
	 * @param property the property
	 * 
     * @return     the previous property value of the specified key in this hashtable,
     *             or <code>null</code> if it did not have one.
	 */
	public Object setProperty(VisidiaProperty property) {
		Object key = property.getKey();
		Object value = property.getValue();
		if (key.equals(PROPERTY_LABEL_TEXT)) {
			VisidiaProperty prop = super.getVisidiaProperty(key);
			if (value instanceof String) setLabel((String) value);
			return prop;
		} else if (key.equals(PROPERTY_WEIGHT_TEXT)) {
			VisidiaProperty prop = super.getVisidiaProperty(key);
			if (value instanceof Double) setWeight(((Double) value).doubleValue());
			return prop;
		} else if (key.equals(PROPERTY_ORIENTED_TEXT)) {
			VisidiaProperty prop = super.getVisidiaProperty(key);
			if (value instanceof Boolean) setOriented(((Boolean) value).booleanValue());
			return prop;
		}

		VisidiaProperty prop = super.setVisidiaProperty(property);
		return (prop == null ? null : prop.getValue());	
	}
	
	/**
	 * Gets the label.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		VisidiaProperty prop = super.getVisidiaProperty(PROPERTY_LABEL_TEXT);
		return (prop == null) ? null : (String) prop.getValue();
	}

	/**
	 * Sets the label.
	 * 
	 * @param s the new label
	 */
	public void setLabel(String s) {
		super.setVisidiaProperty(new VisidiaProperty(PROPERTY_LABEL_TEXT, s, VisidiaProperty.Tag.PERSISTENT_PROPERTY));
	}

	/**
	 * Gets the weight.
	 * 
	 * @return the weight
	 */
	public Double getWeight() {
		VisidiaProperty prop = super.getVisidiaProperty(PROPERTY_WEIGHT_TEXT);
		return (prop == null) ? null : (Double) prop.getValue();
	}
	
	/**
	 * Sets the weight.
	 * 
	 * @param weight the new weight
	 */
	public void setWeight(double weight) {
		super.setVisidiaProperty(new VisidiaProperty(PROPERTY_WEIGHT_TEXT, new Double(weight), VisidiaProperty.Tag.PERSISTENT_PROPERTY));
	}

	/**
	 * Checks if the edge oriented.
	 * 
	 * @return true, if it is oriented
	 */
	public Boolean isOriented() {
		VisidiaProperty prop = super.getVisidiaProperty(PROPERTY_ORIENTED_TEXT);
		return (prop == null) ? null : (Boolean) prop.getValue();
	}

	/**
	 * Sets the orientation status.
	 * 
	 * @param oriented the new orientation status
	 */
	public void setOriented(boolean oriented) {
		super.setVisidiaProperty(new VisidiaProperty(PROPERTY_ORIENTED_TEXT, new Boolean(oriented), VisidiaProperty.Tag.PERSISTENT_PROPERTY));
	}

}
