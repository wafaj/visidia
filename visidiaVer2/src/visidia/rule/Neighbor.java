package visidia.rule;

import java.io.Serializable;

/**
 * A Neighbor contains all the information concerning a node.
 */
public class Neighbor implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7262290307093698316L;

	protected String state;

	protected boolean mark;

	protected int door;

	static String unknown = "UNKNOWN";

	/**
	 * the default constructor default values are : "UNKNOWN", false , -1
	 */
	public Neighbor() {
		this.state = Neighbor.unknown;
		this.mark = false;
		this.door = -1;
	}

	/**
	 * constructor.
	 * 
	 * @param state the new Label
	 * @param edgMark the mark of the edge
	 * @param door the number of the door
	 */

	public Neighbor(String state, boolean edgMark, int door) {
		this.state = state;
		this.mark = edgMark;
		this.door = door;
	}

	public Neighbor(String state, boolean edgMark) {
		this(state, edgMark, -1);
	}

	public Neighbor(boolean edgMark, int door) {
		this(Neighbor.unknown, edgMark, door);
	}

	/**
	 * 
	 * @param state the label of the node
	 */
	public Neighbor(String state) {
		this(state, false, -1);
	}

	public Neighbor(int door) {
		this(Neighbor.unknown, false, door);
	}

	public String toString() {
		return " Neigh(" + this.state + "_" + this.door + "_" + this.mark + ")";
	}

	/* accessors */
	/**
	 * return the door number.
	 * 
	 * @return accessor to door.
	 */
	public int doorNum() {
		return this.door;
	}

	/**
	 * set the value of door.
	 * 
	 * @param n the new door number.
	 */
	public void setDoorNum(int n) {
		this.door = n;
	}

	/**
	 * sets properties with value of those of the Neighbor given on
	 * parameters. only the door number is not set.
	 * 
	 * @param n Neighbor.
	 */
	public void setState(Neighbor n) {// not doors
		this.mark = n.mark();
		this.state = n.state();
	}

	/**
	 * return the edge mark.
	 * 
	 * @return the edge mark.
	 */
	public boolean mark() {
		return this.mark;
	}

	/**
	 * return the state ( a )
	 * 
	 * @return the state (label).
	 */
	public String state() {
		return this.state;
	}

	public Object clone() {
		return new Neighbor(this.state, this.mark, this.door);
	}

	/* comparators: doors are not compared, but door should not be affected */
	/**
	 * two neighbors are equal if they have same labels, marks, and doors.
	 * doors are not compared if not affected ( -1 ).
	 * 
	 * @param n
	 * @return true if Neighbors are equals, -1 otherwise.
	 */
	public boolean equals(Neighbor n) {
		if ((this.mark == n.mark) && this.state.equals(n.state)) {
			if (n.door == -1) {
				return true;
			}
			return (this.door == n.door);
		}
		return false;
	}

	/**
	 * compare only the labels.
	 * 
	 * @param n
	 * @return true if labels are equals, -1 otherwise.
	 */
	public boolean sameState(Neighbor n) {
		return (this.state.equals(n.state));
	}

}
