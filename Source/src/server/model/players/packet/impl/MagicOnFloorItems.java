package server.model.players.packet.impl;
import server.Server;
import server.model.players.Player;
import server.model.players.packet.PacketType;
import server.model.players.skills.magic.Enchantment;


/**
 * Magic on floor items
 **/
public class MagicOnFloorItems implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		int itemY = player.getInStream().readSignedWordBigEndian();
		int itemId = player.getInStream().readUnsignedWord();
		int itemX = player.getInStream().readSignedWordBigEndian();
		@SuppressWarnings("unused")
		int spellId = player.getInStream().readUnsignedWordA();

		if(!Server.itemHandler.itemExists(itemId, itemX, itemY)) {
			player.stopMovement();
			return;
		}
		player.usingMagic = true;
		if(!player.getCombat().checkMagicReqs(51)) {
			player.stopMovement();
			return;
		}
		
		if(player.goodDistance(player.getX(), player.getY(), itemX, itemY, 12)) {
			int offY = (player.getX() - itemX) * -1;
			int offX = (player.getY() - itemY) * -1;
			player.teleGrabX = itemX;
			player.teleGrabY = itemY;
			player.teleGrabItem = itemId;
			player.turnPlayerTo(itemX, itemY);
			player.teleGrabDelay = System.currentTimeMillis();
			player.startAnimation(Enchantment.MAGIC_SPELLS[51][2]);
			player.gfx100(Enchantment.MAGIC_SPELLS[51][3]);
			player.getPA().createPlayersStillGfx(144, itemX, itemY, 0, 72);
			player.getPA().createPlayersProjectile(player.getX(), player.getY(), offX, offY, 50, 70, Enchantment.MAGIC_SPELLS[51][4], 50, 10, 0, 50);
			player.getPA().addSkillXP(Enchantment.MAGIC_SPELLS[51][7], 6);
			player.getPA().refreshSkill(6);
			player.stopMovement();
		}
	}

}
