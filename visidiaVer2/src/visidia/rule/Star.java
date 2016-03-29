package visidia.rule;

import java.io.Serializable;
import java.util.Iterator;

public class Star implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -194990087737191284L;

	protected String centerState;

	protected RuleVector neighborhood;

	static String unknown = "UNKNOWN";

	/**
	 * default constructor. default centerState is "UNKNOWN"
	 */
	public Star() {
		this.centerState = Star.unknown;
		this.neighborhood = new RuleVector();
	}

	/**
	 * constructor of a star clone of an other.
	 * 
	 * @param s a Star.
	 */
	public Star(Star s) {
		this();
		int i;
		int arity = s.arity();
		this.centerState = new String(s.centerState);
		this.neighborhood = new RuleVector(arity);
		for (i = 0; i < arity; i++) {
			this.neighborhood.add((s.neighbor(i)).clone());
		}
	}

	/**
	 * @param centerState the label of the center.
	 */
	public Star(String centerState) {
		this.centerState = centerState;
		this.neighborhood = new RuleVector();
	}

	/**
	 * create a Star which Neighbors doors are numbered from 0 to arity -1.
	 * 
	 * @param centerState the label of the center.
	 * @param arity the arity of the star.
	 */
	public Star(String centerState, int arity) {
		int i;
		this.centerState = centerState;
		this.neighborhood = new RuleVector(arity);
		for (i = 0; i < arity; i++) {
			this.neighborhood.add(new Neighbor(i));
		}
	}

	/**
	 * create a Star. which Neighbors doors are numbered from 0 to arity -1.
	 * center state is "UNKNOWN"
	 * 
	 * @param arity the arity of the star.
	 */
	public Star(int arity) {
		int i;
		this.centerState = Star.unknown;
		this.neighborhood = new RuleVector(arity);
		for (i = 0; i < arity; i++) {

			this.neighborhood.add(new Neighbor(i));
		}
	}

	public String toString() {
		return "\n<Star>" + this.centerState + "," + this.arity()
		+ " Neighbours:" + this.neighborhood.toString()
		+ "\n<End Star>";
	}

	/* accessors */

	public void setCenterState(String state) {
		this.centerState = new String(state);
	}

	/**
	 * 
	 * @return the state of the center.
	 */
	public String centerState() {
		return new String(this.centerState);
	}

	/**
	 * 
	 * @param i the position
	 * @return the Neighbor on the position i.
	 */
	public Neighbor neighbor(int i) {
		return (Neighbor) this.neighborhood.get(i);
	}

	/**
	 * 
	 * @param i a position.
	 * @return the number of the door of the neighbor on the position i.
	 */
	public int neighbourDoor(int i) {
		return (((Neighbor) this.neighborhood.get(i))).doorNum();
	}

	/**
	 * add a the Neighbor v to the neighborhood. the Neighbor is added at the
	 * end of the vector.
	 * 
	 * @param v a new Neighbor
	 */
	public void addNeighbor(Neighbor v) {
		this.neighborhood.add(v);
	}

	/**
	 * remove from neighborhood the neighbor at position i.
	 * 
	 * @param i a position.
	 */
	public void removeNeighbour(int i) {
		this.neighborhood.remove(i);
	}

	/**
	 * remove all elements from neighborhood.
	 */
	public void removeAll() {
		this.neighborhood.clear();
	}

	/**
	 * sets the Neighbor n at the position i in the neighborhood.
	 * 
	 * @param position a position in neighborhood.
	 * @param n a Neighbor.
	 */
	public void setState(int position, Neighbor n) {
		this.neighborhood.setElementAt(n, position);
	}

	/**
	 * 
	 * @param s2 star
	 * @return true if centers are equals, false otherwise.
	 */
	public boolean sameCentState(Star s2) {
		return this.centerState.equals(s2.centerState());
	}

	/**
	 * 
	 * @return the arity of the star.
	 */
	public int arity() {
		return (this.neighborhood.count());
	}

	/**
	 * 
	 * @return the neighborhood.
	 */
	public RuleVector neighbourhood() {
		return this.neighborhood;
	}

	/**
	 * sets the door numbers of the star, with the value of door numbers of
	 * those at the same position in the star b.
	 * 
	 * @param b a star with the same arity.
	 */
	public void setDoors(Star b) {
		int i;
		for (i = 0; i < b.arity(); i++) {
			this.neighbor(i).setDoorNum(b.neighbor(i).doorNum());
		}
	}

	/**
	 * sets states of the star elements (center and neighbors), with the value
	 * of states of those at the same position in the star b.
	 * 
	 * @param b a star with the same arity.
	 */
	public void setStates(Star b) {
		int i;
		this.setCenterState(b.centerState());
		for (i = 0; i < b.arity(); i++) {
			for (int k = 0; k < this.arity(); k++) {
				if (b.neighbor(i).doorNum() == this.neighbor(k).doorNum()) {
					this.setState(k, b.neighbor(i));
				}
			}
		}
	}

	/* comparators, they use random access */
	/**
	 * looks in the star for a Neighbor equals to the Neighbor nei. ATT!!! it
	 * also sets the door number of nei with the door number of the element if
	 * found! * the operation looking for is Randomized
	 * 
	 * @param nei
	 * @return the index of the element if found. -1 otherwise.
	 */
	public int contains(Neighbor nei) {
		int i = this.neighborhood.indexOf(nei);
		if (i > -1) {
			nei.setDoorNum(((Neighbor) this.neighborhood.get(i)).doorNum());
		}

		return i;
	}

	/**
	 * looks in the star for a Neighbor with the same label of the Neighbor
	 * nei. the operation "looking for" is Randomized
	 * 
	 * @param nei
	 * @return the index of the element if found. -1 otherwise.
	 */
	public int containsLabel(Neighbor nei) {
		int i = this.neighborhood.indexOfLabel(nei);
		if (i > -1) {
		}
		return i;
	}

	/* we don't begin with the first neighbor, but we choose anyone */
	/**
	 * warning: this method sets doors of context by those of corresponding
	 * elements in the star. so always use a copy of the context while using
	 * this method. the sense of equality is defined in the class Neighbor
	 * 
	 * @param context a context
	 * @return true it's to identify the context with a part of the Star.
	 */
	public boolean contains(Star context) {
		int k = 0;
		int i = -1;
		if (this.sameCentState(context) == false) {
			return false;
		} else {
			Iterator it = context.neighborhood.randIterator();
			while (it.hasNext()) {
				Neighbor n = (Neighbor) it.next();
				i = this.contains(n);
				if (i < 0) {
					return false;
				} else {
					int door = ((Neighbor) this.neighborhood.get(i))
					.doorNum();
					n.setDoorNum(door);
					this.removeNeighbour(i);

				}
				k++;
			}

			return true;
		}
	}

	/**
	 * 
	 * @param star a star
	 * @return true it's to identify the context with a part of the Star. the
	 *         identification concerns only labels.
	 */
	public boolean containsLabels(Star star) {
		int k = 0;
		int i = -1;
		while (k < star.neighborhood.count()) {
			i = this.containsLabel(star.neighbor(k));
			if (i < 0) {
				// System.out.println("<0 ? ="+i+" (k="+k+")");
				return false;
			} else {
				this.removeNeighbour(i);
				// System.out.println("i="+i+"k"+k+"door
				// "+door+"=?"+star.neighbor(k));
			}
			k++;
		}
		return true;
	}

	public Object clone() {
		Star s = new Star(this);
		return s;
	}

}
