package visidia.examples.algo.sensor;

import java.util.Vector;

import visidia.misc.SynchronizedRandom;
import visidia.simulation.process.algorithm.SensorSyncAlgorithm;
import visidia.simulation.process.edgestate.MarkedState;
import visidia.simulation.process.messages.IntegerMessage;
import visidia.simulation.process.messages.MessageType;
import visidia.simulation.process.messages.VectorMessage;

public class Angluin extends SensorSyncAlgorithm {

	private static final long serialVersionUID = 1053660058810365303L;

	transient private static int compteur_rdv;

	private int vie;
	private int T;

	//The message related to rendez-vous part of algorithm is set as not visible
	transient static MessageType rendez_vous = new MessageType("rdv",false);    

	//The message in which sensors share their life and temperature data
	transient static MessageType infos = new MessageType("infos",true);

	//This message is used to choose which of the two sensors is going to stay alive
	//It is also set as not visible
	transient static MessageType holder_choice = new MessageType("choice", false); 

	@Override
	public void init() {
		String myColor = new String("N");
		int n = getNetSize();
		//int n = this.proc.getServer().getConsole().getGraph().order();

		compteur_rdv = 0;
		this.vie = 1; //Sensors are alive at the beginning of the algorithm              
		this.T = Math.abs(SynchronizedRandom.nextInt()) % 2; //Their temperature is randomly set as 0 or 1

		do {
			myColor = this.getNewColor(this.vie);            
			this.putProperty("label", myColor);              
			this.putProperty("vie", this.vie);
			this.putProperty("temperature", this.T);
			this.move(this.getId());

			//rendez-vous algorithm
			if (getArity() > 0) {
				int rendezVousNeighbour = this.chooseNeigbour();
				int neighbourCount = getArity();

				// send 1 to rendezVousNeighbour
				this.sendTo(rendezVousNeighbour, new IntegerMessage(1,Angluin.rendez_vous));
				// and send 0 to others
				for (int i = 0; i < neighbourCount; i++) {
					if (i != rendezVousNeighbour) {
						this.sendTo(i, new IntegerMessage(0,Angluin.rendez_vous));
					}
				}
				boolean rendezVousIsAccepted = false;
				// receive all neighbour rendez-vous message
				// and check whether the selected neigbour accept
				// the rendez-vous.
				for (int i = 0; i < neighbourCount; i++) {
					IntegerMessage m = (IntegerMessage) this.receiveFrom(i);
					if ((i == rendezVousNeighbour) && (m.value() == 1)) {
						rendezVousIsAccepted = true;
						Angluin.compteur_rdv ++;
						this.setDoorState(new MarkedState(true), rendezVousNeighbour);
					}
				}

				/*IntegerMessage msg = (IntegerMessage) this.receiveFrom(rendezVousNeighbour);
				if ( msg.value() == 1 ) {
					rendezVousIsAccepted = true;
					Angluin.compteur_rdv ++;
					this.setDoorState(new MarkedState(true), rendezVousNeighbour);
				}*/

				//if rendez-vous is accepted
				if (rendezVousIsAccepted) {
					Vector<Integer> vec = new Vector<Integer>(2);
					vec.add(new Integer(T));
					vec.add(new Integer(vie));
					sendTo(rendezVousNeighbour, new VectorMessage((Vector) vec.clone(),	Angluin.infos));	

					VectorMessage vm = (VectorMessage) this.receiveFrom(rendezVousNeighbour);
					Vector data = vm.data();

					int T2 =((Integer) data.elementAt(0)).intValue();
					int vie2 =((Integer) data.elementAt(1)).intValue();

					if ((this.vie == 0) && (T2 > this.T)) {
						this.T = T2;
						Angluin.compteur_rdv = 0;
					}
					else if ((this.vie + vie2) == 2) {
						//We choose one holder of the information
						IntegerMessage m;
						int c;			
						do {
							c = Math.abs((SynchronizedRandom.nextInt()));
							sendTo(rendezVousNeighbour,new IntegerMessage(c, Angluin.holder_choice));
							m = (IntegerMessage) this.receiveFrom(rendezVousNeighbour);
						} while (m.value() == c);

						this.T += T2;	
						if (T2 != 0) compteur_rdv = 0; 			
						if (c > m.value()) this.vie = 0; 
					}
					this.setDoorState(new MarkedState(false), rendezVousNeighbour);
				}
			} 
		} while (compteur_rdv < (n*n));
	}

	private int chooseNeigbour() {
		return Math.abs((SynchronizedRandom.nextInt())) % this.getArity();
	}

	@Override
	public Object clone() {
		return new Angluin();
	}

	private String getNewColor(int colors) {
		if (colors == 0) {
			return new String("S");
		} else if (colors == 1) {
			return new String("N");
		} else {
			return new String("Z");
		}
	}

}
