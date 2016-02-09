package server.model.content;

import server.model.players.Client;

public class GoldEnchating 
{
	private static int[][] ENCHANT_DATA =
		{
				{590, 2946}, {2347,2949}
		};
	public static void executeEnchantment (Client c)
	{
		for (int i = 0; i < ENCHANT_DATA.length; i++) {
			if (!(c.playerMagic < 70 && c.playerSmithing < 80))
			{
				c.sendMessage("You need a Magic level of 70, and a Smithing level of 80 to enchant this " + c.getItems().getItemName(ENCHANT_DATA[i][0]));
				return;
			}
			c.getItems().deleteItem(ENCHANT_DATA[i][0], 1);
			c.getItems().deleteItem(2357, 1);
			c.getItems().addItem(ENCHANT_DATA[i][1], 1);
		}
	}
	
	public static boolean isEnchantable (int itemOne, int itemTwo)
	{
		for (int i = 0; i < ENCHANT_DATA.length; i++) {
			if (itemOne == 2357 && itemTwo == ENCHANT_DATA[i][0])
			{
				return true;
			}
		}
		return false;
	}
}
