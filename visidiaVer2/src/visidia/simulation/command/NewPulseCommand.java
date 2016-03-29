package visidia.simulation.command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import visidia.simulation.Console;
import visidia.simulation.evtack.VisidiaEvent;
import visidia.simulation.process.ProcessType;
import visidia.stats.PulseStat;
import visidia.stats.Statistics;

// TODO: Auto-generated Javadoc
/**
 * NewPulseCommand is the command involved when a new pulse occurs.
 */
public class NewPulseCommand extends Command {

	private static final long serialVersionUID = 3162723203516822669L;

	/** The process id. */
	private int processId;

	/** The pulse. */
	private int pulse;

	/** The console. */
	transient private Console console;

	/**
	 * Instantiates a new new pulse command.
	 * 
	 * @param pulse the pulse
	 * @param processId the process id
	 */
	public NewPulseCommand(int processId, int pulse) {
		super();
		this.processId = processId;
		this.pulse = pulse;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": processId=" + processId + ": pulse=" + pulse;
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
		console = event.getConsole();
		CommandListener[] listeners = console.getCommandListeners();
		ProcessType sender = console.getProcess(processId);

		for (CommandListener listener : listeners) {
			listener.pulseChanged(sender, pulse);
		}

		Statistics stats = sender.getServer().getConsole().getStats();
		stats.add(new PulseStat());
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#executeAfterAck()
	 */
	@Override
	public void executeAfterAck() throws InterruptedException {
		console.notifyAllLockSync();
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#serialize(java.io.ObjectOutputStream)
	 */
	@Override
	public void serialize(ObjectOutputStream out) throws IOException {
		out.writeInt(processId);
		out.writeInt(pulse);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#deserialize(java.io.ObjectInputStream)
	 */
	@Override
	public void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {
		processId = in.readInt();
		pulse = in.readInt();
	}

}
