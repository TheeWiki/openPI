package server.world.locations.impl;

import server.model.players.Player;
import server.world.locations.AbstractLocations;

public class MageBank extends AbstractLocations
{

	@Override
	public void sendFirstClickObject(Player player, int object) {
		switch(object)
		{
		case 2873:
			if (!player.getItems().ownsCape()) {
				player.startAnimation(645);
				player.getActionSender().sendMessage("Saradomin blesses you with a cape.");
				player.getItems().addItem(2412, 1);
			}
			break;
		case 2875:
			if (!player.getItems().ownsCape()) {
				player.startAnimation(645);
				player.getActionSender().sendMessage("Guthix blesses you with a cape.");
				player.getItems().addItem(2413, 1);
			}
			break;
		case 2874:
			if (!player.getItems().ownsCape()) {
				player.startAnimation(645);
				player.getActionSender().sendMessage("Zamorak blesses you with a cape.");
				player.getItems().addItem(2414, 1);
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