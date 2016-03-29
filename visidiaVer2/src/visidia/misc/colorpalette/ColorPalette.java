package visidia.misc.colorpalette;

import java.awt.Color;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;

// TODO: Auto-generated Javadoc
/**
 * ColorPalette is the base class for manipulating color palettes.
 */
public class ColorPalette implements Serializable {

	private static final long serialVersionUID = 6948353469483771618L;

	/** The table. */
	protected Hashtable<Object, Color> table;

	/**
	 * Instantiates a new color palette.
	 */
	protected ColorPalette() {
		table = new Hashtable<Object, Color>();
	}

	/**
	 * Gets the color associated to key.
	 * 
	 * @param key the key
	 * 
	 * @return the color
	 */
	public Color getColor(Object key) {
		return table.get(key);
	}

	/**
	 * Gets the keys.
	 * 
	 * @return the keys
	 */
	public Enumeration<Object> keys() {
		return table.keys();
	}

	/**
	 * Returns the number of colors in current palette.
	 * 
	 * @return the palette size
	 */
	public int size() {
		return table.size();
	}

}
