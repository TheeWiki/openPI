package server.world.cities;

import server.model.players.Client;
import server.world.cities.impl.Home;

public final class CityController {

	public static void sendFirstClickObject(Client c, int object)
	{

	}
	public static void sendSecondClickObject(Client c, int object)
	{
		
	}
	public static void sendThirdClickObject(Client c, int object)
	{
		
	}
	public static void sendFirstClickNpc(Client c, int npc)
	{
		Home.sendFirstClickNpc(c, npc);
	}
	public static void sendSecondClickNpc(Client c, int npc)
	{
		
	}
	public static void sendThirdClickNpc(Client c, int npc)
	{
		
	}
}
