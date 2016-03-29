package visidia.misc.property.cell;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

// TODO: Auto-generated Javadoc
/**
 * JButtonCell is the class for rendering in a JTable a cell containing a JButton.
 */
public class JButtonCell extends CellViewer {

	private static final long serialVersionUID = 5883314673552466431L;

	/** The button. */
	private JButton button;

	/**
	 * Instantiates a new jbutton cell.
	 * 
	 * @param button the button
	 */
	public JButtonCell(JButton button) {
		super(button);
		this.button = button;
	}

	/* (non-Javadoc)
	 * @see visidia.misc.property.cell.CellViewer#setEditable(boolean)
	 */
	public void setEditable(boolean isEditable) {
		super.setEditable(false);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object oValue, boolean arg2, boolean arg3, int arg4, int arg5) {
		return button;
	}

	/* (non-Javadoc)
	 * @see visidia.misc.property.cell.CellViewer#getEditor()
	 */
	public TableCellEditor getEditor() {
		return null;
	}

	/* (non-Javadoc)
	 * @see visidia.misc.property.cell.CellViewer#getRenderer()
	 */
	public TableCellRenderer getRenderer() {
		return this;
	}

}
