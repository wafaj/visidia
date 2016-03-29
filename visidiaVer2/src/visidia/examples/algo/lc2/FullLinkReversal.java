package visidia.examples.algo.lc2;

import visidia.graph.Edge;
import visidia.graph.Vertex;
import visidia.simulation.process.algorithm.LC2_Algorithm;

public class FullLinkReversal extends LC2_Algorithm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2554851704768140675L;

	private final int destination = 0;
	private boolean unDirected = true;

	@Override
	public String getDescription() {

		return "Full Link Reversal Algorithm (LC2)";
	}

	@Override
	protected void beforeStart() {

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
	}

	@Override
	protected void onStarCenter() {

		if ( vertex.getId() != destination) {
			
			if (unDirected)
				directGraph();

			if ( isSink()) {
				for(int door:activeDoors){
					Edge edge = vertex.getEdge(vertex.getNeighborByDoor(door));
					edge.switchOriginAndDestination();
					setLocalProperty("incomings", (Integer)getLocalProperty("incomings")-1);
					setNeighborProperty(door, "incomings", (Integer)getNeighborProperty(door, "incomings")+1);
				}
			}

		}
	}

	public void directGraph() {

		for(int door:activeDoors){
			Edge edge = vertex.getEdge(vertex.getNeighborByDoor(door));

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

		super.updateVertexPropoerties();

	}

	@Override
	public Object clone() {
		return new FullLinkReversal();
	}

}
