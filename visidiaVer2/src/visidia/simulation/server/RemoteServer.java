package visidia.simulation.server;

import visidia.simulation.Console;
import visidia.simulation.command.Command;

// TODO: Auto-generated Javadoc
/**
 * This class implements a remote server.
 */
public class RemoteServer extends Server {

	/**
	 * Instantiates a new remote server.
	 * 
	 * @param console the console
	 */
	public RemoteServer(Console console) {
		super(console);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.server.Server#sendToConsole(visidia.simulation.command.Command)
	 */
	@Override
	public void sendToConsole(Command cmd) {
		// TODO Auto-generated method stub

	}

}
