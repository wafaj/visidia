package visidia.gui.undo;

import java.util.ListIterator;
import java.util.Vector;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;

// TODO: Auto-generated Javadoc
/**
 * UndoInfo represent an element of an UndoStack, and may correspond either to a single operation or to a group of operations.
 * In the latter case, UndoInfo owns a set of other UndoInfo objects.
 */
public class UndoInfo extends AbstractUndoableEdit {

	private static final long serialVersionUID = -5819238837917100885L;

	/** The description of the undo element. */
	private String description;
	
	/** This variable specifies if elements can be added to the info (that is the info is a group). */
	private boolean allowAdds;
	
	/** The edits added to the group. */
	private Vector<UndoableEdit> addedEdits = null;

	/** The group parent */
	private UndoInfo parent = null;
	/**
	 * Instantiates a new undo info.
	 * 
	 * @param description the undo element description
	 * @param allowAdds true if info is a group
	 */
	
	protected UndoInfo(String description, boolean allowAdds) {
		this.description = description;
		this.allowAdds = allowAdds;
		if (allowAdds) {
			addedEdits = new Vector<UndoableEdit>(10, 10);
			this.parent = null;
		}
	}
	protected UndoInfo(String description, boolean allowAdds, UndoInfo parent) {
		this.description = description;
		this.allowAdds = allowAdds;
		if (allowAdds) {
			addedEdits = new Vector<UndoableEdit>(10, 10);
			this.parent = parent;
		}
	}

	/**
	 * Gets the undo element description.
	 * 
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	
	public UndoInfo getParent(){
		return parent;
	}

	/**
	 * Undo last single operation or group of operations.
	 * 
	 * @see javax.swing.undo.AbstractUndoableEdit#undo()
	 */
	public void undo() {
		try {
			if (allowAdds && addedEdits.size() > 0) {
				ListIterator<UndoableEdit> itr = addedEdits.listIterator();
				while (itr.hasNext()) itr.next();
				while (itr.hasPrevious()) itr.previous().undo();
			}
			super.undo();		    
		} catch(CannotUndoException e) {
		}
	}

	/**
	 * Redo last undo'ed operation (or group of operations).
	 * 
	 * @see javax.swing.undo.AbstractUndoableEdit#redo()
	 */
	public void redo() {
		try {
			if (addedEdits != null && addedEdits.size() > 0) {
				ListIterator<UndoableEdit> itr = addedEdits.listIterator();
				while (itr.hasNext()) itr.next().redo();
			}

			super.redo();
		} catch(CannotUndoException e) {
		}
	}

	/**
	 * Adds the element to the group, if allowAdds is true. If not, just return false.
	 * 
	 * @param element the element
	 * 
	 * @return true, if successful
	 */
	public boolean addElement(UndoableEdit element) {
		if (allowAdds) {
			addedEdits.addElement(element);
			return true;
		} else
			return false;
	}

}
