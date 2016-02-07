package server.util;

import java.util.Random;

import server.model.players.Client;

public class MagicFormula {

	private static Random r = new Random();

	public static int magicMaxHit(Client client) {
		int maxHit = client.MAGIC_SPELLS[client.spellId][6];
		/**
		 * Chaos gauntlets
		 */
		if (client.getEquipment()[9] == 777 && isBoltSpell(client)) {
			maxHit += 3;
		}
		/**
		 * Occult necklace
		 */
		if (client.getEquipment()[2] == 12002) {
			maxHit += ((int) maxHit * .10);
		}

		/**
		 * Staff of the dead etc..
		 */
		if (client.getEquipment()[3] == 11791
				|| client.getEquipment()[3] == 12904) {
			maxHit += ((int) maxHit * .15);
		}
		/**
		 * Amulet of the damned + ahrims boost
		 */
		if (client.getEquipment()[2] == 12853 && wearingArhims(client)) {
			if (r.nextInt(4) == 0) {
				maxHit += ((int) maxHit * .30);
			}
		}
		client.sendMessage("Max Magic Hit: " + maxHit);
		return (int) Math.floor(maxHit);
	}

	private static boolean isBoltSpell(Client client) {
		switch (client.MAGIC_SPELLS[client.spellId][0]) {

		case 1160:
		case 1163:
		case 1166:
		case 1169:
			return true;

		default:
			return false;
		}
	}

	private static boolean wearingArhims(Client client) {
		return client.getEquipment()[client.playerHat] == 4708
				&& client.getEquipment()[3] == 4710
				&& client.getEquipment()[client.playerChest] == 4712
				&& client.getEquipment()[client.playerLegs] == 4713;
	}
}
