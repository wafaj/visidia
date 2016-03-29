package visidia.simulation.process.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import visidia.misc.SynchronizedRandom;
import visidia.misc.property.PropertyTable;
import visidia.simulation.SimulationConstants;
import visidia.simulation.process.edgestate.EdgeState;
import visidia.simulation.process.edgestate.SyncState;
import visidia.simulation.process.messages.Message;
import visidia.simulation.process.messages.PropertyMessage;
import visidia.simulation.process.messages.MessageType;
import visidia.simulation.process.messages.SynchroMessage;


public abstract class LC2_Algorithm extends Algorithm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -628581240060486313L;

	public static MessageType synchroMType = SimulationConstants.Messages.SYNC;
	public static MessageType propertiesMType = SimulationConstants.Messages.PROP;
	public static MessageType terminationMType = SimulationConstants.Messages.TERM;


	protected transient PropertyTable[] neighborProperties;
	protected transient int starCenterDoor = 0;

	private final int starCenter=-1;
	private final int notInTheStar=-2;

	public LC2_Algorithm() {
		super();
	}

	@Override
	public abstract String getDescription();


	@Override
	public final Collection<MessageType> getMessageTypeList() {
		Collection<MessageType> typesList = new LinkedList<MessageType>();
		typesList.add(synchroMType);
		typesList.add(propertiesMType);
		typesList.add(terminationMType);
		return typesList;
	}


	public final void init(){

		neighborProperties = new PropertyTable[getArity()];

		activeDoors = new ArrayList<Integer>(getArity());
		for(int i = 0; i < getArity(); i++)
			activeDoors.add(i);	

		if(isIsolated()) {
			localTermination(); 
		} else {
			beforeStart();

			while(working){
				startSynchronization();
				if(starCenterDoor == starCenter)
					onStarCenter();			
				endSynchronization();
			}
		}
	}

	protected abstract void beforeStart();

	protected final void startSynchronization() {
		trySynchronization();
		if(starCenterDoor == starCenter){
			for(int door:activeDoors){
				neighborProperties[door] = (PropertyTable) super.receiveFrom(door).getData();
			}
		}
		else if(starCenterDoor != notInTheStar){
			super.sendTo(starCenterDoor, new PropertyMessage(properties,propertiesMType));
		}
	}

	private final void trySynchronization() {
		int answer;
		ArrayList<Integer> neighborsFinished = new ArrayList<Integer>();
		/*random */
		int choosenNumber = Math.abs(SynchronizedRandom.nextInt());

		/*Send to all neighbors */
		for (int door:activeDoors)
			sendTo(door,new SynchroMessage(new Integer(choosenNumber),synchroMType));

		/*receive all numbers from neighbors */
		int max = choosenNumber;
		for(int door:activeDoors){
			answer = ((SynchroMessage)receiveFrom(door)).value();
			if (answer > max)
				max = answer;
			if (answer == SynchroMessage.TERM)
				neighborsFinished.add(door);
			if (answer == SynchroMessage.GTERM){
				neighborsFinished.add(door);
				globalTermDetected = true;
			}
		}
		activeDoors.removeAll(neighborsFinished);
		neighborsFinished.clear();

		if (globalTermDetected)
			globalTermination();

		else {
			for (int door:activeDoors)
				sendTo(door,new SynchroMessage(new Integer(max),synchroMType));

			/*get all answers from neighbors */
			max = choosenNumber;
			for(int door:activeDoors){
				answer = ((SynchroMessage)receiveFrom(door)).value();
				if (answer > max)
					max = answer;
				if (answer == SynchroMessage.TERM)
					neighborsFinished.add(door);
				if (answer == SynchroMessage.GTERM){
					neighborsFinished.add(door);
					globalTermDetected = true;
				}
			}
			activeDoors.removeAll(neighborsFinished);
			neighborsFinished.clear();

			if (globalTermDetected)
				globalTermination();

			else if (choosenNumber >= max) {
				for(int door:activeDoors) {					
					sendTo(door, new SynchroMessage(SynchroMessage.OK, synchroMType));
					addSync_LC2_Step(door);
				}

				for (int door:activeDoors) {
					if (((SynchroMessage)super.receiveFrom(door)).value() == SynchroMessage.GTERM){  // TERM ??
						neighborsFinished.add(door);
						globalTermination();				
					} else {
						setDoorState(new SyncState(true),door);
					}
				}

				activeDoors.removeAll(neighborsFinished);
				if(!(activeDoors.isEmpty() && globalTermDetected))
					starCenterDoor =  starCenter;

			} else {
				starCenterDoor = notInTheStar;

				for (int door:activeDoors)
					sendTo(door,new SynchroMessage(SynchroMessage.NO,synchroMType));

				for (int door:activeDoors) {
					int value = ((SynchroMessage)super.receiveFrom(door)).value();
					if  (value == SynchroMessage.OK) 
						starCenterDoor = door;
					else if (value == SynchroMessage.TERM){
						neighborsFinished.add(door);
					} else if (value == SynchroMessage.GTERM){
						neighborsFinished.add(door);
						globalTermination();
					}
				}
				activeDoors.removeAll(neighborsFinished);
			}
		}
	}

	protected abstract void onStarCenter();

	protected final void endSynchronization() {

		if(starCenterDoor != starCenter && starCenterDoor != notInTheStar){

			Message msg = receiveFrom(starCenterDoor);

			if ( msg.getType() == synchroMType) {
				if ( ((SynchroMessage) msg).value() == SynchroMessage.TERM ) {
					activeDoors.remove(new Integer(starCenterDoor));
				} else if ( ((SynchroMessage) msg).value() == SynchroMessage.GTERM ) {
					activeDoors.remove(new Integer(starCenterDoor));
					globalTermination();
				} 
			} else if ( msg.getType() == propertiesMType) {
				//properties = (PropertyTable) msg.getData();
				setLocalProperty((PropertyTable) msg.getData());
				this.setDoorState(new SyncState(false), starCenterDoor);
			}

		} else if(starCenterDoor == starCenter)
			for(int door:activeDoors) {
				super.sendTo(door, new PropertyMessage((PropertyTable)neighborProperties[door].clone(), propertiesMType)); 
				setDoorState(new SyncState(false),door);
			}

		updateVertexPropoerties();

		starCenterDoor = notInTheStar;

		if(globalTermDetected){
			globalTermination(terminationMType);

		} else if(!working)
			localTermination(terminationMType);

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

	protected final Object getNeighborProperty(int door, String key) {
		if (!activeDoors.contains(door))
			return null;
		return this.neighborProperties[door].getValueOf(key);
	}

	protected final void setNeighborProperty(int door, String key, Object value) {
		if (activeDoors.contains(door))
			this.neighborProperties[door].setValue(key, value);
	}

	protected final void setNeighborProperty(int door, String key, Object value, int tag) {
		if (activeDoors.contains(door))
			this.neighborProperties[door].setValue(key, value, tag);
	}

	protected final void setAllNeighborsProperty(String key, Object value){
		for(int door:activeDoors)
			this.setNeighborProperty(door, key, value);
	}

	protected final void setAllNeighborsProperty(String key, Object value, int tag){
		for(int door:activeDoors)
			this.setNeighborProperty(door, key, value, tag);
	}

	protected final void setEdgeProperty (Object key, Object value) {
		for(int door:activeDoors)
			super.setEdgeProperty(door, key, value);
	}

	protected final void setAllEdgesProperty (Object key, Object value, boolean displayable) {
		for(int door:activeDoors)
			super.setEdgeProperty(door, key, value, displayable);
	}

	protected final void setDoorState(EdgeState st) {
		for(int door:activeDoors)
			super.setDoorState(st, door);
	}

	@SuppressWarnings("unchecked")
	protected final ArrayList<Integer> getActiveDoors(){
		return  (ArrayList<Integer>) activeDoors.clone();
	}

	protected final boolean isActiveDoor(int door){
		return activeDoors.contains(door);
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
}
