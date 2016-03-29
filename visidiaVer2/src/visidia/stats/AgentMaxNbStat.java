package visidia.stats;

// TODO: Auto-generated Javadoc
/**
 * The Class AgentNbMaxStat.
 */
public class AgentMaxNbStat extends AgentStat {

	/**
	 * Instantiates a stat for max number of agents.
	 * 
	 * @param agClass the agent class
	 */
	public AgentMaxNbStat(Class<?> agClass) {
		super(agClass);
	}

	/**
	 * Instantiates a stat for max number of agents.
	 * 
	 * @param agClass the agent class
	 * @param agId the agent id
	 */
	public AgentMaxNbStat(Class<?> agClass, Integer agId) {
		super(agClass, agId);
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#descriptionName()
	 */
	@Override
	protected String descriptionName() {
		return "Max Number of Agents";
	}

}
