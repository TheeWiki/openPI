package server.world.locations.impl;

import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Player;
import server.util.Misc;
import server.world.locations.AbstractLocations;

public class Barrows extends AbstractLocations
{

	@Override
	public void sendFirstClickObject(Player player, int object) {
		switch(object)
		{
		case 6772:
			if(server.model.minigames.barrows.Barrows.selectCoffin(player, object)) {
				return;
			}
			if(player.barrowsNpcs[1][1] == 0) {
				Server.npcHandler.spawnNpc(player, 2029, player.getX()+1, player.getY(), -1, 0, 120, 20, 200, 200, true, true);
				player.barrowsNpcs[1][1] = 1;
			} else {
				player.getActionSender().sendMessage("You have already searched in this sarcophagus.");
			}
			break;
			
		case 6707: // Verac
			player.getPA().movePlayer(3556, 3298, 0);
			break;
			
		case 6823:
			if(server.model.minigames.barrows.Barrows.selectCoffin(player, object)) {
				return;
			}
			if(player.barrowsNpcs[0][1] == 0) {
				Server.npcHandler.spawnNpc(player, 2030, player.getX(), player.getY()-1, -1, 0, 120, 25, 200, 200, true, true);
				player.barrowsNpcs[0][1] = 1;
			} else {
				player.getActionSender().sendMessage("You have already searched in this sarcophagus.");
			}
			break;

		case 6706: // Torag
			player.getPA().movePlayer(3553, 3283, 0);
			break;
						
		case 6705: // Karil
			player.getPA().movePlayer(3565, 3276, 0);
			break;
		case 6822:
			if(server.model.minigames.barrows.Barrows.selectCoffin(player, object)) {
				return;
			}
			if(player.barrowsNpcs[2][1] == 0) {
				Server.npcHandler.spawnNpc(player, 2028, player.getX(), player.getY()-1, -1, 0, 90, 17, 200, 200, true, true);
				player.barrowsNpcs[2][1] = 1;
			} else {
				player.getActionSender().sendMessage("You have already searched in this sarcophagus.");
			}
			break;
			
		case 6704: // Guthan
			player.getPA().movePlayer(3578, 3284, 0);
			break;
		case 6773:
			if(server.model.minigames.barrows.Barrows.selectCoffin(player, object)) {
				return;
			}
			if(player.barrowsNpcs[3][1] == 0) {
				Server.npcHandler.spawnNpc(player, 2027, player.getX(), player.getY()-1, -1, 0, 120, 23, 200, 200, true, true);
				player.barrowsNpcs[3][1] = 1;
			} else {
				player.getActionSender().sendMessage("You have already searched in this sarcophagus.");
			}
			break;
			
		case 6703: // Dharok
			player.getPA().movePlayer(3574, 3298, 0);
			break;
		case 6771:
			if(server.model.minigames.barrows.Barrows.selectCoffin(player, object)) {
				return;
			}
			if(player.barrowsNpcs[4][1] == 0) {
				Server.npcHandler.spawnNpc(player, 2026, player.getX(), player.getY()-1, -1, 0, 120, 45, 250, 250, true, true);
				player.barrowsNpcs[4][1] = 1;
			} else {
				player.getActionSender().sendMessage("You have already searched in this sarcophagus.");
			}
			break;
			
		case 6702: // Ahrim
			player.getPA().movePlayer(3565, 3290, 0);
			break;
		case 6821:
			if(server.model.minigames.barrows.Barrows.selectCoffin(player, object)) {
				return;
			}
			if(player.barrowsNpcs[5][1] == 0) {
				Server.npcHandler.spawnNpc(player, 2025, player.getX(), player.getY()-1, -1, 0, 90, 19, 200, 200, true, true);
				player.barrowsNpcs[5][1] = 1;
			} else {
				player.getActionSender().sendMessage("You have already searched in this sarcophagus.");
			}
			break;
		
		/**
		 * Clicking the barrows chest.
		 */
		case 10284:
			if(player.barrowsKillCount < 5) {
				player.getActionSender().sendMessage("You haven't killed all the brothers");
			}
			if(player.barrowsKillCount == 5 && player.barrowsNpcs[player.randomCoffin][1] == 1) {
				player.getActionSender().sendMessage("I have already summoned this npc.");
			}
			if(player.barrowsNpcs[player.randomCoffin][1] == 0 && player.barrowsKillCount >= 5) {
				Server.npcHandler.spawnNpc(player, player.barrowsNpcs[player.randomCoffin][0], 3551, 9694-1, 0, 0, 120, 30, 200, 200, true, true);
				player.barrowsNpcs[player.randomCoffin][1] = 1;
			}
			if((player.barrowsKillCount > 5 || player.barrowsNpcs[player.randomCoffin][1] == 2) && player.getItems().freeSlots() >= 2) {
				player.getPA().resetBarrows();
				player.getItems().addItem(player.getPA().randomRunes(), Misc.random(150) + 100);
				if (Misc.random(2) == 1)
					player.getItems().addItem(player.getPA().randomBarrows(), 1);
				player.getActionSender().shakeScreen(player, 2);
				CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						player.getPA().createPlayersProjectile(player.absX, player.absY, player.absX, player.absY, 60, 60, 60, 43, 31, - player.playerId - 1, 0);
						player.handleHitMask(5);
						container.stop();
					}

					@Override
					public void stop() {

					}
				}, 10_000);
//				c.getPA().startTeleport(3564, 3288, 0, "modern");
			} else if(player.barrowsKillCount > 5 && player.getItems().freeSlots() <= 1) {
				player.getActionSender().sendMessage("You need at least 2 inventory slot opened.");
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
