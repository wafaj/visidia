package visidia.gui.window.dialog.preferences;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import visidia.gui.window.GraphPanel;
import visidia.misc.ColorLabel;
import visidia.misc.ColorLabelListCellRenderer;
import visidia.misc.VisidiaSettings;
import visidia.misc.colorpalette.ColorPaletteManager;

// TODO: Auto-generated Javadoc
/**
 * GraphDisplayPreferencesPanel is the panel to set graph display preferences.
 */
public class GraphPreferencesPanel extends PreferencesPanel {

	private static final long serialVersionUID = 1769068287553430915L;

	/** The color label panel. */
	private ColorLabelPanel colorLabelPanel = new ColorLabelPanel();
	
	/** The directed graph check box. */
	private JCheckBox directedGraphCheckBox = new JCheckBox();

	/** The import GML edge label and weight check box. */
	private JCheckBox importEdgeLabelAndWeightCheckBox = new JCheckBox();

	/** The import GML Label check box. */
	private JCheckBox importGMLLabelCheckBox = new JCheckBox();
	
	/** The color label. */
	private ColorLabel colorLabel;
	
	/** The directed graph. */
	private boolean directedGraph;

	/** The import GML edge label and weight. */
	private boolean importGMLEdgeLabelAndWeight;
	
	/** The import GML Label. */
	private boolean importGMLLabel;

	/** The graph panel. */
	private GraphPanel graphPanel;

	/**
	 * Instantiates a new graph display preferences panel.
	 * 
	 * @param graphPanel the graph panel
	 */
	public GraphPreferencesPanel(GraphPanel graphPanel) {
		super();
		this.graphPanel = graphPanel;

		colorLabel = (ColorLabel) settings.getObject(VisidiaSettings.Constants.VERTEX_DEFAULT_LABEL);
		colorLabelPanel.setColorLabel(colorLabel);
		directedGraph = settings.getBoolean(VisidiaSettings.Constants.DIRECTED_GRAPH);
		importGMLEdgeLabelAndWeight = settings.getBoolean(VisidiaSettings.Constants.GML_EDGE_PROPS);
		importGMLLabel = settings.getBoolean(VisidiaSettings.Constants.GML_NODE_LABEL);
		createGUI();
	}

	/* (non-Javadoc)
	 * @see visidia.gui.window.dialog.preferences.PreferencesPanel#resetToDefault()
	 */
	@Override
	public void resetToDefault() {
		colorLabel = (ColorLabel) settings.getDefaultObject(VisidiaSettings.Constants.VERTEX_DEFAULT_LABEL);
		colorLabelPanel.setColorLabel(colorLabel);
		settings.set(VisidiaSettings.Constants.VERTEX_DEFAULT_LABEL, colorLabel);
		
		directedGraph = settings.getDefaultBoolean(VisidiaSettings.Constants.DIRECTED_GRAPH);
		directedGraphCheckBox.setSelected(directedGraph);
		settings.set(VisidiaSettings.Constants.DIRECTED_GRAPH, new Boolean(directedGraph));

		importGMLEdgeLabelAndWeight = settings.getDefaultBoolean(VisidiaSettings.Constants.GML_EDGE_PROPS);
		importEdgeLabelAndWeightCheckBox.setSelected(importGMLEdgeLabelAndWeight);
		settings.set(VisidiaSettings.Constants.GML_EDGE_PROPS, new Boolean(importGMLEdgeLabelAndWeight));

		importGMLLabel = settings.getDefaultBoolean(VisidiaSettings.Constants.GML_NODE_LABEL);
		importGMLLabelCheckBox.setSelected(importGMLLabel);
		settings.set(VisidiaSettings.Constants.GML_NODE_LABEL, new Boolean(importGMLLabel));
	}

	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source.equals(directedGraphCheckBox)) {
			directedGraph = directedGraphCheckBox.isSelected();
			settings.set(VisidiaSettings.Constants.DIRECTED_GRAPH, new Boolean(directedGraph));
		} else if (source.equals(importEdgeLabelAndWeightCheckBox)) {
			importGMLEdgeLabelAndWeight = importEdgeLabelAndWeightCheckBox.isSelected();
			settings.set(VisidiaSettings.Constants.GML_EDGE_PROPS, new Boolean(importGMLEdgeLabelAndWeight));
		} else if (source.equals(importGMLLabelCheckBox)) {
			importGMLLabel = importGMLLabelCheckBox.isSelected();
			settings.set(VisidiaSettings.Constants.GML_NODE_LABEL, new Boolean(importGMLLabel));
		}

		if (graphPanel != null) graphPanel.repaint();
	}

	/**
	 * Creates the GUI.
	 */
	private void createGUI() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10,10,10,10),
				BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Graph display preferences"),
						BorderFactory.createEmptyBorder(10,10,10,10))));

		add(Box.createVerticalGlue());
		colorLabelPanel.createGUI(this, "Default vertex label");		
		add(Box.createVerticalGlue());
		addBooleanInputLine(this, "Directed Graph", directedGraphCheckBox, directedGraph);
		add(Box.createVerticalGlue());
		addBooleanInputLine(this, "Import GML Vertex Label", importGMLLabelCheckBox, importGMLLabel);
		add(Box.createVerticalGlue());
		addBooleanInputLine(this, "Import GML Edges Properties (weight, Label)", importEdgeLabelAndWeightCheckBox, importGMLEdgeLabelAndWeight);
		add(Box.createVerticalGlue());		
	}

	@Override
	public void propertyChange(PropertyChangeEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}


class ColorLabelPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2209324327015047812L;
	private JComboBox comboBox;
	ColorLabel[] colorLabels;
	
	public ColorLabelPanel() {
		ColorPaletteManager colorPaletteManager = ColorPaletteManager.getInstance();
		Enumeration<Object> colorKeys = colorPaletteManager.getStandardKeys();
		colorLabels = new ColorLabel[colorPaletteManager.size()];
		for (int i = 0; colorKeys.hasMoreElements(); ++i) {
			String key = colorKeys.nextElement().toString();
			colorLabels[colorPaletteManager.size()-i-1] = new ColorLabel(colorPaletteManager.getColor(key), key);
		}
		comboBox = new JComboBox(colorLabels);
	}

	public void setColorLabel(ColorLabel colorLabel) {
		comboBox.setSelectedItem(colorLabel);
	}
	
	public void createGUI(JPanel parent, String message) {
		JPanel tmp = new JPanel(new GridLayout(1, 2, 40, 10));

		JLabel l1 = new JLabel(message);
		l1.setAlignmentX(LEFT_ALIGNMENT);
		tmp.add(l1);

		comboBox.setRenderer(new ColorLabelListCellRenderer());
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				ColorLabel item = (ColorLabel) comboBox.getSelectedItem();
				VisidiaSettings.getInstance().set(VisidiaSettings.Constants.VERTEX_DEFAULT_LABEL, item);
			}
		});
		//checkBox.addActionListener(this);
		comboBox.setAlignmentX(LEFT_ALIGNMENT);
		tmp.add(comboBox);

		parent.add(tmp);
	}

}
