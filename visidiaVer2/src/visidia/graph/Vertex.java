package visidia.graph;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import java.awt.Point;

import visidia.misc.ColorLabel;
import visidia.misc.VisidiaSettings;
import visidia.misc.colorpalette.ColorPaletteManager;
import visidia.misc.property.PropertyTable;
import visidia.misc.property.VisidiaProperty;
import visidia.gui.graphview.VertexView;

// TODO: Auto-generated Javadoc
/**
 * The Class Vertex represents a graph vertex by a unique identifier. A vertex knows both its neighbors and its incident edges.
 * There is at most one edge between two vertices.
 * 
 * A vertex can have a set of properties, stored in a whiteboard (property table).
 */
public class Vertex extends PropertyTable {

	private static final long serialVersionUID = 5912640963251914418L;

	/** The Constant PROPERTY_LABEL_TEXT. */
	transient private static final String PROPERTY_LABEL_TEXT = "label";

	/** The neighbors. */
	protected Vector<Vertex> neighbors;

	/** The incident edges. */
	protected Vector<Edge> edges;

	/** The vertex id. */
	private int id;

	/** The switched on label. */
	private String switchedOnLabel = ""; // used when switching a vertex on; stores the last label before "Switched off"

	/** The color palette manager. */
	transient private ColorPaletteManager colorPaletteManager = ColorPaletteManager.getInstance();

	/** The VertexView associated with a vertex */
	private VertexView view;

	private int posX;
	private int posY;

	public void setView(VertexView v){
		this.view = v;
	}

	public VertexView getView(){
		return this.view;
	}

	public Point getPos(){
		return new Point(posX,posY);
	}

	public void setPos(int posX,int posY){
		this.posX = posX;
		this.posY = posY;
	}

	/**
	 * Instantiates a new vertex.
	 * 
	 * @param id the vertex id
	 */
	protected Vertex(int id) {
		super(null, new Hashtable<Object, VisidiaProperty>());
		this.id = id;
		this.neighbors = new Vector<Vertex>(10, 10);
		this.edges = new Vector<Edge>(10, 10);
		VisidiaSettings settings = VisidiaSettings.getInstance();
		ColorLabel cl = (ColorLabel) settings.getObject(VisidiaSettings.Constants.VERTEX_DEFAULT_LABEL);
		setLabel(cl.getLabel());
	}

	/**
	 * Links to another vertex (create an edge).
	 * 
	 * @param v the vertex to be linked to.
	 * @param oriented the oriented
	 * 
	 * @return the created edge
	 */
	public Edge linkTo(Vertex v, boolean oriented) {
		if (v.equals(this)) return null;
		Edge edge;
		if((edge=this.getEdge(v))!=null){
			if(edge.isOriented() && edge.getOrigin().getId()==v.getId())
				edge.setOriented(false);
			return null;
		}
		edge = new Edge(this, v, oriented);
		if (!neighbors.contains(v)) neighbors.add(v);
		if (!edges.contains(edge)) edges.add(edge);
		if (!v.neighbors.contains(this)) v.neighbors.add(this);
		if (!v.edges.contains(edge)) v.edges.add(edge);
		return edge;
	}

	/**
	 * Gets the edge incident to vertex v and this.
	 * 
	 * @param v the vertex
	 * 
	 * @return the incident edge, or null if the vertices are not connected
	 */
	public Edge getEdge(Vertex v) {
		Iterator<Edge> itr = edges.iterator();
		while(itr.hasNext()) {
			Edge edge = itr.next();
			if (edge.isConnectedTo(this) && edge.isConnectedTo(v))
				return edge;
		}
		return null;
	}

	/**
	 * Unlinks this vertex and vertex v.
	 * 
	 * @param v a vertex.
	 */
	public void unlink(Vertex v) {
		Edge edge = getEdge(v); // find the edge incident to both v and this
		if (edge != null) {
			edges.remove(edge);		
			v.edges.remove(edge);	
			edge = null;
		}
		neighbors.remove(v);
		v.neighbors.remove(this);
	}

	/**
	 * Removes the vertex and its incident edges.
	 */
	void remove() {
		neighbors.clear();
		removeIncidentEdges();
		id = -1;
	}

	/**
	 * Gets the neighbors.
	 * 
	 * @return the neighbors
	 */
	public Enumeration<Vertex> getNeighbors() {
		return neighbors.elements();
	}

	/**
	 * Gets the neighbor associated to the door.
	 * 
	 * @param door the door
	 * 
	 * @return the neighbor
	 */
	public Vertex getNeighborByDoor(int door) {
		if (door >= neighbors.size()) return null;
		return neighbors.elementAt(door);
	}

