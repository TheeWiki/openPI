package server.model.players.skills;

import java.util.HashMap;

/**
 * The @enum {@link #SkillIndex(int, double)} is used for getting the
 * {@link #getSkillId()} and {@link #expRatio} when adding skill exp
 * when training a specific skill, or used however else.
 * 
 * @author Dennis
 *
 */
public enum SkillIndex 
{
	ATTACK(0, 25.5),
	DEFENCE(1, 25.5),
	STRENGTH(2, 25.5),
	HITPOINTS(3, 20.0),
	RANGE(4, 25.0),
	PRAYER(5, 15.8),
	MAGIC(6, 20.0),
	COOKING(7, 45.0),
	WOODCUTTING(8, 30.0),
	FLETCHING(9, 30.0),
	FISHING(10, 28.7),
	FIREMAKING(11, 55.3),
	CRAFTING(12, 40.0),
	SMITHING(13, 30.5),
	MINING(14, 35.0),
	HERBLORE(15, 35.9),
	AGILITY(16, 40.5),
	THIEVING(17, 45.6),
	SLAYER(18, 32.7),
	FARMING(19, 43.8),
	RUNECRAFTING(20, 40.7);
	
	/**
	 * The Integer skillId, is a variable that is used in {@link #getSkillId()},
	 * that contains its own appropriate value.
	 */
	private int skilId;
	/**
	 * The Double expRatio, is a variable that is used in {@link #getExpRatio()}
	 * , that contains its own appropriate value.
	 */
	private double expRatio;

	/**
	 * Gets the variable {@link #skilId}, and returns its public value to be
	 * utilized outside the {@link #SkillIndex(int, double)} enumeration class.
	 * 
	 * @return skilId
	 */
	public int getSkillId() {
		return skilId;
	}

	/**
	 * Gets the variable {@link #expRatio}, and returns its public value to be
	 * utilized outside the {@link #SkillIndex(int, double)} enumeration class.
	 * 
	 * @return expRatio
	 */
	public double getExpRatio() {
		return expRatio;
	}

	/**
	 * Constructs the {@link #SkillIndex(int, double)} enum class.
	 * 
	 * @param skillId
	 * @param expRatio
	 */
	private SkillIndex(int skillId, double expRatio) {
		this.skilId = skillId;
		this.expRatio = expRatio;
	}

	/**
	 * Loops through the {@link #SkillIndex(int, double)} enumeration and
	 * returns the values of {@link #getSkillId()} as the skill parameter.
	 * 
	 * @param skill
	 * @return
	 */
	public static int getSkills(int skill)
	{
		for (SkillIndex si : values()) {
			skill = si.getSkillId();
		}
		return skill;
	}
	/**
	 * This is a HashMap, in where the variables of
	 * {@link #SkillIndex(int, double)} will be stored inside of.
	 */
	private static HashMap<Integer, SkillIndex> skillMap = new HashMap<Integer, SkillIndex>();

	/**
	 * The @enum SkillIndex values are now being looped and being put inside of
	 * the HashMap known as skillMap.
	 */
	static {
		for (SkillIndex si : values()) {
			skillMap.put(si.getSkillId(), si);
		}
	}
}