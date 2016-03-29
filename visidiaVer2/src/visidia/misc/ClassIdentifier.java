package visidia.misc;

import java.io.File;
import java.io.Serializable;
import java.net.URL;

// TODO: Auto-generated Javadoc
/**
 * This class manages some properties about classes loaded at runtime: url, package name, class name, if the class is in a JAR file.
 */
public class ClassIdentifier implements Serializable {

	private static final long serialVersionUID = -7406909534060901769L;

	/** The Constant emptyClassId. */
	transient public final static ClassIdentifier emptyClassId = new ClassIdentifier((URL) null);
	
	/** The URL. */
	private URL url;

	/** Boolean indicating if we are dealing with a JAR file. */
	private boolean inJarFile;

	/** The class package name. */
	private String packageName;

	/** The class name. */
	private String className;

	/** The instance type. */
	private Class<?> instanceType = null;
	
	/**
	 * Instantiates a new class identifier.
	 * 
	 * @param instanceType the instance type
	 */
	public ClassIdentifier(Class<?> instanceType) {
		this((URL) null);
		this.instanceType = instanceType;
	}
	
	/**
	 * Instantiates a new class identifier.
	 * 
	 * @param url the URL
	 */
	public ClassIdentifier(URL url) {
		this.url = url;
		inJarFile = false;
		this.packageName = findPackageName();
		this.className = findClassName();		
	}	

	/**
	 * Instantiates a new class identifier.
	 * 
	 * @param url the URL
	 * @param baseDir the base directory
	 * @param className the class name
	 */
	public ClassIdentifier(URL url, String baseDir, String className) {
		this.url = url;		
		inJarFile = url.toExternalForm().startsWith("jar:");
		this.packageName = baseDir.replace("/", File.separator)+File.separator;
		this.className = className;		
	}

	/**
	 * Gets the URL.
	 * 
	 * @return the URL
	 */
	public URL getURL() {
		return url;
	}

	/**
	 * Checks if is in jar file.
	 * 
	 * @return true, if is in jar file
	 */
	public boolean isInJarFile() {
		return inJarFile;
	}

	/**
	 * Gets the package name.
	 * 
	 * @return the package name
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * Gets the class name.
	 * 
	 * @return the class name
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Gets the instance type.
	 * 
	 * @return the instance type
	 */
	public Class<?> getInstanceType() {
		return instanceType;
	}

	/**
	 * Sets the instance type.
	 */
	public void setInstanceType(Class<?> instanceType) {
		this.instanceType = instanceType;
	}
	
	/**
	 * Finds the class name.
	 * 
	 * @return the class name
	 */
	private String findClassName() {
		try {
			String fileName = new File(url.getFile()).getName();
			int index = fileName.lastIndexOf('.');
			if (index == -1) return fileName;
			return fileName.substring(0, index);
		} catch (Exception e) {
			return "";
		}
	}

	/**
	 * Finds the package name.
	 * 
	 * @return the package name
	 */
	private String findPackageName() {
		String packageName = "";
		try {
			packageName = ClassInfo.getPackage(new File(url.getFile()).getAbsolutePath());
		} catch (Exception e) {
		}

		return (packageName == null) ? "" : packageName.replace("/", File.separator)+File.separator;
	}

}
