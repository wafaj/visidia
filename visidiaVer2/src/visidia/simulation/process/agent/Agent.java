package visidia.simulation.process.agent;

import java.util.Collection;
import java.util.Hashtable;

import visidia.graph.Vertex;
import visidia.io.ClassIO;
import visidia.misc.VisidiaSettings;
import visidia.misc.property.PropertyTable;
import visidia.misc.property.VisidiaProperty;
import visidia.simulation.SimulationAbortError;
import visidia.simulation.command.ChangeNodePropertyCommand;
import visidia.simulation.process.AgentProcess;
import visidia.simulation.process.MoveException;
import visidia.simulation.process.edgestate.EdgeState;
import visidia.stats.AgentAccessVertexWhiteBoardStat;
import visidia.stats.AgentChangeVertexWhiteBoardStat;

// TODO: Auto-generated Javadoc
/**
 * This is the abstract base class representing an agent in visidia.
 * 
 * It is the API to be used to implement new agents (extending Agent class).
 */
public abstract class Agent extends PropertyTable implements Runnable {

	private static final long serialVersionUID = 2853568571194842573L;

	/** The process. */
	transient protected AgentProcess proc = null;

	/** The agent mover. */
	protected AgentMover agentMover = null;

    /** Synchronization object for vertex lock. */
	transient private static Boolean askForLock = new Boolean(true);
    
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public abstract Object clone();

	/**
	 * Initializes the agent.
	 */
	protected abstract void init();

	/**
	 * Gets the description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return "No description for this agent.";
	}
	/**
	 * Instantiates a new agent.
	 */
	protected Agent() {
		super(null, new Hashtable<Object, VisidiaProperty>());
	}
	
	/*******************/
	/* GENERAL METHODS */
	/*******************/

	/**
	 * Returns the collection of agents which are on the destination vertex of current agent.
	 * 
	 * @return the collection
	 */
	protected final Collection<Agent> agentsOnVertex() {
		if (proc == null) return null;
		return proc.getServer().getConsole().getAgentsVertexCollection(proc.getDestinationVertex().getId());
	}

	/**
	 * Gets the arity.
	 * 
	 * @return the arity
	 */
	public final int getArity() {
		if (proc == null) return 0;
		return proc.getDestinationVertex().degree();
	}

    /**
     * Pauses the agent for a given number of milliseconds.
     * 
     * @param milliseconds the number of milliseconds
     */
	protected final void sleep(int milliseconds) {
        try {
            this.proc.sleep(this, milliseconds);
        } catch (InterruptedException e) {
            throw new SimulationAbortError(e);
        }
	}
	
	/**
	 * Gets the net size. Provided for convenience, since an agent is not supposed to know graph global information.
	 * 
	 * @return the net size
	 */
	protected final int getNetSize() {
		try {
			return proc.getServer().getConsole().getGraph().order();
		} catch (Exception e) {
			return 0;
		}
	}
	
	/********************
	/* PROPERTY METHODS */
	/********************/

	/**
	 * Returns the property value associated with the key. If the key can't be found, returns null.
	 * 
	 * @param key the key
	 * 
	 * @return the property value
	 */
	public final Object getProperty(Object key) {
		VisidiaProperty prop = super.getVisidiaProperty(key);
		return (prop == null ? null : prop.getValue());	
	}
	

	/**
	 * Sets the tagged property.
	 *
	 * @param key the key
	 * @param value the value
	 * @param tag the tag
	 * @return the object
	 */
	private final Object setTaggedProperty(Object key, Object value, int tag) {
		VisidiaProperty property = new VisidiaProperty(key, value, tag);
		VisidiaProperty prop = super.setVisidiaProperty(property);
		return (prop == null ? null : prop.getValue());	
	}
	
	/**
	 * Sets the property (key, value).
	 *
	 * @param key the key
	 * @param value the value
	 * @param status the status
	 * @return     the previous property value of the specified key in this hashtable,
	 * or  if it did not have one.
	 */
	public final Object setProperty(Object key, Object value, int status) {
		return setTaggedProperty(key, value, VisidiaProperty.getPropertyTagFromStatus(status));	
	}
	
