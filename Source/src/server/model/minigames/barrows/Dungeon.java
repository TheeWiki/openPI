package server.model.minigames.barrows;

import server.model.players.Player;
import server.util.Misc;

public class Dungeon
{

	private static final int[][] dungeonSpawn =
	{
			{
					3568, 9677
			},
			{
					3568, 9712
			},
			{
					3534, 9712
			},
			{
					3534, 9677
			}
	};

	private static final int findX()
	{
		int x = 0;
		final int total = Dungeon.dungeonSpawn.length - 1;
		final int length = Misc.random(total);
		x = Dungeon.dungeonSpawn[length][0];
		return x;
	}

	private static final int findY()
	{
		int y = 0;
		final int total = Dungeon.dungeonSpawn.length - 1;
		final int length = Misc.random(total);
		y = Dungeon.dungeonSpawn[length][1];
		return y;
	}

	public static void enterDungeon(final Player player)
	{
		if (System.currentTimeMillis() - player.lastThieve < 2000)
		{
			return;
		}
		player.lastThieve = System.currentTimeMillis();
		player.getPA().movePlayer(Dungeon.findX(), Dungeon.findY(), 0);
		player.getPA().removeAllWindows();
		player.getActionSender().sendMessage("You have entered the dungeon.");
	}

	public static void openDungeonDoor(final Player player, final int object,
			final int objectX, final int objectY)
	{
		int x = 0;
		int y = 0;
		switch (object)
		{
		case 6747:
		case 6728:
			if (player.absY == 9683)
			{
				x = objectX;
				y = objectY;
			}
			else if (player.absY == 9684)
			{
				x = objectX;
				y = objectY - 1;
			}
			else if (player.absY == 9688)
			{
				x = objectX;
				y = objectY + 1;
			}
			else if (player.absY == 9689)
			{
				x = objectX;
				y = objectY;
			}
			else
			{
				return;
			}
			break;
		case 6741:
		case 6722:
			if (player.absY == 9700)
			{
				x = objectX;
				y = objectY;
			}
			else if (player.absY == 9701)
			{
				x = objectX;
				y = objectY - 1;
			}
			else if (player.absY == 9705)
			{
				x = objectX;
				y = objectY + 1;
			}
			else if (player.absY == 9706)
			{
				x = objectX;
				y = objectY;
			}
			else
			{
				return;
			}
			break;
		case 6737:
		case 6718:
			if (player.absY == 9706)
			{
				x = objectX;
				y = objectY;
			}
			else if (player.absY == 9705)
			{
				x = objectX;
				y = objectY + 1;
			}
			else if (player.absY == 9701)
			{
				x = objectX;
				y = objectY - 1;
			}
			else if (player.absY == 9700)
			{
				x = objectX;
				y = objectY;
			}
			else
			{
				return;
			}
			break;
		case 6745:
		case 6726:
			if (player.absY == 9689)
			{
				x = objectX;
				y = objectY;
			}
			else if (player.absY == 9688)
			{
				x = objectX;
				y = objectY + 1;
			}
			else if (player.absY == 9684)
			{
				x = objectX;
				y = objectY - 1;
			}
			else if (player.absY == 9683)
			{
				x = objectX;
				y = objectY;
			}
			else
			{
				return;
			}
			break;
		case 6740:
		case 6721:
			if (player.absX == 3563)
			{
				x = objectX;
				y = objectY;
			}
			else if (player.absX == 3562)
			{
				x = objectX + 1;
				y = objectY;
			}
			else if (player.absX == 3558)
			{
				x = objectX - 1;
				y = objectY;
			}
			else if (player.absX == 3557)
			{
				x = objectX;
				y = objectY;
			}
			else
			{
				return;
			}
			break;
		case 6738:
		case 6719:
			if (player.absX == 3546)
			{
				x = objectX;
				y = objectY;
			}
			else if (player.absX == 3545)
			{
				x = objectX + 1;
				y = objectY;
			}
			else if (player.absX == 3541)
			{
				x = objectX - 1;
				y = objectY;
			}
			else if (player.absX == 3540)
			{
				x = objectX;
				y = objectY;
			}
			else
			{
				return;
			}
			break;
		case 6748:
		case 6729:
			if (player.absX == 3540)
			{
				x = objectX;
				y = objectY;
			}
			else if (player.absX == 3541)
			{
				x = objectX - 1;
				y = objectY;
			}
			else if (player.absX == 3545)
			{
				x = objectX + 1;
				y = objectY;
			}
			else if (player.absX == 3546)
			{
				x = objectX;
				y = objectY;
			}
			else
			{
				return;
			}
			break;
		case 6749:
		case 6730:
			if (player.absX == 3557)
			{
				x = objectX;
				y = objectY;
			}
			else if (player.absX == 3558)
			{
				x = objectX - 1;
				y = objectY;
			}
			else if (player.absX == 3562)
			{
				x = objectX + 1;
				y = objectY;
			}
			else if (player.absX == 3563)
			{
				x = objectX;
				y = objectY;
			}
			else
			{
				return;
			}
			break;
		}
		player.getPA().movePlayer(x, y, 0);
	}

