package server.model.players.combat;

import server.Constants;
import server.model.Graphic;
import server.model.npcs.NPCHandler;
import server.model.players.Client;
import server.util.Misc;

public class Experience {

	public static void calculateDamageNPC(Client c, int i, int style) {
		c.combatStyle = style;
		c.magicFailed = false;
		for (int index = 0; i < c.getDamage().length; index++) {
			c.getDamage()[index] = 0;
		}
		NPCHandler.npcs[i].underAttack = true;
		c.killingNpcIndex = c.npcIndex;
		c.lastNpcAttacked = i;
		if (style == 0) {
			c.getDamage()[0] = Misc.random(c.getCombat().calculateMeleeMaxHit()); 
			if (NPCHandler.npcs[i].HP - c.getDamage()[0] < 0) {
				c.getDamage()[0] = NPCHandler.npcs[i].HP;
			}
			boolean fullVeracsEffect = c.getPA().fullVeracs() && Misc.random(3) == 1;
			if (!fullVeracsEffect) {
				if (Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(c.getCombat().calculateMeleeAttack())) {
					c.getDamage()[0] = 0;
				} else if (NPCHandler.npcs[i].npcType == 2882 || NPCHandler.npcs[i].npcType == 2883) {
					c.getDamage()[0] = 0;
				}
			}
			boolean guthansEffect = false;
			if (c.getPA().fullGuthans()) {
				if (Misc.random(3) == 1) {
					guthansEffect = true;
				}
			}
			if (c.getDamage()[0] > 0 && guthansEffect) {
				c.getLevel()[3] += c.getDamage()[0];
				if (c.getLevel()[3] > c.getLevelForXP(c.getExperience()[3]))
					c.getLevel()[3] = c.getLevelForXP(c.getExperience()[3]);
				c.getPA().refreshSkill(3);
				NPCHandler.npcs[i].gfx0(398);
			}
			switch (c.specEffect) {
			case 4:
				if (c.getDamage()[0] > 0) {
					if (c.getLevel()[3] + c.getDamage()[0] > c.getLevelForXP(c.getExperience()[3]))
						if (c.getLevel()[3] > c.getLevelForXP(c.getExperience()[3]))
							;
						else
							c.getLevel()[3] = c.getLevelForXP(c.getExperience()[3]);
					else
						c.getLevel()[3] += c.getDamage()[0];
					c.getPA().refreshSkill(3);
				}
				break;

			}
			addExperience(c, c.getDamage()[0], 0);
		}
		if (style == 1) {
			c.getDamage()[1] = Misc.random(c.getCombat().rangeMaxHit());
			boolean ignoreDef = false;
			if (Misc.random(5) == 1 && c.lastArrowUsed == 9243) {
				ignoreDef = true;
				NPCHandler.npcs[i].gfx0(758);
			}
			if (Misc.random(NPCHandler.npcs[i].defence) > Misc.random(10 + c.getCombat().calculateRangeAttack())
					&& !ignoreDef) {
				c.getDamage()[1] = 0;
			} else if (NPCHandler.npcs[i].npcType == 2881 || NPCHandler.npcs[i].npcType == 2883 && !ignoreDef) {
				c.getDamage()[1] = 0;
			}
			if (Misc.random(4) == 1 && c.lastArrowUsed == 9242 && c.getDamage()[1] > 0) {
				NPCHandler.npcs[i].gfx0(754);
				c.getDamage()[1] = NPCHandler.npcs[i].HP / 5;
				c.handleHitMask(c.getLevel()[3] / 10);
				c.dealDamage(c.getLevel()[3] / 10);
				c.playGraphic(Graphic.create(754, 0, 0));
			}
			if (c.getDamage()[1] > 0 && Misc.random(5) == 1 && c.lastArrowUsed == 9244) {
				c.getDamage()[1] *= 1.45;
				NPCHandler.npcs[i].gfx0(756);
			}
			if (NPCHandler.npcs[i].HP - c.getDamage()[1] < 0) {
				c.getDamage()[1] = NPCHandler.npcs[i].HP;
			}
			addExperience(c, c.getDamage()[1], 1);
		}
		if (style == 2) {
			c.getDamage()[2] = Misc.random(MagicFormula.magicMaxHit(c));
			if (c.getCombat().godSpells()) {
				if (System.currentTimeMillis() - c.godSpellDelay < Constants.GOD_SPELL_CHARGE) {
					c.getDamage()[2] += Misc.random(10);
				}
			}
			@SuppressWarnings("unused")
			boolean magicFailed = false;
			// c.npcIndex = 0;
			int bonusAttack = c.getCombat().getBonusAttack(i);
			if (Misc.random(NPCHandler.npcs[i].defence) > 10 + Misc.random(c.getCombat().mageAtk()) + bonusAttack) {
				c.getDamage()[2] = 0;
				c.magicFailed = true;
			} else if (NPCHandler.npcs[i].npcType == 2881 || NPCHandler.npcs[i].npcType == 2882) {
				c.getDamage()[2] = 0;
				c.magicFailed = true;
			}
			if (NPCHandler.npcs[i].HP - c.getDamage()[2] < 0) {
				c.getDamage()[2] = NPCHandler.npcs[i].HP;
			}
			if (!c.magicFailed) {
				int freezeDelay = c.getCombat().getFreezeTime();// freeze
				if (freezeDelay > 0 && NPCHandler.npcs[i].freezeTimer == 0) {
					NPCHandler.npcs[i].freezeTimer = freezeDelay;
				}
				switch (c.MAGIC_SPELLS[c.oldSpellId][0]) {
				case 12901:
				case 12919: // blood spells
				case 12911:
				case 12929:
					int heal = Misc.random(c.getDamage()[2] / 2);
					if (c.getLevel()[3] + heal >= c.getPA().getLevelForXP(c.getExperience()[3])) {
						c.getLevel()[3] = c.getPA().getLevelForXP(c.getExperience()[3]);
					} else {
						c.getLevel()[3] += heal;
					}
					c.getPA().refreshSkill(3);
					break;
				}

			}
			addExperience(c, c.getDamage()[2], 2);
		}
	}

