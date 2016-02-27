package server.model.content;

import server.Server;
import server.model.players.Player;

/**
 * TODO: finish/fix
 * @author Dennis
 *
 */
public class KillLog {
	/*
	 * always add new npcs to the end of the array to prevent conflicts with
	 * existing saved data 
	 */

	private static final int[] NPCS = { 2745, 2552, 6260, 6222, 1158, 50, 6247,
			3200, 1160, 5902, 3340, 2882, 2883, 2881, 1615, 2783, 5666, 3847,
			5247, 5363, 3068, 5422, 2349, 4175, 4173, 4172 };

	private static final int[] NOTIFY_KC = { 100, 250, 500, 750, 1000, 1250,
			1500, 1750, 2000, 2250, 2500, 2750, 3000, 5000, 10000 };

	public static final int[] KILLS_REQUIRED_FOR_PET = { 200, 200, 500, 500,
			300, 400, 500, 200, 300, 200, 200, 150, 150, 150, 2000, 1000, 200,
			300, 100, 1000, 1000, 500, 200, 200, 200 };

	public static final int[] PETS = { 4002, 2553, 4001, 4005, 4004, 4000,
			4007, 4003, 4017, 4018, 4019, 4008, 4009, 4010, 4020, 4121, 4125,
			4129, 4122, 4127, 4126, 5424 };

	public static void killInterface(Player player) {
		player.getPA().sendFrame126("@blu@NPC Kill Log", 8144);

		player.getPA().sendFrame126("", 8145);
		player.getPA().sendFrame126("", 8146);
		for (int i = 0; i < 101; i++) {
			player.getPA().sendFrame126("", 8147 + i);
		}
		for (int i = 0; i < NPCS.length; i++) {
			if (NPCS[i] > 0) {
				String name = Server.npcHandler.getNpcName(NPCS[i]);
				player.getPA().sendFrame126(
						"@dre@" + name + "@bla@: " + player.loggedKills[i],
						8147 + i);
			}
		}
		player.getPA().showInterface(8134);
	}

	public static boolean isBossPet(int id) {
		for (int i = 0; PETS.length > i; i++) {
			if (PETS[i] == id)
				return true;
		}
		return false;

	}

	public static boolean hasKills(Player p, int index) {
		return p.loggedKills[index] >= getKillsForPet(index);
	}

	public static int getPet(int index) {
		return PETS[index];
	}

	public static int getKillsForPet(int index) {
		return KILLS_REQUIRED_FOR_PET[index];
	}

	public static void handleKill(Player player, int npcType) {
		if (isNpc(npcType)) {
			int index = getNpcArrayIndex(npcType);
			player.loggedKills[index] += 1;
			String name = Server.npcHandler.getNpcName(npcType);
//			if (name.contains("Inad"))
//				name = "Inadaquecy";
//			else if (name.contains("King black"))
//				name = "KBD";
			name.replace("_", " ");
			player.sendMessage("You now have @dre@" + player.loggedKills[index]
					+ "@bla@ " + name + " kills.");
			if (shouldNotify(player.loggedKills[index])) {
				player.sendMessage("@grd@" + player.playerName + " has achieved " + player.loggedKills[index] + " " + name + " kills!");
				player.addPoints(10);
//				FileLog.writeLatestAchievment(player.playerName
//						+ " has achieved " + player.loggedKills[index] + " "
//						+ name + " kills.");

			}
		}
	}

	public static boolean isNpc(int npcType) {
		for (int i = 0; NPCS.length > i; i++) {
			if (NPCS[i] == npcType)
				return true;
		}
		return false;
	}

	private static boolean shouldNotify(int kills) {
		for (int i = 0; NOTIFY_KC.length > i; i++) {
			if (kills == NOTIFY_KC[i])
				return true;
		}
		return false;
	}

	public static int getNpcArrayIndex(int npcType) {
		for (int i = 0; NPCS.length > i; i++) {
			if (NPCS[i] == npcType)
				return i;
		}
		return -1;
	}

	public static int getBossPetIndex(int npcType) {
		for (int i = 0; PETS.length > i; i++) {
			if (PETS[i] == npcType)
				return i;
		}
		return -1;
	}

}
