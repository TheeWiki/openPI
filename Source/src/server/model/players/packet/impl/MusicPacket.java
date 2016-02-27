package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;

public class MusicPacket implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int id = c.getInStream().readDWord();
		
		switch(id)
		{
		
		}
	}
}