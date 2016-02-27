package server.model.players.packet.impl;

import server.Server;
import server.model.players.Player;
import server.model.players.packet.PacketType;

/**
 * Challenge Player
 **/
public class ChallengePlayer implements PacketType {

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {		
		switch(packetType) {
			case 128:
			int answerPlayer = c.getInStream().readUnsignedWord();
			if(Server.playerHandler.players[answerPlayer] == null) {
				return;
			}			
			
			if(c.arenas() || c.duelStatus == 5) {
				c.sendMessage("You can't challenge inside the arena!");
				return;
			}

			c.getTradeAndDuel().requestDuel(answerPlayer);
			break;
		}		
	}	
}
