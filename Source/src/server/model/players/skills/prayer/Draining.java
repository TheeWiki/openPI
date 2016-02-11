package server.model.players.skills.prayer;

import server.model.players.Client;

public class Draining {
	
	public static double[] PRAYER_DRAIN = { 0.5, // Thick Skin.
			0.5, // Burst of Strength.
			0.5, // Clarity of Thought.
			0.5, // Sharp Eye.
			0.5, // Mystic Will.
			1, // Rock Skin.
			1, // SuperHuman Strength.
			1, // Improved Reflexes.
			0.15, // Rapid restore
			0.3, // Rapid Heal.
			0.3, // Protect Items
			1, // Hawk eye.
			1, // Mystic Lore.
			2, // Steel Skin.
			2, // Ultimate Strength.
			2, // Incredible Reflexes.
			2, // Protect from Magic.
			2, // Protect from Missiles.
			2, // Protect from Melee.
			2, // Eagle Eye.
			2, // Mystic Might.
			0.5, // Retribution.
			1, // Redemption.
			2, // Smite
			2, // Chivalry.
			4, // Piety.
	};
	public static double[] CURSE_DRAIN =

	{
			0.3, //Protect Item
			2.5, //Sap Warrior
			2.5, //Sap Ranger
			2.5, //Sap Mage
			2.5, //Sap Spirit
			0.3, //Berserker
			2, //Deflect Summoning
			2, //Deflect Magic
			2, //Deflect Missiles
			2, //Deflect Melee
			1.6, //Leech Attack
			1.6, //Leech Ranged
			1.6, //Leech Magic
			1.6, //Leech Defence
			1.6, //Leech Energy
			1.6, //Leech Special
			0.5, //Wrath
			3, //SS
			3, //Turmoil
		};

	public static void handlePrayerDrain(Client c) {
		c.usingPrayer = false;
		double toRemove = 0.0;
		for (int i = 0; i < PRAYER_DRAIN.length; i++) {
			if (c.prayerActive[i]) {
				toRemove += PRAYER_DRAIN[i] / 10;
				c.usingPrayer = true;
			}
		}
		if (toRemove > 0) {
			toRemove /= (1 + (0.035 * c.playerBonus[11]));
		}
		c.prayerPoint -= toRemove;
		if (c.prayerPoint <= 0) {
			c.prayerPoint = 1.0 + c.prayerPoint;
			c.getCombat().reducePrayerLevel();
		}
	}
	public static void handleCurseDrain(Client c) {
		c.usingPrayer = false;
		double toRemove = 0.0;
		for(int i = 0; i < CURSE_DRAIN.length; i++) {
			if(c.curseActive[i]) { 
				toRemove += CURSE_DRAIN[i]/10;
				c.usingPrayer = true;
			}
		}
		if (toRemove > 0) {
			toRemove /= (1 + (0.035 * c.playerBonus[11]));		
		}
		c.prayerPoint -= toRemove;
		if (c.prayerPoint <= 0) {
			c.prayerPoint = 1.0 + c.prayerPoint;
			c.getCombat().reducePrayerLevel();
		}
	}
}
