package server.model.shops;

import server.Constants;
import server.Server;
import server.model.items.Item;
import server.model.players.Player;

public class ShopAssistant {

	private Player player;
	
	public ShopAssistant(Player Player) {
		this.player = Player;
	}
	
	/**
	*Shops
	**/
	
	@SuppressWarnings("static-access")
	public void openShop(int ShopID){		
		player.getItems().resetItems(3823);
		resetShop(ShopID);
		player.isShopping = true;
		player.myShopId = ShopID;
		player.getPA().sendFrame248(3824, 3822);
		player.getPA().sendFrame126(Server.shopHandler.ShopName[ShopID], 3901);
	}
	
	@SuppressWarnings("static-access")
	public void updatePlayerShop() {
		for (int i = 1; i < Constants.MAX_PLAYERS; i++) {
			if (Server.playerHandler.players[i] != null) {
				if (Server.playerHandler.players[i].isShopping == true && Server.playerHandler.players[i].myShopId == player.myShopId && i != player.playerId) {
					Server.playerHandler.players[i].updateShop = true;
				}
			}
		}
	}
	
	
	public void updateshop(int i){
		resetShop(i);
	}
	
	@SuppressWarnings("static-access")
	public void resetShop(int ShopID) {
		synchronized(player) {
			int TotalItems = 0;
			for (int i = 0; i < Server.shopHandler.MaxShopItems; i++) {
				if (Server.shopHandler.ShopItems[ShopID][i] > 0) {
					TotalItems++;
				}
			}
			if (TotalItems > Server.shopHandler.MaxShopItems) {
				TotalItems = Server.shopHandler.MaxShopItems;
			}
			player.getOutStream().createFrameVarSizeWord(53);
			player.getOutStream().writeWord(3900);
			player.getOutStream().writeWord(TotalItems);
			int TotalCount = 0;
			for (int i = 0; i < Server.shopHandler.ShopItems.length; i++) {
				if (Server.shopHandler.ShopItems[ShopID][i] > 0 || i <= Server.shopHandler.ShopItemsStandard[ShopID]) {
					if (Server.shopHandler.ShopItemsN[ShopID][i] > 254) {
						player.getOutStream().writeByte(255); 					
						player.getOutStream().writeDWord_v2(Server.shopHandler.ShopItemsN[ShopID][i]);	
					} else {
						player.getOutStream().writeByte(Server.shopHandler.ShopItemsN[ShopID][i]);
					}
					if (Server.shopHandler.ShopItems[ShopID][i] > Constants.ITEM_LIMIT || Server.shopHandler.ShopItems[ShopID][i] < 0) {
						Server.shopHandler.ShopItems[ShopID][i] = Constants.ITEM_LIMIT;
					}
					player.getOutStream().writeWordBigEndianA(Server.shopHandler.ShopItems[ShopID][i]);
					TotalCount++;
				}
				if (TotalCount > TotalItems) {
					break;
				}
			}
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();	
		}
	}
	
	
	@SuppressWarnings({ "unused", "static-access" })
	public double getItemShopValue(int ItemID, int Type, int fromSlot) {
		double ShopValue = 1;
		double Overstock = 0;
		double TotPrice = 0;
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == ItemID) {
					ShopValue = Server.itemHandler.ItemList[i].ShopValue;
				}
			}
		}
		
		TotPrice = ShopValue;

		if (Server.shopHandler.ShopBModifier[player.myShopId] == 1) {
			TotPrice *= 1; 
			TotPrice *= 1;
			if (Type == 1) {
				TotPrice *= 1; 
			}
		} else if (Type == 1) {
			TotPrice *= 1; 
		}
		return TotPrice;
	}
	
	public int getItemShopValue(int itemId) {
		for (int i = 0; i < Constants.ITEM_LIMIT; i++) {
			if (Server.itemHandler.ItemList[i] != null) {
				if (Server.itemHandler.ItemList[i].itemId == itemId) {
					return (int)Server.itemHandler.ItemList[i].ShopValue;
				}
			}	
		}
		return 0;
	}
	
	
	
	/**
	*buy item from shop (Shop Price)
	**/
	
	public void buyFromShopPrice(int removeId, int removeSlot){
		int ShopValue = (int) Math.floor(getItemShopValue(removeId, 1, removeSlot) * 0.90);
		ShopValue *= 1.15;
		String ShopAdd = "";
		if (player.myShopId >= 17) {
			player.getActionSender().sendMessage(player.getItems().getItemName(removeId)+": currently costs " + getSpecialItemValue(removeId) + " points.");
			return;
		}
		if (player.myShopId == 15) {
			player.getActionSender().sendMessage("This item current costs " + player.getItems().getUntradePrice(removeId) + " coins.");
			return;
		}
		if (ShopValue >= 1000 && ShopValue < 1000000) {
			ShopAdd = " (" + (ShopValue / 1000) + "K)";
		} else if (ShopValue >= 1000000) {
			ShopAdd = " (" + (ShopValue / 1000000) + " million)";
		}
		player.getActionSender().sendMessage(player.getItems().getItemName(removeId)+": currently costs "+ShopValue+" coins"+ShopAdd);
	}
	
	public int getSpecialItemValue(int id) {
		switch (id) {
			case 6889:
			case 6914:
			return 200;
			case 6916:
			case 6918:
			case 6920:
			case 6922:
			case 6924:
			return 50;
			case 11663:
			case 11664:
			case 11665:
			case 8842:
			return 30;
			case 8839:
			case 8840:
			return 75;
			case 10499:
			return 20;
			case 8845:
			return 5;
			case 8846:
			return 10;
			case 8847:
			return 15;
			case 8848:
			return 20;
			case 8849:
			case 8850:
			return 25;
			case 7462:
			return 40;
			case 10551:
			return 100;
		}
		return 0;
	}
	
	
	
	/**
	*Sell item to shop (Shop Price)
	**/
	@SuppressWarnings("static-access")
	public void sellToShopPrice(int removeId, int removeSlot) {
		for (int i : Constants.ITEM_SELLABLE) {
			if (i == removeId) {
				player.getActionSender().sendMessage("You can't sell "+player.getItems().getItemName(removeId).toLowerCase()+".");
				return;
			} 
		}
		boolean IsIn = false;
		if (Server.shopHandler.ShopSModifier[player.myShopId] > 1) {
			for (int j = 0; j <= Server.shopHandler.ShopItemsStandard[player.myShopId]; j++) {
				if (removeId == (Server.shopHandler.ShopItems[player.myShopId][j] - 1)) {
					IsIn = true;
					break;
				}
			}
		} else {
			IsIn = true;
		}
		if (IsIn == false) {
			player.getActionSender().sendMessage("You can't sell "+player.getItems().getItemName(removeId).toLowerCase()+" to this store.");
		} else {
			int ShopValue = (int)Math.floor(getItemShopValue(removeId, 1, removeSlot));
			String ShopAdd = "";
			if (ShopValue >= 1000 && ShopValue < 1000000) {
				ShopAdd = " (" + (ShopValue / 1000) + "K)";
			} else if (ShopValue >= 1000000) {
				ShopAdd = " (" + (ShopValue / 1000000) + " million)";
			}
			player.getActionSender().sendMessage(player.getItems().getItemName(removeId)+": shop will buy for "+ShopValue+" coins"+ShopAdd);
		}
	}
	
	
	
	@SuppressWarnings("static-access")
	public boolean sellItem(int itemID, int fromSlot, int amount) {
		if (player.myShopId == 14)
			return false;
		for (int i : Constants.ITEM_SELLABLE) {
			if (i == itemID) {
				player.getActionSender().sendMessage("You can't sell "+player.getItems().getItemName(itemID).toLowerCase()+".");
				return false;
			} 
		}
		if(player.playerRights == 2 && !Constants.ADMIN_CAN_SELL_ITEMS) {
			player.getActionSender().sendMessage("Selling items as an admin has been disabled.");
			return false;
		}
		
		if (amount > 0 && itemID == (player.playerItems[fromSlot] - 1)) {
			if (Server.shopHandler.ShopSModifier[player.myShopId] > 1) {
				boolean IsIn = false;
				for (int i = 0; i <= Server.shopHandler.ShopItemsStandard[player.myShopId]; i++) {
					if (itemID == (Server.shopHandler.ShopItems[player.myShopId][i] - 1)) {
						IsIn = true;
						break;
					}
				}
				if (IsIn == false) {
					player.getActionSender().sendMessage("You can't sell "+player.getItems().getItemName(itemID).toLowerCase()+" to this store.");
					return false;
				}
			}

			if (amount > player.playerItemsN[fromSlot] && (Item.itemIsNote[(player.playerItems[fromSlot] - 1)] == true || Item.itemStackable[(player.playerItems[fromSlot] - 1)] == true)) {
				amount = player.playerItemsN[fromSlot];
			} else if (amount > player.getItems().getItemAmount(itemID) && Item.itemIsNote[(player.playerItems[fromSlot] - 1)] == false && Item.itemStackable[(player.playerItems[fromSlot] - 1)] == false) {
				amount = player.getItems().getItemAmount(itemID);
			}
			//double ShopValue;
			//double TotPrice;
			int TotPrice2 = 0;
			//int Overstock;
			for (int i = amount; i > 0; i--) {
				TotPrice2 = (int) Math.floor(getItemShopValue(itemID, 1, fromSlot) * 0.90);
				if (player.getItems().freeSlots() > 0 || player.getItems().playerHasItem(995)) {
					if (Item.itemIsNote[itemID] == false) {
						player.getItems().deleteItem(itemID, player.getItems().getItemSlot(itemID), 1);
					} else {
						player.getItems().deleteItem(itemID, fromSlot, 1);
					}
					player.getItems().addItem(995, TotPrice2);
					addShopItem(itemID, 1);
				} else {
					player.getActionSender().sendMessage("You don't have enough space in your inventory.");
					break;
				}
			}
			player.getItems().resetItems(3823);
			resetShop(player.myShopId);
			updatePlayerShop();
			return true;
		}
		return true;
	}
	
	@SuppressWarnings("static-access")
	public boolean addShopItem(int itemID, int amount) {
		boolean Added = false;
		if (amount <= 0) {
			return false;
		}
		if (Item.itemIsNote[itemID] == true) {
			itemID = player.getItems().getUnnotedItem(itemID);
		}
		for (int i = 0; i < Server.shopHandler.ShopItems.length; i++) {
			if ((Server.shopHandler.ShopItems[player.myShopId][i] - 1) == itemID) {
				Server.shopHandler.ShopItemsN[player.myShopId][i] += amount;
				Added = true;
			}
		}
		if (Added == false) {
			for (int i = 0; i < Server.shopHandler.ShopItems.length; i++) {
				if (Server.shopHandler.ShopItems[player.myShopId][i] == 0) {
					Server.shopHandler.ShopItems[player.myShopId][i] = (itemID + 1);
					Server.shopHandler.ShopItemsN[player.myShopId][i] = amount;
					Server.shopHandler.ShopItemsDelay[player.myShopId][i] = 0;
					break;
				}
			}
		}
		return true;
	}
	
	@SuppressWarnings("static-access")
	public boolean buyItem(int itemID, int fromSlot, int amount) {
		if (player.myShopId == 14) {
			skillBuy(itemID);
			return false;
		} else if (player.myShopId == 15) {
			buyVoid(itemID);
			return false;		
		}
		if (amount > 0) {
			if (amount > Server.shopHandler.ShopItemsN[player.myShopId][fromSlot]) {
				amount = Server.shopHandler.ShopItemsN[player.myShopId][fromSlot];
			}
			//double ShopValue;
			//double TotPrice;
			int TotPrice2 = 0;
			//int Overstock;
			int Slot = 0;
			int Slot1 = 0;//Tokkul
			@SuppressWarnings("unused")
			int Slot2 = 0;//Pking Points
			if (player.myShopId >= 17) {
				handleOtherShop(itemID);
				return false;
			}	
			for (int i = amount; i > 0; i--) {
				TotPrice2 = (int)Math.floor(getItemShopValue(itemID, 0, fromSlot));
				Slot = player.getItems().getItemSlot(995);
				Slot1 = player.getItems().getItemSlot(6529);
				if (Slot == -1 && player.myShopId != 29 && player.myShopId != 30 && player.myShopId != 31 && player.myShopId != 47) {
					player.getActionSender().sendMessage("You don't have enough coins.");
					break;
				}
				if(Slot1 == -1 && player.myShopId == 29 || player.myShopId == 30 || player.myShopId == 31) {
					player.getActionSender().sendMessage("You don't have enough tokkul.");
					break;
				}
                if(TotPrice2 <= 1) {
                	TotPrice2 = (int)Math.floor(getItemShopValue(itemID, 0, fromSlot));
                	TotPrice2 *= 1.66;
                }
                if(player.myShopId != 29 || player.myShopId != 30 || player.myShopId != 31 || player.myShopId != 47) {
					if (player.playerItemsN[Slot] >= TotPrice2) {
						if (player.getItems().freeSlots() > 0) {
							player.getItems().deleteItem(995, player.getItems().getItemSlot(995), TotPrice2);
							player.getItems().addItem(itemID, 1);
							Server.shopHandler.ShopItemsN[player.myShopId][fromSlot] -= 1;
							Server.shopHandler.ShopItemsDelay[player.myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > Server.shopHandler.ShopItemsStandard[player.myShopId]) {
								Server.shopHandler.ShopItems[player.myShopId][fromSlot] = 0;
							}
						} else {
							player.getActionSender().sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						player.getActionSender().sendMessage("You don't have enough coins.");
						break;
					}
                }
                if(player.myShopId == 29 || player.myShopId == 30 || player.myShopId == 31) {
                	if (player.playerItemsN[Slot1] >= TotPrice2) {
						if (player.getItems().freeSlots() > 0) {
							player.getItems().deleteItem(6529, player.getItems().getItemSlot(6529), TotPrice2);
							player.getItems().addItem(itemID, 1);
							Server.shopHandler.ShopItemsN[player.myShopId][fromSlot] -= 1;
							Server.shopHandler.ShopItemsDelay[player.myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > Server.shopHandler.ShopItemsStandard[player.myShopId]) {
								Server.shopHandler.ShopItems[player.myShopId][fromSlot] = 0;
							}
						} else {
							player.getActionSender().sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						player.getActionSender().sendMessage("You don't have enough tokkul.");
						break;
					}
                }
                if(player.myShopId == 47) {
                	if (player.pkPoints >= TotPrice2) {
						if (player.getItems().freeSlots() > 0) {
							player.pkPoints -= TotPrice2;
							player.getItems().addItem(itemID, 1);
							Server.shopHandler.ShopItemsN[player.myShopId][fromSlot] -= 1;
							Server.shopHandler.ShopItemsDelay[player.myShopId][fromSlot] = 0;
							if ((fromSlot + 1) > Server.shopHandler.ShopItemsStandard[player.myShopId]) {
								Server.shopHandler.ShopItems[player.myShopId][fromSlot] = 0;
							}
						} else {
							player.getActionSender().sendMessage("You don't have enough space in your inventory.");
							break;
						}
					} else {
						player.getActionSender().sendMessage("You don't have enough Pk Points.");
						break;
					}
                }
			}
			player.getItems().resetItems(3823);
			resetShop(player.myShopId);
			updatePlayerShop();
			return true;
		}
		return false;
	}	
	
		public void handleOtherShop(int itemID) {
			if (player.myShopId == 17) {
				if (player.magePoints >= getSpecialItemValue(itemID)) {
					if (player.getItems().freeSlots() > 0){
						player.magePoints -= getSpecialItemValue(itemID);
						player.getItems().addItem(itemID,1);	
						player.getItems().resetItems(3823);
					}
				} else {
					player.getActionSender().sendMessage("You do not have enough points to buy this item.");			
				}
			} else if (player.myShopId == 18) {
				if (player.pcPoints >= getSpecialItemValue(itemID)) {
					if (player.getItems().freeSlots() > 0){
						player.pcPoints -= getSpecialItemValue(itemID);
						player.getItems().addItem(itemID,1);
						player.getItems().resetItems(3823);
					}
				} else {
					player.getActionSender().sendMessage("You do not have enough points to buy this item.");			
				}

			}
		}
		
		public void openSkillCape() {
			int capes = get99Count();
			if (capes > 1)
				capes = 1;
			else
				capes = 0;
			player.myShopId = 14;
			setupSkillCapes(capes, get99Count());		
		}
		
		
		
		/*public int[][] skillCapes = {{0,9747,4319,2679},{1,2683,4329,2685},{2,2680,4359,2682},{3,2701,4341,2703},{4,2686,4351,2688},{5,2689,4347,2691},{6,2692,4343,2691},
									{7,2737,4325,2733},{8,2734,4353,2736},{9,2716,4337,2718},{10,2728,4335,2730},{11,2695,4321,2697},{12,2713,4327,2715},{13,2725,4357,2727},
									{14,2722,4345,2724},{15,2707,4339,2709},{16,2704,4317,2706},{17,2710,4361,2712},{18,2719,4355,2721},{19,2737,4331,2739},{20,2698,4333,2700}};*/
		public int[] skillCapes = {9747,9753,9750,9768,9756,9759,9762,9801,9807,9783,9798,9804,9780,9795,9792,9774,9771,9777,9786,9810,9765};
		public int get99Count() {
			int count = 0;
			for (int j = 0; j < player.playerLevel.length; j++) {
				if (player.getLevelForXP(player.playerXP[j]) >= 99) {
					count++;				
				}			
			}		
			return count;
		}
		
		@SuppressWarnings("static-access")
		public void setupSkillCapes(int capes, int capes2) {
			synchronized(player) {
				player.getItems().resetItems(3823);
				player.isShopping = true;
				player.myShopId = 14;
				player.getPA().sendFrame248(3824, 3822);
				player.getPA().sendFrame126("Skillcape Shop", 3901);
				
				int TotalItems = 0;
				TotalItems = capes2;
				if (TotalItems > Server.shopHandler.MaxShopItems) {
					TotalItems = Server.shopHandler.MaxShopItems;
				}
				player.getOutStream().createFrameVarSizeWord(53);
				player.getOutStream().writeWord(3900);
				player.getOutStream().writeWord(TotalItems);
				@SuppressWarnings("unused")
				int TotalCount = 0;
				for (int i = 0; i < 21; i++) {
					if (player.getLevelForXP(player.playerXP[i]) < 99)
						continue;
					player.getOutStream().writeByte(1);
					player.getOutStream().writeWordBigEndianA(skillCapes[i] + 2);
					TotalCount++;
				}
				player.getOutStream().endFrameVarSizeWord();
				player.flushOutStream();	
			}
		}
		
		public void skillBuy(int item) {
			int nn = get99Count();
			if (nn > 1)
				nn = 1;
			else
				nn = 0;			
			for (int j = 0; j < skillCapes.length; j++) {
				if (skillCapes[j] == item || skillCapes[j]+1 == item) {
					if (player.getItems().freeSlots() > 1) {
						if (player.getItems().playerHasItem(995,99000)) {
							if (player.getLevelForXP(player.playerXP[j]) >= 99) {
								player.getItems().deleteItem(995, player.getItems().getItemSlot(995), 99000);
								player.getItems().addItem(skillCapes[j] + nn,1);
								player.getItems().addItem(skillCapes[j] + 2,1);
							} else {
								player.getActionSender().sendMessage("You must have 99 in the skill of the cape you're trying to buy.");
							}
						} else {
							player.getActionSender().sendMessage("You need 99k to buy this item.");
						}
					} else {
						player.getActionSender().sendMessage("You must have at least 1 inventory spaces to buy this item.");					
					}				
				}
				/*if (skillCapes[j][1 + nn] == item) {
					if (c.getItems().freeSlots() >= 1) {
						if (c.getItems().playerHasItem(995,99000)) {
							if (c.getLevelForXP(c.playerXP[j]) >= 99) {
								c.getItems().deleteItem(995, c.getItems().getItemSlot(995), 99000);
								c.getItems().addItem(skillCapes[j] + nn,1);
								c.getItems().addItem(skillCapes[j] + 2,1);
							} else {
								c.sendMessage("You must have 99 in the skill of the cape you're trying to buy.");
							}
						} else {
							c.sendMessage("You need 99k to buy this item.");
						}
					} else {
						c.sendMessage("You must have at least 1 inventory spaces to buy this item.");					
					}
					break;				
				}*/			
			}
			player.getItems().resetItems(3823);			
		}
		
		public void openVoid() {
			/*synchronized(c) {
				c.getItems().resetItems(3823);
				c.isShopping = true;
				c.myShopId = 15;
				c.getPA().sendFrame248(3824, 3822);
				c.getPA().sendFrame126("Void Recovery", 3901);
				
				int TotalItems = 5;
				c.getOutStream().createFrameVarSizeWord(53);
				c.getOutStream().writeWord(3900);
				c.getOutStream().writeWord(TotalItems);
				for (int i = 0; i < c.voidStatus.length; i++) {
					c.getOutStream().writeByte(c.voidStatus[i]);
					c.getOutStream().writeWordBigEndianA(2519 + i * 2);
				}
				c.getOutStream().endFrameVarSizeWord();
				c.flushOutStream();	
			}*/		
		}

		public void buyVoid(int item) {
			/*if (item > 2527 || item < 2518)
				return;
			//c.sendMessage("" + item);
			if (c.voidStatus[(item-2518)/2] > 0) {
				if (c.getItems().freeSlots() >= 1) {
					if (c.getItems().playerHasItem(995,c.getItems().getUntradePrice(item))) {
						c.voidStatus[(item-2518)/2]--;
						c.getItems().deleteItem(995,c.getItems().getItemSlot(995), c.getItems().getUntradePrice(item));
						c.getItems().addItem(item,1);
						openVoid();
					} else {
						c.sendMessage("This item costs " + c.getItems().getUntradePrice(item) + " coins to rebuy.");				
					}
				} else {
					c.sendMessage("I should have a free inventory space.");
				}
			} else {
				c.sendMessage("I don't need to recover this item from the void knights.");
			}*/
		}


}

