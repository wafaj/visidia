package visidia.simulation.playrec;

import java.io.EOFException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JOptionPane;

import visidia.VisidiaMain;
import visidia.simulation.Console;
import visidia.simulation.evtack.VisidiaAck;
import visidia.simulation.evtack.VisidiaEvent;

// TODO: Auto-generated Javadoc
/**
 * The Class ReplayInfo.
 */
public class ReplayInfo {

	/** The messages. */
	private Vector<Object> messages = new Vector<Object>();

	/** The index. */
	volatile int index = 0;

	/**
	 * Instantiates a new replay info.
	 */
	public ReplayInfo() {
	}

	/**
	 * Read the input stream containing replay info.
	 * 
	 * @param objectIS the object is
	 */
	public synchronized boolean read(ObjectInputStream objectIS) {
		try {
			while (true) {
				Object o = null;
				o = objectIS.readObject();
				if (o instanceof VisidiaAck) {
					VisidiaAck ack = (VisidiaAck) o;
					this.messages.add(ack);
				}
				else if (o instanceof VisidiaEvent) {
					VisidiaEvent event = (VisidiaEvent) o;
					this.messages.add(event);
				}
			}
		} catch (InvalidClassException e) {
			JOptionPane.showMessageDialog(VisidiaMain.getParentFrame(),	"Incompatible file version.", "Replay simulation", JOptionPane.ERROR_MESSAGE);
		} catch (EOFException e) {
			// wait for EOFException
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Gets the next message.
	 * 
	 * @return the message if any, or null
	 */
	public synchronized Object nextMessage() {
		if (index < 0 || index >= messages.size()) return null;
		
		return messages.elementAt(index++);
	}

	/**
	 * Rewind.
	 */
	public synchronized void rewind() {
		index = 0;
	}

	/**
	 * Sets the console.
	 * 
	 * @param console the new console
	 */
	public void setConsole(Console console) {
		Enumeration<Object> items = messages.elements();
		while (items.hasMoreElements()) {
			Object elt = items.nextElement();
			if (elt instanceof VisidiaAck) ((VisidiaAck) elt).getEvent().setConsole(console);
			else if (elt instanceof VisidiaEvent) ((VisidiaEvent) elt).setConsole(console);
		}
	}

}
