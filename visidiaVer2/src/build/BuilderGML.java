package build;

import visidia.graph.Graph;
import visidia.graph.Vertex;

public class BuilderGML {

	public static void main(String[] args) {
		Graph g= new Graph();
		for (int i = 0; i < 5; i++) {
			g.createVertex(i);
			;//g.addVertex(new Vertex(i));
		}
		//g.

	}

}
