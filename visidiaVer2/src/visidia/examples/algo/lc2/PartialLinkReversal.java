package visidia.examples.algo.lc2;

import java.util.ArrayList;

import visidia.graph.Edge;
import visidia.graph.Vertex;
import visidia.simulation.process.algorithm.LC2_Algorithm;

public class PartialLinkReversal extends LC2_Algorithm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2554851704768148975L;

	private final int destination = 0;
	private boolean unDirected = true;
	private ArrayList<Integer> list;

	@Override
	public String getDescription() {

		return "Partial Link Reversal Algorithm";
	}

	@Override
	protected void beforeStart() {

		list = new ArrayList<Integer>();

		if (vertex.getId() == destination) {
			setLocalProperty("height", 0);
			for(int door:activeDoors){
				Vertex neighbor = vertex.getNeighborByDoor(door);
				Edge edge = vertex.getEdge(neighbor);

				edge.setOrigin(neighbor);
				edge.setDestination(vertex);
				edge.setOriented(true);
			}
			setLocalProperty("incomings", vertex.degree());
		} else {
			int height = vertex.getId() != 0 ? vertex.getId() : destination;
			setLocalProperty("height", height);
			setLocalProperty("incomings", 0);
		}

		setLocalProperty("reversal", false);
	}

	@Override
	protected void onStarCenter() {

		if ( vertex.getId() != destination) {

			if (unDirected)
				directGraph();

			if ( isSink()) {
				for(int door:activeDoors){
					if ( !list.contains(door)) {
						Edge edge = vertex.getEdge(vertex.getNeighborByDoor(door));
						edge.switchOriginAndDestination();
						setLocalProperty("incomings", (Integer)getLocalProperty("incomings")-1);
						setNeighborProperty(door, "incomings", (Integer)getNeighborProperty(door, "incomings")+1);
						setNeighborProperty(door, "reversal", true);
					}
				}
				setLocalProperty("reversal", false);
				list.clear();
			}

		}
	}

	public void directGraph() {

		for(int door:activeDoors){
			Edge edge = vertex.getEdge(vertex.getNeighborByDoor(new Integer(door)));

			if ( !edge.isOriented() ) {
				edge.setOriented(true);
				if ( (Integer)getLocalProperty("height") < (Integer) getNeighborProperty(door, "height")) {
					if (vertex.getId() == edge.getDestination().getId()) {
						edge.switchOriginAndDestination();
					} 
					setNeighborProperty(door, "incomings", (Integer)getNeighborProperty(door, "incomings")+1);
				} else {
					if (vertex.getId() == edge.getOrigin().getId()) {
						edge.switchOriginAndDestination();
					}
					setLocalProperty("incomings", (Integer)getLocalProperty("incomings")+1);
				}
			} 
		}

		unDirected = false;
		setLocalProperty("label", "P");
	}

	public boolean isSink() {

		return ( (Integer)getLocalProperty("incomings") == vertex.degree() );
	}

	@Override
	protected void updateVertexPropoerties() {
		

		if (isSink())
			setLocalProperty("label", "A");
		else if (!unDirected)
			setLocalProperty("label", "P");

		if ( (Boolean)getLocalProperty("reversal")) {
			list.add(new Integer(starCenterDoor));
		}

		setLocalProperty("reversal", false);
		super.updateVertexPropoerties();

	}

	@Override
	public Object clone() {
		return new PartialLinkReversal();
	}

}
