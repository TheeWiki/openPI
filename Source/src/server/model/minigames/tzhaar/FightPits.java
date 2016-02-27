package server.model.minigames.tzhaar;

import server.Server;
import server.model.players.Player;
import server.util.Misc;

/**
 * @author Sanity
 */

public class FightPits {

	public int[] playerInPits = new int[200];
	
	private int GAME_TIMER = 140;
	private int GAME_START_TIMER = 40;
	
	private int gameTime = -1;
	private int gameStartTimer = 30;
	private int properTimer = 0;
	public int playersRemaining = 0;
	
	public String pitsChampion = "Nobody";
	
	public void process() {
		if (properTimer > 0) {
			properTimer--;
			return;
		} else {
			properTimer = 4;
		}
		if (gameStartTimer > 0) {
			gameStartTimer--;
			updateWaitRoom();
		} 
		if (gameStartTimer == 0) {
			startGame();
		}
		if (gameTime > 0) {
			gameTime--;
			if (playersRemaining == 1)
				endPitsGame(getLastPlayerName());
		} else if (gameTime == 0)
			endPitsGame("Nobody");
	}
	
	@SuppressWarnings("static-access")
	public String getLastPlayerName() {
		for (int j = 0; j < playerInPits.length; j++) {
			if (playerInPits[j] > 0)
				return Server.playerHandler.players[playerInPits[j]].playerName;
		}	
		return "Nobody";
	}
	
	@SuppressWarnings("static-access")
	public void updateWaitRoom() {
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null) {
				Player c = (Player) Server.playerHandler.players[j];
				if (c.getPA().inPitsWait()) {
					c.getPA().sendFrame126("     Next Game Begins In : " + ((gameStartTimer * 3) + (gameTime * 3)) + " seconds.", 6570);
					c.getPA().sendFrame126("Champion: " + pitsChampion, 6572);
					c.getPA().sendFrame126("Current waiting players: " + getWaitAmount() , 6664); 
					c.getPA().walkableInterface(6673);
				}	
			}	
		}	
	}
	
	@SuppressWarnings("static-access")
	public void startGame() {
		if (getWaitAmount() < 2) {
			//lack of players
			return;
		}	
		for (int player = 0; player < Server.playerHandler.players.length; player++) {
			if (Server.playerHandler.players[player] != null )  {
					Player c = (Player)Server.playerHandler.players[player];
//					Server.npcHandler.spawnNpc(c,2743,2391,5186,0,0,200,0,0,100, true, false);
//					Server.npcHandler.spawnNpc(c,2743,2385,5145,0,0,200,0,0,100, true, false);
//					Server.npcHandler.spawnNpc(c,2743,2409,5155,0,0,200,0,0,100, true, false);
//					
//					Server.npcHandler.spawnNpc(c,2741,2392,5137,0,0,200,0,0,100, true, false);
//					Server.npcHandler.spawnNpc(c,2741,2403,5140,0,0,200,0,0,100, true, false);
				if (c.getPA().inPitsWait())
					addToPitsGame(player);
			}	
		}
		gameStartTimer = GAME_START_TIMER + GAME_TIMER;
		gameTime = GAME_TIMER;
	}
	
	@SuppressWarnings("static-access")
	public int getWaitAmount() {
		int count = 0;
		for (int j = 0; j < Server.playerHandler.players.length; j++) {
			if (Server.playerHandler.players[j] != null )  {
					Player c = (Player)Server.playerHandler.players[j];
					if (c.getPA().inPitsWait())
						count++;
			}	
		}
		return count;
	}
	
	@SuppressWarnings("static-access")
	public void removePlayerFromPits(int playerId) {
		for (int j = 0; j < playerInPits.length; j++) {
			if (playerInPits[j] == playerId) {
				Player c = (Player)Server.playerHandler.players[playerInPits[j]];
				c.getPA().movePlayer(2399, 5173, 0);
				playerInPits[j] = -1;
				playersRemaining--;
				c.inPits = false;
				break;
			}
		}
	}
	
	@SuppressWarnings("static-access")
	public void endPitsGame(String champion) {
		@SuppressWarnings("unused")
		boolean giveReward = false;
		if (playersRemaining == 1)
			giveReward = true;
		for (int j = 0; j < playerInPits.length; j++) {
			if (playerInPits[j] < 0)
				continue;
			if (Server.playerHandler.players[playerInPits[j]] == null)
				continue;
			Player c = (Player)Server.playerHandler.players[playerInPits[j]];
			c.getPA().movePlayer(2399, 5173, 0);
			c.inPits = false;
		}
		playerInPits = new int[200];	
		pitsChampion = champion;
		playersRemaining = 0;
		pitsSlot = 0;
		gameStartTimer = GAME_START_TIMER;
		gameTime = -1;
	}
	
	private int pitsSlot = 0;
	
	@SuppressWarnings("static-access")
	public void addToPitsGame(int playerId) {
		if (Server.playerHandler.players[playerId] == null)
			return;
		playersRemaining++;
		Player c = (Player)Server.playerHandler.players[playerId];
		c.getPA().walkableInterface(-1);
		playerInPits[pitsSlot++] = playerId;
		c.getPA().movePlayer(2392 + Misc.random(12), 5139 + Misc.random(25), 0);
		c.inPits = true;		
	}
}