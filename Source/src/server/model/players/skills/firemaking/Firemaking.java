package server.model.players.skills.firemaking;

import java.util.HashMap;
import java.util.Map;

import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Client;
import server.util.Misc;

public class Firemaking {

	private static enum Data {
		NORMAL(1511, 100, 1), 
		OAK(1521, 200, 15), 
		WILLOW(1519, 300, 30), 
		MAPLE(1517, 400, 45),
		YEW(1515, 500, 60), 
		MAGIC(1513, 600, 75);

		Data(int logId, int EXP, int levelREQ) {
			this.logId = logId;
			this.EXP = EXP;
			this.levelREQ = levelREQ;
		}

		private int logId, EXP, levelREQ;

		private int getLogId() {
			return logId;
		}

		private int getEXP() {
			return EXP;
		}

		private int getLevelREQ() {
			return levelREQ;
		}

		private static Map<Object, Data> data = new HashMap<Object, Data>();

		static {
			for (Data d : Data.values()) {
				Data.data.put(d.getLogId(), d);
			}
		}
	}
 
	public static boolean handleTinderBox(Client player, int itemUsed, int itemUsedWith) {
 
		if (itemUsed == 590) {
			final Data data = Data.data.get(itemUsedWith);
			if (itemUsedWith == data.getLogId()) {
				attemptToLight(player, itemUsedWith, player.getItems().getItemSlot(itemUsedWith));
				return true;
			}
 
		} else if (itemUsedWith == 590) {
			final Data data = Data.data.get(itemUsed);
			if (itemUsed == data.getLogId()) {
				attemptToLight(player, itemUsed, player.getItems().getItemSlot(itemUsed));
				return true;
			}
		}
		return false;
	}
 
	private static void attemptToLight(final Client player, final int logId, final int slot) {
		final Data data = Data.data.get(logId);
		
		if (data == null) {
			return;
		}
		
		double multiplyFailChance = Misc.random(player.playerLevel[11]) / 10;
		final int chanceToLight = Misc.random(player.playerLevel[11]) + 1;
		final double chanceToFail = Misc.random(9) + (multiplyFailChance);
 
		if (!player.getIsSkilling()) {
			 
			if (player.playerLevel[player.playerFiremaking] >= data.getLevelREQ()) {
				player.getItems().deleteItem(data.getLogId(), slot, 1);
 
				Server.itemHandler.createGroundItem(player, data.getLogId(), player.getX(), player.getY(), 1, player.playerId);
				player.startAnimation(733, 0);
 
				player.setIsSkilling(true);
				
				CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
					int failTimer = 10;
					@Override
					public void execute(CycleEventContainer container) {
						/**
						 * If the player walks away from the fire, or somehow stops skilling, 
						 * we end the tick.
						 */
						if (!player.getIsSkilling()) {
							player.startAnimation(65535);
							stop();
							return;
						}
 
						if (chanceToLight >= chanceToFail && failTimer != 0) {
							lightFire(player, data.getLogId());
							player.startAnimation(65535);
							stop();
						}
 
						if (failTimer == 0) {
							player.sendMessage("You have failed to light the fire!");
							player.setIsSkilling(false);
							player.startAnimation(65535);
							stop();
							return;
						}
						failTimer--;
						container.stop();
					}

					@Override
					public void stop() {

					}
				}, 3);
			} else {
				player.sendMessage("You need a Firemaking level of at least " + data.getLevelREQ() + " to burn this log.");
			}
		}
	}
 
	private static void lightFire(final Client player, int logId) {
		final Data data = Data.data.get(logId);
		
		if (data == null) {
			return;
		}
 
		final int[] coords = new int[2];
		coords[0] = player.absX;
		coords[1] = player.absY;
		
		player.getPA().addSkillXP(data.getEXP(), player.playerFiremaking);
		
		Server.itemHandler.removeGroundItem(player, data.getLogId(), coords[0], coords[1], false);
		Server.objectHandler.createAnObject(player, 2732, coords[0], coords[1]);
 
		player.getPA().walkTo(-1, 0);
 
		player.turnPlayerTo(player.absX + 1, player.absY);
		
		player.sendMessage("You light the logs.");

		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				player.setIsSkilling(false);
				container.stop();
			}

			@Override
			public void stop() {

			}
		}, 2);
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			@Override
			public void execute(CycleEventContainer container) {
				Server.objectHandler.createAnObject(player, -1, coords[0], coords[1]);
				Server.itemHandler.createGroundItem(player, 592, coords[0], coords[1], 1, player.getId());
				container.stop();
			}

			@Override
			public void stop() {

			}
		}, 45);
	}
}