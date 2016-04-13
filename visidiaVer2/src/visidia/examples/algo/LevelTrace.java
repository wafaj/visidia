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
	private long nb_WinessCacheMax;
	private long nb_WinessCache;
	private long nb_calimsMax;
	private long nb_claims;
	private long Nb_Messages;
	private Point posA;
	private Point posCloneA;
	public int getIdA() {
		return idA;
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





	private int idA;
	private int idCloneA;
	
	
	
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
		nb_calimsMax = copy.nb_calimsMax;
		nb_claims = copy.nb_claims;
		Nb_Messages = copy.Nb_Messages;	
		this.idA=copy.idA;
		this.idCloneA=copy.idCloneA;
		this.posA=new Point(copy.getPosA());
		this.posCloneA=new Point(copy.getPosCloneA());
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

	public long getNb_calimsMax() {
		return nb_calimsMax;
	}

	public void setNb_calimsMax(long nb_calimsMax) {
		this.nb_calimsMax = nb_calimsMax;
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
		this.setNb_calimsMax(Math.max(this.getNb_calimsMax(), r.getClaims().size()));
		this.setNb_claims(this.getNb_claims() + r.getClaims().size());
		this.setNb_WinessCache(this.getNb_WinessCache() + r.getCache().size());
		this.setNb_WinessCacheMax(Math.max(this.getNb_calimsMax(), r.getCache().size()));
		this.idA=idA;
		this.idCloneA=idCloneA;
		this.setPosA(posA);
		this.setPosCloneA(posCloneA);
	


	}

	public String show(Integer iterationNumber, Boolean cloneDetected) {
		String s="";
		s+=this.toString(iterationNumber, cloneDetected);
		System.out.println(s);
		return s;
				
	
		
		

	}

	//@Override
	public String toString1() {
		return "LevelTrace [nb_WinessCacheMax=" + nb_WinessCacheMax + ", nb_WinessCache=" + nb_WinessCache
				+ ", nb_calimsMax=" + nb_calimsMax + ", nb_claims=" + nb_claims + ", Nb_Messages=" + Nb_Messages + "]";
	}

	public String toString(Integer iterationNumber, Boolean cloneDetected) {
		String s="";
		s+=iterationNumber+" ";
		if(cloneDetected)
			s+=" detected ";
			//System.out.print(String.valueOf(iterationNumber) + " " + "detected    ");
		else
			s+=" notDtected ";
			//System.out.print(String.valueOf(iterationNumber) + " " + "noDetected    ");

		s+=idA+" ";
		s+=idCloneA+" ";
		s+="nb_WinessCacheMax=" + nb_WinessCacheMax + " nb_WinessCache=" + nb_WinessCache
				+ ", nb_calimsMax=" + nb_calimsMax + ", nb_claims=" + nb_claims + " Nb_Messages=" + Nb_Messages;
		return s;
		//s+=+" ";
		
	
	}


	public long getNb_Messages() {
		return Nb_Messages;
	}

	public void setNb_Messages(long nb_Messages) {
		Nb_Messages = nb_Messages;
	}

	public void hello(int id) {
		System.out.println("hello " + id);

	}

	public void incrementNbMessage(int nbMessage) {
		this.Nb_Messages += nbMessage;

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
		nb_calimsMax = 0;
		nb_claims = 0;
		Nb_Messages = 0;
	}

}
