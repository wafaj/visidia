package visidia.simulation.playrec;

import visidia.simulation.evtack.VQueue;

// TODO: Auto-generated Javadoc
/**
 * This class is a thread responsible for recording events/acks coming from the simulator.
 */
public class RecordHandler<Type> extends Thread {

	/** The input queue. */
	private VQueue queueIn;

	/** The output queue. */
	private VQueue queueOut;

	/** The stopped status. */
	private volatile boolean stopped = false;

	/** The object writer. */
	private ObjectWriter writer;

	/**
	 * Instantiates a new event handler.
	 * 
	 * @param queueIn input queue
	 * @param queueOut output queue
	 * @param writer the object writer
	 */
	public RecordHandler(VQueue queueIn, VQueue queueOut, ObjectWriter writer) {
		this.queueIn = queueIn;
		this.queueOut = queueOut;
		this.writer = writer;
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
				Type obj = null;
				try {
					obj = (Type) this.queueIn.get();
					this.writer.writeObject(obj);
					this.queueOut.put(obj);
				} catch (ClassCastException e) {
					e.printStackTrace();
					continue;
				}

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
