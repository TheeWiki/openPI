package server.model.items;

import server.Constants;
import server.Server;
import server.model.minigames.castle_wars.CastleWars;
import server.model.minigames.duel_arena.Rules;
import server.model.npcs.NPCHandler;
import server.model.players.EquipmentListener;
import server.model.players.Player;
import server.model.players.PlayerHandler;
import server.util.Misc;

/**
 * Indicates Several Usage Of Items
 * 
 * @author Sanity Revised by Shawn Notes by Shawn
 */
public class ItemAssistant {

	private Player player;

	public ItemAssistant(Player Player) {
		this.player = Player;
	}

	/**
	 * Trimmed and untrimmed skillcapes.
	 */
	public int[][] skillcapes = { { 9747, 9748 }, // Attack
			{ 9753, 9754 }, // Defence
			{ 9750, 9751 }, // Strength
			{ 9768, 9769 }, // Hitpoints
			{ 9756, 9757 }, // Range
			{ 9759, 9760 }, // Prayer
			{ 9762, 9763 }, // Magic
			{ 9801, 9802 }, // Cooking
			{ 9807, 9808 }, // Woodcutting
			{ 9783, 9784 }, // Fletching
			{ 9798, 9799 }, // Fishing
			{ 9804, 9805 }, // Firemaking
			{ 9780, 9781 }, // Crafting
			{ 9795, 9796 }, // Smithing
			{ 9792, 9793 }, // Mining
			{ 9774, 9775 }, // Herblore
			{ 9771, 9772 }, // Agility
			{ 9777, 9778 }, // Thieving
			{ 9786, 9787 }, // Slayer
			{ 9810, 9811 }, // Farming
			{ 9765, 9766 } // Runecraft
	};

	/**
	 * Broken barrows items.
	 */
	public int[][] brokenBarrows = { { 4708, 4860 }, { 4710, 4866 }, { 4712, 4872 }, { 4714, 4878 }, { 4716, 4884 },
			{ 4720, 4896 }, { 4718, 4890 }, { 4720, 4896 }, { 4722, 4902 }, { 4732, 4932 }, { 4734, 4938 },
			{ 4736, 4944 }, { 4738, 4950 }, { 4724, 4908 }, { 4726, 4914 }, { 4728, 4920 }, { 4730, 4926 },
			{ 4745, 4956 }, { 4747, 4926 }, { 4749, 4968 }, { 4751, 4794 }, { 4753, 4980 }, { 4755, 4986 },
			{ 4757, 4992 }, { 4759, 4998 } };

	public boolean addBankItem(int item, double amount) {
		if (amount < 1) {
			amount = 1;
		}
		if (item <= 0) {
			return false;
		}
		for (int i = 0; i < player.bankItems.length; i++) {
			if ((player.bankItems[i] == (item + 1)) && (player.bankItems[i] > 0)
					&& (player.bankItemsN[i] < Constants.MAXITEM_AMOUNT)) {
				player.bankItems[i] = (item + 1);
				if (((player.bankItemsN[i] + amount) < Constants.MAXITEM_AMOUNT) && ((player.bankItemsN[i] + amount) > -1)) {
					player.bankItemsN[i] += amount;
				} else {
					int old = player.bankItemsN[i];
					player.bankItemsN[i] = Constants.MAXITEM_AMOUNT;
					if ((old + amount) - (long) Constants.MAXITEM_AMOUNT > 0) {
						addBankItem(item, (old + amount) - (long) Constants.MAXITEM_AMOUNT);
					}
				}
				return true;
			}
		}
		for (int i = 0; i < player.bankItems.length; i++) {
			if (player.bankItems[i] <= 0) {
				player.bankItems[i] = item + 1;
				if ((amount < Constants.MAXITEM_AMOUNT) && (amount > -1)) {
					player.bankItemsN[i] = 1;
					if (amount > 1) {
						addBankItem(item, amount - 1);
						return true;
					}
				} else {
					int old = 0;
					player.bankItemsN[i] = Constants.MAXITEM_AMOUNT;
					if ((old + amount) - (long) Constants.MAXITEM_AMOUNT > 0) {
						addBankItem(item, (old + amount) - (long) Constants.MAXITEM_AMOUNT);
					}
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Empties all of (a) player's items.
	 * 
	 * @param WriteFrame
	 */
	public void resetItems(int WriteFrame) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(WriteFrame);
			player.getOutStream().writeWord(player.playerItems.length);
			for (int i = 0; i < player.playerItems.length; i++) {
				if (player.playerItemsN[i] > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord_v2(player.playerItemsN[i]);
				} else {
					player.getOutStream().writeByte(player.playerItemsN[i]);
				}
				player.getOutStream().writeWordBigEndianA(player.playerItems[i]);
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}
		// }
	}

	/**
	 * Counts (a) player's items.
	 * 
	 * @param itemID
	 * @return itemId
	 */
	public int getItemCount(int itemID) {
		int count = 0;
		for (int j = 0; j < player.playerItems.length; j++) {
			if (player.playerItems[j] == itemID + 1) {
				count += player.playerItemsN[j];
			}
		}
		return count;
	}

	/**
	 * Gets the bonus' of an item.
	 */
	public void writeBonus() {
		int offset = 0;
		String send = "";
		for (int i = 0; i < player.playerBonus.length; i++) {
			if (player.playerBonus[i] >= 0) {
				send = BONUS_NAMES[i] + ": +" + player.playerBonus[i];
			} else {
				send = BONUS_NAMES[i] + ": -" + java.lang.Math.abs(player.playerBonus[i]);
			}

			if (i == 10) {
				offset = 1;
			}
			player.getPA().sendFrame126(send, (1675 + i + offset));
		}

	}

	/**
	 * Gets the total count of (a) player's items.
	 * 
	 * @param itemID
	 * @return itemID
	 */
	public int getTotalCount(int itemID) {
		int count = 0;
		for (int j = 0; j < player.playerItems.length; j++) {
			if (Item.itemIsNote[itemID + 1]) {
				if (itemID + 2 == player.playerItems[j])
					count += player.playerItemsN[j];
			}
			if (!Item.itemIsNote[itemID + 1]) {
				if (itemID + 1 == player.playerItems[j]) {
					count += player.playerItemsN[j];
				}
			}
		}
		for (int j = 0; j < player.bankItems.length; j++) {
			if (player.bankItems[j] == itemID + 1) {
				count += player.bankItemsN[j];
			}
		}
		return count;
	}

	/**
	 * Send the items kept on death.
	 */
	public void sendItemsKept() {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(6963);
			player.getOutStream().writeWord(player.itemKeptId.length);
			for (int i = 0; i < player.itemKeptId.length; i++) {
				if (player.playerItemsN[i] > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord_v2(1);
				} else {
					player.getOutStream().writeByte(1);
				}
				if (player.itemKeptId[i] > 0) {
					player.getOutStream().writeWordBigEndianA(player.itemKeptId[i] + 1);
				} else {
					player.getOutStream().writeWordBigEndianA(0);
				}
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		} // }
	}

	/**
	 * Item kept on death
	 * 
	 * @param keepItem
	 * @param deleteItem
	 */
	public void keepItem(int keepItem, boolean deleteItem) {
		int value = 0;
		int item = 0;
		int slotId = 0;
		boolean itemInInventory = false;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] - 1 > 0) {
				int inventoryItemValue = player.getShops().getItemShopValue(player.playerItems[i] - 1);
				if (inventoryItemValue > value && (!player.invSlot[i])) {
					value = inventoryItemValue;
					item = player.playerItems[i] - 1;
					slotId = i;
					itemInInventory = true;
				}
			}
		}
		for (int i1 = 0; i1 < player.playerEquipment.length; i1++) {
			if (player.playerEquipment[i1] > 0) {
				int equipmentItemValue = player.getShops().getItemShopValue(player.playerEquipment[i1]);
				if (equipmentItemValue > value && (!player.equipSlot[i1])) {
					value = equipmentItemValue;
					item = player.playerEquipment[i1];
					slotId = i1;
					itemInInventory = false;
				}
			}
		}
		if (itemInInventory) {
			player.invSlot[slotId] = true;
			if (deleteItem) {
				deleteItem(player.playerItems[slotId] - 1, getItemSlot(player.playerItems[slotId] - 1), 1);
			}
		} else {
			player.equipSlot[slotId] = true;
			if (deleteItem) {
				deleteEquipment(item, slotId);
			}
		}
		player.itemKeptId[keepItem] = item;
	}

	/**
	 * Reset items kept on death.
	 **/
	public void resetKeepItems() {
		for (int i = 0; i < player.itemKeptId.length; i++) {
			player.itemKeptId[i] = -1;
		}
		for (int i1 = 0; i1 < player.invSlot.length; i1++) {
			player.invSlot[i1] = false;
		}
		for (int i2 = 0; i2 < player.equipSlot.length; i2++) {
			player.equipSlot[i2] = false;
		}
	}

	/**
	 * Deletes all of a player's items.
	 **/
	public void deleteAllItems() {
		for (int i1 = 0; i1 < player.playerEquipment.length; i1++) {
			deleteEquipment(player.playerEquipment[i1], i1);
		}
		for (int i = 0; i < player.playerItems.length; i++) {
			deleteItem(player.playerItems[i] - 1, getItemSlot(player.playerItems[i] - 1), player.playerItemsN[i]);
		}
	}

