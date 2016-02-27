package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;
import server.util.Misc;

/**
 * Item Click 2 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 * Proper Streams
 */

public class ItemClick2 implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int itemId = player.getInStream().readSignedWordA();
		
		if (!player.getItems().playerHasItem(itemId,1))
			return;

		switch (itemId) {
		
		default:
			if (player.playerRights == 3)
				Misc.println(player.playerName+ " - Item3rdOption: "+itemId);
			break;
		}

	}

}
