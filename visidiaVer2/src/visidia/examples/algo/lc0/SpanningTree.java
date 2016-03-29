package visidia.examples.algo.lc0;

import visidia.simulation.process.algorithm.LC0_Algorithm;
import visidia.simulation.process.edgestate.MarkedState;

public class SpanningTree extends LC0_Algorithm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5119095075115871042L;

	@Override
	public String getDescription() {

		return "This algorithm creates a spanning tree from a specified vertex (labeled \"A\") by using the LC0 coordination procedure.\n" +
				"\n" +
				"This algorithm implements the following rule: \n" +
				"R: A---N ---> A---A";
	}
	
	@Override
	protected  void beforeStart(){
		setLocalProperty("label", vertex.getLabel());
	}

	@Override
	protected void onStarCenter() {

		if ( getLocalProperty("label").equals("A") && 
				getNeighborProperty("label").equals("N") ) {

			setNeighborProperty("label", "A");
			setDoorState(new MarkedState(true), neighborDoor);
		}
	}


	@Override
	public Object clone() {
		return new SpanningTree();
	}

}
