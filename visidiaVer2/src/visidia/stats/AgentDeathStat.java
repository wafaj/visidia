package visidia.stats;

// TODO: Auto-generated Javadoc
/**
 * The Class AgentDeathStat.
 */
public class AgentDeathStat extends AgentStat {

	/**
	 * Instantiates a new agent death stat.
	 * 
	 * @param agClass the agent class
	 */
	public AgentDeathStat(Class<?> agClass) {
		super(agClass);
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#descriptionName()
	 */
	@Override
	protected String descriptionName() {
		return "Dead agents";
	}

}
