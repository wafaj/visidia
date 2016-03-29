package visidia.gui.window.dialog.preferences;

import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;

import visidia.misc.VisidiaSettings;

// TODO: Auto-generated Javadoc
/**
 * PreferencesPanel is the abstract base class for panels displayed in ViSiDiA preferences dialog.
 */
public abstract class PreferencesPanel extends JPanel implements PropertyChangeListener, ActionListener {

	private static final long serialVersionUID = 8333818148829297725L;

	/** The settings. */
	protected VisidiaSettings settings = null;

	/**
	 * Instantiates a new preferences panel.
	 */
	protected PreferencesPanel() {
		super();
		this.settings = VisidiaSettings.getInstance();		
	}

	/**
	 * Reset to default.
	 */
	public abstract void resetToDefault();
	
	/**
	 * Adds the integer input line.
	 * 
	 * @param parent the parent
	 * @param label the label
	 * @param textField the text field
	 * @param value the value
	 */
	protected void addIntegerInputLine(JPanel parent, String label, JFormattedTextField textField, int value) {
		JPanel tmp = new JPanel(new GridLayout(1, 2, 40, 10));

		JLabel l1 = new JLabel(label);
		l1.setAlignmentX(LEFT_ALIGNMENT);
		tmp.add(l1);

		textField.setValue(value);
		textField.setColumns(10);
		textField.addPropertyChangeListener("value", this);
		textField.setAlignmentX(LEFT_ALIGNMENT);
		tmp.add(textField);

		parent.add(tmp);
	}

	/**
	 * Adds the boolean input line.
	 * 
	 * @param parent the parent
	 * @param label the label
	 * @param checkBox the check box
	 * @param selected the selected
	 */
	protected void addBooleanInputLine(JPanel parent, String label, JCheckBox checkBox, boolean selected) {
		JPanel tmp = new JPanel(new GridLayout(1, 2, 40, 10));

		JLabel l1 = new JLabel(label);
		l1.setAlignmentX(LEFT_ALIGNMENT);
		tmp.add(l1);

		checkBox.setSelected(selected);
		checkBox.addActionListener(this);
		checkBox.setAlignmentX(LEFT_ALIGNMENT);
		tmp.add(checkBox);

		parent.add(tmp);
	}
	
	/**
	 * Adds the label button line.
	 * 
	 * @param parent the parent
	 * @param label the label
	 * @param button the button
	 */
	protected void addLabelButtonLine(JPanel parent, String label, JButton button) {
		JPanel tmp = new JPanel(new GridLayout(1, 2, 40, 10));

		JLabel l1 = new JLabel(label);
		l1.setAlignmentX(LEFT_ALIGNMENT);
		tmp.add(l1);

		button.addActionListener(this);
		button.setAlignmentX(LEFT_ALIGNMENT);
		tmp.add(button);

		parent.add(tmp);
	}

}
