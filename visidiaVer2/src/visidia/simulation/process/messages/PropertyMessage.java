package visidia.simulation.process.messages;

import visidia.misc.property.PropertyTable;;

public class PropertyMessage extends Message {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6094607144893653419L;
	private PropertyTable data;
	
	public PropertyMessage(PropertyTable data){
		this.data = data;
	}
	
	public PropertyMessage(PropertyTable data, MessageType type){
		this.setType(type);
		this.data = data;
		
	}
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getData() {
		return ((PropertyTable) data.clone());
	}

	@Override
	public String toString() {
		return "\u2709";
	}

}
