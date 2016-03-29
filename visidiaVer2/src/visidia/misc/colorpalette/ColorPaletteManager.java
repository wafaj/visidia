package visidia.misc.colorpalette;

import java.awt.Color;
import java.util.Enumeration;

import visidia.misc.VisidiaSettings;

// TODO: Auto-generated Javadoc
/**
 * ColorPaletteManager is used to manage color palettes.
 */
public class ColorPaletteManager {

	/** The instance. */
	private static ColorPaletteManager instance = new ColorPaletteManager();

	/** The standard palette. */
	private StandardColorPalette standardPalette = new StandardColorPalette();

	/** The custom palette. */
	private ColorPalette customPalette = null;

	/**
	 * Instantiates a new color palette manager.
	 */
	private ColorPaletteManager() {
		customPalette = null;
	}

	/**
	 * Gets the single instance of ColorPaletteManager.
	 * 
	 * @return single instance of ColorPaletteManager
	 */
	public static ColorPaletteManager getInstance() {
		return instance;
	}

	/**
	 * Resets current palette to standard palette.
	 */
	public void resetPalette() {
		customPalette = null;
	}

	/**
	 * Tells if using a custom palette.
	 * 
	 * @return true, if using a custom palette
	 */
	public boolean useCustomPalette() {
		return (customPalette != null && VisidiaSettings.getInstance().getBoolean(VisidiaSettings.Constants.USE_CUSTOM_COLOR_PALETTE));
	}
	
	/**
	 * Creates a new palette and sets it as current palette.
	 * 
	 * @param nbColors the number of colors to generate
	 * 
	 * @return the color palette
	 */
	public ColorPalette createAndUseNewPalette(int nbColors) {
		customPalette = new CustomColorPalette(nbColors);
		return customPalette;
	}

	/**
	 * Gets the color associated to key.
	 * First search in custom palette.
	 * If not defined or if it does not contain the key, then search in standard palette.
	 * If no match found, return null.
	 * 
	 * @param key the key
	 * 
	 * @return the color
	 */
	public Color getColor(Object key) {
		Object obj = null;
		if (customPalette != null  && VisidiaSettings.getInstance().getBoolean(VisidiaSettings.Constants.USE_CUSTOM_COLOR_PALETTE)) obj = customPalette.getColor(key);
		if (obj == null) obj = standardPalette.getColor(key);
		return (Color) obj;
	}

	/**
	 * Gets the color associated to key.
	 * First search in custom palette.
	 * If not defined or if it does not contain the key, then search in standard palette.
	 * If no match found, return null.
	 * 
	 * @param key the key
	 * 
	 * @return the color
	 */
	public Color getCustomColor(Object key) {
		Object obj = null;
		if (customPalette != null) obj = customPalette.getColor(key);
		if (obj == null) obj = standardPalette.getColor(key);
		return (Color) obj;
	}
	
	/**
	 * Gets the color associated to key.
	 * First search in custom palette.
	 * If not defined or if it does not contain the key, then search in standard palette.
	 * If no match found, return null.
	 * 
	 * @param key the key
	 * 
	 * @return the color
	 */
	public Color getStandardColor(Object key) {
		Object obj = standardPalette.getColor(key);
		return (Color) obj;
	}
	/**
	 * Returns custom palette keys if defined.
	 * Else returns standard palette keys.
	 * 
	 * @return the keys
	 */
	public Enumeration<Object> getAllKeys() {
		if (customPalette != null && VisidiaSettings.getInstance().getBoolean(VisidiaSettings.Constants.USE_CUSTOM_COLOR_PALETTE)) return customPalette.keys();
		return standardPalette.keys();
	}

	/**
	 * Returns custom palette keys if defined.
	 * Else returns standard palette keys.
	 * 
	 * @return the keys
	 */
	public Enumeration<Object> getCustomKeys() {
		if (customPalette != null) return customPalette.keys();
		return standardPalette.keys();
	}
	/**
	 * Gets keys of standard palette.
	 * 
	 * @return the keys
	 */
	public Enumeration<Object> getStandardKeys() {
		return standardPalette.keys();
	}

	/**
	 * Returns the number of colors in current palette.
	 * 
	 * @return the palette size
	 */
	public int size() {
		return ((customPalette != null && VisidiaSettings.getInstance().getBoolean(VisidiaSettings.Constants.USE_CUSTOM_COLOR_PALETTE))? customPalette.size() : standardPalette.size());
	}

	/**
	 * Returns the number of colors in current palette.
	 * 
	 * @return the palette size
	 */
	public int customSize() {
		return (customPalette != null? customPalette.size() : standardPalette.size());
	}
	/**
	 * Returns the number of colors in standard palette.
	 * 
	 * @return the palette size
	 */
	public int standardSize() {
		return   standardPalette.size();
	}
	/**
	 * Sets the custom palette.
	 * 
	 * @param colorPalette the new custom palette
	 */
	public void setCustomPalette(ColorPalette colorPalette) {
		customPalette = colorPalette;
	}

}
