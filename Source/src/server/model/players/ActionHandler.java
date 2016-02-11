package server.model.players;

import server.world.locations.LocationController;

public class ActionHandler {

	private Client c;

	public ActionHandler(Client Client) {
		this.c = Client;
	}

	public void firstClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		LocationController.sendFirstClickObject(c, objectType);
		c.sendMessage("[object 1] - " + objectType + " [posX] - " + obX + " [posY] - " + obY);
		switch (objectType) {

		/**
		 * Opening the bank when clicking on bank booths, etc.
		 */
//		case 2213:
		case 14367:
		case 11758:
		case 3193:
			c.getPA().openUpBank();
			break;

		/**
		 * Aquring god capes.
		 */
		case 2873:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Saradomin blesses you with a cape.");
				c.getItems().addItem(2412, 1);
			}
			break;
		case 2875:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Guthix blesses you with a cape.");
				c.getItems().addItem(2413, 1);
			}
			break;
		case 2874:
			if (!c.getItems().ownsCape()) {
				c.startAnimation(645);
				c.sendMessage("Zamorak blesses you with a cape.");
				c.getItems().addItem(2414, 1);
			}
			break;


		case 1516:
		case 1519:
			if (c.objectY == 9698) {
				if (c.absY >= c.objectY)
					c.getPA().walkTo(0, -1);
				else
					c.getPA().walkTo(0, 1); 
			}
			break;
		case 9319:
			if (c.heightLevel == 0)
				c.getPA().movePlayer(c.absX, c.absY, 1);
			else if (c.heightLevel == 1)
				c.getPA().movePlayer(c.absX, c.absY, 2);
			break;

		case 9320:
			if (c.heightLevel == 1)
				c.getPA().movePlayer(c.absX, c.absY, 0);
			else if (c.heightLevel == 2)
				c.getPA().movePlayer(c.absX, c.absY, 1);
			break;

		case 4496:
		case 4494:
			if (c.heightLevel == 2) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 0);
			}
			break;

		case 4493:
			if (c.heightLevel == 0) {
				c.getPA().movePlayer(c.absX - 5, c.absY, 1);
			} else if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
			break;

		case 4495:
			if (c.heightLevel == 1) {
				c.getPA().movePlayer(c.absX + 5, c.absY, 2);
			}
			break;

		case 5126:
			if (c.absY == 3554)
				c.getPA().walkTo(0, 1);
			else
				c.getPA().walkTo(0, -1);
			break;
		}
	}

	public void secondClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		c.sendMessage("[object 2] -  " + objectType);
		LocationController.sendSecondClickObject(c, objectType);
		switch (objectType) {

		/**
		 * Opening the bank.
		 */
		case 2213:
		case 14367:
		case 11758:
			c.getPA().openUpBank();
			break;

		}
	}

	public void thirdClickObject(int objectType, int obX, int obY) {
		c.clickObjectType = 0;
		c.sendMessage("[object 3] -  " + objectType);
		LocationController.sendThirdClickObject(c, objectType);
		switch (objectType) {
		}
	}

	public void firstClickNpc(int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		c.sendMessage("[npc 1] - " + npcType);
		LocationController.sendFirstClickNpc(c, npcType);
		switch (npcType) {

		/**
		 * Shops.
		 */
		case 2566:
			c.getShops().openSkillCape();
			break;

		}
	}

	public void secondClickNpc(int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		c.sendMessage("[npc 2] - " + npcType);
		LocationController.sendSecondClickNpc(c, npcType);
		switch (npcType) {

		}
	}

	public void thirdClickNpc(int npcType) {
		c.clickNpcType = 0;
		c.npcClickIndex = 0;
		c.sendMessage("[npc 3] - " + npcType);
		LocationController.sendThirdClickNpc(c, npcType);
		switch (npcType) {

		}
	}
}