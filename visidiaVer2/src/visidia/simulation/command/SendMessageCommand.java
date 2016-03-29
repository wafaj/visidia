package visidia.simulation.command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import visidia.simulation.Console;
import visidia.simulation.evtack.VisidiaEvent;
import visidia.simulation.process.MessageProcess;
import visidia.simulation.process.ProcessType;
import visidia.simulation.process.messages.MessagePacket;
import visidia.stats.MessageNbSentStat;
import visidia.stats.Statistics;

// TODO: Auto-generated Javadoc
/**
 * SendMessageCommand is the command involved when a message is sent.
 */
public class SendMessageCommand extends Command {

	private static final long serialVersionUID = 5769373717874167500L;

	/** The sender id. */
	private int senderId;

	/** The receiver id. */
	private int receiverId;

	/** The message packet. */
	private MessagePacket msgPacket;

	/** The console. */
	transient private Console console;

	/**
	 * Instantiates a new command to send a message.
	 * 
	 * @param receiverId the receiver id
	 * @param msgPacket the message packet
	 * @param senderId the sender id
	 */
	public SendMessageCommand(int senderId, int receiverId, MessagePacket msgPacket) {
		super();
		this.senderId = senderId;
		this.receiverId = receiverId;
		this.msgPacket = msgPacket;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": senderId=" + senderId + ": receiverId=" + receiverId + ", msgPacket=" + msgPacket.toString();
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#needSynchronization()
	 */
	@Override
	public boolean needSynchronization() {
		return false;
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#generateAck()
	 */
	@Override
	public boolean generateImmediateAck() {
		return false;
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#executeBeforeAck(visidia.simulation.evtack.VisidiaEvent)
	 */
	@Override
	public void executeBeforeAck(VisidiaEvent event) {
		console = event.getConsole();
		Statistics stats = console.getStats();
		stats.add(new MessageNbSentStat());
		stats.add(new MessageNbSentStat(console.getSimulationId()));

		CommandListener[] listeners = console.getCommandListeners();

		for (CommandListener listener : listeners) {
			listener.messageSent(senderId, receiverId, msgPacket.message(), event);
		}
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#executeAfterAck()
	 */
	@Override
	public void executeAfterAck() throws InterruptedException {
		ProcessType sender = console.getProcess(senderId);
		MessageProcess receiver = (MessageProcess) sender.getServer().getProcess(receiverId);
		receiver.putMessage(msgPacket);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#serialize(java.io.ObjectOutputStream)
	 */
	@Override
	public void serialize(ObjectOutputStream out) throws IOException {
		out.writeInt(senderId);
		out.writeInt(receiverId);
		out.writeObject(msgPacket);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#deserialize(java.io.ObjectInputStream)
	 */
	@Override
	public void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {
		senderId = in.readInt();
		receiverId = in.readInt();
		msgPacket = (MessagePacket) in.readObject();
	}

}
