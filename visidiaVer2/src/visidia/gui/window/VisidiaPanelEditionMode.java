package visidia.gui.window;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;

import visidia.VisidiaMain;
import visidia.gui.graphview.GraphView;
import visidia.gui.graphview.SensorGraphView;
import visidia.gui.window.dialog.DialogSensorRandomPlacement;
import visidia.io.gml.GMLGraphFileFilter;
import visidia.io.gml.GMLGraphIO;
import visidia.simulation.SimulationConstants;

// TODO: Auto-generated Javadoc
/**
 * VisidiaPanelEditionMode manages toolbars and menus when the GUI (VisidiaPanel) is in the edition mode.
 * 
 * Requires Java6 (for method JTabbedPane.setTabComponentAt(int index,Component component)).
 */
public class VisidiaPanelEditionMode implements VisidiaPanelMode, ActionListener {

	/** The visidia panel. */
	private VisidiaPanel visidiaPanel = null;

	/** The edition primary tool bar. */
	private JToolBar editionPrimaryToolBar = null;

	/** The edition horizontal secondary tool bar. */
	public JToolBar editionHorizontalSecondaryToolBar = null;

	/** The "new graph" button. */
	private JButton buttonNewGraph = null;

	/** The "open graph" button. */
	private JButton buttonOpenGraph = null;

	/** The "save graph" button. */
	private JButton buttonSaveGraph = null;

	/** The undo button. */
	private JButton buttonUndo = null;

	/** The redo button. */
	private JButton buttonRedo = null;

	/** The "new simulation" button. */
	private JButton buttonNewSimulation = null;

	/** The edition menu. */
	private JPopupMenu menuEdition = null;

	/** The edition button. */
	private MenuButton buttonEdition = null;

	/** The "new graph" item. */
	private JMenuItem itemNewGraph = null;

	/** The "open graph" item. */
	private JMenuItem itemOpenGraph = null;

	/** The "save graph" item. */
	private JMenuItem itemSaveGraph = null;

	/** The undo item. */
	private JMenuItem itemUndo = null;

	/** The redo item. */
	private JMenuItem itemRedo = null;

	/** The delete selection item. */
	private JMenuItem itemDeleteSelection = null;

	/** The duplicate selection item. */
	private JMenuItem itemDuplicateSelection = null;

	/** The item complete graph. */
	private JMenuItem itemCompleteGraph = null;

	/** The item select all. */
	private JMenuItem itemSelectAll = null;

	/** The item sensor random placement. */
	private JMenuItem itemSensorRandomPlacement = null;

	/** The fixed processes button. */
	private JRadioButton buttonFixedProcesses = null;

	/** The mobile processes button. */
	private JRadioButton buttonMobileProcesses = null;

	/** The local network button. */
	private JRadioButton buttonLocalNetwork = null;

	/** The remote network button. */
	private JRadioButton buttonRemoteNetwork = null;

	/** The messages button. */
	private JRadioButton buttonMessages = null;

	/** The agents button. */
	private JRadioButton buttonAgents = null;

	/**
	 * The Enum SensorEditionState.
	 */
	private enum SensorEditionState {

		/** Graph support definition. */
		SUPPORT_DEFINITION,

		/** Sensors placement. */
		SENSORS_PLACEMENT
	};

	/** The sensor edition state. */
	private SensorEditionState sensorEditionState = SensorEditionState.SUPPORT_DEFINITION;

	/** The graph view for sensor edition. */
	private SensorGraphView sensorEditionView = null;