	/**
	 * Sets the property.
	 *
	 * @param key the key
	 * @param value the value
	 * 
     * @return     the previous property value of the specified key in this hashtable,
     *             or <code>null</code> if it did not have one.
	 */
	public final Object setProperty(Object key, Object value) {
		VisidiaProperty prop = super.getVisidiaProperty(key);
		if (prop == null) return this.setTaggedProperty(key, value, VisidiaProperty.Tag.USER_PROPERTY);
		return this.setTaggedProperty(key, value, prop.getTag());
	}
	
	/**
	 * Gets the vertex property value.
	 * 
	 * @param key the key
	 * 
	 * @return the vertex property value
	 */
	protected final Object getVertexProperty(Object key) {
		if (proc == null) return null;		
		Vertex destination = proc.getDestinationVertex();

		synchronized (destination) {
			while (destination.locked() && (destination.getLockOwner() != this)) {
				try {
					destination.wait();
				} catch (InterruptedException e) {
					throw new SimulationAbortError(e);
				}
			}
			this.proc.getServer().getConsole().getStats().add(new AgentAccessVertexWhiteBoardStat(this.getClass(), proc.getId()));

			VisidiaProperty prop = destination.getVisidiaProperty(key);
			return (prop == null ? null : prop.getValue());	
		}
	}
	
	/**
	 * Sets the vertex property.
	 *
	 * @param key the key
	 * @param value the value
	 * @param tag the property tag
	 */
	private final void setVertexTaggedProperty(Object key, Object value, int tag) {
		if (proc == null) return;
		Vertex destination = proc.getDestinationVertex();

		synchronized (destination) {
			while (destination.locked() && (destination.getLockOwner() != this)) {
				try {
					destination.wait();
				} catch (InterruptedException e) {
					throw new SimulationAbortError(e);
				}
			}

			this.proc.getServer().getConsole().getStats().add(new AgentChangeVertexWhiteBoardStat(this.getClass(), proc.getId()));
			VisidiaProperty prop = new VisidiaProperty(key, value, tag);
			destination.setProperty(prop);
			proc.getServer().sendToConsole(new ChangeNodePropertyCommand(destination.getId(), prop));
		}
	}

	/**
	 * Sets the vertex property.
	 *
	 * @param key the key
	 * @param value the value
	 * @param status the status
	 */
	protected final void setVertexProperty(Object key, Object value, int status) {
		setVertexTaggedProperty(key, value, VisidiaProperty.getPropertyTagFromStatus(status));
	}
	
	/**
	 * Sets the vertex property.
	 *
	 * @param key the key
	 * @param value the value
	 */
	protected final void setVertexProperty(Object key, Object value) {
		if (proc == null) return;
		Vertex destination = proc.getDestinationVertex();
		VisidiaProperty prop = destination.getVisidiaProperty(key);
		if (prop == null) setVertexTaggedProperty(key, value, VisidiaProperty.Tag.USER_PROPERTY);
		else setVertexTaggedProperty(key, value, prop.getTag());
	}

	/**
	 * Gets the identity of vertex on which the agent is located.
	 * 
	 * @return the vertex identity
	 */
	protected final int getVertexIdentity() {
		if (proc == null) return 0;
		return proc.getDestinationVertex().getId();
	}

	/**
	 * Gets the vertex label.
	 * 
	 * @return the vertex label
	 */
	protected final String getVertexLabel() {
		return getVertexProperty("label").toString();
	}
	
	/**
	 * Sets the vertex label.
	 * 
	 * @param label the new vertex label
	 */
	protected final void setVertexLabel(String label) {
		setVertexTaggedProperty("label", label, VisidiaProperty.Tag.PERSISTENT_PROPERTY);
	}
	
	/**
	 * Lock vertex properties.
	 */
	protected final void lockVertexProperties() {
		if (proc == null) return;
		Vertex actualVertex = this.proc.getDestinationVertex();
		actualVertex.lockProperties(this);
	}

	/**
	 * Unlock vertex properties.
	 */
	protected final void unlockVertexProperties() {
		if (proc == null) return;
		Vertex actualVertex = this.proc.getDestinationVertex();
		actualVertex.unlockProperties(this);
	}

    /**
     * Return true if the Vertex is locked, otherwise false.
     *
     * @see #lockVertexProperties()
     */
	protected final boolean vertexPropertiesLocked() {
		if (proc == null) return false;
		return this.proc.getDestinationVertex().locked();
    }

