package server.model.players.skills;

import java.util.HashMap;

/**
 * This class is dedicated to the methods of how bonus experience is given when
 * a player is skilling, fighting, etc.. The {@link #AdditionalExp(double)}
 * offers a simple, lightweight system that is easy to use and understand for
 * the developers to modify to their own needs. Documentation is also done in
 * detail so that the developer knows each and every step of the
 * {@link #AdditionalExp(double)} enumeration.
 * 
 * @author Dennis
 *
 */
public enum AdditionalExp {

	/*
	 * Double experience weekend
	 */
	DOUBLE_EXP(2.0),
	/*
	 * Golden Hammer 
	 */
	GOLDEN_HAMMER(2.7),
	/*
	 * Golden Tinderbox
	 */
	GOLDEN_TINDERBOX(1.6),
	/*
	 * Ring of farming item bonus
	 */
	RING_OF_FARMING(1.7);

	/**
	 * This variable is known as a double, in which contains the core value in
	 * which the addition experience will come from, as seen in
	 * {@link #AdditionalExp(double)}. Values change based on what the player is
	 * wearing (Ring of Farming), taking apart in (events like double exp
	 * weekend, etc..).
	 */
	private double exp;

	/**
	 * Gets the variable {@link #exp} and returns its core value in a return
	 * variable as seen below.
	 * 
	 * @return exp
	 */
	public double getExp() {
		return exp;
	}

	/**
	 * This is the constructor in which builds the
	 * {@link #AdditionalExp(double)} enumeration.
	 * 
	 * @param exp
	 */
	private AdditionalExp(double exp) {
		this.exp = exp;
	}

	/**
	 * The HashMap known as {@link #expMap} is where the {@link #exp} ratios are
	 * being directly stored inside of.
	 */
	private static HashMap<Double, AdditionalExp> expMap = new HashMap<Double, AdditionalExp>();

	/**
	 * Now looping through the values of the enumeration and placing them inside
	 * of the HashMap.
	 */
	static {
		for (AdditionalExp ae : values()) {
			expMap.put(ae.getExp(), ae);
		}
	}
}