package visidia.gui.undo;

import visidia.gui.graphview.EdgeView;
import visidia.gui.graphview.VertexView;

// TODO: Auto-generated Javadoc
/**
 * ChangeEdgeExtremitiesEdit is the undo information corresponding to a change in an edge extremities (origin and destination vertices).
 */
public class ChangeOrientation extends UndoInfo {

	private static final long serialVersionUID = 4395224451733367585L;

	/** The edge. */
	private EdgeView edge = null;
	
	/** the old orientation. */
	private boolean oldOrientation;
	
	/** the new orientation. */
	private boolean newOrientation;

	/**
	 * Instantiates a new undo info for the operation of edge extremities change.
	 * 
	 * @param edge the edge
	 * @param initialOrigin the initial origin
	 * @param initialDestination the initial destination
	 */
	public ChangeOrientation(EdgeView edge, boolean orientation) {
		super("Modify edge", false);
		this.edge = edge;
		this.oldOrientation = edge.getEdge().isOriented();
		this.newOrientation = orientation;
		
	}

	/**
	 * Undo operation: restore initial origin and destination vertices.
	 * @see visidia.gui.undo.UndoInfo#undo()
	 */
	public void undo() {
		edge.getEdge().setOriented(oldOrientation);
	}

	/**
	 * Redo operation: restore modified origin and destination vertices.
	 * @see visidia.gui.undo.UndoInfo#redo()
	 */
	public void redo() {
		edge.getEdge().setOriented(newOrientation);
	}
}
