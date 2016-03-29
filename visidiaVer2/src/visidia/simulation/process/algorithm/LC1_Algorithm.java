package visidia.simulation.process.algorithm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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

public abstract class LC1_Algorithm extends Algorithm {

	/**
	 * 
	 */
	private static final long serialVersionUID = -628581240060486313L;


	public static MessageType synchroMType = SimulationConstants.Messages.SYNC;
	public static MessageType propertiesMType = SimulationConstants.Messages.PROP;
	public static MessageType terminationMType = SimulationConstants.Messages.TERM;

	private transient PropertyTable[] neighborProperties;
	private transient ArrayList<Integer> centers;
	
	public LC1_Algorithm() {
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
		for(int i=0; i<getArity(); i++)
			activeDoors.add(i);	

		if(isIsolated()) {
			localTermination(); 
		} else {
			beforeStart();

			while(working) {
				startSynchronization();
				if(centers.size()!=0 && centers.get(0) == -1)
					onStarCenter();	
				endSynchronization();
				
				if(isIsolated()) 
					localTermination(); 
			}
		}

	}

	protected abstract void beforeStart();

	protected final void startSynchronization() {
		trySynchronization();
		if(centers.size()!=0 && centers.get(0) == -1){
			for(int door:activeDoors){
				Message msg = super.receiveFrom(door);
				if (msg.getType() == propertiesMType)
					neighborProperties[door] = (PropertyTable) msg.getData();
				else if (msg.getType() == synchroMType)
					if ( ( ((SynchroMessage) msg).value() == SynchroMessage.TERM) ) {
						activeDoors.remove(new Integer(door));
					} else if ( ( ((SynchroMessage) msg).value() == SynchroMessage.GTERM) ) {
						activeDoors.remove(new Integer(door));
						globalTermination();
						return;
					}
			}
		}
		else if(centers.size()!=0)
			for (Iterator<Integer> it = centers.iterator();it.hasNext();)
				super.sendTo(it.next(), new PropertyMessage(properties, propertiesMType));
	}

	private final void trySynchronization() {

		int answer;
		centers = new ArrayList<Integer>();
		ArrayList<Integer> neighborsFinished = new ArrayList<Integer>();

		/*random */
		int choosenNumber = Math.abs(SynchronizedRandom.nextInt());

		/*Send to all neighbors */
		for (int door:activeDoors)
			super.sendTo(door,new SynchroMessage(new Integer(choosenNumber),synchroMType));

		/*receive all numbers from neighbors */
		int max = choosenNumber;
		for(int door:activeDoors){
			answer = ((SynchroMessage)super.receiveFrom(door)).value();
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
			for (int door:activeDoors)	{				
				super.sendTo(door,new SynchroMessage(SynchroMessage.OK, synchroMType));
				addSync_LC1_Step(door);
			}

			for (int door:activeDoors) {
				if (((SynchroMessage)super.receiveFrom(door)).value() == SynchroMessage.GTERM){
					neighborsFinished.add(door);
					globalTermination();
				}else super.setDoorState(new SyncState(true),door);	
			}

			activeDoors.removeAll(neighborsFinished);
			if(!activeDoors.isEmpty())
				centers.add(new Integer(-1));
		}
		else {			
			for (int door:activeDoors) 
				super.sendTo(door,new SynchroMessage(SynchroMessage.NO, synchroMType));
			int value = 0;
			for (int door:activeDoors) {
				value = ((SynchroMessage)super.receiveFrom(door)).value();
				if  (value == SynchroMessage.OK) 
					centers.add(new Integer(door));
				else if (value == SynchroMessage.GTERM)
					globalTermination();
			}

		}
	}

	protected abstract void onStarCenter();

	protected final void endSynchronization() {
		if(centers.size()!= 0 && centers.get(0) == -1) {
			for(int door:activeDoors)
				super.setDoorState(new SyncState(false),door);

			updateVertexPropoerties();
		}

		if(globalTermDetected)
			globalTermination(terminationMType);

		else if(!working)
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

	protected final void setEdgeProperty(Object key, Object value){
		for (int door:activeDoors)
			super.setEdgeProperty(door, key, value);
	}

	protected final void setDoorState(EdgeState st){
		for(int door:activeDoors)
			super.setDoorState(st, door);
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

	@SuppressWarnings("unchecked")
	protected final ArrayList<Integer> getActiveDoors(){
		return  (ArrayList<Integer>) activeDoors.clone();
	}

	protected final boolean isActiveDoor(int door){
		return activeDoors.contains(door);
	}

}

