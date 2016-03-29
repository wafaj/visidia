package visidia.io;

/**
 * This interface deals with input/output operations.
 */
public interface VisidiaIO {

	/**
	 * Loads an object.
	 * 
	 * @return the object
	 */
	public Object load();

	/**
	 * Saves an object.
	 * 
	 * @param object the object
	 */
	public void save(Object object);
	
}
