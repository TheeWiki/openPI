package server.world.cities.impl;

import server.model.players.Client;
import server.world.Location;

public class Home {
	
	public static void executeHomeActions(Client c, int object) {
		switch (object) {
		}
	}

	public static void sendSecondClickObject(Client c, int object) {
		switch (object) {
		}
	}

	public static void sendThirdClickObject(Client c, int object) {
		switch (object) {
		}
	}

	public static void sendFirstClickNpc(Client c, int npc) {
		switch (npc) {
		case 599: 
			c.getPA().showInterface(3559); 
			c.canChangeAppearance = true;
		break;
		}
	}

	public static void sendSecondClickNpc(Client c, int npc) {
		switch (npc) {
		}
	}

	public static void sendThirdClickNpc(Client c, int npc) {
		switch (npc) {
		}
	}
}
