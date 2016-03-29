package visidia.graph;

import visidia.io.ClassIO;
import visidia.misc.ClassIdentifier;
import visidia.misc.VisidiaSettings;
import visidia.misc.property.VisidiaProperty;
import visidia.simulation.process.SensorMover;

// TODO: Auto-generated Javadoc
/**
 * This class manages a sensor as a vertex belonging to a graph G1, and moving along edges of a graph G2.
 */
public class Sensor extends Vertex {

	private static final long serialVersionUID = 6435241491772129642L;

	transient private static final String PROPERTY_SENSOR_MOVER_TEXT = "sensorMover";
	
	/** The support vertex. */
	private SupportVertex supportVertex = null;

	/**
	 * Instantiates a new sensor.
	 * 
	 * @param id the id
	 */
	public Sensor(int id) {
		super(id);
		setSensorMover(null);
	}

	/* (non-Javadoc)
	 * @see visidia.graph.Vertex#clone()
	 */
	public Object clone() {
		Sensor copy = (Sensor) super.clone();
		copy.supportVertex = (SupportVertex) supportVertex.clone();
		// set sensor mover
		ClassIdentifier classId = (ClassIdentifier) super.getVisidiaProperty(PROPERTY_SENSOR_MOVER_TEXT).getValue();
		copy.setProperty(new VisidiaProperty(PROPERTY_SENSOR_MOVER_TEXT, classId, VisidiaProperty.Tag.PERSISTENT_PROPERTY));
		
		return copy;
	}
	
	/**
	 * Gets the support vertex.
	 * 
	 * @return the support vertex
	 */
	public SupportVertex getSupportVertex() {
		return supportVertex;
	}

	/**
	 * Sets the support vertex.
	 * 
	 * @param supportVertex the new support vertex
	 */
	public void setSupportVertex(SupportVertex supportVertex) {
		this.supportVertex = supportVertex;
	}

	/**
	 * Reset properties.
	 */
	public void resetProperties() {
		super.resetProperties();
		setSensorMover(null);
	}

	/* (non-Javadoc)
	 * @see visidia.graph.Vertex#setProperty(visidia.misc.property.VisidiaProperty)
	 */
	public Object setProperty(VisidiaProperty property) {
		Object key = property.getKey();
		Object value = property.getValue();
		if (key.equals(PROPERTY_SENSOR_MOVER_TEXT)) {
			VisidiaProperty prop = super.getVisidiaProperty(key);
			if (value instanceof ClassIdentifier)
				super.setProperty(property);
			
			return prop;
		}

		return super.setProperty(property);
	}

	/**
	 * Gets the sensor mover.
	 * 
	 * @return the sensor mover
	 */
	public SensorMover getSensorMover() {
		ClassIdentifier classId = (ClassIdentifier) super.getVisidiaProperty(PROPERTY_SENSOR_MOVER_TEXT).getValue();
		if (classId == null || classId.equals(ClassIdentifier.emptyClassId)) return null;

		//return (SensorMover) new ClassIO(classId).load();
		
		ClassIO loader = new ClassIO(classId); 
		Object obj = loader.load();
		SensorMover mover = null;

		try {
			mover = (SensorMover) obj;
			if (mover == null) throw new NullPointerException();
		} catch (Exception e) {
			mover = (SensorMover) loader.recreateInSystemClassLoader(obj);
		}
		
		return mover;
	}

	/**
	 * Sets the sensor mover.
	 * 
	 * @param name the name
	 */
	public void setSensorMover(String name) {
		VisidiaSettings settings = VisidiaSettings.getInstance();
		ClassIdentifier classId = null;
		
		if (name != null) {
			String visidiaSensorMoverPath = settings.getString(VisidiaSettings.Constants.VISIDIA_SENSOR_MOVER_PATH);
			classId = ClassIO.getClassIdentifier(visidiaSensorMoverPath, name);
			classId.setInstanceType(SensorMover.class);
		}
		
		if (classId == null) {
			classId = (ClassIdentifier) settings.getObject(VisidiaSettings.Constants.VISIDIA_GLOBAL_SENSOR_MOVER);
			classId.setInstanceType(SensorMover.class);
		}

		if (classId == null) {
			classId = new ClassIdentifier(SensorMover.class);
		}

		super.setProperty(new VisidiaProperty(PROPERTY_SENSOR_MOVER_TEXT, classId, VisidiaProperty.Tag.PERSISTENT_PROPERTY));
	}

}
