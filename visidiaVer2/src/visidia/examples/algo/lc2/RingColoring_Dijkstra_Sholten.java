package visidia.examples.algo.lc2;


import visidia.misc.ColorLabel;
import visidia.simulation.SimulationConstants.PropertyStatus;
import visidia.simulation.process.algorithm.LC2_Algorithm;
import visidia.simulation.process.edgestate.MarkedState;

public class RingColoring_Dijkstra_Sholten extends LC2_Algorithm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 67629039952975275L;

	@Override
	public String getDescription() {

		return "Ring colouring with the global termination detection";
	}

	@Override
	protected void beforeStart() {

		setLocalProperty("label", vertex.getLabel());
		
		if (getLocalProperty("label").equals("A")) {
			setLocalProperty("treeLabel", "A",PropertyStatus.DISPLAYED);
			setLocalProperty("state", "Active",PropertyStatus.DISPLAYED);
		}
		else{
			setLocalProperty("treeLabel", "N",PropertyStatus.DISPLAYED);
			setLocalProperty("state", "Passive",PropertyStatus.DISPLAYED);
		}

		setLocalProperty("color", new ColorLabel("X"));
		setLocalProperty("Sc", new Integer(0),PropertyStatus.DISPLAYED);

		setAllEdgesProperty("mark", false, false); // key, value, displayableOption
	}

	@Override
	protected void onStarCenter() {

		/*
		 * The following instruction allows stopping the simulation
		 * but it does not impact the algorithm's behavior
		 */
		if (getLocalProperty("treeLabel").equals("F"))
			localTermination();


		if (getActiveDoors().size()==2) 
			while ( getNeighborProperty(0,"color").equals( getLocalProperty("color"))
					|| getNeighborProperty(1,"color").equals( getLocalProperty("color")) )
				nextColor();

		if (getLocalProperty("state").equals("Active")) {

			for (int door : getActiveDoors()) {

				// If the neighbor is not in the tree
				if(getNeighborProperty(door, "treeLabel").equals("N")){
					setNeighborProperty(door, "treeLabel","M");
					setNeighborProperty(door, "state","Active");

					setEdgeProperty(door, "mark",true, false);
					setDoorState(new MarkedState(true),door);

					setLocalProperty("Sc", ((Integer)getLocalProperty("Sc"))+1);
				}
			} 

			if (((Integer)getLocalProperty("Sc"))==0 ) {

				for (int door : getActiveDoors()) 
					if ((Boolean) getEdgeProperty(door, "mark")) {
						setNeighborProperty(door, "Sc",((Integer)getNeighborProperty(door, "Sc"))-1);
						setDoorState(new MarkedState(false), door);
						setEdgeProperty(door, "mark",false, false);
					}

				setLocalProperty("treeLabel", "F");
				setLocalProperty("state", "Passive");
			}
		}
	}

	public void nextColor() {

		if (((ColorLabel) getLocalProperty("color")).getLabel().compareTo("X")==0) {
			setLocalProperty("color", new ColorLabel("Y"));
		} else if (((ColorLabel) getLocalProperty("color")).getLabel().compareTo("Y")==0) {
			setLocalProperty("color", new ColorLabel("Z"));
		} else if (((ColorLabel) getLocalProperty("color")).getLabel().compareTo("Z")==0) {
			setLocalProperty("color", new ColorLabel("X"));
		}

	}

	@Override
	public void updateVertexPropoerties() {
		
		setLocalProperty("label", ((ColorLabel)getLocalProperty("color")).getLabel());
		super.updateVertexPropoerties();

		/* For displaying node's properties on the graph. It is not mandatory */
		putProperty("state", getLocalProperty("state"), PropertyStatus.DISPLAYED);
		putProperty("Sc", getLocalProperty("Sc"), PropertyStatus.DISPLAYED);
	}

	@Override
	public Object clone() {
		return new RingColoring_Dijkstra_Sholten();
	}

}
