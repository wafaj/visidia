package visidia.gui.window;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.ListSelectionModel;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

import visidia.VisidiaMain;
import visidia.simulation.SimulationConstants;
import visidia.gui.graphview.GraphItemView;
import visidia.gui.graphview.VertexView;
import visidia.gui.window.dialog.ClassFileChooser;
import visidia.gui.window.dialog.DialogAgentProperties;
import visidia.gui.window.rule.DialogRulesDefinition;
import visidia.io.AgentIO;
import visidia.io.AlgorithmIO;
import visidia.misc.CheckBoxList;
import visidia.misc.FileHandler;
import visidia.misc.VisidiaSettings;
import visidia.simulation.process.agent.Agent;
import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.process.messages.MessageType;
import visidia.simulation.process.synchronization.SynchronizationAlgorithm;

// TODO: Auto-generated Javadoc
/**
 * VisidiaPanelSimulationMode manages toolbars and menus when the GUI (VisidiaPanel) is in the simulation mode.
 */
public class VisidiaPanelSimulationMode implements VisidiaPanelMode, ActionListener, KeyListener {

	/** The visidia panel. */
	VisidiaPanel visidiaPanel = null;

	/** The simulation primary tool bar. */
	private JToolBar simulationPrimaryToolBar = null;

	/** The edition vertical secondary tool bar. */
	private JToolBar simulationVerticalSecondaryToolBar = null;
	private JPanel simulationVerticalThirdToolBar = null;

	/** The "start simulation" button. */
	private JButton buttonStartSimulation = null;

	/** The "pause simulation" button. */
	private JButton buttonPauseSimulation = null;

	/** The "stop simulation" button. */
	private JButton buttonStopSimulation = null;

	/** The simulation menu. */
	private JPopupMenu menuSimulation = null;

	/** The item set number of simulations. */
	private JMenuItem itemSetNbSimulations = null;

	/** The item define rules. */
	private JMenuItem itemDefineRules = null;

	/** The item select algo. */
	private JMenuItem itemSelectAlgo = null;

	/** The item select agent. */
	private JMenuItem itemSelectAgent = null;

	/** The item agent random placement. */
	private JMenuItem itemAgentRandomPlacement = null;

	/** The simulation button. */
	private MenuButton buttonSimulation = null;

	/** The mode. */
	private int mode = -1;

	/** The box for algorithm messages. */
	private Box boxAlgoMessages = null;

	/** The label algorithm. */
	JLabel labelAlgorithm;

	/** The list model. */
	private DefaultListModel listModel;

	/** The algorithm message table. */
	private Hashtable<JCheckBox, MessageType> algorithmMessageTable = new Hashtable<JCheckBox, MessageType>();

	/** The button recordAs. */
	private JButton buttonRecordAs = null;

	/** The button stats. */
	private JButton buttonStats = null;

	/** The box for agent toolbar. */
	Box boxAgentToolBar = null;

	/** The button to switch a vertex on/off. */
	private ToolBarButton buttonSwitchOnOffVertex = null;

	/** The button to kill an agent. */
	private ToolBarButton buttonKillAgent = null;

	/** The button to access agent properties. */
	private ToolBarButton buttonAgentProp = null;

	/** The number of agents specified by user for random placement. */
	private int nbAgents = 0;

	/** The speed slider. */
	private JSlider speedSlider;

	/** The button to increase the speed. */
	private JButton buttonFaster =null;

	/** The button to decrease the speed */
	private JButton buttonSlower =null;
	public static JToggleButton stepByStepButton=null;
	public JButton buttonNext=null;

	public static JList listAction;
	public static DefaultListModel model;
	public JScrollPane listScroller;
	public static JLabel redLabel;
	public static JCheckBox checkBoxMarkStep =null;
	public static JCheckBox checkBoxSyncStep =null;
	public static JCheckBox checkBoxPropertyStep =null;

	JPanel descriptionpanel=null;
	GridBagConstraints panelConstraits;

	/** The record file. */
	File recordFile = null;

