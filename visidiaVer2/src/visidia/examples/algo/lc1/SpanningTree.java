package visidia.examples.algo.lc1;

import visidia.simulation.process.algorithm.LC1_Algorithm;
import visidia.simulation.process.edgestate.MarkedState;

public class SpanningTree extends LC1_Algorithm {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2630545512604145326L;

	private final String ANode = "A";
	private final String NNode = "N";


	@Override
	public String getDescription() {

		return "This algorithm creates a spanning tree from a specified vertex (labeled \"A\") by using LC1 coordination procedure.\n";
	}

	@Override
	protected void beforeStart() {
		setLocalProperty("label", vertex.getLabel());		
	}

	@Override
	protected void onStarCenter() {

		int doorA = -1;

		if( getLocalProperty("label").equals(NNode) ) {
			for (int door:getActiveDoors()){
				if (getNeighborProperty(door, "label").equals(ANode))
					doorA = door;
			}
			
			if( doorA != -1) {
				setDoorState(new MarkedState(true) , doorA);
				setLocalProperty("label", ANode);
			}
		}
	}

	@Override
	public Object clone() {
		return new SpanningTree();
	}

}
