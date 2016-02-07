package server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.text.DecimalFormat;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.util.HashedWheelTimer;

import server.net.PipelineFactory;

import server.util.log.Logger;

import server.event.CycleEventHandler;
import server.event.Task;
import server.event.TaskScheduler;
import server.world.StillGraphicsManager;
import server.world.PlayerManager;
import server.jagcached.FileServer;
import server.model.objects.DoubleDoors;
import server.model.npcs.NPCHandler;
import server.model.players.PlayerHandler;
import server.model.objects.Doors;
import server.model.minigames.*;
import server.world.ItemHandler;
import server.world.ObjectHandler;
import server.world.ObjectManager;
import server.world.ShopHandler;
import server.world.ClanChatHandler;

/**
 */
public class Server {
	
	
	/**
	 *Calls to manage the players on the server.
	 */
	public static PlayerManager playerManager = null;
	private static StillGraphicsManager stillGraphicsManager = null;
	
	
	/**
	 * Sleep mode of the server.
	 */
	public static boolean sleeping;
	
	
	/**
	 * Calls the rate in which an event cycles. 
	 */
	public static final int cycleRate;
	
	
	/**
	 * Server updating.
	 */
	public static boolean UpdateServer = false;
	
	
	/**
	 * Calls in which the server was last saved.
	 */
	public static long lastMassSave = System.currentTimeMillis();
	
	
	/**
	 * Calls the usage of CycledEvents. 
	 */
	private static long cycleTime, cycles, totalCycleTime, sleepTime;
	
	
	/**
	 * Used for debugging the server.
	 */
	private static DecimalFormat debugPercentFormat;
	
	
	/**
	 * Forced shutdowns.
	 */
	public static boolean shutdownServer = false;		
	public static boolean shutdownClientHandler;	
	
	
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
	 * Handles the clan chat.
	 */
	public static ClanChatHandler clanChat = new ClanChatHandler();
        
	
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
		if(!Config.SERVER_DEBUG) {
			serverlistenerPort = 43594;
		} else {
			serverlistenerPort = 43594;
		}
		cycleRate = 600;
		shutdownServer = false;
		sleepTime = 0;
		debugPercentFormat = new DecimalFormat("0.0#%");
	}
	
	
	/**
	 * Starts the server.
	 */    
	public static void main(java.lang.String args[]) throws NullPointerException, IOException {

		long startTime = System.currentTimeMillis();
		System.setOut(new Logger(System.out));
		System.setErr(new Logger(System.err));
		System.out.println("Launching fileserver..");
		

		try {
			new FileServer().start();
			new server.fileserver.FileServer().bind();
		} catch(Exception e) {
			e.printStackTrace();
		}
		
	
		bind();
                
                
		playerManager = PlayerManager.getSingleton();
		playerManager.setupRegionPlayers();
		stillGraphicsManager = new StillGraphicsManager();

		
	
		Doors.getSingleton().load();
		DoubleDoors.getSingleton().load();
		Connection.initialize();

		
		/**
		 *Successfully loaded the server.
		 */
                long endTime = System.currentTimeMillis();
                long elapsed = endTime-startTime;
                System.out.println("Server started up in "+elapsed+" ms");
		
                
                
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
	 * Gets the sleep mode timer and puts the server into sleep mode.
	 */
	public static long getSleepTimer() {
		return sleepTime;
	}
	
	
	/**
	 * Gets the Graphics manager.
	 */
	public static StillGraphicsManager getStillGraphicsManager() {
		return stillGraphicsManager;
	}
	
	
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
 * Java connection.
 * Ports.
 */
        private static void bind() {
            ServerBootstrap serverBootstrap = new ServerBootstrap (new NioServerSocketChannelFactory(Executors.newCachedThreadPool(), Executors.newCachedThreadPool()));
            serverBootstrap.setPipelineFactory (new PipelineFactory(new HashedWheelTimer()));
            serverBootstrap.bind (new InetSocketAddress(serverlistenerPort));	
        }
	
}
