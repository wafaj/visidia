package visidia.misc;

import java.util.Random;

// TODO: Auto-generated Javadoc
/**
 * SynchronizedRandom is a generator of pseudorandom numbers in a distributed context with concurrent calls.
 * This generator is a singleton (unique instance).
 */
public class SynchronizedRandom {

	/** The generator. */
	private static Random generator = new Random();

	/**
	 * Generates an integer value.
	 * 
	 * @return the integer value
	 */
	public static synchronized int nextInt() {
		return (SynchronizedRandom.generator.nextInt()+1);

	}

	/**
	 * Generates an float value.
	 * 
	 * @return the float value
	 */
	public static synchronized float nextFloat() {
		return (SynchronizedRandom.generator.nextFloat()+1);
	}

}
