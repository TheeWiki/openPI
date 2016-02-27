package server.model.minigames.duel_arena;

import java.util.HashMap;

/**
 * Rules identifier enumeration
 * 
 * @author Dennis
 */
public enum Rules {
	
	WALKING_RULE(1),
	RANGE_RULE(2),
	MELEE_RULE(3),
	MAGIC_RULE(4),
	DRINK_RULE(5),
	EAT_RULE(6),
	PRAYER_RULE(7),
	OBSTICLES_RULE(8),
	FUN_WEAPONS_RULE(9),
	SPECIAL_ATTACKS_RULE(10),
	HATS_RULE(11),
	CAPES_RULE(12),
	AMULET_RULE(13),
	WEAPONS_RULE(14),
	BODY_ARMOR_RULE(15),
	SHIELD_RULE(16),
	LEGS_ARMOR(17),
	GLOVES_RULE(18),
	BOOTS_RULE(19),
	RINGS_RULE(20),
	ARROWS_RULE(21);
	
	/**
	 * This is the integer that is used in @enum Rules, which contains its own
	 * special value dedicated to the enum type.
	 */
	private int ruleId;

	/**
	 * Gets the Integer known as ruleId, and returns its value as ruleId
	 * 
	 * @return ruleId
	 */
	public int getRule() {
		return ruleId;
	}

	/**
	 * Constructs the @enum Rules and sets the appropriate values
	 * 
	 * @param ruleId
	 */
	private Rules(int ruleId) {
		this.ruleId = ruleId;
	}

	/**
	 * Places all of @enum Rules values inside of a HashMap {@link #hashCode()}
	 */
	private static HashMap<Integer, Rules> rulesMap = new HashMap<Integer, Rules>();
	
	/**
	 * Loops between the values of @enum Rules and sets the ruleId, which is the
	 * ready to be used outside of the @enum Rules class
	 */
	static {
		for (Rules rule : values()) {
			rulesMap.put(rule.getRule(), rule);
		}
	}
}