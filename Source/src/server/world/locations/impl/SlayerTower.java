package server.world.locations.impl;

import server.model.players.Client;
import server.world.locations.AbstractLocations;

public class SlayerTower extends AbstractLocations
{

	@Override
	public void sendFirstClickObject(Client c, int object) {
		switch(object)
		{
		case 388:
			c.getDH().sendPlayerChat1("Probably some spooky scary skeletons in there");
			break;
		case 389:
			c.sendMessage("Nothing in here..");
			break;
		case 4496:
		case 4494:
			if (c.heightLevel == 2) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 0);
			}
			break;

		case 4493:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
			break;

		case 4495:
			if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
			break;
		case 9319:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(c.absX, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX, c.absY, 2);
			}
			break;

		case 9320:
			if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX, c.absY, 0);
			} else if (c.heightLevel == 2) {
				c.getPA().movePlayer(c.absX, c.absY, 1);
			}
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