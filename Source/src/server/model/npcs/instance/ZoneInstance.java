package server.model.npcs.instance;

import java.util.HashMap;

/**
 * The {@link #ZoneInstance(int, int, int, int)} is an enum that controls the
 * object interactions of the instance system that is inside this package.
 * 
 * @author Dennis
 *
 */
public enum ZoneInstance {

	KBD_ENTERANCE(2, 3222, 3222, 995, 200_000);

	/**
	 * Integer values that are used in the
	 * {@link #ZoneInstance(int, int, int, int)}, setting their core values
	 * accordingly to the {@link #objectId}.
	 */
	private int objectId, locX, locY, currency, cost;

	/**
	 * Gets the variable {@link #objectId} and return the value.
	 * 
	 * @return objectId
	 */
	public int getObject() {
		return objectId;
	}

	/**
	 * Gets the variable {@link #locX} and returns the value.
	 * 
	 * @return locX
	 */
	public int getLocX() {
		return locX;
	}

	/**
	 * Gets the variable {@link #locY} and returns the value.
	 * 
	 * @return locY
	 */
	public int getLocY() {
		return locY;
	}

	/**
	 * Gets the currency type used to pay for the instance zone.
	 * 
	 * @return
	 */
	public int getCurrency() {
		return currency;
	}

	/**
	 * Gets the variable {@link #cost} and returns the value.
	 * 
	 * @return cost
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * Constructs the {@link #ZoneInstance(int, int, int, int)} enumeration and
	 * sets the values accordingly
	 * 
	 * @param objectId
	 * @param locX
	 * @param locY
	 * @param cost
	 */
	private ZoneInstance(int objectId, int locX, int locY, int currency, int cost) {
		this.objectId = objectId;
		this.locX = locX;
		this.locY = locY;
		this.currency = currency;
		this.cost = cost;
	}

	/**
	 * {@link #zoneInstanceMap} is where the
	 * {@link #ZoneInstance(int, int, int, int)} variables will stored inside of
	 */
	private static HashMap<Integer, ZoneInstance> zoneInstanceMap = new HashMap<Integer, ZoneInstance>();

	/**
	 * Loops through the enumerations values, and puts them in the HashMap.
	 */
	static {
		for (ZoneInstance zi : values()) {
			zoneInstanceMap.put(zi.getObject(), zi);
			zoneInstanceMap.put(zi.getLocX(), zi);
			zoneInstanceMap.put(zi.getLocY(), zi);
			zoneInstanceMap.put(zi.getCurrency(), zi);
			zoneInstanceMap.put(zi.getCost(), zi);
		}
	}
}