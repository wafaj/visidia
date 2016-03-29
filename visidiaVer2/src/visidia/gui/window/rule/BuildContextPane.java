package visidia.gui.window.rule;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Enumeration;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import visidia.gui.graphview.GraphItemView;
import visidia.gui.graphview.GraphView;
import visidia.gui.graphview.VertexView;
import visidia.rule.Star;

/**
 * A BuildContextPane is a panel where a context is composed. A popup menu proposes various actions (Add/Delete/Insert context) implemented by RulePane.
 */
class BuildContextPane extends JPanel {

	private static final long serialVersionUID = -4408408032870048549L;

	GraphView vg;

	Point center;

	VertexView sommetC;

	StarVisuPanel svp;

	JPopupMenu popup;

	boolean empty;

	Color backCl = new Color(153, 255, 153);

	// Circle stroke
	float[] dash = { 6.0f, 4.0f, 2.0f, 4.0f, 2.0f, 4.0f };

	BasicStroke dashS = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,	BasicStroke.JOIN_MITER, 10.0f, this.dash, 0.0f);

	/**
	 * If empty is true, a message "NoContext" is displayed and no context can
	 * be composed. Therefore that panel must be suppressed when the user wants
	 * one. "star" value can be null if the graph doesn't contains any vertex.
	 */
	public BuildContextPane(final ContextTabbedPaneControl tabbedPaneControl, boolean empty, Star star) {
		this.empty = empty;
		this.setBackground(RuleConstants.contextColor);
		this.center = new Point(RuleConstants.ctxt_left + RuleConstants.ray, RuleConstants.ctxt_top + RuleConstants.ray);

		this.vg = new GraphView();
		this.sommetC = this.vg.createVertexAndView(this.center.x, this.center.y);

		if (star != null) {
			StarGraphViewConvertor.star2StarVueGraphe(star, this.vg, this.sommetC);
		}

		if (!empty) {
			this.svp = new StarVisuPanel(this.vg, RuleConstants.ray, this.center, this, false);
			if (star != null) {
				this.svp.reorganizeVertex();
			}
		}

		JMenuItem deleteContextMenuItem = new JMenuItem("Delete context");
		JMenuItem addContextMenuItem = new JMenuItem("Add context");
		JMenuItem insertContextMenuItem = new JMenuItem("Insert context");
		JMenuItem reorganizeVertexMenuItem = new JMenuItem("Reorganize vertexes");

		if (empty) {
			deleteContextMenuItem.setEnabled(false);
			reorganizeVertexMenuItem.setEnabled(false);
		}

		deleteContextMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Enumeration v_enum = BuildContextPane.this.vg.getGraphItems(); v_enum.hasMoreElements();) {
					GraphItemView f = (GraphItemView) v_enum.nextElement();
					if (f != BuildContextPane.this.sommetC) {
						BuildContextPane.this.vg.removeGraphItemAndView(f);
					}
				}
				BuildContextPane.this.repaint();
				tabbedPaneControl.deleteContext();
			}
		});
		addContextMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPaneControl.addNewContext();
			}
		});
		insertContextMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPaneControl.insertContext();
			}
		});
		reorganizeVertexMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BuildContextPane.this.svp.reorganizeVertex();
			}
		});
		this.popup = new JPopupMenu();
		this.popup.add(deleteContextMenuItem);
		this.popup.add(addContextMenuItem);
		this.popup.add(insertContextMenuItem);
		this.popup.addSeparator();
		this.popup.add(reorganizeVertexMenuItem);
		this.popup.setBorder(BorderFactory.createRaisedBevelBorder());

		this.addMouseListener(this.svp);
		this.addMouseMotionListener(this.svp);
		this.addMouseListener(new MouseAdapter() {
			private void callPopup(MouseEvent evt) {
				int x = evt.getX();
				int y = evt.getY();
				int modifiers = evt.getModifiers();
				if (modifiers == InputEvent.BUTTON3_MASK) {
					GraphItemView f = BuildContextPane.this.vg.getItemAtPosition(x, y);
					if (f == null)
						BuildContextPane.this.maybeShowPopup(evt);
				}
			}

			public void mousePressed(MouseEvent evt) {
				this.callPopup(evt);
			}

			public void mouseReleased(MouseEvent evt) {
				this.callPopup(evt);
			}
		});
	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			this.popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(RuleConstants.ctxt_left + RuleConstants.ray * 2
				+ RuleConstants.ctxt_right, RuleConstants.ctxt_top + RuleConstants.ray * 2
				+ RuleConstants.ctxt_bottom);
	}

	public Dimension getMinimumSize() {
		return this.getPreferredSize();
	}

	public Dimension getMaxmimumSize() {
		return this.getPreferredSize();
	}

	public boolean isEmpty() {
		return this.empty;
	}

	public Star getStar() {
		if (this.isEmpty()) {
			return null;
		} else {
			return StarGraphViewConvertor.starVueGraphe2Star(this.vg, this.sommetC);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		if (this.empty) {
			g.drawString("No context", this.center.x - RuleConstants.ray / 2,
					this.center.y);
		} else {
			// Degree 0 of the circle
			g.drawLine(this.center.x, this.center.y - RuleConstants.ray - 2,
					this.center.x, this.center.y - RuleConstants.ray + 2);

			// Circle
			Stroke tmp = ((Graphics2D) g).getStroke();
			((Graphics2D) g).setStroke(this.dashS);
			g.drawArc(this.center.x - RuleConstants.ray, this.center.y
					- RuleConstants.ray, RuleConstants.ray * 2, RuleConstants.ray * 2, 0, 360);
			((Graphics2D) g).setStroke(tmp);
			this.vg.drawGraph(g);
		}
	}

}