package server.model.players.packet;

import server.model.players.Player;

/**
 * Packet interface.
 * 
 * @author Graham
 * 
 */
public interface Packet {

	public void handlePacket(Player Player, int packetType, int packetSize);

}
