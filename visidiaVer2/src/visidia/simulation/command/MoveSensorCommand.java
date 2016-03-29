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
 * MoveSensorCommand is the command involved when a sensor moves.
 */
public class MoveSensorCommand extends Command {

	private static final long serialVersionUID = -5272413133290566189L;

	/** The sensor id. */
	private int sensorId;

	/** The origin vertex. */
	private int originId;

	/** The destination vertex. */
	private int destinationId;

	/**
	 * Instantiates a new move sensor command.
	 * 
	 * @param sensorId the sensor id
	 * @param originId the origin id
	 * @param destinationId the destination id
	 */
	public MoveSensorCommand(int sensorId, int originId, int destinationId) {
		super();
		this.sensorId = sensorId;
		this.originId = originId;
		this.destinationId = destinationId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": sensorId=" + sensorId + ": originId=" + originId + ", destinationId=" + destinationId;
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#needSynchronization()
	 */
	@Override
	public boolean needSynchronization() {
		return true;
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
		Console console = event.getConsole();

		Graph supportGraph = ((SensorGraph) console.getGraph()).getSupportGraph();
		SupportVertex origin = (SupportVertex) supportGraph.getVertex(originId);
		SupportVertex destination = (SupportVertex) supportGraph.getVertex(destinationId);

		CommandListener[] listeners = console.getCommandListeners();

		for (CommandListener listener : listeners) {
			if (listener instanceof SensorCommandListener)
				((SensorCommandListener) listener).sensorMoved(sensorId, origin, destination, event);
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
		out.writeInt(sensorId);
		out.writeInt(originId);
		out.writeInt(destinationId);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#deserialize(java.io.ObjectInputStream)
	 */
	@Override
	public void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {
		sensorId = in.readInt();
		originId = in.readInt();
		destinationId = in.readInt();
	}

}
