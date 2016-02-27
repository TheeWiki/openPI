package server.model;

/**
 * Holds the update flags for an entity in the game world.
 * @author Advocatus
 * 
 */
public class UpdateFlags {
	
	/**
	 * The boolean array that contains updating flags.
	 */
	private boolean[] flags = new boolean[10];
	
	/**
	 * Represents all of the different update flags.
	 * @author Advocatus
	 *
	 */
	public interface UpdateFlag {
		
		/**
		 * Appearance update.
		 */
		public static final int APPEARANCE = 0;
		
		/**
		 * Chat update.
		 */
		public static final int CHAT = 1;
		
		/**
		 * Graphics update.
		 */
		public static final int GRAPHICS = 2;
		
		/**
		 * Animation update.
		 */
		public static final int ANIMATION = 3;
		
		/**
		 * Forced chat update.
		 */
		public static final int FORCED_CHAT = 4;
		
		/**
		 * Interacting entity update.
		 */
		public static final int FACE_ENTITY = 5;
		
		/**
		 * Face coordinate entity update.
		 */
		public static final int FACE_COORDINATE = 6;
		
		/**
		 * Hit update.
		 */
		public static final int HIT = 7;
		
		/**
		 * Hit 2 update.
		 */
		public static final int HIT_2 = 8;
		
		/**
		 * Update flag used to transform npc to another.
		 */
		public static final int TRANSFORM = 9;
	}
	
	/**
	 * Checks if an update required.
	 * @return <code>true</code> if 1 or more flags are set,
	 * <code>false</code> if not.
	 */
	public boolean isUpdateRequired() {
		for (int i = 0; i < flags.length; i++)
			if (flags[i])
				return true;
		return false;
	}
	
	/**
	 * Flags (sets to true) a flag.
	 * @param flag The flag to flag.
	 */
	public void flag(int flag) {
		flags[flag] = true;
	}
	
	/**
	 * Sets a flag.
	 * @param flag The flag.
	 * @param value The value.
	 */
	public void set(int flag, boolean value) {
		flags[flag] = value;
	}
	
	/**
	 * Gets the value of a flag.
	 * @param flag The flag to get the value of.
	 * @return The flag value.
	 */
	public boolean get(int flag) {
		return flags[flag];
	}
	
	/**
	 * Resest all update flags.
	 */
	public void reset() {
		for (int i = 0; i < flags.length; i++)
			flags[i] = false;
	}
}