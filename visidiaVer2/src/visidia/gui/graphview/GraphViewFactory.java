package visidia.gui.graphview;

import visidia.graph.Vertex;
import visidia.graph.Edge;

// TODO: Auto-generated Javadoc
/**
 * The GraphViewFactory class is a factory for creating VertexView and EdgeView objects.
 */
public class GraphViewFactory {

	/** The vertex view prototype. */
	private VertexView vertexViewPrototype;

	/** The edge view prototype. */
	private EdgeView edgeViewPrototype;

	/**
	 * Instantiates a new graph view factory.
	 * 
	 * @param vv the vertex view prototype
	 * @param ev the edge view prototype
	 */
	public GraphViewFactory(VertexView vv, EdgeView ev) {
		vertexViewPrototype = vv;
		edgeViewPrototype = ev;
	}

	/**
	 * Creates a new vertex view.
	 * 
	 * @param vertex the vertex associated to the view
	 * 
	 * @return the vertex view
	 */
	public VertexView makeVertexView(Vertex vertex) {
		if (vertex == null) return null;
		VertexView view = (VertexView) vertexViewPrototype.clone();
		view.setVertex(vertex);
		return view;
	}

	/**
	 * Creates a new edge view.
	 * 
	 * @param edge the edge associated to the view
	 * 
	 * @return the edge view
	 */
	public EdgeView makeEdgeView(Edge edge) {
		if (edge == null) return null;
		EdgeView view = (EdgeView) edgeViewPrototype.clone();
		view.setEdge(edge);
		return view;
	}

	/**
	 * Gets the vertex view prototype.
	 * 
	 * @return the vertex view prototype
	 */
	public VertexView getVertexViewPrototype() {
		return vertexViewPrototype;
	}

	/**
	 * Gets the edge view prototype.
	 * 
	 * @return the edge view prototype
	 */
	public EdgeView getEdgeViewPrototype() {
		return edgeViewPrototype;
	}
	
	/**
	 * Sets the vertex view prototype.
	 * 
	 * @param vv the new vertex view prototype
	 */
	public void setVertexViewPrototype(VertexView vv) {
		vertexViewPrototype = vv;
	}

	/**
	 * Sets the edge view prototype.
	 * 
	 * @param ev the new edge view prototype
	 */
	public void setEdgeViewPrototype(EdgeView ev) {
		edgeViewPrototype = ev;
	}
}
