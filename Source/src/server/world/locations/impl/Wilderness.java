package server.world.locations.impl;

import server.Server;
import server.model.objects.Object;
import server.model.players.Player;
import server.world.locations.AbstractLocations;

public class Wilderness extends AbstractLocations
{

	@Override
	public void sendFirstClickObject(Player c, int object) {
		switch(object)
		{
		case 733:
			c.startAnimation(451);
			if (c.objectX == 3158 && c.objectY == 3951) {
				new Object(734, c.objectX, c.objectY, c.heightLevel, 1, 10, 733, 50);
			} else {
				new Object(734, c.objectX, c.objectY, c.heightLevel, 0, 10, 733, 50);
			}
			break;
		case 14829:
		case 14830:
		case 14827:
		case 14828:
		case 14826:
		case 14831:
			Server.objectManager.startObelisk(object);
			break;
		case 2566:
		case 2567:
		case 2568:
			// chest in pirate house
			break;
		case 411:
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sendThirdClickNpc(Player c, int npc) {
		// TODO Auto-generated method stub
		
	}

}
