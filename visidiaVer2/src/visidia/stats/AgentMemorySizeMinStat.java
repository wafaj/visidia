package visidia.stats;


// TODO: Auto-generated Javadoc
/**
 * The Class AgentMemorySizeMinStat.
 */
public class AgentMemorySizeMinStat extends AgentStat {

	/**
	 * Instantiates a new agent stat for memory size min.
	 * 
	 * @param agClass the agent class
	 */
	public AgentMemorySizeMinStat(Class<?> agClass) {
		super(agClass);
	}

	/**
	 * Instantiates a new agent stat for memory size min.
	 * 
	 * @param agClass the agent class
	 * @param agId the agent id
	 */
	public AgentMemorySizeMinStat(Class<?> agClass, Integer agId) {
		super(agClass, agId);
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#descriptionName()
	 */
	@Override
	protected String descriptionName() {
		return "Min of Memory Size [in WB key(s)]";
	}

}
