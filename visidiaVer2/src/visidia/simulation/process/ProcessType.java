package visidia.simulation.process;

import visidia.graph.Edge;
import visidia.graph.Vertex;
import visidia.gui.window.VisidiaPanelSimulationMode;
import visidia.misc.property.VisidiaProperty;
import visidia.simulation.SimulationAbortError;
import visidia.simulation.command.ChangeEdgePropertyCommand;
import visidia.simulation.command.ChangeEdgeStateCommand;
import visidia.simulation.evtack.StepEvent;
import visidia.simulation.evtack.StepHandler;
import visidia.simulation.process.edgestate.EdgeState;
import visidia.simulation.process.step.MarkedStep;
import visidia.simulation.process.step.PropertyStep;
import visidia.simulation.process.step.Step;
import visidia.simulation.process.step.SyncStep;
import visidia.simulation.process.step.SyncStepAgent;
import visidia.simulation.server.Server;

// TODO: Auto-generated Javadoc
/**
 * This class represents a calculation entity (called a process) in visidia.
 * A process run following specific communication types. For simplicity, we refer to types of processes instead of
 * types of communication used by processes.
 * ProcessType is the abstract base class for all these types.
 */
public abstract class ProcessType {

	StepEvent stepEvenBean;
	StepHandler listener ;

	/** The process id. */
	protected int id;

	/** The server. */
	protected Server server;

	/** The thread. */
	private Thread thread = null;

	/**
	 * Instantiates a new process type.
	 * 
	 * @param id the id
	 * @param server the server
	 */
	protected ProcessType(int id, Server server) {
		this.id = id;
		this.server = server;
		stepEvenBean = new StepEvent();
		listener = new StepHandler();
	}

	/**
	 * Sets the thread.
	 * 
	 * @param thread the new thread
	 */
	public void setThread(Thread thread) {
		this.thread = thread;
	}

	/**
	 * Gets the id.
	 * 
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the server.
	 * 
	 * @return the server
	 */
	public Server getServer() {
		return server;
	}

	/**
	 * Running control.
	 */
	public void runningControl() {
		server.getConsole().runningControl();
	}

	/**
	 * Starts the process.
	 */
	public void start() {
		this.thread.start();
	}

	/**
	 * Stops the process.
	 */
	public void stop() {
		this.thread.interrupt();
	}

	/**
	 * Changes the edge state.
	 * 
	 * @param vertex the vertex
	 * @param door the door
	 * @param newEdgeState the new edge state
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public void changeEdgeState(Vertex vertex, int door, EdgeState newEdgeState) throws InterruptedException {

		ChangeEdgeStateCommand cmd = new ChangeEdgeStateCommand(vertex.getId(), door, newEdgeState);
		server.sendToConsole(cmd);

	}

	public void executeStep(Vertex vertex, Object obj,Object obj2, Step step) throws InterruptedException {

		String text = null;
		SyncStep syncStp = new SyncStep();
		SyncStepAgent syncstepAg=new SyncStepAgent();

		String htmlBegin = "<html><font size=\"3\" face=\"verdana\" color=\"";
		String htmlEnd = "</font><html>";

		String greenColor = "green";
		String redColor = "red";
		String blueColor = "blue";


		if (step.getClass() == MarkedStep.class 
				&& VisidiaPanelSimulationMode.checkBoxMarkStep.isSelected()){

			text = "Marking the edge between vertex (" + vertex.getId() + ")"
					+ " and vertex (" + vertex.getNeighborByDoor((Integer)obj).getId() + ")"
					+ " via door: " + (Integer)obj ;

			VisidiaPanelSimulationMode.model.addElement(htmlBegin + greenColor + "\">" + text + htmlEnd);
			VisidiaPanelSimulationMode.listAction.revalidate();
			stepEvenBean.addPropertyChangeListener(listener);
			stepEvenBean.setText(""); 

		}

		if (step.getClass() == PropertyStep.class 
				&&  VisidiaPanelSimulationMode.checkBoxPropertyStep.isSelected()){
			
			if ( obj != null && !vertex.getNeighborByDoor( (Integer)obj).getLabel().equals(obj2) ){
				
				Vertex neighbor = vertex.getNeighborByDoor( (Integer)obj);

				text = "Changing the label of vertex (" + neighbor.getId() + ") from " + neighbor.getLabel() 
						+ " to " + (String)obj2;

				VisidiaPanelSimulationMode.model.addElement(htmlBegin + redColor + "\">" + text + htmlEnd);

			} else if (!vertex.getLabel().equals(obj2)) {
						
				text = "Changing the label of vertex (" + vertex.getId() + ") from " + vertex.getLabel() 
						+ " to " + (String)obj2;

				VisidiaPanelSimulationMode.model.addElement(htmlBegin + redColor + "\">" + text + htmlEnd);
			}

			VisidiaPanelSimulationMode.listAction.revalidate();
			stepEvenBean.addPropertyChangeListener(listener);
			stepEvenBean.setText(""); 

		}

		if (step.getClass()==SyncStep.class 
				&&  VisidiaPanelSimulationMode.checkBoxSyncStep.isSelected()) {

			syncStp=(SyncStep)step;

			if (syncStp.id == SyncStep.LC2 || syncStp.id == SyncStep.LC1 )
				text = "LC" + syncStp.id + " Synchronisation: Vertex Center (" + vertex.getId() + ") synchronises with neighbor (" + vertex.getNeighborByDoor((Integer)obj).getId() + ")"
						+ " via door: " + (Integer)obj;
			else
				text = "LC0 Synchronisation: Vertex (" + vertex.getId() + ") synchronises with neighbor (" + vertex.getNeighborByDoor((Integer)obj).getId() +")" 
						+ " via door : " + (Integer)obj;

			VisidiaPanelSimulationMode.model.addElement(htmlBegin + blueColor + "\">" + text + htmlEnd);
			VisidiaPanelSimulationMode.listAction.revalidate();
			stepEvenBean.addPropertyChangeListener(listener);
			stepEvenBean.setText("");  
		}

		/************** Agent **************/
		if (step.getClass()==SyncStepAgent.class &&  VisidiaPanelSimulationMode.checkBoxSyncStep.isSelected()) {
			
			syncstepAg=(SyncStepAgent)step;
			
			if (syncstepAg.id==SyncStepAgent.LC2 || syncstepAg.id==SyncStepAgent.LC1 )
				text = "[Synchronisation]: Vertex Center (" + vertex.getNeighborByDoor((Integer)obj).getId() + ") synchronises with neighbor ("+ vertex.getId() +")" 
				+ " via door : " + (Integer)obj;
			else
				text="[Synchronisation]: A handshake between Vertex (" + vertex.getId() + ") and Vertex (" + vertex.getNeighborByDoor((Integer)obj).getId() + ")";
			
			VisidiaPanelSimulationMode.model.addElement(htmlBegin + blueColor + "\">" + text + htmlEnd);
			VisidiaPanelSimulationMode.listAction.revalidate();
			stepEvenBean.addPropertyChangeListener(listener);
			stepEvenBean.setText("");  
		}

