package visidia.gui.window.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import visidia.misc.TableSorter;
import visidia.stats.StatListener;
import visidia.stats.Statistics;

// TODO: Auto-generated Javadoc
/**
 * DialogVisidiaStat is the dialog for visualizing statistics.
 */
public class DialogVisidiaStat extends VisidiaDialog implements StatListener {

	private static final long serialVersionUID = 5939310488186804362L;

	/** The table model. */
	ReadOnlyHashTableModel tableModel;

	/** The result table. */
	private JTable resultTable;

	/** The scroll pane. */
	private JScrollPane scrollPane;

	/**
	 * Instantiates a new dialog for visidia statistics.
	 * 
	 * @param owner the owner
	 * @param stats the statistics
	 */
	public DialogVisidiaStat(Frame owner, Statistics stats) {
		super(owner, "Statistics", false); // non-modal dialog
		createGUI(stats);
		pack();
	}

	/**
	 * Creates the GUI.
	 * 
	 * @param stats the statistics
	 */
	private void createGUI(Statistics stats) {
		this.tableModel = new ReadOnlyHashTableModel(stats);
		TableSorter sorter = new TableSorter(this.tableModel);
		this.resultTable = new JTable(sorter);
		sorter.setTableHeader(this.resultTable.getTableHeader());
		this.scrollPane = new JScrollPane(this.resultTable);
		mainPanel.add(scrollPane, BorderLayout.CENTER);
	}

	/* (non-Javadoc)
	 * @see visidia.stats.StatListener#updatedStats(visidia.stats.Statistics)
	 */
	public void updatedStats(Statistics stats) {
		tableModel.setProperties((stats == null) ? null : stats.asHashTable());
	}


	/**
	 * The Class ReadOnlyHashTableModel.
	 */
	class ReadOnlyHashTableModel extends HashTableModel {

		/**
		 * Instantiates a new read only hash table model.
		 * 
		 * @param table the table
		 */
		public ReadOnlyHashTableModel(Statistics table) {
			super((table == null) ? null : table.asHashTable());
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
		 */
		public Class getColumnClass(int column) {
			if (column == 0) {
				return String.class;
			} else if (column == 1) {
				return Long.class;
			} else {
				throw new ArrayIndexOutOfBoundsException();
			}
		}

		/* (non-Javadoc)
		 * @see visidia.gui.window.dialog.DialogVisidiaStat.HashTableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int row, int col) {
			switch (col) {

			case 0:
				return this.keys.get(row).toString();
			case 1:
				return this.table.get(this.keys.get(row));

			}
			throw new IllegalArgumentException();
		}
	}


	/**
	 * The Class HashTableModel.
	 */
	public class HashTableModel extends AbstractTableModel {

		/** The table. */
		protected Map<Object, Long> table = null;

		/** The keys. */
		protected List<Object> keys = null;

		/**
		 * Instantiates a new hash table model.
		 * 
		 * @param table the table
		 */
		public HashTableModel(Map<Object, Long> table) {
			this.setProperties(table);
		}

		/**
		 * Sets the properties.
		 * 
		 * @param table the table
		 */
		public void setProperties(Map<Object, Long>  table) {

			if (table == null) {
				table = new Hashtable<Object, Long>();
			}

			this.table = table;
			this.keys = new Vector<Object>(table.keySet());
			this.fireTableDataChanged();
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getRowCount()
		 */
		public int getRowCount() {
			return this.keys.size();
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getColumnCount()
		 */
		public int getColumnCount() {
			return 2;
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.TableModel#getValueAt(int, int)
		 */
		public Object getValueAt(int row, int col) {
			switch (col) {

			case 0:
				return this.keys.get(row);
			case 1:
				return this.table.get(this.keys.get(row));

			}
			throw new IllegalArgumentException();
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#getColumnName(int)
		 */
		public String getColumnName(int col) {
			switch (col) {
			case 0:
				return "Keys";
			case 1:
				return "Values";
			}
			throw new IllegalArgumentException();
		}

		/* (non-Javadoc)
		 * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
		 */
		public boolean isCellEditable(int row, int col) {
			return false;
		}

	}

}
