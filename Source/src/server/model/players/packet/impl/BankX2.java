package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;
/**
 * Bank X Items
 **/
public class BankX2 implements PacketType {
	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int Xamount = player.getInStream().readDWord();
		if (Xamount == 0)
			Xamount = 1;
		switch(player.xInterfaceId) {
			case 5064:
			player.getItems().bankItem(player.playerItems[player.xRemoveSlot] , player.xRemoveSlot, Xamount);
			break;
				
			case 5382:
			player.getItems().fromBank(player.bankItems[player.xRemoveSlot] , player.xRemoveSlot, Xamount);
			break;
				
			case 3322:
			if(player.duelStatus <= 0) {
            	player.getTradeAndDuel().tradeItem(player.xRemoveId, player.xRemoveSlot, Xamount);
            } else {				
				player.getTradeAndDuel().stakeItem(player.xRemoveId, player.xRemoveSlot, Xamount);
			}  
			break;
				
			case 3415: 
			if(player.duelStatus <= 0) { 
            	player.getTradeAndDuel().fromTrade(player.xRemoveId, player.xRemoveSlot, Xamount);
			} 
			break;
				
			case 6669:
			player.getTradeAndDuel().fromDuel(player.xRemoveId, player.xRemoveSlot, Xamount);
			break;			
		}
	}
}