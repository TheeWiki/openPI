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
	
	public static void pickup(Player c, int object,  final int x, final int y) {
		if (System.currentTimeMillis() - c.buryDelay > Constants.TICK) {
			CycleEventHandler.getSingleton().addEvent(c, new CycleEvent() {
				@Override
				public void execute(CycleEventContainer container) {
					for (int i = 0; i < DATA.length; i++) {
						if (c.getItems().freeSlots() != 0) {
				            c.getItems().addItem(1779, 1);
				            c.startAnimation(827);
							c.sendMessage("You pick some flax.");
							if (Misc.random(3) == 1) {
								flaxRemoved.add(new int[] { x, y });
								Server.objectManager.removeObject(x, y);
							}
						} else {
							c.sendMessage("Not enough space in your inventory.");
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
