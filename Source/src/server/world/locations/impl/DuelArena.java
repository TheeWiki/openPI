package server.world.locations.impl;

import server.model.players.Player;
import server.world.locations.AbstractLocations;

public class DuelArena extends AbstractLocations
{

	@Override
	public void sendFirstClickObject(Player player, int object) {
		switch(object)
		{
		case 3195:
			player.getActionSender().sendMessage("To be added");
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
		switch(npc)
		{
		case 963:
			player.getActionSender().sendMessage("Statistic tracker soon");
			break;		
		}
	}

	@Override
	public void sendSecondClickNpc(Player player, int npc) {
		switch(npc)
		{
		case 961:
		case 960:
		case 959:
			player.getActionSender().sendMessage("Healing soon");
			break;

		}
	}

	@Override
	public void sendThirdClickNpc(Player player, int npc) {
		// TODO Auto-generated method stub
		
	}

}
