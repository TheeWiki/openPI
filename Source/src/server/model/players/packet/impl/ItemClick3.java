package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;
import server.model.players.skills.runecrafting.TalismanHandler;
import server.model.players.skills.runecrafting.TalismanHandler.talismanData;
import server.util.Misc;

/**
 * Item Click 3 Or Alternative Item Option 1
 * 
 * @author Ryan / Lmctruck30
 * 
 * Proper Streams
 */

public class ItemClick3 implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		int itemId11 = c.getInStream().readSignedWordBigEndianA();
		int itemId1 = c.getInStream().readSignedWordA();
		int itemId = c.getInStream().readSignedWordA();
		
		for (talismanData t : talismanData.values()) {
			if (itemId == t.getTalisman()) {
				TalismanHandler.handleTalisman(c, itemId);
			}
		}
		switch (itemId) {
		default:
			if (c.playerRights == 3)
				Misc.println(c.playerName+ " - Item3rdOption: "+itemId+" : "+itemId11+" : "+itemId1);
			break;
		}
	}
}