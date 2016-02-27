package server.model.players.packet;

import server.model.players.Player;

public interface PacketType {
	public void processPacket(Player player, int packetType, int packetSize);
}

