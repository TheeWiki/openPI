package server.model.npcs.instance;

import server.Server;
import server.model.players.ActionHandler;
import server.model.players.Player;
import server.util.Misc;

/**
 * The {@link #InstanceController()} class is used to manage the
 * {@link BossInstance} and {@link ZoneInstance} enumerations.
 * 
 * With this system, the user has the option to purchase an instanced zone to
 * finally 1v1 a boss alone. With the Instance zones comes different values,
 * based on the boss statistics. User then has the option after killing the boss
 * to teleport away whenever they decide (no respawning).
 * 
 * @author Dennis
 *
 */
public class InstanceController {

	/**
	 * Executes the instance after verifying it from
	 * {@link #isInstanceObject(int)} boolean and then checks to see if the
	 * player has the amount of GP (995) required to purchase the instance, if
	 * the player doesn't then the player is returned with a warning message.
	 * 
	 * Using the {@link ZoneInstance} values the player is then moved to the
	 * instanced area.
	 * 
	 * After the player is transfered to the instanced area, now using
	 * {@link BossInstance} the values are now used in spawning the boss for the
	 * specified instance zone
	 * 
	 * @param player
	 * @param object
	 */
	public static void executeInstance(Player player, int object) {
		if (!player.membership)
		{
			player.getDH().sendStatement("You need to be a member to purchase instances.");
			return;
		}
		for (ZoneInstance zi : ZoneInstance.values()) {
			if (!player.getItems().playerHasItem(zi.getCurrency(), zi.getCost())) {
				player.getActionSender().sendMessage("You don't have " + Misc.format(zi.getCost()) + " " +player.getItems().getItemName(zi.getCurrency()) +" to purchase this instance.");
				return;
			}
			player.getPA().movePlayer(zi.getLocX(), zi.getLocY(), player.playerId * 4);
			player.getItems().deleteItem2(995, zi.getCost());
			player.getActionSender().sendMessage("You have purchased this private instance zone for 1 kill only, good luck!");
		}
		for (BossInstance bi : BossInstance.values()) {
			Server.npcHandler.spawnNpc(player, bi.getNpcID(), player.absX + 4, player.absY, player.playerId * 4, 1, bi.getHP(), bi.maxHit(),
					bi.getAtt(), bi.getDef(), bi.isAgressive(), bi.hasHeadIcon());
		}
	}

	/**
	 * Checks the values of the objects in {@link ZoneInstance} and checks if it
	 * matches the paramter of the boolean, if so it then returns true. The
	 * boolean is then called in {@link ActionHandler} first click object, to
	 * verify if the object is equal to the values in {@link ZoneInstance} else
	 * its just a regular object that can't be instanced.
	 * 
	 * @param object
	 * @return object
	 */
	public static boolean isInstanceObject(int object) {
		for (ZoneInstance zi : ZoneInstance.values()) {
			if (object == zi.getObject()) {
				return true;
			}
		}
		return false;
	}
}