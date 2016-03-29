package visidia.gui.window;

import javax.swing.JPopupMenu;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;

import visidia.misc.ImageHandler;

/**
 * MenuButton is a button used for popping up a menu, when clicked.
 */
public class MenuButton extends VisidiaButton {

	private static final long serialVersionUID = 8736075344338431246L;

	/**
	 * Instantiates a new menu button.
	 * 
	 * @param text the button text
	 * @param menu the associated menu
	 */
	MenuButton(String text, final JPopupMenu menu) {
		super(text);
		setFocusable(false);
		setIcon(ImageHandler.getInstance().createImageIcon("/menu_closed.png"));
		addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent e) {
				menu.show(e.getComponent(), e.getX(), e.getY());
			}
		});
		menu.addPopupMenuListener(new PopupMenuListener() {
			public void popupMenuCanceled(PopupMenuEvent e) {
			}

			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				setIcon(ImageHandler.getInstance().createImageIcon("/menu_closed.png"));
			}

			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				setIcon(ImageHandler.getInstance().createImageIcon("/menu_open.png"));
			}
		});
	}

}
