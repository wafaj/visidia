package visidia.simulation.process.step;

public class SyncStepAgent extends Step {
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -3605192666898175804L;
	
	public static final int LC0=0;
	public static final int LC1=1;
	public static final int LC2=2;
	
	public int id;
	
	
	public SyncStepAgent() {
		id = 0;
	}
	
	public SyncStepAgent(int id) {
		this.id = id;
	}
	
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return new SyncStepAgent(id);
	}

}
