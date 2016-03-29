package visidia.io.gml;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

// TODO: Auto-generated Javadoc
/**
 * A file chooser designed for GML files.
 */
public class GMLFileChooser extends JFileChooser implements ActionListener {

	private static final long serialVersionUID = 1057097455930665898L;

	/** A "valid" file: with the good extension. */
	protected File validFile;

	/** The current path for exporting. */
	protected String path;

	/** The file writer to write. */
	protected static FileWriter fileWriter;

	/** Boolean to know if we display graphics informations. */
	private static boolean graphicsInfo = true;

	/** Button for the graphics properties. */
	JRadioButton graphicsButton;

	/**
	 * Instantiates a new GMLFileChooser object, used to export the graph
	 * which is in the "parent" window in a file of the directory "path".
	 * 
	 * @param path the path
	 */
	public GMLFileChooser(String path) {
		super(path);
		this.path = path;

		this.validFile = new File(path, "default.gml");
		this.setSelectedFile(this.validFile);

		addTheButtons();
		setDialogTitle("Export in GML");
		setApproveButtonText("Export");
		javax.swing.filechooser.FileFilter gmlFileFilter = new GMLGraphFileFilter();
		addChoosableFileFilter(gmlFileFilter);
		setFileFilter(gmlFileFilter);
	}

	/**
	 * This method permits to get the name of the file chosen by the user. We take
	 * care here of errors and warnings (existing file, extension errors...)
	 */
	public void approveSelection() {
		boolean save = true;
		File f = this.getSelectedFile();
		String s = f.getName();
		int i = s.lastIndexOf('.');
		if ((i > 0) && (i < s.length() - 1)) {
			String extension = s.substring(i + 1).toLowerCase();
			if (!extension.equals("gml")) {
				JOptionPane.showMessageDialog(this, this.getName(f)
						+ " : this file has not a recognized\n"
						+ "extension. The required extension is '.gml ",
						"Warning", JOptionPane.WARNING_MESSAGE);
				this.setSelectedFile(this.validFile);
				save = false;

			}
		} else {
			if (i == -1) {
				this.setSelectedFile(new File(this.path, s + ".gml"));
				save = true;
			} else {
				this.setSelectedFile(new File(this.path, s + "gml"));
				save = true;
			}
		}

		if (this.getSelectedFile().exists()) {
			int overwrite = JOptionPane.showConfirmDialog(this, this
					.getName(this.getSelectedFile())
					+ " : this file aldready exists.\n"
					+ "Do you want to overwrite it ?", "Warning",
					JOptionPane.YES_NO_OPTION);
			if (overwrite == JOptionPane.YES_OPTION) {
				super.approveSelection();
			} else {
				this.setSelectedFile(this.validFile);
			}
		} else {
			if (save) {
				super.approveSelection();
			}
		}
	}

	/**
	 * Cancels the export operation, or empty name file.
	 */
	public void cancelSelection() {
		if (this.getSelectedFile() == null) {
			JOptionPane.showMessageDialog(this,
					"You must choose a file to export your graph in !",
					"Warning", JOptionPane.WARNING_MESSAGE);
			this.setSelectedFile(this.validFile);
		} else {
			super.cancelSelection();
		}
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.graphicsButton) {
			GMLFileChooser.graphicsInfo = this.graphicsButton.isSelected();
		}
	}

	/**
	 * Adds the buttons.
	 */
	private void addTheButtons() {
		JPanel buttonPane = new JPanel(new FlowLayout());
		JLabel labelGraphics = new JLabel("Print graphics coordinates ");

		this.graphicsButton = new JRadioButton();
		this.graphicsButton.setSelected(GMLFileChooser.graphicsInfo); // is the button selected ?
		this.graphicsButton.addActionListener(this);

		buttonPane.add(labelGraphics);
		buttonPane.add(this.graphicsButton);

		this.add(buttonPane, BorderLayout.SOUTH);
	}

}
