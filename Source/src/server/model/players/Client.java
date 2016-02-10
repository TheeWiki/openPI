package server.model.players;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Future;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import server.Constants;
import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.EmoteHandler;
import server.model.items.ItemAssistant;
import server.model.npcs.NPC;
import server.model.players.combat.CombatAssistant;
import server.model.players.combat.Experience;
import server.model.players.combat.PlayerKilling;
import server.model.players.combat.Specials;
import server.model.players.packet.PacketHandler;
import server.model.players.skills.cooking.Food;
import server.model.players.skills.herblore.PotionMixing;
import server.model.players.skills.herblore.Potions;
import server.model.players.skills.prayer.BuryBones;
import server.model.shops.ShopAssistant;
import server.net.Packet;
import server.net.Packet.Type;
import server.util.Misc;
import server.util.Stream;

public class Client extends Player {

	public byte buffer[] = null;
	public Stream inStream = null, outStream = null;
	private Channel session;
	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private ShopAssistant shopAssistant = new ShopAssistant(this);
	private TradeAndDuel tradeAndDuel = new TradeAndDuel(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private CombatAssistant combatAssistant = new CombatAssistant(this);
	private ActionHandler actionHandler = new ActionHandler(this);
	private PlayerKilling playerKilling = new PlayerKilling(this);
	private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Queue<Packet> queuedPackets = new LinkedList<Packet>();
	private Potions potions = new Potions(this);
	private PotionMixing potionMixing = new PotionMixing(this);
	private Food food = new Food(this);

	public int lowMemoryVersion = 0;
	public int timeOutCounter = 0;
	public int returnCode = 2;
	private Future<?> currentTask;
	public int currentRegion = 0;

	public Client(Channel s, int _playerId) {
		super(_playerId);
		this.session = s;
		synchronized (this) {
			outStream = new Stream(new byte[Constants.BUFFER_SIZE]);
			outStream.currentOffset = 0;

			inStream = new Stream(new byte[Constants.BUFFER_SIZE]);
			inStream.currentOffset = 0;
			buffer = new byte[Constants.BUFFER_SIZE];
		}
	}

	/**
	 * Shakes the player's screen. Parameters 1, 0, 0, 0 to reset.
	 * 
	 * @param verticleAmount
	 *            How far the up and down shaking goes (1-4).
	 * @param verticleSpeed
	 *            How fast the up and down shaking is.
	 * @param horizontalAmount
	 *            How far the left-right tilting goes.
	 * @param horizontalSpeed
	 *            How fast the right-left tiling goes..
	 */
	public void shakeScreen(int verticleAmount, int verticleSpeed, int horizontalAmount, int horizontalSpeed) {
		outStream.createFrame(35); // Creates frame 35.
		outStream.writeByte(verticleAmount);
		outStream.writeByte(verticleSpeed);
		outStream.writeByte(horizontalAmount);
		outStream.writeByte(horizontalSpeed);
	}

	/*
	 * Resets the shaking of the player's screen.
	 */
	public void resetShaking() {
		shakeScreen(1, 0, 0, 0);
	}

	public boolean updateItems = false;

	private HashMap<String, Object> attributes = new HashMap<String, Object>();

	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key) {
		return (T) attributes.get(key);
	}

	@SuppressWarnings("unchecked")
	public <T> T getAttribute(String key, T fail) {
		T t = (T) attributes.get(key);
		if (t != null) {
			return t;
		}
		return fail;
	}

	public Specials getSA() {
		return specials;
	}

	private Specials specials = new Specials(this);

	public void removeAttribute(String key) {
		attributes.remove(key);
	}

	public void flushOutStream() {
		if (!session.isConnected() || disconnected || outStream.currentOffset == 0)
			return;

		byte[] temp = new byte[outStream.currentOffset];
		System.arraycopy(outStream.buffer, 0, temp, 0, temp.length);
		Packet packet = new Packet(-1, Type.FIXED, ChannelBuffers.wrappedBuffer(temp));
		session.write(packet);
		outStream.currentOffset = 0;

	}

