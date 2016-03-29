package visidia.simulation.command;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import visidia.simulation.evtack.VisidiaEvent;

// TODO: Auto-generated Javadoc
/**
 * Command is the abstract base class to manages commands exchanged between console and processes.
 */
public abstract class Command implements Serializable {

	private static final long serialVersionUID = -4077433802024370307L;

	/**
	 * Instantiates a new command.
	 */
	protected Command() {
	}

	/**
	 * Defines if command needs synchronization.
	 * 
	 * @return true, if to be synchronized
	 */
	public abstract boolean needSynchronization();

	/**
	 * Defines if an ack is to be generated immediately.
	 * 
	 * @return true, if an ack must be generated immediately
	 */	
	public abstract boolean generateImmediateAck();

	/**
	 * Instructions to be executed before ack.
	 * 
	 * @param event the event
	 */
	public abstract void executeBeforeAck(VisidiaEvent event);

	/**
	 * Instructions to be executed after ack.
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public abstract void executeAfterAck() throws InterruptedException;

	/**
	 * Serialize.
	 * 
	 * @param out the output stream
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public abstract void serialize(ObjectOutputStream out) throws IOException;

	/**
	 * Deserialize.
	 * 
	 * @param in the input stream
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException the class not found exception
	 */
	public abstract void deserialize(ObjectInputStream in) throws IOException, ClassNotFoundException;

	/**
	 * Write object.
	 * 
	 * @param out the output stream
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		serialize(out);
	}

	/**
	 * Read object.
	 * 
	 * @param in the input stream
	 * 
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException the class not found exception
	 */
	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();		
		deserialize(in);
	}

}
