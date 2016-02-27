package server.model.minigames.pest_control;

import java.util.HashMap;

public enum Pests 
{
	//TODO Get npc ids/statistics
	VOID_KNIGHT(50, 90, 19, 200, 200, false, true),
	BLUE_PORTAL(50, 90, 19, 200, 200, false, false),
	RED_PORAL(50, 90, 19, 200, 200, false, false),
	GREEN_PORTAL(50, 90, 19, 200, 200, false, false),
	WHITE_PORTAL(50, 90, 19, 200, 200, false, false),
	CRAWLER(50, 90, 19, 200, 200, false, false),
	SPINNER(50, 90, 19, 200, 200, false, false),
	BRUTE(50, 90, 19, 200, 200, false, false);

	/**
	 * Integer variables that are used in the @enum Brothers
	 */
	private int npcId, HP, maxHit, att, def;
	/**
	 * Booleans variables that are used in the @enum Brothers
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
	 * Constructs the {@link #Pests(int, int, int, int, int, boolean, boolean)}
	 * enumeration and sets the values according to the proper npc type.
	 * 
	 * @param npcId
	 * @param HP
	 * @param maxHit
	 * @param att
	 * @param def
	 * @param aggressive
	 * @param headIcon
	 */
	private Pests(int npcId, int HP, int maxHit, int att, int def, boolean aggressive, boolean headIcon) {
		this.npcId = npcId;
		this.HP = HP;
		this.maxHit = maxHit;
		this.att = att;
		this.def = def;
		this.aggressive = aggressive;
		this.headIcon = headIcon;
	}

	private static HashMap<Integer, Pests> pestsMap = new HashMap<Integer, Pests>();

	static {
		for (Pests pests : values()) 
		{
			pestsMap.put(pests.getNpcID(), pests);
			pestsMap.put(pests.getAtt(), pests);
			pestsMap.put(pests.getHP(), pests);
			pestsMap.put(pests.maxHit(), pests);
			pestsMap.put(pests.getDef(), pests);
		}
	}
}