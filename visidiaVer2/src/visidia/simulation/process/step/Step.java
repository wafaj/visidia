package visidia.simulation.process.step;

import java.io.Serializable;

public abstract class Step implements Cloneable, Serializable {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -1452495052453844652L;
	
	
	public abstract Object clone();

}
