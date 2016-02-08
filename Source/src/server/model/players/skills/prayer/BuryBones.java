
package server.model.players.skills.prayer;

import java.util.HashMap;

import server.Constants;
import server.model.players.Client;

public class BuryBones {
	
	Client c;

	public BuryBones(Client c) {
		this.c = c;
	}
	enum BonesData {

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
		
		static HashMap<Integer, BonesData> BoneInfo = new HashMap<Integer, BonesData>();
		int boneID, boneXP;
		String boneName;
		static {
			for (final BonesData bones : BoneInfo.values())
				BonesData.BoneInfo.put(bones.boneXP, bones);
		}

		BonesData(final int boneID, final int boneXP, final String boneName) {
			this.boneID = boneID;
			this.boneXP = boneXP;
			this.boneName = boneName;
		}

		int getboneID() {
			return boneID;
		}

		int getboneXP() {
			return boneXP;
		}

		String getboneName() {
			return boneName;
		}
	}
	public boolean readBone(int boneID) {
		for (final BonesData bones : BonesData.values()) {
			if (c.getItems().playerHasItem(bones.getboneID(), 1))
				;
			if (boneID == bones.getboneID()) {
				return true;
			}
		}
		return false;
	}

	public void boneOnGround(int boneID) {
		if (System.currentTimeMillis() - c.buryDelay > Constants.BONE_BURY_TIME) {
			for (final BonesData bones : BonesData.values()) {
				if (boneID == bones.getboneID()) {
					c.getItems().deleteItem(boneID, 1);
					c.sendMessage("You bury some " + bones.getboneName() + ".");
					c.getPA().addSkillXP(bones.getboneXP() * Constants.PRAYER_EXPERIENCE, 5);
					c.buryDelay = System.currentTimeMillis();
					c.startAnimation(827);
				}
			}
		}
	}
}