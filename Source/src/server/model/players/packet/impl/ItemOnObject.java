package server.model.players.packet.impl;

/**
 * @author Ryan / Lmctruck30
 */

import server.model.items.UseItem;
import server.model.players.Player;
import server.model.players.packet.PacketType;

public class ItemOnObject implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		/*
		 * a = ?
		 * b = ?
		 */
		
		@SuppressWarnings("unused")
		int a = c.getInStream().readUnsignedWord();
		int objectId = c.getInStream().readSignedWordBigEndian();
		int objectY = c.getInStream().readSignedWordBigEndianA();
		@SuppressWarnings("unused")
		int b = c.getInStream().readUnsignedWord();
		int objectX = c.getInStream().readSignedWordBigEndianA();
		int itemId = c.getInStream().readUnsignedWord();
		UseItem.ItemonObject(c, objectId, objectX, objectY, itemId);
		
	}

}
