package visidia.simulation;

import visidia.simulation.process.messages.MessageType;

// TODO: Auto-generated Javadoc
/**
 * This class defines several simulation constants.
 */
public final class SimulationConstants {

	/**
	 * This class handles constants relative to messages.
	 */
	public static class Messages {
		
		/** The synchronization message type. */
		public static MessageType SYNC = new MessageType("synchronization", true, java.awt.Color.blue);

		/** The labels message type. */
		public static MessageType LABE = new MessageType("labels", true, java.awt.Color.red);
		
		/** The properties message type. */
		public static MessageType PROP = new MessageType("properties", true, java.awt.Color.red);

		/** The termination message type. */
		public static MessageType TERM = new MessageType("termination", true, java.awt.Color.green);

		/** The mark message type. */
		public static MessageType MARK = new MessageType("mark", true, java.awt.Color.red);

		/** The duel message type. */
		public static MessageType DUEL = new MessageType("duel", true, java.awt.Color.orange);
	}

	/**
	 * This class handles constants relative to running mode.
	 */
	public final static class RunningMode { 
		
		/** The Constant COMMUNICATION_MESSAGES. */
		public final static int COMMUNICATION_MESSAGES = 1;

		/** The Constant COMMUNICATION_AGENTS. */
		public final static int COMMUNICATION_AGENTS = 2;

		/** The Constant NETWORK_LOCAL. */
		public final static int NETWORK_LOCAL = 4;

		/** The Constant NETWORK_REMOTE. */
		public final static int NETWORK_REMOTE = 8;

		/** The Constant PROCESSES_FIXED. */
		public final static int PROCESSES_FIXED = 16;

		/** The Constant PROCESSES_MOBILE. */
		public final static int PROCESSES_MOBILE = 32;

		/**
		 * Checks if messages mode is active.
		 * 
		 * @param mode the mode
		 * 
		 * @return true, if successful
		 */
		public static boolean messagesMode(int mode) {
			return ((mode & COMMUNICATION_MESSAGES) != 0);
		}

		/**
		 * Checks if agents mode is active.
		 * 
		 * @param mode the mode
		 * 
		 * @return true, if successful
		 */
		public static boolean agentsMode(int mode) {
			return ((mode & COMMUNICATION_AGENTS) != 0);
		}

		/**
		 * Checks if local network mode is active.
		 * 
		 * @param mode the mode
		 * 
		 * @return true, if successful
		 */
		public static boolean localMode(int mode) {
			return ((mode & NETWORK_LOCAL) != 0);
		}

		/**
		 * Checks if remote network mode is active.
		 * 
		 * @param mode the mode
		 * 
		 * @return true, if successful
		 */
		public static boolean remoteMode(int mode) {
			return ((mode & NETWORK_REMOTE) != 0);
		}

		/**
		 * Checks if fixes processes mode is active.
		 * 
		 * @param mode the mode
		 * 
		 * @return true, if successful
		 */
		public static boolean fixedMode(int mode) {
			return ((mode & PROCESSES_FIXED) != 0);
		}

		/**
		 * Checks if mobile sensors mode is active.
		 * 
		 * @param mode the mode
		 * 
		 * @return true, if successful
		 */
		public static boolean mobileMode(int mode) {
			return ((mode & PROCESSES_MOBILE) != 0);
		}

	}
	
	/**
	 * The SimulationType enumeration.
	 */
	public static enum SimulationType {
		
		/** The STANDARD type. */
		STANDARD,
		
		/** The RECORD type. */
		RECORD,
		
		/** The REPLAY type. */
		REPLAY
	}
	
	/**
	 * The SimulationStatus enumeration.
	 */
	public static enum SimulationStatus {

		/** The STARTED status. */
		STARTED, 

		/** The ABORTED status. */
		ABORTED, 

		/** The PAUSED status. */
		PAUSED, 

		/** The STOPPED status. */
		STOPPED
	}

	/**
	 * The Class PropertyStatus.
	 */
	public static class PropertyStatus {
		
		/** The Constant DISPLAYED. */
		public final static int DISPLAYED = 0;
		
		/** The Constant DISPLAYABLE. */
		public final static int DISPLAYABLE= 1;
		
		/** The Constant UNDISPLAYABLE. */
		public final static int NON_DISPLAYABLE = 2;
	}
	
}