	/**
	 * Instantiates a new edition mode.
	 * 
	 * @param visidiaPanel the visidia panel
	 */
	VisidiaPanelEditionMode(VisidiaPanel visidiaPanel) {	
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
		return buttonEdition;
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
		return editionHorizontalSecondaryToolBar;
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
	 * Sets the edition graph panel.
	 * 
	 * @param graphEditionPanel the graph edition panel
	 */
	private void setEditionPanel(GraphPanelEdition graphEditionPanel) {
		int graphEditionPanelTabId = visidiaPanel.getGraphEditionPanelTabId();
		if (graphEditionPanelTabId != -1) {
			// TODO: ask for confirmation
			visidiaPanel.getTabbedPane().setComponentAt(graphEditionPanelTabId, graphEditionPanel);
		} else {
			JTabbedPane pane = visidiaPanel.getTabbedPane();
			pane.addTab("Edition", null, graphEditionPanel, null);
			pane.setTabComponentAt(pane.getTabCount()-1, new ClosableTab(pane, false));
		}
	}

	/**
	 * Opens the edition tab for the user to draw a new graph.
	 */
	void newGraph() {
		sensorEditionState = SensorEditionState.SUPPORT_DEFINITION;
		buttonFixedProcesses.setSelected(true);	
		GraphView graphView = new GraphView();
		visidiaPanel.setDisplayGraph(true);
		GraphPanelEdition graphEditionPanel = new GraphPanelEdition(graphView, true);
		setEditionPanel(graphEditionPanel);
	}

	/**
	 * Loads a graph.
	 */
	private void loadGraph() {
		JFileChooser fc = new JFileChooser(".");
		// try GML loader
		javax.swing.filechooser.FileFilter graphFileFilter = new GMLGraphFileFilter();
		fc.addChoosableFileFilter(graphFileFilter);
		fc.setFileFilter(graphFileFilter);
		fc.setAcceptAllFileFilterUsed(false);

		JCheckBox displayGraphButton = new JCheckBox("Display graph");
		displayGraphButton.setSelected(true);
		fc.setAccessory(displayGraphButton);

		int returnVal = fc.showOpenDialog(visidiaPanel);
		if (returnVal == JFileChooser.CANCEL_OPTION) return;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			boolean displayGraph = displayGraphButton.isSelected();
			visidiaPanel.setDisplayGraph(displayGraph);

			GraphView graphView = null;
			try {
				graphView = new GMLGraphIO(fc.getSelectedFile()).load();
			} catch (OutOfMemoryError e) {
				JOptionPane.showMessageDialog(VisidiaMain.getParentFrame(),	"Not enough memory to load this graph!", "Error in loading file", JOptionPane.ERROR_MESSAGE);
			}
			if (graphView != null) { // load is successful
				GraphPanelEdition graphEditionPanel = new GraphPanelEdition(graphView, displayGraph);
				setEditionPanel(graphEditionPanel);
			} else {
				// try another loader
			}
		}
	}

