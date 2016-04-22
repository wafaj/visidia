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
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;


public class SDC_pts extends Routing{
	//private WitnessCache cache;
	//private Vector<SensorMessage> claims;
	private boolean isMalacious;
	public static final int NoWitnessPoints = 1;
	private static Boolean receiving = true;
	private static Vector<Point> WitnessPoints = new Vector<Point>();
	private Vector<Point> cellNeighborLocs;
	private static Boolean cloneDetected = new Boolean(false);
	private Set<Integer> receivedClaimIds;
	private Integer cellId;

	//Set to true so that comporomised and cloned nodes drop locaiotn claims
	private static Boolean dropLocationClaims = new Boolean(false);
	//Set to a value between 0 and 1. If set to x => probability of transmission = 1-x
	private static double THRESHOLD = 0.90;
	//Number of witness rows
	private static Integer witnessRows = 9;
	//Number of witness colums
	private static Integer witnessCols = 9;
	//Probabilty of Storage
	private static double PS = 0.50;
	//Node ID of clone A
	private static Integer cloneA = new Integer(0);
	//Starting node id for clone A'
	private static Integer startId = new Integer(1);

	@Override
	public Object clone(){
		return new SDC_pts();
	}


	private boolean sameCell(Point p){
		int ofx = (int)((double)p.getX()/((this.xMax+1)/witnessRows));
		int ofy = (int)((double)p.getY()/((this.yMax+1)/witnessCols));
		int offset = ofx+witnessCols*ofy;
		if(offset == this.cellId) return true;
		else return false;
	}
	private void makeCells(){
		int ofx = (int)(this.xPos/((this.xMax+1)/witnessRows));
		int ofy = (int)(this.yPos/((this.yMax+1)/witnessCols));
		this.cellId = ofx + witnessCols*ofy;
		cellNeighborLocs = new Vector<Point>();
		for(Point p: this.neighborLocs){
			if(sameCell(p)){
				this.cellNeighborLocs.addElement(p);
			}
		}
		/*char l = (char)(this.cellId+'A');
		if(!this.getProperty("label").toString().equals("P")){
			this.putProperty("label", Character.toString(l));
		}*/
		
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
		//if(!label.equals(new String("N")) && !label.equals(new String("L")) && !label.equals(new String("M")) ){
		if(label.equals(new String("P"))){
			synchronized (levelTrace) {
				this.levelTrace.incrementNbMessage(this.getArity());
			}
			this.sendAll(new SensorMessage(label,new Point(-1,-1),this.getPosition()));
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

	private boolean floodProbability(){
		double val = this.rand.nextDouble();
		if( val <= this.PS ){
			return true;
		}else{
			return false;
		}
	}
	private void transmitClaims(){
		if(this.isMalacious && this.dropLocationClaims) return;
		synchronized(WitnessPoints){
			for(SensorMessage msg : this.claims){
				if(this.shouldISend()){
					for(Point dest: this.WitnessPoints){
						msg.setDest(dest);
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
				if(this.receivedClaimIds.contains(msg.getUID())) continue;
				else this.receivedClaimIds.add(msg.getUID());
				if(this.sameCell(msg.getDest())){
					if(this.floodProbability()){
						for(Point p: this.cellNeighborLocs){
							SensorMessage tmp = new SensorMessage(msg);
							tmp.setDest(p);
							this.forwardMessage(p,tmp);
							receiving |= true;
						}
					}else{
						cache.addClaim(msg.getLabel(),msg.getClaim());
					}
				}else if(!this.forwardMessage(msg.getDest(),msg)){
					//Cannot reach destination cell..Do Nothing
				}else{
					receiving |= true;
				}
			}
		}
	}

	@Override
	public void init(){
                // the 4 following lines for the version of SDC with points evluation
		/*
		 * if(this.iterationNumber/TOTAL_ITERATIONS_BY_CONFIGURATION + startId == this.getId() || this.getId() == cloneA){
		 /*
			if(this.iterationNumber/TOTAL_ITERATIONS_BY_CONFIGURATION + startId == this.getId() ){
				posA=this.vertex.getPos();
			}
			if(this.getId() == cloneA){	
				posCloneA=new Point(this.vertex.getPos());
			}
			this.putProperty("label", new String("P"));
			//System.out.println(this.getId()+" "+this.vertex.getPos().getX()+" "+this.vertex.getPos().getY());
		}else{
			this.putProperty("label", new String("N"));
		}
		*/
		if(isAn_A()){
			posA = this.vertex.getPos();
		
			this.putProperty("label", new String("P"));
			
		}else{
			this.putProperty("label", new String("N"));
			
		}
		
		if (isAn_cloneA()){
			posCloneA = new Point(this.vertex.getPos());
			this.putProperty("label", new String("P"));
			
		}
		else{
			this.putProperty("label", new String("N"));
			
		}

	

		// Step -1
		this.cache = new WitnessCache();
		this.claims = new Vector<SensorMessage>();
		this.receiving = true;
		this.cloneDetected = false;
		this.receivedClaimIds = new HashSet<Integer>();
		this.setUpRouting();this.nextPulse();



		this.setUpRouting();
		this.clearWitnessPoints();
		this.nextPulse();
		this.makeCells();

		String label = this.getProperty("label").toString();
		//if(label.equals(new String("N")) || label.equals(new String("L")) ){
		if(!label.equals(new String("P"))){
			this.isMalacious = false;
		}else{
			this.isMalacious = true;
		}
		this.nextPulse();

		// Step 0
		this.fixWitnessPoints();
		this.nextPulse();

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
					//System.out.println(String.valueOf(iterationNumber)+" "+"detected");
					cloneDetected = true;
				}
			}
		}
		//--Uncomment to randomly generate compromised and cloned nodes
		this.nextPulse();
		statisticsProc(iterationNumber, cloneDetected, posA,	posCloneA);

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
