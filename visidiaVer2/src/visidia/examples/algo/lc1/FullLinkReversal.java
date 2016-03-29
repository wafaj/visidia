package visidia.examples.algo.lc1;

import visidia.graph.Edge;
import visidia.graph.Vertex;
import visidia.simulation.process.algorithm.LC1_Algorithm;

public class FullLinkReversal extends LC1_Algorithm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2768899105786872948L;

	private final int destination = 0;
	private boolean unDirected = true;

	@Override
	public String getDescription() {

		return "Full Link Reversal Algorithm (LC1)";
	}

	@Override
	protected void beforeStart() {
		
		if (vertex.getId() == destination) {
			properties.setValue("height", 0);
			for(int door:activeDoors){
				Vertex neighbor = vertex.getNeighborByDoor(door);
				Edge edge = vertex.getEdge(neighbor);

				edge.setOrigin(neighbor);
				edge.setDestination(vertex);
				edge.setOriented(true);
			}
		} else {
			int height = vertex.getId() != 0 ? vertex.getId() : destination;
			properties.setValue("height", height);
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
				} else {
					if (vertex.getId() == edge.getOrigin().getId()) {
						edge.switchOriginAndDestination();
					}
				}
			} 
		}

		unDirected = false;
		setLocalProperty("label", "P");
	}

	public boolean isSink() {

		int nbIncomings = 0;

		for(int door:activeDoors){
			Edge edge = vertex.getEdge(vertex.getNeighborByDoor(door));

			if ( edge.isOriented() && vertex.getId() == edge.getDestination().getId() ) {
				nbIncomings++;
			}
		}

		return ( nbIncomings == vertex.degree());

	}

	@Override
	public Object clone() {
		return new FullLinkReversal();
	}

}
