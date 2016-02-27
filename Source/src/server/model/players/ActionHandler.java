package server.model.players;

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

	public void firstClickObject(Player c, int objectType, int obX, int obY) {
		c.clickObjectType = 1;
		c.sendMessage("[object 1] - " + objectType + " [posX] - " + obX + " [posY] - " + obY);
		CastleWarObjects.handleObject(c, objectType, obX, obY);
		Plugin.execute("locControl_o_1", c, objectType, obX, obY);
		switch (objectType) {
		
		default:
			Plugin.execute("objectClick1_" + objectType, c, objectType, obX, obY);
			break;
		}
	}

	public void secondClickObject(Player c, int objectType, int obX, int obY) {
		c.clickObjectType = 2;
		c.sendMessage("[object 2] - " + objectType + " [posX] - " + obX + " [posY] - " + obY);
		Plugin.execute("locControl_o_2", c, objectType, obX, obY);
		switch (objectType) {
		
		default:
			Plugin.execute("objectClick2_" + objectType, c, objectType, obX, obY);
			break;
		}
	}

	public void thirdClickObject(Player c, int objectType, int obX, int obY) {
		c.clickObjectType = 3;
		c.sendMessage("[object 3] - " + objectType + " [posX] - " + obX + " [posY] - " + obY);
		Plugin.execute("locControl_o_3", c, objectType, obX, obY);
		switch (objectType) {

		default:
			Plugin.execute("objectClick3_" + objectType, c, objectType, obX, obY);
			break;
		}
	}

	public void firstClickNpc(Player c, int npcType) {
		c.clickNpcType = 1;
		c.npcClickIndex = 1;
		c.sendMessage("[npc 1] - " + npcType);
		Plugin.execute("locControl_n_1", c, npcType);
		switch (npcType) {
		
		default:
			Plugin.execute("npcClick1_" + npcType, c, npcType);
			break;
		}
	}

	public void secondClickNpc(Player c, int npcType) {
		c.clickNpcType = 2;
		c.npcClickIndex = 2;
		c.sendMessage("[npc 2] - " + npcType);
		Plugin.execute("locControl_n_2", c, npcType);
		switch (npcType) {
		default:
			Plugin.execute("npcClick2_" + npcType, c, npcType);
			break;
		}
	}

	public void thirdClickNpc(Player c, int npcType) {
		c.clickNpcType = 3;
		c.npcClickIndex = 3;
		c.sendMessage("[npc 3] - " + npcType);
		Plugin.execute("locControl_n_3", c, npcType);
		switch (npcType) {
		
		default:
			Plugin.execute("npcClick3_" + npcType, c, npcType);
			break;
		}
	}
}