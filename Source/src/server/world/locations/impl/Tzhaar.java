package server.world.locations.impl;

import server.Server;
import server.model.dialogues.DialogueAction;
import server.model.dialogues.DialogueContainer;
import server.model.players.Player;
import server.model.players.EquipmentListener;
import server.world.locations.AbstractLocations;

public class Tzhaar extends AbstractLocations
{

	@Override
	public void sendFirstClickObject(Player c, int object) {
		switch(object)
		{
		case 9368:
			if (c.getY() < 5169) {
				Server.fightPits.removePlayerFromPits(c.playerId);
				c.getPA().movePlayer(2399, 5169, 0);
			}
			break;
		case 9356:
			DialogueContainer.CreateDialogue(c, new DialogueAction() {
				@Override
				public void execute(DialogueContainer container) {
					switch (container.getOptionId()) {
					case 1: 
						c.fightForOnyx = false;
						c.getPA().enterCaves();
						break;
					case 2:
						c.fightForOnyx = true;
						c.getPA().enterCaves();
						break;
					}
				}
			}, "Fight for Fire Cape", "Fight for Uncut Onyx gem", "Nevermind");
			
			break;
		case 9391:
			c.getDH().sendStatement("To be added shortly");
			break;
		case 9369:
			if (c.getY() > 5175)
				c.getPA().movePlayer(2399, 5175, 0);
			else
				c.getPA().movePlayer(2399, 5177, 0);
			break;
		case 9357:
			c.getDH().sendNpcChat1("Pathetic, run and don't come back in.", 2617, "Tk-nub");
			c.getPA().movePlayer(2438, 5168, 0);
			break;
		}
	}

	@Override
	public void sendSecondClickObject(Player c, int object) {
		switch(object)
		{
		case 9390: // bonus experience 
			if (c.playerEquipment[EquipmentListener.CAPE_SLOT.getSlot()] == 6570)
			{
				//send interface
			} else {
				c.sendMessage("You need to be wearing a Fire Cape to use this Lava Forge");
			}
			break;
		}
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
