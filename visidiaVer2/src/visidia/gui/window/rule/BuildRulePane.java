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
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import visidia.gui.graphview.GraphItemView;
import visidia.gui.graphview.GraphView;
import visidia.gui.graphview.VertexView;
import visidia.rule.Star;

/**
 * That left side panel is used to paint and to compose one rule A popup menu
 * proposes various actions (Add/remove/insert rule) implemented by
 * StarRuleFrame.
 */
class BuildRulePane extends JPanel {

	private static final long serialVersionUID = -6891088192742926914L;

	VertexView sommetCLeft, sommetCRight;

	GraphView vgLeft, vgRight;

	Point centerLeft, centerRight;

	RuleTabbedPaneControl tabbedPaneControl;

	boolean isSimpleRule;

	// Circle stroke
	float[] dash = { 6.0f, 4.0f, 2.0f, 4.0f, 2.0f, 4.0f };

	BasicStroke dashS = new BasicStroke(1.0f, BasicStroke.CAP_BUTT,	BasicStroke.JOIN_MITER, 10.0f, this.dash, 0.0f);

	// Used to visualize the two VueGraph
	DuoStarVisuPanel dsvp;

	JPopupMenu popup;

	JMenuItem deleteRuleMenuItem, addRuleMenuItem, insertRuleMenuItem;

	JMenuItem switchLeftMenuItem, switchRightMenuItem, reorganizeVertexMenuItem;

	JCheckBoxMenuItem simpleRuleMenuItem;

