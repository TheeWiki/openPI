package server.model.players.packet.impl;

import server.Server;
import server.model.minigames.duel_arena.Rules;
import server.model.players.Player;
import server.model.players.packet.PacketType;


/**
 * Walking packet
 **/
public class Walking implements PacketType {

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player player, int packetType, int packetSize) {	
		player.walkingToItem = false;
		player.clickNpcType = 0;
		player.clickObjectType = 0;
		if (packetType == 248 || packetType == 164) {
			player.faceUpdate(0);
			player.npcIndex = 0;
			player.playerIndex = 0;
			if (player.followId > 0 || player.followId2 > 0) {
				player.getPA().resetFollow();
			}
			if (player.clickObjectType > 0)
				player.clickObjectType = 0;
		}		
		player.getPA().removeAllWindows();
		if(player.duelRule[Rules.WALKING_RULE.getRule()] && player.duelStatus == 5) {
			if(Server.playerHandler.players[player.duelingWith] != null) { 
				if(!player.goodDistance(player.getX(), player.getY(), Server.playerHandler.players[player.duelingWith].getX(), Server.playerHandler.players[player.duelingWith].getY(), 1) || player.attackTimer == 0) {
					player.getActionSender().sendMessage("Walking has been disabled in this duel!");
				}
			}
			player.playerIndex = 0;	
			return;		
		}
		
		if(player.freezeTimer > 0) {
			if(Server.playerHandler.players[player.playerIndex] != null) {
				if(player.goodDistance(player.getX(), player.getY(), Server.playerHandler.players[player.playerIndex].getX(), Server.playerHandler.players[player.playerIndex].getY(), 1) && packetType != 98) {
					player.playerIndex = 0;	
					return;
				}
			}
			if (packetType != 98) {
				player.getActionSender().sendMessage("A magical force stops you from moving.");
				player.playerIndex = 0;
			}	
			return;
		}
		
		if (System.currentTimeMillis() - player.lastSpear < 4000) {
			player.getActionSender().sendMessage("You have been stunned.");
			player.playerIndex = 0;
			return;
		}
		
		if (packetType == 98) {
			player.mageAllowed = true;
		}
		
		if((player.duelStatus >= 1 && player.duelStatus <= 4) || player.duelStatus == 6) {
			if(player.duelStatus == 6) {
				player.getTradeAndDuel().claimStakedItems();		
			}
			return;
		}
		
		
		if(player.respawnTimer > 3) {
			return;
		}
		if(player.inTrade) {
			return;
		}
		if(packetType == 248) {
			packetSize -= 14;
		}
		player.newWalkCmdSteps = (packetSize - 5)/2;
		if(++player.newWalkCmdSteps > player.walkingQueueSize) {
			player.newWalkCmdSteps = 0;
			return;
		}
		
		player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = 0;
		
		int firstStepX = player.getInStream().readSignedWordBigEndianA()-player.getMapRegionX()*8;
		for(int i = 1; i < player.newWalkCmdSteps; i++) {
			player.getNewWalkCmdX()[i] = player.getInStream().readSignedByte();
			player.getNewWalkCmdY()[i] = player.getInStream().readSignedByte();
		}
		
		int firstStepY = player.getInStream().readSignedWordBigEndian()-player.getMapRegionY()*8;
		player.setNewWalkCmdIsRunning(player.getInStream().readSignedByteC() == 1);
		for(int i1 = 0; i1 < player.newWalkCmdSteps; i1++) {
			player.getNewWalkCmdX()[i1] += firstStepX;
			player.getNewWalkCmdY()[i1] += firstStepY;
		}
	}

}
