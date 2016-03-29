package visidia.examples.algo;

import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.process.algorithm.SynchronousAlgorithm;
import visidia.simulation.process.edgestate.MarkedState;
import visidia.simulation.process.messages.IntegerMessage;
import visidia.simulation.process.messages.StringMessage;
import visidia.simulation.process.messages.VectorMessage;
import visidia.simulation.process.messages.Message;
import visidia.simulation.process.messages.Door;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.awt.Color;
import java.util.Random;
import java.awt.Point;
import java.util.Arrays;


public class RED extends Routing{
	private WitnessCache cache;
	private Vector<SensorMessage> claims;
	private boolean isMalacious;
	public static final int NoWitnessPoints = 1;
	private static Boolean receiving = true;
	private static Vector<Point> WitnessPoints = new Vector<Point>();
	private static Boolean cloneDetected = new Boolean(false);

	//Set to true so that comporomised and cloned nodes drop locaiotn claims
	private static Boolean dropLocationClaims = new Boolean(false);
	//Set to a value between 0 and 1. If set to x => probability of transmission = 1-x
	private static double THRESHOLD = 0.90;

	@Override
	public Object clone(){
		return new RED();
	}

	private void fixWitnessPoints(){
		synchronized(WitnessPoints){
			if(this.WitnessPoints.size() < this.NoWitnessPoints){
				this.WitnessPoints.addElement(this.getRandPoint());
			}
			//System.out.println(this.WitnessPoints.size());
		}
	}

	private void clearWitnessPoints(){
		synchronized(WitnessPoints){
			if(this.WitnessPoints.size() != 0){
				this.WitnessPoints = new Vector<Point>();
				iterationNumber += 1;
			}
		}
	}

	private void broadcastLoc(){
		String label = this.getProperty("label").toString();
		// Dest Point(-1,-1) is used for broadcast
		if(!label.equals(new String("N")) && !label.equals(new String("L")) && !label.equals(new String("M")) ){
			System.out.println(this.getId()+" _broadcastLoc");
			this.sendAll(new SensorMessage(label,new Point(-1,-1),this.getPosition()));
		}
	}
	
	private void receiveClaims(){
		Door d = new Door();
		while(this.anyMsg()){
			this.putProperty("label", "A");
			SensorMessage msg = (SensorMessage)this.receive(d);	
			System.out.println(this.getId()+" Receive "+msg);
			msg.setLastPos(this.vertex.getNeighborByDoor(d.getNum()).getPos());
			System.out.println("mise à jour de LastPos "+this.vertex.getNeighborByDoor(d.getNum()).getPos()+"avec le numero du port " +d.getNum());

			claims.addElement(msg);
			cache.addClaim(msg.getLabel(),msg.getClaim());
		}
	}

	private void receiveClaims(boolean store){
		Door d = new Door();
		while(this.anyMsg()){
			this.putProperty("label", "T");
			SensorMessage msg = (SensorMessage)this.receive(d);	
			System.out.println(this.getId()+" Receive "+msg);
			msg.setLastPos(this.vertex.getNeighborByDoor(d.getNum()).getPos());
			System.out.println("mise à jour de LastPos "+this.vertex.getNeighborByDoor(d.getNum()).getPos()+"avec le numero du port " +d.getNum());

			claims.addElement(msg);
			if(store){
				cache.addClaim(msg.getLabel(),msg.getClaim());
				}
		}
	}
	
	private boolean shouldISend(){
		double val = this.rand.nextDouble();
		if( val > this.THRESHOLD ){
			return true;
		}else{
			return false;
		}
	}

	private void transmitClaims(){

		
		if(this.isMalacious && this.dropLocationClaims){
			System.out.println(this.getId()+" _transmitClaims Fail");
			return;
		}
		synchronized(WitnessPoints){
			for(SensorMessage msg : this.claims){
				if(this.shouldISend()){
					System.out.println(this.getId()+" _transmitClaims ");
					for(Point dest: this.WitnessPoints){
						msg.setDest(dest);
						//System.out.println(dest);
						this.forwardMessage(dest,msg);
					}
				}
			}
		}
	}

	private void processClaims(){
		Door d = new Door();
		synchronized(receiving){
			for(SensorMessage msg: claims){
				if(this.isMalacious && this.dropLocationClaims) continue;
				//Differs here for RED and LSM -- LSM = > We add all messages to cache.
				if(!this.forwardMessage(msg.getDest(),msg)){
					//Wasn't able to forward message => this was the destination
					cache.addClaim(msg.getLabel(),msg.getClaim());
				}else{
					// Still some node needs to receive this message
					receiving |= true;
				}
			}
		}
	}

	@Override
	public void init(){
		// Step -1
		this.cache = new WitnessCache();
		this.claims = new Vector<SensorMessage>();
		this.receiving = true;
		this.cloneDetected = false;

		this.setUpRouting();this.nextPulse();


		this.setUpRouting();
		this.clearWitnessPoints();
		//--Uncomment to randomly generate compromised and cloned nodes
		this.compromise(5); //compromise(x) x-> percentage of compromised nodes and cloning nodes !!!!
		this.nextPulse();

		String label = this.getProperty("label").toString();
		if(label.equals(new String("N")) || label.equals(new String("L")) ){
			this.isMalacious = false;
		}else{
			this.isMalacious = true;
		}
		this.nextPulse();

		// Step 0
		this.fixWitnessPoints();
		this.nextPulse();
		//System.out.println(this.WitnessPoints.elementAt(0));
		//Step 1
		this.broadcastLoc();
		this.nextPulse();
		//Step 1.5
		this.receiveClaims();
		this.nextPulse();

		//Step 2,2.5
		this.transmitClaims();
		this.nextPulse();

		//Step 2.5,3
		while(receiving){
			claims.clear();
			this.receiveClaims(false);
			this.nextPulse();
			receiving = false;
			this.nextPulse();
			this.processClaims();
			this.nextPulse();
		}

		//Step 4
		if(this.cache.cloneDetected() && !isMalacious){
			this.putProperty("label", new String("L"));
			synchronized(cloneDetected){
				if(!cloneDetected){
					System.out.println(String.valueOf(iterationNumber)+" "+"detected");
					cloneDetected = true;
				}
			}
		}
		//--Uncomment to randomly generate compromised and cloned nodes
		this.unCompromise();
		this.nextPulse();

	}
}
/*
Step 0 - Fix some set of locations -- so this using static member type
Step 1 - Broadcast location
Step 1.5 - Recive the location claims and store them
Step 2 - Send location claims to witnesses with some probability -- Forwarding might take multiple pulses
Step 2.5 - Malacious nodes drop location claims.. -- Can be made smarter.. For the time being drop all
Step 3 - Receive the location claims -- Synchornize using a static variable(Could have been done by time..)
Step 4 - Change node label in case of clone detection
*/
