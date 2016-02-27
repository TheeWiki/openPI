package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;
/**
 * Bank X Items
 **/
public class BankX1 implements PacketType {

	public static final int PART1 = 135;
	public static final int	PART2 = 208;
	public int XremoveSlot, XinterfaceID, XremoveID, Xamount;
	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		if (packetType == 135) {
			player.xRemoveSlot = player.getInStream().readSignedWordBigEndian();
			player.xInterfaceId = player.getInStream().readUnsignedWordA();
			player.xRemoveId = player.getInStream().readSignedWordBigEndian();
		}
		if (player.xInterfaceId == 3900) {
			player.getShops().buyItem(player.xRemoveId, player.xRemoveSlot, 20);//buy 20
			player.xRemoveSlot = 0;
			player.xInterfaceId = 0;
			player.xRemoveId = 0;
			return;
		}

		if(packetType == PART1) {
			synchronized(player) {
				player.getOutStream().createFrame(27);
			}			
		}
	
	}
}
