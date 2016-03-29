package visidia.io;

import java.io.File;
import visidia.gui.graphview.GraphView;

/**
 * This class deals with input/output operations on graphs.
 */
public abstract class GraphIO implements VisidiaIO {

	/** The file to read/write. */
	protected File file = null;

	/**
	 * Instantiates a new graph input/output.
	 * 
	 * @param file the file
	 */
	protected GraphIO(File file) {
		this.file = file;
	}

	/**
	 * Loads the current file as a graph view.
	 * 
	 * @return the graph view
	 * 
	 * @see visidia.io.VisidiaIO#load()
	 */
	public abstract GraphView load();

	/* (non-Javadoc)
	 * @see visidia.simulation.graph.io.VisidiaIO#save(java.lang.Object)
	 */
	public abstract void save(Object object);

}
