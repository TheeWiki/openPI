package server.model.players.packet.impl;

import server.Server;
import server.model.players.Client;
import server.model.players.packet.PacketType;


/**
 * Clicking stuff (interfaces)
 **/
public class ClickingStuff implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		if (c.inTrade) {
			if (!c.acceptedTrade) {
				@SuppressWarnings("static-access")
				Client o = (Client) Server.playerHandler.players[c.tradeWith];
				o.tradeAccepted = false;
				c.tradeAccepted = false;
				o.tradeStatus = 0;
				c.tradeStatus = 0;
				c.tradeConfirmed = false;
				c.tradeConfirmed2 = false;
				c.sendMessage("@blu@You have declined the trade.");
				o.sendMessage(c.playerName2 + "@blu@ has declined the trade.");
				c.getTradeAndDuel().declineTrade();
			}
		}
		//if (c.isBanking)
		//c.isBanking = false;

		@SuppressWarnings("static-access")
		Client o = (Client) Server.playerHandler.players[c.duelingWith];
			if (c.duelStatus == 5) {
				//c.sendMessage("This glitch has been fixed by Ardi, sorry sir.");
				return;
			}
		if(o != null) {
			if(c.duelStatus >= 1 && c.duelStatus <= 4) {
				c.getTradeAndDuel().declineDuel();
				o.getTradeAndDuel().declineDuel();
			}
		}
		
		if(c.duelStatus == 6) {
			c.getTradeAndDuel().claimStakedItems();		
		}
	}		
}