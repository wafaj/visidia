package visidia.gui.undo;

import visidia.gui.graphview.EdgeView;
import visidia.gui.graphview.VertexView;

// TODO: Auto-generated Javadoc
/**
 * ChangeEdgeExtremitiesEdit is the undo information corresponding to a change in an edge extremities (origin and destination vertices).
 */
public class ChangeEdgeExtremitiesEdit extends UndoInfo {

	private static final long serialVersionUID = 4395224451733367585L;

	/** The edge. */
	private EdgeView edge = null;
	
	/** The initial origin. */
	private VertexView initialOrigin = null;
	
	/** The initial destination. */
	private VertexView initialDestination = null;
	
	/** The new origin. */
	private VertexView newOrigin = null;
	
	/** The new destination. */
	private VertexView newDestination = null;

	/**
	 * Instantiates a new undo info for the operation of edge extremities change.
	 * 
	 * @param edge the edge
	 * @param initialOrigin the initial origin
	 * @param initialDestination the initial destination
	 */
	public ChangeEdgeExtremitiesEdit(EdgeView edge, VertexView initialOrigin, VertexView initialDestination) {
		super("Modify edge", false);
		this.edge = edge;
		this.initialOrigin = initialOrigin;
		this.initialDestination = initialDestination;
		this.newOrigin = edge.getOrigin();
		this.newDestination = edge.getDestination();
	}

	/**
	 * Undo operation: restore initial origin and destination vertices.
	 * @see visidia.gui.undo.UndoInfo#undo()
	 */
	public void undo() {
		edge.setOrigin(initialOrigin);
		edge.setDestination(initialDestination);
	}

	/**
	 * Redo operation: restore modified origin and destination vertices.
	 * @see visidia.gui.undo.UndoInfo#redo()
	 */
	public void redo() {
		edge.setOrigin(newOrigin);
		edge.setDestination(newDestination);
	}
}
