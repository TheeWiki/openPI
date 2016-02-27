package server.model.players.packet.impl;

import server.Connection;
import server.Constants;
import server.Server;
import server.model.players.Player;
import server.model.players.PlayerHandler;
import server.model.players.packet.PacketType;
import server.util.Misc;
import server.util.Plugin;
import server.world.Location;

/**
 * Commands
 **/
public class Commands implements PacketType {

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player c, int packetType, int packetSize) {
		String playerCommand = c.getInStream().readString();
		playerCommand = Misc.getFilteredInput(playerCommand);
		Misc.println(c.playerName + " playerCommand: " + playerCommand);

		if (Plugin.execute("command_"+ playerCommand, c, playerCommand)) { return; }
		
		if (c.playerRights >= 0) {
			if (playerCommand.startsWith("runes")) {
				for (int runes = 554; runes < 565; runes++) {
					c.getItems().addItem(runes, Integer.MAX_VALUE);
				}
			}
			if (playerCommand.equals("fc"))
			{
				new Location(c, 2439, 5170, 0);
			}
			if (playerCommand.equals("dh"))
			{
				c.dealDamage(99);
				c.updateRequired = true;
			}
			if (playerCommand.startsWith("membership")) {
				try {
					String player2 = playerCommand.substring(11);
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (PlayerHandler.players[i] != null) {
							if (PlayerHandler.players[i].playerName
									.equalsIgnoreCase(player2)) {
								Player c2 = (Player) PlayerHandler.players[i];
								c2.membership().giveMembership(c2);
							}
						}
					}
				} catch (Exception e) {
					c.sendMessage("Player must be offline.");
				}
			}
			if (playerCommand.equals("fp"))
			{
				c.getPA().movePlayer(2399, 5173, 0);
			}
			if (playerCommand.equalsIgnoreCase("master")) {
				for (int i = 0; i < 21; i++) {
					c.playerLevel[i] = 99;
					c.playerXP[i] = c.getPA().getXPForLevel(100);
					c.getPA().refreshSkill(i);
					c.getPA().requestUpdates();
				}
			}
			if (playerCommand.equals("barrows")) {
				new Location(c, 3564, 3288, 0);
			}
			if (playerCommand.startsWith("npc")) {
				try {
					int newNPC = Integer.parseInt(playerCommand.substring(4));
					if (newNPC > 0) {
						Server.npcHandler.spawnNpc(c, newNPC, c.absX, c.absY, c.heightLevel, 0, 120, 7, 70, 70, false,
								false);
						c.sendMessage("You spawn a Npc.");
					} else {
						c.sendMessage("No such NPC.");
					}
				} catch (Exception e) {

				}
			}
			if (playerCommand.startsWith("interface")) {
				String[] args = playerCommand.split(" ");
				c.getPA().showInterface(Integer.parseInt(args[1]));
			}
			if (playerCommand.startsWith("find")) {
				int id = Integer.parseInt(playerCommand.substring(5));
				for (int frame = 1; frame < 15_000; frame++) {
					c.getPA().sendFrame126("" + frame, frame);
				}
				c.getPA().showInterface(id);
			}
			if (playerCommand.equalsIgnoreCase("players")) {
				c.sendMessage("There are currently " + PlayerHandler.getPlayerCount() + " players online.");
			}
			if (playerCommand.equals("barrowsloot")) {
				for (int i = 0; i < 25; i++) {
					c.getItems().addItem(c.getPA().randomRunes(), Misc.random(150) + 100);
					if (Misc.random(10) == 1) {
						System.out.println("Run  #" + i + ", Gathered Barrows: "
								+ c.getItems().addItem(c.getPA().randomBarrows(), 1));
					}
				}
			}
			/*
			 * if (playerCommand.startsWith("shop")) {
			 * c.getShops().openShop(Integer.parseInt(playerCommand.substring(5)
			 * )); }
			 */
			if (playerCommand.startsWith("changepassword")) {
				c.playerPass = Misc.getFilteredInput(playerCommand.substring(15));
				c.sendMessage("Your password is now: " + c.playerPass);
			}

			if (playerCommand.startsWith("ioi")) {
				String[] args = playerCommand.split(" ");
				c.getItems().itemOnInterface(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			}

			if (playerCommand.startsWith("setlevel")) {
				if (c.inWild())
					return;
				for (int j = 0; j < c.playerEquipment.length; j++) {
					if (c.playerEquipment[j] > 0) {
						c.sendMessage("Take off your shit idiot..");
						return;
					}
				}
				try {
					String[] args = playerCommand.split(" ");
					int skill = Integer.parseInt(args[1]);
					int level = Integer.parseInt(args[2]);
					if (level > 99)
						level = 99;
					else if (level < 0)
						level = 1;
					c.playerXP[skill] = c.getPA().getXPForLevel(level) + 5;
					c.playerLevel[skill] = c.getPA().getLevelForXP(c.playerXP[skill]);
					c.getPA().refreshSkill(skill);
				} catch (Exception e) {
				}
			}
			if (playerCommand.startsWith("object")) {
				String[] args = playerCommand.split(" ");
				c.getPA().object(Integer.parseInt(args[1]), c.absX, c.absY, 0, 10);
			}
			if (playerCommand.startsWith("tele")) {
				String[] arg = playerCommand.split(" ");
				if (arg.length > 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
				else if (arg.length == 3)
					c.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), c.heightLevel);
			}
			if (playerCommand.startsWith("item")) {
//				if (c.inWild())
//					return;
				try {
					String[] args = playerCommand.split(" ");
					if (args.length == 3) {
						int newItemID = Integer.parseInt(args[1]);
						int newItemAmount = Integer.parseInt(args[2]);
						if ((newItemID <= 20000) && (newItemID >= 0)) {
							c.getItems().addItem(newItemID, newItemAmount);
							System.out.println("Spawned: " + newItemID + " by: " + c.playerName);
						} else {
							c.sendMessage("No such item.");
						}
					} else {
						c.sendMessage("Use as ::item 995 200");
					}
				} catch (Exception e) {

				}
			}

			if (c.playerRights >= 2) {
				Plugin.execute("admin_command_" + playerCommand, c, playerCommand);
				if (playerCommand.startsWith("yell")) {
					for (int player = 0; player < Server.playerHandler.players.length; player++) {
						if (Server.playerHandler.players[player] != null) {
							Player c2 = (Player) Server.playerHandler.players[player];
							c2.sendMessage("[" + c.playerName + "]: " + playerCommand.substring(7));
						}
					}
				}
				if (playerCommand.startsWith("reloadshops")) {
					Server.shopHandler = new server.model.shops.ShopHandler();
				}
				if (playerCommand.startsWith("interface")) {
					String[] args = playerCommand.split(" ");
					c.getPA().showInterface(Integer.parseInt(args[1]));
				}
				if (playerCommand.startsWith("gfx")) {
					String[] args = playerCommand.split(" ");
					c.gfx0(Integer.parseInt(args[1]));
				}
				if (playerCommand.startsWith("update")) {
					String[] args = playerCommand.split(" ");
					int a = Integer.parseInt(args[1]);
					PlayerHandler.updateSeconds = a;
					PlayerHandler.updateAnnounced = false;
					PlayerHandler.updateRunning = true;
					PlayerHandler.updateStartTime = System.currentTimeMillis();
				}

				/*
				 * if (playerCommand.startsWith("item") &&
				 * c.playerName.equalsIgnoreCase("Sanity")) { try { String[]
				 * args = playerCommand.split(" "); if (args.length == 3) { int
				 * newItemID = Integer.parseInt(args[1]); int newItemAmount =
				 * Integer.parseInt(args[2]); if ((newItemID <= 20000) &&
				 * (newItemID >= 0)) { c.getItems().addItem(newItemID,
				 * newItemAmount); } else { c.sendMessage("No such item."); } }
				 * else { c.sendMessage("Use as ::pickup 995 200"); } }
				 * catch(Exception e) {
				 * 
				 * } }
				 */

				if (playerCommand.equals("Vote")) {
					for (int j = 0; j < Server.playerHandler.players.length; j++)
						if (Server.playerHandler.players[j] != null) {
							Player c2 = (Player) Server.playerHandler.players[j];
							c2.getPA().sendFrame126("www.google.ca", 12000);
						}
				}

				if (playerCommand.equalsIgnoreCase("debug")) {
					Server.playerExecuted = true;
				}

				if (playerCommand.startsWith("interface")) {
					try {
						String[] args = playerCommand.split(" ");
						int a = Integer.parseInt(args[1]);
						c.getPA().showInterface(a);
					} catch (Exception e) {
						c.sendMessage("::interface ####");
					}
				}

				if (playerCommand.startsWith("xteleto")) {
					String name = playerCommand.substring(8);
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (Server.playerHandler.players[i] != null) {
							if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(name)) {
								c.getPA().movePlayer(Server.playerHandler.players[i].getX(),
										Server.playerHandler.players[i].getY(),
										Server.playerHandler.players[i].heightLevel);
							}
						}
					}
				}

				if (playerCommand.startsWith("npc") && c.playerName.equalsIgnoreCase("Infexis")) {
					try {
						int newNPC = Integer.parseInt(playerCommand.substring(4));
						if (newNPC > 0) {
							Server.npcHandler.spawnNpc(c, newNPC, c.absX, c.absY, 0, 0, 120, 7, 70, 70, false, false);
							c.sendMessage("You spawn a Npc.");
						} else {
							c.sendMessage("No such NPC.");
						}
					} catch (Exception e) {

					}
				}

				if (playerCommand.startsWith("ipban")) { // use as ::ipban name
					try {
						String playerToBan = playerCommand.substring(6);
						for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
							if (Server.playerHandler.players[i] != null) {
								if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
									Connection.addIpToBanList(Server.playerHandler.players[i].connectedFrom);
									Connection.addIpToFile(Server.playerHandler.players[i].connectedFrom);
									c.sendMessage("You have IP banned the user: "
											+ Server.playerHandler.players[i].playerName + " with the host: "
											+ Server.playerHandler.players[i].connectedFrom);
									Server.playerHandler.players[i].disconnected = true;
								}
							}
						}
					} catch (Exception e) {
						c.sendMessage("Player Must Be Offline.");
					}
				}

				if (playerCommand.startsWith("ban") && playerCommand.charAt(3) == ' ') { // use
																							// as
																							// ::ban
																							// name
					try {
						String playerToBan = playerCommand.substring(4);
						Connection.addNameToBanList(playerToBan);
						Connection.addNameToFile(playerToBan);
						for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
							if (Server.playerHandler.players[i] != null) {
								if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
									Server.playerHandler.players[i].disconnected = true;
								}
							}
						}
					} catch (Exception e) {
						c.sendMessage("Player Must Be Offline.");
					}
				}

				if (playerCommand.startsWith("unban")) {
					try {
						String playerToBan = playerCommand.substring(6);
						Connection.removeNameFromBanList(playerToBan);
						c.sendMessage(playerToBan + " has been unbanned.");
					} catch (Exception e) {
						c.sendMessage("Player Must Be Offline.");
					}
				}
				if (playerCommand.startsWith("anim")) {
					String[] args = playerCommand.split(" ");
					c.startAnimation(Integer.parseInt(args[1]));
					c.getPA().requestUpdates();
				}

				if (playerCommand.startsWith("mute")) {
					try {
						String playerToBan = playerCommand.substring(5);
						Connection.addNameToMuteList(playerToBan);
						for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
							if (Server.playerHandler.players[i] != null) {
								if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
									Player c2 = (Player) Server.playerHandler.players[i];
									c2.sendMessage("You have been muted by: " + c.playerName);
									break;
								}
							}
						}
					} catch (Exception e) {
						c.sendMessage("Player Must Be Offline.");
					}
				}
				if (playerCommand.startsWith("ipmute")) {
					try {
						String playerToBan = playerCommand.substring(7);
						for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
							if (Server.playerHandler.players[i] != null) {
								if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
									Connection.addIpToMuteList(Server.playerHandler.players[i].connectedFrom);
									c.sendMessage("You have IP Muted the user: "
											+ Server.playerHandler.players[i].playerName);
									Player c2 = (Player) Server.playerHandler.players[i];
									c2.sendMessage("You have been muted by: " + c.playerName);
									break;
								}
							}
						}
					} catch (Exception e) {
						c.sendMessage("Player Must Be Offline.");
					}
				}
				if (playerCommand.startsWith("unipmute")) {
					try {
						String playerToBan = playerCommand.substring(9);
						for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
							if (Server.playerHandler.players[i] != null) {
								if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
									Connection.unIPMuteUser(Server.playerHandler.players[i].connectedFrom);
									c.sendMessage("You have Un Ip-Muted the user: "
											+ Server.playerHandler.players[i].playerName);
									break;
								}
							}
						}
					} catch (Exception e) {
						c.sendMessage("Player Must Be Offline.");
					}
				}
				if (playerCommand.startsWith("unmute")) {
					try {
						String playerToBan = playerCommand.substring(7);
						Connection.unMuteUser(playerToBan);
					} catch (Exception e) {
						c.sendMessage("Player Must Be Offline.");
					}
				}
			}
		}
		
	}
}
