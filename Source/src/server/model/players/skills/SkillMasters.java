package server.model.players.skills;

import java.util.HashMap;

public enum SkillMasters
{

	ATTACK(1, 1, 1, 1, ""),
	DEFENCE(1, 1, 1, 1, ""),
	STRENGTH(1, 1, 1, 1, ""),
	HITPOINTS(1, 1, 1, 1, ""),
	RANGE(1, 1, 1, 1, ""),
	PRAYER(1, 1, 1, 1, ""),
	MAGIC(1, 1, 1, 1, ""),
	COOKING(1, 1, 1, 1, ""),
	WOODCUTTING(1, 1, 1, 1, ""),
	FLETCHING(1, 1, 1, 1, ""),
	FISHING(1, 1, 1, 1, ""),
	FIREMAKING(1, 1, 1, 1, ""),
	CRAFTING(1, 1, 1, 1, ""),
	SMITHING(1, 1, 1, 1, ""),
	MINING(1, 1, 1, 1, ""),
	HERBLORE(1, 1, 1, 1, ""),
	AGILITY(1, 1, 1, 1, ""),
	THIEVING(1, 1, 1, 1, ""),
	SLAYER(1, 1, 1, 1, ""),
	FARMING(1, 1, 1, 1, ""),
	RUNECRAFTING(1, 1, 1, 1, "");

	private int npcId, capeId, hoodId, dialogueId;
	private String name;
	
	public int getNpcId() {
		return npcId;
	}

	public int getCape() {
		return capeId;
	}

	public int getHood() {
		return hoodId;
	}

	public int getDialogue() {
		return dialogueId;
	}

	public String getName()
	{
		return name;
	}
	private SkillMasters(int npcId, int capeId, int hoodId, int dialogueId, String name) {
		this.npcId = npcId;
		this.capeId = capeId;
		this.hoodId = hoodId;
		this.dialogueId = dialogueId;
		this.name = name;
	}
	
	private static HashMap<Integer, SkillMasters> skillMastersMap = new HashMap<Integer, SkillMasters>();
	
	static
	{
		for (SkillMasters sm : values()) {
			skillMastersMap.put(sm.getNpcId(), sm);
			skillMastersMap.put(sm.getCape(), sm);
			skillMastersMap.put(sm.getHood(), sm);
			skillMastersMap.put(sm.getDialogue(), sm);
		}
	}
}