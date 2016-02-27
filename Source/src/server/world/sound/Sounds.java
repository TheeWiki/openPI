package server.world.sound;

import server.Server;
import server.model.players.Client;
import server.model.players.EquipmentListener;
import server.util.Misc;

/**
 * Not implemented, just standalone class
 * @author Dennis
 *
 */
public class Sounds {

	Client c;

	public Sounds(Client c) {
		this.c = c;
	}

	/**
	 * Singular sound variables.
	 */

	public final int LEVELUP = 67;
	public final int DUELWON = 77;
	public final int DUELLOST = 76;
	public final int FOODEAT = 317;
	public final int DROPITEM = 376;
	public final int COOKITEM = 357;
	public final int SHOOT_ARROW = 370;
	public final int TELEPORT = 202;
	public final int BONE_BURY = 380;
	public final int DRINK_POTION = 334;

	public static int getNpcAttackSounds(int NPCID) {
		String npc = GetNpcName(NPCID).toLowerCase();
		if (npc.contains("bat")) {
			return 1;
		}
		if (npc.contains("cow")) {
			return 4;
		}
		if (npc.contains("imp")) {
			return 11;
		}
		if (npc.contains("rat")) {
			return 17;
		}
		if (npc.contains("duck")) {
			return 26;
		}
		if (npc.contains("wolf") || npc.contains("bear")) {
			return 28;
		}
		if (npc.contains("dragon")) {
			return 47;
		}
		if (npc.contains("ghost")) {
			return 57;
		}
		if (npc.contains("goblin")) {
			return 88;
		}
		if (npc.contains("skeleton") || npc.contains("demon") || npc.contains("ogre") || npc.contains("giant")
				|| npc.contains("tz-") || npc.contains("jad")) {
			return 48;
		}
		if (npc.contains("zombie")) {
			return 1155;
		}
		if (npc.contains("man") || npc.contains("woman") || npc.contains("monk")) {
			return 417;
		}
		return Misc.random(6) > 3 ? 398 : 394;

	}

	public static int getNpcBlockSound(int NPCID) {
		String npc = GetNpcName(NPCID).toLowerCase();
		if (npc.contains("bat")) {
			return 7;
		}
		if (npc.contains("cow")) {
			return 5;
		}
		if (npc.contains("imp")) {
			return 11;
		}
		if (npc.contains("rat")) {
			return 16;
		}
		if (npc.contains("duck")) {
			return 24;
		}
		if (npc.contains("wolf") || npc.contains("bear")) {
			return 34;
		}
		if (npc.contains("dragon")) {
			return 45;
		}
		if (npc.contains("ghost")) {
			return 53;
		}
		if (npc.contains("goblin")) {
			return 87;
		}
		if (npc.contains("skeleton") || npc.contains("demon") || npc.contains("ogre") || npc.contains("giant")
				|| npc.contains("tz-") || npc.contains("jad")) {
			return 1154;
		}
		if (npc.contains("zombie")) {
			return 1151;
		}
		if (npc.contains("man") && !npc.contains("woman")) {
			return 816;
		}
		if (npc.contains("monk")) {
			return 816;
		}

		if (!npc.contains("man") && npc.contains("woman")) {
			return 818;
		}
		return 791;

	}

	public static int getNpcDeathSounds(int NPCID) {
		String npc = GetNpcName(NPCID).toLowerCase();
		if (npc.contains("bat")) {
			return 7;
		}
		if (npc.contains("cow")) {
			return 3;
		}
		if (npc.contains("imp")) {
			return 9;
		}
		if (npc.contains("rat")) {
			return 15;
		}
		if (npc.contains("duck")) {
			return 25;
		}
		if (npc.contains("wolf") || npc.contains("bear")) {
			return 35;
		}
		if (npc.contains("dragon")) {
			return 44;
		}
		if (npc.contains("ghost")) {
			return 60;
		}
		if (npc.contains("goblin")) {
			return 125;
		}
		if (npc.contains("skeleton") || npc.contains("demon") || npc.contains("ogre") || npc.contains("giant")
				|| npc.contains("tz-") || npc.contains("jad")) {
			return 70;
		}
		if (npc.contains("zombie")) {
			return 1140;
		}
		return 70;

	}

	public static String GetNpcName(int NpcID) {
		return Server.npcHandler.getNpcName(NpcID);
	}

	public static String getItemName(int ItemID) {
		return Server.itemHandler.ItemList[ItemID].itemName;
	}

