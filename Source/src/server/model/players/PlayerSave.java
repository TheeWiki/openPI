package server.model.players;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import server.Server;
import server.util.Misc;

/**
 * TODO: Add better saving (Json preferred) scf = server character file
 * 
 * @author Dennis
 */
public class PlayerSave {

	/**
	 * Loading
	 **/
	public static int loadGame(Player player, String playerName, String playerPass) {
		String line = "";
		String token = "";
		String token2 = "";
		String[] token3 = new String[3];
		boolean EndOfFile = false;
		int ReadMode = 0;
		BufferedReader characterfile = null;
		boolean File1 = false;

		try {
			characterfile = new BufferedReader(new FileReader("./Data/characters/" + playerName + ".scf"));
			File1 = true;
		} catch (FileNotFoundException fileex1) {
		}

		if (File1) {
		} else {
			Misc.println(playerName + ": character file not found.");
			player.newPlayer = false;
			return 0;
		}
		try {
			line = characterfile.readLine();
		} catch (IOException ioexception) {
			Misc.println(playerName + ": error loading file.");
			return 3;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token3 = token2.split("\t");
				switch (ReadMode) {
				case 1:
					if (token.equals("character-password")) {
						if (playerPass.equalsIgnoreCase(token2) || Misc.basicEncrypt(playerPass).equals(token2)) {
							playerPass = token2;
						} else {
							return 3;
						}
					}
					break;
				case 2:
					if (token.equals("character-height")) {
						player.heightLevel = Integer.parseInt(token2);
					} else if (token.equals("character-posx")) {
						player.teleportToX = (Integer.parseInt(token2) <= 0 ? 3210 : Integer.parseInt(token2));
					} else if (token.equals("character-posy")) {
						player.teleportToY = (Integer.parseInt(token2) <= 0 ? 3424 : Integer.parseInt(token2));
					} else if (token.equals("character-rights")) {
						player.playerRights = Integer.parseInt(token2);
					} else if (token.equals("crystal-bow-shots")) {
						player.crystalBowArrowCount = Integer.parseInt(token2);
					} else if (token.equals("skull-timer")) {
						player.skullTimer = Integer.parseInt(token2);
					} else if (token.equals("magic-book")) {
						player.playerMagicBook = Integer.parseInt(token2);
					} else if (token.equals("pc-points")) {
						player.points = Integer.parseInt(token2);
					} else if (token.equals("kills")) {
						for (int j = 0; j < token3.length; j++) {
							player.loggedKills[j] = Integer.parseInt(token3[j]);
					}
					} else if (token.equals("membership")) {
						player.membership = Boolean.parseBoolean(token2);
					} else if (token.equals("startdate")) {
						player.startDate = Integer.parseInt(token2);
					} else if (token.equals("brother-info")) {
						player.barrowsNpcs[Integer.parseInt(token3[0])][1] = Integer.parseInt(token3[1]);
					} else if (token.equals("special-amount")) {
						player.specAmount = Double.parseDouble(token2);
					} else if (token.equals("selected-coffin")) {
						player.randomCoffin = Integer.parseInt(token2);
					} else if (token.equals("pk-points")) {
						player.pkPoints = Integer.parseInt(token2);
						// java.lang.ArrayIndexOutOfBoundsException: 384
//					} else if (token.equals("music-unlocked")) {
//                        for (int music = 0; music < token3.length; music++) {
//                        	Music.unlocked[music] = Boolean.parseBoolean(token3[music]); 
//                        }
//					}
					} else if(token.equals("character-energy")) {
						player.runEnergy = Integer.parseInt(token2);
					} else if (token.equals("teleblock-length")) {
						player.teleBlockDelay = System.currentTimeMillis();
						player.teleBlockLength = Integer.parseInt(token2);
					} else if (token.equals("pc-points")) {
						player.pcPoints = Integer.parseInt(token2);
					} else if (token.equals("slayerTask")) {
						player.slayerTask = Integer.parseInt(token2);
					} else if (token.equals("taskAmount")) {
						player.taskAmount = Integer.parseInt(token2);
					} else if (token.equals("magePoints")) {
						player.magePoints = Integer.parseInt(token2);
					} else if (token.equals("autoRet")) {
						player.autoRet = Integer.parseInt(token2);
					} else if (token.equals("barrowskillcount")) {
						player.barrowsKillCount = Integer.parseInt(token2);
					} else if (token.equals("flagged")) {
						player.accountFlagged = Boolean.parseBoolean(token2);
					} else if (token.equals("wave")) {
						player.waveId = Integer.parseInt(token2);
					} else if (token.equals("void")) {
						for (int j = 0; j < token3.length; j++) {
							player.voidStatus[j] = Integer.parseInt(token3[j]);
						}
					} else if (token.equals("gwkc")) {
						player.killCount = Integer.parseInt(token2);
					} else if (token.equals("fightMode")) {
						player.fightMode = Integer.parseInt(token2);
					}
					break;
				case 3:
					if (token.equals("character-equip")) {
						player.playerEquipment[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						player.playerEquipmentN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 4:
					if (token.equals("character-look")) {
						player.playerAppearance[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
					}
					break;
				case 5:
					if (token.equals("character-skill")) {
						player.playerLevel[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						player.playerXP[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 6:
					if (token.equals("character-item")) {
						player.playerItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						player.playerItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				case 7:
					if (token.equals("character-bank")) {
						player.bankItems[Integer.parseInt(token3[0])] = Integer.parseInt(token3[1]);
						player.bankItemsN[Integer.parseInt(token3[0])] = Integer.parseInt(token3[2]);
					}
					break;
				}
			} else {
				if (line.equals("[ACCOUNT]")) {
					ReadMode = 1;
				} else if (line.equals("[CHARACTER]")) {
					ReadMode = 2;
				} else if (line.equals("[EQUIPMENT]")) {
					ReadMode = 3;
				} else if (line.equals("[LOOK]")) {
					ReadMode = 4;
				} else if (line.equals("[SKILLS]")) {
					ReadMode = 5;
				} else if (line.equals("[ITEMS]")) {
					ReadMode = 6;
				} else if (line.equals("[BANK]")) {
					ReadMode = 7;
				} else if (line.equals("[EOF]")) {
					try {
						characterfile.close();
					} catch (IOException ioexception) {
					}
					return 1;
				}
			}
			try {
				line = characterfile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			characterfile.close();
		} catch (IOException ioexception) {
		}
		return 13;
	}

	/**
	 * Saving
	 **/
	@SuppressWarnings("static-access")
	public static boolean saveGame(Player player) {
		if (!player.saveFile || player.newPlayer || !player.saveCharacter) {
			// System.out.println("first");
			return false;
		}
		if (player.playerName == null || Server.playerHandler.players[player.playerId] == null) {
			// System.out.println("second");
			return false;
		}
		player.playerName = player.playerName2;
		int tbTime = (int) (player.teleBlockDelay - System.currentTimeMillis() + player.teleBlockLength);
		if (tbTime > 300000 || tbTime < 0) {
			tbTime = 0;
		}

		BufferedWriter characterfile = null;
		try {
			characterfile = new BufferedWriter(new FileWriter("./Data/characters/" + player.playerName + ".scf"));

			/* ACCOUNT */
			characterfile.write("[ACCOUNT]", 0, 9);
			characterfile.newLine();
			characterfile.write("character-username = ", 0, 21);
			characterfile.write(player.playerName, 0, player.playerName.length());
			characterfile.newLine();
			characterfile.write("character-password = ", 0, 21);
			characterfile.write(player.playerPass, 0, player.playerPass.length());
//			characterfile.write(Misc.basicEncrypt(p.playerPass).toString(),  0, Misc.basicEncrypt(p.playerPass).toString().length());
			characterfile.newLine();
			characterfile.newLine();

			/* CHARACTER */
			characterfile.write("[CHARACTER]", 0, 11);
			characterfile.newLine();
			characterfile.write("character-height = ", 0, 19);
			characterfile.write(Integer.toString(player.heightLevel), 0, Integer.toString(player.heightLevel).length());
			characterfile.newLine();
			characterfile.write("character-posx = ", 0, 17);
			characterfile.write(Integer.toString(player.absX), 0, Integer.toString(player.absX).length());
			characterfile.newLine();
			characterfile.write("character-posy = ", 0, 17);
			characterfile.write(Integer.toString(player.absY), 0, Integer.toString(player.absY).length());
			characterfile.newLine();
			characterfile.write("character-rights = ", 0, 19);
			characterfile.write(Integer.toString(player.playerRights), 0, Integer.toString(player.playerRights).length());
			characterfile.newLine();
			characterfile.write("crystal-bow-shots = ", 0, 20);
			characterfile.write(Integer.toString(player.crystalBowArrowCount), 0, Integer.toString(player.crystalBowArrowCount).length());
			characterfile.newLine();
			characterfile.write("skull-timer = ", 0, 14);
			characterfile.write(Integer.toString(player.skullTimer), 0, Integer.toString(player.skullTimer).length());
			characterfile.newLine();
			characterfile.write("magic-book = ", 0, 13);
			characterfile.write(Integer.toString(player.playerMagicBook), 0, Integer.toString(player.playerMagicBook).length());
			characterfile.newLine();
			characterfile.write("pc-points = ", 0, 12);
			characterfile.write(Integer.toString(player.points), 0, Integer.toString(player.points).length());
			characterfile.newLine();
			characterfile.write("kills = ", 0, 8);
			final String killWrite = player.loggedKills[0] + "\t" + player.loggedKills[1]
					+ "\t" + player.loggedKills[2] + "\t" + player.loggedKills[3] + "\t"
					+ player.loggedKills[4] + "\t" + player.loggedKills[5] + "\t"
					+ player.loggedKills[6] + "\t" + player.loggedKills[7] + "\t"
					+ player.loggedKills[8] + "\t" + player.loggedKills[9] + "\t"
					+ player.loggedKills[10] + "\t" + player.loggedKills[11] + "\t"
					+ player.loggedKills[12] + "\t" + player.loggedKills[13] + "\t"
					+ player.loggedKills[14] + "\t" + player.loggedKills[15] + "\t"
					+ player.loggedKills[16] + "\t" + player.loggedKills[17] + "\t"
					+ player.loggedKills[18] + "\t" + player.loggedKills[19] + "\t"
					+ player.loggedKills[20] + "\t" + player.loggedKills[21] + "\t"
					+ player.loggedKills[22] + "\t" + player.loggedKills[23] + "\t"
					+ player.loggedKills[24] + "\t" + player.loggedKills[25] + "\t";
			characterfile.write(killWrite);
			characterfile.newLine();
			characterfile.write("membership = ", 0, 13);
			characterfile.write(Boolean.toString(player.membership), 0, Boolean.toString(player.membership).length());
			characterfile.newLine();
			characterfile.write("startdate = ", 0, 12);
			characterfile.write(Integer.toString(player.startDate), 0, Integer
					.toString(player.startDate).length());
			characterfile.newLine();
			characterfile.write("special-amount = ", 0, 17);
			characterfile.write(Double.toString(player.specAmount), 0, Double.toString(player.specAmount).length());
			characterfile.newLine();
			characterfile.write("selected-coffin = ", 0, 18);
			characterfile.write(Integer.toString(player.randomCoffin), 0, Integer.toString(player.randomCoffin).length());
			characterfile.newLine();
			characterfile.write("barrows-killcount = ", 0, 20);
			characterfile.write(Integer.toString(player.barrowsKillCount), 0, Integer.toString(player.barrowsKillCount).length());
			characterfile.newLine();
			characterfile.write("teleblock-length = ", 0, 19);
			characterfile.write(Integer.toString(tbTime), 0, Integer.toString(tbTime).length());
			characterfile.newLine();
			characterfile.write("character-energy = ", 0, 19);
			characterfile.write(Integer.toString(player.runEnergy), 0, Integer.toString(player.runEnergy).length());
			characterfile.newLine();
			characterfile.write("pc-points = ", 0, 12);
			characterfile.write(Integer.toString(player.pcPoints), 0, Integer.toString(player.pcPoints).length());
			characterfile.newLine();
			characterfile.write("slayerTask = ", 0, 13);
			characterfile.write(Integer.toString(player.slayerTask), 0, Integer.toString(player.slayerTask).length());
			characterfile.newLine();
			characterfile.write("taskAmount = ", 0, 13);
			characterfile.write(Integer.toString(player.taskAmount), 0, Integer.toString(player.taskAmount).length());
			characterfile.newLine();
			characterfile.write("magePoints = ", 0, 13);
			characterfile.write(Integer.toString(player.magePoints), 0, Integer.toString(player.magePoints).length());
			characterfile.newLine();
//			characterfile.write("music-unlocked = ", 0, 17);
//		        String music = "";
//		        for(int i = 0; i < Music.unlocked.length; i++) {
//		        	music += Music.unlocked[i] + "\t";
//		        	characterfile.write(music);
//		        }
		    characterfile.newLine();
			characterfile.write("autoRet = ", 0, 10);
			characterfile.write(Integer.toString(player.autoRet), 0, Integer.toString(player.autoRet).length());
			characterfile.newLine();
			characterfile.write("barrowskillcount = ", 0, 19);
			characterfile.write(Integer.toString(player.barrowsKillCount), 0, Integer.toString(player.barrowsKillCount).length());
			characterfile.newLine();
			characterfile.write("flagged = ", 0, 10);
			characterfile.write(Boolean.toString(player.accountFlagged), 0, Boolean.toString(player.accountFlagged).length());
			characterfile.newLine();
			characterfile.write("wave = ", 0, 7);
			characterfile.write(Integer.toString(player.waveId), 0, Integer.toString(player.waveId).length());
			characterfile.newLine();
			characterfile.write("gwkc = ", 0, 7);
			characterfile.write(Integer.toString(player.killCount), 0, Integer.toString(player.killCount).length());
			characterfile.newLine();
			characterfile.write("fightMode = ", 0, 12);
			characterfile.write(Integer.toString(player.fightMode), 0, Integer.toString(player.fightMode).length());
			characterfile.newLine();
			characterfile.write("void = ", 0, 7);
			String toWrite = player.voidStatus[0] + "\t" + player.voidStatus[1] + "\t" + player.voidStatus[2] + "\t" + player.voidStatus[3] + "\t" + player.voidStatus[4];
			characterfile.write(toWrite);
			characterfile.newLine();
			characterfile.newLine();

			/* EQUIPMENT */
			characterfile.write("[EQUIPMENT]", 0, 11);
			characterfile.newLine();
			for (int i = 0; i < player.playerEquipment.length; i++) {
				characterfile.write("character-equip = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(player.playerEquipment[i]), 0,
						Integer.toString(player.playerEquipment[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(player.playerEquipmentN[i]), 0,
						Integer.toString(player.playerEquipmentN[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.newLine();
			}
			characterfile.newLine();

			/* LOOK */
			characterfile.write("[LOOK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < player.playerAppearance.length; i++) {
				characterfile.write("character-look = ", 0, 17);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(player.playerAppearance[i]), 0,
						Integer.toString(player.playerAppearance[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* SKILLS */
			characterfile.write("[SKILLS]", 0, 8);
			characterfile.newLine();
			for (int i = 0; i < player.playerLevel.length; i++) {
				characterfile.write("character-skill = ", 0, 18);
				characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(player.playerLevel[i]), 0, Integer.toString(player.playerLevel[i]).length());
				characterfile.write("	", 0, 1);
				characterfile.write(Integer.toString(player.playerXP[i]), 0, Integer.toString(player.playerXP[i]).length());
				characterfile.newLine();
			}
			characterfile.newLine();

			/* ITEMS */
			characterfile.write("[ITEMS]", 0, 7);
			characterfile.newLine();
			for (int i = 0; i < player.playerItems.length; i++) {
				if (player.playerItems[i] > 0) {
					characterfile.write("character-item = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(player.playerItems[i]), 0,
							Integer.toString(player.playerItems[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(player.playerItemsN[i]), 0,
							Integer.toString(player.playerItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();

			/* BANK */
			characterfile.write("[BANK]", 0, 6);
			characterfile.newLine();
			for (int i = 0; i < player.bankItems.length; i++) {
				if (player.bankItems[i] > 0) {
					characterfile.write("character-bank = ", 0, 17);
					characterfile.write(Integer.toString(i), 0, Integer.toString(i).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(player.bankItems[i]), 0, Integer.toString(player.bankItems[i]).length());
					characterfile.write("	", 0, 1);
					characterfile.write(Integer.toString(player.bankItemsN[i]), 0,
							Integer.toString(player.bankItemsN[i]).length());
					characterfile.newLine();
				}
			}
			characterfile.newLine();
			
			characterfile.write("[EOF]", 0, 5);
			characterfile.close();
		} catch (IOException ioexception) {
			Misc.println(player.playerName + ": error writing file.");
			return false;
		}
		return true;
	}

}