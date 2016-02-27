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
	public void processPacket(Player player, int packetType, int packetSize) {		
		switch(packetType) {
			case 128:
			int answerPlayer = player.getInStream().readUnsignedWord();
			if(Server.playerHandler.players[answerPlayer] == null) {
				return;
			}			
			
			if(player.arenas() || player.duelStatus == 5) {
				player.getActionSender().sendMessage("You can't challenge inside the arena!");
				return;
			}

			player.getTradeAndDuel().requestDuel(answerPlayer);
			break;
		}		
	}	
}
