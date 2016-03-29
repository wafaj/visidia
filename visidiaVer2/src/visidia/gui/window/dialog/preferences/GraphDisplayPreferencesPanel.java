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
public class GraphDisplayPreferencesPanel extends PreferencesPanel {

	private static final long serialVersionUID = 1769068287553430915L;

	/** The show vertex label check box. */
	private JCheckBox showVertexLabelCheckBox = new JCheckBox();

	/** The show edge label and weight check box. */
	private JCheckBox showEdgeLabelAndWeightCheckBox = new JCheckBox();

	/** The show weight check box. */
	private JCheckBox showWeightCheckBox = new JCheckBox();
	
	/** The show displayed properties check box.*/
	private JCheckBox showDisplayedPropertiesCheckBox = new JCheckBox();

	/** The weight number of decimals text field. */
	private JFormattedTextField weightNbDecimalsTextField = new JFormattedTextField();
	
	/** The show vertex label. */
	private boolean showVertexLabel;

	/** The show edge label and weight. */
	private boolean showEdgeLabelAndWeight;

	/** The show displayed properties. */
	private boolean showDisplayedProperties;
	
	/** The show weight. */
	private boolean showWeight;

	/** The weight number of decimals. */
	private int weightNbDecimals;
	

	/** The graph panel. */
	private GraphPanel graphPanel;

	/**
	 * Instantiates a new graph display preferences panel.
	 * 
	 * @param graphPanel the graph panel
	 */
	public GraphDisplayPreferencesPanel(GraphPanel graphPanel) {
		super();
		this.graphPanel = graphPanel;

		showVertexLabel = settings.getBoolean(VisidiaSettings.Constants.SHOW_VERTEX_LABEL);
		showEdgeLabelAndWeight = settings.getBoolean(VisidiaSettings.Constants.SHOW_EDGE_LABEL_AND_WEIGHT);
		showDisplayedProperties = settings.getBoolean(VisidiaSettings.Constants.SHOW_DISPLAYED_PROPS);
		showWeight = settings.getBoolean(VisidiaSettings.Constants.SHOW_WEIGHT);
		weightNbDecimals = settings.getInt(VisidiaSettings.Constants.WEIGHT_NB_DECIMALS);
		createGUI();
	}

	/* (non-Javadoc)
	 * @see visidia.gui.window.dialog.preferences.PreferencesPanel#resetToDefault()
	 */
	@Override
	public void resetToDefault() {
		showVertexLabel = settings.getDefaultBoolean(VisidiaSettings.Constants.SHOW_VERTEX_LABEL);
		showVertexLabelCheckBox.setSelected(showVertexLabel);
		settings.set(VisidiaSettings.Constants.SHOW_VERTEX_LABEL, new Boolean(showVertexLabel));

		showEdgeLabelAndWeight = settings.getDefaultBoolean(VisidiaSettings.Constants.SHOW_EDGE_LABEL_AND_WEIGHT);
		showEdgeLabelAndWeightCheckBox.setSelected(showEdgeLabelAndWeight);
		settings.set(VisidiaSettings.Constants.SHOW_EDGE_LABEL_AND_WEIGHT, new Boolean(showEdgeLabelAndWeight));

		showDisplayedProperties = settings.getDefaultBoolean(VisidiaSettings.Constants.SHOW_DISPLAYED_PROPS);
		showDisplayedPropertiesCheckBox.setSelected(showDisplayedProperties);
		settings.set(VisidiaSettings.Constants.SHOW_DISPLAYED_PROPS, new Boolean(showDisplayedProperties));
		
		showWeight = settings.getDefaultBoolean(VisidiaSettings.Constants.SHOW_WEIGHT);
		showWeightCheckBox.setSelected(showWeight);
		settings.set(VisidiaSettings.Constants.SHOW_WEIGHT, new Boolean(showWeight));

		weightNbDecimals = settings.getDefaultInt(VisidiaSettings.Constants.WEIGHT_NB_DECIMALS);
		weightNbDecimalsTextField.setText(String.valueOf(weightNbDecimals));
		settings.set(VisidiaSettings.Constants.WEIGHT_NB_DECIMALS, new Integer(weightNbDecimals));
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent e) {
		Object source = e.getSource();
		if (source.equals(weightNbDecimalsTextField)) {
			weightNbDecimals = ((Number) weightNbDecimalsTextField.getValue()).intValue();
			settings.set(VisidiaSettings.Constants.WEIGHT_NB_DECIMALS, new Integer(weightNbDecimals));
		}

		if (graphPanel != null) graphPanel.repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		
		if (source.equals(showVertexLabelCheckBox)) {
			showVertexLabel = showVertexLabelCheckBox.isSelected();
			settings.set(VisidiaSettings.Constants.SHOW_VERTEX_LABEL, new Boolean(showVertexLabel));
		} else if (source.equals(showEdgeLabelAndWeightCheckBox)) {
			showEdgeLabelAndWeight = showEdgeLabelAndWeightCheckBox.isSelected();
			settings.set(VisidiaSettings.Constants.SHOW_EDGE_LABEL_AND_WEIGHT, new Boolean(showEdgeLabelAndWeight));
		} else if (source.equals(showWeightCheckBox)) {
			showWeight = showWeightCheckBox.isSelected();
			settings.set(VisidiaSettings.Constants.SHOW_WEIGHT, new Boolean(showWeight));
		} else if (source.equals(showDisplayedPropertiesCheckBox)) {
			showDisplayedProperties = showDisplayedPropertiesCheckBox.isSelected();
			settings.set(VisidiaSettings.Constants.SHOW_DISPLAYED_PROPS, new Boolean(showDisplayedProperties));
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
		
		addBooleanInputLine(this, "Show vertex label", showVertexLabelCheckBox, showVertexLabel);
		add(Box.createVerticalGlue());
		addBooleanInputLine(this, "Show edge label and weight", showEdgeLabelAndWeightCheckBox, showEdgeLabelAndWeight);
		add(Box.createVerticalGlue());
		addBooleanInputLine(this, "Show edges whose weight equals 1.0", showWeightCheckBox, showWeight);
		add(Box.createVerticalGlue());
		addIntegerInputLine(this, "Number of decimals for weight display:", weightNbDecimalsTextField, weightNbDecimals);
		add(Box.createVerticalGlue());
		addBooleanInputLine(this, "Show displayed properties", showDisplayedPropertiesCheckBox, showDisplayedProperties);
		add(Box.createVerticalGlue());
	}

}
