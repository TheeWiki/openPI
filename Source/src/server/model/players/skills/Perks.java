package server.model.players.skills;

import java.util.HashMap;

public enum Perks {
	/*
	 * Cooking perk
	 */
	COOKED_PERFECTLY(2.4),
	/*
	 * Herblore perk
	 */
	POTION_MASTER(3.2),
	/*
	 * Mining perk
	 */
	GOLD_MINE(2.8),
	/*
	 * Prayer perk
	 */
	HIGH_FAITH(2.1),
	/*
	 * Agility perk
	 */
	SWIFT_MOVEMENT(2.1),
	/*
	 * Crafting perk
	 */
	CUT_TO_PERFECTION(2.6),
	/*
	 * Firemaking perk
	 */
	QUICK_BURNER(3.8),
	/*
	 * Fishing perk
	 */
	FISH_BUISSNESS(3.4),
	/*
	 * Fletching perk
	 */
	SHARP_KNIFE(2.8),
	/*
	 * Runecrafting perk
	 */
	ENHANCED_ESSENCES(2.4),
	/*
	 * Slayer perk
	 */
	SLAYERS_FURY(2.3),
	/*
	 * Smithing perk
	 */
	EASY_SMITHING(3.4),
	/*
	 * Thieving perk
	 */
	GOLD_FINGER(2.7),
	/*
	 * Woodcutting perk
	 */
	SHARPER_AXE(2.6);

	/**
	 * The variable known as {@link #exp} contains the core value in which
	 * Determines the additional experience the player will receive when
	 * training the skill with the selected perk.
	 */
	private double exp;

	/**
	 * Gets the {@link #exp} variable and returns it as {@link #getXP()}.
	 * 
	 * @return xp
	 */
	public double getXP() {
		return exp;
	}

	/**
	 * Constructs the {@link #Perks(double)} enumeration, sets the core values
	 * accordingly to the perks name and then is ready to be used outside of the
	 * {@link #Perks(double)} class.
	 * 
	 * @param exp
	 */
	private Perks(double exp) {
		this.exp = exp;
	}

	/**
	 * The {@link #perksMap} is a HashMap that will contain all of the
	 * {@link #exp} values, and stores it inside.
	 */
	private static HashMap<Double, Perks> perksMap = new HashMap<Double, Perks>();

	/**
	 * Loops through the enumeration and puts the values inside of the HashMap.
	 */
	static {
		for (Perks perk : values()) {
			perksMap.put(perk.getXP(), perk);
		}
	}
}