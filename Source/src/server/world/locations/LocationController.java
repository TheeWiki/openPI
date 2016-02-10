package server.world.locations;

import server.model.players.Client;

public final class LocationController {

	public static void sendFirstClickObject(Client c, int object)
	{
		Locations.getHome().sendFirstClickObject(c, object);
		Locations.getBarrows().sendFirstClickObject(c, object);
		Locations.getTzhaar().sendFirstClickObject(c, object);
		Locations.getWilderness().sendFirstClickObject(c, object);
		Locations.getTraverlyDungeon().sendFirstClickObject(c, object);
	}
	public static void sendSecondClickObject(Client c, int object)
	{
		
	}
	public static void sendThirdClickObject(Client c, int object)
	{
		
	}
	public static void sendFirstClickNpc(Client c, int npc)
	{

	}
	public static void sendSecondClickNpc(Client c, int npc)
	{
		
	}
	public static void sendThirdClickNpc(Client c, int npc)
	{
		
	}
}
