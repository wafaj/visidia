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


public class WitnessCache{
	// Label - Point
	private HashMap<String,Point> cache;
	private boolean detection = false;
	private Vector<String> clonedLabels;

	public WitnessCache(){
		cache = new HashMap<String,Point>();
		clonedLabels = new Vector<String>();
	}
	public void addClaim(String label,Point location){
		//TODO: Ignore label N
		if( cache.containsKey(label) ){
			if(!cache.get(label).equals(location)){
				detection = true;
				clonedLabels.addElement(label);
			}
		}else{
			cache.put(label,location);
		}
	}

	public void clearCache(){
		cache.clear();
		clonedLabels.clear();
		detection = false;
	}

	public boolean cloneDetected(){
		return detection;
	}

	public Vector<String> getClones(){
		return new Vector<String>(clonedLabels);
	}
	public int size() {
		return cache.size();
	}
};
