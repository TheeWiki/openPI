package server.model.players.packet.impl;

import server.Constants;
import server.model.players.Player;
import server.model.players.packet.PacketType;

/**
 * Trading
 */
public class Trade implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int tradeId = player.getInStream().readSignedWordBigEndian();
		player.getPA().resetFollow();
		
		if(player.arenas()) {
			player.getActionSender().sendMessage("You can't trade inside the arena!");
			return;
		}
		
		
		if(player.playerRights == 2 && !Constants.ADMIN_CAN_TRADE) {
			player.getActionSender().sendMessage("Trading as an admin has been disabled.");
			return;
		}
		if (tradeId != player.playerId)
			player.getTradeAndDuel().requestTrade(tradeId);
	}
		
}