	public static void calculateDamage(Client client, Client otherClient, int style) {
		if (otherClient == null) {
			return;
		}
		client.combatStyle = style;
		client.magicFailed = false;
		for (int i = 0; i < client.getDamage().length; i++) {
			client.getDamage()[i] = 0;
		}
		otherClient.logoutDelay = System.currentTimeMillis();
		otherClient.underAttackBy = client.playerId;
		otherClient.killerId = client.playerId;
		otherClient.singleCombatDelay = System.currentTimeMillis();
		if (client.killedBy != otherClient.playerId)
			client.totalPlayerDamageDealt = 0;
		client.killedBy = otherClient.playerId;
		if (style == 0) {
			boolean veracsEffect = false;
			boolean guthansEffect = false;
			if (client.getPA().fullVeracs()) {
				if (Misc.random(4) == 1) {
					veracsEffect = true;
				}
			}
			if (client.getPA().fullGuthans()) {
				if (Misc.random(4) == 1) {
					guthansEffect = true;
				}
			}
			client.getDamage()[0] = Misc.random(client.getCombat().calculateMeleeMaxHit());
			if (Misc.random(otherClient.getCombat().calculateMeleeDefence()) > Misc
					.random(client.getCombat().calculateMeleeAttack()) && !veracsEffect) {
				client.getDamage()[0] = 0;
				client.bonusAttack = 0;
			} else if (client.getEquipment()[client.playerWeapon] == 5698 && otherClient.poisonDamage <= 0
					&& Misc.random(3) == 1) {
				otherClient.getPA().appendPoison(13);
				client.bonusAttack += client.getDamage()[0] / 3;
			} else {
				client.bonusAttack += client.getDamage()[0] / 3;
			}
			if (otherClient.prayerActive[18] && System.currentTimeMillis() - otherClient.protMeleeDelay > 1500
					&& !veracsEffect) { // if prayer active reduce damage by 40%
				client.getDamage()[0] = (int) client.getDamage()[0] * 60 / 100;
			}
			if (client.getCombat().staffOfDeadEffect(client.oldPlayerIndex) && client.getDamage()[0] > 0) {
				client.getDamage()[0] = (int) (client.getDamage()[0] * .50);
			}
			if (client.getDamage()[0] > 0 && guthansEffect) {
				client.getLevel()[3] += client.getDamage()[0];
				if (client.getLevel()[3] > client.getLevelForXP(client.getExperience()[3]))
					client.getLevel()[3] = client.getLevelForXP(client.getExperience()[3]);
				client.getPA().refreshSkill(3);
				otherClient.playGraphic(Graphic.create(398, 0, 0));
			}
			if (client.getDamage()[0] > otherClient.getLevel()[3]) {
				client.getDamage()[0] = otherClient.getLevel()[3];
			}
			switch (client.specEffect) {
			case 1: // dragon scimmy special
				if (client.getDamage()[0] > 0) {
					if (otherClient.prayerActive[16] || otherClient.prayerActive[17] || otherClient.prayerActive[18]) {
						otherClient.headIcon = -1;
						otherClient.getPA().sendFrame36(client.PRAYER_GLOW[16], 0);
						otherClient.getPA().sendFrame36(client.PRAYER_GLOW[17], 0);
						otherClient.getPA().sendFrame36(client.PRAYER_GLOW[18], 0);
					}
					otherClient.sendMessage("You have been injured!");
					otherClient.stopPrayerDelay = System.currentTimeMillis();
					otherClient.prayerActive[16] = false;
					otherClient.prayerActive[17] = false;
					otherClient.prayerActive[18] = false;
					otherClient.getPA().requestUpdates();
				}
				break;
			case 2:
				if (client.getDamage()[0] > 0) {
					if (otherClient.freezeTimer <= 0)
						otherClient.freezeTimer = 30;
					otherClient.playGraphic(Graphic.create(369, 0, 0));
					otherClient.sendMessage("You have been frozen.");
					otherClient.frozenBy = client.playerId;
					otherClient.stopMovement();
					client.sendMessage("You freeze your enemy.");
				}
				break;
			case 3:
				if (client.getDamage()[0] > 0) {
					otherClient.getLevel()[1] -= client.getDamage()[0];
					otherClient.sendMessage("You feel weak.");
					if (otherClient.getLevel()[1] < 1)
						otherClient.getLevel()[1] = 1;
					otherClient.getPA().refreshSkill(1);
				}
				break;
			case 4:
				if (client.getDamage()[0] > 0) {
					if (client.getLevel()[3] + client.getDamage()[0] > client.getLevelForXP(client.getExperience()[3]))
						if (client.getLevel()[3] > client.getLevelForXP(client.getExperience()[3]))
							;
						else
							client.getLevel()[3] = client.getLevelForXP(client.getExperience()[3]);
					else
						client.getLevel()[3] += client.getDamage()[0];
					client.getPA().refreshSkill(3);
				}
				break;
			}
			client.specEffect = 0;
			if (client.getDamage()[0] < 0)
				client.getDamage()[0] = 0;
			addExperience(client, client.getDamage()[0], 0);
		} else if (style == 1) {
			client.getDamage()[1] = Misc.random(client.getCombat().rangeMaxHit());
			if (Misc.random(10 + otherClient.getCombat().calculateRangeDefence()) > Misc
					.random(10 + client.getCombat().calculateRangeAttack())) {
				client.getDamage()[1] = 0;
			}
			if (client.armadylSpecial) {
				client.getDamage()[1] = client.getCombat().rangeMaxHit();
				client.armadylSpecial = false;
			}
			if (client.getDamage()[1] > 0 && Misc.random(5) == 1 && client.lastArrowUsed == 9244) {
				client.getDamage()[1] *= 1.45;
				otherClient.playGraphic(Graphic.create(756, 0, 0));
			}
			if (otherClient.prayerActive[17] && System.currentTimeMillis() - otherClient.protRangeDelay > 1500) {
				client.getDamage()[1] = (int) client.getDamage()[1] * 60 / 100;
			}
			if (client.dbowSpec) {
				if (client.getDamage()[1] < 8)
					client.getDamage()[1] = 8;
				if (client.getDamage()[1] < 8)
					client.getDamage()[1] = 8;
			}
			if (client.getDamage()[1] > otherClient.getLevel()[3]) {
				client.getDamage()[1] = otherClient.getLevel()[3];
			}
			if (client.getDamage()[1] < 0)
				client.getDamage()[1] = 0;
			addExperience(client, client.getDamage()[1], 1);
		}
		if (style == 2) {
			client.getDamage()[2] = Misc.random(MagicFormula.magicMaxHit(client));
			if (client.getCombat().godSpells()) {
				if (System.currentTimeMillis() - client.godSpellDelay < Constants.GOD_SPELL_CHARGE) {
					client.getDamage()[2] += 10;
				}
			}
			if (Misc.random(otherClient.getCombat().mageDef()) > Misc.random(client.getCombat().mageAtk())) {
				client.getDamage()[2] = 0;
				client.magicFailed = true;
			}
			int freezeDelay = client.getCombat().getFreezeTime();// freeze time
			if (freezeDelay > 0 && otherClient.freezeTimer <= -3 && !client.magicFailed) {
				otherClient.freezeTimer = freezeDelay;
				otherClient.getPA().sendFrame126("freezetimer:" + (freezeDelay + 2), 1810);
				otherClient.resetWalkingQueue();
				otherClient.sendMessage("You have been frozen.");
				otherClient.frozenBy = client.playerId;
			}
			if (otherClient.prayerActive[16] && System.currentTimeMillis() - otherClient.protMageDelay > 1500) {
				client.getDamage()[2] = (int) client.getDamage()[2] * 60 / 100;
			}
			if (client.getDamage()[2] > otherClient.getLevel()[3]) {
				client.getDamage()[2] = otherClient.getLevel()[3];
			}
			if (client.getDamage()[2] < 0)
				client.getDamage()[2] = 0;
			addExperience(client, client.getDamage()[2], 2);
			if (!client.magicFailed) {
				if (System.currentTimeMillis() - otherClient.reduceStat > 35000) {
					otherClient.reduceStat = System.currentTimeMillis();
					switch (client.MAGIC_SPELLS[client.spellId][0]) {
					case 12987:
					case 13011:
					case 12999:
					case 13023:
						otherClient.getLevel()[0] -= ((otherClient.getPA().getLevelForXP(otherClient.getExperience()[0])
								* 10) / 100);
						break;
					}
				}

				switch (client.MAGIC_SPELLS[client.spellId][0]) {
				case 12445: // teleblock
					if (System.currentTimeMillis() - otherClient.teleBlockDelay > otherClient.teleBlockLength) {
						otherClient.teleBlockDelay = System.currentTimeMillis();
						otherClient.sendMessage("You have been teleblocked.");
						if (otherClient.prayerActive[16]
								&& System.currentTimeMillis() - otherClient.protMageDelay > 1500)
							otherClient.teleBlockLength = 150000;
						else
							otherClient.teleBlockLength = 300000;
					}
					break;

				case 12901:
				case 12919: // blood spells
				case 12911:
				case 12929:
					int heal = (int) (client.getDamage()[2] / 4);
					if (client.getLevel()[3] + heal > client.getPA().getLevelForXP(client.getExperience()[3])) {
						client.getLevel()[3] = client.getPA().getLevelForXP(client.getExperience()[3]);
					} else {
						client.getLevel()[3] += heal;
					}
					client.getPA().refreshSkill(3);
					break;

				case 1153:
					otherClient
							.getLevel()[0] -= ((otherClient.getPA().getLevelForXP(otherClient.getExperience()[0]) * 5)
									/ 100);
					otherClient.sendMessage("Your attack level has been reduced!");
					otherClient.reduceSpellDelay[client.reduceSpellId] = System.currentTimeMillis();
					otherClient.getPA().refreshSkill(0);
					break;

				case 1157:
					otherClient
							.getLevel()[2] -= ((otherClient.getPA().getLevelForXP(otherClient.getExperience()[2]) * 5)
									/ 100);
					otherClient.sendMessage("Your strength level has been reduced!");
					otherClient.reduceSpellDelay[client.reduceSpellId] = System.currentTimeMillis();
					otherClient.getPA().refreshSkill(2);
					break;

				case 1161:
					otherClient
							.getLevel()[1] -= ((otherClient.getPA().getLevelForXP(otherClient.getExperience()[1]) * 5)
									/ 100);
					otherClient.sendMessage("Your defence level has been reduced!");
					otherClient.reduceSpellDelay[client.reduceSpellId] = System.currentTimeMillis();
					otherClient.getPA().refreshSkill(1);
					break;

				case 1542:
					otherClient
							.getLevel()[1] -= ((otherClient.getPA().getLevelForXP(otherClient.getExperience()[1]) * 10)
									/ 100);
					otherClient.sendMessage("Your defence level has been reduced!");
					otherClient.reduceSpellDelay[client.reduceSpellId] = System.currentTimeMillis();
					otherClient.getPA().refreshSkill(1);
					break;

				case 1543:
					otherClient
							.getLevel()[2] -= ((otherClient.getPA().getLevelForXP(otherClient.getExperience()[2]) * 10)
									/ 100);
					otherClient.sendMessage("Your strength level has been reduced!");
					otherClient.reduceSpellDelay[client.reduceSpellId] = System.currentTimeMillis();
					otherClient.getPA().refreshSkill(2);
					break;

				case 1562:
					otherClient
							.getLevel()[0] -= ((otherClient.getPA().getLevelForXP(otherClient.getExperience()[0]) * 10)
									/ 100);
					otherClient.sendMessage("Your attack level has been reduced!");
					otherClient.reduceSpellDelay[client.reduceSpellId] = System.currentTimeMillis();
					otherClient.getPA().refreshSkill(0);
					break;
				}
			}
		}
	}

