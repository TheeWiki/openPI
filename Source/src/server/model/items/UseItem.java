package server.model.items;

import server.model.players.Player;
import server.util.Misc;

/**
 * @author Sanity
 * @author Ryan
 * @author Lmctruck30 Revised by Shawn Notes by Shawn
 */

public class UseItem {

	/**
	 * Using items on an object.
	 * 
	 * @param c
	 * @param objectID
	 * @param objectX
	 * @param objectY
	 * @param itemId
	 */
	public static void ItemonObject(Player c, int objectID, int objectX, int objectY, int itemId) {
		if (!c.getItems().playerHasItem(itemId, 1))
			return;
		switch (objectID) {

		default:
			if (c.playerRights == 3)
				Misc.println("Player At Object id: " + objectID + " with Item id: " + itemId);
			break;
		}

	}

	/**
	 * Using items on items.
	 * 
	 * @param c
	 * @param itemUsed
	 * @param useWith
	 */
	public static void ItemonItem(Player c, int itemUsed, int useWith) {
//		if (itemUsed == 2368 && useWith == 2366 || itemUsed == 2366 && useWith == 2368) {
//			c.getItems().deleteItem(2368, c.getItems().getItemSlot(2368), 1);
//			c.getItems().deleteItem(2366, c.getItems().getItemSlot(2366), 1);
//			c.getItems().addItem(1187, 1);
//		}
		if(Poisonable.useItemonItem(c, useWith, itemUsed))
            return;
		switch (itemUsed) {

		default:
			if (c.playerRights == 3)
				Misc.println("Player used Item id: " + itemUsed + " with Item id: " + useWith);
			break;
		}
	}

	/**
	 * Using items on NPCs.
	 * 
	 * @param c
	 * @param itemId
	 * @param npcId
	 * @param slot
	 */
	public static void ItemonNpc(Player c, int itemId, int npcId, int slot) {
		switch (itemId) {

		default:
			if (c.playerRights == 3)
				Misc.println("Player used Item id: " + itemId + " with Npc id: " + npcId + " With Slot : " + slot);
			break;
		}

	}

}
