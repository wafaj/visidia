package visidia.simulation.playrec;

import java.util.Iterator;
import java.util.Vector;

import visidia.simulation.evtack.VQueue;
import visidia.simulation.evtack.VisidiaAck;
import visidia.simulation.evtack.VisidiaEvent;

// TODO: Auto-generated Javadoc
/**
 * The simulation player.
 */
public class SimulationPlayer extends Thread {

	/** The output ack queue. */
	private VQueue ackOut = null;

	/** The replay info. */
	private ReplayInfo replayInfo;

	/** The stopped status. */
	private volatile boolean stopped = false;

	/**
	 * Instantiates a new simulation player.
	 * 
	 * @param ackOut the output ack queue
	 * @param replayInfo the replay info
	 */
	public SimulationPlayer(VQueue ackOut, ReplayInfo replayInfo) {
		this.ackOut = ackOut;
		this.replayInfo = replayInfo;
	}

	/**
	 * Abort.
	 */
	public void abort() {
		stopped = true;
		interrupt();
	}

	/**
	 * Checks if acks match.
	 * 
	 * @param ack1 the ack1
	 * @param ack2 the ack2
	 * 
	 * @return true, if successful
	 */
	private boolean acksMatch(VisidiaAck ack1, VisidiaAck ack2) {
		return ack1.getEvent().getCommand().equals(ack2.getEvent().getCommand());
	}

	/**
	 * Gets the element position in vector matching the given ack.
	 * 
	 * @param ack the ack
	 * @param v the vector
	 * 
	 * @return the matching element position if it exists; otherwise -1
	 */
	private int getAckMatching(VisidiaAck ack, Vector<VisidiaAck> v) {
		Iterator<VisidiaAck> itr = v.iterator();
		int pos = 0;
		while (itr.hasNext()) {
			VisidiaAck a = itr.next();
			if (acksMatch(a, ack)) return pos;
			pos ++;
		}
		return -1;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		Vector<VisidiaAck> ackRcv = new Vector<VisidiaAck>();

		while (! stopped && ! Thread.currentThread().isInterrupted()) {
			try {
				Object incomingMessage = replayInfo.nextMessage();

				if (incomingMessage != null) {
					if (incomingMessage instanceof VisidiaEvent) {
						// generate the event					
						VisidiaEvent evt = (VisidiaEvent) incomingMessage;
						evt.getConsole().runCommand(evt.getCommand());
					} else if (incomingMessage instanceof VisidiaAck) {
						// wait for the console to generate the ack
						VisidiaAck readAck = (VisidiaAck) incomingMessage;
						int position = -1;
						while ((position = getAckMatching(readAck, ackRcv)) == -1) {
							VisidiaAck generatedAck = (VisidiaAck) ackOut.get();
							ackRcv.add(generatedAck);
						}

						ackRcv.remove(position);
					}
				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
			}
		}
	}

}
