package server.model.content;

import java.util.HashMap;

import server.model.Animation;
import server.model.Graphic;
import server.model.players.Player;
import server.model.players.EquipmentListener;

public class EmoteHandler {

	public enum Emotes {
		Yes(168, 855, -1), 
		No(169, 856, -1), 
		Bow(164, 858, -1), 
		Angry(167, 864, -1), 
		Think(162, 857, -1), 
		Wave(163, 863, -1), 
		Shrug(52058, 2113, -1), 
		Cheer(171, 862, -1), 
		Beckon(165, 859, -1), 
		Laugh(170, 861, -1), 
		Jump_For_Joy(52054, 2109, -1), 
		Yawn(52056, 2111,-1),
		Dance(166, 866, -1),
		Jig(52051, 2106, -1),
		Twirl(52052,2107, -1), 
		Headbang(52053, 2108, -1),
		Cry(161, 860, -1), 
		Blow_Kiss(43092, 0x558, 574), 
		Panic(52050, 2105, -1), 
		Rasberry(52055, 2110, -1), 
		Clap(172, 865, -1), 
		Salute(52057, 2112, -1),
		Goblin_Bow(52071, 0x84F, -1), 
		Goblin_Salute(52072, 0x850, -1), 
		Glass_Box(2155, 0x46B, -1), 
		Climb_Rope(25103, 0x46A, -1), 
		Lean(25106, 0x469, -1), 
		Glass_Wall(2154, 0x468, -1),
		Idea(72032, 4276, 712), 
		Stomp(72033, 4278, -1),
		Flap(59062, 4280, -1), 
		Slap_Head(72254, 4275,-1), 
		Zombie_Walk(73001, 3544, -1), 
		Zombie_Dance(73000, 3543, -1),
		Zombie_Hand(73004, 7272, 1244), 
		Scared(15166, 2836, -1), 
		Bunny_Hop(72255, 6111, -1),
		Skillcape(154, 1, 1),
		SNOWMAN_DANCE(88058, 7531, -1),
		AIR_GUITAR(88059, 2414, 1537),
		FREEZE_AND_MELT(88063, 11044, 1973),
		SAFETY_FIRST(88060, 8770, 1553),
		EXPLORE(88061, 9990, 1734),
		TRICK(88062, 10530, 1864),
		AROUND_THE_WORLD(88065, 11542, 2037),
		DRAMATIC_POINT(88066, 12658, 780),
		
		;

		private Emotes(int buttonId, int animId, int gfxId) {
			this.buttonID = buttonId;
			this.animID = animId;
			this.gfxID = gfxId;
		}

		public static HashMap<Integer, Emotes> emotes = new HashMap<Integer, Emotes>();

		public static Emotes loadEmote(int buttonId) {
			return emotes.get(buttonId);
		}

		static {
			for (Emotes e : Emotes.values())
				emotes.put(e.buttonID, e);
		}

		public int gfxID;
		public int animID;
		public int buttonID;
	}

	public static void startEmote(Player player, int buttonId) {
		Emotes EMOTES = Emotes.loadEmote(buttonId);
		if (EMOTES != null) {
			if (EMOTES.animID != 1) {
				player.playAnimation(Animation.create(EMOTES.animID));
				if (EMOTES.gfxID != -1)
					player.playGraphic(Graphic.create(EMOTES.gfxID));
			} else {
				doSkillcapeEmote(player);
			}
		}
	}


	public static int[][] skillcapeData = { { 9747, 823, 4959 }, { 9750, 828, 4981 }, { 9753, 824, 4961 },
			{ 9756, 832, 4973 }, { 9759, 829, 4979 }, { 9762, 813, 4939 }, { 9765, 817, 4947 }, { 9771, 830, 4977 },
			{ 9774, 835, 4969 }, { 9777, 826, 4965 }, { 9780, 818, 4949 }, { 9783, 812, 4937 }, { 9786, 827, 4967 },
			{ 9789, 820, 4953 }, { 9792, 814, 4941 }, { 9795, 815, 4943 }, { 9798, 819, 4951 }, { 9801, 821, 4955 },
			{ 9804, 831, 4975 }, { 9807, 822, 4957 }, { 9810, 825, 4963 }, { 9948, 907, 5158 }, { 9813, 816, 4945 }, };

	public static void doSkillcapeEmote(Player c) {
		if (c.playerEquipment[EquipmentListener.CAPE_SLOT.getSlot()] == 9768 || c.playerEquipment[EquipmentListener.CAPE_SLOT.getSlot()] == 9769) {
			c.playGraphic(Graphic.create(c.playerAppearance[0] == 0 ? 833 : 834, 0, 0));
			c.playAnimation(Animation.create(4971));
			return;
		}
		for (int i = 0; i < skillcapeData.length; i++) {
			if (c.playerEquipment[1] == skillcapeData[i][0] || c.playerEquipment[1] == skillcapeData[i][0] + 1) {
				c.playAnimation(Animation.create(skillcapeData[i][2]));
				c.playGraphic(Graphic.create(skillcapeData[i][1], 0, 0));
				c.stopMovement();
				c.getPA().removeAllWindows();

			}
		}
	}

}