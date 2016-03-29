package visidia.gui.graphview;

import java.awt.Graphics;
import java.awt.Point;

import visidia.graph.Edge;

// TODO: Auto-generated Javadoc
/**
 * EdgeView is the abstract base class used as a prototype representing edge visualization.
 */
public abstract class EdgeView extends GraphItemView {

	private static final long serialVersionUID = -4873146302574089953L;

	/** The edge associated to the view. */
	private Edge edge;

	/** The origin vertex. */
	protected VertexView origin;

	/** The destination vertex. */
	protected VertexView destination;

	/**
	 * Instantiates a new edge view.
	 */
	EdgeView() {
		edge = null;
		origin = null;
		destination = null;
	}

	/**
	 * Returns a clone of this view. Makes a shallow copy of edge.
	 * 
	 * @return the object
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		return super.clone();
	}

	/**
	 * Draw the edge in a Graphics context.
	 * 
	 * @param g the Graphics context
	 */
	@Override
	public abstract void draw(Graphics g);

	/**
	 * Checks if the (x,y) screen point corresponds to the item.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * 
	 * @return true, if successful
	 */
	@Override
	public boolean containsPoint(int x, int y) {
		// Static variable for the click precision
		double precision = 3;

		testIfSwitchedEdge();
		Point originPos = origin.getPosition();
		Point destinationPos = destination.getPosition();
		double origx = originPos.getX();
		double origy = originPos.getY();
		double destx = destinationPos.getX();
		double desty = destinationPos.getY();

		double scalar = (x - origx) * (destx - origx) + (y - origy) * (desty - origy);
		double squareLength = Math.pow(destx - origx, 2) + Math.pow(desty - origy, 2);

		if (scalar > 0) {
			double squareDistance = ((Math.pow(x - origx, 2) + Math.pow(y - origy, 2))
					* squareLength - Math.pow(scalar, 2)) / squareLength;
			return ((Math.pow(scalar, 2) < Math.pow(squareLength, 2)) && (squareDistance < Math.pow(precision, 2)));
		}		

		return false;
	}

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
	public boolean isInsideRegion(int x1, int y1, int x2, int y2) {
		testIfSwitchedEdge();
		Point originPos = origin.getPosition();
		Point destinationPos = destination.getPosition();
		double origx = originPos.getX();
		double origy = originPos.getY();
		double destx = destinationPos.getX();
		double desty = destinationPos.getY();

		return ((x1 <= origx) && (y1 <= origy) && (x2 >= destx)
				&& (y2 >= desty) && (x1 <= destx)
				&& (y1 <= desty) && (x2 >= origx) && (y2 >= origy));
	}

	/**
	 * Test if the origin and destination vertices of edge has been switched directly on graph, and not through graphview.
	 * If true, also switch the views.
	 */
	private void testIfSwitchedEdge() {
		if (origin.vertex.equals(edge.getDestination()) && destination.vertex.equals(edge.getOrigin())) {
			VertexView v = origin;
			origin = destination;
			destination = v;			
		}
	}

	/**
	 * Switch origin and destination vertices.
	 */
	public void switchOriginAndDestination() {
		VertexView v = origin;
		origin = destination;
		destination = v;
		edge.switchOriginAndDestination();
	}

	/**
	 * Associates an edge to the view.
	 * 
	 * @param edge the new edge
	 */
	public void setEdge(Edge edge) {
		this.edge = edge;
	}

	/**
	 * Gets the edge associated to the view.
	 * 
	 * @return the edge
	 */
	public Edge getEdge() {
		return edge;
	}

	/**
	 * Gets the origin vertex.
	 * 
	 * @return the origin
	 */
	public VertexView getOrigin() {
		testIfSwitchedEdge();
		return origin;
	}

	/**
	 * Sets the origin vertex.
	 * 
	 * @param v the new origin
	 */
	public void setOrigin(VertexView v) {
		origin = v;
	}

	/**
	 * Gets the destination vertex.
	 * 
	 * @return the destination
	 */
	public VertexView getDestination() {
		testIfSwitchedEdge();
		return destination;
	}

	/**
	 * Sets the destination vertex.
	 * 
	 * @param v the new destination
	 */
	public void setDestination(VertexView v) {
		destination = v;
	}

}
