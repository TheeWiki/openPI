package server.model.npcs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import server.Constants;
import server.Server;
import server.model.content.KillLog;
import server.model.items.GameItem;
import server.model.items.Item;
import server.model.minigames.tzhaar.FightCaves;
import server.model.npcs.drops.Drop;
import server.model.npcs.drops.NPCDrops;
import server.model.players.Player;
import server.model.players.EquipmentListener;
import server.model.players.PlayerHandler;
import server.model.players.skills.SkillIndex;
import server.util.Misc;
import server.world.sound.Sounds;

public class NPCHandler {
	public static int maxNPCs = 10000;
	public static int maxListedNPCs = 10000;
	public static int maxNPCDrops = 10000;
	public static NPC npcs[] = new NPC[maxNPCs];
	public static NPCList NpcList[] = new NPCList[maxListedNPCs];

	public NPCHandler() {
		for (int i = 0; i < maxNPCs; i++) {
			npcs[i] = null;
		}
		for (int i = 0; i < maxListedNPCs; i++) {
			NpcList[i] = null;
		}
		loadNPCList("./Data/CFG/npc.cfg");
		loadAutoSpawn("./Data/CFG/spawn-config.cfg");
		// System.out.println("NPC Spawns Loaded");
	}

	@SuppressWarnings("static-access")
	public void multiAttackGfx(int i, int gfx) {
		if (npcs[i].projectileId < 0)
			return;
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Player player = (Player) Server.playerHandler.players[j];
				if (player.heightLevel != npcs[i].heightLevel)
					continue;
				if (Server.playerHandler.players[j].goodDistance(player.absX, player.absY, npcs[i].absX, npcs[i].absY, 15)) {
					int nX = Server.npcHandler.npcs[i].getX() + offset(i);
					int nY = Server.npcHandler.npcs[i].getY() + offset(i);
					int pX = player.getX();
					int pY = player.getY();
					int offX = (nY - pY) * -1;
					int offY = (nX - pX) * -1;
					player.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i),
							npcs[i].projectileId, 43, 31, -player.getId() - 1, 65);
				}
			}
		}
	}

	public boolean switchesAttackers(int i) {
		switch (npcs[i].npcType) {
		case 2551:
		case 2552:
		case 2553:
		case 2559:
		case 2560:
		case 2561:
		case 2563:
		case 2564:
		case 2565:
		case 2892:
		case 2894:
			return true;

		}

		return false;
	}

	@SuppressWarnings("static-access")
	public void multiAttackDamage(int i) {
		int max = getMaxHit(i);
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Player player = (Player) Server.playerHandler.players[j];
				if (player.isDead || player.heightLevel != npcs[i].heightLevel)
					continue;
				if (Server.playerHandler.players[j].goodDistance(player.absX, player.absY, npcs[i].absX, npcs[i].absY, 15)) {
					if (npcs[i].attackType == 2) {
						if (!player.prayerActive[16]) {
							if (Misc.random(500) + 200 > Misc.random(player.getCombat().mageDef())) {
								int dam = Misc.random(max);
								player.dealDamage(dam);
								player.handleHitMask(dam);
							} else {
								player.dealDamage(0);
								player.handleHitMask(0);
							}
						} else {
							player.dealDamage(0);
							player.handleHitMask(0);
						}
					} else if (npcs[i].attackType == 1) {
						if (!player.prayerActive[17]) {
							int dam = Misc.random(max);
							if (Misc.random(500) + 200 > Misc.random(player.getCombat().calculateRangeDefence())) {
								player.dealDamage(dam);
								player.handleHitMask(dam);
							} else {
								player.dealDamage(0);
								player.handleHitMask(0);
							}
						} else {
							player.dealDamage(0);
							player.handleHitMask(0);
						}
					}
					if (npcs[i].endGfx > 0) {
						player.gfx0(npcs[i].endGfx);
					}
				}
				player.getPA().refreshSkill(3);
			}
		}
	}

	@SuppressWarnings("static-access")
	public int getClosePlayer(int i) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				if (j == npcs[i].spawnedBy)
					return j;
				if (goodDistance(Server.playerHandler.players[j].absX, Server.playerHandler.players[j].absY,
						npcs[i].absX, npcs[i].absY, 2 + distanceRequired(i) + followDistance(i)) || isFightCaveNpc(i)) {
					if ((Server.playerHandler.players[j].underAttackBy <= 0
							&& Server.playerHandler.players[j].underAttackBy2 <= 0)
							|| Server.playerHandler.players[j].inMulti())
						if (Server.playerHandler.players[j].heightLevel == npcs[i].heightLevel)
							return j;
				}
			}
		}
		return 0;
	}

	@SuppressWarnings("static-access")
	public int getCloseRandomPlayer(int i) {
		ArrayList<Integer> players = new ArrayList<Integer>();
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null
					&& (System.currentTimeMillis() - Server.playerHandler.players[j].toleranceTimer < 1500000)) {
				if (goodDistance(Server.playerHandler.players[j].absX, Server.playerHandler.players[j].absY,
						npcs[i].absX, npcs[i].absY, 2 + distanceRequired(i) + followDistance(i)) || isFightCaveNpc(i)) {
					if ((Server.playerHandler.players[j].underAttackBy <= 0
							&& Server.playerHandler.players[j].underAttackBy2 <= 0)
							|| Server.playerHandler.players[j].inMulti())
						if (Server.playerHandler.players[j].heightLevel == npcs[i].heightLevel)
							players.add(j);
				}
			}
		}
		if (players.size() > 0)
			return players.get(Misc.random(players.size() - 1));
		else
			return 0;
	}

	public int npcSize(int i) {
		switch (npcs[i].npcType) {
		case 2883:
		case 2882:
		case 2881:
			return 3;
		}
		return 0;
	}

	public boolean isAggressive(int i) {
		switch (npcs[i].npcType) {
		case 2550:
		case 2551:
		case 2552:
		case 2553:
		case 2558:
		case 2559:
		case 2560:
		case 2561:
		case 2562:
		case 2563:
		case 2564:
		case 2565:
		case 2892:
		case 2894:
		case 2881:
		case 2882:
		case 2883:
			return true;
		}
		if (npcs[i].inWild() && npcs[i].MaxHP > 0)
			return true;
		if (isFightCaveNpc(i))
			return true;
		return false;
	}

	public boolean isFightCaveNpc(int i) {
		switch (npcs[i].npcType) {
		case 2627:
		case 2630:
		case 2631:
		case 2741:
		case 2743:
		case 2745:
			return true;
		}
		return false;
	}

	/**
	 * Summon npc, barrows, etc
	 * 
	 * @param player
	 *            player
	 * @param npcType
	 *            npc ID
	 * @param x
	 *            location x
	 * @param y
	 *            location y
	 * @param heightLevel
	 *            height level spawning
	 * @param WalkingType
	 *            is it walking
	 * @param HP
	 *            hp values
	 * @param maxHit
	 *            max hit of npc
	 * @param attack
	 *            attack bonus
	 * @param defence
	 *            defence bonus
	 * @param attackPlayer
	 *            aggressive?
	 * @param headIcon
	 *            has head icon?
	 */
	public void spawnNpc(Player player, int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit,
			int attack, int defence, boolean attackPlayer, boolean headIcon) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		newNPC.spawnedBy = player.getId();
		if (headIcon)
			player.getPA().drawHeadicon(1, slot, 0, 0);
		if (attackPlayer) {
			newNPC.underAttack = true;
			if (player != null) {
				if (server.model.minigames.barrows.Barrows.COFFIN_AND_BROTHERS[player.randomCoffin][1] != newNPC.npcType) {
					if (newNPC.npcType == 2025 || newNPC.npcType == 2026 || newNPC.npcType == 2027
							|| newNPC.npcType == 2028 || newNPC.npcType == 2029 || newNPC.npcType == 2030) {
						newNPC.forceChat("You dare disturb my rest!");
					}
				}
				if (server.model.minigames.barrows.Barrows.COFFIN_AND_BROTHERS[player.randomCoffin][1] == newNPC.npcType) {
					newNPC.forceChat("You dare steal from us!");
				}

				newNPC.killerId = player.playerId;
			}
		}
		npcs[slot] = newNPC;
	}

	/**
	 * 
	 * @param npcType
	 * @param x
	 * @param y
	 * @param heightLevel
	 * @param WalkingType
	 * @param HP
	 * @param maxHit
	 * @param attack
	 * @param defence
	 */
	public void spawnNpc2(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack,
			int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}

		if (slot == -1) {
			// Misc.println("No Free Slot");
			return; // no free slot found
		}
		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
	}

	/**
	 * Emotes
	 **/

	/**
	 * 
	 * @param i
	 * @return
	 */
	@SuppressWarnings("static-access")
	public static int getAttackEmote(int i) {
		switch (Server.npcHandler.npcs[i].npcType) {
		case 13457: // ancient range
		case 13459: // ancient mage
		case 13456: // ancient melee
			if (npcs[i].attackType == 1) {
				return 426;
			} else if (npcs[i].attackType == 2) {
				return 1979;
			} else {
				return 7041;
			}

		case 8281:// Ballance Elemental
			return 10680;

		case 8282:// Ballance Elemental
			return 10669;

		case 8283:// Ballance Elemental
			return 10681;
		case 5902:
		case 5903:
		case 5904:
		case 5905:
			return 0;
		// case 13447:
		// if (npcs[i].nexStage == 1 || npcs[i].nexStage == 2) {
		// switch (npcs[i].glod) {
		// case 1:
		// return 6987;
		// case 2:
		// return 6986;
		// }
		// } else if (npcs[i].nexStage == 3 || npcs[i].nexStage == 4) {
		// switch (npcs[i].glod) {
		// case 1:
		// return 6987;
		// case 2:
		// return 6355;
		// case 3:
		// return 6984;
		// }
		// } else if (npcs[i].nexStage == 5 || npcs[i].nexStage == 6) {
		// switch (npcs[i].glod) {
		// case 1:
		// case 2:
		// return 6987;
		// case 3:
		// return 6948;
		// }
		// } else if (npcs[i].nexStage == 7 || npcs[i].nexStage == 8) {
		// switch (npcs[i].glod) {
		// case 1:
		// case 2:
		// case 3:
		// return 6987;
		// }
		// } else if (npcs[i].nexStage == 9) {
		// switch (npcs[i].glod) {
		// case 1:
		// return 6987;
		// }
		// }
		// return 6354;
		case 8776:
		case 8777:
			return 12177;
		case 8596:// Avatar Of Destruction
			return 11197;
		case 8597:// Avatar Of Creation
			return 11202;
		case 8528:
			return 12696;
		case 8133:// corp beast
			if (npcs[i].attackType == 2)
				return 10053;
			else if (npcs[i].attackType == 1)
				return 10059;
			else if (npcs[i].attackType == 0)
				return 10057;
		case 8349:// tormented demon
			if (npcs[i].attackType == 2)
				return 10917;
			else if (npcs[i].attackType == 1)
				return 10918;
			else if (npcs[i].attackType == 0)
				return 10922;
		case 112:
			return 4652;
		case 1640: // jelly attack
			return 8575;
		case 1613:
			return 9487; // nechryael attack

		case 63: // deadly red spider attack
		case 134: // poison spider attack
			return 143;

		case 111:
			return 4652; // Ice Giant attack
		case 110:

			return 4666;

		case 13480:// Revenant Knight
			return 7441;

		case 13473:// Revenant Vampire
			return 7441;

		case 13471:// Revenant Icefiend
		case 13470:// Revenant Pyrefiend
			return 7481;

		case 13745:// Revenant Cyclops
			return 7453;

		case 13476:// Revenant Hellhound
			return 7460;

		case 13477:// Revenant Demon
			return 7474;

		case 13479:// Revenant Dark Beast
			return 7476;

		case 13481:// Revenant Dragon
			return 8589;

		case 13478:// Revenant Ork
			return 7411;

		case 1382:
			return 9955;
		case 6213:
		case 6212:
			return 6547;
		case 6219:
		case 6254:
		case 6255:
		case 6256:
		case 6257:
		case 6258:
		case 6214:
			return 806;
		case 6216:
			return 1582;
		case 6218:
			return 4300;
		case 6221:
			return 811;
		case 6229:
		case 6230:
		case 6231:
		case 6232:
		case 6233:
		case 6234:
		case 6235:
		case 6236:
		case 6237:
		case 6238:
		case 6239:
		case 6240:
		case 6241:
		case 6242:
		case 6243:
		case 6244:
		case 6245:
		case 6246:
			return 6953;
		case 6210:
			return 6581;
		case 6211:
			return 169;
		case 6268:
			return 2935;
		case 6269:
		case 6270:
			return 4652;
		case 6271:
		case 6272:
		case 6274:
			return 4320;
		case 6279:
		case 6280:
		case 6283:
			return 6184;
		case 6276:
		case 6277:
			return 4320;
		case 6275:
			return 164;
		case 6282:
			return 6188;
		case 6260:
			return 7060;
		/** BORK **/
		case 7133:
			if (npcs[i].attackType == 0)
				return 8754; // Melee
			else if (npcs[i].attackType == 1)
				return 8757; // Magic
			/** END BORK **/
		case 2550:
			if (npcs[i].attackType == 0)
				return 7060;
			else
				return 7063;
		case 7159:
		case 7160:
			return 8799;
		case 3819:
			return 3957;
		case 2892:
		case 2894:
			return 2868;
		case 90:
		case 94:
			return 5485;
		case 5996: // glod
			return 6501;
		case 5529:
			return 5782;
		case 2627:
			return 2621;
		case 2630:
			return 2625;
		case 2631:
			return 2633;
		case 2741:
			return 2637;
		case 2746:
			return 2637;
		case 2607:
			return 2611;
		case 2743:// 360
			return 2647;
		// bandos gwd
		case 2551:
		case 2552:
		case 2553:
			return 6154;
		// end of gwd
		// arma gwd
		case 2558:
			return 3505;
		case 2560:
			return 6953;
		case 2559:
			return 6952;
		case 2561:
			return 6954;
		// end of arma gwd
		// sara gwd
		case 2562:
			return 6964;
		case 2563:
			return 6376;
		case 2564:
			return 7018;
		case 2565:
			return 7009;
		// end of sara gwd
		case 13: // wizards
			return 711;

		case 103:
		case 655:
			return 123;

		case 1624:
			return 1557;

		case 1648:
			return 1590;

		case 2783: // dark beast
			return 2731;

		case 1615: // abby demon
			return 1537;

		case 1610:
		case 1611: // garg
			return 1519;

		case 1616: // basilisk
			return 1546;

		case 50:// drags
		case 53:
		case 54:
		case 55:
		case 941:
		case 1590:
		case 1591:
		case 1592:
		case 124: // earth warrior
			return 390;

		case 803: // monk
			return 422;

		case 52: // baby drag
			return 25;

		case 58: // Shadow Spider
		case 59: // Giant Spider
		case 60: // Giant Spider
		case 61: // Spider
		case 62: // Jungle Spider
		case 64: // Ice Spider
		case 10500:
			return 5327;

		case 105: // Bear
		case 106: // Bear
			return 41;

		case 412:
		case 78:
			return 30;

		case 2033: // rat
			return 138;

		case 2031: // bloodworm
			return 2070;

		case 101: // goblin
			return 309;

		case 81: // cow
			return 0x03B;

		case 21: // hero
			return 451;

		case 41: // chicken
			return 55;

		case 9: // guard
		case 32: // guard
		case 20: // paladin
			return 451;

		case 1338:
		case 1340:
		case 1341:
		case 1342:
		case 2455:
			return 1341;

		case 19: // white knight
			return 406;

		case 2452:
			return 1312;

		case 2889:
			return 2859;

		case 118:
		case 119:
			return 99;

		case 82:// Lesser Demon
		case 83:// Greater Demon
		case 84:// Black Demon
		case 1472:// jungle demon
			return 64;

		case 1267:
		case 1265:
			return 1312;
		case 1883:
			return 13049;

		case 125: // ice warrior
		case 178:
			return 451;

		case 1153: // Kalphite Worker
		case 1154: // Kalphite Soldier
		case 1155: // Kalphite guardian
		case 1156: // Kalphite worker
		case 1157: // Kalphite guardian
			return 1184;

		case 123:
		case 122:
			return 164;

		case 2028: // karil
			return 2075;

		case 2025: // ahrim
			return 729;

		case 2026: // dharok
			return 2067;

		case 2027: // guthan
			return 2080;

		case 2029: // torag
			return 0x814;

		case 2030: // verac
			return 2062;

		case 2881: // supreme
			return 2855;

		case 2882: // prime
			return 2854;

		case 2883: // rex
			return 2851;

		case 3200:
			return 3146;

		case 2745:
			if (npcs[i].attackType == 2)
				return 2656;
			else if (npcs[i].attackType == 1)
				return 2652;
			else if (npcs[i].attackType == 0)
				return 2655;
		case 11000:
			return 5540;
		case 10650:
			return 5499;
		case 10775:
			return 13151;
		case 10575:
		case 10600:
			return 711;
		case 10800:
			return 13061;
		case 10475:
			return 5499;
		case 10675:
			return 5485;
		case 10725:
			return 64;
		case 10325:
			return 426;
		case 10825:
			return 5532;
		case 9775:
			return 13706;
		case 9911:
			return 14380;
		case 9964:
			return 13717;
		case 10156:
			return 13016;
		case 10039:
			return 14375;
		case 10057:
			return 10816;
		case 10105:
			return 13001;
		case 8833:
			return 12204;
		case 8834:
			return 12205;
		case 8832:
			return 12196;
		default:
			return 390;
		}
	}

	/**
	 * 
	 * @param i
	 * @return
	 */
	public int getDeadEmote(int i) {
		switch (npcs[i].npcType) {
		case 8281:// Ballance Elemental
		case 8282:// Ballance Elemental
		case 8283:// Ballance Elemental
			return 10679;
		case 5902:
		case 5903:
		case 5904:
		case 5905:
			return 0;
		case 8833:
			return 12206;
		case 8776:
		case 8777:
			return 12181;
		case 13745:// Revenant Cyclops
			return 7454;

		case 13481:// Revenant Dragon
			return 8593;

		case 13479:// Revenant Dark Beast
			return 7468;

		case 13477:// Revenant Demon
			return 7475;

		case 13476:// Revenant Hellhound
			return 7461;

		case 13471:// Revenant Pyrefiend
		case 13470:// Revenant Icefiend
			return 7484;

		case 13473:// Revenant Vampire
			return 7428;

		case 13480:// Revenant Knight
			return 7442;

		case 13478:// Revenant Ork
			return 7416;

		case 13447:
			return 6951;
		case 8528: // nomad
			return 12694;
		case 8133: // corp beast
			return 10059;
		case 10141:
			return 13602;
		case 9900:
			return 13605;
		case 7133:// Bork
			return 8756;
		case 3622:
			return 5572;
		case 10110:
			return 13002;
		case 10542:
			return 2304;
		case 10127:
			return 13171;
		case 8349: // torm demon
			return 10924;
		case 1613:
			return 1530; // nechryael death

		case 111:
			return 4653; // Ice Giant death

		case 63: // deadly red spider death
		case 134: // poison spider
			return 146;
		case 82:
			return 67;
		case 112:
			return 4653;
		case 110:
			return 4668;
		case 1382:
			return 9961;
		case 6203:
			return 6946;
		case 6204:
		case 6206:
		case 6208:
			return 67;
		case 6282:
			return 6182;
		case 6275:
			return 167;
		case 6276:
		case 6277:
			return 4321;
		case 6279:
		case 6280:
		case 6283:
			return 6182;
		case 6211:
			return 172;
		case 6212:
			return 6537;
		case 6219:
		case 6221:
		case 6254:
		case 6255:
		case 6256:
		case 6257:
		case 6258:
		case 6214:
			return 0x900;
		case 6216:
			return 1580;
		case 6218:
			return 4302;
		case 6268:
			return 2938;
		case 6269:
		case 6270:
			return 4653;
		case 6229:
		case 6230:
		case 6231:
		case 6232:
		case 6233:
		case 6234:
		case 6235:
		case 6236:
		case 6237:
		case 6238:
		case 6239:
		case 6240:
		case 6241:
		case 6242:
		case 6243:
		case 6244:
		case 6245:
		case 6246:
			return 6956;
		case 6210:
			return 6576;
		case 6271:
		case 6272:
		case 6274:
			return 4321;
		case 6260:
			return 7062;
		case 7159:
		case 7160:
			return 8790;
		case 3819:
			return 3962;
		case 90:
		case 94:
			return 5491;
		case 5996:
			return 6502;
		// sara gwd
		case 2562:
			return 6965;
		case 5529:
			return 5784;
		case 1265:
			return 1314;
		case 2563:
			return 6377;
		case 2564:
			return 7016;
		case 2565:
			return 7011;
		// bandos gwd
		case 2551:
		case 2552:
		case 2553:
			return 6156;
		case 2550:
			return 7062;
		case 2892:
		case 2894:
			return 2865;
		case 1612: // banshee
			return 1524;
		case 2558:
			return 3503;
		case 2559:
		case 2560:
		case 2561:
			return 6956;
		case 2607:
			return 2607;
		case 2627:
			return 2620;
		case 2630:
			return 2627;
		case 2631:
			return 2630;
		case 2738:
			return 2627;
		case 2741:
			return 2638;
		case 2746:
			return 2638;
		case 2743:
			return 2646;
		case 2745:
			return 2654;

		case 3777:
		case 3778:
		case 3779:
		case 3780:
			return -1;

		case 3200:
			return 3147;

		case 2035: // spider
			return 146;

		case 2033: // rat
			return 141;

		case 2031: // bloodvel
			return 2073;

		case 101: // goblin
			return 313;

		case 81: // cow
			return 0x03E;

		case 41: // chicken
			return 57;

		case 1338:
		case 1340:
		case 1341:
		case 1342:
		case 2455:
			return 1342;

		case 2881:
		case 2882:
		case 2883:
			return 2856;

		case 125: // ice warrior
			return 843;

		case 751:// Zombies!!
			return 302;

		case 1626:
		case 1627:
		case 1628:
		case 1629:
		case 1630:
		case 1631:
		case 1632: // turoth!
			return 1597;

		case 1616: // basilisk
			return 1548;

		case 1653: // hand
			return 1590;

		case 1605:// abby spec
			return 1508;

		case 51:// baby drags
		case 52:
		case 1589:
		case 3376:
			return 28;

		case 1610:
		case 1611:
			return 1518;

		case 1618:
		case 7642:
		case 1619:
			return 1553;

		case 1620:
		case 1621:
			return 1563;

		case 2783:
			return 2732;

		case 1615:
			return 1538;

		case 1624:
			return 1558;

		case 1633:
		case 1634:
		case 1635:
		case 1636:
			return 1580;

		case 1648:
		case 1649:
		case 1650:
		case 1651:
		case 1652:
		case 1654:
		case 1655:
		case 1656:
		case 1657:
			return 1590;

		case 100:
		case 102:
			return 313;

		case 105:
		case 106:
			return 44;

		case 412:
		case 78:
			return 36;

		case 122:
		case 123:
			return 167;

		case 58:
		case 59:
		case 60:
		case 61:
		case 62:
		case 64:
			return 146;

		case 1153:
		case 1154:
		case 1155:
		case 1156:
		case 1157:
			return 1190;

		case 103:
		case 104:
			return 123;

		case 118:
		case 119:
			return 102;

		case 53:
		case 54:
		case 55:
		case 941:
		case 1590:
		case 1591:
		case 1592:
		case 5363:// Dragons
			return 28;
		case 10475:
			return 5491;
		case 10500:
			return 5329;
		case 11000:
			return 5541;
		case 10675:
			return 5491;
		case 10725:
			return 67;
		case 10825:
			return 5537;
		case 10775:
			return 13153;
		case 9775:
			return 13702;
		case 9911:
			return 14378;
		case 9964:
			return 13715;
		case 10156:
			return 13017;
		case 10039:
			return 14378;
		case 10057:
			return 10815;
		case 10105:
			return 13005;
		case 8834:
			return 12206;
		case 8832:
			return 12199;
		default:
			return 2304;
		}
	}

	/**
	 * Attack delays
	 * 
	 * @param i
	 * @return
	 */
	public int getNpcDelay(int i) {
		switch (npcs[i].npcType) {
		case 2025:
		case 2028:
			return 7;

		case 2745:
			return 8;

		case 2558:
		case 2559:
		case 2560:
		case 2561:
		case 2550:
			return 6;
		// saradomin gw boss
		case 2562:
			return 2;

		default:
			return 5;
		}
	}

	/**
	 * Hit delays
	 * 
	 * @param i
	 * @return
	 */
	public int getHitDelay(int i) {
		switch (npcs[i].npcType) {
		case 2881:
		case 2882:
		case 3200:
		case 2892:
		case 2894:
			return 3;

		case 2743:
		case 2631:
		case 2558:
		case 2559:
		case 2560:
			return 3;

		case 2745:
			if (npcs[i].attackType == 1 || npcs[i].attackType == 2)
				return 5;
			else
				return 2;

		case 2025:
			return 4;
		case 2028:
			return 3;

		default:
			return 2;
		}
	}

	/**
	 * Npc respawn time
	 **/
	public int getRespawnTime(int i) {
		switch (npcs[i].npcType) {
		case 2881:
		case 2882:
		case 2883:
		case 2558:
		case 2559:
		case 2560:
		case 2561:
		case 2562:
		case 2563:
		case 2564:
		case 2550:
		case 2551:
		case 2552:
		case 2553:
			return 100;
		case 3777:
		case 3778:
		case 3779:
		case 3780:
			return 500;
		default:
			return 100;
		}
	}

	public void newNPC(int npcType, int x, int y, int heightLevel, int WalkingType, int HP, int maxHit, int attack,
			int defence) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 1; i < maxNPCs; i++) {
			if (npcs[i] == null) {
				slot = i;
				break;
			}
		}

		if (slot == -1)
			return; // no free slot found

		NPC newNPC = new NPC(slot, npcType);
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = WalkingType;
		newNPC.HP = HP;
		newNPC.MaxHP = HP;
		newNPC.maxHit = maxHit;
		newNPC.attack = attack;
		newNPC.defence = defence;
		npcs[slot] = newNPC;
	}

	public void newNPCList(int npcType, String npcName, int combat, int HP) {
		// first, search for a free slot
		int slot = -1;
		for (int i = 0; i < maxListedNPCs; i++) {
			if (NpcList[i] == null) {
				slot = i;
				break;
			}
		}

		if (slot == -1)
			return; // no free slot found

		NPCList newNPCList = new NPCList(npcType);
		newNPCList.npcName = npcName;
		newNPCList.npcCombat = combat;
		newNPCList.npcHealth = HP;
		NpcList[slot] = newNPCList;
	}

	@SuppressWarnings("static-access")
	public void process() {
		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] == null)
				continue;
			npcs[i].clearUpdateFlags();

		}

		for (int i = 0; i < maxNPCs; i++) {
			if (npcs[i] != null) {
				if (npcs[i].actionTimer > 0) {
					npcs[i].actionTimer--;
				}

				if (npcs[i].npcType == 945) {
					npcs[i].forcedText = "Hello there, you should go to www.sabsabionline.com!";
					npcs[i].forcedChatRequired = true;
					npcs[i].updateRequired = true;
				}

				if (npcs[i].freezeTimer > 0) {
					npcs[i].freezeTimer--;
				}

				if (npcs[i].hitDelayTimer > 0) {
					npcs[i].hitDelayTimer--;
				}

				if (npcs[i].hitDelayTimer == 1) {
					npcs[i].hitDelayTimer = 0;
					applyDamage(i);
				}

				if (npcs[i].attackTimer > 0) {
					npcs[i].attackTimer--;
				}

				if (npcs[i].spawnedBy > 0) { // delete summons npc
					if (Server.playerHandler.players[npcs[i].spawnedBy] == null
							|| Server.playerHandler.players[npcs[i].spawnedBy].heightLevel != npcs[i].heightLevel
							|| Server.playerHandler.players[npcs[i].spawnedBy].respawnTimer > 0
							|| !Server.playerHandler.players[npcs[i].spawnedBy].goodDistance(npcs[i].getX(),
									npcs[i].getY(), Server.playerHandler.players[npcs[i].spawnedBy].getX(),
									Server.playerHandler.players[npcs[i].spawnedBy].getY(), 20)) {

						if (Server.playerHandler.players[npcs[i].spawnedBy] != null) {
							for (int o = 0; o < Server.playerHandler.players[npcs[i].spawnedBy].barrowsNpcs.length; o++) {
								if (npcs[i].npcType == Server.playerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][0]) {
									if (Server.playerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] == 1)
										Server.playerHandler.players[npcs[i].spawnedBy].barrowsNpcs[o][1] = 0;
								}
							}
						}
						npcs[i] = null;
					}
				}
				if (npcs[i] == null)
					continue;

				/**
				 * Attacking player
				 **/
				if (isAggressive(i) && !npcs[i].underAttack && !npcs[i].isDead && !switchesAttackers(i)) {
					npcs[i].killerId = getCloseRandomPlayer(i);
				} else if (isAggressive(i) && !npcs[i].underAttack && !npcs[i].isDead && switchesAttackers(i)) {
					npcs[i].killerId = getCloseRandomPlayer(i);
				}

				if (System.currentTimeMillis() - npcs[i].lastDamageTaken > 5000)
					npcs[i].underAttackBy = 0;

				if ((npcs[i].killerId > 0 || npcs[i].underAttack) && !npcs[i].walkingHome
						&& retaliates(npcs[i].npcType)) {
					if (!npcs[i].isDead) {
						int p = npcs[i].killerId;
						if (Server.playerHandler.players[p] != null) {
							Player player = (Player) Server.playerHandler.players[p];
							followPlayer(i, player.playerId);
							if (npcs[i] == null)
								continue;
							if (npcs[i].attackTimer == 0) {
								if (player != null) {
									attackPlayer(player, i);
								} /*
									 * else { npcs[i].killerId = 0;
									 * npcs[i].underAttack = false;
									 * npcs[i].facePlayer(0); }
									 */
							}
						} else {
							npcs[i].killerId = 0;
							npcs[i].underAttack = false;
							npcs[i].facePlayer(0);
						}
					}
				}

				/**
				 * Random walking and walking home
				 **/
				if (npcs[i] == null)
					continue;
				if ((!npcs[i].underAttack || npcs[i].walkingHome) && npcs[i].randomWalk && !npcs[i].isDead) {
					npcs[i].facePlayer(0);
					npcs[i].killerId = 0;
					if (npcs[i].spawnedBy == 0) {
						if ((npcs[i].absX > npcs[i].makeX + Constants.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absX < npcs[i].makeX - Constants.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absY > npcs[i].makeY + Constants.NPC_RANDOM_WALK_DISTANCE)
								|| (npcs[i].absY < npcs[i].makeY - Constants.NPC_RANDOM_WALK_DISTANCE)) {
							npcs[i].walkingHome = true;
						}
					}

					if (npcs[i].walkingHome && npcs[i].absX == npcs[i].makeX && npcs[i].absY == npcs[i].makeY) {
						npcs[i].walkingHome = false;
					} else if (npcs[i].walkingHome) {
						npcs[i].moveX = GetMove(npcs[i].absX, npcs[i].makeX);
						npcs[i].moveY = GetMove(npcs[i].absY, npcs[i].makeY);
						npcs[i].getNextNPCMovement(i);
						npcs[i].updateRequired = true;
					}
					if (npcs[i].walkingType == 1) {
						if (Misc.random(3) == 1 && !npcs[i].walkingHome) {
							int MoveX = 0;
							int MoveY = 0;
							int Rnd = Misc.random(9);
							if (Rnd == 1) {
								MoveX = 1;
								MoveY = 1;
							} else if (Rnd == 2) {
								MoveX = -1;
							} else if (Rnd == 3) {
								MoveY = -1;
							} else if (Rnd == 4) {
								MoveX = 1;
							} else if (Rnd == 5) {
								MoveY = 1;
							} else if (Rnd == 6) {
								MoveX = -1;
								MoveY = -1;
							} else if (Rnd == 7) {
								MoveX = -1;
								MoveY = 1;
							} else if (Rnd == 8) {
								MoveX = 1;
								MoveY = -1;
							}

							if (MoveX == 1) {
								if (npcs[i].absX + MoveX < npcs[i].makeX + 1) {
									npcs[i].moveX = MoveX;
								} else {
									npcs[i].moveX = 0;
								}
							}

							if (MoveX == -1) {
								if (npcs[i].absX - MoveX > npcs[i].makeX - 1) {
									npcs[i].moveX = MoveX;
								} else {
									npcs[i].moveX = 0;
								}
							}

							if (MoveY == 1) {
								if (npcs[i].absY + MoveY < npcs[i].makeY + 1) {
									npcs[i].moveY = MoveY;
								} else {
									npcs[i].moveY = 0;
								}
							}

							if (MoveY == -1) {
								if (npcs[i].absY - MoveY > npcs[i].makeY - 1) {
									npcs[i].moveY = MoveY;
								} else {
									npcs[i].moveY = 0;
								}
							}

							@SuppressWarnings("unused")
							int x = (npcs[i].absX + npcs[i].moveX);
							@SuppressWarnings("unused")
							int y = (npcs[i].absY + npcs[i].moveY);
							// if (VirtualWorld.I(npcs[i].heightLevel,
							// npcs[i].absX, npcs[i].absY, x, y, 0))
							npcs[i].getNextNPCMovement(i);
							// else
							// {
							// npcs[i].moveX = 0;
							// npcs[i].moveY = 0;
							// }
							npcs[i].updateRequired = true;
						}
					}
				}

				if (npcs[i].isDead == true) {
					if (npcs[i].actionTimer == 0 && npcs[i].applyDead == false && npcs[i].needRespawn == false) {
						npcs[i].updateRequired = true;
						npcs[i].facePlayer(0);
						npcs[i].killedBy = getNpcKillerId(i);
						npcs[i].animNumber = getDeadEmote(i); // dead emote
						npcs[i].animUpdateRequired = true;
						npcs[i].freezeTimer = 0;
						npcs[i].applyDead = true;
						killedBarrow(i);
						npcs[i].actionTimer = 4; // delete time
						resetPlayersInCombat(i);
						npcs[i].dagColor = "";
					} else if (npcs[i].actionTimer == 0 && npcs[i].applyDead == true && npcs[i].needRespawn == false) {
						npcs[i].needRespawn = true;
						npcs[i].actionTimer = getRespawnTime(i); // respawn time
						dropItems(i); // npc drops items!
						tzhaarDeathHandler(i);
						appendSlayerExperience(i);
						appendKillCount(i);
						// appendJailKc(i);
						npcs[i].absX = npcs[i].makeX;
						npcs[i].absY = npcs[i].makeY;
						npcs[i].HP = npcs[i].MaxHP;
						npcs[i].animNumber = 0x328;
						npcs[i].updateRequired = true;
						npcs[i].animUpdateRequired = true;
						if (npcs[i].npcType >= 2440 && npcs[i].npcType <= 2446) {
							Server.objectManager.removeObject(npcs[i].absX, npcs[i].absY);
						}
						if (npcs[i].npcType == 2745) {
							handleJadDeath(i);
						}
					} else if (npcs[i].actionTimer == 0 && npcs[i].needRespawn == true) {
						Player player = (Player) Server.playerHandler.players[npcs[i].spawnedBy];
						if (player != null) {

							npcs[i] = null;
						} else {
							int old1 = npcs[i].npcType;
							int old2 = npcs[i].makeX;
							int old3 = npcs[i].makeY;
							int old4 = npcs[i].heightLevel;
							int old5 = npcs[i].walkingType;
							int old6 = npcs[i].MaxHP;
							int old7 = npcs[i].maxHit;
							int old8 = npcs[i].attack;
							int old9 = npcs[i].defence;

							npcs[i] = null;
							newNPC(old1, old2, old3, old4, old5, old6, old7, old8, old9);
						}
					}
				}
			}
		}
	}

	public boolean getsPulled(int i) {
		switch (npcs[i].npcType) {
		case 2550:
			if (npcs[i].firstAttacker > 0)
				return false;
			break;
		}
		return true;
	}

	public boolean multiAttacks(int i) {
		switch (npcs[i].npcType) {
		case 2558:
			return true;
		case 2562:
			if (npcs[i].attackType == 2)
				return true;
		case 2550:
			if (npcs[i].attackType == 1)
				return true;
		default:
			return false;
		}

	}

	/**
	 * Npc killer id?
	 **/

	@SuppressWarnings({ "static-access", "unused" })
	public int getNpcKillerId(int npcId) {
		int oldDamage = 0;
		int count = 0;
		int killerId = 0;
		for (int p = 1; p < Constants.MAX_PLAYERS; p++) {
			if (Server.playerHandler.players[p] != null) {
				if (Server.playerHandler.players[p].lastNpcAttacked == npcId) {
					if (Server.playerHandler.players[p].totalDamageDealt > oldDamage) {
						oldDamage = Server.playerHandler.players[p].totalDamageDealt;
						killerId = p;
					}
					Server.playerHandler.players[p].totalDamageDealt = 0;
				}
			}
		}
		return killerId;
	}

	/**
	 * 
	 */
	@SuppressWarnings("static-access")
	private void killedBarrow(int i) {
		Player player = (Player) Server.playerHandler.players[npcs[i].killedBy];
		if (player != null) {
			for (int o = 0; o < player.barrowsNpcs.length; o++) {
				if (npcs[i].npcType == player.barrowsNpcs[o][0]) {
					player.barrowsNpcs[o][1] = 2; // 2 for dead
					player.barrowsKillCount++;
				}
			}
		}
	}

	@SuppressWarnings({ "static-access" })
	private void killedTzhaar(int i) {
		final Player player = (Player) Server.playerHandler.players[npcs[i].spawnedBy];
		player.tzhaarKilled++;
		if (player.tzhaarKilled == player.tzhaarToKill) {
			player.waveId++;

			if (player != null) {
				Server.fightCaves.spawnNextWave(player);
				player.getPA().wave += 1;
				player.waveId++;

				player.getActionSender().sendMessage("@blu@Wave: " + player.getPA().wave);
			}

		}
	}

	@SuppressWarnings("static-access")
	public void handleJadDeath(int i) {
		Player player = (Player) Server.playerHandler.players[npcs[i].spawnedBy];
		player.getItems().addItem(player.fightForOnyx == true ? 6571 : 6570, 1);
		player.getItems().addItem(6529, player.fightForOnyx == true ? 32_000 / 2 + Misc.random(999) : 32_000 + Misc.random(999));
		player.getPA().resetTzhaar();
		player.getDH().sendNpcChat2("Congratulations warrior, you've defeated Jad!", "Take your reward and get out of my sight.", 2617, "Tk-nub");
		player.getActionSender().sendMessage("Congratulations on completing the fight caves minigame!");
	}

	public void sendDrop(Player player, Drop drop, int i) {
		/*
		 * Since I dumped drops from rswiki it contains items that your server
		 * might not support.
		 */
		/*
		 * This is to stop those items from dropping, If you load higher
		 * revision items, I suggest you modify this.
		 */
		if (drop.getItemId() >= Constants.ITEM_LIMIT) {
			return;
		}
		if (Item.getItemName(drop.getItemId()) == null) {
			return;
		}
		GameItem item = new GameItem(drop.getItemId(), 1).stackable
				? new GameItem(drop.getItemId(),
						(drop.getMinAmount() * Constants.DROP_RATE)
								+ Misc.random(drop.getExtraAmount() * Constants.DROP_RATE))
				: new GameItem(drop.getItemId(), drop.getMinAmount() + Misc.random(drop.getExtraAmount()));
		Server.itemHandler.createGroundItem(player, item.id, npcs[i].absX, npcs[i].absY, item.amount, player.playerId);
	}

	public void dropItems(int i) {
		Player killer = (Player) PlayerHandler.players[npcs[i].killedBy];
		Drop[] drops = NPCDrops.getDrops(npcs[i].npcType);
		if (drops == null)
			return;
		Player player = (Player) PlayerHandler.players[npcs[i].killedBy];
		if (player != null) {
			KillLog.handleKill(player, npcs[i].npcType);
			if (npcs[i].npcType == 912 || npcs[i].npcType == 913 || npcs[i].npcType == 914)
				player.magePoints += 1;
			Drop[] possibleDrops = new Drop[drops.length];
			int possibleDropsCount = 0;
			for (Drop drop : drops) {
				if (drop.getRate() == 100)
					sendDrop(killer, drop, i);
				// TODO: test
				if (player.playerEquipment[EquipmentListener.RING_SLOT.getSlot()] == 2572) {
					if (drop.isRare()) {
						possibleDropsCount = (int) (drop.getRate() / 2);
					}
				} else {
					if ((Misc.random(99) + 1) <= drop.getRate() * 1.0)
						possibleDrops[possibleDropsCount++] = drop;
				}
			}
			if (possibleDropsCount > 0)
				sendDrop(killer, possibleDrops[Misc.random(possibleDropsCount - 1)], i);
		}
	}

	@SuppressWarnings("static-access")
	public void appendKillCount(int i) {
		Player player = (Player) Server.playerHandler.players[npcs[i].killedBy];
		if (player != null) {
			int[] kcMonsters = { 122, 49, 2558, 2559, 2560, 2561, 2550, 2551, 2552, 2553, 2562, 2563, 2564, 2565 };
			for (int j : kcMonsters) {
				if (npcs[i].npcType == j) {
					if (player.killCount < 20) {
						player.killCount++;
						player.getActionSender().sendMessage("Killcount: " + player.killCount);
					} else {
						player.getActionSender().sendMessage("You already have 20 kill count");
					}
					break;
				}
			}
		}
	}

	// id of bones dropped by npcs
	public int boneDrop(int type) {
		switch (type) {
		case 1:// normal bones
		case 9:
		case 100:
		case 12:
		case 17:
		case 803:
		case 18:
		case 81:
		case 101:
		case 41:
		case 19:
		case 90:
		case 75:
		case 86:
		case 78:
		case 912:
		case 913:
		case 914:
		case 1648:
		case 1643:
		case 1618:
		case 1624:
		case 181:
		case 119:
		case 49:
		case 26:
		case 1341:
			return 526;
		case 117:
			return 532;// big bones
		case 50:// drags
		case 53:
		case 54:
		case 55:
		case 941:
		case 1590:
		case 1591:
		case 1592:
			return 536;
		case 84:
		case 1615:
		case 1613:
		case 82:
		case 3200:
			return 592;
		case 2881:
		case 2882:
		case 2883:
			return 6729;
		default:
			return -1;
		}
	}

	public int getStackedDropAmount(int itemId, int npcId) {
		switch (itemId) {
		case 995:
			switch (npcId) {
			case 1:
				return 50 + Misc.random(50);
			case 9:
				return 133 + Misc.random(100);
			case 1624:
				return 1000 + Misc.random(300);
			case 1618:
				return 1000 + Misc.random(300);
			case 1643:
				return 1000 + Misc.random(300);
			case 1610:
				return 1000 + Misc.random(1000);
			case 1613:
				return 1500 + Misc.random(1250);
			case 1615:
				return 3000;
			case 18:
				return 500;
			case 101:
				return 60;
			case 913:
			case 912:
			case 914:
				return 750 + Misc.random(500);
			case 1612:
				return 250 + Misc.random(500);
			case 1648:
				return 250 + Misc.random(250);
			case 90:
				return 200;
			case 82:
				return 1000 + Misc.random(455);
			case 52:
				return 400 + Misc.random(200);
			case 49:
				return 1500 + Misc.random(2000);
			case 1341:
				return 1500 + Misc.random(500);
			case 26:
				return 500 + Misc.random(100);
			case 20:
				return 750 + Misc.random(100);
			case 21:
				return 890 + Misc.random(125);
			case 117:
				return 500 + Misc.random(250);
			case 2607:
				return 500 + Misc.random(350);
			}
			break;
		case 11212:
			return 10 + Misc.random(4);
		case 565:
		case 561:
			return 10;
		case 560:
		case 563:
		case 562:
			return 15;
		case 555:
		case 554:
		case 556:
		case 557:
			return 20;
		case 892:
			return 40;
		case 886:
			return 100;
		case 6522:
			return 6 + Misc.random(5);

		}

		return 1;
	}

	@SuppressWarnings("static-access")
	/**
	 * slayer exp
	 * 
	 * @param i
	 */
	public void appendSlayerExperience(int i) {
		@SuppressWarnings("unused")
		int npc = 0;
		Player player = (Player) Server.playerHandler.players[npcs[i].killedBy];
		if (player != null) {
			if (player.slayerTask == npcs[i].npcType) {
				player.taskAmount--;
				player.getPA().addSkillXP(npcs[i].MaxHP * SkillIndex.SLAYER.getExpRatio(), SkillIndex.SLAYER.getSkillId());
				if (player.taskAmount <= 0) {
					player.getPA().addSkillXP((npcs[i].MaxHP * 8) * SkillIndex.SLAYER.getExpRatio(),
							SkillIndex.SLAYER.getSkillId());
					player.slayerTask = -1;
					player.getActionSender().sendMessage("You completed your slayer task. Please see a slayer master to get a new one.");
				}
			}
		}
	}

	@SuppressWarnings({ "static-access" })
	/**
	 * Resets players in combat
	 * 
	 * @param i
	 */
	public void resetPlayersInCombat(int i) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null)
				if (Server.playerHandler.players[j].underAttackBy2 == i)
					Server.playerHandler.players[j].underAttackBy2 = 0;
		}
	}

	@SuppressWarnings("static-access")
	/**
	 * Npc names
	 * 
	 * @param npcId
	 * @return npcId
	 */
	public String getNpcName(int npcId) {
		for (int i = 0; i < maxNPCs; i++) {
			if (Server.npcHandler.NpcList[i] != null) {
				if (Server.npcHandler.NpcList[i].npcId == npcId) {
					return Server.npcHandler.NpcList[i].npcName;
				}
			}
		}
		return "-1";
	}

	/**
	 * Npc Follow Player
	 * 
	 * @param Place1
	 * @param Place2
	 * @return 0
	 */
	public int GetMove(int Place1, int Place2) {
		if ((Place1 - Place2) == 0) {
			return 0;
		} else if ((Place1 - Place2) < 0) {
			return 1;
		} else if ((Place1 - Place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean followPlayer(int i) {
		switch (npcs[i].npcType) {
		case 2892:
		case 2894:
			return false;
		}
		return true;
	}

	@SuppressWarnings("static-access")
	public void followPlayer(int i, int playerId) {
		if (Server.playerHandler.players[playerId] == null) {
			return;
		}
		if (Server.playerHandler.players[playerId].respawnTimer > 0) {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
			return;
		}

		if (!followPlayer(i)) {
			npcs[i].facePlayer(playerId);
			return;
		}

		int playerX = Server.playerHandler.players[playerId].absX;
		int playerY = Server.playerHandler.players[playerId].absY;
		npcs[i].randomWalk = false;
		if (goodDistance(npcs[i].getX(), npcs[i].getY(), playerX, playerY, distanceRequired(i)))
			return;
		if ((npcs[i].spawnedBy > 0) || ((npcs[i].absX < npcs[i].makeX + Constants.NPC_FOLLOW_DISTANCE)
				&& (npcs[i].absX > npcs[i].makeX - Constants.NPC_FOLLOW_DISTANCE)
				&& (npcs[i].absY < npcs[i].makeY + Constants.NPC_FOLLOW_DISTANCE)
				&& (npcs[i].absY > npcs[i].makeY - Constants.NPC_FOLLOW_DISTANCE))) {
			if (npcs[i].heightLevel == Server.playerHandler.players[playerId].heightLevel) {
				if (Server.playerHandler.players[playerId] != null && npcs[i] != null) {
					if (playerY < npcs[i].absY) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerY > npcs[i].absY) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerX < npcs[i].absX) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerX > npcs[i].absX) {
						npcs[i].moveX = GetMove(npcs[i].absX, playerX);
						npcs[i].moveY = GetMove(npcs[i].absY, playerY);
					} else if (playerX == npcs[i].absX || playerY == npcs[i].absY) {
						int o = Misc.random(3);
						switch (o) {
						case 0:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY + 1);
							break;

						case 1:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY - 1);
							break;

						case 2:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX + 1);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY);
							break;

						case 3:
							npcs[i].moveX = GetMove(npcs[i].absX, playerX - 1);
							npcs[i].moveY = GetMove(npcs[i].absY, playerY);
							break;
						}
					}
					@SuppressWarnings("unused")
					int x = (npcs[i].absX + npcs[i].moveX);
					@SuppressWarnings("unused")
					int y = (npcs[i].absY + npcs[i].moveY);
					npcs[i].facePlayer(playerId);
					// if (checkClipping(i))
					npcs[i].getNextNPCMovement(i);
					/*
					 * else { npcs[i].moveX = 0; npcs[i].moveY = 0; }
					 */
					npcs[i].facePlayer(playerId);
					npcs[i].updateRequired = true;
				}
			}
		} else {
			npcs[i].facePlayer(0);
			npcs[i].randomWalk = true;
			npcs[i].underAttack = false;
		}
	}

	@SuppressWarnings("unused")
	public boolean checkClipping(int i) {
		NPC npc = npcs[i];
		int size = npcSize(i);

		for (int x = 0; x < size; x++) {
			for (int y = 0; y < size; y++) {
				// if (!VirtualWorld.I(npc.heightLevel, npc.absX + x, npc.absY +
				// y, npc.absX + npc.moveX, npc.absY + npc.moveY, 0))
				return false;
			}
		}

		return true;
	}

	/**
	 * load spell
	 * 
	 * @param i
	 */
	public void loadSpell2(int i) {
		npcs[i].attackType = 3;
		int random = Misc.random(3);
		if (random == 0) {
			npcs[i].projectileId = 393; // red
			npcs[i].endGfx = 430;
		} else if (random == 1) {
			npcs[i].projectileId = 394; // green
			npcs[i].endGfx = 429;
		} else if (random == 2) {
			npcs[i].projectileId = 395; // white
			npcs[i].endGfx = 431;
		} else if (random == 3) {
			npcs[i].projectileId = 396; // blue
			npcs[i].endGfx = 428;
		}
	}

	@SuppressWarnings("static-access")
	public void loadSpell(int i) {
		switch (npcs[i].npcType) {
		case 2892:
			npcs[i].projectileId = 94;
			npcs[i].attackType = 2;
			npcs[i].endGfx = 95;
			break;
		case 2894:
			npcs[i].projectileId = 298;
			npcs[i].attackType = 1;
			break;
		case 50:
			int random = Misc.random(4);
			if (random == 0) {
				npcs[i].projectileId = 393; // red
				npcs[i].endGfx = 430;
				npcs[i].attackType = 3;
			} else if (random == 1) {
				npcs[i].projectileId = 394; // green
				npcs[i].endGfx = 429;
				npcs[i].attackType = 3;
			} else if (random == 2) {
				npcs[i].projectileId = 395; // white
				npcs[i].endGfx = 431;
				npcs[i].attackType = 3;
			} else if (random == 3) {
				npcs[i].projectileId = 396; // blue
				npcs[i].endGfx = 428;
				npcs[i].attackType = 3;
			} else if (random == 4) {
				npcs[i].projectileId = -1; // melee
				npcs[i].endGfx = -1;
				npcs[i].attackType = 0;
			}
			break;
		// arma npcs
		case 2561:
			npcs[i].attackType = 0;
			break;
		case 2560:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 1190;
			break;
		case 2559:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 2558:
			random = Misc.random(1);
			npcs[i].attackType = 1 + random;
			if (npcs[i].attackType == 1) {
				npcs[i].projectileId = 1197;
			} else {
				npcs[i].attackType = 2;
				npcs[i].projectileId = 1198;
			}
			break;
		// sara npcs
		case 2562: // sara
			random = Misc.random(1);
			if (random == 0) {
				npcs[i].attackType = 2;
				npcs[i].endGfx = 1224;
				npcs[i].projectileId = -1;
			} else if (random == 1)
				npcs[i].attackType = 0;
			break;
		case 2563: // star
			npcs[i].attackType = 0;
			break;
		case 2564: // growler
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 2565: // bree
			npcs[i].attackType = 1;
			npcs[i].projectileId = 9;
			break;
		// bandos npcs
		case 2550:
			random = Misc.random(2);
			if (random == 0 || random == 1)
				npcs[i].attackType = 0;
			else {
				npcs[i].attackType = 1;
				npcs[i].endGfx = 1211;
				npcs[i].projectileId = 288;
			}
			break;
		case 2551:
			npcs[i].attackType = 0;
			break;
		case 2552:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 1203;
			break;
		case 2553:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 1206;
			break;
		case 2025:
			npcs[i].attackType = 2;
			int r = Misc.random(3);
			if (r == 0) {
				npcs[i].gfx100(158);
				npcs[i].projectileId = 159;
				npcs[i].endGfx = 160;
			}
			if (r == 1) {
				npcs[i].gfx100(161);
				npcs[i].projectileId = 162;
				npcs[i].endGfx = 163;
			}
			if (r == 2) {
				npcs[i].gfx100(164);
				npcs[i].projectileId = 165;
				npcs[i].endGfx = 166;
			}
			if (r == 3) {
				npcs[i].gfx100(155);
				npcs[i].projectileId = 156;
			}
			break;
		case 2881:// supreme
			npcs[i].attackType = 1;
			npcs[i].projectileId = 298;
			break;

		case 2882:// prime
			npcs[i].attackType = 2;
			npcs[i].projectileId = 162;
			npcs[i].endGfx = 477;
			break;

		case 2028:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 27;
			break;

		case 3200:
			int r2 = Misc.random(1);
			if (r2 == 0) {
				npcs[i].attackType = 1;
				npcs[i].gfx100(550);
				npcs[i].projectileId = 551;
				npcs[i].endGfx = 552;
			} else {
				npcs[i].attackType = 2;
				npcs[i].gfx100(553);
				npcs[i].projectileId = 554;
				npcs[i].endGfx = 555;
			}
			break;
		case 2745:
			int r3 = 0;
			if (goodDistance(npcs[i].absX, npcs[i].absY, Server.playerHandler.players[npcs[i].spawnedBy].absX,
					Server.playerHandler.players[npcs[i].spawnedBy].absY, 1))
				r3 = Misc.random(2);
			else
				r3 = Misc.random(1);
			if (r3 == 0) {
				npcs[i].attackType = 2;
				npcs[i].endGfx = 157;
				npcs[i].projectileId = 448;
			} else if (r3 == 1) {
				npcs[i].attackType = 1;
				npcs[i].endGfx = 451;
				npcs[i].projectileId = -1;
			} else if (r3 == 2) {
				npcs[i].attackType = 0;
				npcs[i].projectileId = -1;
			}
			break;
		case 2743:
			npcs[i].attackType = 2;
			npcs[i].projectileId = 445;
			npcs[i].endGfx = 446;
			break;

		case 2631:
			npcs[i].attackType = 1;
			npcs[i].projectileId = 443;
			break;
		}
	}

	/**
	 * Distanced required to attack
	 * 
	 * @param i
	 * @return
	 */
	public int distanceRequired(int i) {
		switch (npcs[i].npcType) {
		case 2025:
		case 2028:
			return 6;
		case 50:
		case 2562:
			return 2;
		case 2881:// dag kings
		case 2882:
		case 3200:// chaos ele
		case 2743:
		case 2631:
		case 2745:
			return 8;
		case 2883:// rex
			return 1;
		case 2552:
		case 2553:
		case 2556:
		case 2557:
		case 2558:
		case 2559:
		case 2560:
		case 2564:
		case 2565:
			return 9;
		// things around dags
		case 2892:
		case 2894:
			return 10;
		default:
			return 1;
		}
	}

	public int followDistance(int i) {
		switch (npcs[i].npcType) {
		case 2550:
		case 2551:
		case 2562:
		case 2563:
			return 8;
		case 2883:
			return 4;
		case 2881:
		case 2882:
			return 1;

		}
		return 0;

	}

	public int getProjectileSpeed(int i) {
		switch (npcs[i].npcType) {
		case 2881:
		case 2882:
		case 3200:
			return 85;

		case 2745:
			return 130;

		case 50:
			return 90;

		case 2025:
			return 85;

		case 2028:
			return 80;

		default:
			return 85;
		}
	}

	@SuppressWarnings("static-access")
	/**
	 * NPC Attacking Player
	 * 
	 * @param c
	 * @param i
	 */
	public void attackPlayer(Player player, int i) {
		if (npcs[i] != null) {
			if (npcs[i].isDead)
				return;
			if (!npcs[i].inMulti() && npcs[i].underAttackBy > 0 && npcs[i].underAttackBy != player.playerId) {
				npcs[i].killerId = 0;
				return;
			}
			if (!npcs[i].inMulti() && (player.underAttackBy > 0 || (player.underAttackBy2 > 0 && player.underAttackBy2 != i))) {
				npcs[i].killerId = 0;
				return;
			}
			if (npcs[i].heightLevel != player.heightLevel) {
				npcs[i].killerId = 0;
				return;
			}
			npcs[i].facePlayer(player.playerId);
			boolean special = false;// specialCase(c,i);
			if (goodDistance(npcs[i].getX(), npcs[i].getY(), player.getX(), player.getY(), distanceRequired(i)) || special) {
				if (player.respawnTimer <= 0) {
					npcs[i].facePlayer(player.playerId);
					npcs[i].attackTimer = getNpcDelay(i);
					if (SpecialNPC.forId(npcs[i].npcType) != null) {
						SpecialNPC.executeAttack(player, i);
						// return;
					}
					npcs[i].hitDelayTimer = getHitDelay(i);
					npcs[i].attackType = 0;
					if (special)
						loadSpell2(i);
					else
						loadSpell(i);
					if (npcs[i].attackType == 3)
						npcs[i].hitDelayTimer += 2;
					if (multiAttacks(i)) {
						multiAttackGfx(i, npcs[i].projectileId);
						startAnimation(getAttackEmote(i), i);
						player.getPA().sendSound(Sounds.getNpcAttackSounds(npcs[i].npcId));
						npcs[i].oldIndex = player.playerId;
						return;
					}

					if (SpecialNPC.forId(npcs[i].npcType) != null) {
						SpecialNPC.executeAttack(player, i);
						return;
					}
					if (npcs[i].projectileId > 0) {
						int nX = Server.npcHandler.npcs[i].getX() + offset(i);
						int nY = Server.npcHandler.npcs[i].getY() + offset(i);
						int pX = player.getX();
						int pY = player.getY();
						int offX = (nY - pY) * -1;
						int offY = (nX - pX) * -1;
						player.getPA().createPlayersProjectile(nX, nY, offX, offY, 50, getProjectileSpeed(i),
								npcs[i].projectileId, 43, 31, -player.getId() - 1, 65);
					}
					player.underAttackBy2 = i;
					player.singleCombatDelay2 = System.currentTimeMillis();
					npcs[i].oldIndex = player.playerId;
					startAnimation(getAttackEmote(i), i);
					player.getPA().removeAllWindows();
				}
			}
		}
	}

	public int offset(int i) {
		switch (npcs[i].npcType) {
		case 50:
			return 2;
		case 2881:
		case 2882:
			return 1;
		case 2745:
		case 2743:
			return 1;
		}
		return 0;
	}

	public boolean specialCase(Player player, int i) { // responsible for npcs that
													// much
		if (goodDistance(npcs[i].getX(), npcs[i].getY(), player.getX(), player.getY(), 8)
				&& !goodDistance(npcs[i].getX(), npcs[i].getY(), player.getX(), player.getY(), distanceRequired(i)))
			return true;
		return false;
	}

	public boolean retaliates(int npcType) {
		return npcType < 3777 || npcType > 3780 && !(npcType >= 2440 && npcType <= 2446);
	}

	@SuppressWarnings("static-access")
	public void applyDamage(int i) {
		if (npcs[i] != null) {
			if (Server.playerHandler.players[npcs[i].oldIndex] == null) {
				return;
			}
			if (npcs[i].isDead)
				return;
			Player player = (Player) Server.playerHandler.players[npcs[i].oldIndex];
			if (multiAttacks(i)) {
				multiAttackDamage(i);
				return;
			}
			if (player.playerIndex <= 0 && player.npcIndex <= 0)
				if (player.autoRet == 1)
					player.npcIndex = i;

			if (player.respawnTimer <= 0) {
				int damage = 0;
				if (npcs[i].attackType == 0) {
					damage = Misc.random(npcs[i].maxHit);
					if (10 + Misc.random(player.getCombat().calculateMeleeDefence()) > Misc
							.random(Server.npcHandler.npcs[i].attack)) {
						damage = 0;
					}
					if (player.prayerActive[18]) { // protect from melee
						if (npcs[i].npcType == 2030)
							damage = (damage / 2);
						else
							damage = 0;
					}
					if (player.playerLevel[3] - damage < 0) {
						damage = player.playerLevel[3];
					}
				}

				if (npcs[i].attackType == 1) { // range
					damage = Misc.random(npcs[i].maxHit);
					if (10 + Misc.random(player.getCombat().calculateRangeDefence()) > Misc
							.random(Server.npcHandler.npcs[i].attack)) {
						damage = 0;
					}
					if (player.prayerActive[17]) { // protect from range
						damage = 0;
					}
					if (player.playerLevel[3] - damage < 0) {
						damage = player.playerLevel[3];
					}
				}

				if (npcs[i].attackType == 2) { // magic
					damage = Misc.random(npcs[i].maxHit);
					boolean magicFailed = false;
					if (10 + Misc.random(player.getCombat().mageDef()) > Misc.random(Server.npcHandler.npcs[i].attack)) {
						damage = 0;
						magicFailed = true;
					}
					if (player.prayerActive[16]) { // protect from magic
						damage = 0;
						magicFailed = true;
					}
					if (player.playerLevel[3] - damage < 0) {
						damage = player.playerLevel[3];
					}
					if (npcs[i].endGfx > 0 && (!magicFailed || isFightCaveNpc(i))) {
						player.gfx100(npcs[i].endGfx);
					} else {
						player.gfx100(85);
					}
				}

				if (npcs[i].attackType == 3) { // fire breath
					int anti = player.getPA().antiFire();
					if (anti == 0) {
						damage = Misc.random(30) + 10;
						player.getActionSender().sendMessage("You are badly burnt by the dragon fire!");
					} else if (anti == 1)
						damage = Misc.random(20);
					else if (anti == 2)
						damage = Misc.random(5);
					if (player.playerLevel[3] - damage < 0)
						damage = player.playerLevel[3];
					player.gfx100(npcs[i].endGfx);
				}

				handleSpecialEffects(player, i, damage);
				player.logoutDelay = System.currentTimeMillis(); // logout delay
				// c.setHitDiff(damage);
				player.handleHitMask(damage);
				player.playerLevel[3] -= damage;
				player.getPA().refreshSkill(3);
				player.updateRequired = true;
				FightCaves.tzKihEffect(player, i, damage);
				// c.setHitUpdateRequired(true);
			}
		}
	}

	public void handleSpecialEffects(Player player, int i, int damage) {
		if (npcs[i].npcType == 2892 || npcs[i].npcType == 2894) {
			if (damage > 0) {
				if (player != null) {
					if (player.playerLevel[5] > 0) {
						player.playerLevel[5]--;
						player.getPA().refreshSkill(5);
						player.getPA().appendPoison(12);
					}
				}
			}
		}

	}

	public void startAnimation(int animId, int i) {
		npcs[i].animNumber = animId;
		npcs[i].animUpdateRequired = true;
		npcs[i].updateRequired = true;
	}

	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX - playerX <= distance && objectX - playerX >= -distance)
				&& (objectY - playerY <= distance && objectY - playerY >= -distance));
	}

	public int getMaxHit(int i) {
		switch (npcs[i].npcType) {
		case 2558:
			if (npcs[i].attackType == 2)
				return 28;
			else
				return 68;
		case 2562:
			return 31;
		case 2550:
			return 36;
		}
		return 1;
	}

	@SuppressWarnings("resource")
	public boolean loadAutoSpawn(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		@SuppressWarnings("unused")
		int ReadMode = 0;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
			return false;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("spawn")) {
					newNPC(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]),
							Integer.parseInt(token3[3]), Integer.parseInt(token3[4]),
							getNpcListHP(Integer.parseInt(token3[0])), Integer.parseInt(token3[5]),
							Integer.parseInt(token3[6]), Integer.parseInt(token3[7]));

				}
			} else {
				if (line.equals("[ENDOFSPAWNLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	public int getNpcListHP(int npcId) {
		for (int i = 0; i < maxListedNPCs; i++) {
			if (NpcList[i] != null) {
				if (NpcList[i].npcId == npcId) {
					return NpcList[i].npcHealth;
				}
			}
		}
		return 0;
	}

	public String getNpcListName(int npcId) {
		for (int i = 0; i < maxListedNPCs; i++) {
			if (NpcList[i] != null) {
				if (NpcList[i].npcId == npcId) {
					return NpcList[i].npcName;
				}
			}
		}
		return "nothing";
	}

	@SuppressWarnings("resource")
	public boolean loadNPCList(String FileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		@SuppressWarnings("unused")
		int ReadMode = 0;
		BufferedReader characterfile = null;
		try {
			characterfile = new BufferedReader(new FileReader("./" + FileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(FileName + ": file not found.");
			return false;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(FileName + ": error loading file.");
			return false;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("npc")) {
					newNPCList(Integer.parseInt(token3[0]), token3[1], Integer.parseInt(token3[2]),
							Integer.parseInt(token3[3]));
				}
			} else {
				if (line.equals("[ENDOFNPCLIST]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return true;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	private void tzhaarDeathHandler(int i) {
		if (isFightCaveNpc(i) && npcs[i].npcType != FightCaves.TZ_KEK) {
			killedTzhaar(i);
		}
		if (npcs[i].npcType == FightCaves.TZ_KEK_SPAWN) {
			int p = npcs[i].killerId;
			if (PlayerHandler.players[p] != null) {
				Player player = (Player) PlayerHandler.players[p];
				player.tzKekSpawn += 1;
				if (player.tzKekSpawn == 2) {
					killedTzhaar(i);
					player.tzKekSpawn = 0;
				}
			}
		}
		if (npcs[i].npcType == FightCaves.TZ_KEK) {
			int p = npcs[i].killerId;
			if (PlayerHandler.players[p] != null) {
				Player player = (Player) PlayerHandler.players[p];
				FightCaves.tzKekEffect(player, i);
			}
		}
	}
}
