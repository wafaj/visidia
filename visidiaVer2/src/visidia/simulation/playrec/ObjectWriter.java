package visidia.simulation.playrec;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

// TODO: Auto-generated Javadoc
/**
 * The Class ObjectWriter.
 */
public class ObjectWriter {

	/** The file output stream. */
	private FileOutputStream fileOS;

	/** The object output stream. */
	private ObjectOutputStream objectOS;

	/**
	 * Instantiates a new object writer.
	 */
	public ObjectWriter() {
	}

	/**
	 * Open.
	 * 
	 * @param file the file
	 * 
	 * @return true, if successful
	 */
	public synchronized boolean open(File file) {
		try {
			this.fileOS = new FileOutputStream(file);
			this.objectOS = new ObjectOutputStream(this.fileOS);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Close.
	 */
	public synchronized void close() {
		try {
			this.objectOS.close();
			this.fileOS.close();
		} catch (Exception e) {
		}
	}

	/**
	 * Write object.
	 * 
	 * @param object the object
	 */
	public synchronized void writeObject(Object object) {
		try {
			this.objectOS.writeObject(object);
			this.objectOS.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
