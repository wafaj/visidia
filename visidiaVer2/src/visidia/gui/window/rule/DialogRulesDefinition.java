package visidia.gui.window.rule;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.WindowConstants;

import visidia.gui.window.GraphPanelSimulation;
import visidia.misc.ImageHandler;
import visidia.rule.RSOptions;
import visidia.rule.RelabelingSystem;
import visidia.rule.Rule;
import visidia.simulation.process.synchronization.SynCT;

// TODO: Auto-generated Javadoc
/**
 * This class is a modal dialog used to manage a relabeling systems.
 */
public class DialogRulesDefinition extends JDialog implements RuleTabbedPaneControl {

	private static final long serialVersionUID = 1149679394695690242L;

	/** The rule. */
	JTabbedPane rule;

	/** The termination option. */
	JCheckBoxMenuItem optionTermination;

	/** The synchronization type for rdv. */
	JCheckBoxMenuItem synRdv;
	
	/** The synchronization type for LC1. */
	JCheckBoxMenuItem synLC1;
	
	/** The synchronization type for LC2. */
	JCheckBoxMenuItem synLC2;
	
	/** The synchronization type for "not specified". */
	JCheckBoxMenuItem synNotSpecified;

	/** Name of the save file. */
	String fileName = null;

	/** The final frame (used by inner class). */
	Frame finalFrame;

	/** The info dialog. */
	InfoDialog infoDialog;

