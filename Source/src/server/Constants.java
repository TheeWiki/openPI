package server;

import server.util.Misc;

public class Constants {

	/**
	 * Will there be a debug printout for whichever uses this boolean?
	 */
	public static final boolean SERVER_DEBUG = false;

	/**
	 * Server tick value
	 */
	public static int TICK = 600;

	/**
	 * Server name
	 */
	public static String SERVER_NAME = "openPI";

	/**
	 * Login message
	 */
	public static String WELCOME_MESSAGE = "Welcome to " + SERVER_NAME
			+ ", open source at http://theewiki.github.io/openPI/";

	/**
	 * Item limit
	 */
	public static final int ITEM_LIMIT = 16000;

	/**
	 * Maximum item amount
	 */
	public static final int MAXITEM_AMOUNT = Integer.MAX_VALUE;

	/**
	 * Maximum bank items allowed to be stored
	 */
	public static final int BANK_SIZE = 352;

	/**
	 * Max players allowed on server (world 1)
	 */
	public static final int MAX_PLAYERS = 1024;

	/**
	 * TODO:
	 */
	public static final boolean WORLD_LIST_FIX = false;

	/**
	 * Items that can be sold
	 */
	public static final int[] ITEM_SELLABLE = { 3842, 3844, 3840, 8844, 8845, 8846, 8847, 8848, 8849, 8850, 10551, 6570,
			7462, 7461, 7460, 7459, 7458, 7457, 7456, 7455, 7454, 9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802, 9808,
			9784, 9799, 9805, 9781, 9796, 9793, 9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758, 9761,
			9764, 9803, 9809, 9785, 9800, 9806, 9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747, 9753, 9750,
			9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810, 9765,
			8839, 8840, 8842, 11663, 11664, 11665, 10499, 995 };

	/**
	 * Items that can be traded
	 */
	public static final int[] ITEM_TRADEABLE = { 3842, 3844, 3840, 8844, 8845, 8846, 8847, 8848, 8849, 8850, 10551,
			6570, 7462, 7461, 7460, 7459, 7458, 7457, 7456, 7455, 7454, 9748, 9754, 9751, 9769, 9757, 9760, 9763, 9802,
			9808, 9784, 9799, 9805, 9781, 9796, 9793, 9775, 9772, 9778, 9787, 9811, 9766, 9749, 9755, 9752, 9770, 9758,
			9761, 9764, 9803, 9809, 9785, 9800, 9806, 9782, 9797, 9794, 9776, 9773, 9779, 9788, 9812, 9767, 9747, 9753,
			9750, 9768, 9756, 9759, 9762, 9801, 9807, 9783, 9798, 9804, 9780, 9795, 9792, 9774, 9771, 9777, 9786, 9810,
			9765, 8839, 8840, 8842, 11663, 11664, 11665, 10499, };

	/**
	 * Items that can't be dropped
	 */
	public static final int[] UNDROPPABLE_ITEMS = { 1057 };

	/**
	 * Server 'fun' weapons (example: yo-yo)
	 */
	public static final int[] FUN_WEAPONS = { 2460, 2461, 2462, 2463, 2464, 2465, 2466, 2467, 2468, 2469, 2470, 2471,
			2471, 2473, 2474, 2475, 2476, 2477 };

	/**
	 * Administrators can trade?
	 */
	public static boolean ADMIN_CAN_TRADE = false;

	/**
	 * Administrators can sell items in shops?
	 */
	public static boolean ADMIN_CAN_SELL_ITEMS = false;

	/**
	 * ) Administrators can drop items ?
	 */
	public static boolean ADMIN_DROP_ITEMS = false;

	/**
	 * Start location x, y
	 */
	public static final int START_LOCATION_X = 2611 + Misc.random(1);
	public static final int START_LOCATION_Y = 3090 + Misc.random(1);

	/**
	 * Respawn location x, y
	 */
	public static final int RESPAWN_X = START_LOCATION_X + Misc.random(1);
	public static final int RESPAWN_Y = START_LOCATION_Y + Misc.random(1);

	/**
	 * Dueling respawn x,y
	 */
	public static final int DUELING_RESPAWN_X = 3362;
	public static final int DUELING_RESPAWN_Y = 3263;

	/**
	 * Random tile respawn from duel
	 */
	public static final int RANDOM_DUELING_RESPAWN = 5;

	/**
	 * Wilderness level required to teleport minimum
	 */
	public static final int NO_TELEPORT_WILD_LEVEL = 21;

	/**
	 * Skull timer
	 */
	public static final int SKULL_TIMER = 1200;

	/**
	 * Are the single and multi zones
	 */
	public static final boolean SINGLE_AND_MULTI_ZONES = true;

	/**
	 * Is there a combat level check on pvp
	 */
	public static final boolean COMBAT_LEVEL_DIFFERENCE = true;

