package visidia.examples.algo;

import java.io.Serializable;

public class LevelTrace implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3347678714052055389L;
	private long nb_WinessCacheMax;
	private long nb_WinessCache;
	private long nb_calimsMax;
	private long nb_claims;
	private long Nb_Messages;
	
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
	public void update(Routing r){
		this.setNb_calimsMax(Math.max(this.getNb_calimsMax(), r.getClaims().size()));
		this.setNb_claims(this.getNb_claims() + r.getClaims().size());
		this.setNb_WinessCache(this.getNb_WinessCache() + r.getCache().size());
		this.setNb_WinessCacheMax(Math.max(this.getNb_calimsMax(), r.getCache().size()));
			
	}
	public void show() {
		System.out.println(this.toString());
		
	}
	@Override
	public String toString() {
		return "LevelTrace [nb_WinessCacheMax=" + nb_WinessCacheMax + ", nb_WinessCache=" + nb_WinessCache
				+ ", nb_calimsMax=" + nb_calimsMax + ", nb_claims=" + nb_claims + ", Nb_Messages=" + Nb_Messages + "]";
	}
	public long getNb_Messages() {
		return Nb_Messages;
	}
	public void setNb_Messages(long nb_Messages) {
		Nb_Messages = nb_Messages;
	}
	public void hello(int id) {
		System.out.println("hello "+id);
		
	}


}
