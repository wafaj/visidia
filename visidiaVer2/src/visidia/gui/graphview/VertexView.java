package visidia.gui.graphview;

import java.awt.Graphics;
import java.awt.Point;
import java.util.Enumeration;
import java.util.Vector;

import visidia.graph.Vertex;

// TODO: Auto-generated Javadoc
/**
 * VertexView is the abstract base class used as a prototype representing vertex visualization.
 */
public abstract class VertexView extends GraphItemView {

	private static final long serialVersionUID = -5853173187863525637L;

	/** The vertex associated to the view. */
	protected Vertex vertex;

	/** The x position of vertex on screen. */
	protected int posx;

	/** The y position of vertex on screen. */
	protected int posy;

	/** The views associated to incident edges. */
	private Vector<EdgeView> edges = null;

	/**
	 * Instantiates a new vertex view.
	 */
	VertexView() {
		vertex = null;
		posx = 0;
		posy = 0;
		edges = new Vector<EdgeView>(10, 10);
	}

	/**
	 * Returns a clone of this view. Makes a shallow copy of vertex.
	 * 
	 * @return the object
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		VertexView copy = (VertexView) super.clone();
		
		copy.vertex = null;
		copy.posx = 0;
		copy.posy = 0;
		copy.edges = new Vector<EdgeView>(10, 10);
		
		return copy;
	}

	/**
	 * Draw the vertex in a Graphics context.
	 * 
	 * @param g the Graphics context
	 */
	@Override
	public abstract void draw(Graphics g);
	
	/**
	 * Gets the border size of vertex. In the case of a circular vertex, border size corresponds to the vertex radius.
	 * 
	 * @return the border size
	 */
	public abstract double getBorderSize();
	
	/**
	 * Moves the vertex on screen.
	 * 
	 * @param dx the displacement along x axis
	 * @param dy the displacement along y axis
	 */
	public void move(int dx, int dy) {
		posx += dx;
		posy += dy;
	}

	/**
	 * Checks if the (x,y) screen point corresponds to the item.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * 
	 * @return true, if successful
	 */
	@Override
	public abstract boolean containsPoint(int x, int y);

	/**
	 * Checks if the item is inside the rectangle defined by points (x1,y1) and (x2,y2) on screen.
	 * 
	 * @param x1 the x coordinate of point 1
	 * @param y1 the y coordinate of point 1
	 * @param x2 the x coordinate of point 2
	 * @param y2 the y coordinate of point 2
	 * 
	 * @return true, if is inside region
	 */
	@Override
	public abstract boolean isInsideRegion(int x1, int y1, int x2, int y2);

	/**
	 * Sets the vertex position on screen.
	 * 
	 * @param posx the x position
	 * @param posy the y position
	 */
	public void setPosition(int posx, int posy) {
		this.vertex.setPos(posx,posy);
		this.posx = posx;
		this.posy = posy;
	}

	/**
	 * Gets the vertex associated to the view.
	 * 
	 * @return the vertex
	 */
	public Vertex getVertex() {
		return vertex;
	}

	/**
	 * Associates a vertex to the view.
	 * 
	 * @param vertex the new vertex
	 */
	public void setVertex(Vertex vertex) {
		this.vertex = vertex;
		this.vertex.setView(this);
	}

	/**
	 * Gets the vertex position on screen.
	 * 
	 * @return the position
	 */
	public Point getPosition() {
		return new Point(posx, posy);
	}


	/**
	 * Adds an incident edge view.
	 * 
	 * @param edge the edge view
	 */
	public void addEdge(EdgeView edge) {
		if (!edges.contains(edge)){
			edges.add(edge);
			if(edge.getOrigin().equals(this))
				this.getVertex().linkTo(edge.getDestination().getVertex(), edge.getEdge().isOriented());
		}
	}

	public boolean removeEdge(EdgeView edge){
		return edges.remove(edge);
	}
	/**
	 * Gets the views on incident edges.
	 * 
	 * @return the views
	 */
	public Enumeration<EdgeView> getEdges() {
		return edges.elements();
	}

	/**
	 * Clears the edges list.
	 */
	public void clearEdges() {
		edges.clear();
	}
}
