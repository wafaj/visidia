package visidia.simulation.evtack;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Vector;

import visidia.simulation.process.criterion.Criterion;

// TODO: Auto-generated Javadoc
/**
 * A VQueue object implements a FIFO (First In First Out) list. It can be used
 * in multi-threads environment since all it's methods are synchronized. The get
 * (resp. put) method blocks when there is no element to return (resp. when the
 * queue contains maxSize elements). If the calling thread is interrupted while
 * it is blocked in these methods, the <code>InterruptedException</code> is
 * thrown.
 */
public class VQueue {
	
	/** The maximum length of queue. */
	private int maxSize;

	/** The queue. */
	private LinkedList<Object> queue;

	/**
	 * Constructs a queue with default maximum size which is Integer.MAX_VALUE.
	 */
	public VQueue() {
		this(Integer.MAX_VALUE);
	}

	/**
	 * Constructs a queue with maximum size <code>maxSize</code>.
	 * 
	 * @param maxSize the max size
	 */
	public VQueue(int maxSize) {
		this.maxSize = maxSize;
		this.queue = new LinkedList<Object>();
	}

	/**
	 * Returns the first element in the queue. If the queue is empty, this method
	 * blocks until an element is available.
	 * 
	 * @return the object
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public synchronized Object get() throws InterruptedException {
		while (this.queue.isEmpty()) {
			this.wait();
		}
		this.notifyAll();
		return this.queue.removeFirst();
	}

	/**
	 * Returns the first element in the queue that match the criterion
	 * <code>c</code>. If the queue does not contain any element that match
	 * the criterion <code>c</code>, this method blocks until one element matching
	 * <code>c</code> becomes available.
	 * 
	 * @param c the criterion
	 * 
	 * @return the object
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public synchronized Object get(Criterion c) throws InterruptedException {
		while (true) {
			ListIterator<Object> li = this.queue.listIterator();
			while (li.hasNext()) {
				Object o = li.next();
				if (c.isMatchedBy(o)) {
					li.remove();
					return o;
				}
			}
			this.wait();
		}
	}

	/**
	 * Returns the first element in the queue that matches the criterion
	 * <code>c</code>. If the queue does not contains any element that match
	 * the criterion <code>c</code>, this method returns null.
	 * 
	 * @param c the criterion
	 * 
	 * @return the no wait
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public synchronized Object getNoWait(Criterion c)
	throws InterruptedException {
		ListIterator<Object> li = this.queue.listIterator();
		while (li.hasNext()) {
			Object o = li.next();
			if (c.isMatchedBy(o)) {
				li.remove();
				return o;
			}
		}

		return null;
	}

	/**
	 * Returns all elements in the queue that match the criterion <code>c</code>.
	 * If the queue does not contain any element that match the criterion
	 * <code>c</code>, this method returns null.
	 * 
	 * @param c the criterion
	 * 
	 * @return the all no wait
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public synchronized Vector<Object> getAllNoWait(Criterion c)
	throws InterruptedException {
		Vector<Object> v = new Vector<Object>();
		ListIterator<Object> li = this.queue.listIterator();
		while (li.hasNext()) {
			Object o = li.next();
			if (c.isMatchedBy(o)) {
				li.remove();
				v.addElement(o);
			}
		}
		if (v.size() == 0) {
			return null;
		} else {
			return v;
		}
	}

	/**
	 * Adds one element at the end of the queue. If the queue contains
	 * <code>maxSize</code> elements this method blocks until one element is
	 * consumed.
	 * 
	 * @param obj the object
	 * 
	 * @throws InterruptedException the interrupted exception
	 */
	public synchronized void put(Object obj) throws InterruptedException {
		while (this.queue.size() >= this.maxSize) {
			this.wait();
		}
		this.queue.addLast(obj);
		this.notifyAll();
	}

	/**
	 * Returns true if the queue is empty.
	 * 
	 * @return true, if is empty
	 */
	public synchronized boolean isEmpty() {
		return this.queue.isEmpty();
	}

	/**
	 * Returns true if the queue contains one element that matches the criterion c.
	 * 
	 * @param c the criterion
	 * 
	 * @return true, if an element is found
	 */
	public synchronized boolean contains(Criterion c) {
		ListIterator<Object> li = this.queue.listIterator();
		while (li.hasNext()) {
			Object o = li.next();
			if (c.isMatchedBy(o)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Removes all elements from the queue.
	 */
	public synchronized void purge() {
		this.queue = new LinkedList<Object>();
	}

	/**
	 * Returns this queue maximum size.
	 * 
	 * @return the max size
	 */
	public int getMaxSize() {
		return this.maxSize;
	}

	/**
	 * Do not use this method if your are not sure about what you are doing. It
	 * notifies all threads waiting on a new entry in the queue.
	 */
	public synchronized void notifyAllGet() {
		this.notifyAll();
	}

}
