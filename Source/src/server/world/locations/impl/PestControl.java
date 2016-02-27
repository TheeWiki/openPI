package server.world.locations.impl;

import server.model.minigames.pest_control.PestControlRewards;
import server.model.players.Player;
import server.world.locations.AbstractLocations;

public class PestControl extends AbstractLocations 
{

	@Override
	public void sendFirstClickObject(Player player, int object) {
		switch(object)
		{
		case 14315:
			player.getPA().movePlayer(2661,2639,0);
		break;
		case 14314:
			player.getPA().movePlayer(2657,2639,0);
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
		switch(npc)
		{
		case 3788:
			PestControlRewards.exchangePestPoints(player);
			break;
		}
	}

	@Override
	public void sendThirdClickNpc(Player player, int npc) {
		// TODO Auto-generated method stub
		
	}

}