		int lastIndex = VisidiaPanelSimulationMode.listAction.getModel().getSize() - 1;
		if (lastIndex >= 0) {
			VisidiaPanelSimulationMode.listAction.ensureIndexIsVisible(lastIndex);
		}  
		
	}


	/**
	 * Gets the edge property.
	 * 
	 * @param vertex the vertex
	 * @param door the door
	 * @param key the key
	 * 
	 * @return the edge property value
	 */
	public Object getEdgeProperty(Vertex vertex, int door, Object key) {
		Vertex v = vertex.getNeighborByDoor(door);
		Edge edge = vertex.getEdge(v);

		synchronized (edge) {
			while (edge.locked() && (edge.getLockOwner() != this)) {
				try {
					edge.wait();
				} catch (InterruptedException e) {
					throw new SimulationAbortError(e);
				}
			}

			VisidiaProperty prop = edge.getVisidiaProperty(key);
			return (prop == null ? null : prop.getValue());
		}
	}

	/**
	 * Sets the edge property.
	 * 
	 * @param vertex the vertex
	 * @param door the door
	 * @param key the key
	 * @param value the value
	 * @param tag the property tag
	 */
	private void setEdgeProperty(Vertex vertex, int door, Object key, Object value, int tag) {
		Vertex v2 = vertex.getNeighborByDoor(door);
		Edge edge = vertex.getEdge(v2);

		synchronized (edge) {
			while (edge.locked() && (edge.getLockOwner() != this)) {
				try {
					edge.wait();
				} catch (InterruptedException e) {
					throw new SimulationAbortError(e);
				}
			}

			VisidiaProperty prop = new VisidiaProperty(key, value, tag);
			edge.setProperty(prop);
			server.sendToConsole(new ChangeEdgePropertyCommand(edge.getOrigin().getId(), edge.getDestination().getId(), prop));
		}	
	}

	/**
	 * Sets the edge property.
	 * 
	 * @param vertex the vertex
	 * @param door the door
	 * @param key the key
	 * @param value the value
	 * @param displayable indicates if the property can be viewed on graph
	 */
	public void setEdgeProperty(Vertex vertex, int door, Object key, Object value, boolean displayable) {
		int tag = displayable ? VisidiaProperty.Tag.DISPLAYED_PROPERTY : VisidiaProperty.Tag.USER_PROPERTY;
		setEdgeProperty(vertex, door, key, value, tag);
	}

	/**
	 * Sets the edge property.
	 * 
	 * @param vertex the vertex
	 * @param door the door
	 * @param key the key
	 * @param value the value
	 */
	public void setEdgeProperty(Vertex vertex, int door, Object key, Object value) {
		setEdgeProperty(vertex, door, key, value, true);
	}

}
