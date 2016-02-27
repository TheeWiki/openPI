package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;

/**
 * Slient Packet
 **/
public class SilentPacket implements PacketType {
	
	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
//			System.out.println("Unknown Packed - [SIZE] - " + packetSize + " [TYPE]" + packetType);
	}	
}
