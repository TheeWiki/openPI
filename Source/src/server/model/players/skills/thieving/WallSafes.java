package server.model.players.skills.thieving;

import server.model.players.Player;
import server.model.players.PlayerHandler;
import server.model.players.skills.SkillIndex;
import server.util.Misc;

/**
 * Wall safes are found in the Rogue's Den, where four of them can be cracked 
 * providing you have a Thieving level of 20. Successfully cracking a safe yields 
 * 1000 thieving experience.
 * Beware - if you fail to crack a safe, a floor trap will spring and deal 
 * 20-70 life points damage on you.
 * 
 * @author A7mad (http://www.rune-server.org/members/a7mad/) & Koshmar
 */
public class WallSafes {
	
	/**
	 * Quantity 
	 */
	private static final int QUANTITY = 1;

	/**
	 * Thieving XP (Since 25 x 40 = 1000 xp per 1 wall safe).
	 */
	private static final int XP = 25;

	/**
	 * Level Required
	 */
	private static final int LEVEL_REQUIRED = 20;

	/**
	 * Animation ID (Animation)
	 */
	private static final int UNLOCKING_ANIM = 2247;

	/**
	 * Money ID
	 */
	private static final int COINS = 995;

	/**
	 * Gems ID
	 */
	public static int[] randomGems = {995, 1617, 1619, 1621, 1623, 1625, 1627, 1629, 1631};
	
	/**
	 * You crack the safe.
	 */
	private static final String SUCCESSFUL = "You crack the safe.";

	/**
	 * You fail to crack the safe.
	 */
	private static final String UNSUCCESSFUL = "@red@You fail to crack the safe.";

	/**
	 * You need a thieving level of at least "+LEVEL_REQUIRED+" to crack the wall safe.
	 */
	private static final String AT_LEAST = "You need a thieving level of at least "+LEVEL_REQUIRED+" to crack the wall safe.";

	/**
	 * Generates random rewards via Misc.random
	 * 
	 * @param player
	 *            The player or Player
	 */
	public static void getRandomReward(Player player) {
		int random = Misc.random(19);
		if (random < 12) {
			player.startAnimation(UNLOCKING_ANIM);
			player.getPA().addSkillXP(XP * SkillIndex.THIEVING.getExpRatio(), SkillIndex.THIEVING.getSkillId());
			if (random < 4) {
				int baseAmt = ((random % 10) > 1) ? 2500 : 1000;
				int addAmt = ((random % 10) > 1) ? ((random % 10) * 2000) : (random * 1500);
				player.getItems().addItem(COINS, baseAmt + addAmt + Misc.random(2500));
			} else
				player.getItems().addItem(1609 + (random * 2), QUANTITY);
		} else
			appendHit(Misc.random(random - 11), player);
		player.getActionSender().sendMessage(random < 12 ? SUCCESSFUL : UNSUCCESSFUL);
	}

	/**
	 * Cause a hit
	 * 
	 * @param damage
	 *            The damage
	 * @param player
	 *            The player or Player
	 */
	public static void appendHit(int damage, Player player) {
		PlayerHandler.players[player.playerId].setHitDiff(damage);
		PlayerHandler.players[player.playerId].playerLevel[3] -= damage;
		player.getPA().refreshSkill(3);
		PlayerHandler.players[player.playerId].setHitUpdateRequired(true);
		PlayerHandler.players[player.playerId].updateRequired = true;
	}

	/**
	 * Checks the wall safe
	 * 
	 * @param player
	 *            The player or Player
	 * @param objectType
	 *            Object ID
	 */
	public static void checkWallSafe(Player player) {
		if (player.playerLevel[SkillIndex.THIEVING.getSkillId()] >= LEVEL_REQUIRED) {
			if (System.currentTimeMillis() - player.lastThieve < 2500)
				return;
			player.lastThieve = System.currentTimeMillis();
			player.turnPlayerTo(player.objectX, player.objectY);
			getRandomReward(player);
		} else {
			player.getActionSender().sendMessage(AT_LEAST);
		}
	}

}