package visidia.examples.algo.lc0;

import visidia.simulation.process.algorithm.LC0_Algorithm;

public class ElectionTree_LTD extends LC0_Algorithm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5119095075115871042L;
	
	private final String lost	= "F";
	private final String leader	= "E";

	@Override
	public String getDescription() {

		return "This algorithm elects a leader in a tree using the LC0 coordination procedure.\n" +
				"The algorithm also detects local termination\n" +
				"It implements the following rules (the \"degree\" of each node is indicated in brackets): \n" +
				"R1: N(1)--N(x) ---> F(0)--N(x-1)   x > 1\n" + 
				"R2: N(1)--N(1) ---> E(0)--F(0) \n" +
				"\n" +
				"When a vertex label becomes F, the vertex stops working";
	}

	@Override
	protected  void beforeStart(){
		setLocalProperty("label", vertex.getLabel());
		setLocalProperty("arity", vertex.degree());
	}


	@Override
	protected void onStarCenter() {
			
		if (getLocalProperty("label").equals(lost))
		localTermination();
		

		if ( (Integer) getLocalProperty("arity") == 1 && 
				(Integer) getNeighborProperty("arity") > 1 ) {

			setLocalProperty("label", lost);
			setLocalProperty("arity", (Integer)getLocalProperty("arity")-1);
			
			setNeighborProperty("arity", (Integer)getNeighborProperty("arity")-1);

		} else if ( (Integer) getLocalProperty("arity") == 1 && 
				(Integer) getNeighborProperty("arity") == 1 ) {
			
			setLocalProperty("label", leader);
			setLocalProperty("arity", (Integer)getLocalProperty("arity")-1);

			setNeighborProperty("label", lost);
			setNeighborProperty("arity", (Integer)getNeighborProperty("arity")-1);

		}
	}


	@Override
	public Object clone() {
		return new ElectionTree_LTD();
	}

}