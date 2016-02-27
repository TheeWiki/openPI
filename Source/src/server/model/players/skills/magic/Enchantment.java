package server.model.players.skills.magic;

import java.util.HashMap;
import java.util.Map;

import server.model.players.Player;
import server.model.players.skills.SkillIndex;

public class Enchantment {

	public enum Enchant {

		SAPPHIRERING(1637, 2550, 7, 18, 719, 114, 1),
		SAPPHIREAMULET(1694, 1727, 7, 18, 719, 114, 1),
		SAPPHIRENECKLACE(1656, 3853, 7, 18, 719, 114, 1),

		EMERALDRING(1639, 2552, 27, 37, 719, 114, 2),
		EMERALDAMULET(1696, 1729, 27, 37, 719, 114, 2),
		EMERALDNECKLACE(1658, 5521, 27, 37, 719, 114, 2),

		RUBYRING(1641, 2568, 47, 59, 720, 115, 3),
		RUBYAMULET(1698, 1725, 47, 59, 720, 115, 3),
		RUBYNECKLACE(1660, 11194, 47, 59, 720, 115, 3),

		DIAMONDRING(1643, 2570, 57, 67, 720, 115, 4),
		DIAMONDAMULET(1700, 1731, 57, 67, 720, 115, 4),
		DIAMONDNECKLACE(1662, 11090, 57, 67, 720, 115, 4),

		DRAGONSTONERING(1645, 2572, 68, 78, 721, 116, 5),
		DRAGONSTONEAMULET(1702, 1712, 68, 78, 721, 116, 5),
		DRAGONSTONENECKLACE(1664, 11105, 68, 78, 721, 116, 5),

		ONYXRING(6575, 6583, 87, 97, 721, 452, 6),
		ONYXAMULET(6581, 6585, 87, 97, 721, 452, 6),
		ONYXNECKLACE(6577, 11128, 87, 97, 721, 452, 6);

		int unenchanted, enchanted, levelReq, xpGiven, anim, gfx, reqEnchantmentLevel;

		private Enchant(int unenchanted, int enchanted, int levelReq, int xpGiven, int anim, int gfx,
				int reqEnchantmentLevel) {
			this.unenchanted = unenchanted;
			this.enchanted = enchanted;
			this.levelReq = levelReq;
			this.xpGiven = xpGiven;
			this.anim = anim;
			this.gfx = gfx;
			this.reqEnchantmentLevel = reqEnchantmentLevel;
		}

		public int getUnenchanted() {
			return unenchanted;
		}

		public int getEnchanted() {
			return enchanted;
		}

		public int getLevelReq() {
			return levelReq;
		}

		public int getXp() {
			return xpGiven;
		}

		public int getAnim() {
			return anim;
		}

		public int getGFX() {
			return gfx;
		}

		public int getELevel() {
			return reqEnchantmentLevel;
		}

		private static final Map<Integer, Enchant> enc = new HashMap<Integer, Enchant>();

		public static Enchant forId(int itemID) {
			return enc.get(itemID);
		}

		static {
			for (Enchant en : Enchant.values()) {
				enc.put(en.getUnenchanted(), en);
			}
		}
	}

	private enum EnchantSpell {

		SAPPHIRE(1155, 555, 1, 564, 1, -1, 0), EMERALD(1165, 556, 3, 564, 1, -1, 0), RUBY(1176, 554, 5, 564, 1, -1,
				0), DIAMOND(1180, 557, 10, 564, 1, -1, 0), DRAGONSTONE(1187, 555, 15, 557, 15, 564, 1), ONYX(6003, 557,
						20, 554, 20, 564, 1);

		int spell, reqRune1, reqAmtRune1, reqRune2, reqAmtRune2, reqRune3, reqAmtRune3;

