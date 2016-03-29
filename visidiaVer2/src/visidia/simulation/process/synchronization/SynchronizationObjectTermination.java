package visidia.simulation.process.synchronization;

/**
 * this class contains of implementation concerning Termination
 */
public class SynchronizationObjectTermination extends SynchronizationObject {

	/** ** Detection of Termination *** */
	protected boolean finishedNode[];

	protected boolean globalEnd;

	protected boolean localEnd;

	public SynchronizationObjectTermination() {
		super();
		this.globalEnd = false;
		this.localEnd = false;
	}

	public void init(int ar) {
		super.init(ar);
		this.finishedNode = new boolean[ar];
		for (int i = 0; i < ar; i++) {
			this.finishedNode[i] = false;
		}
	}

	public Object clone() {
		return new SynchronizationObjectTermination();
	}

	public String toString() {
		return super.toString() + "TERM";
	}

	public boolean hasFinished(int neighbour) {
		return this.finishedNode[neighbour];
	}

	public boolean allFinished() {
		return this.globalEnd;
	}

	public void setGlobEnd(boolean b) {
		this.globalEnd = b;
	}

	public void setFinished(int neighbour, boolean b) {
		this.finishedNode[neighbour] = b;
	}

}
