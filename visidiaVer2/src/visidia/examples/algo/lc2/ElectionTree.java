package visidia.examples.algo.lc2;

import visidia.simulation.process.algorithm.LC2_Algorithm;

public class ElectionTree extends LC2_Algorithm{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8915643157843265481L;
	
	private final String defaultLabel = new String("N");
	private final String lost = new String("F");
	private final String leader = new String("E");

	@Override
	public String getDescription() {
		
		return "This algorithm elects a leader in a tree using the LC2 coordination procedure.\n";
	}

	@Override
	protected void beforeStart() {
		setLocalProperty("label", vertex.getLabel());
		
	}

	@Override
	protected void onStarCenter() {
		
		if (getLocalProperty("label").equals(defaultLabel))  {
			
			if (getLocalProperty("nbNodes") == null)
				setLocalProperty("nbNodes", countNNodes());
			
						
			if( (Integer)getLocalProperty("nbNodes") == 1 ) {
				setLocalProperty("label", lost);
				for(int door: getActiveDoors()) {
					if(getNeighborProperty(door, "label").equals(defaultLabel)) {
						if ( getNeighborProperty(door, "nbNodes") != null ) {
							setNeighborProperty(door, "nbNodes", (Integer)getNeighborProperty(door, "nbNodes")-1);
							if ( (Integer)getNeighborProperty(door, "nbNodes") == 0)
								setNeighborProperty(door, "label", leader);
						}
						return;
					}
				}
			} else if( (Integer)getLocalProperty("nbNodes") == 0 ){
				setLocalProperty("label", leader);
			}
		}
	}
	
	public int countNNodes() {
		int nbNnodes = 0;
		for(int door: getActiveDoors()) {
			if(getNeighborProperty(door, "label").equals(defaultLabel))
				nbNnodes++;
		}
		
		return nbNnodes;
	}

	@Override
	public Object clone() {
		return new ElectionTree();
	}

}