		private EnchantSpell(int spell, int reqRune1, int reqAmtRune1, int reqRune2, int reqAmtRune2, int reqRune3,
				int reqAmtRune3) {
			this.spell = spell;
			this.reqRune1 = reqRune1;
			this.reqAmtRune1 = reqAmtRune1;
			this.reqRune2 = reqRune2;
			this.reqAmtRune2 = reqAmtRune2;
			this.reqRune3 = reqRune3;
			this.reqAmtRune3 = reqAmtRune3;
		}

		public int getSpell() {
			return spell;
		}

		public int getReq1() {
			return reqRune1;
		}

		public int getReqAmt1() {
			return reqAmtRune1;
		}

		public int getReq2() {
			return reqRune2;
		}

		public int getReqAmt2() {
			return reqAmtRune2;
		}

		public int getReq3() {
			return reqRune3;
		}

		public int getReqAmt3() {
			return reqAmtRune3;
		}

		public static final Map<Integer, EnchantSpell> ens = new HashMap<Integer, EnchantSpell>();

		public static EnchantSpell forId(int id) {
			return ens.get(id);
		}

		static {
			for (EnchantSpell en : EnchantSpell.values()) {
				ens.put(en.getSpell(), en);
			}
		}

	}

	private static boolean hasRunes(Player player, int spellID) {
		EnchantSpell ens = EnchantSpell.forId(spellID);
		if (ens.getReq3() == 0) {
			if (player.getCombat().wearingStaff(ens.getReq1())
					&& player.getItems().playerHasItem(ens.getReq2(),
							ens.getReqAmt2())
					&& player.getItems().playerHasItem(ens.getReq3(), ens.getReqAmt3())
					|| (player.getCombat().wearingStaff(ens.getReq2())
							&& player.getItems().playerHasItem(ens.getReq1(), ens.getReqAmt1())
							&& player.getItems().playerHasItem(ens.getReq3(), ens.getReqAmt3())
							|| (player.getCombat().wearingStaff(ens.getReq3())
									&& player.getItems().playerHasItem(ens.getReq1(), ens.getReqAmt1())
									&& player.getItems().playerHasItem(ens.getReq2(), ens.getReqAmt2())))) {
				return true;
			} else if (player.getItems().playerHasItem(ens.getReq1(), ens.getReqAmt1())
					&& player.getItems().playerHasItem(ens.getReq2(), ens.getReqAmt2())
					&& player.getItems().playerHasItem(ens.getReq3(), ens.getReqAmt3())) {
				return true;
			} else {
				return false;
			}
		} else {
			if (player.getCombat().wearingStaff(ens.getReq1()) && player.getItems().playerHasItem(ens.getReq2(), ens.getReqAmt2())
					|| (player.getCombat().wearingStaff(ens.getReq2())
							&& player.getItems().playerHasItem(ens.getReq1(), ens.getReqAmt1()))) {
				return true;
			} else if (player.getItems().playerHasItem(ens.getReq1(), ens.getReqAmt1())
					&& player.getItems().playerHasItem(ens.getReq2(), ens.getReqAmt2())) {
				return true;
			} else {
				return false;
			}
		}
	}

	private static int getEnchantmentLevel(int spellID) {
		switch (spellID) {
		case 1155: // Lvl-1 enchant sapphire
			return 1;
		case 1165: // Lvl-2 enchant emerald
			return 2;
		case 1176: // Lvl-3 enchant ruby
			return 3;
		case 1180: // Lvl-4 enchant diamond
			return 4;
		case 1187: // Lvl-5 enchant dragonstone
			return 5;
		case 6003: // Lvl-6 enchant onyx
			return 6;
		}
		return 0;
	}

