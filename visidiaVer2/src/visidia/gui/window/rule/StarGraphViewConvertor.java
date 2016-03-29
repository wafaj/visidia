package visidia.gui.window.rule;

import java.util.Enumeration;
import java.util.Iterator;

import visidia.gui.graphview.EdgeView;
import visidia.gui.graphview.GraphItemView;
import visidia.gui.graphview.GraphView;
import visidia.gui.graphview.VertexView;
import visidia.rule.Neighbor;
import visidia.rule.Star;

public class StarGraphViewConvertor {

	/**
	 * The GraphView must already contain sommetC. Adds to vg the data and the
	 * other vertexes stored into star. The graphical position of the elements
	 * must be reorganized.
	 */
	static public void star2StarVueGraphe(Star star, GraphView vg, VertexView sommetC) {
		sommetC.getVertex().setLabel(star.centerState());
		sommetC.getVertex().setId(0);
		for (Iterator it = star.neighbourhood().iterator(); it.hasNext();) {
			Neighbor n = (Neighbor) it.next();
			VertexView s = vg.createVertexAndView(10, 10);
			s.getVertex().setLabel(n.state());
			// s.setEtiquette("" + (n.doorNum() + 1));
			EdgeView a = vg.createEdgeAndView(sommetC, s, false);
			a.mark(n.mark());
		}
	}

	// Returns SommetDessin s as s.getEtiquette().equals(id)
	static private VertexView getVertex(GraphView vg, String id) {
		for (Enumeration e = vg.getGraphItems(); e.hasMoreElements();) {
			GraphItemView f = (GraphItemView) e.nextElement();
			if (f instanceof VertexView) {
				String fId = Integer.toString(((VertexView) f).getVertex().getId());
				if (fId.equals(id)) {
					return (VertexView) f;
				}
			}
		}
		return null;
	}

	static public Star starVueGraphe2Star(GraphView vg, VertexView sommetC) {
		int vertexNumber = vg.getNbGraphItems();
		if (vertexNumber > 1) {
			// Edge must be deduted
			vertexNumber = vertexNumber - (vertexNumber / 2);
		}

		Star star = new Star((String) sommetC.getVertex().getLabel());

		for (int i = 1; i < vertexNumber; i++) {
			VertexView s = StarGraphViewConvertor.getVertex(vg, "" + i);
			EdgeView a = vg.getEdgeView(s.getVertex(), sommetC.getVertex());
			star.addNeighbor(new Neighbor((String) s.getVertex().getLabel(), a.isMarked()));
		}
		return star;
	}

}
