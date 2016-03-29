package visidia.gui.graphview;

import java.awt.Graphics;
import java.awt.Point;

import visidia.simulation.evtack.VisidiaEvent;
import visidia.simulation.process.messages.Message;
import visidia.simulation.process.messages.MessageType;

// TODO: Auto-generated Javadoc
/**
 * A MovableMessage is an item containing a message and moving along a straight line.
 */
public class MovableMessage extends MovableItem {

	private static final long serialVersionUID = 2160247350165311320L;

	/** The message. */
	private Message msg;

	// TRICKY: step is a single element array, as it is an easy way to pass the step value by reference
	/**
	 * Instantiates a new movable message.
	 * 
	 * @param msg the message
	 * @param a the trajectory start point
	 * @param b the trajectory end point
	 * @param step the moving step
	 * @param event the event
	 */
	public MovableMessage(Message msg, Point a, Point b, int[] step, VisidiaEvent event) {
		super(a, b, step, event);
		this.msg = msg;
	}

	/**
	 * Draws the item in a Graphics context.
	 * 
	 * @param g the Graphics context
	 * 
	 * @see visidia.gui.graphview.MovableItem#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		MessageType messageType = this.msg.getType();
		if (messageType.getToPaint()) {

			if (this.msg.getVisualization()) {
				Point p = this.currentLocation();
				g.setColor(messageType.getColor());
				g.drawString(this.msg.toString(), p.x, p.y);
			}
		}
	}

}
