package visidia.gui.graphview;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import visidia.graph.Edge;
import visidia.misc.VisidiaSettings;
import visidia.misc.property.VisidiaProperty;

// TODO: Auto-generated Javadoc
/**
 * SegmentEdge is the class for representing and drawing an edge as a segment line.
 */
public class SegmentEdge extends EdgeView {

	private static final long serialVersionUID = 2426045296606907468L;

	/**
	 * Instantiates a new segment edge.
	 */
	public SegmentEdge() {
		super();
	}

	/**
	 * Draw the edge in a Graphics context.
	 * 
	 * @param g the Graphics context
	 * 
	 * @see visidia.gui.graphview.EdgeView#draw(java.awt.Graphics)
	 */
	@Override
	public void draw(Graphics g) {
		if (this.state == ViewableItem.ItemState.DELETED) return;

		Point originPos = origin.getPosition();
		Point destinationPos = destination.getPosition();
		Color initialColor = g.getColor();
		Graphics2D g2 = (Graphics2D) g;

		int stroke = (this.isMarked()) ? 3 : 0;

		g2.setColor(g.getColor());
		g2.setStroke(new BasicStroke(1+stroke));
		
		if (this.state == ViewableItem.ItemState.SELECTED || this.isSynchronized()) {
			g2.setColor(Color.red);
			g2.setStroke(new BasicStroke(3+stroke));
		}

		double origX = originPos.getX();
		double origY = originPos.getY();
		double destX = destinationPos.getX();
		double destY = destinationPos.getY();

		double dist = Math.sqrt((destX-origX)*(destX-origX) + (destY-origY)*(destY-origY));
		
		
		
		// orientation
		Edge edge = this.getEdge();
		
		if (edge != null && edge.isOriented()) {
			double border = this.getOrigin().getBorderSize();
			double u = 1 - border / dist;
			destX = origX + u * (destX-origX);
			destY = origY + u * (destY-origY);
			
			double theta = Math.atan((destY-origY)/(destX-origX));
			if (origX > destX) theta += Math.PI;
			double alpha = destX;
			double beta = destY;

			AffineTransform affineTransform = new AffineTransform();
			affineTransform.setToRotation(theta);
			Polygon triangle = new Polygon(new int[] {-10,-10,0}, new int[] {-5,5,0}, 3);
			Shape arrow = affineTransform.createTransformedShape(triangle);
			affineTransform.setToTranslation(alpha, beta);
			
			g2.draw(affineTransform.createTransformedShape(arrow));
			g2.fill(affineTransform.createTransformedShape(arrow));
		}else if(edge != null && VisidiaSettings.getInstance().getBoolean(VisidiaSettings.Constants.DIRECTED_GRAPH)){
			double border = this.getOrigin().getBorderSize();
			double u = 1 - border / dist;
			destX = origX + u * (destX-origX);
			destY = origY + u * (destY-origY);
			
			double theta = Math.atan((destY-origY)/(destX-origX));
			if (origX > destX) theta += Math.PI;
			double alpha = destX;
			double beta = destY;

			AffineTransform affineTransform1 = new AffineTransform();
			affineTransform1.setToRotation(theta);
			Polygon triangle1 = new Polygon(new int[] {-10,-10,0}, new int[] {-5,5,0}, 3);
			Shape arrow1 = affineTransform1.createTransformedShape(triangle1);
			affineTransform1.setToTranslation(alpha, beta);
			
			g2.draw(affineTransform1.createTransformedShape(arrow1));
			g2.fill(affineTransform1.createTransformedShape(arrow1));
			
			AffineTransform affineTransform2 = new AffineTransform();
			affineTransform2.setToRotation(theta + Math.PI);
			Polygon triangle2 = new Polygon(new int[] {-10,-10,0}, new int[] {-5,5,0}, 3);
			Shape arrow2 = affineTransform2.createTransformedShape(triangle2);
			alpha = destX - u * (destX-origX);
			beta = destY - u * (destY-origY);
			affineTransform2.setToTranslation(alpha, beta);
			
			g2.draw(affineTransform2.createTransformedShape(arrow2));
			g2.fill(affineTransform2.createTransformedShape(arrow2));
		}
		g2.drawLine(originPos.x, originPos.y, destinationPos.x, destinationPos.y);
		
		VisidiaSettings settings = VisidiaSettings.getInstance();
		boolean showEdgeLabelAndWeight = settings.getBoolean(VisidiaSettings.Constants.SHOW_EDGE_LABEL_AND_WEIGHT);
		// label and weight
		int textPos = (originPos.y + destinationPos.y) / 2;
		if (showEdgeLabelAndWeight) {
			String label = edge.getLabel();
			String s = "";
			if (label != null) s += label;
			double weight = edge.getWeight();

			boolean showWeight = settings.getBoolean(VisidiaSettings.Constants.SHOW_WEIGHT);
			int weightNbDecimals = settings.getInt(VisidiaSettings.Constants.WEIGHT_NB_DECIMALS);
			if (weightNbDecimals < 0) weightNbDecimals = 0;

			String format = "0.";
			for (int i = 0; i < weightNbDecimals; ++i) format += "#";
			DecimalFormat df = new DecimalFormat(format);
			if (showWeight || weight != 1.0) s = s + " (" + df.format(weight) + ")";

			g.setColor(Color.blue);
			g.drawString(s, (originPos.x + destinationPos.x) / 2 + 5, textPos);
			textPos += 12;
		}

		// user-defined displayable properties
		Vector<String> propToDisp = new Vector<String>();
		Set<Object> keys = edge.getPropertyKeys();
		Iterator<Object> itr = keys.iterator();
		while (itr.hasNext()) {
			Object key = itr.next();
			VisidiaProperty prop = edge.getVisidiaProperty(key);
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
		g.drawString(msg, (originPos.x + destinationPos.x) / 2 + 5, textPos);
		
		g.setColor(initialColor);
	}
	
}
