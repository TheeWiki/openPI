package server.model.players;

import java.util.concurrent.CopyOnWriteArrayList;

import server.Constants;
import server.Server;
import server.model.items.GameItem;
import server.model.items.Item;
import server.model.minigames.duel_arena.Rules;
import server.util.Misc;

public class TradeAndDuel {

	private Player player;

	public TradeAndDuel(Player Player) {
		this.player = Player;
	}

	/**
	 * Trading
	 **/

	public CopyOnWriteArrayList<GameItem> offeredItems = new CopyOnWriteArrayList<GameItem>();

	/**
	 * TODO: Fix bug where if you click accept trade while in a trade your
	 * interface will close for requested player
	 * 
	 * @param id
	 */
	public void requestTrade(int id) {
		try {
			@SuppressWarnings("static-access")
			Player o = (Player) Server.playerHandler.players[id];
			if (id == player.playerId)
				return;
			player.tradeWith = id;
			if (!player.inTrade && o.tradeRequested && o.tradeWith == player.playerId) {
				player.getTradeAndDuel().openTrade();
				o.getTradeAndDuel().openTrade();
			} else if (!player.inTrade) {

				player.tradeRequested = true;
				player.getActionSender().sendMessage("Sending trade request...");
				o.getActionSender().sendMessage(player.playerName + ":tradereq:");
			}
		} catch (Exception e) {
			Misc.println("Error requesting trade.");
		}
	}

	public void openTrade() {
		@SuppressWarnings("static-access")
		Player o = (Player) Server.playerHandler.players[player.tradeWith];

		if (o == null) {
			return;
		}
		player.inTrade = true;
		player.canOffer = true;
		player.tradeStatus = 1;
		player.tradeRequested = false;
		player.getItems().resetItems(3322);
		resetTItems(3415);
		resetOTItems(3416);
		String out = o.playerName;

		if (o.playerRights == 1) {
			out = "@cr1@" + out;
		} else if (o.playerRights == 2) {
			out = "@cr2@" + out;
		}
		player.getPA().sendFrame126(
				"Trading with: " + o.playerName + " who has @gre@" + o.getItems().freeSlots() + " free slots", 3417);
		player.getPA().sendFrame126("", 3431);
		player.getPA().sendFrame126("Are you sure you want to make this trade?", 3535);
		player.getPA().sendFrame248(3323, 3321);
	}

