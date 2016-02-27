package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;
import server.util.Plugin;

/**
 * Clicking an item, bury bone, eat food etc
 **/
public class ClickItem implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		@SuppressWarnings("unused")
		int junk = player.getInStream().readSignedWordBigEndianA();
		int itemSlot = player.getInStream().readUnsignedWordA();
		int itemId = player.getInStream().readUnsignedWordBigEndian();
		if (itemId != player.playerItems[itemSlot] - 1) {
			return;
		}
		/**
		 * Gets the static usage from the skill class and access it in the Python script
		 */
		Plugin.execute("click", player, itemId, itemSlot);
		
		Plugin.execute("itemClick_" + itemId, player, itemId, itemSlot);
	}
}