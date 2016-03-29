package visidia.examples.algo;

import java.util.Collection;
import java.util.LinkedList;

import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.process.edgestate.MarkedState;
import visidia.simulation.process.messages.Door;
import visidia.simulation.process.messages.MessageType;
import visidia.simulation.process.messages.StringMessage;

public class Broadcast extends Algorithm {

	private static final long serialVersionUID = -4971761276474307858L;

	@Override
	public String getDescription(){
		return "This algorithm implements a broadcast from a specified vertex.\n" +
				"Set the label of the node you want to be the broadcast source to A.\n" +
				"The information will spread into the network.";
	}
	
	public static MessageType wave = new MessageType("Wave", true);

	public static MessageType ack = new MessageType("Acknowledgment", true,
			java.awt.Color.blue);

	@Override
	public Collection<MessageType> getMessageTypeList() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(Broadcast.ack);
		typesList.add(Broadcast.wave);
		return typesList;
	}

	@Override
	public void init() {
		int degres = this.getArity();
		int fatherDoor;

		String label = this.getProperty("label").toString();

		if (label.compareTo("A") == 0) {
			for (int i = 0; i < degres; i++) {
				this.sendTo(i, new StringMessage("Wave", Broadcast.wave));
			}
		} else {
			Door door = new Door();
			/* Message msg = */this.receive(door);

			fatherDoor = door.getNum();

			this.sendTo(fatherDoor, new StringMessage("Ack", Broadcast.ack));

			this.putProperty("label", new String("A"));
			this.setDoorState(new MarkedState(true), fatherDoor);

			for (int i = 0; i < degres; i++) {
				if (i != fatherDoor) {
					this.sendTo(i, new StringMessage("Wave", Broadcast.wave));
				}
			}
		}
	}

	@Override
	public Object clone() {
		return new Broadcast();
	}

}
