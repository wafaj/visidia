package visidia.simulation.command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import visidia.misc.property.VisidiaProperty;
import visidia.simulation.Console;
import visidia.simulation.evtack.VisidiaEvent;

// TODO: Auto-generated Javadoc
/**
 * ChangeNodePropertyCommand is the command involved when a node property changes.
 */
public class ChangeNodePropertyCommand extends Command {

	private static final long serialVersionUID = 3613827235577270294L;

	/** The node id. */
	private int nodeId;

	/** The property. */
	private VisidiaProperty property;
	
	/**
	 * Instantiates a new command to change a node property.
	 * 
	 * @param nodeId the node id
	 * @param property the property
	 */
	public ChangeNodePropertyCommand(int nodeId, VisidiaProperty property) {
		super();
		this.nodeId = nodeId;
		this.property = property;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": nodeId=" + nodeId + ", key=" + property.getKey() + ", value=" + property.getValue().toString();
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

		for (CommandListener listener : listeners) {
			listener.nodePropertyChanged(nodeId, property);
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
		out.writeInt(nodeId);
		out.writeObject(property);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#deserialize(java.io.ObjectInputStream)
	 */
	@Override
	public void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {
		nodeId = in.readInt();
		property = (VisidiaProperty) in.readObject();
	}

}
