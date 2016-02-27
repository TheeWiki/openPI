package server.model.players.packet.impl;

import server.Constants;
import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.items.GameItem;
import server.model.minigames.duel_arena.Rules;
import server.model.players.Player;
import server.model.players.EquipmentListener;
import server.model.players.packet.PacketType;
import server.model.players.skills.magic.Enchantment;
import server.util.Misc;
import server.util.Plugin;
import server.world.Location;

/**
 * Clicking most buttons
 **/
public class ClickingButtons implements PacketType {

	@SuppressWarnings({ "static-access", "null" })
	@Override
	public void processPacket(final Player player, int packetType, int packetSize) {
		int actionButtonId = Misc.hexToInt(player.getInStream().buffer, 0, packetSize);
		// int actionButtonId = c.getInStream().readShort();
		if (player.isDead)
			return;
		if (player.playerRights >= 2)
			Misc.println(player.playerName + " - actionbutton: " + actionButtonId);

		Plugin.execute("buttons", player, actionButtonId);

		switch (actionButtonId) {
		case 485:
			player.getRG().exchangePoints();
			break;

		case 150:
				player.autoRet = 1;
				break;
		case 151:
				player.autoRet = 0;
			break;
			
		case 9190: // 1st option (5)
		case 9191: // 2nd option (5)
		case 9192: // 3rd option (5)
		case 9193: // 4th option (5)
		case 9194: // 5th option (5)
			if (player.dialogueContainer != null) {
				player.dialogueContainer.execute((actionButtonId - 9190) + 1);
				return;
			}
			break;

		case 58253:
			// c.getPA().showInterface(15106);
			player.getItems().writeBonus();
			break;
			
		case 9178:
			if (player.dialogueContainer != null) {
				player.dialogueContainer.execute((actionButtonId - 9178) + 1);
				return;
			}
			switch(player.dialogueAction)
			{
			case 485:
				player.getRG().buyArrows();
				break;
			case 2:
				player.getPA().startTeleport(3428, 3538, 0, "modern");	
				break;
			}
			break;

		case 9179:
			if (player.dialogueContainer != null) {
				player.dialogueContainer.execute((actionButtonId - 9178) + 1);
				return;
			}
			switch(player.dialogueAction) {
			case 485:
				player.getRG().exchangePoints();
				break;
			case 2:
				player.getPA().startTeleport(2884, 3395, 0, "modern");	
				break;
			}
			break;

		case 9180:
			if (player.dialogueContainer != null) {
				player.dialogueContainer.execute((actionButtonId - 9178) + 1);
				return;
			}
			switch(player.dialogueAction) {
			case 485:
				player.getRG().howAmIDoing();
				break;
				
			}
			break;

		case 9181:
			if (player.dialogueContainer != null) {
				player.dialogueContainer.execute((actionButtonId - 9178) + 1);
				return;
			}
			switch(player.dialogueAction) {
			case 2:
				player.getPA().startTeleport(2669, 3714, 0, "modern");
				break;
			}
			break;

		case 1093:
		case 1094:
		case 1097:
			if (player.autocastId > 0) {
				player.getPA().resetAutocast();
			} else {
	
				if (player.playerMagicBook == 1) {
					if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4675)
						player.setSidebarInterface(0, 1689);
					else
						player.getActionSender().sendMessage("You can't autocast ancients without an ancient staff.");
				} else if (player.playerMagicBook == 0) {
					if (player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4170) {
						player.setSidebarInterface(0, 12050);
					} else {
						player.setSidebarInterface(0, 1829);
					}
				}

			}
			break;

		case 9157:
//			if (c.dialogueAction == 1) {
//				c.getDH().sendDialogues(38, -1);
//			}
			if (player.dialogueContainer != null) {
				player.dialogueContainer.execute((actionButtonId - 9157) + 1);
				return;
			}
			switch(player.dialogueAction)
			{
			case 1:
				int r = 4;
				// int r = Misc.random(3);
				switch (r) {
				case 0:
					player.getPA().movePlayer(3534, 9677, 0);
					break;

				case 1:
					player.getPA().movePlayer(3534, 9712, 0);
					break;

				case 2:
					player.getPA().movePlayer(3568, 9712, 0);
					break;

				case 3:
					player.getPA().movePlayer(3568, 9677, 0);
					break;
				case 4:
					player.getPA().movePlayer(3551, 9694, 0);
					break;
				}
			}
		break;
		case 9158:
			if (player.dialogueContainer != null) {
				player.dialogueContainer.execute((actionButtonId - 9157) + 1);
				return;
			}
			break;
		case 9058:
			if (player.dialogueContainer != null) {
				player.dialogueContainer.execute((actionButtonId - 9157) + 1);
				return;
			}
			break;
		case 9167: // 1st option (3)
		case 9168: // 2nd option (3)
		case 9169: // 3rd option (3)
			if (player.dialogueContainer != null) {
				player.dialogueContainer.execute((actionButtonId - 9167) + 1);
				return;
			}
			break;

		/** Specials **/
		case 29188:
			player.specBarId = 7636; // the special attack text - sendframe126(S P E
								// C I A L A T T A C K, c.specBarId);
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		case 29163:
			player.specBarId = 7611;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		case 33033:
			player.specBarId = 8505;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		case 29038:
			player.specBarId = 7486;
			/*
			 * if (c.specAmount >= 5) { c.attackTimer = 0;
			 * c.getCombat().attackPlayer(c.playerIndex); c.usingSpecial = true;
			 * c.specAmount -= 5; }
			 */
			player.getCombat().handleGmaulPlayer();
			player.getItems().updateSpecialBar();
			break;

		case 29063:
			if (player.getCombat().checkSpecAmount(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()])) {
				player.gfx0(246);
				player.forcedChat("Raarrrrrgggggghhhhhhh!");
				player.startAnimation(1056);
				player.playerLevel[2] = player.getLevelForXP(player.playerXP[2]) + (player.getLevelForXP(player.playerXP[2]) * 15 / 100);
				player.getPA().refreshSkill(2);
				player.getItems().updateSpecialBar();
			} else {
				player.getActionSender().sendMessage("You don't have the required special energy to use this attack.");
			}
			break;

