package visidia.stats;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageStat.
 */
public abstract class MessageStat extends VisidiaStat {

	/** The simulation id. */
	private Integer simulationId;

	/**
	 * Instantiates a new message stat.
	 */
	public MessageStat() {
	}

	/**
	 * Instantiates a new message stat.
	 * 
	 * @param simulationId the simulation id
	 */
	public MessageStat(Integer simulationId) {
		this.simulationId = simulationId;
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if (super.equals(o) == false) {
			return false;
		}

		MessageStat o2 = (MessageStat) o;
		return this.simulationId == o2.simulationId;
	}

	/**
	 * Gets the simulation id.
	 * 
	 * @return the simulation id
	 */
	public Integer getSimulationId() {
		return simulationId;
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#hashCode()
	 */
	public int hashCode() {
		return 79 * super.hashCode();
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#toString()
	 */
	public String toString() {
		if (this.simulationId == null) return this.descriptionName();
		else return this.descriptionName() + " (simulation : "+ this.simulationId + ")";
	}

}
