package visidia.simulation.process.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import visidia.misc.SynchronizedRandom;
import visidia.misc.property.PropertyTable;
import visidia.simulation.SimulationConstants;
import visidia.simulation.process.edgestate.EdgeState;
import visidia.simulation.process.edgestate.SyncState;
import visidia.simulation.process.messages.Door;
import visidia.simulation.process.messages.Message;
import visidia.simulation.process.messages.PropertyMessage;
import visidia.simulation.process.messages.MessageType;
import visidia.simulation.process.messages.SynchroMessage;

public abstract class LC0_Algorithm extends Algorithm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -628581240060486313L;

	public static MessageType synchroMType = SimulationConstants.Messages.SYNC;
	public static MessageType propertiesMType = SimulationConstants.Messages.PROP;

	protected transient PropertyTable neighborProperties;
	protected transient int neighborDoor = 0;

	private transient boolean isCenter = false;

	public LC0_Algorithm() {
		super();
	}

	@Override
	public abstract String getDescription();


	@Override
	public Collection<MessageType> getMessageTypeList() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(synchroMType);
		typesList.add(propertiesMType);
		return typesList;
	}

	public final void init(){

		activeDoors = new ArrayList<Integer>(getArity());
		for(int i = 0; i < getArity(); i++)
			activeDoors.add(i);

		if(isIsolated()) {
			localTermination(); 
		} else {
			beforeStart();

			while (working) {
				startSynchronization();
				if (neighborDoor >= 0) {
					if (isCenter)
						onStarCenter();
					endSynchronization();
				}
			}
		}
	}

	protected abstract void beforeStart();

	private final int chooseNeighbor() {

		if (isIsolated()) {
			localTermination();
			return -1;
		} else {
			int choice;
			do {
				choice = Math.abs(SynchronizedRandom.nextInt()) % this.getArity();
			} while(!activeDoors.contains(choice));

			return choice;
		}
	}

	protected final void startSynchronization() {

		neighborDoor = -1;
		neighborProperties = new PropertyTable();

		while (neighborDoor < 0 && working){
			neighborDoor = trySynchronization();
		}

		if (neighborDoor >= 0) {
			this.setDoorState(new SyncState(true), neighborDoor);

			if ( !isCenter) 
				super.sendTo(neighborDoor, new PropertyMessage(properties, propertiesMType));
			else {
				Message msg = super.receiveFrom(neighborDoor);
				if (msg.getType() == propertiesMType)
					neighborProperties = (PropertyTable) msg.getData();
				else if (msg.getType() == synchroMType)
					if ( ( ((SynchroMessage) msg).value() == SynchroMessage.GTERM) ) {
						activeDoors.remove(new Integer(neighborDoor));
						neighborDoor = -1;
					} else if ( ( ((SynchroMessage) msg).value() == SynchroMessage.GTERM) ) {
						neighborDoor = -1;
						globalTermination();
					}
			}
		}
	}

	private final int trySynchronization() {

		boolean synchroReply = false;
		Door door = new Door();

		// choosing a neighbor
		int choosenNeighbor = chooseNeighbor();

		if (choosenNeighbor != -1) { // if the vertex is not isolated

			// +1 because 0 corresponds to SynchroMessage.NO 
			int choosenNumber = 1 + Math.abs(SynchronizedRandom.nextInt()) % SynchroMessage.MAX;
			this.sendTo(choosenNeighbor, new SynchroMessage(choosenNumber, synchroMType));

			while (!synchroReply){

				Message msg = receive(door);

				if(door.getNum() != choosenNeighbor){
					if ( msg.getType() == synchroMType) {
						if ( ((SynchroMessage) msg).value() == SynchroMessage.TERM ) {
							activeDoors.remove(new Integer(door.getNum()));
						} else if ( ((SynchroMessage) msg).value() == SynchroMessage.GTERM ) {
							globalTermination();
						} else {
							this.sendTo(door.getNum(), new SynchroMessage(SynchroMessage.NO, synchroMType));
						}
					}
				} else {
					if ( msg.getType() == synchroMType) {
						synchroReply = true;
						int answer = ((SynchroMessage) msg).value();
						if ( answer > SynchroMessage.NO ) {	
							if (choosenNumber > answer) {
								isCenter = true;
								addSync_LC0_Step(choosenNeighbor);
								return choosenNeighbor;
							} else if (choosenNumber < answer) { // in case of equality the synchronization fails
								isCenter = false;
								return choosenNeighbor;
							} else 
								return -1;
						} else {
							if ( answer == SynchroMessage.TERM ) {
								activeDoors.remove((Integer) door.getNum());
							} else if ( answer == SynchroMessage.GTERM ) {
								globalTermination();
							} 

							return -1;
						}
					}
				}
			}
		}
		return -1;
	}

	protected abstract void onStarCenter();

	protected final void endSynchronization() {

		if ( isCenter) {
			super.sendTo(neighborDoor, new PropertyMessage(neighborProperties, propertiesMType));
		} else {
			boolean releaseSynchro = false;
			Door door = new Door();

			while (!releaseSynchro){
				Message msg = receive(door);
				if(door.getNum() != neighborDoor){
					if ( msg.getType() == synchroMType ) {
						if ( ((SynchroMessage) msg).value() == SynchroMessage.TERM ) {
							activeDoors.remove(new Integer(door.getNum()));
						} else if ( ((SynchroMessage) msg).value() == SynchroMessage.GTERM ) {
							globalTermination();
						} else {
							this.sendTo(door.getNum(), new SynchroMessage(SynchroMessage.NO, synchroMType));
						}
					}
				}
				else {
					if (msg.getType() == propertiesMType) { 
						//properties = (PropertyTable) msg.getData();
						setLocalProperty((PropertyTable) msg.getData());
						releaseSynchro = true;
					} else if ( msg.getType() == synchroMType ) {
						if ( ((SynchroMessage) msg).value() == SynchroMessage.TERM ) {
							activeDoors.remove(new Integer(door.getNum()));
							releaseSynchro = true;
						} else if ( ((SynchroMessage) msg).value() == SynchroMessage.GTERM ) {
							globalTermination();
							releaseSynchro = true;
						} 
					}
				}
			}

		}

		updateVertexPropoerties();
		this.setDoorState(new SyncState(false), neighborDoor);
		neighborDoor = -1;
		isCenter = false;
	}

	protected void updateVertexPropoerties() {

		if (getLocalProperty("label") != null) {
			String label = (String) getLocalProperty("label");
			if ( !vertex.getLabel().equals(label) ) {
				addPropertyStep(label);
				vertex.setLabel(label);
			} else {
				vertex.setLabel(label);
			}
		}
		
	}

	protected final void localTermination(){
		working = false;

		for (int door:activeDoors) {
			this.sendTo(door, new SynchroMessage(SynchroMessage.TERM, synchroMType));
		}
	}

	protected final void localTermination(MessageType messageType){
		working = false;

		for (int door:activeDoors) {
			this.sendTo(door, new SynchroMessage(SynchroMessage.TERM, messageType));
		}
	}

	protected final void globalTermination(){
		working = false;

		for (int door:activeDoors) {
			this.sendTo(door, new SynchroMessage(SynchroMessage.GTERM, synchroMType));
		}
	}

	protected final void globalTermination(MessageType messageType){
		working = false;

		for (int door:activeDoors) {
			this.sendTo(door, new SynchroMessage(SynchroMessage.GTERM, messageType));
		}
	}

	protected final Object getNeighborProperty(String key) {
		return this.neighborProperties.getValueOf(key);
	}
	
	protected final void setNeighborProperty(String key, Object value) {
			this.neighborProperties.setValue(key, value);
	}
	
	protected final void setNeighborProperty(String key, Object value, int tag) {
			this.neighborProperties.setValue(key, value, tag);
	}

	protected final Object getEdgeProperty (Object key) {
		return super.getEdgeProperty(neighborDoor, key);
	}

	protected final void setEdgeProperty (Object key, Object value) {
		super.setEdgeProperty(neighborDoor, key, value);
	}
	
	protected final void setEdgeProperty (Object key, Object value, boolean displayable) {
		super.setEdgeProperty(neighborDoor, key, value, displayable);
	}

	protected final void setDoorState(EdgeState st) {
		super.setDoorState(st, neighborDoor);
	}

}