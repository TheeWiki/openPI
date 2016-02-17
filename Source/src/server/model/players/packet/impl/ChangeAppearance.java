package server.model.players.packet.impl;

import server.model.players.Client;
import server.model.players.packet.PacketType;

/**
 * Change appearance
 **/
public class ChangeAppearance implements PacketType {

	/**
	 * TODO: Fix updating from make-over mage accept button
	 */
	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		int[] appearance = new int[13];
		appearance[0] = c.getInStream().readSignedByte();

		for (int i = 1; i < appearance.length; i++) {
			appearance[i] = c.getInStream().readSignedWord();
		}
	}
}
