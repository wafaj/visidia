package visidia.examples.rule;

import visidia.examples.algo.synchronization.LC1;
import visidia.rule.RelabelingSystem;
import visidia.simulation.process.algorithm.RuleAlgorithm;
import visidia.simulation.process.synchronization.SynCT;

public class LC1Rule extends RuleAlgorithm {

	private static final long serialVersionUID = 8524991950494958710L;

	public LC1Rule() {
		super();
		this.synType = SynCT.LC1;
		this.synal = new LC1();
	}

	public LC1Rule(RelabelingSystem r) {
		super(r);
		this.synType = SynCT.LC1;
		this.synal = new LC1();
	}

	public LC1Rule(RuleAlgorithm algo) {
		super(algo);
	}
	
	@Override
	public Object clone() {
		return new LC1Rule(this);
	}

	public String toString() {
		return ("RSAlgo: synal=" + this.synType + " opt="
				+ this.relSys.userPreferences.toString() + "\n RS=" + this.relSys.toString());
	}

	/* for LC1 */
	/*
	public void sendMyState() {
		for (int i = 0; i < this.synob.getCenters().size(); i++) {
			int door = ((Integer) this.synob.getCenters().elementAt(i)).intValue();
			if (this.synob.isConnected(door)) {
				this.sendTo(door, new StringMessage(((String) this.getProperty("label")), SimulationConstants.LABE));
			}
		}
	}
*/
	/* for LC1 */
	/*
	public void receiveAndUpdateMyState() {
		for (int i = 0; i < this.synob.getCenters().size(); i++) {
			int neighbour = ((Integer) this.synob.getCenters().elementAt(i)).intValue();
			if (this.synob.isConnected(neighbour)) {
				Message msg = this.receiveFrom(neighbour);
				if (msg != null) {
					this.setDoorState(new MarkedState(((BooleanMessage) msg).data()), neighbour);
					this.synob.setMark(neighbour, ((BooleanMessage) msg).data());
				}
			}
		}
	}
	*/

}
