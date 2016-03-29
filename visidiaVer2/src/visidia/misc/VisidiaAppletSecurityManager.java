package visidia.misc;

import java.security.Permission;

// TODO: Auto-generated Javadoc
/**
 * This class redefines a security policy for the application when running as an applet.
 */
public class VisidiaAppletSecurityManager extends SecurityManager {

	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkRead(java.lang.String)
	 */
	public void checkRead(String file) {
	}

	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkWrite(java.lang.String)
	 */
	public void checkWrite(String file) {
		if (file.contains(VisidiaSettings.Constants.VISIDIA_BASE_URL)) throw new SecurityException();
	}

	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkCreateClassLoader()
	 */
	public void checkCreateClassLoader() {
	}

	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkAccess(java.lang.Thread)
	 */
	public void checkAccess(Thread t) {
	}

	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkPackageAccess(java.lang.String)
	 */
	public void checkPackageAccess(String pkg) {
	}

	/* (non-Javadoc)
	 * @see java.lang.SecurityManager#checkPermission(java.security.Permission)
	 */
	public void checkPermission(Permission perm) {
	}

}