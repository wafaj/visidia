package visidia.simulation.command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import visidia.graph.Vertex;
import visidia.simulation.Console;
import visidia.simulation.evtack.VisidiaEvent;

// TODO: Auto-generated Javadoc
/**
 * RemoveEdgeCommand is the command involved when an edge is removed (in sensor simulation).
 */
public class RemoveEdgeCommand extends Command {

	private static final long serialVersionUID = -1029829047729851088L;

	/** The origin. */
	transient private Vertex origin;
	
	/** The origin id. */
	private int originId;

	/** The destination. */
	transient private Vertex destination;
	
	/** The destination id. */
	private int destinationId;

	/**
	 * Instantiates a new command to remove an edge.
	 * 
	 * @param originId the origin id
	 * @param destinationId the destination id
	 */
	public RemoveEdgeCommand(int originId, int destinationId) {
		super();
		this.originId = originId;
		this.destinationId = destinationId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": originId=" + originId + ": destinationId=" + destinationId;
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
		origin = console.getGraph().getVertex(originId);
		destination = console.getGraph().getVertex(destinationId);

		CommandListener[] listeners = console.getCommandListeners();

		for (CommandListener listener : listeners) {
			if (listener instanceof SensorCommandListener)
				((SensorCommandListener) listener).edgeRemoved(origin, destination);
		}
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#executeAfterAck()
	 */
	@Override
	public void executeAfterAck() throws InterruptedException {
		origin.unlink(destination);
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
