package visidia.gui.window;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import visidia.VisidiaMain;
import visidia.gui.graphview.CircleVertex;
import visidia.gui.graphview.EdgeView;
import visidia.gui.graphview.GraphView;
import visidia.gui.graphview.GraphViewFactory;
import visidia.gui.graphview.SegmentEdge;
import visidia.gui.graphview.SensorGraphView;
import visidia.gui.graphview.VertexView;
import visidia.gui.window.dialog.preferences.DialogVisidiaPreferences;
import visidia.misc.VisidiaSettings;
import visidia.simulation.SimulationConstants;
import visidia.simulation.process.algorithm.Algorithm;

// TODO: Auto-generated Javadoc
/**
 * The VisidiaPanel class is the principal display panel, which contains all the buttons, options and tabs.
 */
public class VisidiaPanel extends JPanel {

	private static final long serialVersionUID = 6094297751283298240L;

	/** The left side margin of the edition primary tool bar. */
	public final int primaryToolBarOffsetX = 40;

	/** The left side margin of the edition secondary tool bar. */
	public final int secondaryToolBarOffsetX = 40;

	/** The graph view factory. */
	private static GraphViewFactory gvFactory = null;

	/** The view mode. */
	VisidiaPanelViewMode viewMode = null;

	/** The edition mode. */
	VisidiaPanelEditionMode editionMode = null;

	/** The simulation mode. */
	VisidiaPanelSimulationMode simulationMode = null;

	/** The visidia menu in menuLikeToolBar. */
	JPopupMenu menuVisidia = null;

	/** The quit item in menuLikeToolBar. */
	JMenuItem itemQuit = null;

	/** The preferences item in menuLikeToolBar. */
	JMenuItem itemPreferences = null;

	/** The replay simulation item in menuLikeToolBar. */
	JMenuItem itemReplaySimulation = null;

	/** The view menu in menuLikeToolBar. */
	JPopupMenu menuView = null;

	/** The tabbed pane. */
	private JTabbedPane tabbedPane = null;
	GridBagConstraints rightToolBarConstraints;
	Box rightToolBarBox;

	/**
	 * Instantiates a new visidia panel.
	 */
	public VisidiaPanel() {
		super(new GridBagLayout());
		VertexView defaultVertexView = new CircleVertex();
		EdgeView defaultEdgeView = new SegmentEdge();
		gvFactory = new GraphViewFactory(defaultVertexView, defaultEdgeView);

		viewMode = new VisidiaPanelViewMode(this);
		editionMode = new VisidiaPanelEditionMode(this);
		simulationMode = new VisidiaPanelSimulationMode(this);
		viewMode.setActive(true);
		editionMode.setActive(true);
		simulationMode.setActive(false);

		buildUI();
		editionMode.newGraph();
	}

	/**
	 * Gets the boolean indicating if the graph is to be displayed.
	 * 
	 * @return true, if graph is displayed
	 */
	public boolean getDisplayGraph() {
		return viewMode.itemDisplayGraph.isSelected();
	}

	/**
	 * Sets the boolean indicating if the graph is to be displayed.
	 * 
	 * @param displayGraph true, if graph is to be displayed
	 */
	public void setDisplayGraph(boolean displayGraph) {
		viewMode.itemDisplayGraph.setSelected(displayGraph);
		GraphPanel panel = (GraphPanel) getTabbedPane().getSelectedComponent();
		if (panel != null)
			panel.setDisplayGraph(displayGraph);
	}

	/**
	 * Gets the factory.
	 * 
	 * @return the factory
	 */
	public static GraphViewFactory getFactory() {
		return gvFactory;
	}

