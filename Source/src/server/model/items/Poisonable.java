package server.model.items;

import server.model.players.Player;

public class Poisonable {
    
    private static final String[][] poisionData = {
        {"arrow"},
        {"dagger"},
        {"spear"},
        {"knife"},
        {"dart"},
        {"javelin"},
        {"bolts"}
    };

	private static String getPoisionPrefix(int poisionId) {

		switch (poisionId) {
		case 187:
			return "(p)";
		case 5937:
			return "(p+)";
		case 5940:
			return "(p++)";
		}
		return null;
	}

	private static boolean isPoisionable(Player player, int wep) {
		String wepName = player.getItems().getItemName(wep);
		for (int i = 0; i < poisionData.length; i++) {
			if (wepName.toLowerCase().contains(poisionData[i][0])) {
				return true;
			}
		}
		return false;
	}

	public static boolean useItemonItem(Player player, int id, int id2) {
		String wep = null;
		String poisionPrefix = null;
		int amount = 5;
		int toPoision = 0;
		int poisionVial = 0;

		if (isPoisionable(player,id)) {
			wep = player.getItems().getItemName(id);
			poisionPrefix = getPoisionPrefix(id2);
			if (!player.getItems().isStackable(id)) {
				amount = 1;
			}
			poisionVial = id2;
			toPoision = id;
		}
		if (isPoisionable(player,id2)) {
			wep = player.getItems().getItemName(id);
			poisionPrefix = getPoisionPrefix(id);
			if (!player.getItems().isStackable(id2)) {
				amount = 1;
			}
			poisionVial = id;
			toPoision = id2;
		}
		if (poisionPrefix == null) {
			return false;
		}
		if (wep.toLowerCase().contains("(p++)") || wep.toLowerCase().contains("(p+)")
				|| wep.toLowerCase().contains("(p)")) {
			player.getActionSender().sendMessage("This weapon has already been poisoned.");
			return false;
		}
		if (player.getItems().getItemAmount(toPoision) < amount) {
			amount = player.getItems().getItemAmount(toPoision);
		}

		int itemToAdd = player.getItems().getItemId(wep + poisionPrefix);

		player.getItems().deleteItem(toPoision, amount);
		player.getItems().deleteItem(poisionVial, 1);
		player.getItems().addItem(itemToAdd, amount);
		player.getItems().addItem(229, 1);
		player.getActionSender().sendMessage("You drop the poison over the weapon.");
		return true;
	}

}
