package visidia.misc.property;

import java.util.EventListener;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving propertyTableViewerChange events.
 * The class that is interested in processing a propertyTableViewerChange
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addPropertyTableViewerChangeListener<code> method. When
 * the propertyTableViewerChange event occurs, that object's appropriate
 * method is invoked.
 */
public interface PropertyTableViewerChangeListener extends EventListener {

	/**
	 * Updated.
	 */
	void updated();

}
