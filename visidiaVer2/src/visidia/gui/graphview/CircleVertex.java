package visidia.gui.graphview;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import visidia.misc.VisidiaSettings;
import visidia.misc.colorpalette.ColorPaletteManager;
import visidia.misc.property.VisidiaProperty;

// TODO: Auto-generated Javadoc
/**
 * CircleVertex is the class for representing and drawing a vertex as a circle.
 */
public class CircleVertex extends VertexView {

	private static final long serialVersionUID = 6347139128915572073L;

	/** The radius of the circle representing the vertex. */
	protected static final int RADIUS = 10;

	/**
	 * Instantiates a new circle vertex.
	 */
	public CircleVertex() {
		super();
	}

	/**
	 * Checks if the (x,y) screen point corresponds to the item.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * 
	 * @return true, if successful
	 * 
	 * @see visidia.gui.graphview.VertexView#containsPoint(int, int)
	 */
	@Override
	public boolean containsPoint(int x, int y) {
		int r = CircleVertex.RADIUS;
		return ((Math.abs(posx - x) <= r) && (Math.abs(posy - y) <= r));
	}

	/**
	 * Draw the vertex in a Graphics context.
	 * 
	 * @param g the Graphics context
	 * 
	 * @see visidia.gui.graphview.VertexView#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		if (this.state == ViewableItem.ItemState.DELETED) return;

		Graphics2D g2 = (Graphics2D) g;
		int d = 2 * CircleVertex.RADIUS;

		if (this.state == ViewableItem.ItemState.SELECTED || this.isSynchronized())
			g2.setColor(Color.red);
		else g2.setColor(Color.black);

		// outer part
		g2.setStroke(new BasicStroke(4));
		g2.drawOval(posx - CircleVertex.RADIUS, posy - CircleVertex.RADIUS, d, d);

		VisidiaSettings settings = VisidiaSettings.getInstance();
		boolean showVertexLabel = settings.getBoolean(VisidiaSettings.Constants.SHOW_VERTEX_LABEL);
		// label
		int textPosUnderVertex = posy + d + 8;
		if (showVertexLabel) {
			g.setColor(Color.blue);
			String label = new Integer(vertex.getId()).toString() + " , " + this.vertex.getLabel();
			g.drawString(label, posx - CircleVertex.RADIUS, textPosUnderVertex);
			textPosUnderVertex += 12;
		}

		try {
			// inner part
			String key = this.vertex.getLabel().substring(0, 1);
			//if (this.vertex.isSwitchedOn())
			g.setColor(ColorPaletteManager.getInstance().getColor(key));
			//else g.setColor(Color.lightGray);
		} catch (NullPointerException e) {
		}
		g.fillOval(posx - CircleVertex.RADIUS, posy - CircleVertex.RADIUS, d, d);

		// user-defined displayable properties
		boolean showProps = settings.getBoolean(VisidiaSettings.Constants.SHOW_DISPLAYED_PROPS);
		if (showProps){
			Vector<String> propToDisp = new Vector<String>();
			Set<Object> keys = vertex.getPropertyKeys();
			Iterator<Object> itr = keys.iterator();
			while (itr.hasNext()) {
				Object key = itr.next();
				VisidiaProperty prop = vertex.getVisidiaProperty(key);
				boolean displayed = (prop.getTag() == VisidiaProperty.Tag.DISPLAYED_PROPERTY);
				if (! displayed) continue;			
				Object value = prop.getValue();
				propToDisp.add(value.toString());
			}
			String msg = "";
			int n = propToDisp.size();
			for (int i = 0; i < n-1; i++) {
				msg += propToDisp.elementAt(i);
				msg += ", ";
			}
			if (n > 0) msg += propToDisp.elementAt(n-1);
			g.setColor(Color.black);
			g.drawString(msg, posx - CircleVertex.RADIUS, textPosUnderVertex);
		}
	}

	/**
	 * Gets the border size of vertex. It corresponds to the vertex radius.
	 * 
	 * @return the border size
	 */
	public double getBorderSize() {
		return RADIUS;
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
	 * 
	 * @see visidia.gui.graphview.VertexView#isInsideRegion(int, int, int, int)
	 */
	@Override
	public boolean isInsideRegion(int x1, int y1, int x2, int y2) {
		int r = CircleVertex.RADIUS;
		return ((x1 <= (posx - r)) && (y1 <= (posy - r))
				&& (x2 >= (posx + r)) && (y2 >= (posy + r)));
	}

}
