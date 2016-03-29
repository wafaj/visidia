package visidia.misc.property.cell.handler;

import visidia.io.SensorMoverIO;
import visidia.misc.VisidiaSettings;
import visidia.simulation.process.SensorMover;

// TODO: Auto-generated Javadoc
/**
 * SensorMoverCellHandler is a concrete handler for ClassIdentifier objects related to a SensorMover.
 */
public class SensorMoverCellHandler extends ClassIdentifierCellHandler {

	/**
	 * Instantiates a new sensor mover cell handler.
	 */
	public SensorMoverCellHandler() {
		super();
	}

	/* (non-Javadoc)
	 * @see visidia.misc.property.cell.ClassIdentifierCellHandler#getPath()
	 */
	public String getPath() {
		return VisidiaSettings.getInstance().getString(VisidiaSettings.Constants.VISIDIA_SENSOR_MOVER_PATH);
	}

	/* (non-Javadoc)
	 * @see visidia.misc.property.cell.ClassIdentifierCellHandler#getLoaderType()
	 */
	public Class<?> getLoaderType() {
		return SensorMoverIO.class;
	}

	/* (non-Javadoc)
	 * @see visidia.misc.property.cell.ClassIdentifierCellHandler#getObjectType()
	 */
	public Class<?> getObjectType() {
		return SensorMover.class;
	}

	/* (non-Javadoc)
	 * @see visidia.misc.property.cell.ClassIdentifierCellHandler#matchType(java.lang.Class)
	 */
	public boolean matchType(Class<?> instanceType) {
		return (instanceType == SensorMover.class);
	}

}
