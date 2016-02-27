package server.model.players;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import server.Constants;
import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.Animation;
import server.model.EntityType;
import server.model.Graphic;
import server.model.content.Membership;
import server.model.dialogues.DialogueContainer;
import server.model.dialogues.DialogueHandler;
import server.model.dialogues.NpcDialogue;
import server.model.items.Item;
import server.model.items.ItemAssistant;
import server.model.minigames.castle_wars.CastleWars;
import server.model.npcs.NPC;
import server.model.npcs.NPCHandler;
import server.model.players.combat.CombatAssistant;
import server.model.players.combat.range.CannonCoords;
import server.model.players.combat.range.DwarfMultiCannon;
import server.model.players.packet.PacketHandler;
import server.model.players.skills.guilds.RangersGuild;
import server.model.shops.ShopAssistant;
import server.net.Packet;
import server.net.Packet.Type;
import server.net.login.SideBars;
import server.util.ISAACCipher;
import server.util.Misc;
import server.util.Plugin;
import server.util.Stream;
import server.world.sound.Sounds;

public class Player {

	public ArrayList<String> killedPlayers = new ArrayList<String>();
	public ArrayList<Integer> attackedPlayers = new ArrayList<Integer>();

	public boolean initialized = false, disconnected = false, ruleAgreeButton = false, RebuildNPCList = false,
			isActive = false, isKicked = false, isSkulled = false, friendUpdate = false, newPlayer = false,
			hasMultiSign = false, saveCharacter = false, mouseButton = false, splitChat = false, chatEffects = true,
			acceptAid = false, nextDialogue = false, autocasting = false, usedSpecial = false, mageFollow = false,
			dbowSpec = false, craftingLeather = false, properLogout = false, secDbow = false, maxNextHit = false,
			ssSpec = false, vengOn = false, addStarter = false, accountFlagged = false, msbSpec = false,
			disableAttEvt = false, AttackEventRunning = false, npcindex, spawned = false, teleporting;

	public int saveDelay, playerKilled, pkPoints, totalPlayerDamageDealt, killedBy, lastChatId = 1, privateChat,
			friendSlot = 0, dialogueId, randomCoffin, newLocation, specEffect, specBarId, attackLevelReq,
			defenceLevelReq, strengthLevelReq, rangeLevelReq, magicLevelReq, followId, skullTimer, votingPoints,
			talkingNpc = -1, dialogueAction = 0, autocastId, followDistance, followId2, barrageCount = 0,
			delayedDamage = 0, delayedDamage2 = 0, pcPoints = 0, magePoints = 0, lastArrowUsed = -1, clanId = -1,
			autoRet = 0, pcDamage = 0, xInterfaceId = 0, xRemoveId = 0, xRemoveSlot = 0, tzhaarToKill = 0,
			tzhaarKilled = 0, waveId, frozenBy = 0, poisonDamage = 0, teleAction = 0, bonusAttack = 0,
			lastNpcAttacked = 0, killCount = 0;

	public boolean settingUpCannon, hasCannon, cannonIsShooting, setUpBase, setUpStand, setUpBarrels, setUpFurnace;
	public int cannonBalls, cannonBaseX, cannonBaseY, cannonBaseH, rotation, cannonID;
	public server.model.objects.Objects oldCannon;

	public int tzKekSpawn = 0, caveWave, tzhaarNpcs;
	public int tzKekTimer;
	public int startDate;
	public boolean membership = false;
	public String clanName, properName;
	public int[] voidStatus = new int[5];
	public int[] itemKeptId = new int[4];
	public int[] pouches = new int[4];
	public final int[] POUCH_SIZE = { 3, 6, 9, 12 };
	public boolean[] invSlot = new boolean[28], equipSlot = new boolean[14];
	public long friends[] = new long[200];
	public int[] loggedKills = new int[30];
	public int[] unlockedPets = new int[30];

	public DialogueContainer dialogueContainer;

	/**
	 * Music
	 */
	public boolean isLoopingMusic = true;
	public int auto = 1;

	public double specAmount = 0;
	public double specAccuracy = 1;
	public int recoilHits = 0;
	public double specDamage = 1;
	public double prayerPoint = 1.0;
	public int teleGrabItem, teleGrabX, teleGrabY, duelCount, underAttackBy, underAttackBy2, wildLevel, teleTimer,
			respawnTimer, saveTimer = 0, teleBlockLength, poisonDelay;
	public long lastVeng, lastProtItem, lastSpear, lastPoisonSip, teleGrabDelay, poisonImmune, protMageDelay,
			protMeleeDelay, protRangeDelay, lastAction, lastThieve, lastLockPick, alchDelay,
			specDelay = System.currentTimeMillis(), duelDelay, teleBlockDelay, godSpellDelay, singleCombatDelay,
			singleCombatDelay2, reduceStat, restoreStatsDelay, logoutDelay, buryDelay, foodDelay, potDelay;
	public boolean canChangeAppearance = false;
	public boolean mageAllowed;
	public byte poisonMask = 0;
	public boolean[] curseActive = { false, false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false };
	public int focusPointX = -1, focusPointY = -1;

	public int DirectionCount = 0;
	public boolean appearanceUpdateRequired = true;
	public int hitDiff2;
	public int hitDiff = 0;
	public boolean hitUpdateRequired2;
	public boolean hitUpdateRequired = false;
	public boolean isDead = false;

	public void faceNPC(int index) {
		faceNPC = index;
		faceNPCupdate = true;
		updateRequired = true;
	}

	protected boolean faceNPCupdate = false;
	public int faceNPC = -1;

	public void appendFaceNPCUpdate(Stream str) {
		str.writeWordBigEndian(faceNPC);
	}

	public boolean isNpc = false;
	public int npcId2 = 0;
	public final int[] BOWS = { 9185, 839, 845, 847, 851, 855, 859, 841, 843, 849, 853, 857, 861, 4212, 4214, 4215,
			11235, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 6724, 4734, 4934, 4935, 4936, 4937 };
	public final int[] ARROWS = { 882, 884, 886, 888, 890, 892, 4740, 11212, 9140, 9141, 4142, 9143, 9144, 9240, 9241,
			9242, 9243, 9244, 9245 };
	public final int[] NO_ARROW_DROP = { 4212, 4214, 4215, 4216, 4217, 4218, 4219, 4220, 4221, 4222, 4223, 4734, 4934,
			4935, 4936, 4937 };
	public final int[] OTHER_RANGE_WEAPONS = { 863, 864, 865, 866, 867, 868, 869, 806, 807, 808, 809, 810, 811, 825,
			826, 827, 828, 829, 830, 800, 801, 802, 803, 804, 805, 6522 };

	public boolean isAutoButton(int button) {
		for (int j = 0; j < autocastIds.length; j += 2) {
			if (autocastIds[j] == button)
				return true;
		}
		return false;
	}

	public int squaresRan = 0, runEnergy = 100;
	public long runEnergyTime;
	public int[] autocastIds = { 51133, 32, 51185, 33, 51091, 34, 24018, 35, 51159, 36, 51211, 37, 51111, 38, 51069, 39,
			51146, 40, 51198, 41, 51102, 42, 51058, 43, 51172, 44, 51224, 45, 51122, 46, 51080, 47, 7038, 0, 7039, 1,
			7040, 2, 7041, 3, 7042, 4, 7043, 5, 7044, 6, 7045, 7, 7046, 8, 7047, 9, 7048, 10, 7049, 11, 7050, 12, 7051,
			13, 7052, 14, 7053, 15, 47019, 27, 47020, 25, 47021, 12, 47022, 13, 47023, 14, 47024, 15 };

	// public String spellName = "Select Spell";
	public void assignAutocast(int button) {
		for (int j = 0; j < autocastIds.length; j++) {
			if (autocastIds[j] == button) {
				@SuppressWarnings("static-access")
				Player c = (Player) Server.playerHandler.players[this.playerId];
				autocasting = true;
				autocastId = autocastIds[j + 1];
				c.getPA().sendFrame36(108, 1);
				c.setSidebarInterface(0, 328);
				// spellName = getSpellName(autocastId);
				// spellName = spellName;
				// c.getPA().sendFrame126(spellName, 354);
				c = null;
				break;
			}
		}
	}

	// statistics
	public int duelWins = 0, duelLoses = 0, teleHome = 0, specsUsed = 0, foodEaten = 0, potsDrank = 0,
			emotesPerformed = 0, timesVoted = 0, votesClaimed = 0, timesMuted = 0, timesBanned = 0, prayersAcivated = 0,
			mbOpened = 0, itemsUpgraded = 0, kills = 0, deaths = 0;

