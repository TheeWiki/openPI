package server.world.locations.impl;

import server.model.players.Client;
import server.util.Misc;
import server.world.locations.AbstractLocations;

public class Home extends AbstractLocations
{

	@Override
	public void sendFirstClickObject(Client c, int object) {
		switch(object)
		{
		case 1276:
			c.sendMessage("Message from abstract class: Home");
			c.getItems().addItem(995, Misc.random(3494));
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
