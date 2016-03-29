package visidia.simulation;

// TODO: Auto-generated Javadoc
/**
 * VisidiaThreadGroup is a thread group in visidia simulation mode.
 * 
 * This class overrides his super class uncaughtException() method to handle
 * uncaught exceptions from threads.
 */
public class VisidiaThreadGroup extends ThreadGroup {

	/**
	 * Instantiates a new thread group.
	 * 
	 * @param name the name
	 */
	public VisidiaThreadGroup(String name) {
		super(name);
	}

	/**
	 * Instantiates a new thread group.
	 * 
	 * @param parent the parent
	 * @param name the name
	 */
	public VisidiaThreadGroup(ThreadGroup parent, String name) {
		super(parent, name);
	}

	/**
	 * Handle uncaught exceptions raised from this thread group children.
	 * 
	 * @param t the thread
	 * @param e the exception
	 */
	public void uncaughtException(Thread t, Throwable e) {
		// The simulationAbortError is thrown to force threads
		// go out their run method. Other Throwable should
		// be handled normally.
		if (!(e instanceof SimulationAbortError)) {
			super.uncaughtException(t, e);
		}
	}

}