package server.world.locations.impl;

import server.model.players.Client;
import server.world.locations.AbstractLocations;

public class Tzhaar extends AbstractLocations
{

	@Override
	public void sendFirstClickObject(Client c, int object) {
		switch(object)
		{
		case 9356:
			c.getPA().enterCaves();
			c.sendMessage("Best of luck in the waves!");
			break;
		case 9369:
			c.sendMessage("TODO");
			break;
		}
	}

	@Override
	public void sendSecondClickObject(Client c, int object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendThirdClickObject(Client c, int object) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendFirstClickNpc(Client c, int npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendSecondClickNpc(Client c, int npc) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendThirdClickNpc(Client c, int npc) {
		// TODO Auto-generated method stub
		
	}

}
