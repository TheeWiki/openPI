package server.world;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import server.Server;
import server.model.objects.Objects;
import server.model.players.Player;
import server.model.players.Player;
import server.model.players.PlayerHandler;
import server.util.Misc;

/**
 * @author Sanity
 */
@SuppressWarnings("all")
public class ObjectHandler {

	public List<Objects> globalObjects = new ArrayList<Objects>();

	public ObjectHandler() { }

	/**
	 * Adds object to list
	 **/
	public void addObject(Objects object) {
		globalObjects.add(object);
	}

	/**
	 * Removes object from list
	 **/
	public void removeObject(Objects object) {
		globalObjects.remove(object);
	}

	public void createAnObject(Player c, int id, int x, int y) {
		Objects OBJECT = new Objects(id, x, y, 0, 0, 10, 0);
		if (id == -1) {
			removeObject(OBJECT);
		} else {
			addObject(OBJECT);
		}
		Server.objectHandler.placeObject(OBJECT);
	}

	public void createAnObject(Player c, int id, int x, int y, int face) {
		Objects OBJECT = new Objects(id, x, y, 0, face, 10, 0);
		if (id == -1) {
			removeObject(OBJECT);
		} else {
			addObject(OBJECT);
		}
		Server.objectHandler.placeObject(OBJECT);
	}

	/**
	 * Does object exist
	 **/
	public Objects objectExists(int objectX, int objectY, int objectHeight) {
		for (Objects o : globalObjects) {
			if (o.getObjectX() == objectX && o.getObjectY() == objectY
					&& o.getObjectHeight() == objectHeight) {
				return o;
			}
		}
		return null;
	}

	/**
	 * Update objects when entering a new region or logging in
	 **/
	public void updateObjects(Player c) {
		for (Objects o : globalObjects) {
			if (c != null) {
				if (c.heightLevel == o.getObjectHeight() && o.objectTicks == 0) {
					if (c.distanceToPoint(o.getObjectX(), o.getObjectY()) <= 60) {
						c.getPA().object(o.getObjectId(), o.getObjectX(),
								o.getObjectY(), o.getObjectFace(),
								o.getObjectType());
					}
				}
			}
		}
		if (c.distanceToPoint(2961, 3389) <= 60) {
			c.getPA().object(6552, 2961, 3389, -1, 10);
		}
	}

	/**
	 * Creates the object for anyone who is within 60 squares of the object
	 **/
	public void placeObject(Objects o) {
		ArrayList<Objects> toremove = new ArrayList<Objects>();

		for (Objects s : globalObjects) {
			if (s.getObjectX() == o.getObjectX()
					&& s.getObjectY() == o.getObjectY()) {
				toremove.add(s);
			}
		}
		for (Objects s : toremove) {
			if (globalObjects.contains(s)) {
				globalObjects.remove(s);
			}
		}
		globalObjects.add(o);
		for (Player p : PlayerHandler.players) {
			if (p != null) {
				Player person = (Player) p;
				if (person != null) {
					if (person.heightLevel == o.getObjectHeight()
							&& o.objectTicks == 0) {
						if (person.distanceToPoint(o.getObjectX(),
								o.getObjectY()) <= 60) {
							person.getPA().object(o.getObjectId(),
									o.getObjectX(), o.getObjectY(),
									o.getObjectFace(), o.getObjectType());
						}
					}
				}
			}
		}
	}

	public void setFire(Player c, int id, int x, int y) {
		Objects OBJECT = new Objects(id, x, y, 0, 0, 10, 0);
		if (id == -1) {
			removeObject(OBJECT);
		} else {
			addObject(OBJECT);
		}
		Server.objectHandler.placeObject(OBJECT);
	}

	public void process() {
		for (int j = 0; j < globalObjects.size(); j++) {
			if (globalObjects.get(j) != null) {
				Objects o = globalObjects.get(j);
				if (o.objectTicks > 0) {
					o.objectTicks--;
				}
				if (o.objectTicks == 1) {
					Objects deleteObject = objectExists(o.getObjectX(),
							o.getObjectY(), o.getObjectHeight());
					removeObject(deleteObject);
					o.objectTicks = 0;
					placeObject(o);
					removeObject(o);
				}
			}

		}
	}