	/**
	 * Builds the tool bars.
	 */
	private void buildToolBars() {
		// menuLikeToolBar
		JToolBar menuLikeToolBar = new JToolBar();
		menuLikeToolBar.setOrientation(JToolBar.VERTICAL);

		ActionListener menuListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuVisidia.setVisible(false);
				if (e.getSource().equals(itemQuit)) {
					VisidiaMain.quit();
				} else if (e.getSource().equals(itemPreferences)) {
					GraphPanel graphPanel = (GraphPanel) getTabbedPane().getSelectedComponent();
					new DialogVisidiaPreferences(VisidiaMain.getParentFrame(), graphPanel).setVisible(true);
				} else if (e.getSource().equals(itemReplaySimulation)) {
					String path = VisidiaSettings.getInstance().getString(VisidiaSettings.Constants.VISIDIA_BASE_URL);
					JFileChooser chooser = new JFileChooser(path);
					FileNameExtensionFilter filter = new FileNameExtensionFilter("Replay files", "replay");
					chooser.setFileFilter(filter);
					int returnVal = chooser.showOpenDialog(VisidiaMain.getParentFrame());
					if (returnVal == JFileChooser.CANCEL_OPTION) return;
					if (returnVal == JFileChooser.APPROVE_OPTION) {
						loadReplay(chooser.getSelectedFile());
					}
				}
			}
		};

		menuVisidia = new JPopupMenu();
		itemPreferences = new JMenuItem("Preferences");
		itemPreferences.addActionListener(menuListener);
		menuVisidia.add(itemPreferences);

		menuVisidia.addSeparator();

		itemReplaySimulation = new JMenuItem("Simulation replay");
		itemReplaySimulation.addActionListener(menuListener);
		menuVisidia.add(itemReplaySimulation);

		menuVisidia.addSeparator();

		itemQuit = new JMenuItem("Quit", KeyEvent.VK_Q);
		itemQuit.addActionListener(menuListener);
		menuVisidia.add(itemQuit);

		JButton buttonViSiDiA = new MenuButton("ViSiDiA", menuVisidia);
		menuLikeToolBar.add(buttonViSiDiA);

		menuLikeToolBar.add(editionMode.getMenuButton());
		menuLikeToolBar.add(viewMode.getMenuButton());
		menuLikeToolBar.add(simulationMode.getMenuButton());

		// add menuLikeToolBar and vertical secondary toolBar to the UI
		Box leftToolBarBox = Box.createVerticalBox();
		JToolBar leftToolBar = new JToolBar();
		leftToolBar.setOrientation(JToolBar.VERTICAL);
		leftToolBar.add(menuLikeToolBar);
		menuLikeToolBar.setAlignmentX(LEFT_ALIGNMENT);
		menuLikeToolBar.setFloatable(false);

		if (editionMode.getVerticalSecondaryToolBar() != null || simulationMode.getVerticalSecondaryToolBar() != null || viewMode.getVerticalSecondaryToolBar() != null)
			leftToolBar.add(Box.createRigidArea(new Dimension(0, 100)));

		if (editionMode.getVerticalSecondaryToolBar() != null) {
			leftToolBar.add(editionMode.getVerticalSecondaryToolBar());
			editionMode.getVerticalSecondaryToolBar().setAlignmentX(LEFT_ALIGNMENT);
			editionMode.getVerticalSecondaryToolBar().setFloatable(false);
		}
		if (simulationMode.getVerticalSecondaryToolBar() != null) {
			leftToolBar.add(simulationMode.getVerticalSecondaryToolBar());
			simulationMode.getVerticalSecondaryToolBar().setAlignmentX(LEFT_ALIGNMENT);
			simulationMode.getVerticalSecondaryToolBar().setFloatable(false);
		}
		if (viewMode.getVerticalSecondaryToolBar() != null) {
			leftToolBar.add(viewMode.getVerticalSecondaryToolBar());	
			viewMode.getVerticalSecondaryToolBar().setAlignmentX(LEFT_ALIGNMENT);
			viewMode.getVerticalSecondaryToolBar().setFloatable(false);	
		}
		leftToolBarBox.add(leftToolBar);

		GridBagConstraints leftToolBarConstraints = new GridBagConstraints();
		leftToolBarConstraints.gridx = 0;
		leftToolBarConstraints.gridy = 1;
		leftToolBarConstraints.anchor = GridBagConstraints.PAGE_START;
		this.add(leftToolBarBox, leftToolBarConstraints);

		/*rightToolBarBox = Box.createVerticalBox();
		JToolBar rightToolBar = new JToolBar();
		rightToolBar.setOrientation(JToolBar.HORIZONTAL);
		rightToolBar.add(Box.createRigidArea(new Dimension(700, 100)));
        rightToolBar.add(simulationMode.getVerticalThirdToolBar());*/

		simulationMode.getVerticalThirdToolBar().setAlignmentX(LEFT_ALIGNMENT);
		//simulationMode.getVerticalThirdToolBar().setFloatable(false);


		//	rightToolBarBox.add(rightToolBar);

		rightToolBarConstraints = new GridBagConstraints();
		rightToolBarConstraints.gridx = 1;
		rightToolBarConstraints.gridy = 2;
		rightToolBarConstraints.weightx = 1;
		rightToolBarConstraints.weighty = 0;
		rightToolBarConstraints.anchor = GridBagConstraints.LINE_START;

		this.add(simulationMode.getVerticalThirdToolBar(), rightToolBarConstraints);
		//rightToolBarBox.setVisible(false);
		simulationMode.getVerticalThirdToolBar().setVisible(false);


		// primary toolBar
		Box primaryToolBarBox = Box.createHorizontalBox();
		primaryToolBarBox.add(Box.createRigidArea(new Dimension(primaryToolBarOffsetX, 0)));
		JToolBar primaryToolBar = new JToolBar();
		primaryToolBar.setOrientation(JToolBar.HORIZONTAL);
		if (editionMode.getPrimaryToolBar() != null) {
			primaryToolBar.add(editionMode.getPrimaryToolBar());
			editionMode.getPrimaryToolBar().setAlignmentX(LEFT_ALIGNMENT);
			editionMode.getPrimaryToolBar().setFloatable(false);
		}
		if (viewMode.getPrimaryToolBar() != null) {
			primaryToolBar.add(viewMode.getPrimaryToolBar());
			viewMode.getPrimaryToolBar().setAlignmentX(LEFT_ALIGNMENT);
			viewMode.getPrimaryToolBar().setFloatable(false);
		}
		if (simulationMode.getPrimaryToolBar() != null) {
			primaryToolBar.add(simulationMode.getPrimaryToolBar());
			simulationMode.getPrimaryToolBar().setAlignmentX(LEFT_ALIGNMENT);
			simulationMode.getPrimaryToolBar().setFloatable(false);
		}
		primaryToolBarBox.add(primaryToolBar);

		GridBagConstraints primaryToolBarConstraints = new GridBagConstraints();
		primaryToolBarConstraints.gridx = 1;
		primaryToolBarConstraints.gridy = 0;
		primaryToolBarConstraints.anchor = GridBagConstraints.LINE_START;
		this.add(primaryToolBarBox, primaryToolBarConstraints);

		// secondary toolBar
		Box horizontalSecondaryToolBarBox = Box.createHorizontalBox();
		horizontalSecondaryToolBarBox.add(Box.createRigidArea(new Dimension(secondaryToolBarOffsetX, 0)));
		if (editionMode.getHorizontalSecondaryToolBar() != null) horizontalSecondaryToolBarBox.add(editionMode.getHorizontalSecondaryToolBar());
		if (simulationMode.getHorizontalSecondaryToolBar() != null) horizontalSecondaryToolBarBox.add(simulationMode.getHorizontalSecondaryToolBar());
		if (viewMode.getHorizontalSecondaryToolBar() != null) horizontalSecondaryToolBarBox.add(viewMode.getHorizontalSecondaryToolBar());

		GridBagConstraints secondaryToolBarConstraints = new GridBagConstraints();
		secondaryToolBarConstraints.gridx = 1;
		secondaryToolBarConstraints.gridy = 2;
		secondaryToolBarConstraints.anchor = GridBagConstraints.LINE_START;
		this.add(horizontalSecondaryToolBarBox, secondaryToolBarConstraints);	
		tabbedPane.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if( tabbedPane.getSelectedIndex()==0);
				hideStepByStepPanel();
			}
		});
	}

	/**
	 * Builds the User Interface.
	 */

	private void buildUI() {
		setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

		GridBagConstraints tabbedPaneConstraits = new GridBagConstraints();
		tabbedPaneConstraits.gridx = 1;
		tabbedPaneConstraits.gridy = 1;
		tabbedPaneConstraits.weightx = 1;
		tabbedPaneConstraits.weighty = 1;
		tabbedPaneConstraits.fill = GridBagConstraints.BOTH;
		this.add(getTabbedPane(), tabbedPaneConstraits);

		buildToolBars();	
	}


	/**
	 * Add the Step by step panel.
	 */
	public void showStepByStepPanel(){

		//rightToolBarBox.setVisible(true);
		simulationMode.getVerticalThirdToolBar().setVisible(true);

	}

	/**
	 * Remove the Step by step panel.
	 */
	public void hideStepByStepPanel(){

		//rightToolBarBox.setVisible(false);
		simulationMode.getVerticalThirdToolBar().setVisible(false);

	}



	/**
	 * Gets the tabbed pane.
	 * 
	 * @return the tabbed pane
	 */
	public JTabbedPane getTabbedPane() {
		if (tabbedPane == null) {
			tabbedPane = new JTabbedPane();
			tabbedPane.setFocusable(false);
			tabbedPane.addChangeListener(new javax.swing.event.ChangeListener() {
				public void stateChanged(javax.swing.event.ChangeEvent e) {
					int selectedId = tabbedPane.getSelectedIndex();
					if (selectedId == -1) return;

					GraphPanel panel = (GraphPanel) getTabbedPane().getSelectedComponent();
					if (panel != null) setDisplayGraph(panel.displayGraph);

					String title = tabbedPane.getTitleAt(selectedId);
					boolean activeEditionMode = title.startsWith("Edition");
					viewMode.setActive(true);
					viewMode.updateZoomLabel(panel);
					editionMode.setActive(activeEditionMode);
					if (!activeEditionMode) {
						GraphPanelSimulation simuPanel = (GraphPanelSimulation) tabbedPane.getComponentAt(selectedId);
						simulationMode.setMode(simuPanel.getMode());
						simulationMode.setSimulationSpeed(simuPanel.getSimulationSpeed());
						Algorithm algo = simuPanel.getAlgorithm();
						if (algo != null) {
							simulationMode.setAlgorithmMessageList(algo.getMessageTypeList());
							simulationMode.labelAlgorithm.setText(algo.getClass().getSimpleName());
						} else {
							simulationMode.setAlgorithmMessageList(null);
							simulationMode.labelAlgorithm.setText("none");

						}
					}
					simulationMode.setActive(!activeEditionMode);
				}
			});
			tabbedPane.addContainerListener(new ContainerListener() {
				public void componentAdded(ContainerEvent e) {
				}

				public void componentRemoved(ContainerEvent e) {
					Component child = e.getChild();
					if (child instanceof GraphPanelSimulation)
						((GraphPanelSimulation) child).stopSimulation();
				}
			});
		}
		return tabbedPane;
	}

	/**
	 * Gets the edition graph panel.
	 * 
	 * @return the edition graph panel
	 */
	GraphPanelEdition getGraphEditionPanel() {
		int count = tabbedPane.getTabCount();
		for (int i = 0; i < count; ++i) {
			if (tabbedPane.getTitleAt(i).equals("Edition"))
				return (GraphPanelEdition) tabbedPane.getComponentAt(i);
		}

		return null; 
	}

	/**
	 * Gets the id of the edition graph panel.
	 * 
	 * @return the id of edition graph panel
	 */
	int getGraphEditionPanelTabId() {
		int count = tabbedPane.getTabCount();
		for (int i = 0; i < count; ++i) {
			if (tabbedPane.getTitleAt(i).equals("Edition"))
				return i;
		}

		return -1; 
	}

	/**
	 * Creates a new simulation tab.
	 * 
	 * @param graphSimulationPanel the graph simulation panel
	 * @param mode the mode
	 * @param tabTitle the tab title
	 */
	private void newSimulation(GraphPanelSimulation graphSimulationPanel, int mode, String tabTitle) {
		simulationMode.recordFile = null;
		simulationMode.setAlgorithmMessageList(null);
		simulationMode.labelAlgorithm.setText("none");
		JTabbedPane pane = getTabbedPane();
		pane.addTab(tabTitle, null, graphSimulationPanel, null); // TODO: adapt title to simulation type/number
		int newTabId = pane.getTabCount()-1;
		simulationMode.setMode(mode);
		pane.setSelectedIndex(newTabId);
		pane.setTabComponentAt(newTabId, new ClosableTab(pane));
	}

	/**
	 * Creates a new simulation tab.
	 * 
	 * @param graphSimulationPanel the graph simulation panel
	 * @param mode the mode
	 */
	void newSimulation(GraphPanelSimulation graphSimulationPanel, int mode) {
		this.newSimulation(graphSimulationPanel, mode, "Simulation");
	}

	/**
	 * Close all.
	 */
	public void closeAll() {
		while (tabbedPane.getTabCount() > 0)
			tabbedPane.remove(0);
	}

	/**
	 * Load a replay.
	 * 
	 * @param file the file
	 */
	private void loadReplay(File file) {
		try {
			FileInputStream fIS = new FileInputStream(file);
			ObjectInputStream oIS = new ObjectInputStream(fIS);

			// read the simulation mode
			int simulationMode = ((Integer) oIS.readObject()).intValue();

			// read the graph
			Object graphView = oIS.readObject(); // GraphView or SensorGraphView

			// read the number of repeated simulations
			int nbSimulations = ((Integer) oIS.readObject()).intValue();

			if (SimulationConstants.RunningMode.mobileMode(simulationMode)) {
				SensorGraphView sgv = ((SensorGraphView) graphView).cleanCopy();
				GraphPanelSensorSimulation graphSimulationPanel = new GraphPanelSensorSimulation(sgv, simulationMode, this);
				graphSimulationPanel.setNbSimulations(nbSimulations);
				this.newSimulation(graphSimulationPanel, simulationMode, "Replay");
				graphSimulationPanel.replaySimulation(oIS);
			} else {
				GraphView gv = ((GraphView) graphView).cleanCopy();
				GraphPanelSimulation graphSimulationPanel = new GraphPanelSimulation(gv, simulationMode, this);
				graphSimulationPanel.setNbSimulations(nbSimulations);
				this.newSimulation(graphSimulationPanel, simulationMode, "Replay");
				graphSimulationPanel.replaySimulation(oIS);
			}

			oIS.close();
			fIS.close();

		} catch (InvalidClassException e) {
			JOptionPane.showMessageDialog(VisidiaMain.getParentFrame(),	"Incompatible file version.", "Replay simulation", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
