package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;

public class MusicPacket implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int id = player.getInStream().readDWord();
		
		switch(id)
		{
		
		}
	}
}