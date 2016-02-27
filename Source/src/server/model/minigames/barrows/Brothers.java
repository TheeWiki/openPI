package server.model.minigames.barrows;

import java.util.HashMap;

public enum Brothers 
{
	TORAG(2029, 120, 20, 200, 200, true, true),
	VERAC(2030, 120, 25, 200, 200, true, true),
	KARIL(2028, 90, 17, 200, 200, true, true),
	GUTHAN(2027, 120, 23, 200, 200, true, true),
	DHAROK(2026, 120, 45, 250, 250, true, true),
	AHRIM(2025, 90, 19, 200, 200, true, true);
	
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
	 * @return npcId
	 */
	public int getNpcID()
	{
		return npcId;
	}
	/**
	 * Gets the Hitpoints value and returns HP
	 * @return HP
	 */
	public int getHP()
	{
		return HP;
	}
	/**
	 * Gets the max hit of the NPC, and returns max hit
	 * @return maxHit
	 */
	public int maxHit()
	{
		return maxHit;
	}
	/**
	 * Gets the attack accuracy of the NPC, returns the attack value
	 * @return att
	 */
	public int getAtt()
	{
		return att;
	}
	/**
	 * Gets the defence of the NPC, returns the def value
	 * @return def
	 */
	public int getDef()
	{
		return def;
	}
	/**
	 * Gets the aggression boolean and returns aggressive
	 * @return aggressive
	 */
	public boolean isAgressive()
	{
		return aggressive;
	}
	/**
	 * Gets the boolean headIcon, returns headIcon
	 * @return headIcon
	 */
	public boolean hasHeadIcon()
	{
		return headIcon;
	}
	
	Brothers(int npcId,int HP,int maxHit,int att,int def, boolean aggressive, boolean headIcon)
	{
		this.npcId = npcId;
		this.HP = HP;
		this.maxHit = maxHit;
		this.att = att;
		this.def = def;
		this.aggressive = aggressive;
		this.headIcon = headIcon;
	}
	
	private static HashMap<Integer, Brothers> brothersMap = new HashMap<Integer, Brothers>();
	
	static 
	{
		for (Brothers brothers : values()) {
			brothersMap.put(brothers.getNpcID(), brothers);
		}
	}
}