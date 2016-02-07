package server.model.objects;

import server.Server;

public class Object {
	
	public int objectId;
	public int objectX;
	public int objectY;
	public int height;
	public int face;
	public int type;
	public int newId;
	public int tick;
	
	private int absX;
	private int absY;
	private int heightLevel;
	
	public Object(int id, int x, int y, int height, int face, int type, int newId, int ticks) {
		this.objectId = id;
		this.objectX = x;
		this.objectY = y;
		this.height = height;
		this.face = face;
		this.type = type;
		this.newId = newId;
		this.tick = ticks;
		
		this.absX = absX;
		this.absY = absY;
		this.heightLevel = heightLevel;
		Server.objectManager.addObject(this);
	}


	public int getAbsX() {
		return absX;
	}

	public int getAbsY() {
		return absY;
	}

	public int getHeightLevel() {
		return heightLevel;
	}
}