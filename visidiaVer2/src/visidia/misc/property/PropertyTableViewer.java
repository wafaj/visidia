package visidia.misc.property;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.EventListenerList;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import visidia.VisidiaMain;
import visidia.misc.ClassIdentifier;
import visidia.misc.ColorLabel;
import visidia.misc.ImageHandler;
import visidia.misc.colorpalette.ColorPaletteManager;
import visidia.misc.property.cell.BooleanCell;
import visidia.misc.property.cell.CellViewer;
import visidia.misc.property.cell.ClassIdentifierCell;
import visidia.misc.property.cell.ColorLabelCell;
import visidia.misc.property.cell.DoubleCell;
import visidia.misc.property.cell.IntegerCell;
import visidia.misc.property.cell.JButtonCell;
import visidia.misc.property.cell.StringCell;

// TODO: Auto-generated Javadoc
/**
 * The Class PropertyTableViewer.
 */
public class PropertyTableViewer extends JTable {

	private static final long serialVersionUID = -1187840111652966685L;

	/** The model. */
	PropertyTableModel model;

	/** The properties. */
	PropertyTable properties;

	/** The delete icon. */
	ImageIcon deleteIcon = ImageHandler.getInstance().createImageIcon("/delete.png");

	/** The color palette manager. */
	private ColorPaletteManager colorPaletteManager = ColorPaletteManager.getInstance();

	/** The color labels. */
	ColorLabel[] colorLabels;

	/** The listeners. */
	private final EventListenerList listeners = new EventListenerList();

