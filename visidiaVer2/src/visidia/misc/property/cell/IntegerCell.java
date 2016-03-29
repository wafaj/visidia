package visidia.misc.property.cell;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

// TODO: Auto-generated Javadoc
/**
 * IntegerCell is the class for editing and rendering in a JTable a cell containing an integer.
 */
public class IntegerCell extends CellViewer {

	private static final long serialVersionUID = -843141260852073171L;

	/** The Constant defaultValue. */
	private final static Integer defaultValue = 0;

	/** The panel. */
	private JPanel panel;

	/** The row. */
	private int row;

	/** The column. */
	private int column;

	/** The o table. */
	private JTable oTable; 

	/** The spinner. */
	private JSpinner spinner;

	/**
	 * Instantiates a new integer cell.
	 */
	public IntegerCell() {
		this(IntegerCell.defaultValue);
	}

	/**
	 * Instantiates a new integer cell.
	 * 
	 * @param oValue the o value
	 */
	public IntegerCell(Integer oValue) {
		this(oValue, false); 
	}

	/**
	 * Instantiates a new integer cell.
	 * 
	 * @param value the value
	 * @param isEditable the editable status
	 */
	public IntegerCell(Integer value, boolean isEditable) {
		super(value, isEditable);

		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		SpinnerNumberModel oModel = new SpinnerNumberModel();
		spinner = new JSpinner(oModel);
		((JFormattedTextField) spinner.getEditor().getComponent(0)).setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(spinner, BorderLayout.CENTER);
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (oTable != null && spinner.getValue() != null) {
					oTable.getModel().setValueAt(spinner.getValue(), row, column);
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable oTable, Object oValue, boolean arg2, int iRow, int iColumn) {
		try {
			spinner.setValue(Integer.decode(oValue.toString()));
		} catch (NumberFormatException e) {
			spinner.setValue(0);
		}
		this.row = iRow;
		this.column = iColumn;
		this.oTable = oTable;
		return panel;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		if (spinner.getValue() != null) {
			return spinner.getValue();
		} else {
			return new Integer(0);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object oValue, boolean arg2, boolean arg3, int arg4, int arg5) {
		try {
			spinner.setValue(Integer.decode(oValue.toString()));
		} catch(NumberFormatException e) {
			spinner.setValue(0);
		}
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