	boolean testSimulationStart = false;
	private boolean testStep = false;
	
	
	/**
	 * Instantiates a new simulation mode.
	 * 
	 * @param visidiaPanel the visidia panel
	 */
	VisidiaPanelSimulationMode(VisidiaPanel visidiaPanel) {
		this.visidiaPanel = visidiaPanel;
		visidiaPanel.addKeyListener(this);
		visidiaPanel.setFocusable(true);
		this.buildMenu();
		this.buildToolBars();
	}

	/**
	 * Gets the menu button.
	 * 
	 * @return the menu button
	 */
	public MenuButton getMenuButton() {
		return buttonSimulation;
	}

	/**
	 * Gets the primary tool bar.
	 * 
	 * @return the primary tool bar
	 */
	public JToolBar getPrimaryToolBar() {
		return simulationPrimaryToolBar;
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
		return simulationVerticalSecondaryToolBar;
	}

	public JPanel getVerticalThirdToolBar() {
		return simulationVerticalThirdToolBar;
	}

	public boolean isStepSelected() {
		return testStep;
	}

	/**
	 * Processes the action events.
	 * 
	 * @param evt the event
	 * 
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent evt) {

		GraphPanelSimulation simulationPanel = (GraphPanelSimulation) visidiaPanel.getTabbedPane().getSelectedComponent();
		if (simulationPanel == null) return;

		if (evt.getSource().equals(buttonStartSimulation)) {

			if(buttonStartSimulation.getText().compareTo("Start") == 0){
				
				if( !buttonStopSimulation.isEnabled() )
					buttonStopSimulation.setEnabled(true);

				if (recordFile != null) {
					try {
						buttonStartSimulation.setIcon(new ImageIcon("/pause.png"));
					} catch (Exception ex) { }

					buttonStartSimulation.setText("Pause");
					simulationPanel.recordSimulation(recordFile);
				} else{
					try {
						buttonStartSimulation.setIcon(new ImageIcon("/pause.png"));
						VisidiaPanelSimulationMode.redLabel.setIcon(new ImageIcon("/green.png"));
					} catch (Exception ex) { }

					buttonStartSimulation.setText("Pause");
					
					if( testSimulationStart == false ) {
						simulationPanel.startSimulation();
						testSimulationStart = simulationPanel.isAlgorithmPutOnNodes();
					} else {
						simulationPanel.pauseSimulation();
					}
				}
			}
			else{
				buttonStartSimulation.setText("Start");
				buttonStartSimulation.setIcon(new ImageIcon("/play.png"));
				simulationPanel.pauseSimulation();
			}
		} else if (evt.getSource().equals(buttonPauseSimulation)) {
			simulationPanel.pauseSimulation();
		} else if (evt.getSource().equals(buttonStopSimulation)) {
			buttonStartSimulation.setText("Start");
			buttonStartSimulation.setIcon(new ImageIcon("/play.png"));
			buttonStopSimulation.setEnabled(false);
			//simulationPanel.stopSimulation();
			simulationPanel.stopSM();
			testSimulationStart=false;
			VisidiaPanelSimulationMode.model.removeAllElements();
			VisidiaPanelSimulationMode.redLabel.setIcon(new ImageIcon("/red.png"));

		} else if (evt.getSource().equals(buttonSwitchOnOffVertex)) {
			simulationPanel.switchSelectedVerticesOnOff();
		} else if (evt.getSource().equals(buttonKillAgent)) {
			if (simulationPanel.console ==  null) 
				return;
			Object[] agents = simulationPanel.console.getActiveAgents();
			Agent ag = (Agent) JOptionPane.showInputDialog(simulationPanel, "Select the agent:", "Agent's Killer", JOptionPane.PLAIN_MESSAGE, null, agents, null);
			
			if (ag != null) 
				simulationPanel.console.killAgent(ag);
		} else if (evt.getSource().equals(buttonAgentProp)) {
			if (simulationPanel.console ==  null) 
				return;
			Object[] agents = simulationPanel.console.getActiveAgents();
			Agent ag = (Agent) JOptionPane.showInputDialog(simulationPanel, "Select the agent:", "Agent's properties", JOptionPane.PLAIN_MESSAGE, null, agents, null);
			if (ag != null)	
				new DialogAgentProperties(VisidiaMain.getParentFrame(), ag).setVisible(true);
			
		} else if(evt.getSource().equals(buttonFaster)){
			speedSlider.setValue(speedSlider.getValue()+1);
		} else if(evt.getSource().equals(buttonSlower)){
			speedSlider.setValue(speedSlider.getValue()-1);
		} else if(evt.getSource().equals(buttonNext)){
			simulationPanel.pauseSimulation();
		}

	}

	/**
	 * Sets the simulation speed.
	 * 
	 * @param speed the new speed
	 */
	public void setSimulationSpeed(int speed) {
		speedSlider.setValue(speed);
	}

