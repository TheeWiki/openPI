package server.model.players.packet.impl;

/**
 * @author Ryan / Lmctruck30
 */

import server.model.items.UseItem;
import server.model.players.Player;
import server.model.players.packet.PacketType;

public class ItemOnObject implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		/*
		 * a = ?
		 * b = ?
		 */
		
		@SuppressWarnings("unused")
		int a = player.getInStream().readUnsignedWord();
		int objectId = player.getInStream().readSignedWordBigEndian();
		int objectY = player.getInStream().readSignedWordBigEndianA();
		@SuppressWarnings("unused")
		int b = player.getInStream().readUnsignedWord();
		int objectX = player.getInStream().readSignedWordBigEndianA();
		int itemId = player.getInStream().readUnsignedWord();
		UseItem.ItemonObject(player, objectId, objectX, objectY, itemId);
		
	}

}