		case 48023:
			player.specBarId = 12335;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		case 29138:
			player.specBarId = 7586;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		case 29113:
			player.specBarId = 7561;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		case 29238:
			player.specBarId = 7686;
			player.usingSpecial = !player.usingSpecial;
			player.getItems().updateSpecialBar();
			break;

		/** Dueling **/
		case 26065: // no forfeit
		case 26040:
			player.duelSlot = -1;
			player.getTradeAndDuel().selectRule(0);
			break;

		case 26066: // no movement
		case 26048:
			player.duelSlot = -1;
			player.getTradeAndDuel().selectRule(1);
			break;

		case 26069: // no range
		case 26042:
			player.duelSlot = -1;
			player.getTradeAndDuel().selectRule(2);
			break;

		case 26070: // no melee
		case 26043:
			player.duelSlot = -1;
			player.getTradeAndDuel().selectRule(3);
			break;

		case 26071: // no mage
		case 26041:
			player.duelSlot = -1;
			player.getTradeAndDuel().selectRule(4);
			break;

		case 26072: // no drinks
		case 26045:
			player.duelSlot = -1;
			player.getTradeAndDuel().selectRule(5);
			break;

		case 26073: // no food
		case 26046:
			player.duelSlot = -1;
			player.getTradeAndDuel().selectRule(6);
			break;

		case 26074: // no prayer
		case 26047:
			player.duelSlot = -1;
			player.getTradeAndDuel().selectRule(7);
			break;

		case 26076: // obsticals
		case 26075:
			player.duelSlot = -1;
			player.getTradeAndDuel().selectRule(8);
			break;

		case 2158: // fun weapons
		case 2157:
			player.duelSlot = -1;
			player.getTradeAndDuel().selectRule(9);
			break;

		case 30136: // sp attack
		case 30137:
			player.duelSlot = -1;
			player.getTradeAndDuel().selectRule(10);
			break;

		case 53245: // no helm
			player.duelSlot = 0;
			player.getTradeAndDuel().selectRule(11);
			break;

		case 53246: // no cape
			player.duelSlot = 1;
			player.getTradeAndDuel().selectRule(12);
			break;

		case 53247: // no ammy
			player.duelSlot = 2;
			player.getTradeAndDuel().selectRule(13);
			break;

		case 53249: // no weapon.
			player.duelSlot = 3;
			player.getTradeAndDuel().selectRule(14);
			break;

		case 53250: // no body
			player.duelSlot = 4;
			player.getTradeAndDuel().selectRule(15);
			break;

		case 53251: // no shield
			player.duelSlot = 5;
			player.getTradeAndDuel().selectRule(16);
			break;

