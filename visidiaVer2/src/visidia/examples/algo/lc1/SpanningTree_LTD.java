package visidia.examples.algo.lc1;

import visidia.simulation.process.algorithm.LC1_Algorithm;
import visidia.simulation.process.edgestate.MarkedState;

public class SpanningTree_LTD extends LC1_Algorithm {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -2630545512604145326L;

	private final String ANode = "A";
	private final String NNode = "N";


	@Override
	public String getDescription() {

		return "This algorithm creates a spanning tree from a specified vertex (labeled \"A\") by using LC1 coordination procedure.\n" + 
		"When a vertex detects that all its neighbours have a label which is different from \"N\" (initial label), it concludes that the " +
		"computation is locally terminated. Therefore it changes its own label to \"W\"";
	}

	@Override
	protected void beforeStart() {
		setLocalProperty("label", vertex.getLabel());		
	}

	@Override
	protected void onStarCenter() {

		int doorA = -1;
		int neighborN = 0;


		for (int door:getActiveDoors()){
			if (getNeighborProperty(door, "label").equals(ANode))
				doorA = door;
			else if (getNeighborProperty(door, "label").equals(NNode))
				neighborN++;
		}

		if( getLocalProperty("label").equals(NNode) && doorA != -1) {
			setDoorState(new MarkedState(true) , doorA);
			setLocalProperty("label", ANode);
		}

		if (neighborN == 0)
			setLocalProperty("label", "W");


	}

	@Override
	public Object clone() {
		return new SpanningTree_LTD();
	}

}
