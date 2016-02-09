package server.world.locations;

import server.model.players.Client;

public abstract class AbstractLocations 
{
	public abstract void sendFirstClickObject(Client c, int object);
	
	public abstract void sendSecondClickObject(Client c, int object);
	
	public abstract void sendThirdClickObject(Client c, int object);
	
	public abstract void sendFirstClickNpc(Client c, int npc);

	public abstract void sendSecondClickNpc(Client c, int npc);
	
	public abstract void sendThirdClickNpc(Client c, int npc);
}