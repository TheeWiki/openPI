package server.world.locations;

import server.world.locations.impl.Barrows;
import server.world.locations.impl.Home;
import server.world.locations.impl.TaverlyDungeon;
import server.world.locations.impl.Tzhaar;
import server.world.locations.impl.Wilderness;

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
	private static Tzhaar tzhaar = new Tzhaar();
	public static Tzhaar getTzhaar()
	{
		return tzhaar;
	}
	private static Wilderness wilderness = new Wilderness();
	public static Wilderness getWilderness()
	{
		return wilderness;
	}
	private static TaverlyDungeon taverlydungeon = new TaverlyDungeon();
	public static TaverlyDungeon getTraverlyDungeon()
	{
		return taverlydungeon;
	}
}
