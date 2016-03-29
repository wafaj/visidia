package visidia.gui.window.dialog.preferences;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JTabbedPane;

import visidia.gui.window.GraphPanel;
import visidia.gui.window.dialog.VisidiaDialog;

// TODO: Auto-generated Javadoc
/**
 * This class is a modal dialog used to display and set ViSiDiA preferences.
 */
public class DialogVisidiaPreferences extends VisidiaDialog {

	private static final long serialVersionUID = -1124493185976669374L;

	/** The graph panel. */
	private GraphPanel graphPanel = null;

	/**
	 * Instantiates a new dialog for visidia preferences.
	 * 
	 * @param owner the owner
	 * @param graphPanel the graph panel
	 */
	public DialogVisidiaPreferences(Frame owner, GraphPanel graphPanel) {
		super(owner, "ViSiDiA Preferences", true); // modal dialog
		this.graphPanel = graphPanel;
		createGUI();
		pack();
	}

	/**
	 * Creates the GUI.
	 */
	private void createGUI() {
		final JTabbedPane tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Display", null, new GraphDisplayPreferencesPanel(graphPanel), "Graph display preferences");
		tabbedPane.addTab("Graph", null, new GraphPreferencesPanel(graphPanel), "Graph preferences");
		tabbedPane.addTab("Sensor", null, new SensorPreferencesPanel(graphPanel), "Sensor preferences");
		tabbedPane.addTab("Color palette", null, new ColorPalettePreferencesPanel(graphPanel), "Color palette preferences");
		
		mainPanel.add(tabbedPane, BorderLayout.CENTER);

		// Button to reset to default
		JButton buttonReset = new JButton("Reset to default");
		buttonReset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				PreferencesPanel panel = (PreferencesPanel) tabbedPane.getSelectedComponent();
				if (panel != null) panel.resetToDefault();
			}
		});
		buttonBox.add(buttonReset);
		buttonBox.add(Box.createHorizontalGlue());
	}

}
