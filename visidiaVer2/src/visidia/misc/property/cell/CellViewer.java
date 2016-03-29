package visidia.misc.property.cell;

import javax.swing.AbstractCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

// TODO: Auto-generated Javadoc
/**
 * CellViewer is the abstract base class for editing and rendering cells in a JTable.
 */
public abstract class CellViewer extends AbstractCellEditor implements TableCellEditor, TableCellRenderer {

	private static final long serialVersionUID = -1411277421532501177L;

	/** The value. */
	private Object value;

	/** The editable status. */
	private boolean isEditable;

	/**
	 * Instantiates a new cell viewer.
	 * 
	 * @param oValue the o value
	 */
	public CellViewer(Object oValue) {
		this(oValue, false); 
	}

	/**
	 * Instantiates a new cell viewer.
	 * 
	 * @param value the value
	 * @param isEditable the editable status
	 */
	public CellViewer(Object value, boolean isEditable) {
		this.value = value;
		this.isEditable = isEditable;
	}

	/**
	 * Sets the value.
	 * 
	 * @param value the new value
	 */
	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Gets the value.
	 * 
	 * @return the value
	 */
	public Object getValue() {
		return value;
	}

	/**
	 * Sets the editable status.
	 * 
	 * @param isEditable the new editable status
	 */
	public void setEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	/**
	 * Gets the editable status.
	 * 
	 * @return the editable status
	 */
	public boolean getEditable() {
		return isEditable;
	}

	/**
	 * Gets the editor.
	 * 
	 * @return the editor
	 */
	public abstract TableCellEditor getEditor();

	/**
	 * Gets the renderer.
	 * 
	 * @return the renderer
	 */
	public abstract TableCellRenderer getRenderer();

}
