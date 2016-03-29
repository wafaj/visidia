package visidia.io;

import java.io.File;

import javax.swing.filechooser.FileFilter;

// TODO: Auto-generated Javadoc
/**
 * This class is a file filter for compiled Java classes (.class extension).
 */
public class ClassFileFilter extends FileFilter {

	/**
	 * Instantiates a new class file filter.
	 */
	public ClassFileFilter() {
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

		if ((index > 0) && (index < filename.length() - 1)) {
			String extension = filename.substring(index + 1).toLowerCase();
			return (extension.equals("class"));
		}

		return false;
	}

	/* (non-Javadoc)
	 * @see javax.swing.filechooser.FileFilter#getDescription()
	 */
	@Override
	public String getDescription() {
		return new String("Compiled Java class (*.class)");
	}

}
