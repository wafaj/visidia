package visidia.examples.algo.lc2;

import visidia.simulation.process.algorithm.LC2_Algorithm;
import visidia.simulation.process.edgestate.MarkedState;

public class SpanningTree extends LC2_Algorithm {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1048869088690879900L;

	private final String ANode = "A";
	private final String NNode = "N";

	@Override
	public String getDescription() {

		return "This algorithm creates a spanning tree from a specified vertex (labeled \"A\") by using LC2 coordination procedure.\n";
	}

	@Override
	protected void beforeStart() {
		setLocalProperty("label", vertex.getLabel());
	}

	@Override
	protected void onStarCenter() {
		
		int nbN = 0;
		int doorA = -1;
		
		for (int door:getActiveDoors()){
			if (getNeighborProperty(door, "label").toString().equals(NNode))
				nbN++;

			if (getNeighborProperty(door, "label").toString().equals(ANode))
				doorA = door;
		}

		if (getLocalProperty("label").equals(NNode) && (doorA != -1)) {
			setDoorState(new MarkedState(true) , doorA);
			setLocalProperty("label", ANode);
		}

		if (getLocalProperty("label").equals(ANode) && (nbN != 0)) {
			for ( int door:getActiveDoors()) {
				if (getNeighborProperty(door, "label").toString().equals(NNode)){
					setDoorState(new MarkedState(true), door);
				}
				setNeighborProperty(door, "label", ANode);
			}
		} 
	}


	@Override
	public Object clone() {
		return new SpanningTree();
	}



}
