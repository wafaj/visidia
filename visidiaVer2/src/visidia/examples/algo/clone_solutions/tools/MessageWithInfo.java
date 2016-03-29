package visidia.examples.algo.clone_solutions.tools;

import java.awt.Point;

import visidia.simulation.process.messages.Door;
import visidia.simulation.process.messages.Message;

public class MessageWithInfo {
	private Message msg;
	private Door door;
	private Point LastPos ;
	private long time;

	@Override
	public String toString() {
		return "MessageWithInfo [msg=" + msg + ", door=" + door + ", time=" + time + "]";
	}


	public MessageWithInfo(Message msg, Door door, Point lastPos, long time) {
		super();
		this.msg = msg;
		this.door = door;
		LastPos = lastPos;
		this.time = time;
	}

	public Message getM() {
		return msg;
	}
	public void setM(Message m) {
		this.msg = m;
	}

	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public Door getDoor() {
		return door;
	}
	public void setDoor(Door door) {
		this.door = door;
	}

	public Point getLastPos() {
		return LastPos;
	}

	public void setLastPos(Point lastPos) {
		LastPos = lastPos;
	}
}
