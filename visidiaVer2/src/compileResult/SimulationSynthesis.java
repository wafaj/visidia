package compileResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimulationSynthesis {
	private int idA;
	private int idCloneA;
	private double detectRate;
	private double averageMemorySize;
	private double maxMemorySize;
	private double averageNbMessageSent;
	private long detectionNb;
	private long lineNB;
	private int totalNbMessageSent;
	public int getTotalNbMessageSent() {
		return totalNbMessageSent;
	}

	public void setTotalNbMessageSent(int totalNbMessageSent) {
		this.totalNbMessageSent = totalNbMessageSent;
	}

	public int getTotalMemorySize() {
		return totalMemorySize;
	}

	public void setTotalMemorySize(int totalMemorySize) {
		this.totalMemorySize = totalMemorySize;
	}

	private int totalMemorySize;
	
	public long getDetectionNb() {
		return detectionNb;
	}

	public void setDetectionNb(long detectionNb) {
		this.detectionNb = detectionNb;
	}

	public long getLineNB() {
		return lineNB;
	}

	public void setLineNB(long lineNB) {
		this.lineNB = lineNB;
	}

	public SimulationSynthesis(int idA, int idCloneA) {
		super();
		this.idA = idA;
		this.idCloneA = idCloneA;
	}

	public double getDetectRate() {
		return detectRate;
	}
	public void setDetectRate(double detectRate) {
		this.detectRate = detectRate;
	}
	public double getAverageMemorySize() {
		return averageMemorySize;
	}
	public void setAverageMemorySize(double averageMemorySize) {
		this.averageMemorySize = averageMemorySize;
	}
	public double getMaxMemorySize() {
		return maxMemorySize;
	}
	public void setMaxMemorySize(double maxMemorySize) {
		this.maxMemorySize = maxMemorySize;
	}
	public void update(double detectRate, double averageMemorySize, double maxMemorySize,double averageNbMessageSent) {
		
		this.detectRate = detectRate;
		this.averageMemorySize = averageMemorySize;
		this.maxMemorySize = maxMemorySize;
		this.averageNbMessageSent = averageNbMessageSent;
	}

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
	public double getAverageNbMessageSent() {
		return averageNbMessageSent;
	}
	public void setAverageNbMessageSent(double averageNbMessageSent) {
		this.averageNbMessageSent = averageNbMessageSent;
	}

	public void update(String ligne) {
		incrementLineNb();
		if(ligne.contains("detected"))
		this.incrementDetectionNb();
		int wc=extractIntFromStringAtPos(ligne,5);
		int c=extractIntFromStringAtPos(ligne,7);
		maxMemorySize=Math.max(this.getMaxMemorySize(),wc+c );
		totalNbMessageSent+=extractIntFromStringAtPos(ligne,8);
		totalMemorySize+=wc+c ;
		this.averageMemorySize=(wc+c)/this.getLineNB();
		this.averageNbMessageSent=this.getTotalMemorySize();
		

		
		
	}

	private int extractIntFromStringAtPos(String line, int n) {
		Pattern pattern = Pattern.compile ("\\d+");
	String target = line.toString();
	Matcher matcher = pattern.matcher (target);
	
	int currentInt=0;
	for (int j = 0; j < n; j++) {
		if(matcher.find())
			currentInt =(int)Integer.parseInt(matcher.group());
		else
			return 0;
		
	}
	return currentInt;

	}



	@Override
	public String toString() {
		return "SimulationSynthesis [idA=" + idA + ", idCloneA=" + idCloneA + ", detectRate=" + detectRate
				+ ", averageMemorySize=" + averageMemorySize + ", maxMemorySize=" + maxMemorySize
				+ ", averageNbMessageSent=" + averageNbMessageSent + ", detectionNb=" + detectionNb + ", lineNB="
				+ lineNB + ", totalNbMessageSent=" + totalNbMessageSent + ", totalMemorySize=" + totalMemorySize + "]";
	}

	private void incrementDetectionNb() {
		this.detectionNb++;
		
	}

	private void incrementLineNb() {
		this.lineNB++;
		
	}

}
