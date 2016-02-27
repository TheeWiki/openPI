package server.model.players;

import server.world.sound.MusicTab;

/**
 * The {@link #Starter()} class sets the starter items upon new user login, as
 * well as a change appearance interface.
 * 
 * @author Dennis
 *
 */
public class Starter {

	/**
	 * The 2D array allows the class to store all of the {@value #ITEMS} inside
	 * a single object known as {@link #ITEMS}.
	 */
	private final static int[][] ITEMS = {
			// misc
			{ 995, 100_000 },

			// mage
			{ 554, 500 }, { 555, 500 }, { 556, 500 }, { 557, 500 }, { 558, 500 }, { 559, 500 }, { 640, 1 }, { 650, 1 },
			{ 1381, 1 }, { 1704, 1 },

			// range
			{ 1095, 1 }, { 1129, 1 }, { 841, 1 }, { 882, 500 },

			// melee
			{ 1059, 1 }, { 1075, 1 }, { 1103, 1 }, { 1155, 1 }, { 1173, 1 }, { 1321, 1 },

	};
	
	/**
	 * If the player is new to the server then the player will receive items
	 * from the {@link #ITEMS} array in their inventory and the interface to
	 * change player appearance to their specifics is also shown at the same
	 * time.
	 * 
	 * @param player
	 */
	public static void newPlayer(Player player) {
		player.getPA().showInterface(3559);
		player.canChangeAppearance = true;
		for (int item = 0; item < ITEMS.length; item++) {
			player.getItems().addItem(ITEMS[item][0], ITEMS[item][1]);
		}

		MusicTab.initializeMusicBooleanFirstTime(player);
	}
}