package server.event.impl;

import server.Server;
import server.model.players.Client;
import server.util.Misc;

public class RiverTroll {
	
	public static int[][] riverTroll = {
		{3, 	10, 	391, 	19, 	1},
		{11, 	20, 	392, 	40, 	1},
		{21, 	40, 	393, 	80, 	3},
		{61, 	90, 	394, 	105, 	4},
		{91, 	110, 	395, 	120, 	5},
		{111, 	138, 	396, 	150, 	7},
	};

	public static void spawnRiverTroll(Client c) {
		if(c.combatLevel <= 4)
			return;
		for (int[] aRiverTroll : riverTroll) {
			if(!c.trollSpawned) {
				if (c.combatLevel >= aRiverTroll[0] && c.combatLevel <= aRiverTroll[1]) {
					Server.npcHandler.spawnNpc(c, aRiverTroll[2], c.getX() + Misc.random(1), c.getY() + Misc.random(1), c.heightLevel, 0, aRiverTroll[3], aRiverTroll[4], aRiverTroll[4] * 10, aRiverTroll[4] * 10, true, false);
					c.trollSpawned = true;
				}
			}
		}
	}

}
