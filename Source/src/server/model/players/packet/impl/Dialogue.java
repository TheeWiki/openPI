package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;

public class Dialogue implements PacketType {

	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		if (c.getDialogue() != null) {
			if (c.getDialogue().getNextDialogueId() > 0) {
				c.getDialogue().sendDialogue(c,
						c.getDialogue().getNextDialogueId());
			} else {
				c.getPA().removeAllWindows();
			}
		}
		c.getDH().sendDialogues(c.nextChat > 0 ? c.nextChat : 0, c.nextChat > 0 ? c.talkingNpc : -1);
	}
}