package server.model.content;

import server.model.players.Client;
import server.util.Misc;

/**
 * Every day the user will be allowed to search a chest located at home for a
 * random reward. Additionally if lucky the user will receive an additional
 * reward from looting the chest.
 * 
 * @author Dennis
 *
 */
public class DailyChest {

	/**
	 * Regular items that the player will recieve upon searching the chest
	 */
	private static final int[][] REGULAR = {
			{995, 50}
	};
	/**
	 * Low value items that are additionally loot
	 */
	private static final int[][] LOW = {

	};
	/**
	 * Medium value items that are additionally loot
	 */
	private static final int[][] MEDIUM = {

	};
	/**
	 * High value items that are additionally loot
	 */
	private static final int[][] HIGH = {

	};

	/**
	 * The {@link #DAY} integer contains the number of milliseconds in a single
	 * day (24 Hours).
	 */
	private static final int DAY = 86_400_000;

	/**
	 * Executes the object interaction, if the player didn't wait a day then the
	 * player will be returned a message.
	 * 
	 * TODO: tweak chance rates
	 * 
	 * @param c
	 */
	public static void checkChest(Client c) {
		int chance = Misc.random(2);
		if (System.currentTimeMillis() - c.foodDelay < DAY) {
			c.sendMessage("Come back tomorrow to loot again.");
			return;
		}
		c.startAnimation(2247);
		for (int regular = 0; regular < REGULAR.length; regular++) {
			c.getItems().addItem(REGULAR[regular][0], REGULAR[regular][1]);
			c.sendMessage("You search the chest and find.. " + c.getItems().getItemName(REGULAR[regular][0]));
		}
		switch (chance) {
		case 1:
			for (int chance_low = 0; chance_low < LOW.length; chance_low++) {
				c.getItems().addItem(LOW[chance_low][0], LOW[chance_low][1]);
				c.sendMessage("You search the chest and find.. " + c.getItems().getItemName(LOW[chance_low][0]));
			}
			break;
		case 2:
			for (int chance_medium = 0; chance_medium < MEDIUM.length; chance_medium++) {
				c.getItems().addItem(MEDIUM[chance_medium][0], MEDIUM[chance_medium][1]);
				c.sendMessage("You search the chest and find.. " + c.getItems().getItemName(MEDIUM[chance_medium][0]));
			}
			break;
		case 3:
			for (int chance_high = 0; chance_high < HIGH.length; chance_high++) {
				c.getItems().addItem(HIGH[chance_high][0], HIGH[chance_high][1]);
				c.sendMessage("You search the chest and find.. " + c.getItems().getItemName(HIGH[chance_high][0]));
			}
			break;
		}
	}
}