package visidia.gui.window.rule;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Enumeration;
import java.util.NoSuchElementException;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import visidia.gui.graphview.EdgeView;
import visidia.gui.graphview.GraphItemView;
import visidia.gui.graphview.GraphView;
import visidia.gui.graphview.VertexView;
import visidia.gui.window.GraphItemLabelPanel;

/**
 * Visualizes and permits to compose a star as a GraphView. The parent must add
 * the instance to its MouseListener and MouseMotionListener
 */
public class StarVisuPanel implements MouseListener, MouseMotionListener {

	// Used for drag'n drop a vertex
	VertexView drag_n_drop_sommet = null;

	Point ancien_pos = null;

	// Used to change vertex label
	JPopupMenu vertexPopup;

	VertexView sommetC;

	GraphView vg;

	boolean isSimpleRule;

	int vertexNumber;

	int ray;

	Point center;

	JPanel parent;

	/**
	 * The center of the star is located at "center" ; if the star is from a
	 * simple rule, just two vertexes can compose the GraphView.
	 */
	public StarVisuPanel(GraphView vg, int ray, Point center, JPanel parent, boolean isSimpleRule) {
		this.isSimpleRule = isSimpleRule;
		this.vg = vg;
		this.ray = ray;
		this.center = center;
		this.parent = parent;
		this.sommetC = (VertexView) vg.getItemAtPosition(center.x, center.y);
		this.vertexPopup = new JPopupMenu();
		this.vertexPopup.setBorder(BorderFactory.createRaisedBevelBorder());
		this.vertexNumber = vg.getNbGraphItems();
		if (this.vertexNumber > 1) {
			// Edges must be deduted
			this.vertexNumber = this.vertexNumber - (this.vertexNumber / 2);
		}
	}

	// Returns SommetDessin s as s.getEtiquette().equals(id)
	private VertexView getVertex(String id) {
		for (Enumeration e = this.vg.getGraphItems(); e.hasMoreElements();) {
			GraphItemView f = (GraphItemView) e.nextElement();
			if (f instanceof VertexView) {
				String fId = Integer.toString(((VertexView) f).getVertex().getId());
				if (fId.equals(id)) {
					return (VertexView) f;
				}
			}
		}
		return null;
	}

	/**
	 * Places the vertexes (not the center one) in a regular way
	 */
	public void reorganizeVertex() {
		if (this.isSimpleRule) {
			VertexView s = this.getVertex("1");
			this.sommetC.setPosition(this.center.x, this.center.y);
			if (s != null) {
				s.setPosition(this.center.x + this.ray, this.center.y);
			}
		} else {
			double alpha = 0;
			double step = 2 * Math.PI / (this.vertexNumber - 1);
			for (int i = 1; i < this.vertexNumber; i++) {
				VertexView s = this.getVertex("" + i);
				int x = this.center.x + (int) (Math.sin(alpha) * this.ray);
				int y = this.center.y - (int) (Math.cos(alpha) * this.ray);
				s.setPosition(x, y);
				alpha += step;
			}
		}
		this.parent.repaint();
	}

	// Clockwise renumbering
	private void renumeberVertex() {
		double alpha = 0;
		VertexView s = null;
		int number = 1;
		this.sommetC.getVertex().setId(0);
		for (alpha = 0.0; alpha < 2 * Math.PI - 0.2; alpha += 0.2) {
			int x = this.center.x + (int) (Math.sin(alpha) * this.ray);
			int y = this.center.y - (int) (Math.cos(alpha) * this.ray);
			try {
				//VertexView s2 = (VertexView) this.vg.en_dessous(x, y, s);
				VertexView s2 = this.vg.getVertexAtPosition(x, y, s);
				if (s2 == null) throw new NoSuchElementException();
				// System.out.println ("Sommet " + s2
				// + " trouve a alpha " + alpha + " " + x + " " + y);
				s2.getVertex().setId(number++);
				s = s2;
			} catch (NoSuchElementException e) {
			}
		}
	}