	/**
	 * Saves current graph.
	 */
	private void saveGraph() {
		GraphPanelEdition graphEditionPanel = visidiaPanel.getGraphEditionPanel(); 
		if (graphEditionPanel == null) return;
		GraphView graphView = graphEditionPanel.getGraphView();
		if (graphView.getGraph() == null) return;

		//GMLFileChooser fc = new GMLFileChooser(".");
		JFileChooser fc = new JFileChooser(".");
		javax.swing.filechooser.FileFilter gmlFileFilter = new GMLGraphFileFilter();
		fc.addChoosableFileFilter(gmlFileFilter);
		fc.setFileFilter(gmlFileFilter);
		fc.setAcceptAllFileFilterUsed(false);

		int returnVal = fc.showSaveDialog(visidiaPanel);
		if (returnVal == JFileChooser.CANCEL_OPTION) return;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File fFile = fc.getSelectedFile ();

			if (fFile.exists ()) {
				int response = JOptionPane.showConfirmDialog (null,
						"Overwrite existing file?","Confirm Overwrite",
						JOptionPane.OK_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE);
				if (response == JOptionPane.CANCEL_OPTION) return;
			}

			String filename = fFile.getName();

			if ( !filename.endsWith(".gml"))
				fFile = new File (filename + ".gml");

			new GMLGraphIO(fFile).save(graphView);
		}
	}

	/**
	 * New simulation.
	 */
	private void newSimulation() {
		int simulationMode = getSimulationMode();
		if (SimulationConstants.RunningMode.mobileMode(simulationMode)) {
			if (sensorEditionState == SensorEditionState.SUPPORT_DEFINITION) {
				try {
					int nbVertices = visidiaPanel.getGraphEditionPanel().getGraphView().getNbVertices();
					if (nbVertices == 0) throw new NullPointerException();
					sensorEditionView = new SensorGraphView(visidiaPanel.getGraphEditionPanel().graphView.cleanCopy());
				} catch (NullPointerException e) {
					int userChoice = JOptionPane.showConfirmDialog(VisidiaMain.getParentFrame(),
							"You have not specified a graph; do you want to use a regular grid?", "Mobile sensors", JOptionPane.YES_NO_OPTION);
					if (userChoice == JOptionPane.YES_OPTION) {
						sensorEditionView = new SensorGraphView(null);
					} else {
						sensorEditionView = null;
						return;
					}
				}

				buttonNewSimulation.setText("New simulation");
				sensorEditionState = SensorEditionState.SENSORS_PLACEMENT;
				itemSensorRandomPlacement.setVisible(true);
				GraphPanelSensorEdition graphEditionPanel = new GraphPanelSensorEdition(sensorEditionView, visidiaPanel.getDisplayGraph());
				setEditionPanel(graphEditionPanel);
			} else if (sensorEditionState == SensorEditionState.SENSORS_PLACEMENT) {
				SensorGraphView graphView = sensorEditionView.cleanCopy();
				GraphPanelSensorSimulation graphSimulationPanel = new GraphPanelSensorSimulation(graphView, simulationMode, visidiaPanel);
				visidiaPanel.newSimulation(graphSimulationPanel, simulationMode);
			}
		} else {
			if (visidiaPanel.getGraphEditionPanelTabId() == -1) return;
			try {
				int nbVertices = visidiaPanel.getGraphEditionPanel().getGraphView().getNbVertices();
				if (nbVertices == 0) throw new NullPointerException();
				GraphView graphView = visidiaPanel.getGraphEditionPanel().graphView.cleanCopy();
				GraphPanelSimulation graphSimulationPanel = new GraphPanelSimulation(graphView, simulationMode, visidiaPanel);
				visidiaPanel.newSimulation(graphSimulationPanel, simulationMode);
			} catch(NullPointerException e) {
				JOptionPane.showMessageDialog(VisidiaMain.getParentFrame(), "You have not specified a graph", "Graph edition", JOptionPane.OK_OPTION);
			}
		}
	}

	/**
	 * Processes the action events.
	 * 
	 * @param evt the event
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {
		if (evt.getSource().equals(buttonNewGraph)) {
			newGraph();
		} else if (evt.getSource().equals(buttonOpenGraph)) {
			loadGraph();
		} else if (evt.getSource().equals(buttonSaveGraph)) {
			saveGraph();
		} else if (evt.getSource().equals(buttonUndo)) {
			GraphPanelEdition graphEditionPanel = visidiaPanel.getGraphEditionPanel(); 
			if (graphEditionPanel != null) {
				graphEditionPanel.undo();
			}
		} else if (evt.getSource().equals(buttonRedo)) {
			GraphPanelEdition graphEditionPanel = visidiaPanel.getGraphEditionPanel(); 
			if (graphEditionPanel != null) {
				graphEditionPanel.redo();
			}
		} else if (evt.getSource().equals(buttonNewSimulation)) {
			newSimulation();
		}
	}

	/**
	 * Gets the simulation mode.
	 * 
	 * @return the simulation mode
	 * 
	 * @see visidia.simulation.SimulationConstants
	 */
	private int getSimulationMode() {
		int mode = 0;

		if (buttonMessages.isSelected()) mode |= SimulationConstants.RunningMode.COMMUNICATION_MESSAGES;
		else if (buttonAgents.isSelected()) mode |= SimulationConstants.RunningMode.COMMUNICATION_AGENTS;

		if (buttonLocalNetwork.isSelected()) mode |= SimulationConstants.RunningMode.NETWORK_LOCAL;
		else if (buttonRemoteNetwork.isSelected()) mode |= SimulationConstants.RunningMode.NETWORK_REMOTE;

		if (buttonFixedProcesses.isSelected()) mode |= SimulationConstants.RunningMode.PROCESSES_FIXED;
		else if (buttonMobileProcesses.isSelected()) mode |= SimulationConstants.RunningMode.PROCESSES_MOBILE;

		return mode;
	}

	/**
	 * Groups buttons.
	 * 
	 * @param b1 the first radiobutton
	 * @param b2 the second radiobutton
	 * 
	 * @return the box
	 */
	private Box groupButtons(JRadioButton b1, JRadioButton b2) {
		ButtonGroup group = new ButtonGroup();
		group.add(b1);
		group.add(b2);

		Box box = Box.createVerticalBox();
		box.add(b1);
		box.add(b2);

		return box;
	}

	/**
	 * Builds the tool bars.
	 */
	private void buildToolBars() {
		// editionPrimaryToolBar
		editionPrimaryToolBar = new JToolBar();

		buttonNewGraph = new ToolBarButton("New", "/new.png", "New graph (Alt-N)", KeyEvent.VK_N, this);
		editionPrimaryToolBar.add(buttonNewGraph);

		buttonOpenGraph = new ToolBarButton("Open", "/open.png", "Open graph file (Alt-O)", KeyEvent.VK_O, this);
		editionPrimaryToolBar.add(buttonOpenGraph);

		buttonSaveGraph = new ToolBarButton("Save", "/save.png", "Save graph (Alt-S)", KeyEvent.VK_S, this);
		editionPrimaryToolBar.add(buttonSaveGraph);

		buttonUndo = new ToolBarButton("Undo", "/undo.png", "Undo (Alt-Z)", KeyEvent.VK_Z, this);
		editionPrimaryToolBar.add(buttonUndo);

		buttonRedo = new ToolBarButton("Redo", "/redo.png", "Redo (Alt-Y)", KeyEvent.VK_Y, this);
		editionPrimaryToolBar.add(buttonRedo);

		// editionHorizontalSecondaryToolBar
		editionHorizontalSecondaryToolBar = new JToolBar();

		buttonFixedProcesses = new JRadioButton("fixed processes");
		buttonFixedProcesses.setFocusable(false);
		buttonMobileProcesses = new JRadioButton("mobile processes");
		buttonMobileProcesses.setFocusable(false);	
		buttonFixedProcesses.setSelected(true);	

		buttonLocalNetwork = new JRadioButton("local network");
		buttonLocalNetwork.setFocusable(false);
		buttonRemoteNetwork = new JRadioButton("remote network");
		buttonRemoteNetwork.setFocusable(false);
		buttonLocalNetwork.setSelected(true);

		buttonMessages = new JRadioButton("messages");
		buttonMessages.setFocusable(false);
		buttonAgents = new JRadioButton("agents");
		buttonAgents.setFocusable(false);	
		buttonMessages.setSelected(true);

		buttonNewSimulation = new JButton("New simulation");
		buttonNewSimulation.setFocusable(false);
		buttonNewSimulation.setToolTipText("Open a new simulation tab");
		buttonNewSimulation.addActionListener(this);

		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.X_AXIS));
		panel.add(groupButtons(buttonFixedProcesses, buttonMobileProcesses), null);
		panel.add(Box.createRigidArea(new Dimension(10,0)));
		panel.add(groupButtons(buttonLocalNetwork, buttonRemoteNetwork), null);
		panel.add(Box.createRigidArea(new Dimension(10,0)));
		panel.add(groupButtons(buttonMessages, buttonAgents), null);
		panel.add(Box.createRigidArea(new Dimension(20,0)));
		panel.add(buttonNewSimulation, null);	

		buttonMobileProcesses.addItemListener(new ItemListener(){
			public void itemStateChanged(ItemEvent e) {
				if (buttonMobileProcesses.isSelected() && sensorEditionState == SensorEditionState.SUPPORT_DEFINITION)
					buttonNewSimulation.setText("Set support graph");
				else buttonNewSimulation.setText("New simulation");

				itemSensorRandomPlacement.setVisible(buttonMobileProcesses.isSelected() && sensorEditionState == SensorEditionState.SENSORS_PLACEMENT);
			}
		});
		editionHorizontalSecondaryToolBar.add(panel);
	}

	/**
	 * Builds the menu.
	 */
	private void buildMenu() {
		ActionListener menuListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuEdition.setVisible(false);
				Object source = e.getSource();
				if (source.equals(itemNewGraph)) {
					newGraph();
				} else if (source.equals(itemOpenGraph)) {
					loadGraph();
				} else if (source.equals(itemSaveGraph)) {
					saveGraph();
				} else if (source.equals(itemUndo)) {
					GraphPanelEdition graphEditionPanel = visidiaPanel.getGraphEditionPanel(); 
					if (graphEditionPanel != null) graphEditionPanel.undo();
				} else if (source.equals(itemRedo)) {
					GraphPanelEdition graphEditionPanel = visidiaPanel.getGraphEditionPanel(); 
					if (graphEditionPanel != null) graphEditionPanel.redo();
				} else if (source.equals(itemDeleteSelection)) {
					GraphPanelEdition graphEditionPanel = visidiaPanel.getGraphEditionPanel(); 
					if (graphEditionPanel != null) graphEditionPanel.removeSelection();	
				} else if (source.equals(itemDuplicateSelection)) {
					GraphPanelEdition graphEditionPanel = visidiaPanel.getGraphEditionPanel(); 
					if (graphEditionPanel != null) graphEditionPanel.duplicateSelection();						
				} else if (source.equals(itemCompleteGraph)) {
					GraphPanelEdition graphEditionPanel = visidiaPanel.getGraphEditionPanel(); 
					if (graphEditionPanel != null) graphEditionPanel.completeGraph();						
				} else if (source.equals(itemSelectAll)) {
					GraphPanelEdition graphEditionPanel = visidiaPanel.getGraphEditionPanel(); 
					if (graphEditionPanel != null) graphEditionPanel.selectAll();						
				} else if (source.equals(itemSensorRandomPlacement)) {
					DialogSensorRandomPlacement dialog = new DialogSensorRandomPlacement(VisidiaMain.getParentFrame());
					dialog.setVisible(true);
					((GraphPanelSensorEdition) visidiaPanel.getGraphEditionPanel()).randomlyPlaceSensors(dialog.getNbSensors());
				}
			}
		};

		menuEdition = new JPopupMenu();

		itemNewGraph = new JMenuItem("New graph", KeyEvent.VK_N);
		itemNewGraph.addActionListener(menuListener);
		menuEdition.add(itemNewGraph);

		itemOpenGraph = new JMenuItem("Open graph file", KeyEvent.VK_O);
		itemOpenGraph.addActionListener(menuListener);
		menuEdition.add(itemOpenGraph);

		itemSaveGraph = new JMenuItem("Save graph", KeyEvent.VK_S);
		itemSaveGraph.addActionListener(menuListener);
		menuEdition.add(itemSaveGraph);

		menuEdition.addSeparator();

		itemUndo = new JMenuItem("Undo", KeyEvent.VK_Z);
		itemUndo.addActionListener(menuListener);
		menuEdition.add(itemUndo);

		itemRedo = new JMenuItem("Redo", KeyEvent.VK_Y);
		itemRedo.addActionListener(menuListener);
		menuEdition.add(itemRedo);

		menuEdition.addSeparator();

		itemDeleteSelection = new JMenuItem("Delete selection", KeyEvent.VK_D);
		itemDeleteSelection.addActionListener(menuListener);
		menuEdition.add(itemDeleteSelection);

		itemDuplicateSelection = new JMenuItem("Duplicate selection");
		itemDuplicateSelection.addActionListener(menuListener);
		menuEdition.add(itemDuplicateSelection);

		itemCompleteGraph = new JMenuItem("Complete graph");
		itemCompleteGraph.addActionListener(menuListener);
		menuEdition.add(itemCompleteGraph);

		itemSelectAll = new JMenuItem("Select all", KeyEvent.VK_A);
		itemSelectAll.addActionListener(menuListener);
		menuEdition.add(itemSelectAll);

		menuEdition.addSeparator();

		itemSensorRandomPlacement = new JMenuItem("Randomly place sensors");
		itemSensorRandomPlacement.setVisible(false);
		itemSensorRandomPlacement.addActionListener(menuListener);
		menuEdition.add(itemSensorRandomPlacement);

		buttonEdition = new MenuButton("Edition", menuEdition);
	}

	/**
	 * Sets if the mode is active.
	 * 
	 * @param isActive the active state
	 */
	public void setActive(boolean isActive) {
		buttonEdition.setVisible(isActive);
		editionPrimaryToolBar.setVisible(isActive);
		editionHorizontalSecondaryToolBar.setVisible(isActive);
	}

}
