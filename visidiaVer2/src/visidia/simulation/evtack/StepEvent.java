package visidia.simulation.evtack;

import java.beans.*;

public class StepEvent {
	
    protected PropertyChangeSupport propertyChangeSupport;
    private String text;

    public StepEvent () {
        propertyChangeSupport = new PropertyChangeSupport(this);
    }	

    public synchronized String getText() {
    	return this.text;
    }
    
    public synchronized void setText(String text) {
        String oldText = this.text;
    	//String oldText = new String(getText());
        this.text = text;
        propertyChangeSupport.firePropertyChange("MyTextProperty",oldText, text);  
    }

    public synchronized void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }
    
}
