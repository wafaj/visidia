package visidia.misc.property.cell;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import visidia.misc.ColorLabel;
import visidia.misc.ColorLabelListCellRenderer;

// TODO: Auto-generated Javadoc
/**
 * ColorLabelCell is the class for editing and rendering in a JTable a cell containing a couple (Label, Color).
 */
public class ColorLabelCell extends CellViewer {

	private static final long serialVersionUID = 6935655981733332000L;

	/** The combo box. */
	private JComboBox comboBox;

	/** The column. */
	private int column;

	/** The row. */
	private int row;

	/** The table. */
	private JTable table;

	/**
	 * Instantiates a new color label cell.
	 * 
	 * @param labels the labels
	 */
	public ColorLabelCell(ColorLabel[] labels) {
		this(null, false, labels);
	}

	/**
	 * Instantiates a new color label cell.
	 * 
	 * @param value the value
	 * @param isEditable the editable status
	 * @param labels the labels
	 */
	public ColorLabelCell(ColorLabel value, boolean isEditable, ColorLabel[] labels) {
		super(value, isEditable);
		comboBox = new JComboBox(labels);
		comboBox.setRenderer(new ColorLabelListCellRenderer());

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
	public Component getTableCellEditorComponent(JTable arg0, Object arg1, boolean arg2, int iRow, int iColumn) {
		comboBox.setSelectedItem(arg1);
		row = iRow;
		column = iColumn;
		table = arg0;
		return comboBox;
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
		return comboBox;
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
