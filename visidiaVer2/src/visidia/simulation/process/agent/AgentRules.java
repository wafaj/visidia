package visidia.simulation.process.agent;

import visidia.rule.Neighbor;
import visidia.rule.RelabelingSystem;
import visidia.rule.Rule;
import visidia.rule.Star;
import visidia.simulation.SimulationAbortError;
import visidia.simulation.process.edgestate.MarkedState;

public class AgentRules extends SynchronousAgent {

	private static final long serialVersionUID = 2432403512843977836L;

	private RelabelingSystem rSys = null;

	public void setRule(RelabelingSystem rSys) {
		this.rSys = rSys;
	}

	protected RelabelingSystem getRelabelling() {
		return this.rSys;
	}

	@Override
	public Object clone() {
		return new AgentRules();
	}

	// private int v,u;
	private String labelV, labelU;

	private int door;

	private int step;

	@Override
	public void init() {
		this.step = 1;

		while (true) {

			if (this.lockVertexIfPossible()) {
				/* this.v = */this.getVertexIdentity();
				this.labelV = (String) this.getVertexProperty("label").toString();
				this.step = 1;

				this.randomMove();

				/* this.u = */this.getVertexIdentity();

				if (this.lockVertexIfPossible()) {
					this.labelU = (String) this.getVertexProperty("label").toString();
					this.door = this.entryDoor();

					//System.out.print("Handshake success");
					this.applyRule();
					this.unlockVertexProperties();
				}

				this.moveBack();
				this.unlockVertexProperties();
			} else {
				this.waitForWB();
			}
			this.nextPulse();
			this.randomWalk();
		}
	}

	public String toString() {
		String name = super.toString();
		if (this.step < 1 || this.step > 5) return name;
		
		String stepName = "";
		if (this.step == 1) stepName = "Construct star";
		else if (this.step == 2) stepName = "No rule";
		else if (this.step == 3) stepName = "Apply rule";
		else if (this.step == 4) stepName = "End application";
		else if (this.step == 5) stepName = "Search new center";
		
		return name + " (" + stepName + ")"; 
	}
	
	private void applyRule() {
		Star contextStar = this.contextStar();
		RelabelingSystem rSys = this.getRelabelling();
		int i = rSys.checkForRule(contextStar);

		if (i == -1) {
			this.step = 2;
		} else {
			this.step = 3;
			Rule rule = rSys.getRule(i);
			Star afterStar = rule.after();
			Neighbor neighbourV = afterStar.neighbor(0);
			this.setVertexProperty("label", neighbourV.state());
			this.moveBack();
			this.setVertexProperty("label", afterStar.centerState());
			this.moveBack();

			if (neighbourV.mark()) {
				//this.markDoor(this.door);
				this.setDoorState(new MarkedState(true), this.door);
			}

			this.step = 4;
		}
	}

	private Star contextStar() {
		Star star = new Star(this.labelV);
		Neighbor nebV = new Neighbor(this.labelU);
		star.addNeighbor(nebV);
		return star;
	}

	private void waitForWB() {
		while (this.vertexPropertiesLocked()) {
			try {
				synchronized (this) {
					this.wait(1000);
				}
			} catch (InterruptedException e) {
				throw new SimulationAbortError(e);
			}
		}
	}

	private void randomMove() {
		this.setAgentMover("RandomAgentMover");
		this.move();
	}

	private void randomWalk() {
		this.step = 5;
		this.setAgentMover("RandomWalk");
		this.move();
	}

}
