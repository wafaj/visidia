package visidia.misc.property;

import javax.swing.table.DefaultTableModel;

import visidia.misc.property.cell.CellViewer;

// TODO: Auto-generated Javadoc
/**
 * The Class PropertyTableModel.
 */
public class PropertyTableModel extends DefaultTableModel {

	private static final long serialVersionUID = -4247038197486056656L;

	/**
	 * Instantiates a new property table model.
	 */
	public PropertyTableModel() {
		super();
	}

	/**
	 * Gets the cell at position (row, column).
	 * 
	 * @param row the row
	 * @param column the column
	 * 
	 * @return the cell
	 */
	public CellViewer getCellAt(int row, int column) {
		Object cellData = super.getValueAt(row, column);
		if (cellData instanceof CellViewer) return (CellViewer) cellData;
		return null;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int row, int column) {
		Object cellData = super.getValueAt(row, column);
		if (cellData instanceof CellViewer) return ((CellViewer) cellData).getValue();
		return cellData;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#isCellEditable(int, int)
	 */
	public boolean isCellEditable(int row, int column) {
		Object cellData = super.getValueAt(row, column);
		if (cellData instanceof CellViewer) return ((CellViewer) cellData).getEditable();
		return super.isCellEditable(row, column);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.DefaultTableModel#setValueAt(java.lang.Object, int, int)
	 */
	public void setValueAt(Object oValue, int row, int column) {
		CellViewer test = (CellViewer) super.getValueAt(row, column);
		test.setValue(oValue);
		super.setValueAt(test, row, column);
		fireTableCellUpdated(row, column);
	}

}
