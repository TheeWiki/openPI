package server.model.minigames.pest_control;

import server.model.players.Player;
import server.model.players.skills.SkillIndex;

public class PestControlRewards {
	
	/**
	 * Can players use points exchange yes ? no
	 */
	public final static boolean CAN_EXCHANGE_POINTS = true;

	/**
	 * Rewards interface int id
	 */
	public final static int REWARDS_INTERFACE = 18691;

	/**
	 * Void Knights that can exchange points
	 */
	public final static int[] VOID_KNIGHTS = {3788, 3789};

	/**
	 * Reward Types
	 */
	public final static int
	NONE = 0,
	ATTACK = 1,
	STRENGTH = 2,
	DEFENCE = 3,
	RANGED = 4,
	MAGIC = 5,
	HITPOINTS = 6,
	PRAYER = 7;

	/**
	 * Reward Type Selected
	 */
	public static int rewardSelected = 0;

	/**
	 * Gets String Reward Chosen
	 * @return
	 */
	public static String checkReward() {
		if(rewardSelected == NONE) {
			return "None";
		} else if(rewardSelected == NONE) {
			return "None";
		} else if(rewardSelected == ATTACK) {
			return "Attack";
		} else if(rewardSelected == STRENGTH) {
			return "Strength";
		} else if(rewardSelected == DEFENCE) {
			return "Defence";
		} else if(rewardSelected == RANGED) {
			return "Ranged";
		} else if(rewardSelected == MAGIC) {
			return "Magic";
		} else if(rewardSelected == HITPOINTS) {
			return "Hitpoints";
		} else if(rewardSelected == PRAYER) {
			return "Prayer";
		}
		return "";
	}

	/**
	 * Opens Point Exchange
	 * @param player
	 * @param button
	 */
	public static void exchangePestPoints(Player player) {
		if (!CAN_EXCHANGE_POINTS) {
			player.getActionSender().sendMessage("Pest Control point exchange is currently disabled.");
			return;
		}
		player.getPA().sendFrame126("Void Knights' Training Options", 18758);
		player.getPA().sendFrame126("ATTACK", 18767);
		player.getPA().sendFrame126("STRENGTH", 18768);
		player.getPA().sendFrame126("DEFENCE", 18769);
		player.getPA().sendFrame126("RANGED", 18770);
		player.getPA().sendFrame126("MAGIC", 18771);
		player.getPA().sendFrame126("HITPOINTS", 18772);
		player.getPA().sendFrame126("PRAYER", 18773);
		player.getPA().sendFrame126(checkReward(), 18782);
		player.getPA().sendFrame126("Points: "+player.pcPoints, 18783);
		player.getActionSender().sendMessage("You currently have "+player.pcPoints+" pest control points.");
		player.getPA().showInterface(REWARDS_INTERFACE);
	}

