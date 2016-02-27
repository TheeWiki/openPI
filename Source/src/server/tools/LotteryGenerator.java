package server.tools;

import server.util.Misc;

/**
 * A fun and simple Random Number Generator (RNG) for picking a lottery number,
 * much like the real life lottery.
 * 
 * @author Dennis
 *
 */
public class LotteryGenerator {
	
	/**
	 * Creates a main void which creates a runnable source
	 * for picking random numbers between 0-5 to be randomly generated.
	 * @param args
	 */
	public static void main(String[] args) {
		for (int i = 0; i < 5; i++) {
			System.out.println("Random Number Picked was: " + Misc.random(i));
		}
	}
}