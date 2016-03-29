package visidia.examples.algo.lc0;

import visidia.simulation.process.algorithm.LC0_Algorithm;
import visidia.simulation.process.edgestate.MarkedState;

public class SpanningTree_LTD extends LC0_Algorithm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5119095075115871042L;

	@Override
	public String getDescription() {

		return "This algorithm creates a spanning tree from a specified vertex (labeled \"A\") by using the LC0 coordination procedure.\n" +
				"\n" +
				"This algorithm implements the following rules: \n" +
				"R1: N(t, f, d)--und--A(t', f', d') ---> A(t+1, f, d)--inc--A(t'+1, f', d') \n" + 
				"R2: A(t, f, d)--und--A(t', f', d') ---> A(t, f+1, d)--exc--A(t', f'+1, d') \n" + 
				"R3: A(t, f, d) ---> W(t, f, d) if t+f = d  (termination detection) \n" +
				"\n" +
				"t: the number of doors which are included in the spanning tree\n" +
				"f: the number of doors which are will not be included in the spanning tree\n" +
				"d: the degree of the vertex\n" +
				"\n" +
				"inc: An edge which is included in the spanning tree\n" +
				"exc: An edge which will not be included in the spanning tree\n" +
				"und: an edge whose status is undefined\n";
	}

	@Override
	protected  void beforeStart(){
		setLocalProperty("label", vertex.getLabel());
		setLocalProperty("degree", vertex.degree());
		setLocalProperty("tree_edges", 0);
		setLocalProperty("nonTree_edges", 0);
	}
	

	@Override
	protected void onStarCenter() {
		
		// Termination detection
		if ( (Integer)getLocalProperty("tree_edges") +
				(Integer)getLocalProperty("nonTree_edges") ==
				(Integer)getLocalProperty("degree")) {

			localTermination();
		}
		
		if (getEdgeProperty("elabel") == null) {
			setEdgeProperty("elabel", "UND", false);  // false : not displayable property
		}
		
		if (getEdgeProperty("elabel").equals("UND")) {
			if ( getLocalProperty("label").equals("A") && 
					getNeighborProperty("label").equals("N") ) {

				setNeighborProperty("label", "A");
				setNeighborProperty("tree_edges", (Integer)getNeighborProperty("tree_edges")+1);
				setLocalProperty("tree_edges", (Integer)getLocalProperty("tree_edges")+1);
				setEdgeProperty("elabel", "INC", false);
				setDoorState(new MarkedState(true), neighborDoor);
				
			} else if ( getLocalProperty("label").equals("A") && 
					getNeighborProperty("label").equals("A") ) {
				
				setNeighborProperty("nonTree_edges", (Integer)getNeighborProperty("nonTree_edges")+1);
				setLocalProperty("nonTree_edges", (Integer)getLocalProperty("nonTree_edges")+1);
				setEdgeProperty("elabel", "EXC", false);
			}
		}

	}

	@Override
	protected void updateVertexPropoerties() {

		if ( (Integer)getLocalProperty("tree_edges") +
				(Integer)getLocalProperty("nonTree_edges") ==
				(Integer)getLocalProperty("degree")) {

			setLocalProperty("label", "W");
		}

		super.updateVertexPropoerties();

	}

	@Override
	public Object clone() {
		return new SpanningTree_LTD();
	}

}
