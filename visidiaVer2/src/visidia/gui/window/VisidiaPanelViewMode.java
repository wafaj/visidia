package visidia.gui.window;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JToolBar;

import visidia.VisidiaMain;
import visidia.gui.window.dialog.DialogGraphProperties;
import visidia.gui.window.dialog.HelpDialog;

/**
 * VisidiaPanelViewMode manages toolbars and menus when the GUI (VisidiaPanel) is in the view mode (edition or simulation).
 */
public class VisidiaPanelViewMode implements VisidiaPanelMode, ActionListener{

	/** The visidia panel. */
	private VisidiaPanel visidiaPanel = null;
	
	/** The edition primary tool bar. */
	private JToolBar editionPrimaryToolBar = null;

	/** The left side margin of the edition primary tool bar. */
	public final int editionPrimaryToolBarOffsetX = 40;

	/** The info button. */
	private JButton buttonInfo = null;

	/** The help button. */
	private JButton buttonHelp = null;

	/** The zoom in button. */
	private JButton buttonZoomIn = null;

	/** The zoom out button. */
	private JButton buttonZoomOut = null;

	/** The button to restore original zoom. */
	private JButton buttonZoomOriginal = null;

	/** The zoom factor label. */
	private JLabel labelZoomFactor = null;
	
	/** The view menu. */
	private JPopupMenu menuView = null;

	/** The view button. */
	private MenuButton buttonView = null;

	/** The item to display/hide graph. */
	JCheckBoxMenuItem itemDisplayGraph = null;

	/** The info item in menuLikeToolBar. */
	private JMenuItem itemInfo = null;

	/** The help item in menuLikeToolBar. */
	private JMenuItem itemHelp = null;

	/** The zoom in item in menuLikeToolBar. */
	private JMenuItem itemZoomIn = null;

	/** The zoom out item in menuLikeToolBar. */
	private JMenuItem itemZoomOut = null;

	/** The item in menuLikeToolBar to restore original zoom. */
	private JMenuItem itemZoomOriginal = null;
	
	/**
	 * Instantiates a new view mode.
	 */
	VisidiaPanelViewMode(VisidiaPanel visidiaPanel) {
		this.visidiaPanel = visidiaPanel;
		this.buildMenu();
		this.buildToolBars();
	}

	/**
	 * Gets the menu button.
	 * 
	 * @return the menu button
	 */
	public MenuButton getMenuButton() {
		return buttonView;
	}

	/**
	 * Gets the primary tool bar.
	 * 
	 * @return the primary tool bar
	 */
	public JToolBar getPrimaryToolBar() {
		return editionPrimaryToolBar;
	}

	/**
	 * Gets the horizontal secondary tool bar.
	 * 
	 * @return the horizontal secondary tool bar
	 */
	public JToolBar getHorizontalSecondaryToolBar() {
		return null;
	}

	/**
	 * Gets the vertical secondary tool bar.
	 * 
	 * @return the vertical secondary tool bar
	 */
	public JToolBar getVerticalSecondaryToolBar() {
		return null;
	}
	
	/**
	 * Update zoom label.
	 * 
	 * @param graphPanel the graph panel
	 */
	void updateZoomLabel(GraphPanel graphPanel) {
		String s = new Double(100*graphPanel.getZoomFactor()).toString();
		if (s.contains(".")) s = s.substring(0, s.indexOf("."));
		labelZoomFactor.setText(s+"%");
	}

	/**
	 * Shows graph information.
	 */
	private void showInfo(GraphPanel graphPanel) {
		new DialogGraphProperties(VisidiaMain.getParentFrame(), graphPanel, graphPanel.selection).setVisible(true);
	}
	
	/**
	 * Show help.
	 */
	private void showHelp() {
		new HelpDialog(VisidiaMain.getParentFrame()).setVisible(true);
	}
	
