package server.model.players.packet.impl;

import server.Server;
import server.model.players.Client;
import server.model.players.packet.PacketType;
import server.world.sound.Music;

/**
 * Change Regions
 */
public class ChangeRegions implements PacketType {

	@Override
	public void processPacket(Client c, int packetType, int packetSize) {
		//Server.objectHandler.updateObjects(c);
		Server.itemHandler.reloadItems(c);
		Server.objectManager.loadObjects(c);
		c.getPA().castleWarsObjects();
		Music.playMusic(c);
		
		c.saveFile = true;
		
		if(c.skullTimer > 0) {
			c.isSkulled = true;	
			c.headIconPk = 0;
			c.getPA().requestUpdates();
		}
		c.toleranceTimer = System.currentTimeMillis();
	}
		
}
