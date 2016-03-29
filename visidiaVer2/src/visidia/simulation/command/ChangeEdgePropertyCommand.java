package visidia.simulation.command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import visidia.graph.Edge;
import visidia.misc.property.VisidiaProperty;
import visidia.simulation.Console;
import visidia.simulation.evtack.VisidiaEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class ChangeEdgePropertyCommand.
 */
public class ChangeEdgePropertyCommand extends Command {
	
	private static final long serialVersionUID = 2558318933626539669L;

	/** The origin id. */
	private int originId;

	/** The destination id. */
	private int destinationId;
	
	/** The property. */
	private VisidiaProperty property;

	/**
	 * Instantiates a new command to change a node property.
	 * 
	 * @param originId the origin id
	 * @param destinationId the destination id
	 * @param property the property
	 */
	public ChangeEdgePropertyCommand(int originId, int destinationId, VisidiaProperty property) {
		super();
		this.originId = originId;
		this.destinationId = destinationId;
		this.property = property;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": originId=" + originId + ", destinationId=" + destinationId + ", key=" + property.getKey() + ", value=" + property.getValue().toString();
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
		Edge edge = console.getGraph().getVertex(originId).getEdge(console.getGraph().getVertex(destinationId));

		for (CommandListener listener : listeners) {
			listener.edgePropertyChanged(edge, property);
		}
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
		out.writeInt(originId);
		out.writeInt(destinationId);
		out.writeObject(property);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#deserialize(java.io.ObjectInputStream)
	 */
	@Override
	public void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {
		originId = in.readInt();
		destinationId = in.readInt();
		property = (VisidiaProperty) in.readObject();
	}

}