    /**
     * If the vertex is already locked, return false and does nothing, 
     * else return true and lock the vertex.
     */
	protected final boolean lockVertexIfPossible() {
        boolean lock;
        
        synchronized (askForLock) {
            lock = this.vertexPropertiesLocked();
            if (lock)
                return false;
            this.lockVertexProperties();
            return true;
        }
    }

	/**
	 * Gets the edge property value.
	 * 
	 * @param door the door
	 * @param key the key
	 * 
	 * @return the edge property value
	 */
	protected final Object getEdgeProperty(int door, Object key) {
		if (proc == null) return null;
		return proc.getEdgeProperty(proc.getDestinationVertex(), door, key);
	}

	/**
	 * Sets the edge property.
	 * 
	 * @param door the door
	 * @param key the key
	 * @param value the value
	 */
	protected final void setEdgeProperty(int door, Object key, Object value) {
		if (proc == null) return;
		proc.setEdgeProperty(proc.getDestinationVertex(), door, key, value);
	}

	/*************************/
	/* VISUALIZATION METHODS */
	/*************************/

	/**
	 * Sets the door state.
	 * 
	 * @param st the new state
	 * @param door the door
	 */
	protected final void setDoorState(EdgeState st, int door) {
		if (proc == null) return;
		proc.runningControl();
		try {
			proc.changeEdgeState(proc.getDestinationVertex(), door, st);
		} catch (InterruptedException e) {
			throw new SimulationAbortError();
		}
	}

	/********************/
	/* MOVEMENT METHODS */
	/********************/

	/**
	 * Moves the agent.
	 */
	protected final void move() {
		if (agentMover == null) return;
		try {
			this.agentMover.move();
		} catch (InterruptedException e) {
			throw new SimulationAbortError(e);
		} catch (MoveException e) {
			// TODO
			/*
			// This zone can be used to control new movement exception
			 if (e.getMouvementTypeException() == MoveException.NoDoorFound) {
				 this.processingAgentIsolated();
			 }
			 else if (e.getMouvementTypeException() == MoveException.SwitchedOffVertex) {
				 this.processingAgentWhenSwitchingOff();
			 }
			 */
		}
	}

    /**
     * Moves the agent back to the vertex from where it comes.
     */
	protected final void moveBack() {
    	this.moveToDoor(this.entryDoor());
    }

	/**
	 * Moves agent to door.
	 * 
	 * @param door the door
	 */
	protected final void moveToDoor(int door) {
		if (proc == null) return;
		proc.runningControl();
		try {
			proc.moveAgentTo(this, door);
		} catch (InterruptedException e) {
			throw new SimulationAbortError(e);
		}
	}

	/**
	 * Returns the door from which the agent comes.
	 * 
	 * @return the door
	 */
	protected final int entryDoor() {
		if (proc == null) return -1;
		return proc.getDestinationVertex().getDoorTo(proc.getOriginVertex());
	}

	/**
	 * Sets the agent mover.
	 * 
	 * @param name the new agent mover
	 */
	protected final void setAgentMover(String name) {
		String visidiaAgentMoverPath = VisidiaSettings.getInstance().getString(VisidiaSettings.Constants.VISIDIA_AGENT_MOVER_PATH);
		agentMover = (AgentMover) ClassIO.load(visidiaAgentMoverPath, name);
		if (agentMover != null) agentMover.setAgent(this);
	}
	
	/**
	 * Gets the destination vertex.
	 * 
	 * @return the destination vertex
	 */
	public final Vertex getDestinationVertex() {
		if (proc == null) return null;
		return proc.getDestinationVertex();
	}	

	/*************************************/
	/* METHODS NOT REFERENCED IN THE API */
	/*        (INTERNAL USE ONLY)        */
	/*************************************/

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		String name = getClass().getSimpleName();
		if (proc == null) return name;
		return name + " " + new Integer(proc.getId()).toString();
	}

	/**
	 * Sets the agent process.
	 * 
	 * @param proc the new agent process
	 */
	public final void setAgentProcess(AgentProcess proc) {
		this.proc = proc;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public final void run() {
		this.init();
		try {
			proc.deadAgent();
		} catch (InterruptedException e) {
			throw new SimulationAbortError(e);
		}
	}	
	
}
