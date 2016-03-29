package visidia.examples.agent;

import java.util.Random;

import visidia.simulation.process.agent.Agent;

public class Cop extends Agent {

	private static final long serialVersionUID = 3091673913824155438L;

	static boolean thiefCaptured = false;

	@Override
	public Object clone() {
		return new Cop();
	}

	@Override
	protected void init() {
		thiefCaptured = false;
		Random randMove = new Random();

		while (!Cop.thiefCaptured) {
			int degree = this.getArity();
			int randomDirection = Math.abs(randMove.nextInt(degree));

			this.moveToDoor(randomDirection);

			this.lockVertexProperties();
			if (this.getVertexLabel().equals("S")) {
				Cop.thiefCaptured = true;
				this.setVertexLabel("C");
			}
			this.unlockVertexProperties();
		}
	}

}
