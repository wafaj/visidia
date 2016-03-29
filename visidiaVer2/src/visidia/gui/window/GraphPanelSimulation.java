package visidia.gui.window;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import visidia.VisidiaMain;
import visidia.examples.rule.LC1Rule;
import visidia.examples.rule.LC2Rule;
import visidia.examples.rule.RDVRule;
import visidia.graph.Edge;
import visidia.graph.Vertex;
import visidia.gui.graphview.EdgeView;
import visidia.gui.graphview.GraphItemView;
import visidia.gui.graphview.GraphView;
import visidia.gui.graphview.MovableAgent;
import visidia.gui.graphview.MovableItem;
import visidia.gui.graphview.MovableMessage;
import visidia.gui.graphview.VertexView;
import visidia.gui.window.dialog.DialogVisidiaStat;
import visidia.misc.property.PropertyTable;
import visidia.misc.property.VisidiaProperty;
import visidia.rule.RSOptions;
import visidia.rule.RelabelingSystem;
import visidia.simulation.Console;
import visidia.simulation.SimulationConstants;
import visidia.simulation.SimulationConstants.SimulationStatus;
import visidia.simulation.SimulationConstants.SimulationType;
import visidia.simulation.command.CommandListener;
import visidia.simulation.evtack.VisidiaEvent;
import visidia.simulation.playrec.ObjectWriter;
import visidia.simulation.playrec.ReplayInfo;
import visidia.simulation.process.MessageProcess;
import visidia.simulation.process.ProcessType;
import visidia.simulation.process.agent.Agent;
import visidia.simulation.process.agent.AgentRules;
import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.process.algorithm.RuleAlgorithm;
import visidia.simulation.process.edgestate.EdgeState;
import visidia.simulation.process.edgestate.MarkedState;
import visidia.simulation.process.edgestate.SyncState;
import visidia.simulation.process.messages.Message;
import visidia.simulation.process.synchronization.SynCT;
import visidia.simulation.process.synchronization.SynchronizationObject;
import visidia.simulation.process.synchronization.SynchronizationObjectTerminationRules;
import visidia.stats.Statistics;

// TODO: Auto-generated Javadoc
/**
 * GraphPanelSimulation extends GraphPanel, for use in simulation context.
 */
public class GraphPanelSimulation extends GraphPanel implements CommandListener, ActionListener, MouseListener {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = -9213783713436513924L;

	/** The console. */
	Console console = null;

	/** The simulation mode. */
	private int simulationMode;

	/** The timer. */
	private javax.swing.Timer timer = null;

	/** The concurrent object (used as a lock). */
	protected Object concurrentObject = new Object();

	/** The wait object (used as a lock). */
	private Object waitObject = new Object();

	/** The Constant DISPLACEMENT_STEP_DEFAULT. */
	private final static int DISPLACEMENT_STEP_DEFAULT = 10;

	/** The messages displacement step. */
	protected int[] displacementStep = new int[1];

	/** The current algorithm. */
	private Algorithm currentAlgorithm = null;

	/** The agent items. */
	private Hashtable<Integer, MovableAgent> agentItems = new Hashtable<Integer, MovableAgent>();

	/** The animated items. */
	protected Vector<MovableItem> animatedItems = new Vector<MovableItem>(10, 10);

	/** The initial properties. */
	VertexProperties[] initialproperties = null;

	/** The initial agents. */
	Vector<VertexAgent> initialAgents = new Vector<VertexAgent>();

	/** The dialog for statistics. */
	DialogVisidiaStat dialogStats = null;

	/** The simulation id. */
	int simulationId = 1;

	/** The object writer. */
	ObjectWriter writer = new ObjectWriter();

	/** The replay info. */
	ReplayInfo replayInfo;

	/** The simulation type. */
	SimulationType simulationType = SimulationType.STANDARD;

	/** The parent. */
	private VisidiaPanel parent = null;

	/** The multi simu thread. */
	MultiSimuThread multiSimuThread = null;

	/** The number of simulations. */
	private int nbSimulations = 1;
	
