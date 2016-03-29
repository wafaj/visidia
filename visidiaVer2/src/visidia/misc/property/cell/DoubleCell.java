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
 * DoubleCell is the class for editing and rendering in a JTable a cell containing a double.
 */
public class DoubleCell extends CellViewer {

	private static final long serialVersionUID = -7440583672326564342L;

	/** The Constant defaultValue. */
	private final static Double defaultValue = 0.d;

	/** The panel. */
	private JPanel panel;

	/** The row. */
	private int row;

	/** The column. */
	private int column;

	/** The table. */
	private JTable table; 

	/** The d decimal. */
	private Double dDecimal;

	/** The d min. */
	private Double dMin;

	/** The d max. */
	private Double dMax;

	/** The d value. */
	private Double dValue;

	/** The spinner. */
	private JSpinner spinner;

	/**
	 * Instantiates a new double cell.
	 */
	public DoubleCell() {
		this(DoubleCell.defaultValue);
	}

	/**
	 * Instantiates a new double cell.
	 * 
	 * @param value the value
	 */
	public DoubleCell(Double value) {
		this(value, false);
	}

	/**
	 * Instantiates a new double cell.
	 * 
	 * @param value the value
	 * @param isEditable the is editable
	 */
	public DoubleCell(Double value, boolean isEditable) {
		this(value, isEditable, -1e10d, 1e10d, 0.0d, 0.1d); 
	}

	/**
	 * Instantiates a new double cell.
	 * 
	 * @param value the value
	 * @param isEditable the editable status
	 * @param dMin the d min
	 * @param dMax the d max
	 * @param idValue the id value
	 * @param dDecimal the d decimal
	 */
	public DoubleCell(Double value, boolean isEditable, Double dMin, Double dMax, Double idValue, Double dDecimal) {
		super(value, isEditable);

		this.dMin = dMin;
		this.dMax = dMax;
		this.dValue = idValue;
		this.dDecimal = dDecimal;
		panel = new JPanel();
		panel.setLayout(new BorderLayout());
		SpinnerNumberModel oModel = new SpinnerNumberModel(dValue, dMin, dMax, dDecimal);
		spinner = new JSpinner(oModel);
		((JFormattedTextField) spinner.getEditor().getComponent(0)).setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(spinner, BorderLayout.CENTER);
		spinner.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				if (spinner.getValue() != null && table != null) {
					dValue = (Double) spinner.getValue();
					table.getModel().setValueAt(spinner.getValue(), row, column);
				}
			}
		});
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable oTable, Object oValue, boolean arg2, int iRow, int iColumn) {
		try {
			dValue = Double.valueOf(oValue.toString());
		} catch(NumberFormatException e) {
			dValue = 0.0d;
		}
		row = iRow;
		column = iColumn;
		table = oTable;
		setSpinnerModel();
		return panel;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return dValue;
	}

	/**
	 * Sets the spinner model.
	 */
	private void setSpinnerModel() {
		spinner.setModel(new SpinnerNumberModel(dValue, dMin, dMax, dDecimal));
		((JFormattedTextField) spinner.getEditor().getComponent(0)).setHorizontalAlignment(SwingConstants.LEFT);
	}

	/**
	 * Sets the decimal.
	 * 
	 * @param dDecimal the new decimal
	 */
	public void setDecimal(Double dDecimal) {
		this.dDecimal = dDecimal;
		setSpinnerModel();
	}

	/**
	 * Sets the min.
	 * 
	 * @param dMin the new min
	 */
	public void setMin(Double dMin) {
		this.dMin = dMin;
		setSpinnerModel();
	}

	/**
	 * Sets the max.
	 * 
	 * @param dMax the new max
	 */
	public void setMax(Double dMax) {
		this.dMax = dMax;
		setSpinnerModel();
	}

	/**
	 * Sets the value.
	 * 
	 * @param dValue the new value
	 */
	public void setValue(Double dValue) {
		this.dValue = dValue;
		setSpinnerModel();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object oValue, boolean arg2, boolean arg3, int arg4, int arg5) {
		try {
			spinner.setValue(Double.valueOf(oValue.toString()));
		} catch(NumberFormatException e) {
			spinner.setValue(0.0d);
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
