package server;

public class Constants {


	public static final boolean SERVER_DEBUG = false;

	public static final String SERVER_NAME = "Project Insanity.";

	public static final String WELCOME_MESSAGE = "Welcome to Project Insanity.";

	public static final String FORUMS = "";

	public static final int CLIENT_VERSION = 317;

	public static int MESSAGE_DELAY = 6000;

	public static final int ITEM_LIMIT = 16000; 

	public static final int MAXITEM_AMOUNT = Integer.MAX_VALUE;

	public static final int BANK_SIZE = 352;

	public static final int MAX_PLAYERS = 1024;

	public static final int CONNECTION_DELAY = 100; 

	public static final int IPS_ALLOWED = 3; 

	public static final boolean WORLD_LIST_FIX = false; 

	public static final int[] ITEM_SELLABLE 		=	{3842,3844,3840,8844,8845,8846,8847,8848,8849,8850,10551,6570,7462,7461,7460,7459,7458,7457,7456,7455,7454,
														9748,9754,9751,9769,9757,9760,9763,9802,9808,9784,9799,9805,9781,9796,9793,9775,9772,9778,9787,9811,9766,
														9749,9755,9752,9770,9758,9761,9764,9803,9809,9785,9800,9806,9782,9797,9794,9776,9773,9779,9788,9812,9767,
														9747,9753,9750,9768,9756,9759,9762,9801,9807,9783,9798,9804,9780,9795,9792,9774,9771,9777,9786,9810,9765,
														8839,8840,8842,11663,11664,11665,10499,995}; 
	

	public static final int[] ITEM_TRADEABLE 		= 	{3842,3844,3840,8844,8845,8846,8847,8848,8849,8850,10551,6570,7462,7461,7460,7459,7458,7457,7456,7455,7454,
														9748,9754,9751,9769,9757,9760,9763,9802,9808,9784,9799,9805,9781,9796,9793,9775,9772,9778,9787,9811,9766,
														9749,9755,9752,9770,9758,9761,9764,9803,9809,9785,9800,9806,9782,9797,9794,9776,9773,9779,9788,9812,9767,
														9747,9753,9750,9768,9756,9759,9762,9801,9807,9783,9798,9804,9780,9795,9792,9774,9771,9777,9786,9810,9765,
														8839,8840,8842,11663,11664,11665,10499,};
	

	public static final int[] UNDROPPABLE_ITEMS 	= 	{};

	public static final int[] FUN_WEAPONS	        =	{2460,2461,2462,2463,2464,2465,2466,2467,2468,2469,2470,2471,2471,2473,2474,2475,2476,2477};

	public static final boolean ADMIN_CAN_TRADE = false; 

	public static final boolean ADMIN_CAN_SELL_ITEMS = false; 

	public static final boolean ADMIN_DROP_ITEMS = false; 

	public static final int START_LOCATION_X = 3087;
	public static final int START_LOCATION_Y = 3502;

	public static final int RESPAWN_X = 3087; 
	public static final int RESPAWN_Y = 3502;

	public static final int DUELING_RESPAWN_X = 3362;
	public static final int DUELING_RESPAWN_Y = 3263;

	public static final int RANDOM_DUELING_RESPAWN = 5; 

	public static final int NO_TELEPORT_WILD_LEVEL = 20; 

	public static final int SKULL_TIMER = 1200; 

	public static final int TELEBLOCK_DELAY = 20000; 

	public static final boolean SINGLE_AND_MULTI_ZONES = true; 

	public static final boolean COMBAT_LEVEL_DIFFERENCE = true; 

	public static final boolean itemRequirements = true;

	public static final int MELEE_EXP_RATE = 100; 
	public static final int RANGE_EXP_RATE = 100;
	public static final int MAGIC_EXP_RATE = 100;

	public static final int SERVER_EXP_BONUS = 1;

	public static final int INCREASE_SPECIAL_AMOUNT = 17500;

	public static final boolean PRAYER_POINTS_REQUIRED = true;
	
	public static final boolean PRAYER_LEVEL_REQUIRED = true; 

	public static final boolean MAGIC_LEVEL_REQUIRED = true;

	public static final int GOD_SPELL_CHARGE = 300000;

	public static final boolean RUNES_REQUIRED = true;

	public static final boolean CORRECT_ARROWS = true; 

	public static final boolean CRYSTAL_BOW_DEGRADES = true; 

	public static final int SAVE_TIMER = 240; // Saves every four minutes.

	public static final int NPC_RANDOM_WALK_DISTANCE = 5; // 5x5 square, NPCs would be able to walk 25 squares around.

	public static final int NPC_FOLLOW_DISTANCE = 10; // 10 squares

	public static final int[] UNDEAD_NPCS = {90,91,92,93,94,103,104,73,74,75,76,77};

	
	
	/**
	 * Glory locations.
	 */
	public static final int EDGEVILLE_X = 3087;
	public static final int EDGEVILLE_Y = 3500;
	public static final String EDGEVILLE = "";
	public static final int AL_KHARID_X = 3293;
	public static final int AL_KHARID_Y = 3174;
	public static final String AL_KHARID = "";
	public static final int KARAMJA_X = 3087;
	public static final int KARAMJA_Y = 3500;
	public static final String KARAMJA = "";
	public static final int MAGEBANK_X = 2538;
	public static final int MAGEBANK_Y = 4716;
	public static final String MAGEBANK = "";
	
	
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
    // 
	
	
	/**
	 * Timeout time.
	 */
	public static final int TIMEOUT = 20;
	
	
	/**
	 * Cycle time.
	 */
	public static final int CYCLE_TIME = 600;
	
	
	/**
	 * Buffer size.
	 */
	public static final int BUFFER_SIZE = 512;
	
	
	/**
	 * Slayer Variables.
	 */
	public static final int[][] SLAYER_TASKS = {{1,87,90,4,5}, //Low tasks
												{6,7,8,9,10}, //Medium tasks
												{11,12,13,14,15}, //High tasks
												{1,1,15,20,25}, //Low requirements
												{30,35,40,45,50}, //Medium requirements
												{60,75,80,85,90}}; //High requirements
	
	
	/**
	* Skill experience multipliers.
	*/	
	public static final int WOODCUTTING_EXPERIENCE = 40;
	public static final int MINING_EXPERIENCE = 40;
	public static final int SMITHING_EXPERIENCE = 40;
	public static final int FARMING_EXPERIENCE = 40;
	public static final int FIREMAKING_EXPERIENCE = 50;
	public static final int HERBLORE_EXPERIENCE = 40;
	public static final int FISHING_EXPERIENCE = 40;
	public static final int AGILITY_EXPERIENCE = 40;
	public static final int PRAYER_EXPERIENCE = 40;
	public static final int RUNECRAFTING_EXPERIENCE = 40;
	public static final int CRAFTING_EXPERIENCE = 40;
	public static final int THIEVING_EXPERIENCE = 40;
	public static final int SLAYER_EXPERIENCE = 50;
	public static final int COOKING_EXPERIENCE = 40;
	public static final int FLETCHING_EXPERIENCE = 40;

	public static final int DROP_RATE = 1;
}
