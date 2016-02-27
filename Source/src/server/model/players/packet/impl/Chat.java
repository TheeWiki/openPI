package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;
import server.net.Connection;

/**
 * Chat
 **/
public class Chat implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		player.setChatTextEffects(player.getInStream().readUnsignedByteS());
		player.setChatTextColor(player.getInStream().readUnsignedByteS());
        player.setChatTextSize((byte)(player.packetSize - 2));
        player.inStream.readBytes_reverseA(player.getChatText(), player.getChatTextSize(), 0); 
		player.setChatTextUpdateRequired(!Connection.isMuted(player));
		
		Report.appendChat(player.playerName, player.getChatText(), packetSize - 2);
	}	
}
