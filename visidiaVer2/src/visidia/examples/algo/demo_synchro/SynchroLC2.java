package visidia.examples.algo.demo_synchro;

import visidia.misc.SynchronizedRandom;
import visidia.simulation.process.algorithm.Algorithm;
import visidia.simulation.process.edgestate.MarkedState;
import visidia.simulation.process.messages.IntegerMessage;
import visidia.simulation.process.messages.Message;

/**
 * implemente l'algorithme de synchronisation en etoile. 
 */
public class SynchroLC2 extends Algorithm {

	private static final long serialVersionUID = 2414189399437281860L;

	@Override
	public String getDescription() {
		return "Implements the algorithm for LC2 star synchronization";
	}

	/**
	 * renvoie <code>true</code> si le noeud est centre d'une etoile
	 */
	public boolean synchroEtoile() {
		int arite = this.getArity();
		int[] answer = new int[arite];

		/* random */
		int choosenNumber = Math.abs(SynchronizedRandom.nextInt());

		/* Send to all neighbours */
		for (int i = 0; i < this.getArity(); i++) {
			this.sendTo(i, new IntegerMessage(new Integer(choosenNumber)));
		}
		// System.out.println( getId() + "nombre: "+choosenNumber);
		/* receive all numbers from neighbours */
		for (int i = 0; i < arite; i++) {
			Message msg = this.receiveFrom(i);
			answer[i] = ((IntegerMessage) msg).value();
		}

		/* get the max */
		int max = choosenNumber;
		for (int element : answer) {
			if (element >= max) {
				max = element;
			}
		}

		for (int i = 0; i < this.getArity(); i++) {
			this.sendTo(i, new IntegerMessage(new Integer(max)));
		}

		/* get alla answers from neighbours */
		for (int i = 0; i < arite; i++) {
			Message msg = this.receiveFrom(i);
			answer[i] = ((IntegerMessage) msg).value();
		}

		/* get the max */
		max = choosenNumber;
		for (int element : answer) {
			if (element >= max) {
				max = element;
			}
		}

		/* results */
		return choosenNumber >= max;
	}

	public String getState() {
		return this.getProperty("label").toString();
	}

	public void setState(String newState) {
		this.putProperty("label", newState);
	}

	@Override
	public Object clone() {
		return new SynchroLC2();
	}

	/**
	 * algorithme de synchronisation.
	 */
	@Override
	public void init() {
		/*
		 * boolean i = synchroEtoile(); if(i==true) setState("centre"); else
		 * setState("perdu");
		 */
		while (true) {
			this.setState("P");
			if (this.synchroEtoile()) {
				this.setState("C");

				int n = this.getArity();

				for (int door = 0; door < n; door++) {
					this.setDoorState(new MarkedState(true), door);
				}

				try {
					Thread.sleep(Math.round(20000 * SynchronizedRandom
							.nextFloat()));
				} catch (InterruptedException e) {
				}

				for (int door = 0; door < n; door++) {
					this.setDoorState(new MarkedState(false), door);
				}
			}

		}

	}

}
