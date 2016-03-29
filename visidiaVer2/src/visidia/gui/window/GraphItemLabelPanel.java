package visidia.gui.window;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import visidia.gui.graphview.EdgeView;
import visidia.gui.graphview.GraphItemView;
import visidia.gui.graphview.VertexView;
import visidia.misc.colorpalette.ColorPaletteManager;

/**
 * A VertexLabel displays the information relative to a vertex.
 */
public class GraphItemLabelPanel extends JPanel implements ListSelectionListener {

	private static final long serialVersionUID = -1478452282694968266L;

	/** The list. */
	JList list;

	/** The labels. */
	String[] labels = { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };

	/** The color palette manager. */
	private ColorPaletteManager colorPaletteManager = ColorPaletteManager.getInstance();

	/** The parent. */
	JPanel parent;
	
	/** The vertex. */
	GraphItemView graphItem;

	/**
	 * Instantiates a new vertex label.
	 * 
	 * @param parent the parent
	 * @param graphItem the graph item
	 */
	public GraphItemLabelPanel(JPanel parent, GraphItemView graphItem) {
		this.parent = parent;
		this.graphItem = graphItem;
		JScrollPane listeAvecAscenseur;
		this.list = new JList(labels);

		this.list.addListSelectionListener(this);
		this.list.setCellRenderer(new CellRenderer());

		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		listeAvecAscenseur = new JScrollPane(this.list);

		listeAvecAscenseur.setPreferredSize(new Dimension(200, 50));
		this.add(listeAvecAscenseur);

		this.list.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				char c = e.getKeyChar();
				if (((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z'))) {
					String strC = "" + c;
					strC = strC.toUpperCase();
					GraphItemLabelPanel.this.list.setSelectedValue(strC, true);
					GraphItemLabelPanel.this.list.repaint();
				}
			}
		});

		String s = "";
		if (graphItem instanceof VertexView) s = (String) ((VertexView) graphItem).getVertex().getLabel();
		else if (graphItem instanceof EdgeView) s = ((EdgeView) graphItem).getEdge().getLabel(); 
		this.list.setSelectedValue(s, true);
		this.setVisible(true);
	}

	/**
	 * That method must be call when the panel is visible, in order to give the
	 * focus to the list.
	 */
	public void requestFocus() {
		this.list.requestFocus();
	}

	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent evt) {
		String s = (String) ((JList) evt.getSource()).getSelectedValue();
		this.list.setSelectedValue(s, true);
		if (graphItem instanceof VertexView) ((VertexView) graphItem).getVertex().setLabel(s);
		else if (graphItem instanceof EdgeView) ((EdgeView) graphItem).getEdge().setLabel(s);
		//graphPanel.repaint();
		parent.repaint();
	}

	/**
	 * This class manages the rendering of a cell in a JList.
	 */
	class CellRenderer extends JPanel implements ListCellRenderer {

		/** The text label. */
		private JLabel textLabel = null;

		/** The color label. */
		private JLabel colorLabel = null;

		/**
		 * Instantiates a new cell renderer.
		 */
		public CellRenderer() {
			setLayout(new GridLayout(0, 2, 50, 10));

			textLabel = new JLabel();
			textLabel.setAlignmentX(LEFT_ALIGNMENT);
			add(textLabel);

			colorLabel = new JLabel();
			colorLabel.setAlignmentX(LEFT_ALIGNMENT);
			colorLabel.setOpaque(true);
			add(colorLabel);
		}

		/* (non-Javadoc)
		 * @see javax.swing.ListCellRenderer#getListCellRendererComponent(javax.swing.JList, java.lang.Object, int, boolean, boolean)
		 */
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			String key = (String) value;

			if (isSelected) {
				setBackground(list.getSelectionBackground());
				setForeground(list.getSelectionForeground());
			} else {
				setBackground(list.getBackground());
				setForeground(list.getForeground());
			}

			textLabel.setText(key);
			colorLabel.setBackground(colorPaletteManager.getColor(key));

			setEnabled(list.isEnabled());
			setFont(list.getFont());
			setOpaque(true);
			return this;
		}
	}

}
