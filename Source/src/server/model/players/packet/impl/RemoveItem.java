package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;


/**
 * Remove Item
 **/
public class RemoveItem implements PacketType {

	@SuppressWarnings("unused")
	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int interfaceId = player.getInStream().readUnsignedWordA();
		int removeSlot = player.getInStream().readUnsignedWordA();
		int removeId = player.getInStream().readUnsignedWordA();
		int shop = 0;
		int value = 0;
		String name = "null";
	
		switch(interfaceId) {
			
			case 1688:
			player.getItems().removeItem(removeId, removeSlot);
			break;
			
			case 5064:
			player.getItems().bankItem(removeId, removeSlot, 1);
			break;
			
			case 5382:
			player.getItems().fromBank(removeId, removeSlot, 1);
			break;
			
			case 3900:
			player.getShops().buyFromShopPrice(removeId, removeSlot);
			break;
			
			case 3823:
			player.getShops().sellToShopPrice(removeId, removeSlot);
			break;
			
			case 3322:
			if(player.duelStatus <= 0) { 
                player.getTradeAndDuel().tradeItem(removeId, removeSlot, 1);
           	} else {
				player.getTradeAndDuel().stakeItem(removeId, removeSlot, 1);
			}
			break;
			
			case 3415:
			if(player.duelStatus <= 0) { 
				player.getTradeAndDuel().fromTrade(removeId, removeSlot, 1);
           	} 
			break;
			
			case 6669:
			player.getTradeAndDuel().fromDuel(removeId, removeSlot, 1);
			break;
			

		}
	}
			
}
