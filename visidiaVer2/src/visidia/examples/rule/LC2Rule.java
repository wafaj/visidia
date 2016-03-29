package visidia.examples.rule;

import visidia.examples.algo.synchronization.LC2;
import visidia.rule.RelabelingSystem;
import visidia.simulation.process.algorithm.RuleAlgorithm;
import visidia.simulation.process.synchronization.SynCT;

public class LC2Rule extends RuleAlgorithm {

	private static final long serialVersionUID = -1159436832573130836L;

	public LC2Rule() {
		super();
		this.synType = SynCT.LC2;
		this.synal = new LC2();
	}

	public LC2Rule(RelabelingSystem r) {
		super(r);
		this.synType = SynCT.LC2;
		this.synal = new LC2();
	}

	public LC2Rule(RuleAlgorithm algo) {
		super(algo);
	}
	
	@Override
	public Object clone() {
		return new LC2Rule(this);
	}

	public String toString() {
		return "LC2Rule" + super.toString();
	}

}
