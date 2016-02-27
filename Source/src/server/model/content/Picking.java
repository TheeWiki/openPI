package server.model.content;

import java.util.ArrayList;

import server.Constants;
import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Player;
import server.util.Misc;

public class Picking {

	private static final int[][] DATA = { { 2646, 1779 } };

	public static ArrayList <int[]> flaxRemoved = new ArrayList<int[]>();
	
	public static void pickup(Player player, int object,  final int x, final int y) {
		if (System.currentTimeMillis() - player.buryDelay > Constants.TICK) {
			CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					for (int i = 0; i < DATA.length; i++) {
						if (player.getItems().freeSlots() != 0) {
				            player.getItems().addItem(1779, 1);
				            player.startAnimation(827);
							player.getActionSender().sendMessage("You pick some flax.");
							if (Misc.random(3) == 1) {
								flaxRemoved.add(new int[] { x, y });
								Server.objectManager.removeObject(x, y);
							}
						} else {
							player.getActionSender().sendMessage("Not enough space in your inventory.");
							return;
						}
					}
					container.stop();
				}
				@Override
				public void stop() {
				}
			}, 2);
		}
	}

	public static boolean isPickable(int object) {
		for (int i = 0; i < DATA.length; i++) {
			if (object == DATA[i][0]) {
				return true;
			}
		}
		return false;
	}
}