	/**
	 * Drops all items for a killer.
	 **/
	public void dropAllItems() {
		Player o = (Player) PlayerHandler.players[player.killerId];

		for (int i = 0; i < player.playerItems.length; i++) {
			if (o != null) {
				if (tradeable(player.playerItems[i] - 1)) {
					Server.itemHandler.createGroundItem(o, player.playerItems[i] - 1, player.getX(), player.getY(), player.playerItemsN[i],
							player.killerId);
				} else {
					if (specialCase(player.playerItems[i] - 1))
						Server.itemHandler.createGroundItem(o, 995, player.getX(), player.getY(),
								getUntradePrice(player.playerItems[i] - 1), player.killerId);
					Server.itemHandler.createGroundItem(player, player.playerItems[i] - 1, player.getX(), player.getY(), player.playerItemsN[i],
							player.playerId);
				}
			} else {
				Server.itemHandler.createGroundItem(player, player.playerItems[i] - 1, player.getX(), player.getY(), player.playerItemsN[i],
						player.playerId);
			}
		}
		for (int e = 0; e < player.playerEquipment.length; e++) {
			if (o != null) {
				if (tradeable(player.playerEquipment[e])) {
					Server.itemHandler.createGroundItem(o, player.playerEquipment[e], player.getX(), player.getY(),
							player.playerEquipmentN[e], player.killerId);
				} else {
					if (specialCase(player.playerEquipment[e]))
						Server.itemHandler.createGroundItem(o, 995, player.getX(), player.getY(),
								getUntradePrice(player.playerEquipment[e]), player.killerId);
					Server.itemHandler.createGroundItem(player, player.playerEquipment[e], player.getX(), player.getY(),
							player.playerEquipmentN[e], player.playerId);
				}
			} else {
				Server.itemHandler.createGroundItem(player, player.playerEquipment[e], player.getX(), player.getY(), player.playerEquipmentN[e],
						player.playerId);
			}
		}
		if (o != null) {
			Server.itemHandler.createGroundItem(o, 526, player.getX(), player.getY(), 1, player.killerId);
		}
	}

	/**
	 * Untradable items with a special currency. (Tokkel, etc)
	 * 
	 * @param item
	 * @return item
	 */
	public int getUntradePrice(int item) {
		switch (item) {
		case 2518:
		case 2524:
		case 2526:
			return 100000;
		case 2520:
		case 2522:
			return 150000;
		}
		return 0;
	}

	/**
	 * Special items with currency.
	 * 
	 * @param itemId
	 * @return itemId
	 */
	public boolean specialCase(int itemId) {
		switch (itemId) {
		case 2518:
		case 2520:
		case 2522:
		case 2524:
		case 2526:
			return true;
		}
		return false;
	}

	/**
	 * Voided items. (Not void knight items..)
	 * 
	 * @param itemId
	 */
	public void addToVoidList(int itemId) {
		switch (itemId) {
		case 2518:
			player.voidStatus[0]++;
			break;
		case 2520:
			player.voidStatus[1]++;
			break;
		case 2522:
			player.voidStatus[2]++;
			break;
		case 2524:
			player.voidStatus[3]++;
			break;
		case 2526:
			player.voidStatus[4]++;
			break;
		}
	}

	/**
	 * Handles tradable items.
	 * 
	 * @param itemId
	 * @return
	 */
	public boolean tradeable(int itemId) {
		for (int j = 0; j < Constants.ITEM_TRADEABLE.length; j++) {
			if (itemId == Constants.ITEM_TRADEABLE[j])
				return false;
		}
		return true;
	}

	/**
	 * Adds an item to a player's inventory.
	 * 
	 * @param item
	 * @param amount
	 * @return
	 */
	public boolean addItem(int item, int amount) {
		// synchronized(c) {
		if (item == CastleWars.SARA_BANNER || item == CastleWars.ZAMMY_BANNER)
			return false;
		if (amount < 1) {
			amount = 1;
		}
		if (item <= 0) {
			return false;
		}
		if ((((freeSlots() >= 1) || playerHasItem(item, 1)) && Item.itemStackable[item])
				|| ((freeSlots() > 0) && !Item.itemStackable[item])) {
			for (int i = 0; i < player.playerItems.length; i++) {
				if ((player.playerItems[i] == (item + 1)) && Item.itemStackable[item] && (player.playerItems[i] > 0)) {
					player.playerItems[i] = (item + 1);
					if (((player.playerItemsN[i] + amount) < Constants.MAXITEM_AMOUNT)
							&& ((player.playerItemsN[i] + amount) > -1)) {
						player.playerItemsN[i] += amount;
					} else {
						player.playerItemsN[i] = Constants.MAXITEM_AMOUNT;
					}
					if (player.getOutStream() != null && player != null) {
						player.getOutStream().createFrameVarSizeWord(34);
						player.getOutStream().writeWord(3214);
						player.getOutStream().writeByte(i);
						player.getOutStream().writeWord(player.playerItems[i]);
						if (player.playerItemsN[i] > 254) {
							player.getOutStream().writeByte(255);
							player.getOutStream().writeDWord(player.playerItemsN[i]);
						} else {
							player.getOutStream().writeByte(player.playerItemsN[i]);
						}
						player.getOutStream().endFrameVarSizeWord();
						player.flushOutStream();
					}
					i = 30;
					return true;
				}
			}
			for (int i = 0; i < player.playerItems.length; i++) {
				if (player.playerItems[i] <= 0) {
					player.playerItems[i] = item + 1;
					if ((amount < Constants.MAXITEM_AMOUNT) && (amount > -1)) {
						player.playerItemsN[i] = 1;
						if (amount > 1) {
							player.getItems().addItem(item, amount - 1);
							return true;
						}
					} else {
						player.playerItemsN[i] = Constants.MAXITEM_AMOUNT;
					}
					resetItems(3214);
					i = 30;
					return true;
				}
			}
			return false;
		} else {
			resetItems(3214);
			player.getActionSender().sendMessage("Not enough space in your inventory.");
			return false;
		} // }
	}

	public void addOrDropItem(int item, int amount) {
		if (isStackable(item) && hasFreeSlots(1)) {
			addItem(item, amount);
		} else if (!hasFreeSlots(amount) && !isStackable(item)) {
			Server.itemHandler.createGroundItem(player, item, player.absX, player.absY, amount, player.playerId);
			player.getActionSender().sendMessage("You have no inventory space, so the item(s) appear beneath you.");
		} else {
			addItem(item, amount);
		}
	}

	public boolean hasFreeSlots(int slots) {
		return (freeSlots() >= slots);
	}

	/**
	 * Gets the item type.
	 * 
	 * @param item
	 * @return
	 */
	public String itemType(int item) {
		for (int i = 0; i < Item.capes.length; i++) {
			if (item == Item.capes[i])
				return "cape";
		}
		for (int i = 0; i < Item.hats.length; i++) {
			if (item == Item.hats[i])
				return "hat";
		}
		for (int i = 0; i < Item.boots.length; i++) {
			if (item == Item.boots[i])
				return "boots";
		}
		for (int i = 0; i < Item.gloves.length; i++) {
			if (item == Item.gloves[i])
				return "gloves";
		}
		for (int i = 0; i < Item.shields.length; i++) {
			if (item == Item.shields[i])
				return "shield";
		}
		for (int i = 0; i < Item.amulets.length; i++) {
			if (item == Item.amulets[i])
				return "amulet";
		}
		for (int i = 0; i < Item.arrows.length; i++) {
			if (item == Item.arrows[i])
				return "arrows";
		}
		for (int i = 0; i < Item.rings.length; i++) {
			if (item == Item.rings[i])
				return "ring";
		}
		for (int i = 0; i < Item.body.length; i++) {
			if (item == Item.body[i])
				return "body";
		}
		for (int i = 0; i < Item.legs.length; i++) {
			if (item == Item.legs[i])
				return "legs";
		}
		return "weapon";
	}

	/**
	 * Item bonuses.
	 **/
	public final String[] BONUS_NAMES = { "Stab", "Slash", "Crush", "Magic", "Range", "Stab", "Slash", "Crush", "Magic",
			"Range", "Strength", "Prayer" };

	/**
	 * Resets item bonuses.
	 */
	public void resetBonus() {
		for (int i = 0; i < player.playerBonus.length; i++) {
			player.playerBonus[i] = 0;
		}
	}

