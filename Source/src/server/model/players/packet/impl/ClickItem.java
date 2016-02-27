package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;
import server.util.Plugin;

/**
 * Clicking an item, bury bone, eat food etc
 **/
public class ClickItem implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		@SuppressWarnings("unused")
		int junk = c.getInStream().readSignedWordBigEndianA();
		int itemSlot = c.getInStream().readUnsignedWordA();
		int itemId = c.getInStream().readUnsignedWordBigEndian();
		if (itemId != c.playerItems[itemSlot] - 1) {
			return;
		}
		/**
		 * Gets the static usage from the skill class and access it in the Python script
		 */
		Plugin.execute("click", c, itemId, itemSlot);
		
		Plugin.execute("itemClick_" + itemId, c, itemId, itemSlot);
	}
}