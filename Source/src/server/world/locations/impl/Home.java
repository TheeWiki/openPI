package server.world.locations.impl;

import server.model.players.Player;
import server.model.players.skills.AwardSkillcape;
import server.util.Misc;
import server.world.locations.AbstractLocations;

public class Home extends AbstractLocations
{

	private final int ANCIENT_ALTER = 6552, PRAYER_ALTER = 409;
	
	@Override
	public void sendFirstClickObject(Player player, int object) {
		switch(object)
		{
		case 6839:
    		player.setForceMovement(0, 7, false, false, 1, 300, 2, 0x338);
		break;
		case 6847:
			int rng = Misc.random(3);
			switch(rng)
			{
			case 0:
				player.getDH().sendPlayerChat1("DING DONG!");
				break;
			case 1:
				player.getDH().sendPlayerChat1("I should probably stop touching this bell..");
				break;
			case 2:
				player.getDH().sendPlayerChat1("That's loud!");
				break;
			case 3:
				player.getDH().sendNpcChat1("Stop ringing that damn bell!", 872, "Head Wizzard");
				break;
			}
			break;
		case 6836:
				AwardSkillcape.executeAward(player);
			break;
		case ANCIENT_ALTER:
			if (player.playerMagicBook == 0) {
				player.playerMagicBook = 1;
				player.setSidebarInterface(6, 12855);
				player.autocasting = false;
				player.getActionSender().sendMessage("An ancient wisdomin fills your mind.");
				player.getPA().resetAutocast();
			} else {
				player.setSidebarInterface(6, 1151);
				player.playerMagicBook = 0;
				player.autocasting = false;
				player.getActionSender().sendMessage("You feel a drain on your memory.");
				player.autocastId = -1;
				player.getPA().resetAutocast();
			}
			break;
		case PRAYER_ALTER:
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
		switch(npc)
		{
		case 599:
			player.getPA().showInterface(3559);
			player.canChangeAppearance = true;
			break;
		case 2566:
			player.getShops().openSkillCape();
			break;
		}
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
