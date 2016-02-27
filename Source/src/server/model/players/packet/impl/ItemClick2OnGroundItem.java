package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;

public class ItemClick2OnGroundItem implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		final int itemX = player.getInStream().readSignedWordBigEndian();
		final int itemY = player.getInStream().readSignedWordBigEndianA();
		final int itemId = player.getInStream().readUnsignedWordA();
		System.out.println("ItemClick2OnGroundItem - " + player.playerName + " - " + itemId + " - " + itemX + " - " + itemY);
//		for (logData l : logData.values()) {
//			if (itemId == l.getLogId()) {
//				Firemaking.attemptFire(c, 590, itemId, itemX, itemY, true);
//			}
//		}
	}
}