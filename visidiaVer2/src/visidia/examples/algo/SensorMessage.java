package visidia.examples.algo;

import java.awt.Point;
import java.util.Random;
import visidia.simulation.process.messages.Message;
import visidia.simulation.process.messages.MessageType;

public class SensorMessage extends Message {
	
	private String data;
	private Point dest;
	private Point locationClaim;
	private Integer walkCounter;
	private Integer UID;
	private Point lastPos;
	private static Random r = new Random();

	public Point getDest(){
		return new Point(dest);
	}

	public void setDest(Point dest){
		this.dest = new Point(dest);
	}

	public SensorMessage(String data, Point dest,Point locationClaim) {
		this.data = new String(data);
		this.dest = new Point(dest);
		this.locationClaim = new Point(locationClaim);
		this.walkCounter = new Integer(0);
		this.UID = new Integer(r.nextInt());
	}

	public SensorMessage(String data, Point dest,Point locationClaim,MessageType type) {
		this.setType(type);
		this.data = new String(data);
		this.dest = new Point(dest);
		this.locationClaim = new Point(locationClaim);
		this.walkCounter = new Integer(0);
		this.UID = new Integer(r.nextInt());
	}

	public SensorMessage(String data, Point dest,Point locationClaim,MessageType type,Integer walkCounter) {
		this.setType(type);
		this.data = new String(data);
		this.dest = new Point(dest);
		this.locationClaim = new Point(locationClaim);
		this.walkCounter = new Integer(walkCounter);
		this.UID = new Integer(r.nextInt());
	}
	
	public SensorMessage(SensorMessage msg){
		this.data = new String(msg.data);
		this.dest = new Point(msg.dest);
		this.locationClaim = new Point(msg.locationClaim);
		this.walkCounter = new Integer(msg.walkCounter);
		this.UID = new Integer(msg.UID);
	}

	public Integer getUID(){
		return new Integer(this.UID);
	}

	public Point getClaim(){
		return new Point(locationClaim);
	}

	public String getLabel(){
		return new String(this.data);
	}

	public String data() {
		return new String(this.data);
	}

	public Integer getCounter(){
		return new Integer(this.walkCounter);
	}

	public void setCounter(Integer count){
		this.walkCounter = new Integer(count);
	}

	public void decCounter(){
		this.walkCounter -= 1;
	}

	@Override
	public Object clone() {
		return new SensorMessage(this.data,this.dest,this.locationClaim,this.getType(),this.walkCounter);
	}
	@Override
	public Object getData() {
		return new String(this.data);
	}
	@Override
	public String toString() {
		if( dest.equals(new Point(-1,-1)) ) return "LocClaim";
		//else return String.valueOf(this.dest.getX())+","+String.valueOf(this.dest.getY())+","+String.valueOf(this.walkCounter);
		else return String.valueOf(this.UID);
	}

	public Point getLastPos() {
		return lastPos;
	}

	public void setLastPos(Point lastPos) {
		this.lastPos =new Point( lastPos);
	}

}
