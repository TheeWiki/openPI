package server.world;

import java.util.ArrayList;

import server.Server;
import server.model.objects.Object;
import server.model.players.Player;
import server.util.Misc;

/**
 * @author Sanity
 */

public class ObjectManager {

	public ArrayList<Object> objects = new ArrayList<Object>();
	private ArrayList<Object> toRemove = new ArrayList<Object>();

	public void process() {
		for (Object o : objects) {
			if (o.tick > 0)
				o.tick--;
			else {
				updateObject(o);
				toRemove.add(o);
			}
		}
		for (Object o : toRemove) {
			if (isObelisk(o.newId)) {
				int index = getObeliskIndex(o.newId);
				if (activated[index]) {
					activated[index] = false;
					teleportObelisk(index);
				}
			}
			objects.remove(o);
		}
		toRemove.clear();
	}

	@SuppressWarnings("static-access")
	public void removeObject(int x, int y) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Player player = (Player) Server.playerHandler.players[j];
				player.getPA().object(-1, x, y, 0, 10);
			}
		}
	}

	@SuppressWarnings("static-access")
	public void updateObject(Object o) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Player player = (Player) Server.playerHandler.players[j];
				player.getPA().object(o.newId, o.objectX, o.objectY, o.face, o.type);
			}
		}
	}

	@SuppressWarnings("static-access")
	public void placeObject(Object o) {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Player player = (Player) Server.playerHandler.players[j];
				if (player.distanceToPoint(o.objectX, o.objectY) <= 60)
					player.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
			}
		}
	}

	public Object getObject(int x, int y, int height) {
		for (Object o : objects) {
			if (o.objectX == x && o.objectY == y && o.height == height)
				return o;
		}
		return null;
	}

	public void loadObjects(Player player) {
		if (player == null)
			return;
		for (Object o : objects) {
			if (loadForPlayer(o, player))
				player.getPA().object(o.objectId, o.objectX, o.objectY, o.face, o.type);
		}
		player.getItems().createGroundItem(952, 3571, 3312, 1); // TODO:
																// pickupable
		loadCustomSpawns(player);
	}

	public void loadCustomSpawns(Player player) {
		player.getPA().checkObjectSpawn(-1, 2950, 3450, 0, 10);
		player.getPA().checkObjectSpawn(1596, 3008, 3850, 1, 0);
		player.getPA().checkObjectSpawn(1596, 3008, 3849, -1, 0);
		player.getPA().checkObjectSpawn(1596, 3040, 10307, -1, 0);
		player.getPA().checkObjectSpawn(1596, 3040, 10308, 1, 0);
		player.getPA().checkObjectSpawn(1596, 3022, 10311, -1, 0);
		player.getPA().checkObjectSpawn(1596, 3022, 10312, 1, 0);
		player.getPA().checkObjectSpawn(1596, 3044, 10341, -1, 0);
		player.getPA().checkObjectSpawn(1596, 3044, 10342, 1, 0);
		player.getPA().checkObjectSpawn(6552, 3094, 3506, 2, 10);
		player.getPA().checkObjectSpawn(409, 2610, 3098, 2, 10);
		player.getPA().checkObjectSpawn(2213, 3047, 9779, 1, 10);
		player.getPA().checkObjectSpawn(2213, 3080, 9502, 1, 10);

		player.getPA().checkObjectSpawn(-1, 2597, 3087, 1, 10);
		player.getPA().checkObjectSpawn(-1, 2597, 3088, 1, 10);

		if (player.heightLevel == 0) {
			player.getPA().checkObjectSpawn(2492, 2911, 3614, 1, 10);
		} else {
			player.getPA().checkObjectSpawn(-1, 2911, 3614, 1, 10);
		}
	}

	public final int IN_USE_ID = 14825;

	public boolean isObelisk(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return true;
		}
		return false;
	}

	public int[] obeliskIds = { 14829, 14830, 14827, 14828, 14826, 14831 };
	public int[][] obeliskCoords = { { 3154, 3618 }, { 3225, 3665 }, { 3033, 3730 }, { 3104, 3792 }, { 2978, 3864 },
			{ 3305, 3914 } };
	public boolean[] activated = { false, false, false, false, false, false };

	public void startObelisk(int obeliskId) {
		int index = getObeliskIndex(obeliskId);
		if (index >= 0) {
			if (!activated[index]) {
				activated[index] = true;
				addObject(
						new Object(14825, obeliskCoords[index][0], obeliskCoords[index][1], 0, -1, 10, obeliskId, 16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1], 0, -1, 10, obeliskId,
						16));
				addObject(new Object(14825, obeliskCoords[index][0], obeliskCoords[index][1] + 4, 0, -1, 10, obeliskId,
						16));
				addObject(new Object(14825, obeliskCoords[index][0] + 4, obeliskCoords[index][1] + 4, 0, -1, 10,
						obeliskId, 16));
			}
		}
	}

	public int getObeliskIndex(int id) {
		for (int j = 0; j < obeliskIds.length; j++) {
			if (obeliskIds[j] == id)
				return j;
		}
		return -1;
	}

	@SuppressWarnings("static-access")
	public void teleportObelisk(int port) {
		int random = Misc.random(5);
		while (random == port) {
			random = Misc.random(5);
		}
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Player player = (Player) Server.playerHandler.players[j];
				int xOffset = player.absX - obeliskCoords[port][0];
				int yOffset = player.absY - obeliskCoords[port][1];
				if (player.goodDistance(player.getX(), player.getY(), obeliskCoords[port][0] + 2, obeliskCoords[port][1] + 2, 1)) {
					player.getPA().startTeleport2(obeliskCoords[random][0] + xOffset, obeliskCoords[random][1] + yOffset, 0);
				}
			}
		}
	}

	public boolean loadForPlayer(Object o, Player c) {
		if (o == null || c == null)
			return false;
		return c.distanceToPoint(o.objectX, o.objectY) <= 60 && c.heightLevel == o.height;
	}

	public void addObject(Object o) {
		if (getObject(o.objectX, o.objectY, o.height) == null) {
			objects.add(o);
			placeObject(o);
		}
	}

}