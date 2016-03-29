package visidia.simulation;

import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Vector;
import javax.swing.event.EventListenerList;

import visidia.graph.Graph;
import visidia.graph.Vertex;
import visidia.simulation.SimulationConstants.SimulationStatus;
import visidia.simulation.SimulationConstants.SimulationType;
import visidia.simulation.command.Command;
import visidia.simulation.command.CommandListener;
import visidia.simulation.command.EndSimulationCommand;
import visidia.simulation.command.NewPulseCommand;
import visidia.simulation.command.RemoveAgentCommand;
import visidia.simulation.evtack.AckHandler;
import visidia.simulation.evtack.EventHandler;
import visidia.simulation.evtack.Locker;
import visidia.simulation.evtack.VQueue;
import visidia.simulation.evtack.VisidiaAck;
import visidia.simulation.evtack.VisidiaEvent;
import visidia.simulation.playrec.ObjectWriter;
import visidia.simulation.playrec.PlayerAckHandler;
import visidia.simulation.playrec.RecordHandler;
import visidia.simulation.playrec.ReplayInfo;
import visidia.simulation.playrec.SimulationPlayer;
import visidia.simulation.process.AgentProcess;
import visidia.simulation.process.MessageProcess;
import visidia.simulation.process.ProcessType;
import visidia.simulation.process.agent.Agent;
import visidia.simulation.process.agent.AgentRules;
import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.server.LocalServer;
import visidia.simulation.server.RemoteServer;
import visidia.simulation.server.Server;
import visidia.stats.AgentCreationStat;
import visidia.stats.AgentDeathStat;
import visidia.stats.Statistics;

// TODO: Auto-generated Javadoc
/**
 * Console is the simulator main class. It manages event&ack queues&handlers, as well as the servers and the list of processes.
 * Console offers a listener possibility: one can connect to the console to listen some simulation events. For example, the GUI can connect
 * to the console to update the display regarding to simulation events.
 */
public class Console {

	/** The command listeners. */
	private final EventListenerList commandListeners = new EventListenerList();
	
	/** The servers. */
	private Vector<Server> servers;

	/** The processes. */
	private Vector<ProcessType> processes;

	/** The event handler. */
	private EventHandler evtHandler = null;

	/** The ack handler. */
	private AckHandler ackHandler = null;

	/** The simulator event queue. */
	private VQueue evtQueueSimu;

	/** The simulator ack queue. */
	private VQueue ackQueueSimu;

	/** The event recorder. */
	private RecordHandler<VisidiaEvent> evtRecorder;

	/** The ack recorder. */
	private RecordHandler<VisidiaAck> ackRecorder;
	
	/** The recorder event queue. */
	private VQueue evtQueueRecord;

	/** The recorder ack queue. */
	private VQueue ackQueueRecord;

	/** The player ack handler. */
	private PlayerAckHandler playerAckHandler;
	
	/** The player ack queue. */
	private VQueue ackQueuePlayer;

	/** The simulation mode. */
	private int simulationMode;

	/** The simulation type. */
	private SimulationType simulationType;
	
	/** The graph. */
	private Graph graph = null;

	/** The simulation status. */
	private SimulationStatus status = SimulationStatus.STOPPED;

	/** The pause lock. */
	private Object pauseLock = new Object();

	/** The simulator thread group. */
	private VisidiaThreadGroup threadGroupSimulator = null;

	/** The Constant THREAD_PRIORITY. */
	private static final int THREAD_PRIORITY = 1;

	/** The lock sync. */
	private Object lockSync = new Object();

	/** The terminated thread count. */
	private int terminatedThreadCount = 0;

	/** The agents. */
	private Hashtable<Integer, Agent> agents;
	
	/** The number of agents. */
	private int nbAgents;

	/** The statistics. */
	private Statistics stats = null;

	/** The collection of agents which are on each vertex. */
	private Hashtable<Vertex, Collection<Agent>> vertexAgentsNumber;

	/** number of nodes that have finished the current pulse (for synchronous algo). */
	private int countNextPulse = 0;

	/** current pulse (for synchronous algo). */
	private int pulse = 1;
	
	/** The terminated algo still moving count (for sensor synchronous algo). */
	private int terminatedAlgoStillMovingCount = 0;
	
	/** The simulation id. */
	private int simulationId = 1;
	
	/** The object writer. */
	ObjectWriter writer = null;

	/** The replay info. */
	ReplayInfo replayInfo;
	
	/** The simulation player. */
	private SimulationPlayer player = null;
	
