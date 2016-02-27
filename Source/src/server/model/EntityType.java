package server.model;

import java.util.HashMap;

/**
 * Gets the entity value of who follows you, or who's attacking you.
 * 
 * @author Dennis
 *
 */
public enum EntityType {

	PLAYER(0), NPC(1), OBJECT(2);

	/**
	 * Sets the {@link #EntityType(int)} variable name, and sets its value
	 * accordingly
	 */
	private int entityValue;

	/**
	 * Gets the {@link #entityValue} variable and returns the value of
	 * {@link #entityValue}
	 * 
	 * @return
	 */
	public int getEntityValue() {
		return entityValue;
	}

	/**
	 * Constructions the enumeration of {@link #EntityType(int)}
	 * 
	 * @param entityValue
	 */
	private EntityType(int entityValue) {
		this.entityValue = entityValue;
	}

	/**
	 * Creates a HashMap, where the variable {@link #entityValue} will be stored
	 * inside of. The HashMap will give the user the option to get
	 * {@link #hashCode()} or put {@link #hashCode()}
	 */
	private static HashMap<Integer, EntityType> entityMap = new HashMap<Integer, EntityType>();

	/**
	 * Loops through the @enum values, and puts the values accordingly
	 */
	static {
		for (EntityType entity : values()) {
			entityMap.put(entity.getEntityValue(), entity);
		}
	}
}