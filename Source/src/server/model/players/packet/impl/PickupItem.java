package server.model.players.packet.impl;

import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Player;
import server.model.players.packet.PacketType;

/**
 * Pickup Item
 **/
public class PickupItem implements PacketType {

	@Override
	public void processPacket(final Player player, int packetType, int packetSize) {
		player.walkingToItem = false;
		player.pItemY = player.getInStream().readSignedWordBigEndian();
		player.pItemId = player.getInStream().readUnsignedWord();
		player.pItemX = player.getInStream().readSignedWordBigEndian();
		if (Math.abs(player.getX() - player.pItemX) > 25 || Math.abs(player.getY() - player.pItemY) > 25) {
			player.resetWalkingQueue();
			return;
		}
		player.getCombat().resetPlayerAttack();
		if (player.getX() == player.pItemX && player.getY() == player.pItemY) {
			Server.itemHandler.removeGroundItem(player, player.pItemId, player.pItemX, player.pItemY, true);
		} else {
			player.walkingToItem = true;
			CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					if (!player.walkingToItem)
						container.stop();
					if (player.getX() == player.pItemX && player.getY() == player.pItemY) {
						Server.itemHandler.removeGroundItem(player, player.pItemId, player.pItemX, player.pItemY, true);
						container.stop();
					}
				}

				@Override
				public void stop() {
					player.walkingToItem = false;
				}
			}, 1);
		}
	}
}