package server.model.players.packet.impl;

import server.Constants;
import server.Server;
import server.model.players.Player;
import server.model.players.PlayerHandler;
import server.model.players.packet.PacketType;
import server.net.Connection;
import server.util.Misc;
import server.util.Plugin;
import server.world.Location;

/**
 * Commands
 **/
public class Commands implements PacketType {

	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		String playerCommand = player.getInStream().readString();
		playerCommand = Misc.getFilteredInput(playerCommand);
		Misc.println(player.playerName + " playerCommand: " + playerCommand);

		if (Plugin.execute("command_"+ playerCommand, player, playerCommand)) { return; }
		
		if (player.playerRights >= 0) {
			if (playerCommand.startsWith("runes")) {
				for (int runes = 554; runes < 565; runes++) {
					player.getItems().addItem(runes, Integer.MAX_VALUE);
				}
			}
			if (playerCommand.equals("fc"))
			{
				new Location(player, 2439, 5170, 0);
			}
			if (playerCommand.equals("dh"))
			{
				player.dealDamage(97);
				player.updateRequired = true;
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
					player.getActionSender().sendMessage("Player must be offline.");
				}
			}
			if (playerCommand.equals("fp"))
			{
				player.getPA().movePlayer(2399, 5173, 0);
			}
			if (playerCommand.equalsIgnoreCase("master")) {
				for (int i = 0; i < 21; i++) {
					player.playerLevel[i] = 99;
					player.playerXP[i] = player.getPA().getXPForLevel(100);
					player.getPA().refreshSkill(i);
					player.getPA().requestUpdates();
				}
			}
			if (playerCommand.equals("barrows")) {
				new Location(player, 3564, 3288, 0);
			}
			if (playerCommand.startsWith("npc")) {
				try {
					int newNPC = Integer.parseInt(playerCommand.substring(4));
					if (newNPC > 0) {
						Server.npcHandler.spawnNpc(player, newNPC, player.absX, player.absY, player.heightLevel, 0, 120, 7, 70, 70, false,
								false);
						player.getActionSender().sendMessage("You spawn a Npc.");
					} else {
						player.getActionSender().sendMessage("No such NPC.");
					}
				} catch (Exception e) {

				}
			}
			if (playerCommand.startsWith("interface")) {
				String[] args = playerCommand.split(" ");
				player.getPA().showInterface(Integer.parseInt(args[1]));
			}
			if (playerCommand.startsWith("find")) {
				int id = Integer.parseInt(playerCommand.substring(5));
				for (int frame = 1; frame < 15_000; frame++) {
					player.getPA().sendFrame126("" + frame, frame);
				}
				player.getPA().showInterface(id);
			}
			if (playerCommand.equalsIgnoreCase("players")) {
				player.getActionSender().sendMessage("There are currently " + PlayerHandler.getPlayerCount() + " players online.");
			}
			if (playerCommand.equals("barrowsloot")) {
				for (int i = 0; i < 25; i++) {
					player.getItems().addItem(player.getPA().randomRunes(), Misc.random(150) + 100);
					if (Misc.random(10) == 1) {
						System.out.println("Run  #" + i + ", Gathered Barrows: "
								+ player.getItems().addItem(player.getPA().randomBarrows(), 1));
					}
				}
			}
			/*
			 * if (playerCommand.startsWith("shop")) {
			 * c.getShops().openShop(Integer.parseInt(playerCommand.substring(5)
			 * )); }
			 */
			if (playerCommand.startsWith("changepassword")) {
				player.playerPass = Misc.getFilteredInput(playerCommand.substring(15));
				player.getActionSender().sendMessage("Your password is now: " + player.playerPass);
			}

			if (playerCommand.startsWith("ioi")) {
				String[] args = playerCommand.split(" ");
				player.getItems().itemOnInterface(Integer.parseInt(args[1]), Integer.parseInt(args[2]));
			}