	public static void enchantItem(Player player, int itemID, int spellID) {
		Enchant enc = Enchant.forId(itemID);
		EnchantSpell ens = EnchantSpell.forId(spellID);
		if (enc == null || ens == null) {
			return;
		}
		if (player.playerLevel[SkillIndex.MAGIC.getSkillId()] >= enc.getLevelReq()) {
			if (player.getItems().playerHasItem(enc.getUnenchanted(), 1)) {
				if (hasRunes(player, spellID)) {
					if (getEnchantmentLevel(spellID) == enc.getELevel()) {
						player.getItems().deleteItem(enc.getUnenchanted(), 1);
						player.getItems().addItem(enc.getEnchanted(), 1);

						player.getPA().addSkillXP(enc.getXp() * SkillIndex.MAGIC.getExpRatio(), SkillIndex.MAGIC.getSkillId());

						player.getItems().deleteItem(ens.getReq1(), player.getItems().getItemSlot(ens.getReq1()), ens.getReqAmt1());
						player.getItems().deleteItem(ens.getReq2(), player.getItems().getItemSlot(ens.getReq2()), ens.getReqAmt2());
						player.startAnimation(enc.getAnim());
						player.gfx100(enc.getGFX());
						if (ens.getReq3() != -1) {
							player.getItems().deleteItem(ens.getReq3(), player.getItems().getItemSlot(ens.getReq3()), ens.getReqAmt3());
						}
						player.getPA().sendFrame106(6);
					} else {
						player.getActionSender().sendMessage("You can only enchant this jewelry using a level-" + enc.getELevel()
								+ " enchantment spell!");
					}
				} else {
					player.getActionSender().sendMessage("You do not have enough runes to cast this spell.");
				}
			}
		} else {
			player.getActionSender().sendMessage("You need a magic level of at least " + enc.getLevelReq() + " to cast this spell.");
		}
	}

	public static int[][] boltData = { { 1155, 879, 9, 9236 }, { 1155, 9337, 17, 9240 }, { 1165, 9335, 19, 9237 },
			{ 1165, 880, 29, 9238 }, { 1165, 9338, 37, 9241 }, { 1176, 9336, 39, 9239 }, { 1176, 9339, 59, 9242 },
			{ 1180, 9340, 67, 9243 }, { 1187, 9341, 78, 9244 }, { 6003, 9342, 97, 9245 } };

