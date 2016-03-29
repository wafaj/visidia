package visidia.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Hashtable;

import visidia.misc.ClassIdentifier;
import visidia.misc.FileHandler;

// TODO: Auto-generated Javadoc
/**
 * This class deals with input operations on java classes.
 */
public class ClassIO implements VisidiaIO {

	/** The class identifier. */
	ClassIdentifier classId;
		
	/**
	 * Instantiates a new class input/output.
	 * 
	 * @param classId the class identifier
	 */
	public ClassIO(ClassIdentifier classId) {
		this.classId = classId;
	}

	/**
	 * Gets the class identifier.
	 * 
	 * @param path the path
	 * @param className the class name
	 * 
	 * @return the class identifier
	 */
	public static ClassIdentifier getClassIdentifier(String path, String className) {
		String rootDir = FileHandler.getRootDirectory(path);
		if (rootDir == null) return null;
		boolean inJarFile = rootDir.endsWith(".jar");
		
		try {
			if (inJarFile) {
				String jarFileName = "jar:" + (rootDir.startsWith("http://") ? "" : "file:") + rootDir + "!/";
				URL url = new URL(jarFileName);
				return new ClassIdentifier(url, path, className);
			} else {
				File f = new File(rootDir+File.separator+path+File.separator+className+".class");
				return new ClassIdentifier(new URL("file:"+f.getAbsolutePath()));
			}
		} catch (Exception e) {
		}
		
		return null;
	}
	
	/**
	 * Loads a class from its name and path and returns an instance of it.
	 * 
	 * @param path the path
	 * @param className the class name
	 * 
	 * @return the object
	 */
	public static Object load(String path, String className) {
		ClassIdentifier classId = getClassIdentifier(path, className);
		if (classId == null) return null;

		Object object = null;
		try {
			ClassIO loader = new ClassIO(classId);
			Object obj = loader.load();
			object = loader.recreateInSystemClassLoader(obj);
			classId.setInstanceType(object.getClass());
		} catch (Exception e) {
			object = null;
		}

		return object;
	}
	
	/**
	 * Loads the current file as an object.
	 * 
	 * @return the object
	 * 
	 * @see visidia.io.VisidiaIO#load()
	 */
	public Object load() {
		Object object = null;

		try {
			String packageName = classId.getPackageName();
			String className = classId.getClassName();
			if (classId.isInJarFile()) {
				URLClassLoader loader = new URLClassLoader(new URL[] { classId.getURL() });
				Class<?> c = loader.loadClass(packageName.replace(File.separator,  ".")+className);
				object = c.newInstance();
			} else {				
				FileClassLoader loader = new FileClassLoader(getDirName());
				Class<?> c = loader.loadClass(packageName.replace(File.separator,  ".")+className, true);
				object = c.newInstance();
			}
			//classId.setInstanceType(object.getClass());
		} catch (Exception e) {
		}

		return object;
	}

	/**
	 * This function does nothing. A class cannot be saved.
	 * 
	 * @param object the object
	 * 
	 * @see visidia.io.VisidiaIO#save(java.lang.Object)
	 */
	public void save(Object object) {
		// nothing to be done here
	}

	/**
	 * Recreate in system class loader.
	 * 
	 * @param obj the object
	 * 
	 * @return the object
	 */
	public Object recreateInSystemClassLoader(Object obj) {
		// A direct cast, such as
		//      myInstance = (MyClass) super.load();
		// is not possible because the MyClass class loaded by the system ClassLoader is
		// not equivalent to the MyClass class that would have created myInstance.
		// To work around this problem, the myInstance object is serialized into a byte array, then
		// deserialized back into an Object. This has the effect of recreating the myInstance object in
		// the system class loader, and the cast to MyClass is thus allowed.
		// This trick implies that the MyClass class (as well as the classes myClass owns an instance)
		// must implement the Serializable interface.

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(obj);
		
			ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
			ObjectInputStream ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Gets the directory name.
	 * 
	 * @return the directory name
	 */
	private String getDirName() {
		String absolutePath = new File(classId.getURL().getFile()).getAbsolutePath();
		String packageName = classId.getPackageName();
		if (! packageName.isEmpty()) {
			int index = absolutePath.lastIndexOf(packageName);
			return (index > 0) ? absolutePath.substring(0, index) : "";
		}

		int index = absolutePath.lastIndexOf(File.separator);
		return (index > 0) ? absolutePath.substring(0, index) : "";
	}

	/**
	 * The Class FileClassLoader.
	 */
	private class FileClassLoader extends ClassLoader {

		/** The directory name. */
		String dirName;

		/** The cache. */
		Hashtable<String, Class<?>> cache = new Hashtable<String, Class<?>>();

		/**
		 * Instantiates a new file class loader.
		 * 
		 * @param dirName the directory name
		 */
		public FileClassLoader(String dirName) {
			super();
			this.dirName = dirName;
		}

		/**
		 * Load class data.
		 * 
		 * @param className the class name
		 * 
		 * @return the class byte code
		 * 
		 * @throws ClassNotFoundException the class not found exception
		 */
		private byte [] loadClassData(String className) throws ClassNotFoundException {
			String fileName = dirName + (dirName.endsWith(File.separator) ? "" : File.separator) + className.replace('.', File.separatorChar) + ".class";
			try {
				File file = new File(fileName);
				if (file.length() > Integer.MAX_VALUE) throw new ClassNotFoundException();
				int len = (int) file.length();
				byte[] result = new byte[len];
				FileInputStream  input = new FileInputStream(file);
				int offset = 0;
				int nb = 0;
				while (len != 0 && ((nb = input.read(result, offset, len)) != -1)) {
					offset += nb;
					len -= nb;
				}
				input.close();
				return result;
			} catch(Exception e) {
				throw new ClassNotFoundException();
			}
		}

		/* (non-Javadoc)
		 * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
		 */
		public synchronized Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
			try {
				return findSystemClass(name);
			} catch(Exception e) {
			}

			Class<?> c = (Class<?>) cache.get(name);
			if (c == null) {
				byte data[] = loadClassData(name);
				c = defineClass(name, data, 0, data.length);
				cache.put(name, c);
			}
			if (resolve) resolveClass(c);
			return c;
		}
	}

}