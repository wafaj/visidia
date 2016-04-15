package visidia.examples.algo.clone_solutions;

import java.awt.Point;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import visidia.examples.algo.clone_solutions.tools.MessageWithInfo;
import visidia.simulation.process.algorithm.SynchronousAlgorithm;
import visidia.simulation.process.messages.Door;
import visidia.simulation.process.messages.Message;
import visidia.simulation.process.messages.StringMessage;

public class NativeGPSR extends SynchronousAlgorithm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1934288331571497294L;
	private Stack<MessageWithInfo> staskNeighborsPosMessage=new Stack<MessageWithInfo>();
	private Stack<MessageWithInfo> staskRequestPosMessage=new Stack<MessageWithInfo>();
	private Stack<MessageWithInfo> staskGPSRMessage=new Stack<MessageWithInfo>();
	private boolean requestPosIsSent=false;
	private static final double seed  = Math.random();
	private static final int SEND_MODE = 0;
	private static final int RECEIVE_MODE = 1;
	private static long src_id;//=(Math.random() * ((proc.getServer().getConsole().getGraph().order()) + 1));
	//private static  long nbNodes=proc.getServer().getConsole().getGraph().order();
	@Override
	public String getDescription() {
		String s = "This is the implementation of the Native GPSR Aalgorithm .\n";
		return s;
	}

	@Override
	public Object clone() {
		return new NativeGPSR();
	}

	@SuppressWarnings("static-access")
	@Override
	public void init() {
		boolean run = true;
		src_id= 2;//(long) (Math.random() * ((this.getNetSize())));

		System.out.println(src_id);
		nextPulse();
		if(this.getId()==src_id){
			System.out.println("id_src="+this.src_id );
			this.putProperty("label", new String("A"));
			staskGPSRMessage.push(new MessageWithInfo(new StringMessage("GPSRMessage_dest=[0,0]_LastPos=[x=14310,y=6630]"),null,this.vertex.getPos(), System.nanoTime()));
		}
	
		nextPulse();
		int mode=SEND_MODE;
		nextPulse();
		
		while(run){
			
			if(mode==SEND_MODE){
				//System.out.println("SEND_MODE");
				send();
				mode=RECEIVE_MODE;
			}
			else if (mode==RECEIVE_MODE){
				//System.out.println("RECEIVE_MODE");
				receive();
				mode=SEND_MODE;
			}
			
			nextPulse();
		}
	}



	private void receive() {
		while(this.anyMsg()){
			Door door = new Door();
			//int numDoor = door.getNum();
			Message m=this.receive(door);
			empiler(m,door);
			}
		}
		
	private void empiler(Message m, Door door) {
		System.out.println("Empiler "+door+"  "+m);
		if(m.toString().substring(0, 10).equals((new String("RequestPosition")).substring(0, 10))){
			this.staskRequestPosMessage.push(new MessageWithInfo(m,door,null, System.nanoTime()));
		}
		else if (m.toString().substring(0, 10).equals((new String("ResponsePosition")).substring(0, 10))){
			this.staskNeighborsPosMessage.push(new MessageWithInfo(m,door,null, System.nanoTime()));
		}
		else if(m.toString().substring(0, 10).equals((new String("GPSRMessage")).substring(0, 10))){
			this.putProperty("label", new String("A"));
				this.staskGPSRMessage.push(new MessageWithInfo(m,door,getLastPosition(m), System.nanoTime()));
		} 
	}

	private void send() {
		//pour tout message request position repondre avec la position	
		//pour tout message GPSR envoyer par GPSR si il ya toute les position 
		//pour tout message GPSR envoyer demandes des positions  GPSR si il ya pas toutes les position 
		
		while(!staskRequestPosMessage.isEmpty()){
			System.out.println(" staskRequestPosMessage is not empty");
			this.sendTo((staskRequestPosMessage.pop()).getDoor().getNum(), new StringMessage("ResponsePosition"+vertex.getPos().toString()));

/***
 * 			if(!this.vertex.getLabel().equals("A")){
	//		if(this.getProperty("label").equals("A")){
//			if (!(this.vertex.getVisidiaProperty("label").equals("A"))){
				this.sendTo((staskRequestPosMessage.pop()).getDoor().getNum(), new StringMessage("ResponsePosition"+vertex.getPos().toString()));

			}
			else{
				MessageWithInfo ignore = staskRequestPosMessage.pop();
				System.out.println(this.getId()+"  "+this.vertex.getLabel());
			}
 */
			
 
				
		}
		//while this.staskGPSRMessage
		//System.out.println(this.getId()+"_staskGPSRMessage.size="+this.staskGPSRMessage.size());
		
		if(!this.staskGPSRMessage.isEmpty()){
			if(this.staskNeighborsPosMessage.empty()){
				;
			}
			if(this.getArity()!=this.staskNeighborsPosMessage.size()){
				if(!this.requestPosIsSent){
					staskNeighborsPosMessage.clear();
					sendPositionsRequest();
					this.requestPosIsSent=true;	
				}		
			}
			else{
				this.requestPosIsSent=false;
				fixGPSRMessages(staskGPSRMessage,this.staskNeighborsPosMessage);
			}
		}
	}

	private void fixGPSRMessages(Stack<MessageWithInfo> staskGPSRMessage,
			Stack<MessageWithInfo> staskNeighborsPosMessage) {
		System.out.println("Envoyer ces messages GPSR\n"+convertToString(staskGPSRMessage)+
				"vers l'un des voisins suivant selon la regle aproprié\n"+convertToString(staskNeighborsPosMessage));
		sendToWithGPSR(new Point(0,0), staskGPSRMessage, staskNeighborsPosMessage,this.vertex.getPos());
		//show(staskGPSRMessage);
		
		
	}
	public  void sendToWithGPSR(
			Point dest, 
			Stack<MessageWithInfo> staskGPSRMessage,
			Stack<MessageWithInfo> staskNeighborsPosMessage, 
			Point position) {
		
		Vector<Point> neighborPos=new Vector<Point>();
		Vector<Door> neighborDoor=new Vector<Door>();
		Vector<Double> distanceBetweenNeighbors_Dest=new Vector<Double>();
		Double distanceFromDest=position.distance(dest);
		double min = Double.MAX_VALUE;
		int indexMinDistanceBetweenNeighbor_Dest=-1;
		while(!staskNeighborsPosMessage.empty()){
			MessageWithInfo messageWithInfoNeighborsPos =staskNeighborsPosMessage.pop();
			Point currentPos=extractPos((StringMessage) messageWithInfoNeighborsPos.getM());
			neighborPos.addElement(currentPos);
			neighborDoor.addElement(messageWithInfoNeighborsPos.getDoor());
			distanceBetweenNeighbors_Dest.addElement( dest.distance(currentPos));
			indexMinDistanceBetweenNeighbor_Dest=minIndex(distanceBetweenNeighbors_Dest);
			min=distanceBetweenNeighbors_Dest.get(indexMinDistanceBetweenNeighbor_Dest);
		}
		show(distanceBetweenNeighbors_Dest);
	
		System.out.println("distanceFromDest :"+distanceFromDest.toString());
		System.out.println("min :"+min);
		
		if (distanceFromDest< min)  {
			//PERIMETER FORWARDING
			System.out.println("//PERIMETER FORWARDING");
			while(!staskGPSRMessage.empty()){
				//Door doorGPsr
				MessageWithInfo messageWithInfoGPSR =staskGPSRMessage.pop();
				StringMessage currentGPSRMsg =(StringMessage) messageWithInfoGPSR.getM();
				Door srcDoorOfCurrentGPSRMsg=messageWithInfoGPSR.getDoor();
				Point lastPosiOfCurrentGPSRMsg=messageWithInfoGPSR.getLastPos();	
				System.out.println("lastPosiOfCurrentGPSRMsg : "+lastPosiOfCurrentGPSRMsg);
				System.out.println("position courante  : "+this.vertex.getPos());
				
				Vector<Double> angleBetweenEdgeNodeSrc_EdgesNeighbors=new Vector<Double>();
				for (int i = 0; i < neighborDoor.size(); i++) {
					System.out.println(neighborDoor.get(i));
					System.out.println("--------");
					System.out.println(srcDoorOfCurrentGPSRMsg);
									
					
				}
				for (int i = 0; i < neighborPos.size(); i++) {
					if(!neighborDoor.get(i).equals(srcDoorOfCurrentGPSRMsg)){
						Double currentAngle=(double) 360;
						try {
							currentAngle=angleBetweenTwoPointsWithFixedPoint(this.vertex.getPos(), lastPosiOfCurrentGPSRMsg, neighborPos.get(i));
						} catch (NullVectorExceotion e) {
							// TODO Auto-generated catch block
							//e.printStackTrace();
							System.out.println("################################################################################");
						}
						angleBetweenEdgeNodeSrc_EdgesNeighbors.add(i, currentAngle);
					}
					else{
						angleBetweenEdgeNodeSrc_EdgesNeighbors.add(i, Double.MAX_VALUE);
					}		
				}
				show(angleBetweenEdgeNodeSrc_EdgesNeighbors);
				int indexMinAngle = minIndex(angleBetweenEdgeNodeSrc_EdgesNeighbors);
				System.out.println("envois vers l'angle numero  : "+indexMinAngle);
				String updatedM=updateLastPosGPSR(currentGPSRMsg);
				messageWithInfoGPSR.setM(new StringMessage(updatedM));
				this.sendTo(neighborDoor.get(indexMinAngle).getNum(), messageWithInfoGPSR.getM());
			}			
        }
		else{
			//GREEDY FORWARDING
			System.out.println("//GREEDY FORWARDING");
			while(!staskGPSRMessage.empty()){
				MessageWithInfo messageWithInfoGPSR =staskGPSRMessage.pop();
				String updatedM=updateLastPosGPSR(messageWithInfoGPSR.getM());
				messageWithInfoGPSR.setM(new StringMessage(updatedM));
				this.sendTo(neighborDoor.get(indexMinDistanceBetweenNeighbor_Dest).getNum(),messageWithInfoGPSR.getM());
			}
		}
	}
	public  void sendToWithGPSR2(
			Point dest, 
			Stack<MessageWithInfo> staskGPSRMessage,
			Stack<MessageWithInfo> staskNeighborsPosMessage, 
			Point position) {
		while(!staskGPSRMessage.empty()){
			MessageWithInfo messageWithInfoGPSR =staskGPSRMessage.pop();
			StringMessage currentGPSRMsg =(StringMessage) messageWithInfoGPSR.getM();
			Door srcDoorOfCurrentGPSRMsg=messageWithInfoGPSR.getDoor();
			Point lastPosiOfCurrentGPSRMsg=messageWithInfoGPSR.getLastPos();
			Vector<Point> neighborPos=new Vector<Point>();
			Vector<Door> neighborDoor=new Vector<Door>();
			Vector<Double> distanceBetweenNeighbors_Dest=new Vector<Double>();
			Double distanceFromDest=position.distance(dest);
			int indexMinDistanceBetweenNeighbor_Dest = 0;
			Double min = null ;
			while(!staskNeighborsPosMessage.empty()){
				
				MessageWithInfo messageWithInfoNeighborsPos =staskNeighborsPosMessage.pop();
				if(messageWithInfoNeighborsPos.getDoor().equals(srcDoorOfCurrentGPSRMsg)){
					System.out.println("message position arrivé de la meme source que GPSR , ignore ");
				}
				else{
					Point currentPos=extractPos((StringMessage) messageWithInfoNeighborsPos.getM());
					neighborPos.addElement(currentPos);
					neighborDoor.addElement(messageWithInfoNeighborsPos.getDoor());
					distanceBetweenNeighbors_Dest.addElement( dest.distance(currentPos));
					indexMinDistanceBetweenNeighbor_Dest = minIndex(distanceBetweenNeighbors_Dest);
					min = distanceBetweenNeighbors_Dest.get(indexMinDistanceBetweenNeighbor_Dest);
				}
				show(distanceBetweenNeighbors_Dest);
				
				System.out.println("distanceFromDest :"+distanceFromDest.toString());
				System.out.println("min :"+min);
				
				if (distanceFromDest< min)  {
					//PERIMETER FORWARDING
					System.out.println("//PERIMETER FORWARDING");
					while(!staskGPSRMessage.empty()){
						//Door doorGPsr
											
						System.out.println("lastPosiOfCurrentGPSRMsg : "+lastPosiOfCurrentGPSRMsg);
						System.out.println("position courante  : "+this.vertex.getPos());
						
						Vector<Double> angleBetweenEdgeNodeSrc_EdgesNeighbors=new Vector<Double>();
						for (int i = 0; i < neighborDoor.size(); i++) {
							System.out.println(neighborDoor.get(i));
							System.out.println("--------");
							System.out.println(srcDoorOfCurrentGPSRMsg);
											
							
						}
						for (int i = 0; i < neighborPos.size(); i++) {
							if(!neighborDoor.get(i).equals(srcDoorOfCurrentGPSRMsg)){
								Double CurrentAngle=(double) 360;
								try {
									CurrentAngle=angleBetweenTwoPointsWithFixedPoint(this.vertex.getPos(), lastPosiOfCurrentGPSRMsg, neighborPos.get(i));
								} catch (NullVectorExceotion e) {
									// TODO Auto-generated catch block
									//e.printStackTrace();
									System.out.println("################################################################################");
								}
								angleBetweenEdgeNodeSrc_EdgesNeighbors.add(i, CurrentAngle);
							}
							else{
								angleBetweenEdgeNodeSrc_EdgesNeighbors.add(i, Double.MAX_VALUE);
							}		
						}
						show(angleBetweenEdgeNodeSrc_EdgesNeighbors);
						int indexMinAngle = minIndex(angleBetweenEdgeNodeSrc_EdgesNeighbors);
						System.out.println("envois vers l'angle numero  : "+indexMinAngle);
						String updatedM=updateLastPosGPSR(currentGPSRMsg);
						messageWithInfoGPSR.setM(new StringMessage(updatedM));
						this.sendTo(neighborDoor.get(indexMinAngle).getNum(), messageWithInfoGPSR.getM());
					}			
		        }
				else{
					//GREEDY FORWARDING
					System.out.println("//GREEDY FORWARDING");
					while(!staskGPSRMessage.empty()){
						messageWithInfoGPSR =staskGPSRMessage.pop();
						String updatedM=updateLastPosGPSR(messageWithInfoGPSR.getM());
						messageWithInfoGPSR.setM(new StringMessage(updatedM));
						this.sendTo(neighborDoor.get(indexMinDistanceBetweenNeighbor_Dest).getNum(),messageWithInfoGPSR.getM());
					}
			
				}	
				
			}
		}
	}
	
	

	
	

	private void show(Vector<Double> vectDouble) {
		for (int i = 0; i < vectDouble.size(); i++) {
			;System.out.println("["+i+"] : "+vectDouble.get(i));
		}		
	}

	private String updateLastPosGPSR(Message m) {
		
		StringMessage sm=new StringMessage("GPSRMessage_dest=[0,0]_LastPos="+this.vertex.getPos());
		return sm.toString();
	}

	private int minIndex(Vector<Double> doubleVector) {
		double min=Double.MAX_VALUE;
		int indexMin=-1;
		for (int i = 0; i < doubleVector.size(); i++) {

			if (Double.compare(doubleVector.get(i), min) < 0) {
	            min = doubleVector.get(i);
	            indexMin = i;
	        }
		}
		return indexMin;
	}

	private Point getLastPosition(Message m) {
		long x = 0 ,y = 0;
		@SuppressWarnings("unused")
		long xdest = 0 ,ydest = 0;
		Pattern pattern = Pattern.compile ("\\d+");
		String target = m.toString();
		Matcher matcher = pattern.matcher (target);
		//if(matcher.find());
		//if(matcher.find());
		if(matcher.find())
			xdest=Integer.parseInt(matcher.group());		
		if(matcher.find())
			ydest=Integer.parseInt(matcher.group());		
		if(matcher.find())
			x=Integer.parseInt(matcher.group());		
		if(matcher.find())
			y=Integer.parseInt(matcher.group());

		Point p=new Point();
		p.setLocation(x, y);
		
		
		//System.out.println();
		return p;// new Point(x,y);
	}

	/**
	 * 
	 * @param fixed 
	 * @param p1
	 * @param p2
	 * @return
	 * @throws NullVectorExceotion 
	 */
	private Double angleBetweenTwoPointsWithFixedPoint(Point fixed, Point p1, Point p2) throws NullVectorExceotion {
		double a1=angleVector(fixed,p1);
		double a2=angleVector(fixed,p2);
		System.out.println("a1="+a1+" est la mesure de l'angle ["+fixed.getX()+","+fixed.getY()+"] et ["+p1.getX()+","+p1.getY()+"]");
		System.out.println("a2="+a1+" est la mesure de l'angle ["+fixed.getX()+","+fixed.getY()+"] et ["+p2.getX()+","+p2.getY()+"]");

		
		
		Double angle = null;
		angle=a2-a1;
		System.out.println("a2-a2="+angle);
		if (angle>=0)angle=360-angle;
		else if (angle<0)angle*=-1;
		//else angle =360;


		return angle;
	}


	static double angleVector(Point o,Point m) throws NullVectorExceotion{

		long x=(long) (m.getX()-o.getX());
		long y=(long) (m.getY()-o.getY());
		double t=0;
		if(x==0){
			if(y>0){
				return Math.toDegrees(Math.PI/2);
			}
			else if(y<0){
				return  Math.toDegrees(-(Math.PI/2));
			}
			else
			{
					throw new NullVectorExceotion("zero vector");
			}
		}
		else
		{
			t=Math.atan2(y,x);
		}
		return Math.toDegrees(t);
	}

	private static Point extractPos(StringMessage m) {
		long x = 0 ,y = 0;
		Pattern pattern = Pattern.compile ("\\d+");
		String target = m.toString();
		
		
		
		Matcher matcher = pattern.matcher (target);
		
		
		
		if(matcher.find())
			x=Integer.parseInt(matcher.group());		
		if(matcher.find())
			y=Integer.parseInt(matcher.group());

		Point p=new Point();
		p.setLocation(x, y);
		return p;// new Point(x,y);
	}
	


	private String convertToString(Stack<MessageWithInfo> staskMessageWithInfo) {
		@SuppressWarnings("unchecked")
		Stack<MessageWithInfo> duplicate = (Stack<MessageWithInfo>) staskMessageWithInfo.clone();
		String s="";
		while (!duplicate.empty()){
			String line =duplicate.pop().toString();
			s+=line+"\n";
		}
		return s;
	}


	private void sendPositionsRequest() {
		for (int i = 0; i <  this.getArity(); i++) {
			System.out.println("this.sendTo("+i+", new StringMessage(RequestPosition));");
			this.sendTo(i, new StringMessage("RequestPosition"));	
		}
	}
}
