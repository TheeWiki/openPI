
package server.model.players.skills.prayer;

import java.util.HashMap;

import server.Constants;
import server.model.players.Player;
import server.model.players.skills.SkillIndex;

public class BuryBones {

	private enum BonesData {

		/* Starter Bones */
		REGULAR(526, 50, "Bones"),
		NORMAL2(2530, 50, "Bones"),
		Burnt(528, 50,"Burnt Bones"),
		BATB(530, 50, "Bat Bones"),
		WOLFB(2859, 50,"Wolf Bones"),
		MONKEYB0(3179, 50, "Monkey Bones"),
		MONKEYB1(3180, 50, "Monkey Bones"),
		MONKEYB2(3181, 50, "Monkey Bones"),
		MONKEYB3(3182, 50, "Monkey Bones"),
		MONKEYB4(3183, 50, "Monkey Bones"),
		MONKEYB5(3185, 50, "Monkey Bones"),
		MONKEYB6(3186, 50, "Monkey Bones"),
		MONKEYB7(3187, 50, "Monkey Bones"),
		/* Decent Bones */
		BBONE(532, 70, "Big Bones"), 
		Jogre(3125, 100, "Jogre Bones"),
		BJogre(3127, 110, "Burnt Jogre Bones"),
		Jogre2(3128, 110, "Pasty Jogre Bones"),
		Jogre3(3129, 110, "Pasty Jogre Bones"),
		Jogre4(3130, 110, "Marinated Jogre Bones"),
		Jogre5(3131, 110, "Pasty Jogre Bones"),
		Jogre6(3132, 110, "Pasty Jogre Bones"),
		Jogre7(3133, 110, "Marinated Jogre Bones"),
		Zogre(4812, 110, "Zogre Bones"),
		Shaik(3123, 110, "Shaikahan Bones"), 
		Baby(534, 130,"Baby Dragon Bones"),
		Wyvern(6812, 210, "Wyvern Bones"),
		Dragon(536, 220, "Dragon Bones"), 
		Fayrg(4830, 250, "Fayrg Bones"),
		/* Good Bones */
		OurgB(14793, 270, "Ourg Bones"),
		RaurgBone(4832, 270, "Raurg Bones"), 
		OurgBone(4834, 270, "Ourg Bones"),
		DagBone(6729, 300, "Dagannoth Bones"),
		ANCIENT(15410, 330, "Ancient Bones"),
		Frost(18830, 350, "Frost Bones"),
		Curvedb(10977, 500, "Curved bone"),
		Longb(10976, 500, "Long bone");
		
		private static HashMap<Integer, BonesData> BoneInfo = new HashMap<Integer, BonesData>();
		
		private int boneID, boneXP;
		private String boneName;

		static {
			for (BonesData bones : values()) {
				BoneInfo.put(bones.getboneID(), bones);
				BoneInfo.put(bones.getboneXP(), bones);
			}
		}

		BonesData(final int boneID, final int boneXP, final String boneName) {
			this.boneID = boneID;
			this.boneXP = boneXP;
			this.boneName = boneName;
		}
		public int getboneID() {
			return boneID;
		}

		public int getboneXP() {
			return boneXP;
		}

		public String getboneName() {
			return boneName;
		}
	}

	public static void boneOnGround(Player c, int itemId, int itemSlot) {
		if (System.currentTimeMillis() - c.buryDelay > Constants.TICK) {
			for (final BonesData bones : BonesData.values()) {
				if (itemId == bones.getboneID()) {
					c.getAttributes().setAttribute("boneBurying", true);
					c.getItems().deleteItem(itemId, itemSlot, 1);
					c.sendMessage("You bury some " + bones.getboneName() + ".");
					c.getPA().addSkillXP(bones.getboneXP() * SkillIndex.PRAYER.getExpRatio(), SkillIndex.PRAYER.getSkillId());

				}
				c.buryDelay = System.currentTimeMillis();
				c.startAnimation(827);
				c.getAttributes().removeAttribute("boneBurying");
			}
		}
	}
}