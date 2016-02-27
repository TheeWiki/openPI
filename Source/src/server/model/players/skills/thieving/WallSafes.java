package server.model.players.skills.thieving;

import server.model.players.Client;
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
	 * @param c
	 *            The player or client
	 */
	public static void getRandomReward(Client c) {
		int random = Misc.random(19);
		if (random < 12) {
			c.startAnimation(UNLOCKING_ANIM);
			c.getPA().addSkillXP(XP * SkillIndex.THIEVING.getExpRatio(), SkillIndex.THIEVING.getSkillId());
			if (random < 4) {
				int baseAmt = ((random % 10) > 1) ? 2500 : 1000;
				int addAmt = ((random % 10) > 1) ? ((random % 10) * 2000) : (random * 1500);
				c.getItems().addItem(COINS, baseAmt + addAmt + Misc.random(2500));
			} else
				c.getItems().addItem(1609 + (random * 2), QUANTITY);
		} else
			appendHit(Misc.random(random - 11), c);
		c.sendMessage(random < 12 ? SUCCESSFUL : UNSUCCESSFUL);
	}

	/**
	 * Cause a hit
	 * 
	 * @param damage
	 *            The damage
	 * @param c
	 *            The player or client
	 */
	public static void appendHit(int damage, Client c) {
		PlayerHandler.players[c.playerId].setHitDiff(damage);
		PlayerHandler.players[c.playerId].playerLevel[3] -= damage;
		c.getPA().refreshSkill(3);
		PlayerHandler.players[c.playerId].setHitUpdateRequired(true);
		PlayerHandler.players[c.playerId].updateRequired = true;
	}

	/**
	 * Checks the wall safe
	 * 
	 * @param c
	 *            The player or client
	 * @param objectType
	 *            Object ID
	 */
	public static void checkWallSafe(Client c) {
		if (c.playerLevel[SkillIndex.THIEVING.getSkillId()] >= LEVEL_REQUIRED) {
			if (System.currentTimeMillis() - c.lastThieve < 2500)
				return;
			c.lastThieve = System.currentTimeMillis();
			c.turnPlayerTo(c.objectX, c.objectY);
			getRandomReward(c);
		} else {
			c.sendMessage(AT_LEAST);
		}
	}

}