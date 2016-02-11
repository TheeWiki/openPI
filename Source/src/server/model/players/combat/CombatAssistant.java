package server.model.players.combat;

import server.Constants;
import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.Animation;
import server.model.Graphic;
import server.model.npcs.NPCHandler;
import server.model.players.Client;
import server.model.players.Player;
import server.model.players.PlayerHandler;
import server.util.Misc;

public class CombatAssistant {

	private Client c;

	public CombatAssistant(Client Client) {
		this.c = Client;
	}

	public int[][] slayerReqs = { { 1648, 5 }, { 1612, 15 }, { 1643, 45 }, { 1618, 50 }, { 1624, 65 }, { 1610, 75 },
			{ 1613, 80 }, { 1615, 85 }, { 2783, 90 } };

	public boolean goodSlayer(int i) {
		for (int j = 0; j < slayerReqs.length; j++) {
			if (slayerReqs[j][0] == NPCHandler.npcs[i].npcType) {
				if (slayerReqs[j][1] > c.getLevel()[c.playerSlayer]) {
					c.sendMessage("You need a slayer level of " + slayerReqs[j][1] + " to harm this NPC.");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Attack Npcs
	 */
	public void attackNpc(int i) {
		if (NPCHandler.npcs[i] != null) {
			if (NPCHandler.npcs[i].isDead || NPCHandler.npcs[i].MaxHP <= 0) {
				c.usingMagic = false;
				c.faceUpdate(0);
				c.npcIndex = 0;
				return;
			}
			if (c.respawnTimer > 0) {
				c.npcIndex = 0;
				return;
			}
			if (NPCHandler.npcs[i].underAttackBy > 0 && NPCHandler.npcs[i].underAttackBy != c.playerId
					&& !NPCHandler.npcs[i].inMulti()) {
				c.npcIndex = 0;
				c.sendMessage("This monster is already in combat.");
				return;
			}
			if ((c.underAttackBy > 0 || c.underAttackBy2 > 0) && c.underAttackBy2 != i && !c.inMulti()) {
				resetPlayerAttack();
				c.sendMessage("I am already under attack.");
				return;
			}
			if (!goodSlayer(i)) {
				resetPlayerAttack();
				return;
			}
			if (NPCHandler.npcs[i].spawnedBy != c.playerId && NPCHandler.npcs[i].spawnedBy > 0) {
				resetPlayerAttack();
				c.sendMessage("This monster was not spawned for you.");
				return;
			}
			c.followId2 = i;
			c.followId = 0;
			if (c.attackTimer <= 0) {
				c.usingBow = false;
				c.magicFailed = false;
				boolean usingArrows = false;
				c.usingRangeWeapon = false;
				boolean usingCross = c.getEquipment()[c.playerWeapon] == 9185;
				c.bonusAttack = 0;
				c.rangeItemUsed = 0;
				c.projectileStage = 0;
				c.oldNpcIndex = -1;
				c.oldPlayerIndex = -1;
				if (c.autocasting) {
					c.spellId = c.autocastId;
					c.usingMagic = true;
				}
				if (c.spellId > 0) {
					c.usingMagic = true;
				}
				c.attackTimer = getAttackDelay(
						c.getItems().getItemName(c.getEquipment()[c.playerWeapon]).toLowerCase());
				c.specAccuracy = 1.0;
				c.specDamage = 1.0; 
				if (!c.usingMagic) {
					for (int bowId : c.BOWS) {
						if (c.getEquipment()[c.playerWeapon] == bowId) {
							c.usingBow = true;
							for (int arrowId : c.ARROWS) {
								if (c.getEquipment()[c.playerArrows] == arrowId) {
									usingArrows = true;
								}
							}
						}
					}

					for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
						if (c.getEquipment()[c.playerWeapon] == otherRangeId) {
							c.usingRangeWeapon = true;
						}
					}
				}
				if (armaNpc(i) && !usingCross && !c.usingBow && !c.usingMagic && !usingCrystalBow()
						&& !c.usingRangeWeapon) {
					resetPlayerAttack();
					return;
				}
				if ((!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 2)
						&& (usingHally() && !c.usingRangeWeapon && !c.usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 4)
								&& (c.usingRangeWeapon && !c.usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 1)
								&& (!c.usingRangeWeapon && !usingHally() && !c.usingBow && !c.usingMagic))
						|| ((!c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(),
								8) && (c.usingBow || c.usingMagic)))) {
					c.attackTimer = 2;
					return;
				}
				if (!usingCross && !usingArrows && c.usingBow
						&& (c.getEquipment()[c.playerWeapon] < 4212 || c.getEquipment()[c.playerWeapon] > 4223)) {
					c.sendMessage("You have run out of arrows!");
					c.stopMovement();
					c.npcIndex = 0;
					return;
				}
				if (!usingCorrectArrows() && Constants.CORRECT_ARROWS && c.usingBow && !usingCrystalBow()
						&& c.getEquipment()[c.playerWeapon] != 9185) {
					c.sendMessage("You can't use "
							+ c.getItems().getItemName(c.getEquipment()[c.playerArrows]).toLowerCase() + "s with a "
							+ c.getItems().getItemName(c.getEquipment()[c.playerWeapon]).toLowerCase() + ".");
					c.stopMovement();
					c.npcIndex = 0;
					return;
				}

				if (c.getEquipment()[c.playerWeapon] == 9185 && !properBolts()) {
					c.sendMessage("You must use bolts with a crossbow.");
					c.stopMovement();
					resetPlayerAttack();
					return;
				}

				if (c.usingBow || c.usingMagic || c.usingRangeWeapon
						|| (c.goodDistance(c.getX(), c.getY(), NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY(), 2)
								&& usingHally())) {
					c.stopMovement();
				}

				if (!checkMagicReqs(c.spellId)) {
					c.stopMovement();
					c.npcIndex = 0;
					return;
				}

				c.faceUpdate(i);
				NPCHandler.npcs[i].underAttackBy = c.playerId;
				NPCHandler.npcs[i].lastDamageTaken = System.currentTimeMillis();
				if (c.usingSpecial && !c.usingMagic) {
					if (checkSpecAmount(c.getEquipment()[c.playerWeapon])) {
						c.lastWeaponUsed = c.getEquipment()[c.playerWeapon];
						c.lastArrowUsed = c.getEquipment()[c.playerArrows];
						c.getSA().activateSpecial(c.playerEquipment[c.playerWeapon], i);
						return;
					} else {
						c.sendMessage("You don't have the required special energy to use this attack.");
						c.usingSpecial = false;
						c.getItems().updateSpecialBar();
						c.npcIndex = 0;
						return;
					}
				}
				c.specMaxHitIncrease = 0;
				if (!c.usingMagic) {
					c.playAnimation(Animation.create(WeaponAnimations.attackAnimation(c)));
				} else {
					c.playAnimation(Animation.create(c.MAGIC_SPELLS[c.spellId][2]));
				}
				c.lastWeaponUsed = c.getEquipment()[c.playerWeapon];
				c.lastArrowUsed = c.getEquipment()[c.playerArrows];
				if (!c.usingBow && !c.usingMagic && !c.usingRangeWeapon) {
					Experience.calculateDamageNPC(c, i, 0);
					c.hitDelay = getHitDelay(c.getItems().getItemName(c.getEquipment()[c.playerWeapon]).toLowerCase());
					c.projectileStage = 0;
					c.oldNpcIndex = i;
				}

				if (c.usingBow && !c.usingRangeWeapon && !c.usingMagic || usingCross) { // range
																						// hit
																						// delay
					if (usingCross)
						c.usingBow = true;
					if (c.fightMode == 2)
						c.attackTimer--;
					c.lastArrowUsed = c.getEquipment()[c.playerArrows];
					c.lastWeaponUsed = c.getEquipment()[c.playerWeapon];
					c.playGraphic(Graphic.create(getRangeStartGFX(), 100));
					Experience.calculateDamageNPC(c, i, 1);
					c.hitDelay = getHitDelay(c.getItems().getItemName(c.getEquipment()[c.playerWeapon]).toLowerCase());
					c.projectileStage = 1;
					c.oldNpcIndex = i;
					if (c.getEquipment()[c.playerWeapon] >= 4212 && c.getEquipment()[c.playerWeapon] <= 4223) {
						c.rangeItemUsed = c.getEquipment()[c.playerWeapon];
						c.crystalBowArrowCount++;
						c.lastArrowUsed = 0;
					} else {
						c.rangeItemUsed = c.getEquipment()[c.playerArrows];
						c.getItems().deleteArrow();
					}
					fireProjectileNpc();
				}

				if (c.usingRangeWeapon && !c.usingMagic && !c.usingBow) { // knives,
																			// darts,
																			// etc
																			// hit
																			// delay
					c.rangeItemUsed = c.getEquipment()[c.playerWeapon];
					if (c.rangeItemUsed != 12926)
						c.getItems().deleteEquipment();
					c.playGraphic(Graphic.create(getRangeStartGFX(), 0, 100));
					c.lastArrowUsed = 0;
					Experience.calculateDamageNPC(c, i, 1);
					c.hitDelay = getHitDelay(c.getItems().getItemName(c.getEquipment()[c.playerWeapon]).toLowerCase());
					c.projectileStage = 1;
					c.oldNpcIndex = i;
					if (c.fightMode == 2)
						c.attackTimer--;
					fireProjectileNpc();
				}

				if (c.usingMagic) { // magic hit delay
					int pX = c.getX();
					int pY = c.getY();
					int nX = NPCHandler.npcs[i].getX();
					int nY = NPCHandler.npcs[i].getY();
					int offX = (pY - nY) * -1;
					int offY = (pX - nX) * -1;
					c.castingMagic = true;
					c.projectileStage = 2;
					if (c.MAGIC_SPELLS[c.spellId][3] > 0) {
						if (getStartGfxHeight() == 100) {
							c.playGraphic(Graphic.create(c.MAGIC_SPELLS[c.spellId][3], 0, 100));
						} else {
							c.playGraphic(Graphic.create(c.MAGIC_SPELLS[c.spellId][3], 0, 0));
						}
					}
					if (c.MAGIC_SPELLS[c.spellId][4] > 0) {
						c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, c.MAGIC_SPELLS[c.spellId][4],
								getStartHeight(), getEndHeight(), i + 1, 50);
					}
					Experience.calculateDamageNPC(c, i, 2);
					c.hitDelay = getHitDelay(c.getItems().getItemName(c.getEquipment()[c.playerWeapon]).toLowerCase());
					c.oldNpcIndex = i;
					c.oldSpellId = c.spellId;
					c.spellId = 0;
					if (!c.autocasting)
						c.npcIndex = 0;
				}

				if (c.usingBow && Constants.CRYSTAL_BOW_DEGRADES) { // crystal bow
																	// degrading
					if (c.getEquipment()[c.playerWeapon] == 4212) { // new
																	// crystal
																	// bow
																	// becomes
																	// full
																	// bow
																	// on
																	// the
																	// first
																	// shot
						c.getItems().wearItem(4214, 1, 3);
					}

					if (c.crystalBowArrowCount >= 250) {
						switch (c.getEquipment()[c.playerWeapon]) {

						case 4223: // 1/10 bow
							c.getItems().wearItem(-1, 1, 3);
							c.sendMessage("Your crystal bow has fully degraded.");
							if (!c.getItems().addItem(4207, 1)) {
								Server.itemHandler.createGroundItem(c, 4207, c.getX(), c.getY(), 1, c.getId());
							}
							c.crystalBowArrowCount = 0;
							break;

						default:
							c.getItems().wearItem(++c.getEquipment()[c.playerWeapon], 1, 3);
							c.sendMessage("Your crystal bow degrades.");
							c.crystalBowArrowCount = 0;
							break;

						}
					}
				}
			}
		}
	}

