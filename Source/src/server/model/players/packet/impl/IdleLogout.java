package server.model.players.packet.impl;


import server.model.players.Client;
import server.model.players.packet.PacketType;


public class IdleLogout implements PacketType {
	
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
	}
}