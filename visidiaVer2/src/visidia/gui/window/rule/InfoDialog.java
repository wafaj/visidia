package visidia.gui.window.rule;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

// TODO: Auto-generated Javadoc
/**
 * The Class InfoDialog.
 */
public class InfoDialog extends JDialog {

	private static final long serialVersionUID = 6603995457264658944L;

	/** The text area. */
	JTextArea textArea;

	/** The owner. */
	Window owner;

	/**
	 * Instantiates a new info dialog.
	 * 
	 * @param parent the parent
	 * @param title the title
	 */
	public InfoDialog(Window parent, String title) {
		super(parent, title);
		this.owner = parent;
		// Create a text area.
		this.textArea = new JTextArea();
		this.textArea.setFont(new Font("Courier", Font.TRUETYPE_FONT, 13));
		this.textArea.setWrapStyleWord(true);
		JScrollPane areaScrollPane = new JScrollPane(this.textArea);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(400, 300));
		areaScrollPane.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(title),
						BorderFactory.createEmptyBorder(5, 5, 5, 5)), areaScrollPane.getBorder()));
		this.textArea.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createRaisedBevelBorder(),
				BorderFactory.createCompoundBorder(BorderFactory.createLoweredBevelBorder(), BorderFactory.createEmptyBorder(5, 5, 5, 5))));

		this.getContentPane().add(areaScrollPane);
		this.pack();
		this.setVisible(false);
	}

	/* (non-Javadoc)
	 * @see java.awt.Dialog#setVisible(boolean)
	 */
	public void setVisible(boolean b) {
		Dimension ownerDim = this.owner.getPreferredSize();
		this.setLocation((int) (this.owner.getX() + ownerDim.getWidth() / 2 - this.getWidth() / 2), (int) (this.owner.getY() + ownerDim.getHeight() / 5));
		super.setVisible(b);
	}

	/**
	 * Sets the text.
	 * 
	 * @param txt the new text
	 */
	public void setText(String txt) {
		this.textArea.setText(txt);
	}

	/**
	 * Gets the text.
	 * 
	 * @return the text
	 */
	public String getText() {
		return this.textArea.getText();
	}

	/**
	 * Sets the editable property.
	 * 
	 * @param b the editable property
	 */
	public void setEditable(boolean b) {
		this.textArea.setEditable(b);
	}

}
