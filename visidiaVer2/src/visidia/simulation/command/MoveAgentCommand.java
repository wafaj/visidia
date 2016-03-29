package visidia.simulation.command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import visidia.graph.Vertex;
import visidia.simulation.Console;
import visidia.simulation.evtack.VisidiaEvent;
import visidia.simulation.process.AgentProcess;
import visidia.simulation.process.ProcessType;
import visidia.simulation.process.agent.Agent;
import visidia.stats.AgentCreationStat;
import visidia.stats.AgentDeathStat;
import visidia.stats.AgentMaxMoveStat;
import visidia.stats.AgentMaxNbStat;
import visidia.stats.AgentMemorySizeAverageStat;
import visidia.stats.AgentMemorySizeMaxStat;
import visidia.stats.AgentMemorySizeMinStat;
import visidia.stats.AgentMemorySizeSumStat;
import visidia.stats.AgentMoveStat;
import visidia.stats.Statistics;

// TODO: Auto-generated Javadoc
/**
 * MoveAgentCommand is the command involved when an agent moves.
 */
public class MoveAgentCommand extends Command {

	private static final long serialVersionUID = -4319954310518773103L;

	/** The process id. */
	private int processId;

	/** The origin vertex. */
	private int originId;

	/** The destination vertex. */
	private int destinationId;

	/**
	 * Instantiates a new move agent command.
	 * 
	 * @param processId the process id
	 * @param originId the origin id
	 * @param destinationId the destination id
	 */
	public MoveAgentCommand(int processId, int originId, int destinationId) {
		super();
		this.processId = processId;
		this.originId = originId;
		this.destinationId = destinationId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.getClass().getSimpleName() + ": processId=" + processId + ": originId=" + originId + ", destinationId=" + destinationId;
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#generateAck()
	 */
	@Override
	public boolean generateImmediateAck() {
		return false;
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#needSynchronization()
	 */
	@Override
	public boolean needSynchronization() {
		return true;
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#executeBeforeAck(visidia.simulation.evtack.VisidiaEvent)
	 */
	@Override
	public void executeBeforeAck(VisidiaEvent event) {
		Console console = event.getConsole();

		Vertex origin = console.getGraph().getVertex(originId);
		Vertex destination = console.getGraph().getVertex(destinationId);
		ProcessType sender = console.getProcess(processId);

		if (sender != null) makeStatOnMove(sender);

		Agent ag = console.getAgent(processId);

		console.removeAgentFromVertex(origin, ag);
		CommandListener[] listeners = console.getCommandListeners();

		for (CommandListener listener : listeners) {
			listener.agentMoved(processId, origin, destination, event);
		}
		console.addAgentToVertex(destination, ag);
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
		out.writeInt(originId);
		out.writeInt(destinationId);
	}

	/* (non-Javadoc)
	 * @see visidia.simulation.command.Command#deserialize(java.io.ObjectInputStream)
	 */
	@Override
	public void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException {
		processId = in.readInt();
		originId = in.readInt();
		destinationId = in.readInt();
	}

	/**
	 * Make all statistics when an agent moves.
	 * 
	 * @param sender the sender
	 */
	private void makeStatOnMove(ProcessType sender) {
		Agent ag = ((AgentProcess) sender).getAgent();
		Class<?> agClass = ag.getClass();
		int agId = sender.getId();
		int agWbSize = ag.getPropertyKeys().size();
		Statistics stats = sender.getServer().getConsole().getStats();

		// Max number of agents by Class of agent
		stats.max(new AgentMaxNbStat(agClass), stats.getOccurrencesOf(new AgentCreationStat(agClass)) - stats.getOccurrencesOf(new AgentDeathStat(agClass)));

		// Max number of steps by Class of agent
		stats.max(new AgentMaxMoveStat(agClass), stats.getOccurrencesOf(new AgentMoveStat(agClass, agId)));

		// Number of steps by Class of agent and by Agent
		stats.add(new AgentMoveStat(agClass));
		stats.add(new AgentMoveStat(agClass, agId));

		// Sum of the size of memory by Class of agent and by agent
		stats.add(new AgentMemorySizeSumStat(agClass), agWbSize);
		stats.add(new AgentMemorySizeSumStat(agClass, agId), agWbSize);

		// Max, min and average size of memory by Class of agent
		stats.max(new AgentMemorySizeMaxStat(agClass), agWbSize);
		stats.min(new AgentMemorySizeMinStat(agClass), agWbSize);
		stats.replace(new AgentMemorySizeAverageStat(agClass), stats.getOccurrencesOf(new AgentMemorySizeSumStat(agClass)) / stats.getOccurrencesOf(new AgentMoveStat(agClass)));
	}

}
