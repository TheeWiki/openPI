package server.model.players.skills.herblore;

import server.model.minigames.duel_arena.Rules;
import server.model.players.Player;

/**
 * @author Sanity
 */

public class Potions {

	private Player player;

	public Potions(Player player) {
		this.player = player;
	}

	public void handlePotion(int itemId, int slot) {
		if (player.duelRule[Rules.DRINK_RULE.getRule()]) {
			player.getActionSender().sendMessage("You may not drink potions in this duel.");
			return;
		}
		if (System.currentTimeMillis() - player.potDelay >= 1500) {
			player.potDelay = System.currentTimeMillis();
			player.foodDelay = player.potDelay;
			player.getCombat().resetPlayerAttack();
			player.attackTimer++;
			switch (itemId) {
			case 6685: // brews
				doTheBrew(itemId, 6687, slot);
				break;
			case 6687:
				doTheBrew(itemId, 6689, slot);
				break;
			case 6689:
				doTheBrew(itemId, 6691, slot);
				break;
			case 6691:
				doTheBrew(itemId, 229, slot);
				break;
			case 2436:
				drinkStatPotion(itemId, 145, slot, 0, true); // sup attack
				break;
			case 145:
				drinkStatPotion(itemId, 147, slot, 0, true);
				break;
			case 147:
				drinkStatPotion(itemId, 149, slot, 0, true);
				break;
			case 149:
				drinkStatPotion(itemId, 229, slot, 0, true);
				break;
			case 2440:
				drinkStatPotion(itemId, 157, slot, 2, true); // sup str
				break;
			case 157:
				drinkStatPotion(itemId, 159, slot, 2, true);
				break;
			case 159:
				drinkStatPotion(itemId, 161, slot, 2, true);
				break;
			case 161:
				drinkStatPotion(itemId, 229, slot, 2, true);
				break;
			case 2444:
				drinkStatPotion(itemId, 169, slot, 4, false); // range pot
				break;
			case 169:
				drinkStatPotion(itemId, 171, slot, 4, false);
				break;
			case 171:
				drinkStatPotion(itemId, 173, slot, 4, false);
				break;
			case 173:
				drinkStatPotion(itemId, 229, slot, 4, false);
				break;
			case 2432:
				drinkStatPotion(itemId, 133, slot, 1, false); // def pot
				break;
			case 133:
				drinkStatPotion(itemId, 135, slot, 1, false);
				break;
			case 135:
				drinkStatPotion(itemId, 137, slot, 1, false);
				break;
			case 137:
				drinkStatPotion(itemId, 229, slot, 1, false);
				break;
			case 113:
				drinkStatPotion(itemId, 115, slot, 2, false); // str pot
				break;
			case 115:
				drinkStatPotion(itemId, 117, slot, 2, false);
				break;
			case 117:
				drinkStatPotion(itemId, 119, slot, 2, false);
				break;
			case 119:
				drinkStatPotion(itemId, 229, slot, 2, false);
				break;
			case 2428:
				drinkStatPotion(itemId, 121, slot, 0, false); // attack pot
				break;
			case 121:
				drinkStatPotion(itemId, 123, slot, 0, false);
				break;
			case 123:
				drinkStatPotion(itemId, 125, slot, 0, false);
				break;
			case 125:
				drinkStatPotion(itemId, 229, slot, 0, false);
				break;
			case 2442:
				drinkStatPotion(itemId, 163, slot, 1, true); // super def pot
				break;
			case 163:
				drinkStatPotion(itemId, 165, slot, 1, true);
				break;
			case 165:
				drinkStatPotion(itemId, 167, slot, 1, true);
				break;
			case 167:
				drinkStatPotion(itemId, 229, slot, 1, true);
				break;
			case 3024:
				drinkPrayerPot(itemId, 3026, slot, true); // sup restore
				break;
			case 3026:
				drinkPrayerPot(itemId, 3028, slot, true);
				break;
			case 3028:
				drinkPrayerPot(itemId, 3030, slot, true);
				break;
			case 3030:
				drinkPrayerPot(itemId, 229, slot, true);
				break;
			case 10925:
				drinkPrayerPot(itemId, 10927, slot, true); // sanfew serums
				curePoison(300000);
				break;
			case 10927:
				drinkPrayerPot(itemId, 10929, slot, true);
				curePoison(300000);
				break;
			case 10929:
				drinkPrayerPot(itemId, 10931, slot, true);
				curePoison(300000);
				break;
			case 10931:
				drinkPrayerPot(itemId, 229, slot, true);
				curePoison(300000);
				break;
			case 2434:
				drinkPrayerPot(itemId, 139, slot, false); // pray pot
				break;
			case 139:
				drinkPrayerPot(itemId, 141, slot, false);
				break;
			case 141:
				drinkPrayerPot(itemId, 143, slot, false);
				break;
			case 143:
				drinkPrayerPot(itemId, 229, slot, false);
				break;
			case 2446:
				drinkAntiPoison(itemId, 175, slot, 30000); // anti poisons
				break;
			case 175:
				drinkAntiPoison(itemId, 177, slot, 30000);
				break;
			case 177:
				drinkAntiPoison(itemId, 179, slot, 30000);
				break;
			case 179:
				drinkAntiPoison(itemId, 229, slot, 30000);
				break;
			case 2448:
				drinkAntiPoison(itemId, 181, slot, 300000); // anti poisons
				break;
			case 181:
				drinkAntiPoison(itemId, 183, slot, 300000);
				break;
			case 183:
				drinkAntiPoison(itemId, 185, slot, 300000);
				break;
			case 185:
				drinkAntiPoison(itemId, 229, slot, 300000);
				break;
			}
		}
	}

