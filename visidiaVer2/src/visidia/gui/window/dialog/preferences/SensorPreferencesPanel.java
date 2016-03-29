package visidia.gui.window.dialog.preferences;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;

import visidia.VisidiaMain;
import visidia.gui.window.GraphPanel;
import visidia.gui.window.dialog.ClassFileChooser;
import visidia.io.SensorMoverIO;
import visidia.misc.ClassIdentifier;
import visidia.misc.FileHandler;
import visidia.misc.VisidiaSettings;
import visidia.simulation.process.SensorMover;

// TODO: Auto-generated Javadoc
/**
 * SensorPreferencesPanel is the panel to set sensor preferences.
 */
public class SensorPreferencesPanel extends PreferencesPanel {

	private static final long serialVersionUID = 5233430567808529046L;

	/** The sensor global mover button. */
	private JButton sensorGlobalMoverButton = new JButton();

	/** The sensor communication distance text field. */
	private JFormattedTextField sensorCommunicationDistanceTextField = new JFormattedTextField();

	/** The text field for the number of sensors along x axis. */
	private JFormattedTextField sensorNbVerticesXTextField = new JFormattedTextField();

	/** The text field for the number of sensors along y axis. */
	private JFormattedTextField sensorNbVerticesYTextField = new JFormattedTextField();

	/** The text field for the gap (in pixels) between two sensors along x axis. */
	private JFormattedTextField sensorVertexGapXTextField = new JFormattedTextField();

	/** The text field for the gap (in pixels) between two sensors along y axis. */
	private JFormattedTextField sensorVertexGapYTextField = new JFormattedTextField();

	/** The sensor communication distance. */
	private int sensorCommunicationDistance;

	/** The number of vertices in regular grid along x axis. */
	private int sensorNbVerticesX;

	/** The number of vertices in regular grid along y axis. */
	private int sensorNbVerticesY;

	/** The gap (in pixels) between two sensors along x axis. */
	private int sensorVertexGapX;

	/** The gap (in pixels) between two sensors along y axis. */
	private int sensorVertexGapY;

	/** The sensor mover class name. */
	private String sensorMoverClassName;

	/** The graph panel. */
	private GraphPanel graphPanel;

	/**
	 * Instantiates a new sensor preferences panel.
	 * 
	 * @param graphPanel the graph panel
	 */
	SensorPreferencesPanel(GraphPanel graphPanel) {
		super();
		this.graphPanel = graphPanel;

		// get current/default values
		sensorCommunicationDistance = settings.getInt(VisidiaSettings.Constants.SENSOR_COMMUNICATION_DISTANCE);
		sensorNbVerticesX = settings.getInt(VisidiaSettings.Constants.SENSOR_NB_VERTICES_X);
		sensorNbVerticesY = settings.getInt(VisidiaSettings.Constants.SENSOR_NB_VERTICES_Y);
		sensorVertexGapX = settings.getInt(VisidiaSettings.Constants.SENSOR_VERTEX_GAP_X);
		sensorVertexGapY = settings.getInt(VisidiaSettings.Constants.SENSOR_VERTEX_GAP_Y);

		ClassIdentifier sensorMover = (ClassIdentifier) settings.getObject(VisidiaSettings.Constants.VISIDIA_GLOBAL_SENSOR_MOVER);
		sensorMoverClassName = (sensorMover == null ? "" : sensorMover.getClassName());

		createGUI();
	}

	/* (non-Javadoc)
	 * @see visidia.gui.window.dialog.preferences.PreferencesPanel#resetToDefault()
	 */
	@Override
	public void resetToDefault() {
		sensorCommunicationDistance = settings.getDefaultInt(VisidiaSettings.Constants.SENSOR_COMMUNICATION_DISTANCE);
		sensorCommunicationDistanceTextField.setText(String.valueOf(sensorCommunicationDistance));
		settings.set(VisidiaSettings.Constants.SENSOR_COMMUNICATION_DISTANCE, new Integer(sensorCommunicationDistance));

		sensorNbVerticesX = settings.getDefaultInt(VisidiaSettings.Constants.SENSOR_NB_VERTICES_X);
		sensorNbVerticesXTextField.setText(String.valueOf(sensorNbVerticesX));
		settings.set(VisidiaSettings.Constants.SENSOR_NB_VERTICES_X, new Integer(sensorNbVerticesX));

		sensorNbVerticesY = settings.getDefaultInt(VisidiaSettings.Constants.SENSOR_NB_VERTICES_Y);
		sensorNbVerticesYTextField.setText(String.valueOf(sensorNbVerticesY));
		settings.set(VisidiaSettings.Constants.SENSOR_NB_VERTICES_Y, new Integer(sensorNbVerticesY));

		sensorVertexGapX = settings.getDefaultInt(VisidiaSettings.Constants.SENSOR_VERTEX_GAP_X);
		sensorVertexGapXTextField.setText(String.valueOf(sensorVertexGapX));
		settings.set(VisidiaSettings.Constants.SENSOR_VERTEX_GAP_X, new Integer(sensorVertexGapX));

		sensorVertexGapY = settings.getDefaultInt(VisidiaSettings.Constants.SENSOR_VERTEX_GAP_Y);
		sensorVertexGapYTextField.setText(String.valueOf(sensorVertexGapY));
		settings.set(VisidiaSettings.Constants.SENSOR_VERTEX_GAP_Y, new Integer(sensorVertexGapY));

		ClassIdentifier sensorMover = (ClassIdentifier) settings.getDefaultObject(VisidiaSettings.Constants.VISIDIA_GLOBAL_SENSOR_MOVER);
		sensorMoverClassName = (sensorMover == null ? "" : sensorMover.getClassName());
		sensorGlobalMoverButton.setText(sensorMoverClassName);
		settings.set(VisidiaSettings.Constants.VISIDIA_GLOBAL_SENSOR_MOVER, sensorMover);
	}