	public String getSpellName(int id) {
		switch (id) {
		case 0:
			return "Air Strike";
		case 1:
			return "Water Strike";
		case 2:
			return "Earth Strike";
		case 3:
			return "Fire Strike";
		case 4:
			return "Air Bolt";
		case 5:
			return "Water Bolt";
		case 6:
			return "Earth Bolt";
		case 7:
			return "Fire Bolt";
		case 8:
			return "Air Blast";
		case 9:
			return "Water Blast";
		case 10:
			return "Earth Blast";
		case 11:
			return "Fire Blast";
		case 12:
			return "Air Wave";
		case 13:
			return "Water Wave";
		case 14:
			return "Earth Wave";
		case 15:
			return "Fire Wave";
		case 32:
			return "Shadow Rush";
		case 33:
			return "Smoke Rush";
		case 34:
			return "Blood Rush";
		case 35:
			return "Ice Rush";
		case 36:
			return "Shadow Burst";
		case 37:
			return "Smoke Burst";
		case 38:
			return "Blood Burst";
		case 39:
			return "Ice Burst";
		case 40:
			return "Shadow Blitz";
		case 41:
			return "Smoke Blitz";
		case 42:
			return "Blood Blitz";
		case 43:
			return "Ice Blitz";
		case 44:
			return "Shadow Barrage";
		case 45:
			return "Smoke Barrage";
		case 46:
			return "Blood Barrage";
		case 47:
			return "Ice Barrage";
		default:
			return "Select Spell";
		}
	}

	public int[] playerEquipment = new int[14];

	public int[] getEquipment() {
		return playerEquipment;
	}

	public int[] playerEquipmentN = new int[14];
	public int[] playerLevel = new int[25];

	public int[] getLevel() {
		return playerLevel;
	}

	public int[] playerXP = new int[25];

	public int[] getExperience() {
		return this.playerXP;
	}

	public boolean fullVoidRange() {
		return playerEquipment[EquipmentListener.HAT_SLOT.getSlot()] == 11664
				&& playerEquipment[EquipmentListener.LEGS_SLOT.getSlot()] == 8840
				&& playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 8839
				&& playerEquipment[EquipmentListener.GLOVES_SLOT.getSlot()] == 8842;
	}

	public boolean fullVoidMage() {
		return playerEquipment[EquipmentListener.HAT_SLOT.getSlot()] == 11663
				&& playerEquipment[EquipmentListener.LEGS_SLOT.getSlot()] == 8840
				&& playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 8839
				&& playerEquipment[EquipmentListener.GLOVES_SLOT.getSlot()] == 8842;
	}

	public boolean fullVoidMelee() {
		return playerEquipment[EquipmentListener.HAT_SLOT.getSlot()] == 11665
				&& playerEquipment[EquipmentListener.LEGS_SLOT.getSlot()] == 8840
				&& playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 8839
				&& playerEquipment[EquipmentListener.GLOVES_SLOT.getSlot()] == 8842;
	}

	public int[][] barrowsNpcs = { { 2030, 0 }, // verac
			{ 2029, 0 }, // toarg
			{ 2028, 0 }, // karil
			{ 2027, 0 }, // guthan
			{ 2026, 0 }, // dharok
			{ 2025, 0 } // ahrim
	};
	public int barrowsKillCount;

	public int reduceSpellId;
	public final int[] REDUCE_SPELL_TIME = { 250000, 250000, 250000, 500000, 500000, 500000 };
	public long[] reduceSpellDelay = new long[6];
	public final int[] REDUCE_SPELLS = { 1153, 1157, 1161, 1542, 1543, 1562 };
	public boolean[] canUseReducingSpell = { true, true, true, true, true, true };

	public int slayerTask, taskAmount;

