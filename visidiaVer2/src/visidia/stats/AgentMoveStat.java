package visidia.stats;

// TODO: Auto-generated Javadoc
/**
 * The Class AgentMoveStat.
 */
public class AgentMoveStat extends AgentStat {

	/**
	 * Instantiates a new agent move stat.
	 * 
	 * @param agClass the agent class
	 */
	public AgentMoveStat(Class<?> agClass) {
		super(agClass);
	}

	/**
	 * Instantiates a new agent move stat.
	 * 
	 * @param agClass the agent class
	 * @param agId the agent id
	 */
	public AgentMoveStat(Class<?> agClass, Integer agId) {
		super(agClass, agId);
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#descriptionName()
	 */
	@Override
	public String descriptionName() {
		return "Moves";
	}

}
