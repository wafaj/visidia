package visidia.misc;

import java.awt.Color;
import java.io.Serializable;

import visidia.misc.colorpalette.ColorPaletteManager;
// TODO: Auto-generated Javadoc
/**
 * The Class ColorLabel associates a color and a label.
 */
public class ColorLabel implements Serializable, Cloneable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -4585667281757362294L;

	/** The color. */
	private Color color;

	/** The label. */
	private String label;

	/**
	 * Instantiates a new color label.
	 * 
	 * @param label the label
	 */
	public ColorLabel(String label) {
		this(ColorPaletteManager.getInstance().getColor(label), label);
	}
	
	/**
	 * Instantiates a new color label.
	 * 
	 * @param color the color
	 * @param label the label
	 */
	public ColorLabel(Color color, String label) {
		set(color, label);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public Object clone() throws CloneNotSupportedException {
		ColorLabel copy = (ColorLabel) super.clone();
		copy.set(color, label);
		return copy;
	}

	/**
	 * Gets the color.
	 * 
	 * @return the color
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * Sets the color.
	 * 
	 * @param color the new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Gets the label.
	 * 
	 * @return the label
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Sets the label.
	 * 
	 * @param label the new label
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * Sets both the color and label.
	 * 
	 * @param color the color
	 * @param label the label
	 */
	public void set(Color color, String label) {
		this.color = color;
		this.label = label;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return label;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj == this)
			return true;

		if (obj instanceof ColorLabel) {
			ColorLabel c = (ColorLabel) obj;

			if (this.color != c.color) {
				if (this.color == null || !this.color.equals(c.color))
					return false;
			}

			if (this.label != c.label) {
				if (this.label == null || !this.label.equals(c.label))
					return false;
			}

			return true;
		}    	

		return false;
	}

}
