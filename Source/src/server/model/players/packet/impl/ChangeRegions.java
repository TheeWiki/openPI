package server.model.players.packet.impl;

import server.Server;
import server.model.players.Player;
import server.model.players.packet.PacketType;
import server.world.sound.Music;

/**
 * Change Regions
 */
public class ChangeRegions implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		//Server.objectHandler.updateObjects(c);
		Server.itemHandler.reloadItems(player);
		Server.objectManager.loadObjects(player);
		player.getPA().castleWarsObjects();
		Music.playMusic(player);
		
		player.saveFile = true;
		
		if(player.skullTimer > 0) {
			player.isSkulled = true;	
			player.headIconPk = 0;
			player.getPA().requestUpdates();
		}
		player.toleranceTimer = System.currentTimeMillis();
	}
		
}
