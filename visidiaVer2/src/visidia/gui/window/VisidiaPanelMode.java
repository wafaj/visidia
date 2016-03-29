package visidia.gui.window;

import javax.swing.JToolBar;

// TODO: Auto-generated Javadoc
/**
 * VisidiaPanelMode is the interface for GUI running modes (edition, simulation,...).
 */
public interface VisidiaPanelMode {

	/**
	 * Gets the menu button.
	 * 
	 * @return the menu button
	 */
	public MenuButton getMenuButton();

	/**
	 * Gets the primary tool bar.
	 * 
	 * @return the primary tool bar
	 */
	public JToolBar getPrimaryToolBar();

	/**
	 * Gets the horizontal secondary tool bar.
	 * 
	 * @return the horizontal secondary tool bar
	 */
	public JToolBar getHorizontalSecondaryToolBar();

	/**
	 * Gets the vertical secondary tool bar.
	 * 
	 * @return the vertical secondary tool bar
	 */
	public JToolBar getVerticalSecondaryToolBar();

	/**
	 * Sets if the mode is active.
	 * 
	 * @param isActive the active state
	 */
	public void setActive(boolean isActive);

}