	public BuildRulePane(final RuleTabbedPaneControl tabbedPaneControl,	Star sLeft, Star sRight, boolean simpleRule) {

		this.tabbedPaneControl = tabbedPaneControl;
		this.isSimpleRule = simpleRule;

		this.setBackground(RuleConstants.ruleColor);
		this.vgLeft = new GraphView();
		this.vgRight = new GraphView();

		this.setCenterPosition();

		this.sommetCLeft = this.vgLeft.createVertexAndView(this.centerLeft.x, this.centerLeft.y);
		this.sommetCRight = this.vgRight.createVertexAndView(this.centerRight.x, this.centerRight.y);

		if ((sLeft != null) && (sRight != null)) {
			StarGraphViewConvertor.star2StarVueGraphe(sLeft, this.vgLeft, this.sommetCLeft);
			StarGraphViewConvertor.star2StarVueGraphe(sRight, this.vgRight, this.sommetCRight);
		}

		this.dsvp = new DuoStarVisuPanel(this, this.vgLeft, this.vgRight,
				this.centerLeft, this.centerRight, RuleConstants.ray * 2 + RuleConstants.rule_center, RuleConstants.ray, this.isSimpleRule);
		this.dsvp.reorganizeVertex();
		this.deleteRuleMenuItem = new JMenuItem("Delete rule");
		this.addRuleMenuItem = new JMenuItem("Add rule");
		this.insertRuleMenuItem = new JMenuItem("Insert rule");
		this.switchLeftMenuItem = new JMenuItem("Switch with left");
		this.switchRightMenuItem = new JMenuItem("Switch with right");
		this.reorganizeVertexMenuItem = new JMenuItem("Reoranize vertex");
		this.simpleRuleMenuItem = new JCheckBoxMenuItem("Simple rule");
		this.simpleRuleMenuItem.setSelected(this.isSimpleRule);

		this.deleteRuleMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				for (Enumeration v_enum = BuildRulePane.this.vgLeft.getGraphItems(); v_enum.hasMoreElements();) {
					GraphItemView f = (GraphItemView) v_enum.nextElement();
					if (f != BuildRulePane.this.sommetCLeft) {
						BuildRulePane.this.vgLeft.removeGraphItemAndView(f);
					}
				}
				for (Enumeration v_enum = BuildRulePane.this.vgRight.getGraphItems(); v_enum.hasMoreElements();) {
					GraphItemView f = (GraphItemView) v_enum.nextElement();
					if (f != BuildRulePane.this.sommetCRight) {
						BuildRulePane.this.vgRight.removeGraphItemAndView(f);
					}
				}
				BuildRulePane.this.repaint();
				tabbedPaneControl.deleteRule();
			}
		});
		this.addRuleMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPaneControl.addNewRule();
			}
		});
		this.insertRuleMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPaneControl.insertRule();
			}
		});
		this.switchLeftMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPaneControl.switchLeft();
			}
		});
		this.switchRightMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPaneControl.switchRight();
			}
		});
		this.reorganizeVertexMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BuildRulePane.this.dsvp.reorganizeVertex();
			}
		});
		this.simpleRuleMenuItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if ((BuildRulePane.this.vgLeft.getNbGraphItems() > 3)
						|| (BuildRulePane.this.vgRight.getNbGraphItems() > 3)) {
					BuildRulePane.this.simpleRuleMenuItem.setSelected(BuildRulePane.this.isSimpleRule);
					return;
				}
				BuildRulePane.this.isSimpleRule = BuildRulePane.this.simpleRuleMenuItem.isSelected();
				BuildRulePane.this.setCenterPosition();
				BuildRulePane.this.sommetCLeft.setPosition(BuildRulePane.this.centerLeft.x, BuildRulePane.this.centerLeft.y);
				BuildRulePane.this.sommetCRight.setPosition(BuildRulePane.this.centerRight.x, BuildRulePane.this.centerRight.y);
				BuildRulePane.this.dsvp.launchStarVisuPanels(
						BuildRulePane.this.centerLeft,
						BuildRulePane.this.centerRight,
						BuildRulePane.this.isSimpleRule);
				BuildRulePane.this.dsvp.reorganizeVertex();
				BuildRulePane.this.repaint();
			}
		});

		this.popup = new JPopupMenu();
		this.popup.add(this.deleteRuleMenuItem);
		this.popup.add(this.addRuleMenuItem);
		this.popup.add(this.insertRuleMenuItem);
		this.popup.add(this.switchLeftMenuItem);
		this.popup.add(this.switchRightMenuItem);
		this.popup.addSeparator();
		this.popup.add(this.reorganizeVertexMenuItem);
		this.popup.add(this.simpleRuleMenuItem);
		this.popup.setBorder(BorderFactory.createRaisedBevelBorder());

		this.addMouseListener(new MouseAdapter() {
			private void callPopup(MouseEvent evt) {
				int x = evt.getX();
				int y = evt.getY();
				int modifiers = evt.getModifiers();
				if (modifiers == InputEvent.BUTTON3_MASK) {
					GraphItemView f1 = BuildRulePane.this.vgLeft.getItemAtPosition(x, y);
					if (f1 == null) {
						GraphItemView f2 = BuildRulePane.this.vgRight.getItemAtPosition(x, y);
						if (f2 == null)
							BuildRulePane.this.maybeShowPopup(evt);
					}
				}
			}

			public void mousePressed(MouseEvent evt) {
				this.callPopup(evt);
			}

			public void mouseReleased(MouseEvent evt) {
				this.callPopup(evt);
			}
		});
		this.addMouseListener(this.dsvp);
		this.addMouseMotionListener(this.dsvp);
	}

	private void setCenterPosition() {
		if (this.isSimpleRule) {
			this.centerLeft = new Point(RuleConstants.ray / 2 + RuleConstants.rule_left, RuleConstants.ray + RuleConstants.rule_top);
			this.centerRight = new Point(this.centerLeft.x + RuleConstants.ray * 2 + RuleConstants.rule_center, RuleConstants.ray + RuleConstants.rule_top);
		} else {
			this.centerLeft = new Point(RuleConstants.ray + RuleConstants.rule_left, RuleConstants.ray + RuleConstants.rule_top);
			this.centerRight = new Point(this.centerLeft.x + RuleConstants.ray * 2 + RuleConstants.rule_center, RuleConstants.ray + RuleConstants.rule_top);
		}
	}

	private void maybeShowPopup(MouseEvent e) {
		if (e.isPopupTrigger()) {
			this.switchRightMenuItem.setEnabled(this.tabbedPaneControl.canSwitchRight());
			this.switchLeftMenuItem.setEnabled(this.tabbedPaneControl.canSwitchLeft());
			this.popup.show(e.getComponent(), e.getX(), e.getY());
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(RuleConstants.rule_left + RuleConstants.ray * 2
				+ RuleConstants.rule_center + RuleConstants.ray * 2 + RuleConstants.rule_left,
				RuleConstants.rule_top + RuleConstants.ray * 2 + RuleConstants.rule_bottom);
	}

	public Star getLeftStar() {
		return StarGraphViewConvertor.starVueGraphe2Star(this.vgLeft, this.sommetCLeft);
	}

	public Star getRightStar() {
		return StarGraphViewConvertor.starVueGraphe2Star(this.vgRight, this.sommetCRight);
	}

	public boolean getIsSimpleRule() {
		return this.isSimpleRule;
	}

	public Dimension getMinimumSize() {
		return this.getPreferredSize();
	}

	public Dimension getMaxmimumSize() {
		return this.getPreferredSize();
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.black);
		Stroke tmp = ((Graphics2D) g).getStroke();
		if (!this.isSimpleRule) {
			// Indicates on the circle the degree 0
			g.drawLine(this.centerLeft.x, this.centerLeft.y - RuleConstants.ray - 2,
					this.centerLeft.x, this.centerLeft.y - RuleConstants.ray + 2);
			g.drawLine(this.centerRight.x, this.centerRight.y - RuleConstants.ray - 2,
					this.centerRight.x, this.centerRight.y - RuleConstants.ray + 2);
		}
		// Center arrow
		((Graphics2D) g).setStroke(new BasicStroke(3.0f));
		g.drawLine(RuleConstants.arrow_x1, this.centerLeft.y, RuleConstants.arrow_x2, this.centerLeft.y);

		g.drawLine(RuleConstants.arrow_x2, this.centerLeft.y, RuleConstants.arrow_x2 - 5, this.centerLeft.y - 5);

		g.drawLine(RuleConstants.arrow_x2, this.centerLeft.y, RuleConstants.arrow_x2 - 5, this.centerLeft.y + 5);
		if (!this.isSimpleRule) {
			// Circles
			((Graphics2D) g).setStroke(this.dashS);
			g.drawArc(this.centerLeft.x - RuleConstants.ray, this.centerLeft.y
					- RuleConstants.ray, RuleConstants.ray * 2, RuleConstants.ray * 2, 0, 360);
			g.drawArc(this.centerRight.x - RuleConstants.ray, this.centerRight.y
					- RuleConstants.ray, RuleConstants.ray * 2, RuleConstants.ray * 2, 0, 360);
		} else {
			g.setColor(Color.gray);
			g.drawLine(this.centerLeft.x + RuleConstants.ray, this.centerLeft.y - 5,
					this.centerLeft.x + RuleConstants.ray, this.centerLeft.y + 5);
			g.drawLine(this.centerLeft.x + RuleConstants.ray - 5, this.centerLeft.y,
					this.centerLeft.x + RuleConstants.ray + 5, this.centerLeft.y);
			g.drawLine(this.centerRight.x + RuleConstants.ray,
					this.centerRight.y - 5, this.centerRight.x + RuleConstants.ray,
					this.centerRight.y + 5);
			g.drawLine(this.centerRight.x + RuleConstants.ray - 5,
					this.centerRight.y, this.centerRight.x + RuleConstants.ray + 5,
					this.centerRight.y);
		}
		((Graphics2D) g).setStroke(tmp);

		this.vgLeft.drawGraph(g);
		this.vgRight.drawGraph(g);
	}

}
