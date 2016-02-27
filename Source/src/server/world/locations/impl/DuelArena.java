package server.world.locations.impl;

import server.model.players.Player;
import server.world.locations.AbstractLocations;

public class DuelArena extends AbstractLocations
{

	@Override
	public void sendFirstClickObject(Player c, int object) {
		switch(object)
		{
		case 3195:
			c.sendMessage("To be added");
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
		switch(npc)
		{
		case 963:
			c.sendMessage("Statistic tracker soon");
			break;		
		}
	}

	@Override
	public void sendSecondClickNpc(Player c, int npc) {
		switch(npc)
		{
		case 961:
		case 960:
		case 959:
			c.sendMessage("Healing soon");
			break;

		}
	}

	@Override
	public void sendThirdClickNpc(Player c, int npc) {
		// TODO Auto-generated method stub
		
	}

}
