package server.world.locations.impl;

import server.model.players.Client;
import server.util.Misc;
import server.world.locations.AbstractLocations;

public class Home extends AbstractLocations
{

	private final int ANCIENT_ALTER = 6552, PRAYER_ALTER = 409;
	
	@Override
	public void sendFirstClickObject(Client c, int object) {
		switch(object)
		{
		case 6839:
    		c.setForceMovement(0, 7, false, false, 1, 300, 2, 0x338);
		break;
		case 6847:
			int rng = Misc.random(3);
			switch(rng)
			{
			case 0:
				c.getDH().sendPlayerChat1("DING DONG!");
				break;
			case 1:
				c.getDH().sendPlayerChat1("I should probably stop touching this bell..");
				break;
			case 2:
				c.getDH().sendPlayerChat1("That's loud!");
				break;
			case 3:
				c.getDH().sendNpcChat1("Stop ringing that damn bell!", 872, "Head Wizzard");
				break;
			}
			break;
		case 2213:
			c.getPA().openUpBank();
			break;
		case ANCIENT_ALTER:
			if (c.playerMagicBook == 0) {
				c.playerMagicBook = 1;
				c.setSidebarInterface(6, 12855);
				c.autocasting = false;
				c.sendMessage("An ancient wisdomin fills your mind.");
				c.getPA().resetAutocast();
			} else {
				c.setSidebarInterface(6, 1151);
				c.playerMagicBook = 0;
				c.autocasting = false;
				c.sendMessage("You feel a drain on your memory.");
				c.autocastId = -1;
				c.getPA().resetAutocast();
			}
			break;
		case PRAYER_ALTER:
			if (c.playerLevel[5] < c.getPA().getLevelForXP(c.playerXP[5])) {
				c.startAnimation(645);
				c.playerLevel[5] = c.getPA().getLevelForXP(c.playerXP[5]);
				c.sendMessage("You recharge your prayer points.");
				c.getPA().refreshSkill(5);
			} else {
				c.sendMessage("You already have full prayer points.");
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
