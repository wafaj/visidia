package visidia.rule;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.Vector;

/**
 * Randomized methods.
 */
public class RuleVector extends Vector implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -464957588853922347L;
	final Vector finalThis = this;

	public RuleVector() {
		super();
	}

	public RuleVector(int i) {
		super(i);
	}

	public RuleVector(Collection c) {
		super(c);
	}

	//returns the number of elements in the vector.
	public int count() {
		return this.size();
	}

	/* Neighbours's Methods */

	/**
	 * looks in the vector for an elements Neighbor equals to the parameter
	 * nei, this operation is Randomized.
	 * 
	 * @param nei
	 * @return the index of nei in the vector if exists, else -1
	 */
	public int indexOf(Neighbor nei) {
		Neighbor n = new Neighbor();
		Iterator it = this.randIterator();
		while (it.hasNext()) {
			n = (Neighbor) it.next();
			if (n.equals(nei)) {
				return ((Vector) this).indexOf(n);
			}
		}
		return -1;
	}

	/**
	 * looks in the vector for an elements having the same label as the
	 * parameter nei. this operation is Randomized.
	 * 
	 * @param nei
	 * @return the index of nei in the vector if exists, else -1
	 */
	public int indexOfLabel(Neighbor nei) {
		Neighbor n = new Neighbor();
		Iterator it = this.randIterator();
		while (it.hasNext()) {
			n = (Neighbor) it.next();
			if (n.sameState(nei)) {
				return ((Vector) this).indexOf(n);
			}
		}
		return -1;
	}

	/* Rules's Methodes */
	/**
	 * clones the vector, all elements should be vectors. no verification is
	 * done.
	 */
	public RuleVector cloneRules() {
		RuleVector v = new RuleVector();
		Rule n = new Rule();
		Iterator it = this.iterator();
		while (it.hasNext()) {
			n = (Rule) it.next();
			v.add(n.clone());
		}
		return v;
	}

	/**
	 * looks in the vector for an elements Rule equals to the parameter r, this
	 * operation is Randomized.
	 * 
	 * @param r
	 * @return the rule index; or -1 if the vector does not contain the rule
	 */
	public int contains(Rule r) {
		Rule n = new Rule();
		Iterator it = this.randIterator();
		while (it.hasNext()) {
			n = (Rule) it.next();
			if (n.equals(r)) {
				return this.indexOf(n);
			}
		}
		return -1;
	}

	/**
	 * 
	 * @return a randomIterator for the elements of the vector.
	 */
	public Iterator randIterator() {
		return new Iterator() {
			Vector v = new Vector(RuleVector.this.finalThis);

			Random r = new Random();

			public boolean hasNext() {
				return this.v.size() > 0;
			}

			public Object next() {
				int pos = this.r.nextInt(this.v.size());
				Object o = this.v.get(pos);
				this.v.remove(pos);
				return o;
			}

			public void remove() {
			}
		};
	}

}