	/**
	 * Items have requirements to wield ?
	 */
	public static final boolean itemRequirements = true;

	/**
	 * Special increase delay
	 */
	public static final int INCREASE_SPECIAL_AMOUNT = 17500;

	/**
	 * Prayer points required
	 */
	public static final boolean PRAYER_POINTS_REQUIRED = true;
	/**
	 * Prayer level required
	 */
	public static final boolean PRAYER_LEVEL_REQUIRED = true;

	/**
	 * Magic level required
	 */
	public static final boolean MAGIC_LEVEL_REQUIRED = true;

	/**
	 * Spell delay before using agian
	 */
	public static final int GOD_SPELL_CHARGE = 300_000;

	/**
	 * Spells require runes to cast (besides teleporting)
	 */
	public static final boolean RUNES_REQUIRED = true;

	/**
	 * Checks to make sure user is using correct arrows?
	 */
	public static final boolean CORRECT_ARROWS = true;

	/**
	 * Crystal bow degrades?
	 */
	public static final boolean CRYSTAL_BOW_DEGRADES = true;

	/**
	 * Save timer delay. Saves every four minutes.
	 */
	public static final int SAVE_TIMER = 240; // Saves every four minutes.

	/**
	 * 5x5 square, NPCs would be able to walk 25 squares around.
	 */
	public static final int NPC_RANDOM_WALK_DISTANCE = 5;

	/**
	 * Follow distance limit (15 tiles)
	 */
	public static final int NPC_FOLLOW_DISTANCE = 15;

	/**
	 * Undead npc ids
	 */
	public static final int[] UNDEAD_NPCS = { 90, 91, 92, 93, 94, 103, 104, 73, 74, 75, 76, 77 };
	/**
	 * Buffer size.
	 */
	public static final int BUFFER_SIZE = 512;

	/**
	 * Drop ratios
	 */
	public static final int DROP_RATE = 1;

	/**
	 * Server control panel stuff
	 */
	public static boolean LOCK_EXPERIENCE = false;
	public static boolean MINI_GAMES = true;
	public static String LOGOUT_MESSAGE = "Click here to logout!";
	public static String DEATH_MESSAGE = "Oh dear you are dead!";
	public static boolean DOUBLE_EXP = true;

	/**
	 * Sounds enabled? (Doesn't work, more of a TODO)
	 */
	public static final boolean SOUND = true;

	/**
	 * number of milliseconds before run energy goes up 1
	 */
	public static final int RUN_ENERGY_GAIN = 2250;

	/**
	 * number of squares player runs before energy decreases
	 */
	public static final int RUN_SQUARE_DECREASE = 2;

	/**
	 * Variables that control the data flow of the {@link Plugin} system.
	 */
	public static final String SCRIPT_DIRECTORY = "./Data/plugins/";
	public static final String SCRIPT_FILE_EXTENSION = ".py";
	public static final boolean PRINT_PLUGIN_DIRECTORIES = false;
	/**
	 * Teleport spells.
	 **/
	/*
	 * Modern spells
	 */
	public static final int VARROCK_X = 3210;
	public static final int VARROCK_Y = 3424;
	public static final String VARROCK = "";

	public static final int LUMBY_X = 3222;
	public static final int LUMBY_Y = 3218;
	public static final String LUMBY = "";

	public static final int FALADOR_X = 2964;
	public static final int FALADOR_Y = 3378;
	public static final String FALADOR = "";

	public static final int CAMELOT_X = 2757;
	public static final int CAMELOT_Y = 3477;
	public static final String CAMELOT = "";

	public static final int ARDOUGNE_X = 2662;
	public static final int ARDOUGNE_Y = 3305;
	public static final String ARDOUGNE = "";

	public static final int WATCHTOWER_X = 3087;
	public static final int WATCHTOWER_Y = 3500;
	public static final String WATCHTOWER = "";

	public static final int TROLLHEIM_X = 3243;
	public static final int TROLLHEIM_Y = 3513;
	public static final String TROLLHEIM = "";

	/*
	 * Ancient spells
	 */
	public static final int PADDEWWA_X = 3098;
	public static final int PADDEWWA_Y = 9884;

	public static final int SENNTISTEN_X = 3322;
	public static final int SENNTISTEN_Y = 3336;

	public static final int KHARYRLL_X = 3492;
	public static final int KHARYRLL_Y = 3471;

	public static final int LASSAR_X = 3006;
	public static final int LASSAR_Y = 3471;

	public static final int DAREEYAK_X = 3161;
	public static final int DAREEYAK_Y = 3671;

	public static final int CARRALLANGAR_X = 3156;
	public static final int CARRALLANGAR_Y = 3666;

	public static final int ANNAKARL_X = 3288;
	public static final int ANNAKARL_Y = 3886;

	public static final int GHORROCK_X = 2977;
	public static final int GHORROCK_Y = 3873;

}