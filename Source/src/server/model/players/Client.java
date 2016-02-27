package server.model.players;

import java.util.LinkedList;
import java.util.Queue;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;

import server.Constants;
import server.Server;
import server.event.CycleEventHandler;
import server.model.EntityType;
import server.model.content.Membership;
import server.model.dialogues.DialogueHandler;
import server.model.dialogues.NpcDialogue;
import server.model.items.ItemAssistant;
import server.model.minigames.castle_wars.CastleWars;
import server.model.players.combat.CombatAssistant;
import server.model.players.combat.range.CannonCoords;
import server.model.players.combat.range.DwarfMultiCannon;
import server.model.players.packet.PacketHandler;
import server.model.players.skills.guilds.RangersGuild;
import server.model.shops.ShopAssistant;
import server.net.Packet;
import server.net.Packet.Type;
import server.net.login.SideBars;
import server.util.Misc;
import server.util.Plugin;
import server.util.Stream;
import server.world.sound.Sounds;

public class Client extends Player {

	public byte buffer[] = null;
	public Stream inStream = null, outStream = null;
	private Channel session;
	private ItemAssistant itemAssistant = new ItemAssistant(this);
	private ShopAssistant shopAssistant = new ShopAssistant(this);
	private TradeAndDuel tradeAndDuel = new TradeAndDuel(this);
	private PlayerAssistant playerAssistant = new PlayerAssistant(this);
	private CombatAssistant combatAssistant = new CombatAssistant(this);
	private ActionHandler actionHandler = new ActionHandler();
	private DialogueHandler dialogueHandler = new DialogueHandler(this);
	private Queue<Packet> queuedPackets = new LinkedList<Packet>();

	public Client(Channel s, int _playerId) {
		super(_playerId);
		this.session = s;
		outStream = new Stream(new byte[Constants.BUFFER_SIZE]);
		outStream.currentOffset = 0;

		inStream = new Stream(new byte[Constants.BUFFER_SIZE]);
		inStream.currentOffset = 0;
		buffer = new byte[Constants.BUFFER_SIZE];
	}

	public DwarfMultiCannon cannon = new DwarfMultiCannon(this);

	public DwarfMultiCannon getCannon() {
		return cannon;
	}

	public boolean shooting;

	private final CannonCoords cannonCoords = new CannonCoords(this);

	public CannonCoords getCannonCoords() {
		return cannonCoords;
	}

	private Membership members = new Membership();

	public Membership membership() {
		return members;
	}

	public boolean updateItems = false;
	
