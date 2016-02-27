package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;

public class Dialogue implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		if (player.getDialogue() != null) {
			if (player.getDialogue().getNextDialogueId() > 0) {
				player.getDialogue().sendDialogue(player,
						player.getDialogue().getNextDialogueId());
			} else {
				player.getPA().removeAllWindows();
			}
		}
		player.getDH().sendDialogues(player.nextChat > 0 ? player.nextChat : 0, player.nextChat > 0 ? player.talkingNpc : -1);
	}
}