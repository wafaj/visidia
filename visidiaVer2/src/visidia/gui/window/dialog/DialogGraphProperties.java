package visidia.gui.window.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import visidia.gui.graphview.EdgeView;
import visidia.gui.graphview.GraphItemView;
import visidia.gui.graphview.VertexView;
import visidia.gui.window.GraphPanel;
import visidia.gui.window.GraphPanelSimulation;
import visidia.gui.window.Selection;
import visidia.gui.window.VisidiaPanel;
import visidia.misc.property.PropertyTable;
import visidia.misc.property.PropertyTableViewer;
import visidia.misc.property.PropertyTableViewerChangeListener;
import visidia.simulation.process.algorithm.Algorithm;

// TODO: Auto-generated Javadoc
/**
 * This class is a modal dialog used to display information relative to a graph.
 */
public class DialogGraphProperties extends VisidiaDialog {

	private static final long serialVersionUID = 5442203199299877925L;

	/**
	 * Instantiates a new dialog graph properties.
	 * 
	 * @param owner the owner
	 * @param graphPanel the graph panel
	 * @param selection the selection
	 */
	public DialogGraphProperties(Frame owner, GraphPanel graphPanel, Selection selection) {
		super(owner, "Graph properties", true); // modal dialog
		createGUI(graphPanel, selection);
		pack();
	}

	/**
	 * Creates the GUI.
	 * 
	 * @param graphPanel the graph panel
	 * @param selection the selection
	 */
	private void createGUI(GraphPanel graphPanel, Selection selection) {
		JPanel generalPanel = createGeneralDialogBox(graphPanel);
		JPanel selectionPanel = createSelectionDialogBox(graphPanel, selection);

		JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("General", null, generalPanel, "General information");
		tabbedPane.addTab("Selection", null, selectionPanel, "Information about selection");
		mainPanel.add(tabbedPane, BorderLayout.CENTER);		
	}

