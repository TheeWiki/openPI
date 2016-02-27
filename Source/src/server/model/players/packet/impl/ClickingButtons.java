package server.model.players.packet.impl;

import server.Constants;
import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.items.GameItem;
import server.model.minigames.duel_arena.Rules;
import server.model.players.Client;
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
	public void processPacket(final Client c, int packetType, int packetSize) {
		int actionButtonId = Misc.hexToInt(c.getInStream().buffer, 0, packetSize);
		// int actionButtonId = c.getInStream().readShort();
		if (c.isDead)
			return;
		if (c.playerRights >= 2)
			Misc.println(c.playerName + " - actionbutton: " + actionButtonId);

		Plugin.execute("buttons", c, actionButtonId);

		switch (actionButtonId) {
		case 485:
			c.getRG().exchangePoints();
			break;

		case 150:
				c.autoRet = 1;
				break;
		case 151:
				c.autoRet = 0;
			break;
			
		case 9190: // 1st option (5)
		case 9191: // 2nd option (5)
		case 9192: // 3rd option (5)
		case 9193: // 4th option (5)
		case 9194: // 5th option (5)
			if (c.dialogueContainer != null) {
				c.dialogueContainer.execute((actionButtonId - 9190) + 1);
				return;
			}
			break;

		case 58253:
			// c.getPA().showInterface(15106);
			c.getItems().writeBonus();
			break;
			
		case 9178:
			if (c.dialogueContainer != null) {
				c.dialogueContainer.execute((actionButtonId - 9178) + 1);
				return;
			}
			switch(c.dialogueAction)
			{
			case 485:
				c.getRG().buyArrows();
				break;
			case 2:
				c.getPA().startTeleport(3428, 3538, 0, "modern");	
				break;
			}
			break;

		case 9179:
			if (c.dialogueContainer != null) {
				c.dialogueContainer.execute((actionButtonId - 9178) + 1);
				return;
			}
			switch(c.dialogueAction) {
			case 485:
				c.getRG().exchangePoints();
				break;
			case 2:
				c.getPA().startTeleport(2884, 3395, 0, "modern");	
				break;
			}
			break;

		case 9180:
			if (c.dialogueContainer != null) {
				c.dialogueContainer.execute((actionButtonId - 9178) + 1);
				return;
			}
			switch(c.dialogueAction) {
			case 485:
				c.getRG().howAmIDoing();
				break;
				
			}
			break;

		case 9181:
			if (c.dialogueContainer != null) {
				c.dialogueContainer.execute((actionButtonId - 9178) + 1);
				return;
			}
			switch(c.dialogueAction) {
			case 2:
				c.getPA().startTeleport(2669, 3714, 0, "modern");
				break;
			}
			break;

		case 1093:
		case 1094:
		case 1097:
			if (c.autocastId > 0) {
				c.getPA().resetAutocast();
			} else {
	
				if (c.playerMagicBook == 1) {
					if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4675)
						c.setSidebarInterface(0, 1689);
					else
						c.sendMessage("You can't autocast ancients without an ancient staff.");
				} else if (c.playerMagicBook == 0) {
					if (c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4170) {
						c.setSidebarInterface(0, 12050);
					} else {
						c.setSidebarInterface(0, 1829);
					}
				}

			}
			break;

		case 9157:
//			if (c.dialogueAction == 1) {
//				c.getDH().sendDialogues(38, -1);
//			}
			if (c.dialogueContainer != null) {
				c.dialogueContainer.execute((actionButtonId - 9157) + 1);
				return;
			}
			switch(c.dialogueAction)
			{
			case 1:
				int r = 4;
				// int r = Misc.random(3);
				switch (r) {
				case 0:
					c.getPA().movePlayer(3534, 9677, 0);
					break;

				case 1:
					c.getPA().movePlayer(3534, 9712, 0);
					break;

				case 2:
					c.getPA().movePlayer(3568, 9712, 0);
					break;

				case 3:
					c.getPA().movePlayer(3568, 9677, 0);
					break;
				case 4:
					c.getPA().movePlayer(3551, 9694, 0);
					break;
				}
			}
		break;
		case 9158:
			if (c.dialogueContainer != null) {
				c.dialogueContainer.execute((actionButtonId - 9157) + 1);
				return;
			}
			break;
		case 9058:
			if (c.dialogueContainer != null) {
				c.dialogueContainer.execute((actionButtonId - 9157) + 1);
				return;
			}
			break;
		case 9167: // 1st option (3)
		case 9168: // 2nd option (3)
		case 9169: // 3rd option (3)
			if (c.dialogueContainer != null) {
				c.dialogueContainer.execute((actionButtonId - 9167) + 1);
				return;
			}
			break;

		/** Specials **/
		case 29188:
			c.specBarId = 7636; // the special attack text - sendframe126(S P E
								// C I A L A T T A C K, c.specBarId);
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29163:
			c.specBarId = 7611;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 33033:
			c.specBarId = 8505;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29038:
			c.specBarId = 7486;
			/*
			 * if (c.specAmount >= 5) { c.attackTimer = 0;
			 * c.getCombat().attackPlayer(c.playerIndex); c.usingSpecial = true;
			 * c.specAmount -= 5; }
			 */
			c.getCombat().handleGmaulPlayer();
			c.getItems().updateSpecialBar();
			break;

		case 29063:
			if (c.getCombat().checkSpecAmount(c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()])) {
				c.gfx0(246);
				c.forcedChat("Raarrrrrgggggghhhhhhh!");
				c.startAnimation(1056);
				c.playerLevel[2] = c.getLevelForXP(c.playerXP[2]) + (c.getLevelForXP(c.playerXP[2]) * 15 / 100);
				c.getPA().refreshSkill(2);
				c.getItems().updateSpecialBar();
			} else {
				c.sendMessage("You don't have the required special energy to use this attack.");
			}
			break;

		case 48023:
			c.specBarId = 12335;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29138:
			c.specBarId = 7586;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29113:
			c.specBarId = 7561;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		case 29238:
			c.specBarId = 7686;
			c.usingSpecial = !c.usingSpecial;
			c.getItems().updateSpecialBar();
			break;

		/** Dueling **/
		case 26065: // no forfeit
		case 26040:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(0);
			break;

		case 26066: // no movement
		case 26048:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(1);
			break;

		case 26069: // no range
		case 26042:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(2);
			break;

		case 26070: // no melee
		case 26043:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(3);
			break;

		case 26071: // no mage
		case 26041:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(4);
			break;

		case 26072: // no drinks
		case 26045:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(5);
			break;

		case 26073: // no food
		case 26046:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(6);
			break;

		case 26074: // no prayer
		case 26047:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(7);
			break;

		case 26076: // obsticals
		case 26075:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(8);
			break;

		case 2158: // fun weapons
		case 2157:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(9);
			break;

		case 30136: // sp attack
		case 30137:
			c.duelSlot = -1;
			c.getTradeAndDuel().selectRule(10);
			break;

		case 53245: // no helm
			c.duelSlot = 0;
			c.getTradeAndDuel().selectRule(11);
			break;

		case 53246: // no cape
			c.duelSlot = 1;
			c.getTradeAndDuel().selectRule(12);
			break;

		case 53247: // no ammy
			c.duelSlot = 2;
			c.getTradeAndDuel().selectRule(13);
			break;

		case 53249: // no weapon.
			c.duelSlot = 3;
			c.getTradeAndDuel().selectRule(14);
			break;

		case 53250: // no body
			c.duelSlot = 4;
			c.getTradeAndDuel().selectRule(15);
			break;

		case 53251: // no shield
			c.duelSlot = 5;
			c.getTradeAndDuel().selectRule(16);
			break;

		case 53252: // no legs
			c.duelSlot = 7;
			c.getTradeAndDuel().selectRule(17);
			break;

		case 53255: // no gloves
			c.duelSlot = 9;
			c.getTradeAndDuel().selectRule(18);
			break;

		case 53254: // no boots
			c.duelSlot = 10;
			c.getTradeAndDuel().selectRule(19);
			break;

		case 53253: // no rings
			c.duelSlot = 12;
			c.getTradeAndDuel().selectRule(20);
			break;

		case 53248: // no arrows
			c.duelSlot = 13;
			c.getTradeAndDuel().selectRule(21);
			break;

		/*
		 * Accepting Duel Interface Fixed by: Ardi Remember to click thanks
		 * button & karma (reputation) for Ardi, if you're using this.
		 */
		case 26018:
			if (c.duelStatus == 5) {
				// c.sendMessage("This glitch has been fixed by Ardi, sorry
				// sir.");
				return;
			}
			if (c.inDuelArena()) {
				Client o = (Client) Server.playerHandler.players[c.duelingWith];
				if (o == null) {
					c.getTradeAndDuel().declineDuel();
					o.getTradeAndDuel().declineDuel();
					return;
				}

				if (c.duelRule[Rules.RANGE_RULE.getRule()] && c.duelRule[Rules.MELEE_RULE.getRule()] && c.duelRule[Rules.MAGIC_RULE.getRule()]) {
					c.sendMessage("You won't be able to attack the player with the rules you have set.");
					break;
				}
				c.duelStatus = 2;
				if (c.duelStatus == 2) {
					c.getPA().sendFrame126("Waiting for other player...", 6684);
					o.getPA().sendFrame126("Other player has accepted.", 6684);
				}
				if (o.duelStatus == 2) {
					o.getPA().sendFrame126("Waiting for other player...", 6684);
					c.getPA().sendFrame126("Other player has accepted.", 6684);
				}

				if (c.duelStatus == 2 && o.duelStatus == 2) {
					c.canOffer = false;
					o.canOffer = false;
					c.duelStatus = 3;
					o.duelStatus = 3;
					c.getTradeAndDuel().confirmDuel();
					o.getTradeAndDuel().confirmDuel();
				}
			} else {
				Client o = (Client) Server.playerHandler.players[c.duelingWith];
				c.getTradeAndDuel().declineDuel();
				o.getTradeAndDuel().declineDuel();
				c.sendMessage("You can't stake out of Duel Arena.");
			}
			break;

		/*
		 * Accepting Duel Interface Fixed by: Ardi Remember to click thanks
		 * button & karma (reputation) for Ardi, if you're using this.
		 */
		case 25120:
			if (c.duelStatus == 5) {
				// c.sendMessage("This glitch has been fixed by Ardi, sorry
				// sir.");
				return;
			}
			if (c.inDuelArena()) {
				if (c.duelStatus == 5) {
					break;
				}
				Client o1 = (Client) Server.playerHandler.players[c.duelingWith];
				if (o1 == null) {
					c.getTradeAndDuel().declineDuel();
					return;
				}

				c.duelStatus = 4;
				if (o1.duelStatus == 4 && c.duelStatus == 4) {
					c.getTradeAndDuel().startDuel();
					o1.getTradeAndDuel().startDuel();
					o1.duelCount = 4;
					c.duelCount = 4;
					c.duelDelay = System.currentTimeMillis();
					o1.duelDelay = System.currentTimeMillis();
				} else {
					c.getPA().sendFrame126("Waiting for other player...", 6571);
					o1.getPA().sendFrame126("Other player has accepted", 6571);
				}
			} else {
				Client o = (Client) Server.playerHandler.players[c.duelingWith];
				c.getTradeAndDuel().declineDuel();
				o.getTradeAndDuel().declineDuel();
				c.sendMessage("You can't stake out of Duel Arena.");
			}
			break;

		case 4169: // god spell charge
			c.usingMagic = true;
			if (!c.getCombat().checkMagicReqs(48)) {
				break;
			}

			if (System.currentTimeMillis() - c.godSpellDelay < Constants.GOD_SPELL_CHARGE) {
				c.sendMessage("You still feel the charge in your body!");
				break;
			}
			c.godSpellDelay = System.currentTimeMillis();
			c.sendMessage("You feel charged with a magical power!");
			c.gfx100(Enchantment.MAGIC_SPELLS[48][3]);
			c.startAnimation(Enchantment.MAGIC_SPELLS[48][2]);
			c.usingMagic = false;
			break;

		case 152:
			c.isRunning2 = !c.isRunning2;
			c.getPA().sendFrame36(173, 0);
			break;
		case 153:
			c.isRunning2 = !c.isRunning2;
			c.getPA().sendFrame36(173, 1);
			break;
		case 21010:
			c.takeAsNote = true;
			break;

		case 21011:
			c.takeAsNote = false;
			break;

		// home teleports
		case 4171:
		case 50056:
			new Location(c, Constants.START_LOCATION_X, Constants.START_LOCATION_Y, 0);
			break;

		case 50235:
		case 4140:
			c.getPA().startTeleport(Constants.RESPAWN_X, Constants.RESPAWN_Y, 0, "modern");
			c.teleAction = 1;
			break;

		case 4143:
		case 50245:
			// Training dialogue options
			c.getDH().sendDialogues(3, -1);
			break;

		case 50253:
		case 4146:
			// Minigame dialogue options
			c.getDH().sendDialogues(4, -1);
			break;

		case 51005:
		case 4150:
			c.getDH().sendDialogues(5, -1);
			break;

		case 51013:
		case 6004:
			c.getPA().startTeleport(Constants.ARDOUGNE_X, Constants.ARDOUGNE_Y, 0, "modern");
			c.teleAction = 5;
			break;

		case 51023:
		case 6005:
			c.getPA().startTeleport(Constants.WATCHTOWER_X, Constants.WATCHTOWER_Y, 0, "modern");
			c.teleAction = 6;
			break;

		case 51031:
		case 29031:
			c.getPA().startTeleport(Constants.TROLLHEIM_X, Constants.TROLLHEIM_Y, 0, "modern");
			c.teleAction = 7;
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
			c.fightMode = 0;
			if (c.autocasting)
				c.getPA().resetAutocast();
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
			c.fightMode = 1;
			if (c.autocasting)
				c.getPA().resetAutocast();
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
			c.fightMode = 3;
			if (c.autocasting)
				c.getPA().resetAutocast();
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
			c.fightMode = 2;
			if (c.autocasting)
				c.getPA().resetAutocast();
			break;

		case 13092:
			Client ot = (Client) Server.playerHandler.players[c.tradeWith];
			if (ot == null) {
				c.getTradeAndDuel().declineTrade();
				c.sendMessage("Trade declined as the other player has disconnected.");
				break;
			}
			c.getPA().sendFrame126("Waiting for other player...", 3431);
			ot.getPA().sendFrame126("Other player has accepted", 3431);
			c.goodTrade = true;
			ot.goodTrade = true;

			for (GameItem item : c.getTradeAndDuel().offeredItems) {
				if (item.id > 0) {
					if (ot.getItems().freeSlots() < c.getTradeAndDuel().offeredItems.size()) {
						c.sendMessage(ot.playerName + " only has " + ot.getItems().freeSlots()
								+ " free slots, please remove "
								+ (c.getTradeAndDuel().offeredItems.size() - ot.getItems().freeSlots()) + " items.");
						ot.sendMessage(c.playerName + " has to remove "
								+ (c.getTradeAndDuel().offeredItems.size() - ot.getItems().freeSlots())
								+ " items or you could offer them "
								+ (c.getTradeAndDuel().offeredItems.size() - ot.getItems().freeSlots()) + " items.");
						c.goodTrade = false;
						ot.goodTrade = false;
						c.getPA().sendFrame126("Not enough inventory space...", 3431);
						ot.getPA().sendFrame126("Not enough inventory space...", 3431);
						break;
					} else {
						c.getPA().sendFrame126("Waiting for other player...", 3431);
						ot.getPA().sendFrame126("Other player has accepted", 3431);
						c.goodTrade = true;
						ot.goodTrade = true;
					}
				}
			}
			if (c.inTrade && !c.tradeConfirmed && ot.goodTrade && c.goodTrade) {
				c.tradeConfirmed = true;
				if (ot.tradeConfirmed) {
					c.getTradeAndDuel().confirmScreen();
					ot.getTradeAndDuel().confirmScreen();
					break;
				}

			}

			break;
		case 17200:
			if (c.absX == 3563 && c.absY == 9694) {
				c.sendMessage("You hear the doors locking mechanism grind open.");
				c.getPA().object(6725, c.objectX, c.objectY, -1, 0);
				c.getPA().removeAllWindows();
				c.getPA().walkTo(-1, 0);

				CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						c.getPA().object(6725, c.objectX, c.objectY, -2, 0);
						container.stop();
					}

					@Override
					public void stop() {

					}
				}, 500);
			} else {
				c.sendMessage("You hear the doors locking mechanism grind open.");
				c.getPA().object(6725, c.objectX, c.objectY, -2, 0);
				c.getPA().removeAllWindows();
				c.getPA().walkTo(0, 1);

				CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						c.getPA().object(6725, c.objectX, c.objectY, -1, 0);
						container.stop();
					}

					@Override
					public void stop() {

					}
				}, 500);
			}
			break;
		case 17199:
			c.getPA().removeAllWindows();
			c.sendMessage("You got the riddle wrong, and it locks back up.");
			server.model.minigames.barrows.Barrows.wrongPuzzle = true;
			break;
		case 17198:
			c.getPA().removeAllWindows();
			c.sendMessage("You got the riddle wrong, and it locks back up.");
			server.model.minigames.barrows.Barrows.wrongPuzzle = true;
			break;
		case 13218:
			c.tradeAccepted = true;
			Client ot1 = (Client) Server.playerHandler.players[c.tradeWith];
			if (ot1 == null) {
				c.getTradeAndDuel().declineTrade();
				c.sendMessage("Trade declined as the other player has disconnected.");
				break;
			}

			if (c.inTrade && c.tradeConfirmed && ot1.tradeConfirmed && !c.tradeConfirmed2) {
				c.tradeConfirmed2 = true;
				if (ot1.tradeConfirmed2) {
					c.acceptedTrade = true;
					ot1.acceptedTrade = true;
					c.getTradeAndDuel().giveItems();
					ot1.getTradeAndDuel().giveItems();
					break;
				}
				ot1.getPA().sendFrame126("Other player has accepted.", 3535);
				c.getPA().sendFrame126("Waiting for other player...", 3535);
			}

			break;
		/* Rules Interface Buttons */
		case 125011: // Click agree
			if (!c.ruleAgreeButton) {
				c.ruleAgreeButton = true;
				c.getPA().sendFrame36(701, 1);
			} else {
				c.ruleAgreeButton = false;
				c.getPA().sendFrame36(701, 0);
			}
			break;
		case 125003:// Accept
			if (c.ruleAgreeButton) {
				c.getPA().showInterface(3559);
				c.newPlayer = false;
			} else if (!c.ruleAgreeButton) {
				c.sendMessage("You need to click on you agree before you can continue on.");
			}
			break;
		case 125006:// Decline
			c.sendMessage("You have chosen to decline, Client will be disconnected from the server.");
			break;
		/* End Rules Interface Buttons */
		/* Player Options */
		case 74176:
			if (!c.mouseButton) {
				c.mouseButton = true;
				c.getPA().sendFrame36(500, 1);
				c.getPA().sendFrame36(170, 1);
			} else if (c.mouseButton) {
				c.mouseButton = false;
				c.getPA().sendFrame36(500, 0);
				c.getPA().sendFrame36(170, 0);
			}
			break;
		case 74184:
			if (!c.splitChat) {
				c.splitChat = true;
				c.getPA().sendFrame36(502, 1);
				c.getPA().sendFrame36(287, 1);
			} else {
				c.splitChat = false;
				c.getPA().sendFrame36(502, 0);
				c.getPA().sendFrame36(287, 0);
			}
			break;
		case 74180:
			if (!c.chatEffects) {
				c.chatEffects = true;
				c.getPA().sendFrame36(501, 1);
				c.getPA().sendFrame36(171, 0);
			} else {
				c.chatEffects = false;
				c.getPA().sendFrame36(501, 0);
				c.getPA().sendFrame36(171, 1);
			}
			break;
		case 74188:
			if (!c.acceptAid) {
				c.acceptAid = true;
				c.getPA().sendFrame36(503, 1);
				c.getPA().sendFrame36(427, 1);
			} else {
				c.acceptAid = false;
				c.getPA().sendFrame36(503, 0);
				c.getPA().sendFrame36(427, 0);
			}
			break;
		case 74192:
			if (!c.isRunning2) {
				c.isRunning2 = true;
				c.getPA().sendFrame36(504, 1);
				c.getPA().sendFrame36(173, 1);
			} else {
				c.isRunning2 = false;
				c.getPA().sendFrame36(504, 0);
				c.getPA().sendFrame36(173, 0);
			}
			break;
		case 74201:// brightness1
			c.getPA().sendFrame36(505, 1);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 1);
			break;
		case 74203:// brightness2
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 1);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 2);
			break;

		case 74204:// brightness3
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 1);
			c.getPA().sendFrame36(508, 0);
			c.getPA().sendFrame36(166, 3);
			break;

		case 74205:// brightness4
			c.getPA().sendFrame36(505, 0);
			c.getPA().sendFrame36(506, 0);
			c.getPA().sendFrame36(507, 0);
			c.getPA().sendFrame36(508, 1);
			c.getPA().sendFrame36(166, 4);
			break;
		case 74206:// area1
			c.getPA().sendFrame36(509, 1);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74207:// area2
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 1);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74208:// area3
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 1);
			c.getPA().sendFrame36(512, 0);
			break;
		case 74209:// area4
			c.getPA().sendFrame36(509, 0);
			c.getPA().sendFrame36(510, 0);
			c.getPA().sendFrame36(511, 0);
			c.getPA().sendFrame36(512, 1);
			break;
		/* END OF EMOTES */

		case 24017:
			c.getPA().resetAutocast();
			// c.sendFrame246(329, 200, c.playerEquipment[c.playerWeapon]);
			c.getItems().sendWeapon(c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()],
					c.getItems().getItemName(c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]));
			// c.setSidebarInterface(0, 328);
			// c.setSidebarInterface(6, c.playerMagicBook == 0 ? 1151 :
			// c.playerMagicBook == 1 ? 12855 : 1151);
			break;
		default:
			Plugin.execute("clickButton_" + actionButtonId, c);
			break;
		}
		if (c.isAutoButton(actionButtonId))
			c.assignAutocast(actionButtonId);
	}

}
