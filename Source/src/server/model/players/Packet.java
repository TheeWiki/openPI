package server.model.players;

/**
 * Packet interface.
 * 
 * @author Graham
 * 
 */
public interface Packet {

	public void handlePacket(Client client, int packetType, int packetSize);

}
