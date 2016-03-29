package visidia.gui.undo;

import visidia.gui.graphview.GraphItemView;
import visidia.gui.graphview.GraphView;


// TODO: Auto-generated Javadoc
/**
 * RemoveGraphItemEdit is the undo information corresponding to a graph item removal.
 */
public class RemoveGraphItemEdit extends UndoInfo {

	private static final long serialVersionUID = 1643241144160339259L;

	/** The item. */
	private GraphItemView item = null;
	
	/** The graph view. */
	private GraphView graphView = null;

	/**
	 * Instantiates a new undo info for item removal operation.
	 * 
	 * @param item the item
	 * @param graphView the graph view
	 */
	public RemoveGraphItemEdit(GraphItemView item, GraphView graphView) {
		super("Remove graph item", false);
		this.item = item;
		this.graphView = graphView;
	}

	/**
	 * Undo operation: add the graph item.
	 * 
	 * @see visidia.gui.undo.UndoInfo#undo()
	 */
	public void undo() {
		graphView.restoreView(item);
	}

	/**
	 * Redo operation: remove the graph item.
	 * 
	 * @see visidia.gui.undo.UndoInfo#redo()
	 */
	public void redo() {
		graphView.removeView(item);
	}
}