	/**
	 * Instantiates a new dialog rules definition.
	 * 
	 * @param owner the owner
	 * @param currentSimulationPanel the current simulation panel
	 */
	public DialogRulesDefinition(Frame owner, final GraphPanelSimulation currentSimulationPanel) {
		super(owner, "Rules definition", true);
		this.finalFrame = owner;
		this.rule = new JTabbedPane();
		this.rule.setBackground(RuleConstants.ruleColor);
		Point p = owner.getLocation();
		this.infoDialog = new InfoDialog(this, "Insert the rules description");
		this.infoDialog.setEditable(true);
		this.setLocation(p.x + 100, p.y + 100);
		this.setTitle();

		Container c = this.getContentPane();
		c.setBackground(new Color(175, 235, 235));
		c.add(this.rule);
		//this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar();
		this.setJMenuBar(menuBar);
		JMenu menuFile = new JMenu("File");
		menuFile.setMnemonic('F');
		JMenu menuSyn = new JMenu("Synchronization");
		menuSyn.setMnemonic('S');
		JMenu menuOption = new JMenu("Options");
		menuOption.setMnemonic('O');
		menuBar.add(Box.createHorizontalStrut(5));
		menuBar.add(menuFile);
		menuBar.add(Box.createHorizontalStrut(5));
		menuBar.add(menuSyn);
		menuBar.add(Box.createHorizontalStrut(5));
		menuBar.add(menuOption);
		menuBar.add(Box.createHorizontalStrut(10));

		this.buildFileMenu(menuFile);

		ButtonGroup synGroup = new ButtonGroup();
		this.synRdv = new JCheckBoxMenuItem("Rendez-vous");
		this.synLC1 = new JCheckBoxMenuItem("LC1");
		this.synLC2 = new JCheckBoxMenuItem("LC2");
		this.synNotSpecified = new JCheckBoxMenuItem("Not specified");
		this.synNotSpecified.setSelected(true);
		synGroup.add(this.synRdv);
		synGroup.add(this.synLC1);
		synGroup.add(this.synLC2);
		synGroup.add(this.synNotSpecified);
		menuSyn.add(this.synNotSpecified);
		menuSyn.addSeparator();
		menuSyn.add(this.synRdv);
		menuSyn.add(this.synLC1);
		menuSyn.add(this.synLC2);

		this.optionTermination = new JCheckBoxMenuItem("Manage termination");
		menuOption.add(this.optionTermination);

		JButton butApply = new JButton("Apply");
		menuBar.add(butApply);
		butApply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int res = JOptionPane.showConfirmDialog(DialogRulesDefinition.this.finalFrame,
						"Apply the rules to the simulation frame ?", "Apply the rules", JOptionPane.YES_NO_OPTION);
				if (res == JOptionPane.YES_OPTION) {
					currentSimulationPanel.applyStarRulesSystem(DialogRulesDefinition.this.getRelabelingSystem());
				}
			}
		});
		ImageIcon imageInfo = ImageHandler.getInstance().createImageIcon("/info.png");
		JButton butInfo = new JButton(imageInfo);
		butInfo.setToolTipText("Info");
		butInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
		butInfo.setAlignmentY(Component.CENTER_ALIGNMENT);
		Dimension dim = new Dimension(imageInfo.getIconWidth() + 8, imageInfo.getIconHeight() + 7);
		butInfo.setSize(dim);
		butInfo.setPreferredSize(dim);
		butInfo.setMaximumSize(dim);
		butInfo.setMinimumSize(dim);
		menuBar.add(Box.createHorizontalStrut(10));
		menuBar.add(butInfo);
		butInfo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DialogRulesDefinition.this.infoDialog.setVisible(true);
				DialogRulesDefinition.this.pack();
			}
		});

		this.newRelabelingSystem(null);
		this.pack();
	}

	/**
	 * Sets the title.
	 */
	public void setTitle() {
		String title = "Star rules system builder";
		if (this.fileName == null) {
			super.setTitle(title + " " + "(untilted)");
		} else {
			super.setTitle(title + " " + "(" + this.fileName + ")");
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.Dialog#setVisible(boolean)
	 */
	public void setVisible(boolean v) {
		this.newRelabelingSystem(null);
		this.fileName = null;
		this.pack();
		super.setVisible(v);
	}

	/**
	 * Defines a new relabeling system (erases all rule tabbed panes)
	 * If rSys is null, a new system is proposed
	 * New relabeling system.
	 * 
	 * @param rSys the relabeling system
	 */
	private void newRelabelingSystem(RelabelingSystem rSys) {
		if (rSys == null) {
			this.rule.removeAll();
			this.rule.addTab("Rule n 1", new RulePane(this, null));
			this.optionTermination.setSelected(true);
			this.synNotSpecified.setSelected(true);
			this.infoDialog.setText("");
		} else {
			this.rule.removeAll();
			for (Iterator it = rSys.getRules(); it.hasNext();) {
				Rule r = (Rule) it.next();
				this.rule.addTab("", new RulePane(this, r));
			}
			RSOptions rsOpt = rSys.getOptions();
			this.optionTermination.setSelected(rsOpt.manageTerm);
			int synType = rsOpt.defaultSynchronisation();
			this.synNotSpecified.setSelected(true);
			this.synRdv.setSelected(synType == SynCT.RDV);
			this.synLC1.setSelected(synType == SynCT.LC1);
			this.synLC2.setSelected(synType == SynCT.LC2);
			this.infoDialog.setText(rSys.getDescription());
		}
		this.renameRule();
	}

	/**
	 * Builds the file menu.
	 * 
	 * @param menuFile the menu file
	 */
	private void buildFileMenu(JMenu menuFile) {
		JMenuItem fileNew = new JMenuItem("New");
		JMenuItem fileOpen = new JMenuItem("Open");
		final JMenuItem fileSave = new JMenuItem("Save");
		JMenuItem fileSaveAs = new JMenuItem("Save as");
		JMenuItem fileClose = new JMenuItem("Close");
		menuFile.add(fileNew);
		menuFile.add(fileOpen);
		menuFile.add(fileSave);
		menuFile.add(fileSaveAs);
		menuFile.addSeparator();
		menuFile.add(fileClose);

		fileSave.setEnabled(false);

		final javax.swing.filechooser.FileFilter filter = new javax.swing.filechooser.FileFilter() {
			public boolean accept(File f) {
				String n = f.getName();
				return n.endsWith("srs");
			}

			public String getDescription() {
				return "srs (star rules system) files";
			}
		};

		fileSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (DialogRulesDefinition.this.fileName == null) {
					return;
				}
				try {
					FileOutputStream ostream = new FileOutputStream(DialogRulesDefinition.this.fileName);
					ObjectOutputStream p = new ObjectOutputStream(ostream);
					p.writeObject(DialogRulesDefinition.this.getRelabelingSystem());
					p.flush();
					ostream.close();
				} catch (Exception exc) {
					System.out.println(exc);
				}
			}
		});

		fileNew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int res = JOptionPane.showConfirmDialog(DialogRulesDefinition.this.finalFrame, "Begin new system ?",	"Begin new system", JOptionPane.YES_NO_OPTION);
				if (res == JOptionPane.YES_OPTION) {
					DialogRulesDefinition.this.newRelabelingSystem(null);
					DialogRulesDefinition.this.fileName = null;
					DialogRulesDefinition.this.setTitle();
					fileSave.setEnabled(false);
				}
			}
		});

		fileClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO: ask for saving changes, if any
				setVisible(false);
				dispose();
			}
		});

		fileSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogType(JFileChooser.SAVE_DIALOG);
				chooser.setFileFilter(filter);
				chooser.setCurrentDirectory(new File("./"));
				int returnVal = chooser.showSaveDialog(DialogRulesDefinition.this.finalFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						String fName = chooser.getSelectedFile().getPath();
						if (!fName.endsWith("srs")) {
							fName += ".srs";
						}
						FileOutputStream ostream = new FileOutputStream(fName);
						ObjectOutputStream p = new ObjectOutputStream(ostream);
						p.writeObject(DialogRulesDefinition.this.getRelabelingSystem());
						p.flush();
						ostream.close();
						DialogRulesDefinition.this.fileName = fName;
						fileSave.setEnabled(true);
						DialogRulesDefinition.this.setTitle();
					} catch (IOException ioe) {
						System.out.println(ioe);
					}
				}
			}
		});

		fileOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		
				JFileChooser chooser = new JFileChooser();
				chooser.setDialogType(JFileChooser.OPEN_DIALOG);
				chooser.setFileFilter(filter);
				chooser.setCurrentDirectory(new File("./"));
				int returnVal = chooser.showOpenDialog(DialogRulesDefinition.this.finalFrame);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					try {
						String fName = chooser.getSelectedFile().getPath();
						FileInputStream istream = new FileInputStream(fName);
						ObjectInputStream p = new ObjectInputStream(istream);
						RelabelingSystem rSys = (RelabelingSystem) p.readObject();
						DialogRulesDefinition.this.newRelabelingSystem(rSys);
						istream.close();
						DialogRulesDefinition.this.fileName = fName;
						fileSave.setEnabled(true);
						DialogRulesDefinition.this.setTitle();
					} catch (IOException ioe) {
						System.out.println(ioe);
					} catch (ClassNotFoundException cnfe) {
						System.out.println(cnfe);
					}
				}
			}
		});
	}

	/**
	 * Renames rule: give a name to the tab according to its index.
	 */
	private void renameRule() {
		int count = this.rule.getTabCount();
		for (int i = 0; i < count; i++) {
			this.rule.setTitleAt(i, "Rule " + (i + 1));
			this.rule.setBackgroundAt(i, RuleConstants.ruleColor);
		}
	}

	/* (non-Javadoc)
	 * @see visidia.gui.window.rule.RuleTabbedPaneControl#addNewRule()
	 */
	public void addNewRule() {
		this.rule.addTab("", new RulePane(this, null));
		this.renameRule();
		this.rule.setSelectedIndex(this.rule.getTabCount() - 1);

	}

	/* (non-Javadoc)
	 * @see visidia.gui.window.rule.RuleTabbedPaneControl#deleteRule()
	 */
	public void deleteRule() {
		int p = this.rule.getSelectedIndex();
		int count = this.rule.getTabCount();
		if (count > 1) {
			this.rule.remove(p);
			this.renameRule();
		}
	}

	/* (non-Javadoc)
	 * @see visidia.gui.window.rule.RuleTabbedPaneControl#insertRule()
	 */
	public void insertRule() {
		this.rule.insertTab("", null, new RulePane(this, null), null, this.rule
				.getSelectedIndex());
		this.renameRule();
	}

	/* (non-Javadoc)
	 * @see visidia.gui.window.rule.RuleTabbedPaneControl#switchLeft()
	 */
	public void switchLeft() {
		int pos = this.rule.getSelectedIndex();
		if (pos >= 1) {
			RulePane r1 = (RulePane) this.rule.getSelectedComponent();
			this.rule.remove(pos);
			this.rule.insertTab("", null, r1, null, pos - 1);
			this.rule.setSelectedIndex(pos);
		}
		this.renameRule();
		this.repaint();
	}

	/* (non-Javadoc)
	 * @see visidia.gui.window.rule.RuleTabbedPaneControl#switchRight()
	 */
	public void switchRight() {
		int pos = this.rule.getSelectedIndex();
		if (pos < this.rule.getTabCount() - 1) {
			RulePane r1 = (RulePane) this.rule.getSelectedComponent();
			this.rule.remove(pos);
			this.rule.insertTab("", null, r1, null, pos + 1);
			this.rule.setSelectedIndex(pos);
		}
		this.renameRule();
	}

	/**
	 * Returns true if the selected pane can switch with the right pane.
	 * 
	 * @return true, if can switch right
	 */
	public boolean canSwitchRight() {
		return (this.rule.getSelectedIndex() < (this.rule.getTabCount() - 1));
	}

	/**
	 * Returns true if the selected pane can switch with the left pane.
	 * 
	 * @return true, if can switch left
	 */
	public boolean canSwitchLeft() {
		return (this.rule.getSelectedIndex() >= 1);
	}

	/**
	 * Gets the relabeling system.
	 * 
	 * @return the relabeling system
	 */
	public RelabelingSystem getRelabelingSystem() {
		Vector v = new Vector();
		int count = this.rule.getTabCount();
		for (int i = 0; i < count; i++) {
			v.add(((RulePane) this.rule.getComponent(i)).getRule());
		}
		RelabelingSystem rSys = new RelabelingSystem(v);
		// FIXME : quel est le choix pour type non specifie ?? (Mohammed)
		int synType = (this.synRdv.isSelected() ? SynCT.RDV : (this.synLC1.isSelected() ? SynCT.LC1	: (this.synLC2.isSelected() ? SynCT.LC2 : -1)));
		RSOptions opt = new RSOptions(synType, this.optionTermination.isSelected());
		rSys.setOptions(opt);
		rSys.setDescription(this.infoDialog.getText());
		return rSys;
	}

}
