package visidia.stats;

// TODO: Auto-generated Javadoc
/**
 * The Class MessageNbSentStat.
 */
public class MessageNbSentStat extends MessageStat {

	/**
	 * Instantiates a new stat for the number of sent messages.
	 */
	public MessageNbSentStat() {
		super();
	}

	/**
	 * Instantiates a new stat for the number of sent messages.
	 * 
	 * @param simulationId the simulation id
	 */
	public MessageNbSentStat(Integer simulationId) {
		super(simulationId);
	}

	/* (non-Javadoc)
	 * @see visidia.stats.VisidiaStat#descriptionName()
	 */
	@Override
	protected String descriptionName() {
		return "Sent messages";
	}

}
