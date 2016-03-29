package visidia.gui.window.dialog;

import java.awt.BorderLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

// TODO: Auto-generated Javadoc
/**
 * VisidiaDialog is the abstract base class for dialogs in ViSiDiA.
 */
public abstract class VisidiaDialog extends JDialog {

	private static final long serialVersionUID = -5825890413819666800L;

	/** The main panel. */
	protected JPanel mainPanel;

	/** The button box. */
	protected Box buttonBox;
	
	/**
	 * Instantiates a new ViSiDiA dialog.
	 * 
	 * @param owner the owner
	 * @param title the title
	 * @param modal the window modality
	 */
	protected VisidiaDialog(Frame owner, String title, boolean modal) {
		super(owner, title, modal);
		mainPanel = new JPanel(new BorderLayout());

		buttonBox = Box.createHorizontalBox(); // allow subclasses to add buttons
		
		// Close button
		JButton buttonClose = new JButton("Close");
		buttonClose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				dispose();
			}
		});
		buttonBox.add(buttonClose);

		mainPanel.add(buttonBox, BorderLayout.PAGE_END);

		setContentPane(mainPanel);

		//Handle window closing correctly.
		//setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		setLocationRelativeTo(owner);
		setResizable(true);
	}

}
