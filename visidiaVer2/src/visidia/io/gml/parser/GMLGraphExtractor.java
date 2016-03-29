package visidia.io.gml.parser;

import java.awt.Rectangle;
import java.awt.geom.Area;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

import visidia.graph.Graph;
import visidia.graph.Vertex;
import visidia.gui.graphview.EdgeView;
import visidia.gui.graphview.GraphView;
import visidia.gui.graphview.VertexView;
import visidia.misc.ColorLabel;
import visidia.misc.VisidiaSettings;

/**
 * This class extracts graph from GMLPairSet.
 */

public class GMLGraphExtractor {

	static public GraphView extractGraph(GMLList list) {
		//System.out.println(list.toString());
		GraphView graphView = new GraphView();
		Graph graph = graphView.getGraph();
		GMLList graphElements = (GMLList) list.getValue("graph");
		Boolean directed = (graphElements != null && graphElements.getValue("directed")!= null && (Integer)graphElements.getValue("directed") == 1);
		Hashtable<Integer, VertexView> hash = new Hashtable<Integer, VertexView>();
		if (graphElements != null) {
			Enumeration<Object> v_enum = graphElements.getValues("node");
			while (v_enum.hasMoreElements()) {
				GMLNode gmlNode = GMLGraphExtractor.extractNode((GMLList) v_enum.nextElement());
				Vertex vertex = graph.createVertex(gmlNode.getId());
				vertex.setLabel(gmlNode.getLabel());
				GMLNodeGraphics graphics = gmlNode.getGraphics();
				Rectangle rect = graphics.getArea().getBounds();
				VertexView view = graphView.createVertexView(vertex, rect.x, rect.y);
				hash.put(vertex.getId(), view);
			}

			v_enum = graphElements.getValues("edge");
			while (v_enum.hasMoreElements()) {
				GMLEdge gmlEdge = GMLGraphExtractor.extractEdge((GMLList) v_enum.nextElement());
				VertexView origin = hash.get(gmlEdge.getSourceId());
				VertexView destination = hash.get(gmlEdge.getTargetId());
				EdgeView edge=graphView.createEdgeAndView(origin, destination, directed);
				if(edge != null){
					edge.getEdge().setLabel(gmlEdge.getLabel());
					edge.getEdge().setWeight(gmlEdge.getWeight());
				}
			}
		}
		return graphView;
	}

	static public GMLNode extractNode(GMLList list) {
		GMLNode gmlNode = new GMLNode();
		gmlNode.setId((Integer) list.getValue("id"));
		if (list.getValue("label") != null && VisidiaSettings.getInstance().getBoolean(VisidiaSettings.Constants.GML_NODE_LABEL))
			gmlNode.setLabel(((String) list.getValue("label")).replace("\"", ""));
		else
			gmlNode.setLabel(((ColorLabel)VisidiaSettings.getInstance().getObject(VisidiaSettings.Constants.VERTEX_DEFAULT_LABEL)).getLabel());
		GMLList graphicList = (GMLList) list.getValue("graphics");
		if (graphicList != null) {
			gmlNode.setGraphics(GMLGraphExtractor.extractNodeGraphics(graphicList));
		}
		return gmlNode;
	}

	static public GMLNodeGraphics extractNodeGraphics(GMLList list) {
		GMLNodeGraphics graphics = new GMLNodeGraphics();
		Number number = null;
		int x = 0, y = 0, w = 10, h = 10;

		number = (Number) list.getValue("x");
		if (number != null) {
			x = number.intValue();
		}
		number = (Number) list.getValue("y");
		if (number != null) {
			y = number.intValue();
		}
		number = (Number) list.getValue("w");
		if (number != null) {
			w = number.intValue();
		}
		number = (Number) list.getValue("h");
		if (number != null) {
			h = number.intValue();
		}
		GMLList centerList = (GMLList) list.getValue("center");
		if (centerList != null) {
			number = (Number) centerList.getValue("x");
			if (number != null) {
				x = number.intValue() - w / 2;
			}
			number = (Number) centerList.getValue("y");
			if (number != null) {
				y = number.intValue() - h / 2;
			}
		}
		graphics.setArea(new Area(new Rectangle(x, y, w, h)));

		graphics.setType((String) list.getValue("type"));
		graphics.setIcon((String) list.getValue("icon"));

		return graphics;
	}

	static public Properties extractNodeProperties(GMLList list) {
		return null;
	}

	static public GMLEdge extractEdge(GMLList list) {
		GMLEdge gmlEdge = new GMLEdge();
		gmlEdge.setSourceId((Integer) list.getValue("source"));
		gmlEdge.setTargetId((Integer) list.getValue("target"));
		if (VisidiaSettings.getInstance().getBoolean(VisidiaSettings.Constants.GML_EDGE_PROPS)){
			if(list.getValue("label") != null)
				gmlEdge.setLabel(((String)list.getValue("label")).replace("\"", ""));
			if(list.getValue("weight") != null)
				gmlEdge.setWeight((Double)list.getValue("weight"));
		}
		return gmlEdge;
	}

}
