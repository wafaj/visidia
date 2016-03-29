package visidia.simulation.process.criterion;

import visidia.simulation.process.messages.IntegerMessage;

// TODO: Auto-generated Javadoc
/*
 * permet de choisir un objet selon sa classe.
 */
/**
 * IntegerMessageCriterion is used to identify a message containing an integer.
 */
public class IntegerMessageCriterion extends MessageCriterion {

	/**
	 * Returns true if object is an IntegerMessage.
	 * 
	 * @param o the object
	 * 
	 * @return true, if criterion is satisfied.
	 * 
	 * @see visidia.simulation.process.criterion.Criterion#isMatchedBy(java.lang.Object)
	 */
	public boolean isMatchedBy(Object o) {
		return o instanceof IntegerMessage;
	}

}
