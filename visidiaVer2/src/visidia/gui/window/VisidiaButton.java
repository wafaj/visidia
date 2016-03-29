package visidia.gui.window;

import javax.swing.JButton;

/**
 * This is the abstract to represent a button of ViSiDiA application.
 */
public abstract class VisidiaButton extends JButton {

	private static final long serialVersionUID = 1557562541854676290L;

	/**
	 * Instantiates a new button.
	 * 
	 * @param text the text
	 */
	protected VisidiaButton(String text) {
		super(text);
	}

}
