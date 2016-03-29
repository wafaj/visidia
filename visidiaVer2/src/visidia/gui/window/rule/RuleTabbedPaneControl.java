package visidia.gui.window.rule;

/**
 * Describes the functions needed by each rule pane proposed in a popup menu.
 */
public interface RuleTabbedPaneControl {

	public void addNewRule();

	public void deleteRule();

	public void insertRule();

	public void switchLeft();

	public void switchRight();

	public boolean canSwitchRight();

	public boolean canSwitchLeft();

}
