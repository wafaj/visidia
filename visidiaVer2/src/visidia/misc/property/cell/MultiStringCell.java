package visidia.misc.property.cell;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Vector;

import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

// TODO: Auto-generated Javadoc
/**
 * MultiStringCell is the class for editing and rendering in a JTable a cell containing several strings.
 */
public class MultiStringCell extends CellViewer {

	private static final long serialVersionUID = 4937542348448156363L;

	/** The combo box. */
	private JComboBox comboBox;

	/** The panel. */
	private JPanel panel;

	/** The row. */
	private int row;

	/** The column. */
	private int column;

	/** The table. */
	private JTable table;

	/** The o values. */
	Vector<String> oValues;

	/**
	 * Instantiates a new multi string cell.
	 * 
	 * @param oValues the o values
	 */
	public MultiStringCell(Vector<String> oValues) {
		super(null);

		this.oValues = oValues;
		panel = new JPanel(new BorderLayout());
		comboBox = new JComboBox(oValues);
		panel.add(comboBox);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if (table != null) {
					table.getModel().setValueAt(comboBox.getSelectedItem(), row, column);
				}
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

		comboBox.setSelectedItem(oValue);
		return panel;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return comboBox.getSelectedItem();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object oValue, boolean arg2, boolean arg3, int arg4, int arg5) {
		comboBox.setSelectedItem(oValue);
		return panel;
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
