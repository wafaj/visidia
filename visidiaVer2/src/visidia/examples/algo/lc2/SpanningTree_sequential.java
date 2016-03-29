package visidia.examples.algo.lc2;

import visidia.simulation.process.algorithm.LC2_Algorithm;
import visidia.simulation.process.edgestate.MarkedState;

public class SpanningTree_sequential extends LC2_Algorithm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5426721214200963784L;

	private static final String	ANode = new String("A");
	private static final String	NNode = new String("N");
	private static final String	MNode = new String("M");
	private static final String	FNode = new String("F");

	@Override
	public String getDescription() {

		return  "This algorithm creates a spanning tree from a specified vertex (labeled \"A\") by using LC2 coordination procedure.\n"

				+ "This algorithm implements the following rule: \n"
				+ "R1: A-0-N  ---> M-1-A\n" 
				+ "R2: M-1-A  ---> A-1-F\n"
				+ "Priority: R1 > R2";
	}

	@Override
	protected void beforeStart() {
		setLocalProperty("label", vertex.getLabel());
	}

	@Override
	protected void onStarCenter() {

		int neighborA = -1;
		int neighborM = -1;
		int neighborN = -1;

		for (int door:getActiveDoors()) {
			
			if (getNeighborProperty(door, "label").equals(ANode)) {
				neighborA=door;
			} else if (getNeighborProperty(door, "label").equals(NNode)) {
				neighborN = door;
			} else if (getNeighborProperty(door, "label").equals(MNode) ) {

				if (getEdgeProperty(door, "mark") != null && (Boolean) getEdgeProperty(door, "mark"))
					neighborM = door;
			}
		}

		if(getLocalProperty("label").equals(ANode) && (neighborN != -1)){ // R1

			setLocalProperty("label", MNode);
			setNeighborProperty(neighborN,"label", ANode);
			
			setEdgeProperty(neighborN, "mark", true, false);   // door, value, displayableOption
			setDoorState(new MarkedState(true), neighborN);
			

		} else if (getProperty("label").equals(NNode) && neighborA != -1) { // R1

			setLocalProperty("label", ANode);
			setNeighborProperty(neighborA,"label", MNode);
			
			setEdgeProperty(neighborA,"mark",true, false); // door, value, displayableOption
			setDoorState(new MarkedState(true), neighborA);

		}else{ 
			if (getProperty("label").toString().equals(ANode) && (neighborM != -1) && (neighborN == -1)) {  // R2

				setLocalProperty("label", FNode);
				setNeighborProperty(neighborM,"label", ANode);

			}
		}
	}

	@Override
	public Object clone() {
		return new SpanningTree_sequential();
	}

}
