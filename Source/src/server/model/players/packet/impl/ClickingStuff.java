package server.model.players.packet.impl;

import server.Server;
import server.model.players.Player;
import server.model.players.packet.PacketType;


/**
 * Clicking stuff (interfaces)
 **/
public class ClickingStuff implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		if (player.inTrade) {
			if (!player.acceptedTrade) {
				@SuppressWarnings("static-access")
				Player o = (Player) Server.playerHandler.players[player.tradeWith];
				o.tradeAccepted = false;
				player.tradeAccepted = false;
				o.tradeStatus = 0;
				player.tradeStatus = 0;
				player.tradeConfirmed = false;
				player.tradeConfirmed2 = false;
				player.getActionSender().sendMessage("@blu@You have declined the trade.");
				o.getActionSender().sendMessage(player.playerName2 + "@blu@ has declined the trade.");
				player.getTradeAndDuel().declineTrade();
			}
		}
		//if (c.isBanking)
		//c.isBanking = false;

		@SuppressWarnings("static-access")
		Player o = (Player) Server.playerHandler.players[player.duelingWith];
			if (player.duelStatus == 5) {
				//c.sendMessage("This glitch has been fixed by Ardi, sorry sir.");
				return;
			}
		if(o != null) {
			if(player.duelStatus >= 1 && player.duelStatus <= 4) {
				player.getTradeAndDuel().declineDuel();
				o.getTradeAndDuel().declineDuel();
			}
		}
		
		if(player.duelStatus == 6) {
			player.getTradeAndDuel().claimStakedItems();		
		}
	}		
}