	/**
	 * Gets the door through which this vertex goes to v.
	 * 
	 * @param v the v
	 * 
	 * @return the door, or -1 if the two vertices are not connected
	 */
	public int getDoorTo(Vertex v) {
		for (int i = 0; i < neighbors.size(); i++) {
			if (neighbors.elementAt(i).equals(v))
				return i;
		}

		return -1;
	}

	/**
	 * Gets the incident edges.
	 * 
	 * @return the incident edges
	 */
	public Enumeration<Edge> getEdges() {
		return edges.elements();
	}

	/**
	 * Gets the vertex id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Sets the vertex id.
	 * 
	 * @param id the new id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Computes the vertex degree as the number of its neighbors.
	 * 
	 * @return the degree
	 */
	public int degree() {
		return neighbors.size();
	}
	
	/**
	 * Computes the vertex degree as the number of its neighbors.
	 * Same as degree()
	 * 
	 * @return the degree
	 */
	public int getDegree() {
		return neighbors.size();
	}

	/**
	 * Removes the incident edges.
	 */
	public void removeIncidentEdges() {
		Iterator<Edge> itr = edges.iterator();
		while(itr.hasNext()) {
			Edge edge = itr.next();
			if (edge != null) {
				Vertex v = edge.getDestination(); 
				if (v.equals(this)) v = edge.getOrigin();
				v.edges.remove(edge);
				v.neighbors.remove(this);
				neighbors.remove(v);
			}
		}
		edges.clear();
	}

	/**
	 * Merges vertex v to this.
	 * 
	 * @param v the vertex
	 */
	public void merge(Vertex v) {
		if (v.equals(this)) return;
		Enumeration<Edge> vEdges = v.getEdges();
		while (vEdges.hasMoreElements()) {
			Edge edge = vEdges.nextElement();
			if (edge.getOrigin().equals(v)) {
				this.linkTo(edge.getDestination(), edge.isOriented());
			} else if (edge.getDestination().equals(v)) {
				edge.getOrigin().linkTo(this, edge.isOriented());
			}
		}
		//v = this;
	}

	/**
	 * Returns a clone of this vertex (shallow copy).
	 * 
	 * @return a clone of this vertex
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		Vertex copy = (Vertex) super.clone();
		copy.id = this.id;
		copy.neighbors = (Vector<Vertex>) this.neighbors.clone();
		copy.edges = (Vector<Edge>) this.edges.clone();

		return copy;
	}

	/**
	 * Reset properties.
	 */
	public void resetProperties() {
		super.resetProperties();
		VisidiaSettings settings = VisidiaSettings.getInstance();
		ColorLabel cl = (ColorLabel) settings.getObject(VisidiaSettings.Constants.VERTEX_DEFAULT_LABEL);
		setLabel(cl.getLabel());
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
			else if (value instanceof ColorLabel) setLabel(((ColorLabel) value).getLabel());
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
		if (prop == null) return null;
		ColorLabel c = (ColorLabel) prop.getValue();
		return (c == null ? null : c.getLabel());
	}

	/**
	 * Sets the label.
	 * 
	 * @param label the new label
	 */
	public void setLabel(String label) {
		if (label.equals("Switched off")) setSwitchedOn(false);
		else {
			String s = getLabel();
			if (s != null && s.equals("Switched off")) setSwitchedOn(true);
			else {
				VisidiaProperty prop = new VisidiaProperty(PROPERTY_LABEL_TEXT, new ColorLabel(colorPaletteManager.getColor(label), label), VisidiaProperty.Tag.PERSISTENT_PROPERTY);
				super.setVisidiaProperty(prop);
				switchedOnLabel = label;
			}
		}
	}

	/**
	 * Checks if is switched on.
	 * 
	 * @return true, if is switched on
	 */
	public boolean isSwitchedOn() {
		return !getLabel().equals("Switched off");
	}

	/**
	 * Switches the vertex on/off.
	 * 
	 * @param switchedOn the switched on
	 */
	public void setSwitchedOn(boolean switchedOn) {
		if (switchedOn) {
			VisidiaProperty prop = new VisidiaProperty(PROPERTY_LABEL_TEXT, new ColorLabel(colorPaletteManager.getColor(switchedOnLabel), switchedOnLabel), VisidiaProperty.Tag.PERSISTENT_PROPERTY);
			super.setVisidiaProperty(prop);
		} else {
			String s = getLabel();
			if (! s.equals("Switched off"))	switchedOnLabel = s;
			VisidiaProperty prop = new VisidiaProperty(PROPERTY_LABEL_TEXT, new ColorLabel(colorPaletteManager.getColor("Switched off"), "Switched off"), VisidiaProperty.Tag.PERSISTENT_PROPERTY);
			super.setVisidiaProperty(prop);
		}
	}

}
