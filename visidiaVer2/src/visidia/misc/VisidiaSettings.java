package visidia.misc;

import java.util.prefs.Preferences;

// TODO: Auto-generated Javadoc
/**
 * The Class VisidiaSettings.
 */
public class VisidiaSettings {

	/**
	 * The Class Constants.
	 */
	public class Constants {
		
		/** The Constant VISIDIA_BASE_URL. */
		public final static String VISIDIA_BASE_URL = "visidiaBaseURL"; // [String]
		
		/** The Constant SENSOR_COMMUNICATION_DISTANCE. */
		public final static String SENSOR_COMMUNICATION_DISTANCE = "sensorCommunicationDistance"; // [Integer]
		
		/** The Constant SENSOR_NB_VERTICES_X. */
		public final static String SENSOR_NB_VERTICES_X = "sensorNbVerticesX"; // [Integer]
		
		/** The Constant SENSOR_NB_VERTICES_Y. */
		public final static String SENSOR_NB_VERTICES_Y = "sensorNbVerticesY"; // [Integer]
		
		/** The Constant SENSOR_VERTEX_GAP_X. */
		public final static String SENSOR_VERTEX_GAP_X = "sensorVertexGapX"; // [Integer]
		
		/** The Constant SENSOR_VERTEX_GAP_Y. */
		public final static String SENSOR_VERTEX_GAP_Y = "sensorVertexGapY"; // [Integer]
		
		/** The Constant VISIDIA_GLOBAL_SENSOR_MOVER. */
		public final static String VISIDIA_GLOBAL_SENSOR_MOVER = "visidiaGlobalSensorMover"; // [ClassIdentifier]
		
		/** The Constant VISIDIA_SENSOR_MOVER_PATH. */
		public final static String VISIDIA_SENSOR_MOVER_PATH = "visidiaSensorMoverPath"; // [String]

		/** The Constant VISIDIA_ALGO_PATH. */
		public final static String VISIDIA_ALGO_PATH = "visidiaAlgoPath"; // [String]
		
		/** The Constant VISIDIA_AGENT_PATH. */
		public final static String VISIDIA_AGENT_PATH = "visidiaAgentPath"; // [String]
		
		/** The Constant VISIDIA_AGENT_MOVER_PATH. */
		public final static String VISIDIA_AGENT_MOVER_PATH = "visidiaAgentMoverPath"; // [String]
		
		/** The Constant USER_FILE_PATHS. */
		public final static String USER_FILE_PATHS = "userFilePaths"; // [Vector<String>]

		/** The Constant VERTEX_DEFAULT_LABEL. */
		public final static String VERTEX_DEFAULT_LABEL = "vertexDefaultLabel"; // [ColorLabel]
				
		/** The Constant SHOW_VERTEX_LABEL. */
		public final static String SHOW_VERTEX_LABEL = "showVertexLabel"; // [Boolean]
		
		/** The Constant SHOW_EDGE_LABEL_AND_WEIGHT. */
		public final static String SHOW_EDGE_LABEL_AND_WEIGHT = "showEdgeLabelAndWeight"; // [Boolean]
		
		/** The Constant SHOW_WEIGHT. */
		public final static String SHOW_WEIGHT = "showWeight"; // [Boolean]
		
		/** The Constant SHOW_DISPLAYED_PROPS. */
		public final static String SHOW_DISPLAYED_PROPS = "showDisplayedProps"; //[Boolean]
		
		/** The Constant WEIGHT_NB_DECIMALS. */
		public final static String WEIGHT_NB_DECIMALS = "weightNbDecimals"; // [Integer]
		
		/** The Constant CUSTOM_COLOR_PALETTE. */
		public final static String CUSTOM_COLOR_PALETTE = "customColorPalette"; // [ColorPalette]
		
		/** The Constant USE_CUSTOM_COLOR_PALETTE. */
		public final static String USE_CUSTOM_COLOR_PALETTE = "useCustomColorPalette"; // [Boolean]


		/** The Constant GML_NODE_LABEL. */
		public final static String GML_NODE_LABEL = "GMLNodeLabel"; // [Boolean]
		
		/** The Constant GML_EDGE_PROP. */
		public final static String GML_EDGE_PROPS = "GMLEdgeProps"; // [Boolean]
		
		/** The Constant DIRECTED_GRAPH. */
		public final static String DIRECTED_GRAPH = "directedGraph"; // [Boolean]
		
	}
	
	/** The Constant PRODUCT_NAME. */
	private final static String PRODUCT_NAME = "ViSiDiA";
	
	/** The instance. */
	private static VisidiaSettings instance = new VisidiaSettings();

	/** The visidia preferences. */
	private Preferences prefsVisidia = null;
	
	/** The visidia default preferences. */
	private Preferences prefsVisidiaDefault = null;

	/** The visidia user preferences. */
	private Preferences prefsVisidiaUser = null;

