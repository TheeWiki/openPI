package server.world.locations.impl;

import server.model.minigames.pest_control.PestControlRewards;
import server.model.players.Player;
import server.world.locations.AbstractLocations;

public class PestControl extends AbstractLocations 
{

	@Override
	public void sendFirstClickObject(Player c, int object) {
		switch(object)
		{
		case 14315:
			c.getPA().movePlayer(2661,2639,0);
		break;
		case 14314:
			c.getPA().movePlayer(2657,2639,0);
		break;
		}
	}

	@Override
	public void sendSecondClickObject(Player c, int object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendThirdClickObject(Player c, int object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendFirstClickNpc(Player c, int npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendSecondClickNpc(Player c, int npc) {
		switch(npc)
		{
		case 3788:
			PestControlRewards.exchangePestPoints(c);
			break;
		}
	}

	@Override
	public void sendThirdClickNpc(Player c, int npc) {
		// TODO Auto-generated method stub
		
	}

}
