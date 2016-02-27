package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;
/**
 * Bank 5 Items
 **/
public class Bank5 implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
	int interfaceId = player.getInStream().readSignedWordBigEndianA();
	int removeId = player.getInStream().readSignedWordBigEndianA();
	int removeSlot = player.getInStream().readSignedWordBigEndian();
	
		switch(interfaceId){

			case 3900:
			player.getShops().buyItem(removeId, removeSlot, 1);
			break;
			
			case 3823:
			player.getShops().sellItem(removeId, removeSlot, 1);
			break;
			
			case 5064:
			player.getItems().bankItem(removeId, removeSlot, 5);
			break;
			
			case 5382:
			player.getItems().fromBank(removeId, removeSlot, 5);
			break;
			
			case 3322:
			if(player.duelStatus <= 0) { 
                player.getTradeAndDuel().tradeItem(removeId, removeSlot, 5);
           	} else {
				player.getTradeAndDuel().stakeItem(removeId, removeSlot, 5);
			}	
			break;
			
			case 3415:
			if(player.duelStatus <= 0) { 
				player.getTradeAndDuel().fromTrade(removeId, removeSlot, 5);
			}
			break;
			
			case 6669:
			player.getTradeAndDuel().fromDuel(removeId, removeSlot, 5);
			break;

			
		}
	}

}