	public void drinkAntiPoison(int itemId, int replaceItem, int slot, long delay) {
		player.startAnimation(829);
		player.playerItems[slot] = replaceItem + 1;
		player.getItems().resetItems(3214);
		curePoison(delay);
	}

	public void curePoison(long delay) {
		player.poisonDamage = 0;
		player.poisonImmune = delay;
		player.lastPoisonSip = System.currentTimeMillis();
	}

	public void drinkStatPotion(int itemId, int replaceItem, int slot, int stat, boolean sup) {
		player.startAnimation(829);
		player.playerItems[slot] = replaceItem + 1;
		player.getItems().resetItems(3214);
		enchanceStat(stat, sup);
	}

	public void drinkPrayerPot(int itemId, int replaceItem, int slot, boolean rest) {
		player.startAnimation(829);
		player.playerItems[slot] = replaceItem + 1;
		player.getItems().resetItems(3214);
		player.playerLevel[5] += (player.getLevelForXP(player.playerXP[5]) * .33);
		if (rest)
			player.playerLevel[5] += 1;
		if (player.playerLevel[5] > player.getLevelForXP(player.playerXP[5]))
			player.playerLevel[5] = player.getLevelForXP(player.playerXP[5]);
		player.getPA().refreshSkill(5);
		if (rest)
			restoreStats();
	}

	public void restoreStats() {
		for (int j = 0; j <= 6; j++) {
			if (j == 5 || j == 3)
				continue;
			if (player.playerLevel[j] < player.getLevelForXP(player.playerXP[j])) {
				player.playerLevel[j] += (player.getLevelForXP(player.playerXP[j]) * .33);
				if (player.playerLevel[j] > player.getLevelForXP(player.playerXP[j])) {
					player.playerLevel[j] = player.getLevelForXP(player.playerXP[j]);
				}
				player.getPA().refreshSkill(j);
				player.getPA().setSkillLevel(j, player.playerLevel[j], player.playerXP[j]);
			}
		}
	}

	public void doTheBrew(int itemId, int replaceItem, int slot) {
		if (player.duelRule[Rules.EAT_RULE.getRule()]) {
			player.getActionSender().sendMessage("You may not eat in this duel.");
			return;
		}
		player.startAnimation(829);
		player.playerItems[slot] = replaceItem + 1;
		player.getItems().resetItems(3214);
		int[] toDecrease = { 0, 2, 4, 6 };

		@SuppressWarnings("unused")
		int[] toIncrease = { 1, 3 };
		for (int tD : toDecrease) {
			player.playerLevel[tD] -= getBrewStat(tD, .10);
			if (player.playerLevel[tD] < 0)
				player.playerLevel[tD] = 1;
			player.getPA().refreshSkill(tD);
			player.getPA().setSkillLevel(tD, player.playerLevel[tD], player.playerXP[tD]);
		}
		player.playerLevel[1] += getBrewStat(1, .20);
		if (player.playerLevel[1] > (player.getLevelForXP(player.playerXP[1]) * 1.2 + 1)) {
			player.playerLevel[1] = (int) (player.getLevelForXP(player.playerXP[1]) * 1.2);
		}
		player.getPA().refreshSkill(1);

		player.playerLevel[3] += getBrewStat(3, .15);
		if (player.playerLevel[3] > (player.getLevelForXP(player.playerXP[3]) * 1.17 + 1)) {
			player.playerLevel[3] = (int) (player.getLevelForXP(player.playerXP[3]) * 1.17);
		}
		player.getPA().refreshSkill(3);
	}

	public void enchanceStat(int skillID, boolean sup) {
		player.playerLevel[skillID] += getBoostedStat(skillID, sup);
		player.getPA().refreshSkill(skillID);
	}

	public int getBrewStat(int skill, double amount) {
		return (int) (player.getLevelForXP(player.playerXP[skill]) * amount);
	}

	public int getBoostedStat(int skill, boolean sup) {
		int increaseBy = 0;
		if (sup)
			increaseBy = (int) (player.getLevelForXP(player.playerXP[skill]) * .20);
		else
			increaseBy = (int) (player.getLevelForXP(player.playerXP[skill]) * .13) + 1;
		if (player.playerLevel[skill] + increaseBy > player.getLevelForXP(player.playerXP[skill]) + increaseBy + 1) {
			return player.getLevelForXP(player.playerXP[skill]) + increaseBy - player.playerLevel[skill];
		}
		return increaseBy;
	}

	public boolean isPotion(int itemId) {
		String name = player.getItems().getItemName(itemId);
		return name.contains("(4)") || name.contains("(3)") || name.contains("(2)") || name.contains("(1)");
	}
}