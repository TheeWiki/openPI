package server;

public class Config {


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

	public static final int[][] NPC_DROPS = {
		// Men
			{1,526,1,0}, {2,526,1,0}, {3,526,1,0},
		// Bandos Boss
			{2550,995,200000,7}, {2550,11704,1,23}, {2550,11710,1,17}, {2550,11712,1,17}, 
			{2550,11714,1,15}, {2550,11724,1,23}, {2550,11726,1,23}, {2550,11728,1,17},
			{2250,3024,1, 0}, 
		// Armadyl Boss
			{2558,995,200000,7}, {2558,11710,1,17}, {2558,11712,1,17}, {2558,11714,1,17},
			{2558,11702,1,23}, {2558,11718,1,17}, {2558,11720,1,17}, {2558,11722,1,17},
			{2558,3024,1,0},
		// Saradomin Boss
			{2562,995,200000,7}, {2562,11710,1,17}, {2562,11712,1,17}, {2562,11714,1,17},
			{2562,11706,1,23}, {2562,11730,1,23},
		// Zamorak Boss
			{2554,995,200000,7}, {2554,11710,1,16}, {2554,11712,1,14}, {2554,11714,1,14},
			{2554,11708,1,23}, {2554,3024,1,0},
		// Abyssal Demon
			{1615,995,5000,7}, {1615,1333,1,8}, {1615,1247,1,8}, {1615,830,40,9}, {1615,1319,1,8},
			{1615,4587,1,11}, {1615,1079,1,8}, {1615,1147,1,6}, {1615,1149,1,9}, {1615,4151,1,17},
			{1615,592,1,0},
		// Banshee
			{1612,995,5000,9}, {1612,1333,1,10}, {1612,1247,1,8}, {1612,830,20,9}, {1612,592,1,0},
		// Crawing Hand
			{1648,526,1,0}, {1649,526,1,0}, {1650,526,1,0}, {1651,526,1,0}, {1652,526,1,0},
		// Infernal Mage
			{1643,526,1,0}, {1643,4675,1,14}, {1643,555,50,7}, {1643,560,20,8}, {1643,565,20,8},
			{1643,4089,1,9}, {1643,4091,1,13}, {1643,4093,1,13}, {1643,4094,1,14}, {1643,4101,1,14},
			{1643,4103,1,14}, {1643,4111,1,14}, {1643,4113,1,14},
			{1644,526,1,0}, {1644,4675,1,14}, {1644,555,50,7}, {1644,560,20,8}, {1644,565,20,8},
			{1644,4089,1,9}, {1644,4091,1,13}, {1644,4093,1,13}, {1644,4094,1,14}, {1644,4101,1,14},
			{1644,4103,1,14}, {1644,4111,1,14}, {1644,4113,1,14},
			{1645,526,1,0}, {1645,4675,1,14}, {1645,555,50,7}, {1645,560,20,8}, {1645,565,20,8},
			{1645,4089,1,9}, {1645,4091,1,13}, {1645,4093,1,13}, {1645,4094,1,14}, {1645,4101,1,14},
			{1645,4103,1,14}, {1645,4111,1,14}, {1645,4113,1,14},
			{1646,526,1,0}, {1646,4675,1,14}, {1646,555,50,7}, {1646,560,20,8}, {1646,565,20,8},
			{1646,4089,1,9}, {1646,4091,1,13}, {1646,4093,1,13}, {1646,4094,1,14}, {1646,4101,1,14},
			{1646,4103,1,14}, {1646,4111,1,14}, {1646,4113,1,14},
			{1647,526,1,0}, {1647,4675,1,14}, {1647,555,50,7}, {1647,560,20,8}, {1647,565,20,8},
			{1647,4089,1,9}, {1647,4091,1,13}, {1647,4093,1,13}, {1647,4094,1,14}, {1647,4101,1,14},
			{1647,4103,1,14}, {1647,4111,1,14}, {1647,4113,1,14},
		// Bloodveld
			{1619,995,5000,7}, {1619,1333,1,12}, {1619,1247,1,11}, {1619,830,40,12}, {1619,1319,1,14},
			{1619,4587,1,11}, {1619,1079,1,13}, {1619,1147,1,13}, {1619,1149,1,12},
			{1619,592,1,0},
		// DustDevil
			{1624,995,5000,7}, {1624,3140,1,27}, {1624,592,1,0}, {1624,1333,1,9}, {1624,1247,1,10},
		// Gargoyle
			{1610,526,1,0}, {1610,1333,1,9}, {1610,4153,1,14},
			{1611,526,1,0}, {1611,1333,1,9}, {1611,4153,1,14},
		// Nechryeal
			{1613,592,1,0}, {1613,11732,1,15}, {1613,4131,1,13},
		// Dark Beast
			{2783,995,5000,7}, {2783,1333,1,8}, {2783,1247,1,8}, {2783,830,40,9}, {2783,1319,1,8},
			{2783,4587,1,11}, {2783,1079,1,8}, {2783,1147,1,6}, {2783,1149,1,9}, {2783,11235,1,17},
			{2783,11212,5,14},{2783,526,1,0},
		// Green Dragon
			{941,536,1,0}, {941,1754,1,0}, {941,1333,1,9}, {941,1247,1,10}, {941,1319,1,11}, {941,4587,1,12},
		// Blue Dragon
			{55,536,1,0}, {55,1751,1,0}, {55,1333,1,9}, {55,1247,1,10}, {55,1319,1,10}, {55,4597,1,10},
		// Skeleton
			{92,526,1,0}, {92,1247,1,8}, {92,995,5000,7},
		// Magic Axe
			{127,1373,1,9}, {127,1363,1,0},
		// Lesser Demon
			{752,592,1,0}, {752,1333,1,9}, {752,1247,1,7},
		// Baby Blue Dragon
			{52,534,1,0},
		// Black Demon
			{84,592,1,0}, {84,1333,1,8}, {84,1247,1,9}, {84,5698,1,10}, {84,4587,1,10},
		// Hill Giant
			{117,995,5000,9}, {117,1333,1,9}, {117,1247,1,9}, {117,830,40,9}, {117,1319,1,9},
			{117,4587,1,9}, {117,1079,1,9}, {117,1147,1,9}, {117,1149,1,9}, 
			{117,532,1,0},
		// Moss Giant
			{112,995,5000,9}, {112,1333,1,9}, {112,1247,1,9}, {112,830,40,9}, {112,1319,1,9},
			{112,4587,1,9}, {112,1079,1,9}, {112,1147,1,9}, {112,1149,1,9}, 
			{112,532,1,0},
		// Fire Giant
			{110,995,5000,12}, {110,1333,1,13}, {110,1247,1,13}, {110,830,40,14}, {110,1319,1,13},
			{110,4587,1,11}, {110,1079,1,13}, {110,1147,1,10}, {110,1149,1,14}, 
			{110,532,1,0},
		// Elf Warrior
			{1183,526,1,0}, {1183,4212,1,18},
		// Dags
			{2881,536,1,0}, {2882,536,1,0}, {2883,536,1,0},
			{2881,6737,1,18}, {2882,6737,1,18}, {2883,6737,1,18},
		// Chaos Elemental
			{3200,11730,1,24}, {3200,592,0,1},
		// KBD
			{50,536,1,0}, {50,11286,1,23}, {50,11732,1,18}, {50,14484,1,25},
		// Tzhaar
			{2591,6522,20,10}, {2591,6523,1,10}, {2591,6524,1,10}, {2591,6525,1,10}, 
			{2591,6526,1,10}, {2591,6527,1,10}, {2591,6528,1,10}, {2591,6571,1,10},
			{2592,6522,20,10}, {2592,6523,1,10}, {2592,6524,1,10}, {2592,6525,1,10}, 
			{2592,6526,1,10}, {2592,6527,1,10}, {2592,6528,1,10}, {2592,6571,1,10},
			{2593,6522,20,10}, {2593,6523,1,10}, {2593,6524,1,10}, {2593,6525,1,10}, 
			{2593,6526,1,10}, {2593,6527,1,10}, {2593,6528,1,10}, {2593,6571,1,10},
			{2594,6522,20,10}, {2594,6523,1,10}, {2594,6524,1,10}, {2594,6525,1,10}, 
			{2594,6526,1,10}, {2594,6527,1,10}, {2594,6528,1,10}, {2594,6571,1,10},
			{2595,6522,20,10}, {2595,6523,1,10}, {2595,6524,1,10}, {2595,6525,1,10}, 
			{2595,6526,1,10}, {2595,6527,1,10}, {2595,6528,1,10}, {2595,6571,1,10},
			{2596,6522,20,10}, {2596,6523,1,10}, {2596,6524,1,10}, {2596,6525,1,10}, 
			{2596,6526,1,10}, {2596,6527,1,10}, {2596,6528,1,10}, {2596,6571,1,10},
			{2597,6522,20,10}, {2597,6523,1,10}, {2597,6524,1,10}, {2597,6525,1,10}, 
			{2597,6526,1,10}, {2597,6527,1,10}, {2597,6528,1,10}, {2597,6571,1,10},
			{2598,6522,20,10}, {2598,6523,1,10}, {2598,6524,1,10}, {2598,6525,1,10}, 
			{2598,6526,1,10}, {2598,6527,1,10}, {2598,6528,1,10}, {2598,6571,1,10},
			{2599,6522,20,10}, {2599,6523,1,10}, {2599,6524,1,10}, {2599,6525,1,10}, 
			{2599,6526,1,10}, {2599,6527,1,10}, {2599,6528,1,10}, {2599,6571,1,10},
			{2600,6522,20,10}, {2600,6523,1,10}, {2600,6524,1,10}, {2600,6525,1,10}, 
			{2600,6526,1,10}, {2600,6527,1,10}, {2600,6528,1,10}, {2600,6571,1,10},
			{2601,6522,20,10}, {2601,6523,1,10}, {2601,6524,1,10}, {2601,6525,1,10}, 
			{2601,6526,1,10}, {2601,6527,1,10}, {2601,6528,1,10}, {2601,6571,1,10},
			{2602,6522,20,10}, {2602,6523,1,10}, {2602,6524,1,10}, {2602,6525,1,10}, 
			{2602,6526,1,10}, {2602,6527,1,10}, {2602,6528,1,10}, {2602,6571,1,10},
			{2603,6522,20,10}, {2603,6523,1,10}, {2603,6524,1,10}, {2603,6525,1,10}, 
			{2603,6526,1,10}, {2603,6527,1,10}, {2603,6528,1,10}, {2603,6571,1,10},
			{2604,6522,20,10}, {2604,6523,1,10}, {2604,6524,1,10}, {2604,6525,1,10}, 
			{2604,6526,1,10}, {2604,6527,1,10}, {2604,6528,1,10}, {2604,6571,1,10},
			{2605,6522,20,10}, {2605,6523,1,10}, {2605,6524,1,10}, {2605,6525,1,10}, 
			{2605,6526,1,10}, {2605,6527,1,10}, {2605,6528,1,10}, {2605,6571,1,10},
			{2606,6522,20,10}, {2606,6523,1,10}, {2606,6524,1,10}, {2606,6525,1,10}, 
			{2606,6526,1,10}, {2606,6527,1,10}, {2606,6528,1,10}, {2606,6571,1,10},
			{2607,6522,20,10}, {2607,6523,1,10}, {2607,6524,1,10}, {2607,6525,1,10}, 
			{2607,6526,1,10}, {2607,6527,1,10}, {2607,6528,1,10}, {2607,6571,1,10},
			{2608,6522,20,10}, {2608,6523,1,10}, {2608,6524,1,10}, {2608,6525,1,10}, 
			{2608,6526,1,10}, {2608,6527,1,10}, {2608,6528,1,10}, {2608,6571,1,10},
			{2609,6522,20,10}, {2609,6523,1,10}, {2609,6524,1,10}, {2609,6525,1,10}, 
			{2609,6526,1,10}, {2609,6527,1,10}, {2609,6528,1,10}, {2609,6571,1,10},
			{2610,6522,20,10}, {2610,6523,1,10}, {2610,6524,1,10}, {2610,6525,1,10}, 
			{2610,6526,1,10}, {2610,6527,1,10}, {2610,6528,1,10}, {2610,6571,1,10},
			{2611,6522,20,10}, {2611,6523,1,10}, {2611,6524,1,10}, {2611,6525,1,10}, 
			{2611,6526,1,10}, {2611,6527,1,10}, {2611,6528,1,10}, {2611,6571,1,10},
			{2612,6522,20,10}, {2612,6523,1,10}, {2612,6524,1,10}, {2612,6525,1,10}, 
			{2612,6526,1,10}, {2612,6527,1,10}, {2612,6528,1,10}, {2612,6571,1,10},
			{2613,6522,20,10}, {2613,6523,1,10}, {2613,6524,1,10}, {2613,6525,1,10}, 
			{2613,6526,1,10}, {2613,6527,1,10}, {2613,6528,1,10}, {2613,6571,1,10},
			{2614,6522,20,10}, {2614,6523,1,10}, {2614,6524,1,10}, {2614,6525,1,10}, 
			{2614,6526,1,10}, {2614,6527,1,10}, {2614,6528,1,10}, {2614,6571,1,10},
			{2615,6522,20,10}, {2615,6523,1,10}, {2615,6524,1,10}, {2615,6525,1,10}, 
			{2615,6526,1,10}, {2615,6527,1,10}, {2615,6528,1,10}, {2615,6571,1,10},
			{2616,6522,20,10}, {2616,6523,1,10}, {2616,6524,1,10}, {2616,6525,1,10}, 
			{2616,6526,1,10}, {2616,6527,1,10}, {2616,6528,1,10}, {2616,6571,1,10}
		};

	
	
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
}