	public static void handlePestButtons(Player player, int button) {
		switch(button) {

		/**
		 * Attack
		 */
		case 73072:
		case 73079:
			rewardSelected = ATTACK;
			player.getPA().sendFrame126(checkReward(), 18782);
			break;

			/**
			 * Strength
			 */
		case 73073:
		case 73080:
			rewardSelected = STRENGTH;
			player.getPA().sendFrame126(checkReward(), 18782);
			break;

			/**
			 * Defence	
			 */
		case 73074:
		case 73081:
			rewardSelected = DEFENCE;
			player.getPA().sendFrame126(checkReward(), 18782);
			break;

			/**
			 * Ranged	
			 */
		case 73075:
		case 73082:
			rewardSelected = RANGED;
			player.getPA().sendFrame126(checkReward(), 18782);
			break;

			/**
			 * Magic		
			 */
		case 73076:
		case 73083:
			rewardSelected = MAGIC;
			player.getPA().sendFrame126(checkReward(), 18782);
			break;

			/**
			 * Hitpoints	
			 */
		case 73077:
		case 73084:
			rewardSelected = HITPOINTS;
			player.getPA().sendFrame126(checkReward(), 18782);
			break;

			/**
			 * Prayer	
			 */
		case 73078:
		case 73085:
			rewardSelected = PRAYER;
			player.getPA().sendFrame126(checkReward(), 18782);
			break;

			/**
			 * Confirm	
			 */
		case 73091:
			switch(rewardSelected) {
			case NONE:
				player.getActionSender().sendMessage("You don't have a reward selected.");
				break;
				
			case ATTACK:
				if(player.pcPoints > 1) {
					player.getPA().addSkillXP(player.playerLevel[SkillIndex.ATTACK.getSkillId()] * player.playerLevel[SkillIndex.ATTACK.getSkillId()]/17.5 * 4, SkillIndex.ATTACK.getSkillId());
					player.getActionSender().sendMessage("You have been rewarded attack experience.");
					player.pcPoints -= 2;
				} else {
					player.getActionSender().sendMessage("You need at least 2 pest control points to exchange your points.");
				}
				break;
			case STRENGTH:
				if(player.pcPoints > 1) {
					player.getPA().addSkillXP(player.playerLevel[SkillIndex.STRENGTH.getSkillId()] * player.playerLevel[SkillIndex.STRENGTH.getSkillId()]/17.5 * 4, SkillIndex.STRENGTH.getSkillId());
					player.getActionSender().sendMessage("You have been rewarded strength experience.");
					player.pcPoints -= 2;
				} else {
					player.getActionSender().sendMessage("You need at least 2 pest control points to exchange your points.");
				}
				break;
			case DEFENCE:
				if(player.pcPoints > 1) {
					player.getPA().addSkillXP(player.playerLevel[SkillIndex.DEFENCE.getSkillId()] * player.playerLevel[SkillIndex.DEFENCE.getSkillId()]/17.5 * 4, SkillIndex.DEFENCE.getSkillId());
					player.getActionSender().sendMessage("You have been rewarded defence experience.");
					player.pcPoints -= 2;
				} else {
					player.getActionSender().sendMessage("You need at least 2 pest control points to exchange your points.");
				}
				break;
			case RANGED:
				if(player.pcPoints > 1) {
					player.getPA().addSkillXP(player.playerLevel[SkillIndex.RANGE.getSkillId()] * player.playerLevel[SkillIndex.RANGE.getSkillId()]/17.5 * 4, SkillIndex.RANGE.getSkillId());
					player.getActionSender().sendMessage("You have been rewarded ranged experience.");
					player.pcPoints -= 2;
				} else {
					player.getActionSender().sendMessage("You need at least 2 pest control points to exchange your points.");
				}
				break;
			case MAGIC:
				if(player.pcPoints > 1) {
					player.getPA().addSkillXP(player.playerLevel[SkillIndex.MAGIC.getSkillId()] * player.playerLevel[SkillIndex.MAGIC.getSkillId()]/17.5 * 4, SkillIndex.MAGIC.getSkillId());
					player.getActionSender().sendMessage("You have been rewarded magic experience.");
					player.pcPoints -= 2;
				} else {
					player.getActionSender().sendMessage("You need at least 2 pest control points to exchange your points.");
				}
				break;
			case HITPOINTS:
				if(player.pcPoints > 1) {
					player.getPA().addSkillXP(player.playerLevel[SkillIndex.HITPOINTS.getSkillId()] * player.playerLevel[SkillIndex.HITPOINTS.getSkillId()]/17.5 * 4, SkillIndex.HITPOINTS.getSkillId());
					player.getActionSender().sendMessage("You have been rewarded hitpoints experience.");
					player.pcPoints -= 2;
				} else {
					player.getActionSender().sendMessage("You need at least 2 pest control points to exchange your points.");
				}
				break;
			case PRAYER:
				if(player.pcPoints > 1) {
					player.getPA().addSkillXP(player.playerLevel[SkillIndex.PRAYER.getSkillId()] * player.playerLevel[SkillIndex.PRAYER.getSkillId()]/8.75 * 4, SkillIndex.PRAYER.getSkillId());
					player.getActionSender().sendMessage("You have been rewarded prayer experience.");
					player.pcPoints -= 2;
				} else {
					player.getActionSender().sendMessage("You need at least 2 pest control points to exchange your points.");
				}
				break;
			}
			
			player.getPA().sendFrame126("Points: "+player.pcPoints, 18783);
			break;
		}
	}

}