	/** The simulation termination thread. */
	private TerminationThread simulationTerminationThread = null;
	
	/**
	 * Instantiates a new console.
	 * 
	 * @param simulationMode the simulation mode
	 * @param simulationType the simulation type
	 * @param graph the graph
	 * @param writer the writer
	 * @param replayInfo the replay info
	 */
	public Console(int simulationMode, SimulationType simulationType, Graph graph, ObjectWriter writer, ReplayInfo replayInfo) {
		servers = new Vector<Server>(10, 10);
		processes = new Vector<ProcessType>(10, 10);
		
		this.simulationMode = simulationMode;
		this.simulationType = simulationType;
		this.writer = writer;
		this.replayInfo = replayInfo;
		initializeQueuesAndHandlers();
		this.graph = graph;
		this.threadGroupSimulator = new VisidiaThreadGroup("simulator");
		this.agents = new Hashtable<Integer, Agent>();
		this.nbAgents = 0;
		stats = new Statistics();
		this.vertexAgentsNumber = new Hashtable<Vertex, Collection<Agent>>();
		simulationTerminationThread = new TerminationThread();

		if (SimulationConstants.RunningMode.localMode(simulationMode))
			buildLocalServer();
		else if (SimulationConstants.RunningMode.remoteMode(simulationMode))
			// TODO
			;//buildRemoteServers();
	}
	
	/**
	 * Gets the simulation id.
	 * 
	 * @return the simulation id
	 */
	public int getSimulationId() {
		return simulationId;
	}

	/**
	 * Sets the simulation id.
	 * 
	 * @param simulationId the new simulation id
	 */
	public void setSimulationId(int simulationId) {
		this.simulationId = simulationId;
	}

	/**
	 * Gets the lock sync object.
	 * 
	 * @return the lock sync object
	 */
	public Object getLockSyncObject() {
		return lockSync;
	}

