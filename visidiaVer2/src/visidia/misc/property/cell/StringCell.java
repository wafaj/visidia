package visidia.misc.property.cell;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

// TODO: Auto-generated Javadoc
/**
 * StringCell is the class for editing and rendering in a JTable a cell containing a single string.
 */
public class StringCell extends CellViewer {

	private static final long serialVersionUID = 3743537154809669017L;

	/** The Constant defaultValue. */
	private final static String defaultValue = "undefined";

	/** The text field. */
	private JTextField textField;

	/** The row. */
	private int row;

	/** The column. */
	private int column;

	/** The table. */
	private JTable table;

	/**
	 * Instantiates a new string cell.
	 */
	public StringCell() {
		this(StringCell.defaultValue);
	}

	/**
	 * Instantiates a new string cell.
	 * 
	 * @param oValue the o value
	 */
	public StringCell(String oValue) {
		this(oValue, false); 
	}

	/**
	 * Instantiates a new string cell.
	 * 
	 * @param value the value
	 * @param isEditable the editable status
	 */
	public StringCell(String value, boolean isEditable) {
		super(value, isEditable);

		textField = new JTextField();
		if (!isEditable) textField.setBorder(null);
		textField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent arg0) {
				table.getModel().setValueAt(textField.getText(), row, column);
			}
		});
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable oTable, Object oValue, boolean arg2, int iRow, int iColumn) {
		table = oTable;
		row = iRow;
		column = iColumn;
		textField.setText((String) oValue);
		return textField;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return textField.getText();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object oValue, boolean arg2, boolean arg3, int arg4, int arg5) {
		textField.setText((String) oValue);
		return textField;
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
