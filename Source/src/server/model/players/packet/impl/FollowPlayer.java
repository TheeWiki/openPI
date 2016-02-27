package server.model.players.packet.impl;

import server.Server;
import server.model.players.Player;
import server.model.players.packet.PacketType;

/**
 * Follow Player
 **/
public class FollowPlayer implements PacketType {
	
	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int followPlayer = c.getInStream().readUnsignedWordBigEndian();
		if(Server.playerHandler.players[followPlayer] == null) {
			return;
		}
		c.playerIndex = 0;
		c.npcIndex = 0;
		c.mageFollow = false;
		c.usingBow = false;
		c.usingRangeWeapon = false;
		c.followDistance = 1;
		c.followId = followPlayer;
	}	
}
