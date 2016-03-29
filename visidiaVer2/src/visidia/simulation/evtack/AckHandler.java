 package visidia.simulation.evtack;

import visidia.simulation.command.Command;

// TODO: Auto-generated Javadoc
/**
 * This class is a thread responsible for dealing with the acks coming from the simulator.
 */
public class AckHandler extends Thread {

	/** The ack queue. */
	private VQueue ackQueue;

	/** The stopped status. */
	private volatile boolean stopped = false;

	/**
	 * Instantiates a new ack handler.
	 * 
	 * @param ackQueue the ack queue
	 */
	public AckHandler(VQueue ackQueue) {
		super();
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
				VisidiaAck ack = null;
				try {
					ack = (VisidiaAck) this.ackQueue.get();
				} catch (ClassCastException e) {
					e.printStackTrace();
					continue;
				}

				VisidiaEvent event = ack.getEvent();
				Command cmd = event.getCommand();
				if (cmd.needSynchronization()) {
					Object lock = Locker.getInstance().removeLock(event.getLockId());
					synchronized (lock) {
						cmd.executeAfterAck();
						lock.notifyAll();
					}
				} else {
					cmd.executeAfterAck();
				}
				
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}

		}
	}

}