	// Returns null if (x, y) is not near the circle or
	// returns the nearest point on the circle
	// If isSimpleRule == true, returns the point of the second vertex or null
	private Point getRoundedPosition(int x, int y) {
		if (this.isSimpleRule) {
			if ((x > this.center.x + this.ray - 25)
					&& (x < this.center.x + this.ray + 25)
					&& (y > this.center.y - 50) && (y < this.center.y + 50)) {
				return new Point(this.center.x + this.ray, this.center.y);
			} else {
				return null;
			}
		} else {
			double r2 = Math.sqrt(Math.pow(x - this.center.x, 2)
					+ Math.pow(y - this.center.y, 2));
			if (Math.abs(r2 - this.ray) > 10) {
				return null;
			}
			return new Point(
					(int) (this.center.x + (this.ray * (x - this.center.x))
							/ r2),
							(int) (this.center.y + (this.ray * (y - this.center.y))
									/ r2));
		}
	}

	/**
	 * Left and center buttons change the structure Right button change the
	 * associated data
	 */
	public void mousePressed(MouseEvent evt) {
		int x = evt.getX();
		int y = evt.getY();
		int modifiers = evt.getModifiers();

		// System.out.println (x + " " + y);

		// Left click
		if ((modifiers == InputEvent.BUTTON1_MASK)
				&& (evt.getClickCount() == 2)) {
			Point p = this.getRoundedPosition(x, y);
			// System.out.println (p);

			if (p != null) {
				// Delete the vertex
				try {
					VertexView s = (VertexView) this.vg.getItemAtPosition(x, y);
					if (s == null) throw new NoSuchElementException();
					EdgeView a = this.vg.getEdgeView(s.getVertex(), sommetC.getVertex());
					if (a == null) throw new NoSuchElementException();

					this.vg.removeGraphItemAndView(s);
					this.vg.removeGraphItemAndView(a);
					this.vertexNumber--;					
				} catch (NoSuchElementException e) {
					VertexView s2 = this.vg.createVertexAndView(p.x, p.y);
					this.vg.createEdgeAndView((VertexView) this.vg.getItemAtPosition(this.center.x, this.center.y), s2, false);
					this.vertexNumber++;
				}

				this.renumeberVertex();
				this.parent.repaint();
			}
		} else if ((modifiers == InputEvent.BUTTON2_MASK)
				|| ((modifiers == (InputEvent.BUTTON1_MASK | InputEvent.ALT_MASK)) && !this.isSimpleRule)) {
			// Center click or left + shift
			try {
				if (this.getRoundedPosition(x, y) != null) {
					this.drag_n_drop_sommet = (VertexView) this.vg.getItemAtPosition(x, y);
					if (this.drag_n_drop_sommet == null) throw new NoSuchElementException();
				}
			} catch (NoSuchElementException e) {
				this.drag_n_drop_sommet = null;
				this.ancien_pos = new Point(x, y);
			}
		} else if (modifiers == InputEvent.BUTTON3_MASK) {
			// Right click
			GraphItemView f = this.vg.getItemAtPosition(x, y);
			if (f != null && (f instanceof EdgeView || f instanceof VertexView)) {
				this.maybeShowPopup(evt, f);
			}
		}
	}

	public void mouseClicked(MouseEvent evt) {
	}

	public void mouseEntered(MouseEvent evt) {
	}

	public void mouseExited(MouseEvent evt) {
	}

	public void mouseMoved(MouseEvent evt) {
	}

