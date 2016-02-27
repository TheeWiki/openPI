package server.model.players.skills.runecrafting;

import server.model.players.Player;

public class TalismanHandler {
	
	public static enum talismanData {
		
		AIR(1438, new int[] {2844, 4830}),
		MIND(1448, new int[] {2782, 4841}),
		WATER(1444, new int[] {2712, 4836}),
		EARTH(1440, new int[] {2654, 4841}),
		FIRE(1442, new int[] {2585, 4834}),
		BODY(1446, new int[] {2525, 4828}),
		COSMIC(1454, new int[] {2138, 4833}),
		CHAOS(1452, new int[] {2267, 4842}),
		NATURE(1462, new int[] {2396, 4841}),
		LAW(1458, new int[] {2464, 4827}),
		DEATH(1456, new int[] {2207, 4834}),
		BLOOD(1450, new int[] {2457, 4895});
			
		private int talismanId;
		private int[] coordinates;
		
		private talismanData(int talismanId, int[] coordinates) {
			this.talismanId = talismanId;
			this.coordinates = coordinates;
		}
		
		public int getTalisman() {
			return talismanId;
		}
		
		public int getXCoordinate() {
			return coordinates[0];
		}
		
		public int getYCoordinate() {
			return coordinates[1];
		}
	}
	
	public static void handleTalisman(final Player player, final int itemId) {
		for (final talismanData t : talismanData.values()) {
			if (itemId == t.getTalisman()) {
				player.getPA().spellTeleport(t.getXCoordinate(), t.getYCoordinate(), 0);
			}
		}
	}
}