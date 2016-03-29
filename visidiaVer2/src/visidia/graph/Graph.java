package visidia.graph;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Class Graph manages a graph as a set of vertices.
 */
public class Graph implements Cloneable, Serializable {

	private static final long serialVersionUID = -6518663841555623995L;

	/** The vertices. */
	protected Vector<Vertex> vertices;
	
	/** The number of created vertices. */
	protected int numberOfCreatedVertices;

	/**
	 * Instantiates a new graph.
	 */
	public Graph() {
		vertices = new Vector<Vertex>(10, 10);
		numberOfCreatedVertices = 0;
	}

	/**
	 * Creates a vertex.
	 * 
	 * @return the created vertex
	 */
	public Vertex createVertex() {
		Vertex v = new Vertex(numberOfCreatedVertices++);
		vertices.add(v);
		return v;
	}

	/**
	 * Creates a vertex with specific id.
	 * Use carefully! Vertex id must be unique!
	 * 
	 * @param id the vertex id
	 * 
	 * @return the created vertex
	 */
	public Vertex createVertex(int id) {
		numberOfCreatedVertices++;
		Vertex v = new Vertex(id);
		vertices.add(v);
		return v;
	}

	/**
	 * Adds the vertex.
	 * 
	 * @param vertex the vertex
	 */
	public void addVertex(Vertex vertex) {
		vertices.add(vertex);
	}
	
	/**
	 * Removes the vertex.
	 * 
	 * @param vertex the vertex
	 */
	public void removeVertex(Vertex vertex) {
		vertices.remove(vertex);
		vertex.remove();
	}
	
	/**
	 * Gets the vertex with this id.
	 * 
	 * @param id the vertex id
	 * 
	 * @return the vertex
	 */
	public Vertex getVertex(int id) {
		Enumeration<Vertex> vertices = getVertices();
		while (vertices.hasMoreElements()) {
			Vertex v = vertices.nextElement();
			if (v.getId() == id) return v;
		}
		
		return null;
	}
	
	/**
	 * Gets the vertices.
	 * 
	 * @return the vertices
	 */
	public Enumeration<Vertex> getVertices() {
		return vertices.elements();
	}

	/**
	 * Gets the edges.
	 * 
	 * @return the edges
	 */
	public Enumeration<Edge> getEdges() {
		HashSet<Edge> set = new HashSet<Edge>();
		Enumeration<Vertex> v = getVertices();
		while (v.hasMoreElements()) {
			ArrayList<Edge> list = new ArrayList<Edge>();
			Enumeration<Edge> edges = v.nextElement().getEdges();
			while (edges.hasMoreElements()) list.add(edges.nextElement());
			set.addAll(list); // only add new edges
		}
		
		Vector<Edge> uniqueEdges = new Vector<Edge>(set);
		return uniqueEdges.elements();
	}

	/**
	 * Computes the graph order as the number of vertices in the graph.
	 * 
	 * @return the order
	 */
	public int order() {
		return vertices.size();
	}

	/**
	 * Computes the number of edges in the graph.
	 * 
	 * @return the number of edges
	 */
	public int getNbEdges() {
		int nbEdges = 0;
		Enumeration<Edge> edges = getEdges();
		while (edges.hasMoreElements()) {
			edges.nextElement();
			nbEdges ++;
		}
		
		return nbEdges;
	}
	
	/**
	 * Returns a clone of this graph (deep copy).
	 * 
	 * @return a clone of this graph
	 * 
	 * @see java.lang.Object#clone()
	 */
	public Object clone() {
		Graph graph = null;
		try {
			graph = (Graph) super.clone();
		} catch (CloneNotSupportedException e) {
		}
		// Hashtable mapping each vertex to its id; will be used to create edges.
		Hashtable<Integer, Vertex> hash = new Hashtable<Integer, Vertex>();
		// deep copy of vertices vector
		Iterator<Vertex> itr = vertices.iterator();
		while (itr.hasNext()) {
			Vertex copiedVertex = (Vertex) itr.next().clone();
			graph.vertices.add(copiedVertex);
			hash.put(copiedVertex.getId(), copiedVertex);
		}
		// link vertices
		itr = vertices.iterator();
		while (itr.hasNext()) {
			Vertex v = itr.next();
			int vId = v.getId();
			Enumeration<Vertex> neighbors = v.getNeighbors();
			while (neighbors.hasMoreElements()) {
				int neighborId = neighbors.nextElement().getId();
				// use the Hashtable to find the vertices to link
				Vertex orig = (Vertex) hash.get(vId);
				Vertex dest = (Vertex) hash.get(neighborId);
				// determine if the edge is oriented
				Edge edge = orig.getEdge(dest);
				if (edge != null)
					orig.linkTo(dest, edge.isOriented());
			}
		}
		
		return graph;
	}

	/**
	 * Recursive method used in distance computation.
	 * 
	 * @param vertex the vertex from which to compute distances
	 * @param distances the distances array
	 */
	private void recursiveDistance(Vertex vertex, int[] distances) {
		Enumeration<Vertex> neighbors = vertex.getNeighbors();
		int nbVisitsVertex = distances[vertex.getId()];
		while (neighbors.hasMoreElements()) {
			Vertex neighbor = neighbors.nextElement();
			if (distances[neighbor.getId()] > nbVisitsVertex + 1) {
				distances[neighbor.getId()] = nbVisitsVertex + 1;
				recursiveDistance(neighbor, distances);
			}
		}
	}
	
	/**
	 * Compute the distances (in number of edges) from this vertex to other vertices in graph.
	 * 
	 * @param vertex the first vertex
	 * 
	 * @return the array of distances
	 */
	public int[] computeDistancesFrom(Vertex vertex) {
		int nLimit = 10000; // TODO: find a better solution
		int[] distances = new int[nLimit];
		for (int k = 0; k < nLimit; ++k) distances[k] = Integer.MAX_VALUE;
		distances[vertex.getId()] = 0;

		recursiveDistance(vertex, distances);
		return distances;
	}

}
