package visidia.simulation.process.agent;

import java.util.Iterator;

import visidia.simulation.Console;
import visidia.simulation.command.NewPulseCommand;

// TODO: Auto-generated Javadoc
/**
 * Extend this class to implement Synchronized Agents.
 * 
 * @see Agent
 */
public abstract class SynchronousAgent extends Agent {

	private static final long serialVersionUID = -7771920160621352372L;

	/**
	 * Defines what two agents (current agent and the one given in parameter) do when they meet.
	 * 
	 * @param agent the agent
	 */
	protected void planning(SynchronousAgent agent) {
		//System.out.println(this.toString() + " meets " + agent.toString());
	}

	/**
	 * Gets the current pulse.
	 * 
	 * @return the pulse
	 */
	public final int getPulse() {
		return this.proc.getServer().getConsole().getPulse();
	}

	/**
	 * Call this method when you want synchronization between agents. Every
	 * synchronized agent will wait until the last has finished.
	 */
	public final void nextPulse() {
		try {
			Console console = this.proc.getServer().getConsole();
			Object lockSync = console.getLockSyncObject();
			synchronized (lockSync) {
				int countNextPulse = (console.getCountNextPulse() + 1) % (console.getNbProcesses() - console.getTerminatedThreadCount());
				console.setCountNextPulse(countNextPulse);
				organizeMeeting();

				if (countNextPulse == 0) {
					try {
						//NewPulseCommand cmd = new NewPulseCommand(proc, this.getPulse());
						NewPulseCommand cmd = new NewPulseCommand(proc.getId(), this.getPulse());
						proc.getServer().sendToConsole(cmd);
					} catch (Exception e) {
					}
					console.setPulse(this.getPulse() + 1);
				}
				lockSync.wait();
			}
		} catch (Exception e) {
		}
	}

	/*************************************/
	/* METHODS NOT REFERENCED IN THE API */
	/*        (INTERNAL USE ONLY)        */
	/*************************************/

	/**
	 * Organize a meeting between agents on the same vertex.
	 */
	private void organizeMeeting() {
		// can only meet an agent with a higher id to prevent from duplicates
		Iterator<Agent> agents = this.agentsOnVertex().iterator();
		while (agents.hasNext()) {
			Agent ag = agents.next();
			if (ag instanceof SynchronousAgent && !ag.equals(this) && ag.proc.getId() > this.proc.getId())
				planning((SynchronousAgent) ag);
		}
	}

}

