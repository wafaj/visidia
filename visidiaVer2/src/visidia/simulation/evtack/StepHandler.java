package visidia.simulation.evtack;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import visidia.VisidiaMain;
import visidia.gui.window.GraphPanelSimulation;
import visidia.gui.window.VisidiaPanelSimulationMode;

public class StepHandler implements PropertyChangeListener {
	GraphPanelSimulation simulationPanel = (GraphPanelSimulation) VisidiaMain.mainPanel.getTabbedPane().getSelectedComponent();

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        if (event.getPropertyName().equals("MyTextProperty") ) {
            if(VisidiaPanelSimulationMode.stepByStepButton.isSelected()){
            	simulationPanel.pauseSimulation(); 
        	
            }
    
        
        }
   
}
}