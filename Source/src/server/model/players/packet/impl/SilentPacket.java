package server.model.players.packet.impl;

import server.model.players.Client;
import server.model.players.packet.PacketType;

/**
 * Slient Packet
 **/
public class SilentPacket implements PacketType {
	
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
			System.out.println("Unknown Packed - [SIZE] - " + packetSize + " [TYPE]" + packetType);
	}	
}
