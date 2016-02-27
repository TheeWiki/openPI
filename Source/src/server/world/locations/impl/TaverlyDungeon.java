package server.world.locations.impl;

import server.model.players.Player;
import server.world.locations.AbstractLocations;

public class TaverlyDungeon extends AbstractLocations
{

	@Override
	public void sendFirstClickObject(Player player, int object) {
		switch(object)
		{
		case 9293:
			if (player.absX < player.objectX) {
				player.getPA().movePlayer(2892, 9799, 0);
			} else {
				player.getPA().movePlayer(2886, 9799, 0);
			}
			break;
		case 9294:
			if (player.absX < player.objectX) {
				player.getPA().movePlayer(player.objectX + 1, player.absY, 0);
			} else if (player.absX > player.objectX) {
				player.getPA().movePlayer(player.objectX - 1, player.absY, 0);
			}
			break;
		case 1759:
			if (player.objectX == 2884 && player.objectY == 3397)
				player.getPA().movePlayer(player.absX, player.absY + 6400, 0);
			break;
		}
	}

	@Override
	public void sendSecondClickObject(Player player, int object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendThirdClickObject(Player player, int object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendFirstClickNpc(Player player, int npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendSecondClickNpc(Player player, int npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendThirdClickNpc(Player player, int npc) {
		// TODO Auto-generated method stub
		
	}

}
