package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;
import server.util.Misc;

public class ItemOnGroundItem implements PacketType {

	@SuppressWarnings("unused")
	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int a1 = player.getInStream().readSignedWordBigEndian();
		int itemUsed = player.getInStream().readSignedWordA();
		int groundItem = player.getInStream().readUnsignedWord();
		int gItemY = player.getInStream().readSignedWordA();
		int itemUsedSlot = player.getInStream().readSignedWordBigEndianA();
		int gItemX = player.getInStream().readUnsignedWord();
		
		switch(itemUsed) {
		
		default:
			if(player.playerRights == 3)
				Misc.println("ItemUsed "+itemUsed+" on Ground Item "+groundItem);
			break;
		}
	}

}
