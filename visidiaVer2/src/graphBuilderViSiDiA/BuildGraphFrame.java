package graphBuilderViSiDiA;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.DefaultComboBoxModel;

import java.awt.Dimension;

import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.ComponentOrientation;

import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JCheckBox;
import javax.swing.JButton;

import visidia.gui.graphview.GraphView;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.awt.event.HierarchyListener;
import java.awt.event.HierarchyEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.CaretEvent;
import javax.swing.JTextPane;

public class BuildGraphFrame extends JFrame {
	private JComboBox switchTopologyCombo;
	private BuildGraphTPanel painterPanel;
	private JPanel panel;
	private JTextField textFieldNbNodes;
	private JPanel panel_1;
	private JTextField textFieldMaxRange;
	private JPanel panel_2;
	private JLabel label_1;
	private JTextField textFieldDensite;
	private JPanel panel_3;
	private JRadioButton radioButton;
	private JRadioButton radioButton_1;
	private JCheckBox chckbxNewCheckBox;
	private JPanel panelFolder;
	public JButton btnGenererGraph;
	private JButton btnNewButton;
	private JTextField textField_Rect_d1;
	private JTextField textField_Rect_d2;
	private JLabel lblD_1;
	private JLabel lblD;

	public BuildGraphFrame() {
		super("Configurer mon graphe");

		// setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setSize(728, 372);
		this.setResizable(false);

		Integer[] sides = new Integer[12];

		for (int i = 0; i < sides.length; i++) {
			sides[i] = i + 3; // Auto-boxing: int -> Integer
		}

		switchTopologyCombo = new JComboBox(sides);
		switchTopologyCombo.setBounds(72, 5, 82, 20);
		switchTopologyCombo.setModel(new DefaultComboBoxModel(new String[] {
				"Thin H", "Thin Cross", "S", "Large Cross", "L", "Large H",
				"Grille  nxn", "Rectangle" }));

		painterPanel = new BuildGraphTPanel(getD1(), getD2());
		painterPanel.setBounds(355, 0, 357, 333);
		painterPanel.setFocusTraversalKeysEnabled(false);
		painterPanel.setFocusable(false);
		painterPanel.setMaximumSize(new Dimension(100, 100));
		getContentPane().setLayout(null);

		JPanel controlsPanel = new JPanel();
		controlsPanel.setBounds(0, 0, 345, 333);
		controlsPanel.setLayout(null);
		JLabel label = new JLabel("Topologie                     ");
		label.setBounds(10, 8, 71, 14);
		controlsPanel.add(label);
		controlsPanel.add(switchTopologyCombo);

		getContentPane().add(controlsPanel);

		panel = new JPanel();
		panel.setLayout(null);
		panel.setToolTipText("\r\n\r\n");
		panel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		panel.setBorder(new TitledBorder(UIManager

		.getBorder("TitledBorder.border"), "Nombre de noeuds",

		TitledBorder.LEADING, TitledBorder.TOP, null,

		new Color(0, 0, 0)));
		panel.setBounds(10, 33, 140, 51);
		controlsPanel.add(panel);

		textFieldNbNodes = new JTextField();
		textFieldNbNodes.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
			}
		});
		textFieldNbNodes.setText("500");
		textFieldNbNodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			}
		});
		textFieldNbNodes.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String t = new String(textFieldNbNodes.getText());

				if (t.equals("") == false) {
					if ((t.matches("^-?\\d+$")) == false) {
						String message = "[" + t + "]"
								+ " n'est pas un nombre \"\n";
						JOptionPane.showMessageDialog(new JFrame(), message,
								"Dialog", JOptionPane.ERROR_MESSAGE);
						textFieldNbNodes.setText("");
					}

				}

			}
		});
		textFieldNbNodes.setColumns(10);
		textFieldNbNodes.setBounds(10, 20, 113, 20);
		panel.add(textFieldNbNodes);

		panel_1 = new JPanel();
		panel_1.setLayout(null);
		panel_1.setBorder(new TitledBorder(null,

		"Rang� de transmission",

		TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(160, 33, 175, 51);
		controlsPanel.add(panel_1);

		textFieldMaxRange = new JTextField();
		textFieldMaxRange.setText("100");
		textFieldMaxRange.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String t = new String(textFieldMaxRange.getText());

				if (t.equals("") == false) {
					if ((t.matches("^-?\\d+$")) == false) {
						String message = "[" + t + "]"
								+ " n'est pas un nombre \"\n";
						JOptionPane.showMessageDialog(new JFrame(), message,
								"Dialog", JOptionPane.ERROR_MESSAGE);
						textFieldMaxRange.setText("");
					}

				}
			}
		});
		textFieldMaxRange.setColumns(10);
		textFieldMaxRange.setBounds(10, 21, 115, 20);
		panel_1.add(textFieldMaxRange);

		panel_2 = new JPanel();
		panel_2.setLayout(null);
		panel_2.setBorder(new TitledBorder(null, "Densit�",

		TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_2.setBounds(10, 84, 140, 58);
		controlsPanel.add(panel_2);

		label_1 = new JLabel("\u2030");
		label_1.setBounds(108, 26, 17, 19);
		panel_2.add(label_1);

		textFieldDensite = new JTextField();
		textFieldDensite.setText("1");
		textFieldDensite.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String t = new String(textFieldDensite.getText());

				if (t.equals("") == false) {
					if ((t.matches("^-?\\d+$")) == false) {
						String message = "[" + t + "]"
								+ " n'est pas un nombre \"\n";
						JOptionPane.showMessageDialog(new JFrame(), message,
								"Dialog", JOptionPane.ERROR_MESSAGE);
						textFieldDensite.setText("");
					}

				}
			}
		});
		textFieldDensite.setColumns(10);
		textFieldDensite.setBounds(10, 25, 88, 20);
		panel_2.add(textFieldDensite);

		panel_3 = new JPanel();
		panel_3.setLayout(null);
		panel_3.setBorder(new TitledBorder(UIManager

		.getBorder("TitledBorder.border"), "Distribution",

		TitledBorder.LEADING, TitledBorder.TOP, null,

		new Color(0, 0, 0)));
		panel_3.setBounds(160, 84, 175, 58);
		controlsPanel.add(panel_3);

		radioButton_1 = new JRadioButton("Distribution al\u00E9atoire");
		radioButton_1.setBounds(6, 34, 125, 12);
		panel_3.add(radioButton_1);
		radioButton_1.setSelected(true);

		radioButton = new JRadioButton("Distribution reguli\u00E8re");
		radioButton.setBounds(6, 17, 128, 12);
		panel_3.add(radioButton);
		radioButton.setToolTipText("Ditribution Gride");
		radioButton.setActionCommand("");

		chckbxNewCheckBox = new JCheckBox("Enregistrer sous");
		chckbxNewCheckBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (chckbxNewCheckBox.isSelected())
					panelFolder.setEnabled(true);
				else
					panelFolder.setEnabled(false);

			}
		});
		chckbxNewCheckBox.setBounds(10, 140, 191, 23);
		controlsPanel.add(chckbxNewCheckBox);

		panelFolder = new JPanel();
		panelFolder.setEnabled(false);
		panelFolder.setToolTipText("");
		panelFolder.setLayout(null);
		panelFolder.setBorder(new TitledBorder(UIManager
				.getBorder("TitledBorder.border"), "Dossier",
				TitledBorder.LEADING, TitledBorder.TOP, null,
				new Color(0, 0, 0)));
		panelFolder.setBounds(10, 163, 325, 135);
		controlsPanel.add(panelFolder);

		JLabel label_2 = new JLabel("Dossier");
		label_2.setBounds(-92, 53, 53, 14);
		panelFolder.add(label_2);

		JButton button = new JButton("Parcourir");
		button.setEnabled(false);
		button.setBounds(193, 23, 90, 17);
		panelFolder.add(button);

		JLabel label_3 = new JLabel("Nom du fichier");
		label_3.setEnabled(false);
		label_3.setBounds(31, 53, 68, 14);
		panelFolder.add(label_3);

		JTextField textField = new JTextField();
		textField.setEnabled(false);
		textField.setText("*.gml");
		textField.setColumns(10);
		textField.setBounds(109, 51, 146, 17);
		panelFolder.add(textField);

		JLabel lblRepertoire = new JLabel("Repertoire");
		lblRepertoire.setEnabled(false);
		lblRepertoire.setBounds(31, 24, 68, 14);
		panelFolder.add(lblRepertoire);

		btnNewButton = new JButton("Annuler");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				// setVisible (false);
				dispose();

			}
		});
		btnNewButton.setBounds(61, 299, 89, 23);
		controlsPanel.add(btnNewButton);

		btnGenererGraph = new JButton("G\u00E9n\u00E9rer Graphe");
		btnGenererGraph.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			}

		});
		btnGenererGraph.setBounds(160, 299, 175, 23);
		controlsPanel.add(btnGenererGraph);

		textField_Rect_d1 = new JTextField();
		textField_Rect_d1.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Integer val = (Integer) (switchTopologyCombo.getSelectedIndex());// .getSelectedItem();
				int D1 = getD1();
				int D2 = getD2();
				painterPanel.setTopologyIndex(val.intValue(), D1, D2);

			}
		});
		textField_Rect_d1.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String t = new String(textField_Rect_d1.getText());
				if (t.equals("") == false) {

					if ((t.matches("^-?\\d+$")) == false) {
						String message = "[" + t + "]"
								+ " n'est pas un nombre \"\n";
						JOptionPane.showMessageDialog(new JFrame(), message,
								"Dialog", JOptionPane.ERROR_MESSAGE);
						textField_Rect_d1.setText("");
					}
				}
			}
		});
		textField_Rect_d1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Integer val = (Integer) (switchTopologyCombo.getSelectedIndex());// .getSelectedItem();
				Integer valD1 = Integer.parseInt(textField_Rect_d1.getText());
				Integer valD2 = Integer.parseInt(textField_Rect_d2.getText());
				painterPanel.setTopologyIndex(val.intValue(), valD1.intValue(),
						valD2.intValue());
				if (val.intValue() == 7) {
					lblD_1.setVisible(true);
					lblD.setVisible(true);
					textField_Rect_d1.setVisible(true);
					textField_Rect_d2.setVisible(true);
				} else {
					lblD_1.setVisible(false);
					lblD.setVisible(false);
					textField_Rect_d1.setVisible(false);
					textField_Rect_d2.setVisible(false);
				}

			}
		});
		textField_Rect_d1.setText("1");
		textField_Rect_d1.setVisible(false);
		textField_Rect_d1.setColumns(10);
		textField_Rect_d1.setBounds(185, 5, 35, 20);
		controlsPanel.add(textField_Rect_d1);

		textField_Rect_d2 = new JTextField();
		textField_Rect_d2.addCaretListener(new CaretListener() {
			public void caretUpdate(CaretEvent arg0) {
				Integer val = (Integer) (switchTopologyCombo.getSelectedIndex());// .getSelectedItem();
				int D1 = getD1();
				int D2 = getD2();
				painterPanel.setTopologyIndex(val.intValue(), D1, D2);

			}
		});
		textField_Rect_d2.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent arg0) {
				String t = new String(textField_Rect_d2.getText());
				if (t.equals("") == false) {

					if ((t.matches("^-?\\d+$")) == false) {
						String message = "[" + t + "]"
								+ " n'est pas un nombre \"\n";
						JOptionPane.showMessageDialog(new JFrame(), message,
								"Dialog", JOptionPane.ERROR_MESSAGE);
						textField_Rect_d2.setText("");
					}
				}

			}
		});
		textField_Rect_d2.setText("1");
		textField_Rect_d2.setVisible(false);
		textField_Rect_d2.setColumns(10);
		textField_Rect_d2.setBounds(263, 5, 35, 20);
		controlsPanel.add(textField_Rect_d2);

		lblD = new JLabel("D1");
		lblD.setVisible(false);
		lblD.setBounds(164, 8, 21, 14);
		controlsPanel.add(lblD);

		lblD_1 = new JLabel("D2");
		lblD_1.setVisible(false);
		lblD_1.setBounds(230, 8, 21, 14);
		controlsPanel.add(lblD_1);
		getContentPane().add(painterPanel);
		painterPanel.setLayout(null);

		switchTopologyCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Integer val = (Integer) (switchTopologyCombo.getSelectedIndex());// .getSelectedItem();
				int D1 = getD1();
				int D2 = getD2();
				painterPanel.setTopologyIndex(val.intValue(), D1, D2);
				if (val.intValue() == 7) {
					lblD_1.setVisible(true);
					lblD.setVisible(true);
					textField_Rect_d1.setVisible(true);
					textField_Rect_d2.setVisible(true);
				} else {
					lblD_1.setVisible(false);
					lblD.setVisible(false);
					textField_Rect_d1.setVisible(false);
					textField_Rect_d2.setVisible(false);
				}

			}

		});
	}

	public String getSelectedTopology() {

		System.out.println("topology" + this.textFieldNbNodes.getText());

		return (String) this.switchTopologyCombo.getSelectedItem();// .getSelectedIndex();
	}

	public int getMaxRange() {
		System.out.println("maxRange" + this.textFieldMaxRange.getText());
		return (Integer.parseInt(this.textFieldMaxRange.getText()));
	}

	public int getNbNodes() {
		System.out.println("nbnode" + this.textFieldNbNodes.getText());
		return (Integer.parseInt(this.textFieldNbNodes.getText()));
	}

	public int getDensite() {
		System.out.println("densit�" + this.textFieldDensite.getText());
		if (validNumeric(textFieldDensite))
			return (Integer.parseInt(this.textFieldDensite.getText()));
		return -1;
	}

	public boolean validInput() {
		return (validNumeric(textFieldNbNodes)
				&& validNumeric(textFieldMaxRange) && validNumeric(textFieldDensite));
	}

	private boolean validNumeric(JTextField textField) {
		if ((textField.getText().equals(""))
				|| (textField.getText().matches("^-?\\d+$") == false))
			return false;
		else
			return true;
	}

	public int getD1() {
		try {
			// return (Integer.parseInt(this.textField_Rect_d1.getText()));
			Integer valD1 = Integer.parseInt(textField_Rect_d1.getText());
			// Integer valD2 = Integer.parseInt(textField_Rect_d2.getText());
			return valD1.intValue();
			// valD2.intValue());

		} catch (NullPointerException e) {
			return 0;
		}

	}

	public int getD2() {

		try {
			Integer valD2 = Integer.parseInt(textField_Rect_d2.getText());
			return valD2.intValue();

		} catch (NullPointerException e) {
			return 0;
		}

	}
}
