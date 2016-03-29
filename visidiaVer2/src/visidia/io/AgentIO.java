package visidia.io;

import visidia.misc.ClassIdentifier;
import visidia.simulation.process.agent.Agent;

// TODO: Auto-generated Javadoc
/**
 * This class deals with input operations on agents.
 */
public class AgentIO extends ClassIO {

	/**
	 * Instantiates a new agent input/output.
	 * 
	 * @param classId the class identifier
	 */
	public AgentIO(ClassIdentifier classId) {
		super(classId);
	}
	
	/**
	 * Loads the current file as an agent.
	 * 
	 * @return the agent
	 * 
	 * @see visidia.io.VisidiaIO#load()
	 */
	public Agent load() {
		Object obj = super.load();
		Agent agent = null;

		try {
			agent = (Agent) obj;
			if (agent == null) throw new NullPointerException();
		} catch (Exception e) {
			agent = (Agent) super.recreateInSystemClassLoader(obj);
		}
		
		return agent;
	}

	/**
	 * This function does nothing. An agent cannot be saved.
	 * 
	 * @param object the object
	 * 
	 * @see visidia.io.VisidiaIO#save(java.lang.Object)
	 */
	public void save(Object object) {
		// nothing to be done here
	}

}
