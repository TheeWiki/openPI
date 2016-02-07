package server.model.players.packet;

import server.model.players.Client;

public interface PacketType {
	public void processPacket(Client c, int packetType, int packetSize);
}

