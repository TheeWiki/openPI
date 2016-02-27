package server.world.locations;

import server.model.players.Player;
/**
 * Handles the region actions based on the location class.
 * This system allows the developer to find and edit faster based on the regional class name.
 * ({@link #wilderness} contains all wilderness actions).
 * @author Dennis
 *
 */
import server.world.locations.impl.Barrows;
import server.world.locations.impl.DuelArena;
import server.world.locations.impl.Home;
import server.world.locations.impl.MageBank;
import server.world.locations.impl.PestControl;
import server.world.locations.impl.SlayerTower;
import server.world.locations.impl.TaverlyDungeon;
import server.world.locations.impl.Tzhaar;
import server.world.locations.impl.Wilderness;

/**
 * {@link #LocationController()} returns all incoming responses from the
 * instanced classes and then sets them in their respective static voids for the
 * Python scripts to read when the player interacts within that regional class.
 * 
 * @author Dennis
 *
 */
public final class LocationController {

	/**
	 * Creates instances of the regional classes constructed by the
	 * {@link AbstractLocations} class.
	 */
	private static final AbstractLocations
			barrows = new Barrows(), duelArena = new DuelArena(), home = new Home(),
			tzhaar = new Tzhaar(), taverlyDungeon = new TaverlyDungeon(), wilderness = new Wilderness(),
			pestControl = new PestControl(), mageBank = new MageBank(), slayerTower = new SlayerTower();

	public static void sendFirstClickObject(Player player, int object) {
		barrows.sendFirstClickObject(player, object);
		home.sendFirstClickObject(player, object);
		pestControl.sendFirstClickObject(player, object);
		tzhaar.sendFirstClickObject(player, object);
		wilderness.sendFirstClickObject(player, object);
		taverlyDungeon.sendFirstClickObject(player, object);
		duelArena.sendFirstClickObject(player, object);
		mageBank.sendFirstClickObject(player, object);
		slayerTower.sendFirstClickObject(player, object);
	}

	public static void sendSecondClickObject(Player player, int object) {
		tzhaar.sendSecondClickObject(player, object);
	}

	public static void sendThirdClickObject(Player player, int object) {

	}

	public static void sendFirstClickNpc(Player player, int npc) {
		duelArena.sendFirstClickNpc(player, npc);
		home.sendFirstClickNpc(player, npc);
	}

	public static void sendSecondClickNpc(Player player, int npc) {
		pestControl.sendSecondClickNpc(player, npc);
		duelArena.sendSecondClickNpc(player, npc);
	}

	public static void sendThirdClickNpc(Player player, int npc) {

	}
}