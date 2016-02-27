package server.model.players.skills;

/**
 * When the player is training the specific skill from
 * {@link #SkillCapeBonuses(int, int)} and is wearing the correct
 * {@link #capes} they will receive addition {@link #exp} upon every skill
 * execution.
 * 
 * This helps the importance of Skillcapes become more beneficial for the players
 * rather than something to just show off.
 * 
 * @author Dennis
 *
 */
public enum SkillCapeBonuses
{
	ATTACK(new int[] {9747, 9748}, 500),
	STRENGTH(new int[] {9750, 9751}, 500),
	DEFENCE(new int[] {9753, 9754}, 500),
	RANGING(new int[] {9756, 9757}, 500),
	PRAYER(new int[] {9759, 9760}, 500),
	MAGIC(new int[] {9762, 9763}, 500),
	RUNECRAFTING(new int[] {9765, 9766}, 500),
	HITPOINTS(new int[] {9768, 9769}, 500),
	AGILITY(new int[] {9771, 9772}, 500),
	HERBLORE(new int[] {9774, 9775}, 500),
	THIEVING(new int[] {9777, 9778}, 500),
	CRAFTING(new int[] {9780, 9781}, 500),
	FLETCHING(new int[] {9783, 9784}, 500),
	SLAYER(new int[] {9786, 9787}, 500),
	CONSTRUCTION(new int[] {9789, 9790}, 500),
	MINING(new int[] {9792, 9793}, 500),
	SMITHING(new int[] {9795, 9796}, 500),
	FISHING(new int[] {9798, 9799}, 500),
	COOKING(new int[] {9801, 9802}, 500),
	FIREMAKING(new int[] {9804, 9805}, 500),
	WOODCUTTING(new int[] {9807, 9808}, 500);

	/**
	 * Sets the first value of the {@link #SkillCapeBonuses(int[], int)}
	 * enumeration.
	 */
	private int capes[];

	/**
	 * Sets the seond value of the {@link #SkillCapeBonuses(int[], int)}
	 * enumeration, in which determines how much experience will be rewarded to
	 * the player.
	 */
	private int exp;

	/**
	 * Gets the {@link #capes} variable and returns it as {@link #getCapes()}.
	 * 
	 * @return
	 */
	public int[] getCapes() {
		return capes;
	}

	/**
	 * Gets the {@link #exp} variable and returns it as {@link #getExp()}.
	 * 
	 * @return
	 */
	public int getExp() {
		return exp;
	}
	/**
	 * Constructs the {@link #SkillCapeBonuses(int, int, int)} enumeration and
	 * sets the core values accordingly.
	 * 
	 * @param capeId
	 * @param exp
	 */
	private SkillCapeBonuses(int[] capes, int exp) {
		this.capes = capes;
		this.exp = exp;
	}
}