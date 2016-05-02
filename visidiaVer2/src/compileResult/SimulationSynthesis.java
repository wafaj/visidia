package compileResult;

import java.awt.Point;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimulationSynthesis implements Comparable{
	private int idA;
	private int idCloneA;
	private Point posA;
	private Point posCloneA;
	

	private double averageNbMessageSent;
	private long detectionNb;
	private long lineNB;
	private long totalNbMessageSent;	
	
	private double detectRate;
	private double averageMemorySize;
	private double maxMemorySize;
	private double totalMemorySize;
		public SimulationSynthesis(String line) {
		//8  notDtected idA=8 idCloneA=0  posCloneA=(50,50)  posA=(100, 100) 
		//nb_WinessCacheMax=0 nb_WinessCache=14 nb_claimsMax=0 nb_claims=0 Nb_Messages=14
		//totalMemory=476 maxMemory=34
		super();
		this.idA = this.extractIntFromStringAtPos(line, 2);
		this.idCloneA = this.extractIntFromStringAtPos(line, 3);
		this.posA=new Point(this.extractIntFromStringAtPos(line, 4),this.extractIntFromStringAtPos(line, 5));
		this.posCloneA=new Point(this.extractIntFromStringAtPos(line, 6),this.extractIntFromStringAtPos(line,7));
		
		this.totalNbMessageSent = this.extractIntFromStringAtPos(line, 12);		
		this.totalMemorySize = this.extractIntFromStringAtPos(line, 13);
		this.maxMemorySize =  this.extractIntFromStringAtPos(line, 14);
		
		if(line.contains("notDtected")){
			this.detectionNb = 0;
		}
		else if (line.contains("detected")){
			for (int i = 0; i < 1000; i++) {
				//System.out.print("#");
				
			}
			this.detectionNb = 1;
		}
			
		this.lineNB = 1;
		this.averageMemorySize =totalMemorySize/lineNB ;
		this.averageNbMessageSent = averageNbMessageSent/lineNB;
		this.detectRate = this.detectionNb/lineNB;
	}
	public double getTotalNbMessageSent() {
		return totalNbMessageSent;
	}

	public void setTotalNbMessageSent(int totalNbMessageSent) {
		this.totalNbMessageSent = totalNbMessageSent;
	}

	public double getTotalMemorySize() {
		return totalMemorySize;
	}

	public void setTotalMemorySize(int totalMemorySize) {
		this.totalMemorySize = totalMemorySize;
	}

	
	
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
			return -1;
		
	}
	return currentInt;

	}



	@Override
	public String toString() {
		DecimalFormat formatter = new DecimalFormat("#.######", DecimalFormatSymbols.getInstance( Locale.ENGLISH ));
		formatter.setRoundingMode( RoundingMode.DOWN );
		double d = detectRate;
		String s = formatter.format(d);
		//if(detectionNb>0)
		return "("+idCloneA+","+idA+") :"+"SimulationSynthesis "
				+ "[idA=" + idA 
				+ ", idCloneA=" + idCloneA 
				+ ", detectRate=" + detectRate
				+ ", averageMemorySize=" + averageMemorySize
				+ ", maxMemorySize=" + maxMemorySize
				+ ", averageNbMessageSent=" + averageNbMessageSent 
				+ ", detectionNb=" + detectionNb 
				+ ", lineNB="+ lineNB 
				+ ", totalNbMessageSent=" + totalNbMessageSent 
				+ ", totalMemorySize=" + totalMemorySize 
				+ "]"
				+s;
		//return "";
	}

	private void incrementDetectionNb() {
		this.detectionNb++;
		
	}

	private void incrementLineNb() {
		this.lineNB++;
		
	}

	@Override
	public int compareTo(Object ss) {
		if(((SimulationSynthesis) ss).getIdA()==this.getIdA() 
				&& ((SimulationSynthesis) ss).getIdCloneA()==this.getIdCloneA()){
			//System.out.println((SimulationSynthesis)ss);
			//System.out.println(this);
			//System.out.println("");
			
			
			return 0;
		}
		
		else {
			return 1;
		}
			
	}

	public void merge(SimulationSynthesis simulationSynthesis) {
		//System.out.println("");
		//System.out.println(this);
		//System.out.println(simulationSynthesis);

		//8  notDtected idA=8 idCloneA=0  posCloneA=(50,50)  posA=(100, 100) 
		//nb_WinessCacheMax=0 nb_WinessCache=14 nb_claimsMax=0 nb_claims=0 Nb_Messages=14
		//totalMemory=476 maxMemory=34
		
		//is.idA = 
		//this.idCloneA = 
		//this.posA=
		//this.posCloneA=
		
		this.totalNbMessageSent +=simulationSynthesis.getTotalNbMessageSent();
		this.totalMemorySize +=simulationSynthesis.getTotalMemorySize();//= this.extractIntFromStringAtPos(line, 13);
		this.maxMemorySize =Math.max(simulationSynthesis.getMaxMemorySize(), this.getMaxMemorySize()); 
		
		this.detectionNb +=simulationSynthesis.getDetectionNb();
		this.lineNB +=simulationSynthesis.getLineNB();
		
		
		//if(simulationSynthesis.getDetectionNb()>0){
			//System.out.println(simulationSynthesis);
			//System.out.println(this);
			
			//System.out.println(this);
			
//		}	
	//	if(simulationSynthesis.getLineNB()>0){
		//	System.out.println(simulationSynthesis);
			
			//System.out.println(this);
			
			//System.out.println(this);
			
		//}
			
		this.averageMemorySize =totalMemorySize/lineNB ;
		this.averageNbMessageSent = averageNbMessageSent/lineNB;
		this.detectRate = this.detectionNb/lineNB;
		//System.out.println(this);
		
		
	}

}
