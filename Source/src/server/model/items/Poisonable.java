package server.model.items;

import server.model.players.Client;

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

	private static boolean isPoisionable(Client c, int wep) {
		String wepName = c.getItems().getItemName(wep);
		for (int i = 0; i < poisionData.length; i++) {
			if (wepName.toLowerCase().contains(poisionData[i][0])) {
				return true;
			}
		}
		return false;
	}

	public static boolean useItemonItem(Client c, int id, int id2) {
		String wep = null;
		String poisionPrefix = null;
		int amount = 5;
		int toPoision = 0;
		int poisionVial = 0;

		if (isPoisionable(c,id)) {
			wep = c.getItems().getItemName(id);
			poisionPrefix = getPoisionPrefix(id2);
			if (!c.getItems().isStackable(id)) {
				amount = 1;
			}
			poisionVial = id2;
			toPoision = id;
		}
		if (isPoisionable(c,id2)) {
			wep = c.getItems().getItemName(id);
			poisionPrefix = getPoisionPrefix(id);
			if (!c.getItems().isStackable(id2)) {
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
			c.sendMessage("This weapon has already been poisoned.");
			return false;
		}
		if (c.getItems().getItemAmount(toPoision) < amount) {
			amount = c.getItems().getItemAmount(toPoision);
		}

		int itemToAdd = c.getItems().getItemId(wep + poisionPrefix);

		c.getItems().deleteItem(toPoision, amount);
		c.getItems().deleteItem(poisionVial, 1);
		c.getItems().addItem(itemToAdd, amount);
		c.getItems().addItem(229, 1);
		c.sendMessage("You drop the poison over the weapon.");
		return true;
	}

}
