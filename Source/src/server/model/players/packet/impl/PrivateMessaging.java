package server.model.players.packet.impl;

import server.Constants;
import server.Server;
import server.model.players.Player;
import server.model.players.packet.PacketType;
import server.net.Connection;
import server.util.Misc;

/**
 * Private messaging, friends etc
 **/
public class PrivateMessaging implements PacketType {

	public final int ADD_FRIEND = 188, SEND_PM = 126, REMOVE_FRIEND = 215, CHANGE_PM_STATUS = 95, REMOVE_IGNORE = 59, ADD_IGNORE = 133;
	
	@SuppressWarnings("static-access")
	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		switch(packetType) {
		
			case ADD_FRIEND:
			player.friendUpdate = true;
			long friendToAdd = player.getInStream().readQWord();
			boolean canAdd = true;

			for (int i1 = 0; i1 < player.friends.length; i1++) {
				if (player.friends[i1] != 0 && player.friends[i1] == friendToAdd) {
					canAdd = false;
					player.getActionSender().sendMessage(friendToAdd + " is already on your friends list.");
				}
			}
			if (canAdd == true) {
				for (int i1 = 0; i1 < player.friends.length; i1++) {
					if (player.friends[i1] == 0) {
						player.friends[i1] = friendToAdd;
						for (int i2 = 1; i2 < Constants.MAX_PLAYERS; i2++) {
							if (Server.playerHandler.players[i2] != null && Server.playerHandler.players[i2].isActive && Misc.playerNameToInt64(Server.playerHandler.players[i2].playerName)== friendToAdd) {
								Player o = (Player)Server.playerHandler.players[i2];
								if(o != null) {
									if (Server.playerHandler.players[i2].privateChat == 0 || (Server.playerHandler.players[i2].privateChat == 1 && o.getPA().isInPM(Misc.playerNameToInt64(player.playerName)))) {
										player.getPA().loadPM(friendToAdd, 1);
										break;
									}
								}
							}
						}
						break;
					}
				}
			}
			break;
			
			case SEND_PM:
			long sendMessageToFriendId = player.getInStream().readQWord();
            byte pmchatText[] = new byte[100];
            int pmchatTextSize = (byte) (packetSize - 8);
			player.getInStream().readBytes(pmchatText, pmchatTextSize, 0);
			if (Connection.isMuted(player))
				break;
            for (int i1 = 0; i1 < player.friends.length; i1++) {
                if (player.friends[i1] == sendMessageToFriendId) {
                    boolean pmSent = false;

                    for (int i2 = 1; i2 < Constants.MAX_PLAYERS; i2++) {
                        if (Server.playerHandler.players[i2] != null && Server.playerHandler.players[i2].isActive && Misc.playerNameToInt64(Server.playerHandler.players[i2].playerName)== sendMessageToFriendId) {
                            Player o = (Player)Server.playerHandler.players[i2];
							if(o != null) {
								if (Server.playerHandler.players[i2].privateChat == 0 || (Server.playerHandler.players[i2].privateChat == 1 && o.getPA().isInPM(Misc.playerNameToInt64(player.playerName)))) {
									o.getPA().sendPM(Misc.playerNameToInt64(player.playerName), player.playerRights, pmchatText, pmchatTextSize);
	                                pmSent = true;
	                            }
							}
                            break;
                        }
                    }
                    if (!pmSent) {
						player.getActionSender().sendMessage("That player is currently offline.");
						break;
                    }
                }
            }
            break;	
			
			
			case REMOVE_FRIEND:
			player.friendUpdate = true;
            long friendToRemove = player.getInStream().readQWord();

            for (int i1 = 0; i1 < player.friends.length; i1++) {
                if (player.friends[i1] == friendToRemove) {
					for (int i2 = 1; i2 < Constants.MAX_PLAYERS; i2++) {
						Player o = (Player)Server.playerHandler.players[i2];		
						if(o != null) {
							if(player.friends[i1] == Misc.playerNameToInt64(Server.playerHandler.players[i2].playerName)){
								o.getPA().updatePM(player.playerId, 0);
								break;
							}
						}
					}
					player.friends[i1] = 0;
                    break;
                }
            }
            break;
			
			case REMOVE_IGNORE:
				int i = player.getInStream().readDWord();
				int i2 = player.getInStream().readDWord();
				int i3 = player.getInStream().readDWord();
				//for other status changing
				player.getPA().handleStatus(i,i2,i3);
            break;
			
			case CHANGE_PM_STATUS:
            @SuppressWarnings("unused")
			int tradeAndCompete = player.getInStream().readUnsignedByte();
            player.privateChat = player.getInStream().readUnsignedByte();
            @SuppressWarnings("unused")
			int publicChat = player.getInStream().readUnsignedByte();
            for (int i1 = 1; i1 < Constants.MAX_PLAYERS; i1++) {
			   if (Server.playerHandler.players[i1] != null && Server.playerHandler.players[i1].isActive == true) {
                    Player o = (Player)Server.playerHandler.players[i1];
					if(o != null) {
						o.getPA().updatePM(player.playerId, 1);
					}
                }
            }
            break;
			
			
			
			case ADD_IGNORE:
				//TODO: fix this so it works :)
				break;
            
		}
		
	}	
}
