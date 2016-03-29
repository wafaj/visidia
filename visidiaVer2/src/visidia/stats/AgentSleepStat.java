package visidia.stats;

// TODO: Auto-generated Javadoc
/**
 * The Class AgentSleepStat.
 */
public class AgentSleepStat extends AgentStat {

	/**
	 * Instantiates a new agent sleep stat.
	 * 
	 * @param agClass the agent class
	 */
	public AgentSleepStat(Class<?> agClass) {
		super(agClass);
	}

	/**
	 * Instantiates a new agent sleep stat.
	 * 
	 * @param agClass the agent class
	 * @param agId the agent id
	 */
	public AgentSleepStat(Class<?> agClass, Integer agId) {
		super(agClass, agId);
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#descriptionName()
	 */
	@Override
	public String descriptionName() {
		return "Sleep time";
	}

}
