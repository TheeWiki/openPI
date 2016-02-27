package server.model;

import server.model.players.Player;

/**
 * The class {@link #Entity()} is a class that defines an Object, Player, NPC.
 * the {@link NPC} & {@link Player} current extend to this abstraction.
 * 
 * @author Dennis
 *
 */
public abstract class Entity {
	/**
	 * Initializing the entity to the game server.
	 */
	public abstract void initialize();

	/**
	 * Updates the entity to the game server.
	 */
	public abstract void update();

	/**
	 * Processes all functions that the entity performs in, as well as events,
	 * etc..
	 */
	public abstract void process();

	/**
	 * The entity processes all inbound and outbound packets.
	 * 
	 * @return {@link #processQueuedPackets()}
	 */
	public abstract boolean processQueuedPackets();

	/**
	 * Gets the entities tile based X coordinate.
	 * 
	 * @return getX
	 */
	public abstract int getX();

	/**
	 * Gets the entities tile based Y coordinate.
	 * 
	 * @return getY
	 */
	public abstract int getY();

	/**
	 * Deals damage to the entity type (Player, Object, NPC).
	 */
	public abstract void dealDamage();

	/**
	 * On death of the entity initiate this function.
	 */
	public abstract void onDeath();

	/**
	 * After the entity dies, the new life of the entity is put inside this
	 * function.
	 */
	public abstract void giveLife();

	/**
	 * Represents the attack styles in which the entity performs inside and
	 * outside of combat.
	 */
	public enum AttackStyles 
	{
		MELEE, MAGIC, RANGED
	}

	/**
	 * Sets the default strike as {@link AttackStyles} Melee.
	 */
	@SuppressWarnings("unused")
	private AttackStyles attackType = AttackStyles.MELEE;

	/**
	 * Creates an instance of the {@link UpdateFlags} class.
	 */
	private UpdateFlags flags = new UpdateFlags();

	/**
	 * Gets the {@link UpdateFlags} and returns {@link #flags}.
	 * 
	 * @return flags
	 */
	public UpdateFlags getUpdateFlags()
	{
		return flags;
	}
}