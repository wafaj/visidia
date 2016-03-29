package visidia.simulation.command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import visidia.simulation.Console;
import visidia.simulation.evtack.VisidiaEvent;
import visidia.simulation.process.ProcessType;

// TODO: Auto-generated Javadoc
/**
 * RemoveAgentCommand is the command involved when an agent is removed.
 */
public class RemoveAgentCommand extends Command {

	private static final long serialVersionUID = 9115129592888705054L;

	/** The process id. */
	private int processId;

	/**
	 * Instantiates a new "remove the agent" command.
	 * 
	 * @param processId the process id
	 */
	public RemoveAgentCommand(int processId) {
		super();
		this.processId = processId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": processId=" + processId;
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#needSynchronization()
	 */
	@Override
	public boolean needSynchronization() {
		return false;
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#generateImmediateAck()
	 */
	@Override
	public boolean generateImmediateAck() {
		return true;
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#executeBeforeAck(visidia.simulation.evtack.VisidiaEvent)
	 */
	@Override
	public void executeBeforeAck(VisidiaEvent event) {
		Console console = event.getConsole();
		ProcessType sender = console.getProcess(processId);

		CommandListener[] listeners = console.getCommandListeners();

		for (CommandListener listener : listeners)
			listener.agentDead(sender.getId(), event);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#executeAfterAck()
	 */
	@Override
	public void executeAfterAck() throws InterruptedException {
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#serialize(java.io.ObjectOutputStream)
	 */
	@Override
	public void serialize(ObjectOutputStream out) throws IOException {
		out.writeInt(processId);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#deserialize(java.io.ObjectInputStream)
	 */
	@Override
	public void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {
		processId = in.readInt();
	}

}
