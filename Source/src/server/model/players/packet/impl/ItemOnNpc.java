package server.model.players.packet.impl;

import server.Server;
import server.model.items.UseItem;
import server.model.players.Player;
import server.model.players.packet.PacketType;


public class ItemOnNpc implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int itemId = c.getInStream().readSignedWordA();
		int i = c.getInStream().readSignedWordA();
		int slot = c.getInStream().readSignedWordBigEndian();
		@SuppressWarnings("static-access")
		int npcId = Server.npcHandler.npcs[i].npcType;
		
		UseItem.ItemonNpc(c, itemId, npcId, slot);
	}
}
