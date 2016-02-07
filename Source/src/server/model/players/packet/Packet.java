package server.model.players.packet;

import server.model.players.Client;

/**
 * Packet interface.
 * 
 * @author Graham
 * 
 */
public interface Packet {

	public void handlePacket(Client client, int packetType, int packetSize);

}
