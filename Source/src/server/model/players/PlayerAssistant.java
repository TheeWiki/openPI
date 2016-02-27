package server.model.players;

import server.Constants;
import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.minigames.castle_wars.CastleWars;
import server.model.minigames.duel_arena.Rules;
import server.model.npcs.NPCHandler;
import server.model.players.skills.AdditionalExp;
import server.model.players.skills.SkillIndex;
import server.model.players.skills.magic.Enchantment;
import server.util.Misc;

public class PlayerAssistant {

	private Player player;

	public PlayerAssistant(Player Player) {
		this.player = Player;
	}

	public int CraftInt, Dcolor, FletchInt;

	/**
	 * MulitCombat icon
	 * 
	 * @param i1
	 *            0 = off 1 = on
	 */
	public void multiWay(int i1) {
		// synchronized(c) {
		player.outStream.createFrame(61);
		player.outStream.writeByte(i1);
		player.updateRequired = true;
		player.setAppearanceUpdateRequired(true);

	}

	public void createPlayersObjectAnim(int X, int Y, int animationID, int tileObjectType, int orientation) {
		try {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC(Y - (player.mapRegionY * 8));
			player.getOutStream().writeByteC(X - (player.mapRegionX * 8));
			int x = 0;
			int y = 0;
			player.getOutStream().createFrame(160);
			player.getOutStream().writeByteS(((x & 7) << 4) + (y & 7));// tiles away
																	// - could
																	// just send
																	// 0
			player.getOutStream().writeByteS((tileObjectType << 2) + (orientation & 3));
			player.getOutStream().writeWordA(animationID);// animation id
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void objectAnim(int X, int Y, int animationID, int tileObjectType, int orientation) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Player players = (Player) p;
				if (players.distanceToPoint(X, Y) <= 25) {
					players.getPA().createPlayersObjectAnim(X, Y, animationID, tileObjectType, orientation);
				}
			}
		}
	}

	public void sendItemOnInterface(int id, int zoom, int model) {
		/*
		 * StreamBuffer.OutBuffer out = StreamBuffer.newOutBuffer(7);
		 * out.writeHeader(player.getEncryptor(), 246); out.writeShort(id == 0 ?
		 * -1 : id, StreamBuffer.ByteOrder.LITTLE); out.writeShort(zoom);
		 * out.writeShort(model); player.send(out.getBuffer());
		 */
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(246);
			player.getOutStream().writeWordBigEndian(id == 0 ? -1 : id);
			player.getOutStream().writeWord(zoom);
			player.getOutStream().writeWord(model);
			player.flushOutStream();
		}
	}

	public void resetAutocast() {
		player.autocastId = 0;
		player.autocasting = false;
		player.getActionSender().sendConfig(108, 0);
	}

	public int getItemSlot(int itemID) {
		for (int i = 0; i < player.playerItems.length; i++) {
			if ((player.playerItems[i] - 1) == itemID) {
				return i;
			}
		}
		return -1;
	}

	public boolean isItemInBag(int itemID) {
		for (int i = 0; i < player.playerItems.length; i++) {
			if ((player.playerItems[i] - 1) == itemID) {
				return true;
			}
		}
		return false;
	}

	public int freeSlots() {
		int freeS = 0;
		for (int i = 0; i < player.playerItems.length; i++) {
			if (player.playerItems[i] <= 0) {
				freeS++;
			}
		}
		return freeS;
	}

	public void turnTo(int pointX, int pointY) {
		player.focusPointX = 2 * pointX + 1;
		player.focusPointY = 2 * pointY + 1;
		player.updateRequired = true;
	}

	public void movePlayer(int x, int y, int h) {
		player.resetWalkingQueue();
		player.teleportToX = x;
		player.teleportToY = y;
		player.heightLevel = h;
		requestUpdates();
	}

	public int getX() {
		return absX;
	}

	public int getY() {
		return absY;
	}

	public int absX, absY;
	public int heightLevel;

	public static void showInterface(Player Player, int i) {
		Player.getOutStream().createFrame(97);
		Player.getOutStream().writeWord(i);
		Player.flushOutStream();
	}

	public static void sendQuest(Player Player, String s, int i) {
		Player.getOutStream().createFrameVarSizeWord(126);
		Player.getOutStream().writeString(s);
		Player.getOutStream().writeWordA(i);
		Player.getOutStream().endFrameVarSizeWord();
		Player.flushOutStream();
	}

	private static int[] optionIds = { 2460, 2470, 2481, 2493, 2493 };

	public void showOptions(Player player, String... lines) {
		if (lines == null || lines.length < 2 || lines.length > 5) {
			return;
		}
		int id = optionIds[lines.length - 2];
		player.getPA().sendFrame126("Select an Option", id++);
		for (int i = 0; i < 5; i++) {
			player.getPA().sendFrame126(i >= lines.length ? "" : lines[i], id++);
		}
		player.getPA().sendFrame164(optionIds[lines.length - 2] - 1);
	}

	public void sendStillGraphics(int id, int heightS, int y, int x, int timeBCS) {
		player.getOutStream().createFrame(85);
		player.getOutStream().writeByteC(y - (player.mapRegionY * 8));
		player.getOutStream().writeByteC(x - (player.mapRegionX * 8));
		player.getOutStream().createFrame(4);
		player.getOutStream().writeByte(0);// Tiles away (X >> 4 + Y & 7)
										// //Tiles away from
		// absX and absY.
		player.getOutStream().writeWord(id); // Graphic ID.
		player.getOutStream().writeByte(heightS); // Height of the graphic when
												// cast.
		player.getOutStream().writeWord(timeBCS); // Time before the graphic
												// plays.
		player.flushOutStream();
	}

	public void createArrow(int type, int id) {
		if (player != null) {
			player.getOutStream().createFrame(254); // The packet ID
			player.getOutStream().writeByte(type); // 1=NPC, 10=Player
			player.getOutStream().writeWord(id); // NPC/Player ID
			player.getOutStream().write3Byte(0); // Junk
		}
	}

	public void createArrow(int x, int y, int height, int pos) {
		if (player != null) {
			player.getOutStream().createFrame(254); // The packet ID
			player.getOutStream().writeByte(pos); // Position on Square(2 = middle, 3
												// = west, 4 = east, 5 = south,
												// 6 = north)
			player.getOutStream().writeWord(x); // X-Coord of Object
			player.getOutStream().writeWord(y); // Y-Coord of Object
			player.getOutStream().writeByte(height); // Height off Ground
		}
	}

	public void sendQuest(String s, int i) {
		player.getOutStream().createFrameVarSizeWord(126);
		player.getOutStream().writeString(s);
		player.getOutStream().writeWordA(i);
		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();
	}

	public void sendFrame126(String s, int id) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrameVarSizeWord(126);
			player.getOutStream().writeString(s);
			player.getOutStream().writeWordA(id);
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}

	}

	public void sendLink(String s) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrameVarSizeWord(187);
			player.getOutStream().writeString(s);
		}

	}
	
	public void setSkillLevel(int skillNum, int currentLevel, int XP) {
		synchronized (player) {
			if (player.getOutStream() != null && player != null) {
				player.getOutStream().createFrame(134);
				player.getOutStream().writeByte(skillNum);
				player.getOutStream().writeDWord_v1(XP);
				player.getOutStream().writeByte(currentLevel);
				player.flushOutStream();
			}
		}
	}

	/**
	 * Play sounds
	 * 
	 * @param SOUNDID
	 *            : ID
	 * @param delay
	 *            : SOUND DELAY
	 */
	public void playSound(int SOUNDID, int delay) {
		if (Constants.SOUND) {
			if (soundVolume <= -1) {
				return;
			}
			/**
			 * Deal with regions We dont need to play this again because you are
			 * in the current region
			 */
			if (player != null) {
				if (soundVolume >= 0) {
					if (player.goodDistance(player.absX, player.absY, this.absX, this.absY, 2)) {
						System.out.println(
								"Playing sound " + player.playerName + ", Id: " + SOUNDID + ", Vol: " + soundVolume);
						player.getOutStream().createFrame(174);
						player.getOutStream().writeWord(SOUNDID);
						player.getOutStream().writeByte(soundVolume);
						player.getOutStream().writeWord(/* delay */0);
					}
				}
			}

		}
	}

	public int soundVolume = 10;

	public void sendString(final String s, final int id) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrameVarSizeWord(126);
			player.getOutStream().writeString(s);
			player.getOutStream().writeWordA(id);
			player.getOutStream().endFrameVarSizeWord();
			player.flushOutStream();
		}

	}

	public void sendSong(int id) {
		if (player.getOutStream() != null && player != null && id != -1) {
			player.getOutStream().createFrame(74);
			player.getOutStream().writeWordBigEndian(id);
		}
	}

	/**
	 * Sends a player a sound effect
	 */
	public void sendSound(int soundId) {
		if (soundId > 0 & player != null && player.outStream != null) {
			player.outStream.createFrame(174);
			player.outStream.writeWord(soundId);
			player.outStream.writeByte(100);
			player.outStream.writeWord(5);
		}
	}

	/**
	 * Sends a player a sound effect
	 */
	public void sound(int soundId) {
		if (soundId > 0 && player.outStream != null) {
			player.outStream.createFrame(174);
			player.outStream.writeWord(soundId);
			player.outStream.writeByte(100);
			player.outStream.writeWord(5);
		}
	}

	/**
	 * TODO: No sounds plays Sends a player a sound effect
	 */
	public void sendSound2(int i1, int i2, int i3) {
		player.outStream.createFrame(174);
		player.outStream.writeWord(i1); // id
		player.outStream.writeByte(i2); // volume, just set it to 100 unless you play
									// around with your Player after this
		player.outStream.writeWord(i3); // delay
		player.updateRequired = true;
		player.appearanceUpdateRequired = true;
		player.flushOutStream();
	}

	public void sendQuickSong(int id, int songDelay) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(121);
			player.getOutStream().writeWordBigEndian(id);
			player.getOutStream().writeWordBigEndian(songDelay);
			player.flushOutStream();
		}
	}

	public void sendColor(int id, int color) {
		if (player.getOutStream() != null && player != null) {
			player.outStream.createFrame(122);
			player.outStream.writeWordBigEndianA(id);
			player.outStream.writeWordBigEndianA(color);
		}
	}

	public void sendSound(int id, int type, int delay) {
		if (player.getOutStream() != null && player != null && id != -1) {
			player.getOutStream().createFrame(174);
			player.getOutStream().writeWordBigEndian(id);
			player.getOutStream().writeByte(type);
			player.getOutStream().writeWordBigEndian(delay);
			player.flushOutStream();
		}
	}

	public void sendFrame106(int sideIcon) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(106);
			player.getOutStream().writeByteC(sideIcon);
			player.flushOutStream();
			requestUpdates();
		}
	}

	public void sendFrame107() {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(107);
			player.flushOutStream();
		}
	}

	public void sendFrame185(int Frame) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(185);
			player.getOutStream().writeWordBigEndianA(Frame);
		}

	}

	public void showInterface(int interfaceid) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(97);
			player.getOutStream().writeWord(interfaceid);
			player.flushOutStream();

		}
	}

	public void sendFrame248(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(248);
			player.getOutStream().writeWordA(MainFrame);
			player.getOutStream().writeWord(SubFrame);
			player.flushOutStream();

		}
	}

	public void sendFrame246(int MainFrame, int SubFrame, int SubFrame2) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(246);
			player.getOutStream().writeWordBigEndian(MainFrame);
			player.getOutStream().writeWord(SubFrame);
			player.getOutStream().writeWord(SubFrame2);
			player.flushOutStream();

		}
	}

	public void sendFrame171(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(171);
			player.getOutStream().writeByte(MainFrame);
			player.getOutStream().writeWord(SubFrame);
			player.flushOutStream();

		}
	}

	public void sendFrame200(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(200);
			player.getOutStream().writeWord(MainFrame);
			player.getOutStream().writeWord(SubFrame);
			player.flushOutStream();
		}
	}

	public void sendFrame70(int i, int o, int id) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(70);
			player.getOutStream().writeWord(i);
			player.getOutStream().writeWordBigEndian(o);
			player.getOutStream().writeWordBigEndian(id);
			player.flushOutStream();
		}

	}

	public void sendFrame75(int MainFrame, int SubFrame) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(75);
			player.getOutStream().writeWordBigEndianA(MainFrame);
			player.getOutStream().writeWordBigEndianA(SubFrame);
			player.flushOutStream();
		}

	}

	public void sendFrame164(int Frame) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(164);
			player.getOutStream().writeWordBigEndian_dup(Frame);
			player.flushOutStream();
		}

	}

	public void setPrivateMessaging(int i) { // friends and ignore list status
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(221);
			player.getOutStream().writeByte(i);
			player.flushOutStream();
		}

	}

	public void setChatOptions(int publicChat, int privateChat, int tradeBlock) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(206);
			player.getOutStream().writeByte(publicChat);
			player.getOutStream().writeByte(privateChat);
			player.getOutStream().writeByte(tradeBlock);
			player.flushOutStream();
		}

	}

	public void sendFrame87(int id, int state) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(87);
			player.getOutStream().writeWordBigEndian_dup(id);
			player.getOutStream().writeDWord_v1(state);
			player.flushOutStream();
		}

	}

	@SuppressWarnings("unused")
	public void sendPM(long name, int rights, byte[] chatmessage, int messagesize) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrameVarSize(196);
			player.getOutStream().writeQWord(name);
			player.getOutStream().writeDWord(player.lastChatId++);
			player.getOutStream().writeByte(rights);
			player.getOutStream().writeBytes(chatmessage, messagesize, 0);
			player.getOutStream().endFrameVarSize();
			player.flushOutStream();
			String chatmessagegot = Misc.textUnpack(chatmessage, messagesize);
			String target = Misc.longToPlayerName(name);
		}

	}

	public void createPlayerHints(int type, int id) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(254);
			player.getOutStream().writeByte(type);
			player.getOutStream().writeWord(id);
			player.getOutStream().write3Byte(0);
			player.flushOutStream();
		}

	}

	public void createObjectHints(int x, int y, int height, int pos) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(254);
			player.getOutStream().writeByte(pos);
			player.getOutStream().writeWord(x);
			player.getOutStream().writeWord(y);
			player.getOutStream().writeByte(height);
			player.flushOutStream();
		}

	}

	public void loadPM(long playerName, int world) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			if (world != 0) {
				world += 9;
			} else if (!Constants.WORLD_LIST_FIX) {
				world += 1;
			}
			player.getOutStream().createFrame(50);
			player.getOutStream().writeQWord(playerName);
			player.getOutStream().writeByte(world);
			player.flushOutStream();
		}

	}

	public void removeAllWindows() {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(219);
			player.flushOutStream();
		}

	}

	public void closeAllWindows() {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(219);
			player.flushOutStream();
		}

	}

	public void sendFrame34(int id, int slot, int column, int amount) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.outStream.createFrameVarSizeWord(34); // init item to smith screen
			player.outStream.writeWord(column); // Column Across Smith Screen
			player.outStream.writeByte(4); // Total Rows?
			player.outStream.writeDWord(slot); // Row Down The Smith Screen
			player.outStream.writeWord(id + 1); // item
			player.outStream.writeByte(amount); // how many there are?
			player.outStream.endFrameVarSizeWord();
		}

	}

	public void walkableInterface(int id) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(208);
			player.getOutStream().writeWordBigEndian_dup(id);
			player.flushOutStream();
		}

	}

	public int mapStatus = 0;

	public void sendFrame99(int state) { // used for disabling map
		// synchronized(c) {
		// if(c.getOutStream() != null && c != null) {
		// if(mapStatus != state) {
		// mapStatus = state;
		// c.getOutStream().createFrame(99);
		// c.getOutStream().writeByte(state);
		// c.flushOutStream();
		// }
		//
		// }
	}

	public void sendCrashFrame() { // used for crashing cheat Players
		synchronized (player) {
			if (player.getOutStream() != null && player != null) {
				player.getOutStream().createFrame(123);
				player.flushOutStream();
			}
		}
	}

	/**
	 * Reseting animations for everyone
	 **/

	public void frame1() {
		// synchronized(c) {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			if (PlayerHandler.players[i] != null) {
				Player person = (Player) PlayerHandler.players[i];
				if (person != null) {
					if (person.getOutStream() != null && !person.disconnected) {
						if (player.distanceToPoint(person.getX(), person.getY()) <= 25) {
							person.getOutStream().createFrame(1);
							person.flushOutStream();
							person.getPA().requestUpdates();
						}
					}
				}

			}
		}
	}

	/**
	 * Creating projectile
	 **/
	public void createProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC((y - (player.getMapRegionY() * 8)) - 2);
			player.getOutStream().writeByteC((x - (player.getMapRegionX() * 8)) - 3);
			player.getOutStream().createFrame(117);
			player.getOutStream().writeByte(angle);
			player.getOutStream().writeByte(offY);
			player.getOutStream().writeByte(offX);
			player.getOutStream().writeWord(lockon);
			player.getOutStream().writeWord(gfxMoving);
			player.getOutStream().writeByte(startHeight);
			player.getOutStream().writeByte(endHeight);
			player.getOutStream().writeWord(time);
			player.getOutStream().writeWord(speed);
			player.getOutStream().writeByte(16);
			player.getOutStream().writeByte(64);
			player.flushOutStream();
		}
	}

	public void createProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC((y - (player.getMapRegionY() * 8)) - 2);
			player.getOutStream().writeByteC((x - (player.getMapRegionX() * 8)) - 3);
			player.getOutStream().createFrame(117);
			player.getOutStream().writeByte(angle);
			player.getOutStream().writeByte(offY);
			player.getOutStream().writeByte(offX);
			player.getOutStream().writeWord(lockon);
			player.getOutStream().writeWord(gfxMoving);
			player.getOutStream().writeByte(startHeight);
			player.getOutStream().writeByte(endHeight);
			player.getOutStream().writeWord(time);
			player.getOutStream().writeWord(speed);
			player.getOutStream().writeByte(slope);
			player.getOutStream().writeByte(64);
			player.flushOutStream();
		}
	}

	public void createProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving, int startHeight,
			int endHeight, int lockon, int time, int slope) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC((y - (player.getMapRegionY() * 8)) - 2);
			player.getOutStream().writeByteC((x - (player.getMapRegionX() * 8)) - 3);
			player.getOutStream().createFrame(117);
			player.getOutStream().writeByte(angle);
			player.getOutStream().writeByte(offY);
			player.getOutStream().writeByte(offX);
			player.getOutStream().writeWord(lockon);
			player.getOutStream().writeWord(gfxMoving);
			player.getOutStream().writeByte(startHeight);
			player.getOutStream().writeByte(endHeight);
			player.getOutStream().writeWord(time);
			player.getOutStream().writeWord(speed);
			player.getOutStream().writeByte(slope);
			player.getOutStream().writeByte(64);
			player.flushOutStream();
		}
	}

	public void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized(c) {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = (Player) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							if (p.heightLevel == player.heightLevel)
								person.getPA().createProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
										endHeight, lockon, time, slope);
						}
					}
				}
			}
		}
	}

	// projectiles for everyone within 25 squares
	public void createPlayersProjectile(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time) {
		// synchronized(c) {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = (Player) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							if (p.heightLevel == player.heightLevel)
								person.getPA().createProjectile(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
										endHeight, lockon, time);
						}
					}
				}
			}
		}
	}

	public void createPlayersProjectile2(int x, int y, int offX, int offY, int angle, int speed, int gfxMoving,
			int startHeight, int endHeight, int lockon, int time, int slope) {
		// synchronized(c) {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = (Player) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getPA().createProjectile2(x, y, offX, offY, angle, speed, gfxMoving, startHeight,
									endHeight, lockon, time, slope);
						}
					}
				}
			}
		}
	}

	/**
	 ** GFX
	 **/
	public void stillGfx(int id, int x, int y, int height, int time) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC(y - (player.getMapRegionY() * 8));
			player.getOutStream().writeByteC(x - (player.getMapRegionX() * 8));
			player.getOutStream().createFrame(4);
			player.getOutStream().writeByte(0);
			player.getOutStream().writeWord(id);
			player.getOutStream().writeByte(height);
			player.getOutStream().writeWord(time);
			player.flushOutStream();
		}

	}

	// creates gfx for everyone
	public void createPlayersStillGfx(int id, int x, int y, int height, int time) {
		// synchronized(c) {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			Player p = PlayerHandler.players[i];
			if (p != null) {
				Player person = (Player) p;
				if (person != null) {
					if (person.getOutStream() != null) {
						if (person.distanceToPoint(x, y) <= 25) {
							person.getPA().stillGfx(id, x, y, height, time);
						}
					}
				}
			}

		}
	}

	/**
	 * Objects, add and remove
	 **/
	public void object(int objectId, int objectX, int objectY, int face, int objectType) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC(objectY - (player.getMapRegionY() * 8));
			player.getOutStream().writeByteC(objectX - (player.getMapRegionX() * 8));
			player.getOutStream().createFrame(101);
			player.getOutStream().writeByteC((objectType << 2) + (face & 3));
			player.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				player.getOutStream().createFrame(151);
				player.getOutStream().writeByteS(0);
				player.getOutStream().writeWordBigEndian(objectId);
				player.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			player.flushOutStream();
		}

	}

	public void checkObjectSpawn(int objectId, int objectX, int objectY, int face, int objectType) {
		if (player.distanceToPoint(objectX, objectY) > 60)
			return;
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(85);
			player.getOutStream().writeByteC(objectY - (player.getMapRegionY() * 8));
			player.getOutStream().writeByteC(objectX - (player.getMapRegionX() * 8));
			player.getOutStream().createFrame(101);
			player.getOutStream().writeByteC((objectType << 2) + (face & 3));
			player.getOutStream().writeByte(0);

			if (objectId != -1) { // removing
				player.getOutStream().createFrame(151);
				player.getOutStream().writeByteS(0);
				player.getOutStream().writeWordBigEndian(objectId);
				player.getOutStream().writeByteS((objectType << 2) + (face & 3));
			}
			player.flushOutStream();
		}

	}

	/**
	 * Show option, attack, trade, follow etc
	 **/
	public String optionType = "null";

	public void showOption(int i, int l, String s, int a) {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			if (!optionType.equalsIgnoreCase(s)) {
				optionType = s;
				player.getOutStream().createFrameVarSize(104);
				player.getOutStream().writeByteC(i);
				player.getOutStream().writeByteA(l);
				player.getOutStream().writeString(s);
				player.getOutStream().endFrameVarSize();
				player.flushOutStream();
			}

		}
	}

	/**
	 * Open bank
	 **/
	public void openUpBank() {
		// synchronized(c) {
		if (player.getOutStream() != null && player != null) {
			player.getItems().resetItems(5064);
			player.getItems().rearrangeBank();
			player.getItems().resetBank();
			player.getItems().resetTempItems();
			player.getOutStream().createFrame(248);
			player.getOutStream().writeWordA(5292);
			player.getOutStream().writeWord(5063);
			player.flushOutStream();
		}

	}

	/**
	 * Private Messaging
	 **/
	public void logIntoPM() {
		setPrivateMessaging(2);
		for (int i1 = 0; i1 < Constants.MAX_PLAYERS; i1++) {
			Player p = PlayerHandler.players[i1];
			if (p != null && p.isActive) {
				Player o = (Player) p;
				if (o != null) {
					o.getPA().updatePM(player.playerId, 1);
				}
			}
		}
		boolean pmLoaded = false;

		for (int i = 0; i < player.friends.length; i++) {
			if (player.friends[i] != 0) {
				for (int i2 = 1; i2 < Constants.MAX_PLAYERS; i2++) {
					Player p = PlayerHandler.players[i2];
					if (p != null && p.isActive && Misc.playerNameToInt64(p.playerName) == player.friends[i]) {
						Player o = (Player) p;
						if (o != null) {
							if (player.playerRights >= 2 || p.privateChat == 0
									|| (p.privateChat == 1 && o.getPA().isInPM(Misc.playerNameToInt64(player.playerName)))) {
								loadPM(player.friends[i], 1);
								pmLoaded = true;
							}
							break;
						}
					}
				}
				if (!pmLoaded) {
					loadPM(player.friends[i], 0);
				}
				pmLoaded = false;
			}
			for (int i1 = 1; i1 < Constants.MAX_PLAYERS; i1++) {
				Player p = PlayerHandler.players[i1];
				if (p != null && p.isActive) {
					Player o = (Player) p;
					if (o != null) {
						o.getPA().updatePM(player.playerId, 1);
					}
				}
			}
		}
	}

	@SuppressWarnings({ "unused" })
	public void updatePM(int pID, int world) { // used for private chat updates
		Player p = PlayerHandler.players[pID];
		if (p == null || p.playerName == null || p.playerName.equals("null")) {
			return;
		}
		Player o = (Player) p;
		if (o == null) {
			return;
		}
		long l = Misc.playerNameToInt64(PlayerHandler.players[pID].playerName);

		if (p.privateChat == 0) {
			for (int i = 0; i < player.friends.length; i++) {
				if (player.friends[i] != 0) {
					if (l == player.friends[i]) {
						loadPM(l, world);
						return;
					}
				}
			}
		} else if (p.privateChat == 1) {
			for (int i = 0; i < player.friends.length; i++) {
				if (player.friends[i] != 0) {
					if (l == player.friends[i]) {
						if (o.getPA().isInPM(Misc.playerNameToInt64(player.playerName))) {
							loadPM(l, world);
							return;
						} else {
							loadPM(l, 0);
							return;
						}
					}
				}
			}
		} else if (p.privateChat == 2) {
			for (int i = 0; i < player.friends.length; i++) {
				if (player.friends[i] != 0) {
					if (l == player.friends[i] && player.playerRights < 2) {
						loadPM(l, 0);
						return;
					}
				}
			}
		}
	}

	public boolean isInPM(long l) {
		for (int i = 0; i < player.friends.length; i++) {
			if (player.friends[i] != 0) {
				if (l == player.friends[i]) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Drink AntiPosion Potions
	 * 
	 * @param itemId
	 *            The itemId
	 * @param itemSlot
	 *            The itemSlot
	 * @param newItemId
	 *            The new item After Drinking
	 * @param healType
	 *            The type of poison it heals
	 */
	public void potionPoisonHeal(int itemId, int itemSlot, int newItemId, int healType) {
		player.attackTimer = player.getCombat().getAttackDelay(
				player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
		if (player.duelRule[Rules.DRINK_RULE.getRule()]) {
			player.getActionSender().sendMessage("Potions has been disabled in this duel!");
			return;
		}
		if (!player.isDead && System.currentTimeMillis() - player.foodDelay > 2000) {
			if (player.getItems().playerHasItem(itemId, 1, itemSlot)) {
				player.getActionSender().sendMessage("You drink the " + player.getItems().getItemName(itemId).toLowerCase() + ".");
				player.foodDelay = System.currentTimeMillis();
				// Actions
				if (healType == 1) {
					// Cures The Poison
				} else if (healType == 2) {
					// Cures The Poison + protects from getting poison again
				}
				player.startAnimation(0x33D);
				player.getItems().deleteItem(itemId, itemSlot, 1);
				player.getItems().addItem(newItemId, 1);
				requestUpdates();
			}
		}
	}

	/**
	 * Magic on items
	 **/

	public void magicOnItems(int slot, int itemId, int spellId) {

		int[][] boltData = { { 1155, 879, 9, 9236 }, { 1155, 9337, 17, 9240 }, { 1165, 9335, 19, 9237 },
				{ 1165, 880, 29, 9238 }, { 1165, 9338, 37, 9241 }, { 1176, 9336, 39, 9239 }, { 1176, 9339, 59, 9242 },
				{ 1180, 9340, 67, 9243 }, { 1187, 9341, 78, 9244 }, { 6003, 9342, 97, 9245 } };
		switch (spellId) {
		case 1155: // Lvl-1 enchant sapphire
		case 1165: // Lvl-2 enchant emerald
		case 1176: // Lvl-3 enchant ruby
		case 1180: // Lvl-4 enchant diamond
		case 1187: // Lvl-5 enchant dragonstone
		case 6003: // Lvl-6 enchant onyx
			for (int i = 0; i < boltData.length; i++) {
				if (itemId == boltData[i][1]) {
					Enchantment.enchantBolt(player, spellId, itemId, 28);
				} else {
					Enchantment.enchantItem(player, itemId, spellId);
				}
			}
			break;
		case 1162: // low alch
			if (System.currentTimeMillis() - player.alchDelay > 1000) {
				if (!player.getCombat().checkMagicReqs(49)) {
					break;
				}
				if (itemId == 995) {
					player.getActionSender().sendMessage("You can't alch coins");
					break;
				}
				player.getItems().deleteItem(itemId, slot, 1);
				player.getItems().addItem(995, player.getShops().getItemShopValue(itemId) / 3);
				player.startAnimation(Enchantment.MAGIC_SPELLS[49][2]);
				player.gfx100(Enchantment.MAGIC_SPELLS[49][3]);
				player.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(Enchantment.MAGIC_SPELLS[49][7] * SkillIndex.MAGIC.getExpRatio(),
						SkillIndex.MAGIC.getSkillId());
				refreshSkill(6);
			}
			break;

		case 1178: // high alch
			if (System.currentTimeMillis() - player.alchDelay > 2000) {
				if (!player.getCombat().checkMagicReqs(50)) {
					break;
				}
				if (itemId == 995) {
					player.getActionSender().sendMessage("You can't alch coins");
					break;
				}
				player.getItems().deleteItem(itemId, slot, 1);
				player.getItems().addItem(995, (int) (player.getShops().getItemShopValue(itemId) * .75));
				player.startAnimation(Enchantment.MAGIC_SPELLS[50][2]);
				player.gfx100(Enchantment.MAGIC_SPELLS[50][3]);
				player.alchDelay = System.currentTimeMillis();
				sendFrame106(6);
				addSkillXP(Enchantment.MAGIC_SPELLS[50][7] * SkillIndex.MAGIC.getExpRatio(),
						SkillIndex.MAGIC.getSkillId());
				refreshSkill(6);
			}
			break;
		}
	}

	/**
	 * Dieing
	 **/

	public void applyDead() {
		int weapon = player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()];
		player.respawnTimer = 15;
		player.isDead = false;
		if (CastleWars.isInCw(player)) {
			CastleWars.respawn();
		}
		if (player.duelStatus != 6) {
			// c.killerId = c.getCombat().getKillerId(c.playerId);
			player.killerId = findKiller();
			Player o = (Player) PlayerHandler.players[player.killerId];
			if (o != null) {
				if (player.killerId != player.playerId)
					o.getActionSender().sendMessage("You have defeated " + player.playerName + "!");
				player.playerKilled = player.playerId;
				if (o.duelStatus == 5) {
					o.duelStatus++;
				}
			}
		}
		if (weapon == CastleWars.SARA_BANNER || weapon == CastleWars.ZAMMY_BANNER) {
			player.getItems().removeItem(weapon, 3);
			player.getItems().deleteItem2(weapon, 1);
			CastleWars.dropFlag(player, weapon);
		}
		player.faceUpdate(0);
		player.npcIndex = 0;
		player.playerIndex = 0;
		player.stopMovement();
		if (player.duelStatus <= 4) {
			player.getActionSender().sendMessage(Constants.DEATH_MESSAGE);
			if (CastleWars.isInCw(player)) {
				Player o = (Player) PlayerHandler.players[player.killerId];
				player.cwDeaths += 1;
				o.cwKills += 1;
			}
		} else if (player.duelStatus != 6) {
			player.getActionSender().sendMessage("You have lost the duel!");
		}
		resetDamageDone();
		player.specAmount = 10;
		player.getItems().addSpecialBar(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]);
		player.lastVeng = 0;
		player.vengOn = false;
		resetFollowers();
		player.attackTimer = 10;
	}

	public void resetDamageDone() {
		for (int i = 0; i < PlayerHandler.players.length; i++) {
			if (PlayerHandler.players[i] != null) {
				PlayerHandler.players[i].damageTaken[player.playerId] = 0;
			}
		}
	}

	public void vengMe() {
		if (System.currentTimeMillis() - player.lastVeng > 30000) {
			if (player.getItems().playerHasItem(557, 10) && player.getItems().playerHasItem(9075, 4)
					&& player.getItems().playerHasItem(560, 2)) {
				player.vengOn = true;
				player.lastVeng = System.currentTimeMillis();
				player.startAnimation(4410);
				player.gfx100(726);
				player.getItems().deleteItem(557, player.getItems().getItemSlot(557), 10);
				player.getItems().deleteItem(560, player.getItems().getItemSlot(560), 2);
				player.getItems().deleteItem(9075, player.getItems().getItemSlot(9075), 4);
			} else {
				player.getActionSender().sendMessage("You do not have the required runes to cast this spell. (9075 for astrals)");
			}
		} else {
			player.getActionSender().sendMessage("You must wait 30 seconds before casting this again.");
		}
	}

	public void resetTb() {
		player.teleBlockLength = 0;
		player.teleBlockDelay = 0;
	}

	public void handleStatus(int i, int i2, int i3) {
		if (i == 1)
			player.getItems().addItem(i2, i3);
		else if (i == 2) {
			player.playerXP[i2] = player.getPA().getXPForLevel(i3) + 5;
			player.playerLevel[i2] = player.getPA().getLevelForXP(player.playerXP[i2]);
		}
	}

	public void resetFollowers() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].followId == player.playerId) {
					Player player = (Player) PlayerHandler.players[j];
					player.getPA().resetFollow();
				}
			}
		}
	}

	public void giveLife() {
		player.isDead = false;
		player.faceUpdate(-1);
		player.freezeTimer = 0;
		if (player.duelStatus <= 4 && !player.getPA().inPitsWait()) { // if we are not in
															// a duel we must be
															// in wildy so
															// remove items
			if (!CastleWars.isInCw(player) && !player.inPits && !player.inFightCaves()) {
				player.getItems().resetKeepItems();
				if ((player.playerRights >= 2 && Constants.ADMIN_DROP_ITEMS) || player.playerRights != 2) {
					if (!player.isSkulled) { // what items to keep
						player.getItems().keepItem(0, true);
						player.getItems().keepItem(1, true);
						player.getItems().keepItem(2, true);
					}
					if (player.prayerActive[10] && System.currentTimeMillis() - player.lastProtItem > 700) {
						player.getItems().keepItem(3, true);
					}
					player.getItems().dropAllItems(); // drop all items
					player.getItems().deleteAllItems(); // delete all items

					if (!player.isSkulled) { // add the kept items once we finish
										// deleting and dropping them
						for (int i1 = 0; i1 < 3; i1++) {
							if (player.itemKeptId[i1] > 0) {
								player.getItems().addItem(player.itemKeptId[i1], 1);
							}
						}
					}
					if (player.prayerActive[10]) { // if we have protect items
						if (player.itemKeptId[3] > 0) {
							player.getItems().addItem(player.itemKeptId[3], 1);
						}
					}
				}
				player.getItems().resetKeepItems();
			} else if (player.inPits) {
				Server.fightPits.removePlayerFromPits(player.playerId);
				player.pitsStatus = 1;
			}
		}
		player.runEnergy = 100;
		player.getCombat().resetPrayers();
		for (int skill = 0; skill < 20; skill++) {
			player.playerLevel[skill] = getLevelForXP(player.playerXP[skill]);
			player.getPA().refreshSkill(skill);
		}
		if (player.pitsStatus == 1) {
			movePlayer(2399, 5173, 0);
		} else if (player.duelStatus <= 4) { // if we are not in a duel repawn to
										// wildy
			movePlayer(Constants.RESPAWN_X, Constants.RESPAWN_Y, 0);
			player.isSkulled = false;
			player.skullTimer = 0;
			player.attackedPlayers.clear();
		} else if (player.inFightCaves()) {
			player.getPA().resetTzhaar();
		} else { // we are in a duel, respawn outside of arena
			Player o = (Player) PlayerHandler.players[player.duelingWith];
			if (o != null) {
				o.getPA().createPlayerHints(10, -1);
				if (o.duelStatus == 6) {
					o.getTradeAndDuel().duelVictory();
				}
			}
			player.getPA().movePlayer(Constants.DUELING_RESPAWN_X + (Misc.random(Constants.RANDOM_DUELING_RESPAWN)),
					Constants.DUELING_RESPAWN_Y + (Misc.random(Constants.RANDOM_DUELING_RESPAWN)), 0);
			o.getPA().movePlayer(Constants.DUELING_RESPAWN_X + (Misc.random(Constants.RANDOM_DUELING_RESPAWN)),
					Constants.DUELING_RESPAWN_Y + (Misc.random(Constants.RANDOM_DUELING_RESPAWN)), 0);
			if (player.duelStatus != 6) { // if we have won but have died, don't
										// reset the duel status.
				player.getTradeAndDuel().resetDuel();
			}
		}
		PlayerSave.saveGame(player);
		player.getCombat().resetPlayerAttack();
		resetAnimation();
		player.startAnimation(65535);
		frame1();
		resetTb();
		player.isSkulled = false;
		player.attackedPlayers.clear();
		player.headIconPk = -1;
		player.skullTimer = -1;
		player.damageTaken = new int[Constants.MAX_PLAYERS];
		player.getPA().requestUpdates();
	}

	/**
	 * Location change for digging, levers etc TODO: Fix barrows dig coordinates
	 **/

	public void changeLocation() {
		switch (player.newLocation) {
		case 1:
			// sendFrame99(2);
			movePlayer(3578, 9706, -1);
			break;
		case 2:
			// sendFrame99(2);
			movePlayer(3568, 9683, -1);
			break;
		case 3:
			// sendFrame99(2);
			movePlayer(3557, 9703, -1);
			break;
		case 4:
			// sendFrame99(2);
			movePlayer(3556, 9718, -1);
			break;
		case 5:
			// sendFrame99(2);
			movePlayer(3534, 9704, -1);
			break;
		case 6:
			// sendFrame99(2);
			movePlayer(3546, 9684, -1);
			break;
		}
		player.newLocation = 0;
	}

	/**
	 * Teleporting
	 **/
	public void spellTeleport(int x, int y, int height) {
		player.getPA().startTeleport(x, y, height, player.playerMagicBook == 1 ? "ancient" : "modern");
	}

	public void startTeleport(int x, int y, int height, String teleportType) {
		if (player.duelStatus == 5) {
			player.getActionSender().sendMessage("You can't teleport during a duel!");
			return;
		}
		if (player.inWild() && player.wildLevel > Constants.NO_TELEPORT_WILD_LEVEL) {
			player.getActionSender().sendMessage("You can't teleport above level " + Constants.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
			return;
		}
		if (System.currentTimeMillis() - player.teleBlockDelay < player.teleBlockLength) {
			player.getActionSender().sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (!player.isDead && player.teleTimer == 0 && player.respawnTimer == -6) {
			if (player.playerIndex > 0 || player.npcIndex > 0)
				player.getCombat().resetPlayerAttack();
			player.stopMovement();
			removeAllWindows();
			player.teleX = x;
			player.teleY = y;
			player.npcIndex = 0;
			player.playerIndex = 0;
			player.faceUpdate(0);
			player.teleHeight = height;
			if (teleportType.equalsIgnoreCase("modern")) {
				player.startAnimation(714);
				player.teleTimer = 11;
				player.teleGfx = 308;
				player.teleEndAnimation = 715;
			}
			if (teleportType.equalsIgnoreCase("ancient")) {
				player.startAnimation(1979);
				player.teleGfx = 0;
				player.teleTimer = 9;
				player.teleEndAnimation = 0;
				player.gfx0(392);
			}

		}
	}

	public void startTeleport2(int x, int y, int height) {
		if (player.duelStatus == 5) {
			player.getActionSender().sendMessage("You can't teleport during a duel!");
			return;
		}
		if (System.currentTimeMillis() - player.teleBlockDelay < player.teleBlockLength) {
			player.getActionSender().sendMessage("You are teleblocked and can't teleport.");
			return;
		}
		if (!player.isDead && player.teleTimer == 0) {
			player.stopMovement();
			removeAllWindows();
			player.teleX = x;
			player.teleY = y;
			player.npcIndex = 0;
			player.playerIndex = 0;
			player.faceUpdate(0);
			player.teleHeight = height;
			player.startAnimation(714);
			player.teleTimer = 11;
			player.teleGfx = 308;
			player.teleEndAnimation = 715;

		}
	}

	public void processTeleport() {
		player.teleportToX = player.teleX;
		player.teleportToY = player.teleY;
		player.heightLevel = player.teleHeight;
		if (player.teleEndAnimation > 0) {
			player.startAnimation(player.teleEndAnimation);
		}
	}

	public void followPlayer() {
		if (PlayerHandler.players[player.followId] == null || PlayerHandler.players[player.followId].isDead) {
			player.followId = 0;
			return;
		}
		if (player.freezeTimer > 0) {
			return;
		}
		if (player.isDead || player.playerLevel[3] <= 0)
			return;

		int otherX = PlayerHandler.players[player.followId].getX();
		int otherY = PlayerHandler.players[player.followId].getY();
		boolean withinDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 2);
		@SuppressWarnings("unused")
		boolean goodDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 1);
		boolean hallyDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 2);
		boolean bowDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 8);
		boolean rangeWeaponDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 4);
		boolean sameSpot = player.absX == otherX && player.absY == otherY;
		if (!player.goodDistance(otherX, otherY, player.getX(), player.getY(), 25)) {
			player.followId = 0;
			return;
		}
		if (player.goodDistance(otherX, otherY, player.getX(), player.getY(), 1)) {
			if (otherX != player.getX() && otherY != player.getY()) {
				stopDiagonal(otherX, otherY);
				return;
			}
		}

		if ((player.usingBow || player.mageFollow || (player.playerIndex > 0 && player.autocastId > 0)) && bowDistance && !sameSpot) {
			return;
		}

		if (player.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return;
		}

		if (player.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
			return;
		}

		player.faceUpdate(player.followId + 32768);
		if (otherX == player.absX && otherY == player.absY) {
			int r = Misc.random(3);
			switch (r) {
			case 0:
				walkTo(0, -1);
				break;
			case 1:
				walkTo(0, 1);
				break;
			case 2:
				walkTo(1, 0);
				break;
			case 3:
				walkTo(-1, 0);
				break;
			}
		} else if (player.isRunning2 && !withinDistance) {
			if (otherY > player.getY() && otherX == player.getX()) {
				walkTo(0, getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
			} else if (otherY < player.getY() && otherX == player.getX()) {
				walkTo(0, getMove(player.getY(), otherY + 1) + getMove(player.getY(), otherY + 1));
			} else if (otherX > player.getX() && otherY == player.getY()) {
				walkTo(getMove(player.getX(), otherX - 1) + getMove(player.getX(), otherX - 1), 0);
			} else if (otherX < player.getX() && otherY == player.getY()) {
				walkTo(getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1), 0);
			} else if (otherX < player.getX() && otherY < player.getY()) {
				walkTo(getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1),
						getMove(player.getY(), otherY + 1) + getMove(player.getY(), otherY + 1));
			} else if (otherX > player.getX() && otherY > player.getY()) {
				walkTo(getMove(player.getX(), otherX - 1) + getMove(player.getX(), otherX - 1),
						getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
			} else if (otherX < player.getX() && otherY > player.getY()) {
				walkTo(getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1),
						getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
			} else if (otherX > player.getX() && otherY < player.getY()) {
				walkTo(getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1),
						getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
			}
		} else {
			if (otherY > player.getY() && otherX == player.getX()) {
				walkTo(0, getMove(player.getY(), otherY - 1));
			} else if (otherY < player.getY() && otherX == player.getX()) {
				walkTo(0, getMove(player.getY(), otherY + 1));
			} else if (otherX > player.getX() && otherY == player.getY()) {
				walkTo(getMove(player.getX(), otherX - 1), 0);
			} else if (otherX < player.getX() && otherY == player.getY()) {
				walkTo(getMove(player.getX(), otherX + 1), 0);
			} else if (otherX < player.getX() && otherY < player.getY()) {
				walkTo(getMove(player.getX(), otherX + 1), getMove(player.getY(), otherY + 1));
			} else if (otherX > player.getX() && otherY > player.getY()) {
				walkTo(getMove(player.getX(), otherX - 1), getMove(player.getY(), otherY - 1));
			} else if (otherX < player.getX() && otherY > player.getY()) {
				walkTo(getMove(player.getX(), otherX + 1), getMove(player.getY(), otherY - 1));
			} else if (otherX > player.getX() && otherY < player.getY()) {
				walkTo(getMove(player.getX(), otherX - 1), getMove(player.getY(), otherY + 1));
			}
		}
		player.faceUpdate(player.followId + 32768);
	}

	public void followNpc() {
		if (NPCHandler.npcs[player.followId2] == null || NPCHandler.npcs[player.followId2].isDead) {
			player.followId2 = 0;
			return;
		}
		if (player.freezeTimer > 0) {
			return;
		}
		if (player.isDead || player.playerLevel[3] <= 0)
			return;

		int otherX = NPCHandler.npcs[player.followId2].getX();
		int otherY = NPCHandler.npcs[player.followId2].getY();
		boolean withinDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 2);
		@SuppressWarnings("unused")
		boolean goodDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 1);
		boolean hallyDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 2);
		boolean bowDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 8);
		boolean rangeWeaponDistance = player.goodDistance(otherX, otherY, player.getX(), player.getY(), 4);
		boolean sameSpot = player.absX == otherX && player.absY == otherY;
		if (!player.goodDistance(otherX, otherY, player.getX(), player.getY(), 25)) {
			player.followId2 = 0;
			return;
		}
		if (player.goodDistance(otherX, otherY, player.getX(), player.getY(), 1)) {
			if (otherX != player.getX() && otherY != player.getY()) {
				stopDiagonal(otherX, otherY);
				return;
			}
		}

		if ((player.usingBow || player.mageFollow || (player.npcIndex > 0 && player.autocastId > 0)) && bowDistance && !sameSpot) {
			return;
		}

		if (player.getCombat().usingHally() && hallyDistance && !sameSpot) {
			return;
		}

		if (player.usingRangeWeapon && rangeWeaponDistance && !sameSpot) {
			return;
		}

		player.faceUpdate(player.followId2);
		if (otherX == player.absX && otherY == player.absY) {
			int r = Misc.random(3);
			switch (r) {
			case 0:
				walkTo(0, -1);
				break;
			case 1:
				walkTo(0, 1);
				break;
			case 2:
				walkTo(1, 0);
				break;
			case 3:
				walkTo(-1, 0);
				break;
			}
		} else if (player.isRunning2 && !withinDistance) {
			if (otherY > player.getY() && otherX == player.getX()) {
				walkTo(0, getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
			} else if (otherY < player.getY() && otherX == player.getX()) {
				walkTo(0, getMove(player.getY(), otherY + 1) + getMove(player.getY(), otherY + 1));
			} else if (otherX > player.getX() && otherY == player.getY()) {
				walkTo(getMove(player.getX(), otherX - 1) + getMove(player.getX(), otherX - 1), 0);
			} else if (otherX < player.getX() && otherY == player.getY()) {
				walkTo(getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1), 0);
			} else if (otherX < player.getX() && otherY < player.getY()) {
				walkTo(getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1),
						getMove(player.getY(), otherY + 1) + getMove(player.getY(), otherY + 1));
			} else if (otherX > player.getX() && otherY > player.getY()) {
				walkTo(getMove(player.getX(), otherX - 1) + getMove(player.getX(), otherX - 1),
						getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
			} else if (otherX < player.getX() && otherY > player.getY()) {
				walkTo(getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1),
						getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
			} else if (otherX > player.getX() && otherY < player.getY()) {
				walkTo(getMove(player.getX(), otherX + 1) + getMove(player.getX(), otherX + 1),
						getMove(player.getY(), otherY - 1) + getMove(player.getY(), otherY - 1));
			}
		} else {
			if (otherY > player.getY() && otherX == player.getX()) {
				walkTo(0, getMove(player.getY(), otherY - 1));
			} else if (otherY < player.getY() && otherX == player.getX()) {
				walkTo(0, getMove(player.getY(), otherY + 1));
			} else if (otherX > player.getX() && otherY == player.getY()) {
				walkTo(getMove(player.getX(), otherX - 1), 0);
			} else if (otherX < player.getX() && otherY == player.getY()) {
				walkTo(getMove(player.getX(), otherX + 1), 0);
			} else if (otherX < player.getX() && otherY < player.getY()) {
				walkTo(getMove(player.getX(), otherX + 1), getMove(player.getY(), otherY + 1));
			} else if (otherX > player.getX() && otherY > player.getY()) {
				walkTo(getMove(player.getX(), otherX - 1), getMove(player.getY(), otherY - 1));
			} else if (otherX < player.getX() && otherY > player.getY()) {
				walkTo(getMove(player.getX(), otherX + 1), getMove(player.getY(), otherY - 1));
			} else if (otherX > player.getX() && otherY < player.getY()) {
				walkTo(getMove(player.getX(), otherX - 1), getMove(player.getY(), otherY + 1));
			}
		}
		player.faceUpdate(player.followId2);
	}

	public int getRunningMove(int i, int j) {
		if (j - i > 2)
			return 2;
		else if (j - i < -2)
			return -2;
		else
			return j - i;
	}

	public void resetFollow() {
		player.followId = 0;
		player.followId2 = 0;
		player.mageFollow = false;
		player.outStream.createFrame(174);
		player.outStream.writeWord(0);
		player.outStream.writeByte(0);
		player.outStream.writeWord(1);
	}

	public void walkTo(int i, int j) {
		player.newWalkCmdSteps = 0;
		if (++player.newWalkCmdSteps > 50)
			player.newWalkCmdSteps = 0;
		int k = player.getX() + i;
		k -= player.mapRegionX * 8;
		player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = 0;
		int l = player.getY() + j;
		l -= player.mapRegionY * 8;

		for (int n = 0; n < player.newWalkCmdSteps; n++) {
			player.getNewWalkCmdX()[n] += k;
			player.getNewWalkCmdY()[n] += l;
		}
	}

	public void walkTo2(int i, int j) {
		if (player.freezeDelay > 0)
			return;
		player.newWalkCmdSteps = 0;
		if (++player.newWalkCmdSteps > 50)
			player.newWalkCmdSteps = 0;
		int k = player.getX() + i;
		k -= player.mapRegionX * 8;
		player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = 0;
		int l = player.getY() + j;
		l -= player.mapRegionY * 8;

		for (int n = 0; n < player.newWalkCmdSteps; n++) {
			player.getNewWalkCmdX()[n] += k;
			player.getNewWalkCmdY()[n] += l;
		}
	}

	public void stopDiagonal(int otherX, int otherY) {
		if (player.freezeDelay > 0)
			return;
		player.newWalkCmdSteps = 1;
		int xMove = otherX - player.getX();
		int yMove = 0;
		if (xMove == 0)
			yMove = otherY - player.getY();
		/*
		 * if (!clipHor) { yMove = 0; } else if (!clipVer) { xMove = 0; }
		 */

		int k = player.getX() + xMove;
		k -= player.mapRegionX * 8;
		player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = 0;
		int l = player.getY() + yMove;
		l -= player.mapRegionY * 8;

		for (int n = 0; n < player.newWalkCmdSteps; n++) {
			player.getNewWalkCmdX()[n] += k;
			player.getNewWalkCmdY()[n] += l;
		}

	}

	public void walkToCheck(int i, int j) {
		if (player.freezeDelay > 0)
			return;
		player.newWalkCmdSteps = 0;
		if (++player.newWalkCmdSteps > 50)
			player.newWalkCmdSteps = 0;
		int k = player.getX() + i;
		k -= player.mapRegionX * 8;
		player.getNewWalkCmdX()[0] = player.getNewWalkCmdY()[0] = 0;
		int l = player.getY() + j;
		l -= player.mapRegionY * 8;

		for (int n = 0; n < player.newWalkCmdSteps; n++) {
			player.getNewWalkCmdX()[n] += k;
			player.getNewWalkCmdY()[n] += l;
		}
	}

	public int getMove(int place1, int place2) {
		if (System.currentTimeMillis() - player.lastSpear < 4000)
			return 0;
		if ((place1 - place2) == 0) {
			return 0;
		} else if ((place1 - place2) < 0) {
			return 1;
		} else if ((place1 - place2) > 0) {
			return -1;
		}
		return 0;
	}

	public boolean fullVeracs() {
		return player.playerEquipment[EquipmentListener.HAT_SLOT.getSlot()] == 4753
				&& player.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 4757
				&& player.playerEquipment[EquipmentListener.LEGS_SLOT.getSlot()] == 4759
				&& player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4755;
	}

	public boolean fullGuthans() {
		return player.playerEquipment[EquipmentListener.HAT_SLOT.getSlot()] == 4724
				&& player.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()] == 4728
				&& player.playerEquipment[EquipmentListener.LEGS_SLOT.getSlot()] == 4730
				&& player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()] == 4726;
	}

	/**
	 * reseting animation
	 **/
	public void resetAnimation() {
		player.getCombat().getPlayerAnimIndex(
				player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]).toLowerCase());
		player.startAnimation(player.playerStandIndex);
		requestUpdates();
	}

	public void requestUpdates() {
		player.updateRequired = true;
		player.setAppearanceUpdateRequired(true);
	}

	public void Obelisks(int id) {
		if (!player.getItems().playerHasItem(id)) {
			player.getItems().addItem(id, 1);
		}
	}

	int answer;

	public int getTotalLevel() {

		for (int i = 0; i < 21; i++) {
			answer = getLevelForXP(player.playerXP[i]);
		}
		return answer;
	}

	public void levelUp(int skill) {
		getTotalLevel();
		sendFrame126("Total Lvl: " + getTotalLevel(), 3984);
		switch (skill) {
		case 0:
			sendFrame126("Congratulations, you just advanced an attack level!", 6248);
			sendFrame126("Your attack level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6249);
			player.getActionSender().sendMessage("Congratulations, you just advanced an attack level.");
			sendFrame164(6247);

			break;

		case 1:
			sendFrame126("Congratulations, you just advanced a defence level!", 6254);
			sendFrame126("Your defence level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6255);
			player.getActionSender().sendMessage("Congratulations, you just advanced a defence level.");
			sendFrame164(6253);

			break;

		case 2:
			sendFrame126("Congratulations, you just advanced a strength level!", 6207);
			sendFrame126("Your strength level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6208);
			player.getActionSender().sendMessage("Congratulations, you just advanced a strength level.");
			sendFrame164(6206);

			break;

		case 3:
			sendFrame126("Congratulations, you just advanced a hitpoints level!", 6217);
			sendFrame126("Your hitpoints level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6218);
			player.getActionSender().sendMessage("Congratulations, you just advanced a hitpoints level.");
			sendFrame164(6216);
			// hitpoints

			break;

		case 4:
			sendFrame126("Congratulations, you just advanced a ranged level!", 5453);
			sendFrame126("Your ranged level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6114);
			player.getActionSender().sendMessage("Congratulations, you just advanced a ranging level.");
			sendFrame164(4443);

			break;

		case 5:
			sendFrame126("Congratulations, you just advanced a prayer level!", 6243);
			sendFrame126("Your prayer level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6244);
			player.getActionSender().sendMessage("Congratulations, you just advanced a prayer level.");
			sendFrame164(6242);

			break;

		case 6:
			sendFrame126("Congratulations, you just advanced a magic level!", 6212);
			sendFrame126("Your magic level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6213);
			player.getActionSender().sendMessage("Congratulations, you just advanced a magic level.");
			sendFrame164(6211);

			break;

		case 7:
			sendFrame126("Congratulations, you just advanced a cooking level!", 6227);
			sendFrame126("Your cooking level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6228);
			player.getActionSender().sendMessage("Congratulations, you just advanced a cooking level.");
			sendFrame164(6226);

			break;

		case 8:
			sendFrame126("Congratulations, you just advanced a woodcutting level!", 4273);
			sendFrame126("Your woodcutting level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4274);
			player.getActionSender().sendMessage("Congratulations, you just advanced a woodcutting level.");
			sendFrame164(4272);

			break;

		case 9:
			sendFrame126("Congratulations, you just advanced a fletching level!", 6232);
			sendFrame126("Your fletching level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6233);
			player.getActionSender().sendMessage("Congratulations, you just advanced a fletching level.");
			sendFrame164(6231);
			break;

		case 10:
			sendFrame126("Congratulations, you just advanced a fishing level!", 6259);
			sendFrame126("Your fishing level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6260);
			player.getActionSender().sendMessage("Congratulations, you just advanced a fishing level.");
			sendFrame164(6258);
			break;

		case 11:
			sendFrame126("Congratulations, you just advanced a fire making level!", 4283);
			sendFrame126("Your firemaking level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4284);
			player.getActionSender().sendMessage("Congratulations, you just advanced a fire making level.");
			sendFrame164(4282);
			break;

		case 12:
			sendFrame126("Congratulations, you just advanced a crafting level!", 6264);
			sendFrame126("Your crafting level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6265);
			player.getActionSender().sendMessage("Congratulations, you just advanced a crafting level.");
			sendFrame164(6263);
			break;

		case 13:
			sendFrame126("Congratulations, you just advanced a smithing level!", 6222);
			sendFrame126("Your smithing level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6223);
			player.getActionSender().sendMessage("Congratulations, you just advanced a smithing level.");
			sendFrame164(6221);
			break;

		case 14:
			sendFrame126("Congratulations, you just advanced a mining level!", 4417);
			sendFrame126("Your mining level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4438);
			player.getActionSender().sendMessage("Congratulations, you just advanced a mining level.");
			sendFrame164(4416);
			break;

		case 15:
			sendFrame126("Congratulations, you just advanced a herblore level!", 6238);
			sendFrame126("Your herblore level is now " + getLevelForXP(player.playerXP[skill]) + ".", 6239);
			player.getActionSender().sendMessage("Congratulations, you just advanced a herblore level.");
			sendFrame164(6237);
			break;

		case 16:
			sendFrame126("Congratulations, you just advanced a agility level!", 4278);
			sendFrame126("Your agility level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4279);
			player.getActionSender().sendMessage("Congratulations, you just advanced an agility level.");
			sendFrame164(4277);
			break;

		case 17:
			sendFrame126("Congratulations, you just advanced a thieving level!", 4263);
			sendFrame126("Your theiving level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4264);
			player.getActionSender().sendMessage("Congratulations, you just advanced a thieving level.");
			sendFrame164(4261);
			break;

		case 18:
			sendFrame126("Congratulations, you just advanced a slayer level!", 12123);
			sendFrame126("Your slayer level is now " + getLevelForXP(player.playerXP[skill]) + ".", 12124);
			player.getActionSender().sendMessage("Congratulations, you just advanced a slayer level.");
			sendFrame164(12122);
			break;

		case 20:
			sendFrame126("Congratulations, you just advanced a runecrafting level!", 4268);
			sendFrame126("Your runecrafting level is now " + getLevelForXP(player.playerXP[skill]) + ".", 4269);
			player.getActionSender().sendMessage("Congratulations, you just advanced a runecrafting level.");
			sendFrame164(4267);
			break;
		}
		player.dialogueAction = 0;
		player.nextChat = 0;
	}

	public void refreshSkill(int i) {
		switch (i) {
		case 0:
			sendFrame126("" + player.playerLevel[0] + "", 4004);
			sendFrame126("" + getLevelForXP(player.playerXP[0]) + "", 4005);
			sendFrame126("" + player.playerXP[0] + "", 4044);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[0]) + 1) + "", 4045);
			break;

		case 1:
			sendFrame126("" + player.playerLevel[1] + "", 4008);
			sendFrame126("" + getLevelForXP(player.playerXP[1]) + "", 4009);
			sendFrame126("" + player.playerXP[1] + "", 4056);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[1]) + 1) + "", 4057);
			break;

		case 2:
			sendFrame126("" + player.playerLevel[2] + "", 4006);
			sendFrame126("" + getLevelForXP(player.playerXP[2]) + "", 4007);
			sendFrame126("" + player.playerXP[2] + "", 4050);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[2]) + 1) + "", 4051);
			break;

		case 3:
			sendFrame126("" + player.playerLevel[3] + "", 4016);
			sendFrame126("" + getLevelForXP(player.playerXP[3]) + "", 4017);
			sendFrame126("" + player.playerXP[3] + "", 4080);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[3]) + 1) + "", 4081);
			break;

		case 4:
			sendFrame126("" + player.playerLevel[4] + "", 4010);
			sendFrame126("" + getLevelForXP(player.playerXP[4]) + "", 4011);
			sendFrame126("" + player.playerXP[4] + "", 4062);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[4]) + 1) + "", 4063);
			break;

		case 5:
			sendFrame126("" + player.playerLevel[5] + "", 4012);
			sendFrame126("" + getLevelForXP(player.playerXP[5]) + "", 4013);
			sendFrame126("" + player.playerXP[5] + "", 4068);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[5]) + 1) + "", 4069);
			sendFrame126("" + player.playerLevel[5] + "/" + getLevelForXP(player.playerXP[5]) + "", 687);// Prayer
																								// frame
			break;

		case 6:
			sendFrame126("" + player.playerLevel[6] + "", 4014);
			sendFrame126("" + getLevelForXP(player.playerXP[6]) + "", 4015);
			sendFrame126("" + player.playerXP[6] + "", 4074);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[6]) + 1) + "", 4075);
			break;

		case 7:
			sendFrame126("" + player.playerLevel[7] + "", 4034);
			sendFrame126("" + getLevelForXP(player.playerXP[7]) + "", 4035);
			sendFrame126("" + player.playerXP[7] + "", 4134);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[7]) + 1) + "", 4135);
			break;

		case 8:
			sendFrame126("" + player.playerLevel[8] + "", 4038);
			sendFrame126("" + getLevelForXP(player.playerXP[8]) + "", 4039);
			sendFrame126("" + player.playerXP[8] + "", 4146);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[8]) + 1) + "", 4147);
			break;

		case 9:
			sendFrame126("" + player.playerLevel[9] + "", 4026);
			sendFrame126("" + getLevelForXP(player.playerXP[9]) + "", 4027);
			sendFrame126("" + player.playerXP[9] + "", 4110);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[9]) + 1) + "", 4111);
			break;

		case 10:
			sendFrame126("" + player.playerLevel[10] + "", 4032);
			sendFrame126("" + getLevelForXP(player.playerXP[10]) + "", 4033);
			sendFrame126("" + player.playerXP[10] + "", 4128);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[10]) + 1) + "", 4129);
			break;

		case 11:
			sendFrame126("" + player.playerLevel[11] + "", 4036);
			sendFrame126("" + getLevelForXP(player.playerXP[11]) + "", 4037);
			sendFrame126("" + player.playerXP[11] + "", 4140);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[11]) + 1) + "", 4141);
			break;

		case 12:
			sendFrame126("" + player.playerLevel[12] + "", 4024);
			sendFrame126("" + getLevelForXP(player.playerXP[12]) + "", 4025);
			sendFrame126("" + player.playerXP[12] + "", 4104);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[12]) + 1) + "", 4105);
			break;

		case 13:
			sendFrame126("" + player.playerLevel[13] + "", 4030);
			sendFrame126("" + getLevelForXP(player.playerXP[13]) + "", 4031);
			sendFrame126("" + player.playerXP[13] + "", 4122);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[13]) + 1) + "", 4123);
			break;

		case 14:
			sendFrame126("" + player.playerLevel[14] + "", 4028);
			sendFrame126("" + getLevelForXP(player.playerXP[14]) + "", 4029);
			sendFrame126("" + player.playerXP[14] + "", 4116);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[14]) + 1) + "", 4117);
			break;

		case 15:
			sendFrame126("" + player.playerLevel[15] + "", 4020);
			sendFrame126("" + getLevelForXP(player.playerXP[15]) + "", 4021);
			sendFrame126("" + player.playerXP[15] + "", 4092);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[15]) + 1) + "", 4093);
			break;

		case 16:
			sendFrame126("" + player.playerLevel[16] + "", 4018);
			sendFrame126("" + getLevelForXP(player.playerXP[16]) + "", 4019);
			sendFrame126("" + player.playerXP[16] + "", 4086);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[16]) + 1) + "", 4087);
			break;

		case 17:
			sendFrame126("" + player.playerLevel[17] + "", 4022);
			sendFrame126("" + getLevelForXP(player.playerXP[17]) + "", 4023);
			sendFrame126("" + player.playerXP[17] + "", 4098);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[17]) + 1) + "", 4099);
			break;

		case 18:
			sendFrame126("" + player.playerLevel[18] + "", 12166);
			sendFrame126("" + getLevelForXP(player.playerXP[18]) + "", 12167);
			sendFrame126("" + player.playerXP[18] + "", 12171);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[18]) + 1) + "", 12172);
			break;

		case 19:
			sendFrame126("" + player.playerLevel[19] + "", 13926);
			sendFrame126("" + getLevelForXP(player.playerXP[19]) + "", 13927);
			sendFrame126("" + player.playerXP[19] + "", 13921);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[19]) + 1) + "", 13922);
			break;

		case 20:
			sendFrame126("" + player.playerLevel[20] + "", 4152);
			sendFrame126("" + getLevelForXP(player.playerXP[20]) + "", 4153);
			sendFrame126("" + player.playerXP[20] + "", 4157);
			sendFrame126("" + getXPForLevel(getLevelForXP(player.playerXP[20]) + 1) + "", 4158);
			break;
		}
	}

	public int getXPForLevel(int level) {
		int points = 0;
		int output = 0;

		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
			if (lvl >= level)
				return output;
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output = 0;
		if (exp > 13034430)
			return 99;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor((double) lvl + 300.0 * Math.pow(2.0, (double) lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public boolean addSkillXP(double d, int skill) {
		if (d + player.playerXP[skill] < 0 || player.playerXP[skill] > 200000000) {
			if (player.playerXP[skill] > 200000000) {
				player.playerXP[skill] = 200000000;
			}
			return false;
		}
		d *= AdditionalExp.DOUBLE_EXP.getExp();
		int oldLevel = getLevelForXP(player.playerXP[skill]);
		player.playerXP[skill] += d;
		if (oldLevel < getLevelForXP(player.playerXP[skill])) {
			if (player.playerLevel[skill] < player.getLevelForXP(player.playerXP[skill]) && skill != 3 && skill != 5)
				player.playerLevel[skill] = player.getLevelForXP(player.playerXP[skill]);
			levelUp(skill);
			player.gfx100(199);
			requestUpdates();
		}
		setSkillLevel(skill, player.playerLevel[skill], player.playerXP[skill]);
		refreshSkill(skill);
		return true;
	}

	public void resetBarrows() {
		player.barrowsNpcs[0][1] = 0;
		player.barrowsNpcs[1][1] = 0;
		player.barrowsNpcs[2][1] = 0;
		player.barrowsNpcs[3][1] = 0;
		player.barrowsNpcs[4][1] = 0;
		player.barrowsNpcs[5][1] = 0;
		player.barrowsKillCount = 0;
		player.randomCoffin = Misc.random(3) + 1;
	}

	public static int Barrows[] = { 4708, 4710, 4712, 4714, 4716, 4718, 4720, 4722, 4724, 4726, 4728, 4730, 4732, 4734,
			4736, 4738, 4745, 4747, 4749, 4751, 4753, 4755, 4757, 4759 };
	public static int Runes[] = { 4740, 558, 560, 565 };
	public static int Pots[] = {};

	public int randomBarrows() {
		return Barrows[(int) (Math.random() * Barrows.length)];
	}

	public int randomRunes() {
		return Runes[(int) (Math.random() * Runes.length)];
	}

	public int randomPots() {
		return Pots[(int) (Math.random() * Pots.length)];
	}

	/**
	 * Show an arrow icon on the selected player.
	 * 
	 * @param i
	 *            - Either 0 or 1; 1 is arrow, 0 is none.
	 * @param j
	 *            - The player/Npc that the arrow will be displayed above.
	 * @param k
	 *            - Keep this set as 0
	 * @param l
	 *            - Keep this set as 0
	 */
	public void drawHeadicon(int i, int j, int k, int l) {
		// synchronized(c) {
		player.outStream.createFrame(254);
		player.outStream.writeByte(i);

		if (i == 1 || i == 10) {
			player.outStream.writeWord(j);
			player.outStream.writeWord(k);
			player.outStream.writeByte(l);
		} else {
			player.outStream.writeWord(k);
			player.outStream.writeWord(l);
			player.outStream.writeByte(j);
		}

	}

	public int getNpcId(int id) {
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				if (NPCHandler.npcs[i].npcId == id) {
					return i;
				}
			}
		}
		return -1;
	}

	public void removeObject(int x, int y) {
		object(-1, x, x, 10, 10);
	}

	private void objectToRemove(int X, int Y) {
		object(-1, X, Y, 10, 10);
	}

	private void objectToRemove2(int X, int Y) {
		object(-1, X, Y, -1, 0);
	}

	public void removeObjects() {
		objectToRemove(2638, 4688);
		objectToRemove2(2635, 4693);
		objectToRemove2(2634, 4693);
	}

	public boolean inPitsWait() {
		return player.getX() <= 2404 && player.getX() >= 2394 && player.getY() <= 5175 && player.getY() >= 5169;
	}

	public void castleWarsObjects() {
		object(-1, 2373, 3119, -3, 10);
		object(-1, 2372, 3119, -3, 10);
	}

	public int antiFire() {
		int toReturn = 0;
		if (player.antiFirePot)
			toReturn++;
		if (player.playerEquipment[EquipmentListener.SHIELD_SLOT.getSlot()] == 1540 || player.prayerActive[12]
				|| player.playerEquipment[EquipmentListener.SHIELD_SLOT.getSlot()] == 11284)
			toReturn++;
		return toReturn;
	}

	public boolean checkForFlags() {
		int[][] itemsToCheck = { { 995, 100000000 }, { 35, 5 }, { 667, 5 }, { 2402, 5 }, { 746, 5 }, { 4151, 150 },
				{ 565, 100000 }, { 560, 100000 }, { 555, 300000 }, { 11235, 10 } };
		for (int j = 0; j < itemsToCheck.length; j++) {
			if (itemsToCheck[j][1] < player.getItems().getTotalCount(itemsToCheck[j][0]))
				return true;
		}
		return false;
	}

	public int getWearingAmount() {
		int count = 0;
		for (int j = 0; j < player.playerEquipment.length; j++) {
			if (player.playerEquipment[j] > 0)
				count++;
		}
		return count;
	}

	public void useOperate(int itemId) {
		switch (itemId) {
		case 11283:
		case 11284:
			if (player.playerIndex > 0) {
				player.getCombat().handleDfs();
			} else if (player.npcIndex > 0) {
				player.getCombat().handleDfsNPC();
			}
			break;
		}
	}

	public void getSpeared(int otherX, int otherY) {
		int x = player.absX - otherX;
		int y = player.absY - otherY;
		if (x > 0)
			x = 1;
		else if (x < 0)
			x = -1;
		if (y > 0)
			y = 1;
		else if (y < 0)
			y = -1;
		moveCheck(x, y);
		player.lastSpear = System.currentTimeMillis();
	}

	public void moveCheck(int xMove, int yMove) {
		movePlayer(player.absX + xMove, player.absY + yMove, player.heightLevel);
	}

	public int findKiller() {
		int killer = player.playerId;
		int damage = 0;
		for (int j = 0; j < Constants.MAX_PLAYERS; j++) {
			if (PlayerHandler.players[j] == null)
				continue;
			if (j == player.playerId)
				continue;
			if (player.goodDistance(player.absX, player.absY, PlayerHandler.players[j].absX, PlayerHandler.players[j].absY, 40)
					|| player.goodDistance(player.absX, player.absY + 9400, PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY, 40)
					|| player.goodDistance(player.absX, player.absY, PlayerHandler.players[j].absX,
							PlayerHandler.players[j].absY + 9400, 40))
				if (player.damageTaken[j] > damage) {
					damage = player.damageTaken[j];
					killer = j;
				}
		}
		return killer;
	}

	public void resetTzhaar() {
		player.waveId = -1;
		player.tzhaarToKill = -1;
		player.tzhaarKilled = -1;
		wave = 1;
		player.getPA().movePlayer(2438, 5168, 0);
	}

	public int wave = 1;

	public void enterCaves() {
		resetTzhaar();
		player.getPA().movePlayer(2413, 5117, player.playerId * 4);
		player.waveId = 0;
		player.tzhaarToKill = -1;
		player.tzhaarKilled = -1;
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer event) {
				Server.fightCaves.spawnNextWave((Player) PlayerHandler.players[player.playerId]);
				player.getActionSender().sendMessage("@blu@Wave: " + wave);
				event.stop();
			}

			@Override
			public void stop() {

			}
		}, 20);
		// c.getDH().sendNpcChat1("Prepare for the fight of your life!", 2617,
		// "Tk-nub");
	}

	public void appendPoison(int damage) {
		if (System.currentTimeMillis() - player.lastPoisonSip > player.poisonImmune) {
			player.getActionSender().sendMessage("You have been poisoned.");
			player.poisonDamage = damage;
		}
	}

	public boolean checkForPlayer(int x, int y) {
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				if (p.getX() == x && p.getY() == y)
					return true;
			}
		}
		return false;
	}

	public void fixAllBarrows() {
		int totalCost = 0;
		int cashAmount = player.getItems().getItemAmount(995);
		for (int j = 0; j < player.playerItems.length; j++) {
			boolean breakOut = false;
			for (int i = 0; i < player.getItems().brokenBarrows.length; i++) {
				if (player.playerItems[j] - 1 == player.getItems().brokenBarrows[i][1]) {
					if (totalCost + 80000 > cashAmount) {
						breakOut = true;
						player.getActionSender().sendMessage("You have run out of money.");
						break;
					} else {
						totalCost += 80000;
					}
					player.playerItems[j] = player.getItems().brokenBarrows[i][0] + 1;
				}
			}
			if (breakOut)
				break;
		}
		if (totalCost > 0)
			player.getItems().deleteItem(995, player.getItems().getItemSlot(995), totalCost);
	}

	public void handleLoginText() {
		player.getPA().sendFrame126("Home Teleport", 1300);
		player.getPA().sendFrame126("Teleport to Home", 1301);

		player.getPA().sendFrame126("Training", 1325);
		player.getPA().sendFrame126("Go fight some monsters", 1326);

		player.getPA().sendFrame126("Minigames", 1350);
		player.getPA().sendFrame126("Play various minigames", 1351);

		player.getPA().sendFrame126("Skilling", 1382);
		player.getPA().sendFrame126("Train your non-combat skills here", 1383);

		player.getPA().sendFrame126("PVP", 1415);
		player.getPA().sendFrame126("Fight to be the best", 1416);

		player.getPA().sendFrame126("Members Zone", 1454);
		player.getPA().sendFrame126("Special Members only area", 1455);
		// c.getPA().sendFrame126("Lumbridge Teleport", 1325);
		// c.getPA().sendFrame126("Falador Teleport", 1350);
		// c.getPA().sendFrame126("Camelot Teleport", 1382);
		// c.getPA().sendFrame126("Ardougne Teleport", 1415);

		player.getActionSender().sendMessage(Constants.WELCOME_MESSAGE);
		if (player.membership) {
			player.membership().checkDate(player);
		} else if (player.membership == false) {
			player.getActionSender().sendMessage("@blu@Your account isn't a member, visit ::donate for more details!");
		}
	}

	/*
	 * Resets the shaking of the player's screen.
	 */
	public void resetShaking() {
		player.getActionSender().shakeScreen(player, 1);
	}

	public void itemOnInterface(int interfaceChild, int zoom, int itemId) {
		if (player.getOutStream() != null && player != null) {
			player.getOutStream().createFrame(246);
			player.getOutStream().writeWordBigEndian(interfaceChild);
			player.getOutStream().writeWord(zoom);
			player.getOutStream().writeWord(itemId);
			player.flushOutStream();
		}
	}

	public void handleWeaponStyle() {
		player.getActionSender().sendConfig(43, player.fightMode == 3?  2 : player.fightMode == 2 ? 1 : player.fightMode == 1 ? 3 : player.fightMode);
	}
}