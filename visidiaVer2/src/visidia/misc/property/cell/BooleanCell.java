package visidia.misc.property.cell;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

// TODO: Auto-generated Javadoc
/**
 * BooleanCell is the class for editing and rendering in a JTable a cell containing a boolean.
 */
public class BooleanCell extends CellViewer {

	private static final long serialVersionUID = -374418615980119405L;

	/** The Constant defaultValue. */
	private final static Boolean defaultValue = true;

	/** The check box. */
	private JCheckBox checkBox;

	/** The column. */
	private int column;

	/** The row. */
	private int row;

	/** The table. */
	private JTable table;

	/**
	 * Instantiates a new boolean cell.
	 */
	public BooleanCell() {
		this(BooleanCell.defaultValue);
	}

	/**
	 * Instantiates a new boolean cell.
	 * 
	 * @param oValue the o value
	 */
	public BooleanCell(Boolean oValue) {
		this(oValue, false); 
	}

	/**
	 * Instantiates a new boolean cell.
	 * 
	 * @param value the value
	 * @param isEditable the editable status
	 */
	public BooleanCell(Boolean value, boolean isEditable) {
		super(value, isEditable);

		checkBox = new JCheckBox();
		checkBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				table.getModel().setValueAt(checkBox.isSelected(), row, column);
			}
		});
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable arg0, Object arg1, boolean arg2, int iRow, int iColumn) {
		checkBox.setSelected((Boolean) arg1);
		row = iRow;
		column = iColumn;
		table = arg0;
		return checkBox;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return checkBox.isSelected();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object oValue, boolean arg2, boolean arg3, int arg4, int arg5) {
		checkBox.setSelected((Boolean) oValue);
		return checkBox;
	}

	/* (non-Javadoc)
	 * @see visidia.misc.property.cell.CellViewer#getEditor()
	 */
	public TableCellEditor getEditor() {
		return this;
	}

	/* (non-Javadoc)
	 * @see visidia.misc.property.cell.CellViewer#getRenderer()
	 */
	public TableCellRenderer getRenderer() {
		return this;
	}

}