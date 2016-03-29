package visidia.examples.algo.lc2;

import visidia.simulation.process.algorithm.LC2_Algorithm;
import visidia.simulation.process.edgestate.MarkedState;

public class SpanningTree_Id extends LC2_Algorithm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3773357811335274264L;

	@Override
	public String getDescription() {
		return "This algorithm creates a spanning tree by using LC2 coordination procedure.\n";
	}

	@Override
	protected void beforeStart() {
		
		vertex.setLabel(Integer.toString(vertex.getId()));
		setLocalProperty("label", vertex.getLabel() );
		
		setLocalProperty("id", vertex.getId() );
		
	}

	@Override
	protected void onStarCenter() {
		
		int neighbourDoor = -1;

		int max=(Integer)getLocalProperty("id");
		int neighborValue;
		
		for (int door:getActiveDoors()) {
			
			neighborValue =(Integer)getNeighborProperty(door, "id");

			if(max < neighborValue) {
				neighbourDoor=door;
				max=neighborValue;
			}
		}

		if (neighbourDoor != -1) {
			int newId = (Integer)getNeighborProperty(neighbourDoor, "id");
			setLocalProperty("id", new Integer(newId));

			setEdgeProperty(neighbourDoor, "id", new Integer(newId));
			if((Integer)getLocalProperty("id") == getNetSize()-1)    //  To be modified !!!
				setDoorState(new MarkedState(true), neighbourDoor);
		}

		for (int door: getActiveDoors()) {
			if(((Integer)getNeighborProperty(door, "id")) < max){
				setEdgeProperty(door, "id", getLocalProperty("id"));
				if((Integer)getLocalProperty("id") == getNetSize()-1)
					setDoorState(new MarkedState(true), door);
			}
		}

		setAllNeighborsProperty("id", getLocalProperty("id"));

	}

	@Override
	public Object clone() {
		return new SpanningTree_Id();
	}

}
