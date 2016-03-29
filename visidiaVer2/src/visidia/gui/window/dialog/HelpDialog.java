package visidia.gui.window.dialog;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.IOException;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;

// TODO: Auto-generated Javadoc
/**
 * DialogHelp is the dialog for displaying quick help.
 */
public class HelpDialog extends VisidiaDialog {

	private static final long serialVersionUID = -1177204053600812166L;

	/**
	 * Instantiates a new help dialog.
	 * 
	 * @param owner the owner
	 */
	public HelpDialog(Frame owner) {
		super(owner, "ViSiDiA help", false); // non-modal dialog
		createGUI();
		pack();
	}

	/**
	 * Creates the GUI.
	 */
	private void createGUI() {
		JEditorPane editorPane = new JEditorPane();
		editorPane.setEditable(false);
		java.net.URL helpURL = HelpDialog.class.getResource("/VisidiaHelp.html");

		try {
			if (helpURL != null) editorPane.setPage(helpURL);
			else editorPane.setPage("../resources/VisidiaHelp.html");
		} catch (IOException e) {
		}

		//Put the editor pane in a scroll pane.
		JScrollPane editorScrollPane = new JScrollPane(editorPane);
		editorScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editorScrollPane.setPreferredSize(new Dimension(500, 290));
		editorScrollPane.setMinimumSize(new Dimension(100, 100));
		mainPanel.add(editorScrollPane, BorderLayout.CENTER);
	}

}