	/**
	 * Gets the item bonus from the item.cfg.
	 */
	public void getBonus() {
		for (int i = 0; i < player.playerEquipment.length; i++) {
			if (player.playerEquipment[i] > -1) {
				for (int j = 0; j < Constants.ITEM_LIMIT; j++) {
					if (Server.itemHandler.ItemList[j] != null) {
						if (Server.itemHandler.ItemList[j].itemId == player.playerEquipment[i]) {
							for (int k = 0; k < player.playerBonus.length; k++) {
								player.playerBonus[k] += Server.itemHandler.ItemList[j].Bonuses[k];
							}
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * Weapon type.
	 * 
	 * @param Weapon
	 * @param WeaponName
	 */
	public void sendWeapon(int Weapon, String WeaponName) {
		String WeaponName2 = WeaponName.replaceAll("Bronze", "");
		WeaponName2 = WeaponName2.replaceAll("Iron", "");
		WeaponName2 = WeaponName2.replaceAll("Steel", "");
		WeaponName2 = WeaponName2.replaceAll("Black", "");
		WeaponName2 = WeaponName2.replaceAll("Mithril", "");
		WeaponName2 = WeaponName2.replaceAll("Adamant", "");
		WeaponName2 = WeaponName2.replaceAll("Rune", "");
		WeaponName2 = WeaponName2.replaceAll("Granite", "");
		WeaponName2 = WeaponName2.replaceAll("Dragon", "");
		WeaponName2 = WeaponName2.replaceAll("Drag", "");
		WeaponName2 = WeaponName2.replaceAll("Crystal", "");
		WeaponName2 = WeaponName2.trim();
		/**
		 * Attack styles.
		 */
		if (WeaponName.equals("Unarmed")) {
			player.setSidebarInterface(0, 5855); // punch, kick, block
			player.getPA().sendFrame126(WeaponName, 5857);
		} else if (WeaponName.endsWith("whip")) {
			player.setSidebarInterface(0, 12290); // flick, lash, deflect
			player.getPA().sendFrame246(12291, 200, Weapon);
			player.getPA().sendFrame126(WeaponName, 12293);
		} else if (WeaponName.endsWith("bow") || WeaponName.endsWith("10") || WeaponName.endsWith("full")
				|| WeaponName.startsWith("seercull")) {
			player.setSidebarInterface(0, 1764); // accurate, rapid, longrange
			player.getPA().sendFrame246(1765, 200, Weapon);
			player.getPA().sendFrame126(WeaponName, 1767);
		} else if (WeaponName.startsWith("Staff") || WeaponName.endsWith("staff") || WeaponName.endsWith("wand")) {
			player.setSidebarInterface(0, 328); // spike, impale, smash, block
			player.getPA().sendFrame246(329, 200, Weapon);
			player.getPA().sendFrame126(WeaponName, 331);
		} else if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 5641 || WeaponName2.startsWith("dart")
				|| WeaponName2.startsWith("knife") || WeaponName2.startsWith("javelin")
				|| WeaponName.equalsIgnoreCase("toktz-xil-ul")) {
			player.setSidebarInterface(0, 4446); // accurate, rapid, longrange
			player.getPA().sendFrame246(4447, 200, Weapon);
			player.getPA().sendFrame126(WeaponName, 4449);
		} else if (WeaponName2.startsWith("dagger") || WeaponName2.contains("sword")) {
			player.setSidebarInterface(0, 2276); // stab, lunge, slash, block
			player.getPA().sendFrame246(2277, 200, Weapon);
			player.getPA().sendFrame126(WeaponName, 2279);
		} else if (WeaponName2.startsWith("pickaxe")) {
			player.setSidebarInterface(0, 5570); // spike, impale, smash, block
			player.getPA().sendFrame246(5571, 200, Weapon);
			player.getPA().sendFrame126(WeaponName, 5573);
		} else if (WeaponName2.startsWith("axe") || WeaponName2.startsWith("battleaxe")) {
			player.setSidebarInterface(0, 1698); // chop, hack, smash, block
			player.getPA().sendFrame246(1699, 200, Weapon);
			player.getPA().sendFrame126(WeaponName, 1701);
		} else if (WeaponName2.startsWith("halberd")) {
			player.setSidebarInterface(0, 8460); // jab, swipe, fend
			player.getPA().sendFrame246(8461, 200, Weapon);
			player.getPA().sendFrame126(WeaponName, 8463);
		} else if (WeaponName2.startsWith("Scythe")) {
			player.setSidebarInterface(0, 8460); // jab, swipe, fend
			player.getPA().sendFrame246(8461, 200, Weapon);
			player.getPA().sendFrame126(WeaponName, 8463);
		} else if (WeaponName2.startsWith("spear")) {
			player.setSidebarInterface(0, 4679); // lunge, swipe, pound, block
			player.getPA().sendFrame246(4680, 200, Weapon);
			player.getPA().sendFrame126(WeaponName, 4682);
		} else if (WeaponName2.toLowerCase().contains("mace")) {
			player.setSidebarInterface(0, 3796);
			player.getPA().sendFrame246(3797, 200, Weapon);
			player.getPA().sendFrame126(WeaponName, 3799);

		} else if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4153) {
			player.setSidebarInterface(0, 425); // war hammer equip.
			player.getPA().sendFrame246(426, 200, Weapon);
			player.getPA().sendFrame126(WeaponName, 428);
		} else {
			player.setSidebarInterface(0, 2423); // chop, slash, lunge, block
			player.getPA().sendFrame246(2424, 200, Weapon);
			player.getPA().sendFrame126(WeaponName, 2426);
		}

	}

	/**
	 * Weapon requirements.
	 * 
	 * @param itemName
	 * @param itemId
	 */
	public void getRequirements(String itemName, int itemId) {
		player.attackLevelReq = player.defenceLevelReq = player.strengthLevelReq = player.rangeLevelReq = player.magicLevelReq = 0;
		if (itemName.contains("mystic") || itemName.contains("nchanted")) {
			if (itemName.contains("staff")) {
				player.magicLevelReq = 20;
				player.attackLevelReq = 40;
			} else {
				player.magicLevelReq = 20;
				player.defenceLevelReq = 20;
			}
		}
		if (itemName.contains("infinity")) {
			player.magicLevelReq = 50;
			player.defenceLevelReq = 25;
		}
		if (itemName.contains("splitbark")) {
			player.magicLevelReq = 40;
			player.defenceLevelReq = 40;
		}
		if (itemName.contains("Green")) {
			if (itemName.contains("hide")) {
				player.rangeLevelReq = 40;
				if (itemName.contains("body"))
					player.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("Blue")) {
			if (itemName.contains("hide")) {
				player.rangeLevelReq = 50;
				if (itemName.contains("body"))
					player.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("Red")) {
			if (itemName.contains("hide")) {
				player.rangeLevelReq = 60;
				if (itemName.contains("body"))
					player.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("Black")) {
			if (itemName.contains("hide")) {
				player.rangeLevelReq = 70;
				if (itemName.contains("body"))
					player.defenceLevelReq = 40;
				return;
			}
		}
		if (itemName.contains("bronze")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				player.attackLevelReq = player.defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("iron")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				player.attackLevelReq = player.defenceLevelReq = 1;
			}
			return;
		}
		if (itemName.contains("steel")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				player.attackLevelReq = player.defenceLevelReq = 5;
			}
			return;
		}
		if (itemName.contains("black")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe") && !itemName.contains("vamb") && !itemName.contains("chap")) {
				player.attackLevelReq = player.defenceLevelReq = 10;
			}
			return;
		}
		if (itemName.contains("mithril")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				player.attackLevelReq = player.defenceLevelReq = 20;
			}
			return;
		}
		if (itemName.contains("adamant")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe")) {
				player.attackLevelReq = player.defenceLevelReq = 30;
			}
			return;
		}
		if (itemName.contains("rune")) {
			if (!itemName.contains("knife") && !itemName.contains("dart") && !itemName.contains("javelin")
					&& !itemName.contains("thrownaxe") && !itemName.contains("'bow")) {
				player.attackLevelReq = player.defenceLevelReq = 40;
			}
			return;
		}
		if (itemName.contains("dragon")) {
			if (!itemName.contains("nti-") && !itemName.contains("fire")) {
				player.attackLevelReq = player.defenceLevelReq = 60;
				return;
			}
		}
		if (itemName.contains("crystal")) {
			if (itemName.contains("shield")) {
				player.defenceLevelReq = 70;
			} else {
				player.rangeLevelReq = 70;
			}
			return;
		}
		if (itemName.contains("ahrim")) {
			if (itemName.contains("staff")) {
				player.magicLevelReq = 70;
				player.attackLevelReq = 70;
			} else {
				player.magicLevelReq = 70;
				player.defenceLevelReq = 70;
			}
		}
		if (itemName.contains("karil")) {
			if (itemName.contains("crossbow")) {
				player.rangeLevelReq = 70;
			} else {
				player.rangeLevelReq = 70;
				player.defenceLevelReq = 70;
			}
		}
		if (itemName.contains("godsword")) {
			player.attackLevelReq = 75;
		}
		if (itemName.contains("3rd age") && !itemName.contains("amulet")) {
			player.defenceLevelReq = 60;
		}
		if (itemName.contains("Initiate")) {
			player.defenceLevelReq = 20;
		}
		if (itemName.contains("verac") || itemName.contains("guthan") || itemName.contains("dharok")
				|| itemName.contains("torag")) {

			if (itemName.contains("hammers")) {
				player.attackLevelReq = 70;
				player.strengthLevelReq = 70;
			} else if (itemName.contains("axe")) {
				player.attackLevelReq = 70;
				player.strengthLevelReq = 70;
			} else if (itemName.contains("warspear")) {
				player.attackLevelReq = 70;
				player.strengthLevelReq = 70;
			} else if (itemName.contains("flail")) {
				player.attackLevelReq = 70;
				player.strengthLevelReq = 70;
			} else {
				player.defenceLevelReq = 70;
			}
		}

		switch (itemId) {
		case 8839:
		case 8840:
		case 8842:
		case 11663:
		case 11664:
		case 11665:
			player.attackLevelReq = 42;
			player.rangeLevelReq = 42;
			player.strengthLevelReq = 42;
			player.magicLevelReq = 42;
			player.defenceLevelReq = 42;
			return;
		case 10551:
		case 2503:
		case 2501:
		case 2499:
		case 1135:
			player.defenceLevelReq = 40;
			return;
		case 11235:
		case 6522:
			player.rangeLevelReq = 60;
			break;
		case 6524:
			player.defenceLevelReq = 60;
			break;
		case 11284:
			player.defenceLevelReq = 75;
			return;
		case 6889:
		case 6914:
			player.magicLevelReq = 60;
			break;
		case 861:
			player.rangeLevelReq = 50;
			break;
		case 10828:
			player.defenceLevelReq = 55;
			break;
		case 11724:
		case 11726:
		case 11728:
			player.defenceLevelReq = 65;
			break;
		case 3751:
		case 3749:
		case 3755:
			player.defenceLevelReq = 40;
			break;

		case 7462:
		case 7461:
			player.defenceLevelReq = 40;
			break;
		case 8846:
			player.defenceLevelReq = 5;
			break;
		case 8847:
			player.defenceLevelReq = 10;
			break;
		case 8848:
			player.defenceLevelReq = 20;
			break;
		case 8849:
			player.defenceLevelReq = 30;
			break;
		case 8850:
			player.defenceLevelReq = 40;
			break;

		case 7460:
			player.defenceLevelReq = 40;
			break;

		case 837:
			player.rangeLevelReq = 61;
			break;

		case 4151:
			player.attackLevelReq = 70;
			return;

		case 6724:
			player.rangeLevelReq = 60;
			return;
		case 4153:
			player.attackLevelReq = 50;
			player.strengthLevelReq = 50;
			return;
		}
	}

	/**
	 * Two handed weapon check.
	 * 
	 * @param itemName
	 * @param itemId
	 * @return
	 */
	public boolean is2handed(String itemName, int itemId) {
		if (itemName.contains("ahrim") || itemName.contains("karil") || itemName.contains("verac")
				|| itemName.contains("guthan") || itemName.contains("dharok") || itemName.contains("torag")) {
			return true;
		}
		if (itemName.contains("longbow") || itemName.contains("shortbow") || itemName.contains("ark bow")) {
			return true;
		}
		if (itemName.contains("crystal")) {
			return true;
		}
		if (itemName.contains("godsword") || itemName.contains("aradomin sword") || itemName.contains("2h")
				|| itemName.contains("spear")) {
			return true;
		}
		switch (itemId) {
		case 6724:
		case 11730:
		case 4153:
		case 6528:
		case 14484:
			return true;
		}
		return false;
	}

	/**
	 * Adds special attack bar to special attack weapons. Removes special attack
	 * bar to weapons that do not have special attacks.
	 * 
	 * @param weapon
	 */
	public void addSpecialBar(int weapon) {
		switch (weapon) {

		case 4151: // whip
			player.getPA().sendFrame171(0, 12323);
			specialAmount(weapon, player.specAmount, 12335);
			break;

		case 859: // magic bows
		case 861:
		case 11235:
			player.getPA().sendFrame171(0, 7549);
			specialAmount(weapon, player.specAmount, 7561);
			break;

		case 4587: // dscimmy
			player.getPA().sendFrame171(0, 7599);
			specialAmount(weapon, player.specAmount, 7611);
			break;

		case 3204: // d hally
			player.getPA().sendFrame171(0, 8493);
			specialAmount(weapon, player.specAmount, 8505);
			break;

		case 1377: // d battleaxe
			player.getPA().sendFrame171(0, 7499);
			specialAmount(weapon, player.specAmount, 7511);
			break;

		case 4153: // gmaul
			player.getPA().sendFrame171(0, 7474);
			specialAmount(weapon, player.specAmount, 7486);
			break;

		case 1249: // dspear
			player.getPA().sendFrame171(0, 7674);
			specialAmount(weapon, player.specAmount, 7686);
			break;

		case 1215:// dragon dagger
		case 1231:
		case 5680:
		case 5698:
		case 1305: // dragon long
		case 11694:
		case 11698:
		case 11700:
		case 11730:
		case 11696:
			player.getPA().sendFrame171(0, 7574);
			specialAmount(weapon, player.specAmount, 7586);
			break;

		case 1434: // dragon mace
			player.getPA().sendFrame171(0, 7624);
			specialAmount(weapon, player.specAmount, 7636);
			break;

		default:
			player.getPA().sendFrame171(1, 7624); // mace interface
			player.getPA().sendFrame171(1, 7474); // hammer, gmaul
			player.getPA().sendFrame171(1, 7499); // axe
			player.getPA().sendFrame171(1, 7549); // bow interface
			player.getPA().sendFrame171(1, 7574); // sword interface
			player.getPA().sendFrame171(1, 7599); // scimmy sword interface, for most
												// swords
			player.getPA().sendFrame171(1, 8493);
			player.getPA().sendFrame171(1, 12323); // whip interface
			break;
		}
	}

	/**
	 * Special attack bar filling amount.
	 * 
	 * @param weapon
	 * @param specAmount
	 * @param barId
	 */
	public void specialAmount(int weapon, double specAmount, int barId) {
		player.specBarId = barId;
		player.getPA().sendFrame70(specAmount >= 10 ? 500 : 0, 0, (--barId));
		player.getPA().sendFrame70(specAmount >= 9 ? 500 : 0, 0, (--barId));
		player.getPA().sendFrame70(specAmount >= 8 ? 500 : 0, 0, (--barId));
		player.getPA().sendFrame70(specAmount >= 7 ? 500 : 0, 0, (--barId));
		player.getPA().sendFrame70(specAmount >= 6 ? 500 : 0, 0, (--barId));
		player.getPA().sendFrame70(specAmount >= 5 ? 500 : 0, 0, (--barId));
		player.getPA().sendFrame70(specAmount >= 4 ? 500 : 0, 0, (--barId));
		player.getPA().sendFrame70(specAmount >= 3 ? 500 : 0, 0, (--barId));
		player.getPA().sendFrame70(specAmount >= 2 ? 500 : 0, 0, (--barId));
		player.getPA().sendFrame70(specAmount >= 1 ? 500 : 0, 0, (--barId));
		updateSpecialBar();
		sendWeapon(weapon, getItemName(weapon));
	}

	/**
	 * Special attack text.
	 **/
	public void updateSpecialBar() {
		if (player.usingSpecial) {
			player.getPA().sendFrame126("" + (player.specAmount >= 2 ? "@yel@S P" : "@bla@S P") + ""
					+ (player.specAmount >= 3 ? "@yel@ E" : "@bla@ E") + "" + (player.specAmount >= 4 ? "@yel@ C I" : "@bla@ C I")
					+ "" + (player.specAmount >= 5 ? "@yel@ A L" : "@bla@ A L") + ""
					+ (player.specAmount >= 6 ? "@yel@  A" : "@bla@  A") + ""
					+ (player.specAmount >= 7 ? "@yel@ T T" : "@bla@ T T") + "" + (player.specAmount >= 8 ? "@yel@ A" : "@bla@ A")
					+ "" + (player.specAmount >= 9 ? "@yel@ C" : "@bla@ C") + ""
					+ (player.specAmount >= 10 ? "@yel@ K" : "@bla@ K"), player.specBarId);
		} else {
			player.getPA().sendFrame126("@bla@S P E C I A L  A T T A C K", player.specBarId);
		}
	}

	/**
	 * Wielding items.
	 * 
	 * @param wearID
	 * @param slot
	 * @return slot
	 */
	public boolean wearItem(int wearID, int slot) {
		// synchronized(c) {
		int targetSlot = 0;
		boolean canWearItem = true;

		if (player.playerItems[slot] == (wearID + 1)) {
			if (CastleWars.isInCw(player) || CastleWars.isInCwWait(player)) {
				if (targetSlot == EquipmentListener.CAPE_SLOT.getSlot() || targetSlot == EquipmentListener.HAT_SLOT.getSlot()) {
					player.getActionSender().sendMessage("You can't wear your own capes or hats in a Castle Wars Game!");
					return false;
				}
			}
			getRequirements(getItemName(wearID).toLowerCase(), wearID);
			targetSlot = Item.targetSlots[wearID];
			if (itemType(wearID).equalsIgnoreCase("cape")) {
				targetSlot = 1;
			} else if (itemType(wearID).equalsIgnoreCase("hat")) {
				targetSlot = 0;
			} else if (itemType(wearID).equalsIgnoreCase("amulet")) {
				targetSlot = 2;
			} else if (itemType(wearID).equalsIgnoreCase("arrows")) {
				targetSlot = 13;
			} else if (itemType(wearID).equalsIgnoreCase("body")) {
				targetSlot = 4;
			} else if (itemType(wearID).equalsIgnoreCase("shield")) {
				targetSlot = 5;
			} else if (itemType(wearID).equalsIgnoreCase("legs")) {
				targetSlot = 7;
			} else if (itemType(wearID).equalsIgnoreCase("gloves")) {
				targetSlot = 9;
			} else if (itemType(wearID).equalsIgnoreCase("boots")) {
				targetSlot = 10;
			} else if (itemType(wearID).equalsIgnoreCase("ring")) {
				targetSlot = 12;
			} else {
				targetSlot = 3;
			}

			if (player.duelRule[Rules.HATS_RULE.getRule()] && targetSlot == 0) {
				player.getActionSender().sendMessage("Wearing hats has been disabled in this duel!");
				return false;
			}
			if (player.duelRule[Rules.CAPES_RULE.getRule()] && targetSlot == 1) {
				player.getActionSender().sendMessage("Wearing capes has been disabled in this duel!");
				return false;
			}
			if (player.duelRule[Rules.AMULET_RULE.getRule()] && targetSlot == 2) {
				player.getActionSender().sendMessage("Wearing amulets has been disabled in this duel!");
				return false;
			}
			if (player.duelRule[Rules.WEAPONS_RULE.getRule()] && targetSlot == 3) {
				player.getActionSender().sendMessage("Wielding weapons has been disabled in this duel!");
				return false;
			}
			if (player.duelRule[Rules.BODY_ARMOR_RULE.getRule()] && targetSlot == 4) {
				player.getActionSender().sendMessage("Wearing bodies has been disabled in this duel!");
				return false;
			}
			if ((player.duelRule[Rules.SHIELD_RULE.getRule()] && targetSlot == 5)
					|| (player.duelRule[16] && is2handed(getItemName(wearID).toLowerCase(), wearID))) {
				player.getActionSender().sendMessage("Wearing shield has been disabled in this duel!");
				return false;
			}
			if (player.duelRule[Rules.LEGS_ARMOR.getRule()] && targetSlot == 7) {
				player.getActionSender().sendMessage("Wearing legs has been disabled in this duel!");
				return false;
			}
			if (player.duelRule[Rules.GLOVES_RULE.getRule()] && targetSlot == 9) {
				player.getActionSender().sendMessage("Wearing gloves has been disabled in this duel!");
				return false;
			}
			if (player.duelRule[Rules.BOOTS_RULE.getRule()] && targetSlot == 10) {
				player.getActionSender().sendMessage("Wearing boots has been disabled in this duel!");
				return false;
			}
			if (player.duelRule[Rules.RINGS_RULE.getRule()] && targetSlot == 12) {
				player.getActionSender().sendMessage("Wearing rings has been disabled in this duel!");
				return false;
			}
			if (player.duelRule[Rules.ARROWS_RULE.getRule()] && targetSlot == 13) {
				player.getActionSender().sendMessage("Wearing arrows has been disabled in this duel!");
				return false;
			}

			if (Constants.itemRequirements) {
				if (targetSlot == 10 || targetSlot == 7 || targetSlot == 5 || targetSlot == 4 || targetSlot == 0
						|| targetSlot == 9 || targetSlot == 10) {
					if (player.defenceLevelReq > 0) {
						if (player.getPA().getLevelForXP(player.playerXP[1]) < player.defenceLevelReq) {
							player.getActionSender().sendMessage("You need a defence level of " + player.defenceLevelReq + " to wear this item.");
							canWearItem = false;
						}
					}
					if (player.rangeLevelReq > 0) {
						if (player.getPA().getLevelForXP(player.playerXP[4]) < player.rangeLevelReq) {
							player.getActionSender().sendMessage("You need a range level of " + player.rangeLevelReq + " to wear this item.");
							canWearItem = false;
						}
					}
					if (player.magicLevelReq > 0) {
						if (player.getPA().getLevelForXP(player.playerXP[6]) < player.magicLevelReq) {
							player.getActionSender().sendMessage("You need a magic level of " + player.magicLevelReq + " to wear this item.");
							canWearItem = false;
						}
					}
				}
				if (targetSlot == 3) {
					if (player.attackLevelReq > 0) {
						if (player.getPA().getLevelForXP(player.playerXP[0]) < player.attackLevelReq) {
							player.getActionSender().sendMessage("You need an attack level of " + player.attackLevelReq + " to wield this weapon.");
							canWearItem = false;
						}
					}
					if (player.rangeLevelReq > 0) {
						if (player.getPA().getLevelForXP(player.playerXP[4]) < player.rangeLevelReq) {
							player.getActionSender().sendMessage("You need a range level of " + player.rangeLevelReq + " to wield this weapon.");
							canWearItem = false;
						}
					}
					if (player.magicLevelReq > 0) {
						if (player.getPA().getLevelForXP(player.playerXP[6]) < player.magicLevelReq) {
							player.getActionSender().sendMessage("You need a magic level of " + player.magicLevelReq + " to wield this weapon.");
							canWearItem = false;
						}
					}
				}
			}

			if (!canWearItem) {
				return false;
			}

			int wearAmount = player.playerItemsN[slot];
			if (wearAmount < 1) {
				return false;
			}

			if (targetSlot == EquipmentListener.WEAPON_SLOT.getSlot()) {
				player.autocasting = false;
				player.autocastId = 0;
				player.getActionSender().sendConfig(108, 0);
			}

			if (slot >= 0 && wearID >= 0) {
				int toEquip = player.playerItems[slot];
				int toEquipN = player.playerItemsN[slot];
				int toRemove = player.playerEquipment[targetSlot];
				int toRemoveN = player.playerEquipmentN[targetSlot];
				 if (CastleWars.SARA_BANNER == toRemove || CastleWars.ZAMMY_BANNER == toRemove) {
                     CastleWars.dropFlag(player, toRemove);
                     toRemove = -1;
						toRemoveN = 0;
                 }
				if (toEquip == toRemove + 1 && Item.itemStackable[toRemove]) {
					deleteItem(toRemove, getItemSlot(toRemove), toEquipN);
					player.playerEquipmentN[targetSlot] += toEquipN;
				} else if (targetSlot != 5 && targetSlot != 3) {
					player.playerItems[slot] = toRemove + 1;
					player.playerItemsN[slot] = toRemoveN;
					player.playerEquipment[targetSlot] = toEquip - 1;
					player.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 5) {
					boolean wearing2h = is2handed(
							getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase(),
							player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]);
					@SuppressWarnings("unused")
					boolean wearingShield = player.playerEquipment[EquipmentListener.SHIELD_SLOT.getSlot()] > 0;
					if (wearing2h) {
						toRemove = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()];
						toRemoveN = player.playerEquipmentN[EquipmentListener.WEAPON_SLOT.getSlot()];
						player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] = -1;
						player.playerEquipmentN[EquipmentListener.WEAPON_SLOT.getSlot()] = 0;
						updateSlot(EquipmentListener.WEAPON_SLOT.getSlot());
					}
					player.playerItems[slot] = toRemove + 1;
					player.playerItemsN[slot] = toRemoveN;
					player.playerEquipment[targetSlot] = toEquip - 1;
					player.playerEquipmentN[targetSlot] = toEquipN;
				} else if (targetSlot == 3) {
					boolean is2h = is2handed(getItemName(wearID).toLowerCase(), wearID);
					boolean wearingShield = player.playerEquipment[EquipmentListener.SHIELD_SLOT.getSlot()] > 0;
					boolean wearingWeapon = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] > 0;
					if (is2h) {
						if (wearingShield && wearingWeapon) {
							if (freeSlots() > 0) {
								player.playerItems[slot] = toRemove + 1;
								player.playerItemsN[slot] = toRemoveN;
								player.playerEquipment[targetSlot] = toEquip - 1;
								player.playerEquipmentN[targetSlot] = toEquipN;
								removeItem(player.playerEquipment[EquipmentListener.SHIELD_SLOT.getSlot()],
										EquipmentListener.SHIELD_SLOT.getSlot());
							} else {
								player.getActionSender().sendMessage("You do not have enough inventory space to do this.");
								return false;
							}
						} else if (wearingShield && !wearingWeapon) {
							player.playerItems[slot] = player.playerEquipment[EquipmentListener.SHIELD_SLOT.getSlot()] + 1;
							player.playerItemsN[slot] = player.playerEquipmentN[EquipmentListener.SHIELD_SLOT.getSlot()];
							player.playerEquipment[targetSlot] = toEquip - 1;
							player.playerEquipmentN[targetSlot] = toEquipN;
							player.playerEquipment[EquipmentListener.SHIELD_SLOT.getSlot()] = -1;
							player.playerEquipmentN[EquipmentListener.SHIELD_SLOT.getSlot()] = 0;
							updateSlot(EquipmentListener.SHIELD_SLOT.getSlot());
						} else {
							player.playerItems[slot] = toRemove + 1;
							player.playerItemsN[slot] = toRemoveN;
							player.playerEquipment[targetSlot] = toEquip - 1;
							player.playerEquipmentN[targetSlot] = toEquipN;
						}
					} else {
						player.playerItems[slot] = toRemove + 1;
						player.playerItemsN[slot] = toRemoveN;
						player.playerEquipment[targetSlot] = toEquip - 1;
						player.playerEquipmentN[targetSlot] = toEquipN;
					}
				}
				player.updateItems = true;
			}
			if (targetSlot == 3) {
				player.usingSpecial = false;
				addSpecialBar(wearID);
			}
			if (player.getOutStream() != null && player != null) {
				player.getOutStream().createFrameVarSizeWord(34);
				player.getOutStream().writeWord(1688);
				player.getOutStream().writeByte(targetSlot);
				player.getOutStream().writeWord(wearID + 1);

				if (player.playerEquipmentN[targetSlot] > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord(player.playerEquipmentN[targetSlot]);
				} else {
					player.getOutStream().writeByte(player.playerEquipmentN[targetSlot]);
				}

				player.getOutStream().endFrameVarSizeWord();
				player.flushOutStream();
			}
			sendWeapon(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()],
					getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]));
			resetBonus();
			getBonus();
			writeBonus();
			player.getCombat().getPlayerAnimIndex(
					player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			player.getPA().requestUpdates();
			return true;
		} else {
			return false;
		}
	}// }

	/**
	 * Indicates the action to wear an item.
	 * 
	 * @param wearID
	 * @param wearAmount
	 * @param targetSlot
	 */
	public void wearItem(int wearID, int wearAmount, int targetSlot) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrameVarSizeWord(34);
			player.getOutStream().writeWord(1688);
			player.getOutStream().writeByte(targetSlot);
			player.getOutStream().writeWord(wearID + 1);

			if (wearAmount > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord(wearAmount);
			} else {
				player.getOutStream().writeByte(wearAmount);
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
			player.playerEquipment[targetSlot] = wearID;
			player.playerEquipmentN[targetSlot] = wearAmount;
			player.getItems().sendWeapon(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()],
					player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]));
			player.getItems().resetBonus();
			player.getItems().getBonus();
			player.getItems().writeBonus();
			player.getCombat().getPlayerAnimIndex(
					player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
			player.updateRequired = true;
			player.setAppearanceUpdateRequired(true);
		} // }
	}

	/**
	 * Updates the slot when wielding an item.
	 * 
	 * @param slot
	 */
	public void updateSlot(int slot) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrameVarSizeWord(34);
			player.getOutStream().writeWord(1688);
			player.getOutStream().writeByte(slot);
			player.getOutStream().writeWord(player.playerEquipment[slot] + 1);
			if (player.playerEquipmentN[slot] > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord(player.playerEquipmentN[slot]);
			} else {
				player.getOutStream().writeByte(player.playerEquipmentN[slot]);
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		} // }
	}

	/**
	 * Removes a wielded item.
	 * 
	 * @param wearID
	 * @param slot
	 */
	public void removeItem(int wearID, int slot) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			if (player.playerEquipment[slot] > -1) {
				if ((player.playerEquipment[slot] == CastleWars.SARA_CAPE || player.playerEquipment[slot] == CastleWars.ZAMMY_CAPE)
                        && (CastleWars.isInCw(player) || CastleWars.isInCwWait(player))) {
                    player.getActionSender().sendMessage("You cannot unequip your castle wars cape during a game!");
                    return;
                }
				if ((player.playerEquipment[slot] == CastleWars.SARA_HOOD || player.playerEquipment[slot] == CastleWars.ZAMMY_HOOD)
                        && (CastleWars.isInCw(player) || CastleWars.isInCwWait(player))) {
                        player.getActionSender().sendMessage("You cannot unequip your castle wars hood during a game!");
                        return;
                }					
				if (player.playerEquipment[slot] == CastleWars.SARA_BANNER || player.playerEquipment[slot] == CastleWars.ZAMMY_BANNER) {
					CastleWars.dropFlag(player, player.playerEquipment[slot]);
				}
				if (addItem(player.playerEquipment[slot], player.playerEquipmentN[slot])) {
					player.playerEquipment[slot] = -1;
					player.playerEquipmentN[slot] = 0;
					sendWeapon(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()],
							getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]));
					resetBonus();
					getBonus();
					writeBonus();
					player.getCombat().getPlayerAnimIndex(player.getItems()
							.getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
					player.getOutStream().createFrame(34);
					player.getOutStream().writeWord(6);
					player.getOutStream().writeWord(1688);
					player.getOutStream().writeByte(slot);
					player.getOutStream().writeWord(0);
					player.getOutStream().writeByte(0);
					player.flushOutStream();
					player.updateRequired = true;
					player.setAppearanceUpdateRequired(true);
				}
			}
		} // }
	}

	/**
	 * Items in your bank.
	 */
	public void rearrangeBank() {
		int totalItems = 0;
		int highestSlot = 0;
		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			if (player.bankItems[i] != 0) {
				totalItems++;
				if (highestSlot <= i) {
					highestSlot = i;
				}
			}
		}

		for (int i = 0; i <= highestSlot; i++) {
			if (player.bankItems[i] == 0) {
				boolean stop = false;

				for (int k = i; k <= highestSlot; k++) {
					if (player.bankItems[k] != 0 && !stop) {
						int spots = k - i;
						for (int j = k; j <= highestSlot; j++) {
							player.bankItems[j - spots] = player.bankItems[j];
							player.bankItemsN[j - spots] = player.bankItemsN[j];
							stop = true;
							player.bankItems[j] = 0;
							player.bankItemsN[j] = 0;
						}
					}
				}
			}
		}

		int totalItemsAfter = 0;
		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			if (player.bankItems[i] != 0) {
				totalItemsAfter++;
			}
		}

		if (totalItems != totalItemsAfter)
			player.disconnected = true;
	}

	/**
	 * Items displayed on the armor interface.
	 * 
	 * @param id
	 * @param amount
	 */
	public void itemOnInterface(int id, int amount) {
		// synchronized(c) {
		player.getOutStream().createFrameVarSizeWord(53);
		player.getOutStream().writeWord(2274);
		player.getOutStream().writeWord(1);
		if (amount > 254) {
			player.getOutStream().writeByte(255);
			player.getOutStream().writeDWord_v2(amount);
		} else {
			player.getOutStream().writeByte(amount);
		}
		player.getOutStream().writeWordBigEndianA(id);
		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();// }
	}

	/**
	 * Reseting your bank.
	 */
	public void resetBank() {
		// synchronized(c) {
		player.getOutStream().createFrameVarSizeWord(53);
		player.getOutStream().writeWord(5382); // Bank
		player.getOutStream().writeWord(Constants.BANK_SIZE);
		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			if (player.bankItemsN[i] > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord_v2(player.bankItemsN[i]);
			} else {
				player.getOutStream().writeByte(player.bankItemsN[i]);
			}
			if (player.bankItemsN[i] < 1) {
				player.bankItems[i] = 0;
			}
			if (player.bankItems[i] > Constants.ITEM_LIMIT || player.bankItems[i] < 0) {
				player.bankItems[i] = Constants.ITEM_LIMIT;
			}
			player.getOutStream().writeWordBigEndianA(player.bankItems[i]);
		}
		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();// }
	}

	/**
	 * Resets temporary worn items. Used in minigames, etc
	 */
	public void resetTempItems() {
		// synchronized(c) {
		int itemCount = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] > -1) {
				itemCount = i;
			}
		}
		player.getOutStream().createFrameVarSizeWord(53);
		player.getOutStream().writeWord(5064);
		player.getOutStream().writeWord(itemCount + 1);
		for (int i = 0; i < itemCount + 1; i++) {
			if (player.playerItemsN[i] > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord_v2(player.playerItemsN[i]);
			} else {
				player.getOutStream().writeByte(player.playerItemsN[i]);
			}
			if (player.playerItems[i] > Constants.ITEM_LIMIT || player.playerItems[i] < 0) {
				player.playerItems[i] = Constants.ITEM_LIMIT;
			}
			player.getOutStream().writeWordBigEndianA(player.playerItems[i]);
		}
		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();
	}// }

	/**
	 * Banking your item.
	 * 
	 * @param itemID
	 * @param fromSlot
	 * @param amount
	 * @return itemID
	 */
	public boolean bankItem(int itemID, int fromSlot, int amount) {
		if (player.playerItemsN[fromSlot] <= 0) {
			return false;
		}
		if (!Item.itemIsNote[player.playerItems[fromSlot] - 1]) {
			if (player.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[player.playerItems[fromSlot] - 1] || player.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Constants.BANK_SIZE; i++) {
					if (player.bankItems[i] == player.playerItems[fromSlot]) {
						if (player.playerItemsN[fromSlot] < amount)
							amount = player.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = Constants.BANK_SIZE + 1;
					}
				}

				/*
				 * Checks if you already have the same item in your bank.
				 */
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Constants.BANK_SIZE; i++) {
						if (player.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Constants.BANK_SIZE + 1;
						}
					}
					player.bankItems[toBankSlot] = player.playerItems[fromSlot];
					if (player.playerItemsN[fromSlot] < amount) {
						amount = player.playerItemsN[fromSlot];
					}
					if ((player.bankItemsN[toBankSlot] + amount) <= Constants.MAXITEM_AMOUNT
							&& (player.bankItemsN[toBankSlot] + amount) > -1) {
						player.bankItemsN[toBankSlot] += amount;
					} else {
						player.getActionSender().sendMessage("Bank full!");
						return false;
					}
					deleteItem((player.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				}

				else if (alreadyInBank) {
					if ((player.bankItemsN[toBankSlot] + amount) <= Constants.MAXITEM_AMOUNT
							&& (player.bankItemsN[toBankSlot] + amount) > -1) {
						player.bankItemsN[toBankSlot] += amount;
					} else {
						player.getActionSender().sendMessage("Bank full!");
						return false;
					}
					deleteItem((player.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					player.getActionSender().sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = player.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Constants.BANK_SIZE; i++) {
					if (player.bankItems[i] == player.playerItems[fromSlot]) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Constants.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Constants.BANK_SIZE; i++) {
						if (player.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Constants.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < player.playerItems.length; i++) {
							if ((player.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							player.bankItems[toBankSlot] = player.playerItems[firstPossibleSlot];
							player.bankItemsN[toBankSlot] += 1;
							deleteItem((player.playerItems[firstPossibleSlot] - 1), firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < player.playerItems.length; i++) {
							if ((player.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							player.bankItemsN[toBankSlot] += 1;
							deleteItem((player.playerItems[firstPossibleSlot] - 1), firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					player.getActionSender().sendMessage("Bank full!");
					return false;
				}
			}
		} else if (Item.itemIsNote[player.playerItems[fromSlot] - 1] && !Item.itemIsNote[player.playerItems[fromSlot] - 2]) {
			if (player.playerItems[fromSlot] <= 0) {
				return false;
			}
			if (Item.itemStackable[player.playerItems[fromSlot] - 1] || player.playerItemsN[fromSlot] > 1) {
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Constants.BANK_SIZE; i++) {
					if (player.bankItems[i] == (player.playerItems[fromSlot] - 1)) {
						if (player.playerItemsN[fromSlot] < amount)
							amount = player.playerItemsN[fromSlot];
						alreadyInBank = true;
						toBankSlot = i;
						i = Constants.BANK_SIZE + 1;
					}
				}

				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Constants.BANK_SIZE; i++) {
						if (player.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Constants.BANK_SIZE + 1;
						}
					}
					player.bankItems[toBankSlot] = (player.playerItems[fromSlot] - 1);
					if (player.playerItemsN[fromSlot] < amount) {
						amount = player.playerItemsN[fromSlot];
					}
					if ((player.bankItemsN[toBankSlot] + amount) <= Constants.MAXITEM_AMOUNT
							&& (player.bankItemsN[toBankSlot] + amount) > -1) {
						player.bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((player.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					if ((player.bankItemsN[toBankSlot] + amount) <= Constants.MAXITEM_AMOUNT
							&& (player.bankItemsN[toBankSlot] + amount) > -1) {
						player.bankItemsN[toBankSlot] += amount;
					} else {
						return false;
					}
					deleteItem((player.playerItems[fromSlot] - 1), fromSlot, amount);
					resetTempItems();
					resetBank();
					return true;
				} else {
					player.getActionSender().sendMessage("Bank full!");
					return false;
				}
			} else {
				itemID = player.playerItems[fromSlot];
				int toBankSlot = 0;
				boolean alreadyInBank = false;
				for (int i = 0; i < Constants.BANK_SIZE; i++) {
					if (player.bankItems[i] == (player.playerItems[fromSlot] - 1)) {
						alreadyInBank = true;
						toBankSlot = i;
						i = Constants.BANK_SIZE + 1;
					}
				}
				if (!alreadyInBank && freeBankSlots() > 0) {
					for (int i = 0; i < Constants.BANK_SIZE; i++) {
						if (player.bankItems[i] <= 0) {
							toBankSlot = i;
							i = Constants.BANK_SIZE + 1;
						}
					}
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < player.playerItems.length; i++) {
							if ((player.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							player.bankItems[toBankSlot] = (player.playerItems[firstPossibleSlot] - 1);
							player.bankItemsN[toBankSlot] += 1;
							deleteItem((player.playerItems[firstPossibleSlot] - 1), firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else if (alreadyInBank) {
					int firstPossibleSlot = 0;
					boolean itemExists = false;
					while (amount > 0) {
						itemExists = false;
						for (int i = firstPossibleSlot; i < player.playerItems.length; i++) {
							if ((player.playerItems[i]) == itemID) {
								firstPossibleSlot = i;
								itemExists = true;
								i = 30;
							}
						}
						if (itemExists) {
							player.bankItemsN[toBankSlot] += 1;
							deleteItem((player.playerItems[firstPossibleSlot] - 1), firstPossibleSlot, 1);
							amount--;
						} else {
							amount = 0;
						}
					}
					resetTempItems();
					resetBank();
					return true;
				} else {
					player.getActionSender().sendMessage("Bank full!");
					return false;
				}
			}
		} else {
			player.getActionSender().sendMessage("Item not supported " + (player.playerItems[fromSlot] - 1));
			return false;
		}
	}

	/**
	 * Checks if you have free bank slots.
	 */
	public int freeBankSlots() {
		int freeS = 0;
		for (int i = 0; i < Constants.BANK_SIZE; i++) {
			if (player.bankItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	/**
	 * Getting items from your bank.
	 * 
	 * @param itemID
	 * @param fromSlot
	 * @param amount
	 */
	public void fromBank(int itemID, int fromSlot, int amount) {
		if (amount > 0) {
			if (player.bankItems[fromSlot] > 0) {
				if (!player.takeAsNote) {
					if (Item.itemStackable[player.bankItems[fromSlot] - 1]) {
						if (player.bankItemsN[fromSlot] > amount) {
							if (addItem((player.bankItems[fromSlot] - 1), amount)) {
								player.bankItemsN[fromSlot] -= amount;
								resetBank();
								player.getItems().resetItems(5064);
							}
						} else {
							if (addItem((player.bankItems[fromSlot] - 1), player.bankItemsN[fromSlot])) {
								player.bankItems[fromSlot] = 0;
								player.bankItemsN[fromSlot] = 0;
								resetBank();
								player.getItems().resetItems(5064);
							}
						}
					} else {
						while (amount > 0) {
							if (player.bankItemsN[fromSlot] > 0) {
								if (addItem((player.bankItems[fromSlot] - 1), 1)) {
									player.bankItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else {
								amount = 0;
							}
						}
						resetBank();
						player.getItems().resetItems(5064);
					}
				} else if (player.takeAsNote && Item.itemIsNote[player.bankItems[fromSlot]]) {
					if (player.bankItemsN[fromSlot] > amount) {
						if (addItem(player.bankItems[fromSlot], amount)) {
							player.bankItemsN[fromSlot] -= amount;
							resetBank();
							player.getItems().resetItems(5064);
						}
					} else {
						if (addItem(player.bankItems[fromSlot], player.bankItemsN[fromSlot])) {
							player.bankItems[fromSlot] = 0;
							player.bankItemsN[fromSlot] = 0;
							resetBank();
							player.getItems().resetItems(5064);
						}
					}
				} else {
					player.getActionSender().sendMessage("This item can't be withdrawn as a note.");
					if (Item.itemStackable[player.bankItems[fromSlot] - 1]) {
						if (player.bankItemsN[fromSlot] > amount) {
							if (addItem((player.bankItems[fromSlot] - 1), amount)) {
								player.bankItemsN[fromSlot] -= amount;
								resetBank();
								player.getItems().resetItems(5064);
							}
						} else {
							if (addItem((player.bankItems[fromSlot] - 1), player.bankItemsN[fromSlot])) {
								player.bankItems[fromSlot] = 0;
								player.bankItemsN[fromSlot] = 0;
								resetBank();
								player.getItems().resetItems(5064);
							}
						}
					} else {
						while (amount > 0) {
							if (player.bankItemsN[fromSlot] > 0) {
								if (addItem((player.bankItems[fromSlot] - 1), 1)) {
									player.bankItemsN[fromSlot] += -1;
									amount--;
								} else {
									amount = 0;
								}
							} else {
								amount = 0;
							}
						}
						resetBank();
						player.getItems().resetItems(5064);
					}
				}
			}
		}
	}

	/**
	 * Checking item amounts.
	 * 
	 * @param itemID
	 * @return
	 */
	public int itemAmount(int itemID) {
		int tempAmount = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] == itemID) {
				tempAmount += player.playerItemsN[i];
			}
		}
		return tempAmount;
	}

	/**
	 * Checks if the item is stackable.
	 * 
	 * @param itemID
	 * @return
	 */
	public boolean isStackable(int itemID) {
		return Item.itemStackable[itemID];
	}

	/**
	 * Updates the equipment tab.
	 **/
	public void setEquipment(int wearID, int amount, int targetSlot) {
		// synchronized(c) {
		player.getOutStream().createFrameVarSizeWord(34);
		player.getOutStream().writeWord(1688);
		player.getOutStream().writeByte(targetSlot);
		player.getOutStream().writeWord(wearID + 1);
		if (amount > 254) {
			player.getOutStream().writeByte(255);
			player.getOutStream().writeDWord(amount);
		} else {
			player.getOutStream().writeByte(amount);
		}
		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();
		player.playerEquipment[targetSlot] = wearID;
		player.playerEquipmentN[targetSlot] = amount;
		player.updateRequired = true;
		player.setAppearanceUpdateRequired(true);
	}// }

	/**
	 * Moving Items in your bag.
	 **/
	public void moveItems(int from, int to, int moveWindow) {
		if (moveWindow == 3724) {
			int tempI;
			int tempN;
			tempI = player.playerItems[from];
			tempN = player.playerItemsN[from];

			player.playerItems[from] = player.playerItems[to];
			player.playerItemsN[from] = player.playerItemsN[to];
			player.playerItems[to] = tempI;
			player.playerItemsN[to] = tempN;
		}

		if (moveWindow == 34453 && from >= 0 && to >= 0 && from < Constants.BANK_SIZE && to < Constants.BANK_SIZE
				&& to < Constants.BANK_SIZE) {
			int tempI;
			int tempN;
			tempI = player.bankItems[from];
			tempN = player.bankItemsN[from];

			player.bankItems[from] = player.bankItems[to];
			player.bankItemsN[from] = player.bankItemsN[to];
			player.bankItems[to] = tempI;
			player.bankItemsN[to] = tempN;
		}

		if (moveWindow == 34453) {
			resetBank();
		}
		if (moveWindow == 18579) {
			int tempI;
			int tempN;
			tempI = player.playerItems[from];
			tempN = player.playerItemsN[from];

			player.playerItems[from] = player.playerItems[to];
			player.playerItemsN[from] = player.playerItemsN[to];
			player.playerItems[to] = tempI;
			player.playerItemsN[to] = tempN;
			resetItems(3214);
		}
		resetTempItems();
		if (moveWindow == 3724) {
			resetItems(3214);
		}

	}

	/**
	 * Delete item equipment.
	 **/
	public void deleteEquipment(int i, int j) {
		// synchronized(c) {
		if (PlayerHandler.players[player.playerId] == null) {
			return;
		}
		if (i < 0) {
			return;
		}

		player.playerEquipment[j] = -1;
		player.playerEquipmentN[j] = player.playerEquipmentN[j] - 1;
		player.getOutStream().createFrame(34);
		player.getOutStream().writeWord(6);
		player.getOutStream().writeWord(1688);
		player.getOutStream().writeByte(j);
		player.getOutStream().writeWord(0);
		player.getOutStream().writeByte(0);
		getBonus();
		if (j == EquipmentListener.WEAPON_SLOT.getSlot()) {
			sendWeapon(-1, "Unarmed");
		}
		resetBonus();
		getBonus();
		writeBonus();
		player.updateRequired = true;
		player.setAppearanceUpdateRequired(true);
	} // }

	/**
	 * Delete items.
	 * 
	 * @param id
	 * @param amount
	 */
	public void deleteItem(int id, int amount) {
		if (id <= 0)
			return;
		for (int j = 0; j < player.playerItems.length; j++) {
			if (amount <= 0)
				break;
			if (player.playerItems[j] == id + 1) {
				player.playerItems[j] = 0;
				player.playerItemsN[j] = 0;
				amount--;
			}
		}
		resetItems(3214);
	}

	public void deleteItem(int id, int slot, int amount) {
		if (id <= 0 || slot < 0) {
			return;
		}
		if (player.playerItems[slot] == (id + 1)) {
			if (player.playerItemsN[slot] > amount) {
				player.playerItemsN[slot] -= amount;
			} else {
				player.playerItemsN[slot] = 0;
				player.playerItems[slot] = 0;
			}
			resetItems(3214);
		}
	}

	public void deleteItem2(int id, int amount) {
		int am = amount;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (am == 0) {
				break;
			}
			if (player.playerItems[i] == (id + 1)) {
				if (player.playerItemsN[i] > amount) {
					player.playerItemsN[i] -= amount;
					break;
				} else {
					player.playerItems[i] = 0;
					player.playerItemsN[i] = 0;
					am--;
				}
			}
		}
		resetItems(3214);
	}

	/**
	 * Delete arrows.
	 **/
	public void deleteArrow() {
		// synchronized(c) {
		if (player.playerEquipment[EquipmentListener.CAPE_SLOT.getSlot()] == 10499 && Misc.random(5) != 1
				&& player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] != 4740)
			return;
		if (player.playerEquipmentN[EquipmentListener.ARROWS_SLOT.getSlot()] == 1) {
			player.getItems().deleteEquipment(player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()],
					EquipmentListener.ARROWS_SLOT.getSlot());
		}
		if (player.playerEquipmentN[EquipmentListener.ARROWS_SLOT.getSlot()] != 0) {
			player.getOutStream().createFrameVarSizeWord(34);
			player.getOutStream().writeWord(1688);
			player.getOutStream().writeByte(EquipmentListener.ARROWS_SLOT.getSlot());
			player.getOutStream().writeWord(player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()] + 1);
			if (player.playerEquipmentN[EquipmentListener.ARROWS_SLOT.getSlot()] - 1 > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord(player.playerEquipmentN[EquipmentListener.ARROWS_SLOT.getSlot()] - 1);
			} else {
				player.getOutStream().writeByte(player.playerEquipmentN[EquipmentListener.ARROWS_SLOT.getSlot()] - 1);
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
			player.playerEquipmentN[EquipmentListener.ARROWS_SLOT.getSlot()] -= 1;
		}
		player.updateRequired = true;
		player.setAppearanceUpdateRequired(true);
	}// }

	public void deleteEquipment() {
		// synchronized(c) {
		if (player.playerEquipmentN[EquipmentListener.WEAPON_SLOT.getSlot()] == 1) {
			player.getItems().deleteEquipment(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()],
					EquipmentListener.WEAPON_SLOT.getSlot());
		}
		if (player.playerEquipmentN[EquipmentListener.WEAPON_SLOT.getSlot()] != 0) {
			player.getOutStream().createFrameVarSizeWord(34);
			player.getOutStream().writeWord(1688);
			player.getOutStream().writeByte(EquipmentListener.WEAPON_SLOT.getSlot());
			player.getOutStream().writeWord(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] + 1);
			if (player.playerEquipmentN[EquipmentListener.WEAPON_SLOT.getSlot()] - 1 > 254) {
				player.getOutStream().writeByte(255);
				player.getOutStream().writeDWord(player.playerEquipmentN[EquipmentListener.WEAPON_SLOT.getSlot()] - 1);
			} else {
				player.getOutStream().writeByte(player.playerEquipmentN[EquipmentListener.WEAPON_SLOT.getSlot()] - 1);
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
			player.playerEquipmentN[EquipmentListener.WEAPON_SLOT.getSlot()] -= 1;
		}
		player.updateRequired = true;
		player.setAppearanceUpdateRequired(true);
	}// }

	/**
	 * Dropping arrows
	 **/
	public void dropArrowNpc() {
		if (player.playerEquipment[EquipmentListener.CAPE_SLOT.getSlot()] == 10499)
			return;
		int enemyX = NPCHandler.npcs[player.oldNpcIndex].getX();
		int enemyY = NPCHandler.npcs[player.oldNpcIndex].getY();
		if (Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(player.rangeItemUsed, enemyX, enemyY) == 0) {
				Server.itemHandler.createGroundItem(player, player.rangeItemUsed, enemyX, enemyY, 1, player.getId());
			} else if (Server.itemHandler.itemAmount(player.rangeItemUsed, enemyX, enemyY) != 0) {
				int amount = Server.itemHandler.itemAmount(player.rangeItemUsed, enemyX, enemyY);
				Server.itemHandler.removeGroundItem(player, player.rangeItemUsed, enemyX, enemyY, false);
				Server.itemHandler.createGroundItem(player, player.rangeItemUsed, enemyX, enemyY, amount + 1, player.getId());
			}
		}
	}

	/**
	 * Ranging arrows.
	 */
	public void dropArrowPlayer() {
		int enemyX = PlayerHandler.players[player.oldPlayerIndex].getX();
		int enemyY = PlayerHandler.players[player.oldPlayerIndex].getY();
		if (player.playerEquipment[EquipmentListener.CAPE_SLOT.getSlot()] == 10499)
			return;
		if (Misc.random(10) >= 4) {
			if (Server.itemHandler.itemAmount(player.rangeItemUsed, enemyX, enemyY) == 0) {
				Server.itemHandler.createGroundItem(player, player.rangeItemUsed, enemyX, enemyY, 1, player.getId());
			} else if (Server.itemHandler.itemAmount(player.rangeItemUsed, enemyX, enemyY) != 0) {
				int amount = Server.itemHandler.itemAmount(player.rangeItemUsed, enemyX, enemyY);
				Server.itemHandler.removeGroundItem(player, player.rangeItemUsed, enemyX, enemyY, false);
				Server.itemHandler.createGroundItem(player, player.rangeItemUsed, enemyX, enemyY, amount + 1, player.getId());
			}
		}
	}

	/**
	 * Removes all items from player's equipment.
	 */
	public void removeAllItems() {
		for (int i = 0; i < player.playerItems.length; i++) {
			player.playerItems[i] = 0;
		}
		for (int i = 0; i < player.playerItemsN.length; i++) {
			player.playerItemsN[i] = 0;
		}
		resetItems(3214);
	}

	public void removeAllBankItems() {
		for (int i = 0; i < player.bankItems.length; i++) {
			player.bankItems[i] = 0;
		}
		for (int i = 0; i < player.bankItemsN.length; i++) {
			player.bankItemsN[i] = 0;
		}
		resetItems(3214);
	}

	/**
	 * Checks if you have a free slot.
	 * 
	 * @return
	 */
	public int freeSlots() {
		int freeS = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	/**
	 * Finds the item.
	 * 
	 * @param id
	 * @param items
	 * @param amounts
	 * @return
	 */
	public int findItem(int id, int[] items, int[] amounts) {
		for (int i = 0; i < player.playerItems.length; i++) {
			if (((items[i] - 1) == id) && (amounts[i] > 0)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the item name from the item.cfg
	 * 
	 * @param ItemID
	 * @return
	 */
	public String getItemName(int ItemID) {
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == ItemID) {
					return Server.itemHandler.ItemList[i].itemName;
				}
			}
		}
		return "Unarmed";
	}

	/**
	 * Gets the item ID from the item.cfg
	 * 
	 * @param itemName
	 * @return
	 */
	public int getItemId(String itemName) {
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemName.equalsIgnoreCase(itemName)) {
					return Server.itemHandler.ItemList[i].itemId;
				}
			}
		}
		return -1;
	}

	/**
	 * Gets the item slot.
	 * 
	 * @param ItemID
	 * @return
	 */
	public int getItemSlot(int ItemID) {
		for (int i = 0; i < player.playerItems.length; i++) {
			if ((player.playerItems[i] - 1) == ItemID) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * Gets the item amount.
	 * 
	 * @param ItemID
	 * @return
	 */
	public int getItemAmount(int ItemID) {
		int itemCount = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if ((player.playerItems[i] - 1) == ItemID) {
				itemCount += player.playerItemsN[i];
			}
		}
		return itemCount;
	}

	/**
	 * Checks if the player has the item.
	 * 
	 * @param itemID
	 * @param amt
	 * @param slot
	 * @return
	 */
	public boolean playerHasItem(int itemID, int amt, int slot) {
		itemID++;
		int found = 0;
		if (player.playerItems[slot] == (itemID)) {
			for (int i = 0; i < player.playerItems.length; i++) {
				if (player.playerItems[i] == itemID) {
					if (player.playerItemsN[i] >= amt) {
						return true;
					} else {
						found++;
					}
				}
			}
			if (found >= amt) {
				return true;
			}
			return false;
		}
		return false;
	}

	public boolean playerHasItem(int itemID) {
		itemID++;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] == itemID)
				return true;
		}
		return false;
	}

	public boolean playerHasItem(int itemID, int amt) {
		itemID++;
		int found = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] == itemID) {
				if (player.playerItemsN[i] >= amt) {
					return true;
				} else {
					found++;
				}
			}
		}
		if (found >= amt) {
			return true;
		}
		return false;
	}

	/**
	 * Getting un-noted items.
	 * 
	 * @param ItemID
	 * @return
	 */
	public int getUnnotedItem(int ItemID) {
		int NewID = ItemID - 1;
		String NotedName = "";
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == ItemID) {
					NotedName = Server.itemHandler.ItemList[i].itemName;
				}
			}
		}
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemName == NotedName) {
					if (Server.itemHandler.ItemList[i].itemDescription
							.startsWith("Swap this note at any bank for a") == false) {
						NewID = Server.itemHandler.ItemList[i].itemId;
						break;
					}
				}
			}
		}
		return NewID;
	}

	/**
	 * Dropping items
	 **/
	public void createGroundItem(int itemID, int itemX, int itemY, int itemAmount) {
		// synchronized(c) {
		player.getOutStream().createFrame(85);
		player.getOutStream().writeByteC((itemY - 8 * player.mapRegionY));
		player.getOutStream().writeByteC((itemX - 8 * player.mapRegionX));
		player.getOutStream().createFrame(44);
		player.getOutStream().writeWordBigEndianA(itemID);
		player.getOutStream().writeWord(itemAmount);
		player.getOutStream().writeByte(0);
		player.flushOutStream();
		// }
	}

	/**
	 * Pickup items from the ground.
	 **/
	public void removeGroundItem(int itemID, int itemX, int itemY, int Amount) {
		// synchronized(c) {
		player.getOutStream().createFrame(85);
		player.getOutStream().writeByteC((itemY - 8 * player.mapRegionY));
		player.getOutStream().writeByteC((itemX - 8 * player.mapRegionX));
		player.getOutStream().createFrame(156);
		player.getOutStream().writeByteS(0);
		player.getOutStream().writeWord(itemID);
		player.flushOutStream();
		// }
	}

	/**
	 * Checks if a player owns a cape.
	 * 
	 * @return
	 */
	public boolean ownsCape() {
		if (player.getItems().playerHasItem(2412, 1) || player.getItems().playerHasItem(2413, 1)
				|| player.getItems().playerHasItem(2414, 1))
			return true;
		for (int j = 0; j < Constants.BANK_SIZE; j++) {
			if (player.bankItems[j] == 2412 || player.bankItems[j] == 2413 || player.bankItems[j] == 2414)
				return true;
		}
		if (player.playerEquipment[EquipmentListener.CAPE_SLOT.getSlot()] == 2413
				|| player.playerEquipment[EquipmentListener.CAPE_SLOT.getSlot()] == 2414
				|| player.playerEquipment[EquipmentListener.CAPE_SLOT.getSlot()] == 2415)
			return true;
		return false;
	}
}