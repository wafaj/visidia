package visidia.gui.undo;

import visidia.gui.graphview.VertexView;

// TODO: Auto-generated Javadoc
/**
 * MoveVertexEdit is the undo information corresponding to a vertex move.
 */
public class MoveVertexEdit extends UndoInfo {

	private static final long serialVersionUID = 2524694204187978394L;

	/** The vertex. */
	private VertexView vertex = null;
	
	/** The displacement along x axis. */
	private int dx;
	
	/** The displacement along y axis. */
	private int dy;

	/**
	 * Instantiates a new undo info for vertex move operation.
	 * 
	 * @param vertex the vertex
	 * @param dx the displacement along x axis
	 * @param dy the displacement along y axis
	 */
	public MoveVertexEdit(VertexView vertex, int dx, int dy) {
		super("Move vertex", false);
		this.vertex = vertex;
		this.dx = dx;
		this.dy = dy;
	}

	/**
	 * Undo operation: move the vertex to its initial position.
	 * 
	 * @see visidia.gui.undo.UndoInfo#undo()
	 */
	public void undo() {
		vertex.move(-dx, -dy);
	}

	/**
	 * Redo operation: move the vertex to its new position.
	 * 
	 * @see visidia.gui.undo.UndoInfo#redo()
	 */
	public void redo() {
		vertex.move(dx, dy);
	}
}
