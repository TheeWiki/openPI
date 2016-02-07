package server.model;

import java.util.HashMap;

import server.model.players.Client;

public class EmoteHandler {

	private final Client c;

	public EmoteHandler(Client c) {
		this.c = c;
	}

	public Emotes EMOTES = null;

	public enum Emotes {
		Yes(168, 855, -1), No(169, 856, -1), Bow(164, 858, -1), Angry(167, 864, -1), Think(162, 857, -1), Wave(163, 863,
				-1), Shrug(52058, 2113, -1), Cheer(171, 862, -1), Beckon(165, 859, -1), Laugh(170, 861,
						-1), Jump_For_Joy(52054, 2109, -1), Yawn(52056, 2111, -1), Dance(166, 866, -1), Jig(52051, 2106,
								-1), Twirl(52052, 2107, -1), Headbang(52053, 2108, -1), Cry(161, 860, -1), Blow_Kiss(
										43092, 0x558, 574), Panic(52050, 2105, -1), Rasberry(52055, 2110, -1), Clap(172,
												865, -1), Salute(52057, 2112, -1), Goblin_Bow(52071, 0x84F,
														-1), Goblin_Salute(52072, 0x850, -1), Glass_Box(2155, 0x46B,
																-1), Climb_Rope(25103, 0x46A, -1), Lean(25106, 0x469,
																		-1), Glass_Wall(2154, 0x468, -1), Idea(88060,
																				4276,
																				712), Stomp(88061, 4278, -1), Flap(
																						88062, 4280,
																						-1), Slap_Head(88063, 4275,
																								-1), Zombie_Walk(72032,
																										3544,
																										-1), Zombie_Dance(
																												72033,
																												3543,
																												-1), Zombie_Hand(
																														88065,
																														7272,
																														1244), Scared(
																																59062,
																																2836,
																																-1), Bunny_Hop(
																																		72254,
																																		6111,
																																		-1), Skillcape(
																																				74108,
																																				1,
																																				1);

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

	public void startEmote(int buttonId) {
		Emotes EMOTES = Emotes.loadEmote(buttonId);
		c.emotesPerformed+=1;
		if (EMOTES != null && this.EMOTES == null) {
			this.EMOTES = EMOTES;
			if (EMOTES.animID != 1) {
				c.playAnimation(Animation.create(EMOTES.animID));
				if (EMOTES.gfxID != -1)
					c.playGraphic(Graphic.create(EMOTES.gfxID, 0, 0));
			} else {
				doSkillcapeEmote(c);
			}
			EMOTES = null;
			this.EMOTES = null;
		}
	}

	public static int[][] skillcapeData = { { 9747, 823, 4959 }, { 9750, 828, 4981 }, { 9753, 824, 4961 },
			{ 9756, 832, 4973 }, { 9759, 829, 4979 }, { 9762, 813, 4939 }, { 9765, 817, 4947 }, { 9771, 830, 4977 },
			{ 9774, 835, 4969 }, { 9777, 826, 4965 }, { 9780, 818, 4949 }, { 9783, 812, 4937 }, { 9786, 827, 4967 },
			{ 9789, 820, 4953 }, { 9792, 814, 4941 }, { 9795, 815, 4943 }, { 9798, 819, 4951 }, { 9801, 821, 4955 },
			{ 9804, 831, 4975 }, { 9807, 822, 4957 }, { 9810, 825, 4963 }, { 9948, 907, 5158 }, { 9813, 816, 4945 }, };

	public static void doSkillcapeEmote(Client c) {
		if (c.playerEquipment[c.playerCape] == 9768 || c.playerEquipment[c.playerCape] == 9769) {
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