	private Attributes attributes = new Attributes();
	public Attributes getAttributes()
	{
		return attributes;
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

	public void destruct() {
		if (session == null)
			return;
		Server.panel.removeEntity(playerName);
		if (CastleWars.isInCwWait(this)) {
			CastleWars.leaveWaitingRoom(this);
		}
		if (CastleWars.isInCw(this)) {
			CastleWars.removePlayerFromCw(this);
		}
		if (inPits) {
			Server.fightPits.removePlayerFromPits(playerId);
		}
		Misc.println("[DESERIALIZED]: " + playerName);
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
		if (getOutStream() != null) {
			outStream.createFrameVarSize(253);
			outStream.writeString(s);
			outStream.endFrameVarSize();
		}

	}

	public void setSidebarInterface(int menuId, int form) {
		if (getOutStream() != null) {
			outStream.createFrame(71);
			outStream.writeWord(form);
			outStream.writeByteA(menuId);
		}

	}

	@SuppressWarnings("static-access")
	public void initialize() {
		outStream.createFrame(249);
		outStream.writeByteA(1); // 1 for members, zero for free
		outStream.writeWordBigEndianA(playerId);
		for (int player = 0; player < Server.playerHandler.players.length; player++) {
			if (player == playerId)
				continue;
			if (Server.playerHandler.players[player] != null) {
				if (Server.playerHandler.players[player].playerName.equalsIgnoreCase(playerName))
					disconnected = true;
			}
		}
		for (int skill = 0; skill < 21; skill++) {
			getPA().setSkillLevel(skill, playerLevel[skill], playerXP[skill]);
			getPA().refreshSkill(skill);
		}
//		IntStream.range(0, 25).forEach(skill -> //lambda expression
//		{
//			getPA().setSkillLevel(skill, playerLevel[skill], playerXP[skill]);
//			getPA().refreshSkill(skill);
//		});
		for (int prayerId = 0; prayerId < PRAYER.length; prayerId++) {
			prayerActive[prayerId] = false;
			getPA().sendFrame36(PRAYER_GLOW[prayerId], 0);
		}

		isRunning2 = !isRunning2;
		int off = isRunning2 == false ? 0 : 1;
		getPA().sendFrame36(173, off);

		runEnergyTime = System.currentTimeMillis();
		getPA().sendFrame126(runEnergy + "%", 149);
		getPA().sendFrame126("10m kg", 184);
		getPA().sendFrame126("QP: " + questPoints, 3985);
		
		// getPA().sendFrame36(43, fightMode-1); // ??
		getPA().sendFrame36(172, autoRet == 1 ? 0 : 1);
		getPA().sendFrame36(166, 3); //brightness
		getPA().sendFrame36(108, 0);// resets autocast button
		getPA().sendFrame107(); // reset screen
		getPA().setChatOptions(0, 0, 0); // reset private messaging options

		for (SideBars sb : SideBars.values()) {
			setSidebarInterface(sb.getSideBar(), sb.getInterfaceId());
		}
		getPA().showOption(4, 0, "Trade With", 3);
		getPA().showOption(5, 0, "Follow", 4);
		getItems().resetItems(3214); // inventory updating

		// this string is used in the Quest tab, as well as Equipment tab
		// getPA().sendFrame126("@cya@Quest Name?", 1677);
		Misc.println("[SERIALIZED]: " + playerName);
		setSidebarInterface(SideBars.REGULAR_MAGIC_TAB.getSideBar(), playerMagicBook == 0 ? SideBars.REGULAR_MAGIC_TAB.getInterfaceId() : SideBars.ANCIENT_MAGIC_TAB.getInterfaceId());
		if (addStarter) {
			Starter.newPlayer(this);
		}
		Plugin.execute("login", this);
		Plugin.execute("bonues", this);
		Plugin.execute("logintext", this);
		Plugin.execute("musictab", this);
		Plugin.execute("panelupdate", this);
		Plugin.execute("resetfollowers", this);
		Plugin.execute("weaponstyles", this);
		Plugin.execute("pmaccess", this);
		
		update();
	}

	public void update() {
		handler.updatePlayer(this, outStream);
		handler.updateNPC(this, outStream);
		flushOutStream();
	}
	private RangersGuild rangersGuild = new RangersGuild(this);

	public RangersGuild getRG() {
		return rangersGuild;
	}
	public void logout() {
		if (System.currentTimeMillis() - logoutDelay > 10000) {
			outStream.createFrame(109);
			CycleEventHandler.getSingleton().stopEvents(this);
			properLogout = true;
		} else {
			sendMessage("You must wait a few seconds from being out of combat to logout.");
		}
	}

	int points;

	public void addPoints(int amt) {
		if (points + amt < 25000) {
			points += amt;
		} else {
			points = 25000;
		}
		sendMessage("You receive " + amt + " points and now have a total of " + points + " openPI points.");
	}

	public int packetSize = 0, packetType = -1;

	@SuppressWarnings("static-access")
	public void process() {

	
		if (System.currentTimeMillis() - specDelay > Constants.INCREASE_SPECIAL_AMOUNT) {
			specDelay = System.currentTimeMillis();
			if (specAmount < 10) {
				specAmount += .5;
				if (specAmount > 10)
					specAmount = 10;
				getItems().addSpecialBar(playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]);
			}
		}
		if ((runEnergy < 100 && ((!isRunning && !isRunning2) || !isMoving))
				&& System.currentTimeMillis() - runEnergyTime > Constants.RUN_ENERGY_GAIN) {
			runEnergyTime = System.currentTimeMillis();
			runEnergy++;
			getPA().sendFrame126(runEnergy + "%", 149);
		}
		if (followId > EntityType.PLAYER.getEntityValue()) {
			getPA().followPlayer();
		} else if (followId2 > EntityType.NPC.getEntityValue()) {
			getPA().followNpc();
		}

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
		} else {
			getPA().sendFrame126(" ", 199);
		}
		if (inDuelArena()) {
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
		}
		if (CastleWars.isInCw(this) || inPits) {
			getPA().showOption(3, 0, "Attack", 1);
		} else if (!inDuelArena() && !CastleWars.isInCw(this) && !CastleWars.isInCwWait(this) && !inWild() && !getPA().inPitsWait() && !inBarrows()) {
			getPA().walkableInterface(-1);
			getPA().sendFrame99(0);
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
				getCombat().delayedHit(oldNpcIndex);
			}
			if (oldPlayerIndex > 0) {
				getCombat().playerDelayedHit(oldPlayerIndex);
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
		Plugin.execute("process", this);
	}
	private static Sounds sounds = new Sounds(null);
	
	public Sounds getSound()
	{
		return sounds;
	}

	public Stream getInStream() {
		return inStream;
	}

	public int getPacketType() {
		return packetType;
	}

	public int getPacketSize() {
		return packetSize;
	}

	public Stream getOutStream() {
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

	public Channel getSession() {
		return session;
	}

	public PlayerAssistant getPlayerAssistant() {
		return playerAssistant;
	}
	
	public void queueMessage(Packet packet) {
		synchronized (queuedPackets) {
			queuedPackets.add(packet);
		}
	}

	public boolean processQueuedPackets() {
		synchronized (queuedPackets) {
			Packet packet = null;
			while ((packet = queuedPackets.poll()) != null) {
				inStream.currentOffset = 0;
				packetType = packet.getOpcode();
				packetSize = packet.getLength();
				inStream.buffer = packet.getPayload().array();
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

	public boolean validClient(Client client) {
		return (client != null && !client.disconnected);
	}
	private NpcDialogue dialogue = null;
	public NpcDialogue getDialogue() {
		return dialogue;
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
	public int nextChat = 0;
	public boolean zombieSpawned;
	public boolean treeSpawned;
	public boolean golemSpawned;
	public boolean trollSpawned;
}