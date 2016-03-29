package visidia.examples.algo.synchronous;

import java.util.Collection;
import java.util.LinkedList;

import visidia.simulation.process.algorithm.SynchronousAlgorithm;
import visidia.simulation.process.edgestate.MarkedState;
import visidia.simulation.process.messages.Door;
import visidia.simulation.process.messages.MessageType;
import visidia.simulation.process.messages.StringMessage;

public class Flooding_BFS extends SynchronousAlgorithm {

	private static final long serialVersionUID = -1567955970781886058L;

	static MessageType wave = new MessageType("wave", true, new java.awt.Color(200, 0, 0));

	public Collection getListTypes() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(Flooding_BFS.wave);

		return typesList;
	}

	@Override
	public void init() {
		boolean run = true;

		Integer id = this.getId();

		this.nextPulse();

		while (run) {
			if (id == 1) {
				this.putProperty("label", new String("R"));
				this.sendAll(new StringMessage("WAVE", Flooding_BFS.wave));
				run = false;
				this.nextPulse();
			} else {
				if (this.anyMsg()) {
					Door door = new Door();
					/* StringMessage msg = (StringMessage) */this.receive(door);
					setDoorState(new MarkedState(true), door.getNum());
					this.putProperty("label", new String("L"));
					
					this.sendAll(new StringMessage("WAVE", Flooding_BFS.wave));
					run = false;  // Broadcast the message on
				}
				this.nextPulse();
			}
		}
	}
	
	@Override
	public Object clone() {
		return new Flooding_BFS();
	}

}
