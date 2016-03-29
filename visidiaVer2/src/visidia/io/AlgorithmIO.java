package visidia.io;

import visidia.misc.ClassIdentifier;
import visidia.simulation.process.algorithm.Algorithm;

// TODO: Auto-generated Javadoc
/**
 * This class deals with input operations on algorithms.
 */
public class AlgorithmIO extends ClassIO {

	/**
	 * Instantiates a new algorithm input/output.
	 * 
	 * @param classId the class identifier
	 */
	public AlgorithmIO(ClassIdentifier classId) {
		super(classId);
	}

	/**
	 * Loads the current file as an algorithm.
	 * 
	 * @return the algorithm
	 * 
	 * @see visidia.io.VisidiaIO#load()
	 */
	public Algorithm load() {
		Object obj = super.load();
		Algorithm algo = null;
		
		try {
			algo = (Algorithm) obj;
			if (algo == null) throw new NullPointerException();
		} catch (Exception e) {
			algo = (Algorithm) super.recreateInSystemClassLoader(obj);
		}
		
		return algo;
	}

	/**
	 * This function does nothing. An algorithm cannot be saved.
	 * 
	 * @param object the object
	 * 
	 * @see visidia.io.VisidiaIO#save(java.lang.Object)
	 */
	public void save(Object object) {
		// nothing to be done here
	}

}
