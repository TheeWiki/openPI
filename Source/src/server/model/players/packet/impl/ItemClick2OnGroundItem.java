package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;

public class ItemClick2OnGroundItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		final int itemX = c.getInStream().readSignedWordBigEndian();
		final int itemY = c.getInStream().readSignedWordBigEndianA();
		final int itemId = c.getInStream().readUnsignedWordA();
		System.out.println("ItemClick2OnGroundItem - " + c.playerName + " - " + itemId + " - " + itemX + " - " + itemY);
//		for (logData l : logData.values()) {
//			if (itemId == l.getLogId()) {
//				Firemaking.attemptFire(c, 590, itemId, itemX, itemY, true);
//			}
//		}
	}
}