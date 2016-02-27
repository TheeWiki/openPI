package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;

/**
 * Move Items
 **/
public class MoveItems implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int somejunk = player.getInStream().readUnsignedWordA(); //junk
		int itemFrom =  player.getInStream().readUnsignedWordA();// slot1
		int itemTo = (player.getInStream().readUnsignedWordA() -128);// slot2
		//c.sendMessage("junk: " + somejunk);
		player.getItems().moveItems(itemFrom, itemTo, somejunk);
	}
}
