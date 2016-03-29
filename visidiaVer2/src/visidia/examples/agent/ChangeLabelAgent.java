package visidia.examples.agent;

import visidia.simulation.process.agent.Agent;

public class ChangeLabelAgent extends Agent {

	private static final long serialVersionUID = 4029197037906754626L;

	@Override
	public Object clone() {
		return new ChangeLabelAgent();
	}

	@Override
	protected void init() {
		String label = new String("B");

		this.setAgentMover("RandomAgentMover");

		do {
			this.setVertexLabel(label);
			this.move();
		} while (true);
	}

}