		case 53252: // no legs
			player.duelSlot = 7;
			player.getTradeAndDuel().selectRule(17);
			break;

		case 53255: // no gloves
			player.duelSlot = 9;
			player.getTradeAndDuel().selectRule(18);
			break;

		case 53254: // no boots
			player.duelSlot = 10;
			player.getTradeAndDuel().selectRule(19);
			break;

		case 53253: // no rings
			player.duelSlot = 12;
			player.getTradeAndDuel().selectRule(20);
			break;

		case 53248: // no arrows
			player.duelSlot = 13;
			player.getTradeAndDuel().selectRule(21);
			break;

		/*
		 * Accepting Duel Interface Fixed by: Ardi Remember to click thanks
		 * button & karma (reputation) for Ardi, if you're using this.
		 */
		case 26018:
			if (player.duelStatus == 5) {
				// c.sendMessage("This glitch has been fixed by Ardi, sorry
				// sir.");
				return;
			}
			if (player.inDuelArena()) {
				Player o = (Player) Server.playerHandler.players[player.duelingWith];
				if (o == null) {
					player.getTradeAndDuel().declineDuel();
					o.getTradeAndDuel().declineDuel();
					return;
				}

				if (player.duelRule[Rules.RANGE_RULE.getRule()] && player.duelRule[Rules.MELEE_RULE.getRule()] && player.duelRule[Rules.MAGIC_RULE.getRule()]) {
					player.getActionSender().sendMessage("You won't be able to attack the player with the rules you have set.");
					break;
				}
				player.duelStatus = 2;
				if (player.duelStatus == 2) {
					player.getPA().sendFrame126("Waiting for other player...", 6684);
					o.getPA().sendFrame126("Other player has accepted.", 6684);
				}
				if (o.duelStatus == 2) {
					o.getPA().sendFrame126("Waiting for other player...", 6684);
					player.getPA().sendFrame126("Other player has accepted.", 6684);
				}

				if (player.duelStatus == 2 && o.duelStatus == 2) {
					player.canOffer = false;
					o.canOffer = false;
					player.duelStatus = 3;
					o.duelStatus = 3;
					player.getTradeAndDuel().confirmDuel();
					o.getTradeAndDuel().confirmDuel();
				}
			} else {
				Player o = (Player) Server.playerHandler.players[player.duelingWith];
				player.getTradeAndDuel().declineDuel();
				o.getTradeAndDuel().declineDuel();
				player.getActionSender().sendMessage("You can't stake out of Duel Arena.");
			}
			break;

		/*
		 * Accepting Duel Interface Fixed by: Ardi Remember to click thanks
		 * button & karma (reputation) for Ardi, if you're using this.
		 */
		case 25120:
			if (player.duelStatus == 5) {
				// c.sendMessage("This glitch has been fixed by Ardi, sorry
				// sir.");
				return;
			}
			if (player.inDuelArena()) {
				if (player.duelStatus == 5) {
					break;
				}
				Player o1 = (Player) Server.playerHandler.players[player.duelingWith];
				if (o1 == null) {
					player.getTradeAndDuel().declineDuel();
					return;
				}

				player.duelStatus = 4;
				if (o1.duelStatus == 4 && player.duelStatus == 4) {
					player.getTradeAndDuel().startDuel();
					o1.getTradeAndDuel().startDuel();
					o1.duelCount = 4;
					player.duelCount = 4;
					player.duelDelay = System.currentTimeMillis();
					o1.duelDelay = System.currentTimeMillis();
				} else {
					player.getPA().sendFrame126("Waiting for other player...", 6571);
					o1.getPA().sendFrame126("Other player has accepted", 6571);
				}
			} else {
				Player o = (Player) Server.playerHandler.players[player.duelingWith];
				player.getTradeAndDuel().declineDuel();
				o.getTradeAndDuel().declineDuel();
				player.getActionSender().sendMessage("You can't stake out of Duel Arena.");
			}
			break;

		case 4169: // god spell charge
			player.usingMagic = true;
			if (!player.getCombat().checkMagicReqs(48)) {
				break;
			}

			if (System.currentTimeMillis() - player.godSpellDelay < Constants.GOD_SPELL_CHARGE) {
				player.getActionSender().sendMessage("You still feel the charge in your body!");
				break;
			}
			player.godSpellDelay = System.currentTimeMillis();
			player.getActionSender().sendMessage("You feel charged with a magical power!");
			player.gfx100(Enchantment.MAGIC_SPELLS[48][3]);
			player.startAnimation(Enchantment.MAGIC_SPELLS[48][2]);
			player.usingMagic = false;
			break;

