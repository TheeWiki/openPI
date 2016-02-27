package server.model.players.combat;

import server.Constants;
import server.Server;
import server.model.items.ItemDegrading;
import server.model.minigames.castle_wars.CastleWars;
import server.model.minigames.duel_arena.Rules;
import server.model.npcs.NPC;
import server.model.npcs.NPCHandler;
import server.model.players.Player;
import server.model.players.EquipmentListener;
import server.model.players.Player;
import server.model.players.PlayerHandler;
import server.model.players.packet.impl.DropItem;
import server.model.players.skills.SkillIndex;
import server.model.players.skills.magic.Enchantment;
import server.util.Misc;

@SuppressWarnings("all")
public class CombatAssistant {

	private Player player;

	public CombatAssistant(Player Player) {
		this.player = Player;
	}

	/**
	 * Attack Npcs
	 */
	public void attackNpc(int i) {
		if (NPCHandler.npcs[i] != null) {
			if (NPCHandler.npcs[i].isDead || NPCHandler.npcs[i].MaxHP <= 0) {
				player.usingMagic = false;
				player.faceUpdate(0);
				player.npcIndex = 0;
				return;
			}
			if (player.respawnTimer > 0) {
				player.npcIndex = 0;
				return;
			}
			if (NPCHandler.npcs[i].underAttackBy > 0 && NPCHandler.npcs[i].underAttackBy != player.playerId
					&& !NPCHandler.npcs[i].inMulti()) {
				player.npcIndex = 0;
				player.getActionSender().sendMessage("This monster is already in combat.");
				return;
			}
			if ((player.underAttackBy > 0 || player.underAttackBy2 > 0) && player.underAttackBy2 != i && !player.inMulti()) {
				resetPlayerAttack();
				player.getActionSender().sendMessage("I am already under attack.");
				return;
			}
			if (NPCHandler.npcs[i].spawnedBy != player.playerId && NPCHandler.npcs[i].spawnedBy > 0) {
				resetPlayerAttack();
				player.getActionSender().sendMessage("This monster was not spawned for you.");
				return;
			}
			player.followId2 = i;
			player.followId = 0;
			if (player.attackTimer <= 0) {
				boolean usingBow = false;
				boolean usingArrows = false;
				boolean usingOtherRangeWeapons = false;
				boolean usingCross = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 9185;
				player.bonusAttack = 0;
				player.rangeItemUsed = 0;
				player.projectileStage = 0;
				if (player.autocasting) {
					player.spellId = player.autocastId;
					player.usingMagic = true;
				}
				if (player.spellId > 0) {
					player.usingMagic = true;
				}
				player.attackTimer = getAttackDelay(
						player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
				player.specAccuracy = 1.0;
				player.specDamage = 1.0;

				if (!player.usingMagic) {
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
				}
				if (armaNpc(i) && !usingCross && !usingBow && !player.usingMagic && !usingCrystalBow()
						&& !usingOtherRangeWeapons) {
					resetPlayerAttack();
					return;
				}
				if ((!player.goodDistance(player.getX(), player.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 2)
						&& (usingHally() && !usingOtherRangeWeapons && !usingBow && !player.usingMagic))
						|| (!player.goodDistance(player.getX(), player.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 4)
								&& (usingOtherRangeWeapons && !usingBow && !player.usingMagic))
						|| (!player.goodDistance(player.getX(), player.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 1)
								&& (!usingOtherRangeWeapons && !usingHally() && !usingBow && !player.usingMagic))
						|| ((!player.goodDistance(player.getX(), player.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(),
								8) && (usingBow || player.usingMagic)))) {
					player.attackTimer = 2;
					return;
				}

				if (!usingCross && !usingArrows && usingBow
						&& (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] < 4212 || player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] > 4223)) {
					player.getActionSender().sendMessage("You have run out of arrows!");
					player.stopMovement();
					player.npcIndex = 0;
					return;
				}
				if (correctBowAndArrows() < player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] && Constants.CORRECT_ARROWS && usingBow
						&& !usingCrystalBow() && player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] != 9185) {
					player.getActionSender().sendMessage("You can't use "
							+ player.getItems().getItemName(player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()]).toLowerCase() + "s with a "
							+ player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase() + ".");
					player.stopMovement();
					player.npcIndex = 0;
					return;
				}

				if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 9185 && !properBolts()) {
					player.getActionSender().sendMessage("You must use bolts with a crossbow.");
					player.stopMovement();
					resetPlayerAttack();
					return;
				}

				if (usingBow || player.usingMagic || usingOtherRangeWeapons
						|| (player.goodDistance(player.getX(), player.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 2)
								&& usingHally())) {
					player.stopMovement();
				}

				if (!checkMagicReqs(player.spellId)) {
					player.stopMovement();
					player.npcIndex = 0;
					return;
				}

				player.faceUpdate(i);
				// c.specAccuracy = 1.0;
				// c.specDamage = 1.0;
				NPCHandler.npcs[i].underAttackBy = player.playerId;
				NPCHandler.npcs[i].lastDamageTaken = System.currentTimeMillis();
				if (player.usingSpecial && !player.usingMagic) {
					if (checkSpecAmount(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()])) {
						player.lastWeaponUsed = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()];
						player.lastArrowUsed = player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()];
						activateSpecial(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()], i);
						return;
					} else {
						player.getActionSender().sendMessage("You don't have the required special energy to use this attack.");
						player.usingSpecial = false;
						player.getItems().updateSpecialBar();
						player.npcIndex = 0;
						return;
					}
				}
				player.specMaxHitIncrease = 0;
				if (!player.usingMagic) {
					player.startAnimation(
							getWepAnim(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase()));
				} else {
					player.startAnimation(Enchantment.MAGIC_SPELLS[player.spellId][2]);
				}
				player.lastWeaponUsed = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()];
				player.lastArrowUsed = player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()];
				if (!usingBow && !player.usingMagic && !usingOtherRangeWeapons) { // melee
																				// hit
																				// delay
					player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
					player.projectileStage = 0;
					player.oldNpcIndex = i;
				}

				if (usingBow && !usingOtherRangeWeapons && !player.usingMagic || usingCross) { // range
																							// hit
																							// delay
					if (usingCross)
						player.usingBow = true;
					if (player.fightMode == 2)
						player.attackTimer--;
					player.lastArrowUsed = player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()];
					player.lastWeaponUsed = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()];
					player.gfx100(getRangeStartGFX());
					player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
					player.projectileStage = 1;
					player.oldNpcIndex = i;
					if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] >= 4212 && player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] <= 4223) {
						player.rangeItemUsed = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()];
						player.crystalBowArrowCount++;
						player.lastArrowUsed = 0;
					} else {
						player.rangeItemUsed = player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()];
						player.getItems().deleteArrow();
					}
					fireProjectileNpc();
				}

				if (usingOtherRangeWeapons && !player.usingMagic && !usingBow) { // knives,
																			// darts,
																			// etc
																			// hit
																			// delay
					player.rangeItemUsed = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()];
					player.getItems().deleteEquipment();
					player.gfx100(getRangeStartGFX());
					player.lastArrowUsed = 0;
					player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
					player.projectileStage = 1;
					player.oldNpcIndex = i;
					if (player.fightMode == 2)
						player.attackTimer--;
					fireProjectileNpc();
				}

				if (player.usingMagic) { // magic hit delay
					int pX = player.getX();
					int pY = player.getY();
					int nX = NPCHandler.npcs[i].getX();
					int nY = NPCHandler.npcs[i].getY();
					int offX = (pY - nY) * -1;
					int offY = (pX - nX) * -1;
					player.castingMagic = true;
					player.projectileStage = 2;
					if (Enchantment.MAGIC_SPELLS[player.spellId][3] > 0) {
						if (getStartGfxHeight() == 100) {
							player.gfx100(Enchantment.MAGIC_SPELLS[player.spellId][3]);
						} else {
							player.gfx0(Enchantment.MAGIC_SPELLS[player.spellId][3]);
						}
					}
					if (Enchantment.MAGIC_SPELLS[player.spellId][4] > 0) {
						player.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, Enchantment.MAGIC_SPELLS[player.spellId][4],
								getStartHeight(), getEndHeight(), i + 1, 50);
					}
					player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
					player.oldNpcIndex = i;
					player.oldSpellId = player.spellId;
					player.spellId = 0;
					if (!player.autocasting)
						player.npcIndex = 0;
				}
				if (usingBow = true && player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 5641)
				{
					
			        int damage = Integer.MAX_VALUE;
			        player.getPA().addSkillXP(SkillIndex.RANGE.getExpRatio() * 2.4, SkillIndex.RANGE.getSkillId());
			        NPCHandler.npcs[i].dealDamage(damage);
			        NPCHandler.npcs[i].killerId = player.getId();
			        NPCHandler.npcs[i].facePlayer(player.getId()); //might need to get player id if it doesnt face automatically == Objects.belongsTo = player.playerName;
			        NPCHandler.npcs[i].hitDiff = damage;
			        NPCHandler.npcs[i].HP -= damage;
			        NPCHandler.npcs[i].hitUpdateRequired = true;
//					if (NPCHandler.npcs[i].HP <= 0) {
//						NPCHandler.npcs[i].isDead = true;
//					}
			        player.getItems().wearItem(5641, 1, 3);
				}
				if (usingBow && Constants.CRYSTAL_BOW_DEGRADES) { // crystal bow
																	// degrading
					if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4212) { // new
																		// crystal
																		// bow
																		// becomes
																		// full
																		// bow
																		// on
																		// the
																		// first
																		// shot
						player.getItems().wearItem(4214, 1, 3);
					}

					if (player.crystalBowArrowCount >= 250) {
						switch (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]) {

						case 4223: // 1/10 bow
							player.getItems().wearItem(-1, 1, 3);
							player.getActionSender().sendMessage("Your crystal bow has fully degraded.");
							if (!player.getItems().addItem(4207, 1)) {
								Server.itemHandler.createGroundItem(player, 4207, player.getX(), player.getY(), 1, player.getId());
							}
							player.crystalBowArrowCount = 0;
							break;

						default:
							player.getItems().wearItem(++player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()], 1, 3);
							player.getActionSender().sendMessage("Your crystal bow degrades.");
							player.crystalBowArrowCount = 0;
							break;

						}
					}
				}
			}
		}
	}

	public void delayedHit(int i) { // npc hit delay
		if (NPCHandler.npcs[i] != null) {
			if (NPCHandler.npcs[i].isDead) {
				player.npcIndex = 0;
				return;
			}
			NPCHandler.npcs[i].facePlayer(player.playerId);

			if (NPCHandler.npcs[i].underAttackBy > 0 && Server.npcHandler.getsPulled(i)) {
				NPCHandler.npcs[i].killerId = player.playerId;
			} else if (NPCHandler.npcs[i].underAttackBy < 0 && !Server.npcHandler.getsPulled(i)) {
				NPCHandler.npcs[i].killerId = player.playerId;
			}
			player.lastNpcAttacked = i;
			if (player.projectileStage == 0) { // melee hit damage
				applyNpcMeleeDamage(i, 1);
				if (player.doubleHit) {
					applyNpcMeleeDamage(i, 2);
				}
			}

			if (!player.castingMagic && player.projectileStage > 0) { // range hit damage
				int damage = Misc.random(rangeMaxHit());
				int damage2 = -1;
				if (player.lastWeaponUsed == 11235 || player.bowSpecShot == 1)
					damage2 = Misc.random(rangeMaxHit());
				boolean ignoreDef = false;
				if (Misc.random(5) == 1 && player.lastArrowUsed == 9243) {
					ignoreDef = true;
					NPCHandler.npcs[i].gfx0(758);
				}

				if (Misc.random(NPCHandler.npcs[i].defence) > Misc.random(10 + calculateRangeAttack()) && !ignoreDef) {
					damage = 0;
				} else if (NPCHandler.npcs[i].npcType == 2881 || NPCHandler.npcs[i].npcType == 2883 && !ignoreDef) {
					damage = 0;
				}

				if (Misc.random(4) == 1 && player.lastArrowUsed == 9242 && damage > 0) {
					NPCHandler.npcs[i].gfx0(754);
					damage = NPCHandler.npcs[i].HP / 5;
					player.handleHitMask(player.playerLevel[3] / 10);
					player.dealDamage(player.playerLevel[3] / 10);
					player.gfx0(754);
				}

				if (player.lastWeaponUsed == 11235 || player.bowSpecShot == 1) {
					if (Misc.random(NPCHandler.npcs[i].defence) > Misc.random(10 + calculateRangeAttack()))
						damage2 = 0;
				}
				if (player.dbowSpec) {
					NPCHandler.npcs[i].gfx100(1100);
					if (damage < 8)
						damage = 8;
					if (damage2 < 8)
						damage2 = 8;
					player.dbowSpec = false;
				}
				if (damage > 0 && Misc.random(5) == 1 && player.lastArrowUsed == 9244) {
					damage *= 1.45;
					NPCHandler.npcs[i].gfx0(756);
				}

				if (NPCHandler.npcs[i].HP - damage < 0) {
					damage = NPCHandler.npcs[i].HP;
				}
				if (NPCHandler.npcs[i].HP - damage <= 0 && damage2 > 0) {
					damage2 = 0;
				}
				if (player.fightMode == 3) {
					player.getPA().addSkillXP((damage * SkillIndex.RANGE.getExpRatio() / 3), SkillIndex.RANGE.getSkillId());
					player.getPA().addSkillXP((damage * SkillIndex.RANGE.getExpRatio() / 3), SkillIndex.DEFENCE.getSkillId());
					player.getPA().addSkillXP((damage * SkillIndex.RANGE.getExpRatio() / 3), SkillIndex.HITPOINTS.getSkillId());
					player.getPA().refreshSkill(1);
					player.getPA().refreshSkill(3);
					player.getPA().refreshSkill(4);
				} else {
					player.getPA().addSkillXP((damage * SkillIndex.RANGE.getExpRatio()), SkillIndex.RANGE.getSkillId());
					player.getPA().addSkillXP((damage * SkillIndex.RANGE.getExpRatio() / 3), SkillIndex.HITPOINTS.getSkillId());
					player.getPA().refreshSkill(3);
					player.getPA().refreshSkill(4);
				}
				if (damage > 0) {

				}
				boolean dropArrows = true;

				for (int noArrowId : player.NO_ARROW_DROP) {
					if (player.lastWeaponUsed == noArrowId) {
						dropArrows = false;
						break;
					}
				}
				if (dropArrows) {
					player.getItems().dropArrowNpc();
				}
				NPCHandler.npcs[i].underAttack = true;
				NPCHandler.npcs[i].hitDiff = damage;
				NPCHandler.npcs[i].HP -= damage;
				if (damage2 > -1) {
					NPCHandler.npcs[i].hitDiff2 = damage2;
					NPCHandler.npcs[i].HP -= damage2;
					player.totalDamageDealt += damage2;
				}
				if (player.killingNpcIndex != player.oldNpcIndex) {
					player.totalDamageDealt = 0;
				}
				player.killingNpcIndex = player.oldNpcIndex;
				player.totalDamageDealt += damage;
				NPCHandler.npcs[i].hitUpdateRequired = true;
				if (damage2 > -1)
					NPCHandler.npcs[i].hitUpdateRequired2 = true;
				NPCHandler.npcs[i].updateRequired = true;

			} else if (player.projectileStage > 0) { // magic hit damage
				int damage = Misc.random(Enchantment.MAGIC_SPELLS[player.oldSpellId][6]);
				if (godSpells()) {
					if (System.currentTimeMillis() - player.godSpellDelay < Constants.GOD_SPELL_CHARGE) {
						damage += Misc.random(10);
					}
				}
				boolean magicFailed = false;
				// c.npcIndex = 0;
				int bonusAttack = getBonusAttack(i);
				if (Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(mageAtk()) + bonusAttack) {
					damage = 0;
					magicFailed = true;
				} else if (NPCHandler.npcs[i].npcType == 2881 || NPCHandler.npcs[i].npcType == 2882) {

					damage = 0;
					magicFailed = true;
				}
				for (int j = 0; j < Server.npcHandler.npcs.length; j++) {
					if (Server.npcHandler.npcs[j] != null && Server.npcHandler.npcs[j].MaxHP > 0) {
						int nX = Server.npcHandler.npcs[j].getX();
						int nY = Server.npcHandler.npcs[j].getY();
						int pX = Server.npcHandler.npcs[i].getX();
						int pY = Server.npcHandler.npcs[i].getY();
						if ((nX - pX == -1 || nX - pX == 0 || nX - pX == 1)
								&& (nY - pY == -1 || nY - pY == 0 || nY - pY == 1)) {
							if (multis() && Server.npcHandler.npcs[i].inMulti()) {
								Player p = (Player) Server.playerHandler.players[player.playerId];
								appendMultiBarrageNPC(j, player.magicFailed);
								Server.npcHandler.attackPlayer(p, j);
							}
						}
					}
				}
				if (NPCHandler.npcs[i].HP - damage < 0) {
					damage = NPCHandler.npcs[i].HP;
				}

				player.getPA().addSkillXP((Enchantment.MAGIC_SPELLS[player.oldSpellId][7] + damage * SkillIndex.MAGIC.getExpRatio()), SkillIndex.MAGIC.getSkillId());
				player.getPA().addSkillXP((Enchantment.MAGIC_SPELLS[player.oldSpellId][7] + damage * SkillIndex.MAGIC.getExpRatio() / 3), SkillIndex.HITPOINTS.getSkillId());
				player.getPA().refreshSkill(3);
				player.getPA().refreshSkill(6);
				if (damage > 0) {

				}
				if (getEndGfxHeight() == 100 && !magicFailed) { // end GFX
					NPCHandler.npcs[i].gfx100(Enchantment.MAGIC_SPELLS[player.oldSpellId][5]);
				} else if (!magicFailed) {
					NPCHandler.npcs[i].gfx0(Enchantment.MAGIC_SPELLS[player.oldSpellId][5]);
				}

				if (magicFailed) {
					NPCHandler.npcs[i].gfx100(85);
				}
				if (!magicFailed) {
					int freezeDelay = getFreezeTime();// freeze
					if (freezeDelay > 0 && NPCHandler.npcs[i].freezeTimer == 0) {
						NPCHandler.npcs[i].freezeTimer = freezeDelay;
					}
					switch (Enchantment.MAGIC_SPELLS[player.oldSpellId][0]) {
					case 12901:
					case 12919: // blood spells
					case 12911:
					case 12929:
						int heal = Misc.random(damage / 2);
						if (player.playerLevel[3] + heal >= player.getPA().getLevelForXP(player.playerXP[3])) {
							player.playerLevel[3] = player.getPA().getLevelForXP(player.playerXP[3]);
						} else {
							player.playerLevel[3] += heal;
						}
						player.getPA().refreshSkill(3);
						break;
					}

				}
				NPCHandler.npcs[i].underAttack = true;
				if (Enchantment.MAGIC_SPELLS[player.oldSpellId][6] != 0) {
					NPCHandler.npcs[i].hitDiff = damage;
					NPCHandler.npcs[i].HP -= damage;
					NPCHandler.npcs[i].hitUpdateRequired = true;
					player.totalDamageDealt += damage;
				}
				player.killingNpcIndex = player.oldNpcIndex;
				NPCHandler.npcs[i].updateRequired = true;
				player.usingMagic = false;
				player.castingMagic = false;
				player.oldSpellId = 0;
			}
		}

		if (player.bowSpecShot <= 0) {
			player.oldNpcIndex = 0;
			player.projectileStage = 0;
			player.doubleHit = false;
			player.lastWeaponUsed = 0;
			player.bowSpecShot = 0;
		}
		if (player.bowSpecShot >= 2) {
			player.bowSpecShot = 0;
			// c.attackTimer =
			// getAttackDelay(c.getItems().getItemName(c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
		}
		if (player.bowSpecShot == 1) {
			fireProjectileNpc();
			player.hitDelay = 2;
			player.bowSpecShot = 0;
		}
	}

	public void applyNpcMeleeDamage(int i, int damageMask) {
		int damage = Misc.random(calculateMeleeMaxHit());
		if (NPCHandler.npcs[i].HP - damage < 0) {
			damage = NPCHandler.npcs[i].HP;
		}
		if (player.fightMode == 3) {
			player.getPA().addSkillXP((damage * SkillIndex.ATTACK.getExpRatio() / 3), SkillIndex.ATTACK.getSkillId());
			player.getPA().addSkillXP((damage * SkillIndex.ATTACK.getExpRatio() / 3), SkillIndex.STRENGTH.getSkillId());
			player.getPA().addSkillXP((damage * SkillIndex.ATTACK.getExpRatio() / 3), SkillIndex.DEFENCE.getSkillId());
			player.getPA().addSkillXP((damage * SkillIndex.ATTACK.getExpRatio() / 3), SkillIndex.HITPOINTS.getSkillId());
			player.getPA().refreshSkill(0);
			player.getPA().refreshSkill(1);
			player.getPA().refreshSkill(2);
			player.getPA().refreshSkill(3);
		} else {
			player.getPA().addSkillXP((damage * SkillIndex.ATTACK.getExpRatio()), player.fightMode);
			player.getPA().addSkillXP((damage * SkillIndex.ATTACK.getExpRatio() / 3), SkillIndex.HITPOINTS.getSkillId());
			player.getPA().refreshSkill(player.fightMode);
			player.getPA().refreshSkill(3);
		}
		if (damage > 0) {

		}
		NPCHandler.npcs[i].underAttack = true;
		// Server.npcHandler.npcs[i].killerId = c.playerId;
		player.killingNpcIndex = player.npcIndex;
		player.lastNpcAttacked = i;
		switch (player.specEffect) {
		case 4:
			if (damage > 0) {
				if (player.playerLevel[3] + damage > player.getLevelForXP(player.playerXP[3]))
					if (player.playerLevel[3] > player.getLevelForXP(player.playerXP[3]))
						;
					else
						player.playerLevel[3] = player.getLevelForXP(player.playerXP[3]);
				else
					player.playerLevel[3] += damage;
				player.getPA().refreshSkill(3);
			}
			break;

		}
		switch (damageMask) {
		case 1:
			NPCHandler.npcs[i].hitDiff = damage;
			NPCHandler.npcs[i].HP -= damage;
			player.totalDamageDealt += damage;
			NPCHandler.npcs[i].hitUpdateRequired = true;
			NPCHandler.npcs[i].updateRequired = true;
			break;

		case 2:
			NPCHandler.npcs[i].hitDiff2 = damage;
			NPCHandler.npcs[i].HP -= damage;
			player.totalDamageDealt += damage;
			NPCHandler.npcs[i].hitUpdateRequired2 = true;
			NPCHandler.npcs[i].updateRequired = true;
			player.doubleHit = false;
			break;

		}
	}

	public void fireProjectileNpc() {
		if (player.oldNpcIndex > 0) {
			if (NPCHandler.npcs[player.oldNpcIndex] != null) {
				player.projectileStage = 2;
				int pX = player.getX();
				int pY = player.getY();
				int nX = NPCHandler.npcs[player.oldNpcIndex].getX();
				int nY = NPCHandler.npcs[player.oldNpcIndex].getY();
				int offX = (pY - nY) * -1;
				int offY = (pX - nX) * -1;
				player.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, getProjectileSpeed(), getRangeProjectileGFX(),
						43, 31, player.oldNpcIndex + 1, getStartDelay());
				if (usingDbow())
					player.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, getProjectileSpeed(),
							getRangeProjectileGFX(), 60, 31, player.oldNpcIndex + 1, getStartDelay(), 35);
			}
		}
	}

	/**
	 * Attack Players, same as npc tbh xD
	 **/

	public void attackPlayer(int i) {
		if (CastleWars.isInCw((Player) PlayerHandler.players[i]) && CastleWars.isInCw(player)) {
			if (CastleWars.getTeamNumber(player) == CastleWars.getTeamNumber((Player) PlayerHandler.players[i])) {
				player.getActionSender().sendMessage("You cannot attack your own teammate.");
				return;
			}
		}
		if (PlayerHandler.players[i] != null) {

			if (PlayerHandler.players[i].isDead) {
				resetPlayerAttack();
				return;
			}

			if (player.respawnTimer > 0 || PlayerHandler.players[i].respawnTimer > 0) {
				resetPlayerAttack();
				return;
			}

			/*
			 * if (c.teleTimer > 0 || Server.playerHandler.players[i].teleTimer
			 * > 0) { resetPlayerAttack(); return; }
			 */

			if (!player.getCombat().checkReqs()) {
				return;
			}

//			if (c.getPA().getWearingAmount() < 4 && c.duelStatus < 1) {
//				c.sendMessage("You must be wearing at least 4 items to attack someone.");
//				resetPlayerAttack();
//				return;
//			}
			boolean sameSpot = player.absX == PlayerHandler.players[i].getX() && player.absY == PlayerHandler.players[i].getY();
			if (!player.goodDistance(PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(), player.getX(), player.getY(),
					25) && !sameSpot) {
				resetPlayerAttack();
				return;
			}

			if (PlayerHandler.players[i].respawnTimer > 0) {
				PlayerHandler.players[i].playerIndex = 0;
				resetPlayerAttack();
				return;
			}

			if (PlayerHandler.players[i].heightLevel != player.heightLevel) {
				resetPlayerAttack();
				return;
			}
			// c.sendMessage("Made it here0.");
			player.followId = i;
			player.followId2 = 0;
			if (player.attackTimer <= 0) {
				player.usingBow = false;
				player.specEffect = 0;
				player.usingRangeWeapon = false;
				player.rangeItemUsed = 0;
				boolean usingBow = false;
				boolean usingArrows = false;
				boolean usingOtherRangeWeapons = false;
				boolean usingCross = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 9185;
				player.projectileStage = 0;

				if (player.absX == PlayerHandler.players[i].absX && player.absY == PlayerHandler.players[i].absY) {
					if (player.freezeTimer > 0) {
						resetPlayerAttack();
						return;
					}
					player.followId = i;
					player.attackTimer = 0;
					return;
				}

				/*
				 * if ((c.inPirateHouse() &&
				 * !Server.playerHandler.players[i].inPirateHouse()) ||
				 * (Server.playerHandler.players[i].inPirateHouse() &&
				 * !c.inPirateHouse())) { resetPlayerAttack(); return; }
				 */
				// c.sendMessage("Made it here1.");
				if (!player.usingMagic) {
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
				}
				if (player.autocasting) {
					player.spellId = player.autocastId;
					player.usingMagic = true;
				}
				// c.sendMessage("Made it here2.");
				if (player.spellId > 0) {
					player.usingMagic = true;
				}
				player.attackTimer = getAttackDelay(
						player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());

				if (player.duelRule[Rules.FUN_WEAPONS_RULE.getRule()]) {
					boolean canUseWeapon = false;
					for (int funWeapon : Constants.FUN_WEAPONS) {
						if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == funWeapon) {
							canUseWeapon = true;
						}
					}
					if (!canUseWeapon) {
						player.getActionSender().sendMessage("You can only use fun weapons in this duel!");
						resetPlayerAttack();
						return;
					}
				}
				// c.sendMessage("Made it here3.");
				if (player.duelRule[Rules.RANGE_RULE.getRule()] && (usingBow || usingOtherRangeWeapons)) {
					player.getActionSender().sendMessage("Range has been disabled in this duel!");
					return;
				}
				if (player.duelRule[Rules.MELEE_RULE.getRule()] && (!usingBow && !usingOtherRangeWeapons && !player.usingMagic)) {
					player.getActionSender().sendMessage("Melee has been disabled in this duel!");
					return;
				}

				if (player.duelRule[Rules.MELEE_RULE.getRule()] && player.usingMagic) {
					player.getActionSender().sendMessage("Magic has been disabled in this duel!");
					resetPlayerAttack();
					return;
				}

				if ((!player.goodDistance(player.getX(), player.getY(), PlayerHandler.players[i].getX(),
						PlayerHandler.players[i].getY(), 4) && (usingOtherRangeWeapons && !usingBow && !player.usingMagic))
						|| (!player.goodDistance(player.getX(), player.getY(), PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(), 2)
								&& (!usingOtherRangeWeapons && usingHally() && !usingBow && !player.usingMagic))
						|| (!player.goodDistance(player.getX(), player.getY(), PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(), getRequiredDistance())
								&& (!usingOtherRangeWeapons && !usingHally() && !usingBow && !player.usingMagic))
						|| (!player.goodDistance(player.getX(), player.getY(), PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(), 10) && (usingBow || player.usingMagic))) {
					// c.sendMessage("Setting attack timer to 1");
					player.attackTimer = 1;
					if (!usingBow && !player.usingMagic && !usingOtherRangeWeapons && player.freezeTimer > 0)
						resetPlayerAttack();
					return;
				}

				if (!usingCross && !usingArrows && usingBow
						&& (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] < 4212 || player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] > 4223)
						&& !player.usingMagic) {
					player.getActionSender().sendMessage("You have run out of arrows!");
					player.stopMovement();
					resetPlayerAttack();
					return;
				}
				if (correctBowAndArrows() < player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] && Constants.CORRECT_ARROWS && usingBow
						&& !usingCrystalBow() && player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] != 9185 && !player.usingMagic) {
					player.getActionSender().sendMessage("You can't use "
							+ player.getItems().getItemName(player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()]).toLowerCase() + "s with a "
							+ player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase() + ".");
					player.stopMovement();
					resetPlayerAttack();
					return;
				}
				if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 9185 && !properBolts() && !player.usingMagic) {
					player.getActionSender().sendMessage("You must use bolts with a crossbow.");
					player.stopMovement();
					resetPlayerAttack();
					return;
				}

				if (usingBow || player.usingMagic || usingOtherRangeWeapons || usingHally()) {
					player.stopMovement();
				}

				if (!checkMagicReqs(player.spellId)) {
					player.stopMovement();
					resetPlayerAttack();
					return;
				}

				player.faceUpdate(i + 32768);

				if (player.duelStatus != 5) {
					if (!player.attackedPlayers.contains(player.playerIndex)
							&& !PlayerHandler.players[player.playerIndex].attackedPlayers.contains(player.playerId)) {
						player.attackedPlayers.add(player.playerIndex);
						player.isSkulled = true;
						player.skullTimer = Constants.SKULL_TIMER;
						player.headIconPk = 0;
						player.getPA().requestUpdates();
					}
				}
				player.specAccuracy = 1.0;
				player.specDamage = 1.0;
				player.delayedDamage = player.delayedDamage2 = 0;
				if (player.usingSpecial && !player.usingMagic) {
					if (player.duelRule[Rules.SPECIAL_ATTACKS_RULE.getRule()] && player.duelStatus == 5) {
						player.getActionSender().sendMessage("Special attacks have been disabled during this duel!");
						player.usingSpecial = false;
						player.getItems().updateSpecialBar();
						resetPlayerAttack();
						return;
					}
					if (checkSpecAmount(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()])) {
						player.lastArrowUsed = player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()];
						activateSpecial(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()], i);
						player.followId = player.playerIndex;
						return;
					} else {
						player.getActionSender().sendMessage("You don't have the required special energy to use this attack.");
						player.usingSpecial = false;
						player.getItems().updateSpecialBar();
						player.playerIndex = 0;
						return;
					}
				}

				if (!player.usingMagic) {
					player.startAnimation(
							getWepAnim(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase()));
					player.mageFollow = false;
				} else {
					player.startAnimation(Enchantment.MAGIC_SPELLS[player.spellId][2]);
					player.mageFollow = true;
					player.followId = player.playerIndex;
				}
				PlayerHandler.players[i].underAttackBy = player.playerId;
				PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
				PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
				PlayerHandler.players[i].killerId = player.playerId;
				player.lastArrowUsed = 0;
				player.rangeItemUsed = 0;
				if (!usingBow && !player.usingMagic && !usingOtherRangeWeapons) { // melee
																				// hit
																				// delay
					player.followId = PlayerHandler.players[player.playerIndex].playerId;
					player.getPA().followPlayer();
					player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
					player.delayedDamage = Misc.random(calculateMeleeMaxHit());
					player.projectileStage = 0;
					player.oldPlayerIndex = i;
				}

				if (usingBow && !usingOtherRangeWeapons && !player.usingMagic || usingCross) { // range
																							// hit
																							// delay
					if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] >= 4212 && player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] <= 4223) {
						player.rangeItemUsed = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()];
						player.crystalBowArrowCount++;
					} else {
						player.rangeItemUsed = player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()];
						player.getItems().deleteArrow();
					}
					if (player.fightMode == 2)
						player.attackTimer--;
					if (usingCross)
						player.usingBow = true;
					player.usingBow = true;
					player.followId = PlayerHandler.players[player.playerIndex].playerId;
					player.getPA().followPlayer();
					player.lastWeaponUsed = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()];
					player.lastArrowUsed = player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()];
					player.gfx100(getRangeStartGFX());
					player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
					player.projectileStage = 1;
					player.oldPlayerIndex = i;
					fireProjectilePlayer();
				}

				if (usingOtherRangeWeapons) { // knives, darts, etc hit delay
					player.rangeItemUsed = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()];
					player.getItems().deleteEquipment();
					player.usingRangeWeapon = true;
					player.followId = PlayerHandler.players[player.playerIndex].playerId;
					player.getPA().followPlayer();
					player.gfx100(getRangeStartGFX());
					if (player.fightMode == 2)
						player.attackTimer--;
					player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
					player.projectileStage = 1;
					player.oldPlayerIndex = i;
					fireProjectilePlayer();
				}

				if (player.usingMagic) { // magic hit delay
					int pX = player.getX();
					int pY = player.getY();
					int nX = PlayerHandler.players[i].getX();
					int nY = PlayerHandler.players[i].getY();
					int offX = (pY - nY) * -1;
					int offY = (pX - nX) * -1;
					player.castingMagic = true;
					player.projectileStage = 2;
					if (Enchantment.MAGIC_SPELLS[player.spellId][3] > 0) {
						if (getStartGfxHeight() == 100) {
							player.gfx100(Enchantment.MAGIC_SPELLS[player.spellId][3]);
						} else {
							player.gfx0(Enchantment.MAGIC_SPELLS[player.spellId][3]);
						}
					}
					if (Enchantment.MAGIC_SPELLS[player.spellId][4] > 0) {
						player.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, Enchantment.MAGIC_SPELLS[player.spellId][4],
								getStartHeight(), getEndHeight(), -i - 1, getStartDelay());
					}
					if (player.autocastId > 0) {
						player.followId = player.playerIndex;
						player.followDistance = 5;
					}
					player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
					player.oldPlayerIndex = i;
					player.oldSpellId = player.spellId;
					player.spellId = 0;
					Player o = (Player) PlayerHandler.players[i];
					if (Enchantment.MAGIC_SPELLS[player.oldSpellId][0] == 12891 && o.isMoving) {
						// c.sendMessage("Barrage projectile..");
						player.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 85, 368, 25, 25, -i - 1,
								getStartDelay());
					}
					if (Misc.random(mageAtk()) > Misc.random(o.getCombat().mageDef())) {
						player.magicFailed = false;
					} else if (Misc.random(mageAtk()) < Misc.random(o.getCombat().mageDef())) {
						player.magicFailed = true;
					}
					int freezeDelay = getFreezeTime();// freeze time
					if (freezeDelay > 0 && PlayerHandler.players[i].freezeTimer <= -3 && !player.magicFailed) {
						PlayerHandler.players[i].freezeTimer = freezeDelay;
						o.resetWalkingQueue();
						o.getActionSender().sendMessage("You have been frozen.");
						o.frozenBy = player.playerId;
					}
					if (!player.autocasting && player.spellId <= 0)
						player.playerIndex = 0;
				}

				if (usingBow && Constants.CRYSTAL_BOW_DEGRADES) { // crystal bow
																	// degrading
					if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4212) { // new
																		// crystal
																		// bow
																		// becomes
																		// full
																		// bow
																		// on
																		// the
																		// first
																		// shot
						player.getItems().wearItem(4214, 1, 3);
					}

					if (player.crystalBowArrowCount >= 250) {
						switch (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]) {

						case 4223: // 1/10 bow
							player.getItems().wearItem(-1, 1, 3);
							player.getActionSender().sendMessage("Your crystal bow has fully degraded.");
							if (!player.getItems().addItem(4207, 1)) {
								Server.itemHandler.createGroundItem(player, 4207, player.getX(), player.getY(), 1, player.getId());
							}
							player.crystalBowArrowCount = 0;
							break;

						default:
							player.getItems().wearItem(++player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()], 1, 3);
							player.getActionSender().sendMessage("Your crystal bow degrades.");
							player.crystalBowArrowCount = 0;
							break;
						}
					}
				}
			}
		}
	}

	public boolean usingCrystalBow() {
		return player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] >= 4212 && player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] <= 4223;
	}

	public void appendVengeance(int otherPlayer, int damage) {
		if (damage <= 0)
			return;
		Player o = PlayerHandler.players[otherPlayer];
		o.forcedText = "Taste Vengeance!";
		o.forcedChatUpdateRequired = true;
		o.updateRequired = true;
		o.vengOn = false;
		if ((o.playerLevel[3] - damage) > 0) {
			damage = (int) (damage * 0.75);
			if (damage > player.playerLevel[3]) {
				damage = player.playerLevel[3];
			}
			player.setHitDiff2(damage);
			player.setHitUpdateRequired2(true);
			player.playerLevel[3] -= damage;
			player.getPA().refreshSkill(3);
		}
		player.updateRequired = true;
	}

	public void playerDelayedHit(int i) {
		if (PlayerHandler.players[i] != null) {
			if (PlayerHandler.players[i].isDead || player.isDead || PlayerHandler.players[i].playerLevel[3] <= 0
					|| player.playerLevel[3] <= 0) {
				player.playerIndex = 0;
				return;
			}
			if (PlayerHandler.players[i].respawnTimer > 0) {
				player.faceUpdate(0);
				player.playerIndex = 0;
				return;
			}
			Player o = (Player) PlayerHandler.players[i];
			o.getPA().removeAllWindows();
			if (o.playerIndex <= 0 && o.npcIndex <= 0) {
				if (o.autoRet == 1) {
					o.playerIndex = player.playerId;
				}
			}
			if (o.attackTimer <= 3 || o.attackTimer == 0 && o.playerIndex == 0 && !player.castingMagic) { // block
																										// animation
				o.startAnimation(o.getCombat().getBlockEmote());
			}
			if (o.inTrade) {
				o.getTradeAndDuel().declineTrade();
			}
			if (player.projectileStage == 0) { // melee hit damage
				applyPlayerMeleeDamage(i, 1);
				if (player.doubleHit) {
					applyPlayerMeleeDamage(i, 2);
				}
			}

			if (!player.castingMagic && player.projectileStage > 0) { // range hit damage
				int damage = Misc.random(rangeMaxHit());
				int damage2 = -1;
				if (player.lastWeaponUsed == 11235 || player.bowSpecShot == 1)
					damage2 = Misc.random(rangeMaxHit());
				boolean ignoreDef = false;
				if (Misc.random(4) == 1 && player.lastArrowUsed == 9243) {
					ignoreDef = true;
					o.gfx0(758);
				}
				if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc.random(10 + calculateRangeAttack())
						&& !ignoreDef) {
					damage = 0;
				}
				if (Misc.random(4) == 1 && player.lastArrowUsed == 9242 && damage > 0) {
					PlayerHandler.players[i].gfx0(754);
					damage = NPCHandler.npcs[i].HP / 5;
					player.handleHitMask(player.playerLevel[3] / 10);
					player.dealDamage(player.playerLevel[3] / 10);
					player.gfx0(754);
				}

				if (player.lastWeaponUsed == 11235 || player.bowSpecShot == 1) {
					if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc
							.random(10 + calculateRangeAttack()))
						damage2 = 0;
				}

				if (player.dbowSpec) {
					o.gfx100(1100);
					if (damage < 8)
						damage = 8;
					if (damage2 < 8)
						damage2 = 8;
					player.dbowSpec = false;
				}
				if (damage > 0 && Misc.random(5) == 1 && player.lastArrowUsed == 9244) {
					damage *= 1.45;
					o.gfx0(756);
				}
				if (o.prayerActive[17] && System.currentTimeMillis() - o.protRangeDelay > 1500) { // if
																									// prayer
																									// active
																									// reduce
																									// damage
																									// by
																									// half
					damage = damage * 60 / 100;
					if (player.lastWeaponUsed == 11235 || player.bowSpecShot == 1)
						damage2 = damage2 * 60 / 100;
				}
				if (PlayerHandler.players[i].playerLevel[3] - damage < 0) {
					damage = PlayerHandler.players[i].playerLevel[3];
				}
				if (PlayerHandler.players[i].playerLevel[3] - damage - damage2 < 0) {
					damage2 = PlayerHandler.players[i].playerLevel[3] - damage;
				}
				if (damage < 0)
					damage = 0;
				if (damage2 < 0 && damage2 != -1)
					damage2 = 0;
				if (o.vengOn) {
					appendVengeance(i, damage);
					appendVengeance(i, damage2);
				}
				if (damage > 0)
					applyRecoil(damage, i);
				if (damage2 > 0)
					applyRecoil(damage2, i);
				if (player.fightMode == 3) {
					player.getPA().addSkillXP((damage * SkillIndex.RANGE.getExpRatio() / 3), SkillIndex.RANGE.getSkillId());
					player.getPA().addSkillXP((damage * SkillIndex.RANGE.getExpRatio() / 3), SkillIndex.DEFENCE.getSkillId());
					player.getPA().addSkillXP((damage * SkillIndex.RANGE.getExpRatio() / 3), SkillIndex.HITPOINTS.getSkillId());
					player.getPA().refreshSkill(1);
					player.getPA().refreshSkill(3);
					player.getPA().refreshSkill(4);
				} else {
					player.getPA().addSkillXP((damage * SkillIndex.RANGE.getExpRatio()), SkillIndex.RANGE.getSkillId());
					player.getPA().addSkillXP((damage * SkillIndex.RANGE.getExpRatio() / 3), SkillIndex.HITPOINTS.getSkillId());
					player.getPA().refreshSkill(3);
					player.getPA().refreshSkill(4);
				}
				boolean dropArrows = true;

				for (int noArrowId : player.NO_ARROW_DROP) {
					if (player.lastWeaponUsed == noArrowId) {
						dropArrows = false;
						break;
					}
				}
				if (dropArrows) {
					player.getItems().dropArrowPlayer();
				}
				PlayerHandler.players[i].underAttackBy = player.playerId;
				PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
				PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
				PlayerHandler.players[i].killerId = player.playerId;
				// Server.playerHandler.players[i].setHitDiff(damage);
				// Server.playerHandler.players[i].playerLevel[3] -= damage;
				PlayerHandler.players[i].dealDamage(damage);
				PlayerHandler.players[i].damageTaken[player.playerId] += damage;
				player.killedBy = PlayerHandler.players[i].playerId;
				PlayerHandler.players[i].handleHitMask(damage);
				if (damage2 != -1) {
					// Server.playerHandler.players[i].playerLevel[3] -=
					// damage2;
					PlayerHandler.players[i].dealDamage(damage2);
					PlayerHandler.players[i].damageTaken[player.playerId] += damage2;
					PlayerHandler.players[i].handleHitMask(damage2);

				}
				o.getPA().refreshSkill(3);

				// Server.playerHandler.players[i].setHitUpdateRequired(true);
				PlayerHandler.players[i].updateRequired = true;
				applySmite(i, damage);
				if (damage2 != -1)
					applySmite(i, damage2);

			} else if (player.projectileStage > 0) { // magic hit damage
				int damage = Misc.random(Enchantment.MAGIC_SPELLS[player.oldSpellId][6]);
				if (godSpells()) {
					if (System.currentTimeMillis() - player.godSpellDelay < Constants.GOD_SPELL_CHARGE) {
						damage += 10;
					}
				}
				// c.playerIndex = 0;
				if (player.magicFailed)
					damage = 0;

				if (o.prayerActive[16] && System.currentTimeMillis() - o.protMageDelay > 1500) { // if
																									// prayer
																									// active
																									// reduce
																									// damage
																									// by
																									// half
					damage = damage * 60 / 100;
				}
				if (PlayerHandler.players[i].playerLevel[3] - damage < 0) {
					damage = PlayerHandler.players[i].playerLevel[3];
				}
				if (o.vengOn)
					appendVengeance(i, damage);
				if (damage > 0)
					applyRecoil(damage, i);
				player.getPA().addSkillXP((Enchantment.MAGIC_SPELLS[player.oldSpellId][7] + damage * SkillIndex.MAGIC.getExpRatio()), SkillIndex.MAGIC.getSkillId());
				player.getPA().addSkillXP((Enchantment.MAGIC_SPELLS[player.oldSpellId][7] + damage * SkillIndex.MAGIC.getExpRatio() / 3), SkillIndex.HITPOINTS.getSkillId());
				player.getPA().refreshSkill(3);
				player.getPA().refreshSkill(6);

				if (getEndGfxHeight() == 100 && !player.magicFailed) { // end GFX
					PlayerHandler.players[i].gfx100(Enchantment.MAGIC_SPELLS[player.oldSpellId][5]);
				} else if (!player.magicFailed) {
					PlayerHandler.players[i].gfx0(Enchantment.MAGIC_SPELLS[player.oldSpellId][5]);
				} else if (player.magicFailed) {
					PlayerHandler.players[i].gfx100(85);
				}

				if (!player.magicFailed) {
					if (System.currentTimeMillis() - PlayerHandler.players[i].reduceStat > 35000) {
						PlayerHandler.players[i].reduceStat = System.currentTimeMillis();
						switch (Enchantment.MAGIC_SPELLS[player.oldSpellId][0]) {
						case 12987:
						case 13011:
						case 12999:
						case 13023:
							PlayerHandler.players[i].playerLevel[0] -= ((o.getPA()
									.getLevelForXP(PlayerHandler.players[i].playerXP[0]) * 10) / 100);
							break;
						}
					}

					switch (Enchantment.MAGIC_SPELLS[player.oldSpellId][0]) {
					case 12445: // teleblock
						if (System.currentTimeMillis() - o.teleBlockDelay > o.teleBlockLength) {
							o.teleBlockDelay = System.currentTimeMillis();
							o.getActionSender().sendMessage("You have been teleblocked.");
							if (o.prayerActive[16] && System.currentTimeMillis() - o.protMageDelay > 1500)
								o.teleBlockLength = 150000;
							else
								o.teleBlockLength = 300000;
						}
						break;

					case 12901:
					case 12919: // blood spells
					case 12911:
					case 12929:
						int heal = damage / 4;
						if (player.playerLevel[3] + heal > player.getPA().getLevelForXP(player.playerXP[3])) {
							player.playerLevel[3] = player.getPA().getLevelForXP(player.playerXP[3]);
						} else {
							player.playerLevel[3] += heal;
						}
						player.getPA().refreshSkill(3);
						break;

					case 1153:
						PlayerHandler.players[i].playerLevel[0] -= ((o.getPA()
								.getLevelForXP(PlayerHandler.players[i].playerXP[0]) * 5) / 100);
						o.getActionSender().sendMessage("Your attack level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[player.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(0);
						break;

					case 1157:
						PlayerHandler.players[i].playerLevel[2] -= ((o.getPA()
								.getLevelForXP(PlayerHandler.players[i].playerXP[2]) * 5) / 100);
						o.getActionSender().sendMessage("Your strength level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[player.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(2);
						break;

					case 1161:
						PlayerHandler.players[i].playerLevel[1] -= ((o.getPA()
								.getLevelForXP(PlayerHandler.players[i].playerXP[1]) * 5) / 100);
						o.getActionSender().sendMessage("Your defence level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[player.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(1);
						break;

					case 1542:
						PlayerHandler.players[i].playerLevel[1] -= ((o.getPA()
								.getLevelForXP(PlayerHandler.players[i].playerXP[1]) * 10) / 100);
						o.getActionSender().sendMessage("Your defence level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[player.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(1);
						break;

					case 1543:
						PlayerHandler.players[i].playerLevel[2] -= ((o.getPA()
								.getLevelForXP(PlayerHandler.players[i].playerXP[2]) * 10) / 100);
						o.getActionSender().sendMessage("Your strength level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[player.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(2);
						break;

					case 1562:
						PlayerHandler.players[i].playerLevel[0] -= ((o.getPA()
								.getLevelForXP(PlayerHandler.players[i].playerXP[0]) * 10) / 100);
						o.getActionSender().sendMessage("Your attack level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[player.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(0);
						break;
					}
				}

				PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
				PlayerHandler.players[i].underAttackBy = player.playerId;
				PlayerHandler.players[i].killerId = player.playerId;
				PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
				if (Enchantment.MAGIC_SPELLS[player.oldSpellId][6] != 0) {
					// Server.playerHandler.players[i].playerLevel[3] -= damage;
					PlayerHandler.players[i].dealDamage(damage);
					PlayerHandler.players[i].damageTaken[player.playerId] += damage;
					if (!player.magicFailed) {
						// Server.playerHandler.players[i].setHitDiff(damage);
						// Server.playerHandler.players[i].setHitUpdateRequired(true);
						PlayerHandler.players[i].handleHitMask(damage);
					}
				}
				applySmite(i, damage);
				player.killedBy = PlayerHandler.players[i].playerId;
				o.getPA().refreshSkill(3);
				PlayerHandler.players[i].updateRequired = true;
				player.usingMagic = false;
				player.castingMagic = false;
				if (o.inMulti() && multis()) {
					player.barrageCount = 0;
					for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							if (j == o.playerId)
								continue;
							if (player.barrageCount >= 9)
								break;
							if (o.goodDistance(o.getX(), o.getY(), PlayerHandler.players[j].getX(),
									PlayerHandler.players[j].getY(), 1))
								appendMultiBarrage(j, player.magicFailed);
						}
					}
				}
				player.getPA().refreshSkill(3);
				player.getPA().refreshSkill(6);
				player.oldSpellId = 0;
			}
		}
		player.getPA().requestUpdates();
		int oldindex = player.oldPlayerIndex;
		if (player.bowSpecShot <= 0) {
			player.oldPlayerIndex = 0;
			player.projectileStage = 0;
			player.lastWeaponUsed = 0;
			player.doubleHit = false;
			player.bowSpecShot = 0;
		}
		if (player.bowSpecShot != 0) {
			player.bowSpecShot = 0;
		}
	}

	public boolean multis() {
		switch (Enchantment.MAGIC_SPELLS[player.oldSpellId][0]) {
		case 12891:
		case 12881:
		case 13011:
		case 13023:
		case 12919: // blood spells
		case 12929:
		case 12963:
		case 12975:
			return true;
		}
		return false;

	}

	public void appendMultiBarrage(int playerId, boolean splashed) {
		if (PlayerHandler.players[playerId] != null) {
			Player players = (Player) PlayerHandler.players[playerId];
			if (players.isDead || players.respawnTimer > 0)
				return;
			if (checkMultiBarrageReqs(playerId)) {
				player.barrageCount++;
				if (Misc.random(mageAtk()) > Misc.random(mageDef()) && !player.magicFailed) {
					if (getEndGfxHeight() == 100) { // end GFX
						players.gfx100(Enchantment.MAGIC_SPELLS[player.oldSpellId][5]);
					} else {
						players.gfx0(Enchantment.MAGIC_SPELLS[player.oldSpellId][5]);
					}
					int damage = Misc.random(Enchantment.MAGIC_SPELLS[player.oldSpellId][6]);
					if (players.prayerActive[12]) {
						damage *= (int) (.60);
					}
					if (players.playerLevel[3] - damage < 0) {
						damage = players.playerLevel[3];
					}
					player.getPA().addSkillXP((Enchantment.MAGIC_SPELLS[player.oldSpellId][7] + damage * SkillIndex.MAGIC.getExpRatio()), SkillIndex.MAGIC.getSkillId());
					player.getPA().addSkillXP((Enchantment.MAGIC_SPELLS[player.oldSpellId][7] + damage * SkillIndex.MAGIC.getExpRatio() / 3), SkillIndex.HITPOINTS.getSkillId());
					// Server.playerHandler.players[playerId].setHitDiff(damage);
					// Server.playerHandler.players[playerId].setHitUpdateRequired(true);
					PlayerHandler.players[playerId].handleHitMask(damage);
					// Server.playerHandler.players[playerId].playerLevel[3] -=
					// damage;
					PlayerHandler.players[playerId].dealDamage(damage);
					PlayerHandler.players[playerId].damageTaken[player.playerId] += damage;
					players.getPA().refreshSkill(3);
					multiSpellEffect(playerId, damage);
				} else {
					players.gfx100(85);
				}
			}
		}
	}

	public void multiSpellEffect(int playerId, int damage) {
		switch (Enchantment.MAGIC_SPELLS[player.oldSpellId][0]) {
		case 13011:
		case 13023:
			if (System.currentTimeMillis() - PlayerHandler.players[playerId].reduceStat > 35000) {
				PlayerHandler.players[playerId].reduceStat = System.currentTimeMillis();
				PlayerHandler.players[playerId].playerLevel[0] -= ((PlayerHandler.players[playerId]
						.getLevelForXP(PlayerHandler.players[playerId].playerXP[0]) * 10) / 100);
			}
			break;
		case 12919: // blood spells
		case 12929:
			int heal = damage / 4;
			if (player.playerLevel[3] + heal >= player.getPA().getLevelForXP(player.playerXP[3])) {
				player.playerLevel[3] = player.getPA().getLevelForXP(player.playerXP[3]);
			} else {
				player.playerLevel[3] += heal;
			}
			player.getPA().refreshSkill(3);
			break;
		case 12891:
		case 12881:
			if (PlayerHandler.players[playerId].freezeTimer < -4) {
				PlayerHandler.players[playerId].freezeTimer = getFreezeTime();
				PlayerHandler.players[playerId].stopMovement();
			}
			break;
		}
	}

	public void applyPlayerMeleeDamage(int i, int damageMask) {
		Player o = (Player) PlayerHandler.players[i];
		if (o == null) {
			return;
		}
		int damage = 0;
		if (damageMask == 1) {
			damage = player.delayedDamage;
			player.delayedDamage = 0;
		} else {
			damage = player.delayedDamage2;
			player.delayedDamage2 = 0;
		}
		if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 5698 && o.poisonDamage <= 0 && Misc.random(3) == 1) {
			o.getPA().appendPoison(13);
			player.bonusAttack += damage / 3;
		} else {
			player.bonusAttack += damage / 3;
		}
		if (player.maxNextHit) {
			damage = calculateMeleeMaxHit();
		}
		if (player.ssSpec && damageMask == 2) {
			damage = 5 + Misc.random(11);
			player.ssSpec = false;
		}
		if (PlayerHandler.players[i].playerLevel[3] - damage < 0) {
			damage = PlayerHandler.players[i].playerLevel[3];
		}
		if (o.vengOn && damage > 0)
			appendVengeance(i, damage);
		if (damage > 0)
			applyRecoil(damage, i);
		switch (player.specEffect) {
		case 1: // dragon scimmy special
			if (damage > 0) {
				if (o.prayerActive[16] || o.prayerActive[17] || o.prayerActive[18]) {
					o.headIcon = -1;
					o.getActionSender().sendConfig(player.PRAYER_GLOW[16], 0);
					o.getActionSender().sendConfig(player.PRAYER_GLOW[17], 0);
					o.getActionSender().sendConfig(player.PRAYER_GLOW[18], 0);
				}
				o.getActionSender().sendMessage("You have been injured!");
				o.stopPrayerDelay = System.currentTimeMillis();
				o.prayerActive[16] = false;
				o.prayerActive[17] = false;
				o.prayerActive[18] = false;
				o.getPA().requestUpdates();
			}
			break;
		case 2:
			if (damage > 0) {
				if (o.freezeTimer <= 0)
					o.freezeTimer = 30;
				o.gfx0(369);
				o.getActionSender().sendMessage("You have been frozen.");
				o.frozenBy = player.playerId;
				o.stopMovement();
				player.getActionSender().sendMessage("You freeze your enemy.");
			}
			break;
		case 3:
			if (damage > 0) {
				o.playerLevel[1] -= damage;
				o.getActionSender().sendMessage("You feel weak.");
				if (o.playerLevel[1] < 1)
					o.playerLevel[1] = 1;
				o.getPA().refreshSkill(1);
			}
			break;
		case 4:
			if (damage > 0) {
				if (player.playerLevel[3] + damage > player.getLevelForXP(player.playerXP[3]))
					if (player.playerLevel[3] > player.getLevelForXP(player.playerXP[3]))
						;
					else
						player.playerLevel[3] = player.getLevelForXP(player.playerXP[3]);
				else
					player.playerLevel[3] += damage;
				player.getPA().refreshSkill(3);
			}
			break;
		}
		player.specEffect = 0;
		if (player.fightMode == 3) {
			player.getPA().addSkillXP((damage * SkillIndex.ATTACK.getExpRatio() / 3), SkillIndex.ATTACK.getSkillId());
			player.getPA().addSkillXP((damage * SkillIndex.ATTACK.getExpRatio() / 3), SkillIndex.DEFENCE.getSkillId());
			player.getPA().addSkillXP((damage * SkillIndex.ATTACK.getExpRatio() / 3), SkillIndex.STRENGTH.getSkillId());
			player.getPA().addSkillXP((damage * SkillIndex.ATTACK.getExpRatio() / 3), SkillIndex.HITPOINTS.getSkillId());
			player.getPA().refreshSkill(0);
			player.getPA().refreshSkill(1);
			player.getPA().refreshSkill(2);
			player.getPA().refreshSkill(3);
		} else {
			player.getPA().addSkillXP((damage * SkillIndex.ATTACK.getExpRatio()), player.fightMode);
			player.getPA().addSkillXP((damage * SkillIndex.ATTACK.getExpRatio() / 3), SkillIndex.HITPOINTS.getSkillId());
			player.getPA().refreshSkill(player.fightMode);
			player.getPA().refreshSkill(3);
		}
		PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
		PlayerHandler.players[i].underAttackBy = player.playerId;
		PlayerHandler.players[i].killerId = player.playerId;
		PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
		if (player.killedBy != PlayerHandler.players[i].playerId)
			player.killedBy = PlayerHandler.players[i].playerId;
		applySmite(i, damage);
		switch (damageMask) {
		case 1:
			/*
			 * if (!Server.playerHandler.players[i].getHitUpdateRequired()){
			 * Server.playerHandler.players[i].setHitDiff(damage);
			 * Server.playerHandler.players[i].setHitUpdateRequired(true); }
			 * else { Server.playerHandler.players[i].setHitDiff2(damage);
			 * Server.playerHandler.players[i].setHitUpdateRequired2(true); }
			 */
			// Server.playerHandler.players[i].playerLevel[3] -= damage;
			PlayerHandler.players[i].dealDamage(damage);
			PlayerHandler.players[i].damageTaken[player.playerId] += damage;
			PlayerHandler.players[i].updateRequired = true;
			o.getPA().refreshSkill(3);
			break;

		case 2:
			/*
			 * if (!Server.playerHandler.players[i].getHitUpdateRequired2()){
			 * Server.playerHandler.players[i].setHitDiff2(damage);
			 * Server.playerHandler.players[i].setHitUpdateRequired2(true); }
			 * else { Server.playerHandler.players[i].setHitDiff(damage);
			 * Server.playerHandler.players[i].setHitUpdateRequired(true); }
			 */
			// Server.playerHandler.players[i].playerLevel[3] -= damage;
			PlayerHandler.players[i].dealDamage(damage);
			PlayerHandler.players[i].damageTaken[player.playerId] += damage;
			PlayerHandler.players[i].updateRequired = true;
			player.doubleHit = false;
			o.getPA().refreshSkill(3);
			break;
		}
		PlayerHandler.players[i].handleHitMask(damage);
	}

	public void applySmite(int index, int damage) {
		if (!player.prayerActive[23])
			return;
		if (damage <= 0)
			return;
		if (PlayerHandler.players[index] != null) {
			Player c2 = (Player) PlayerHandler.players[index];
			c2.playerLevel[5] -= damage / 4;
			if (c2.playerLevel[5] <= 0) {
				c2.playerLevel[5] = 0;
				c2.getCombat().resetPrayers();
			}
			c2.getPA().refreshSkill(5);
		}

	}

	public void fireProjectilePlayer() {
		if (player.oldPlayerIndex > 0) {
			if (PlayerHandler.players[player.oldPlayerIndex] != null) {
				player.projectileStage = 2;
				int pX = player.getX();
				int pY = player.getY();
				int oX = PlayerHandler.players[player.oldPlayerIndex].getX();
				int oY = PlayerHandler.players[player.oldPlayerIndex].getY();
				int offX = (pY - oY) * -1;
				int offY = (pX - oX) * -1;
				if (!player.msbSpec)
					player.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, getProjectileSpeed(),
							getRangeProjectileGFX(), 43, 31, -player.oldPlayerIndex - 1, getStartDelay());
				else if (player.msbSpec) {
					player.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, getProjectileSpeed(),
							getRangeProjectileGFX(), 43, 31, -player.oldPlayerIndex - 1, getStartDelay(), 10);
					player.msbSpec = false;
				}
				if (usingDbow())
					player.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50, getProjectileSpeed(),
							getRangeProjectileGFX(), 60, 31, -player.oldPlayerIndex - 1, getStartDelay(), 35);
			}
		}
	}

	public boolean usingDbow() {
		return player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 11235;
	}

	/** Prayer **/

	public void activatePrayer(int i) {
		if (player.duelRule[Rules.PRAYER_RULE.getRule()]) {
			for (int p = 0; p < player.PRAYER.length; p++) { // reset prayer glows
				player.prayerActive[p] = false;
				player.getActionSender().sendConfig(player.PRAYER_GLOW[p], 0);
			}
			player.getActionSender().sendMessage("Prayer has been disabled in this duel!");
			return;
		}
		if (i == 24 && player.playerLevel[1] < 65) {
			player.getActionSender().sendConfig(player.PRAYER_GLOW[i], 0);
			player.getActionSender().sendMessage("You may not use this prayer yet.");
			return;
		}
		if (i == 25 && player.playerLevel[1] < 70) {
			player.getActionSender().sendConfig(player.PRAYER_GLOW[i], 0);
			player.getActionSender().sendMessage("You may not use this prayer yet.");
			return;
		}
		int[] defPray = { 0, 5, 13, 24, 25 };
		int[] strPray = { 1, 6, 14, 24, 25 };
		int[] atkPray = { 2, 7, 15, 24, 25 };
		int[] rangePray = { 3, 11, 19 };
		int[] magePray = { 4, 12, 20 };

		if (player.playerLevel[5] > 0 || !Constants.PRAYER_POINTS_REQUIRED) {
			if (player.getPA().getLevelForXP(player.playerXP[5]) >= player.PRAYER_LEVEL_REQUIRED[i]
					|| !Constants.PRAYER_LEVEL_REQUIRED) {
				boolean headIcon = false;
				switch (i) {
				case 0:
				case 5:
				case 13:
					if (player.prayerActive[i] == false) {
						for (int j = 0; j < defPray.length; j++) {
							if (defPray[j] != i) {
								player.prayerActive[defPray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[defPray[j]], 0);
							}
						}
					}
					break;

				case 1:
				case 6:
				case 14:
					if (player.prayerActive[i] == false) {
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								player.prayerActive[strPray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								player.prayerActive[rangePray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								player.prayerActive[magePray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;

				case 2:
				case 7:
				case 15:
					if (player.prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								player.prayerActive[atkPray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								player.prayerActive[rangePray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								player.prayerActive[magePray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;

				case 3:// range prays
				case 11:
				case 19:
					if (player.prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								player.prayerActive[atkPray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								player.prayerActive[strPray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								player.prayerActive[rangePray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								player.prayerActive[magePray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case 4:
				case 12:
				case 20:
					if (player.prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								player.prayerActive[atkPray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								player.prayerActive[strPray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								player.prayerActive[rangePray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								player.prayerActive[magePray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case 10:
					player.lastProtItem = System.currentTimeMillis();
					break;

				case 16:
				case 17:
				case 18:
					if (System.currentTimeMillis() - player.stopPrayerDelay < 5000) {
						player.getActionSender().sendMessage("You have been injured and can't use this prayer!");
						player.getActionSender().sendConfig(player.PRAYER_GLOW[16], 0);
						player.getActionSender().sendConfig(player.PRAYER_GLOW[17], 0);
						player.getActionSender().sendConfig(player.PRAYER_GLOW[18], 0);
						return;
					}
					if (i == 16)
						player.protMageDelay = System.currentTimeMillis();
					else if (i == 17)
						player.protRangeDelay = System.currentTimeMillis();
					else if (i == 18)
						player.protMeleeDelay = System.currentTimeMillis();
				case 21:
				case 22:
				case 23:
					headIcon = true;
					for (int p = 16; p < 24; p++) {
						if (i != p && p != 19 && p != 20) {
							player.prayerActive[p] = false;
							player.getActionSender().sendConfig(player.PRAYER_GLOW[p], 0);
						}
					}
					break;
				case 24:
				case 25:
					if (player.prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								player.prayerActive[atkPray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								player.prayerActive[strPray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								player.prayerActive[rangePray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								player.prayerActive[magePray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[magePray[j]], 0);
							}
						}
						for (int j = 0; j < defPray.length; j++) {
							if (defPray[j] != i) {
								player.prayerActive[defPray[j]] = false;
								player.getActionSender().sendConfig(player.PRAYER_GLOW[defPray[j]], 0);
							}
						}
					}
					break;
				}

				if (!headIcon) {
					if (player.prayerActive[i] == false) {
						player.prayerActive[i] = true;
						player.getActionSender().sendConfig(player.PRAYER_GLOW[i], 1);
					} else {
						player.prayerActive[i] = false;
						player.getActionSender().sendConfig(player.PRAYER_GLOW[i], 0);
					}
				} else {
					if (player.prayerActive[i] == false) {
						player.prayerActive[i] = true;
						player.getActionSender().sendConfig(player.PRAYER_GLOW[i], 1);
						player.headIcon = player.PRAYER_HEAD_ICONS[i];
						player.getPA().requestUpdates();
					} else {
						player.prayerActive[i] = false;
						player.getActionSender().sendConfig(player.PRAYER_GLOW[i], 0);
						player.headIcon = -1;
						player.getPA().requestUpdates();
					}
				}
			} else {
				player.getActionSender().sendConfig(player.PRAYER_GLOW[i], 0);
				player.getPA().sendFrame126("You need a @blu@Prayer level of " + player.PRAYER_LEVEL_REQUIRED[i] + " to use "
						+ player.PRAYER_NAME[i] + ".", 357);
				player.getPA().sendFrame126("Click here to continue", 358);
				player.getPA().sendFrame164(356);
			}
		} else {
			player.getActionSender().sendConfig(player.PRAYER_GLOW[i], 0);
			player.getActionSender().sendMessage("You have run out of prayer points!");
		}

	}

	/**
	 * Specials
	 **/

	public void activateSpecial(int weapon, int i) {
		if (NPCHandler.npcs[i] == null && player.npcIndex > 0) {
			return;
		}
		if (PlayerHandler.players[i] == null && player.playerIndex > 0) {
			return;
		}
		player.doubleHit = false;
		player.specEffect = 0;
		player.projectileStage = 0;
		player.specMaxHitIncrease = 2;
		if (player.npcIndex > 0) {
			player.oldNpcIndex = i;
		} else if (player.playerIndex > 0) {
			player.oldPlayerIndex = i;
			PlayerHandler.players[i].underAttackBy = player.playerId;
			PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
			PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
			PlayerHandler.players[i].killerId = player.playerId;
		}
		switch (weapon) {

		case 1305: // dragon long
			player.gfx100(248);
			player.startAnimation(1058);
			player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			player.specAccuracy = 1.10;
			player.specDamage = 1.20;
			break;

		case 1215: // dragon daggers
		case 1231:
		case 5680:
		case 5698:
			player.gfx100(252);
			player.startAnimation(1062);
			player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			player.doubleHit = true;
			player.specAccuracy = 1.30;
			player.specDamage = 1.05;
			break;

		case 11730:
			player.gfx100(1224);
			player.startAnimation(811);
			player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			player.doubleHit = true;
			player.ssSpec = true;
			player.specAccuracy = 1.30;
			break;

		case 4151: // whip
			if (NPCHandler.npcs[i] != null) {
				NPCHandler.npcs[i].gfx100(341);
			}
			player.specAccuracy = 1.10;
			player.startAnimation(1658);
			player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			break;

		case 11694: // ags
			player.startAnimation(4304);
			player.specDamage = 1.25;
			player.specAccuracy = 1.85;
			player.gfx0(1222);
			player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			break;

		case 11700:
			player.startAnimation(4302);
			player.gfx0(1221);
			player.specAccuracy = 1.25;
			player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			player.specEffect = 2;
			break;

		case 11696:
			player.startAnimation(4301);
			player.gfx0(1223);
			player.specDamage = 1.10;
			player.specAccuracy = 1.5;
			player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			player.specEffect = 3;
			break;

		case 11698:
			player.startAnimation(4303);
//			c.gfx0(1220);
			player.specAccuracy = 1.25;
			player.getActionSender().sendMessage("actived");
			player.specEffect = 4;
			player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			break;

		case 1249:
			player.startAnimation(405);
			player.gfx100(253);
			if (player.playerIndex > 0) {
				Player o = (Player) PlayerHandler.players[i];
				o.getPA().getSpeared(player.absX, player.absY);
			}
			break;

		case 3204: // d hally
			player.gfx100(282);
			player.startAnimation(1203);
			player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			if (NPCHandler.npcs[i] != null && player.npcIndex > 0) {
				if (!player.goodDistance(player.getX(), player.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 1)) {
					player.doubleHit = true;
				}
			}
			if (PlayerHandler.players[i] != null && player.playerIndex > 0) {
				if (!player.goodDistance(player.getX(), player.getY(), PlayerHandler.players[i].getX(),
						PlayerHandler.players[i].getY(), 1)) {
					player.doubleHit = true;
					player.delayedDamage2 = Misc.random(calculateMeleeMaxHit());
				}
			}
			break;

		case 4153: // maul
			player.startAnimation(1667);
			player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			/*
			 * if (c.playerIndex > 0) gmaulPlayer(i); else gmaulNpc(i);
			 */
			player.gfx100(337);
			break;

		case 4587: // dscimmy
			player.gfx100(347);
			player.specEffect = 1;
			player.startAnimation(1872);
			player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			break;

		case 1434: // mace
			player.startAnimation(1060);
			player.gfx100(251);
			player.specMaxHitIncrease = 3;
			player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase()) + 1;
			player.specDamage = 1.35;
			player.specAccuracy = 1.15;
			break;

		case 859: // magic long
			player.usingBow = true;
			player.bowSpecShot = 3;
			player.rangeItemUsed = player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()];
			player.getItems().deleteArrow();
			player.lastWeaponUsed = weapon;
			player.startAnimation(426);
			player.gfx100(250);
			player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			player.projectileStage = 1;
			if (player.fightMode == 2)
				player.attackTimer--;
			break;

		case 861: // magic short
			player.usingBow = true;
			player.bowSpecShot = 1;
			player.rangeItemUsed = player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()];
			player.getItems().deleteArrow();
			player.lastWeaponUsed = weapon;
			player.startAnimation(1074);
			player.hitDelay = 3;
			player.projectileStage = 1;
			player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			if (player.fightMode == 2)
				player.attackTimer--;
			if (player.playerIndex > 0)
				fireProjectilePlayer();
			else if (player.npcIndex > 0)
				fireProjectileNpc();
			break;

		case 11235: // dark bow
			player.usingBow = true;
			player.dbowSpec = true;
			player.rangeItemUsed = player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()];
			player.getItems().deleteArrow();
			player.getItems().deleteArrow();
			player.lastWeaponUsed = weapon;
			player.hitDelay = 3;
			player.startAnimation(426);
			player.projectileStage = 1;
			player.gfx100(getRangeStartGFX());
			player.hitDelay = getHitDelay(player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			if (player.fightMode == 2)
				player.attackTimer--;
			if (player.playerIndex > 0)
				fireProjectilePlayer();
			else if (player.npcIndex > 0)
				fireProjectileNpc();
			player.specAccuracy = 1.75;
			player.specDamage = 1.50;
			break;
		}
		player.delayedDamage = Misc.random(calculateMeleeMaxHit());
		player.delayedDamage2 = Misc.random(calculateMeleeMaxHit());
		player.usingSpecial = false;
		player.getItems().updateSpecialBar();
	}

	public boolean checkSpecAmount(int weapon) {
		switch (weapon) {
		case 1249:
		case 1215:
		case 1231:
		case 5680:
		case 5698:
		case 1305:
		case 1434:
			if (player.specAmount >= 2.5) {
				player.specAmount -= 2.5;
				player.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 4151:
		case 11694:
		case 11698:
		case 4153:
			if (player.specAmount >= 5) {
				player.specAmount -= 5;
				player.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 3204:
			if (player.specAmount >= 3) {
				player.specAmount -= 3;
				player.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 1377:
		case 11696:
		case 11730:
			if (player.specAmount >= 10) {
				player.specAmount -= 10;
				player.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		case 4587:
		case 859:
		case 861:
		case 11235:
		case 11700:
			if (player.specAmount >= 5.5) {
				player.specAmount -= 5.5;
				player.getItems().addSpecialBar(weapon);
				return true;
			}
			return false;

		default:
			return true; // incase u want to test a weapon
		}
	}

	public void resetPlayerAttack() {
		player.usingMagic = false;
		player.npcIndex = 0;
		player.faceUpdate(0);
		player.playerIndex = 0;
		player.getPA().resetFollow();
		// c.sendMessage("Reset attack.");
	}

	public int getCombatDifference(int combat1, int combat2) {
		if (combat1 > combat2) {
			return (combat1 - combat2);
		}
		if (combat2 > combat1) {
			return (combat2 - combat1);
		}
		return 0;
	}

	/**
	 * Get killer id
	 **/

	public int getKillerId(int playerId) {
		int oldDamage = 0;
		int count = 0;
		int killerId = 0;
		for (int i = 1; i < Constants.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].killedBy == playerId) {

					PlayerHandler.players[i].killedBy = 0;
				}
			}
		}
		return killerId;
	}

	public void reducePrayerLevel() {
		if (player.playerLevel[5] - 1 > 0) {
			player.playerLevel[5] -= 1;
		} else {
			player.getActionSender().sendMessage("You have run out of prayer points!");
			player.playerLevel[5] = 0;
			resetPrayers();
			player.prayerId = -1;
		}
		player.getPA().refreshSkill(5);
	}

	public void resetPrayers() {
		for (int i = 0; i < player.prayerActive.length; i++) {
			player.prayerActive[i] = false;
			player.getActionSender().sendConfig(player.PRAYER_GLOW[i], 0);
		}
		player.headIcon = -1;
		player.getPA().requestUpdates();
	}

	/**
	 * Wildy and duel info
	 **/

	public boolean checkReqs() {
		if (PlayerHandler.players[player.playerIndex] == null) {
			return false;
		}
		if (CastleWars.isInCw(player)) {
			return true;
		}
		if (player.playerIndex == player.playerId)
			return false;
		if (PlayerHandler.players[player.playerIndex].inDuelArena() && player.duelStatus != 5 && !player.usingMagic) {
			if (player.arenas() || player.duelStatus == 5) {
				player.getActionSender().sendMessage("You can't challenge inside the arena!");
				return false;
			}
			player.getTradeAndDuel().requestDuel(player.playerIndex);
			return false;
		}
		if (player.duelStatus == 5 && PlayerHandler.players[player.playerIndex].duelStatus == 5) {
			if (PlayerHandler.players[player.playerIndex].duelingWith == player.getId()) {
				return true;
			} else {
				player.getActionSender().sendMessage("This isn't your opponent!");
				return false;
			}
		}
		if (!PlayerHandler.players[player.playerIndex].inWild()) {
			player.getActionSender().sendMessage("That player is not in the wilderness.");
			player.stopMovement();
			player.getCombat().resetPlayerAttack();
			return false;
		}
		if (!player.inWild()) {
			player.getActionSender().sendMessage("You are not in the wilderness.");
			player.stopMovement();
			player.getCombat().resetPlayerAttack();
			return false;
		}
		if (Constants.COMBAT_LEVEL_DIFFERENCE) {
			int combatDif1 = player.getCombat().getCombatDifference(player.combatLevel,
					PlayerHandler.players[player.playerIndex].combatLevel);
			if (combatDif1 > player.wildLevel || combatDif1 > PlayerHandler.players[player.playerIndex].wildLevel) {
				player.getActionSender().sendMessage("Your combat level difference is too great to attack that player here.");
				player.stopMovement();
				player.getCombat().resetPlayerAttack();
				return false;
			}
		}

		if (Constants.SINGLE_AND_MULTI_ZONES) {
			if (!PlayerHandler.players[player.playerIndex].inMulti()) { // single
																	// combat
																	// zones
				if (PlayerHandler.players[player.playerIndex].underAttackBy != player.playerId
						&& PlayerHandler.players[player.playerIndex].underAttackBy != 0) {
					player.getActionSender().sendMessage("That player is already in combat.");
					player.stopMovement();
					player.getCombat().resetPlayerAttack();
					return false;
				}
				if (PlayerHandler.players[player.playerIndex].playerId != player.underAttackBy && player.underAttackBy != 0
						|| player.underAttackBy2 > 0) {
					player.getActionSender().sendMessage("You are already in combat.");
					player.stopMovement();
					player.getCombat().resetPlayerAttack();
					return false;
				}
			}
		}
		return true;
	}

	public boolean checkMultiBarrageReqs(int i) {
		if (PlayerHandler.players[i] == null) {
			return false;
		}
		if (i == player.playerId)
			return false;
		if (!PlayerHandler.players[i].inWild()) {
			return false;
		}
		if (Constants.COMBAT_LEVEL_DIFFERENCE) {
			int combatDif1 = player.getCombat().getCombatDifference(player.combatLevel, PlayerHandler.players[i].combatLevel);
			if (combatDif1 > player.wildLevel || combatDif1 > PlayerHandler.players[i].wildLevel) {
				player.getActionSender().sendMessage("Your combat level difference is too great to attack that player here.");
				return false;
			}
		}

		if (Constants.SINGLE_AND_MULTI_ZONES) {
			if (!PlayerHandler.players[i].inMulti()) { // single combat zones
				if (PlayerHandler.players[i].underAttackBy != player.playerId
						&& PlayerHandler.players[i].underAttackBy != 0) {
					return false;
				}
				if (PlayerHandler.players[i].playerId != player.underAttackBy && player.underAttackBy != 0) {
					player.getActionSender().sendMessage("You are already in combat.");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Weapon stand, walk, run, etc emotes
	 **/

	public void getPlayerAnimIndex(String weaponName) {
		player.playerStandIndex = 0x328;
		player.playerTurnIndex = 0x337;
		player.playerWalkIndex = 0x333;
		player.playerTurn180Index = 0x334;
		player.playerTurn90CWIndex = 0x335;
		player.playerTurn90CCWIndex = 0x336;
		player.playerRunIndex = 0x338;

		if (weaponName.contains("halberd") || weaponName.contains("guthan")) {
			player.playerStandIndex = 809;
			player.playerWalkIndex = 1146;
			player.playerRunIndex = 1210;
			return;
		}
		if (weaponName.contains("dharok")) {
			player.playerStandIndex = 0x811;
			player.playerWalkIndex = 0x67F;
			player.playerRunIndex = 0x680;
			return;
		}
		if (weaponName.contains("ahrim")) {
			player.playerStandIndex = 809;
			player.playerWalkIndex = 1146;
			player.playerRunIndex = 1210;
			return;
		}
		if (weaponName.contains("verac")) {
			player.playerStandIndex = 1832;
			player.playerWalkIndex = 1830;
			player.playerRunIndex = 1831;
			return;
		}
		if (weaponName.contains("wand") || weaponName.contains("staff")) {
			player.playerStandIndex = 809;
			player.playerRunIndex = 1210;
			player.playerWalkIndex = 1146;
			return;
		}
		if (weaponName.contains("karil")) {
			player.playerStandIndex = 2074;
			player.playerWalkIndex = 2076;
			player.playerRunIndex = 2077;
			return;
		}
		if (weaponName.contains("2h sword") || weaponName.contains("godsword") || weaponName.contains("saradomin sw")) {
			player.playerStandIndex = 4300;
			player.playerWalkIndex = 4306;
			player.playerRunIndex = 4305;
			return;
		}
		if (weaponName.contains("bow")) {
			player.playerStandIndex = 808;
			player.playerWalkIndex = 819;
			player.playerRunIndex = 824;
			return;
		}

		switch (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]) {
		case 4151:
			player.playerStandIndex = 1832;
			player.playerWalkIndex = 1660;
			player.playerRunIndex = 1661;
			break;
		case 6528:
			player.playerStandIndex = 0x811;
			player.playerWalkIndex = 2064;
			player.playerRunIndex = 1664;
			break;
		case 4153:
			player.playerStandIndex = 1662;
			player.playerWalkIndex = 1663;
			player.playerRunIndex = 1664;
			break;
		case 11694:
		case 11696:
		case 11730:
		case 11698:
		case 11700:
			player.playerStandIndex = 4300;
			player.playerWalkIndex = 4306;
			player.playerRunIndex = 4305;
			break;
		case 1305:
			player.playerStandIndex = 809;
			break;
		}
	}

	/**
	 * Weapon emotes
	 **/

	public int getWepAnim(String weaponName) {
		if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] <= 0) {
			switch (player.fightMode) {
			case 0:
				return 422;
			case 2:
				return 423;
			case 1:
				return 451;
			}
		}
		if (weaponName.contains("knife") || weaponName.contains("Death-tocuh Dart") || weaponName.contains("dart")
				|| weaponName.contains("javelin") || weaponName.contains("thrownaxe")) {
			return 806;
		}
		if (weaponName.contains("halberd")) {
			return 440;
		}
		if (weaponName.startsWith("dragon dagger")) {
			return 402;
		}
		if (weaponName.endsWith("dagger")) {
			return 412;
		}
		if (weaponName.contains("2h sword") || weaponName.contains("godsword")
				|| weaponName.contains("aradomin sword")) {
			return 4307;
		}
		if (weaponName.contains("sword")) {
			return 451;
		}
		if (weaponName.contains("karil")) {
			return 2075;
		}
		if (weaponName.contains("bow") && !weaponName.contains("'bow")) {
			return 426;
		}
		if (weaponName.contains("'bow"))
			return 4230;

		switch (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]) { // if you don't want to use
														// strings
		case 6522:
			return 2614;
		case 4153: // granite maul
			return 1665;
		case 4726: // guthan
			return 2080;
		case 4747: // torag
			return 0x814;
		case 4718: // dharok
			return 2067;
		case 4710: // ahrim
			return 406;
		case 4755: // verac
			return 2062;
		case 4734: // karil
			return 2075;
		case 4151:
			return 1658;
		case 6528:
			return 2661;
		default:
			return 451;
		}
	}

	/**
	 * Block emotes
	 */
	public int getBlockEmote() {
		if (player.playerEquipment[EquipmentListener.SHIELD_SLOT.getSlot()] >= 8844 && player.playerEquipment[EquipmentListener.SHIELD_SLOT.getSlot()] <= 8850) {
			return 4177;
		}
		switch (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]) {
		case 4755:
			return 2063;

		case 4153:
			return 1666;

		case 4151:
			return 1659;

		case 11694:
		case 11698:
		case 11700:
		case 11696:
		case 11730:
			return -1;
		default:
			return 404;
		}
	}

	/**
	 * Weapon and magic attack speed!
	 **/

	public int getAttackDelay(String s) {
		if (player.usingMagic) {
			switch (Enchantment.MAGIC_SPELLS[player.spellId][0]) {
			case 12871: // ice blitz
			case 13023: // shadow barrage
			case 12891: // ice barrage
				return 5;

			default:
				return 5;
			}
		}
		if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == -1)
			return 4;// unarmed

		switch (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]) {
		case 11235:
			return 9;
		case 11730:
			return 4;
		case 6528:
			return 7;
		}

		if (s.endsWith("greataxe"))
			return 7;
		else if (s.equals("torags hammers"))
			return 5;
		else if (s.equals("guthans warspear"))
			return 5;
		else if (s.equals("veracs flail"))
			return 5;
		else if (s.equals("ahrims staff"))
			return 6;
		else if (s.contains("staff")) {
			if (s.contains("zamarok") || s.contains("guthix") || s.contains("saradomian") || s.contains("slayer")
					|| s.contains("ancient"))
				return 4;
			else
				return 5;
		} else if (s.contains("bow")) {
			if (s.contains("composite") || s.equals("seercull"))
				return 5;
			else if (s.contains("aril"))
				return 4;
			else if (s.contains("Ogre"))
				return 8;
			else if (s.contains("short") || s.contains("hunt") || s.contains("sword"))
				return 4;
			else if (s.contains("long") || s.contains("crystal"))
				return 6;
			else if (s.contains("'bow"))
				return 7;

			return 5;
		} else if (s.contains("dagger"))
			return 4;
		else if (s.contains("godsword") || s.contains("2h"))
			return 6;
		else if (s.contains("longsword"))
			return 5;
		else if (s.contains("sword"))
			return 4;
		else if (s.contains("scimitar"))
			return 4;
		else if (s.contains("mace"))
			return 5;
		else if (s.contains("battleaxe"))
			return 6;
		else if (s.contains("pickaxe"))
			return 5;
		else if (s.contains("thrownaxe"))
			return 5;
		else if (s.contains("axe"))
			return 5;
		else if (s.contains("warhammer"))
			return 6;
		else if (s.contains("2h"))
			return 7;
		else if (s.contains("spear"))
			return 5;
		else if (s.contains("claw"))
			return 4;
		else if (s.contains("halberd"))
			return 7;

		// sara sword, 2400ms
		else if (s.equals("granite maul"))
			return 7;
		else if (s.equals("toktz-xil-ak"))// sword
			return 4;
		else if (s.equals("tzhaar-ket-em"))// mace
			return 5;
		else if (s.equals("tzhaar-ket-om"))// maul
			return 7;
		else if (s.equals("toktz-xil-ek"))// knife
			return 4;
		else if (s.equals("toktz-xil-ul"))// rings
			return 4;
		else if (s.equals("toktz-mej-tal"))// staff
			return 6;
		else if (s.contains("whip"))
			return 4;
		else if (s.contains("dart"))
			return 3;
		else if (s.contains("knife"))
			return 3;
		else if (s.contains("javelin"))
			return 6;
		return 5;
	}

	/**
	 * How long it takes to hit your enemy
	 **/
	public int getHitDelay(String weaponName) {
		if (player.usingMagic) {
			switch (Enchantment.MAGIC_SPELLS[player.spellId][0]) {
			case 12891:
				return 4;
			case 12871:
				return 6;
			default:
				return 4;
			}
		} else {

			if (weaponName.contains("knife") || weaponName.contains("Death-Touch Dart") || weaponName.contains("dart")
					|| weaponName.contains("javelin") || weaponName.contains("thrownaxe")) {
				return 3;
			}
			if (weaponName.contains("cross") || weaponName.contains("c'bow")) {
				return 4;
			}
			if (weaponName.contains("bow") && !player.dbowSpec) {
				return 4;
			} else if (player.dbowSpec) {
				return 4;
			}

			switch (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]) {
			case 6522: // Toktz-xil-ul
				return 3;

			default:
				return 2;
			}
		}
	}

	public int getRequiredDistance() {
		if (player.followId > 0 && player.freezeTimer <= 0 && !player.isMoving)
			return 2;
		else if (player.followId > 0 && player.freezeTimer <= 0 && player.isMoving) {
			return 3;
		} else {
			return 1;
		}
	}

	public boolean usingHally() {
		switch (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]) {
		case 3190:
		case 3192:
		case 3194:
		case 3196:
		case 3198:
		case 3200:
		case 3202:
		case 3204:
			return true;

		default:
			return false;
		}
	}

	/**
	 * Melee
	 **/

	public int calculateMeleeAttack() {
		int attackLevel = player.playerLevel[0];
		// 2, 5, 11, 18, 19
		if (player.prayerActive[2]) {
			attackLevel += player.getLevelForXP(player.playerXP[SkillIndex.ATTACK.getSkillId()]) * 0.05;
		} else if (player.prayerActive[7]) {
			attackLevel += player.getLevelForXP(player.playerXP[SkillIndex.ATTACK.getSkillId()]) * 0.1;
		} else if (player.prayerActive[15]) {
			attackLevel += player.getLevelForXP(player.playerXP[SkillIndex.ATTACK.getSkillId()]) * 0.15;
		} else if (player.prayerActive[24]) {
			attackLevel += player.getLevelForXP(player.playerXP[SkillIndex.ATTACK.getSkillId()]) * 0.15;
		} else if (player.prayerActive[25]) {
			attackLevel += player.getLevelForXP(player.playerXP[SkillIndex.ATTACK.getSkillId()]) * 0.2;
		}
		if (player.fullVoidMelee())
			attackLevel += player.getLevelForXP(player.playerXP[SkillIndex.ATTACK.getSkillId()]) * 0.1;
		attackLevel *= player.specAccuracy;
		// c.sendMessage("Attack: " + (attackLevel +
		// (c.playerBonus[bestMeleeAtk()] * 2)));
		int i = player.playerBonus[bestMeleeAtk()];
		i += player.bonusAttack;
		if (player.playerEquipment[EquipmentListener.AMULET_SLOT.getSlot()] == 11128 && player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 6528) {
			i *= 1.30;
		}
		return (int) (attackLevel + (attackLevel * 0.15) + (i + i * 0.05));
	}

	public int bestMeleeAtk() {
		if (player.playerBonus[0] > player.playerBonus[1] && player.playerBonus[0] > player.playerBonus[2])
			return 0;
		if (player.playerBonus[1] > player.playerBonus[0] && player.playerBonus[1] > player.playerBonus[2])
			return 1;
		return player.playerBonus[2] <= player.playerBonus[1] || player.playerBonus[2] <= player.playerBonus[0] ? 0 : 2;
	}

	public int calculateMeleeMaxHit() {
		double maxHit = 0;
		int strBonus = player.playerBonus[10];
		int strength = player.playerLevel[2];
		int lvlForXP = player.getLevelForXP(player.playerXP[2]);
		if (player.prayerActive[1]) {
			strength += (int) (lvlForXP * .05);
		} else if (player.prayerActive[6]) {
			strength += (int) (lvlForXP * .10);
		} else if (player.prayerActive[14]) {
			strength += (int) (lvlForXP * .15);
		} else if (player.prayerActive[24]) {
			strength += (int) (lvlForXP * .18);
		} else if (player.prayerActive[25]) {
			strength += (int) (lvlForXP * .23);
		}
		if (player.playerEquipment[EquipmentListener.HAT_SLOT.getSlot()] == 2526 && player.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 2520
				&& player.playerEquipment[EquipmentListener.LEGS_SLOT.getSlot()] == 2522) {
			maxHit += (maxHit * 10 / 100);
		}
		maxHit += 1.05D + strBonus * strength * 0.00175D;
		maxHit += strength * 0.11D;
		if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4718 && player.playerEquipment[EquipmentListener.HAT_SLOT.getSlot()] == 4716
				&& player.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 4720 && player.playerEquipment[EquipmentListener.LEGS_SLOT.getSlot()] == 4722) {
			maxHit += (player.getPA().getLevelForXP(player.playerXP[3]) - player.playerLevel[3]) / 2;
		}
		if (player.specDamage > 1)
			maxHit = (int) (maxHit * player.specDamage);
		if (maxHit < 0)
			maxHit = 1;
		if (player.fullVoidMelee())
			maxHit = (int) (maxHit * 1.10);
		if (player.playerEquipment[EquipmentListener.AMULET_SLOT.getSlot()] == 11128 && player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 6528) {
			maxHit *= 1.20;
		}
		return (int) Math.floor(maxHit);
	}

	public int calculateMeleeDefence() {
		int defenceLevel = player.playerLevel[1];
		int i = player.playerBonus[bestMeleeDef()];
		if (player.prayerActive[0]) {
			defenceLevel += player.getLevelForXP(player.playerXP[SkillIndex.DEFENCE.getSkillId()]) * 0.05;
		} else if (player.prayerActive[5]) {
			defenceLevel += player.getLevelForXP(player.playerXP[SkillIndex.DEFENCE.getSkillId()]) * 0.1;
		} else if (player.prayerActive[13]) {
			defenceLevel += player.getLevelForXP(player.playerXP[SkillIndex.DEFENCE.getSkillId()]) * 0.15;
		} else if (player.prayerActive[24]) {
			defenceLevel += player.getLevelForXP(player.playerXP[SkillIndex.DEFENCE.getSkillId()]) * 0.2;
		} else if (player.prayerActive[25]) {
			defenceLevel += player.getLevelForXP(player.playerXP[SkillIndex.DEFENCE.getSkillId()]) * 0.25;
		}
		return (int) (defenceLevel + (defenceLevel * 0.15) + (i + i * 0.05));
	}

	public int bestMeleeDef() {
		if (player.playerBonus[5] > player.playerBonus[6] && player.playerBonus[5] > player.playerBonus[7])
			return 5;
		if (player.playerBonus[6] > player.playerBonus[5] && player.playerBonus[6] > player.playerBonus[7])
			return 6;
		return player.playerBonus[7] <= player.playerBonus[5] || player.playerBonus[7] <= player.playerBonus[6] ? 5 : 7;
	}

	/**
	 * Range
	 **/

	public int calculateRangeAttack() {
		int attackLevel = player.playerLevel[4];
		attackLevel *= player.specAccuracy;
		if (player.fullVoidRange())
			attackLevel += player.getLevelForXP(player.playerXP[SkillIndex.RANGE.getSkillId()]) * 0.1;
		if (player.prayerActive[3])
			attackLevel *= 1.05;
		else if (player.prayerActive[11])
			attackLevel *= 1.10;
		else if (player.prayerActive[19])
			attackLevel *= 1.15;
		// dbow spec
		if (player.fullVoidRange() && player.specAccuracy > 1.15) {
			attackLevel *= 1.75;
		}
		return (int) (attackLevel + (player.playerBonus[4] * 1.95));
	}

	public int calculateRangeDefence() {
		int defenceLevel = player.playerLevel[1];
		if (player.prayerActive[0]) {
			defenceLevel += player.getLevelForXP(player.playerXP[SkillIndex.DEFENCE.getSkillId()]) * 0.05;
		} else if (player.prayerActive[5]) {
			defenceLevel += player.getLevelForXP(player.playerXP[SkillIndex.DEFENCE.getSkillId()]) * 0.1;
		} else if (player.prayerActive[13]) {
			defenceLevel += player.getLevelForXP(player.playerXP[SkillIndex.DEFENCE.getSkillId()]) * 0.15;
		} else if (player.prayerActive[24]) {
			defenceLevel += player.getLevelForXP(player.playerXP[SkillIndex.DEFENCE.getSkillId()]) * 0.2;
		} else if (player.prayerActive[25]) {
			defenceLevel += player.getLevelForXP(player.playerXP[SkillIndex.DEFENCE.getSkillId()]) * 0.25;
		}
		return defenceLevel + player.playerBonus[9] + (player.playerBonus[9] / 2);
	}

	public boolean usingBolts() {
		return player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] >= 9130 && player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] <= 9145
				|| player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] >= 9230 && player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] <= 9245;
	}

	public int rangeMaxHit() {
		int rangeLevel = player.playerLevel[4];
		double modifier = 1.0;
		double wtf = player.specDamage;
		int itemUsed = player.usingBow ? player.lastArrowUsed : player.lastWeaponUsed;
		if (player.prayerActive[3])
			modifier += 0.05;
		else if (player.prayerActive[11])
			modifier += 0.10;
		else if (player.prayerActive[19])
			modifier += 0.15;
		if (player.fullVoidRange())
			modifier += 0.20;
		double c = modifier * rangeLevel;
		int rangeStr = getRangeStr(itemUsed);
		double max = (c + 8) * (rangeStr + 64) / 640;
		if (wtf != 1)
			max *= wtf;
		if (max < 1)
			max = 1;
		return (int) max;
	}

	public int getRangeStr(int i) {
		if (i == 4214)
			return 70;
		switch (i) {
		// bronze to rune bolts
		case 877:
			return 10;
		case 9140:
			return 46;
		case 9141:
			return 64;
		case 9142:
		case 9241:
		case 9240:
			return 82;
		case 9143:
		case 9243:
		case 9242:
			return 100;
		case 9144:
		case 9244:
		case 9245:
			return 115;
		// bronze to dragon arrows
		case 882:
			return 7;
		case 884:
			return 10;
		case 886:
			return 16;
		case 888:
			return 22;
		case 890:
			return 31;
		case 892:
		case 4740:
			return 49;
		case 11212:
			return 60;
		// knifes
		case 864:
			return 3;
		case 863:
			return 4;
		case 865:
			return 7;
		case 866:
			return 10;
		case 867:
			return 14;
		case 868:
			return 24;
		case 5641:
			return 99999;
		}
		return 0;
	}

	/*
	 * public int rangeMaxHit() { int rangehit = 0; rangehit += c.playerLevel[4]
	 * / 7.5; int weapon = c.lastWeaponUsed; int Arrows = c.lastArrowUsed; if
	 * (weapon == 4223) {//Cbow 1/10 rangehit = 2; rangehit += c.playerLevel[4]
	 * / 7; } else if (weapon == 4222) {//Cbow 2/10 rangehit = 3; rangehit +=
	 * c.playerLevel[4] / 7; } else if (weapon == 4221) {//Cbow 3/10 rangehit =
	 * 3; rangehit += c.playerLevel[4] / 6.5; } else if (weapon == 4220) {//Cbow
	 * 4/10 rangehit = 4; rangehit += c.playerLevel[4] / 6.5; } else if (weapon
	 * == 4219) {//Cbow 5/10 rangehit = 4; rangehit += c.playerLevel[4] / 6; }
	 * else if (weapon == 4218) {//Cbow 6/10 rangehit = 5; rangehit +=
	 * c.playerLevel[4] / 6; } else if (weapon == 4217) {//Cbow 7/10 rangehit =
	 * 5; rangehit += c.playerLevel[4] / 5.5; } else if (weapon == 4216) {//Cbow
	 * 8/10 rangehit = 6; rangehit += c.playerLevel[4] / 5.5; } else if (weapon
	 * == 4215) {//Cbow 9/10 rangehit = 6; rangehit += c.playerLevel[4] / 5; }
	 * else if (weapon == 4214) {//Cbow Full rangehit = 7; rangehit +=
	 * c.playerLevel[4] / 5; } else if (weapon == 6522) { rangehit = 5; rangehit
	 * += c.playerLevel[4] / 6; } else if (weapon == 9029) {//dragon darts
	 * rangehit = 8; rangehit += c.playerLevel[4] / 10; } else if (weapon == 811
	 * || weapon == 868) {//rune darts rangehit = 2; rangehit +=
	 * c.playerLevel[4] / 8.5; } else if (weapon == 810 || weapon == 867)
	 * {//adamant darts rangehit = 2; rangehit += c.playerLevel[4] / 9; } else
	 * if (weapon == 809 || weapon == 866) {//mithril darts rangehit = 2;
	 * rangehit += c.playerLevel[4] / 9.5; } else if (weapon == 808 || weapon ==
	 * 865) {//Steel darts rangehit = 2; rangehit += c.playerLevel[4] / 10; }
	 * else if (weapon == 807 || weapon == 863) {//Iron darts rangehit = 2;
	 * rangehit += c.playerLevel[4] / 10.5; } else if (weapon == 806 || weapon
	 * == 864) {//Bronze darts rangehit = 1; rangehit += c.playerLevel[4] / 11;
	 * } else if (Arrows == 4740 && weapon == 4734) {//BoltRacks rangehit = 3;
	 * rangehit += c.playerLevel[4] / 6; } else if (Arrows == 11212) {//dragon
	 * arrows rangehit = 4; rangehit += c.playerLevel[4] / 5.5; } else if
	 * (Arrows == 892) {//rune arrows rangehit = 3; rangehit += c.playerLevel[4]
	 * / 6; } else if (Arrows == 890) {//adamant arrows rangehit = 2; rangehit
	 * += c.playerLevel[4] / 7; } else if (Arrows == 888) {//mithril arrows
	 * rangehit = 2; rangehit += c.playerLevel[4] / 7.5; } else if (Arrows ==
	 * 886) {//steel arrows rangehit = 2; rangehit += c.playerLevel[4] / 8; }
	 * else if (Arrows == 884) {//Iron arrows rangehit = 2; rangehit +=
	 * c.playerLevel[4] / 9; } else if (Arrows == 882) {//Bronze arrows rangehit
	 * = 1; rangehit += c.playerLevel[4] / 9.5; } else if (Arrows == 9244) {
	 * rangehit = 8; rangehit += c.playerLevel[4] / 3; } else if (Arrows ==
	 * 9139) { rangehit = 12; rangehit += c.playerLevel[4] / 4; } else if
	 * (Arrows == 9140) { rangehit = 2; rangehit += c.playerLevel[4] / 7; } else
	 * if (Arrows == 9141) { rangehit = 3; rangehit += c.playerLevel[4] / 6; }
	 * else if (Arrows == 9142) { rangehit = 4; rangehit += c.playerLevel[4] /
	 * 6; } else if (Arrows == 9143) { rangehit = 7; rangehit +=
	 * c.playerLevel[4] / 5; } else if (Arrows == 9144) { rangehit = 7; rangehit
	 * += c.playerLevel[4] / 4.5; } int bonus = 0; bonus -= rangehit / 10;
	 * rangehit += bonus; if (c.specDamage != 1) rangehit *= c.specDamage; if
	 * (rangehit == 0) rangehit++; if (c.fullVoidRange()) { rangehit *= 1.10; }
	 * if (c.prayerActive[3]) rangehit *= 1.05; else if (c.prayerActive[11])
	 * rangehit *= 1.10; else if (c.prayerActive[19]) rangehit *= 1.15; return
	 * rangehit; }
	 */

	public boolean properBolts() {
		return player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] >= 9140 && player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] <= 9144
				|| player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] >= 9240 && player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] <= 9244;
				
	}

	public int correctBowAndArrows() {
		if (usingBolts())
			return -1;
		switch (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]) {

		case 839:
		case 841:
			return 882;

		case 843:
		case 845:
			return 884;

		case 847:
		case 849:
			return 886;

		case 851:
		case 853:
			return 888;

		case 855:
		case 857:
			return 890;

		case 859:
		case 861:
			return 892;

		case 4734:
		case 4935:
		case 4936:
		case 4937:
			return 4740;

		case 11235:
			return 11212;
		}
		return -1;
	}

	public int getRangeStartGFX() {
		switch (player.rangeItemUsed) {

		case 863:
			return 220;
		case 864:
			return 219;
		case 865:
			return 221;
		case 866: // knives
			return 223;
		case 867:
			return 224;
		case 868:
			return 225;
		case 869:
			return 222;

		case 806:
			return 232;
		case 807:
			return 233;
		case 808:
			return 234;
		case 809: // darts
		case 5641:
			return 235;
		case 810:
			return 236;
		case 811:
			return 237;

		case 825:
			return 206;
		case 826:
			return 207;
		case 827: // javelin
			return 208;
		case 828:
			return 209;
		case 829:
			return 210;
		case 830:
			return 211;

		case 800:
			return 42;
		case 801:
			return 43;
		case 802:
			return 44; // axes
		case 803:
			return 45;
		case 804:
			return 46;
		case 805:
			return 48;

		case 882:
			return 19;

		case 884:
			return 18;

		case 886:
			return 20;

		case 888:
			return 21;

		case 890:
			return 22;

		case 892:
			return 24;

		case 11212:
			return 26;

		case 4212:
		case 4214:
		case 4215:
		case 4216:
		case 4217:
		case 4218:
		case 4219:
		case 4220:
		case 4221:
		case 4222:
		case 4223:
			return 250;

		}
		return -1;
	}

	public int getRangeProjectileGFX() {
		if (player.dbowSpec) {
			return 672;
		}
		if (player.bowSpecShot > 0) {
			switch (player.rangeItemUsed) {
			default:
				return 249;
			}
		}
		if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 9185)
			return 27;
		switch (player.rangeItemUsed) {

		case 863:
			return 213;
		case 864:
			return 212;
		case 865:
			return 214;
		case 866: // knives
			return 216;
		case 867:
			return 217;
		case 868:
			return 218;
		case 869:
			return 215;

		case 806:
			return 226;
		case 807:
			return 227;
		case 808:
			return 228;
		case 809: // darts
		case 5641:
			return 229;
		case 810:
			return 230;
		case 811:
			return 231;

		case 825:
			return 200;
		case 826:
			return 201;
		case 827: // javelin
			return 202;
		case 828:
			return 203;
		case 829:
			return 204;
		case 830:
			return 205;

		case 6522: // Toktz-xil-ul
			return 442;

		case 800:
			return 36;
		case 801:
			return 35;
		case 802:
			return 37; // axes
		case 803:
			return 38;
		case 804:
			return 39;
		case 805:
			return 40;

		case 882:
			return 10;

		case 884:
			return 9;

		case 886:
			return 11;

		case 888:
			return 12;

		case 890:
			return 13;

		case 892:
			return 15;

		case 11212:
			return 17;

		case 4740: // bolt rack
			return 27;

		case 4212:
		case 4214:
		case 4215:
		case 4216:
		case 4217:
		case 4218:
		case 4219:
		case 4220:
		case 4221:
		case 4222:
		case 4223:
			return 249;

		}
		return -1;
	}

	public int getProjectileSpeed() {
		if (player.dbowSpec)
			return 100;
		return 70;
	}

	public int getProjectileShowDelay() {
		switch (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]) {
		case 863:
		case 864:
		case 865:
		case 866: // knives
		case 867:
		case 868:
		case 869:

		case 806:
		case 807:
		case 808:
		case 809: // darts
		case 810:
		case 811:
		case 5641:

		case 825:
		case 826:
		case 827: // javelin
		case 828:
		case 829:
		case 830:

		case 800:
		case 801:
		case 802:
		case 803: // axes
		case 804:
		case 805:

		case 4734:
		case 9185:
		case 4935:
		case 4936:
		case 4937:
			return 15;

		default:
			return 5;
		}
	}

	/**
	 * MAGIC
	 **/

	public boolean canHitMage(Player o) {
		return Misc.random(mageAtk()) > Misc.random(o.getCombat().mageDef());
	}

	public int mageAtk() {
		int attackLevel = player.playerLevel[6];
		if (player.fullVoidMage())
			attackLevel = (int) (attackLevel + player.getLevelForXP(player.playerXP[6]) * 0.20000000000000001D);
		if (player.prayerActive[4])
			attackLevel = (int) (attackLevel * 1.05D);
		else if (player.prayerActive[12])
			attackLevel = (int) (attackLevel * 1.1000000000000001D);
		else if (player.prayerActive[20])
			attackLevel = (int) (attackLevel * 1.1499999999999999D);
		return (int) (attackLevel + player.playerBonus[3] * 2.25D);
	}

	public int mageDef() {
		int defenceLevel = player.playerLevel[1] / 2 + player.playerLevel[6] / 2;
		if (player.prayerActive[4])
			defenceLevel = (int) (defenceLevel + player.getLevelForXP(player.playerXP[SkillIndex.DEFENCE.getSkillId()]) * 0.050000000000000003D);
		else if (player.prayerActive[12])
			defenceLevel = (int) (defenceLevel + player.getLevelForXP(player.playerXP[SkillIndex.DEFENCE.getSkillId()]) * 0.10000000000000001D);
		else if (player.prayerActive[20])
			defenceLevel = (int) (defenceLevel + player.getLevelForXP(player.playerXP[SkillIndex.DEFENCE.getSkillId()]) * 0.14999999999999999D);
		return defenceLevel + player.playerBonus[8] + player.playerBonus[8] / 4;
	}

	public boolean wearingStaff(int runeId) {
		int wep = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()];
		switch (runeId) {
		case 554:
			return wep == 1387;
		case 555:
			return wep == 1383;
		case 556:
			return wep == 1381;
		case 557:
			return wep == 1385;
		}
		return false;
	}

	public boolean checkMagicReqs(int spell) {
		if (player.usingMagic && Constants.RUNES_REQUIRED) { // check for runes
			if ((!player.getItems().playerHasItem(Enchantment.MAGIC_SPELLS[spell][8], Enchantment.MAGIC_SPELLS[spell][9])
					&& !wearingStaff(Enchantment.MAGIC_SPELLS[spell][8]))
					|| (!player.getItems().playerHasItem(Enchantment.MAGIC_SPELLS[spell][10], Enchantment.MAGIC_SPELLS[spell][11])
							&& !wearingStaff(Enchantment.MAGIC_SPELLS[spell][10]))
					|| (!player.getItems().playerHasItem(Enchantment.MAGIC_SPELLS[spell][12], Enchantment.MAGIC_SPELLS[spell][13])
							&& !wearingStaff(Enchantment.MAGIC_SPELLS[spell][12]))
					|| (!player.getItems().playerHasItem(Enchantment.MAGIC_SPELLS[spell][14], Enchantment.MAGIC_SPELLS[spell][15])
							&& !wearingStaff(Enchantment.MAGIC_SPELLS[spell][14]))) {
				player.getActionSender().sendMessage("You don't have the required runes to cast this spell.");
				return false;
			}
		}

		if (player.usingMagic && player.playerIndex > 0) {
			if (PlayerHandler.players[player.playerIndex] != null) {
				for (int r = 0; r < player.REDUCE_SPELLS.length; r++) {
					if (PlayerHandler.players[player.playerIndex].REDUCE_SPELLS[r] == Enchantment.MAGIC_SPELLS[spell][0]) {
						player.reduceSpellId = r;
						if ((System.currentTimeMillis()
								- PlayerHandler.players[player.playerIndex].reduceSpellDelay[player.reduceSpellId]) > PlayerHandler.players[player.playerIndex].REDUCE_SPELL_TIME[player.reduceSpellId]) {
							PlayerHandler.players[player.playerIndex].canUseReducingSpell[player.reduceSpellId] = true;
						} else {
							PlayerHandler.players[player.playerIndex].canUseReducingSpell[player.reduceSpellId] = false;
						}
						break;
					}
				}
				if (!PlayerHandler.players[player.playerIndex].canUseReducingSpell[player.reduceSpellId]) {
					player.getActionSender().sendMessage("That player is currently immune to this spell.");
					player.usingMagic = false;
					player.stopMovement();
					resetPlayerAttack();
					return false;
				}
			}
		}

		int staffRequired = getStaffNeeded();
		if (player.usingMagic && staffRequired > 0 && Constants.RUNES_REQUIRED) { // staff
																				// required
			if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] != staffRequired) {
				player.getActionSender().sendMessage(
						"You need a " + player.getItems().getItemName(staffRequired).toLowerCase() + " to cast this spell.");
				return false;
			}
		}

		if (player.usingMagic && Constants.MAGIC_LEVEL_REQUIRED) { // check magic
																// level
			if (player.playerLevel[6] < Enchantment.MAGIC_SPELLS[spell][1]) {
				player.getActionSender().sendMessage("You need to have a magic level of " + Enchantment.MAGIC_SPELLS[spell][1] + " to cast this spell.");
				return false;
			}
		}
		if (player.usingMagic && Constants.RUNES_REQUIRED) {
			if (Enchantment.MAGIC_SPELLS[spell][8] > 0) { // deleting runes
				if (!wearingStaff(Enchantment.MAGIC_SPELLS[spell][8]))
					player.getItems().deleteItem(Enchantment.MAGIC_SPELLS[spell][8],
							player.getItems().getItemSlot(Enchantment.MAGIC_SPELLS[spell][8]), Enchantment.MAGIC_SPELLS[spell][9]);
			}
			if (Enchantment.MAGIC_SPELLS[spell][10] > 0) {
				if (!wearingStaff(Enchantment.MAGIC_SPELLS[spell][10]))
					player.getItems().deleteItem(Enchantment.MAGIC_SPELLS[spell][10],
							player.getItems().getItemSlot(Enchantment.MAGIC_SPELLS[spell][10]), Enchantment.MAGIC_SPELLS[spell][11]);
			}
			if (Enchantment.MAGIC_SPELLS[spell][12] > 0) {
				if (!wearingStaff(Enchantment.MAGIC_SPELLS[spell][12]))
					player.getItems().deleteItem(Enchantment.MAGIC_SPELLS[spell][12],
							player.getItems().getItemSlot(Enchantment.MAGIC_SPELLS[spell][12]), Enchantment.MAGIC_SPELLS[spell][13]);
			}
			if (Enchantment.MAGIC_SPELLS[spell][14] > 0) {
				if (!wearingStaff(Enchantment.MAGIC_SPELLS[spell][14]))
					player.getItems().deleteItem(Enchantment.MAGIC_SPELLS[spell][14],
							player.getItems().getItemSlot(Enchantment.MAGIC_SPELLS[spell][14]), Enchantment.MAGIC_SPELLS[spell][15]);
			}
		}
		return true;
	}

	public void multiSpellEffectNPC(int npcId, int damage) {
		switch (Enchantment.MAGIC_SPELLS[player.oldSpellId][0]) {
		case 12891:
		case 12881:
			if (Server.npcHandler.npcs[npcId].freezeTimer < -4) {
				Server.npcHandler.npcs[npcId].freezeTimer = getFreezeTime();
			}
			break;
		}
	}

	public boolean checkMultiBarrageReqsNPC(int i) {
		if (Server.npcHandler.npcs[i] == null) {
			return false;
		} else {
			return true;
		}
	}

	public void appendMultiBarrageNPC(int npcId, boolean splashed) {
		if (Server.npcHandler.npcs[npcId] != null) {
			NPC n = (NPC) Server.npcHandler.npcs[npcId];
			if (n.isDead)
				return;
			if (checkMultiBarrageReqsNPC(npcId)) {
				player.barrageCount++;
				Server.npcHandler.npcs[npcId].underAttackBy = player.playerId;
				Server.npcHandler.npcs[npcId].underAttack = true;
				if (Misc.random(mageAtk()) > Misc.random(mageDef()) && !player.magicFailed) {
					if (getEndGfxHeight() == 100) { // end GFX
						n.gfx100(Enchantment.MAGIC_SPELLS[player.oldSpellId][5]);
					} else {
						n.gfx0(Enchantment.MAGIC_SPELLS[player.oldSpellId][5]);
					}
					int damage = Misc.random(Enchantment.MAGIC_SPELLS[player.oldSpellId][6]);
					if (Server.npcHandler.npcs[npcId].HP - damage < 0) {
						damage = Server.npcHandler.npcs[npcId].HP;
					}
					player.getPA().addSkillXP((Enchantment.MAGIC_SPELLS[player.oldSpellId][7] + damage * SkillIndex.MAGIC.getExpRatio()), SkillIndex.MAGIC.getSkillId());
					player.getPA().addSkillXP((Enchantment.MAGIC_SPELLS[player.oldSpellId][7] + damage * SkillIndex.MAGIC.getExpRatio() / 3), SkillIndex.HITPOINTS.getSkillId());
					Server.npcHandler.npcs[npcId].handleHitMask(damage);
					Server.npcHandler.npcs[npcId].dealDamage(damage);
					player.totalPlayerDamageDealt += damage;
					multiSpellEffectNPC(npcId, damage);
				} else {
					n.gfx100(85);
				}
			}
		}
	}

	public int getFreezeTime() {
		switch (Enchantment.MAGIC_SPELLS[player.oldSpellId][0]) {
		case 1572:
		case 12861: // ice rush
			return 10;

		case 1582:
		case 12881: // ice burst
			return 17;

		case 1592:
		case 12871: // ice blitz
			return 25;

		case 12891: // ice barrage
			return 33;

		default:
			return 0;
		}
	}

	public void freezePlayer(int i) {
	}

	public int getStartHeight() {
		switch (Enchantment.MAGIC_SPELLS[player.spellId][0]) {
		case 1562: // stun
			return 25;

		case 12939:// smoke rush
			return 35;

		case 12987: // shadow rush
			return 38;

		case 12861: // ice rush
			return 15;

		case 12951: // smoke blitz
			return 38;

		case 12999: // shadow blitz
			return 25;

		case 12911: // blood blitz
			return 25;

		default:
			return 43;
		}
	}

	public int getEndHeight() {
		switch (Enchantment.MAGIC_SPELLS[player.spellId][0]) {
		case 1562: // stun
			return 10;

		case 12939: // smoke rush
			return 20;

		case 12987: // shadow rush
			return 28;

		case 12861: // ice rush
			return 10;

		case 12951: // smoke blitz
			return 28;

		case 12999: // shadow blitz
			return 15;

		case 12911:
			return 10;// blood blitz

		default:
			return 31;
		}
	}

	public int getStartDelay() {
		switch (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]) {
		case 15241:
			return 0;
		}
		switch (Enchantment.MAGIC_SPELLS[player.spellId][0]) {
		case 1539:
			return 60;
		default:
			return 53;
		}
	}

	public int getStaffNeeded() {
		switch (Enchantment.MAGIC_SPELLS[player.spellId][0]) {
		case 1539:
			return 1409;

		case 12037:
			return 4170;

		case 1190:
			return 2415;

		case 1191:
			return 2416;

		case 1192:
			return 2417;

		default:
			return 0;
		}
	}

	public boolean godSpells() {
		switch (Enchantment.MAGIC_SPELLS[player.spellId][0]) {
		case 1190:
			return true;

		case 1191:
			return true;

		case 1192:
			return true;

		default:
			return false;
		}
	}

	public int getEndGfxHeight() {
		switch (Enchantment.MAGIC_SPELLS[player.oldSpellId][0]) {
		case 12987:
		case 12901:
		case 12861:
		case 12445:
		case 1192:
		case 13011:
		case 12919:
		case 12881:
		case 12999:
		case 12911:
		case 12871:
		case 13023:
		case 12929:
		case 12891:
			return 0;

		default:
			return 100;
		}
	}

	public int getStartGfxHeight() {
		switch (Enchantment.MAGIC_SPELLS[player.spellId][0]) {
		case 12871:
		case 12891:
			return 0;

		default:
			return 100;
		}
	}

	public void handleDfs() {
	}

	public void handleDfsNPC() {
	}

	public int getBonusAttack(int i) {
		switch (NPCHandler.npcs[i].npcType) {
		case 2883:
			return Misc.random(50) + 30;
		case 2026:
		case 2027:
		case 2029:
		case 2030:
			return Misc.random(50) + 30;
		}
		return 0;
	}

	public void handleGmaulPlayer() {
		if (player.playerIndex > 0) {
			Player o = (Player) PlayerHandler.players[player.playerIndex];
			if (player.goodDistance(player.getX(), player.getY(), o.getX(), o.getY(), getRequiredDistance())) {
				if (checkReqs()) {
					if (checkSpecAmount(4153)) {
						boolean hit = Misc.random(calculateMeleeAttack()) > Misc
								.random(o.getCombat().calculateMeleeDefence());
						int damage = 0;
						if (hit)
							damage = Misc.random(calculateMeleeMaxHit());
						if (o.prayerActive[18]
								|| o.curseActive[9] && System.currentTimeMillis() - o.protMeleeDelay > 1500)
							damage *= .6;
						o.handleHitMask(damage);
						player.startAnimation(1667);
						player.gfx100(340);
						o.dealDamage(damage);
					}
				}
			}
		}
	}

	public void applyRecoil(int damage, int i) {
		if (damage > 0 && PlayerHandler.players[i].playerEquipment[EquipmentListener.RING_SLOT.getSlot()] == 2550) {
			int recDamage = damage / 10 + 1;
			if (!player.getHitUpdateRequired()) {
				player.setHitDiff(recDamage);
				removeRecoil(player);
				player.recoilHits = recDamage;
				player.setHitUpdateRequired(true);
			} else if (!player.getHitUpdateRequired2()) {
				player.setHitDiff2(recDamage);
				player.setHitUpdateRequired2(true);
			}
			player.dealDamage(recDamage);
			player.updateRequired = true;
		}
	}

	public void removeRecoil(Player c) {
		if (c.recoilHits >= 50) {
			c.getItems().removeItem(2550, EquipmentListener.RING_SLOT.getSlot());
			c.getItems().deleteItem(2550, c.getItems().getItemSlot(2550), 1);
			c.getActionSender().sendMessage("Your ring of recoil shaters!");
			c.recoilHits = 0;
		} else {
			c.recoilHits++;
		}
	}

	public boolean armaNpc(int i) {
		switch (NPCHandler.npcs[i].npcType) {
		case 2558:
		case 2559:
		case 2560:
		case 2561:
			return true;
		}
		return false;
	}

	public static int finalMagicDamage(Player c) {
		double damage = Enchantment.MAGIC_SPELLS[c.oldSpellId][6] * 10;
		double damageMultiplier = 1;
		int level = c.playerLevel[SkillIndex.MAGIC.getSkillId()];
		if (level > c.getLevelForXP(c.playerXP[6]) && c.getLevelForXP(c.playerXP[6]) >= 95)
			damageMultiplier += .03 * ((level > 104 ? 104 : level) - 99);
		else
			damageMultiplier = 1;
		switch (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]) {
		case 18371: // Gravite Staff
			damageMultiplier += .05;
			break;
		case 4675: // Ancient Staff
		case 4710: // Ahrim's Staff
		case 4862: // Ahrim's Staff
		case 4864: // Ahrim's Staff
		case 4865: // Ahrim's Staff
		case 6914: // Master Wand
		case 8841: // Void Knight Mace
		case 13867: // Zuriel's Staff
		case 13869: // Zuriel's Staff (Deg)
			damageMultiplier += .10;
			break;
		case 15486: // Staff of Light
			damageMultiplier += .15;
			break;
		case 18355: // Chaotic Staff
			damageMultiplier += .20;
			break;
		}
		switch (c.playerEquipment[EquipmentListener.AMULET_SLOT.getSlot()]) {
		case 18333: // Arcane Pulse
			damageMultiplier += .05;
			break;
		case 18334:// Arcane Blast
			damageMultiplier += .10;
			break;
		case 18335:// Arcane Stream
			damageMultiplier += .15;
			break;
		}
		damage *= damageMultiplier;
		return (int) damage;
	}

}