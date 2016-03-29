package visidia.simulation.process.criterion;

import visidia.simulation.process.messages.Message;

// TODO: Auto-generated Javadoc
/**
 * This class implements a criterion that tests if the object is a Message.
 */
public class MessageCriterion implements Criterion {

	/**
	 * Returns true if the object is a Message.
	 * 
	 * @param o the object
	 * 
	 * @return true, if criterion is satisfied.
	 * 
	 * @see visidia.simulation.process.criterion.Criterion#isMatchedBy(java.lang.Object)
	 */
	public boolean isMatchedBy(Object o) {
		return o instanceof Message;
	}

}