	public static void puzzleDoors(final Player player, final int object,
			final int objectX, final int objectY)
	{
		int x = 0;
		int y = 0;
		switch (object)
		{
		case 6746:
		case 6727:
			if (player.absY == 9683)
			{
				Dungeon.doorPuzzle(player);
				return;
			}
			else if (player.absY == 9684)
			{
				x = objectX;
				y = objectY - 1;
			}
			else if (player.absY == 9688)
			{
				x = objectX;
				y = objectY + 1;
			}
			else if (player.absY == 9689)
			{
				x = objectX;
				y = objectY;
			}
			else
			{
				return;
			}
			break;

		case 6739:
		case 6720:
			if (player.absY == 9706)
			{
				Dungeon.doorPuzzle(player);
				return;
			}
			else if (player.absY == 9705)
			{
				x = objectX;
				y = objectY + 1;
			}
			else if (player.absY == 9701)
			{
				x = objectX;
				y = objectY - 1;
			}
			else if (player.absY == 9700)
			{
				x = objectX;
				y = objectY;
			}
			else
			{
				return;
			}
			break;

		case 6724:
		case 6743:
			if (player.absX == 3540)
			{
				Dungeon.doorPuzzle(player);
				return;
			}
			else if (player.absX == 3541)
			{
				x = objectX - 1;
				y = objectY;
			}
			else if (player.absX == 3545)
			{
				x = objectX + 1;
				y = objectY;
			}
			else if (player.absX == 3546)
			{
				x = objectX;
				y = objectY;
			}
			else
			{
				return;
			}
			break;

		case 6744:
		case 6725:
			if (player.absX == 3563)
			{
				Dungeon.doorPuzzle(player);
				return;
			}
			else if (player.absX == 3562)
			{
				x = objectX + 1;
				y = objectY;
			}
			else if (player.absX == 3558)
			{
				x = objectX - 1;
				y = objectY;
			}
			else if (player.absX == 3557)
			{
				x = objectX;
				y = objectY;
			}
			else
			{
				return;
			}
			break;
		}
		player.getPA().movePlayer(x, y, 0);
	}

	public static boolean wrongPuzzle = false;

	private static void doorPuzzle(final Player player)
	{
		player.randomBarrows = Misc.random(2);
		player.getPA().sendFrame246(4545, 250, 365);
		player.getPA().sendFrame126("1.", 4553);
		player.getPA().sendFrame246(4546, 250, 373);
		player.getPA().sendFrame126("2.", 4554);
		player.getPA().sendFrame246(4547, 250, 379);
		player.getPA().sendFrame126("3.", 4555);
		player.getPA().sendFrame246(4548, 250, 385);
		player.getPA().sendFrame126("4.", 4556);
		if (player.randomBarrows == 1)
		{
			player.getPA().sendFrame246(4550, 250, 4151);
			player.getPA().sendFrame246(4551, 250, 3244);
			player.getPA().sendFrame246(4552, 250, 391);
		}
		else if (player.randomBarrows == 0)
		{
			player.getPA().sendFrame246(4550, 250, 3244);
			player.getPA().sendFrame246(4551, 250, 391);
			player.getPA().sendFrame246(4552, 250, 4151);
		}
		else
		{
			player.getPA().sendFrame246(4550, 250, 391);
			player.getPA().sendFrame246(4551, 250, 4151);
			player.getPA().sendFrame246(4552, 250, 3244);
		}
		player.getPA().showInterface(4543);
	}
}