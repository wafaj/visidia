package visidia.examples.algo.clone_solutions;

import visidia.examples.algo.Routing;

public class RoutingAlgo extends Routing{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7785245867643799751L;

	@Override
	public Object clone() {
		return new RoutingAlgo();
	}
	
	public void unit(){
		
	}
	

}
