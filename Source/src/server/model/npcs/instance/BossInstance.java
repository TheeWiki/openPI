package server.model.npcs.instance;

import java.util.HashMap;

/**
 * The {@link #BossInstance(int, int, int, int, int, boolean, boolean)} is an
 * enumeration where the bosses data are set, and is then ready to be used
 * outside of the class
 * {@link #BossInstance(int, int, int, int, int, boolean, boolean)}, which will
 * then be utilized in the class {@link InstanceController}.
 * 
 * @author Dennis
 *
 */
public enum BossInstance {

	MAN(2, 90, 19, 200, 200, true, true);

	/**
	 * Integer variables that are used in the @enum BossInstance
	 */
	private int npcId, HP, maxHit, att, def;
	/**
	 * Booleans variables that are used in the @enum BossInstance
	 */
	private boolean aggressive, headIcon;

	/**
	 * Gets the NPC id, and returns the npcId id
	 * 
	 * @return npcId
	 */
	public int getNpcID() {
		return npcId;
	}

	/**
	 * Gets the Hitpoints value and returns HP
	 * 
	 * @return HP
	 */
	public int getHP() {
		return HP;
	}

	/**
	 * Gets the max hit of the NPC, and returns max hit
	 * 
	 * @return maxHit
	 */
	public int maxHit() {
		return maxHit;
	}

	/**
	 * Gets the attack accuracy of the NPC, returns the attack value
	 * 
	 * @return att
	 */
	public int getAtt() {
		return att;
	}

	/**
	 * Gets the defence of the NPC, returns the def value
	 * 
	 * @return def
	 */
	public int getDef() {
		return def;
	}

	/**
	 * Gets the aggression boolean and returns aggressive
	 * 
	 * @return aggressive
	 */
	public boolean isAgressive() {
		return aggressive;
	}

	/**
	 * Gets the boolean headIcon, returns headIcon
	 * 
	 * @return headIcon
	 */
	public boolean hasHeadIcon() {
		return headIcon;
	}

	/**
	 * Constructs the
	 * {@link #BossInstance(int, int, int, int, int, boolean, boolean)}
	 * enumeration and sets the core values accordingly the to
	 * {@link #BossInstance(int, int, int, int, int, boolean, boolean) #name().
	 * 
	 * @param npcId
	 * @param HP
	 * @param maxHit
	 * @param att
	 * @param def
	 * @param aggressive
	 * @param headIcon
	 */
	private BossInstance(int npcId, int HP, int maxHit, int att, int def, boolean aggressive, boolean headIcon) {
		this.npcId = npcId;
		this.HP = HP;
		this.maxHit = maxHit;
		this.att = att;
		this.def = def;
		this.aggressive = aggressive;
		this.headIcon = headIcon;
	}

	/**
	 * The {@link #bossInstanceMap} is where the values of
	 * {@link #BossInstance(int, int, int, int, int, boolean, boolean)} will be
	 * stored inside of.
	 */
	private static HashMap<Integer, BossInstance> bossInstanceMap = new HashMap<Integer, BossInstance>();

	/**
	 * Loops through the enumeration and puts the values inside of the HashMap.
	 */
	static {
		for (BossInstance boss : values()) {
			bossInstanceMap.put(boss.getNpcID(), boss);
			bossInstanceMap.put(boss.getHP(), boss);
			bossInstanceMap.put(boss.maxHit(), boss);
			bossInstanceMap.put(boss.getAtt(), boss);
			bossInstanceMap.put(boss.getDef(), boss);
		}
	}
}