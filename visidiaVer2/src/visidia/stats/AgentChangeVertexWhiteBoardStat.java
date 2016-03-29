package visidia.stats;

// TODO: Auto-generated Javadoc
/**
 * The Class AgentChangeVertexWhiteBoardStat.
 */
public class AgentChangeVertexWhiteBoardStat extends AgentStat {

	/**
	 * Instantiates a new agent stat for change in vertex white board.
	 * 
	 * @param agClass the agent class
	 */
	public AgentChangeVertexWhiteBoardStat(Class<?> agClass) {
		super(agClass);
	}

	/**
	 * Instantiates a new agent stat for change in vertex white board.
	 * 
	 * @param agClass the agent class
	 * @param agId the agent id
	 */
	public AgentChangeVertexWhiteBoardStat(Class<?> agClass, Integer agId) {
		super(agClass, agId);
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#descriptionName()
	 */
	@Override
	protected String descriptionName() {
		return "Vertex WB changes";
	}

}
