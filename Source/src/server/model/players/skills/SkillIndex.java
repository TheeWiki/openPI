package server.model.players.skills;

import java.util.HashMap;

public enum SkillIndex 
{
	ATTACK(0),
	DEFENCE(1),
	STRENGTH(2),
	HITPOINTS(3),
	RANGE(4),
	PRAYER(5),
	MAGIC(6);
	
	private int skillID;
	
	public int getSkillID()
	{
		return skillID;
	}
	
	SkillIndex(int skillID)
	{
		this.skillID = skillID;
	}
	
	public static SkillIndex getSkill(int skill)
	{
		for (SkillIndex index : values()) {
			if (skill == index.getSkillID())
			{
				return index;
			}
		}
		return null;
	}
}
