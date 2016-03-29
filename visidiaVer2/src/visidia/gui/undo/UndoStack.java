package visidia.gui.undo;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

// TODO: Auto-generated Javadoc
/**
 * This class manages a stack of undo/redo operations.
 * Several entries may be grouped, to be all undo'ed/redo'ed as a single undo/redo operation.
 */

public class UndoStack {

	/** The undo manager. */
	private UndoManager manager = null;

	/** The current group. */
	UndoInfo currentGroup = null;

	/** The in group. */
	boolean inGroup = false;

	/**
	 * Instantiates a new undo stack.
	 */
	public UndoStack() {
		manager = new UndoManager();
	}

	/**
	 * Clears the undo stack.
	 */
	public void clear() {
		manager.discardAllEdits();
	}
	
	/**
	 * Begin a new group.
	 * 
	 * @param description the description
	 */
	public void beginGroup(String description) {
		if(!inGroup){
			currentGroup = new UndoInfo(description, true);
			manager.addEdit(currentGroup);
		}
		else{
			currentGroup.addElement(currentGroup = new UndoInfo(description, true, currentGroup));
		}//manager.addEdit(currentGroup);
		inGroup = true;
	}

	/**
	 * End the current group.
	 */
	public void endGroup() {
		currentGroup = currentGroup.getParent();
		if (currentGroup == null)
			inGroup = false;
	}

	/**
	 * Adds the undo info to the stack.
	 * 
	 * @param info the info
	 */
	public void addUndoInfo(UndoInfo info) {
		if (inGroup)
			currentGroup.addElement(info);
		else
			manager.addEdit(info);
	}

	/**
	 * Undo last operation.
	 */
	public void undo() {
		try {
			manager.undo();
		} catch (CannotUndoException e) {
		}
	}

	/**
	 * Redo last operation.
	 */
	public void redo() {
		try {
			manager.redo();
		} catch (CannotRedoException e) {
		}
	}
}