	public int prayerId = -1;
	public int headIcon = -1;
	public int bountyIcon = 0;
	public long stopPrayerDelay, prayerDelay;
	public boolean usingPrayer;
	public final int[] PRAYER_DRAIN_RATE = { 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500,
			500, 500, 500, 500, 500, 500, 500, 500, 500, 500, 500 };
	public final int[] PRAYER_LEVEL_REQUIRED = { 1, 4, 7, 8, 9, 10, 13, 16, 19, 22, 25, 26, 27, 28, 31, 34, 37, 40, 43,
			44, 45, 46, 49, 52, 60, 70 };
	public final int[] PRAYER = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23,
			24, 25 };
	public final String[] PRAYER_NAME = { "Thick Skin", "Burst of Strength", "Clarity of Thought", "Sharp Eye",
			"Mystic Will", "Rock Skin", "Superhuman Strength", "Improved Reflexes", "Rapid Restore", "Rapid Heal",
			"Protect Item", "Hawk Eye", "Mystic Lore", "Steel Skin", "Ultimate Strength", "Incredible Reflexes",
			"Protect from Magic", "Protect from Missiles", "Protect from Melee", "Eagle Eye", "Mystic Might",
			"Retribution", "Redemption", "Smite", "Chivalry", "Piety" };
	public final int[] PRAYER_GLOW = { 83, 84, 85, 601, 602, 86, 87, 88, 89, 90, 91, 603, 604, 92, 93, 94, 95, 96, 97,
			605, 606, 98, 99, 100, 607, 608 };
	public final int[] PRAYER_HEAD_ICONS = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 2, 1, 0,
			-1, -1, 3, 5, 4, -1, -1 };
	// {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,-1,3,2,1,4,6,5};

	public boolean[] prayerActive = { false, false, false, false, false, false, false, false, false, false, false,
			false, false, false, false, false, false, false, false, false, false, false, false, false, false, false };

	public int duelTimer, duelTeleX, duelTeleY, duelSlot, duelSpaceReq, duelOption, duelingWith, duelStatus;
	public int headIconPk = -1, headIconHints;
	public boolean duelRequested;
	public boolean[] duelRule = new boolean[22];
	public final int[] DUEL_RULE_ID = { 1, 2, 16, 32, 64, 128, 256, 512, 1024, 4096, 8192, 16384, 32768, 65536, 131072,
			262144, 524288, 2097152, 8388608, 16777216, 67108864, 134217728 };

	public boolean doubleHit, usingSpecial, npcDroppingItems, usingRangeWeapon, usingBow, usingMagic, castingMagic;
	public int specMaxHitIncrease, freezeDelay, freezeTimer = -6, killerId, playerIndex, oldPlayerIndex, lastWeaponUsed,
			projectileStage, crystalBowArrowCount, playerMagicBook, teleGfx, teleEndAnimation, teleHeight, teleX, teleY,
			rangeItemUsed, killingNpcIndex, totalDamageDealt, oldNpcIndex, fightMode, attackTimer, npcIndex,
			npcClickIndex, npcType, castingSpellId, oldSpellId, spellId, hitDelay;
	public boolean magicFailed, oldMagicFailed;
	public int bowSpecShot, clickNpcType, clickObjectType, objectId, objectX, objectY, objectXOffset, objectYOffset,
			objectDistance;
	public int pItemX, pItemY, pItemId;
	public boolean isMoving, walkingToItem;
	public boolean isShopping, updateShop;
	public int myShopId;
	public int tradeStatus, tradeWith;
	public boolean forcedChatUpdateRequired, inDuel, tradeAccepted, goodTrade, inTrade, tradeRequested,
			tradeResetNeeded, tradeConfirmed, tradeConfirmed2, canOffer, acceptTrade, acceptedTrade;
	public int attackAnim, animationRequest = -1, animationWaitCycles;
	public int[] playerBonus = new int[12];
	public boolean isRunning2 = true;
	public boolean takeAsNote;
	public int combatLevel;
	public boolean saveFile = false;
	public int playerAppearance[] = new int[13];
	public int apset;
	public int actionID;
	public int wearItemTimer, wearId, wearSlot, interfaceId;
	public int XremoveSlot, XinterfaceID, XremoveID, Xamount;
	public boolean isAttackingGate = false;

	public boolean antiFirePot = false;

	/**
	 * Castle Wars
	 */
	public int castleWarsTeam;
	public boolean inCwGame;
	public boolean inCwWait;

	/**
	 * Fight Pits
	 */
	public boolean inPits = false;
	public int pitsStatus = 0;

	/**
	 * SouthWest, NorthEast, SouthWest, NorthEast
	 */

	public boolean isInTut() {
		if (absX >= 2625 && absX <= 2687 && absY >= 4670 && absY <= 4735) {
			return true;
		}
		return false;
	}

	public boolean inBarrows() {
		if (absX > 3520 && absX < 3598 && absY > 9653 && absY < 9750) {
			return true;
		}
		return false;
	}

	public boolean inArea(int x, int y, int x1, int y1) {
		if (absX > x && absX < x1 && absY < y && absY > y1) {
			return true;
		}
		return false;
	}

	public boolean inWild() {
		if (absX > 2941 && absX < 3392 && absY > 3518 && absY < 3966
				|| absX > 2941 && absX < 3392 && absY > 9918 && absY < 10366) {
			return true;
		}
		return false;
	}

	public boolean arenas() {
		if (absX > 3331 && absX < 3391 && absY > 3242 && absY < 3260) {
			return true;
		}
		return false;
	}

	public boolean inDuelArena() {
		if ((absX > 3322 && absX < 3394 && absY > 3195 && absY < 3291)
				|| (absX > 3311 && absX < 3323 && absY > 3223 && absY < 3248)) {
			return true;
		}
		return false;
	}

	public boolean inMulti() {
		if ((absX >= 3136 && absX <= 3327 && absY >= 3519 && absY <= 3607)
				|| (absX >= 3190 && absX <= 3327 && absY >= 3648 && absY <= 3839)
				|| (absX >= 3200 && absX <= 3390 && absY >= 3840 && absY <= 3967)
				|| (absX >= 2992 && absX <= 3007 && absY >= 3912 && absY <= 3967)
				|| (absX >= 2946 && absX <= 2959 && absY >= 3816 && absY <= 3831)
				|| (absX >= 3008 && absX <= 3199 && absY >= 3856 && absY <= 3903)
				|| (absX >= 3008 && absX <= 3071 && absY >= 3600 && absY <= 3711)
				|| (absX >= 3072 && absX <= 3327 && absY >= 3608 && absY <= 3647)
				|| (absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619)
				|| (absX >= 2371 && absX <= 2422 && absY >= 5062 && absY <= 5117)
				|| (absX >= 2896 && absX <= 2927 && absY >= 3595 && absY <= 3630)
				|| (absX >= 2892 && absX <= 2932 && absY >= 4435 && absY <= 4464)
				|| (absX >= 2256 && absX <= 2287 && absY >= 4680 && absY <= 4711)) {
			return true;
		}
		return false;
	}

	public boolean inFightCaves() {
		return absX >= 2360 && absX <= 2445 && absY >= 5045 && absY <= 5125;
	}

	public boolean inPirateHouse() {
		return absX >= 3038 && absX <= 3044 && absY >= 3949 && absY <= 3959;
	}

	public String connectedFrom = "";

	public int playerId = -1;
	public String playerName = null;
	public String playerName2 = null;
	public String playerPass = null;
	public int playerRights;
	public PlayerHandler handler = new PlayerHandler();
	public int playerItems[] = new int[28];
	public int playerItemsN[] = new int[28];
	public int bankItems[] = new int[Constants.BANK_SIZE];
	public int bankItemsN[] = new int[Constants.BANK_SIZE];
	public boolean bankNotes = false;

	public int playerStandIndex = 0x328;
	public int playerTurnIndex = 0x337;
	public int playerWalkIndex = 0x333;
	public int playerTurn180Index = 0x334;
	public int playerTurn90CWIndex = 0x335;
	public int playerTurn90CCWIndex = 0x336;
	public int playerRunIndex = 0x338;

	public int cwKills, cwDeaths, cwGames;

	@SuppressWarnings("static-access")
	public void updateshop(int i) {
		Player p = (Player) Server.playerHandler.players[playerId];
		p.getShops().resetShop(i);
	}

	public void println_debug(String str) {
		System.out.println("[player-" + playerId + "]: " + str);
	}

	public void println(String str) {
		System.out.println("[player-" + playerId + "]: " + str);
	}

	/**
	 * Animations
	 **/
	public void playAnimation(Animation animation) {
		animationRequest = animation.getId();
		animationWaitCycles = animation.getDelay();
		updateRequired = true;
	}

	public void playGraphic(Graphic graphic) {
		mask100var1 = graphic.getId();
		mask100var2 = graphic.getDelay() + (65536 * graphic.getHeight());
		mask100update = true;
		updateRequired = true;
	}

	public boolean inSafeZone() {
		if (this.inDuelArena()) {
			return true;
		}
		if (this.absX <= 3098 && this.absY <= 3499 && this.absX >= 3091 && this.absY >= 3488
				|| this.absX <= 3226 && this.absY <= 3228 && this.absX >= 3201 && this.absY >= 3209
				|| this.absX >= 3227 && this.absY <= 3219 && this.absX <= 3229 && this.absY >= 3217
				|| this.absX >= 3201 && this.absY >= 3229 && this.absX <= 3226 && this.absY <= 3236
				|| this.absX >= 3201 && this.absY >= 3201 && this.absX <= 3226 && this.absY <= 3208
				|| this.absX <= 2547 && this.absY >= 4708 && this.absX >= 2529 && this.absY <= 4725
				|| this.absX <= 2949 && this.absY <= 3369 && this.absX >= 2943 && this.absY >= 3368
				|| this.absX <= 2947 && this.absY >= 3369 && this.absX >= 2943 && this.absY <= 3373
				|| this.absX <= 2102 && this.absY <= 4475 && this.absX >= 2074 && this.absY >= 4455
				|| (absX >= 2800 && absX <= 2950 && absY >= 5200 && absY <= 5400)) {
			return true;
		}
		return false;
	}

	public Player(Channel s, int _playerId) {

		playerId = _playerId;
		playerRights = 0;

		for (int i = 0; i < playerItems.length; i++) {
			playerItems[i] = 0;
		}
		for (int i = 0; i < playerItemsN.length; i++) {
			playerItemsN[i] = 0;
		}

		for (int i = 0; i < playerLevel.length; i++) {
			if (i == 3) {
				playerLevel[i] = 10;
			} else {
				playerLevel[i] = 1;
			}
		}

		for (int i = 0; i < playerXP.length; i++) {
			if (i == 3) {
				playerXP[i] = 1300;
			} else {
				playerXP[i] = 0;
			}
		}
		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			bankItems[i] = 0;
		}

		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			bankItemsN[i] = 0;
		}

		playerAppearance[0] = 0; // gender
		playerAppearance[1] = 7; // head
		playerAppearance[2] = 25;// Torso
		playerAppearance[3] = 29; // arms
		playerAppearance[4] = 35; // hands
		playerAppearance[5] = 39; // legs
		playerAppearance[6] = 44; // feet
		playerAppearance[7] = 14; // beard
		playerAppearance[8] = 7; // hair colour
		playerAppearance[9] = 8; // torso colour
		playerAppearance[10] = 9; // legs colour
		playerAppearance[11] = 5; // feet colour
		playerAppearance[12] = 0; // skin colour

		apset = 0;
		actionID = 0;

		playerEquipment[EquipmentListener.HAT_SLOT.getSlot()] = -1;
		playerEquipment[EquipmentListener.CAPE_SLOT.getSlot()] = -1;
		playerEquipment[EquipmentListener.AMULET_SLOT.getSlot()] = -1;
		playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] = -1;
		playerEquipment[EquipmentListener.SHIELD_SLOT.getSlot()] = -1;
		playerEquipment[EquipmentListener.LEGS_SLOT.getSlot()] = -1;
		playerEquipment[EquipmentListener.GLOVES_SLOT.getSlot()] = -1;
		playerEquipment[EquipmentListener.BOOTS_SLOT.getSlot()] = -1;
		playerEquipment[EquipmentListener.RING_SLOT.getSlot()] = -1;
		playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] = -1;
		playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] = -1;

		heightLevel = 0;

		teleportToX = Constants.START_LOCATION_X;
		teleportToY = Constants.START_LOCATION_Y;

		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();

		session = s;
		outStream = new Stream(new byte[Constants.BUFFER_SIZE]);
		outStream.currentOffset = 0;

		inStream = new Stream(new byte[Constants.BUFFER_SIZE]);
		inStream.currentOffset = 0;
		buffer = new byte[Constants.BUFFER_SIZE];
	}

	public static final int maxPlayerListSize = Constants.MAX_PLAYERS;
	public Player playerList[] = new Player[maxPlayerListSize];
	public int playerListSize = 0;

	public byte playerInListBitmap[] = new byte[(Constants.MAX_PLAYERS + 7) >> 3];

	public static final int maxNPCListSize = NPCHandler.maxNPCs;
	public NPC npcList[] = new NPC[maxNPCListSize];
	public int npcListSize = 0;

	public byte npcInListBitmap[] = new byte[(NPCHandler.maxNPCs + 7) >> 3];

	public boolean withinDistance(Player otherPlr) {
		if (heightLevel != otherPlr.heightLevel)
			return false;
		int deltaX = otherPlr.absX - absX, deltaY = otherPlr.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public boolean withinDistance(NPC npc) {
		if (heightLevel != npc.heightLevel)
			return false;
		if (npc.needRespawn == true)
			return false;
		int deltaX = npc.absX - absX, deltaY = npc.absY - absY;
		return deltaX <= 15 && deltaX >= -16 && deltaY <= 15 && deltaY >= -16;
	}

	public int distanceToPoint(int pointX, int pointY) {
		return (int) Math.sqrt(Math.pow(absX - pointX, 2) + Math.pow(absY - pointY, 2));
	}

	public int mapRegionX, mapRegionY;
	public int absX, absY;
	public int currentX, currentY;

	public int heightLevel;
	public int playerSE = 0x328;
	public int playerSEW = 0x333;
	public int playerSER = 0x334;

	public boolean updateRequired = true;

	public final int walkingQueueSize = 50;
	public int walkingQueueX[] = new int[walkingQueueSize], walkingQueueY[] = new int[walkingQueueSize];
	public int wQueueReadPtr = 0;
	public int wQueueWritePtr = 0;
	public boolean isRunning = true;
	public int teleportToX = -1, teleportToY = -1;

	public void resetWalkingQueue() {
		wQueueReadPtr = wQueueWritePtr = 0;

		for (int i = 0; i < walkingQueueSize; i++) {
			walkingQueueX[i] = currentX;
			walkingQueueY[i] = currentY;
		}
	}

	public void addToWalkingQueue(int x, int y) {
		// if (VirtualWorld.I(heightLevel, absX, absY, x, y, 0)) {
		int next = (wQueueWritePtr + 1) % walkingQueueSize;
		if (next == wQueueWritePtr)
			return;
		walkingQueueX[wQueueWritePtr] = x;
		walkingQueueY[wQueueWritePtr] = y;
		wQueueWritePtr = next;
		// }
	}

	public boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX - playerX <= distance && objectX - playerX >= -distance)
				&& (objectY - playerY <= distance && objectY - playerY >= -distance));
	}

	// public boolean goodDistance(int objectX, int objectY, int playerX, int
	// playerY, int distance) {
	// for (int i = 0; i <= distance; i++) {
	// for (int j = 0; j <= distance; j++) {
	// if ((objectX + i) == playerX
	// && ((objectY + j) == playerY || (objectY - j) == playerY || objectY ==
	// playerY)) {
	// return true;
	// } else if ((objectX - i) == playerX
	// && ((objectY + j) == playerY || (objectY - j) == playerY || objectY ==
	// playerY)) {
	// return true;
	// } else if (objectX == playerX
	// && ((objectY + j) == playerY || (objectY - j) == playerY || objectY ==
	// playerY)) {
	// return true;
	// }
	// }
	// }
	// return false;
	// }
	public long toleranceTimer;

	public int getNextWalkingDirection() {
		if (wQueueReadPtr == wQueueWritePtr)
			return -1;
		int dir;
		do {
			dir = Misc.direction(currentX, currentY, walkingQueueX[wQueueReadPtr], walkingQueueY[wQueueReadPtr]);
			if (dir == -1) {
				wQueueReadPtr = (wQueueReadPtr + 1) % walkingQueueSize;
			} else if ((dir & 1) != 0) {
				println_debug("Invalid waypoint in walking queue!");
				resetWalkingQueue();
				return -1;
			}
		} while ((dir == -1) && (wQueueReadPtr != wQueueWritePtr));
		if (dir == -1)
			return -1;
		dir >>= 1;
		currentX += Misc.directionDeltaX[dir];
		currentY += Misc.directionDeltaY[dir];
		absX += Misc.directionDeltaX[dir];
		absY += Misc.directionDeltaY[dir];
		return dir;
	}

	public boolean zombieSpawned;
	public boolean treeSpawned;
	public boolean golemSpawned;
	public boolean trollSpawned;
	public int nextChat = 0;
	public boolean didTeleport = false;
	public boolean mapRegionDidChange = false;
	public int dir1 = -1, dir2 = -1;
	public boolean createItems = false;
	public int poimiX = 0, poimiY = 0;

	public byte buffer[] = null;
	public Stream inStream = null, outStream = null;
	protected Channel session;
	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private ShopAssistant shopAssistant = new ShopAssistant(this);
	private TradeAndDuel tradeAndDuel = new TradeAndDuel(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private CombatAssistant combatAssistant = new CombatAssistant(this);
	private ActionHandler actionHandler = new ActionHandler();
	private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Queue<Packet> queuedPackets = new LinkedList<Packet>();

	private Sounds sounds = new Sounds(this);

	public Sounds getSound() {
		return sounds;
	}

	public Stream getInStream() {
		return inStream;
	}

	public int getPacketType() {
		return packetType;
	}

	public int getPacketSize() {
		return packetSize;
	}

	public Stream getOutStream() {
		return outStream;
	}

	public ItemAssistant getItems() {
		return itemAssistant;
	}

	public PlayerAssistant getPA() {
		return playerAssistant;
	}

	public DialogueHandler getDH() {
		return dialogueHandler;
	}

	public ShopAssistant getShops() {
		return shopAssistant;
	}

	public TradeAndDuel getTradeAndDuel() {
		return tradeAndDuel;
	}

	public CombatAssistant getCombat() {
		return combatAssistant;
	}

	public ActionHandler getActions() {
		return actionHandler;
	}

	public Channel getSession() {
		return session;
	}

	public PlayerAssistant getPlayerAssistant() {
		return playerAssistant;
	}

	public void queueMessage(Packet packet) {
		synchronized (queuedPackets) {
			queuedPackets.add(packet);
		}
	}

	public boolean processQueuedPackets() {
		synchronized (queuedPackets) {
			Packet packet = null;
			while ((packet = queuedPackets.poll()) != null) {
				inStream.currentOffset = 0;
				packetType = packet.getOpcode();
				packetSize = packet.getLength();
				inStream.buffer = packet.getPayload().array();
				if (packetType > 0) {
					PacketHandler.processPacket(this, packetType, packetSize);
				}
			}
		}
		return true;
	}

	public DwarfMultiCannon cannon = new DwarfMultiCannon(this);

	public DwarfMultiCannon getCannon() {
		return cannon;
	}

	public boolean shooting;

	private final CannonCoords cannonCoords = new CannonCoords(this);

	public CannonCoords getCannonCoords() {
		return cannonCoords;
	}

	private Membership members = new Membership();

	public Membership membership() {
		return members;
	}

	private Attributes attributes = new Attributes();

	public Attributes getAttributes() {
		return attributes;
	}

	private NpcDialogue dialogue = null;

	public NpcDialogue getDialogue() {
		return dialogue;
	}

	public synchronized void getNextPlayerMovement() {
		mapRegionDidChange = false;
		didTeleport = false;
		dir1 = dir2 = -1;

		if (teleportToX != -1 && teleportToY != -1) {
			mapRegionDidChange = true;
			if (mapRegionX != -1 && mapRegionY != -1) {
				int relX = teleportToX - mapRegionX * 8, relY = teleportToY - mapRegionY * 8;
				if (relX >= 2 * 8 && relX < 11 * 8 && relY >= 2 * 8 && relY < 11 * 8)
					mapRegionDidChange = false;
			}
			if (mapRegionDidChange) {
				mapRegionX = (teleportToX >> 3) - 6;
				mapRegionY = (teleportToY >> 3) - 6;
			}
			currentX = teleportToX - 8 * mapRegionX;
			currentY = teleportToY - 8 * mapRegionY;
			absX = teleportToX;
			absY = teleportToY;
			resetWalkingQueue();

			teleportToX = teleportToY = -1;
			didTeleport = true;
		} else {
			dir1 = getNextWalkingDirection();
			if (dir1 == -1)
				return;
			if (isRunning2) {
				dir2 = getNextWalkingDirection();
				if (runEnergy > 0) {
					squaresRan++;
					if (squaresRan == Constants.RUN_SQUARE_DECREASE) {
						runEnergy--;
						Player c = (Player) PlayerHandler.players[playerId];
						c.getPA().sendFrame126(runEnergy + "%", 149);
						squaresRan = 0;
						if (runEnergy == 0) {
							isRunning = false;
							isRunning2 = false;
							c.getPA().sendFrame36(173, 0);
						}
					}
				}
			}
			@SuppressWarnings("unused")
			Player c = (Player) this;
			// c.sendMessage("Cycle Ended");
			int deltaX = 0, deltaY = 0;
			if (currentX < 2 * 8) {
				deltaX = 4 * 8;
				mapRegionX -= 4;
				mapRegionDidChange = true;
			} else if (currentX >= 11 * 8) {
				deltaX = -4 * 8;
				mapRegionX += 4;
				mapRegionDidChange = true;
			}
			if (currentY < 2 * 8) {
				deltaY = 4 * 8;
				mapRegionY -= 4;
				mapRegionDidChange = true;
			} else if (currentY >= 11 * 8) {
				deltaY = -4 * 8;
				mapRegionY += 4;
				mapRegionDidChange = true;
			}

			if (mapRegionDidChange/*
									 * && VirtualWorld.I(heightLevel, currentX,
									 * currentY, currentX + deltaX, currentY +
									 * deltaY, 0)
									 */) {
				currentX += deltaX;
				currentY += deltaY;
				for (int i = 0; i < walkingQueueSize; i++) {
					walkingQueueX[i] += deltaX;
					walkingQueueY[i] += deltaY;
				}
			}
			// CoordAssistant.processCoords(this);

		}
	}

	public void updateThisPlayerMovement(Stream str) {
		// synchronized(this) {
		if (mapRegionDidChange) {
			str.createFrame(73);
			str.writeWordA(mapRegionX + 6);
			str.writeWord(mapRegionY + 6);
		}

		if (didTeleport) {
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);
			str.writeBits(2, 3);
			str.writeBits(2, heightLevel);
			str.writeBits(1, 1);
			str.writeBits(1, (updateRequired) ? 1 : 0);
			str.writeBits(7, currentY);
			str.writeBits(7, currentX);
			return;
		}

		if (dir1 == -1) {
			// don't have to update the character position, because we're just
			// standing
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			isMoving = false;
			if (updateRequired) {
				// tell Player there's an update block appended at the end
				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else {
				str.writeBits(1, 0);
			}
			if (DirectionCount < 50) {
				DirectionCount++;
			}
		} else {
			DirectionCount = 0;
			str.createFrameVarSizeWord(81);
			str.initBitAccess();
			str.writeBits(1, 1);

			if (dir2 == -1) {
				isMoving = true;
				str.writeBits(2, 1);
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				if (updateRequired)
					str.writeBits(1, 1);
				else
					str.writeBits(1, 0);
			} else {
				isMoving = true;
				str.writeBits(2, 2);
				str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
				str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
				if (updateRequired)
					str.writeBits(1, 1);
				else
					str.writeBits(1, 0);
			}
		}

	}

	public void updatePlayerMovement(Stream str) {
		// synchronized(this) {
		if (dir1 == -1) {
			if (updateRequired || isChatTextUpdateRequired()) {

				str.writeBits(1, 1);
				str.writeBits(2, 0);
			} else
				str.writeBits(1, 0);
		} else if (dir2 == -1) {

			str.writeBits(1, 1);
			str.writeBits(2, 1);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1 : 0);
		} else {

			str.writeBits(1, 1);
			str.writeBits(2, 2);
			str.writeBits(3, Misc.xlateDirectionToClient[dir1]);
			str.writeBits(3, Misc.xlateDirectionToClient[dir2]);
			str.writeBits(1, (updateRequired || isChatTextUpdateRequired()) ? 1 : 0);
		}

	}

	public byte cachedPropertiesBitmap[] = new byte[(Constants.MAX_PLAYERS + 7) >> 3];

	public void addNewNPC(NPC npc, Stream str, Stream updateBlock) {
		// synchronized(this) {
		int id = npc.npcId;
		npcInListBitmap[id >> 3] |= 1 << (id & 7);
		npcList[npcListSize++] = npc;

		str.writeBits(14, id);

		int z = npc.absY - absY;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
		z = npc.absX - absX;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);

		str.writeBits(1, 0);
		str.writeBits(12, npc.npcType);

		boolean savedUpdateRequired = npc.updateRequired;
		npc.updateRequired = true;
		npc.appendNPCUpdateBlock(updateBlock);
		npc.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
	}

	public void addNewPlayer(Player plr, Stream str, Stream updateBlock) {
		// synchronized(this) {
		if (playerListSize >= 255) {
			return;
		}
		int id = plr.playerId;
		playerInListBitmap[id >> 3] |= 1 << (id & 7);
		playerList[playerListSize++] = plr;
		str.writeBits(11, id);
		str.writeBits(1, 1);
		boolean savedFlag = plr.isAppearanceUpdateRequired();
		boolean savedUpdateRequired = plr.updateRequired;
		plr.setAppearanceUpdateRequired(true);
		plr.updateRequired = true;
		plr.appendPlayerUpdateBlock(updateBlock);
		plr.setAppearanceUpdateRequired(savedFlag);
		plr.updateRequired = savedUpdateRequired;
		str.writeBits(1, 1);
		int z = plr.absY - absY;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
		z = plr.absX - absX;
		if (z < 0)
			z += 32;
		str.writeBits(5, z);
	}

	protected static Stream playerProps;

	static {
		playerProps = new Stream(new byte[100]);
	}

	protected void appendPlayerAppearance(Stream str) {
		// synchronized(this) {
		playerProps.currentOffset = 0;

		playerProps.writeByte(playerAppearance[0]);

		playerProps.writeByte(headIcon);
		playerProps.writeByte(headIconPk);
		// playerProps.writeByte(headIconHints);
		// playerProps.writeByte(bountyIcon);

		if (playerEquipment[EquipmentListener.HAT_SLOT.getSlot()] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[EquipmentListener.HAT_SLOT.getSlot()]);
		} else {
			playerProps.writeByte(0);
		}

		if (playerEquipment[EquipmentListener.CAPE_SLOT.getSlot()] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[EquipmentListener.CAPE_SLOT.getSlot()]);
		} else {
			playerProps.writeByte(0);
		}

		if (playerEquipment[EquipmentListener.AMULET_SLOT.getSlot()] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[EquipmentListener.AMULET_SLOT.getSlot()]);
		} else {
			playerProps.writeByte(0);
		}

		if (playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]);
		} else {
			playerProps.writeByte(0);
		}

		if (playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()]);
		} else {
			playerProps.writeWord(0x100 + playerAppearance[2]);
		}

		if (playerEquipment[EquipmentListener.SHIELD_SLOT.getSlot()] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[EquipmentListener.SHIELD_SLOT.getSlot()]);
		} else {
			playerProps.writeByte(0);
		}

		if (!Item.isFullBody(playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()])) {
			playerProps.writeWord(0x100 + playerAppearance[3]);
		} else {
			playerProps.writeByte(0);
		}

		if (playerEquipment[EquipmentListener.LEGS_SLOT.getSlot()] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[EquipmentListener.LEGS_SLOT.getSlot()]);
		} else {
			playerProps.writeWord(0x100 + playerAppearance[5]);
		}

		if (!Item.isFullHelm(playerEquipment[EquipmentListener.HAT_SLOT.getSlot()])
				&& !Item.isFullMask(playerEquipment[EquipmentListener.HAT_SLOT.getSlot()])) {
			playerProps.writeWord(0x100 + playerAppearance[1]);
		} else {
			playerProps.writeByte(0);
		}

		if (playerEquipment[EquipmentListener.GLOVES_SLOT.getSlot()] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[EquipmentListener.GLOVES_SLOT.getSlot()]);
		} else {
			playerProps.writeWord(0x100 + playerAppearance[4]);
		}

		if (playerEquipment[EquipmentListener.BOOTS_SLOT.getSlot()] > 1) {
			playerProps.writeWord(0x200 + playerEquipment[EquipmentListener.BOOTS_SLOT.getSlot()]);
		} else {
			playerProps.writeWord(0x100 + playerAppearance[6]);
		}

		if (playerAppearance[0] != 1 && !Item.isFullMask(playerEquipment[EquipmentListener.HAT_SLOT.getSlot()])) {
			playerProps.writeWord(0x100 + playerAppearance[7]);
		} else {
			playerProps.writeByte(0);
		}

		playerProps.writeByte(playerAppearance[8]);
		playerProps.writeByte(playerAppearance[9]);
		playerProps.writeByte(playerAppearance[10]);
		playerProps.writeByte(playerAppearance[11]);
		playerProps.writeByte(playerAppearance[12]);
		playerProps.writeWord(playerStandIndex); // standAnimIndex
		playerProps.writeWord(playerTurnIndex); // standTurnAnimIndex
		playerProps.writeWord(playerWalkIndex); // walkAnimIndex
		playerProps.writeWord(playerTurn180Index); // turn180AnimIndex
		playerProps.writeWord(playerTurn90CWIndex); // turn90CWAnimIndex
		playerProps.writeWord(playerTurn90CCWIndex); // turn90CCWAnimIndex
		playerProps.writeWord(playerRunIndex); // runAnimIndex

		playerProps.writeQWord(Misc.playerNameToInt64(playerName));

		int mag = (int) ((getLevelForXP(playerXP[6])) * 1.5);
		int ran = (int) ((getLevelForXP(playerXP[4])) * 1.5);
		int attstr = (int) ((double) (getLevelForXP(playerXP[0])) + (double) (getLevelForXP(playerXP[2])));

		combatLevel = 0;
		if (ran > attstr) {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[4])) * 0.4875));
		} else if (mag > attstr) {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[6])) * 0.4875));
		} else {
			combatLevel = (int) (((getLevelForXP(playerXP[1])) * 0.25) + ((getLevelForXP(playerXP[3])) * 0.25)
					+ ((getLevelForXP(playerXP[5])) * 0.125) + ((getLevelForXP(playerXP[0])) * 0.325)
					+ ((getLevelForXP(playerXP[2])) * 0.325));
		}
		playerProps.writeByte(combatLevel); // combat level
		playerProps.writeWord(0);
		str.writeByteC(playerProps.currentOffset);
		str.writeBytes(playerProps.buffer, playerProps.currentOffset, 0);
	}

	public boolean fightForOnyx = false;

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp)
				return lvl;
		}
		return 99;
	}

	private boolean chatTextUpdateRequired = false;
	private byte chatText[] = new byte[4096];
	private byte chatTextSize = 0;
	private int chatTextColor = 0;
	private int chatTextEffects = 0;

	protected void appendPlayerChatText(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian(((getChatTextColor() & 0xFF) << 8) + (getChatTextEffects() & 0xFF));
		str.writeByte(playerRights);
		str.writeByteC(getChatTextSize());
		str.writeBytes_reverse(getChatText(), getChatTextSize(), 0);

	}

	public void forcedChat(String text) {
		forcedText = text;
		forcedChatUpdateRequired = true;
		updateRequired = true;
		setAppearanceUpdateRequired(true);
	}

	public String forcedText = "null";

	public void appendForcedChat(Stream str) {
		// synchronized(this) {
		str.writeString(forcedText);
	}

	/**
	 * Graphics
	 **/

	public int mask100var1 = 0;
	public int mask100var2 = 0;
	protected boolean mask100update = false;

	public void appendMask100Update(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian(mask100var1);
		str.writeDWord(mask100var2);

	}

	public void gfx100(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 6553600;
		mask100update = true;
		updateRequired = true;
	}

	public void gfx0(int gfx) {
		mask100var1 = gfx;
		mask100var2 = 65536;
		mask100update = true;
		updateRequired = true;
	}

	public boolean wearing2h() {
		Player c = (Player) this;
		String name = c.getItems().getItemName(c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]);
		if (name.contains("2h"))
			return true;
		else if (name.contains("godsword"))
			return true;
		return false;
	}

	/**
	 * Animations
	 **/
	public void startAnimation(int animId) {
		if (wearing2h() && animId == 829)
			return;
		animationRequest = animId;
		animationWaitCycles = 0;
		updateRequired = true;
	}

	public void startAnimation(int animId, int time) {
		animationRequest = animId;
		animationWaitCycles = time;
		updateRequired = true;
	}

	public void appendAnimationRequest(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian((animationRequest == -1) ? 65535 : animationRequest);
		str.writeByteC(animationWaitCycles);

	}

	/**
	 * Face Update
	 **/

	private boolean faceUpdateRequired = false;
	private int face = -1;
	private int FocusPointX = -1, FocusPointY = -1;

	public void faceUpdate(int index) {
		face = index;
		faceUpdateRequired = true;
		updateRequired = true;
	}

	public void appendFaceUpdate(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndian(face);

	}

	public void turnPlayerTo(int pointX, int pointY) {
		FocusPointX = 2 * pointX + 1;
		FocusPointY = 2 * pointY + 1;
		updateRequired = true;
	}

	private void appendSetFocusDestination(Stream str) {
		// synchronized(this) {
		str.writeWordBigEndianA(FocusPointX);
		str.writeWordBigEndian(FocusPointY);

	}

	/**
	 * Hit Update
	 **/

	protected void appendHitUpdate(Stream str) {
		// synchronized(this) {
		str.writeByte(getHitDiff()); // What the perseon got 'hit' for
		if (poisonMask == 1) {
			str.writeByteA(2);
		} else if (getHitDiff() > 0) {
			str.writeByteA(1); // 0: red hitting - 1: blue hitting
		} else {
			str.writeByteA(0); // 0: red hitting - 1: blue hitting
		}
		if (playerLevel[3] <= 0) {
			playerLevel[3] = 0;
			isDead = true;
		}
		str.writeByteC(playerLevel[3]); // Their current hp, for HP bar
		str.writeByte(getLevelForXP(playerXP[3])); // Their max hp, for HP bar

	}

	protected void appendHitUpdate2(Stream str) {
		// synchronized(this) {
		str.writeByte(hitDiff2); // What the perseon got 'hit' for
		if (poisonMask == 2) {
			str.writeByteS(2);
			poisonMask = -1;
		} else if (hitDiff2 > 0) {
			str.writeByteS(1); // 0: red hitting - 1: blue hitting
		} else {
			str.writeByteS(0); // 0: red hitting - 1: blue hitting
		}
		if (playerLevel[3] <= 0) {
			playerLevel[3] = 0;
			isDead = true;
		}
		str.writeByte(playerLevel[3]); // Their current hp, for HP bar
		str.writeByteC(getLevelForXP(playerXP[3])); // Their max hp, for HP bar

	}

	public void appendPlayerUpdateBlock(Stream str) {
		// synchronized(this) {
		if (!updateRequired && !isChatTextUpdateRequired())
			return; // nothing required
		int updateMask = 0;
		if (forceMovementUpdateRequired) {
			updateMask |= 0x400;
		}
		if (mask100update) {
			updateMask |= 0x100;
		}
		if (animationRequest != -1) {
			updateMask |= 8;
		}
		if (forcedChatUpdateRequired) {
			updateMask |= 4;
		}
		if (isChatTextUpdateRequired()) {
			updateMask |= 0x80;
		}
		if (isAppearanceUpdateRequired()) {
			updateMask |= 0x10;
		}
		if (faceUpdateRequired) {
			updateMask |= 1;
		}
		if (FocusPointX != -1) {
			updateMask |= 2;
		}
		if (isHitUpdateRequired()) {
			updateMask |= 0x20;
		}

		if (hitUpdateRequired2) {
			updateMask |= 0x200;
		}

		if (updateMask >= 0x100) {
			updateMask |= 0x40;
			str.writeByte(updateMask & 0xFF);
			str.writeByte(updateMask >> 8);
		} else {
			str.writeByte(updateMask);
		}

		if (forceMovementUpdateRequired) {
			appendMask400Update(str);
		}
		// now writing the various update blocks itself - note that their order
		// crucial
		if (mask100update) {
			appendMask100Update(str);
		}
		if (animationRequest != -1) {
			appendAnimationRequest(str);
		}
		if (forcedChatUpdateRequired) {
			appendForcedChat(str);
		}
		if (isChatTextUpdateRequired()) {
			appendPlayerChatText(str);
		}
		if (faceUpdateRequired) {
			appendFaceUpdate(str);
		}
		if (isAppearanceUpdateRequired()) {
			appendPlayerAppearance(str);
		}
		if (FocusPointX != -1) {
			appendSetFocusDestination(str);
		}
		if (isHitUpdateRequired()) {
			appendHitUpdate(str);
		}
		if (hitUpdateRequired2) {
			appendHitUpdate2(str);
		}

	}

	public boolean forceMovementUpdateRequired = false;
	private int x1 = -1;
	private int y1 = -1;
	private int x2 = -1;
	private int y2 = -1;
	private int speed1 = -1;
	private int speed2 = -1;
	private int direction = -1;

	/**
	 * TODO: Fix bug where when you finish route and click away you restart from
	 * Original tile and walk to the tile selected after event.
	 * 
	 * @param x2
	 * @param y2
	 * @param x1
	 * @param y1
	 * @param speed1
	 * @param speed2
	 * @param direction
	 * @param emote
	 */
	public void setForceMovement(final int x2, final int y2, boolean x1, boolean y1, final int speed1, final int speed2,
			final int direction, final int emote) {
		canWalk = false;
		this.x1 = currentX;
		this.y1 = currentY;
		this.x2 = x1 ? currentX + x2 : currentX - x2;
		this.y2 = y1 ? currentY + y2 : currentY - y2;
		this.speed1 = speed1;
		this.speed2 = speed2;
		this.direction = direction;
		updateRequired = true;
		forceMovementUpdateRequired = true;
		final Player c = (Player) this;
		CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				c.getCombat().getPlayerAnimIndex(c.getItems()
						.getItemName(playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
				c.getPA().walkTo(x2, y2);

				updateRequired = true;
				forceMovementUpdateRequired = false;
				canWalk = true;
				c.getPA().requestUpdates();
				// TODO: add a reset movement after event is done (when u
				// finished being force moved, and click the tile you're under
				// the character will run back to original tile to force
				// movement tile)
				container.stop();
			}

			@Override
			public void stop() {
				c.teleporting = false;
				resetWalkingQueue();
			}
		}, (x2 + y2) * 600);
	}

	public boolean canWalk = true;

	public void appendMask400Update(Stream str) {
		str.writeByteS(x1);
		str.writeByteS(y1);
		str.writeByteS(x2);
		str.writeByteS(y2);
		str.writeWordBigEndianA(speed1);
		str.writeWordA(speed2);
		str.writeByteS(direction);
	}

	public void clearUpdateFlags() {
		forceMovementUpdateRequired = false;
		updateRequired = false;
		setChatTextUpdateRequired(false);
		setAppearanceUpdateRequired(false);
		setHitUpdateRequired(false);
		hitUpdateRequired2 = false;
		forcedChatUpdateRequired = false;
		mask100update = false;
		animationRequest = -1;
		focusPointX = -1;
		focusPointY = -1;
		poisonMask = -1;
		faceUpdateRequired = false;
		face = 65535;
	}

	public void stopMovement() {
		if (teleportToX <= 0 && teleportToY <= 0) {
			teleportToX = absX;
			teleportToY = absY;
		}
		newWalkCmdSteps = 0;
		getNewWalkCmdX()[0] = getNewWalkCmdY()[0] = travelBackX[0] = travelBackY[0] = 0;
		getNextPlayerMovement();
	}

	private int newWalkCmdX[] = new int[walkingQueueSize];
	private int newWalkCmdY[] = new int[walkingQueueSize];
	public int newWalkCmdSteps = 0;
	private boolean newWalkCmdIsRunning = false;
	private int travelBackX[] = new int[walkingQueueSize];
	private int travelBackY[] = new int[walkingQueueSize];
	private int numTravelBackSteps = 0;

	public void preProcessing() {
		newWalkCmdSteps = 0;
	}

	public void process() {
		if (System.currentTimeMillis() - specDelay > Constants.INCREASE_SPECIAL_AMOUNT) {
			specDelay = System.currentTimeMillis();
			if (specAmount < 10) {
				specAmount += .5;
				if (specAmount > 10)
					specAmount = 10;
				getItems().addSpecialBar(playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]);
			}
		}
		if ((runEnergy < 100 && ((!isRunning && !isRunning2) || !isMoving))
				&& System.currentTimeMillis() - runEnergyTime > Constants.RUN_ENERGY_GAIN) {
			runEnergyTime = System.currentTimeMillis();
			runEnergy++;
			getPA().sendFrame126(runEnergy + "%", 149);
		}
		if (followId > EntityType.PLAYER.getEntityValue()) {
			getPA().followPlayer();
		} else if (followId2 > EntityType.NPC.getEntityValue()) {
			getPA().followNpc();
		}

		if (System.currentTimeMillis() - singleCombatDelay > 3300) {
			underAttackBy = 0;
		}
		if (System.currentTimeMillis() - singleCombatDelay2 > 3300) {
			underAttackBy2 = 0;
		}

		if (System.currentTimeMillis() - restoreStatsDelay > 60000) {
			restoreStatsDelay = System.currentTimeMillis();
			for (int level = 0; level < playerLevel.length; level++) {
				if (playerLevel[level] < getLevelForXP(playerXP[level])) {
					if (level != 5) { // prayer doesn't restore
						playerLevel[level] += 1;
						getPA().setSkillLevel(level, playerLevel[level], playerXP[level]);
						getPA().refreshSkill(level);
					}
				} else if (playerLevel[level] > getLevelForXP(playerXP[level])) {
					playerLevel[level] -= 1;
					getPA().setSkillLevel(level, playerLevel[level], playerXP[level]);
					getPA().refreshSkill(level);
				}
			}
		}

		if (inWild()) {
			int modY = absY > 6400 ? absY - 6400 : absY;
			wildLevel = (((modY - 3520) / 8) + 1);
			getPA().walkableInterface(197);
			if (Constants.SINGLE_AND_MULTI_ZONES) {
				if (inMulti()) {
					getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
				} else {
					getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
				}
			} else {
				getPA().multiWay(-1);
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			}
			getPA().showOption(3, 0, "Attack", 1);
		} else {
			getPA().sendFrame126(" ", 199);
		}
		if (inDuelArena()) {
			getPA().walkableInterface(201);
			if (duelStatus == 5) {
				getPA().showOption(3, 0, "Attack", 1);
			} else {
				getPA().showOption(3, 0, "Challenge", 1);
			}
		} else if (inBarrows()) {
			getPA().sendFrame99(2);
			getPA().sendFrame126("Kill Count: " + barrowsKillCount, 4536);
			getPA().walkableInterface(4535);
		} else if (inCwGame || inPits) {
			getPA().showOption(3, 0, "Attack", 1);
		}
		if (CastleWars.isInCw(this) || inPits) {
			getPA().showOption(3, 0, "Attack", 1);
		} else if (!inDuelArena() && !CastleWars.isInCw(this) && !CastleWars.isInCwWait(this) && !inWild()
				&& !getPA().inPitsWait() && !inBarrows()) {
			getPA().walkableInterface(-1);
			getPA().sendFrame99(0);
			getPA().showOption(3, 0, "Null", 1);
		}
		if (!hasMultiSign && inMulti()) {
			hasMultiSign = true;
			getPA().multiWay(1);
		}

		if (hasMultiSign && !inMulti()) {
			hasMultiSign = false;
			getPA().multiWay(-1);
		}

		if (skullTimer > 0) {
			skullTimer--;
			if (skullTimer == 1) {
				isSkulled = false;
				attackedPlayers.clear();
				headIconPk = -1;
				skullTimer = -1;
				getPA().requestUpdates();
			}
		}

		if (isDead && respawnTimer == -6) {
			getPA().applyDead();
		}

		if (respawnTimer == 7) {
			respawnTimer = -6;
			getPA().giveLife();
		} else if (respawnTimer == 12) {
			respawnTimer--;
			startAnimation(0x900);
			poisonDamage = -1;
		}

		if (respawnTimer > -6) {
			respawnTimer--;
		}
		if (freezeTimer > -6) {
			freezeTimer--;
			if (frozenBy > 0) {
				if (Server.playerHandler.players[frozenBy] == null) {
					freezeTimer = -1;
					frozenBy = -1;
				} else if (!goodDistance(absX, absY, Server.playerHandler.players[frozenBy].absX,
						Server.playerHandler.players[frozenBy].absY, 20)) {
					freezeTimer = -1;
					frozenBy = -1;
				}
			}
		}

		if (hitDelay > 0) {
			hitDelay--;
		}

		if (teleTimer > 0) {
			teleTimer--;
			if (!isDead) {
				if (teleTimer == 1 && newLocation > 0) {
					teleTimer = 0;
					getPA().changeLocation();
				}
				if (teleTimer == 5) {
					teleTimer--;
					getPA().processTeleport();
				}
				if (teleTimer == 9 && teleGfx > 0) {
					teleTimer--;
					gfx100(teleGfx);
				}
			} else {
				teleTimer = 0;
			}
		}

		if (hitDelay == 1) {
			if (oldNpcIndex > 0) {
				getCombat().delayedHit(oldNpcIndex);
			}
			if (oldPlayerIndex > 0) {
				getCombat().playerDelayedHit(oldPlayerIndex);
			}
		}

		if (attackTimer > 0) {
			attackTimer--;
		}

		if (attackTimer == 1) {
			if (npcIndex > 0 && clickNpcType == 0) {
				getCombat().attackNpc(npcIndex);
			}
			if (playerIndex > 0) {
				getCombat().attackPlayer(playerIndex);
			}
		} else if (attackTimer <= 0 && (npcIndex > 0 || playerIndex > 0)) {
			if (npcIndex > 0) {
				attackTimer = 0;
				getCombat().attackNpc(npcIndex);
			} else if (playerIndex > 0) {
				attackTimer = 0;
				getCombat().attackPlayer(playerIndex);
			}
		}

		if (inTrade && tradeResetNeeded) {
			Player o = (Player) Server.playerHandler.players[tradeWith];
			if (o != null) {
				if (o.tradeResetNeeded) {
					getTradeAndDuel().resetTrade();
					o.getTradeAndDuel().resetTrade();
				}
			}
		}
		Plugin.execute("process", this);
	}

	public boolean updateItems = false;

	public void flushOutStream() {
		if (!session.isConnected() || disconnected || outStream.currentOffset == 0)
			return;

		byte[] temp = new byte[outStream.currentOffset];
		System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
		Packet packet = new Packet(-1, Type.FIXED, ChannelBuffers.wrappedBuffer(temp));
		session.write(packet);
		outStream.currentOffset = 0;

	}

	public void destruct() {
		if (session == null)
			return;
		Server.panel.removeEntity(playerName);
		if (CastleWars.isInCwWait(this)) {
			CastleWars.leaveWaitingRoom(this);
		}
		if (CastleWars.isInCw(this)) {
			CastleWars.removePlayerFromCw(this);
		}
		if (inPits) {
			Server.fightPits.removePlayerFromPits(playerId);
		}
		Misc.println("[DESERIALIZED]: " + playerName);
		CycleEventHandler.getSingleton().stopEvents(this);
		disconnected = true;
		session.close();
		session = null;
		inStream = null;
		outStream = null;
		isActive = false;
		buffer = null;

		playerListSize = 0;
		for (int i = 0; i < maxPlayerListSize; i++)
			playerList[i] = null;
		absX = absY = -1;
		mapRegionX = mapRegionY = -1;
		currentX = currentY = 0;
		resetWalkingQueue();
	}

	public void sendMessage(String s) {
		if (getOutStream() != null) {
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.endFrameVarSize();
		}

	}

	public void setSidebarInterface(int menuId, int form) {
		if (getOutStream() != null) {
			outStream.createFrame(71);
			outStream.writeWord(form);
			outStream.writeByteA(menuId);
		}

	}

	@SuppressWarnings("static-access")
	public void initialize() {
		outStream.createFrame(249);
		outStream.writeByteA(1); // 1 for members, zero for free
		outStream.writeWordBigEndianA(playerId);
		for (int player = 0; player < Server.playerHandler.players.length; player++) {
			if (player == playerId)
				continue;
			if (Server.playerHandler.players[player] != null) {
				if (Server.playerHandler.players[player].playerName.equalsIgnoreCase(playerName))
					disconnected = true;
			}
		}
		for (int skill = 0; skill < 21; skill++) {
			getPA().setSkillLevel(skill, playerLevel[skill], playerXP[skill]);
			getPA().refreshSkill(skill);
		}
		// IntStream.range(0, 25).forEach(skill -> //lambda expression
		// {
		// getPA().setSkillLevel(skill, playerLevel[skill], playerXP[skill]);
		// getPA().refreshSkill(skill);
		// });
		for (int prayerId = 0; prayerId < PRAYER.length; prayerId++) {
			prayerActive[prayerId] = false;
			getPA().sendFrame36(PRAYER_GLOW[prayerId], 0);
		}

		isRunning2 = !isRunning2;
		int off = isRunning2 == false ? 0 : 1;
		getPA().sendFrame36(173, off);

		runEnergyTime = System.currentTimeMillis();
		getPA().sendFrame126(runEnergy + "%", 149);
		getPA().sendFrame126("10m kg", 184);
		getPA().sendFrame126("QP: " + questPoints, 3985);

		// getPA().sendFrame36(43, fightMode-1); // ??
		getPA().sendFrame36(172, autoRet == 1 ? 0 : 1);
		getPA().sendFrame36(166, 3); // brightness
		getPA().sendFrame36(108, 0);// resets autocast button
		getPA().sendFrame107(); // reset screen
		getPA().setChatOptions(0, 0, 0); // reset private messaging options

		for (SideBars sb : SideBars.values()) {
			setSidebarInterface(sb.getSideBar(), sb.getInterfaceId());
		}
		getPA().showOption(4, 0, "Trade With", 3);
		getPA().showOption(5, 0, "Follow", 4);
		getItems().resetItems(3214); // inventory updating

		// this string is used in the Quest tab, as well as Equipment tab
		// getPA().sendFrame126("@cya@Quest Name?", 1677);
		Misc.println("[SERIALIZED]: " + playerName);
		setSidebarInterface(SideBars.REGULAR_MAGIC_TAB.getSideBar(), playerMagicBook == 0
				? SideBars.REGULAR_MAGIC_TAB.getInterfaceId() : SideBars.ANCIENT_MAGIC_TAB.getInterfaceId());
		if (addStarter) {
			Starter.newPlayer(this);
		}
		Plugin.execute("login", this);
		Plugin.execute("bonues", this);
		Plugin.execute("logintext", this);
		Plugin.execute("musictab", this);
		Plugin.execute("panelupdate", this);
		Plugin.execute("resetfollowers", this);
		Plugin.execute("weaponstyles", this);
		Plugin.execute("pmaccess", this);

		update();
	}

	public void update() {
		handler.updatePlayer(this, outStream);
		handler.updateNPC(this, outStream);
		flushOutStream();
	}

	private RangersGuild rangersGuild = new RangersGuild(this);

	public RangersGuild getRG() {
		return rangersGuild;
	}

	public void logout() {
		if (System.currentTimeMillis() - logoutDelay > 10000) {
			outStream.createFrame(109);
			CycleEventHandler.getSingleton().stopEvents(this);
			properLogout = true;
		} else {
			sendMessage("You must wait a few seconds from being out of combat to logout.");
		}
	}

	int points;

	public void addPoints(int amt) {
		if (points + amt < 25000) {
			points += amt;
		} else {
			points = 25000;
		}
		sendMessage("You receive " + amt + " points and now have a total of " + points + " openPI points.");
	}

	public int packetSize = 0, packetType = -1;

	public synchronized void postProcessing() {
		if (newWalkCmdSteps > 0) {
			int firstX = getNewWalkCmdX()[0], firstY = getNewWalkCmdY()[0];

			int lastDir = 0;
			boolean found = false;
			numTravelBackSteps = 0;
			int ptr = wQueueReadPtr;
			int dir = Misc.direction(currentX, currentY, firstX, firstY);
			if (dir != -1 && (dir & 1) != 0) {
				do {
					lastDir = dir;
					if (--ptr < 0)
						ptr = walkingQueueSize - 1;

					travelBackX[numTravelBackSteps] = walkingQueueX[ptr];
					travelBackY[numTravelBackSteps++] = walkingQueueY[ptr];
					dir = Misc.direction(walkingQueueX[ptr], walkingQueueY[ptr], firstX, firstY);
					if (lastDir != dir) {
						found = true;
						break;
					}

				} while (ptr != wQueueWritePtr);
			} else
				found = true;

			if (!found)
				println_debug("Fatal: couldn't find connection vertex! Dropping packet.");
			else {
				wQueueWritePtr = wQueueReadPtr;

				addToWalkingQueue(currentX, currentY);

				if (dir != -1 && (dir & 1) != 0) {

					for (int i = 0; i < numTravelBackSteps - 1; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
					int wayPointX2 = travelBackX[numTravelBackSteps - 1],
							wayPointY2 = travelBackY[numTravelBackSteps - 1];
					int wayPointX1, wayPointY1;
					if (numTravelBackSteps == 1) {
						wayPointX1 = currentX;
						wayPointY1 = currentY;
					} else {
						wayPointX1 = travelBackX[numTravelBackSteps - 2];
						wayPointY1 = travelBackY[numTravelBackSteps - 2];
					}

					dir = Misc.direction(wayPointX1, wayPointY1, wayPointX2, wayPointY2);
					if (dir == -1 || (dir & 1) != 0) {
						println_debug("Fatal: The walking queue is corrupt! wp1=(" + wayPointX1 + ", " + wayPointY1
								+ "), " + "wp2=(" + wayPointX2 + ", " + wayPointY2 + ")");
					} else {
						dir >>= 1;
						found = false;
						int x = wayPointX1, y = wayPointY1;
						while (x != wayPointX2 || y != wayPointY2) {
							x += Misc.directionDeltaX[dir];
							y += Misc.directionDeltaY[dir];
							if ((Misc.direction(x, y, firstX, firstY) & 1) == 0) {
								found = true;
								break;
							}
						}
						if (!found) {
							println_debug("Fatal: Internal error: unable to determine connection vertex!" + "  wp1=("
									+ wayPointX1 + ", " + wayPointY1 + "), wp2=(" + wayPointX2 + ", " + wayPointY2
									+ "), " + "first=(" + firstX + ", " + firstY + ")");
						} else
							addToWalkingQueue(wayPointX1, wayPointY1);
					}
				} else {
					for (int i = 0; i < numTravelBackSteps; i++) {
						addToWalkingQueue(travelBackX[i], travelBackY[i]);
					}
				}

				for (int i = 0; i < newWalkCmdSteps; i++) {
					addToWalkingQueue(getNewWalkCmdX()[i], getNewWalkCmdY()[i]);
				}

			}
			isRunning = runEnergy > 0 ? isRunning2 : false;
			// isRunning = isNewWalkCmdIsRunning() || isRunning2;
		}
	}

	public int getMapRegionX() {
		return mapRegionX;
	}

	public int getMapRegionY() {
		return mapRegionY;
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public int getId() {
		return playerId;
	}

	public boolean inPcBoat() {
		return absX >= 2660 && absX <= 2663 && absY >= 2638 && absY <= 2643;
	}

	public boolean inPcGame() {
		return absX >= 2624 && absX <= 2690 && absY >= 2550 && absY <= 2619;
	}

	public void setHitDiff(int hitDiff) {
		this.hitDiff = hitDiff;
	}

	public void setHitDiff2(int hitDiff2) {
		this.hitDiff2 = hitDiff2;
	}

	public int getHitDiff() {
		return hitDiff;
	}

	public void setHitUpdateRequired(boolean hitUpdateRequired) {
		this.hitUpdateRequired = hitUpdateRequired;
	}

	public void setHitUpdateRequired2(boolean hitUpdateRequired2) {
		this.hitUpdateRequired2 = hitUpdateRequired2;
	}

	public boolean isHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public boolean getHitUpdateRequired() {
		return hitUpdateRequired;
	}

	public boolean getHitUpdateRequired2() {
		return hitUpdateRequired2;
	}

	public void setAppearanceUpdateRequired(boolean appearanceUpdateRequired) {
		this.appearanceUpdateRequired = appearanceUpdateRequired;
	}

	public boolean isAppearanceUpdateRequired() {
		return appearanceUpdateRequired;
	}

	public void setChatTextEffects(int chatTextEffects) {
		this.chatTextEffects = chatTextEffects;
	}

	public int getChatTextEffects() {
		return chatTextEffects;
	}

	public void setChatTextSize(byte chatTextSize) {
		this.chatTextSize = chatTextSize;
	}

	public byte getChatTextSize() {
		return chatTextSize;
	}

	public void setChatTextUpdateRequired(boolean chatTextUpdateRequired) {
		this.chatTextUpdateRequired = chatTextUpdateRequired;
	}

	public boolean isChatTextUpdateRequired() {
		return chatTextUpdateRequired;
	}

	public void setChatText(byte chatText[]) {
		this.chatText = chatText;
	}

	public byte[] getChatText() {
		return chatText;
	}

	public void setChatTextColor(int chatTextColor) {
		this.chatTextColor = chatTextColor;
	}

	public int getChatTextColor() {
		return chatTextColor;
	}

	public void setNewWalkCmdX(int newWalkCmdX[]) {
		this.newWalkCmdX = newWalkCmdX;
	}

	public int[] getNewWalkCmdX() {
		return newWalkCmdX;
	}

	public void setNewWalkCmdY(int newWalkCmdY[]) {
		this.newWalkCmdY = newWalkCmdY;
	}

	public int[] getNewWalkCmdY() {
		return newWalkCmdY;
	}

	public void setNewWalkCmdIsRunning(boolean newWalkCmdIsRunning) {
		this.newWalkCmdIsRunning = newWalkCmdIsRunning;
	}

	public boolean isNewWalkCmdIsRunning() {
		return newWalkCmdIsRunning;
	}

	@SuppressWarnings("unused")
	private ISAACCipher inStreamDecryption = null, outStreamDecryption = null;

	public void setInStreamDecryption(ISAACCipher inStreamDecryption) {
		this.inStreamDecryption = inStreamDecryption;
	}

	public void setOutStreamDecryption(ISAACCipher outStreamDecryption) {
		this.outStreamDecryption = outStreamDecryption;
	}

	@SuppressWarnings("static-access")
	public boolean samePlayer() {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (j == playerId)
				continue;
			if (Server.playerHandler.players[j] != null) {
				if (Server.playerHandler.players[j].playerName.equalsIgnoreCase(playerName)) {
					disconnected = true;
					return true;
				}
			}
		}
		return false;
	}

	public int questPoints = 14;

	public void dealDamage(int damage) {
		if (teleTimer <= 0)
			playerLevel[3] -= damage;
		else {
			if (hitUpdateRequired)
				hitUpdateRequired = false;
			if (hitUpdateRequired2)
				hitUpdateRequired2 = false;
		}

	}

	public int[] damageTaken = new int[Constants.MAX_PLAYERS];

	public void handleHitMask(int damage) {
		if (!hitUpdateRequired) {
			hitUpdateRequired = true;
			hitDiff = damage;
		} else if (!hitUpdateRequired2) {
			hitUpdateRequired2 = true;
			hitDiff2 = damage;
		}
		updateRequired = true;
	}
}