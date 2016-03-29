package visidia.simulation.playrec;

import visidia.simulation.command.Command;
import visidia.simulation.evtack.Locker;
import visidia.simulation.evtack.VQueue;
import visidia.simulation.evtack.VisidiaAck;
import visidia.simulation.evtack.VisidiaEvent;

// TODO: Auto-generated Javadoc
/**
 * This class is a thread responsible for dealing with the acks coming from the simulator in replay mode.
 */
public class PlayerAckHandler extends Thread {

	/** The input ack queue. */
	private VQueue ackQueueIn;

	/** The output ack queue. */
	private VQueue ackQueueOut;

	/** The stopped status. */
	private volatile boolean stopped = false;

	/**
	 * Instantiates a new ack handler.
	 * 
	 * @param ackQueueIn the input ack queue
	 * @param ackQueueOut the output ack queue
	 */
	public PlayerAckHandler(VQueue ackQueueIn, VQueue ackQueueOut) {
		super();
		this.ackQueueIn = ackQueueIn;
		this.ackQueueOut = ackQueueOut;
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
					ack = (VisidiaAck) this.ackQueueIn.get();
				} catch (ClassCastException e) {
					e.printStackTrace();
					continue;
				}

				ackQueueOut.put(ack);

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
