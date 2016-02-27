package server.world;

public final class GameObject {
	
	public int id;
	private int type;
	public int x;
	public int y;
	private int face;

	public GameObject(int id, int type, int x, int y, int face) {
		this.id = id;
		this.type = type;
		this.x = x;
		this.y = y;
		this.face = face;
	}

	public int id() {
		return id;
	}

	public int type() {
		return type;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getFace() {
		return face;
	}

	public Location location;

	public int getZ() {
		return location.getHeight();
	}
}