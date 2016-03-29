package visidia.examples.agent;

import java.util.Random;

import visidia.simulation.process.agent.Agent;

public class Thief extends Agent {

	private static final long serialVersionUID = 3322264148029664413L;

	@Override
	public Object clone() {
		return new Thief();
	}

	@Override
	protected void init() {
		Random randHide = new Random();
		Random randMove = new Random();

		boolean captured = false;

		while (!captured) {

			while(this.getArity() == 0)
				try {
					Thread.sleep(100);
				} 
			catch (InterruptedException e) {}

			int degree = this.getArity();
			int randomDirection = Math.abs(randMove.nextInt(degree));
			this.moveToDoor(randomDirection);

			String oldLabel = this.getVertexLabel();

			this.setVertexLabel("H");
			try {
				Thread.sleep((Math.abs(randHide.nextInt(5)) + 1) * 500);
			} catch (Exception e) {
			}

			if (oldLabel.equals("N")) {
				this.setVertexLabel("S");
				try {
					Thread.sleep(3000);
				} catch (Exception e) {
				}

				this.lockVertexProperties();
				if (this.getVertexLabel().equals("C")) {
					captured = true;
				} else {
					this.setVertexLabel("D");
				}
				this.unlockVertexProperties();
			} else {
				this.setVertexLabel("D");
			}
		}
	}

}
