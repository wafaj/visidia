package visidia.gui.graphview;

import java.awt.Graphics;
import java.awt.Point;

import visidia.gui.graphview.MovableItem;
import visidia.simulation.evtack.VisidiaEvent;

// TODO: Auto-generated Javadoc
/**
 * A MovableSensor is an item containing a sensor and moving along a straight line.
 */
public class MovableSensor extends MovableItem {

	private static final long serialVersionUID = 688258446827720680L;

	/** The sensor view. */
	private VertexView sensorView;

	/** The trajectory start point. */
	Point start;

	/** The trajectory end point. */
	Point end;

	/**
	 * for dealing with the action made on moving a sensor.
	 * 
	 * @param sensorView the sensor view
	 * @param a the trajectory start point
	 * @param b the trajectory end point
	 * @param step the step
	 * @param event the event
	 */
	public MovableSensor(VertexView sensorView, Point a, Point b, int[] step, VisidiaEvent event) {
		super(a, b, step, event);
		this.sensorView = sensorView;
		this.start = a;
		this.end = b;
	}

	/* (non-Javadoc)
	 * @see visidia.gui.graphview.MovableItem#moveForward()
	 */
	public void moveForward() {
		super.moveForward(); // compute the next position
		Point p = this.currentLocation();
		if (start.distance(p) > start.distance(end)) { 
			p.x = end.x;
			p.y = end.y;
		}
		sensorView.setPosition(p.x, p.y);
	}

	/* (non-Javadoc)
	 * @see visidia.gui.graphview.MovableItem#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		sensorView.draw(g);
	}

}
