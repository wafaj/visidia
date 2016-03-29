package visidia.examples.algo.lc0;

import visidia.simulation.process.algorithm.LC0_Algorithm;
import visidia.simulation.process.edgestate.MarkedState;

public class SpanningTree_with_Id extends LC0_Algorithm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5453687686523158580L;

	private int linkValues[];
	
	@Override
	public String getDescription() {
		return "This algorithm creates a spanning tree by using LC0 coordination procedure.\n"
				+ "\n"
				+ "This algorithm implements the following rule: \n"
				+ "R: (X,i)--a--(X,j) ---> (X,i)--i--(X,i)  if i > j";
	}

	@Override
	protected void beforeStart() {
		setLocalProperty("label", Integer.toString(vertex.getId()) );
		
		linkValues = new int[vertex.degree()];
		for (int door = 0; door < vertex.degree(); door++)
			linkValues[door] = 0;
	}

	@Override
	protected void onStarCenter() {
		
		int localLabel = Integer.parseInt((String) getLocalProperty("label"));
		int neighborLabel =Integer.parseInt((String) getNeighborProperty("label"));

		if (neighborLabel > localLabel) {
			
			linkValues[neighborDoor]=neighborLabel;
			setLocalProperty("label", Integer.toString(neighborLabel));
			
			for (int door = 0; door < vertex.degree(); door++)
				if (linkValues[door] < neighborLabel)
					setDoorState(new MarkedState(false), door);
			
			setDoorState(new MarkedState(true));

		} else if (neighborLabel < localLabel) {
			linkValues[neighborDoor]= localLabel;
		}
	}

	@Override
	public Object clone() {
		return new SpanningTree_with_Id();
	}

}
