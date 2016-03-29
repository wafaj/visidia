package visidia.simulation.server;

import java.util.Enumeration;
import java.util.Vector;

import visidia.simulation.Console;
import visidia.simulation.command.Command;
import visidia.simulation.process.ProcessType;

// TODO: Auto-generated Javadoc
/**
 * This is the abstract base class to represent a server in visidia. A server is the communication gateway between console and process.
 */
public abstract class Server {

	/** The processes. */
	private Vector<ProcessType> procs;

	/** The console. */
	protected Console console;

	/**
	 * Instantiates a new server.
	 * 
	 * @param console the console
	 */
	protected Server(Console console) {
		procs = new Vector<ProcessType>(10, 10);
		this.console = console;
	}

	/**
	 * Sends a command to the console.
	 * 
	 * @param cmd the command
	 */
	public abstract void sendToConsole(Command cmd);

	/**
	 * Adds a process.
	 * 
	 * @param proc the process
	 */
	public void addProcess(ProcessType proc) {
		if (proc != null)
			procs.add(proc);
	}

	/**
	 * Gets the console.
	 * 
	 * @return the console
	 */
	public Console getConsole() {
		return console;
	}

	/**
	 * Gets the process.
	 * 
	 * @param processId the process id
	 * 
	 * @return the process
	 */
	public ProcessType getProcess(int processId) {
		// search process in this server
		Enumeration<ProcessType> processes = procs.elements();
		while (processes.hasMoreElements()) {
			ProcessType p = processes.nextElement();
			if (p.getId() == processId) return p;
		}

		// enlarge research field to other servers
		return console.getProcess(processId);
	}
}