		case 152:
			player.isRunning2 = !player.isRunning2;
			player.getActionSender().sendConfig(173, 0);
			break;
		case 153:
			player.isRunning2 = !player.isRunning2;
			player.getActionSender().sendConfig(173, 1);
			break;
		case 21010:
			player.takeAsNote = true;
			break;

		case 21011:
			player.takeAsNote = false;
			break;

		// home teleports
		case 4171:
		case 50056:
		case 50235:
		case 4140:
			new Location(player, Constants.START_LOCATION_X, Constants.START_LOCATION_Y, 0);
			break;
			
		case 4143:
		case 50245:
			// Training dialogue options
			player.getDH().sendDialogues(3, -1);
			break;

		case 50253:
		case 4146:
			// Minigame dialogue options
			player.getDH().sendDialogues(4, -1);
			break;

		case 51005:
		case 4150:
			player.getDH().sendDialogues(5, -1);
			break;

		case 51013:
		case 6004:
			player.getPA().startTeleport(Constants.ARDOUGNE_X, Constants.ARDOUGNE_Y, 0, "modern");
			player.teleAction = 5;
			break;

		case 51023:
		case 6005:
			player.getPA().startTeleport(Constants.WATCHTOWER_X, Constants.WATCHTOWER_Y, 0, "modern");
			player.teleAction = 6;
			break;

		case 51031:
		case 29031:
			player.getPA().startTeleport(Constants.TROLLHEIM_X, Constants.TROLLHEIM_Y, 0, "modern");
			player.teleAction = 7;
			break;

		case 9125: // Accurate
		case 6221: // range accurate
		case 22228: // punch (unarmed)
		case 48010: // flick (whip)
		case 21200: // spike (pickaxe)
		case 1080: // bash (staff)
		case 6168: // chop (axe)
		case 6236: // accurate (long bow)
		case 17102: // accurate (darts)
		case 8234: // stab (dagger)
			player.fightMode = 0;
			if (player.autocasting)
				player.getPA().resetAutocast();
			break;

		case 9126: // Defensive
		case 48008: // deflect (whip)
		case 22229: // block (unarmed)
		case 21201: // block (pickaxe)
		case 1078: // focus - block (staff)
		case 6169: // block (axe)
		case 33019: // fend (hally)
		case 18078: // block (spear)
		case 8235: // block (dagger)
			player.fightMode = 1;
			if (player.autocasting)
				player.getPA().resetAutocast();
			break;

		case 9127: // Controlled
		case 48009: // lash (whip)
		case 33018: // jab (hally)
		case 6234: // longrange (long bow)
		case 6219: // longrange
		case 18077: // lunge (spear)
		case 18080: // swipe (spear)
		case 18079: // pound (spear)
		case 17100: // longrange (darts)
			player.fightMode = 3;
			if (player.autocasting)
				player.getPA().resetAutocast();
			break;

		case 9128: // Aggressive
		case 6220: // range rapid
		case 22230: // kick (unarmed)
		case 21203: // impale (pickaxe)
		case 21202: // smash (pickaxe)
		case 1079: // pound (staff)
		case 6171: // hack (axe)
		case 6170: // smash (axe)
		case 33020: // swipe (hally)
		case 6235: // rapid (long bow)
		case 17101: // repid (darts)
		case 8237: // lunge (dagger)
		case 8236: // slash (dagger)
			player.fightMode = 2;
			if (player.autocasting)
				player.getPA().resetAutocast();
			break;

		case 13092:
			Player ot = (Player) Server.playerHandler.players[player.tradeWith];
			if (ot == null) {
				player.getTradeAndDuel().declineTrade();
				player.getActionSender().sendMessage("Trade declined as the other player has disconnected.");
				break;
			}
			player.getPA().sendFrame126("Waiting for other player...", 3431);
			ot.getPA().sendFrame126("Other player has accepted", 3431);
			player.goodTrade = true;
			ot.goodTrade = true;