	public void delayedHit(int i) { // npc hit delay
		if (NPCHandler.npcs[i] != null) {

			if (!c.castingMagic && c.projectileStage > 0) { // range hit damage
				int damage = Misc.random(rangeMaxHit());
				int damage2 = -1;
				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1)
					damage2 = Misc.random(rangeMaxHit());
				boolean ignoreDef = false;
				if (Misc.random(5) == 1 && c.lastArrowUsed == 9243) {
					ignoreDef = true;
					NPCHandler.npcs[i].gfx0(758);
				}

				if (Misc.random(NPCHandler.npcs[i].defence) > Misc.random(10 + calculateRangeAttack()) && !ignoreDef) {
					damage = 0;
				} else if (NPCHandler.npcs[i].npcType == 2881 || NPCHandler.npcs[i].npcType == 2883 && !ignoreDef) {
					damage = 0;
				}

				if (Misc.random(4) == 1 && c.lastArrowUsed == 9242 && damage > 0) {
					NPCHandler.npcs[i].gfx0(754);
					damage = NPCHandler.npcs[i].HP / 5;
					c.handleHitMask(c.getLevel()[3] / 10);
					c.dealDamage(c.getLevel()[3] / 10);
					c.playGraphic(Graphic.create(754, 0, 0));
				}

				if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1) {
					if (Misc.random(NPCHandler.npcs[i].defence) > Misc.random(10 + calculateRangeAttack()))
						damage2 = 0;
				}
				if (c.dbowSpec) {
					NPCHandler.npcs[i].gfx100(1100);
					if (damage < 8)
						damage = 8;
					if (damage2 < 8)
						damage2 = 8;
					c.dbowSpec = false;
				}
				if (damage > 0 && Misc.random(5) == 1 && c.lastArrowUsed == 9244) {
					damage *= 1.45;
					NPCHandler.npcs[i].gfx0(756);
				}

				if (NPCHandler.npcs[i].HP - damage < 0) {
					damage = NPCHandler.npcs[i].HP;
				}
				if (NPCHandler.npcs[i].HP - damage <= 0 && damage2 > 0) {
					damage2 = 0;
				}
				if (c.fightMode == 3) {
					c.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 4);
					c.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 1);
					c.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 3);
					c.getPA().refreshSkill(1);
					c.getPA().refreshSkill(3);
					c.getPA().refreshSkill(4);
				} else {
					c.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE), 4);
					c.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 3);
					c.getPA().refreshSkill(3);
					c.getPA().refreshSkill(4);
				}
				if (damage > 0) {
					if (NPCHandler.npcs[i].npcType >= 3777 && NPCHandler.npcs[i].npcType <= 3780) {
						c.pcDamage += damage;
					}
				}
				boolean dropArrows = true;

				for (int noArrowId : c.NO_ARROW_DROP) {
					if (c.lastWeaponUsed == noArrowId) {
						dropArrows = false;
						break;
					}
				}
				if (dropArrows) {
					c.getItems().dropArrowNpc();
				}
				NPCHandler.npcs[i].underAttack = true;
				NPCHandler.npcs[i].hitDiff = damage;
				NPCHandler.npcs[i].HP -= damage;
				if (damage2 > -1) {
					NPCHandler.npcs[i].hitDiff2 = damage2;
					NPCHandler.npcs[i].HP -= damage2;
					c.totalDamageDealt += damage2;
				}
				if (c.killingNpcIndex != c.oldNpcIndex) {
					c.totalDamageDealt = 0;
				}
				c.killingNpcIndex = c.oldNpcIndex;
				c.totalDamageDealt += damage;
				NPCHandler.npcs[i].hitUpdateRequired = true;
				if (damage2 > -1)
					NPCHandler.npcs[i].hitUpdateRequired2 = true;
				NPCHandler.npcs[i].updateRequired = true;

			} else if (c.projectileStage > 0) { // magic hit damage
				int damage = Misc.random(c.MAGIC_SPELLS[c.oldSpellId][6]);
				if (godSpells()) {
					if (System.currentTimeMillis() - c.godSpellDelay < Constants.GOD_SPELL_CHARGE) {
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

				if (NPCHandler.npcs[i].HP - damage < 0) {
					damage = NPCHandler.npcs[i].HP;
				}

				c.getPA().addSkillXP((c.MAGIC_SPELLS[c.oldSpellId][7] + damage * Constants.MAGIC_EXP_RATE), 6);
				c.getPA().addSkillXP((c.MAGIC_SPELLS[c.oldSpellId][7] + damage * Constants.MAGIC_EXP_RATE / 3), 3);
				c.getPA().refreshSkill(3);
				c.getPA().refreshSkill(6);
				if (damage > 0) {
					if (NPCHandler.npcs[i].npcType >= 3777 && NPCHandler.npcs[i].npcType <= 3780) {
						c.pcDamage += damage;
					}
				}
				if (getEndGfxHeight() == 100 && !magicFailed) { // end GFX
					NPCHandler.npcs[i].gfx100(c.MAGIC_SPELLS[c.oldSpellId][5]);
				} else if (!magicFailed) {
					NPCHandler.npcs[i].gfx0(c.MAGIC_SPELLS[c.oldSpellId][5]);
				}

				if (magicFailed) {
					NPCHandler.npcs[i].gfx100(85);
				}
				if (!magicFailed) {
					int freezeDelay = getFreezeTime();// freeze
					if (freezeDelay > 0 && NPCHandler.npcs[i].freezeTimer == 0) {
						NPCHandler.npcs[i].freezeTimer = freezeDelay;
					}
					switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
					case 12901:
					case 12919: // blood spells
					case 12911:
					case 12929:
						int heal = Misc.random(damage / 2);
						if (c.getLevel()[3] + heal >= c.getPA().getLevelForXP(c.getExperience()[3])) {
							c.getLevel()[3] = c.getPA().getLevelForXP(c.getExperience()[3]);
						} else {
							c.getLevel()[3] += heal;
						}
						c.getPA().refreshSkill(3);
						break;
					}

				}
				NPCHandler.npcs[i].underAttack = true;
				if (c.MAGIC_SPELLS[c.oldSpellId][6] != 0) {
					NPCHandler.npcs[i].hitDiff = damage;
					NPCHandler.npcs[i].HP -= damage;
					NPCHandler.npcs[i].hitUpdateRequired = true;
					c.totalDamageDealt += damage;
				}
				c.killingNpcIndex = c.oldNpcIndex;
				NPCHandler.npcs[i].updateRequired = true;
				c.usingMagic = false;
				c.castingMagic = false;
				c.oldSpellId = 0;
			}
		}

		if (c.bowSpecShot <= 0) {
			c.oldNpcIndex = 0;
			c.projectileStage = 0;
			c.doubleHit = false;
			c.lastWeaponUsed = 0;
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot >= 2) {
			c.bowSpecShot = 0;
			// c.attackTimer =
			// getAttackDelay(c.getItems().getItemName(c.getEquipment()[c.playerWeapon]).toLowerCase());
		}
		if (c.bowSpecShot == 1) {
			fireProjectileNpc();
			c.hitDelay = 2;
			c.bowSpecShot = 0;
		}
	}

	public void fireProjectileNpc() {
		if (c.oldNpcIndex > 0) {
			if (NPCHandler.npcs[c.oldNpcIndex] != null) {
				c.projectileStage = 2;
				int pX = c.getX();
				int pY = c.getY();
				int nX = NPCHandler.npcs[c.oldNpcIndex].getX();
				int nY = NPCHandler.npcs[c.oldNpcIndex].getY();
				int offX = (pY - nY) * -1;
				int offY = (pX - nX) * -1;
				/*
				 * c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50,
				 * getProjectileSpeed(), getRangeProjectileGFX(), 43, 31,
				 * c.oldNpcIndex + 1, getStartDelay()); if (usingDbow())
				 * c.getPA().createPlayersProjectile2(pX, pY, offX, offY, 50,
				 * getProjectileSpeed(), getRangeProjectileGFX(), 60, 31,
				 * c.oldNpcIndex + 1, getStartDelay(), 35);
				 */
				int clientSpeed = 0;
				int showDelay = 0;
				int slope = 0;
				int distance = c.distanceToPoint(nX, nY);
				if (c.lastWeaponUsed == 9185 || c.lastWeaponUsed == 11785) {
					clientSpeed = c.armadylSpecial ? 75 : 55;
					showDelay = 45;
					slope = 5;
					if (c.armadylSpecial) {
						c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, clientSpeed, 301, 46, 36,
								c.oldNpcIndex + 1, showDelay, slope);
					} else {
						c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, clientSpeed, getRangeProjectileGFX(),
								46, 36, c.oldNpcIndex + 1, showDelay, slope);
					}
					return;
				} else if (c.lastWeaponUsed == 11235) {
					if (distance <= 1) {
						clientSpeed = 55;
					} else if (distance > 1 && distance <= 3) {
						clientSpeed = 55;
					} else if (distance > 3 && distance <= 8) {
						clientSpeed = 65;
						c.hitDelay += 1;
					} else {
						clientSpeed = 75;
					}
					showDelay = 45;
					slope = 15;
					clientSpeed += 30;
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, clientSpeed, getRangeProjectileGFX(), 41,
							31, c.oldNpcIndex + 1, 36, 3);
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, clientSpeed + 10, getRangeProjectileGFX(),
							46, 36, c.oldNpcIndex + 1, 36, slope + 6);
					return;
				} else if (c.msbSpec) {
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 40 + (distance * 5), 249, 43, 35,
							c.oldNpcIndex + 1, 36, 15);
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 65 + (distance * 5), 249, 43, 35,
							c.oldNpcIndex + 1, 36, 15);
					return;
				} else {
					clientSpeed = 55 + (distance * 5);
					if (distance > 2) {
						c.hitDelay += 1;
					}
					showDelay = 45;
					slope = 15;
				}
				c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, clientSpeed, getRangeProjectileGFX(), 46, 36,
						c.oldNpcIndex + 1, showDelay, slope);
			}
		}
	}

	/**
	 * Attack Players, same as npc tbh xD
	 **/

	public void attackPlayer(int i) {

		if (PlayerHandler.players[i] != null) {

			if (PlayerHandler.players[i].isDead || PlayerHandler.players[i].getLevel()[3] <= 0 || c.getLevel()[3] <= 0
					|| c.isDead) {
				resetPlayerAttack();
				return;
			}

			if (c.respawnTimer > 0 || PlayerHandler.players[i].respawnTimer > 0) {
				resetPlayerAttack();
				return;
			}
			if (!c.getCombat().checkReqs()) {
				return;
			}
			boolean sameSpot = c.absX == PlayerHandler.players[i].getX() && c.absY == PlayerHandler.players[i].getY();
			if (!c.goodDistance(PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY(), c.getX(), c.getY(),
					25) && !sameSpot) {
				resetPlayerAttack();
				return;
			}

			if (PlayerHandler.players[i].respawnTimer > 0) {
				PlayerHandler.players[i].playerIndex = 0;
				resetPlayerAttack();
				return;
			}

			if (PlayerHandler.players[i].heightLevel != c.heightLevel) {
				resetPlayerAttack();
				return;
			}
			// c.sendMessage("Made it here0.");
			c.followId = i;
			c.followId2 = 0;
			if (c.attackTimer <= 0) {
				c.usingBow = false;
				c.specEffect = 0;
				c.usingRangeWeapon = false;
				c.rangeItemUsed = 0;
				boolean usingArrows = false;
				c.usingRangeWeapon = false;
				c.projectileStage = 0;
				c.oldNpcIndex = -1;
				c.oldPlayerIndex = -1;
				c.getCombatBonus()[0] = c.playerBonus[10];
				if (c.absX == PlayerHandler.players[i].absX && c.absY == PlayerHandler.players[i].absY) {
					if (c.freezeTimer > 0) {
						resetPlayerAttack();
						return;
					}
					c.followId = i;
					c.attackTimer = 0;
					return;
				}

				/*
				 * if ((c.inPirateHouse() &&
				 * !Server.playerHandler.players[i].inPirateHouse()) ||
				 * (Server.playerHandler.players[i].inPirateHouse() &&
				 * !c.inPirateHouse())) { resetPlayerAttack(); return; }
				 */
				// c.sendMessage("Made it here1.");
				if (!c.usingMagic) {
					for (int bowId : c.BOWS) {
						if (c.getEquipment()[c.playerWeapon] == bowId) {
							c.usingBow = true;
							for (int arrowId : c.ARROWS) {
								if (c.getEquipment()[c.playerArrows] == arrowId) {
									usingArrows = true;
								}
							}
						}
					}

					for (int otherRangeId : c.OTHER_RANGE_WEAPONS) {
						if (c.getEquipment()[c.playerWeapon] == otherRangeId) {
							c.usingRangeWeapon = true;
						}
					}
				}
				if (c.autocasting) {
					c.spellId = c.autocastId;
					c.usingMagic = true;
				}
				// c.sendMessage("Made it here2.");
				if (c.spellId > 0) {
					c.usingMagic = true;
				}
				c.turnPlayerTo(PlayerHandler.players[i].getX(), PlayerHandler.players[i].getY());
				c.attackTimer = getAttackDelay(
						c.getItems().getItemName(c.getEquipment()[c.playerWeapon]).toLowerCase());

				if (c.duelRule[9]) {
					boolean canUseWeapon = false;
					for (int funWeapon : Constants.FUN_WEAPONS) {
						if (c.getEquipment()[c.playerWeapon] == funWeapon) {
							canUseWeapon = true;
						}
					}
					if (!canUseWeapon) {
						c.sendMessage("You can only use fun weapons in this duel!");
						resetPlayerAttack();
						return;
					}
				}
				// c.sendMessage("Made it here3.");
				if (c.duelRule[2] && (c.usingBow || c.usingRangeWeapon)) {
					c.sendMessage("Range has been disabled in this duel!");
					return;
				}
				if (c.duelRule[3] && (!c.usingBow && !c.usingRangeWeapon && !c.usingMagic)) {
					c.sendMessage("Melee has been disabled in this duel!");
					return;
				}

				if (c.duelRule[4] && c.usingMagic) {
					c.sendMessage("Magic has been disabled in this duel!");
					resetPlayerAttack();
					return;
				}

				if ((!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(),
						PlayerHandler.players[i].getY(), 4) && (c.usingRangeWeapon && !c.usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(), 2)
								&& (!c.usingRangeWeapon && usingHally() && !c.usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(), getRequiredDistance())
								&& (!c.usingRangeWeapon && !usingHally() && !c.usingBow && !c.usingMagic))
						|| (!c.goodDistance(c.getX(), c.getY(), PlayerHandler.players[i].getX(),
								PlayerHandler.players[i].getY(), 10) && (c.usingBow || c.usingMagic))) {
					// c.sendMessage("Setting attack timer to 1");
					c.attackTimer = 1;
					if (!c.usingBow && !c.usingMagic && !c.usingRangeWeapon && c.freezeTimer > 0)
						resetPlayerAttack();
					return;
				}

				if (!usingArrows && c.usingBow
						&& (c.getEquipment()[c.playerWeapon] < 4212 || c.getEquipment()[c.playerWeapon] > 4223)
						&& !c.usingMagic) {
					c.sendMessage("You have run out of arrows!");
					c.stopMovement();
					resetPlayerAttack();
					return;
				}
				if (!usingCorrectArrows() && Constants.CORRECT_ARROWS && c.usingBow && !usingCrystalBow()
						&& !c.usingMagic) {
					c.sendMessage("You can't use "
							+ c.getItems().getItemName(c.getEquipment()[c.playerArrows]).toLowerCase() + "s with a "
							+ c.getItems().getItemName(c.getEquipment()[c.playerWeapon]).toLowerCase() + ".");
					c.stopMovement();
					resetPlayerAttack();
					return;
				}

				if (!checkMagicReqs(c.spellId)) {
					c.stopMovement();
					resetPlayerAttack();
					return;
				}

				c.faceUpdate(PlayerHandler.players[i].getFace());
				if (!c.attackedPlayers.contains(c.playerIndex)
						&& !PlayerHandler.players[c.playerIndex].attackedPlayers.contains(c.playerId)) {
					c.attackedPlayers.add(c.playerIndex);
					c.isSkulled = true;
					c.skullTimer = Constants.SKULL_TIMER;
					c.headIconPk = 0;
					c.getPA().requestUpdates();
				}
				c.specAccuracy = 1.0;
				c.specDamage = 1.0;
				c.oldPlayerIndex = -1;
				c.delayedDamage = c.delayedDamage2 = 0;
				Client otherClient = (Client) PlayerHandler.players[i];
				if (!c.usingMagic && !c.usingBow) {
					if (otherClient.attackTimer <= 3 || otherClient.attackTimer == 0) {
						otherClient.playAnimation(Animation.create(WeaponAnimations.blockAnimation(otherClient)));
					}
				} else {
					if (otherClient.attackTimer <= 3 || otherClient.attackTimer == 0) {
						otherClient.playAnimation(Animation.create(WeaponAnimations.blockAnimation(otherClient), 50));
					}
				}
				if (c.getEquipment()[3] == 12006) {
					if (c.getHits()[0] > 0) {
						c.getHits()[0] -= 1;
						if (c.getHits()[0] == 0) {
							c.getItems().removeItem(12006, 3);
							c.getItems().deleteItem(12006, 1);
							c.getItems().addItem(12004, 1);
							c.sendMessage("Your abyssal tentacle has degraded.");
						}
					}
				}
				if (c.usingSpecial && !c.usingMagic) {
					if (c.duelRule[10] && c.duelStatus == 5) {
						c.sendMessage("Special attacks have been disabled during this duel!");
						c.usingSpecial = false;
						c.getItems().updateSpecialBar();
						resetPlayerAttack();
						return;
					}
					if (checkSpecAmount(c.getEquipment()[c.playerWeapon])) {
						c.lastArrowUsed = c.getEquipment()[c.playerArrows];
						c.getSA().activateSpecial(c.playerEquipment[c.playerWeapon], i);
						c.followId = c.playerIndex;
						return;
					} else {
						c.sendMessage("You don't have the required special energy to use this attack.");
						c.usingSpecial = false;
						c.getItems().updateSpecialBar();
						c.playerIndex = 0;
						return;
					}
				}

				if (!c.usingMagic) {
					c.playAnimation(Animation.create(WeaponAnimations.attackAnimation(c)));
					c.mageFollow = false;
				} else {
					c.playAnimation(Animation.create(c.MAGIC_SPELLS[c.spellId][2]));
					c.mageFollow = true;
					c.followId = c.playerIndex;
				}
				if (!PlayerHandler.players[i].inSafeZone()) {
					PlayerHandler.players[i].attackableTimer = System.currentTimeMillis();
				}
				PlayerHandler.players[i].underAttackBy = c.playerId;
				PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
				PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
				PlayerHandler.players[i].killerId = c.playerId;
				c.lastArrowUsed = 0;
				c.rangeItemUsed = 0;
				if (!c.usingBow && !c.usingMagic && !c.usingRangeWeapon) { // melee
																			// hit
																			// delay
					c.followId = PlayerHandler.players[c.playerIndex].playerId;
					c.getPA().followPlayer();
					c.delayedDamage = Misc.random(calculateMeleeMaxHit());
					c.projectileStage = 0;
					c.oldPlayerIndex = i;
					Experience.calculateDamage(c, (Client) PlayerHandler.players[c.oldPlayerIndex], 0);
					if (Misc.random(1) == 0) {
						c.getCombat().appendDamage(c.oldPlayerIndex, 0, 1);
					} else {
						c.hitDelay = getHitDelay(
								c.getItems().getItemName(c.getEquipment()[c.playerWeapon]).toLowerCase());
					}
				}

				if (c.usingBow && !c.usingRangeWeapon && !c.usingMagic) { // range
																			// hit
																			// delay
					if (c.getEquipment()[c.playerWeapon] >= 4212 && c.getEquipment()[c.playerWeapon] <= 4223) {
						c.rangeItemUsed = c.getEquipment()[c.playerWeapon];
						c.crystalBowArrowCount++;
					} else {
						c.rangeItemUsed = c.getEquipment()[c.playerArrows];
						c.getItems().deleteArrow();
					}
					if (c.fightMode == 2)
						c.attackTimer--;
					c.usingBow = true;
					c.followId = PlayerHandler.players[c.playerIndex].playerId;
					c.getPA().followPlayer();
					c.lastWeaponUsed = c.getEquipment()[c.playerWeapon];
					c.lastArrowUsed = c.getEquipment()[c.playerArrows];
					c.playGraphic(Graphic.create(getRangeStartGFX(), 0, 100));
					c.projectileStage = 1;
					c.oldPlayerIndex = i;
					Experience.calculateDamage(c, (Client) PlayerHandler.players[c.oldPlayerIndex], 1);
					if (c.lastWeaponUsed == 11235) {
						c.dbowIndex = c.playerIndex;
						c.dbowTimer = 5;
					} else {
						c.hitDelay = c.getCombat().getHitDelay(c.getItems().getItemName(c.getEquipment()[3]));
					}
					fireProjectilePlayer();
				}

				if (c.usingRangeWeapon) { // knives, darts, etc hit delay
					c.rangeItemUsed = c.getEquipment()[c.playerWeapon];
					if (c.rangeItemUsed != 12926)
						c.getItems().deleteEquipment();
					c.usingRangeWeapon = true;
					c.followId = PlayerHandler.players[c.playerIndex].playerId;
					c.getPA().followPlayer();
					c.playGraphic(Graphic.create(getRangeStartGFX(), 0, 100));
					if (c.fightMode == 2)
						c.attackTimer--;
					c.hitDelay = getHitDelay(c.getItems().getItemName(c.getEquipment()[c.playerWeapon]).toLowerCase());
					if (c.fightMode == 2)
						c.hitDelay--;
					c.projectileStage = 1;
					c.oldPlayerIndex = i;
					Experience.calculateDamage(c, (Client) PlayerHandler.players[c.oldPlayerIndex], 1);
					fireProjectilePlayer();
				}

				if (c.usingMagic) { // magic hit delay
					int pX = c.getX();
					int pY = c.getY();
					int nX = PlayerHandler.players[i].getX();
					int nY = PlayerHandler.players[i].getY();
					int offX = (pY - nY) * -1;
					int offY = (pX - nX) * -1;
					c.castingMagic = true;
					c.projectileStage = 2;
					if (c.MAGIC_SPELLS[c.spellId][3] > 0) {
						if (getStartGfxHeight() == 100) {
							c.playGraphic(Graphic.create(c.MAGIC_SPELLS[c.spellId][3], 0, 100));
						} else {
							c.playGraphic(Graphic.create(c.MAGIC_SPELLS[c.spellId][3], 0, 0));
						}
					}
					if (c.MAGIC_SPELLS[c.spellId][4] > 0) {
						c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 78, c.MAGIC_SPELLS[c.spellId][4],
								getStartHeight(), getEndHeight(), -i - 1, getStartDelay());
					}
					if (c.autocastId > 0) {
						c.followId = c.playerIndex;
						c.followDistance = 5;
					}
					Client o = (Client) PlayerHandler.players[i];
					Experience.calculateDamage(c, o, 2);
					c.hitDelay = (int) getDelay(c, o, c.getCombat().getStartDelay(), 78);
					if (c.MAGIC_SPELLS[c.spellId][0] == 12871) {
						c.attackTimer = c.hitDelay;
					}
					c.oldPlayerIndex = i;
					c.oldSpellId = c.spellId;
					c.spellId = 0;
					if (c.MAGIC_SPELLS[c.oldSpellId][0] == 12891 && o.isMoving) {
						// c.sendMessage("Barrage projectile..");
						c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 85, 368, 25, 25, -i - 1,
								getStartDelay());
					}
					if (!c.autocasting && c.spellId <= 0)
						c.playerIndex = 0;
				}

				if (c.usingBow && Constants.CRYSTAL_BOW_DEGRADES) { // crystal bow
																	// degrading
					if (c.getEquipment()[c.playerWeapon] == 4212) { // new
																	// crystal
																	// bow
																	// becomes
																	// full
																	// bow
																	// on
																	// the
																	// first
																	// shot
						c.getItems().wearItem(4214, 1, 3);
					}

					if (c.crystalBowArrowCount >= 250) {
						switch (c.getEquipment()[c.playerWeapon]) {

						case 4223: // 1/10 bow
							c.getItems().wearItem(-1, 1, 3);
							c.sendMessage("Your crystal bow has fully degraded.");
							if (!c.getItems().addItem(4207, 1)) {
								Server.itemHandler.createGroundItem(c, 4207, c.getX(), c.getY(), 1, c.getId());
							}
							c.crystalBowArrowCount = 0;
							break;

						default:
							c.getItems().wearItem(++c.getEquipment()[c.playerWeapon], 1, 3);
							c.sendMessage("Your crystal bow degrades.");
							c.crystalBowArrowCount = 0;
							break;
						}
					}
				}
			}
		}
	}

	public boolean isAttackable(int i) {
		if (System.currentTimeMillis() - PlayerHandler.players[c.playerIndex].attackableTimer > 10000) {
			return true;
		}
		return false;
	}

	public boolean usingCrystalBow() {
		return c.getEquipment()[c.playerWeapon] >= 4212 && c.getEquipment()[c.playerWeapon] <= 4223;
	}

	public void appendVengeance(int otherPlayer, final int damage) {
		if (damage <= 0)
			return;
		Player o = PlayerHandler.players[otherPlayer];
		o.forcedText = "Taste Vengeance!";
		o.forcedChatUpdateRequired = true;
		o.updateRequired = true;
		o.vengOn = false;
		CycleEventHandler.getSingleton().addEvent(2, c, new CycleEvent() {
			int vengDamage = damage;

			@Override
			public void execute(CycleEventContainer container) {
				if (c == null || c.getLevel()[3] <= 0 || c.isDead) {
					container.stop();
					return;
				}
				vengDamage = (int) (vengDamage * 0.75);
				if (vengDamage > c.getLevel()[3]) {
					vengDamage = c.getLevel()[3];
				}
				c.setHitDiff2(vengDamage);
				c.setHitUpdateRequired2(true);
				c.getLevel()[3] -= vengDamage;
				c.getPA().refreshSkill(3);
				c.updateRequired = true;
				container.stop();
			}

			@Override
			public void stop() {
				// TODO Auto-generated method stub

			}

		}, 1);
	}

	public void playerDelayedHit(int i) {
		if (PlayerHandler.players[i] != null) {
			if (PlayerHandler.players[i].isDead || c.isDead || PlayerHandler.players[i].getLevel()[3] <= 0
					|| c.getLevel()[3] <= 0) {
				c.playerIndex = 0;
				return;
			}
			if (PlayerHandler.players[i].respawnTimer > 0) {
				c.faceUpdate(0);
				c.playerIndex = 0;
				return;
			}
			Client o = (Client) PlayerHandler.players[i];
			o.getPA().removeAllWindows();
			if (o.playerIndex <= 0 && o.npcIndex <= 0) {
				if (o.autoRet == 1) {
					o.playerIndex = c.playerId;
				}
			}
//			if (o.getTradeHandler().getCurrentTrade() != null) {
//				if (o.getTradeHandler().getCurrentTrade().isOpen()) {
//					o.getTradeHandler().decline();
//				}
//			}

			if (!c.castingMagic && c.projectileStage > 0) { // range hit damage
				int damage = Misc.random(rangeMaxHit());
				int damage2 = -1;
				if (c.lastWeaponUsed == 11235 || c.msbSpec)
					damage2 = Misc.random(rangeMaxHit());
				boolean ignoreDef = false;
				if (Misc.random(4) == 1 && c.lastArrowUsed == 9243) {
					ignoreDef = true;
					c.playGraphic(Graphic.create(758, 0, 0));
				}
				if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc.random(10 + calculateRangeAttack())
						&& !ignoreDef) {
					damage = 0;
				}
				if (Misc.random(4) == 1 && c.lastArrowUsed == 9242 && damage > 0) {
					PlayerHandler.players[i].playGraphic(Graphic.create(754, 0, 0));
					damage = NPCHandler.npcs[i].HP / 5;
					c.handleHitMask(c.getLevel()[3] / 10);
					c.dealDamage(c.getLevel()[3] / 10);
					c.playGraphic(Graphic.create(754, 0, 0));
				}

				if (c.lastWeaponUsed == 11235 || c.msbSpec) {
					if (Misc.random(10 + o.getCombat().calculateRangeDefence()) > Misc
							.random(10 + calculateRangeAttack()))
						damage2 = 0;
				}

				if (c.dbowSpec) {
					o.playGraphic(Graphic.create(1100, 0, 100));
					if (damage < 8)
						damage = 8;
					if (damage2 < 8)
						damage2 = 8;
					c.dbowSpec = false;
				}
				if (c.msbSpec) {
					c.msbSpec = false;
					c.attackTimer++;
					c.hitDelay++;
				}
				if (c.armadylSpecial) {
					damage = c.getCombat().rangeMaxHit();
					c.armadylSpecial = false;
				}
				if (damage > 0 && Misc.random(5) == 1 && c.lastArrowUsed == 9244) {
					damage *= 1.45;
					o.playGraphic(Graphic.create(756, 0, 0));
				}
				if (o.prayerActive[17] && System.currentTimeMillis() - o.protRangeDelay > 1500) { // if
																									// prayer
																									// active
																									// reduce
																									// damage
																									// by
																									// half
					damage = (int) damage * 60 / 100;
					if (c.lastWeaponUsed == 11235 || c.bowSpecShot == 1)
						damage2 = (int) damage2 * 60 / 100;
				}
				if (PlayerHandler.players[i].getLevel()[3] - damage < 0) {
					damage = PlayerHandler.players[i].getLevel()[3];
				}
				if (PlayerHandler.players[i].getLevel()[3] - damage - damage2 < 0) {
					damage2 = PlayerHandler.players[i].getLevel()[3] - damage;
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
				if (c.fightMode == 3) {
					c.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 4);
					c.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 1);
					c.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 3);
					c.getPA().refreshSkill(1);
					c.getPA().refreshSkill(3);
					c.getPA().refreshSkill(4);
				} else {
					c.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE), 4);
					c.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 3);
					c.getPA().refreshSkill(3);
					c.getPA().refreshSkill(4);
				}
				boolean dropArrows = true;

				for (int noArrowId : c.NO_ARROW_DROP) {
					if (c.lastWeaponUsed == noArrowId) {
						dropArrows = false;
						break;
					}
				}
				if (dropArrows) {
					c.getItems().dropArrowPlayer();
				}
				PlayerHandler.players[i].underAttackBy = c.playerId;
				PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
				PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
				PlayerHandler.players[i].killerId = c.playerId;
				// Server.playerHandler.players[i].setHitDiff(damage);
				// Server.playerHandler.players[i].getLevel()[3] -= damage;
				PlayerHandler.players[i].dealDamage(damage);
				PlayerHandler.players[i].damageTaken[c.playerId] += damage;
				c.killedBy = PlayerHandler.players[i].playerId;
				PlayerHandler.players[i].handleHitMask(damage);
				if (damage2 != -1) {
					// Server.playerHandler.players[i].getLevel()[3] -=
					// damage2;
					PlayerHandler.players[i].dealDamage(damage2);
					PlayerHandler.players[i].damageTaken[c.playerId] += damage2;
					PlayerHandler.players[i].handleHitMask(damage2);

				}
				o.getPA().refreshSkill(3);

				// Server.playerHandler.players[i].setHitUpdateRequired(true);
				PlayerHandler.players[i].updateRequired = true;
				applySmite(i, damage);
				if (damage2 != -1)
					applySmite(i, damage2);

			} else if (c.projectileStage > 0) { // magic hit damage
				int damage = Misc.random(MagicFormula.magicMaxHit(c));
				if (godSpells()) {
					if (System.currentTimeMillis() - c.godSpellDelay < Constants.GOD_SPELL_CHARGE) {
						damage += 10;
					}
				}
				if (o.prayerActive[16] && System.currentTimeMillis() - o.protMageDelay > 1500) { // if
																									// prayer
																									// active
																									// reduce
																									// damage
																									// by
																									// half
					damage = (int) damage * 60 / 100;
				}
				if (PlayerHandler.players[i].getLevel()[3] - damage < 0) {
					damage = PlayerHandler.players[i].getLevel()[3];
				}
				if (o.vengOn)
					appendVengeance(i, damage);
				if (damage > 0)
					applyRecoil(damage, i);
				c.getPA().addSkillXP((c.MAGIC_SPELLS[c.oldSpellId][7] + damage * Constants.MAGIC_EXP_RATE), 6);
				c.getPA().addSkillXP((c.MAGIC_SPELLS[c.oldSpellId][7] + damage * Constants.MAGIC_EXP_RATE / 3), 3);
				c.getPA().refreshSkill(3);
				c.getPA().refreshSkill(6);
				if (!c.magicFailed) {
					if (System.currentTimeMillis() - PlayerHandler.players[i].reduceStat > 35000) {
						PlayerHandler.players[i].reduceStat = System.currentTimeMillis();
						switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
						case 12987:
						case 13011:
						case 12999:
						case 13023:
							PlayerHandler.players[i].getLevel()[0] -= ((o.getPA()
									.getLevelForXP(PlayerHandler.players[i].getExperience()[0]) * 10) / 100);
							break;
						}
					}

					switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
					case 12445: // teleblock
						if (System.currentTimeMillis() - o.teleBlockDelay > o.teleBlockLength) {
							o.teleBlockDelay = System.currentTimeMillis();
							o.sendMessage("You have been teleblocked.");
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
						int heal = (int) (damage / 4);
						if (c.getLevel()[3] + heal > c.getPA().getLevelForXP(c.getExperience()[3])) {
							c.getLevel()[3] = c.getPA().getLevelForXP(c.getExperience()[3]);
						} else {
							c.getLevel()[3] += heal;
						}
						c.getPA().refreshSkill(3);
						break;

					case 1153:
						PlayerHandler.players[i]
								.getLevel()[0] -= ((o.getPA().getLevelForXP(PlayerHandler.players[i].getExperience()[0])
										* 5) / 100);
						o.sendMessage("Your attack level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(0);
						break;

					case 1157:
						PlayerHandler.players[i]
								.getLevel()[2] -= ((o.getPA().getLevelForXP(PlayerHandler.players[i].getExperience()[2])
										* 5) / 100);
						o.sendMessage("Your strength level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(2);
						break;

					case 1161:
						PlayerHandler.players[i]
								.getLevel()[1] -= ((o.getPA().getLevelForXP(PlayerHandler.players[i].getExperience()[1])
										* 5) / 100);
						o.sendMessage("Your defence level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(1);
						break;

					case 1542:
						PlayerHandler.players[i]
								.getLevel()[1] -= ((o.getPA().getLevelForXP(PlayerHandler.players[i].getExperience()[1])
										* 10) / 100);
						o.sendMessage("Your defence level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(1);
						break;

					case 1543:
						PlayerHandler.players[i]
								.getLevel()[2] -= ((o.getPA().getLevelForXP(PlayerHandler.players[i].getExperience()[2])
										* 10) / 100);
						o.sendMessage("Your strength level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(2);
						break;

					case 1562:
						PlayerHandler.players[i]
								.getLevel()[0] -= ((o.getPA().getLevelForXP(PlayerHandler.players[i].getExperience()[0])
										* 10) / 100);
						o.sendMessage("Your attack level has been reduced!");
						PlayerHandler.players[i].reduceSpellDelay[c.reduceSpellId] = System.currentTimeMillis();
						o.getPA().refreshSkill(0);
						break;
					}
				}

				PlayerHandler.players[i].logoutDelay = System.currentTimeMillis();
				PlayerHandler.players[i].underAttackBy = c.playerId;
				PlayerHandler.players[i].killerId = c.playerId;
				PlayerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
				if (c.MAGIC_SPELLS[c.oldSpellId][6] != 0) {
					// Server.playerHandler.players[i].getLevel()[3] -= damage;
					PlayerHandler.players[i].dealDamage(damage);
					PlayerHandler.players[i].damageTaken[c.playerId] += damage;
					c.totalPlayerDamageDealt += damage;
					if (!c.magicFailed) {
						// Server.playerHandler.players[i].setHitDiff(damage);
						// Server.playerHandler.players[i].setHitUpdateRequired(true);
						PlayerHandler.players[i].handleHitMask(damage);
					}
				}
				applySmite(i, damage);
				c.killedBy = PlayerHandler.players[i].playerId;
				o.getPA().refreshSkill(3);
				PlayerHandler.players[i].updateRequired = true;
				c.usingMagic = false;
				c.castingMagic = false;
				if (o.inMulti() && multis()) {
					c.barrageCount = 0;
					for (int j = 0; j < PlayerHandler.players.length; j++) {
						if (PlayerHandler.players[j] != null) {
							if (j == o.playerId)
								continue;
							if (c.barrageCount >= 9)
								break;
							if (o.goodDistance(o.getX(), o.getY(), PlayerHandler.players[j].getX(),
									PlayerHandler.players[j].getY(), 1))
								appendMultiBarrage(j, c.magicFailed);
						}
					}
				}
				c.getPA().refreshSkill(3);
				c.getPA().refreshSkill(6);
				c.oldSpellId = 0;
			}
		}
		c.getPA().requestUpdates();
		if (c.bowSpecShot <= 0) {
			c.oldPlayerIndex = 0;
			c.projectileStage = 0;
			c.lastWeaponUsed = 0;
			c.doubleHit = false;
			c.bowSpecShot = 0;
		}
		if (c.bowSpecShot != 0) {
			c.bowSpecShot = 0;
		}
	}

	public boolean multis() {
		switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
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
			Client c2 = (Client) PlayerHandler.players[playerId];
			if (c2.isDead || c2.respawnTimer > 0)
				return;
			if (checkMultiBarrageReqs(playerId)) {
				c.barrageCount++;
				if (Misc.random(mageAtk()) > Misc.random(mageDef()) && !c.magicFailed) {
					if (getEndGfxHeight() == 100) { // end GFX
						c2.playGraphic(Graphic.create(c.MAGIC_SPELLS[c.oldSpellId][5], 0, 100));
					} else {
						c2.playGraphic(Graphic.create(c.MAGIC_SPELLS[c.oldSpellId][5], 0, 0));
					}
					int damage = Misc.random(c.MAGIC_SPELLS[c.oldSpellId][6]);
					if (c2.prayerActive[12]) {
						damage *= (int) (.60);
					}
					if (c2.getLevel()[3] - damage < 0) {
						damage = c2.getLevel()[3];
					}
					c.getPA().addSkillXP((c.MAGIC_SPELLS[c.oldSpellId][7] + damage * Constants.MAGIC_EXP_RATE), 6);
					c.getPA().addSkillXP((c.MAGIC_SPELLS[c.oldSpellId][7] + damage * Constants.MAGIC_EXP_RATE / 3), 3);
					// Server.playerHandler.players[playerId].setHitDiff(damage);
					// Server.playerHandler.players[playerId].setHitUpdateRequired(true);
					PlayerHandler.players[playerId].handleHitMask(damage);
					// Server.playerHandler.players[playerId].getLevel()[3] -=
					// damage;
					PlayerHandler.players[playerId].dealDamage(damage);
					PlayerHandler.players[playerId].damageTaken[c.playerId] += damage;
					c2.getPA().refreshSkill(3);
					c.totalPlayerDamageDealt += damage;
					multiSpellEffect(playerId, damage);
				} else {
					c2.playGraphic(Graphic.create(85, 0, 100));
				}
			}
		}
	}

	public void multiSpellEffect(int playerId, int damage) {
		switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
		case 13011:
		case 13023:
			if (System.currentTimeMillis() - PlayerHandler.players[playerId].reduceStat > 35000) {
				PlayerHandler.players[playerId].reduceStat = System.currentTimeMillis();
				PlayerHandler.players[playerId].getLevel()[0] -= ((PlayerHandler.players[playerId]
						.getLevelForXP(PlayerHandler.players[playerId].getExperience()[0]) * 10) / 100);
			}
			break;
		case 12919: // blood spells
		case 12929:
			int heal = (int) (damage / 4);
			if (c.getLevel()[3] + heal >= c.getPA().getLevelForXP(c.getExperience()[3])) {
				c.getLevel()[3] = c.getPA().getLevelForXP(c.getExperience()[3]);
			} else {
				c.getLevel()[3] += heal;
			}
			c.getPA().refreshSkill(3);
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

	public void appendDamageNPC(int i, int style, int damageMask) {
		int damage = 0;
		damage = c.getDamage()[style];
		if (NPCHandler.npcs[i] == null) {
			c.npcIndex = 0;
			return;
		}
		if (NPCHandler.npcs[i].isDead) {
			c.npcIndex = 0;
			return;
		}
		NPCHandler.npcs[i].facePlayer(c.playerId);

		if (NPCHandler.npcs[i].underAttackBy > 0 && Server.npcHandler.getsPulled(i)) {
			NPCHandler.npcs[i].killerId = c.playerId;
		} else if (NPCHandler.npcs[i].underAttackBy < 0 && !Server.npcHandler.getsPulled(i)) {
			NPCHandler.npcs[i].killerId = c.playerId;
		}
		if (c.projectileStage > 0) {
			if (c.msbSpec) {
				c.msbSpec = false;
				c.attackTimer++;
				c.hitDelay++;
			}
		} else if (c.projectileStage > 0 && c.castingMagic) {
			if (getEndGfxHeight() == 100 && !c.magicFailed) { // end GFX
				NPCHandler.npcs[i].gfx100(c.MAGIC_SPELLS[c.oldSpellId][5]);
			} else if (!c.magicFailed) {
				NPCHandler.npcs[i].gfx0(c.MAGIC_SPELLS[c.oldSpellId][5]);
			}

			if (c.magicFailed) {
				NPCHandler.npcs[i].gfx100(85);
			}
		}
		c.lastNpcAttacked = i;
		switch (damageMask) {
		case 1:
			NPCHandler.npcs[i].hitDiff = damage;
			NPCHandler.npcs[i].HP -= damage;
			c.totalDamageDealt += damage;
			NPCHandler.npcs[i].hitUpdateRequired = true;
			NPCHandler.npcs[i].updateRequired = true;
			break;

		case 2:
			NPCHandler.npcs[i].hitDiff2 = damage;
			NPCHandler.npcs[i].HP -= damage;
			c.totalDamageDealt += damage;
			NPCHandler.npcs[i].hitUpdateRequired2 = true;
			NPCHandler.npcs[i].updateRequired = true;
			c.doubleHit = false;
			break;
		}
		c.usingMagic = false;
		c.castingMagic = false;
		c.oldSpellId = 0;
	}

	public void appendDamage(int i, int style, int hitMask) {
		int damage = 0;
		damage = c.getDamage()[style];
		Client o = (Client) PlayerHandler.players[i];
		if (o == null) {
			return;
		}
		if (o.getLevel()[3] <= 0 || o.isDead || c.isDead || c.getLevel()[3] <= 0 || o.respawnTimer > 0) {
			c.faceUpdate(0);
			c.playerIndex = 0;
			return;
		}
		o.getPA().removeAllWindows();
		if (o.playerIndex <= 0 && o.npcIndex <= 0) {
			if (o.autoRet == 1) {
				o.playerIndex = c.playerId;
			}
		}
		if (c.projectileStage > 0 && !c.castingMagic) {
			if (c.msbSpec) {
				c.msbSpec = false;
				c.attackTimer++;
				c.hitDelay++;
			}
		} else if (c.projectileStage > 0) {
			if (getEndGfxHeight() == 100 && !c.magicFailed) { // end GFX
				o.playGraphic(Graphic.create(c.MAGIC_SPELLS[c.oldSpellId][5], 0, 100));
			} else if (!c.magicFailed) {
				o.playGraphic(Graphic.create(c.MAGIC_SPELLS[c.oldSpellId][5], 0, 0));
			} else if (c.magicFailed) {
				o.playGraphic(Graphic.create(85, 0, 100));
			}
		}
		if (o.vengOn && damage > 0)
			appendVengeance(i, damage);
		if (damage > 0)
			applyRecoil(damage, i);
		if (o.getEquipment()[c.playerAmulet] == 12853) {
			if (o.getEquipment()[c.playerWeapon] == 4718 && o.getEquipment()[c.playerHat] == 4716
					&& o.getEquipment()[c.playerChest] == 4720 && o.getEquipment()[c.playerLegs] == 4722) {
				if (Misc.random(3) == 0 && damage > 1) {
					int damnedDamage = (int) (damage * .15);
					if (damnedDamage > c.getLevel()[3]) {
						damnedDamage = c.getLevel()[3];
					}
					c.sendMessage(o.playerName + "'s amulet of damaged 15% of your damage on you.");
					c.dealDamage(damnedDamage);
					c.updateRequired = true;
					c.handleHitMask(damnedDamage);
					c.getPA().refreshSkill(3);
				}
			}
		}
		applySmite(i, damage);
		o.logoutDelay = System.currentTimeMillis();
		o.underAttackBy = c.playerId;
		o.killerId = c.playerId;
		o.singleCombatDelay = System.currentTimeMillis();
		if (c.killedBy != PlayerHandler.players[i].playerId)
			c.totalPlayerDamageDealt = 0;
		c.killedBy = o.playerId;
		o.dealDamage(damage);
		o.damageTaken[c.playerId] += damage;
		c.totalPlayerDamageDealt += damage;
		o.updateRequired = true;
		o.handleHitMask(damage);
		if (c.doubleHit) {
			Experience.calculateDamage(c, o, style);
			damage = c.getDamage()[style];
			o.dealDamage(damage);
			o.damageTaken[c.playerId] += damage;
			c.totalPlayerDamageDealt += damage;
			o.updateRequired = true;
			c.doubleHit = false;
			o.handleHitMask(damage);
		}
		o.getPA().refreshSkill(3);
		c.oldSpellId = 0;
		c.projectileStage = 0;
		c.usingMagic = false;
		c.castingMagic = false;
		c.getPA().requestUpdates();
	}

	public void applySmite(int index, int damage) {
		if (!c.prayerActive[23])
			return;
		if (damage <= 0)
			return;
		if (PlayerHandler.players[index] != null) {
			Client c2 = (Client) PlayerHandler.players[index];
			c2.getLevel()[5] -= (int) (damage / 4);
			if (c2.getLevel()[5] <= 0) {
				c2.getLevel()[5] = 0;
				c2.getCombat().resetPrayers();
			}
			c2.getPA().refreshSkill(5);
		}

	}

	public void fireProjectilePlayer() {
		if (c.oldPlayerIndex > 0) {
			if (PlayerHandler.players[c.oldPlayerIndex] != null) {
				c.projectileStage = 2;
				int pX = c.getX();
				int pY = c.getY();
				int oX = PlayerHandler.players[c.oldPlayerIndex].getX();
				int oY = PlayerHandler.players[c.oldPlayerIndex].getY();
				int offX = (pY - oY) * -1;
				int offY = (pX - oX) * -1;
				int clientSpeed = 0;
				int showDelay = 0;
				int slope = 0;
				int distance = c.distanceToPoint(oX, oY);
				String itemName = c.getItems().getItemName(c.lastWeaponUsed).toLowerCase();
				if (c.lastWeaponUsed == 9185 || c.lastWeaponUsed == 11785) {
					clientSpeed = c.armadylSpecial ? 75 : 55;
					showDelay = 45;
					slope = 5;
					if (c.armadylSpecial) {
						c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, clientSpeed, 301, 46, 36,
								-c.oldPlayerIndex - 1, showDelay, slope);
					} else {
						c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, clientSpeed, getRangeProjectileGFX(),
								46, 36, -c.oldPlayerIndex - 1, showDelay, slope);
					}
					return;
				} else if (itemName.contains("dart") || itemName.contains("knife") || itemName.contains("blowpipe")) {
					clientSpeed = 35;
					showDelay = 20;
					slope = 13;
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, clientSpeed, getRangeProjectileGFX(), 46,
							36, -c.oldPlayerIndex - 1, showDelay, slope);
					return;
				} else if (c.lastWeaponUsed == 11235) {
					if (distance <= 1) {
						clientSpeed = 55;
					} else if (distance > 1 && distance <= 3) {
						clientSpeed = 55;
					} else if (distance > 3 && distance <= 8) {
						clientSpeed = 65;
						c.hitDelay += 1;
					} else {
						clientSpeed = 75;
					}
					showDelay = 45;
					slope = 15;
					clientSpeed += 30;
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, clientSpeed, getRangeProjectileGFX(), 41,
							31, -c.oldPlayerIndex - 1, 36, 3);
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, clientSpeed + 10, getRangeProjectileGFX(),
							46, 36, -c.oldPlayerIndex - 1, 36, slope + 6);
					return;
				} else if (c.msbSpec) {
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 40 + (distance * 5), 249, 43, 35,
							-c.oldPlayerIndex - 1, 36, 15);
					c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, 65 + (distance * 5), 249, 43, 35,
							-c.oldPlayerIndex - 1, 36, 15);
					return;
				} else {
					clientSpeed = 55 + (distance * 5);
					if (distance > 2) {
						c.hitDelay += 1;
					}
					showDelay = 45;
					slope = 15;
				}
				c.getPA().createPlayersProjectile(pX, pY, offX, offY, 50, clientSpeed, getRangeProjectileGFX(), 46, 36,
						-c.oldPlayerIndex - 1, showDelay, slope);
			}
		}
	}

	public boolean usingDbow() {
		return c.getEquipment()[c.playerWeapon] == 11235;
	}

	/** Prayer **/

	public void activatePrayer(int i) {
		if (c.duelRule[7]) {
			for (int p = 0; p < c.PRAYER.length; p++) { // reset prayer glows
				c.prayerActive[p] = false;
				c.getPA().sendFrame36(c.PRAYER_GLOW[p], 0);
			}
			c.sendMessage("Prayer has been disabled in this duel!");
			return;
		}
		if (i == 24 && c.getLevel()[1] < 65) {
			c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
			c.sendMessage("You may not use this prayer yet.");
			return;
		}
		if (i == 25 && c.getLevel()[1] < 70) {
			c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
			c.sendMessage("You may not use this prayer yet.");
			return;
		}
		int[] defPray = { 0, 5, 13, 24, 25 };
		int[] strPray = { 1, 6, 14, 24, 25 };
		int[] atkPray = { 2, 7, 15, 24, 25 };
		int[] rangePray = { 3, 11, 19 };
		int[] magePray = { 4, 12, 20 };

		if (c.getLevel()[5] > 0 || !Constants.PRAYER_POINTS_REQUIRED) {
			if (c.getPA().getLevelForXP(c.getExperience()[5]) >= c.PRAYER_LEVEL_REQUIRED[i]
					|| !Constants.PRAYER_LEVEL_REQUIRED) {
				boolean headIcon = false;
				switch (i) {
				case 0:
				case 5:
				case 13:
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < defPray.length; j++) {
							if (defPray[j] != i) {
								c.prayerActive[defPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[defPray[j]], 0);
							}
						}
					}
					break;

				case 1:
				case 6:
				case 14:
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.prayerActive[strPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.prayerActive[rangePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.prayerActive[magePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;

				case 2:
				case 7:
				case 15:
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								c.prayerActive[atkPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.prayerActive[rangePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.prayerActive[magePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;

				case 3:// range prays
				case 11:
				case 19:
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								c.prayerActive[atkPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.prayerActive[strPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.prayerActive[rangePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.prayerActive[magePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case 4:
				case 12:
				case 20:
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								c.prayerActive[atkPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.prayerActive[strPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.prayerActive[rangePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.prayerActive[magePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
							}
						}
					}
					break;
				case 10:
					c.lastProtItem = System.currentTimeMillis();
					break;

				case 16:
				case 17:
				case 18:
					if (System.currentTimeMillis() - c.stopPrayerDelay < 5000) {
						c.sendMessage("You have been injured and can't use this prayer!");
						c.getPA().sendFrame36(c.PRAYER_GLOW[16], 0);
						c.getPA().sendFrame36(c.PRAYER_GLOW[17], 0);
						c.getPA().sendFrame36(c.PRAYER_GLOW[18], 0);
						return;
					}
					if (i == 16)
						c.protMageDelay = System.currentTimeMillis();
					else if (i == 17)
						c.protRangeDelay = System.currentTimeMillis();
					else if (i == 18)
						c.protMeleeDelay = System.currentTimeMillis();
				case 21:
				case 22:
				case 23:
					headIcon = true;
					for (int p = 16; p < 24; p++) {
						if (i != p && p != 19 && p != 20) {
							c.prayerActive[p] = false;
							c.getPA().sendFrame36(c.PRAYER_GLOW[p], 0);
						}
					}
					break;
				case 24:
				case 25:
					if (c.prayerActive[i] == false) {
						for (int j = 0; j < atkPray.length; j++) {
							if (atkPray[j] != i) {
								c.prayerActive[atkPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[atkPray[j]], 0);
							}
						}
						for (int j = 0; j < strPray.length; j++) {
							if (strPray[j] != i) {
								c.prayerActive[strPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[strPray[j]], 0);
							}
						}
						for (int j = 0; j < rangePray.length; j++) {
							if (rangePray[j] != i) {
								c.prayerActive[rangePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[rangePray[j]], 0);
							}
						}
						for (int j = 0; j < magePray.length; j++) {
							if (magePray[j] != i) {
								c.prayerActive[magePray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[magePray[j]], 0);
							}
						}
						for (int j = 0; j < defPray.length; j++) {
							if (defPray[j] != i) {
								c.prayerActive[defPray[j]] = false;
								c.getPA().sendFrame36(c.PRAYER_GLOW[defPray[j]], 0);
							}
						}
					}
					break;
				}

				if (!headIcon) {
					if (c.prayerActive[i] == false) {
						c.prayerActive[i] = true;
						c.getPA().sendFrame36(c.PRAYER_GLOW[i], 1);
					} else {
						c.prayerActive[i] = false;
						c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
					}
				} else {
					if (c.prayerActive[i] == false) {
						c.prayerActive[i] = true;
						c.getPA().sendFrame36(c.PRAYER_GLOW[i], 1);
						c.headIcon = c.PRAYER_HEAD_ICONS[i];
						c.getPA().requestUpdates();
					} else {
						c.prayerActive[i] = false;
						c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
						c.headIcon = -1;
						c.getPA().requestUpdates();
					}
				}
			} else {
				c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
				c.getPA().sendFrame126("You need a @blu@Prayer level of " + c.PRAYER_LEVEL_REQUIRED[i] + " to use "
						+ c.PRAYER_NAME[i] + ".", 357);
				c.getPA().sendFrame126("Click here to continue", 358);
				c.getPA().sendFrame164(356);
			}
		} else {
			c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
			c.sendMessage("You have run out of prayer points!");
		}

	}

	/**
	 * Specials
	 **/

	public void activateSpecial(int weapon, int i) {
		c.getSA().activateSpecial(weapon, i);
	}

	public boolean checkSpecAmount(int weapon) {
		if(c.specAmount >= c.getSA().specAmount()) {
			c.specAmount -= c.getSA().specAmount();
			c.getItems().addSpecialBar(weapon);
			return true;
		}
		return false;
	}

	public void resetPlayerAttack() {
		c.usingMagic = false;
		c.npcIndex = 0;
		c.playerIndex = 0;
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
		int killerId = 0;
		for (int i = 1; i < Constants.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				if (PlayerHandler.players[i].killedBy == playerId) {
					if (PlayerHandler.players[i].withinDistance(PlayerHandler.players[playerId])) {
						if (PlayerHandler.players[i].totalPlayerDamageDealt > oldDamage) {
							oldDamage = PlayerHandler.players[i].totalPlayerDamageDealt;
							killerId = i;
						}
					}
					PlayerHandler.players[i].totalPlayerDamageDealt = 0;
					PlayerHandler.players[i].killedBy = 0;
				}
			}
		}
		return killerId;
	}

	double[] prayerData = { 1, // Thick Skin.
			1, // Burst of Strength.
			1, // Clarity of Thought.
			1, // Sharp Eye.
			1, // Mystic Will.
			2, // Rock Skin.
			2, // SuperHuman Strength.
			2, // Improved Reflexes.
			0.4, // Rapid restore.
			0.6, // Rapid Heal.
			0.6, // Protect Items.
			1.5, // Hawk eye.
			2, // Mystic Lore.
			4, // Steel Skin.
			4, // Ultimate Strength.
			4, // Incredible Reflexes.
			4, // Protect from Magic.
			4, // Protect from Missiles.
			4, // Protect from Melee.
			4, // Eagle Eye.
			4, // Mystic Might.
			1, // Retribution.
			2, // Redemption.
			6, // Smite.
			8, // Chivalry.
			8, // Piety.
	};

	public void handlePrayerDrain() {
		c.usingPrayer = false;
		double toRemove = 0.0;
		for (int j = 0; j < prayerData.length; j++) {
			if (c.prayerActive[j]) {
				toRemove += prayerData[j] / 20;
				c.usingPrayer = true;
			}
		}
		if (toRemove > 0) {
			toRemove /= (1 + (0.035 * c.playerBonus[11]));
		}
		c.prayerPoint -= toRemove;
		if (c.prayerPoint <= 0) {
			c.prayerPoint = 1.0 + c.prayerPoint;
			reducePrayerLevel();
		}

	}

	public void reducePrayerLevel() {
		if (c.getLevel()[5] - 1 > 0) {
			c.getLevel()[5] -= 1;
		} else {
			c.sendMessage("You have run out of prayer points!");
			c.getLevel()[5] = 0;
			resetPrayers();
			c.prayerId = -1;
		}
		c.getPA().refreshSkill(5);
	}

	public void resetPrayers() {
		for (int i = 0; i < c.prayerActive.length; i++) {
			c.prayerActive[i] = false;
			c.getPA().sendFrame36(c.PRAYER_GLOW[i], 0);
		}
		c.headIcon = -1;
		c.getPA().requestUpdates();
	}

	/**
	 * Wildy and duel info
	 **/

	public boolean checkReqs() {
		if (PlayerHandler.players[c.playerIndex] == null) {
			return false;
		}
		if (c.playerIndex == c.playerId)
			return false;
		if (c.inPits && PlayerHandler.players[c.playerIndex].inPits)
			return true;
		if (c.duelStatus == 5 && PlayerHandler.players[c.playerIndex].duelStatus == 5
				&& PlayerHandler.players[c.playerIndex].arenas() && c.arenas()) {
			if (PlayerHandler.players[c.playerIndex].duelingWith == c.getId()) {
				return true;
			} else {
				c.sendMessage("This isn't your opponent!");
				return false;
			}
		}
		if (PlayerHandler.players[c.playerIndex].inSafeZone() && isAttackable(c.playerIndex)) {
			c.sendMessage(PlayerHandler.players[c.playerIndex].playerName + " is in a safe zone.");
			c.stopMovement();
			c.getCombat().resetPlayerAttack();
			return false;
		}
		if (c.inSafeZone() && isAttackable(c.playerIndex)) {
			c.sendMessage("You are not in a PVP zone.");
			c.stopMovement();
			c.getCombat().resetPlayerAttack();
			return false;
		}
		if (!c.withinRange((Client) PlayerHandler.players[c.playerIndex])) {
			c.sendMessage("Your combat level difference is too great to attack that player here.");
			c.stopMovement();
			c.getCombat().resetPlayerAttack();
			return false;
		}
		if (!PlayerHandler.players[c.playerIndex].inMulti()) {
			if (PlayerHandler.players[c.playerIndex].underAttackBy != c.playerId
					&& PlayerHandler.players[c.playerIndex].underAttackBy != 0) {
				c.sendMessage("That player is already in combat.");
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return false;
			}
			if (PlayerHandler.players[c.playerIndex].playerId != c.underAttackBy && c.underAttackBy != 0
					|| c.underAttackBy2 > 0) {
				c.sendMessage("You are already in combat.");
				c.stopMovement();
				c.getCombat().resetPlayerAttack();
				return false;
			}
		}
		return true;
	}

	public boolean checkMultiBarrageReqs(int i) {
		if (PlayerHandler.players[i] == null) {
			return false;
		}
		if (i == c.playerId)
			return false;
		if (c.inPits && PlayerHandler.players[i].inPits)
			return true;
		if (!PlayerHandler.players[i].inWild()) {
			return false;
		}
		if (Constants.COMBAT_LEVEL_DIFFERENCE) {
			int combatDif1 = c.getCombat().getCombatDifference(c.combatLevel, PlayerHandler.players[i].combatLevel);
			if (combatDif1 > c.wildLevel || combatDif1 > PlayerHandler.players[i].wildLevel) {
				c.sendMessage("Your combat level difference is too great to attack that player here.");
				return false;
			}
		}

		if (Constants.SINGLE_AND_MULTI_ZONES) {
			if (!PlayerHandler.players[i].inMulti()) { // single combat
														// zones
				if (PlayerHandler.players[i].underAttackBy != c.playerId
						&& PlayerHandler.players[i].underAttackBy != 0) {
					return false;
				}
				if (PlayerHandler.players[i].playerId != c.underAttackBy && c.underAttackBy != 0) {
					c.sendMessage("You are already in combat.");
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * Weapon stand, walk, run, etc emotes
	 **/

	public void getPlayerAnimIndex() {
		c.playerStandIndex = WeaponAnimations.standAnimation(c);
		c.playerTurnIndex = 0x337;
		c.playerWalkIndex = WeaponAnimations.walkAnimation(c);
		c.playerRunIndex = WeaponAnimations.runAnimation(c);
		WeaponAnimations.setTurnAnimations(c);
		c.getPA().requestUpdates();
	}

	/**
	 * Weapon and magic attack speed!
	 **/

	public int getAttackDelay(String s) {
		if (c.usingMagic) {
			switch (c.MAGIC_SPELLS[c.spellId][0]) {
			case 12871: // ice blitz
			case 13023: // shadow barrage
			case 12891: // ice barrage
				return 5;

			default:
				return 5;
			}
		}
		if (c.getEquipment()[c.playerWeapon] == -1)
			return 4;// unarmed

		switch (c.getEquipment()[c.playerWeapon]) {
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
			else if (s.contains("crossbow"))
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
		else if (s.contains("whip") || s.contains("tentacle"))
			return 4;
		else if (s.contains("dart") || s.contains("blowpipe"))
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
		if (c.usingMagic) {
			switch (c.MAGIC_SPELLS[c.spellId][0]) {
			case 12891:
				return 4;
			case 12871:
				return 6;
			default:
				return 4;
			}
		} else {

			if (weaponName.contains("knife") || weaponName.contains("dart") || weaponName.contains("javelin")
					|| weaponName.contains("thrownaxe") || weaponName.contains("blowpipe")) {
				return 3;
			}
			if (weaponName.contains("cross") || weaponName.contains("c'bow")) {
				return 3;
			}
			if (weaponName.contains("bow")) {
				return 3;
			} else if (c.dbowSpec) {
				return 3;
			}

			switch (c.getEquipment()[c.playerWeapon]) {
			case 6522: // Toktz-xil-ul
				return 3;
			}
			return 2;
		}
	}

	public int getRequiredDistance() {
		if (!c.usingMagic && !c.usingBow && c.freezeTimer <= 0) {
			return c.isMoving ? 3 : 1;
		} else if (c.usingMagic || c.usingBow) {
			return 9;
		}
		return 1;
	}

	public boolean usingHally() {
		switch (c.getEquipment()[c.playerWeapon]) {
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
		int attackLevel = c.getLevel()[0];
		if (c.prayerActive[2]) {
			attackLevel = (int) ((double) attackLevel
					+ (double) c.getLevelForXP(c.getExperience()[c.playerAttack]) * 0.05D);
		} else if (c.prayerActive[7]) {
			attackLevel = (int) ((double) attackLevel
					+ (double) c.getLevelForXP(c.getExperience()[c.playerAttack]) * 0.1D);
		} else if (c.prayerActive[15]) {
			attackLevel = (int) ((double) attackLevel
					+ (double) c.getLevelForXP(c.getExperience()[c.playerAttack]) * 0.15D);
		} else if (c.prayerActive[24]) {
			attackLevel = (int) ((double) attackLevel
					+ (double) c.getLevelForXP(c.getExperience()[c.playerAttack]) * 0.15D);
		} else if (c.prayerActive[25]) {
			attackLevel = (int) ((double) attackLevel
					+ (double) c.getLevelForXP(c.getExperience()[c.playerAttack]) * 0.2D);
		}

		if (c.fullVoidMelee()) {
			attackLevel = (int) ((double) attackLevel
					+ (double) c.getLevelForXP(c.getExperience()[c.playerAttack]) * 0.1D);
		}

		attackLevel = (int) ((double) attackLevel * c.specAccuracy);
		int i = c.playerBonus[bestMeleeAtk()];
		i += c.bonusAttack;

		int toReturn = (int) ((double) attackLevel + (double) attackLevel * 0.15D + (double) i + (double) i * 0.5D);

		return toReturn;
	}

	public int bestMeleeAtk() {
		return c.playerBonus[0] > c.playerBonus[1] && c.playerBonus[0] > c.playerBonus[2] ? 0
				: (c.playerBonus[1] > c.playerBonus[0] && c.playerBonus[1] > c.playerBonus[2] ? 1
						: (c.playerBonus[2] > c.playerBonus[1] && c.playerBonus[2] > c.playerBonus[0] ? 2 : 0));
	}

	public int calculateMeleeMaxHit() {
		int maxHit = 0;
		double prayerMultiplier = 1;
		double otherBonusMultiplier = 1; // TODO: void melee = 1.2, slayer helm
											// = 1.15, salve amulet = 1.15,
											// salve amulet(e) = 1.2
		int strengthLevel = c.getLevel()[2];
		int combatStyleBonus = 0;

		if (c.prayerActive[1]) {
			prayerMultiplier = 1.05;
		} else if (c.prayerActive[6]) {
			prayerMultiplier = 1.1;
		} else if (c.prayerActive[14]) {
			prayerMultiplier = 1.15;
		} else if (c.prayerActive[24]) {
			prayerMultiplier = 1.18;
		} else if (c.prayerActive[25]) {
			prayerMultiplier = 1.23;
		}

		if (c.fullVoidMelee()) {
			otherBonusMultiplier = 1.1;
		}

		int effectiveStrengthDamage = (int) ((strengthLevel * prayerMultiplier * otherBonusMultiplier)
				+ combatStyleBonus);
		double baseDamage = 1.3 + (effectiveStrengthDamage / 10) + (c.getCombatBonus()[0] / 80)
				+ ((effectiveStrengthDamage * c.getCombatBonus()[0]) / 640);

		maxHit = (int) (baseDamage * c.specDamage);

		if (c.getEquipment()[c.playerWeapon] == 4718 && c.getEquipment()[c.playerHat] == 4716
				&& c.getEquipment()[c.playerChest] == 4720 && c.getEquipment()[c.playerLegs] == 4722) {
			int hpLost = c.getPA().getLevelForXP(c.getExperience()[3]) - c.getLevel()[3];
			maxHit += hpLost * 0.35;
		}
		if (c.getEquipment()[3] == 6528 && c.getEquipment()[c.playerAmulet] == 11128) {
			maxHit += ((int) maxHit * .20);
		}
		return (int) Math.floor(maxHit);
	}

	public int calculateMeleeDefence() {
		int defenceLevel = c.getLevel()[1];
		int i = c.playerBonus[bestMeleeDef()];
		if (c.prayerActive[0]) {
			defenceLevel = (int) ((double) defenceLevel
					+ (double) c.getLevelForXP(c.getExperience()[c.playerDefence]) * 0.05D);
		} else if (c.prayerActive[5]) {
			defenceLevel = (int) ((double) defenceLevel
					+ (double) c.getLevelForXP(c.getExperience()[c.playerDefence]) * 0.1D);
		} else if (c.prayerActive[13]) {
			defenceLevel = (int) ((double) defenceLevel
					+ (double) c.getLevelForXP(c.getExperience()[c.playerDefence]) * 0.15D);
		} else if (c.prayerActive[24]) {
			defenceLevel = (int) ((double) defenceLevel
					+ (double) c.getLevelForXP(c.getExperience()[c.playerDefence]) * 0.2D);
		} else if (c.prayerActive[25]) {
			defenceLevel = (int) ((double) defenceLevel
					+ (double) c.getLevelForXP(c.getExperience()[c.playerDefence]) * 0.25D);
		}
		double mod = 1.0D;
		return (int) (((double) defenceLevel + (double) defenceLevel * 0.05D + (double) i + (double) i * 0.05D) * mod);
	}

	public int bestMeleeDef() {
		return c.playerBonus[5] > c.playerBonus[6] && c.playerBonus[5] > c.playerBonus[7] ? 5
				: (c.playerBonus[6] > c.playerBonus[5] && c.playerBonus[6] > c.playerBonus[7] ? 6
						: (c.playerBonus[7] > c.playerBonus[5] && c.playerBonus[7] > c.playerBonus[6] ? 7 : 5));
	}

	/**
	 * Range
	 **/

	public int calculateRangeAttack() {
		int attackLevel = c.getLevel()[4];
		attackLevel *= c.specAccuracy;
		if (c.fullVoidRange())
			attackLevel += c.getLevelForXP(c.getExperience()[c.playerRanged]) * 0.1;
		if (c.prayerActive[3])
			attackLevel *= 1.05;
		else if (c.prayerActive[11])
			attackLevel *= 1.10;
		else if (c.prayerActive[19])
			attackLevel *= 1.15;
		// dbow spec
		if (c.fullVoidRange() && c.specAccuracy > 1.15) {
			attackLevel *= 1.75;
		}
		return (int) (attackLevel + (c.playerBonus[4] * 1.95));
	}

	public int calculateRangeDefence() {
		int defenceLevel = c.getLevel()[1];
		if (c.prayerActive[0]) {
			defenceLevel += c.getLevelForXP(c.getExperience()[c.playerDefence]) * 0.05;
		} else if (c.prayerActive[5]) {
			defenceLevel += c.getLevelForXP(c.getExperience()[c.playerDefence]) * 0.1;
		} else if (c.prayerActive[13]) {
			defenceLevel += c.getLevelForXP(c.getExperience()[c.playerDefence]) * 0.15;
		} else if (c.prayerActive[24]) {
			defenceLevel += c.getLevelForXP(c.getExperience()[c.playerDefence]) * 0.2;
		} else if (c.prayerActive[25]) {
			defenceLevel += c.getLevelForXP(c.getExperience()[c.playerDefence]) * 0.25;
		}
		return (int) (defenceLevel + c.playerBonus[9] + (c.playerBonus[9] / 2));
	}

	public boolean usingBolts() {
		return c.getEquipment()[c.playerArrows] >= 9130 && c.getEquipment()[c.playerArrows] <= 9145
				|| c.getEquipment()[c.playerArrows] >= 9230 && c.getEquipment()[c.playerArrows] <= 9245;
	}

	public int rangeMaxHit() {
		int a = c.getLevel()[4];
		int d = getRangeStr(c.usingBow ? c.lastArrowUsed : c.rangeItemUsed);
		double b = 1.00;
		if (c.prayerActive[3]) {
			b *= 1.05;
		} else if (c.prayerActive[11]) {
			b *= 1.10;
		} else if (c.prayerActive[19]) {
			b *= 1.15;
		}
		if (c.fullVoidRange()) {
			b *= 1.20;
		}
		double e = Math.floor(a * b);
		if (c.fightMode == 0) {
			e = (e + 3.0);
		}
		double darkbow = 1.0;
		if (c.dbowSpec) {
			if (c.lastWeaponUsed == 11235) {
				if (c.lastArrowUsed == 11212) {
					darkbow = 1.5;
				} else {
					darkbow = 1.3;
				}
			}
		}
		double max = (1.3 + e / 10 + d / 80 + e * d / 640) * darkbow;
		return (int) max;
	}

	public int getRangeStr(int i) {
		int str = 0;
		int[][] data = { { 877, 10 }, { 9140, 46 }, { 9145, 36 }, { 9141, 64 }, { 9142, 82 }, { 9143, 100 },
				{ 9144, 115 }, { 9236, 14 }, { 9237, 30 }, { 9238, 48 }, { 9239, 66 }, { 9240, 83 }, { 9241, 85 },
				{ 9242, 103 }, { 9243, 105 }, { 9244, 117 }, { 9245, 120 }, { 882, 7 }, { 884, 10 }, { 886, 16 },
				{ 888, 22 }, { 890, 31 }, { 892, 49 }, { 4740, 55 }, { 11212, 60 }, { 806, 1 }, { 807, 3 }, { 808, 4 },
				{ 809, 7 }, { 810, 10 }, { 811, 14 }, { 12926, 60 }, { 11230, 20 }, { 864, 3 }, { 863, 4 }, { 865, 7 },
				{ 866, 10 }, { 867, 14 }, { 868, 24 }, { 825, 6 }, { 826, 10 }, { 827, 12 }, { 828, 18 }, { 829, 28 },
				{ 830, 42 }, { 800, 5 }, { 801, 7 }, { 802, 11 }, { 803, 16 }, { 804, 23 }, { 805, 36 }, { 9976, 0 },
				{ 9977, 15 }, { 4212, 70 }, { 4214, 70 }, { 4215, 70 }, { 4216, 70 }, { 4217, 70 }, { 4218, 70 },
				{ 4219, 70 }, { 4220, 70 }, { 4221, 70 }, { 4222, 70 }, { 4223, 70 }, { 6522, 49 }, { 10034, 15 }, };
		for (int l = 0; l < data.length; l++) {
			if (i == data[l][0]) {
				str = data[l][1];
			}
		}
		return str;
	}

	/*
	 * public int rangeMaxHit() { int rangehit = 0; rangehit += c.getLevel()[4]
	 * / 7.5; int weapon = c.lastWeaponUsed; int Arrows = c.lastArrowUsed; if
	 * (weapon == 4223) {//Cbow 1/10 rangehit = 2; rangehit += c.getLevel()[4] /
	 * 7; } else if (weapon == 4222) {//Cbow 2/10 rangehit = 3; rangehit +=
	 * c.getLevel()[4] / 7; } else if (weapon == 4221) {//Cbow 3/10 rangehit =
	 * 3; rangehit += c.getLevel()[4] / 6.5; } else if (weapon == 4220) {//Cbow
	 * 4/10 rangehit = 4; rangehit += c.getLevel()[4] / 6.5; } else if (weapon
	 * == 4219) {//Cbow 5/10 rangehit = 4; rangehit += c.getLevel()[4] / 6; }
	 * else if (weapon == 4218) {//Cbow 6/10 rangehit = 5; rangehit +=
	 * c.getLevel()[4] / 6; } else if (weapon == 4217) {//Cbow 7/10 rangehit =
	 * 5; rangehit += c.getLevel()[4] / 5.5; } else if (weapon == 4216) {//Cbow
	 * 8/10 rangehit = 6; rangehit += c.getLevel()[4] / 5.5; } else if (weapon
	 * == 4215) {//Cbow 9/10 rangehit = 6; rangehit += c.getLevel()[4] / 5; }
	 * else if (weapon == 4214) {//Cbow Full rangehit = 7; rangehit +=
	 * c.getLevel()[4] / 5; } else if (weapon == 6522) { rangehit = 5; rangehit
	 * += c.getLevel()[4] / 6; } else if (weapon == 9029) {//dragon darts
	 * rangehit = 8; rangehit += c.getLevel()[4] / 10; } else if (weapon == 811
	 * || weapon == 868) {//rune darts rangehit = 2; rangehit += c.getLevel()[4]
	 * / 8.5; } else if (weapon == 810 || weapon == 867) {//adamant darts
	 * rangehit = 2; rangehit += c.getLevel()[4] / 9; } else if (weapon == 809
	 * || weapon == 866) {//mithril darts rangehit = 2; rangehit +=
	 * c.getLevel()[4] / 9.5; } else if (weapon == 808 || weapon == 865)
	 * {//Steel darts rangehit = 2; rangehit += c.getLevel()[4] / 10; } else if
	 * (weapon == 807 || weapon == 863) {//Iron darts rangehit = 2; rangehit +=
	 * c.getLevel()[4] / 10.5; } else if (weapon == 806 || weapon == 864)
	 * {//Bronze darts rangehit = 1; rangehit += c.getLevel()[4] / 11; } else if
	 * (Arrows == 4740 && weapon == 4734) {//BoltRacks rangehit = 3; rangehit +=
	 * c.getLevel()[4] / 6; } else if (Arrows == 11212) {//dragon arrows
	 * rangehit = 4; rangehit += c.getLevel()[4] / 5.5; } else if (Arrows ==
	 * 892) {//rune arrows rangehit = 3; rangehit += c.getLevel()[4] / 6; } else
	 * if (Arrows == 890) {//adamant arrows rangehit = 2; rangehit +=
	 * c.getLevel()[4] / 7; } else if (Arrows == 888) {//mithril arrows rangehit
	 * = 2; rangehit += c.getLevel()[4] / 7.5; } else if (Arrows == 886)
	 * {//steel arrows rangehit = 2; rangehit += c.getLevel()[4] / 8; } else if
	 * (Arrows == 884) {//Iron arrows rangehit = 2; rangehit += c.getLevel()[4]
	 * / 9; } else if (Arrows == 882) {//Bronze arrows rangehit = 1; rangehit +=
	 * c.getLevel()[4] / 9.5; } else if (Arrows == 9244) { rangehit = 8;
	 * rangehit += c.getLevel()[4] / 3; } else if (Arrows == 9139) { rangehit =
	 * 12; rangehit += c.getLevel()[4] / 4; } else if (Arrows == 9140) {
	 * rangehit = 2; rangehit += c.getLevel()[4] / 7; } else if (Arrows == 9141)
	 * { rangehit = 3; rangehit += c.getLevel()[4] / 6; } else if (Arrows ==
	 * 9142) { rangehit = 4; rangehit += c.getLevel()[4] / 6; } else if (Arrows
	 * == 9143) { rangehit = 7; rangehit += c.getLevel()[4] / 5; } else if
	 * (Arrows == 9144) { rangehit = 7; rangehit += c.getLevel()[4] / 4.5; } int
	 * bonus = 0; bonus -= rangehit / 10; rangehit += bonus; if (c.specDamage !=
	 * 1) rangehit *= c.specDamage; if (rangehit == 0) rangehit++; if
	 * (c.fullVoidRange()) { rangehit *= 1.10; } if (c.prayerActive[3]) rangehit
	 * *= 1.05; else if (c.prayerActive[11]) rangehit *= 1.10; else if
	 * (c.prayerActive[19]) rangehit *= 1.15; return rangehit; }
	 */

	public boolean properBolts() {
		return c.getEquipment()[c.playerArrows] >= 9140 && c.getEquipment()[c.playerArrows] <= 9144
				|| c.getEquipment()[c.playerArrows] >= 9240 && c.getEquipment()[c.playerArrows] <= 9244;
	}

	public boolean usingCorrectArrows() {
		int data[][] = { { 841, 882, 884 }, { 843, 882, 884, 886 }, { 849, 882, 884, 886, 888 },
				{ 853, 882, 884, 886, 888, 890 }, { 857, 882, 884, 886, 888, 890 },
				{ 861, 882, 884, 886, 888, 890, 892 }, { 11235, 882, 884, 886, 888, 890, 11212 },
				{ 9185, 9144, 9244, 9243 }, { 11785, 9144, 9244, 9243 }, { 4734, 4740 } };
		for (int i = 0; i < data.length; i++) {
			if (c.getEquipment()[3] == data[i][0]) {
				for (int x = 0; x < data[i].length; x++) {
					if (c.getEquipment()[c.playerArrows] == data[i][x]) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public int getRangeStartGFX() {
		switch (c.rangeItemUsed) {

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
		if (c.dbowSpec) {
			return 1099;
		}
		if (c.msbSpec) {
			return 249;
		}
		if (c.getEquipment()[3] == 9185 || c.getEquipment()[3] == 11785)
			return 27;
		switch (c.rangeItemUsed) {

		case 12926:
			return 1122;

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
		if (c.dbowSpec)
			return 100;
		return 70;
	}

	public int getProjectileShowDelay() {
		switch (c.getEquipment()[c.playerWeapon]) {
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

	public int mageAtk() {
		int attackLevel = c.getLevel()[6];
		if (c.fullVoidMage()) {
			attackLevel = (int) ((double) attackLevel + (double) c.getLevelForXP(c.getExperience()[6]) * 0.2D);
		}

		if (c.prayerActive[4]) {
			attackLevel = (int) ((double) attackLevel * 1.05D);
		} else if (c.prayerActive[12]) {
			attackLevel = (int) ((double) attackLevel * 1.1D);
		} else if (c.prayerActive[20]) {
			attackLevel = (int) ((double) attackLevel * 1.15D);
		}

		return (int) ((double) attackLevel + (double) c.playerBonus[3] * 2.25D);
	}

	public int mageDef() {
		int defenceLevel = c.getLevel()[1] / 2 + c.getLevel()[6] / 2;
		if (c.prayerActive[4]) {
			defenceLevel = (int) ((double) defenceLevel
					+ (double) c.getLevelForXP(c.getExperience()[c.playerDefence]) * 0.05D);
		} else if (c.prayerActive[12]) {
			defenceLevel = (int) ((double) defenceLevel
					+ (double) c.getLevelForXP(c.getExperience()[c.playerDefence]) * 0.1D);
		} else if (c.prayerActive[20]) {
			defenceLevel = (int) ((double) defenceLevel
					+ (double) c.getLevelForXP(c.getExperience()[c.playerDefence]) * 0.15D);
		}

		return defenceLevel + c.playerBonus[8] + c.playerBonus[8] / 4;
	}

	public boolean wearingStaff(int runeId) {
		int wep = c.getEquipment()[c.playerWeapon];
		switch (runeId) {
		case 554:
			if (wep == 1387)
				return true;
			break;
		case 555:
			if (wep == 1383)
				return true;
			break;
		case 556:
			if (wep == 1381)
				return true;
			break;
		case 557:
			if (wep == 1385)
				return true;
			break;
		}
		return false;
	}

	public boolean checkMagicReqs(int spell) {
		if (c.usingMagic && Constants.RUNES_REQUIRED) { // check for runes
			if ((!c.getItems().playerHasItem(c.MAGIC_SPELLS[spell][8], c.MAGIC_SPELLS[spell][9])
					&& !wearingStaff(c.MAGIC_SPELLS[spell][8]))
					|| (!c.getItems().playerHasItem(c.MAGIC_SPELLS[spell][10], c.MAGIC_SPELLS[spell][11])
							&& !wearingStaff(c.MAGIC_SPELLS[spell][10]))
					|| (!c.getItems().playerHasItem(c.MAGIC_SPELLS[spell][12], c.MAGIC_SPELLS[spell][13])
							&& !wearingStaff(c.MAGIC_SPELLS[spell][12]))
					|| (!c.getItems().playerHasItem(c.MAGIC_SPELLS[spell][14], c.MAGIC_SPELLS[spell][15])
							&& !wearingStaff(c.MAGIC_SPELLS[spell][14]))) {
				c.sendMessage("You don't have the required runes to cast this spell.");
				return false;
			}
		}

		if (c.usingMagic && c.playerIndex > 0) {
			if (PlayerHandler.players[c.playerIndex] != null) {
				for (int r = 0; r < c.REDUCE_SPELLS.length; r++) { // reducing
																	// spells,
																	// confuse
																	// etc
					if (PlayerHandler.players[c.playerIndex].REDUCE_SPELLS[r] == c.MAGIC_SPELLS[spell][0]) {
						c.reduceSpellId = r;
						if ((System.currentTimeMillis()
								- PlayerHandler.players[c.playerIndex].reduceSpellDelay[c.reduceSpellId]) > PlayerHandler.players[c.playerIndex].REDUCE_SPELL_TIME[c.reduceSpellId]) {
							PlayerHandler.players[c.playerIndex].canUseReducingSpell[c.reduceSpellId] = true;
						} else {
							PlayerHandler.players[c.playerIndex].canUseReducingSpell[c.reduceSpellId] = false;
						}
						break;
					}
				}
				if (!PlayerHandler.players[c.playerIndex].canUseReducingSpell[c.reduceSpellId]) {
					c.sendMessage("That player is currently immune to this spell.");
					c.usingMagic = false;
					c.stopMovement();
					resetPlayerAttack();
					return false;
				}
			}
		}

		int staffRequired = getStaffNeeded();
		if (c.usingMagic && staffRequired > 0 && Constants.RUNES_REQUIRED) { // staff
																			// required
			if (c.getEquipment()[c.playerWeapon] != staffRequired) {
				c.sendMessage(
						"You need a " + c.getItems().getItemName(staffRequired).toLowerCase() + " to cast this spell.");
				return false;
			}
		}

		if (c.usingMagic && Constants.MAGIC_LEVEL_REQUIRED) { // check magic level
			if (c.getLevel()[6] < c.MAGIC_SPELLS[spell][1]) {
				c.sendMessage("You need to have a magic level of " + c.MAGIC_SPELLS[spell][1] + " to cast this spell.");
				return false;
			}
		}
		if (c.usingMagic && Constants.RUNES_REQUIRED) {
			if (c.MAGIC_SPELLS[spell][8] > 0) { // deleting runes
				if (!wearingStaff(c.MAGIC_SPELLS[spell][8]))
					c.getItems().deleteItem(c.MAGIC_SPELLS[spell][8],
							c.getItems().getItemSlot(c.MAGIC_SPELLS[spell][8]), c.MAGIC_SPELLS[spell][9]);
			}
			if (c.MAGIC_SPELLS[spell][10] > 0) {
				if (!wearingStaff(c.MAGIC_SPELLS[spell][10]))
					c.getItems().deleteItem(c.MAGIC_SPELLS[spell][10],
							c.getItems().getItemSlot(c.MAGIC_SPELLS[spell][10]), c.MAGIC_SPELLS[spell][11]);
			}
			if (c.MAGIC_SPELLS[spell][12] > 0) {
				if (!wearingStaff(c.MAGIC_SPELLS[spell][12]))
					c.getItems().deleteItem(c.MAGIC_SPELLS[spell][12],
							c.getItems().getItemSlot(c.MAGIC_SPELLS[spell][12]), c.MAGIC_SPELLS[spell][13]);
			}
			if (c.MAGIC_SPELLS[spell][14] > 0) {
				if (!wearingStaff(c.MAGIC_SPELLS[spell][14]))
					c.getItems().deleteItem(c.MAGIC_SPELLS[spell][14],
							c.getItems().getItemSlot(c.MAGIC_SPELLS[spell][14]), c.MAGIC_SPELLS[spell][15]);
			}
		}
		return true;
	}

	public int getFreezeTime() {
		switch (c.MAGIC_SPELLS[c.spellId][0]) {
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
		switch (c.MAGIC_SPELLS[c.spellId][0]) {
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
		switch (c.MAGIC_SPELLS[c.spellId][0]) {
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

		case 12911: // blood blitz
			return 10;

		default:
			return 31;
		}
	}

	public int getStartDelay() {
		switch (c.MAGIC_SPELLS[c.spellId][0]) {
		case 1539:
			return 60;

		default:
			return 53;
		}
	}

	public int getStaffNeeded() {
		switch (c.MAGIC_SPELLS[c.spellId][0]) {
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
		switch (c.MAGIC_SPELLS[c.spellId][0]) {
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
		switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
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
		switch (c.MAGIC_SPELLS[c.spellId][0]) {
		case 12871:
		case 12891:
			return 0;

		default:
			return 100;
		}
	}

	public void handleDfs() {
		if (System.currentTimeMillis() - c.dfsDelay > 30000) {
			if (c.playerIndex > 0 && PlayerHandler.players[c.playerIndex] != null) {
				int damage = Misc.random(15) + 5;
				c.playAnimation(Animation.create(2836));
				c.playGraphic(Graphic.create(600, 0, 0));
				PlayerHandler.players[c.playerIndex].getLevel()[3] -= damage;
				PlayerHandler.players[c.playerIndex].hitDiff2 = damage;
				PlayerHandler.players[c.playerIndex].hitUpdateRequired2 = true;
				PlayerHandler.players[c.playerIndex].updateRequired = true;
				c.dfsDelay = System.currentTimeMillis();
			} else {
				c.sendMessage("I should be in combat before using this.");
			}
		} else {
			c.sendMessage("My shield hasn't finished recharging yet.");
		}
	}

	public void handleDfsNPC() {
		if (System.currentTimeMillis() - c.dfsDelay > 30000) {
			if (c.npcIndex > 0 && NPCHandler.npcs[c.npcIndex] != null) {
				int damage = Misc.random(15) + 5;
				c.playAnimation(Animation.create(2836));
				c.playGraphic(Graphic.create(600, 0, 0));
				NPCHandler.npcs[c.npcIndex].HP -= damage;
				NPCHandler.npcs[c.npcIndex].hitDiff2 = damage;
				NPCHandler.npcs[c.npcIndex].hitUpdateRequired2 = true;
				NPCHandler.npcs[c.npcIndex].updateRequired = true;
				c.dfsDelay = System.currentTimeMillis();
			} else {
				c.sendMessage("I should be in combat before using this.");
			}
		} else {
			c.sendMessage("My shield hasn't finished recharging yet.");
		}
	}

	public void applyRecoil(int damage, int i) {
		if (damage > 0 && PlayerHandler.players[i].getEquipment()[c.playerRing] == 2550) {
			int recDamage = damage / 10 + 1;
			if (!c.getHitUpdateRequired()) {
				c.setHitDiff(recDamage);
				c.setHitUpdateRequired(true);
			} else if (!c.getHitUpdateRequired2()) {
				c.setHitDiff2(recDamage);
				c.setHitUpdateRequired2(true);
			}
			c.dealDamage(recDamage);
			c.updateRequired = true;
		}
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
		if (c.playerIndex > 0) {
			Client o = (Client) PlayerHandler.players[c.playerIndex];
			if (c.goodDistance(c.getX(), c.getY(), o.getX(), o.getY(), getRequiredDistance())) {
				if (checkReqs()) {
					if (checkSpecAmount(4153)) {
						boolean hit = Misc.random(calculateMeleeAttack()) > Misc
								.random(o.getCombat().calculateMeleeDefence());
						int damage = 0;
						if (hit)
							damage = Misc.random(calculateMeleeMaxHit());
						if (o.prayerActive[18] && System.currentTimeMillis() - o.protMeleeDelay > 1500)
							damage *= .6;
						o.handleHitMask(damage);
						c.playAnimation(Animation.create(1667));
						c.playGraphic(Graphic.create(340, 0, 100));
						o.dealDamage(damage);
					}
				}
			}
		}
	}

	public boolean staffOfDeadEffect(int i) {
		if (System.currentTimeMillis() - PlayerHandler.players[i].stofdDelay > 60000) {
			return false;
		}
		return true;
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

	public static double getDelay(Client attacker, Client victim, int delay, int speed) {
		/** The distance between the entities. */
		int distance = attacker.distanceToPoint(victim.getX(), victim.getY());

		/** The speed at which the projectile is traveling. */
		int projectileSpeed = delay + speed + distance * 5;

		/** The delay of the hit. */
		double hitDelay = projectileSpeed * .02857;

		/** Returns the hit delay. */
		return hitDelay;
	}
}
