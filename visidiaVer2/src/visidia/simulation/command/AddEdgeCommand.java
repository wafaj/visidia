package visidia.simulation.command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import visidia.graph.Vertex;
import visidia.simulation.Console;
import visidia.simulation.evtack.VisidiaEvent;

// TODO: Auto-generated Javadoc
/**
 * AddEdgeCommand is the command involved when an edge is added (in sensor simulation).
 */
public class AddEdgeCommand extends Command {

	private static final long serialVersionUID = 7368300563771433262L;

	/** The origin. */
	transient private Vertex origin;

	/** The origin id. */
	private int originId;

	/** The destination. */
	transient private Vertex destination;

	/** The destination id. */
	private int destinationId;

	/**
	 * Instantiates a new command to add an edge.
	 * 
	 * @param originId the origin id
	 * @param destinationId the destination id
	 */
	public AddEdgeCommand(int originId, int destinationId) {
		super();
		this.originId = originId;
		this.destinationId = destinationId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": originId=" + originId + ", destinationId=" + destinationId;
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#needSynchronization()
	 */
	@Override
	public boolean needSynchronization() {
		return true;
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
		CommandListener[] listeners = console.getCommandListeners();
		origin = console.getGraph().getVertex(originId);
		destination = console.getGraph().getVertex(destinationId);

		for (CommandListener listener : listeners) {
			if (listener instanceof SensorCommandListener)
				((SensorCommandListener) listener).edgeAdded(origin, destination);
		}
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#executeAfterAck()
	 */
	@Override
	public void executeAfterAck() throws InterruptedException {
		origin.linkTo(destination, false);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#serialize(java.io.ObjectOutputStream)
	 */
	@Override
	public void serialize(ObjectOutputStream out) throws IOException {
		out.writeInt(originId);
		out.writeInt(destinationId);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#deserialize(java.io.ObjectInputStream)
	 */
	@Override
	public void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {
		originId = in.readInt();
		destinationId = in.readInt();
	}

}
