package visidia.misc.property.cell.handler;

// TODO: Auto-generated Javadoc
/**
 * ClassIdentifierCellHandler is the abstract base class for handling requests on ClassIdentifier instanceTypes.
 * It is involved in a pattern "chain of responsability".
 */
public abstract class ClassIdentifierCellHandler {

	/** The successor. */
	protected ClassIdentifierCellHandler successor;

	/**
	 * Gets the successor.
	 * 
	 * @return the successor
	 */
	public ClassIdentifierCellHandler getSuccessor() {
		return successor;
	}
	
	/**
	 * Sets the successor.
	 * 
	 * @param successor the new successor
	 */
	public void setSuccessor(ClassIdentifierCellHandler successor) {
		this.successor = successor;
	}
	
	/**
	 * Gets the path.
	 * 
	 * @return the path
	 */
	public abstract String getPath();

	/**
	 * Gets the loader type.
	 * 
	 * @return the loader type
	 */
	public abstract Class<?> getLoaderType();

	/**
	 * Gets the object type.
	 * 
	 * @return the object type
	 */
	public abstract Class<?> getObjectType();

	/**
	 * Match type.
	 * 
	 * @param instanceType the instance type
	 * 
	 * @return true, if successful
	 */
	public abstract boolean matchType(Class<?> instanceType);

}
