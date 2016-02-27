package server.model.players.packet.impl;

import server.model.items.GameItem;
import server.model.items.Item;
import server.model.players.Player;
import server.model.players.packet.PacketType;

/**
 * Bank All Items
 **/
public class BankAll implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
	int removeSlot = player.getInStream().readUnsignedWordA();
	int interfaceId = player.getInStream().readUnsignedWord();
	int removeId = player.getInStream().readUnsignedWordA();
	
		switch(interfaceId){			
			case 3900:
			player.getShops().buyItem(removeId, removeSlot, 10);
			break;
			
			case 3823:
			player.getShops().sellItem(removeId, removeSlot, 10);
			break;
			
			case 5064:
			if (Item.itemStackable[removeId]) {
				player.getItems().bankItem(player.playerItems[removeSlot] , removeSlot, player.playerItemsN[removeSlot]);
			} else {
				player.getItems().bankItem(player.playerItems[removeSlot] , removeSlot, player.getItems().itemAmount(player.playerItems[removeSlot]));
			}
			break;
			
			case 5382:
			player.getItems().fromBank(player.bankItems[removeSlot] , removeSlot, player.bankItemsN[removeSlot]);
			break;	
			
			case 3322:
			if(player.duelStatus <= 0) { 
				if(Item.itemStackable[removeId]){
					player.getTradeAndDuel().tradeItem(removeId, removeSlot, player.playerItemsN[removeSlot]);
		    	} else {
					player.getTradeAndDuel().tradeItem(removeId, removeSlot, 28);  
				}
			} else {
				if(Item.itemStackable[removeId] || Item.itemIsNote[removeId]) {
					player.getTradeAndDuel().stakeItem(removeId, removeSlot, player.playerItemsN[removeSlot]);
				} else {
					player.getTradeAndDuel().stakeItem(removeId, removeSlot, 28);
				}
			}
			break;
			
			case 3415: 
			if(player.duelStatus <= 0) { 
				if(Item.itemStackable[removeId]) {
					for (GameItem item : player.getTradeAndDuel().offeredItems) {
						if(item.id == removeId) {
							player.getTradeAndDuel().fromTrade(removeId, removeSlot, player.getTradeAndDuel().offeredItems.get(removeSlot).amount);
						}
					}
				} else {
					for (GameItem item : player.getTradeAndDuel().offeredItems) {
						if(item.id == removeId) {
							player.getTradeAndDuel().fromTrade(removeId, removeSlot, 28);
						}
					}
				}
            } 
			break;
			
			case 7295:
			if (Item.itemStackable[removeId]) {
			player.getItems().bankItem(player.playerItems[removeSlot] , removeSlot, player.playerItemsN[removeSlot]);
			player.getItems().resetItems(7423);
			} else {
			player.getItems().bankItem(player.playerItems[removeSlot] , removeSlot, player.getItems().itemAmount(player.playerItems[removeSlot]));
			player.getItems().resetItems(7423);
			}
			break;
			
			case 6669:
			if(Item.itemStackable[removeId] || Item.itemIsNote[removeId]) {
				for (GameItem item : player.getTradeAndDuel().stakedItems) {
					if(item.id == removeId) {
						player.getTradeAndDuel().fromDuel(removeId, removeSlot, player.getTradeAndDuel().stakedItems.get(removeSlot).amount);
					}
				}
						
			} else {
				player.getTradeAndDuel().fromDuel(removeId, removeSlot, 28);
			}
			break;

		}
	}

}
