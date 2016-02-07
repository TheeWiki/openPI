/**
 *  This class contains all the toggleable settings for the client
 *  Each boolean giving the description of what their toggleable function does in-game
 * @author Dennis
 *
 */
public class Constants 
{
	
	/**
	 * Declares the official client name of the your server
	 */
	public static final String CLIENT_NAME = "Mopar";
	
	/**
	 * Declares the official connection socket port for the client & server to respond to
	 */
	public static final int CLIENT_PORT = 43594;
	
	/**
	 * Declares the client connection IP to to be connected to
	 * "localhost" is just another way of putting "127.0.0.1" as an integer statement
	 */
	public static final String CLIENT_IP = "localhost";
	
	/**
	 * What is the cache we're calling for our client?
	 */
	public static final String CACHE_NAME = "/cachev7/";
	
	/**
	 * When moving the camera in the Left or Right direction long enough the camera will give an addition movement 
	 * creating a simple gliding sequence
	 */
	public static boolean CAMERA_GLIDE = true;
	
	/**
	 * Will the entities hit x10 damage display on each other in combat?
	 * Everything becomes x10, including the hp/hp orb
	 */
	public static boolean X10_DAMAGE = true;
	
	/**
	 * When in combat will the entities be giving out a 562 based hitmarks on each other?
	 */
	public static boolean HITMARKS_562 = true;
	
	/**
	 * Will the entities have 562 revision hit bars above their heads?
	 */
	public static boolean HIT_BAR_562 = true;
	
	/**
	 * Will the Run Energy orb display contain any values? (if false sets value to 1)
	 */
	public static boolean DISABLE_RUN_ENERGY = true;
	
	/**
	 * Declares what the npc revision we'll be loading (only change if you update your npcs)
	 * Make sure your server npc bits are the same (PI -> Player.java -> addnewnpc)
	 * 12 = 317/377
	 * 14 = 474+
	 * 16 = 600+
	 */
	public static final int NPC_BITS = 12;

	/**
	 * Will the minimap display have more detail to it?
	 */
	public static final boolean HD_MINIMAP = true;
	
	/**
	 * Are we displaying all of our orbs on top the gameframe positioned along the left side
	 * of the minimap?
	 */
	public static boolean LOAD_ALL_ORBS = true;
	
	/**
	 * Should the player be able to rotate the camera around by holding down the scroll wheel down?
	 * TODO: if false let the user click around, currently does nothing now
	 */
	public static boolean ALLOW_MOUSEWHEEL_MOVEMENT = true;
	
	/**
	 * Shoulod there be a shadow displayed under the character?
	 * TODO: fix shadows not appearing immediately on first login (equip an item then shadows will appear, works after relog as well)
	 */
	public static boolean DISPLAY_PLAYER_SHADOWS = true;
}