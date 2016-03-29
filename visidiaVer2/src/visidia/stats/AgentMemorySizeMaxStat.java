package visidia.stats;


// TODO: Auto-generated Javadoc
/**
 * The Class AgentMemorySizeMaxStat.
 */
public class AgentMemorySizeMaxStat extends AgentStat {

	/**
	 * Instantiates a new agent stat for memory size max.
	 * 
	 * @param agClass the agent class
	 */
	public AgentMemorySizeMaxStat(Class<?> agClass) {
		super(agClass);
	}

	/**
	 * Instantiates a new agent stat for memory size max.
	 * 
	 * @param agClass the agent class
	 * @param agId the agent id
	 */
	public AgentMemorySizeMaxStat(Class<?> agClass, Integer agId) {
		super(agClass, agId);
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#descriptionName()
	 */
	@Override
	protected String descriptionName() {
		return "Max of Memory Size [in WB key(s)]";
	}

}
