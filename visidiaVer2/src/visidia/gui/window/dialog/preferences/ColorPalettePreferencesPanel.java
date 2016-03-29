package visidia.gui.window.dialog.preferences;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import visidia.gui.window.GraphPanel;
import visidia.misc.ColorLabel;
import visidia.misc.ColorLabelListCellRenderer;
import visidia.misc.VisidiaSettings;
import visidia.misc.colorpalette.ColorPalette;
import visidia.misc.colorpalette.ColorPaletteManager;
import visidia.misc.colorpalette.CustomColorPalette;
import visidia.misc.colorpalette.NewCustomColorPalette;

// TODO: Auto-generated Javadoc
/**
 * ColorPalettePreferencesPanel is the panel to set color palette preferences.
 */
public class ColorPalettePreferencesPanel extends PreferencesPanel implements ListSelectionListener, MouseListener{

	private static final long serialVersionUID = 8688385984446002406L;

	private static final String DEFAULT_KEY = "A";

	private String key = DEFAULT_KEY;

	/** The generate button. */
	JButton generateButton = new JButton("Change Color:");

	/** The generate text field. */
	JFormattedTextField generateTextField = new JFormattedTextField();

	/** The color palette manager. */
	private ColorPaletteManager colorPaletteManager = ColorPaletteManager.getInstance();

	/** The color palette. */
	private ColorPalette colorPalette;

	/** The custom color check box. */
	private JCheckBox colorCustomCheckBox = new JCheckBox();
	private boolean colorCustom;

	private JList customPaletteList = new JList();

	private GraphPanel graphPanel;

	/**
	 * Instantiates a new color palette preferences panel.
	 */
	public ColorPalettePreferencesPanel(GraphPanel graphPanel) {
		super();
		this.graphPanel = graphPanel;
		colorPalette = (NewCustomColorPalette) settings.getObject(VisidiaSettings.Constants.CUSTOM_COLOR_PALETTE);
		customPaletteList.setListData(buildListDataCustom(ColorPaletteManager.getInstance().getCustomKeys()));
		colorCustom =settings.getBoolean(VisidiaSettings.Constants.USE_CUSTOM_COLOR_PALETTE);
		createGUI();
	}

