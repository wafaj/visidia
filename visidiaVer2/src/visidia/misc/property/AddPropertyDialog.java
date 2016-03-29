package visidia.misc.property;

import java.awt.Frame;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * This class allows the user to add its own properties to the graph.
 */
class AddPropertyDialog extends JDialog implements PropertyChangeListener {
	
	private static final long serialVersionUID = -4789681039437015174L;
	
	private String propertyName = null;
	private Class<?> propertyValueClass = null;
	private Boolean propertyDisplayable = null;
	private JTextField textFieldName;
	private JComboBox comboBoxValueClass;
	private JCheckBox checkBoxDisplay;

	private JOptionPane optionPane;

	private String btnString1 = "Enter";
	private String btnString2 = "Cancel";

	PropertyTable properties;
	Class<?>[] availableClasses;

	/**
	 * Returns null if the typed string was invalid;
	 * otherwise, returns the string as the user entered it.
	 */
	public String getPropertyName() {
		return propertyName;
	}

	/**
	 * Returns the property value class.
	 */
	public Class<?> getPropertyValueClass() {
		return propertyValueClass;
	}

	/**
	 * Gets the property displayable status.
	 *
	 * @return the property displayable status
	 */
	public Boolean getPropertyDisplayable() {
		return propertyDisplayable;
	}
	
	/** Creates the reusable dialog. */
	public AddPropertyDialog(Frame aFrame, PropertyTable properties, Class<?>[] availableClasses) {
		super(aFrame, true);
		this.properties = properties;
		this.availableClasses = availableClasses;

		setTitle("New property");

		textFieldName = new JTextField(10);
		String[] entries = new String[availableClasses.length];
		for (int i = 0; i < availableClasses.length; ++i) entries[i] = availableClasses[i].getSimpleName();
		comboBoxValueClass = new JComboBox(entries);
		checkBoxDisplay = new JCheckBox("displayable property", false);

		//Create an array of the text and components to be displayed.
		String msgString1 = "Please enter a name for the new property:";
		String msgString2 = "(You cannot enter a name already used)";
		String msgString3 = "Please select a type for this property value:";
		String msgString4 = "(You will have to define the property value in the property table)";
		String msgString5 = "Please tick the following box if you want the property to be displayed on graph view.";
		Object[] array = {msgString1, msgString2, "\n", textFieldName, "\n", "\n", msgString3, msgString4, "\n", comboBoxValueClass, "\n", "\n", msgString5, checkBoxDisplay};

		//Create an array specifying the number of dialog buttons and their text.
		Object[] options = {btnString1, btnString2};

		//Create the JOptionPane.
		optionPane = new JOptionPane(array,	JOptionPane.QUESTION_MESSAGE, JOptionPane.YES_NO_OPTION, null, options, options[0]);

		//Make this dialog display it.
		setContentPane(optionPane);

		//Handle window closing correctly.
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				// Instead of directly closing the window, we're going to change the JOptionPane's value property.
				optionPane.setValue(new Integer(JOptionPane.CLOSED_OPTION));
			}
		});

		//Ensure the text field always gets the first focus.
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent ce) {
				textFieldName.requestFocusInWindow();
			}
		});

		//Register an event handler that reacts to option pane state changes.
		optionPane.addPropertyChangeListener(this);
		
		pack();
	}

	/** This method reacts to state changes in the option pane. */
	public void propertyChange(PropertyChangeEvent e) {
		String prop = e.getPropertyName();

		if (isVisible() && (e.getSource() == optionPane)
				&& (JOptionPane.VALUE_PROPERTY.equals(prop) ||	JOptionPane.INPUT_VALUE_PROPERTY.equals(prop))) {
			Object value = optionPane.getValue();

			if (value == JOptionPane.UNINITIALIZED_VALUE) {
				//ignore reset
				return;
			}

			//Reset the JOptionPane's value.
			//If you don't do this, then if the user
			//presses the same button next time, no
			//property change event will be fired.
			optionPane.setValue(JOptionPane.UNINITIALIZED_VALUE);
			
			if (btnString1.equals(value)) {		        
				propertyName = textFieldName.getText();
		        int index = comboBoxValueClass.getSelectedIndex();
				propertyValueClass = availableClasses[index];
				propertyDisplayable = checkBoxDisplay.isSelected();
				String errorMessage = null;
				if (propertyName.isEmpty() || properties.containsElement(propertyName)) {
					errorMessage = propertyName.isEmpty()
						? "Sorry, you cannot use an empty name.\n" + "Please try again."
						: "Sorry, this property name is already used.\n" + "Please enter another name.";
				}
				
				if (errorMessage != null) {
					//text was invalid
					textFieldName.selectAll();
					JOptionPane.showMessageDialog(AddPropertyDialog.this, errorMessage, "Property name", JOptionPane.ERROR_MESSAGE);
					propertyName = null;
					textFieldName.requestFocusInWindow();
				} else {
					//we're done; clear and dismiss the dialog
					clearAndHide();
				}
			} else { //user closed dialog or clicked cancel
				propertyName = null;
				clearAndHide();
			}
		}
	}

	/** This method clears the dialog and hides it. */
	public void clearAndHide() {
		textFieldName.setText(null);
		checkBoxDisplay.setSelected(false);
		setVisible(false);
	}
	
}
