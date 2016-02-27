package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import server.event.CycleEventHandler;
import server.event.Task;
import server.event.TaskScheduler;
import server.model.minigames.castle_wars.CastleWars;
import server.model.minigames.pest_control.PestControl;
import server.model.minigames.tzhaar.FightCaves;
import server.model.minigames.tzhaar.FightPits;
import server.model.npcs.NPCHandler;
import server.model.npcs.drops.NPCDrops;
import server.model.objects.Doors;
import server.model.objects.DoubleDoors;
import server.model.players.PlayerHandler;
import server.model.shops.ShopHandler;
import server.net.PipelineFactory;
import server.panel.ControlPanel;
import server.util.Logger;
import server.util.Plugin;
import server.world.ItemHandler;
import server.world.ObjectHandler;
import server.world.ObjectManager;
import server.world.PlayerManager;

/**
 */
public class Server {

	/**
	 * Calls to manage the players on the server.
	 */
	public static PlayerManager playerManager = new PlayerManager();

	/**
	 * Calls the rate in which an event cycles.
	 */
	public static final int cycleRate;

	/**
	 * Server updating.
	 */
	public static boolean UpdateServer = false;

	/**
	 * Forced shutdowns.
	 */
	public static boolean shutdownServer = false;

	public static ControlPanel panel = new ControlPanel(true); // false if you
																// want it off

	/**
	 * Used to identify the server port.
	 */
	public static int serverlistenerPort;

	/**
	 * Calls the usage of player items.
	 */
	public static ItemHandler itemHandler = new ItemHandler();

	/**
	 * Handles logged in players.
	 */
	public static PlayerHandler playerHandler = new PlayerHandler();

	/**
	 * Handles global NPCs.
	 */
	public static NPCHandler npcHandler = new NPCHandler();

	/**
	 * Handles global shops.
	 */
	public static ShopHandler shopHandler = new ShopHandler();

	/**
	 * Handles global objects.
	 */
	public static ObjectHandler objectHandler = new ObjectHandler();
	public static ObjectManager objectManager = new ObjectManager();

	/**
	 * Handles the castlewars minigame.
	 */
	public static CastleWars castleWars = new CastleWars();

	/**
	 * Handles the fightpits minigame.
	 */
	public static FightPits fightPits = new FightPits();

	/**
	 * Handles the pestcontrol minigame.
	 */
	public static PestControl pestControl = new PestControl();

	/**
	 * Handles the fightcaves minigames.
	 */
	public static FightCaves fightCaves = new FightCaves();

	/**
	 * Handles the task scheduler.
	 */
	private static final TaskScheduler scheduler = new TaskScheduler();

	/**
	 * Gets the task scheduler.
	 */
	public static TaskScheduler getTaskScheduler() {
		return scheduler;
	}

	static {
		serverlistenerPort = 43594;
		
		cycleRate = 600;
		shutdownServer = false;
	}

	/**
	 * Starts the server.
	 */
	public static void main(java.lang.String args[]) throws NullPointerException, IOException {

		long startTime = System.currentTimeMillis();
		System.setOut(new Logger(System.out));
		System.setErr(new Logger(System.err));
		Plugin.load();
	
		NPCDrops.init();
		bind();

		playerManager = PlayerManager.getSingleton();
		playerManager.setupRegionPlayers();

		Doors.getSingleton().load();
		DoubleDoors.getSingleton().load();
		Connection.initialize();
		/**
		 * Successfully loaded the server.
		 */
		long endTime = System.currentTimeMillis();
		long elapsed = endTime - startTime;
		System.out.println("Server started up in " + elapsed + " ms");

		/**
		 * Main server tick.
		 */
		scheduler.schedule(new Task() {
			@Override
			protected void execute() {
				itemHandler.process();
				playerHandler.process();
				npcHandler.process();
				shopHandler.process();
				CycleEventHandler.getSingleton().process();
				objectManager.process();
			}
		});
		
		/**
		 * Minigame tick
		 */
		scheduler.schedule(new Task() {
			@Override
			protected void execute() {
				CastleWars.process();
				fightPits.process();
				pestControl.process();
			}
		});
	}

	/**
	 * Logging execution.
	 */
	public static boolean playerExecuted = false;

	/**
	 * Gets the Player manager.
	 */
	public static PlayerManager getPlayerManager() {
		return playerManager;
	}

	/**
	 * Gets the Object manager.
	 */
	public static ObjectManager getObjectManager() {
		return objectManager;
	}

	/**
	 * Java connection. Ports.
	 */
	private static void bind() {
		ServerBootstrap serverBootstrap = new ServerBootstrap( new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
		serverBootstrap.setPipelineFactory(new PipelineFactory(new HashedWheelTimer()));
		serverBootstrap.bind(new InetSocketAddress(serverlistenerPort));
	}
}