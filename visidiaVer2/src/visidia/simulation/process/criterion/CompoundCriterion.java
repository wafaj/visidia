package visidia.simulation.process.criterion;

import java.util.Iterator;
import java.util.LinkedList;


// TODO: Auto-generated Javadoc
/**
 * This class defines a criterion as a combination of several criteria.
 * The CompoundCriterion is matched if and only if all contained criteria are matched.
 */
public class CompoundCriterion implements Criterion {

	/** The criterion list. */
	private LinkedList<Criterion> criterionList = null;

	/**
	 * Instantiates a new (empty) compound criterion.
	 */
	public CompoundCriterion() {
		this.criterionList = new LinkedList<Criterion>();
	}

	/**
	 * Adds a criterion to the CompoundCriterion.
	 * 
	 * @param c the criterion
	 */
	public void add(Criterion c) {
		this.criterionList.add(c);
	}

	/**
	 * Removes a criterion.
	 * 
	 * @param c the criterion
	 * 
	 * @return true, if successful
	 */
	public boolean remove(Criterion c) {
		return this.criterionList.remove(c);
	}

	/**
	 * Removes all criteria.
	 */
	public void removeAllCriterion() {
		this.criterionList = new LinkedList<Criterion>();
	}

	/**
	 * Checks if object satisfies the criterion.
	 * 
	 * @param o the object
	 * 
	 * @return true, if criterion is satisfied. If the CompoundCriterion is empty, returns false.
	 * 
	 * @see visidia.simulation.process.criterion.Criterion#isMatchedBy(java.lang.Object)
	 */
	public boolean isMatchedBy(Object o) {
		if (this.criterionList.isEmpty()) {
			return false;
		}

		Iterator<Criterion> iterator = this.criterionList.iterator();
		while (iterator.hasNext()) {
			Criterion c = (Criterion) iterator.next();
			if (!c.isMatchedBy(o)) {
				return false;
			}
		}

		return true;
	}

}

