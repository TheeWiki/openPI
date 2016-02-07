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
	 * Should the player be able to rotate the camera around by holding down the scroll wheel down?
	 * TODO: if false let the user click around, currently does nothing now
	 */
	public static boolean ALLOW_MOUSEWHEEL_MOVEMENT = true;
}