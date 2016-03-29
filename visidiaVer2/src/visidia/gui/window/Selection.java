package visidia.gui.window;

import java.util.Enumeration;
import java.util.Vector;
import visidia.gui.graphview.GraphItemView;

/**
 * Selection represents a list of selected graph items.
 */
public class Selection {

	/** The selected items. */
	private Vector<GraphItemView> selectedItems = null;

	/**
	 * Instantiates a new selection.
	 */
	Selection() {
		selectedItems = new Vector<GraphItemView>(10, 10);
	}

	/**
	 * Checks if is empty.
	 * 
	 * @return true, if is empty
	 */
	public boolean isEmpty() {
		return selectedItems.isEmpty();
	}

	/**
	 * Clear the selection.
	 */
	public void clear() {
		Enumeration<GraphItemView> items = selectedItems.elements();
		while (items.hasMoreElements()) items.nextElement().activate();
		selectedItems.clear();
	}

	/**
	 * Adds the element.
	 * 
	 * @param item the item
	 */
	public void addElement(GraphItemView item) {
		if (item != null) {
			item.select();
			selectedItems.addElement(item);
		}
	}

	/**
	 * Determines if element is already in the selection.
	 * 
	 * @param element the element
	 * 
	 * @return true, if successful
	 */
	public boolean contains(Object element) {
		return selectedItems.contains(element);
	}

	/**
	 * Removes the element.
	 * 
	 * @param element the element
	 * 
	 * @return true, if successful
	 */
	public boolean removeElement(Object element) {
		if (element != null)
			((GraphItemView) element).activate();
		return selectedItems.remove(element);
	}

	/**
	 * Gets the selected elements.
	 * 
	 * @return an enumeration the selected items
	 */
	public Enumeration<GraphItemView> elements() {
		return selectedItems.elements();
	}
	
	/**
	 * Gets the number of selected items.
	 * 
	 *@return the number of selected items
	 */
	public int size() {
		return selectedItems.size();
	}

}
