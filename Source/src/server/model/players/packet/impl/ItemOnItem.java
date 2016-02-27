package server.model.players.packet.impl;

/**
 * @author Ryan / Lmctruck30
 */

import server.model.items.UseItem;
import server.model.players.Player;
import server.model.players.packet.PacketType;

public class ItemOnItem implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int usedWithSlot = player.getInStream().readUnsignedWord();
		int itemUsedSlot = player.getInStream().readUnsignedWordA();
		int useWith = player.playerItems[usedWithSlot] - 1;
		int itemUsed = player.playerItems[itemUsedSlot] - 1;
		UseItem.ItemonItem(player, itemUsed, useWith);
	}

}