	public void sendClan(String name, String message, String clan, int rights) {
		outStream.createFrameVarSizeWord(217);
		outStream.writeString(name);
		outStream.writeString(message);
		outStream.writeString(clan);
		outStream.writeWord(rights);
		outStream.endFrameVarSize();
	}

	public static final int PACKET_SIZES[] = { 0, 0, 0, 1, -1, 0, 0, 0, 0, 0, // 0
			0, 0, 0, 0, 8, 0, 6, 2, 2, 0, // 10
			0, 2, 0, 6, 0, 12, 0, 0, 0, 0, // 20
			0, 0, 0, 0, 0, 8, 4, 0, 0, 2, // 30
			2, 6, 0, 6, 0, -1, 0, 0, 0, 0, // 40
			0, 0, 0, 12, 0, 0, 0, 8, 8, 12, // 50
			8, 8, 0, 0, 0, 0, 0, 0, 0, 0, // 60
			6, 0, 2, 2, 8, 6, 0, -1, 0, 6, // 70
			0, 0, 0, 0, 0, 1, 4, 6, 0, 0, // 80
			0, 0, 0, 0, 0, 3, 0, 0, -1, 0, // 90
			0, 13, 0, -1, 0, 0, 0, 0, 0, 0, // 100
			0, 0, 0, 0, 0, 0, 0, 6, 0, 0, // 110
			1, 0, 6, 0, 0, 0, -1, 0, 2, 6, // 120
			0, 4, 6, 8, 0, 6, 0, 0, 0, 2, // 130
			0, 0, 0, 0, 0, 6, 0, 0, 0, 0, // 140
			0, 0, 1, 2, 0, 2, 6, 0, 0, 0, // 150
			0, 0, 0, 0, -1, -1, 0, 0, 0, 0, // 160
			0, 0, 0, 0, 0, 0, 0, 0, 0, 0, // 170
			0, 8, 0, 3, 0, 2, 0, 0, 8, 1, // 180
			0, 0, 12, 0, 0, 0, 0, 0, 0, 0, // 190
			2, 0, 0, 0, 0, 0, 0, 0, 4, 0, // 200
			4, 0, 0, 0, 7, 8, 0, 0, 10, 0, // 210
			0, 0, 0, 0, 0, 0, -1, 0, 6, 0, // 220
			1, 0, 0, 0, 6, 0, 6, 8, 1, 0, // 230
			0, 4, 0, 0, 0, 0, -1, 0, -1, 4, // 240
			0, 0, 6, 6, 0, 0, 0 // 250
	};

	public void destruct() {
		if (session == null)
			return;
		Server.panel.removeEntity(playerName);
		// PlayerSaving.getSingleton().requestSave(playerId);
		getPA().removeFromCW();
		if (inPits)
			Server.fightPits.removePlayerFromPits(playerId);
		if (clanId >= 0)
			Server.clanChat.leaveClan(playerId, clanId);
		Misc.println("[DEREGISTERED]: " + playerName + "");
		CycleEventHandler.getSingleton().stopEvents(this);
		disconnected = true;
		session.close();
		session = null;
		inStream = null;
		outStream = null;
		isActive = false;
		buffer = null;
		super.destruct();
	}

	public void sendMessage(String s) {
		// synchronized (this) {
		if (getOutStream() != null) {
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.endFrameVarSize();
		}

	}

	public void setSidebarInterface(int menuId, int form) {
		// synchronized (this) {
		if (getOutStream() != null) {
			outStream.createFrame(71);
			outStream.writeWord(form);
			outStream.writeByteA(menuId);
		}

	}