	/* (non-Javadoc)
	 * @see visidia.gui.window.dialog.preferences.PreferencesPanel#resetToDefault()
	 */
	@Override
	public void resetToDefault() {
		generateTextField.setText(DEFAULT_KEY);
		colorPalette = (NewCustomColorPalette) settings.getDefaultObject(VisidiaSettings.Constants.CUSTOM_COLOR_PALETTE);
		settings.set(VisidiaSettings.Constants.CUSTOM_COLOR_PALETTE, colorPalette);
		colorPaletteManager.setCustomPalette(colorPalette);
		customPaletteList.setListData(buildListDataCustom(colorPalette.keys()));
		colorCustom = settings.getDefaultBoolean(VisidiaSettings.Constants.USE_CUSTOM_COLOR_PALETTE);
		colorCustomCheckBox.setSelected(colorCustom);
		settings.set(VisidiaSettings.Constants.USE_CUSTOM_COLOR_PALETTE, new Boolean(colorCustom));
		this.repaint();
		if (graphPanel != null) graphPanel.repaint();
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent e) {
		Object source = e.getSource();
		if (source.equals(generateTextField)) {
			key = (String) (generateTextField.getValue());
		}		
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(generateButton)) {
			Color color = JColorChooser.showDialog(null,"Change Color Label",null);
			if (color != null){
				((NewCustomColorPalette)colorPalette).setColor(key,color);
				settings.set(VisidiaSettings.Constants.CUSTOM_COLOR_PALETTE, colorPalette);
				colorPaletteManager.setCustomPalette(colorPalette);
				customPaletteList.setListData(buildListDataCustom(colorPalette.keys()));
				this.repaint();
			}
		} else if(source.equals(colorCustomCheckBox)){
			colorCustom = colorCustomCheckBox.isSelected();
			settings.set(VisidiaSettings.Constants.USE_CUSTOM_COLOR_PALETTE, colorCustom);
		}
		if (graphPanel != null) graphPanel.repaint();
	}

	/**
	 * Creates the GUI.
	 */
	private void createGUI() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10,10,10,10),
				BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Sensor preferences"),
						BorderFactory.createEmptyBorder(10,10,10,10))));

		Box hBox = Box.createHorizontalBox();

		// panel for standard color palette
		JPanel standardPanel = new JPanel();
		standardPanel.setLayout(new BoxLayout(standardPanel, BoxLayout.PAGE_AXIS));
		placeList(new JList(buildListDataStandard( ColorPaletteManager.getInstance().getStandardKeys())), "Standard palette:", standardPanel);
		hBox.add(standardPanel);

		// panel for custom color palette
		JPanel customPanel = new JPanel();
		customPanel.setLayout(new BoxLayout(customPanel, BoxLayout.PAGE_AXIS));
		customPaletteList.setListData(buildListDataCustom( ColorPaletteManager.getInstance().getCustomKeys()));
		placeList(customPaletteList, "Custom palette:", customPanel);
		customPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		JPanel generatePanel = new JPanel();
		generatePanel.setLayout(new BoxLayout(generatePanel, BoxLayout.LINE_AXIS));
		generateButton.addActionListener(this);
		generatePanel.add(generateButton);
		generateTextField.setValue(key);
		generateTextField.setMaximumSize(new Dimension(50, 30));
		generateTextField.setPreferredSize(generateTextField.getMaximumSize());
		generateTextField.addPropertyChangeListener("value", this);
		generatePanel.add(generateTextField);
		generatePanel.add(new JLabel(" colors"));
		customPanel.add(generatePanel);
		customPanel.add(Box.createVerticalGlue());
		addBooleanInputLine(customPanel, "Use Custom Color Palette", colorCustomCheckBox, colorCustom);
		hBox.add(customPanel);

		add(hBox);
	}

	/**
	 * Builds the list data.
	 * 
	 * @param colorKeys the color keys
	 * 
	 * @return the object[]
	 */
	private Object[] buildListDataStandard(Enumeration<Object> colorKeys) {
		ColorLabel[] colorLabels = new ColorLabel[colorPaletteManager.standardSize()];
		for (int i = 0; colorKeys.hasMoreElements(); ++i) {
			String key = colorKeys.nextElement().toString();
			colorLabels[colorPaletteManager.size()-i-1] = new ColorLabel(colorPaletteManager.getStandardColor(key), key);
		}

		return colorLabels;
	}

	/**
	 * Builds the list data.
	 * 
	 * @param colorKeys the color keys
	 * 
	 * @return the object[]
	 */
	private Object[] buildListDataCustom(Enumeration<Object> colorKeys) {
		ColorLabel[] colorLabels = new ColorLabel[colorPaletteManager.customSize()];
		for (int i = 0; colorKeys.hasMoreElements(); ++i) {
			String key = colorKeys.nextElement().toString();
			colorLabels[colorPaletteManager.size()-i-1] = new ColorLabel(colorPaletteManager.getCustomColor(key), key);
		}

		return colorLabels;
	}

	/**
	 * Place list.
	 * 
	 * @param list the list
	 * @param title the title
	 * @param panel the panel
	 */
	private void placeList(JList list, String title, JPanel panel) {
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setLayoutOrientation(JList.VERTICAL);
		list.setCellRenderer(new ColorLabelListCellRenderer());

		//list.addListSelectionListener(this);
		list.addMouseListener(this);

		JScrollPane scrollPane = new JScrollPane(list);
		scrollPane.setBounds(list.getBounds());

		panel.add(new JLabel(title));
		panel.add(Box.createRigidArea(new Dimension(0, 10)));
		panel.add(scrollPane);
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		Object source = e.getSource();
		if(source!= null && source.equals(customPaletteList)){
			key = ((ColorLabel)customPaletteList.getSelectedValue()).getLabel();
			generateTextField.setValue(key);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		Object source = e.getSource();
		if(e.getClickCount() == 2 && source.equals(customPaletteList)){
			key = ((ColorLabel)customPaletteList.getSelectedValue()).getLabel();
			generateTextField.setValue(key);
			Color color = JColorChooser.showDialog(null,"test",null);
			if (color != null){
				((NewCustomColorPalette)colorPalette).setColor(key,color);
				settings.set(VisidiaSettings.Constants.CUSTOM_COLOR_PALETTE, colorPalette);
				colorPaletteManager.setCustomPalette(colorPalette);
				customPaletteList.setListData(buildListDataCustom(colorPalette.keys()));
				this.repaint();
				if (graphPanel != null) graphPanel.repaint();
			}
		}else if(e.getClickCount() == 1 && source.equals(customPaletteList)){
			key = ((ColorLabel)customPaletteList.getSelectedValue()).getLabel();
			generateTextField.setValue(key);

			this.repaint();
		}
	
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}