package visidia.stats;

// TODO: Auto-generated Javadoc
/**
 * The Class AgentAccessVertexWhiteBoardStat.
 */
public class AgentAccessVertexWhiteBoardStat extends AgentStat {

	/**
	 * Instantiates a new agent stat for access to vertex white board.
	 * 
	 * @param agClass the agent class
	 */
	public AgentAccessVertexWhiteBoardStat(Class<?> agClass) {
		super(agClass);
	}

	/**
	 * Instantiates a new agent stat for access to vertex white board.
	 * 
	 * @param agClass the agent class
	 * @param agId the agent id
	 */
	public AgentAccessVertexWhiteBoardStat(Class<?> agClass, Integer agId) {
		super(agClass, agId);
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#descriptionName()
	 */
	@Override
	protected String descriptionName() {
		return "Vertex WB access";
	}

}