	private void maybeShowPopup(MouseEvent e, final GraphItemView f) {
		if (e.isPopupTrigger()) {
			final boolean estSommet = (f instanceof VertexView);
			/*
			VueEtatPanel vueEtatPanel = new VueEtatPanel() {
				public void elementModified(String str) {
					if (estSommet) ((SommetDessin) f).setEtat(str);
					else ((AreteDessin) f).setEtat(str);
					StarVisuPanel.this.vertexPopup.setVisible(false);
					StarVisuPanel.this.parent.repaint();
				}
			};
			 */
			/*
			JPanel vueEtatPanel = new JPanel() {
				public void paintComponent(Graphics g) {
					System.out.println(((VertexView) f).getProperty("label"));
					super.paintComponent(g);
					vg.drawGraph(g);
					StarVisuPanel.this.parent.repaint();
				}
			};
			 */
			this.vertexPopup.removeAll();
			String etat = (String) (estSommet ? ((VertexView) f).getVertex().getLabel() : ((EdgeView) f).getEdge().getLabel());

			if (!estSommet) {
				final boolean isMarked = ((EdgeView) f).isMarked();
				JMenuItem edgePopUpItem = new JMenuItem(isMarked ? "Dismark" : "Mark");
				edgePopUpItem.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						((EdgeView) f).mark(!isMarked);
						StarVisuPanel.this.vertexPopup.setVisible(false);
						StarVisuPanel.this.parent.repaint();
					}
				});
				this.vertexPopup.add(edgePopUpItem);
				if (etat != null) {
					edgePopUpItem = new JMenuItem("No label");
					edgePopUpItem.addActionListener(new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							((EdgeView) f).getEdge().setLabel(null);
							StarVisuPanel.this.vertexPopup.setVisible(false);
							StarVisuPanel.this.parent.repaint();
						}
					});
					this.vertexPopup.add(edgePopUpItem);
				}
				this.vertexPopup.addSeparator();
			}
			if (etat == null) {
				etat = "N";
			}
			
			//EtatPanel etatPanel = new EtatPanel(TableCouleurs.getTableCouleurs(), vueEtatPanel, etat, true);
			GraphItemLabelPanel etatPanel = new GraphItemLabelPanel(parent, f);
			this.vertexPopup.add(etatPanel);
			this.vertexPopup.show(e.getComponent(), e.getX(), e.getY());
			etatPanel.requestFocus();
		}
	}

	/**
	 * Left and center buttons just modify the structure.
	 */
	public void mouseReleased(MouseEvent evt) {
		int x = evt.getX();
		int y = evt.getY();
		int modifiers = evt.getModifiers();

		if (modifiers == InputEvent.BUTTON3_MASK) {
			GraphItemView f = this.vg.getItemAtPosition(x, y);
			if (f != null)
				this.maybeShowPopup(evt, f);
		} else if (this.drag_n_drop_sommet != null) {
			// On verifie s'il ne faut pas fusionner deux sommets
			try {
				VertexView s = this.vg.getVertexAtPosition(x, y, this.drag_n_drop_sommet);
				if (s == null) throw new NoSuchElementException();
				EdgeView a = this.vg.getEdgeView(s.getVertex(), this.sommetC.getVertex());
				if (a == null) throw new NoSuchElementException();

				this.vg.removeGraphItemAndView(a);
				this.vg.removeGraphItemAndView(s);
				this.renumeberVertex();
				this.vertexNumber--;
				this.parent.repaint();				
			} catch (NoSuchElementException e) {
				this.drag_n_drop_sommet = null;
			}

		}
	}

	/**
	 * Modify the structure
	 */
	public void mouseDragged(MouseEvent evt) {
		int x = evt.getX();
		int y = evt.getY();
		int modifiers = evt.getModifiers();

		if ((modifiers == InputEvent.BUTTON2_MASK)
				|| (modifiers == (InputEvent.BUTTON1_MASK | InputEvent.ALT_MASK))) {
			if (this.drag_n_drop_sommet != null) {
				Point p = this.getRoundedPosition(x, y);
				if (p != null) {
					try {
						// S'il y a un sommet dessous, il est aspire
						VertexView s = this.vg.getVertexAtPosition(x, y, this.drag_n_drop_sommet);
						if (s == null) throw new NoSuchElementException();
						this.ancien_pos = new Point(s.getPosition().x, s.getPosition().y);
						this.drag_n_drop_sommet.setPosition(this.ancien_pos.x, this.ancien_pos.y);
					} catch (NoSuchElementException e) {
						// Sinon il est simplement deplace
						this.drag_n_drop_sommet.setPosition(p.x, p.y);
						this.ancien_pos = new Point(x, y);
					}
					this.renumeberVertex();
					this.parent.repaint();
				}
			}
		}
	}

}
