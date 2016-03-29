package visidia.stats;

// TODO: Auto-generated Javadoc
/**
 * The Class AgentCreationStat.
 */
public class AgentCreationStat extends AgentStat {

	/**
	 * Instantiates a new agent creation stat.
	 * 
	 * @param agClass the agent class
	 */
	public AgentCreationStat(Class<?> agClass) {
		super(agClass);
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#descriptionName()
	 */
	@Override
	protected String descriptionName() {
		return "Created agent";
	}

}
