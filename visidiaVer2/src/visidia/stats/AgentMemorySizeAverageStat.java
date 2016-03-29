package visidia.stats;


// TODO: Auto-generated Javadoc
/**
 * The Class AgentMemorySizeAverageStat.
 */
public class AgentMemorySizeAverageStat extends AgentStat {

	/**
	 * Instantiates a new agent stat for memory size average.
	 * 
	 * @param agClass the agent class
	 */
	public AgentMemorySizeAverageStat(Class<?> agClass) {
		super(agClass);
	}

	/**
	 * Instantiates a new agent stat for memory size average.
	 * 
	 * @param agClass the agent class
	 * @param agId the agent id
	 */
	public AgentMemorySizeAverageStat(Class<?> agClass, Integer agId) {
		super(agClass, agId);
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#descriptionName()
	 */
	@Override
	protected String descriptionName() {
		return "Memory Average Size";
	}

}