	public static void enchantBolt(Player player, int spell, int bolt, int amount) {
		EnchantSpell ens = EnchantSpell.forId(spell);
		for (int i = 0; i < boltData.length; i++) {
			if (spell == boltData[i][0]) {
				if (bolt == boltData[i][1]) {
					if (hasRunes(player, spell)) {
						player.getItems().deleteItem(ens.getReq1(), player.getItems().getItemSlot(ens.getReq1()),
								ens.getReqAmt1());
						player.getItems().deleteItem(ens.getReq2(), player.getItems().getItemSlot(ens.getReq2()),
								ens.getReqAmt2());
					} else {
						player.getActionSender().sendMessage("You don't have the required runes to use this spell.");
						return;
					}
					if (!player.getItems().playerHasItem(boltData[i][1], amount))
						amount = player.getItems().getItemAmount(bolt);
					player.getItems().deleteItem(boltData[i][1], player.getItems().getItemSlot(boltData[i][1]), amount);

					player.getPA().addSkillXP(boltData[i][2] * amount * SkillIndex.MAGIC.getExpRatio(),
							SkillIndex.MAGIC.getSkillId());

					player.getItems().addItem(boltData[i][3], amount);
					player.getPA().sendFrame106(6);
					return;
				}
			}
		}
	}
	public final static int[][] MAGIC_SPELLS = {
			// example {magicId, level req, animation, startGFX, projectile Id,
			// endGFX, maxhit, exp gained, rune 1, rune 1 amount, rune 2, rune 2
			// amount, rune 3, rune 3 amount, rune 4, rune 4 amount}

			// Modern Spells
			{ 1152, 1, 711, 90, 91, 92, 2, 5, 556, 1, 558, 1, 0, 0, 0, 0 }, // wind
																			// strike
			{ 1154, 5, 711, 93, 94, 95, 4, 7, 555, 1, 556, 1, 558, 1, 0, 0 }, // water
																				// strike
			{ 1156, 9, 711, 96, 97, 98, 6, 9, 557, 2, 556, 1, 558, 1, 0, 0 }, // earth
																				// strike
			{ 1158, 13, 711, 99, 100, 101, 8, 11, 554, 3, 556, 2, 558, 1, 0, 0 }, // fire
																					// strike
			{ 1160, 17, 711, 117, 118, 119, 9, 13, 556, 2, 562, 1, 0, 0, 0, 0 }, // wind
																					// bolt
			{ 1163, 23, 711, 120, 121, 122, 10, 16, 556, 2, 555, 2, 562, 1, 0, 0 }, // water
																					// bolt
			{ 1166, 29, 711, 123, 124, 125, 11, 20, 556, 2, 557, 3, 562, 1, 0, 0 }, // earth
																					// bolt
			{ 1169, 35, 711, 126, 127, 128, 12, 22, 556, 3, 554, 4, 562, 1, 0, 0 }, // fire
																					// bolt
			{ 1172, 41, 711, 132, 133, 134, 13, 25, 556, 3, 560, 1, 0, 0, 0, 0 }, // wind
																					// blast
			{ 1175, 47, 711, 135, 136, 137, 14, 28, 556, 3, 555, 3, 560, 1, 0, 0 }, // water
																					// blast
			{ 1177, 53, 711, 138, 139, 140, 15, 31, 556, 3, 557, 4, 560, 1, 0, 0 }, // earth
																					// blast
			{ 1181, 59, 711, 129, 130, 131, 16, 35, 556, 4, 554, 5, 560, 1, 0, 0 }, // fire
																					// blast
			{ 1183, 62, 711, 158, 159, 160, 17, 36, 556, 5, 565, 1, 0, 0, 0, 0 }, // wind
																					// wave
			{ 1185, 65, 711, 161, 162, 163, 18, 37, 556, 5, 555, 7, 565, 1, 0, 0 }, // water
																					// wave
			{ 1188, 70, 711, 164, 165, 166, 19, 40, 556, 5, 557, 7, 565, 1, 0, 0 }, // earth
																					// wave
			{ 1189, 75, 711, 155, 156, 157, 20, 42, 556, 5, 554, 7, 565, 1, 0, 0 }, // fire
																					// wave
			{ 1153, 3, 716, 102, 103, 104, 0, 13, 555, 3, 557, 2, 559, 1, 0, 0 }, // confuse
			{ 1157, 11, 716, 105, 106, 107, 0, 20, 555, 3, 557, 2, 559, 1, 0, 0 }, // weaken
			{ 1161, 19, 716, 108, 109, 110, 0, 29, 555, 2, 557, 3, 559, 1, 0, 0 }, // curse
			{ 1542, 66, 729, 167, 168, 169, 0, 76, 557, 5, 555, 5, 566, 1, 0, 0 }, // vulnerability
			{ 1543, 73, 729, 170, 171, 172, 0, 83, 557, 8, 555, 8, 566, 1, 0, 0 }, // enfeeble
			{ 1562, 80, 729, 173, 174, 107, 0, 90, 557, 12, 555, 12, 556, 1, 0, 0 }, // stun
			{ 1572, 20, 711, 177, 178, 181, 0, 30, 557, 3, 555, 3, 561, 2, 0, 0 }, // bind
			{ 1582, 50, 711, 177, 178, 180, 2, 60, 557, 4, 555, 4, 561, 3, 0, 0 }, // snare
			{ 1592, 79, 711, 177, 178, 179, 4, 90, 557, 5, 555, 5, 561, 4, 0, 0 }, // entangle
			{ 1171, 39, 724, 145, 146, 147, 15, 25, 556, 2, 557, 2, 562, 1, 0, 0 }, // crumble
																					// undead
			{ 1539, 50, 708, 87, 88, 89, 25, 42, 554, 5, 560, 1, 0, 0, 0, 0 }, // iban
																				// blast
			{ 12037, 50, 1576, 327, 328, 329, 19, 30, 560, 1, 558, 4, 0, 0, 0, 0 }, // magic
																					// dart
			{ 1190, 60, 811, 0, 0, 76, 20, 60, 554, 2, 565, 2, 556, 4, 0, 0 }, // sara
																				// strike
			{ 1191, 60, 811, 0, 0, 77, 20, 60, 554, 1, 565, 2, 556, 4, 0, 0 }, // cause
																				// of
																				// guthix
			{ 1192, 60, 811, 0, 0, 78, 20, 60, 554, 4, 565, 2, 556, 1, 0, 0 }, // flames
																				// of
																				// zammy
			{ 12445, 85, 1819, 0, 344, 345, 0, 65, 563, 1, 562, 1, 560, 1, 0, 0 }, // teleblock

			// Ancient Spells
			{ 12939, 50, 1978, 0, 384, 385, 13, 30, 560, 2, 562, 2, 554, 1, 556, 1 }, // smoke
																						// rush
			{ 12987, 52, 1978, 0, 378, 379, 14, 31, 560, 2, 562, 2, 566, 1, 556, 1 }, // shadow
																						// rush
			{ 12901, 56, 1978, 0, 0, 373, 15, 33, 560, 2, 562, 2, 565, 1, 0, 0 }, // blood
																					// rush
			{ 12861, 58, 1978, 0, 360, 361, 16, 34, 560, 2, 562, 2, 555, 2, 0, 0 }, // ice
																					// rush
			{ 12963, 62, 1979, 0, 0, 389, 19, 36, 560, 2, 562, 4, 556, 2, 554, 2 }, // smoke
																					// burst
			{ 13011, 64, 1979, 0, 0, 382, 20, 37, 560, 2, 562, 4, 556, 2, 566, 2 }, // shadow
																					// burst
			{ 12919, 68, 1979, 0, 0, 376, 21, 39, 560, 2, 562, 4, 565, 2, 0, 0 }, // blood
																					// burst
			{ 12881, 70, 1979, 0, 0, 363, 22, 40, 560, 2, 562, 4, 555, 4, 0, 0 }, // ice
																					// burst
			{ 12951, 74, 1978, 0, 386, 387, 23, 42, 560, 2, 554, 2, 565, 2, 556, 2 }, // smoke
																						// blitz
			{ 12999, 76, 1978, 0, 380, 381, 24, 43, 560, 2, 565, 2, 556, 2, 566, 2 }, // shadow
																						// blitz
			{ 12911, 80, 1978, 0, 374, 375, 25, 45, 560, 2, 565, 4, 0, 0, 0, 0 }, // blood
																					// blitz
			{ 12871, 82, 1978, 366, 0, 367, 26, 46, 560, 2, 565, 2, 555, 3, 0, 0 }, // ice
																					// blitz
			{ 12975, 86, 1979, 0, 0, 391, 27, 48, 560, 4, 565, 2, 556, 4, 554, 4 }, // smoke
																					// barrage
			{ 13023, 88, 1979, 0, 0, 383, 28, 49, 560, 4, 565, 2, 556, 4, 566, 3 }, // shadow
																					// barrage
			{ 12929, 92, 1979, 0, 0, 377, 29, 51, 560, 4, 565, 4, 566, 1, 0, 0 }, // blood
																					// barrage
			{ 12891, 94, 1979, 0, 0, 369, 30, 52, 560, 4, 565, 2, 555, 6, 0, 0 }, // ice
																					// barrage

			{ -1, 80, 811, 301, 0, 0, 0, 0, 554, 3, 565, 3, 556, 3, 0, 0 }, // charge
			{ -1, 21, 712, 112, 0, 0, 0, 10, 554, 3, 561, 1, 0, 0, 0, 0 }, // low
																			// alch
			{ -1, 55, 713, 113, 0, 0, 0, 20, 554, 5, 561, 1, 0, 0, 0, 0 }, // high
																			// alch
			{ -1, 33, 728, 142, 143, 144, 0, 35, 556, 1, 563, 1, 0, 0, 0, 0 } // telegrab

	};
}