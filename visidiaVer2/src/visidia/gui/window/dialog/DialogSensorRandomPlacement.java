package visidia.gui.window.dialog;

import java.awt.Dimension;
import java.awt.Frame;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;

// TODO: Auto-generated Javadoc
/**
 * This class is a modal dialog used to randomly place sensors on a support grid.
 */
public class DialogSensorRandomPlacement extends VisidiaDialog {

	private static final long serialVersionUID = -336757235107519272L;

	/** The number of sensors. */
	private int nbSensors = 10;

	/**
	 * Instantiates a new dialog sensor random placement.
	 * 
	 * @param owner the owner
	 */
	public DialogSensorRandomPlacement(Frame owner) {
		super(owner, "Sensor random placement", true); // modal dialog
		createGUI();
		pack();
	}

	/**
	 * Gets the number of sensors.
	 * 
	 * @return the number of sensors
	 */
	public int getNbSensors() {
		return nbSensors;
	}

	/**
	 * Creates the GUI.
	 */
	private void createGUI() {
		Box box = Box.createVerticalBox();
		box.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		JLabel label = new JLabel("Number of sensors to place:"); 
		label.setAlignmentX(Box.CENTER_ALIGNMENT);
		box.add(label);
		box.add(Box.createRigidArea(new Dimension(0, 5)));

		final JFormattedTextField textField = new JFormattedTextField();
		textField.setAlignmentX(Box.CENTER_ALIGNMENT);
		textField.setValue(nbSensors);
		textField.setMaximumSize(new Dimension(100, 30));
		textField.setPreferredSize(textField.getMaximumSize());
		textField.addPropertyChangeListener("value", new PropertyChangeListener(){
			public void propertyChange(PropertyChangeEvent evt) {
				nbSensors = ((Number) textField.getValue()).intValue();
			}
		});
		box.add(textField);
		mainPanel.add(box);
	}

}
