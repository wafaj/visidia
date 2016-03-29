package visidia.misc;

import java.io.File;
import java.net.URL;

// TODO: Auto-generated Javadoc
/**
 * This class manages a unique file handler.
 */
public class FileHandler {

	/** The instance. */
	private static FileHandler instance = new FileHandler();

	/**
	 * Instantiates a new file handler.
	 */
	private FileHandler() {
	}

	/**
	 * Gets the single instance of FileHandler.
	 * 
	 * @return single instance of FileHandler
	 */
	public static FileHandler getInstance() {
		return instance;
	}

	/**
	 * Recursive file search.
	 * 
	 * @param f the current file or directory
	 * @param name the file name
	 * 
	 * @return the file, or null if not found
	 */
	private File recursiveFileSearch(File f, String name) {
		if (f.getName().equals(name)) return f;

		File[] children = f.listFiles();

		if (children != null) {
			for (File child:children) {
				File found = recursiveFileSearch(child, name);
				if (found != null) return found;
			}
		}

		return null;
	}

	/**
	 * Searches a file.
	 * 
	 * @param name the name
	 * 
	 * @return the file, or null if not found
	 */
	public File searchFile(String name) {
		File f = recursiveFileSearch(new File("."), name+".class");
		return f.getAbsoluteFile();
	}

	/**
	 * Gets the root directory.
	 * 
	 * @param pathToReach the path to reach
	 * 
	 * @return the root directory
	 */
	public static String getRootDirectory(String pathToReach) {
		String rootDir = "";

		try {
			URL url = FileHandler.class.getProtectionDomain().getCodeSource().getLocation();
			String urlFile = url.toExternalForm();
			if (urlFile.endsWith(".jar")) {
				String jarFileName = urlFile.replace("%20", " ");
				// TODO: utiliser URLDecoder pour supprimer les %20
				if (jarFileName.charAt(0) == '/' && jarFileName.charAt(2) == ':') // Windows leading slash
					jarFileName = jarFileName.substring(1);
				// BUG: problem with accentuated characters in file names
				if (jarFileName.startsWith("file:")) jarFileName = jarFileName.substring(5);
				rootDir = jarFileName;
			} else {
				rootDir = new File(FileHandler.class.getResource("/" + pathToReach).toURI()).getAbsolutePath();
				rootDir = rootDir.replace("%20", " ");
				// TODO: utiliser URLDecoder pour supprimer les %20
			}
		} catch (Exception e) {
			return null;
		}

		int index = rootDir.lastIndexOf(pathToReach.replace('/', File.separatorChar));
		rootDir = (index > 0) ? rootDir.substring(0, index-1) : rootDir;
		
		return rootDir;
	}
	
}
