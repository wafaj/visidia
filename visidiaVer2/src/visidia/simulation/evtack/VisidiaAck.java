package visidia.simulation.evtack;

import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * This class corresponds to a simulation ack, associated to a simulation event.
 */
public class VisidiaAck implements Serializable {

	private static final long serialVersionUID = 6657378925709371986L;

	/** The event. */
	private VisidiaEvent event = null;

	/**
	 * Instantiates a new ack.
	 * 
	 * @param event the event
	 */
	public VisidiaAck(VisidiaEvent event) {
		this.event = event;
	}

	/**
	 * Gets the event.
	 * 
	 * @return the event
	 */
	public VisidiaEvent getEvent() {
		return event;
	}
}
