package visidia.examples.algo.lc1;

import visidia.simulation.process.algorithm.LC1_Algorithm;

public class ElectionTree extends LC1_Algorithm{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4297859572154195867L;

	final String defaultLabel	= new String("N");
	final String lost			= new String("F");
	final String leader 		= new String("E");

	@Override
	public String getDescription() {
		return "This algorithm elects a leader in a tree using the LC1 coordination procedure.\n" +
				"\n" +
				"It uses the following rules: \n" +
				"- if the center of the star has exactly one neighbor whose label is N," +
				"the center's state becomes \"lost\" (label F)\n" + 
				"- if the center of the star has no neighbor whose label is N," +
				"the center's state becomes \"leader\" (label E)\n" + 
				"\n" +
				"When a vertex label becomes E, the vertex stops working and asks all vertices to stop";
	}

	@Override
	protected void beforeStart() {

		setLocalProperty("label", vertex.getLabel());
	}

	@Override
	protected void onStarCenter() {

		if (getLocalProperty("label").equals(leader))
			globalTermination();
		 

		/*if (getLocalProperty("label").equals(lost) || getLocalProperty("label").equals(leader))
			localTermination();
		 */

		int nbNnodes = 0;

		if (getLocalProperty("label").equals(defaultLabel))  {
			for(int door: getActiveDoors()) {
				if(getNeighborProperty(door, "label").equals(defaultLabel))
					nbNnodes++;
			}

			if(nbNnodes==1)
				setLocalProperty("label", lost);
			else if(nbNnodes==0){
				setLocalProperty("label", leader);
			}
		}
	}

	@Override
	public Object clone() {
		return new ElectionTree();
	}

}
