package server.model.players.skills.mining;

import java.util.HashMap;

public enum Pickaxes {
	
	BRONZE(1265, 1),
	IRON(1267, 1),
	STEEL(1269, 10),
	MITHRIL(1273, 21),	
	ADAMANT(1271, 31),
	RUNE(1275, 41);

	private int pickaxeId, levelReq;

	public int getPickaxeId() {
		return pickaxeId;
	}

	public int getLevelReq() {
		return levelReq;
	}

	Pickaxes(int pickaxeId, int levelReq) {
		this.pickaxeId = pickaxeId;
		this.levelReq = levelReq;
	}

	private static HashMap<Integer, Pickaxes> pickaxeMap = new HashMap<Integer, Pickaxes>();

	public static Pickaxes getPickaxe(int pickaxe) {
		return pickaxeMap.get(pickaxe);
	}
	
	static
	{
		for (Pickaxes pickaxes : values()) {
			pickaxeMap.put(pickaxes.getPickaxeId(), pickaxes);
			pickaxeMap.put(pickaxes.getLevelReq(), pickaxes);
		}
	}
}
