package server.world.locations.impl;

import server.model.players.Player;
import server.world.locations.AbstractLocations;

public class SlayerTower extends AbstractLocations
{

	@Override
	public void sendFirstClickObject(Player player, int object) {
		switch(object)
		{
		case 388:
			player.getDH().sendPlayerChat1("Probably some spooky scary skeletons in there");
			break;
		case 389:
			player.getActionSender().sendMessage("Nothing in here..");
			break;
		case 4496:
		case 4494:
			if (player.heightLevel == 2) {
				player.getPA().movePlayer(player.absX - 5, player.absY, 1);
			} else if (player.heightLevel == 1) {
				player.getPA().movePlayer(player.absX + 5, player.absY, 0);
			}
			break;

		case 4493:
			if (player.heightLevel == 0) {
				player.getPA().movePlayer(player.absX - 5, player.absY, 1);
			} else if (player.heightLevel == 1) {
				player.getPA().movePlayer(player.absX + 5, player.absY, 2);
			}
			break;

		case 4495:
			if (player.heightLevel == 1) {
				player.getPA().movePlayer(player.absX + 5, player.absY, 2);
			}
			break;
		case 9319:
			if (player.heightLevel == 0) {
				player.getPA().movePlayer(player.absX, player.absY, 1);
			} else if (player.heightLevel == 1) {
				player.getPA().movePlayer(player.absX, player.absY, 2);
			}
			break;

		case 9320:
			if (player.heightLevel == 1) {
				player.getPA().movePlayer(player.absX, player.absY, 0);
			} else if (player.heightLevel == 2) {
				player.getPA().movePlayer(player.absX, player.absY, 1);
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