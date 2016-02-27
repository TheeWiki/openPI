package server.model.minigames.barrows;

import server.model.players.Player;
import server.util.Misc;


/**
 * The Barrows Minigame
 * @author Sanity
 * Revised by Shawn
 * Notes by Shawn
 */
public class Barrows {

	public static final int[][] COFFIN_AND_BROTHERS = {
			{6823, 2030},
			{6772, 2029},
			{6822, 2028},
			{6773, 2027},
			{6771, 2026},
			{6821, 2025}
	
	};

	/**
	 * Picking the random coffin.
	 **/
	public static int getRandomCoffin() {
		return Misc.random(COFFIN_AND_BROTHERS.length - 1);
	}

	public static boolean wrongPuzzle = false;

	/**
	 * Selects the coffin and shows the interface if coffin ID matches random
	 * coffin.
	 **/
	public static boolean selectCoffin(Player player, int coffinId) {
		if (player.randomCoffin == 0) {
			player.randomCoffin = getRandomCoffin();
		}

		if (COFFIN_AND_BROTHERS[player.randomCoffin][0] == coffinId) {
			player.getDH().sendDialogues(1, -1);
			return true;
		}
		return false;
	}

}