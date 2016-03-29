package visidia.simulation.process.messages;

import java.awt.Color;
import java.io.Serializable;

// TODO: Auto-generated Javadoc
/**
 * This class represents the type of the messages.
 * 
 * The programmer can create as many types as he wants for his messages, and
 * specify for each type if he thinks that the messages must be displayed or
 * not. By default the messages are displayed.
 * 
 * For each type of messages the programmer can also specify the color he
 * prefers to use when the messages will be displayed. The default color is red.
 * 
 * During the simulation of the algorithm, the user knows all the types used by
 * the algorithm and can choose to display them or not.
 */
public class MessageType implements Serializable {

	private static final long serialVersionUID = -8091042735794298526L;

	/** The Constant defaultColor. */
	private static final Color defaultColor = Color.red;

	/** The type name. */
	private String typeName;

	/** Indicates if the messages must be displayed or not */
	private boolean toPaint;

	/** The color. */
	private Color color;

	/**
	 * Instantiates a new message type.
	 * 
	 * @param typeName the type name
	 * @param toPaint indicates if the messages must be displayed or not
	 * @param color the color
	 */
	public MessageType(String typeName, boolean toPaint, Color color) {
		this.typeName = typeName;
		this.toPaint = toPaint;
		this.color = color;
	}

	/**
	 * Instantiates a new message type.
	 * 
	 * @param typeName the type name
	 * @param toPaint indicates if the messages must be displayed or not
	 */
	public MessageType(String typeName, boolean toPaint) {
		this(typeName, toPaint, MessageType.defaultColor);
	}

	/**
	 * Instantiates a new message type.
	 * 
	 * @param typeName the type name
	 */
	public MessageType(String typeName) {
		this(typeName, true);
	}

	/**
	 * Gets the type.
	 * 
	 * @return the type
	 */
	public String getType() {
		return this.typeName;
	}

	/**
	 * Sets the boolean indicating if the messages must be displayed or not.
	 * 
	 * @param bool the boolean
	 */
	public void setToPaint(boolean bool) {
		this.toPaint = bool;
	}

	/**
	 * Gets the boolean indicating if the messages must be displayed or not.
	 * 
	 * @return true/false
	 */
	public boolean getToPaint() {
		return this.toPaint;
	}

	/**
	 * Sets the color.
	 * 
	 * @param color the new color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

	/**
	 * Gets the color.
	 * 
	 * @return the color
	 */
	public Color getColor() {
		return this.color;
	}

	/**
	 * This type is used each time that the programmer of the algorithm does not
	 * specify the type of a message. By default, the messages whose type is
	 * defaultMessageType are displayed.
	 */
	public static final MessageType defaultMessageType = new MessageType("default");

}
