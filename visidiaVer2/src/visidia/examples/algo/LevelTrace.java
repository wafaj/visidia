package visidia.examples.algo;

import java.awt.Point;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class LevelTrace implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3347678714052055389L;
	private static final long sizeOfWitnessCacheObject=sizeOf("visidia.examples.algo.WitnessCache");
	private static final long sizeOfClaimObject=sizeOf("visidia.examples.algo.WitnessCache");
	
	
	private int idA;
	private int idCloneA;	
	private  long nb_claimsMax;
	private long nb_WinessCacheMax;	
	
	private long nb_WinessCache;
	private long nb_claims;
	private long nb_Messages;
	
	private long totalMemory;
	private long maxMemory;

	private Point posA;
	private Point posCloneA;
	
	
	
	public int getIdA() {
		return idA;
	}


	private static long sizeOf(String theClassName) {
		if(theClassName.equals("visidia.examples.algo.WitnessCache"))
			return 160;
		else if (theClassName.equals("visidia.examples.algo.SensorMessage"))
			return (24*3)+(4*2)+10;
		return -1;
		
		/*long size=-1;
		ObjectSizer sizer = new ObjectSizer();
		 Class theClass = null;
		    try {
		      theClass = Class.forName(theClassName);
		      size= sizer.getObjectSize(theClass);
		      System.out.println("The size of a "+theClassName + " object is " + size);
		      return size;
		    }
		    catch (Exception ex) {
		    	System.out.println("Cannot build a Class object: " +theClassName);
		    	System.out.println("Use a package-qualified name, and check classpath.");
		    }
		    return -1;*/
	}


	public void setIdA(int idA) {
		this.idA = idA;
	}


	public int getIdCloneA() {
		return idCloneA;
	}


	public void setIdCloneA(int idCloneA) {
		this.idCloneA = idCloneA;
	}





	public long getTotalMemory() {
		return totalMemory;
	}


	public void setTotalMemory(long totalMemory) {
		this.totalMemory = totalMemory;
	}


	public long getMaxMemory() {
		return maxMemory;
	}


	public void setMaxMemory(long maxMemory) {
		this.maxMemory = maxMemory;
	}



	
	
	
	public Point getPosA() {
		return posA;
	}


	public void setPosA(Point posA) {
		this.posA = posA;
	}


	public Point getPosCloneA() {
		return posCloneA;
	}


	public void setPosCloneA(Point posCloneA) {
		this.posCloneA = posCloneA;
	}





	

	public LevelTrace(LevelTrace copy) {
		nb_WinessCacheMax = copy.nb_WinessCacheMax;
		nb_WinessCache = copy.nb_WinessCache;
		nb_claimsMax = copy.nb_claimsMax;
		nb_claims = copy.nb_claims;
		nb_Messages = copy.nb_Messages;	
		this.idA=copy.idA;
		this.idCloneA=copy.idCloneA;
		this.posA=new Point(copy.getPosA());
		this.totalMemory=copy.totalMemory;
		this.maxMemory=copy.maxMemory;

	}


	public LevelTrace() {
		super();
		
	}


	public long getNb_WinessCache() {
		return nb_WinessCache;
	}

	public void setNb_WinessCache(long nb_WinessCache) {
		this.nb_WinessCache = nb_WinessCache;
	}

	public long getNb_claimsMax() {
		return nb_claimsMax;
	}

	public void setNb_claimsMax(long nb_claimsMax) {
		this.nb_claimsMax = nb_claimsMax;
	}

	public long getNb_claims() {
		return nb_claims;
	}

	public void setNb_claims(long nb_claimsCache) {
		this.nb_claims = nb_claimsCache;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public long getNb_WinessCacheMax() {
		return nb_WinessCacheMax;
	}

	public void setNb_WinessCacheMax(long nb_WinessCacheMax) {
		this.nb_WinessCacheMax = nb_WinessCacheMax;
	}

	public void update(Routing r, int idA, int idCloneA, Point posA, Point posCloneA) {
		//System.out.println("update"+idA+" "+idCloneA+" "+ posA+" "+ posCloneA);
		this.setNb_claimsMax(Math.max(this.getNb_claimsMax(), r.getClaims().size()));
		this.setNb_claims(this.getNb_claims() + r.getClaims().size());
		this.setNb_WinessCache(this.getNb_WinessCache() + r.getCache().size());
		this.setNb_WinessCacheMax(Math.max(this.getNb_claimsMax(), r.getCache().size()));
		this.idA=idA;
		this.idCloneA=idCloneA;
		this.setPosA(posA);
		this.setPosCloneA(posCloneA);
		long currentMemorySize=r.claims.size()*sizeOf("visidia.examples.algo.SensorMessage")+r.getCache().size()*(10+24);
		this.setTotalMemory(currentMemorySize+this.getTotalMemory());
		this.setMaxMemory(
				Math.max(this.getMaxMemory(),currentMemorySize));
		
		
	


	}

	public String show(Integer iterationNumber, Boolean cloneDetected) {
		String s="";
		s+=this.toString(iterationNumber, cloneDetected);
		//System.out.println(s);
		return s;
	}



	public String toString(Integer iterationNumber, Boolean cloneDetected) {
		String s="";
		s+=iterationNumber+" ";
		
		if(cloneDetected)		s+=" detected ";
		else					s+=" notDtected ";


		s+="idA="+idA+" ";
		s+="idCloneA="+idCloneA+" ";
		
		s+=" posCloneA=("+(int)posCloneA.getX()+","+(int)posCloneA.getY()+") ";
		s+=" posA=("+(int)posA.getX()+", "+(int)posA.getY()+") ";
				
		s+="nb_WinessCacheMax=" + nb_WinessCacheMax + " nb_WinessCache=" + nb_WinessCache
				+ " nb_claimsMax=" + nb_claimsMax + " nb_claims=" + nb_claims + " Nb_Messages=" + nb_Messages;
		s+=" totalMemory="+totalMemory;
		s+=" maxMemory="+maxMemory;
		
		
		return s;
		//s+=+" ";
		
	
	}


	public long getNb_Messages() {
		return nb_Messages;
	}

	public void setNb_Messages(long nb_Messages) {
		nb_Messages = nb_Messages;
	}

	public void hello(int id) {
		System.out.println("hello " + id);

	}

	public void incrementNbMessage(int nbMessage) {
		this.nb_Messages += nbMessage;

	}

	public void saveToFile(boolean save) {
		if (save) {
			LevelTrace toSavedObject= new LevelTrace(this);
			try {
				FileOutputStream fout = new FileOutputStream("LevelTracesFile.dat");
				ObjectOutputStream oos = new ObjectOutputStream(fout);
				oos.writeObject(toSavedObject);
				oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub

	}

	

	

	public void initialize() {
		nb_WinessCacheMax = 0;
		nb_WinessCache = 0;
		nb_claimsMax = 0;
		nb_claims = 0;
		nb_Messages = 0;
		this.totalMemory=0;
		this.maxMemory=0;
		
	}

}
