package server.world.locations;

import server.model.players.Client;

/**
 * This abstracted class constructs how the classes that extends this abstract
 * class will be formated as, each abstract void giving it's own special
 * function for the player to conduct with.
 * 
 * @author Dennis
 *
 */
public abstract class AbstractLocations {
	/**
	 * This abstract void represents the first click option of a object in which
	 * the player interacts with, an Integer is placed inside the parameter so
	 * that the abstraction can use the switch(Integer){} function.
	 * 
	 * @param c
	 * @param object
	 */
	public abstract void sendFirstClickObject(Client c, int object);

	/**
	 * This abstract void represents the second click option of a object in
	 * which the player interacts with, an Integer is placed inside the
	 * parameter so that the abstraction can use the switch(Integer){} function.
	 * 
	 * @param c
	 * @param object
	 */
	public abstract void sendSecondClickObject(Client c, int object);

	/**
	 * This abstract void represents the third click option of a object in which
	 * the player interacts with, an Integer is placed inside the parameter so
	 * that the abstraction can use the switch(Integer){} function.
	 * 
	 * @param c
	 * @param object
	 */
	public abstract void sendThirdClickObject(Client c, int object);

	/**
	 * This abstract void represents the first click option of a npc in which
	 * the player interacts with, an Integer is placed inside the parameter so
	 * that the abstraction can use the switch(Integer){} function.
	 * 
	 * @param c
	 * @param npc
	 */
	public abstract void sendFirstClickNpc(Client c, int npc);

	/**
	 * This abstract void represents the second click option of a npc in which
	 * the player interacts with, an Integer is placed inside the parameter so
	 * that the abstraction can use the switch(Integer){} function.
	 * 
	 * @param c
	 * @param npc
	 */
	public abstract void sendSecondClickNpc(Client c, int npc);

	/**
	 * This abstract void represents the third click option of a npc in which
	 * the player interacts with, an Integer is placed inside the parameter so
	 * that the abstraction can use the switch(Integer){} function.
	 * 
	 * @param c
	 * @param npc
	 */
	public abstract void sendThirdClickNpc(Client c, int npc);
}