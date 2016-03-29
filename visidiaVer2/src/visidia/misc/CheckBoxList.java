package visidia.misc;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;

// TODO: Auto-generated Javadoc
/**
 * This class manages a list of checkboxes.
 */
public class CheckBoxList extends JList {
	
	private static final long serialVersionUID = 7418358476067338669L;

	/**
	 * Instantiates a new check box list.
	 */
	public CheckBoxList() {
		setCellRenderer(new CellRenderer());

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int index = locationToIndex(e.getPoint());

				if (index != -1) {
					JCheckBox checkbox = (JCheckBox)
					getModel().getElementAt(index);
					checkbox.setSelected(!checkbox.isSelected());
					repaint();
				}
			}
		});

		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}

	/**
	 * The Class CellRenderer.
	 */
	protected class CellRenderer implements ListCellRenderer {
		
		/* (non-Javadoc)
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			JCheckBox checkbox = (JCheckBox) value;
			//checkbox.setBackground(getBackground());
			//checkbox.setForeground(getForeground());
			checkbox.setEnabled(isEnabled());
			//checkbox.setFont(getFont());
			checkbox.setFocusPainted(false);
			checkbox.setBorderPainted(true);
			return checkbox;
		}
	}
}
