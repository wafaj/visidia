package visidia.simulation.command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import visidia.graph.Vertex;
import visidia.simulation.Console;
import visidia.simulation.evtack.VisidiaEvent;
import visidia.simulation.process.edgestate.EdgeState;

// TODO: Auto-generated Javadoc
/**
 * ChangeEdgeStateCommand is the command involved when a edge state changes.
 */
public class ChangeEdgeStateCommand extends Command {

	private static final long serialVersionUID = -5135612311568372988L;

	/** The vertex. */
	private int vertexId;

	/** The door. */
	private int door;

	/** The new edge state. */
	private EdgeState newEdgeState;

	/**
	 * Instantiates a new command to change an edge state.
	 * 
	 * @param door the door
	 * @param newEdgeState the new edge state
	 * @param vertexId the vertex id
	 */
	public ChangeEdgeStateCommand(int vertexId, int door, EdgeState newEdgeState) {
		super();
		this.vertexId = vertexId;
		this.door = door;
		this.newEdgeState = newEdgeState;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": vertexId=" + vertexId + ", door=" + door + ", newEdgeState=" + newEdgeState.toString();
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
		Vertex vertex = console.getGraph().getVertex(vertexId);


		for (CommandListener listener : listeners) {
			listener.edgeStateChanged(newEdgeState, vertex, vertex.getNeighborByDoor(door));
		}
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#executeAfterAck()
	 */
	@Override
	public void executeAfterAck() {
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#serialize(java.io.ObjectOutputStream)
	 */
	@Override
	public void serialize(ObjectOutputStream out) throws IOException {
		out.writeInt(vertexId);
		out.writeInt(door);
		out.writeObject(newEdgeState);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#deserialize(java.io.ObjectInputStream)
	 */
	@Override
	public void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {
		vertexId = in.readInt();
		door = in.readInt();
		newEdgeState = (EdgeState) in.readObject();
	}

}
