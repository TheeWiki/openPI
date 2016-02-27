package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;


/**
 * Magic on items
 **/
public class MagicOnItems implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int slot = player.getInStream().readSignedWord();
		int itemId = player.getInStream().readSignedWordA();
		@SuppressWarnings("unused")
		int junk = player.getInStream().readSignedWord();
		int spellId = player.getInStream().readSignedWordA();
		
		player.usingMagic = true;
		player.getPA().magicOnItems(slot, itemId, spellId);
		player.usingMagic = false;

	}

}
