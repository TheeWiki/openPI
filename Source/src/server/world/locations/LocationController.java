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

	public static void sendFirstClickObject(Player c, int object) {
		barrows.sendFirstClickObject(c, object);
		home.sendFirstClickObject(c, object);
		pestControl.sendFirstClickObject(c, object);
		tzhaar.sendFirstClickObject(c, object);
		wilderness.sendFirstClickObject(c, object);
		taverlyDungeon.sendFirstClickObject(c, object);
		duelArena.sendFirstClickObject(c, object);
		mageBank.sendFirstClickObject(c, object);
		slayerTower.sendFirstClickObject(c, object);
	}

	public static void sendSecondClickObject(Player c, int object) {
		tzhaar.sendSecondClickObject(c, object);
	}

	public static void sendThirdClickObject(Player c, int object) {

	}

	public static void sendFirstClickNpc(Player c, int npc) {
		duelArena.sendFirstClickNpc(c, npc);
		home.sendFirstClickNpc(c, npc);
	}

	public static void sendSecondClickNpc(Player c, int npc) {
		pestControl.sendSecondClickNpc(c, npc);
		duelArena.sendSecondClickNpc(c, npc);
	}

	public static void sendThirdClickNpc(Player c, int npc) {

	}
}