	public void resetTItems(int WriteFrame) {
		synchronized (player) {
			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(WriteFrame);
			int len = offeredItems.toArray().length;
			int current = 0;
			player.getOutStream().writeWord(len);
			for (GameItem item : offeredItems) {
				if (item.amount > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord_v2(item.amount);
				} else {
					player.getOutStream().writeByte(item.amount);
				}
				player.getOutStream().writeWordBigEndianA(item.id + 1);
				current++;
			}
			if (current < 27) {
				for (int i = current; i < 28; i++) {
					player.getOutStream().writeByte(1);
					player.getOutStream().writeWordBigEndianA(-1);
				}
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}
	}

	public boolean fromTrade(int itemID, int fromSlot, int amount) {
		@SuppressWarnings("static-access")
		Player o = (Player) Server.playerHandler.players[player.tradeWith];
		if (o == null) {
			return false;
		}
		try {
			if (!player.inTrade || !player.canOffer) {
				declineTrade();
				return false;
			}
			player.tradeConfirmed = false;
			o.tradeConfirmed = false;
			if (!Item.itemStackable[itemID]) {
				for (int a = 0; a < amount; a++) {
					for (GameItem item : offeredItems) {
						if (item.id == itemID) {
							if (!item.stackable) {
								offeredItems.remove(item);
								player.getItems().addItem(itemID, 1);
								o.getPA().sendFrame126("Trading with: " + player.playerName + " who has @gre@"
										+ player.getItems().freeSlots() + " free slots", 3417);
							} else {
								if (item.amount > amount) {
									item.amount -= amount;
									player.getItems().addItem(itemID, amount);
									o.getPA().sendFrame126("Trading with: " + player.playerName + " who has @gre@"
											+ player.getItems().freeSlots() + " free slots", 3417);
								} else {
									amount = item.amount;
									offeredItems.remove(item);
									player.getItems().addItem(itemID, amount);
									o.getPA().sendFrame126("Trading with: " + player.playerName + " who has @gre@"
											+ player.getItems().freeSlots() + " free slots", 3417);
								}
							}
							break;
						}
						o.getPA().sendFrame126("Trading with: " + player.playerName + " who has @gre@"
								+ player.getItems().freeSlots() + " free slots", 3417);
						player.tradeConfirmed = false;
						o.tradeConfirmed = false;
						player.getItems().resetItems(3322);
						resetTItems(3415);
						o.getTradeAndDuel().resetOTItems(3416);
						player.getPA().sendFrame126("", 3431);
						o.getPA().sendFrame126("", 3431);
					}
				}
			}
			for (GameItem item : offeredItems) {
				if (item.id == itemID) {
					if (!item.stackable) {
					} else {
						if (item.amount > amount) {
							item.amount -= amount;
							player.getItems().addItem(itemID, amount);
							o.getPA().sendFrame126("Trading with: " + player.playerName + " who has @gre@"
									+ player.getItems().freeSlots() + " free slots", 3417);
						} else {
							amount = item.amount;
							offeredItems.remove(item);
							player.getItems().addItem(itemID, amount);
							o.getPA().sendFrame126("Trading with: " + player.playerName + " who has @gre@"
									+ player.getItems().freeSlots() + " free slots", 3417);
						}
					}
					break;
				}
			}

			o.getPA().sendFrame126(
					"Trading with: " + player.playerName + " who has @gre@" + player.getItems().freeSlots() + " free slots",
					3417);
			player.tradeConfirmed = false;
			o.tradeConfirmed = false;
			player.getItems().resetItems(3322);
			resetTItems(3415);
			o.getTradeAndDuel().resetOTItems(3416);
			player.getPA().sendFrame126("", 3431);
			o.getPA().sendFrame126("", 3431);
		} catch (Exception e) {
		}
		return true;
	}

	public boolean tradeItem(int itemID, int fromSlot, int amount) {
		@SuppressWarnings("static-access")
		Player o = (Player) Server.playerHandler.players[player.tradeWith];
		if (o == null) {
			return false;
		}

		for (int i : Constants.ITEM_TRADEABLE) {
			if (i == itemID) {
				player.getActionSender().sendMessage("You can't trade this item.");
				return false;
			}
		}
		player.tradeConfirmed = false;
		o.tradeConfirmed = false;
		if (!Item.itemStackable[itemID] && !Item.itemIsNote[itemID]) {
			for (int a = 0; a < amount; a++) {
				if (player.getItems().playerHasItem(itemID, 1)) {
					offeredItems.add(new GameItem(itemID, 1));
					player.getItems().deleteItem(itemID, player.getItems().getItemSlot(itemID), 1);
					o.getPA().sendFrame126("Trading with: " + player.playerName + " who has @gre@" + player.getItems().freeSlots()
							+ " free slots", 3417);
				}
			}
			o.getPA().sendFrame126(
					"Trading with: " + player.playerName + " who has @gre@" + player.getItems().freeSlots() + " free slots",
					3417);
			player.getItems().resetItems(3322);
			resetTItems(3415);
			o.getTradeAndDuel().resetOTItems(3416);
			player.getPA().sendFrame126("", 3431);
			o.getPA().sendFrame126("", 3431);
		}

		if (!player.inTrade || !player.canOffer) {
			declineTrade();
			return false;
		}

		if (Item.itemStackable[itemID] || Item.itemIsNote[itemID]) {
			boolean inTrade = false;
			for (GameItem item : offeredItems) {
				if (item.id == itemID) {
					inTrade = true;
					item.amount += amount;
					player.getItems().deleteItem(itemID, player.getItems().getItemSlot(itemID), amount);
					o.getPA().sendFrame126("Trading with: " + player.playerName + " who has @gre@" + player.getItems().freeSlots()
							+ " free slots", 3417);
					break;
				}
			}

			if (!inTrade) {
				offeredItems.add(new GameItem(itemID, amount));
				player.getItems().deleteItem(itemID, fromSlot, amount);
				o.getPA().sendFrame126(
						"Trading with: " + player.playerName + " who has @gre@" + player.getItems().freeSlots() + " free slots",
						3417);
			}
		}
		o.getPA().sendFrame126(
				"Trading with: " + player.playerName + " who has @gre@" + player.getItems().freeSlots() + " free slots", 3417);
		player.getItems().resetItems(3322);
		resetTItems(3415);
		o.getTradeAndDuel().resetOTItems(3416);
		player.getPA().sendFrame126("", 3431);
		o.getPA().sendFrame126("", 3431);
		return true;
	}

	public void resetTrade() {
		offeredItems.clear();
		player.inTrade = false;
		player.tradeWith = 0;
		player.canOffer = true;
		player.tradeConfirmed = false;
		player.tradeConfirmed2 = false;
		player.acceptedTrade = false;
		player.getPA().removeAllWindows();
		player.tradeResetNeeded = false;
		player.getPA().sendFrame126("Are you sure you want to make this trade?", 3535);
	}

	public void declineTrade() {
		player.tradeStatus = 0;
		declineTrade(true);
	}

	public void declineTrade(boolean tellOther) {
		player.getPA().removeAllWindows();
		@SuppressWarnings("static-access")
		Player o = (Player) Server.playerHandler.players[player.tradeWith];
		if (o == null) {
			return;
		}

		if (tellOther) {
			o.getTradeAndDuel().declineTrade(false);
			o.getTradeAndDuel().player.getPA().removeAllWindows();
		}

		for (GameItem item : offeredItems) {
			if (item.amount < 1) {
				continue;
			}
			if (item.stackable) {
				player.getItems().addItem(item.id, item.amount);
			} else {
				for (int i = 0; i < item.amount; i++) {
					player.getItems().addItem(item.id, 1);
				}
			}
		}
		player.canOffer = true;
		player.tradeConfirmed = false;
		player.tradeConfirmed2 = false;
		offeredItems.clear();
		player.inTrade = false;
		player.tradeWith = 0;
	}

	public void resetOTItems(int WriteFrame) {
		synchronized (player) {
			@SuppressWarnings("static-access")
			Player o = (Player) Server.playerHandler.players[player.tradeWith];
			if (o == null) {
				return;
			}
			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(WriteFrame);
			int len = o.getTradeAndDuel().offeredItems.toArray().length;
			int current = 0;
			player.getOutStream().writeWord(len);
			for (GameItem item : o.getTradeAndDuel().offeredItems) {
				if (item.amount > 254) {
					player.getOutStream().writeByte(255); // item's stack count. if
														// over 254, write byte
														// 255
					player.getOutStream().writeDWord_v2(item.amount);
				} else {
					player.getOutStream().writeByte(item.amount);
				}
				player.getOutStream().writeWordBigEndianA(item.id + 1); // item id
				current++;
			}
			if (current < 27) {
				for (int i = current; i < 28; i++) {
					player.getOutStream().writeByte(1);
					player.getOutStream().writeWordBigEndianA(-1);
				}
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}
	}

	public void confirmScreen() {
		@SuppressWarnings("static-access")
		Player o = (Player) Server.playerHandler.players[player.tradeWith];
		if (o == null) {
			return;
		}
		player.canOffer = false;
		player.getItems().resetItems(3214);
		String SendTrade = "Absolutely nothing!";
		String SendAmount = "";
		int Count = 0;
		for (GameItem item : offeredItems) {
			if (item.id > 0) {
				if (item.amount >= 1000 && item.amount < 1000000) {
					SendAmount = "@cya@" + (item.amount / 1000) + "K @whi@(" + Misc.format(item.amount) + ")";
				} else if (item.amount >= 1000000) {
					SendAmount = "@gre@" + (item.amount / 1000000) + " million @whi@(" + Misc.format(item.amount) + ")";
				} else {
					SendAmount = "" + Misc.format(item.amount);
				}

				if (Count == 0) {
					SendTrade = player.getItems().getItemName(item.id);
				} else {
					SendTrade = SendTrade + "\\n" + player.getItems().getItemName(item.id);
				}

				if (item.stackable) {
					SendTrade = SendTrade + " x " + SendAmount;
				}
				Count++;
			}
		}

		player.getPA().sendFrame126(SendTrade, 3557);
		SendTrade = "Absolutely nothing!";
		SendAmount = "";
		Count = 0;

		for (GameItem item : o.getTradeAndDuel().offeredItems) {
			if (item.id > 0) {
				if (item.amount >= 1000 && item.amount < 1000000) {
					SendAmount = "@cya@" + (item.amount / 1000) + "K @whi@(" + Misc.format(item.amount) + ")";
				} else if (item.amount >= 1000000) {
					SendAmount = "@gre@" + (item.amount / 1000000) + " million @whi@(" + Misc.format(item.amount) + ")";
				} else {
					SendAmount = "" + Misc.format(item.amount);
				}
				// SendAmount = SendAmount;

				if (Count == 0) {
					SendTrade = player.getItems().getItemName(item.id);
				} else {
					SendTrade = SendTrade + "\\n" + player.getItems().getItemName(item.id);
				}
				if (item.stackable) {
					SendTrade = SendTrade + " x " + SendAmount;
				}
				Count++;
			}
		}
		player.getPA().sendFrame126(SendTrade, 3558);
		// TODO: find out what 197 does eee 3213
		player.getPA().sendFrame248(3443, 197);
	}

	public void giveItems() {
		@SuppressWarnings("static-access")
		Player o = (Player) Server.playerHandler.players[player.tradeWith];
		if (o == null) {
			return;
		}
		try {
			for (GameItem item : o.getTradeAndDuel().offeredItems) {
				if (item.id > 0) {
					player.getItems().addItem(item.id, item.amount);
				}
			}

			player.getPA().removeAllWindows();
			player.tradeResetNeeded = true;
		} catch (Exception e) {
		}
	}

	/**
	 * Dueling
	 **/

	public CopyOnWriteArrayList<GameItem> otherStakedItems = new CopyOnWriteArrayList<GameItem>();
	public CopyOnWriteArrayList<GameItem> stakedItems = new CopyOnWriteArrayList<GameItem>();

	public void requestDuel(int id) {
		try {
			if (id == player.playerId)
				return;
			resetDuel();
			resetDuelItems();
			player.duelingWith = id;
			@SuppressWarnings("static-access")
			Player o = (Player) Server.playerHandler.players[id];
			if (o == null) {
				return;
			}
			player.duelRequested = true;
			if (player.duelStatus == 0 && o.duelStatus == 0 && player.duelRequested && o.duelRequested
					&& player.duelingWith == o.getId() && o.duelingWith == player.getId()) {
				if (player.goodDistance(player.getX(), player.getY(), o.getX(), o.getY(), 1)) {
					player.getTradeAndDuel().openDuel();
					o.getTradeAndDuel().openDuel();
				} else {
					player.getActionSender().sendMessage("You need to get closer to your opponent to start the duel.");
				}

			} else {
				player.getActionSender().sendMessage("Sending duel request...");
				o.getActionSender().sendMessage(player.playerName + ":duelreq:");
			}
		} catch (Exception e) {
			Misc.println("Error requesting duel.");
		}
	}

	public void openDuel() {
		@SuppressWarnings("static-access")
		Player o = (Player) Server.playerHandler.players[player.duelingWith];
		if (o == null) {
			return;
		}
		player.duelStatus = 1;
		refreshduelRules();
		refreshDuelScreen();
		player.canOffer = true;
		for (int i = 0; i < player.playerEquipment.length; i++) {
			sendDuelEquipment(player.playerEquipment[i], player.playerEquipmentN[i], i);
		}
		player.getPA().sendFrame126("Dueling with: " + o.playerName + " (level-" + o.combatLevel + ")", 6671);
		player.getPA().sendFrame126("", 6684);
		player.getPA().sendFrame248(6575, 3321);
		player.getItems().resetItems(3322);
	}

	public void sendDuelEquipment(int itemId, int amount, int slot) {
		synchronized (player) {
			if (itemId != 0) {
				player.getOutStream().createFrameVarSizeWord(34);
				player.getOutStream().writeWord(13824);
				player.getOutStream().writeByte(slot);
				player.getOutStream().writeWord(itemId + 1);

				if (amount > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord(amount);
				} else {
					player.getOutStream().writeByte(amount);
				}
				player.getOutStream().endFrameVarSizeWord();
				player.flushOutStream();
			}
		}
	}

	public void refreshduelRules() {
		for (int i = 0; i < player.duelRule.length; i++) {
			player.duelRule[i] = false;
		}
		player.getPA().sendFrame87(286, 0);
		player.duelOption = 0;
	}

	public void refreshDuelScreen() {
		synchronized (player) {
			@SuppressWarnings("static-access")
			Player o = (Player) Server.playerHandler.players[player.duelingWith];
			if (o == null) {
				return;
			}
			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(6669);
			player.getOutStream().writeWord(stakedItems.toArray().length);
			int current = 0;
			for (GameItem item : stakedItems) {
				if (item.amount > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord_v2(item.amount);
				} else {
					player.getOutStream().writeByte(item.amount);
				}
				if (item.id > Constants.ITEM_LIMIT || item.id < 0) {
					item.id = Constants.ITEM_LIMIT;
				}
				player.getOutStream().writeWordBigEndianA(item.id + 1);

				current++;
			}

			if (current < 27) {
				for (int i = current; i < 28; i++) {
					player.getOutStream().writeByte(1);
					player.getOutStream().writeWordBigEndianA(-1);
				}
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();

			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(6670);
			player.getOutStream().writeWord(o.getTradeAndDuel().stakedItems.toArray().length);
			current = 0;
			for (GameItem item : o.getTradeAndDuel().stakedItems) {
				if (item.amount > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord_v2(item.amount);
				} else {
					player.getOutStream().writeByte(item.amount);
				}
				if (item.id > Constants.ITEM_LIMIT || item.id < 0) {
					item.id = Constants.ITEM_LIMIT;
				}
				player.getOutStream().writeWordBigEndianA(item.id + 1);
				current++;
			}

			if (current < 27) {
				for (int i = current; i < 28; i++) {
					player.getOutStream().writeByte(1);
					player.getOutStream().writeWordBigEndianA(-1);
				}
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}
	}

	public boolean stakeItem(int itemID, int fromSlot, int amount) {

		for (int i : Constants.ITEM_TRADEABLE) {
			if (i == itemID) {
				player.getActionSender().sendMessage("You can't stake this item.");
				return false;
			}
		}
		if (amount <= 0)
			return false;
		@SuppressWarnings("static-access")
		Player o = (Player) Server.playerHandler.players[player.duelingWith];
		if (o == null) {
			declineDuel();
			return false;
		}
		if (o.duelStatus <= 0 || player.duelStatus <= 0) {
			declineDuel();
			o.getTradeAndDuel().declineDuel();
			return false;
		}
		if (!player.canOffer) {
			return false;
		}
		changeDuelStuff();
		if (!Item.itemStackable[itemID]) {
			for (int a = 0; a < amount; a++) {
				if (player.getItems().playerHasItem(itemID, 1)) {
					stakedItems.add(new GameItem(itemID, 1));
					player.getItems().deleteItem(itemID, player.getItems().getItemSlot(itemID), 1);
				}
			}
			player.getItems().resetItems(3214);
			player.getItems().resetItems(3322);
			o.getItems().resetItems(3214);
			o.getItems().resetItems(3322);
			refreshDuelScreen();
			o.getTradeAndDuel().refreshDuelScreen();
			player.getPA().sendFrame126("", 6684);
			o.getPA().sendFrame126("", 6684);
		}

		if (!player.getItems().playerHasItem(itemID, amount)) {
			return false;
		}
		if (Item.itemStackable[itemID] || Item.itemIsNote[itemID]) {
			boolean found = false;
			for (GameItem item : stakedItems) {
				if (item.id == itemID) {
					found = true;
					item.amount += amount;
					player.getItems().deleteItem(itemID, fromSlot, amount);
					break;
				}
			}
			if (!found) {
				player.getItems().deleteItem(itemID, fromSlot, amount);
				stakedItems.add(new GameItem(itemID, amount));
			}
		}

		player.getItems().resetItems(3214);
		player.getItems().resetItems(3322);
		o.getItems().resetItems(3214);
		o.getItems().resetItems(3322);
		refreshDuelScreen();
		o.getTradeAndDuel().refreshDuelScreen();
		player.getPA().sendFrame126("", 6684);
		o.getPA().sendFrame126("", 6684);
		return true;
	}

	public boolean fromDuel(int itemID, int fromSlot, int amount) {
		@SuppressWarnings("static-access")
		Player o = (Player) Server.playerHandler.players[player.duelingWith];
		if (o == null) {
			declineDuel();
			return false;
		}
		if (o.duelStatus <= 0 || player.duelStatus <= 0) {
			declineDuel();
			o.getTradeAndDuel().declineDuel();
			return false;
		}
		if (Item.itemStackable[itemID]) {
			if (player.getItems().freeSlots() - 1 < (player.duelSpaceReq)) {
				player.getActionSender().sendMessage("You have too many rules set to remove that item.");
				return false;
			}
		}

		if (!player.canOffer) {
			return false;
		}

		changeDuelStuff();
		boolean goodSpace = true;
		if (!Item.itemStackable[itemID]) {
			for (int a = 0; a < amount; a++) {
				for (GameItem item : stakedItems) {
					if (item.id == itemID) {
						if (!item.stackable) {
							if (player.getItems().freeSlots() - 1 < (player.duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							stakedItems.remove(item);
							player.getItems().addItem(itemID, 1);
						} else {
							if (player.getItems().freeSlots() - 1 < (player.duelSpaceReq)) {
								goodSpace = false;
								break;
							}
							if (item.amount > amount) {
								item.amount -= amount;
								player.getItems().addItem(itemID, amount);
							} else {
								if (player.getItems().freeSlots() - 1 < (player.duelSpaceReq)) {
									goodSpace = false;
									break;
								}
								amount = item.amount;
								stakedItems.remove(item);
								player.getItems().addItem(itemID, amount);
							}
						}
						break;
					}
					o.duelStatus = 1;
					player.duelStatus = 1;
					player.getItems().resetItems(3214);
					player.getItems().resetItems(3322);
					o.getItems().resetItems(3214);
					o.getItems().resetItems(3322);
					player.getTradeAndDuel().refreshDuelScreen();
					o.getTradeAndDuel().refreshDuelScreen();
					o.getPA().sendFrame126("", 6684);
				}
			}
		}

		for (GameItem item : stakedItems) {
			if (item.id == itemID) {
				if (!item.stackable) {
				} else {
					if (item.amount > amount) {
						item.amount -= amount;
						player.getItems().addItem(itemID, amount);
					} else {
						amount = item.amount;
						stakedItems.remove(item);
						player.getItems().addItem(itemID, amount);
					}
				}
				break;
			}
		}
		o.duelStatus = 1;
		player.duelStatus = 1;
		player.getItems().resetItems(3214);
		player.getItems().resetItems(3322);
		o.getItems().resetItems(3214);
		o.getItems().resetItems(3322);
		player.getTradeAndDuel().refreshDuelScreen();
		o.getTradeAndDuel().refreshDuelScreen();
		o.getPA().sendFrame126("", 6684);
		if (!goodSpace) {
			player.getActionSender().sendMessage("You have too many rules set to remove that item.");
			return true;
		}
		return true;
	}

	public void confirmDuel() {
		@SuppressWarnings("static-access")
		Player o = (Player) Server.playerHandler.players[player.duelingWith];
		if (o == null) {
			declineDuel();
			return;
		}
		String itemId = "";
		for (GameItem item : stakedItems) {
			if (Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
				itemId += player.getItems().getItemName(item.id) + " x " + Misc.format(item.amount) + "\\n";
			} else {
				itemId += player.getItems().getItemName(item.id) + "\\n";
			}
		}
		player.getPA().sendFrame126(itemId, 6516);
		itemId = "";
		for (GameItem item : o.getTradeAndDuel().stakedItems) {
			if (Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
				itemId += player.getItems().getItemName(item.id) + " x " + Misc.format(item.amount) + "\\n";
			} else {
				itemId += player.getItems().getItemName(item.id) + "\\n";
			}
		}
		player.getPA().sendFrame126(itemId, 6517);
		player.getPA().sendFrame126("", 8242);
		for (int i = 8238; i <= 8253; i++) {
			player.getPA().sendFrame126("", i);
		}
		player.getPA().sendFrame126("Hitpoints will be restored.", 8250);
		player.getPA().sendFrame126("Boosted stats will be restored.", 8238);
		if (player.duelRule[Rules.OBSTICLES_RULE.getRule()]) {
			player.getPA().sendFrame126("There will be obstacles in the arena.", 8239);
		}
		player.getPA().sendFrame126("", 8240);
		player.getPA().sendFrame126("", 8241);

		String[] rulesOption = { "Players cannot forfeit!", "Players cannot move.", "Players cannot use range.",
				"Players cannot use melee.", "Players cannot use magic.", "Players cannot drink pots.",
				"Players cannot eat food.", "Players cannot use prayer." };

		int lineNumber = 8242;
		for (int i = 0; i < 8; i++) {
			if (player.duelRule[i]) {
				player.getPA().sendFrame126("" + rulesOption[i], lineNumber);
				lineNumber++;
			}
		}
		player.getPA().sendFrame126("", 6571);
		player.getPA().sendFrame248(6412, 197);
		// c.getPA().showInterface(6412);
	}

	public void startDuel() {
		@SuppressWarnings("static-access")
		Player o = (Player) Server.playerHandler.players[player.duelingWith];
		if (o == null) {
			duelVictory();
		}
		player.headIconHints = 2;

		if (player.duelRule[Rules.PRAYER_RULE.getRule()]) {
			for (int p = 0; p < player.PRAYER.length; p++) { // reset prayer glows
				player.prayerActive[p] = false;
				player.getActionSender().sendConfig(player.PRAYER_GLOW[p], 0);
			}
			player.headIcon = -1;
			player.getPA().requestUpdates();
		}
		/**
		 * TODO: FIgure out what these are
		 */
		if (player.duelRule[11]) {
			player.getItems().removeItem(player.playerEquipment[0], 0);
		}
		if (player.duelRule[12]) {
			player.getItems().removeItem(player.playerEquipment[1], 1);
		}
		if (player.duelRule[13]) {
			player.getItems().removeItem(player.playerEquipment[2], 2);
		}
		if (player.duelRule[14]) {
			player.getItems().removeItem(player.playerEquipment[3], 3);
		}
		if (player.duelRule[15]) {
			player.getItems().removeItem(player.playerEquipment[4], 4);
		}
		if (player.duelRule[16]) {
			player.getItems().removeItem(player.playerEquipment[5], 5);
		}
		if (player.duelRule[17]) {
			player.getItems().removeItem(player.playerEquipment[7], 7);
		}
		if (player.duelRule[18]) {
			player.getItems().removeItem(player.playerEquipment[9], 9);
		}
		if (player.duelRule[19]) {
			player.getItems().removeItem(player.playerEquipment[10], 10);
		}
		if (player.duelRule[20]) {
			player.getItems().removeItem(player.playerEquipment[12], 12);
		}
		if (player.duelRule[21]) {
			player.getItems().removeItem(player.playerEquipment[13], 13);
		}
		player.duelStatus = 5;
		player.getPA().removeAllWindows();
		player.specAmount = 10;
		player.getItems().addSpecialBar(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]);

		if (player.duelRule[Rules.OBSTICLES_RULE.getRule()]) {
			if (player.duelRule[Rules.WALKING_RULE.getRule()]) {
				player.getPA().movePlayer(player.duelTeleX, player.duelTeleY, 0);
			} else {
				player.getPA().movePlayer(3366 + Misc.random(12), 3246 + Misc.random(6), 0);
			}
		} else {
			if (player.duelRule[Rules.WALKING_RULE.getRule()]) {
				player.getPA().movePlayer(player.duelTeleX, player.duelTeleY, 0);
			} else {
				player.getPA().movePlayer(3335 + Misc.random(12), 3246 + Misc.random(6), 0);
			}
		}

		player.getPA().createPlayerHints(10, o.playerId);
		player.getPA().showOption(3, 0, "Attack", 1);
		for (int i = 0; i < 20; i++) {
			player.playerLevel[i] = player.getPA().getLevelForXP(player.playerXP[i]);
			player.getPA().refreshSkill(i);
		}
		for (GameItem item : o.getTradeAndDuel().stakedItems) {
			otherStakedItems.add(new GameItem(item.id, item.amount));
		}
		player.getPA().requestUpdates();
	}

	public void duelVictory() {
		@SuppressWarnings("static-access")
		Player o = (Player) Server.playerHandler.players[player.duelingWith];
		if (o != null) {
			player.getPA().sendFrame126("" + o.combatLevel, 6839);
			player.getPA().sendFrame126(o.playerName, 6840);
			o.duelStatus = 0;
		} else {
			player.getPA().sendFrame126("", 6839);
			player.getPA().sendFrame126("", 6840);
		}
		player.duelStatus = 6;
		player.getCombat().resetPrayers();
		for (int i = 0; i < 20; i++) {
			player.playerLevel[i] = player.getPA().getLevelForXP(player.playerXP[i]);
			player.getPA().refreshSkill(i);
		}
		player.getPA().refreshSkill(3);
		duelRewardInterface();
		player.getPA().showInterface(6733);
		player.getPA().movePlayer(Constants.DUELING_RESPAWN_X + (Misc.random(Constants.RANDOM_DUELING_RESPAWN)),
				Constants.DUELING_RESPAWN_Y + (Misc.random(Constants.RANDOM_DUELING_RESPAWN)), 0);
		player.getPA().requestUpdates();
		player.getPA().showOption(3, 0, "Challenge", 3);
		player.getPA().createPlayerHints(10, -1);
		player.canOffer = true;
		player.duelSpaceReq = 0;
		player.duelingWith = 0;
		player.getCombat().resetPlayerAttack();
		player.duelRequested = false;
	}

	public void duelRewardInterface() {
		synchronized (player) {
			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(6822);
			player.getOutStream().writeWord(otherStakedItems.toArray().length);
			for (GameItem item : otherStakedItems) {
				if (item.amount > 254) {
					player.getOutStream().writeByte(255);
					player.getOutStream().writeDWord_v2(item.amount);
				} else {
					player.getOutStream().writeByte(item.amount);
				}
				if (item.id > Constants.ITEM_LIMIT || item.id < 0) {
					item.id = Constants.ITEM_LIMIT;
				}
				player.getOutStream().writeWordBigEndianA(item.id + 1);
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}
	}

	public void claimStakedItems() {
		for (GameItem item : otherStakedItems) {
			if (item.id > 0 && item.amount > 0) {
				if (Item.itemStackable[item.id]) {
					if (!player.getItems().addItem(item.id, item.amount)) {
						Server.itemHandler.createGroundItem(player, item.id, player.getX(), player.getY(), item.amount, player.getId());
					}
				} else {
					int amount = item.amount;
					for (int a = 1; a <= amount; a++) {
						if (!player.getItems().addItem(item.id, 1)) {
							Server.itemHandler.createGroundItem(player, item.id, player.getX(), player.getY(), 1, player.getId());
						}
					}
				}
			}
		}
		for (GameItem item : stakedItems) {
			if (item.id > 0 && item.amount > 0) {
				if (Item.itemStackable[item.id]) {
					if (!player.getItems().addItem(item.id, item.amount)) {
						Server.itemHandler.createGroundItem(player, item.id, player.getX(), player.getY(), item.amount, player.getId());
					}
				} else {
					int amount = item.amount;
					for (int a = 1; a <= amount; a++) {
						if (!player.getItems().addItem(item.id, 1)) {
							Server.itemHandler.createGroundItem(player, item.id, player.getX(), player.getY(), 1, player.getId());
						}
					}
				}
			}
		}
		resetDuel();
		resetDuelItems();
		player.duelStatus = 0;
	}

	public void declineDuel() {
		player.getPA().removeAllWindows();
		player.canOffer = true;
		player.duelStatus = 0;
		player.duelingWith = 0;
		player.duelSpaceReq = 0;
		player.duelRequested = false;
		for (GameItem item : stakedItems) {
			if (item.amount < 1)
				continue;
			if (Item.itemStackable[item.id] || Item.itemIsNote[item.id]) {
				player.getItems().addItem(item.id, item.amount);
			} else {
				player.getItems().addItem(item.id, 1);
			}
		}
		stakedItems.clear();
		for (int i = 0; i < player.duelRule.length; i++) {
			player.duelRule[i] = false;
		}
	}

	public void resetDuel() {
		player.getPA().showOption(3, 0, "Challenge", 3);
		player.headIconHints = 0;
		for (int i = 0; i < player.duelRule.length; i++) {
			player.duelRule[i] = false;
		}
		player.getPA().createPlayerHints(10, -1);
		player.duelStatus = 0;
		player.canOffer = true;
		player.duelSpaceReq = 0;
		player.duelingWith = 0;
		player.getPA().requestUpdates();
		player.getCombat().resetPlayerAttack();
		player.duelRequested = false;
	}

	public void resetDuelItems() {
		stakedItems.clear();
		otherStakedItems.clear();
	}

	public void changeDuelStuff() {
		@SuppressWarnings("static-access")
		Player o = (Player) Server.playerHandler.players[player.duelingWith];
		if (o == null) {
			return;
		}
		o.duelStatus = 1;
		player.duelStatus = 1;
		o.getPA().sendFrame126("", 6684);
		player.getPA().sendFrame126("", 6684);
	}

	public void selectRule(int i) { // rules
		@SuppressWarnings("static-access")
		Player o = (Player) Server.playerHandler.players[player.duelingWith];
		if (o == null) {
			return;
		}
		if (!player.canOffer)
			return;
		changeDuelStuff();
		o.duelSlot = player.duelSlot;
		if (i >= 11 && player.duelSlot > -1) {
			if (player.playerEquipment[player.duelSlot] > 0) {
				if (!player.duelRule[i]) {
					player.duelSpaceReq++;
				} else {
					player.duelSpaceReq--;
				}
			}
			if (o.playerEquipment[o.duelSlot] > 0) {
				if (!o.duelRule[i]) {
					o.duelSpaceReq++;
				} else {
					o.duelSpaceReq--;
				}
			}
		}

		if (i >= 11) {
			if (player.getItems().freeSlots() < (player.duelSpaceReq) || o.getItems().freeSlots() < (o.duelSpaceReq)) {
				player.getActionSender().sendMessage("You or your opponent don't have the required space to set this rule.");
				if (player.playerEquipment[player.duelSlot] > 0) {
					player.duelSpaceReq--;
				}
				if (o.playerEquipment[o.duelSlot] > 0) {
					o.duelSpaceReq--;
				}
				return;
			}
		}

		if (!player.duelRule[i]) {
			player.duelRule[i] = true;
			player.duelOption += player.DUEL_RULE_ID[i];
		} else {
			player.duelRule[i] = false;
			player.duelOption -= player.DUEL_RULE_ID[i];
		}

		player.getPA().sendFrame87(286, player.duelOption);
		o.duelOption = player.duelOption;
		o.duelRule[i] = player.duelRule[i];
		o.getPA().sendFrame87(286, o.duelOption);

		if (player.duelRule[Rules.OBSTICLES_RULE.getRule()]) {
			if (player.duelRule[Rules.WALKING_RULE.getRule()]) {
				player.duelTeleX = 3366 + Misc.random(12);
				o.duelTeleX = player.duelTeleX - 1;
				player.duelTeleY = 3246 + Misc.random(6);
				o.duelTeleY = player.duelTeleY;
			}
		} else {
			if (player.duelRule[Rules.WALKING_RULE.getRule()]) {
				player.duelTeleX = 3335 + Misc.random(12);
				o.duelTeleX = player.duelTeleX - 1;
				player.duelTeleY = 3246 + Misc.random(6);
				o.duelTeleY = player.duelTeleY;
			}
		}

	}

}
