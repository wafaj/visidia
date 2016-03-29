package visidia.misc.property.cell;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import visidia.VisidiaMain;
import visidia.gui.window.dialog.ClassFileChooser;
import visidia.misc.ClassIdentifier;
import visidia.misc.FileHandler;
import visidia.misc.property.cell.handler.ClassIdentifierCellHandler;
import visidia.misc.property.cell.handler.SensorMoverCellHandler;

// TODO: Auto-generated Javadoc
/**
 * SensorMoverCell is the class to specify a sensor mover from sensor properties.
 */
public class ClassIdentifierCell extends CellViewer {

	private static final long serialVersionUID = 6376166499049607521L;

	/** The mover class id. */
	private ClassIdentifier classIdentifier;

	/** The button. */
	private JButton button;

	/** The column. */
	private int column;

	/** The row. */
	private int row;

	/** The table. */
	private JTable table;

	private ClassIdentifierCellHandler handler = null;
	
	/**
	 * Instantiates a new sensor mover cell.
	 */
	public ClassIdentifierCell() {
		this(ClassIdentifier.emptyClassId);
	}

	/**
	 * Instantiates a new sensor mover cell.
	 * 
	 * @param classId the class id
	 */
	private ClassIdentifierCell(ClassIdentifier classId) {
		super(classId, true);
		this.classIdentifier = classId;

		handler = new SensorMoverCellHandler();
		// TODO: add other handlers (set successors)
		
		button = new JButton();
		button.setText(classId.getClassName());
		button.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				while (handler != null && ! handler.matchType(((ClassIdentifier) getValue()).getInstanceType()))
					handler = handler.getSuccessor();
				
				if (handler == null) return;
				String path = handler.getPath();
				String rootDir = FileHandler.getRootDirectory(path);
				if (rootDir == null) return;

				ClassFileChooser fc = new ClassFileChooser(VisidiaMain.getParentFrame(), rootDir, path, handler.getLoaderType(), null);
				int returnVal = fc.showDialog();
				if (returnVal == JFileChooser.CANCEL_OPTION) return;
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					ClassIdentifier classId = fc.getSelectedObjectIdentifier();
					classId.setInstanceType(handler.getObjectType());
					if (classId == null) return;
					classIdentifier = classId;
					if (table != null)
						table.getModel().setValueAt(classIdentifier, row, column);

					button.setText(classIdentifier.getClassName());
				}
				
			}
		});
	}

	/* (non-Javadoc)
	 * @see visidia.misc.property.cell.CellViewer#setEditable(boolean)
	 */
	public void setEditable(boolean isEditable) {
		super.setEditable(true);
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellEditor#getTableCellEditorComponent(javax.swing.JTable, java.lang.Object, boolean, int, int)
	 */
	public Component getTableCellEditorComponent(JTable oTable, Object oValue, boolean arg2, int iRow, int iColumn) {
		button.setText(((ClassIdentifier) oValue).getClassName());
		row = iRow;
		column = iColumn;
		table = oTable;
		return button;
	}

	/* (non-Javadoc)
	 * @see javax.swing.CellEditor#getCellEditorValue()
	 */
	public Object getCellEditorValue() {
		return classIdentifier;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableCellRenderer#getTableCellRendererComponent(javax.swing.JTable, java.lang.Object, boolean, boolean, int, int)
	 */
	public Component getTableCellRendererComponent(JTable arg0, Object oValue, boolean arg2, boolean arg3, int arg4, int arg5) {
		button.setText(((ClassIdentifier) oValue).getClassName());
		return button;
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
