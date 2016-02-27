package server.model.minigames.pest_control;

import server.Server;
import server.model.npcs.NPCHandler;
import server.model.players.Player;
import server.model.players.EquipmentListener;
import server.model.players.PlayerHandler;

/**
 * PestControl.java
 *
 * @author Acquittal
 *
 */

public class PestControl {

	public PestControl() {

	}

	public final int GAME_TIMER = 70; // 5 minutes
	public final int WAIT_TIMER = 7;

	public int gameTimer = -1;
	public int waitTimer = 30;
	public int properTimer = 0;
	public int Portal1kill = 0;
	public int Portal2kill = 0;
	public int Portal3kill = 0;
	public int Portal4kill = 0;

	public void process() {
		setInterface();
		if (properTimer > 0) {
			properTimer--;
			return;
		} else {
			properTimer = 4;
		}
		if (waitTimer > 0)
			waitTimer--;
		else if (waitTimer == 0)
			startGame();
		if (gameTimer > 0) {
			gameTimer--;
			if (allPortalsDead()) {
				endGame(true);
			}
		} else if (gameTimer == 0)
			endGame(false);
	}

	public void startGame() {
		if (playersInBoat() > 0) {
			gameTimer = GAME_TIMER;
			waitTimer = -1;
			// spawn npcs
			spawnNpcs();
			// move players into game
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					if (PlayerHandler.players[j].inPcBoat()) {
						movePlayer(j);
					}
				}
			}
		} else {
			waitTimer = WAIT_TIMER;
			for (int j = 0; j < PlayerHandler.players.length; j++) {
				if (PlayerHandler.players[j] != null) {
					if (PlayerHandler.players[j].inPcBoat()) {
						Player player = (Player) PlayerHandler.players[j];
						player.getActionSender().sendMessage("There need to be at least 3 players to start a game of pest control.");
					}
				}
			}
		}
	}

	public int playersInBoat() {
		int count = 0;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].inPcBoat()) {
					count++;
				}
			}
		}
		return count;
	}

	public void setInterface() {
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].inPcBoat()) {
					Player player = (Player) PlayerHandler.players[j];
					player.getPA().sendFrame126("Next Departure: " + waitTimer + "", 21120);
					player.getPA().sendFrame126("Players Ready: " + playersInBoat() + "", 21121);
					player.getPA().sendFrame126("(Need 3 to 25 players)", 21122);
					player.getPA().sendFrame126("Points: " + player.pcPoints + "", 21123);
				}
				if (PlayerHandler.players[j].inPcGame()) {
					Player player = (Player) PlayerHandler.players[j];
					for (j = 0; j < NPCHandler.npcs.length; j++) {
						if (NPCHandler.npcs[j] != null) {
							if (NPCHandler.npcs[j].npcType == 6142)
								if (Portal1kill == 0) {
									player.getPA().sendFrame126("" + NPCHandler.npcs[j].HP + "", 21111);
									if (NPCHandler.npcs[j].HP == 0) {
										Portal1kill = 1;
									}
								} else {
									player.getPA().sendFrame126("Dead", 21111);
								}
							if (NPCHandler.npcs[j].npcType == 6143)
								if (Portal2kill == 0) {
									player.getPA().sendFrame126("" + NPCHandler.npcs[j].HP + "", 21112);
									if (NPCHandler.npcs[j].HP == 0) {
										Portal2kill = 1;
									}
								} else {
									player.getPA().sendFrame126("Dead", 21112);
								}
							if (NPCHandler.npcs[j].npcType == 6144)
								if (Portal3kill == 0) {
									player.getPA().sendFrame126("" + NPCHandler.npcs[j].HP + "", 21113);
									if (NPCHandler.npcs[j].HP == 0) {
										Portal3kill = 1;
									}
								} else {
									player.getPA().sendFrame126("Dead", 21113);
								}
							if (NPCHandler.npcs[j].npcType == 6145)
								if (Portal4kill == 0) {
									player.getPA().sendFrame126("" + NPCHandler.npcs[j].HP + "", 21114);
									if (NPCHandler.npcs[j].HP == 0) {
										Portal4kill = 1;
									}
								} else {
									player.getPA().sendFrame126("Dead", 21114);
								}
							if (NPCHandler.npcs[j].npcType == 3782)
								player.getPA().sendFrame126("" + NPCHandler.npcs[j].HP + "", 21115);
						}
					}
					player.getPA().sendFrame126("0", 21115);
					player.getPA().sendFrame126("0", 21116);
					player.getPA().sendFrame126("Time remaining: " + gameTimer + "", 21117);
				}
			}
		}
	}

	public void endGame(boolean won) {
		gameTimer = -1;
		waitTimer = WAIT_TIMER;
		for (int j = 0; j < PlayerHandler.players.length; j++) {
			if (PlayerHandler.players[j] != null) {
				if (PlayerHandler.players[j].inPcGame()) {
					Player player = (Player) PlayerHandler.players[j];
					player.getPA().movePlayer(2657, 2639, 0);
					if (won && player.pcDamage > 4) {
						player.getActionSender().sendMessage(
								"You have won the pest control game and have been awarded 4 pest control points.");
						player.pcPoints += 4;
						player.getPA().sendFrame126("@red@Pest Control Points: @or2@" + player.pcPoints, 7333);
						player.playerLevel[3] = player.getLevelForXP(player.playerXP[3]);
						player.playerLevel[5] = player.getLevelForXP(player.playerXP[5]);
						player.specAmount = 100;
						player.getItems().addItem(995, player.combatLevel * 50);
						player.getPA().refreshSkill(3);
						player.getPA().refreshSkill(5);
					} else if (won) {
						player.getActionSender().sendMessage("The void knights notice your lack of zeal.");
					} else {
						player.getActionSender().sendMessage(
								"You failed to kill all the portals in 5 minutes and have not been awarded any points.");
					}
					player.pcDamage = 0;
					player.getItems().addSpecialBar(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]);
					player.getCombat().resetPrayers();
					Portal1kill = 0;
					Portal2kill = 0;
					Portal3kill = 0;
					Portal4kill = 0;
				}
			}
		}

		for (int j = 0; j < NPCHandler.npcs.length; j++) {
			if (NPCHandler.npcs[j] != null) {
				if (NPCHandler.npcs[j].npcType >= 6142 && NPCHandler.npcs[j].npcType <= 6145)
					NPCHandler.npcs[j] = null;
			}
		}
	}

	public boolean allPortalsDead() {
		int count = 0;
		for (int j = 0; j < NPCHandler.npcs.length; j++) {
			if (NPCHandler.npcs[j] != null) {
				if (NPCHandler.npcs[j].npcType >= 6142 && NPCHandler.npcs[j].npcType <= 6145)
					if (NPCHandler.npcs[j].needRespawn)
						count++;
			}
		}
		return count >= 4;
	}

	public void movePlayer(int index) {
		Player player = (Player) PlayerHandler.players[index];
		if (player.combatLevel < 40) {
			player.getActionSender().sendMessage("You must be at least 40 to enter this boat.");
			return;
		}
		player.getPA().movePlayer(2658, 2611, 0);
	}

	public void spawnNpcs() {
		Server.npcHandler.spawnNpc2(6142, 2628, 2591, 0, 0, 200, 0, 0, 100);
		Server.npcHandler.spawnNpc2(6143, 2680, 2588, 0, 0, 200, 0, 0, 100);
		Server.npcHandler.spawnNpc2(6144, 2669, 2570, 0, 0, 200, 0, 0, 100);
		Server.npcHandler.spawnNpc2(6145, 2645, 2569, 0, 0, 200, 0, 0, 100);
	}

}