package server.event.impl;

import server.Server;
import server.model.players.Client;
import server.util.Misc;

public class SpiritTree  {

	public static int[][] spiritTree = {
		{3, 	10, 	438, 	19, 	1},
		{11, 	20, 	439, 	40, 	1},
		{21, 	40, 	440, 	80, 	3},
		{61, 	90, 	441, 	105, 	4},
		{91, 	110, 	442, 	120, 	5},
		{111, 	138, 	443, 	150, 	7},
	};

	public static void spawnSpiritTree(Client c) {
		if(c.combatLevel <= 4)
			return;
		for (int[] aSpiritTree : spiritTree) {
			if(!c.treeSpawned) {
				if (c.combatLevel >= aSpiritTree[0] && c.combatLevel <= aSpiritTree[1]) {
					Server.npcHandler.spawnNpc(c, aSpiritTree[2], c.getX() + Misc.random(1), c.getY() + Misc.random(1), c.heightLevel, 0, aSpiritTree[3], aSpiritTree[4], aSpiritTree[4] * 10, aSpiritTree[4] * 10, true, false);
					c.treeSpawned = true;
				}
			}
		}
	}
}