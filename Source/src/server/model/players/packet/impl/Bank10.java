package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;

/**
 * Bank 10 Items
 **/
public class Bank10 implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int interfaceId = player.getInStream().readUnsignedWordBigEndian();
		int removeId = player.getInStream().readUnsignedWordA();
		int removeSlot = player.getInStream().readUnsignedWordA();

		switch (interfaceId) {
		case 1688:
			player.getPA().useOperate(removeId);
			break;
		case 3900:
			if (player.inTrade) {
				player.getTradeAndDuel().declineTrade(true);
			}
			player.getShops().buyItem(removeId, removeSlot, 5);
			break;

		case 3823:
			if (player.inTrade) {
				player.getTradeAndDuel().declineTrade(true);
			}
			player.getShops().sellItem(removeId, removeSlot, 5);
			break;

		case 5064:
			if (player.inTrade) {
				player.getTradeAndDuel().declineTrade(true);
			}
			player.getItems().bankItem(removeId, removeSlot, 10);
			break;

		case 5382:
			if (player.inTrade) {
				player.getTradeAndDuel().declineTrade(true);
			}
			player.getItems().fromBank(removeId, removeSlot, 10);
			break;

		case 3322:
			if (player.duelStatus <= 0) {
				player.getTradeAndDuel().tradeItem(removeId, removeSlot, 10);
			} else {
				player.getTradeAndDuel().stakeItem(removeId, removeSlot, 10);
			}
			break;

		case 3415:
			if (player.duelStatus <= 0) {
				player.getTradeAndDuel().fromTrade(removeId, removeSlot, 10);
			}
			break;

		case 6669:
			player.getTradeAndDuel().fromDuel(removeId, removeSlot, 10);
			break;

		}
	}
}