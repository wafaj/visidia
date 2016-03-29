package visidia.simulation.evtack;

import visidia.simulation.command.Command;

// TODO: Auto-generated Javadoc
/**
 * This class is a thread responsible for dealing with the events coming from the simulator.
 */
public class EventHandler extends Thread {

	/** The event queue. */
	private VQueue evtQueue;

	/** The ack queue. */
	private VQueue ackQueue;

	/** The stopped status. */
	private volatile boolean stopped = false;
	
	/**
	 * Instantiates a new event handler.
	 * 
	 * @param evtQueue the event queue
	 * @param ackQueue the ack queue
	 */
	public EventHandler(VQueue evtQueue, VQueue ackQueue) {
		this.evtQueue = evtQueue;
		this.ackQueue = ackQueue;
	}

	/**
	 * Aborts the handler.
	 */
	
	public void abort() {
		stopped = true;
		this.interrupt();
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (! stopped && ! Thread.currentThread().isInterrupted()) {
			try {
				VisidiaEvent event = null;
				try {
					event = (VisidiaEvent) this.evtQueue.get();
				} catch (ClassCastException e) {
					e.printStackTrace();
					continue;
				}

				Command cmd = event.getCommand();
				cmd.executeBeforeAck(event);

				if (cmd.generateImmediateAck()) {
					ackQueue.put(new VisidiaAck(event));
				}

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

		}
	}

}
