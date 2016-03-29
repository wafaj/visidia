package visidia;

import java.awt.Container;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Enumeration;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import visidia.gui.window.VisidiaPanel;
import visidia.io.ClassIO;
import visidia.misc.ClassIdentifier;
import visidia.misc.ColorLabel;
import visidia.misc.VisidiaAppletSecurityManager;
import visidia.misc.VisidiaSettings;
import visidia.misc.colorpalette.ColorPaletteManager;
import visidia.misc.colorpalette.NewCustomColorPalette;
import visidia.simulation.process.SensorMover;

// TODO: Auto-generated Javadoc
/**
 * The VisidiaMain class manages the application startup, which can be run either as a local application or as a web application.
 */
public class VisidiaMain extends JApplet {

	private static final long serialVersionUID = 7449874851404373809L;

	/** The parent frame. */
	private static Frame parentFrame = null;

	/** The main panel. */
	public static VisidiaPanel mainPanel;

	/**
	 * Instantiates a new visidia application.
	 */
	public VisidiaMain() {
		super();
	}

	/**
	 * Find parent frame.
	 * 
	 * @return the frame
	 */
	private Frame findParentFrame() { 
		Container c = this; 
		while (c != null) { 
			if (c instanceof Frame)	return (Frame) c; // will be modal 
			c = c.getParent(); 
		} 
		return new Frame(); // won't be modal 
	} 

	/**
	 * Gets the parent frame.
	 * 
	 * @return the parent frame
	 */
	public static Frame getParentFrame() {
		return parentFrame;
	}

