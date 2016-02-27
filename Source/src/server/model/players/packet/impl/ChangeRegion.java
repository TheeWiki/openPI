package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;

public class ChangeRegion implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		player.getPA().removeObjects();
		//Server.objectManager.loadObjects(c);
	}

}
