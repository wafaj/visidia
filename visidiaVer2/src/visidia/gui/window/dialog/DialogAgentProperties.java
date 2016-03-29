package visidia.gui.window.dialog;

import java.awt.Frame;
import javax.swing.JScrollPane;

import visidia.misc.property.PropertyTableViewer;
import visidia.simulation.process.agent.Agent;

// TODO: Auto-generated Javadoc
/**
 * This class is a modal dialog used to display agent properties.
 */
public class DialogAgentProperties extends VisidiaDialog {

	private static final long serialVersionUID = -362569679986294545L;

	/**
	 * Instantiates a new dialog agent properties.
	 * 
	 * @param owner the owner
	 * @param agent the agent
	 */
	public DialogAgentProperties(Frame owner, Agent agent) {
		super(owner, "Agent properties", true); // modal dialog
		createGUI(agent);
		pack();
	}

	/**
	 * Creates the GUI.
	 * 
	 * @param agent the agent
	 */
	private void createGUI(Agent agent) {
		PropertyTableViewer viewer = new PropertyTableViewer(agent);
		JScrollPane scrollPane = new JScrollPane(viewer);
		scrollPane.setBounds(viewer.getBounds());

		mainPanel.add(scrollPane);
	}

}