	/**
	 * Instantiates a new visidia settings.
	 */
	private VisidiaSettings() {
		prefsVisidia = Preferences.userRoot().node(PRODUCT_NAME);
		prefsVisidiaUser = prefsVisidia.node("User");
		prefsVisidiaDefault = prefsVisidia.node("Default");
	}

	/**
	 * Gets the single instance of VisidiaSettings.
	 * 
	 * @return single instance of VisidiaSettings
	 */
	public static VisidiaSettings getInstance() {
		return instance;
	}
	
	/**
	 * Gets the default boolean value associated with key.
	 * 
	 * @param key the key
	 * 
	 * @return the default boolean value associated with key, or false if no value is associated with key
	 */
	public boolean getDefaultBoolean(String key) {
		return prefsVisidiaDefault.getBoolean(key, new Boolean(false));
	}

	/**
	 * Gets the default double value associated with key.
	 * 
	 * @param key the key
	 * 
	 * @return the default double value associated with key, or 0 if no value is associated with key
	 */
	public double getDefaultDouble(String key) {
		return prefsVisidiaDefault.getDouble(key, new Double(0));
	}

	/**
	 * Gets the default integer value associated with key.
	 * 
	 * @param key the key
	 * 
	 * @return the default integer value associated with key, or 0 if no value is associated with key
	 */
	public int getDefaultInt(String key) {
		return prefsVisidiaDefault.getInt(key, new Integer(0));
	}

	/**
	 * Gets the default string associated with key.
	 * 
	 * @param key the key
	 * 
	 * @return the default string associated with key, or the empty string if no value is associated with key
	 */
	public String getDefaultString(String key) {
		return prefsVisidiaDefault.get(key, new String(""));
	}

	/**
	 * Gets the default object associated with key.
	 * 
	 * @param key the key
	 * 
	 * @return the default object associated with key, or null if no object is associated with key
	 */
	public Object getDefaultObject(String key) {
		try {
			return PrefObj.getObject(prefsVisidiaDefault, key);
		} catch (Exception e1) {
			return null;
		}
	}

	/**
	 * Sets the default value associated with key.
	 * 
	 * @param key the key
	 * @param value the value
	 */
	public void setDefault(String key, Object value) {
		setPreferences(prefsVisidiaDefault, key, value);
	}
	
	/**
	 * Gets the boolean value associated with key.
	 * 
	 * @param key the key
	 * 
	 * @return the boolean value associated with key, or the default value if no value is associated with key
	 */
	public boolean getBoolean(String key) {
		return prefsVisidiaUser.getBoolean(key, getDefaultBoolean(key));
	}

	/**
	 * Gets the double value associated with key.
	 * 
	 * @param key the key
	 * 
	 * @return the double value associated with key, or the default value if no value is associated with key
	 */
	public double getDouble(String key) {
		return prefsVisidiaUser.getDouble(key, getDefaultDouble(key));
	}

	/**
	 * Gets the integer value associated with key.
	 * 
	 * @param key the key
	 * 
	 * @return the integer value associated with key, or the default value if no value is associated with key
	 */
	public int getInt(String key) {
		return prefsVisidiaUser.getInt(key, getDefaultInt(key));
	}

	/**
	 * Gets the string associated with key.
	 * 
	 * @param key the key
	 * 
	 * @return the string associated with key, or the default value if no value is associated with key
	 */
	public String getString(String key) {
		return prefsVisidiaUser.get(key, getDefaultString(key));
	}

	/**
	 * Gets the object associated with key.
	 * 
	 * @param key the key
	 * 
	 * @return the object
	 */
	public Object getObject(String key) {
		try {
			return PrefObj.getObject(prefsVisidiaUser, key);
		} catch (Exception e) {
			return getDefaultObject(key);
		}
	}

	/**
	 * Sets the value associated with key.
	 * 
	 * @param key the key
	 * @param value the value
	 */
	public void set(String key, Object value) {
		setPreferences(prefsVisidiaUser, key, value);
	}
	
	/**
	 * Sets the preferences.
	 * 
	 * @param preferences the preferences
	 * @param key the key
	 * @param object the object
	 */
	private void setPreferences(Preferences preferences, String key, Object object) {
		if (object instanceof Boolean)
			preferences.putBoolean(key, ((Boolean) object).booleanValue());
		else if (object instanceof Double)
			preferences.putDouble(key, ((Double) object).doubleValue());
		else if (object instanceof Integer)
			preferences.putInt(key, ((Integer) object).intValue());
		else if (object instanceof String)
			preferences.put(key, (String) object);
		else {
			try {
				PrefObj.putObject(preferences, key, object);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Load settings.
	 */
	/*
	public void load(String filename) {
		try {
			FileInputStream fis = new FileInputStream(filename);
			Preferences.importPreferences(fis);
			fis.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (InvalidPreferencesFormatException e) {
		}
	}
*/
	/**
	 * Save settings.
	 */
	/*
	public void save(String filename) {
		FileOutputStream fos;
		try {
			fos = new FileOutputStream(filename);
			prefsVisidia.exportSubtree(fos);
			fos.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		} catch (BackingStoreException e) {
		}
	}
*/
	
}
