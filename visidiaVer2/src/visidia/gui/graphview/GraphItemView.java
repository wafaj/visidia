package visidia.gui.graphview;

import java.awt.Graphics;

// TODO: Auto-generated Javadoc
/**
 * GraphItemView is the abstract base class used as a prototype representing visualization of graph items (e.g. edges, vertices).
 */
public abstract class GraphItemView extends ViewableItem {

	private static final long serialVersionUID = 5488703793639392124L;

	/**
	 * Returns a clone of this view.
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
	 * Checks if the (x,y) screen point corresponds to the item.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * 
	 * @return true, if successful
	 */
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
	public abstract boolean isInsideRegion(int x1, int y1, int x2, int y2);

	/**
	 * Abstract method to draw the graph item in a Graphics context.
	 * 
	 * @param g the Graphics context
	 * 
	 * @see visidia.gui.graphview.ViewableItem#draw(java.awt.Graphics)
	 */
	@Override
	public abstract void draw(Graphics g);

}
