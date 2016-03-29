package visidia.examples.algo.synchronization;

import visidia.misc.SynchronizedRandom;
import visidia.simulation.SimulationConstants;
import visidia.simulation.process.edgestate.SyncState;
import visidia.simulation.process.messages.IntegerMessage;
import visidia.simulation.process.synchronization.SynCT;
import visidia.simulation.process.synchronization.SynchronizationAlgorithm;

public class LC1 extends SynchronizationAlgorithm {

	private static final long serialVersionUID = -5396266522423907571L;

	public LC1() {
		super();
	}

	public LC1(SynchronizationAlgorithm algo) {
		super(algo);
	}
	
	@Override
	public Object clone() {
		return new LC1(this);
	}

	public String toString() {
		return "LC1";
	}

	public void trySynchronize() {
		int arity = this.getArity();
		int[] answer = new int[arity];

		// waitWhileDisconnected();
		this.synob.reset();
		/* random */
		int choosenNumber = Math.abs(SynchronizedRandom.nextInt());

		/* Send to all neighbors */
		for (int i = 0; i < arity; i++) {
			if (!this.synob.hasFinished(i)) {
				boolean b = this.sendTo(i, new IntegerMessage(choosenNumber, SimulationConstants.Messages.SYNC));
				this.synob.setConnected(i, b);
			}
		}
		/* receive all numbers from neighbors */
		for (int i = 0; i < arity; i++) {
			if (!this.synob.hasFinished(i) && this.synob.isConnected(i)) {
				IntegerMessage msg = (IntegerMessage) this.receiveFrom(i);
				if (msg != null) {
					answer[i] = msg.value();
					if (msg.getType().equals(SimulationConstants.Messages.TERM)) {
						if (answer[i] == SynCT.LOCAL_END) {
							this.synob.setFinished(i, true);
						}
						if (answer[i] == SynCT.GLOBAL_END) {
							this.synob.setGlobEnd(true);
							this.synob.setFinished(i, true);
						}
					}
				} else {
					this.synob.setConnected(i, false);
				}
			}
		}

		// derniere chance
		for (int i = 0; i < arity; i++) {
			if (!this.synob.hasFinished(i) && !this.synob.isConnected(i)) {
				boolean b = this.sendTo(i, new IntegerMessage(choosenNumber, SimulationConstants.Messages.SYNC));
				this.synob.setConnected(i, b);
				if (b) {
					IntegerMessage msg = (IntegerMessage) this.receiveFrom(i);
					if (msg != null) {
						answer[i] = msg.value();
						if (msg.getType().equals(SimulationConstants.Messages.TERM)) {
							if (answer[i] == SynCT.LOCAL_END) {
								this.synob.setFinished(i, true);
							}
							if (answer[i] == SynCT.GLOBAL_END) {
								this.synob.setGlobEnd(true);
								this.synob.setFinished(i, true);
							}
						}
					} else {
						this.synob.setConnected(i, false);
					}
				}
			}
		}

		/* get the max */
		int max = choosenNumber;
		for (int i = 0; i < arity; i++) {
			if (!this.synob.hasFinished(i) && this.synob.isConnected(i)) {
				if (answer[i] >= max) {
					max = answer[i];
				}
			}
		}

		/* if elected */
		if (choosenNumber >= max) {
			for (int door = 0; door < arity; door++) {
				if (!this.synob.hasFinished(door)) {
					this.setDoorState(new SyncState(true), door);
					this.synob.addSynchronizedDoor(door);
				}
			}
			for (int i = 0; i < arity; i++) {
				if (!this.synob.hasFinished(i) && this.synob.isConnected(i)) {
					boolean b = this.sendTo(i, new IntegerMessage(1, SimulationConstants.Messages.SYNC));
					this.synob.setConnected(i, b);
				}
			}

			for (int i = 0; i < arity; i++) {
				if (!this.synob.hasFinished(i)) {
					this.receiveFrom(i);
				}
			}
			this.synob.setState(SynCT.IAM_THE_CENTER);
			return;
		}
		// not elected
		else {
			this.synob.setState(SynCT.NOT_IN_THE_STAR);
			for (int i = 0; i < arity; i++) {
				if (!this.synob.hasFinished(i) && this.synob.isConnected(i)) {
					boolean b = this.sendTo(i, new IntegerMessage(0, SimulationConstants.Messages.SYNC));
					this.synob.setConnected(i, b);
				}
			}
			for (int i = 0; i < arity; i++) {
				if (!this.synob.hasFinished(i) && this.synob.isConnected(i)) {
					IntegerMessage msg = (IntegerMessage) this.receiveFrom(i);
					if (msg == null) {
						this.synob.setConnected(i, false);
					} else {
						int value = msg.value();
						if (value == 1) {
							this.synob.addCenter(i);
							this.synob.setState(SynCT.IN_THE_STAR);
						}
					}
				}
			}
			return;
		}
	}

	public void reconnectionEvent(int door) {
		// Message m;
		while ((/* m = */this.receiveFrom(door)) != null) {
		}
	}

}