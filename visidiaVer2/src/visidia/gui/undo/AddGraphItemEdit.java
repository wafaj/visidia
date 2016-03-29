package visidia.gui.undo;

import visidia.gui.graphview.GraphItemView;
import visidia.gui.graphview.GraphView;

// TODO: Auto-generated Javadoc
/**
 * AddGraphItemEdit is the undo information corresponding to a graph item add.
 */
public class AddGraphItemEdit extends UndoInfo {

	private static final long serialVersionUID = 8341267498590126133L;

	/** The item. */
	private GraphItemView item = null;
	
	/** The graph view. */
	private GraphView graphView = null;

	/**
	 * Instantiates a new undo info for item add operation.
	 * 
	 * @param item the item
	 * @param graphView the graph view
	 */
	public AddGraphItemEdit(GraphItemView item, GraphView graphView) {
		super("Add graph item", false);
		this.item = item;
		this.graphView = graphView;
	}

	/**
	 * Undo operation: remove the graph item.
	 * 
	 * @see visidia.gui.undo.UndoInfo#undo()
	 */
	public void undo() {
		graphView.removeView(item);
	}

	/**
	 * Redo operation: add the graph item.
	 * 
	 * @see visidia.gui.undo.UndoInfo#redo()
	 */
	public void redo() {
		graphView.restoreView(item);
	}
}
