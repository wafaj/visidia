package visidia.gui.graphview;

import java.awt.Graphics;
import java.awt.Point;

import visidia.simulation.evtack.VisidiaEvent;

// TODO: Auto-generated Javadoc
/**
 * MovableItem is the abstract base class for items which can be part of a graphics animation.
 * A movable item trajectory is a straight line.
 */
public abstract class MovableItem extends ViewableItem {

	private static final long serialVersionUID = -7435622685898555817L;

	/** The x coordinate of the trajectory start point. */
	double ax;

	/** The y coordinate of the trajectory start point. */
	double ay;

	/** The x coordinate of the trajectory end point. */
	double bx;

	/** The y coordinate of the trajectory end point. */
	double by;

	/** The x coordinate of item current position. */
	private double x;

	/** The y coordinate of item current position. */
	private double y;

	// TRICKY: step is a single element array, as it is an easy way to pass the step value by reference
	/** The moving step. */
	private int[] step;

	/** The x coordinate of unit vector. */
	double ux;

	/** The y coordinate of unit vector. */
	double uy;
	
	/** The event. */
	private VisidiaEvent event;
	
	/** Indicated if the item is frozen (not animated). */
	protected boolean frozen = false;
	
	/**
	 * Instantiates a new movable item.
	 * 
	 * @param a the trajectory start point
	 * @param b the trajectory end point
	 * @param event the event
	 */
	protected MovableItem(Point a, Point b, VisidiaEvent event) {
		this(a, b, null, event);
	}

	// TRICKY: step is a single element array, as it is an easy way to pass the step value by reference
	/**
	 * Instantiates a new movable item.
	 * 
	 * @param a the trajectory start point
	 * @param b the trajectory end point
	 * @param step the moving step
	 * @param event the event
	 */
	protected MovableItem(Point a, Point b, int[] step, VisidiaEvent event) {
		setTrajectory(a, b, step);
		this.event = event;
	}

	/**
	 * Gets the event.
	 * 
	 * @return the event
	 */
	public VisidiaEvent getEvent() {
		return event;
	}

	/**
	 * Sets the event.
	 * 
	 * @param event the new event
	 */
	public void setEvent(VisidiaEvent event) {
		this.event = event;
	}
	
	/**
	 * Sets the trajectory.
	 * 
	 * @param a the a
	 * @param b the b
	 * @param step the step
	 */
	public void setTrajectory(Point a, Point b, int[] step) {
		if (step == null) {
			this.step = new int[1];
			this.step[0] = 1;
		} else this.step = step;

		this.ax = a.getX();
		this.ay = a.getY();
		this.bx = b.getX();
		this.by = b.getY();

		this.x = this.ax;
		this.y = this.ay;

		double distance = a.distance(b);
		this.ux = (this.bx - this.ax) / distance;
		this.uy = (this.by - this.ay) / distance;
	}
	
	/**
	 * Gets the current location of item.
	 * 
	 * @return the point
	 */
	public Point currentLocation() {
		Point p = new Point();
		p.setLocation(this.x, this.y);
		return p;
	}

	/**
	 * Freeze.
	 */
	public void freeze() {
		frozen = true;
	}
	
	/**
	 * Moves forward the item.
	 */
	public void moveForward() {
		frozen = false;
		this.x += this.step[0] * this.ux;
		this.y += this.step[0] * this.uy;
	}

	/**
	 * Moves backward the item.
	 */
	public void moveBackward() {
		frozen = false;
		this.x -= this.step[0] * this.ux;
		this.y -= this.step[0] * this.uy;
	}

	/**
	 * Determines if the item is on the trajectory, between start and end points.
	 * 
	 * @return true, if is into bounds
	 */
	// TRICKY: A point M  is located into the segment AB if the value of the vector scalar product MA.MB is positif.
	public boolean isIntoBounds() {
		return ((this.x - this.ax) * (this.x - this.bx) + (this.y - this.ay) * (this.y - this.by)) <= 0;
	}

	/**
	 * Resets the item position to start point.
	 */
	public void reset() {
		this.x = this.ax;
		this.y = this.ay;
	}

	/**
	 * Put the object to its end position.
	 */
	public void end() {
		this.x = this.bx;
		this.y = this.by;
	}

	/**
	 * Abstract method to draw the viewable item in a Graphics context.
	 * 
	 * @param g the Graphics context
	 */
	public abstract void draw(Graphics g);

}