	/**
	 * Notify all threads waiting for lockSync object.
	 */
	public void notifyAllLockSync() {
		synchronized (lockSync) {
			try {
				lockSync.notifyAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Gets the count next pulse.
	 * 
	 * @return the count next pulse
	 */
	public int getCountNextPulse() {
		return countNextPulse;
	}
	
	/**
	 * Sets the count next pulse.
	 * 
	 * @param countNextPulse the new count next pulse
	 */
	public void setCountNextPulse(int countNextPulse) {
		this.countNextPulse = countNextPulse;
	}

	/**
	 * Gets the pulse.
	 * 
	 * @return the pulse
	 */
	public int getPulse() {
		return pulse;
	}
	
	/**
	 * Sets the pulse.
	 * 
	 * @param pulse the new pulse
	 */
	public void setPulse(int pulse) {
		this.pulse = pulse;
	}

	/**
	 * Gets the terminated algo still moving count.
	 * 
	 * @return the terminated algo still moving count
	 */
	public int getTerminatedAlgoStillMovingCount() {
		return terminatedAlgoStillMovingCount;
	}
	
	/**
	 * Sets the terminated algo still moving count.
	 * 
	 * @param terminatedAlgoStillMovingCount the new terminated algo still moving count
	 */
	public void setTerminatedAlgoStillMovingCount(int terminatedAlgoStillMovingCount) {
		this.terminatedAlgoStillMovingCount = terminatedAlgoStillMovingCount;
	}
	
	/**
	 * Gets the graph.
	 * 
	 * @return the graph
	 */
	public final Graph getGraph() {
		return graph;
	}
	
	/**
	 * Gets the number of processes.
	 * 
	 * @return the number of processes
	 */
	public int getNbProcesses() {
		return processes.size();
	}
	
	/**
	 * Gets the number of terminated threads.
	 * 
	 * @return the terminated thread count
	 */
	public int getTerminatedThreadCount() {
		return terminatedThreadCount;
	}
	
	/**
	 * Gets the simulation status.
	 * 
	 * @return the status
	 */
	public SimulationStatus getStatus() {
		return status;
	}

	/**
	 * Gets the stats.
	 * 
	 * @return the stats
	 */
	public Statistics getStats() {
		return stats;
	}

	/**
	 * Sets the stats.
	 * 
	 * @param stats the new stats
	 */
	public void setStats(Statistics stats) {
		this.stats = stats;
	}
	
	/**
	 * Gets a process.
	 * 
	 * @param processId the process id
	 * 
	 * @return the process
	 */
	public ProcessType getProcess(int processId) {
		Enumeration<ProcessType> procs = processes.elements();
		while (procs.hasMoreElements()) {
			ProcessType p = procs.nextElement();
			if (p.getId() == processId) return p;
		}

		return null;
	}

	/**
	 * Returns the collection of agents which are on the vertex defined by its id.
	 * 
	 * @param vertexId the vertex id
	 * 
	 * @return the agents vertex collection
	 */
	public Collection<Agent> getAgentsVertexCollection(int vertexId) {
		return this.vertexAgentsNumber.get(this.graph.getVertex(vertexId));
	}

	/**
	 * Adds a command listener.
	 * 
	 * @param listener the listener
	 */
	public void addCommandListener(CommandListener listener) {
		commandListeners.add(CommandListener.class, listener);
	}

	/**
	 * Removes a command listener.
	 * 
	 * @param listener the listener
	 */
	public void removeCommandListener(CommandListener listener) {
		commandListeners.remove(CommandListener.class, listener);
	}

	/**
	 * Gets the command listeners.
	 * 
	 * @return the command listeners
	 */
	public CommandListener[] getCommandListeners() {
		return commandListeners.getListeners(CommandListener.class);
	}

	/**
	 * Running control.
	 */
	public void runningControl() {
		if (status == SimulationStatus.ABORTED) {
			throw new SimulationAbortError();
		}

		if (status == SimulationStatus.PAUSED) {
			synchronized (this.pauseLock) {
				try {
					this.pauseLock.wait();
				} catch (InterruptedException e) {
					throw new SimulationAbortError();
				}
			}
		}
	}
	
	/**
	 * Resets console.
	 */
	public void reset() {
		stats = new Statistics(); 

		stopAckHandler();
		//evtHandler.abort();
		noMoreEvents();
		
		while (this.threadGroupSimulator.activeCount() > 0) {
			try {
				this.threadGroupSimulator.interrupt();
				Thread.sleep(50);
			} catch (InterruptedException e) {
			}
		}

		if (simulationType == SimulationType.RECORD) {
			evtRecorder.abort();
			ackRecorder.abort();
		} else if (simulationType == SimulationType.REPLAY) {
			player.abort();
		}

		this.vertexAgentsNumber.clear();
		//SynchronousAgent.resetPulseNumber();
		
		simulationTerminationThread.abort();
	}

	/**
	 * Starts console.
	 */
	public void start() {
		if (status == SimulationStatus.STARTED) return;
		
		simulationTerminationThread.start();
		
		if (simulationType == SimulationType.RECORD) {
			evtRecorder.start();
			ackRecorder.start();
		}
		
		// start event handler
		evtHandler.start();

		if (simulationType == SimulationType.REPLAY) {
			player.start();
			playerAckHandler.setPriority(9);
			playerAckHandler.start();
		} else {
			// initialize algorithm objects
			Enumeration<ProcessType> procs = processes.elements();
			while (procs.hasMoreElements()) {
				ProcessType pt = procs.nextElement();
				if (pt instanceof MessageProcess) {
					MessageProcess mp = (MessageProcess) pt;
					Thread thread = new Thread(threadGroupSimulator, mp.getAlgorithm());
					thread.setPriority(THREAD_PRIORITY);
					mp.setThread(thread);
				} else if (pt instanceof AgentProcess) {
					AgentProcess ap = (AgentProcess) pt;
					Thread thread = new Thread(threadGroupSimulator, ap.getAgent());
					thread.setPriority(THREAD_PRIORITY);
					ap.setThread(thread);
				}
			}

			// start ack handler
			ackHandler.setPriority(9);
			ackHandler.start();

			// start node threads
			procs = processes.elements();
			while (procs.hasMoreElements()) {
				ProcessType pt = procs.nextElement();
				pt.start();
			}
		}

		status = SimulationStatus.STARTED;
	}

	/**
	 * Pauses console.
	 */
	public void pause() {
		if (status == SimulationStatus.PAUSED) {
			synchronized (this.pauseLock) {
				status = SimulationStatus.STARTED;
				this.pauseLock.notifyAll();
			}
		} else if (status == SimulationStatus.STARTED) {
			status = SimulationStatus.PAUSED;
		}
	}

	/**
	 * Stops console.
	 */
	public void stop() {
		status = SimulationStatus.ABORTED;

		try {
			synchronized (this.lockSync) {
				this.lockSync.notifyAll();
			}
		} catch (Exception e) {
		}

		reset();
		initializeQueuesAndHandlers();
	}

	/**
	 * Prevents from any deadlock.
	 * 
	 * @param sender the sender
	 */
	private void preventFromDeadlock(ProcessType sender) {
		// Si par hasard un thread (sommet) termine et les n-1 autres
		// threads sont toujours actif. En plus si les n-1 threads
		// font appel a la methode nextPulse() avant que notre thread
		// n'appele terminated thread (avant d'incrementer la variable
		// terminated thread. Alors il y'a deadlock des n-1 thread: le
		// handler ne les reveille pas car personne n'a executer la
		// methode pushNextPulseEvent(). Un thread qui termine doit
		// verifier que les threads encore actifs ne sont pas een
		// attente d'etre debloques ...

		synchronized (this.lockSync) {
			int i = (this.countNextPulse + 1) % (this.processes.size() - this.terminatedThreadCount);

			if ((i == 0) && (this.terminatedThreadCount != this.processes.size() - 1)) {
				try {
					NewPulseCommand cmd = new NewPulseCommand(sender.getId(), pulse);
					runCommand(cmd);
				} catch (Exception e) {
					// aucune exception n'est normalement levee ici
					//System.out.println(" bug ref 0.3 Simulator Synchrone");
				}
				this.pulse++;
			}

			this.terminatedThreadCount++;
		}
	}

	/**
	 * No more events.
	 */
	private void noMoreEvents() {
		evtHandler.abort();
		if (simulationType == SimulationType.RECORD) {
			evtRecorder.abort();
		} else if (simulationType == SimulationType.REPLAY) {
			player.abort();
		}
	}

	/**
	 * Stops the ack handler.
	 */
	private void stopAckHandler() {
		if (simulationType == SimulationType.REPLAY) {
			playerAckHandler.abort();
		} else {
			ackHandler.abort();
		}
		status = SimulationStatus.ABORTED;
	}

	/**
	 * Terminate simulation.
	 */
	public void terminateSimulation() {
		simulationTerminationThread.activate();
	}

	/**
	 * Counts terminated threads on the graph. If the number equals the number of processes,
	 * it sends algorithm termination event.
	 * 
	 * @param sender the sender
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public void terminatedAlgorithm(MessageProcess sender) throws InterruptedException {
		preventFromDeadlock(sender);
		
		if (this.terminatedThreadCount == this.processes.size()) {
			// sends algorithms end notification.
			runCommand(new EndSimulationCommand());
		}
	}

	/**
	 * Dead agent.
	 * 
	 * @param sender the sender
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public void deadAgent(AgentProcess sender) throws InterruptedException {
		this.removeAgentFromVertex(sender.getDestinationVertex(), sender.getAgent());
		stats.add(new AgentDeathStat(sender.getAgent().getClass()));
		preventFromDeadlock(sender);
		
		if (this.terminatedThreadCount == this.processes.size()) {
			// sends algorithms end notification.
			runCommand(new EndSimulationCommand());
		}
	}

	/**
	 * Puts algorithm on nodes.
	 * 
	 * @param algorithm the algorithm
	 */
	public void putAlgorithmOnNodes(Algorithm algorithm) {
		Enumeration<ProcessType> procs = processes.elements();
		while (procs.hasMoreElements()) {
			ProcessType pt = procs.nextElement();
			if (pt instanceof MessageProcess) {
				Algorithm algoClone = (Algorithm) algorithm.clone();
				((MessageProcess) pt).setAlgorithm(algoClone);
			}
		}
	}

	/**
	 * Initializes the message processes.
	 * 
	 * @param vertices the vertices
	 * @param server the server
	 */
	private void initMessageProcesses(Enumeration<Vertex> vertices, Server server) {
		while (vertices.hasMoreElements()) {
			Vertex v = vertices.nextElement();
			MessageProcess mp = new MessageProcess(server, v);
			server.addProcess(mp);
			processes.add(mp);
		}
	}

	/**
	 * Gets the active agents.
	 * 
	 * @return the active agents
	 */
	public Object[] getActiveAgents() {
		Vector<Agent> agents = new Vector<Agent>();		
		Enumeration<ProcessType> procs = processes.elements();
		while (procs.hasMoreElements()) {
			ProcessType proc = procs.nextElement();
			if (proc != null && proc instanceof AgentProcess) {
				Agent ag = ((AgentProcess) proc).getAgent();
				if (ag != null) agents.add(ag);
			}
		}
		
		return agents.toArray();
	}
	
	/**
	 * Creates a new agent on vertex.
	 * 
	 * @param v the vertex
	 * @param agent the agent to clone
	 * 
	 * @return the new agent
	 */
	public Agent createAgentOnVertex(Vertex v, Agent agent) {
		Agent newAgent = null;
		if (agent instanceof AgentRules) newAgent = agent;
		else newAgent = (Agent) agent.clone();
		
		if (simulationType != SimulationType.REPLAY) {
			if (SimulationConstants.RunningMode.localMode(simulationMode)) {
				Server server = servers.elementAt(0);
				AgentProcess ap = new AgentProcess(server, v, nbAgents);
				ap.setAgent(newAgent);
				server.addProcess(ap);
				processes.add(ap);
			} else if (SimulationConstants.RunningMode.remoteMode(simulationMode)) {
				// TODO
			}
			stats.add(new AgentCreationStat(agent.getClass()));
		}
		agents.put(nbAgents, newAgent);
		this.addAgentToVertex(v, newAgent);
		nbAgents ++;
		return newAgent;
	}

	/**
	 * Gets the agent.
	 * 
	 * @param agentId the agent id
	 * 
	 * @return the agent
	 */
	public Agent getAgent(int agentId) {
		return agents.get(agentId);
	}
	
	/**
	 * Adds an agent to a specified vertex.
	 * 
	 * @param vertex the vertex
	 * @param ag the agent
	 * 
	 * @return the new number of agents on the vertex
	 */
	public int addAgentToVertex(Vertex vertex, Agent ag) {
		synchronized (this.vertexAgentsNumber) {
			if (this.vertexAgentsNumber.get(vertex) != null) {
				this.vertexAgentsNumber.get(vertex).add(ag);
			} else {
				Collection<Agent> colOfAgents = new HashSet<Agent>();
				colOfAgents.add(ag);
				this.vertexAgentsNumber.put(vertex, colOfAgents);
			}
			return this.vertexAgentsNumber.get(vertex).size();
		}
	}

	/**
	 * Removes a specified agent from a specified vertex. Returns the new number
	 * of agents on the vertex.
	 * 
	 * @param vertex the vertex
	 * @param ag the agent
	 * 
	 * @return the number of agents after removal
	 * 
	 * @see #addAgentToVertex(Vertex, Agent)
	 */
	public int removeAgentFromVertex(Vertex vertex, Agent ag) {
		synchronized (this.vertexAgentsNumber) {
			if (this.vertexAgentsNumber.get(vertex) != null && ag != null) {
				this.vertexAgentsNumber.get(vertex).remove(ag);
				if (this.vertexAgentsNumber.get(vertex).isEmpty()) {
					this.vertexAgentsNumber.remove(vertex);
					return 0;
				} else {
					return this.vertexAgentsNumber.get(vertex).size();
				}
			}
			return 0;
		}
	}

	/**
	 * Builds the local server.
	 */
	private void buildLocalServer() {
		Server localServer = new LocalServer(this);
		servers.add(localServer);

		// create the processes on server
		if (SimulationConstants.RunningMode.messagesMode(simulationMode)) {
			initMessageProcesses(graph.getVertices(), localServer);
		} else if (SimulationConstants.RunningMode.agentsMode(simulationMode)) {
			// nothing to be done here; processes will be created when new agents will be added
		}
	}

	/**
	 * Builds the remote servers.
	 * 
	 * @param nbServers the number of servers
	 */
	private void buildRemoteServers(int nbServers) {
		for (int i = 0; i < nbServers; i++) {
			Server remoteServer = new RemoteServer(this);
			servers.add(remoteServer);

			// create the processes of each server
			if (SimulationConstants.RunningMode.messagesMode(simulationMode)) {
				//initMessageProcesses(nodeDistribution[i], remoteServer);
			}
		}
		/*
		if (SimulationConstants.agentsMode(simulationMode)) { // CASE AGENT PROCESS
			// preconditions: some agents has been associated to nodes
			Hashtable<Agent,Vertex> placedAgents = getPlacedAgents();
			Set<Agent> agents = placedAgents.keySet();
			Iterator itr = agents.iterator(); // PAS D'ITERATEURS (fail-fast) !!
			while (itr.hasNext()) {
				Agent a = itr.next();
				Vertex v = placedAgents.get(a);
				Server s = v.parentServer();
				AgentProcess ap = new AgentProcess(a, v, s);
				s.addProcess(ap);
			}
		} // END CASE AGENT PROCESS
		 */
	}

	/**
	 * Runs a command.
	 * 
	 * @param cmd the command
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public void runCommand(Command cmd) throws InterruptedException {
		Locker locker = Locker.getInstance();
		Long lockId = locker.createLock();
		VisidiaEvent event = new VisidiaEvent(lockId, cmd, this);

		if (cmd.needSynchronization() && simulationType != SimulationType.REPLAY) {
			Object lock = locker.getLock(lockId);
			synchronized (lock) {
				evtQueueSimu.put(event);
				lock.wait();
			}
		} else {
			evtQueueSimu.put(event);
		}
	}

	/**
	 * Generate delayed command ack.
	 * 
	 * @param event the event
	 */
	public void generateDelayedCommandAck(VisidiaEvent event) {
		VisidiaAck ack = new VisidiaAck(event);
		VQueue q = null;
		if (simulationType == SimulationType.RECORD) q = ackQueueRecord;
		else q = ackQueueSimu;
		
		try {
			q.put(ack);
		} catch (InterruptedException e) {
		}
	}
	
	/**
	 * Switch vertex on/off.
	 * 
	 * @param vertex the vertex
	 */
	public void switchVertexOnOff(Vertex vertex) {
		vertex.setSwitchedOn(!vertex.isSwitchedOn());
		
		// up to now, this method is only used in agent-based simulation
		
		if (SimulationConstants.RunningMode.messagesMode(simulationMode)) {
			// TODO: stop the corresponding thread?
		}	
	}

	/**
	 * Kill agent.
	 * 
	 * @param agent the agent
	 */
	public void killAgent(Agent agent) {
		try {
			Enumeration<ProcessType> procs = processes.elements();
			while (procs.hasMoreElements()) {
				ProcessType proc = procs.nextElement();
				if (proc instanceof AgentProcess) {
					Agent ag = ((AgentProcess) proc).getAgent();
					if (ag.equals(agent)) {
						proc.stop();
						proc.setThread(null);
						processes.setElementAt(null, processes.indexOf(proc));

						runCommand(new RemoveAgentCommand(proc.getId()));
						deadAgent((AgentProcess) proc);
					}
				}
			}
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Initialize queues and handlers.
	 */
	private void initializeQueuesAndHandlers() {
		if (simulationType == SimulationType.STANDARD) {
			evtQueueSimu = new VQueue(); // evtPipeOut
			ackQueueSimu = new VQueue(); // ackPipeOut
			evtHandler = new EventHandler(evtQueueSimu, ackQueueSimu);
			ackHandler = new AckHandler(ackQueueSimu);
		} else if (simulationType == SimulationType.RECORD) {
			evtQueueSimu = new VQueue(); // evtPipeIn
			ackQueueSimu = new VQueue(); // ackPipeOut
			evtQueueRecord = new VQueue();
			ackQueueRecord = new VQueue();
			
			evtHandler = new EventHandler(evtQueueRecord, ackQueueRecord);
			ackHandler = new AckHandler(ackQueueSimu);

			evtRecorder = new RecordHandler<VisidiaEvent>(evtQueueSimu, evtQueueRecord, writer);
			ackRecorder = new RecordHandler<VisidiaAck>(ackQueueRecord, ackQueueSimu, writer);
		} else if (simulationType == SimulationType.REPLAY) {
			evtQueueSimu = new VQueue(); // evtPipeOut
			ackQueueSimu = new VQueue(); // ackPipeOut
			
			evtHandler = new EventHandler(evtQueueSimu, ackQueueSimu);
						
			ackQueuePlayer = new VQueue();
			playerAckHandler = new PlayerAckHandler(ackQueueSimu, ackQueuePlayer);
			
			player = new SimulationPlayer(ackQueuePlayer, replayInfo);
		}
	}

	/**
	 * The Class TerminationThread.
	 */
	class TerminationThread extends Thread {

		/** The stopped. */
		private volatile boolean stopped = false;

		/** The activated. */
		private volatile boolean activated = false;

		/**
		 * Instantiates a new termination thread.
		 */
		public TerminationThread() {
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
		public void activate() {
			this.activated = true;
		}

		/* (non-Javadoc)
		 * @see java.lang.Thread#run()
		 */
		public void run() {
			while (! stopped && ! Thread.currentThread().isInterrupted()) {
				if (activated) {
					CommandListener[] listeners = getCommandListeners();

					for (CommandListener listener : listeners) {
						listener.simulationTerminated();
					}
					abort();
				}
			}
		}

	}

}
