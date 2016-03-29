package visidia.io.gml;

import java.io.File;

import javax.swing.filechooser.FileFilter;

// TODO: Auto-generated Javadoc
/**
 * This class is a file filter for GML files (.gml extension).
 */
public class GMLGraphFileFilter extends FileFilter {

	/**
	 * Instantiates a new GML graph file filter.
	 */
	public GMLGraphFileFilter() {
		super();
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#accept(java.io.File)
	 */
	@Override
	public boolean accept(File f) {
		if (f.isDirectory()) return true;

		String filename = f.getName();
		int index = filename.lastIndexOf(".");
		if (index == -1) return false;
		if (filename.substring(index + 1).equals("gml")) return true;

		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return new String("GML graph (*.gml)");
	}

}
