package server.model.players.packet.impl;

import server.Constants;
import server.Server;
import server.model.players.Player;
import server.model.players.packet.PacketType;

/**
 * Drop Item
 **/
public class DropItem implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int itemId = player.getInStream().readUnsignedWordA();
		player.getInStream().readUnsignedByte();
		player.getInStream().readUnsignedByte();
		int slot = player.getInStream().readUnsignedWordA();
		if(player.arenas()) {
			player.getActionSender().sendMessage("You can't drop items inside the arena!");
			return;
		}

		boolean droppable = true;
		for (int i : Constants.UNDROPPABLE_ITEMS) {
			if (i == itemId) {
				droppable = false;
				break;
			}
		}
		if(player.playerItemsN[slot] != 0 && itemId != -1 && player.playerItems[slot] == itemId + 1) {
			if(droppable) {
				if (player.underAttackBy > 0) {
					if (player.getShops().getItemShopValue(itemId) > 1000) {
						player.getActionSender().sendMessage("You may not drop items worth more than 1000 while in combat.");
						return;
					}
				}
				Server.itemHandler.createGroundItem(player, itemId, player.getX(), player.getY(), player.playerItemsN[slot], player.getId());
				player.getItems().deleteItem(itemId, slot, player.playerItemsN[slot]);
			} else {
				player.getActionSender().sendMessage("This items cannot be dropped.");
			}
		}

	}
}
