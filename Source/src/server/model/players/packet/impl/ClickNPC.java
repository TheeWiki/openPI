package server.model.players.packet.impl;

import server.Constants;
import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.npcs.NPCHandler;
import server.model.players.Player;
import server.model.players.EquipmentListener;
import server.model.players.packet.PacketType;
import server.model.players.skills.magic.Enchantment;

/**
 * Click NPC
 */
public class ClickNPC implements PacketType {

	public static final int ATTACK_NPC = 72, MAGE_NPC = 131, FIRST_CLICK = 155, SECOND_CLICK = 17, THIRD_CLICK = 21;

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(final Player player, int packetType, int packetSize) {
		player.npcIndex = 0;
		player.npcClickIndex = 0;
		player.playerIndex = 0;
		player.clickNpcType = 0;
		player.getPA().resetFollow();
		switch (packetType) {

		/**
		 * Attack npc melee or range
		 **/
		case ATTACK_NPC:
			if (!player.mageAllowed) {
				player.mageAllowed = true;
				player.getActionSender().sendMessage("I can't reach that.");
				break;
			}
			player.npcIndex = player.getInStream().readUnsignedWordA();
			if (Server.npcHandler.npcs[player.npcIndex] == null) {
				player.npcIndex = 0;
				break;
			}
			if (Server.npcHandler.npcs[player.npcIndex].MaxHP == 0) {
				player.npcIndex = 0;
				break;
			}
			if (Server.npcHandler.npcs[player.npcIndex] == null) {
				break;
			}
			if (player.autocastId > 0)
				player.autocasting = true;
			if (!player.autocasting && player.spellId > 0) {
				player.spellId = 0;
			}
			player.faceUpdate(player.npcIndex);
			player.usingMagic = false;
			boolean usingBow = false;
			boolean usingOtherRangeWeapons = false;
			boolean usingArrows = false;
			boolean usingCross = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 9185;
			if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] >= 4214
					&& player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] <= 4223)
				usingBow = true;
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
			if ((usingBow || player.autocasting) && player.goodDistance(player.getX(), player.getY(),
					Server.npcHandler.npcs[player.npcIndex].getX(), Server.npcHandler.npcs[player.npcIndex].getY(), 7)) {
				player.stopMovement();
			}

			if (usingOtherRangeWeapons && player.goodDistance(player.getX(), player.getY(), Server.npcHandler.npcs[player.npcIndex].getX(),
					Server.npcHandler.npcs[player.npcIndex].getY(), 4)) {
				player.stopMovement();
			}
			if (!usingCross && !usingArrows && usingBow
					&& player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] < 4212
					&& player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] > 4223 && !usingCross) {
				player.getActionSender().sendMessage("You have run out of arrows!");
				break;
			}
			if (player.getCombat().correctBowAndArrows() < player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()]
					&& Constants.CORRECT_ARROWS && usingBow && !player.getCombat().usingCrystalBow()
					&& player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] != 9185) {
				player.getActionSender().sendMessage("You can't use "
						+ player.getItems().getItemName(player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()])
								.toLowerCase()
						+ "s with a " + player.getItems()
								.getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase()
						+ ".");
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

			if (player.followId > 0) {
				player.getPA().resetFollow();
			}
			if (player.attackTimer <= 0) {
				player.getCombat().attackNpc(player.npcIndex);
				player.attackTimer++;
			}

			break;

		/**
		 * Attack npc with magic
		 **/
		case MAGE_NPC:
			if (!player.mageAllowed) {
				player.mageAllowed = true;
				player.getActionSender().sendMessage("I can't reach that.");
				break;
			}
			// c.usingSpecial = false;
			// c.getItems().updateSpecialBar();

			player.npcIndex = player.getInStream().readSignedWordBigEndianA();
			int castingSpellId = player.getInStream().readSignedWordA();
			player.usingMagic = false;

			if (Server.npcHandler.npcs[player.npcIndex] == null) {
				break;
			}

			if (Server.npcHandler.npcs[player.npcIndex].MaxHP == 0 || Server.npcHandler.npcs[player.npcIndex].npcType == 944) {
				player.getActionSender().sendMessage("You can't attack this npc.");
				break;
			}

			for (int i = 0; i < Enchantment.MAGIC_SPELLS.length; i++) {
				if (castingSpellId == Enchantment.MAGIC_SPELLS[i][0]) {
					player.spellId = i;
					player.usingMagic = true;
					break;
				}
			}
			if (castingSpellId == 1171) { // crumble undead
				for (int npc : Constants.UNDEAD_NPCS) {
					if (Server.npcHandler.npcs[player.npcIndex].npcType != npc) {
						player.getActionSender().sendMessage("You can only attack undead monsters with this spell.");
						player.usingMagic = false;
						player.stopMovement();
						break;
					}
				}
			}
			/*
			 * if(!c.getCombat().checkMagicReqs(c.spellId)) { c.stopMovement();
			 * break; }
			 */

			if (player.autocasting)
				player.autocasting = false;

			if (player.usingMagic) {
				if (player.goodDistance(player.getX(), player.getY(), Server.npcHandler.npcs[player.npcIndex].getX(),
						Server.npcHandler.npcs[player.npcIndex].getY(), 6)) {
					player.stopMovement();
				}
				if (player.attackTimer <= 0) {
					player.getCombat().attackNpc(player.npcIndex);
					player.attackTimer++;
				}
			}

			break;

		case FIRST_CLICK:
			player.npcClickIndex = player.inStream.readSignedWordBigEndian();
			player.npcType = Server.npcHandler.npcs[player.npcClickIndex].npcType;
			if (player.goodDistance(Server.npcHandler.npcs[player.npcClickIndex].getX(),
					Server.npcHandler.npcs[player.npcClickIndex].getY(), player.getX(), player.getY(), 1)) {
				player.turnPlayerTo(Server.npcHandler.npcs[player.npcClickIndex].getX(),
						Server.npcHandler.npcs[player.npcClickIndex].getY());
				Server.npcHandler.npcs[player.npcClickIndex].facePlayer(player.playerId);
				player.getActions().firstClickNpc(player, player.npcType);
			} else {
				player.clickNpcType = 1;
				CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if ((player.clickNpcType == 1) && NPCHandler.npcs[player.npcClickIndex] != null) {
							if (player.goodDistance(player.getX(), player.getY(), NPCHandler.npcs[player.npcClickIndex].getX(),
									NPCHandler.npcs[player.npcClickIndex].getY(), 1)) {
								player.turnPlayerTo(NPCHandler.npcs[player.npcClickIndex].getX(),
										NPCHandler.npcs[player.npcClickIndex].getY());
								NPCHandler.npcs[player.npcClickIndex].facePlayer(player.playerId);
								player.getActions().firstClickNpc(player, player.npcType);
								container.stop();
							}
						}
						if (player.clickNpcType == 0 || player.clickNpcType > 1)
							container.stop();
					}

					@Override
					public void stop() {
						player.clickNpcType = 0;
					}
				}, 1);
			}
			break;

		case SECOND_CLICK:
			player.npcClickIndex = player.inStream.readUnsignedWordBigEndianA();
			player.npcType = Server.npcHandler.npcs[player.npcClickIndex].npcType;
			if (player.goodDistance(Server.npcHandler.npcs[player.npcClickIndex].getX(),
					Server.npcHandler.npcs[player.npcClickIndex].getY(), player.getX(), player.getY(), 1)) {
				player.turnPlayerTo(Server.npcHandler.npcs[player.npcClickIndex].getX(),
						Server.npcHandler.npcs[player.npcClickIndex].getY());
				Server.npcHandler.npcs[player.npcClickIndex].facePlayer(player.playerId);
				player.getActions().secondClickNpc(player, player.npcType);
			} else {
				player.clickNpcType = 2;
				CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if ((player.clickNpcType == 2) && NPCHandler.npcs[player.npcClickIndex] != null) {
							if (player.goodDistance(player.getX(), player.getY(), NPCHandler.npcs[player.npcClickIndex].getX(),
									NPCHandler.npcs[player.npcClickIndex].getY(), 1)) {
								player.turnPlayerTo(NPCHandler.npcs[player.npcClickIndex].getX(),
										NPCHandler.npcs[player.npcClickIndex].getY());
								NPCHandler.npcs[player.npcClickIndex].facePlayer(player.playerId);
								player.getActions().secondClickNpc(player, player.npcType);
								container.stop();
							}
						}
						if (player.clickNpcType < 2 || player.clickNpcType > 2)
							container.stop();
					}

					@Override
					public void stop() {
						player.clickNpcType = 0;
					}
				}, 1);
			}
			break;

		case THIRD_CLICK:
			player.npcClickIndex = player.inStream.readSignedWord();
			player.npcType = Server.npcHandler.npcs[player.npcClickIndex].npcType;
			if (player.goodDistance(Server.npcHandler.npcs[player.npcClickIndex].getX(),
					Server.npcHandler.npcs[player.npcClickIndex].getY(), player.getX(), player.getY(), 1)) {
				player.turnPlayerTo(Server.npcHandler.npcs[player.npcClickIndex].getX(),
						Server.npcHandler.npcs[player.npcClickIndex].getY());
				Server.npcHandler.npcs[player.npcClickIndex].facePlayer(player.playerId);
				player.getActions().thirdClickNpc(player, player.npcType);
			} else {
				player.clickNpcType = 3;
				CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if ((player.clickNpcType == 3) && NPCHandler.npcs[player.npcClickIndex] != null) {
							if (player.goodDistance(player.getX(), player.getY(), NPCHandler.npcs[player.npcClickIndex].getX(),
									NPCHandler.npcs[player.npcClickIndex].getY(), 1)) {
								player.turnPlayerTo(NPCHandler.npcs[player.npcClickIndex].getX(),
										NPCHandler.npcs[player.npcClickIndex].getY());
								NPCHandler.npcs[player.npcClickIndex].facePlayer(player.playerId);
								player.getActions().thirdClickNpc(player, player.npcType);
								container.stop();
							}
						}
						if (player.clickNpcType < 3)
							container.stop();
					}

					@Override
					public void stop() {
						player.clickNpcType = 0;
					}
				}, 1);
			}
			break;
		}

	}
}
