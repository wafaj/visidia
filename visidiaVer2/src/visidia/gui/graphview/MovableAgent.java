package visidia.gui.graphview;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import visidia.misc.ImageHandler;
import visidia.simulation.evtack.VisidiaEvent;
import visidia.simulation.process.agent.Agent;

// TODO: Auto-generated Javadoc
/**
 * A MovableAgent is an item containing a agent and moving along a straight line.
 */
public class MovableAgent extends MovableItem {

	private static final long serialVersionUID = 655228866007920688L;

	/** The agent. */
	private Agent agent;
	
	// TRICKY: step is a single element array, as it is an easy way to pass the step value by reference
	/**
	 * Instantiates a new movable agent.
	 * 
	 * @param agent the agent
	 * @param a the trajectory start point
	 * @param b the trajectory end point
	 * @param step the moving step
	 * @param event the event
	 */
	public MovableAgent(Agent agent, Point a, Point b, int[] step, VisidiaEvent event) {
		super(a, b, step, event);
		this.agent = agent;
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
		boolean moveForward = (ax < bx);
		
		String iconFilename = (moveForward ? "/man-mirror" : "/man");
		iconFilename += (frozen ? ".png" : ".gif");
		Image img = ImageHandler.getInstance().createImageIcon(iconFilename).getImage();

		int imgHeight = img.getHeight(null);
		int imgWidth = img.getWidth(null);
		int stringSize = (int) (g.getFontMetrics().getStringBounds(this.agent.toString(), g).getWidth());

		//MessageType messageType = this.event.message().getType();
		//if (messageType.getToPaint()) {
			//if ((this.event.message()).getVisualization()) {
				Point p = this.currentLocation();
				//g.setColor(messageType.getColor());
				g.setColor(Color.black);
				g.drawString(this.agent.toString(), p.x - (stringSize / 2), p.y + (imgHeight / 2));
				g.drawImage(img, p.x - (imgWidth / 2), p.y - (imgHeight / 2), null, null);
			//}
		//}
	}

}
