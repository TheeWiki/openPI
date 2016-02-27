package server.util;

import java.util.HashMap;
import java.util.Map;

public enum Direction {

	NORTH(516), 
	NORTH_EAST(517), 
	EAST(518), 
	SOUTH_EAST(519), 
	SOUTH(520), 
	SOUTH_WEST(521), 
	WEST(514), 
	NORTH_WEST(515);

	private int id = 0;

	public int getId() {
		return id;
	}
	private Direction(int id) {
		this.id = id;
	}

	private static Map<Integer, Direction> states = new HashMap<Integer, Direction>();

	static {
		for (Direction direction : states.values())
			Direction.states.put(direction.getId(), direction);
	}
}