	/**
	 * Processes the action events.
	 * 
	 * @param evt the event
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		GraphPanel graphPanel = (GraphPanel) visidiaPanel.getTabbedPane().getSelectedComponent();
		if (graphPanel == null)	return;

		if (evt.getSource().equals(buttonZoomIn)) {
			graphPanel.zoomIn();
			updateZoomLabel(graphPanel);
		} else if (evt.getSource().equals(buttonZoomOut)) {
			graphPanel.zoomOut();
			updateZoomLabel(graphPanel);
		} else if (evt.getSource().equals(buttonZoomOriginal)) {
			graphPanel.zoomToOriginal();
			updateZoomLabel(graphPanel);
		} else if (evt.getSource().equals(buttonInfo)) {
			showInfo(graphPanel);
		} else if (evt.getSource().equals(buttonHelp)) {
			showHelp();
		}
	}

	/**
	 * Builds the tool bars.
	 */
	private void buildToolBars() {
		// editionPrimaryToolBar
		editionPrimaryToolBar = new JToolBar();

		buttonHelp = new ToolBarButton("", "/help.png", "ViSiDiA help", -1, this);
		editionPrimaryToolBar.add(buttonHelp);
		
		buttonInfo = new ToolBarButton("", "/info.png", "Graph properties", -1, this);
		editionPrimaryToolBar.add(buttonInfo);
		
		editionPrimaryToolBar.addSeparator();
		
		buttonZoomIn = new ToolBarButton("Zoom In", "/zoomin.png", "Zoom In (Alt-+)", KeyEvent.VK_ADD, this);
		editionPrimaryToolBar.add(buttonZoomIn);
		
		buttonZoomOut = new ToolBarButton("Zoom Out", "/zoomout.png", "Zoom Out (Alt--)", KeyEvent.VK_MINUS, this);
		editionPrimaryToolBar.add(buttonZoomOut);
		
		buttonZoomOriginal = new ToolBarButton("1:1", "/zoom.png", "Zoom to original size (Alt-=)", KeyEvent.VK_EQUALS, this);
		editionPrimaryToolBar.add(buttonZoomOriginal);
        
        labelZoomFactor = new JLabel("100%");
		editionPrimaryToolBar.add(labelZoomFactor);
	}

	/**
	 * Builds the menu.
	 */
	private void buildMenu() {
		ActionListener menuListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuView.setVisible(false);
				GraphPanel graphPanel = (GraphPanel) visidiaPanel.getTabbedPane().getSelectedComponent();
				if (graphPanel == null) return;
				
				Object source = e.getSource();
				if (source.equals(itemZoomIn)) {
					graphPanel.zoomIn();
					updateZoomLabel(graphPanel);
				} else if (source.equals(itemZoomOut)) {
					graphPanel.zoomOut();
					updateZoomLabel(graphPanel);
				} else if (source.equals(itemZoomOriginal)) {
					graphPanel.zoomToOriginal();
					updateZoomLabel(graphPanel);
				} else if (source.equals(itemInfo)) {
					showInfo(graphPanel);
				} else if (source.equals(itemHelp)) {
					showHelp();
				}
			}
		};

		menuView = new JPopupMenu();

		itemDisplayGraph = new JCheckBoxMenuItem("Display graph");
		itemDisplayGraph.setSelected(true);
		itemDisplayGraph.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				visidiaPanel.setDisplayGraph(itemDisplayGraph.isSelected());
			}
		});
		menuView.add(itemDisplayGraph);

		menuView.addSeparator();

		itemHelp = new JMenuItem("Help");
		itemHelp.addActionListener(menuListener);
		menuView.add(itemHelp);

		itemInfo = new JMenuItem("Properties");
		itemInfo.addActionListener(menuListener);
		menuView.add(itemInfo);
		
		menuView.addSeparator();
		
		itemZoomIn = new JMenuItem("Zoom In", KeyEvent.VK_ADD);
		itemZoomIn.addActionListener(menuListener);
		menuView.add(itemZoomIn);
		
		itemZoomOut = new JMenuItem("Zoom Out", KeyEvent.VK_MINUS);
		itemZoomOut.addActionListener(menuListener);
		menuView.add(itemZoomOut);

		itemZoomOriginal = new JMenuItem("Zoom 1:1", KeyEvent.VK_EQUALS);
		itemZoomOriginal.addActionListener(menuListener);
		menuView.add(itemZoomOriginal);

		buttonView = new MenuButton("View", menuView);
	}

	/**
	 * Sets if the mode is active.
	 * 
	 * @param isActive the active state
	 */
	public void setActive(boolean isActive) {
		buttonView.setVisible(isActive);
		editionPrimaryToolBar.setVisible(isActive);
	}
	
}
