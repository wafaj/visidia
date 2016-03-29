package visidia.gui.graphview;

import java.awt.Graphics;
import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * ViewableItem is the abstract base class used as a prototype representing viewable items such as graph edges or vertices.
 */
public abstract class ViewableItem implements Cloneable, Serializable {

	private static final long serialVersionUID = 4500220587972557643L;

	/**
	 * An enumeration describing available item states.
	 */
	protected enum ItemState implements Serializable {

		/** The DELETED state. */
		DELETED, 

		/** The ACTIVE state. */
		ACTIVE, 

		/** The SELECTED state. */
		SELECTED
	}

	/** The marked state. */
	private boolean marked = false;

	/** The synchronized state. */
	private boolean synchro = false;
	
	/** The item's current state. */
	protected ItemState state = ItemState.ACTIVE;

	/**
	 * Gets the item's current state.
	 * 
	 * @return the state
	 */
	public ItemState getState() {
		return state;
	}

	/**
	 * Marks the item as deleted.
	 */
	public void delete() {
		state = ItemState.DELETED;	
	}
	
	/**
	 * Selects the item.
	 */
	public void select() {
		state = ItemState.SELECTED;
	}

	/**
	 * Activates the item.
	 */
	public void activate() {
		state = ItemState.ACTIVE;
	}

	/**
	 * Sets the item (un)marked.
	 * 
	 * @param marked the marked state
	 */
	public void mark(boolean marked) {
		this.marked = marked;
	}

	/**
	 * Sets the item (un)synchronized.
	 * 
	 * @param synchro the synchronized state
	 */
	public void synchronize(boolean synchro) {
		this.synchro = synchro;
	}

	/**
	 * Checks if is marked.
	 * 
	 * @return true, if is marked
	 */
	public boolean isMarked() {
		return marked;
	}

	/**
	 * Checks if is synchronized.
	 * 
	 * @return true, if is synchronized
	 */
	public boolean isSynchronized() {
		return synchro;
	}

	/**
	 * Reset item state.
	 */
	public void resetState() {
		this.marked = false;
		this.synchro = false;
		this.state = ItemState.ACTIVE;
	}

	/**
	 * Abstract method to draw the viewable item in a Graphics context.
	 * 
	 * @param g the Graphics context
	 */
	public abstract void draw(Graphics g);

	/**
	 * Returns a clone of this view.
	 * 
	 * @return the object
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public Object clone() {
		ViewableItem item = null;
		try {
			item = (ViewableItem) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return item;
	}
}