	public static void addExperience(Client client, int damage, int style) {
		if (style == 0) {
			if (client.fightMode == 3) {
				client.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE / 3), 0);
				client.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE / 3), 1);
				client.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE / 3), 2);
				client.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE / 3), 3);
				client.getPA().refreshSkill(0);
				client.getPA().refreshSkill(1);
				client.getPA().refreshSkill(2);
				client.getPA().refreshSkill(3);
			} else {
				client.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE), client.fightMode);
				client.getPA().addSkillXP((damage * Constants.MELEE_EXP_RATE / 3), 3);
				client.getPA().refreshSkill(client.fightMode);
				client.getPA().refreshSkill(3);
			}
		}
		if (style == 1) {
			if (client.fightMode == 3) {
				client.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 4);
				client.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 1);
				client.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 3);
				client.getPA().refreshSkill(1);
				client.getPA().refreshSkill(3);
				client.getPA().refreshSkill(4);
			} else {
				client.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE), 4);
				client.getPA().addSkillXP((damage * Constants.RANGE_EXP_RATE / 3), 3);
				client.getPA().refreshSkill(3);
				client.getPA().refreshSkill(4);
			}
		}
		if (style == 2) {
			client.getPA().addSkillXP((client.MAGIC_SPELLS[client.spellId][7] + damage * Constants.MAGIC_EXP_RATE), 6);
			client.getPA().addSkillXP((client.MAGIC_SPELLS[client.spellId][7] + damage * Constants.MAGIC_EXP_RATE / 3), 3);
			client.getPA().refreshSkill(3);
			client.getPA().refreshSkill(6);
		}
	}

}
