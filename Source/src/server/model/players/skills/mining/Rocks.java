package server.model.players.skills.mining;

import java.util.HashMap;

public enum Rocks 
{

	ESSENCES(0, 1050, 45),
	COPPER(0, 1050, 60),
	TIN(0, 1050, 60),
	COAL(0, 1050, 150),
	MITHRIL(0, 1050, 257),
	ADAMANT(0, 1050, 371),
	RUNE(0, 1050, 493);
	
	private int objectId, itemId, experience;
	
	public int getObjectId()
	{
		return objectId;
	}
	public int getItemId()
	{
		return itemId;
	}
	public int getExperience()
	{
		return experience;
	}
	Rocks(int objectId, int itemId, int experience)
	{
		this.objectId = objectId;
		this.itemId = itemId;
		this.experience = experience;
	}
	
	private static HashMap<Integer, Rocks> rocksMap = new HashMap<Integer, Rocks>();
	
	public static Rocks getRock(int rock)
	{
		return rocksMap.get(rock);
	}
	
	static
	{
		for (Rocks rocks : values()) {
			rocksMap.put(rocks.getObjectId(), rocks);
		}
	}
}
