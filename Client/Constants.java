/**
 * This class contains all the toggleable settings for the client Each boolean
 * giving the description of what their toggleable function does in-game
 * 
 * @author Dennis
 *
 */
public class Constants {

	/**
	 * Declares the official client name of the your server
	 */
	public static final String CLIENT_NAME = "openPI";

	/**
	 * Declares what the npc revision we'll be loading (only change if you
	 * update your npcs) Make sure your server npc bits are the same (PI ->
	 * Player.java -> addnewnpc) 
	 * 12 = 317/377 
	 * 14 = 474+ 
	 * 16 = 600+
	 */
	public static final int NPC_BITS = 12;
	
}