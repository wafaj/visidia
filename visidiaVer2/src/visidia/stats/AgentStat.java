package visidia.stats;

// TODO: Auto-generated Javadoc
/**
 * The Class AgentStat.
 */
public abstract class AgentStat extends VisidiaStat {

	/** The agent class. */
	private Class<?> agClass;
	
	/** The agent id. */
	private Integer agId;

	/**
	 * Instantiates a new agent stat.
	 * 
	 * @param agClass the agent class
	 */
	public AgentStat(Class<?> agClass) {
		this.agClass = agClass;
	}

	/**
	 * Instantiates a new agent stat.
	 * 
	 * @param agClass the agent class
	 * @param agId the agent id
	 */
	public AgentStat(Class<?> agClass, Integer agId) {
		this.agClass = agClass;
		this.agId = agId;
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (super.equals(o) == false) {
			return false;
		}

		AgentStat o2 = (AgentStat) o;
		return this.agClass.equals(o2.agClass) && this.agId == o2.agId;
	}

	/**
	 * Gets the agent class name.
	 * 
	 * @return the agent class name
	 */
	public String getAgentClassName() {
		return this.agClass.getSimpleName();
	}

	/**
	 * Gets the agent id.
	 * 
	 * @return the agent id
	 */
	public Integer getAgentId() {
		return this.agId;
	}

	/**
	 * Gets the agent class.
	 * 
	 * @return the agent class
	 */
	public Class<?> getAgentClass() {
		return this.agClass;
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#hashCode()
	 */
	public int hashCode() {
		int hash;

		hash = super.hashCode();
		hash = 31 * hash + this.agClass.hashCode();
		return hash;
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#toString()
	 */
	public String toString() {
		if (this.agId == null)
			return this.descriptionName() + " (" + this.getAgentClassName() + ")";
		else
			return this.descriptionName() + " (" + this.getAgentClassName() + " : "+ this.getAgentId() + ")";
	}

}
