package visidia.stats;

// TODO: Auto-generated Javadoc
/**
 * The Class AgentMaxMoveStat.
 */
public class AgentMaxMoveStat extends AgentStat {

	/**
	 * Instantiates a new agent stat for max number of steps.
	 * 
	 * @param agClass the agent class
	 */
	public AgentMaxMoveStat(Class<?> agClass) {
		super(agClass);
	}

	/**
	 * Instantiates a new agent stat for max number of steps.
	 * 
	 * @param agClass the agent class
	 * @param agId the agent id
	 */
	public AgentMaxMoveStat(Class<?> agClass, Integer agId) {
		super(agClass, agId);
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#descriptionName()
	 */
	@Override
	protected String descriptionName() {
		return "Max Step";
	}

}
