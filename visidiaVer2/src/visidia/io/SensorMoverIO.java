package visidia.io;

import visidia.misc.ClassIdentifier;
import visidia.simulation.process.SensorMover;

/**
 * This class deals with input operations on sensor movers.
 */
public class SensorMoverIO extends ClassIO {

	/**
	 * Instantiates a new sensor mover input/output.
	 * 
	 * @param classId the class identifier
	 */
	public SensorMoverIO(ClassIdentifier classId) {
		super(classId);
	}

	/**
	 * Loads the current file as a sensor mover.
	 * 
	 * @return the sensor mover
	 * 
	 * @see visidia.io.VisidiaIO#load()
	 */
	public SensorMover load() {
		Object obj = super.load();
		classId.setInstanceType(SensorMover.class);
		SensorMover mover = null;
		
		try {
			mover = (SensorMover) obj;
			if (mover == null) throw new NullPointerException();
		} catch (Exception e) {
			mover = (SensorMover) super.recreateInSystemClassLoader(obj);
		}

		return mover;
	}

	/**
	 * This function does nothing. A sensor mover cannot be saved.
	 * 
	 * @param object the object
	 * 
	 * @see visidia.io.VisidiaIO#save(java.lang.Object)
	 */
	public void save(Object object) {
		// nothing to be done here
	}

}