			if (playerCommand.startsWith("setlevel")) {
				if (player.inWild())
					return;
				for (int j = 0; j < player.playerEquipment.length; j++) {
					if (player.playerEquipment[j] > 0) {
						player.getActionSender().sendMessage("Take off your shit idiot..");
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
					player.playerXP[skill] = player.getPA().getXPForLevel(level) + 5;
					player.playerLevel[skill] = player.getPA().getLevelForXP(player.playerXP[skill]);
					player.getPA().refreshSkill(skill);
				} catch (Exception e) {
				}
			}
			if (playerCommand.startsWith("object")) {
				String[] args = playerCommand.split(" ");
				player.getPA().object(Integer.parseInt(args[1]), player.absX, player.absY, 0, 10);
			}
			if (playerCommand.startsWith("tele")) {
				String[] arg = playerCommand.split(" ");
				if (arg.length > 3)
					player.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), Integer.parseInt(arg[3]));
				else if (arg.length == 3)
					player.getPA().movePlayer(Integer.parseInt(arg[1]), Integer.parseInt(arg[2]), player.heightLevel);
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
							player.getItems().addItem(newItemID, newItemAmount);
							System.out.println("Spawned: " + newItemID + " by: " + player.playerName);
						} else {
							player.getActionSender().sendMessage("No such item.");
						}
					} else {
						player.getActionSender().sendMessage("Use as ::item 995 200");
					}
				} catch (Exception e) {

				}
			}

			if (player.playerRights >= 2) {
				Plugin.execute("admin_command_" + playerCommand, player, playerCommand);
				if (playerCommand.startsWith("yell")) {
					for (int plyr = 0; plyr < Server.playerHandler.players.length; plyr++) {
						if (Server.playerHandler.players[plyr] != null) {
							Player c2 = (Player) Server.playerHandler.players[plyr];
							c2.getActionSender().sendMessage("[" + player.playerName + "]: " + playerCommand.substring(7));
						}
					}
				}
				if (playerCommand.startsWith("reloadshops")) {
					Server.shopHandler = new server.model.shops.ShopHandler();
				}
				if (playerCommand.startsWith("interface")) {
					String[] args = playerCommand.split(" ");
					player.getPA().showInterface(Integer.parseInt(args[1]));
				}
				if (playerCommand.startsWith("gfx")) {
					String[] args = playerCommand.split(" ");
					player.gfx0(Integer.parseInt(args[1]));
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
						player.getPA().showInterface(a);
					} catch (Exception e) {
						player.getActionSender().sendMessage("::interface ####");
					}
				}

				if (playerCommand.startsWith("xteleto")) {
					String name = playerCommand.substring(8);
					for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
						if (Server.playerHandler.players[i] != null) {
							if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(name)) {
								player.getPA().movePlayer(Server.playerHandler.players[i].getX(),
										Server.playerHandler.players[i].getY(),
										Server.playerHandler.players[i].heightLevel);
							}
						}
					}
				}

				if (playerCommand.startsWith("npc") && player.playerName.equalsIgnoreCase("Infexis")) {
					try {
						int newNPC = Integer.parseInt(playerCommand.substring(4));
						if (newNPC > 0) {
							Server.npcHandler.spawnNpc(player, newNPC, player.absX, player.absY, 0, 0, 120, 7, 70, 70, false, false);
							player.getActionSender().sendMessage("You spawn a Npc.");
						} else {
							player.getActionSender().sendMessage("No such NPC.");
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
									player.getActionSender().sendMessage("You have IP banned the user: "
											+ Server.playerHandler.players[i].playerName + " with the host: "
											+ Server.playerHandler.players[i].connectedFrom);
									Server.playerHandler.players[i].disconnected = true;
								}
							}
						}
					} catch (Exception e) {
						player.getActionSender().sendMessage("Player Must Be Offline.");
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
						player.getActionSender().sendMessage("Player Must Be Offline.");
					}
				}

				if (playerCommand.startsWith("unban")) {
					try {
						String playerToBan = playerCommand.substring(6);
						Connection.removeNameFromBanList(playerToBan);
						player.getActionSender().sendMessage(playerToBan + " has been unbanned.");
					} catch (Exception e) {
						player.getActionSender().sendMessage("Player Must Be Offline.");
					}
				}
				if (playerCommand.startsWith("anim")) {
					String[] args = playerCommand.split(" ");
					player.startAnimation(Integer.parseInt(args[1]));
					player.getPA().requestUpdates();
				}

				if (playerCommand.startsWith("mute")) {
					try {
						String playerToBan = playerCommand.substring(5);
						Connection.addNameToMuteList(playerToBan);
						for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
							if (Server.playerHandler.players[i] != null) {
								if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
									Player c2 = (Player) Server.playerHandler.players[i];
									c2.getActionSender().sendMessage("You have been muted by: " + player.playerName);
									break;
								}
							}
						}
					} catch (Exception e) {
						player.getActionSender().sendMessage("Player Must Be Offline.");
					}
				}
				if (playerCommand.startsWith("ipmute")) {
					try {
						String playerToBan = playerCommand.substring(7);
						for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
							if (Server.playerHandler.players[i] != null) {
								if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
									Connection.addIpToMuteList(Server.playerHandler.players[i].connectedFrom);
									player.getActionSender().sendMessage("You have IP Muted the user: "
											+ Server.playerHandler.players[i].playerName);
									Player c2 = (Player) Server.playerHandler.players[i];
									c2.getActionSender().sendMessage("You have been muted by: " + player.playerName);
									break;
								}
							}
						}
					} catch (Exception e) {
						player.getActionSender().sendMessage("Player Must Be Offline.");
					}
				}
				if (playerCommand.startsWith("unipmute")) {
					try {
						String playerToBan = playerCommand.substring(9);
						for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
							if (Server.playerHandler.players[i] != null) {
								if (Server.playerHandler.players[i].playerName.equalsIgnoreCase(playerToBan)) {
									Connection.unIPMuteUser(Server.playerHandler.players[i].connectedFrom);
									player.getActionSender().sendMessage("You have Un Ip-Muted the user: "
											+ Server.playerHandler.players[i].playerName);
									break;
								}
							}
						}
					} catch (Exception e) {
						player.getActionSender().sendMessage("Player Must Be Offline.");
					}
				}
				if (playerCommand.startsWith("unmute")) {
					try {
						String playerToBan = playerCommand.substring(7);
						Connection.unMuteUser(playerToBan);
					} catch (Exception e) {
						player.getActionSender().sendMessage("Player Must Be Offline.");
					}
				}
			}
		}
		
	}
}