	@SuppressWarnings("static-access")
	public void initialize() {
		// synchronized (this) {
		Server.panel.addEntity(playerName);
		outStream.createFrame(249);
		outStream.writeByteA(1); // 1 for members, zero for free
		outStream.writeWordBigEndianA(playerId);
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (j == playerId)
				continue;
			if (Server.playerHandler.players[j] != null) {
				if (Server.playerHandler.players[j].playerName.equalsIgnoreCase(playerName))
					disconnected = true;
			}
		}
		for (int i = 0; i < 25; i++) {
			getPA().setSkillLevel(i, playerLevel[i], playerXP[i]);
			getPA().refreshSkill(i);
		}
		for (int p = 0; p < PRAYER.length; p++) { // reset prayer glows
			prayerActive[p] = false;
			getPA().sendFrame36(PRAYER_GLOW[p], 0);
		}
		// if (playerName.equalsIgnoreCase("Sanity")) {
		// getPA().sendCrashFrame();
		// }
		getPA().handleWeaponStyle();
		getPA().handleLoginText();
		accountFlagged = getPA().checkForFlags();
		// getPA().sendFrame36(43, fightMode-1);
		getPA().sendFrame36(108, 0);// resets autocast button
		getPA().sendFrame36(172, 1);
		getPA().sendFrame107(); // reset screen
		getPA().setChatOptions(0, 0, 0); // reset private messaging options
		setSidebarInterface(1, 3917);
		setSidebarInterface(2, 638);
		setSidebarInterface(3, 3213);
		setSidebarInterface(4, 1644);
		setSidebarInterface(5, 5608);
		if (playerMagicBook == 0) {
			setSidebarInterface(6, 1151); // modern
		} else {
			setSidebarInterface(6, 12855); // ancient
		}
		correctCoordinates();
		setSidebarInterface(7, 18128);
		setSidebarInterface(8, 5065);
		setSidebarInterface(9, 5715);
		setSidebarInterface(10, 2449);
		// setSidebarInterface(11, 4445); // wrench tab
		setSidebarInterface(11, 904); // wrench tab
		setSidebarInterface(12, 147); // run tab
		setSidebarInterface(13, -1);
		setSidebarInterface(0, 2423);
		sendMessage("Welcome to " + Constants.SERVER_NAME);
		getPA().showOption(4, 0, "Trade With", 3);
		getPA().showOption(5, 0, "Follow", 4);
		getItems().resetItems(3214);
		getItems().sendWeapon(playerEquipment[playerWeapon], getItems().getItemName(playerEquipment[playerWeapon]));
		getItems().resetBonus();
		getItems().getBonus();
		getItems().writeBonus();
		getItems().setEquipment(playerEquipment[playerHat], 1, playerHat);
		getItems().setEquipment(playerEquipment[playerCape], 1, playerCape);
		getItems().setEquipment(playerEquipment[playerAmulet], 1, playerAmulet);
		getItems().setEquipment(playerEquipment[playerArrows], playerEquipmentN[playerArrows], playerArrows);
		getItems().setEquipment(playerEquipment[playerChest], 1, playerChest);
		getItems().setEquipment(playerEquipment[playerShield], 1, playerShield);
		getItems().setEquipment(playerEquipment[playerLegs], 1, playerLegs);
		getItems().setEquipment(playerEquipment[playerHands], 1, playerHands);
		getItems().setEquipment(playerEquipment[playerFeet], 1, playerFeet);
		getItems().setEquipment(playerEquipment[playerRing], 1, playerRing);
		getItems().setEquipment(playerEquipment[playerWeapon], playerEquipmentN[playerWeapon], playerWeapon);
		getItems().sendWeapon(getEquipment()[playerWeapon], getItems().getItemName(getEquipment()[playerWeapon]));
		getPA().logIntoPM();
		getItems().addSpecialBar(playerEquipment[playerWeapon]);
		saveTimer = Constants.SAVE_TIMER;
		saveCharacter = true;
		Misc.println("[REGISTERED]: " + playerName + "");
		handler.updatePlayer(this, outStream);
		handler.updateNPC(this, outStream);
		flushOutStream();
		getPA().clearClanChat();
		getPA().resetFollow();
		if (addStarter)
			getPA().addStarter();
		if (autoRet == 1)
			getPA().sendFrame36(172, 1);
		else
			getPA().sendFrame36(172, 0);
	}

	public void update() {
		// synchronized (this) {
		handler.updatePlayer(this, outStream);
		handler.updateNPC(this, outStream);
		flushOutStream();

	}

	public void logout() {
		// synchronized (this) {
		if (System.currentTimeMillis() - logoutDelay > 10000) {
			outStream.createFrame(109);
			CycleEventHandler.getSingleton().stopEvents(this);
			properLogout = true;
		} else {
			sendMessage("You must wait a few seconds from being out of combat to logout.");
		}

	}

	public int packetSize = 0, packetType = -1;

	@SuppressWarnings("static-access")
	public void process() {

		if (this.updateItems)
			this.getItems().resetItems(3214);
		if (System.currentTimeMillis() - specDelay > Constants.INCREASE_SPECIAL_AMOUNT) {
			specDelay = System.currentTimeMillis();
			if (specAmount < 10) {
				specAmount += .5;
				if (specAmount > 10)
					specAmount = 10;
				getItems().addSpecialBar(playerEquipment[playerWeapon]);
			}
		}

		if (followId > 0) {
			getPA().followPlayer();
		} else if (followId2 > 0) {
			getPA().followNpc();
		}
		getCombat().handlePrayerDrain();
		if (System.currentTimeMillis() - singleCombatDelay > 3300) {
			underAttackBy = 0;
		}
		if (System.currentTimeMillis() - singleCombatDelay2 > 3300) {
			underAttackBy2 = 0;
		}

		if (System.currentTimeMillis() - restoreStatsDelay > 60000) {
			restoreStatsDelay = System.currentTimeMillis();
			for (int level = 0; level < playerLevel.length; level++) {
				if (playerLevel[level] < getLevelForXP(playerXP[level])) {
					if (level != 5) { // prayer doesn't restore
						playerLevel[level] += 1;
						getPA().setSkillLevel(level, playerLevel[level], playerXP[level]);
						getPA().refreshSkill(level);
					}
				} else if (playerLevel[level] > getLevelForXP(playerXP[level])) {
					playerLevel[level] -= 1;
					getPA().setSkillLevel(level, playerLevel[level], playerXP[level]);
					getPA().refreshSkill(level);
				}
			}
		}

		if (inWild()) {
			int modY = absY > 6400 ? absY - 6400 : absY;
			wildLevel = (((modY - 3520) / 8) + 1);
			getPA().walkableInterface(197);
			if (Constants.SINGLE_AND_MULTI_ZONES) {
				if (inMulti()) {
					getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
				} else {
					getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
				}
			} else {
				getPA().multiWay(-1);
				getPA().sendFrame126("@yel@Level: " + wildLevel, 199);
			}
			getPA().showOption(3, 0, "Attack", 1);
		} else if (inDuelArena()) {
			getPA().walkableInterface(201);
			if (duelStatus == 5) {
				getPA().showOption(3, 0, "Attack", 1);
			} else {
				getPA().showOption(3, 0, "Challenge", 1);
			}
		} else if (inBarrows()) {
			getPA().sendFrame99(2);
			getPA().sendFrame126("Kill Count: " + barrowsKillCount, 4536);
			getPA().walkableInterface(4535);
		} else if (inCwGame || inPits) {
			getPA().showOption(3, 0, "Attack", 1);
		} else if (getPA().inPitsWait()) {
			getPA().showOption(3, 0, "Null", 1);
		} else if (!inCwWait) {
			getPA().sendFrame99(0);
			getPA().walkableInterface(-1);
			getPA().showOption(3, 0, "Null", 1);
		}

		if (!hasMultiSign && inMulti()) {
			hasMultiSign = true;
			getPA().multiWay(1);
		}

		if (hasMultiSign && !inMulti()) {
			hasMultiSign = false;
			getPA().multiWay(-1);
		}

		if (skullTimer > 0) {
			skullTimer--;
			if (skullTimer == 1) {
				isSkulled = false;
				attackedPlayers.clear();
				headIconPk = -1;
				skullTimer = -1;
				getPA().requestUpdates();
			}
		}

		if (isDead && respawnTimer == -6) {
			getPA().applyDead();
		}

		if (respawnTimer == 7) {
			respawnTimer = -6;
			getPA().giveLife();
		} else if (respawnTimer == 12) {
			respawnTimer--;
			startAnimation(0x900);
			poisonDamage = -1;
		}

		if (respawnTimer > -6) {
			respawnTimer--;
		}
		if (freezeTimer > -6) {
			freezeTimer--;
			if (frozenBy > 0) {
				if (Server.playerHandler.players[frozenBy] == null) {
					freezeTimer = -1;
					frozenBy = -1;
				} else if (!goodDistance(absX, absY, Server.playerHandler.players[frozenBy].absX,
						Server.playerHandler.players[frozenBy].absY, 20)) {
					freezeTimer = -1;
					frozenBy = -1;
				}
			}
		}

		if (hitDelay > 0) {
			hitDelay--;
		}

		if (teleTimer > 0) {
			teleTimer--;
			if (!isDead) {
				if (teleTimer == 1 && newLocation > 0) {
					teleTimer = 0;
					getPA().changeLocation();
				}
				if (teleTimer == 5) {
					teleTimer--;
					getPA().processTeleport();
				}
				if (teleTimer == 9 && teleGfx > 0) {
					teleTimer--;
					gfx100(teleGfx);
				}
			} else {
				teleTimer = 0;
			}
		}

		if (hitDelay == 1) {
			if (oldNpcIndex > 0) {
				getCombat().appendDamageNPC(oldNpcIndex, combatStyle, 1);
				if (doubleHit) {
					Experience.calculateDamageNPC(this, oldNpcIndex, combatStyle);
					getCombat().appendDamageNPC(oldNpcIndex, combatStyle, 2);
				}
			} else if (oldPlayerIndex > 0) {
				getCombat().appendDamage(oldPlayerIndex, combatStyle, 1);
			}
		}

		if (attackTimer > 0) {
			attackTimer--;
		}

		if (attackTimer == 1) {
			if (npcIndex > 0 && clickNpcType == 0) {
				getCombat().attackNpc(npcIndex);
			}
			if (playerIndex > 0) {
				getCombat().attackPlayer(playerIndex);
			}
		} else if (attackTimer <= 0 && (npcIndex > 0 || playerIndex > 0)) {
			if (npcIndex > 0) {
				attackTimer = 0;
				getCombat().attackNpc(npcIndex);
			} else if (playerIndex > 0) {
				attackTimer = 0;
				getCombat().attackPlayer(playerIndex);
			}
		}

		if (inTrade && tradeResetNeeded) {
			Client o = (Client) Server.playerHandler.players[tradeWith];
			if (o != null) {
				if (o.tradeResetNeeded) {
					getTradeAndDuel().resetTrade();
					o.getTradeAndDuel().resetTrade();
				}
			}
		}
	}

	public void setCurrentTask(Future<?> task) {
		currentTask = task;
	}

	public Future<?> getCurrentTask() {
		return currentTask;
	}

	public synchronized Stream getInStream() {
		return inStream;
	}

	public synchronized int getPacketType() {
		return packetType;
	}

	public synchronized int getPacketSize() {
		return packetSize;
	}

	public synchronized Stream getOutStream() {
		return outStream;
	}

	public ItemAssistant getItems() {
		return itemAssistant;
	}

	public PlayerAssistant getPA() {
		return playerAssistant;
	}

	public DialogueHandler getDH() {
		return dialogueHandler;
	}

	public ShopAssistant getShops() {
		return shopAssistant;
	}

	public TradeAndDuel getTradeAndDuel() {
		return tradeAndDuel;
	}

	public CombatAssistant getCombat() {
		return combatAssistant;
	}

	public ActionHandler getActions() {
		return actionHandler;
	}

	public PlayerKilling getKill() {
		return playerKilling;
	}

	public Channel getSession() {
		return session;
	}

	public Potions getPotions() {
		return potions;
	}

	public PotionMixing getPotMixing() {
		return potionMixing;
	}

	public Food getFood() {
		return food;
	}

	private BuryBones buryBones = new BuryBones(this);

	public BuryBones getBones() {
		return buryBones;
	}

	private boolean isBusy = false;
	private boolean isBusyHP = false;
	public boolean isBusyFollow = false;

	public boolean checkBusy() {
		/*
		 * if (getCombat().isFighting()) { return true; }
		 */
		if (isBusy) {
			// actionAssistant.sendMessage("You are too busy to do that.");
		}
		return isBusy;
	}

	public boolean checkBusyHP() {
		return isBusyHP;
	}

	public boolean checkBusyFollow() {
		return isBusyFollow;
	}

	public void setBusy(boolean isBusy) {
		this.isBusy = isBusy;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public void setBusyFollow(boolean isBusyFollow) {
		this.isBusyFollow = isBusyFollow;
	}

	public void setBusyHP(boolean isBusyHP) {
		this.isBusyHP = isBusyHP;
	}

	public boolean isBusyHP() {
		return isBusyHP;
	}

	public boolean isBusyFollow() {
		return isBusyFollow;
	}

	private boolean canWalk = true;
	public int emotesPerformed = 0;

	public boolean canWalk() {
		return canWalk;
	}

	private EmoteHandler emoteHandler = new EmoteHandler(this);

	public EmoteHandler getEmoteHandler() {
		return emoteHandler;
	}

	public void setCanWalk(boolean canWalk) {
		this.canWalk = canWalk;
	}

	public PlayerAssistant getPlayerAssistant() {
		return playerAssistant;
	}

	/**
	 * End of Skill Constructors
	 */

	public void queueMessage(Packet arg1) {
		synchronized (queuedPackets) {
			queuedPackets.add(arg1);
		}
	}

	public boolean processQueuedPackets() {
		synchronized (queuedPackets) {
			Packet p = null;
			while ((p = queuedPackets.poll()) != null) {
				inStream.currentOffset = 0;
				packetType = p.getOpcode();
				packetSize = p.getLength();
				inStream.buffer = p.getPayload().array();
				if (packetType > 0) {
					PacketHandler.processPacket(this, packetType, packetSize);
				}
			}
		}
		return true;
	}
	
	public Client getClient(String name) {
		name = name.toLowerCase();
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			if (validClient(i)) {
				Client client = getClient(i);
				if (client.playerName.toLowerCase().equalsIgnoreCase(name)) {
					return client;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("static-access")
	public Client getClient(int id) {
		return (Client) Server.playerHandler.players[id];
	}

	public boolean validClient(int id) {
		if (id < 0 || id > Constants.MAX_PLAYERS) {
			return false;
		}
		return validClient(getClient(id));
	}

	public boolean validClient(String name) {
		return validClient(getClient(name));
	}

	public boolean validClient(Client client) {
		return (client != null && !client.disconnected);
	}

	public boolean validNpc(int index) {
		if (index < 0 || index >= Constants.MAX_NPCS) {
			return false;
		}
		NPC n = getNpc(index);
		if (n != null) {
			return true;
		}
		return false;
	}

	@SuppressWarnings("static-access")
	public NPC getNpc(int index) {
		return ((NPC) Server.npcHandler.npcs[index]);
	}

	public void yell(String s) {
		for (int i = 0; i < Constants.MAX_PLAYERS; i++) {
			if (validClient(i)) {
				getClient(i).sendMessage(s);
			}
		}
	}

	public void puzzleBarrow(Client c) {
		getPA().sendFrame246(4545, 250, 6833);
		getPA().sendFrame126("1.", 4553);
		getPA().sendFrame246(4546, 250, 6832);
		getPA().sendFrame126("2.", 4554);
		getPA().sendFrame246(4547, 250, 6830);
		getPA().sendFrame126("3.", 4555);
		getPA().sendFrame246(4548, 250, 6829);
		getPA().sendFrame126("4.", 4556);
		getPA().sendFrame246(4550, 250, 3454);
		getPA().sendFrame246(4551, 250, 8746);
		getPA().sendFrame246(4552, 250, 6830);
		getPA().showInterface(4543);
	}

	public void correctCoordinates() {
		if (inPcGame()) {
			getPA().movePlayer(2657, 2639, 0);
		}
		if (inFightCaves()) {
			getPA().movePlayer(absX, absY, playerId * 4);
			sendMessage("Your wave will start in 10 seconds.");
			CycleEventHandler.getSingleton().addEvent(this, new CycleEvent() {
				@SuppressWarnings("static-access")
				@Override
				public void execute(CycleEventContainer container) {
					Server.fightCaves.spawnNextWave((Client) Server.playerHandler.players[playerId]);
					container.stop();
				}

				@Override
				public void stop() {

				}
			}, 20);

		}

	}

	public void setIsSkilling(boolean isSkilling) {
		this.isSkilling = isSkilling;
	}

	public boolean getIsSkilling() {
		return isSkilling;
	}

	private boolean isSkilling;

}
