package server.model.players.packet.impl;

import server.Connection;
import server.model.players.Player;
import server.model.players.packet.PacketType;

/**
 * Chat
 **/
public class Chat implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		c.setChatTextEffects(c.getInStream().readUnsignedByteS());
		c.setChatTextColor(c.getInStream().readUnsignedByteS());
        c.setChatTextSize((byte)(c.packetSize - 2));
        c.inStream.readBytes_reverseA(c.getChatText(), c.getChatTextSize(), 0); 
		c.setChatTextUpdateRequired(!Connection.isMuted(c));
		
		Report.appendChat(c.playerName, c.getChatText(), packetSize - 2);
	}	
}
