package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;


/**
 * Wear Item
 **/
public class WearItem implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		player.wearId = player.getInStream().readUnsignedWord();
		player.wearSlot = player.getInStream().readUnsignedWordA();
		player.interfaceId = player.getInStream().readUnsignedWordA();
		
		@SuppressWarnings("unused")
		int oldCombatTimer = player.attackTimer;
		if (player.playerIndex > 0 || player.npcIndex > 0)
			player.getCombat().resetPlayerAttack();
	
//		Pouches.emptyRCPouch(c, c.wearId);
			//c.attackTimer = oldCombatTimer;
		player.getItems().wearItem(player.wearId, player.wearSlot);
	}

}
