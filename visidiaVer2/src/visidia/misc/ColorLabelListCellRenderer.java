package visidia.misc;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;

/**
 * This class manages the rendering of a cell in a JList.
 */
public class ColorLabelListCellRenderer extends JPanel implements ListCellRenderer {

	private static final long serialVersionUID = 2422957033090725760L;

	/** The text label. */
	private JLabel textLabel = null;

	/** The color label. */
	private JLabel colorLabel = null;

	/**
	 * Instantiates a new cell renderer.
	 */
	public ColorLabelListCellRenderer() {
		setLayout(new GridLayout(0, 2, 50, 10));

		textLabel = new JLabel();
		textLabel.setAlignmentX(LEFT_ALIGNMENT);
		add(textLabel);

		colorLabel = new JLabel();
		colorLabel.setAlignmentX(LEFT_ALIGNMENT);
		colorLabel.setOpaque(true);
		add(colorLabel);
	}

	/* (non-Javadoc)
	 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
	 */
	public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		ColorLabel c = (ColorLabel) value;
		if (c == null) return this;

		if (isSelected) {
			setBackground(list.getSelectionBackground());
			setForeground(list.getSelectionForeground());
		} else {
			setBackground(list.getBackground());
			setForeground(list.getForeground());
		}

		textLabel.setText(c.getLabel());
		colorLabel.setBackground(c.getColor());

		setEnabled(list.isEnabled());
		setFont(list.getFont());
		setOpaque(true);
		return this;
	}
	
}