			for (GameItem item : player.getTradeAndDuel().offeredItems) {
				if (item.id > 0) {
					if (ot.getItems().freeSlots() < player.getTradeAndDuel().offeredItems.size()) {
						player.getActionSender().sendMessage(ot.playerName + " only has " + ot.getItems().freeSlots()
								+ " free slots, please remove "
								+ (player.getTradeAndDuel().offeredItems.size() - ot.getItems().freeSlots()) + " items.");
						ot.getActionSender().sendMessage(player.playerName + " has to remove "
								+ (player.getTradeAndDuel().offeredItems.size() - ot.getItems().freeSlots())
								+ " items or you could offer them "
								+ (player.getTradeAndDuel().offeredItems.size() - ot.getItems().freeSlots()) + " items.");
						player.goodTrade = false;
						ot.goodTrade = false;
						player.getPA().sendFrame126("Not enough inventory space...", 3431);
						ot.getPA().sendFrame126("Not enough inventory space...", 3431);
						break;
					} else {
						player.getPA().sendFrame126("Waiting for other player...", 3431);
						ot.getPA().sendFrame126("Other player has accepted", 3431);
						player.goodTrade = true;
						ot.goodTrade = true;
					}
				}
			}
			if (player.inTrade && !player.tradeConfirmed && ot.goodTrade && player.goodTrade) {
				player.tradeConfirmed = true;
				if (ot.tradeConfirmed) {
					player.getTradeAndDuel().confirmScreen();
					ot.getTradeAndDuel().confirmScreen();
					break;
				}

			}

			break;
		case 17200:
			if (player.absX == 3563 && player.absY == 9694) {
				player.getActionSender().sendMessage("You hear the doors locking mechanism grind open.");
				player.getPA().object(6725, player.objectX, player.objectY, -1, 0);
				player.getPA().removeAllWindows();
				player.getPA().walkTo(-1, 0);

				CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						player.getPA().object(6725, player.objectX, player.objectY, -2, 0);
						container.stop();
					}