	private boolean isAlgorithmPutOnNodes = false;

	/**
	 * Instantiates a new graph panel in simulation mode.
	 * 
	 * @param graphView the graph view
	 * @param simulationMode the simulation mode
	 * @param parent the parent
	 */
	protected GraphPanelSimulation(GraphView graphView, int simulationMode, VisidiaPanel parent) {
		super(graphView, GraphPanel.DrawingPanelMode.SIMULATION, parent.getDisplayGraph());

		this.parent = parent;
		this.simulationMode = simulationMode;
		this.timer = new javax.swing.Timer(30, this);
		this.displacementStep[0] = DISPLACEMENT_STEP_DEFAULT;
		parent.simulationMode.setSimulationSpeed(this.displacementStep[0]);
		multiSimuThread = new MultiSimuThread();

		dialogStats = new DialogVisidiaStat(VisidiaMain.getParentFrame(), null);
		dialogStats.pack();

		this.setDisplayGraph(parent.getDisplayGraph());
	}

	/**
	 * Sets the number of simulations.
	 * 
	 * @param nbSimulations the new number of simulations
	 */
	void setNbSimulations(int nbSimulations) {
		this.nbSimulations = nbSimulations;
	}

	/* (non-Javadoc)
	 * @see visidia.gui.window.GraphPanel#setDisplayGraph(boolean)
	 */
	public void setDisplayGraph(boolean displayGraph) {
		drawingPanel.removeMouseListener(this);
		if (displayGraph) drawingPanel.addMouseListener(this);

		super.setDisplayGraph(displayGraph);
	}

	/**
	 * Gets the mode.
	 * 
	 * @return the mode
	 */
	public int getMode() {
		return simulationMode;
	}

	/**
	 * Gets the algorithm.
	 * 
	 * @return the algorithm
	 */
	public Algorithm getAlgorithm() {
		return currentAlgorithm;
	}

	/**
	 * Sets the algorithm.
	 * 
	 * @param algorithm the new algorithm
	 */
	public void setAlgorithm(Algorithm algorithm) {
		currentAlgorithm = algorithm;
	}
	
	/**
	 * Cheks if the algorithm is put on nodes
	 */
	public boolean isAlgorithmPutOnNodes(){
		return isAlgorithmPutOnNodes;
	}

	/**
	 * Sets the agent.
	 * 
	 * @param agent the new agent
	 */
	public void setAgent(Agent agent) {
		Enumeration<GraphItemView> selected = selection.elements();
		while (selected.hasMoreElements()) {
			GraphItemView item = selected.nextElement();
			if (item instanceof VertexView) {
				VertexView view = (VertexView) item;
				int vertexId = view.getVertex().getId();
				placeAgentOnVertex(agent, vertexId);
			}
		}
		repaint();
	}

	/**
	 * Place agent on vertex.
	 * 
	 * @param agent the agent
	 * @param vertexId the vertex id
	 */
	public void placeAgentOnVertex(Agent agent, int vertexId) {
		Point a = graphView.getVertexView(vertexId).getPosition();
		MovableAgent item = new MovableAgent(agent, a, a, this.displacementStep, null);
		item.draw(this.getGraphics());
		agentItems.put(initialAgents.size(), item);
		graphView.addDecorativeItem(item);
		initialAgents.add(new VertexAgent(graphView.getVertexView(vertexId).getVertex(), agent));
	}

	/**
	 * Sets the rule-based agent.
	 * 
	 * @param rSys the relabeling system
	 */
	private void setAgentRules(RelabelingSystem rSys) {
		AgentRules agent = new AgentRules();
		agent.setRule(rSys);
		this.setAgent(agent);
	}