	public static int getPlayerBlockSounds(Client c) {

		int blockSound = 511;
		
		if (c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 2499 || c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 2501
				|| c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 2503 || c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 4746
				|| c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 4757 || c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 10330) {// Dragonhide
																											// sound
			blockSound = 24;
		} else if (c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 10551 || // Torso
				c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 10438) {// 3rd age
			blockSound = 32;// Weird sound
		} else if (c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 10338 || // 3rd age
				c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 7399 || // Enchanted
				c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 6107 || // Ghostly
				c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 4091 || // Mystic
				c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 4101 || // Mystic
				c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 4111 || // Mystic
				c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1035 || // Zamorak
				c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 12971) {// Combat
			blockSound = 14;// Robe sound 
		} else if (c.playerEquipment[EquipmentListener.SHIELD_SLOT.getSlot()] == 4224) {// Crystal Shield
			blockSound = 30;// Crystal sound
		} else if (c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1101 || // Chains
				c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1103 || c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1105
				|| c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1107 || c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1109
				|| c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1111 || c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1113
				|| c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1115 || // Plates
				c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1117 || c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1119
				|| c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1121 || c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1123
				|| c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1125 || c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1127
				|| c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 4720 || // Barrows armour
				c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 4728 || c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 4749
				|| c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 4712 || c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 11720 || // Godwars
																											// armour
				c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 11724 || c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 3140 || // Dragon
				c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 2615 || // Fancy
				c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 2653 || c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 2661
				|| c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 2669 || c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 2623
				|| c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 3841 || c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 1127) {// Metal
																											// armour
																									// sound
			blockSound = 15;
		} else {
			blockSound = 511;
		}
		return blockSound;
	}

	public static int GetWeaponSound(Client c) {

		String wep = getItemName(c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase();
		
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4718) {// Dharok's Greataxe
			return 1320;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4734) {// Karil's Crossbow
			return 1081;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4747) {// Torag's Hammers
			return 1330;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4710) {// Ahrim's Staff
			return 2555;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4755) {// Verac's Flail
			return 1323;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4726) {// Guthan's Warspear
			return 2562;
		}

		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 772 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1379
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1381 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1383
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1385 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1387
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1389 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1391
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1393 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1395
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1397 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1399
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1401 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1403
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1405 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1407
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1409 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 9100) { // Staff
																												// wack
			return 394;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 839 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 841
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 843 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 845
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 847 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 849
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 851 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 853
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 855 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 857
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 859 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 861
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4734 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 2023 // RuneC'Bow
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4212 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4214
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4934 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 9104
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 9107) { // Bows/Crossbows
			return 370;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1363 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1365
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1367 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1369
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1371 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1373
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1375 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1377
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1349 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1351
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1353 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1355
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1357 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1359
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1361 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 9109) { // BattleAxes/Axes
			return 399;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4718 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 7808) { // Dharok
																										// GreatAxe
			return 400;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 6609 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1307
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1309 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1311
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1313 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1315
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1317 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1319) { // 2h
			return 425;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1321 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1323
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1325 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1327
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1329 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1331
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 1333 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4587) { // Scimitars
			return 396;
		}
		if (wep.contains("halberd")) {
			return 420;
		}
		if (wep.contains("long")) {
			return 396;
		}
		if (wep.contains("knife")) {
			return 368;
		}
		if (wep.contains("javelin")) {
			return 364;
		}

		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 9110) {
			return 401;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4755) {
			return 1059;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4153) {
			return 1079;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 9103) {
			return 385;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == -1) { // fists
			return 417;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 2745 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 2746
				|| c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 2747 || c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 2748) { // Godswords
			return 390;
		}
		if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4151) {
			return 1080;
		} else {
			return 398; // Daggers(this is enything that isn't added)
		}
	}

	public static int specialSounds(int id) {
		if (id == 4151) // whip
		{
			return 1081;
		}
		if (id == 5698) // dds
		{
			return 793;
		}
		if (id == 1434)// Mace
		{
			return 387;
		}
		if (id == 3204) // halberd
		{
			return 420;
		}
		if (id == 4153) // gmaul
		{
			return 1082;
		}
		if (id == 7158) // d2h
		{
			return 426;
		}
		if (id == 4587) // dscim
		{
			return 1305;
		}
		if (id == 1215) // Dragon dag
		{
			return 793;
		}
		if (id == 1305) // D Long
		{
			return 390;
		}
		if (id == 861) // MSB
		{
			return 386;
		}
		if (id == 11235) // DBow
		{
			return 386;
		}
		if (id == 6739) // D Axe
		{
		}
		if (id == 1377) // DBAxe
		{
			return 389;
		}
		return -1;
	}
}