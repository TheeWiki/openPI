package server.event.impl;

import server.Server;
import server.model.players.Player;
import server.util.Misc;

public class Zombie {
	
	public static int[][] zombie = {
		{3, 	10, 	419, 	19, 	1},
		{11, 	20, 	420, 	40, 	1},
		{21, 	40, 	421, 	80, 	3},
		{61, 	90, 	422, 	105, 	4},
		{91, 	110, 	423, 	120, 	5},
		{111, 	138, 	424, 	150, 	7},
	};

	public static void spawnZombie(Player c) {
		if(c.combatLevel <= 4)
			return;
		for (int[] aZombie : zombie) {
			if(!c.zombieSpawned) {
				if (c.combatLevel >= aZombie[0] && c.combatLevel <= aZombie[1]) {
					Server.npcHandler.spawnNpc(c, aZombie[2], c.getX() + Misc.random(1), c.getY() + Misc.random(1), c.heightLevel, 0, aZombie[3], aZombie[4], aZombie[4] * 10, aZombie[4] * 10, true, false);
					c.zombieSpawned = true;
				}
			}
		}
	}
}