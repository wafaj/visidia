package visidia.simulation.evtack;

import java.io.Serializable;

import visidia.simulation.Console;
import visidia.simulation.command.Command;

// TODO: Auto-generated Javadoc
/**
 * This class corresponds to a simulation event.
 */
public class VisidiaEvent implements Serializable {

	private static final long serialVersionUID = -8736037779081837063L;

	/** The lock id. */
	private Long lockId;
	
	/** The command. */
	private Command command;

	/** The console. */
	transient private Console console;
	
	/**
	 * Instantiates a new event.
	 * 
	 * @param lockId the lock id
	 * @param command the command
	 * @param console the console
	 */
	public VisidiaEvent(Long lockId, Command command, Console console) {
		this.lockId = lockId;
		this.command = command;
		this.console = console;
	}

	/**
	 * Gets the lock id.
	 * 
	 * @return the lock id
	 */
	public Long getLockId() {
		return lockId;
	}

	/**
	 * Gets the command.
	 * 
	 * @return the command
	 */
	public Command getCommand() {
		return command;
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
	 * Sets the console.
	 * 
	 * @param console the new console
	 */
	public void setConsole(Console console) {
		this.console = console;
	}

}
