package visidia.examples.rule;

import visidia.examples.algo.synchronization.RDV;
import visidia.rule.RelabelingSystem;
import visidia.simulation.process.algorithm.RuleAlgorithm;
import visidia.simulation.process.synchronization.SynCT;

public class RDVRule extends RuleAlgorithm {

	private static final long serialVersionUID = 5900208921301417121L;

	public RDVRule() {
		super();
		this.synType = SynCT.RDV;
		this.synal = new RDV();
	}

	public RDVRule(RelabelingSystem r) {
		super(r);
		this.synType = SynCT.RDV;
		this.synal = new RDV();
	}

	public RDVRule(RuleAlgorithm algo) {
		super(algo);
	}
	
	@Override
	public Object clone() {
		return new RDVRule(this);
	}

	public String toString() {
		return "RDVRule" + super.toString();
	}

}