	/**
	 * Builds the tool bars.
	 */
	private void buildToolBars() {
		// simulationPrimaryToolBar		
		simulationPrimaryToolBar = new JToolBar();

		buttonStartSimulation = new ToolBarButton("Start", "/play.png", "", -1, this);
		buttonStartSimulation.setPreferredSize(new Dimension(70,30));
		simulationPrimaryToolBar.add(buttonStartSimulation);

		buttonPauseSimulation = new ToolBarButton("Pause", "/pause.png", "", -1, this);
		//simulationPrimaryToolBar.add(buttonPauseSimulation);

		buttonStopSimulation = new ToolBarButton("Stop", "/stop.png", "", -1, this);

		buttonStopSimulation.addActionListener(this);

		simulationPrimaryToolBar.add(buttonStopSimulation);
		buttonFaster = new ToolBarButton("", "/faster.png","Increase speed", -1, this);
		buttonFaster.addActionListener(this);

		buttonSlower = new ToolBarButton("", "/slower.png","Decrease speed", -1, this);
		stepByStepButton = new JToggleButton("Step By Step", false);
		stepByStepButton.setBackground(Color.cyan);

		stepByStepButton.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent arg0) {
				// TODO Auto-generated method stub
				if(stepByStepButton.isSelected()){
					visidiaPanel.showStepByStepPanel();
				}
				else{
					visidiaPanel.hideStepByStepPanel();
					stepByStepButton.setBackground(Color.cyan);

				}
			}
		});
		
		buttonSlower.addActionListener(this);
		JLabel speedLabel= new JLabel();
		speedLabel.setIcon(new ImageIcon("/speed_icon.png"));
		simulationPrimaryToolBar.add(speedLabel);
		simulationPrimaryToolBar.add(buttonSlower);


		speedSlider = new JSlider(1, 20, 10);
		speedSlider.setMinimumSize(new Dimension(150, 10));
		speedSlider.setSize(speedSlider.getMinimumSize());
		speedSlider.setPreferredSize(speedSlider.getMinimumSize());
		/******************/
		speedSlider.setMajorTickSpacing(5); 
		speedSlider.setMinorTickSpacing(1); 
		speedSlider.setPaintTicks(true); 
		speedSlider.setPaintLabels(false); 
		/********************/
		speedSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				
				JSlider source = (JSlider) e.getSource();
				if (!source.getValueIsAdjusting()) {
					Component c = visidiaPanel.getTabbedPane().getSelectedComponent();
					if (! (c instanceof GraphPanelSimulation)) 
						return;
					
					GraphPanelSimulation simulationPanel = (GraphPanelSimulation) c;
					if (simulationPanel == null) 
						return;
					simulationPanel.updateDisplacementStep(source.getValue());
				}
			}
		});
		
		simulationPrimaryToolBar.add(speedSlider);
		simulationPrimaryToolBar.add(buttonFaster);
		simulationPrimaryToolBar.add(stepByStepButton);

		// simulationVerticalSecondaryToolBar
		simulationVerticalSecondaryToolBar = new JToolBar();
		simulationVerticalSecondaryToolBar.setOrientation(JToolBar.VERTICAL);
		simulationVerticalSecondaryToolBar.setLayout(new BorderLayout());

		simulationVerticalThirdToolBar = new JPanel();
		//simulationVerticalThirdToolBar.setOrientation(JToolBar.HORIZONTAL);
		simulationVerticalThirdToolBar.setLayout(new FlowLayout());
		Box vBox = Box.createVerticalBox();


		buttonRecordAs = new ToolBarButton("Record as", "/record.png", "", -1, this);
		buttonRecordAs.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				String path = VisidiaSettings.getInstance().getString(VisidiaSettings.Constants.VISIDIA_BASE_URL);
				JFileChooser chooser = new JFileChooser(path);
				FileNameExtensionFilter filter = new FileNameExtensionFilter("Replay files", "replay");
				chooser.setFileFilter(filter);
				int returnVal = chooser.showSaveDialog(VisidiaMain.getParentFrame());
				if (returnVal == JFileChooser.CANCEL_OPTION) return;
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					recordFile = chooser.getSelectedFile();
				}
			}
		});
		vBox.add(buttonRecordAs);

		vBox.add(Box.createRigidArea(new Dimension(0, 15)));

		buttonStats = new ToolBarButton("Statistics", "/statistics.png", "", -1, this);
		buttonStats.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				GraphPanelSimulation simulationPanel = (GraphPanelSimulation) visidiaPanel.getTabbedPane().getSelectedComponent();
				if (simulationPanel == null) return;
				simulationPanel.showStats();
			}
		});
		vBox.add(buttonStats);
		simulationVerticalSecondaryToolBar.add(vBox, BorderLayout.NORTH);

		boxAlgoMessages = Box.createVerticalBox();
		boxAlgoMessages.add(Box.createRigidArea(new Dimension(0, 30)));

		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());

		Box boxAlgoHeader = Box.createVerticalBox();
		boxAlgoHeader.add(new JLabel("<html>Algorithm:<br></html>"));
		labelAlgorithm = new JLabel("none");
		boxAlgoHeader.add(labelAlgorithm);
		boxAlgoHeader.add(Box.createRigidArea(new Dimension(0, 20)));
		boxAlgoHeader.add(new JLabel("<html>Displayed<br>messages:</html>"));
		boxAlgoHeader.add(Box.createRigidArea(new Dimension(0, 20)));
		panel.add(boxAlgoHeader, BorderLayout.NORTH);

		JList listAlgoMessages = new CheckBoxList();
		listAlgoMessages.setBackground(visidiaPanel.getBackground());
		listAlgoMessages.setOpaque(false);
		listModel = new DefaultListModel();
		listAlgoMessages.setModel(listModel);
		panel.add(listAlgoMessages, BorderLayout.CENTER);

		JScrollPane scroller = new JScrollPane(panel,
				ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED,
				ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		scroller.setMinimumSize(new Dimension(80, 180));
		scroller.setPreferredSize(scroller.getMinimumSize());
		scroller.setSize(scroller.getMinimumSize());
		scroller.revalidate();
		boxAlgoMessages.add(scroller);

		simulationVerticalSecondaryToolBar.add(boxAlgoMessages, BorderLayout.CENTER);
		JPanel p1=new JPanel();
		JPanel p2=new JPanel();
		JPanel preferencePanel=new JPanel();

		p1.setLayout(new GridLayout(2,1));
		checkBoxMarkStep = new JCheckBox("Marking Edge");
		checkBoxSyncStep = new JCheckBox("Synchronisation");
		checkBoxPropertyStep = new JCheckBox("Changing Properties");
		preferencePanel.setLayout(new GridLayout(3,1));
		preferencePanel.add(checkBoxMarkStep);
		preferencePanel.add(checkBoxSyncStep);
		preferencePanel.add(checkBoxPropertyStep);

		buttonNext =new JButton();
		buttonNext.setText("Next");

		buttonNext.setPreferredSize(new Dimension(70,30));
		try {
			buttonNext.setIcon(new ImageIcon("/next.png"));
		} catch (Exception ex) { }
		buttonNext.addActionListener(this);

		model = new DefaultListModel();
		listAction =new  JList( model );
		//listAction.setBackground(visidiaPanel.getBackground());
		listAction.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		//listAction.setLayoutOrientation(JList.VERTICAL);
		listScroller = new JScrollPane(listAction);

		listScroller.setPreferredSize(new Dimension(700, 150));
		descriptionpanel =new JPanel();
		descriptionpanel.add(listScroller);

		//listScroller.getVerticalScrollBar().setValue(listScroller.getVerticalScrollBar().getMaximum());



		/* listScroller.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {  
	        public void adjustmentValueChanged(AdjustmentEvent e) {  
	            e.getAdjustable().setValue(e.getAdjustable().getMaximum());  
	        }
	    }); */
		redLabel= new JLabel("");
		try {
			redLabel.setIcon(new ImageIcon("/red.png"));
		} catch (Exception ex) { }
		p2.add(buttonNext);
		p2.add(redLabel);

		simulationVerticalThirdToolBar.add(descriptionpanel);

		simulationVerticalThirdToolBar.add(preferencePanel);
		simulationVerticalThirdToolBar.add(p2);	


		boxAgentToolBar = Box.createVerticalBox();
		boxAgentToolBar.add(Box.createRigidArea(new Dimension(0, 30)));
		JToolBar agentToolBar = new JToolBar();
		agentToolBar.setOrientation(JToolBar.VERTICAL);
		agentToolBar.setAlignmentX(Component.LEFT_ALIGNMENT);
		agentToolBar.setFloatable(false);

		buttonSwitchOnOffVertex = new ToolBarButton("", "/switch_on_off.png", "Switch vertex on/off", -1, this);
		agentToolBar.add(buttonSwitchOnOffVertex);

		buttonKillAgent = new ToolBarButton("", "/agent_killer.png", "Remove agent", -1, this);
		agentToolBar.add(buttonKillAgent);

		buttonAgentProp = new ToolBarButton("", "/agent_prop.png", "Agent properties", -1, this);
		agentToolBar.add(buttonAgentProp);


		boxAgentToolBar.add(agentToolBar);
		simulationVerticalSecondaryToolBar.add(boxAgentToolBar, BorderLayout.SOUTH);
	}

	/**
	 * Sets the algorithm message list.
	 * 
	 * @param messageTypes the new algorithm message list
	 */
	void setAlgorithmMessageList(Collection<MessageType> messageTypes) {
		listModel.clear();
		algorithmMessageTable.clear();
		if (messageTypes == null) return;

		Enumeration<MessageType> elements = Collections.enumeration(messageTypes);
		while (elements.hasMoreElements()) {
			MessageType mType = elements.nextElement();
			JCheckBox cb = new JCheckBox(mType.getType());
			cb.setSelected(mType.getToPaint());
			cb.setForeground(mType.getColor());
			cb.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e) {
					JCheckBox source = (JCheckBox) e.getItemSelectable();
					MessageType mType = algorithmMessageTable.get(source);
					mType.setToPaint(source.isSelected());
				}
			});
			algorithmMessageTable.put(cb, mType);
			listModel.addElement(cb);			
		}
	}

	/**
	 * Loads an algorithm.
	 */
	private void loadAlgorithm() {
		String visidiaAlgoPath = VisidiaSettings.getInstance().getString(VisidiaSettings.Constants.VISIDIA_ALGO_PATH);
		String rootDir = FileHandler.getRootDirectory(visidiaAlgoPath);
		if (rootDir == null) return;

		ClassFileChooser fc = new ClassFileChooser(VisidiaMain.getParentFrame(), rootDir, visidiaAlgoPath, AlgorithmIO.class, null);
		int returnVal = fc.showDialog();
		if (returnVal == JFileChooser.CANCEL_OPTION) return;
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			Algorithm algo = (Algorithm) fc.getSelectedObject();
			if (algo != null && !(algo instanceof SynchronizationAlgorithm)) { // load is successful
				GraphPanelSimulation simulationPanel = (GraphPanelSimulation) visidiaPanel.getTabbedPane().getSelectedComponent();
				if (simulationPanel != null) {
					simulationPanel.setAlgorithm(algo);
					setAlgorithmMessageList(algo.getMessageTypeList());
					labelAlgorithm.setText(algo.getClass().getSimpleName());
					buttonStartSimulation.setEnabled(true);
					buttonStopSimulation.setEnabled(false);
					speedSlider.setEnabled(true);
					buttonSlower.setEnabled(true);
					buttonFaster.setEnabled(true);
					buttonRecordAs.setEnabled(true);
					buttonStats.setEnabled(true);
					if(buttonStartSimulation.getText().compareTo("Pause")==0){
						buttonStartSimulation.setText("Start");
						buttonStartSimulation.setIcon(new ImageIcon("/play.png"));
					}
				}
			}
		}
	}

	/**
	 * Select agent.
	 * 
	 * @param accessory the accessory
	 * 
	 * @return the agent
	 */
	private Agent selectAgent(JComponent accessory) {
		String visidiaAgentPath = VisidiaSettings.getInstance().getString(VisidiaSettings.Constants.VISIDIA_AGENT_PATH);
		String rootDir = FileHandler.getRootDirectory(visidiaAgentPath);
		if (rootDir == null) return null;

		ClassFileChooser fc = new ClassFileChooser(VisidiaMain.getParentFrame(), rootDir, visidiaAgentPath, AgentIO.class, accessory);
		int returnVal = fc.showDialog();
		if (returnVal == JFileChooser.CANCEL_OPTION) return null;
		if (returnVal == JFileChooser.APPROVE_OPTION)
			return (Agent) fc.getSelectedObject();
		return null;
	}

	/**
	 * Loads an agent.
	 */
	private void loadAgent() {
		GraphPanelSimulation simulationPanel = (GraphPanelSimulation) visidiaPanel.getTabbedPane().getSelectedComponent();
		if (simulationPanel == null) return;

		boolean noVertexSelected = true;
		Enumeration<GraphItemView> items = simulationPanel.selection.elements();
		while (items.hasMoreElements()) {
			if (items.nextElement() instanceof VertexView) {
				noVertexSelected = false;
				break;
			}
		}

		if (noVertexSelected) {
			JOptionPane.showMessageDialog(VisidiaMain.getParentFrame(),	"Please first select at least one vertex", "Agent selection", JOptionPane.ERROR_MESSAGE);
			return;
		}
		buttonStartSimulation.setEnabled(true);
		buttonStopSimulation.setEnabled(false);
		speedSlider.setEnabled(true);
		buttonSlower.setEnabled(true);
		buttonFaster.setEnabled(true);
		buttonRecordAs.setEnabled(true);
		buttonStats.setEnabled(true);
		Agent agent = selectAgent(null);

		if(buttonStartSimulation.getText().compareTo("Pause")==0){
			buttonStartSimulation.setText("Start");
			buttonStartSimulation.setIcon(new ImageIcon("/play.png"));
		}
		if (agent != null) simulationPanel.setAgent(agent);

	}

	/**
	 * Randomly place agents.
	 */
	private void randomlyPlaceAgents() {
		GraphPanelSimulation simulationPanel = (GraphPanelSimulation) visidiaPanel.getTabbedPane().getSelectedComponent();
		if (simulationPanel == null) return;

		int nbVertices = ((GraphPanelSimulation) this.visidiaPanel.getTabbedPane().getSelectedComponent()).graphView.getNbVertices();
		nbAgents = nbVertices / 2;

		Box box = Box.createVerticalBox();
		box.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		JLabel label = new JLabel("Number of agents to place:"); 
		label.setAlignmentX(Box.CENTER_ALIGNMENT);
		box.add(label);
		box.add(Box.createRigidArea(new Dimension(0, 5)));
		label = new JLabel("(max = " + nbVertices + ")");
		label.setAlignmentX(Box.CENTER_ALIGNMENT);
		box.add(label);
		box.add(Box.createRigidArea(new Dimension(0, 10)));

		final JFormattedTextField textField = new JFormattedTextField();
		textField.setAlignmentX(Box.CENTER_ALIGNMENT);
		textField.setValue(nbAgents);
		textField.setMaximumSize(new Dimension(100, 30));
		textField.setPreferredSize(textField.getMaximumSize());
		textField.addPropertyChangeListener("value", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				nbAgents = ((Number) textField.getValue()).intValue();
			}
		});
		box.add(textField);

		Agent agent = selectAgent(box);
		if (agent == null) return;
		if (nbAgents > nbVertices) nbAgents = nbVertices;

		boolean[] mark = new boolean[nbVertices];
		for (int i = 0; i < nbVertices; ++i) mark[i] = false;

		Random r = new Random();
		while (nbAgents > 0) {
			int n = r.nextInt(nbVertices);
			if (mark[n] == true) continue;
			mark[n] = true;
			nbAgents --;
			simulationPanel.placeAgentOnVertex((Agent) agent.clone(), n);
		}
	}

	/**
	 * Define rules.
	 */
	private void defineRules() {
		GraphPanelSimulation simulationPanel = (GraphPanelSimulation) visidiaPanel.getTabbedPane().getSelectedComponent();
		if (simulationPanel == null) return;

		if (SimulationConstants.RunningMode.agentsMode(mode) && simulationPanel.selection.isEmpty()) {
			JOptionPane.showMessageDialog(VisidiaMain.getParentFrame(),	"Please first select at least one vertex", "Agent selection", JOptionPane.ERROR_MESSAGE);
			return;

		}


		new DialogRulesDefinition(VisidiaMain.getParentFrame(), simulationPanel).setVisible(true);
	}

	/**
	 * Sets the number of simulations.
	 */
	private void setNbSimulations() {
		GraphPanelSimulation simulationPanel = (GraphPanelSimulation) visidiaPanel.getTabbedPane().getSelectedComponent();
		if (simulationPanel == null) return;

		String s = JOptionPane.showInputDialog(VisidiaMain.getParentFrame(), "Enter the number of simulations:", new Integer(1));

		int nbSimulations = 1;
		try {
			nbSimulations = Integer.parseInt(s);
		} catch (Exception e) {
			if (s != null)
				JOptionPane.showMessageDialog(VisidiaMain.getParentFrame(),	"Incorrect entry; using default value.", "Multiple selection", JOptionPane.ERROR_MESSAGE);
		}

		simulationPanel.setNbSimulations(nbSimulations);
	}

	/**
	 * Builds the menu.
	 */
	private void buildMenu() {
		ActionListener menuListener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuSimulation.setVisible(false);
				if (e.getSource().equals(itemSetNbSimulations)) {
					setNbSimulations();
				} else if (e.getSource().equals(itemDefineRules)) {
					defineRules();
				} else if (e.getSource().equals(itemSelectAlgo)) {
					loadAlgorithm();
				} else if (e.getSource().equals(itemSelectAgent)) {
					loadAgent();
				} else if (e.getSource().equals(itemAgentRandomPlacement)) {
					randomlyPlaceAgents();
				}
			}
		};

		menuSimulation = new JPopupMenu();

		itemSetNbSimulations = new JMenuItem("Set number of simulations");
		itemSetNbSimulations.addActionListener(menuListener);
		menuSimulation.add(itemSetNbSimulations);

		menuSimulation.addSeparator();

		itemDefineRules = new JMenuItem("Define rewriting rules");
		itemDefineRules.addActionListener(menuListener);
		menuSimulation.add(itemDefineRules);

		// for messages
		itemSelectAlgo = new JMenuItem("Select algorithm");
		itemSelectAlgo.addActionListener(menuListener);
		menuSimulation.add(itemSelectAlgo);

		// for agents
		itemSelectAgent = new JMenuItem("Select agent");
		itemSelectAgent.addActionListener(menuListener);
		menuSimulation.add(itemSelectAgent);

		itemAgentRandomPlacement = new JMenuItem("Randomly place agents");
		itemAgentRandomPlacement.addActionListener(menuListener);
		menuSimulation.add(itemAgentRandomPlacement);


		buttonSimulation = new MenuButton("Simulation", menuSimulation);
	}

	/**
	 * Sets if the mode is active.
	 * 
	 * @param isActive the active state
	 */
	public void setActive(boolean isActive) {
		if (isActive) {
			itemSelectAlgo.setVisible(SimulationConstants.RunningMode.messagesMode(mode));
			itemSelectAgent.setVisible(SimulationConstants.RunningMode.agentsMode(mode));
			itemAgentRandomPlacement.setVisible(SimulationConstants.RunningMode.agentsMode(mode));
			boxAlgoMessages.setVisible(SimulationConstants.RunningMode.messagesMode(mode));
			boxAgentToolBar.setVisible(SimulationConstants.RunningMode.agentsMode(mode));
		}

		buttonSimulation.setVisible(isActive);
		simulationPrimaryToolBar.setVisible(isActive);
		simulationVerticalSecondaryToolBar.setVisible(isActive);
	}

	/**
	 * Sets the mode.
	 * 
	 * @param mode the new mode
	 */
	public void setMode(int mode) {
		this.mode = mode;
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode()== KeyEvent.VK_SPACE && !visidiaPanel.editionMode.editionHorizontalSecondaryToolBar.isVisible())
			loadAlgorithm();

	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}

}