	/* (non-Javadoc)
	 * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
	 */
	public void propertyChange(PropertyChangeEvent e) {
		Object source = e.getSource();
		if (source.equals(sensorCommunicationDistanceTextField)) {
			sensorCommunicationDistance = ((Number) sensorCommunicationDistanceTextField.getValue()).intValue();
			settings.set(VisidiaSettings.Constants.SENSOR_COMMUNICATION_DISTANCE, new Integer(sensorCommunicationDistance));
		} else if (source.equals(sensorNbVerticesXTextField)) {
			sensorNbVerticesX = ((Number) sensorNbVerticesXTextField.getValue()).intValue();
			settings.set(VisidiaSettings.Constants.SENSOR_NB_VERTICES_X, new Integer(sensorNbVerticesX));
		} else if (source.equals(sensorNbVerticesYTextField)) {
			sensorNbVerticesY = ((Number) sensorNbVerticesYTextField.getValue()).intValue();
			settings.set(VisidiaSettings.Constants.SENSOR_NB_VERTICES_Y, new Integer(sensorNbVerticesY));
		} else if (source.equals(sensorVertexGapXTextField)) {
			sensorVertexGapX = ((Number) sensorVertexGapXTextField.getValue()).intValue();
			settings.set(VisidiaSettings.Constants.SENSOR_VERTEX_GAP_X, new Integer(sensorVertexGapX));
		} else if (source.equals(sensorVertexGapYTextField)) {
			sensorVertexGapY = ((Number) sensorVertexGapYTextField.getValue()).intValue();
			settings.set(VisidiaSettings.Constants.SENSOR_VERTEX_GAP_Y, new Integer(sensorVertexGapY));
		}

		if (graphPanel != null) graphPanel.repaint();
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source.equals(sensorGlobalMoverButton)) {
			String visidiaSensorMoverPath = settings.getString(VisidiaSettings.Constants.VISIDIA_SENSOR_MOVER_PATH);
			String rootDir = FileHandler.getRootDirectory(visidiaSensorMoverPath);
			if (rootDir == null) return;

			ClassFileChooser fc = new ClassFileChooser(VisidiaMain.getParentFrame(), rootDir, visidiaSensorMoverPath, SensorMoverIO.class, null);
			int returnVal = fc.showDialog();
			if (returnVal == JFileChooser.CANCEL_OPTION) return;
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				SensorMover mover = (SensorMover) fc.getSelectedObject();
				ClassIdentifier classId = fc.getSelectedObjectIdentifier();
				classId.setInstanceType(SensorMover.class);
				if (mover == null || classId == null) return;

				settings.set(VisidiaSettings.Constants.VISIDIA_GLOBAL_SENSOR_MOVER, classId);
				sensorGlobalMoverButton.setText(classId.getClassName());
			}
		}
	}

	/**
	 * Creates the GUI.
	 */
	private void createGUI() {
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(10,10,10,10),
				BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Sensor preferences"),
						BorderFactory.createEmptyBorder(10,10,10,10))));

		addLabelButtonLine(this, "Global mover:", sensorGlobalMoverButton);
		sensorGlobalMoverButton.setText(sensorMoverClassName);
		add(Box.createRigidArea(new Dimension(0, 20)));

		addIntegerInputLine(this, "Grid number of vertices in X:", sensorNbVerticesXTextField, sensorNbVerticesX);
		add(Box.createRigidArea(new Dimension(0, 10)));
		addIntegerInputLine(this, "Grid number of vertices in Y:", sensorNbVerticesYTextField, sensorNbVerticesY);
		add(Box.createRigidArea(new Dimension(0, 20)));

		addIntegerInputLine(this, "Grid gap (in pixels) between two vertices in X:", sensorVertexGapXTextField, sensorVertexGapX);
		add(Box.createRigidArea(new Dimension(0, 10)));
		addIntegerInputLine(this, "Grid gap (in pixels) between two vertices in Y:", sensorVertexGapYTextField, sensorVertexGapY);
		add(Box.createRigidArea(new Dimension(0, 20)));

		addIntegerInputLine(this, "Communication distance:", sensorCommunicationDistanceTextField, sensorCommunicationDistance);
		add(Box.createRigidArea(new Dimension(0, 20)));
	}

}
