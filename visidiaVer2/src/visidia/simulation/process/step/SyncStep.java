package visidia.simulation.process.step;

public class SyncStep extends Step {

	/**
	 * 
	 */
	
	private static final long	serialVersionUID	= 6738431745188680987L;
	
	public static final int LC0=0;
	public static final int LC1=1;
	public static final int LC2=2;
	
	public int id;
	

	public SyncStep(int id) {
		this.id = id;	
	}
	
	public SyncStep() {
		id = 0;	
	}
	
	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return new SyncStep(id);
	}

}
