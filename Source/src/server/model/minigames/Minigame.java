package server.model.minigames;

/**
 * Creates a abstract minigame interface in which the minigames will extend and
 * utilize {@link #Minigame()} to its full functions.
 * 
 * Managing and creating minigames are more simplified when extending the
 * {@link #Minigame()} abstraction to the desired minigame class.
 * 
 * @author Dennis
 *
 */
public abstract class Minigame {

	/**
	 * Gives the minigame a processing function in which dynamic updating to the
	 * minigame and player are done instantly or through their own specific
	 * event.
	 */
	public abstract void process();

	/**
	 * When entering the minigame, if there's a lobby in which the user must
	 * wait inside of (example: {@link PestControl}) then the user will be sent
	 * to the lobby for queue entry to the minigame itself.
	 */
	public abstract void joinLobby();

	/**
	 * Upon entering the minigame queue the {@link #initialize()} function will
	 * send the entity directly to the main minigame and the {@link #process()}
	 * function will now be in effect for all current entities within the
	 * initialized minigame.
	 */
	public abstract void initialize();

	/**
	 * Upon death of the entity the actions will be done with this void.
	 */
	public abstract void onDeath();

	/**
	 * Sets the walkable interface or regular interface in which the minigame
	 * will be used in the {@link #process()} of the minigame.
	 * 
	 * @return interface
	 */
	public abstract int setInterface();

	/**
	 * Finishes the minigame and the end results, etc.. are stored within this
	 * void.
	 */
	public abstract void completeGame();

	/**
	 * When participating and or completing the minigame successfully, or even
	 * when failing the player will receive a reward for participating.
	 */
	public abstract void rewardPlayer();
}