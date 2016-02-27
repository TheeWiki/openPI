package server.model.players.skills.herblore;

import java.util.HashMap;
import java.util.Map;

import server.model.players.Client;
import server.model.players.skills.SkillIndex;

public class Cleaning {

	public enum CleanData {// identify , clean, level, experience
		GUAM(199, 249, 1, 2.5), 
		MARRENTILL(201, 251, 5, 3.8), 
		TARROMIN(203, 253, 11, 5), 
		HARRALANDER(205, 255, 20, 6.3), 
		RANARR(207, 257, 25, 7.5), 
		TOADFLAX(3049, 2998, 30, 8), 
		IRIT(209, 259, 40, 8.8), 
		AVANTOE(211, 261, 48, 10), 
		KWUARM(213, 263, 54, 11.3), 
		SNAPDRAGON(3051, 3000, 59, 11.8), 
		CADANTINE(215, 265, 65, 12.5), 
		LANTADYME(2485, 2481, 67, 13.1), 
		DWARF_WEED(217, 267, 70, 13.8), 
		TORSTOL(219, 269, 75, 15);

		private int identifyId;
		private int cleanId;
		private int level;
		private double experience;
		
		public int getIdentifyId() {
			return identifyId;
		}

		public int getCleanId() {
			return cleanId;
		}

		public int getLevel() {
			return level;
		}

		public double getExperience() {
			return experience;
		}

		private static Map<Integer, CleanData> clean = new HashMap<Integer, CleanData>();

		public static CleanData forId(int id) {
			return clean.get(id);
		}

		static {
			for (CleanData data : CleanData.values()) {
				clean.put(data.identifyId, data);
			}
		}

		private CleanData(int identifyId, int cleanId, int level, double experience) {
			this.identifyId = identifyId;
			this.cleanId = cleanId;
			this.level = level;
			this.experience = experience;
		}
	}

	public static boolean handleCleaning(Client player, int itemId, int itemSlot) {
		CleanData herbloring = CleanData.forId(itemId);
		if (herbloring != null) {
			if (player.playerLevel[SkillIndex.HERBLORE.getSkillId()] < herbloring.getLevel()) {
				player.getDH().sendStatement("You need a herblore level of " + herbloring.getLevel() + " to identifiy this herb.");
				return true;
			}
			player.getPA().addSkillXP((int) herbloring.getExperience() * SkillIndex.HERBLORE.getExpRatio(), SkillIndex.HERBLORE.getSkillId());
			player.getItems().deleteItem(itemId, itemSlot, 1);
			player.getItems().addItem(herbloring.getCleanId(), 1);
			player.sendMessage("You identify the herb, it's a " + player.getItems().getItemName(herbloring.getCleanId()).toLowerCase().replace("clean", "") + "");
			return true;
		}
		return false;
	}
}