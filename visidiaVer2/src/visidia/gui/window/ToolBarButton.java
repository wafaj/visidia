package visidia.gui.window;

import java.awt.event.ActionListener;

import visidia.misc.ImageHandler;

/**
 * ToolBarButton is a button to be inserted in a toolbar.
 */
public class ToolBarButton extends VisidiaButton {

	private static final long serialVersionUID = -5285001888012083795L;

	/**
	 * Instantiates a new tool bar button.
	 */
	ToolBarButton(String text, String icon, String toolTip, int mnemonic, ActionListener listener) {
		super(text);
		if (icon != null && !icon.equals("")) setIcon(ImageHandler.getInstance().createImageIcon(icon));
		setFocusable(false);
		if (toolTip != null && !toolTip.equals("")) setToolTipText(toolTip);
		if (mnemonic != -1) setMnemonic(mnemonic);
		addActionListener(listener);
	}

}