	/**
	 * Instantiates a new property table viewer.
	 * 
	 * @param properties the properties
	 */
	public PropertyTableViewer(final PropertyTable properties) {
		super();
		this.properties = properties;

		Enumeration<Object> colorKeys = colorPaletteManager.getStandardKeys();
		colorLabels = new ColorLabel[colorPaletteManager.size()];
		for (int i = 0; colorKeys.hasMoreElements(); ++i) {
			String key = colorKeys.nextElement().toString();
			colorLabels[colorPaletteManager.size()-i-1] = new ColorLabel(colorPaletteManager.getColor(key), key);
		}

		model = new PropertyTableModel();
		setModel(model);
		configureTable();

		model.addTableModelListener(new TableModelListener(){
			public void tableChanged(TableModelEvent e) {
				int firstRow = e.getFirstRow();
				int column = e.getColumn();
				// Update: cell (firstRow,column) was changed
				// column should equal 2 (property value) or 3 (displayable status)
				if (e.getType() == TableModelEvent.UPDATE && firstRow != TableModelEvent.HEADER_ROW && column != TableModelEvent.ALL_COLUMNS) {
					Object value = model.getValueAt(firstRow, 2);
					String key = (String) model.getValueAt(firstRow, 1);
					Object objDisplayable = model.getValueAt(firstRow, 3);
					if (objDisplayable instanceof Boolean) {
						boolean displayed = ((Boolean) objDisplayable).booleanValue();
						int tag = displayed ? VisidiaProperty.Tag.DISPLAYED_PROPERTY : VisidiaProperty.Tag.DISPLAYABLE_PROPERTY;
						VisidiaProperty prop = new VisidiaProperty(key, value, tag);
						properties.setVisidiaProperty(prop);
						notifyListeners();
					} else {// objDisplayable is an instance of String if unused
						int tag = properties.isPersistentProperty(key) ? VisidiaProperty.Tag.PERSISTENT_PROPERTY : VisidiaProperty.Tag.USER_PROPERTY;
						VisidiaProperty prop = new VisidiaProperty(key, value, tag);
						properties.setVisidiaProperty(prop);
						notifyListeners();
					}
				}
			}
		});

		Set<Object> keys = properties.getPropertyKeys();
		Iterator<Object> itr = keys.iterator();
		while (itr.hasNext()) {
			Object key = itr.next();
			VisidiaProperty prop = properties.getVisidiaProperty(key);
			Object value = prop.getValue();
			CellViewer cell = buildValueCell(value.getClass());
			cell.setValue(value);
			ImageIcon icon = properties.isPersistentProperty(key) ? null : deleteIcon; // null icon ==> this property cannot be removed
			boolean displayable = false;
			boolean displayed = false;
			if (prop.getTag() == VisidiaProperty.Tag.DISPLAYED_PROPERTY) {
				displayable = true;
				displayed = true;
			} else if (prop.getTag() == VisidiaProperty.Tag.DISPLAYABLE_PROPERTY) {
				displayable = true;
			} 
			addPropertyRow(icon, (String) key, cell, displayable, displayed);
		}
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#getCellEditor(int, int)
	 */
	public TableCellEditor getCellEditor(int row, int col) {
		TableCellEditor editor = null;
		CellViewer cellData = model.getCellAt(row, col);
		if (cellData != null) editor = cellData.getEditor();		
		if (editor != null) return editor;
		return super.getCellEditor(row,col);
	}

	/* (non-Javadoc)
	 * @see javax.swing.JTable#getCellRenderer(int, int)
	 */
	public TableCellRenderer getCellRenderer(int row, int col) {
		TableCellRenderer renderer = null;
		CellViewer cellData = model.getCellAt(row, col);
		if (cellData != null) renderer = cellData.getRenderer();		
		if (renderer != null) return renderer;
		return super.getCellRenderer(row,col);
	}

	/**
	 * Adds the listener.
	 * 
	 * @param listener the listener
	 */
	public void addListener(PropertyTableViewerChangeListener listener) {
		listeners.add(PropertyTableViewerChangeListener.class, listener);
	}

	/**
	 * Removes the listener.
	 * 
	 * @param listener the listener
	 */
	public void removeListener(PropertyTableViewerChangeListener listener) {
		listeners.remove(PropertyTableViewerChangeListener.class, listener);
	}

	/**
	 * Gets the listeners.
	 * 
	 * @return the listeners
	 */
	public PropertyTableViewerChangeListener[] getListeners() {
		return listeners.getListeners(PropertyTableViewerChangeListener.class);
	}

	/**
	 * Notify listeners.
	 */
	private void notifyListeners()  {
		PropertyTableViewerChangeListener[] listeners = getListeners();
		for (PropertyTableViewerChangeListener listener : listeners) listener.updated();
	}
	
	/**
	 * Builds the value cell.
	 * 
	 * @param eType the e type
	 * 
	 * @return the cell viewer
	 */
	private CellViewer buildValueCell(Class<?> eType) {
		CellViewer cell;	

		// TODO: add new value types here
		if (eType == Boolean.class) cell = new BooleanCell();
		else if (eType == Double.class) cell = new DoubleCell();
		else if (eType == Integer.class) cell = new IntegerCell();
		else if (eType == ColorLabel.class) cell = new ColorLabelCell(colorLabels);
		else if (eType == ClassIdentifier.class) cell = new ClassIdentifierCell();
		else cell = new StringCell(); // default (String)

		if (eType != ClassIdentifier.class) cell.setEditable(true);
		return cell;
	}

	/**
	 * Adds the property row.
	 * 
	 * @param icon the icon
	 * @param sName the s name
	 * @param valueCell the value cell
	 */
	private void addPropertyRow(ImageIcon icon, String sName, CellViewer valueCell, boolean displayable, boolean displayed) {
		int iRowIndex = model.getRowCount()-1;

		JButton button = new JButton(icon);
		button.setBorderPainted(false);
		if (icon != null) button.setToolTipText("Remove this property");
		JButtonCell buttonCell = new JButtonCell(button);

		Object[] data = new Object[model.getColumnCount()];
		data[0] = buttonCell;
		data[1] = new StringCell(sName);
		data[2] = valueCell;
		if (displayable) data[3] = new BooleanCell(displayed, true);
		else data[3] = new StringCell(""); // don't want to show unavailable checkboxes
		model.insertRow(iRowIndex, data);
	}

	/**
	 * Configure table.
	 */
	private void configureTable() {
		String[] colIds = { "", "Property", "Value", "Displayed" };
		model.setColumnIdentifiers(colIds);

		setIntercellSpacing(new Dimension(4, 2));
		getTableHeader().setReorderingAllowed(false);

		int[] colWidths = { 30, 150, 250, 100 };
		Dimension d = new Dimension();
		for (int j = 0; j < colWidths.length; j++) d.width += colWidths[j];
		d.height = 180;
		setPreferredScrollableViewportSize(d);

		TableColumnModel colModel = getColumnModel();
		for (int j = 0; j < colModel.getColumnCount(); j++) {
			TableColumn col = colModel.getColumn(j);
			col.setPreferredWidth(colWidths[j]);
		}

		// TODO: add new value types here
		Class<?>[] availableClasses = { String.class, Boolean.class, Double.class, Integer.class };
		final AddPropertyDialog dialog = new AddPropertyDialog(VisidiaMain.getParentFrame(), properties, availableClasses);

		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getClickCount() == 1) {
					Point p = e.getPoint();

					try {
						int row = convertRowIndexToModel(rowAtPoint(p));
						int column = columnAtPoint(p);
						if (row >= 0 && column == 0) {
							if (row == getRowCount() - 1) {
								dialog.setVisible(true);
								String name = dialog.getPropertyName();
								if (name != null) {
									Class <?> valueClass = dialog.getPropertyValueClass();
									boolean displayable = dialog.getPropertyDisplayable().booleanValue();
									CellViewer valueCell = buildValueCell(valueClass);
									Object value = valueCell.getValue();
									addPropertyRow(deleteIcon, name, valueCell, displayable, true);
									int tag = displayable ? VisidiaProperty.Tag.DISPLAYABLE_PROPERTY : VisidiaProperty.Tag.USER_PROPERTY;
									VisidiaProperty prop = new VisidiaProperty(name, value, tag);
									properties.setVisidiaProperty(prop);
									notifyListeners();
								}
							} else {
								CellViewer cellData = model.getCellAt(row, 0);
								JButton button = (JButton) cellData.getValue();
								if (button.getIcon() != null) {
									String key = (String) model.getValueAt(row, 1);
									model.removeRow(row);
									properties.removeProperty(key);
									notifyListeners();
								}
							}
						}
					} catch (IndexOutOfBoundsException exc) {
					}
				}
			}
		});

		JButton button = new JButton(ImageHandler.getInstance().createImageIcon("/add.png"));
		button.setBorderPainted(false);
		button.setToolTipText("Add a new property");
		JButtonCell buttonCell = new JButtonCell(button);

		Object[] data = new Object[model.getColumnCount()];
		data[0] = buttonCell;
		data[1] = new StringCell("");
		data[2] = new StringCell("");
		data[3] = new StringCell(""); // don't want to show unavailable checkboxes
		model.addRow(data);

		setShowGrid(false);
		setFillsViewportHeight(true);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setColumnSelectionAllowed(false);
		setRowSelectionAllowed(false);
		setRowHeight(24);
	}

}
