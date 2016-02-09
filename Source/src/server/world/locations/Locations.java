package server.world.locations;

import server.world.locations.impl.*;

public final class Locations 
{
	private static Home home = new Home();
	public static Home getHome()
	{
		return home;
	}
	private static Barrows barrows = new Barrows();
	public static Barrows getBarrows()
	{
		return barrows;
	}
}
