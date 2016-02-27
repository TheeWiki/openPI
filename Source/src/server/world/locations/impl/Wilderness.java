package server.world.locations.impl;

import server.Server;
import server.model.objects.Object;
import server.model.players.Player;
import server.world.locations.AbstractLocations;

public class Wilderness extends AbstractLocations
{

	@Override
	public void sendFirstClickObject(Player player, int object) {
		switch(object)
		{
		case 733:
			player.startAnimation(451);
			if (player.objectX == 3158 && player.objectY == 3951) {
				new Object(734, player.objectX, player.objectY, player.heightLevel, 1, 10, 733, 50);
			} else {
				new Object(734, player.objectX, player.objectY, player.heightLevel, 0, 10, 733, 50);
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
			if (player.playerLevel[5] < player.getPA().getLevelForXP(player.playerXP[5])) {
				player.startAnimation(645);
				player.playerLevel[5] = player.getPA().getLevelForXP(player.playerXP[5]);
				player.getActionSender().sendMessage("You recharge your prayer points.");
				player.getPA().refreshSkill(5);
			} else {
				player.getActionSender().sendMessage("You already have full prayer points.");
			}
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