	/**
	 * The main method used to run the software as a local application.
	 * 
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		final JFrame mainWindow = new JFrame();
		final VisidiaMain mainApp = new VisidiaMain();

		mainWindow.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				mainApp.stop();
				mainApp.destroy();
				mainWindow.dispose();
			}
		});

		mainWindow.getContentPane().add(mainApp);

		mainApp.init();

		mainWindow.setTitle("ViSiDiA");
		mainWindow.setSize(1150, 750);
		mainWindow.setExtendedState(JFrame.MAXIMIZED_BOTH ); 
		mainWindow.setVisible(true);

		mainApp.start();
	}

	/**
	 * Initializes the applet/application, building the Graphical User Interface.
	 * The method is used when running the software both as a web application and as a local application.
	 * 
	 * @see java.applet.Applet#init()
	 */
	public void init() {
		Locale.setDefault(Locale.ENGLISH);
		try { 
			// Bug report: http://bugs.sun.com/view_bug.do?bug_id=4884480
			ResourceBundle props = ResourceBundle.getBundle("com.sun.swing.internal.plaf.basic.resources.basic");
			Enumeration<String> keys = props.getKeys();
			while (keys.hasMoreElements()) {
				String next = (String) keys.nextElement();
				String value = props.getString(next);
				UIManager.put(next, value);
			}

			UIManager.setLookAndFeel(UIManager.getLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
		}
		/*
	    UIManager.put("OptionPane.okButtonText", "OK");
	    UIManager.put("OptionPane.cancelButtonText", "Cancel");
	    UIManager.put("OptionPane.noButtonText", "No");
	    UIManager.put("OptionPane.yesButtonText", "Yes");
		 */
		parentFrame = findParentFrame();
		setSize(970, 600);
		mainPanel = new VisidiaPanel();
		getContentPane().add(mainPanel);

		// initializes default values for preferences
		VisidiaSettings settings = VisidiaSettings.getInstance();

		try {
			URL url = getCodeBase();
			settings.setDefault(VisidiaSettings.Constants.VISIDIA_BASE_URL, url.getFile());
		} catch(NullPointerException e) {
			settings.setDefault(VisidiaSettings.Constants.VISIDIA_BASE_URL, System.getProperty("user.dir"));
		}

		settings.setDefault(VisidiaSettings.Constants.SENSOR_COMMUNICATION_DISTANCE, new Integer(5));
		settings.setDefault(VisidiaSettings.Constants.SENSOR_NB_VERTICES_X, new Integer(30));
		settings.setDefault(VisidiaSettings.Constants.SENSOR_NB_VERTICES_Y, new Integer(20));
		settings.setDefault(VisidiaSettings.Constants.SENSOR_VERTEX_GAP_X, new Integer(30));
		settings.setDefault(VisidiaSettings.Constants.SENSOR_VERTEX_GAP_Y, new Integer(30));

		// identify default sensor mover
		String baseDir = "visidia/examples/algo/sensor/mover";
		settings.setDefault(VisidiaSettings.Constants.VISIDIA_SENSOR_MOVER_PATH, baseDir);
		try {
			ClassIdentifier sensorMover = ClassIO.getClassIdentifier(baseDir, "OneRandomStep");
			//JOptionPane.showMessageDialog(VisidiaMain.getParentFrame(), sensorMover.getURL());
			sensorMover.setInstanceType(SensorMover.class);
			settings.setDefault(VisidiaSettings.Constants.VISIDIA_GLOBAL_SENSOR_MOVER, sensorMover);
		} catch (Exception e) {
		}

		// TRICKY: backward slashes do not work with ClassLoader, which requires forward slashes, even under Windows!
		// ref: http://blogs.atlassian.com/developer/2006/12/how_to_use_file_separator_when.html
		settings.setDefault(VisidiaSettings.Constants.VISIDIA_ALGO_PATH, "visidia/examples/algo");
		settings.setDefault(VisidiaSettings.Constants.VISIDIA_AGENT_PATH, "visidia/examples/agent");
		settings.setDefault(VisidiaSettings.Constants.VISIDIA_AGENT_MOVER_PATH, "visidia/examples/agent/mover");

		settings.setDefault(VisidiaSettings.Constants.VERTEX_DEFAULT_LABEL, new ColorLabel("N"));

		settings.setDefault(VisidiaSettings.Constants.GML_NODE_LABEL, new Boolean(true));
		settings.setDefault(VisidiaSettings.Constants.GML_EDGE_PROPS, new Boolean(false));
		settings.setDefault(VisidiaSettings.Constants.DIRECTED_GRAPH, new Boolean(false));

		settings.setDefault(VisidiaSettings.Constants.SHOW_DISPLAYED_PROPS, new Boolean(true));
		settings.setDefault(VisidiaSettings.Constants.SHOW_VERTEX_LABEL, new Boolean(true));
		settings.setDefault(VisidiaSettings.Constants.SHOW_EDGE_LABEL_AND_WEIGHT, new Boolean(true));

		settings.setDefault(VisidiaSettings.Constants.SHOW_WEIGHT, new Boolean(false));
		settings.setDefault(VisidiaSettings.Constants.WEIGHT_NB_DECIMALS, new Integer(3));

		settings.setDefault(VisidiaSettings.Constants.CUSTOM_COLOR_PALETTE, new NewCustomColorPalette());
		settings.setDefault(VisidiaSettings.Constants.USE_CUSTOM_COLOR_PALETTE, false);
		try {
			ColorPaletteManager.getInstance().setCustomPalette((NewCustomColorPalette) settings.getObject(VisidiaSettings.Constants.CUSTOM_COLOR_PALETTE));
		} catch (Exception e){
			settings.set(VisidiaSettings.Constants.CUSTOM_COLOR_PALETTE, new NewCustomColorPalette());
			ColorPaletteManager.getInstance().setCustomPalette((NewCustomColorPalette) settings.getObject(VisidiaSettings.Constants.CUSTOM_COLOR_PALETTE));
		}
		try {
			System.setSecurityManager(new VisidiaAppletSecurityManager());
		} catch(SecurityException e) {
		}
	}

	/**
	 * Destroys the applet.
	 * The method is used when running the software as a web application.
	 * 
	 * @see java.applet.Applet#destroy()
	 */
	public void destroy() {
		quit();
	}

	/**
	 * Starts the applet execution.
	 * The method is used when running the software as a web application.
	 * 
	 * @see java.applet.Applet#start()
	 */
	public void start() {

	}

	/**
	 * Stops the applet execution.
	 * The method is used when running the software as a web application.
	 * 
	 * @see java.applet.Applet#stop()
	 */
	public void stop() {
		parentFrame.dispose();
	}

	/**
	 * Quit the application.
	 */
	public static void quit() {
		mainPanel.closeAll();
		parentFrame.dispose();
	}

}
