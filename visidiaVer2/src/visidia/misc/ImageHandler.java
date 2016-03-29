package visidia.misc;

import javax.swing.ImageIcon;

// TODO: Auto-generated Javadoc
/**
 * This class manages a unique image handler.
 */
public class ImageHandler {

	/** The instance. */
	private static ImageHandler instance = new ImageHandler();

	/**
	 * Instantiates a new image handler.
	 */
	private ImageHandler() {
	}

	/**
	 * Gets the single instance of ImageHandler.
	 * 
	 * @return single instance of ImageHandler
	 */
	public static ImageHandler getInstance() {
		return instance;
	}

	/**
	 * Creates an image icon.
	 * 
	 * @param path the path
	 * 
	 * @return the image icon, or null if the path was invalid
	 */
	public ImageIcon createImageIcon(String path) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL);
		} else {
			return new ImageIcon("../resources/icons/" + path);
		}
	}

}