	/**
	 * Show statistics.
	 */
	public void showStats() {
		Statistics stats = null;
		if (console != null) stats = console.getStats();
		dialogStats.updatedStats(stats);
		dialogStats.setVisible(true);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JComponent#setVisible(boolean)
	 */
	public void setVisible(boolean visible) {
		if (!visible && dialogStats != null) dialogStats.setVisible(false);
		super.setVisible(visible);
	}

	/**
	 * Record simulation.
	 * 
	 * @param recordFile the record file
	 */
	public void recordSimulation(File recordFile) {
		writer.close();
		if (! writer.open(recordFile)) {
			JOptionPane.showMessageDialog(VisidiaMain.getParentFrame(),	"Unable to open this file: " + recordFile.getName(), "Record simulation", JOptionPane.ERROR_MESSAGE);
			return;
		}

		writer.writeObject(new Integer(simulationMode));
		writer.writeObject(graphView);
		writer.writeObject(new Integer(nbSimulations));

		simulationType = SimulationType.RECORD;
		startSimulation();
	}

	/**
	 * Replay simulation.
	 * 
	 * @param oIS the o is
	 */
	public void replaySimulation(ObjectInputStream oIS) {
		VertexProperties[] initProp = null;
		Vector<VertexAgent> initAgents = new Vector<VertexAgent>();

		try {
			initProp = (VertexProperties[]) oIS.readObject();
			if (SimulationConstants.RunningMode.agentsMode(simulationMode))
				initAgents = (Vector<VertexAgent>) oIS.readObject();
		} catch (InvalidClassException e) {
			JOptionPane.showMessageDialog(VisidiaMain.getParentFrame(),	"Incompatible file version.", "Replay simulation", JOptionPane.ERROR_MESSAGE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// read the simulation events
		replayInfo = new ReplayInfo();
		if (! replayInfo.read(oIS))	return; // error

		this.initialproperties = initProp;
		this.initialAgents = initAgents;

		initVertexProperties();

		if (SimulationConstants.RunningMode.agentsMode(simulationMode)) {
			int id = 0;
			Enumeration<VertexAgent> startVertices = initAgents.elements();
			while (startVertices.hasMoreElements()) {
				VertexAgent vAgent = startVertices.nextElement();
				vAgent.agent = new ReplayAgent(vAgent.agentName);
				initAgentItem(vAgent, id++);
			}
		}

		simulationType = SimulationType.REPLAY;
	}

	/**
	 * Initializes the vertex properties.
	 */
	private void initVertexProperties() {
		if (initialproperties != null) {
			graphView.resetItemsProperties();
			for (VertexProperties vProps : initialproperties) {
				Set<Map.Entry<Object, VisidiaProperty>> props = vProps.props.entrySet();
				for (Map.Entry<Object, VisidiaProperty> entry : props) {
					nodePropertyChanged(vProps.id, entry.getValue());
				}
			}
		}
	}

	/**
	 * Inits the agent item.
	 * 
	 * @param vAgent the v agent
	 * @param id the id
	 */
	private void initAgentItem(VertexAgent vAgent, int id) {
		int vertexId = vAgent.vertex.getId();
		Point a = graphView.getVertexView(vertexId).getPosition();
		MovableAgent item = new MovableAgent(vAgent.agent, a, a, this.displacementStep, null);
		agentItems.put(id, item);
		graphView.addDecorativeItem(item);
	}

	/**
	 * New version of stop simulation.
	 */

	public void stopSM() {

		Statistics stats = null;

		if (console != null) {
			if (simulationId > 1) stats = console.getStats();			
			console.reset();
			multiSimuThread.abort();
		} else {
			if (simulationType != SimulationType.REPLAY) {
				// first time the simulation is run --> store vertices initial properties
				int nbVertices = graphView.getNbVertices();
				initialproperties = new VertexProperties[nbVertices];
				int n = 0;
				Enumeration<Vertex> vertices = graphView.getGraph().getVertices();
				while (vertices.hasMoreElements()) {
					Vertex v = vertices.nextElement();
					int vertexId = v.getId();
					initialproperties[n++] = new VertexProperties(vertexId, (PropertyTable) v.clone());
				}
			}

			if (simulationType == SimulationType.RECORD && simulationId == 1) {
				writer.writeObject(initialproperties);
				if (SimulationConstants.RunningMode.agentsMode(simulationMode)) {
					writer.writeObject(initialAgents);
				}
			}
		}

		if (replayInfo != null && simulationId == 1) replayInfo.rewind();

		multiSimuThread = new MultiSimuThread();
		multiSimuThread.start();

		console = new Console(simulationMode, simulationType, graphView.getGraph(), writer, replayInfo);
		
		if (replayInfo != null) 
			replayInfo.setConsole(console);
		
		console.addCommandListener(this);
		console.setSimulationId(simulationId);
		
		if (simulationId > 1 && stats != null) 
			console.setStats(stats);
		
		dialogStats.updatedStats(console.getStats());
		console.getStats().addStatListener(dialogStats);

		this.drawingPanel.setInfoText("Simulation " + simulationId + " is running");

		Enumeration<MovableItem> animItems = animatedItems.elements();
		
		while (animItems.hasMoreElements()) 
			graphView.removeDecorativeItem(animItems.nextElement());
		
		animatedItems.clear();

		if (SimulationConstants.RunningMode.messagesMode(simulationMode)) {
			if (currentAlgorithm != null) {
				console.putAlgorithmOnNodes(currentAlgorithm);
			} else if (simulationType != SimulationType.REPLAY) {
				JOptionPane.showMessageDialog(VisidiaMain.getParentFrame(),	"Please select an algorithm", "Simulation start", JOptionPane.ERROR_MESSAGE);
				return;
			}
			initVertexProperties();
		} else if (SimulationConstants.RunningMode.agentsMode(simulationMode)) {
			Enumeration<MovableAgent> items = agentItems.elements();
			while (items.hasMoreElements()) 
				graphView.removeDecorativeItem(items.nextElement());
			
			agentItems.clear();

			initVertexProperties();

			if (!initialAgents.isEmpty()) {
				Enumeration<VertexAgent> startVertices = initialAgents.elements();
				int id = 0;
				while (startVertices.hasMoreElements()) {
					VertexAgent vAgent = startVertices.nextElement();
					if (this.simulationType == SimulationType.REPLAY)
						console.addAgentToVertex(vAgent.vertex, vAgent.agent);
					else
						vAgent.agent = console.createAgentOnVertex(vAgent.vertex, vAgent.agent);

					initAgentItem(vAgent, id++);
				}
			} else if (simulationType != SimulationType.REPLAY) {
				JOptionPane.showMessageDialog(VisidiaMain.getParentFrame(),	"Please select an agent", "Simulation start", JOptionPane.ERROR_MESSAGE);
				return;
			}

		}

		revalidate();
		repaint();
		concurrentObject = new Object();
		waitObject = new Object();


	}

	/**
	 * Starts simulation.
	 */
	public void startSimulation() {
		Statistics stats = null;

		if (console != null) {
			if (simulationId > 1) stats = console.getStats();			
			console.reset();
			multiSimuThread.abort();
		} else {
			if (simulationType != SimulationType.REPLAY) {
				// first time the simulation is run --> store vertices initial properties
				int nbVertices = graphView.getNbVertices();
				initialproperties = new VertexProperties[nbVertices];
				int n = 0;
				Enumeration<Vertex> vertices = graphView.getGraph().getVertices();
				while (vertices.hasMoreElements()) {
					Vertex v = vertices.nextElement();
					int vertexId = v.getId();
					initialproperties[n++] = new VertexProperties(vertexId, (PropertyTable) v.clone());
				}
			}

			if (simulationType == SimulationType.RECORD && simulationId == 1) {
				writer.writeObject(initialproperties);
				if (SimulationConstants.RunningMode.agentsMode(simulationMode)) {
					writer.writeObject(initialAgents);
				}
			}
		}

		if (replayInfo != null && simulationId == 1) replayInfo.rewind();

		multiSimuThread = new MultiSimuThread();
		multiSimuThread.start();

		console = new Console(simulationMode, simulationType, graphView.getGraph(), writer, replayInfo);
		if (replayInfo != null) replayInfo.setConsole(console);
		console.addCommandListener(this);
		console.setSimulationId(simulationId);
		if (simulationId > 1 && stats != null) console.setStats(stats);
		dialogStats.updatedStats(console.getStats());
		console.getStats().addStatListener(dialogStats);

		this.drawingPanel.setInfoText("Simulation " + simulationId + " is running");

		Enumeration<MovableItem> animItems = animatedItems.elements();
		while (animItems.hasMoreElements()) graphView.removeDecorativeItem(animItems.nextElement());
		animatedItems.clear();

		if (SimulationConstants.RunningMode.messagesMode(simulationMode)) {
			if (currentAlgorithm != null) {
				console.putAlgorithmOnNodes(currentAlgorithm);
				isAlgorithmPutOnNodes = true;
			} else if (simulationType != SimulationType.REPLAY) {
				JOptionPane.showMessageDialog(VisidiaMain.getParentFrame(),	"Please select an algorithm", "Simulation start", JOptionPane.ERROR_MESSAGE);
				isAlgorithmPutOnNodes = false;
				return;
			}
			initVertexProperties();
		} else if (SimulationConstants.RunningMode.agentsMode(simulationMode)) {
			Enumeration<MovableAgent> items = agentItems.elements();
			while (items.hasMoreElements()) graphView.removeDecorativeItem(items.nextElement());
			agentItems.clear();

			initVertexProperties();

			if (!initialAgents.isEmpty()) {
				Enumeration<VertexAgent> startVertices = initialAgents.elements();
				int id = 0;
				while (startVertices.hasMoreElements()) {
					VertexAgent vAgent = startVertices.nextElement();
					if (this.simulationType == SimulationType.REPLAY)
						console.addAgentToVertex(vAgent.vertex, vAgent.agent);
					else
						vAgent.agent = console.createAgentOnVertex(vAgent.vertex, vAgent.agent);

					initAgentItem(vAgent, id++);
				}
			} else if (simulationType != SimulationType.REPLAY) {
				JOptionPane.showMessageDialog(VisidiaMain.getParentFrame(),	"Please select an agent", "Simulation start", JOptionPane.ERROR_MESSAGE);
				return;
			}

		}

		revalidate();
		repaint();
		concurrentObject = new Object();
		waitObject = new Object();
		timer.start();
		console.start();
	}

	/**
	 * Pauses simulation.
	 */
	public void pauseSimulation() {
		if (console == null) return;
		if (console.getStatus() == SimulationStatus.STARTED) {
			timer.stop();
			console.pause();
			 VisidiaPanelSimulationMode.redLabel.setIcon(new ImageIcon("/red.png"));
		} else if (console.getStatus() == SimulationStatus.PAUSED) {
			timer.start();
			console.pause();
			 VisidiaPanelSimulationMode.redLabel.setIcon(new ImageIcon("/green.png"));
		}
	}

	/**
	 * Stops simulation.
	 */
	public void stopSimulation() {
		
		if (console == null) return;
		
		console.stop();
		timer.stop();
		multiSimuThread.abort();
		simulationId = 1;
		if (replayInfo != null) replayInfo.rewind();

		synchronized (this.concurrentObject) {
			Enumeration<MovableItem> animItems = animatedItems.elements();
			while (animItems.hasMoreElements()) graphView.removeDecorativeItem(animItems.nextElement());
			animatedItems.clear();
		}

		synchronized (this.waitObject) {
			try {
				this.waitObject.notifyAll();
			} catch (Exception e) {
			}
		}

		//globalClock.initState();
	}

	/**
	 * Updates displacement step of messages.
	 * 
	 * @param step the step
	 */
	public void updateDisplacementStep(int step) {
		displacementStep[0] = step;
	}

	/**
	 * Gets the simulation speed.
	 * 
	 * @return the simulation speed
	 */
	public int getSimulationSpeed() {
		return displacementStep[0];
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.CommandListener#messageSent(int, int, visidia.simulation.process.messages.Message, visidia.simulation.evtack.VisidiaEvent)
	 */
	public void messageSent(int senderId, int receiverId, Message msg, VisidiaEvent event) {
		synchronized (this.concurrentObject) {
			Point a = graphView.getVertexView(senderId).getPosition();
			Point b = graphView.getVertexView(receiverId).getPosition();
			MovableMessage item = new MovableMessage(msg, a, b, this.displacementStep, event);
			this.animatedItems.add(item);
			graphView.addDecorativeItem(item);
		}
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.CommandListener#edgeStateChanged(visidia.simulation.process.edgestate.EdgeState, visidia.simulation.graph.Vertex, visidia.simulation.graph.Vertex)
	 */
	public void edgeStateChanged(EdgeState newEdgeState, Vertex v1, Vertex v2) {
		if (newEdgeState instanceof MarkedState) {
			MarkedState state = (MarkedState) newEdgeState;
			EdgeView edge = graphView.getEdgeView(v1, v2);
			edge.mark(state.isMarked());
		} else if (newEdgeState instanceof SyncState) {
			SyncState state = (SyncState) newEdgeState;
			EdgeView edge = graphView.getEdgeView(v1, v2);
			edge.synchronize(state.isSynchronized());

			VertexView vertex1 = graphView.getVertexView(v1.getId());
			vertex1.synchronize(state.isSynchronized());

			VertexView vertex2 = graphView.getVertexView(v2.getId());
			vertex2.synchronize(state.isSynchronized());
		}
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.CommandListener#nodePropertyChanged(int, java.lang.String, java.lang.Object)
	 */
	public void nodePropertyChanged(int vertexId, VisidiaProperty property) {
		graphView.getVertexView(vertexId).getVertex().setProperty(property);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.CommandListener#edgePropertyChanged(visidia.graph.Edge, java.lang.String, java.lang.Object)
	 */
	public void edgePropertyChanged(Edge edge, VisidiaProperty property) {
		edge.setProperty(property);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.CommandListener#algorithmTerminated()
	 */
	public void simulationTerminated() {
		synchronized (this.concurrentObject) {
			this.repaint();
		}

		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}

		console.stop();
		multiSimuThread.endSimulation(simulationId ++);

		if (simulationId > nbSimulations) {
			simulationId = 1;
			writer.close();
		}
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.CommandListener#agentMoved(int, visidia.graph.Vertex, visidia.graph.Vertex, visidia.simulation.evtack.VisidiaEvent)
	 */
	public void agentMoved(int agentId, Vertex origin, Vertex destination, VisidiaEvent event) {
		synchronized (this.concurrentObject) {
			MovableAgent item = agentItems.get(agentId);
			if (item != null) {
				Point a = graphView.getVertexView(origin.getId()).getPosition();
				Point b = graphView.getVertexView(destination.getId()).getPosition();
				item.setTrajectory(a, b, this.displacementStep);
				item.setEvent(event);
				this.animatedItems.add(item);
				graphView.addDecorativeItem(item);
			}
		}
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.CommandListener#agentDead(int, visidia.simulation.evtack.VisidiaEvent)
	 */
	public void agentDead(int agentId, VisidiaEvent event) {
		synchronized (this.concurrentObject) {
			MovableAgent item = agentItems.get(agentId);
			if (item != null) {
				agentItems.remove(agentId);
				Point p = item.currentLocation();
				item.setTrajectory(p, p, this.displacementStep);
				graphView.removeDecorativeItem(item);
				animatedItems.remove(item);
			}

			repaint();
		}
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.CommandListener#pulseChanged(int)
	 */
	public void pulseChanged(ProcessType sender, int pulse) {
		if (sender instanceof MessageProcess) {
			// attendre la fin de la visualization des messages du pulse en cours
			// et envoyer un acquitement
			try {
				synchronized (this.waitObject) {
					if (this.animatedItems.size() != 0) {
						this.waitObject.wait();
					}
				}
			} catch (Exception e) {
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (console == null) return;

		synchronized (this.concurrentObject) {
			MovableItem item;
			int size = this.animatedItems.size();

			Vector<MovableItem> tmpVect = new Vector<MovableItem>(size);
			for (int i = 0; i < size; i++) {
				item = this.animatedItems.elementAt(i);
				if (item.isIntoBounds()) {
					item.moveForward();
					tmpVect.add(item);
				} else {
					if (! (item instanceof MovableAgent))
						graphView.removeDecorativeItem(item);
					item.freeze();
					console.generateDelayedCommandAck(item.getEvent());
				}
			}

			this.animatedItems = tmpVect;

			if (tmpVect.size() == 0) {
				try {
					synchronized (this.waitObject) {
						this.waitObject.notify();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	public void mouseClicked(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	public void mouseEntered(MouseEvent e) {
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * Select an object on screen.
	 * 
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	private void selectObject(int x, int y) {
		GraphItemView clickedObject = graphView.getItemAtPosition(x, y);

		if (clickedObject != null/* && clickedObject instanceof VertexView*/) {
			if (selection.contains(clickedObject)) selection.removeElement(clickedObject);
			else selection.addElement(clickedObject);
		} else selection.clear();

		drawingPanel.repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	public void mousePressed(MouseEvent e) {
		int x = (int) (e.getX() / drawingPanel.getZoomFactor());
		int y = (int) (e.getY() / drawingPanel.getZoomFactor());

		switch (e.getModifiers()) {
		case InputEvent.BUTTON3_MASK:
			selectObject(x, y);
			break;

		default:		
		}	
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	public void mouseReleased(MouseEvent e) {
	}

	/**
	 * Apply star rules system.
	 * 
	 * @param relabelingSystem the relabeling system
	 */
	public void applyStarRulesSystem(RelabelingSystem relabelingSystem) {
		boolean simulationRules = false;
		Vector<RelabelingSystem> agentsRules = null;

		if (SimulationConstants.RunningMode.messagesMode(simulationMode)) {
			if (simulationRules) {
				JOptionPane.showMessageDialog(this,
						"An algorithm has already been selected", "Warning",
						JOptionPane.WARNING_MESSAGE);
				return;
			}
			simulationRules = true;
			RuleAlgorithm rsAlgo = this.buildAlgoRule(relabelingSystem); // The algorithm which will simulate the relabeling system
			setAlgorithm(rsAlgo);
			parent.simulationMode.setAlgorithmMessageList(rsAlgo.getMessageTypeList());
		} else if (SimulationConstants.RunningMode.agentsMode(simulationMode)) {
			this.rulesWarnings(relabelingSystem);

			if (agentsRules == null) {
				agentsRules = new Vector<RelabelingSystem>();
			}

			int size = agentsRules.size();
			agentsRules.add(relabelingSystem);
			RelabelingSystem rSys = (RelabelingSystem) agentsRules.get(size);
			setAgentRules(rSys);
		}
	}

	/**
	 * Display warnings relative to rules.
	 * 
	 * @param r the rules
	 */
	private void rulesWarnings(RelabelingSystem r) {
		int synType;// user choice
		int type;// default choice

		RSOptions options = r.getOptions();
		if (options.defaultSynchronisation() != -1) {
			type = r.defaultSynchronisation();
			// user choice
			synType = options.defaultSynchronisation();
			if ((synType == SynCT.RDV) && (type == SynCT.LC1)) {
				JOptionPane.showMessageDialog(this,
						"The rendez-vous synchronisation cannot be used\n"
								+ "because of context or arity", "Error",
								JOptionPane.WARNING_MESSAGE);
			}
			if ((synType == SynCT.LC1) && (type == SynCT.RDV)) {
				JOptionPane.showMessageDialog(this,
						"The use of use of LC1 is not recommended :\n"
								+ "check if you modify the neighbors.",
								"Error", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			// default option
			synType = r.defaultSynchronisation();
		}
	}

	/**
	 * Builds an algorithm from rules.
	 * 
	 * @param r the rule
	 * 
	 * @return the rule-based algorithm
	 */
	public RuleAlgorithm buildAlgoRule(RelabelingSystem r) {
		RuleAlgorithm algo;
		int synType;// user choice
		int type;// default choice
		SynchronizationObject synob;
		RSOptions options = r.getOptions();
		if (options.defaultSynchronisation() != -1) {
			type = r.defaultSynchronisation();
			// user choice
			synType = options.defaultSynchronisation();
			if ((synType == SynCT.RDV) && (type == SynCT.LC1)) {
				JOptionPane.showMessageDialog(this,
						"The rendez-vous synchronisation cannot be used\n"
								+ "because of context or arity", "Error",
								JOptionPane.WARNING_MESSAGE);
			}
			if ((synType == SynCT.LC1) && (type == SynCT.RDV)) {
				JOptionPane.showMessageDialog(this,
						"The use of use of LC1 is not recommended :\n"
								+ "check if you modify the neighbors.",
								"Error", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			// default option
			synType = r.defaultSynchronisation();
		}

		switch (synType) {
		case SynCT.LC2:
			r.dupplicateSimpleRules(synType);
			algo = new LC2Rule(r);
			break;
		case SynCT.LC1:
			algo = new LC1Rule(r);
			break;
		default:
			r.dupplicateSimpleRules(synType);
			algo = new RDVRule(r);
			break;
		}
		synob = new SynchronizationObjectTerminationRules();

		algo.getSynchronizationAlgorithm().setSynchronizationObject(synob);
		return algo;
	}

	/**
	 * Switch selected vertices on/off.
	 */
	public void switchSelectedVerticesOnOff() {
		Enumeration<GraphItemView> items = selection.elements();
		while (items.hasMoreElements()) {
			GraphItemView item = items.nextElement();
			if (item instanceof VertexView) {
				if (console != null)
					console.switchVertexOnOff(((VertexView) item).getVertex());
			}
		}
		repaint();
	}

	/**
	 * The Class MultiSimuThread.
	 */
	class MultiSimuThread extends Thread {

		/** The stopped. */
		private volatile boolean stopped = false;

		/** The terminated. */
		private volatile boolean terminated = false;

		/** The  simu id. */
		private volatile int terminatedSimuId;

		/**
		 * Instantiates a new multi simu thread.
		 */
		public MultiSimuThread() {
			super();
		}

		/**
		 * Abort.
		 */
		public void abort() {
			stopped = true;
			interrupt();
		}

		/**
		 * Activate.
		 */
		public void endSimulation(int simuId) {
			this.terminated = true;
			this.terminatedSimuId = simuId;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			while (! stopped && ! Thread.currentThread().isInterrupted()) {
				if (terminated && animatedItems.isEmpty()) {
					if (terminatedSimuId < nbSimulations) {
						terminated = false;
						startSimulation();
					} else {
						//stopSimulation();
						console.stop();
						//timer.stop();				
						drawingPanel.setInfoText("Simulation is terminated");
						//abort();
						stopped = true;
					}
				}
			}

			if (! Thread.currentThread().isInterrupted() && isAlgorithmPutOnNodes) {
				JOptionPane.showMessageDialog(VisidiaMain.getParentFrame(), "Simulation is terminated");
				interrupt();
			}
		}
	}

}


/**
 * The Class VertexProperties.
 */
class VertexProperties implements Serializable {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 9007330846883292483L;

	/** The id. */
	public int id;

	/** The properties. */
	PropertyTable props;

	/**
	 * Instantiates a new vertex label.
	 * 
	 * @param id the id
	 * @param props the props
	 */
	public VertexProperties(int id, PropertyTable props) {
		this.id = id;
		this.props = props;
	}

}

/**
 * The Class VertexAgent.
 */
class VertexAgent implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6486884111349763856L;

	/** The vertex. */
	public Vertex vertex;

	/** The agent. */
	transient public Agent agent;

	/** The agent name. */
	public String agentName;

	/**
	 * Instantiates a new vertex agent.
	 * 
	 * @param vertex the vertex
	 * @param agent the agent
	 */
	public VertexAgent(Vertex vertex, Agent agent) {
		this.vertex = vertex;
		this.agent = agent;
		this.agentName = agent.toString();
	}

}

class ReplayAgent extends Agent {

	private String name;

	public ReplayAgent(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	@Override
	public Object clone() {
		return new ReplayAgent(name);
	}

	@Override
	protected void init() {
	}

}
