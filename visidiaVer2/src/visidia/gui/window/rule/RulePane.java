package visidia.gui.window.rule;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.border.Border;

import visidia.rule.Rule;
import visidia.rule.RuleVector;
import visidia.rule.Star;
import visidia.simulation.process.synchronization.SynCT;

/**
 * A RulePane is a panel containing one rule and a tabbed pane of contexts. It
 * is inserted into the tabbed pane of the rules.
 */
class RulePane extends JPanel implements ContextTabbedPaneControl {

	private static final long serialVersionUID = -2002253524039640310L;

	final public static int NOT_TERMINIATION_RULE = 0;

	final public static int LOCAL_TERMINIATION_RULE = 1;

	final public static int GLOBAL_TERMINIATION_RULE = 2;

	final public static String TERMINATION[] = { "Not a termination rule",
		"Local termination rule", "Global termination rule" };

	// The panel containing on rule
	BuildRulePane buildRulePane;

	// The contexts
	JTabbedPane context;

	int terminationType;

	public RulePane(RuleTabbedPaneControl tabbedPaneControl, Rule rule) {
		this.setBackground(RuleConstants.ruleColor);
		Border b = (BorderFactory.createCompoundBorder(BorderFactory
				.createEmptyBorder(5, 5, 5, 5), BorderFactory
				.createCompoundBorder(BorderFactory.createLoweredBevelBorder(),
						BorderFactory.createEmptyBorder(5, 10, 5, 10))));

		// That TabbedPane will store each context associated to the rule
		this.context = new JTabbedPane();
		this.context.setBorder(b);

		if (rule == null) { // There is no rule
			this.context.addTab("Context 1", new BuildContextPane(this, true, null));
			this.buildRulePane = new BuildRulePane(tabbedPaneControl, null, null, false);
			this.terminationType = RulePane.NOT_TERMINIATION_RULE;
		} else {
			this.buildRulePane = new BuildRulePane(tabbedPaneControl, rule.befor(), rule.after(), rule.isSimpleRule());
			if (rule.forbContexts().size() == 0) {
				// System.out.println ("Zero forboedde");
				this.context.addTab("Context 1", new BuildContextPane(this, true, null));
			} else {
				for (Iterator it = rule.forbContexts().iterator(); it.hasNext();) {
					Star s = (Star) it.next();
					this.context.addTab("",	new BuildContextPane(this, false, s));
				}
			}
			int t = rule.getType();
			if (t == SynCT.GENERIC) {
				this.terminationType = RulePane.NOT_TERMINIATION_RULE;
			} else if (t == SynCT.LOCAL_END) {
				this.terminationType = RulePane.LOCAL_TERMINIATION_RULE;
			} else if (t == SynCT.GLOBAL_END) {
				this.terminationType = RulePane.GLOBAL_TERMINIATION_RULE;
			}
			this.renameContext();
		}

		// The combo box permits to choose the type of termination
		JPanel rulePaneWithCombo = new JPanel();
		rulePaneWithCombo.setBackground(RuleConstants.ruleColor);
		JPanel comboPanel = new JPanel();
		comboPanel.setBackground(RuleConstants.ruleColor);
		final JComboBox comboTermination = new JComboBox(RulePane.TERMINATION);
		comboTermination.setBackground(new Color(185, 225, 215));
		comboTermination.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RulePane.this.terminationType = comboTermination.getSelectedIndex();
			}
		});
		comboPanel.add(comboTermination);
		comboTermination.setSelectedIndex(this.terminationType);

		rulePaneWithCombo.setLayout(new BorderLayout(0, 0));
		rulePaneWithCombo.add(this.buildRulePane, BorderLayout.CENTER);
		rulePaneWithCombo.add(comboPanel, BorderLayout.SOUTH);

		rulePaneWithCombo.setBorder(b);

		GridBagLayout g = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(g);
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 3;
		c.gridheight = 1;
		c.weightx = 70;
		c.weighty = 0;
		c.fill = GridBagConstraints.BOTH;
		this.add(rulePaneWithCombo, c);
		c.gridx = 3;
		c.gridy = 0;
		c.gridwidth = 1;
		c.gridheight = 1;
		c.weightx = 30;
		c.weighty = 0;
		this.add(this.context, c);
	}

	public Rule getRule() {
		RuleVector v = new RuleVector();
		int count = this.context.getTabCount();
		for (int i = 0; i < count; i++) {
			Star s = ((BuildContextPane) this.context.getComponent(i))
			.getStar();
			if (s != null) {
				v.add(s);
			}
		}
		Rule r = new Rule(this.buildRulePane.getLeftStar(), this.buildRulePane.getRightStar(), v);
		// Sets the type of the rule
		if (this.terminationType == RulePane.NOT_TERMINIATION_RULE) {
			r.setType(SynCT.GENERIC);
		} else if (this.terminationType == RulePane.LOCAL_TERMINIATION_RULE) {
			r.setType(SynCT.LOCAL_END);
		} else if (this.terminationType == RulePane.GLOBAL_TERMINIATION_RULE) {
			r.setType(SynCT.GLOBAL_END);
		}
		r.setSimpleRule(this.buildRulePane.getIsSimpleRule());

		return r;
	}

	// Give a name to the tab according to its index
	private void renameContext() {
		int count = this.context.getTabCount();
		for (int i = 0; i < count; i++) {
			this.context.setTitleAt(i, "Context " + (i + 1));
			this.context.setBackgroundAt(i, RuleConstants.contextColor);
		}
	}

	/**
	 * If the selected context is empty, it is deleted. A new one is added.
	 */
	public void addNewContext() {
		if (((BuildContextPane) this.context.getSelectedComponent()).isEmpty()) {
			this.context.remove(0);
		}
		String s1 = this.buildRulePane.getLeftStar().centerState();
		Star centerContext = null;
		centerContext = new Star(s1);
		this.context.addTab("",	new BuildContextPane(this, false, centerContext));
		this.renameContext();
		this.context.setSelectedIndex(this.context.getTabCount() - 1);
	}

	/**
	 * If there is just one pane, it is replaced by an empty-one
	 */
	public void deleteContext() {
		int p = this.context.getSelectedIndex();
		this.context.remove(p);
		int count = this.context.getTabCount();
		if (count == 0) {
			this.context.addTab("", new BuildContextPane(this, true, null));
		}
		this.renameContext();
	}

	public void insertContext() {
		int pos;
		if (((BuildContextPane) this.context.getSelectedComponent()).isEmpty()) {
			this.context.remove(0);
			pos = 0;
		} else {
			pos = this.context.getSelectedIndex();
		}
		this.context.insertTab("", null, new BuildContextPane(this, false, null), null, pos);
		this.renameContext();
	}

}