					@Override
					public void stop() {

					}
				}, 500);
			} else {
				player.getActionSender().sendMessage("You hear the doors locking mechanism grind open.");
				player.getPA().object(6725, player.objectX, player.objectY, -2, 0);
				player.getPA().removeAllWindows();
				player.getPA().walkTo(0, 1);

				CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						player.getPA().object(6725, player.objectX, player.objectY, -1, 0);
						container.stop();
					}

					@Override
					public void stop() {

					}
				}, 500);
			}
			break;
		case 17199:
			player.getPA().removeAllWindows();
			player.getActionSender().sendMessage("You got the riddle wrong, and it locks back up.");
			server.model.minigames.barrows.Barrows.wrongPuzzle = true;
			break;
		case 17198:
			player.getPA().removeAllWindows();
			player.getActionSender().sendMessage("You got the riddle wrong, and it locks back up.");
			server.model.minigames.barrows.Barrows.wrongPuzzle = true;
			break;
		case 13218:
			player.tradeAccepted = true;
			Player ot1 = (Player) Server.playerHandler.players[player.tradeWith];
			if (ot1 == null) {
				player.getTradeAndDuel().declineTrade();
				player.getActionSender().sendMessage("Trade declined as the other player has disconnected.");
				break;
			}

			if (player.inTrade && player.tradeConfirmed && ot1.tradeConfirmed && !player.tradeConfirmed2) {
				player.tradeConfirmed2 = true;
				if (ot1.tradeConfirmed2) {
					player.acceptedTrade = true;
					ot1.acceptedTrade = true;
					player.getTradeAndDuel().giveItems();
					ot1.getTradeAndDuel().giveItems();
					break;
				}
				ot1.getPA().sendFrame126("Other player has accepted.", 3535);
				player.getPA().sendFrame126("Waiting for other player...", 3535);
			}

			break;
		/* Rules Interface Buttons */
		case 125011: // Click agree
			if (!player.ruleAgreeButton) {
				player.ruleAgreeButton = true;
				player.getActionSender().sendConfig(701, 1);
			} else {
				player.ruleAgreeButton = false;
				player.getActionSender().sendConfig(701, 0);
			}
			break;
		case 125003:// Accept
			if (player.ruleAgreeButton) {
				player.getPA().showInterface(3559);
				player.newPlayer = false;
			} else if (!player.ruleAgreeButton) {
				player.getActionSender().sendMessage("You need to click on you agree before you can continue on.");
			}
			break;
		case 125006:// Decline
			player.getActionSender().sendMessage("You have chosen to decline, Player will be disconnected from the server.");
			break;
		/* End Rules Interface Buttons */
		/* Player Options */
		case 74176:
			if (!player.mouseButton) {
				player.mouseButton = true;
				player.getActionSender().sendConfig(500, 1);
				player.getActionSender().sendConfig(170, 1);
			} else if (player.mouseButton) {
				player.mouseButton = false;
				player.getActionSender().sendConfig(500, 0);
				player.getActionSender().sendConfig(170, 0);
			}
			break;
		case 74184:
			if (!player.splitChat) {
				player.splitChat = true;
				player.getActionSender().sendConfig(502, 1);
				player.getActionSender().sendConfig(287, 1);
			} else {
				player.splitChat = false;
				player.getActionSender().sendConfig(502, 0);
				player.getActionSender().sendConfig(287, 0);
			}
			break;
		case 74180:
			if (!player.chatEffects) {
				player.chatEffects = true;
				player.getActionSender().sendConfig(501, 1);
				player.getActionSender().sendConfig(171, 0);
			} else {
				player.chatEffects = false;
				player.getActionSender().sendConfig(501, 0);
				player.getActionSender().sendConfig(171, 1);
			}
			break;
		case 74188:
			if (!player.acceptAid) {
				player.acceptAid = true;
				player.getActionSender().sendConfig(503, 1);
				player.getActionSender().sendConfig(427, 1);
			} else {
				player.acceptAid = false;
				player.getActionSender().sendConfig(503, 0);
				player.getActionSender().sendConfig(427, 0);
			}
			break;
		case 74192:
			if (!player.isRunning2) {
				player.isRunning2 = true;
				player.getActionSender().sendConfig(504, 1);
				player.getActionSender().sendConfig(173, 1);
			} else {
				player.isRunning2 = false;
				player.getActionSender().sendConfig(504, 0);
				player.getActionSender().sendConfig(173, 0);
			}
			break;
		case 74201:// brightness1
			player.getActionSender().sendConfig(505, 1);
			player.getActionSender().sendConfig(506, 0);
			player.getActionSender().sendConfig(507, 0);
			player.getActionSender().sendConfig(508, 0);
			player.getActionSender().sendConfig(166, 1);
			break;
		case 74203:// brightness2
			player.getActionSender().sendConfig(505, 0);
			player.getActionSender().sendConfig(506, 1);
			player.getActionSender().sendConfig(507, 0);
			player.getActionSender().sendConfig(508, 0);
			player.getActionSender().sendConfig(166, 2);
			break;

		case 74204:// brightness3
			player.getActionSender().sendConfig(505, 0);
			player.getActionSender().sendConfig(506, 0);
			player.getActionSender().sendConfig(507, 1);
			player.getActionSender().sendConfig(508, 0);
			player.getActionSender().sendConfig(166, 3);
			break;

		case 74205:// brightness4
			player.getActionSender().sendConfig(505, 0);
			player.getActionSender().sendConfig(506, 0);
			player.getActionSender().sendConfig(507, 0);
			player.getActionSender().sendConfig(508, 1);
			player.getActionSender().sendConfig(166, 4);
			break;
		case 74206:// area1
			player.getActionSender().sendConfig(509, 1);
			player.getActionSender().sendConfig(510, 0);
			player.getActionSender().sendConfig(511, 0);
			player.getActionSender().sendConfig(512, 0);
			break;
		case 74207:// area2
			player.getActionSender().sendConfig(509, 0);
			player.getActionSender().sendConfig(510, 1);
			player.getActionSender().sendConfig(511, 0);
			player.getActionSender().sendConfig(512, 0);
			break;
		case 74208:// area3
			player.getActionSender().sendConfig(509, 0);
			player.getActionSender().sendConfig(510, 0);
			player.getActionSender().sendConfig(511, 1);
			player.getActionSender().sendConfig(512, 0);
			break;
		case 74209:// area4
			player.getActionSender().sendConfig(509, 0);
			player.getActionSender().sendConfig(510, 0);
			player.getActionSender().sendConfig(511, 0);
			player.getActionSender().sendConfig(512, 1);
			break;
		/* END OF EMOTES */

		case 24017:
			player.getPA().resetAutocast();
			// c.sendFrame246(329, 200, c.playerEquipment[c.playerWeapon]);
			player.getItems().sendWeapon(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()],
					player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]));
			// c.setSidebarInterface(0, 328);
			// c.setSidebarInterface(6, c.playerMagicBook == 0 ? 1151 :
			// c.playerMagicBook == 1 ? 12855 : 1151);
			break;
		default:
			Plugin.execute("clickButton_" + actionButtonId, player);
			break;
		}
		if (player.isAutoButton(actionButtonId))
			player.assignAutocast(actionButtonId);
	}

}
