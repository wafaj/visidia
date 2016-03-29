package visidia.simulation.process.synchronization;

import visidia.rule.Neighbor;
import visidia.rule.Star;

public class SynchronizationObjectTerminationRules extends SynchronizationObjectTermination {

	public Star neighborhood = new Star();

	/* Basic */

	public String toString() {
		return "SynObjectRules" + super.toString();
	}

	public void reset() {
		super.reset();
		this.neighborhood.removeAll();
	}

	public Object clone() {
		return new SynchronizationObjectTerminationRules();
	}

	/* Neighbourhood Accessors */
	public void setNeighborhood(Star n) {
		this.neighborhood = n;
	}

	public void setCenterState(String label) {
		this.neighborhood.setCenterState(label);
	}

	public void refresh() {
		for (int i = 0; i < this.synDoors.size(); i++) {
			int door = ((Integer) this.synDoors.get(i)).intValue();
			this.neighborhood.addNeighbor(new Neighbor(this.getMark(door), door));
		}

	}

}
