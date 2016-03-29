package visidia.stats;


// TODO: Auto-generated Javadoc
/**
 * The Class AgentMemorySizeSumStat.
 */
public class AgentMemorySizeSumStat extends AgentStat {

	/**
	 * Instantiates a new agent stat for memory size sum.
	 * 
	 * @param agClass the agent class
	 */
	public AgentMemorySizeSumStat(Class<?> agClass) {
		super(agClass);
	}
	
	/**
	 * Instantiates a new agent stat for memory size sum.
	 * 
	 * @param agClass the agent class
	 * @param agId the agent id
	 */
	public AgentMemorySizeSumStat(Class<?> agClass, Integer agId) {
		super(agClass, agId);
	}
	
	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#descriptionName()
	 */
	@Override
	protected String descriptionName() {
		return "Sum of Memory Size [in WB key(s)]";
	}

}