	public boolean loadGlobalObjects(String fileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader objectFile = null;
		try {
			objectFile = new BufferedReader(new FileReader("./" + fileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(fileName + ": file not found.");
			return false;
		}
		try {
			line = objectFile.readLine();
		} catch (IOException ioexception) {
			Misc.println(fileName + ": error loading file.");
			return false;
		}
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("object")) {
					Objects object = new Objects(Integer.parseInt(token3[0]),
							Integer.parseInt(token3[1]),
							Integer.parseInt(token3[2]),
							Integer.parseInt(token3[3]),
							Integer.parseInt(token3[4]),
							Integer.parseInt(token3[5]), 0);
					addObject(object);
				}
			} else {
				if (line.equals("[ENDOFOBJECTLIST]")) {
					try {
						objectFile.close();
					} catch (IOException ioexception) {
					}
					return true;
				}
			}
			try {
				line = objectFile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			objectFile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	/**
	 * Doors
	 **/

	public static final int MAX_DOORS = 30;
	public static int[][] doors = new int[MAX_DOORS][5];
	public static int doorFace = 0;

	public void doorHandling(int doorId, int doorX, int doorY, int doorHeight) {
		for (int i = 0; i < doors.length; i++) {
			if (doorX == doors[i][0] && doorY == doors[i][1]
					&& doorHeight == doors[i][2]) {
				if (doors[i][4] == 0) {
					doorId++;
				} else {
					doorId--;
				}
				for (Player p : PlayerHandler.players) {
					if (p != null) {
						Player person = (Player) p;
						if (person != null) {
							if (person.heightLevel == doorHeight) {
								if (person.distanceToPoint(doorX, doorY) <= 60) {
									person.getPA().object(-1, doors[i][0],
											doors[i][1], 0, 0);
									if (doors[i][3] == 0 && doors[i][4] == 1) {
										person.getPA().object(doorId,
												doors[i][0], doors[i][1] + 1,
												-1, 0);
									} else if (doors[i][3] == -1
											&& doors[i][4] == 1) {
										person.getPA().object(doorId,
												doors[i][0] - 1, doors[i][1],
												-2, 0);
									} else if (doors[i][3] == -2
											&& doors[i][4] == 1) {
										person.getPA().object(doorId,
												doors[i][0], doors[i][1] - 1,
												-3, 0);
									} else if (doors[i][3] == -3
											&& doors[i][4] == 1) {
										person.getPA().object(doorId,
												doors[i][0] + 1, doors[i][1],
												0, 0);
									} else if (doors[i][3] == 0
											&& doors[i][4] == 0) {
										person.getPA().object(doorId,
												doors[i][0] - 1, doors[i][1],
												-3, 0);
									} else if (doors[i][3] == -1
											&& doors[i][4] == 0) {
										person.getPA().object(doorId,
												doors[i][0], doors[i][1] - 1,
												0, 0);
									} else if (doors[i][3] == -2
											&& doors[i][4] == 0) {
										person.getPA().object(doorId,
												doors[i][0] + 1, doors[i][1],
												-1, 0);
									} else if (doors[i][3] == -3
											&& doors[i][4] == 0) {
										person.getPA().object(doorId,
												doors[i][0], doors[i][1] + 1,
												-2, 0);
									}
								}
							}
						}
					}
				}
			}
		}
	}

	public boolean loadDoorConfig(String fileName) {
		String line = "";
		String token = "";
		String token2 = "";
		String token2_2 = "";
		String[] token3 = new String[10];
		boolean EndOfFile = false;
		BufferedReader objectFile = null;
		try {
			objectFile = new BufferedReader(new FileReader("./" + fileName));
		} catch (FileNotFoundException fileex) {
			Misc.println(fileName + ": file not found.");
			return false;
		}
		try {
			line = objectFile.readLine();
		} catch (IOException ioexception) {
			Misc.println(fileName + ": error loading file.");
			return false;
		}
		int door = 0;
		while (EndOfFile == false && line != null) {
			line = line.trim();
			int spot = line.indexOf("=");
			if (spot > -1) {
				token = line.substring(0, spot);
				token = token.trim();
				token2 = line.substring(spot + 1);
				token2 = token2.trim();
				token2_2 = token2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token2_2 = token2_2.replaceAll("\t\t", "\t");
				token3 = token2_2.split("\t");
				if (token.equals("door")) {
					doors[door][0] = Integer.parseInt(token3[0]);
					doors[door][1] = Integer.parseInt(token3[1]);
					doors[door][2] = Integer.parseInt(token3[2]);
					doors[door][3] = Integer.parseInt(token3[3]);
					doors[door][4] = Integer.parseInt(token3[4]);
					door++;
				}
			} else {
				if (line.equals("[ENDOFDOORLIST]")) {
					try {
						objectFile.close();
					} catch (IOException ioexception) {
					}
					return true;
				}
			}
			try {
				line = objectFile.readLine();
			} catch (IOException ioexception1) {
				EndOfFile = true;
			}
		}
		try {
			objectFile.close();
		} catch (IOException ioexception) {
		}
		return false;
	}

	public final int IN_USE_ID = 14825;

}
