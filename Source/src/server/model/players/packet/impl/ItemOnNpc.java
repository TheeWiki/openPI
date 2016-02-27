package server.model.players.packet.impl;

import server.Server;
import server.model.items.UseItem;
import server.model.players.Player;
import server.model.players.packet.PacketType;


public class ItemOnNpc implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int itemId = player.getInStream().readSignedWordA();
		int i = player.getInStream().readSignedWordA();
		int slot = player.getInStream().readSignedWordBigEndian();
		@SuppressWarnings("static-access")
		int npcId = Server.npcHandler.npcs[i].npcType;
		
		UseItem.ItemonNpc(player, itemId, npcId, slot);
	}
}
