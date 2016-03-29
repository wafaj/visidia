package visidia.simulation.command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import visidia.graph.Graph;
import visidia.graph.SensorGraph;
import visidia.graph.SupportVertex;
import visidia.simulation.Console;
import visidia.simulation.evtack.VisidiaEvent;

// TODO: Auto-generated Javadoc
/**
 * SetSensorNumberCommand is the command involved when setting a sensor number.
 */
public class SetSensorNumberCommand extends Command {

	private static final long serialVersionUID = -3326092866857162208L;

	/** The display. */
	private int supportVertexId;

	/**
	 * Instantiates a new move sensor command.
	 * 
	 * @param supportVertexId the support vertex id
	 */
	public SetSensorNumberCommand(int supportVertexId) {
		super();
		this.supportVertexId = supportVertexId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": supportVertexId=" + supportVertexId;
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

		Graph supportGraph = ((SensorGraph) console.getGraph()).getSupportGraph();
		SupportVertex supportVertex = (SupportVertex) supportGraph.getVertex(supportVertexId);

		CommandListener[] listeners = console.getCommandListeners();

		for (CommandListener listener : listeners) {
			if (listener instanceof SensorCommandListener)
				((SensorCommandListener) listener).sensorNumberSet(supportVertex);
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
		out.writeInt(supportVertexId);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#deserialize(java.io.ObjectInputStream)
	 */
	@Override
	public void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {
		supportVertexId = in.readInt();
	}

}
