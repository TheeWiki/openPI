package server.model.players.packet.impl;

import server.Constants;
import server.Server;
import server.model.minigames.duel_arena.Rules;
import server.model.players.Player;
import server.model.players.EquipmentListener;
import server.model.players.packet.PacketType;
import server.model.players.skills.magic.Enchantment;

/**
 * Attack Player
 **/
public class AttackPlayer implements PacketType {

	public static final int ATTACK_PLAYER = 73, MAGE_PLAYER = 249;

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		player.playerIndex = 0;
		player.npcIndex = 0;
		switch (packetType) {

		/**
		 * Attack player
		 **/
		case ATTACK_PLAYER:
			player.playerIndex = player.getInStream().readSignedWordBigEndian();
			if (Server.playerHandler.players[player.playerIndex] == null) {
				break;
			}

			if (player.respawnTimer > 0) {
				break;
			}

			if (player.autocastId > 0)
				player.autocasting = true;

			if (!player.autocasting && player.spellId > 0) {
				player.spellId = 0;
			}
			player.mageFollow = false;
			player.spellId = 0;
			player.usingMagic = false;
			boolean usingBow = false;
			boolean usingOtherRangeWeapons = false;
			boolean usingArrows = false;
			boolean usingCross = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 9185;
			for (int bowId : player.BOWS) {
				if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == bowId) {
					usingBow = true;
					for (int arrowId : player.ARROWS) {
						if (player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] == arrowId) {
							usingArrows = true;
						}
					}
				}
			}
			for (int otherRangeId : player.OTHER_RANGE_WEAPONS) {
				if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == otherRangeId) {
					usingOtherRangeWeapons = true;
				}
			}
			if (player.duelStatus == 5) {
				if (player.duelCount > 0) {
					player.getActionSender().sendMessage("The duel hasn't started yet!");
					player.playerIndex = 0;
					return;
				}
				if (player.duelRule[Rules.FUN_WEAPONS_RULE.getRule()]) {
					boolean canUseWeapon = false;
					for (int funWeapon : Constants.FUN_WEAPONS) {
						if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == funWeapon) {
							canUseWeapon = true;
						}
					}
					if (!canUseWeapon) {
						player.getActionSender().sendMessage("You can only use fun weapons in this duel!");
						return;
					}
				}

				if (player.duelRule[Rules.RANGE_RULE.getRule()] && (usingBow || usingOtherRangeWeapons)) {
					player.getActionSender().sendMessage("Range has been disabled in this duel!");
					return;
				}
				if (player.duelRule[Rules.MELEE_RULE.getRule()] && (!usingBow && !usingOtherRangeWeapons)) {
					player.getActionSender().sendMessage("Melee has been disabled in this duel!");
					return;
				}
			}

			if ((usingBow || player.autocasting)
					&& player.goodDistance(player.getX(), player.getY(), Server.playerHandler.players[player.playerIndex].getX(),
							Server.playerHandler.players[player.playerIndex].getY(), 6)) {
				player.usingBow = true;
				player.stopMovement();
			}

			if (usingOtherRangeWeapons
					&& player.goodDistance(player.getX(), player.getY(), Server.playerHandler.players[player.playerIndex].getX(),
							Server.playerHandler.players[player.playerIndex].getY(), 3)) {
				player.usingRangeWeapon = true;
				player.stopMovement();
			}
			if (!usingBow)
				player.usingBow = false;
			if (!usingOtherRangeWeapons)
				player.usingRangeWeapon = false;

			if (!usingCross && !usingArrows && usingBow && player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] < 4212
					&& player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] > 4223) {
				player.getActionSender().sendMessage("You have run out of arrows!");
				return;
			}
			if (player.getCombat().correctBowAndArrows() < player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] && Constants.CORRECT_ARROWS
					&& usingBow && !player.getCombat().usingCrystalBow() && player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] != 9185) {
				player.getActionSender().sendMessage("You can't use "
						+ player.getItems().getItemName(player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()]).toLowerCase() + "s with a "
						+ player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase() + ".");
				player.stopMovement();
				player.getCombat().resetPlayerAttack();
				return;
			}
			if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 9185 && !player.getCombat().properBolts()) {
				player.getActionSender().sendMessage("You must use bolts with a crossbow.");
				player.stopMovement();
				player.getCombat().resetPlayerAttack();
				return;
			}
			if (player.getCombat().checkReqs()) {
				player.followId = player.playerIndex;
				if (!player.usingMagic && !usingBow && !usingOtherRangeWeapons) {
					player.followDistance = 1;
					player.getPA().followPlayer();
				}
				if (player.attackTimer <= 0) {
					// c.sendMessage("Tried to attack...");
					// c.getCombat().attackPlayer(c.playerIndex);
					// c.attackTimer++;
				}
			}
			break;

		/**
		 * Attack player with magic
		 **/
		case MAGE_PLAYER:
			if (!player.mageAllowed) {
				player.mageAllowed = true;
				break;
			}
			// c.usingSpecial = false;
			// c.getItems().updateSpecialBar();

			player.playerIndex = player.getInStream().readSignedWordA();
			int castingSpellId = player.getInStream().readSignedWordBigEndian();
			player.usingMagic = false;
			if (Server.playerHandler.players[player.playerIndex] == null) {
				break;
			}

			if (player.respawnTimer > 0) {
				break;
			}

			for (int i = 0; i < Enchantment.MAGIC_SPELLS.length; i++) {
				if (castingSpellId == Enchantment.MAGIC_SPELLS[i][0]) {
					player.spellId = i;
					player.usingMagic = true;
					break;
				}
			}

			if (player.autocasting)
				player.autocasting = false;

			if (!player.getCombat().checkReqs()) {
				break;
			}
			if (player.duelStatus == 5) {
				if (player.duelCount > 0) {
					player.getActionSender().sendMessage("The duel hasn't started yet!");
					player.playerIndex = 0;
					return;
				}
				if (player.duelRule[Rules.MAGIC_RULE.getRule()]) {
					player.getActionSender().sendMessage("Magic has been disabled in this duel!");
					return;
				}
			}

			for (int r = 0; r < player.REDUCE_SPELLS.length; r++) { // reducing
																// spells,
																// confuse etc
				if (Server.playerHandler.players[player.playerIndex].REDUCE_SPELLS[r] == Enchantment.MAGIC_SPELLS[player.spellId][0]) {
					if ((System.currentTimeMillis()
							- Server.playerHandler.players[player.playerIndex].reduceSpellDelay[r]) < Server.playerHandler.players[player.playerIndex].REDUCE_SPELL_TIME[r]) {
						player.getActionSender().sendMessage("That player is currently immune to this spell.");
						player.usingMagic = false;
						player.stopMovement();
						player.getCombat().resetPlayerAttack();
					}
					break;
				}
			}

			if (System.currentTimeMillis()
					- Server.playerHandler.players[player.playerIndex].teleBlockDelay < Server.playerHandler.players[player.playerIndex].teleBlockLength
					&& Enchantment.MAGIC_SPELLS[player.spellId][0] == 12445) {
				player.getActionSender().sendMessage("That player is already affected by this spell.");
				player.usingMagic = false;
				player.stopMovement();
				player.getCombat().resetPlayerAttack();
			}

			if (!player.getCombat().checkMagicReqs(player.spellId)) {
				player.stopMovement();
				player.getCombat().resetPlayerAttack();
				break;
			}

			if (player.usingMagic) {
				if (player.goodDistance(player.getX(), player.getY(), Server.playerHandler.players[player.playerIndex].getX(),
						Server.playerHandler.players[player.playerIndex].getY(), 7)) {
					player.stopMovement();
				}
				if (player.getCombat().checkReqs()) {
					player.followId = player.playerIndex;
					player.mageFollow = true;
					if (player.attackTimer <= 0) {
						player.getCombat().attackPlayer(player.playerIndex);
						player.attackTimer++;
					}
				}
			}
			break;
		}
	}
}