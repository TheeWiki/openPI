package server.model.players.packet;

import server.model.players.Player;

public interface PacketType {
	public void processPacket(Player c, int packetType, int packetSize);
}

