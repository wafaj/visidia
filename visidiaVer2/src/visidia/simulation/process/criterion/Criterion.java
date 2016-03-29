package visidia.simulation.process.criterion;

// TODO: Auto-generated Javadoc
/**
 * This interface represents a arbitrary criterion in visidia simulation.
 */
public interface Criterion {

	/**
	 * Checks if object satisfies the criterion.
	 * 
	 * @param o the object
	 * 
	 * @return true, if criterion is satisfied
	 */
	public boolean isMatchedBy(Object o);

}
