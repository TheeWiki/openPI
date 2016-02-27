package server.model.players;

import server.model.minigames.barrows.Dungeon;
import server.model.minigames.castle_wars.CastleWarObjects;
import server.util.Plugin;
import server.world.locations.AbstractLocations;

/**
 * ActionHandler controls all entity actions currently.
 * 
 * Plan to port all of the actions done in this class to the region classes at
 * {@link AbstractLocations}, and global actions (objects that may exist in
 * numerous regions) are done in Python utilizing the {@link Plugin}.
 * 
 * @author Dennis
 *
 */
public class ActionHandler {

	public void firstClickObject(Player player, int objectType, int obX, int obY) {
		player.clickObjectType = 1;
		player.getActionSender().sendMessage("[object 1] - " + objectType + " [posX] - " + obX + " [posY] - " + obY);
		CastleWarObjects.handleObject(player, objectType, obX, obY);
		Plugin.execute("locControl_o_1", player, objectType, obX, obY);
		switch (objectType) {
		case 6746:
		case 6727:
		case 6739:
		case 6720:
		case 6724:
		case 6743:
		case 6744:
		case 6725:
			Dungeon.puzzleDoors(player, objectType, obX, obY);
			break;
		case 6747:
		case 6728:
		case 6741:
		case 6722:
		case 6740:
		case 6721:
		case 6738:
		case 6719:
		case 6737:
		case 6718:
		case 6745:
		case 6726:
		case 6748:
		case 6729:
		case 6749:
		case 6730:
			Dungeon.openDungeonDoor(player, objectType, obX, obY);
			break;
		default:
			Plugin.execute("objectClick1_" + objectType, player, objectType, obX, obY);
			break;
		}
	}

	public void secondClickObject(Player player, int objectType, int obX, int obY) {
		player.clickObjectType = 2;
		player.getActionSender().sendMessage("[object 2] - " + objectType + " [posX] - " + obX + " [posY] - " + obY);
		Plugin.execute("locControl_o_2", player, objectType, obX, obY);
		switch (objectType) {
		
		default:
			Plugin.execute("objectClick2_" + objectType, player, objectType, obX, obY);
			break;
		}
	}

	public void thirdClickObject(Player player, int objectType, int obX, int obY) {
		player.clickObjectType = 3;
		player.getActionSender().sendMessage("[object 3] - " + objectType + " [posX] - " + obX + " [posY] - " + obY);
		Plugin.execute("locControl_o_3", player, objectType, obX, obY);
		switch (objectType) {

		default:
			Plugin.execute("objectClick3_" + objectType, player, objectType, obX, obY);
			break;
		}
	}

	public void firstClickNpc(Player player, int npcType) {
		player.clickNpcType = 1;
		player.npcClickIndex = 1;
		player.getActionSender().sendMessage("[npc 1] - " + npcType);
		Plugin.execute("locControl_n_1", player, npcType);
		switch (npcType) {
		
		default:
			Plugin.execute("npcClick1_" + npcType, player, npcType);
			break;
		}
	}

	public void secondClickNpc(Player player, int npcType) {
		player.clickNpcType = 2;
		player.npcClickIndex = 2;
		player.getActionSender().sendMessage("[npc 2] - " + npcType);
		Plugin.execute("locControl_n_2", player, npcType);
		switch (npcType) {
		default:
			Plugin.execute("npcClick2_" + npcType, player, npcType);
			break;
		}
	}

	public void thirdClickNpc(Player player, int npcType) {
		player.clickNpcType = 3;
		player.npcClickIndex = 3;
		player.getActionSender().sendMessage("[npc 3] - " + npcType);
		Plugin.execute("locControl_n_3", player, npcType);
		switch (npcType) {
		
		default:
			Plugin.execute("npcClick3_" + npcType, player, npcType);
			break;
		}
	}
}