	/**
	 * Creates the selection dialog box.
	 * 
	 * @param graphPanel the graph panel
	 * @param selection the selection
	 * 
	 * @return the panel
	 */
	private JPanel createSelectionDialogBox(final GraphPanel graphPanel, Selection selection) {
		JPanel selectionPanel = new JPanel();
		selectionPanel.setLayout(new BoxLayout(selectionPanel, BoxLayout.PAGE_AXIS));
		selectionPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10,10,10,10),
				BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Information about selection"),
						BorderFactory.createEmptyBorder(10,10,10,10))));

		if (selection == null || selection.isEmpty()) {
			selectionPanel.add(new JLabel("empty"));
			return selectionPanel;
		}

		int nbVertices = 0;
		int nbEdges = 0;

		Enumeration<GraphItemView> items = selection.elements();
		while (items.hasMoreElements()) {
			GraphItemView item = items.nextElement();
			if (item instanceof VertexView) {
				nbVertices ++;
			} else if (item instanceof EdgeView) {
				nbEdges ++;
			}
		}

		if (nbVertices == 1 && nbEdges == 0) { // One vertex is selected
			addNonEditableLine(selectionPanel, "One vertex.", "");
			selectionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			VertexView vertex = (VertexView) selection.elements().nextElement();
			int vertexId = vertex.getVertex().getId();
			addNonEditableLine(selectionPanel, "Id: ", new Integer(vertexId).toString());						

			selectionPanel.add(Box.createRigidArea(new Dimension(0,10)));
			Point pos = vertex.getPosition();
			String s = "(" + new Integer(pos.x).toString() + ", " + new Integer(pos.x).toString() + ")";
			addNonEditableLine(selectionPanel, "Position: ", s);

			selectionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			addNonEditableLine(selectionPanel, "Degree: ", new Integer(vertex.getVertex().degree()).toString());						

			selectionPanel.add(Box.createRigidArea(new Dimension(0, 10)));

			String algoName = "none";
			if (graphPanel instanceof GraphPanelSimulation) {
				Algorithm algo = ((GraphPanelSimulation) graphPanel).getAlgorithm();
				if (algo != null) algoName = algo.getClass().getSimpleName();
			}
			addNonEditableLine(selectionPanel, "Algorithm: ", algoName);
		} else if (nbVertices == 0 && nbEdges == 1) { // One edge is selected
			addNonEditableLine(selectionPanel, "One edge.", "");
			selectionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			final EdgeView edge = (EdgeView) selection.elements().nextElement();
			final JLabel origLabel = addNonEditableLine(selectionPanel, "Origin vertex id: ", new Integer(edge.getOrigin().getVertex().getId()).toString());
			selectionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			final JLabel destLabel = addNonEditableLine(selectionPanel, "Destination vertex id: ", new Integer(edge.getDestination().getVertex().getId()).toString());

			JButton switchButton = new JButton("Switch origin and destination");
			switchButton.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					edge.switchOriginAndDestination();
					String text = origLabel.getText();
					origLabel.setText(destLabel.getText());
					destLabel.setText(text);
					graphPanel.repaint();
				}
			});
			selectionPanel.add(switchButton);
		} else { // Various items are selected
			addNonEditableLine(selectionPanel, "Nb vertices: ", new Integer(nbVertices).toString());
			selectionPanel.add(Box.createRigidArea(new Dimension(0, 10)));
			addNonEditableLine(selectionPanel, "Nb edges: ", new Integer(nbEdges).toString());

			selectionPanel.add(Box.createRigidArea(new Dimension(0,20)));	
		}

		selectionPanel.add(Box.createRigidArea(new Dimension(0,20)));

		if (nbVertices + nbEdges == 1) { // a single element (either a vertex or an edge) is selected
			PropertyTable properties = null;
			GraphItemView item = selection.elements().nextElement();
			properties = (nbVertices == 1 ? ((VertexView) item).getVertex() : ((EdgeView) item).getEdge());

			PropertyTableViewer viewer = new PropertyTableViewer(properties);
			viewer.addListener(new PropertyTableViewerChangeListener(){
				public void updated() {
					graphPanel.repaint();
				}
			});

			JScrollPane scrollPane = new JScrollPane(viewer);
			scrollPane.setBounds(viewer.getBounds());
			selectionPanel.add(scrollPane);
		}

		return selectionPanel;
	}

	/**
	 * Adds the non editable line.
	 * 
	 * @param parent the parent
	 * @param label the label
	 * @param data the data
	 * 
	 * @return the data JLabel
	 */
	private JLabel addNonEditableLine(JPanel parent, String label, String data) {
		JPanel tmp = new JPanel(new GridLayout(0, 2, 40, 10));

		JLabel l1 = new JLabel(label);
		l1.setAlignmentX(LEFT_ALIGNMENT);
		tmp.add(l1);

		JLabel l2 = new JLabel(data);
		l2.setAlignmentX(LEFT_ALIGNMENT);
		tmp.add(l2);

		parent.add(tmp);
		return l2;
	}

	/**
	 * Creates the general dialog box.
	 * 
	 * @param graphPanel the graph panel
	 * 
	 * @return the panel
	 */
	private JPanel createGeneralDialogBox(GraphPanel graphPanel) {
		JPanel generalPanel = new JPanel();
		generalPanel.setLayout(new BoxLayout(generalPanel, BoxLayout.PAGE_AXIS));
		generalPanel.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10,10,10,10),
				BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "General information"),
						BorderFactory.createEmptyBorder(10,10,10,10))));


		addNonEditableLine(generalPanel, "Nb vertices: ", new Integer(graphPanel.getGraphView().getNbVertices()).toString());
		generalPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		addNonEditableLine(generalPanel, "Nb edges: ", new Integer(graphPanel.getGraphView().getNbEdges()).toString());

		generalPanel.add(Box.createRigidArea(new Dimension(0,20)));

		addNonEditableLine(generalPanel, "Vertex shape: ", VisidiaPanel.getFactory().getVertexViewPrototype().getClass().getSimpleName());
		generalPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		addNonEditableLine(generalPanel, "Edge shape: ", VisidiaPanel.getFactory().getEdgeViewPrototype().getClass().getSimpleName());

		generalPanel.add(Box.createRigidArea(new Dimension(0, 20)));

		return generalPanel;
	}

}
