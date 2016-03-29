package visidia.simulation.server;

import visidia.simulation.Console;
import visidia.simulation.command.Command;

// TODO: Auto-generated Javadoc
/**
 * This class implements a local server.
 */
public class LocalServer extends Server {

	/**
	 * Instantiates a new local server.
	 * 
	 * @param console the console
	 */
	public LocalServer(Console console) {
		super(console);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.server.Server#sendToConsole(visidia.simulation.command.Command)
	 */
	@Override
	public void sendToConsole(Command cmd) {
		try {
			console.runCommand(cmd);
		} catch (InterruptedException e) {
			//e.printStackTrace();
		}
	}

}
