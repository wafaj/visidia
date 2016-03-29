package visidia.examples.algo.lc1;

import visidia.misc.SynchronizedRandom;
import visidia.simulation.process.algorithm.LC1_Algorithm;

public class SSP extends LC1_Algorithm{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2588984350130697494L;
	
	private int choosenNumber = 0;

	@Override
	public String getDescription() {
		return "The Szymanski, Shy et Prywes (SSP) algorithm, using LC1 coordination procedure.\n";
	}

	@Override
	protected void beforeStart() {
		
		setLocalProperty("label", vertex.getLabel());
		setLocalProperty("predicate", false);
		setLocalProperty("a", -1);
		
		choosenNumber = Math.abs(SynchronizedRandom.nextInt());
	}

	@Override
	protected void onStarCenter() {
		
		if ( !(Boolean)getLocalProperty("predicate")) {
			int randomNumber = Math.abs(SynchronizedRandom.nextInt());
			if ( randomNumber >= choosenNumber ) {
				setLocalProperty("predicate", true);
			}
		}
		
		if ( (Boolean)getLocalProperty("predicate")) {
			int minA = (Integer) getLocalProperty("a");
			
			for(int door : getActiveDoors())
				if ((Integer)getNeighborProperty(door, "a") < minA)
					minA = (Integer)getNeighborProperty(door, "a");
			
			setLocalProperty("a", minA + 1);
			setLocalProperty("label", Integer.toString((Integer) getLocalProperty("a")));
		}
	}

	@Override
	public Object clone() {
		return new SSP();
	}

}
