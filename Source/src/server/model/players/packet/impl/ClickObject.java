package server.model.players.packet.impl;

import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.objects.Doors;
import server.model.players.Player;
import server.model.players.packet.PacketType;
import server.util.Misc;
/**
 * Click Object
 */
public class ClickObject implements PacketType {

	public static final int FIRST_CLICK = 132, SECOND_CLICK = 252, THIRD_CLICK = 70;	
	@Override
	public void processPacket(final Player player, int packetType, int packetSize) {		
		player.clickObjectType = player.objectX = player.objectId = player.objectY = 0;
		player.objectYOffset = player.objectXOffset = 0;
		player.getPA().resetFollow();
		switch(packetType) {
			
			case FIRST_CLICK:
			player.objectX = player.getInStream().readSignedWordBigEndianA();
			player.objectId = player.getInStream().readUnsignedWord();
			player.objectY = player.getInStream().readUnsignedWordA();
			player.objectDistance = 1;
			if(player.goodDistance(player.getX(), player.getY(), player.objectX, player.objectY, 1)) {
				if (Doors.getSingleton().handleDoor(player.objectId, player.objectX, player.objectY, player.heightLevel)) { 
				}
			}
			
			if(player.playerRights >= 3 && player.playerName.equalsIgnoreCase("Sanity")) {
				Misc.println("objectId: "+player.objectId+"  ObjectX: "+player.objectX+ "  objectY: "+player.objectY+" Xoff: "+ (player.getX() - player.objectX)+" Yoff: "+ (player.getY() - player.objectY)); 
			} else if (player.playerRights == 3) {
				player.getActionSender().sendMessage("objectId: " + player.objectId + " objectX: " + player.objectX + " objectY: " + player.objectY);
			}
			if (Math.abs(player.getX() - player.objectX) > 25 || Math.abs(player.getY() - player.objectY) > 25) {
				player.resetWalkingQueue();
				break;
			}
			switch(player.objectId) {
			case 9398://deposit
				player.getPA().sendFrame126("The Bank of RuneScape - Deposit Box", 7421);
				player.getPA().sendFrame248(4465, 197);//197 just because you can't see it =\
				player.getItems().resetItems(7423);
			break;
			
				case 1733:
					player.objectYOffset = 2;
				break;
				
				case 3044:
					player.objectDistance = 3;
				break;
				
				case 245:
					player.objectYOffset = -1;
					player.objectDistance = 0;
				break;
				
				case 272:
					player.objectYOffset = 1;
					player.objectDistance = 0;
				break;
				
				case 273:
					player.objectYOffset = 1;
					player.objectDistance = 0;
				break;
				
				case 246:
					player.objectYOffset = 1;
					player.objectDistance = 0;
				break;
				
				case 4493:
				case 4494:
				case 4496:
				case 4495:
					player.objectDistance = 5;
				break;
				case 10229:
				case 6522:
					player.objectDistance = 2;
				break;
				case 8959:
					player.objectYOffset = 1;
				break;
				case 4417:
				if (player.objectX == 2425 && player.objectY == 3074)
					player.objectYOffset = 2;
				break;
				case 4420:
				if (player.getX() >= 2383 && player.getX() <= 2385){
					player.objectYOffset = 1;
				} else {
					player.objectYOffset = -2;
				}
				case 6552:
				case 409:
					player.objectDistance = 2;
				break;
				case 2879:
				case 2878:
					player.objectDistance = 3;
				break;
				case 2558:
					player.objectDistance = 0;
					if (player.absX > player.objectX && player.objectX == 3044)
						player.objectXOffset = 1;
					if (player.absY > player.objectY)
						player.objectYOffset = 1;
					if (player.absX < player.objectX && player.objectX == 3038)
						player.objectXOffset = -1;
				break;
				case 9356:
					player.objectDistance = 2;
				break;
				case 5959:
				case 1815:
				case 5960:
				case 1816:
					player.objectDistance = 0;
				break;
				
				case 9293:
					player.objectDistance = 2;
				break;
				case 4418:
				if (player.objectX == 2374 && player.objectY == 3131)
					player.objectYOffset = -2;
				else if (player.objectX == 2369 && player.objectY == 3126)
					player.objectXOffset = 2;
				else if (player.objectX == 2380 && player.objectY == 3127)
					player.objectYOffset = 2;
				else if (player.objectX == 2369 && player.objectY == 3126)
					player.objectXOffset = 2;
				else if (player.objectX == 2374 && player.objectY == 3131)
					player.objectYOffset = -2;
				break;
				case 9706:
					player.objectDistance = 0;
					player.objectXOffset = 1;
				break;
				case 9707:
					player.objectDistance = 0;
					player.objectYOffset = -1;
				break;
				case 4419:
				case 6707: // verac
				player.objectYOffset = 3;
				break;
				case 6823:
				player.objectDistance = 2;
				player.objectYOffset = 1;
				break;
				
				case 6706: // torag
				player.objectXOffset = 2;
				break;
				case 6772:
				player.objectDistance = 2;
				player.objectYOffset = 1;
				break;
				
				case 6705: // karils
				player.objectYOffset = -1;
				break;
				case 6822:
				player.objectDistance = 2;
				player.objectYOffset = 1;
				break;
				
				case 6704: // guthan stairs
				player.objectYOffset = -1;
				break;
				case 6773:
				player.objectDistance = 2;
				player.objectXOffset = 1;
				player.objectYOffset = 1;
				break;
				
				case 6703: // dharok stairs
				player.objectXOffset = -1;
				break;
				case 6771:
				player.objectDistance = 2;
				player.objectXOffset = 1;
				player.objectYOffset = 1;
				break;
				
				case 6702: // ahrim stairs
				player.objectXOffset = -1;
				break;
				case 6821:
				player.objectDistance = 2;
				player.objectXOffset = 1;
				player.objectYOffset = 1;
				break;
				case 1276:
				case 1278://trees
				case 1281: //oak
				case 1308: //willow
				case 1307: //maple
				case 1309: //yew
				case 1306: //yew
				player.objectDistance = 3;
				break;
				case 2513:
					player.getRG().fireAtTarget();
					break;
				default:
				player.objectDistance = 1;
				player.objectXOffset = 0;
				player.objectYOffset = 0;
				break;		
			}
			if(player.goodDistance(player.objectX+player.objectXOffset, player.objectY+player.objectYOffset, player.getX(), player.getY(), player.objectDistance)) {
				player.getActions().firstClickObject(player, player.objectId, player.objectX, player.objectY);
			} else {
				player.clickObjectType = 1;
				CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if(player.clickObjectType == 1 && player.goodDistance(player.objectX + player.objectXOffset, player.objectY + player.objectYOffset, player.getX(), player.getY(), player.objectDistance)) {
							player.getActions().firstClickObject(player, player.objectId, player.objectX, player.objectY);
							container.stop();
						}
						if(player.clickObjectType > 1 || player.clickObjectType == 0)
							container.stop();
					}
					@Override
					public void stop() {
						player.clickObjectType = 0;
					}
				}, 1);
			}
			break;
			
			case SECOND_CLICK:
			player.objectId = player.getInStream().readUnsignedWordBigEndianA();
			player.objectY = player.getInStream().readSignedWordBigEndian();
			player.objectX = player.getInStream().readUnsignedWordA();
			player.objectDistance = 1;
			
			if(player.playerRights >= 3) {
				Misc.println("objectId: "+player.objectId+"  ObjectX: "+player.objectX+ "  objectY: "+player.objectY+" Xoff: "+ (player.getX() - player.objectX)+" Yoff: "+ (player.getY() - player.objectY)); 
			}
			
			switch(player.objectId) {
			case 6163:
			case 6165:
			case 6166:
			case 6164:
			case 6162:
				player.objectDistance = 2;
			break;
				default:
				player.objectDistance = 1;
				player.objectXOffset = 0;
				player.objectYOffset = 0;
				break;
				
			}
			if(player.goodDistance(player.objectX+player.objectXOffset, player.objectY+player.objectYOffset, player.getX(), player.getY(), player.objectDistance)) { 
				player.getActions().secondClickObject(player, player.objectId, player.objectX, player.objectY);
			} else {
				player.clickObjectType = 2;
				CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if(player.clickObjectType == 2 && player.goodDistance(player.objectX + player.objectXOffset, player.objectY + player.objectYOffset, player.getX(), player.getY(), player.objectDistance)) {
							player.getActions().secondClickObject(player, player.objectId, player.objectX, player.objectY);
							container.stop();
						}
						if(player.clickObjectType < 2 || player.clickObjectType > 2)
							container.stop();
					}
					@Override
					public void stop() {
						player.clickObjectType = 0;
					}
				}, 1);
			}
			break;
			
			case THIRD_CLICK:
			player.objectX = player.getInStream().readSignedWordBigEndian();
			player.objectY = player.getInStream().readUnsignedWord();
			player.objectId = player.getInStream().readUnsignedWordBigEndianA();
			
			if(player.playerRights >= 3) {
				Misc.println("objectId: "+player.objectId+"  ObjectX: "+player.objectX+ "  objectY: "+player.objectY+" Xoff: "+ (player.getX() - player.objectX)+" Yoff: "+ (player.getY() - player.objectY)); 
			}
			
			switch(player.objectId) {
				default:
				player.objectDistance = 1;
				player.objectXOffset = 0;
				player.objectYOffset = 0;
				break;		
			}
			if(player.goodDistance(player.objectX+player.objectXOffset, player.objectY+player.objectYOffset, player.getX(), player.getY(), player.objectDistance)) { 
				player.getActions().secondClickObject(player, player.objectId, player.objectX, player.objectY);
			} else {
				player.clickObjectType = 3;
				CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
					@Override
					public void execute(CycleEventContainer container) {
						if(player.clickObjectType == 3 && player.goodDistance(player.objectX + player.objectXOffset, player.objectY + player.objectYOffset, player.getX(), player.getY(), player.objectDistance)) {
							player.getActions().thirdClickObject(player, player.objectId, player.objectX, player.objectY);
							container.stop();
						}
						if(player.clickObjectType < 3)
							container.stop();
					}
					@Override
					public void stop() {
						player.clickObjectType = 0;
					}
				}, 1);
			}	
			break;
		}

	}
	public void handleSpecialCase(Player c, int id, int x, int y) {

	}

}
