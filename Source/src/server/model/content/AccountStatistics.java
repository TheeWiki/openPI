package server.model.content;

import server.model.players.Client;
import server.util.Misc;
import server.util.impl.ClearInterface;

public class AccountStatistics implements ClearInterface {

	@Override
	public int getInterfaceID() {
		return 6308;
	}

	@Override
	public void clearInterface(Client c) {
		for (int i = 6402; i < 6411; i++) {
			c.getPA().sendFrame126("", i);
		}
		for (int i = 8578; i < 8618; i++) {
			c.getPA().sendFrame126("", i);
		}
		c.getPA().showInterface(getInterfaceID());
		c.flushOutStream();
	}

	@Override
	public void setNewText(Client c) {
		clearInterface(c);
		//some variables don't save for some reason, look into quickly if you have the time
		c.getPA().sendFrame126(Misc.formatPlayerName(c.playerName) + "'s official account statistics log", 6400);

		c.getPA().sendFrame126("I have Killed " + Misc.formatNumbers(c.kills) + " players", 6402);
		c.getPA().sendFrame126("I have Died " + Misc.formatNumbers(c.deaths) + " times", 6403);
		c.getPA().sendFrame126("I have won " + Misc.formatNumbers(c.duelWins)  + " Duel Arena fights", 6404);
		c.getPA().sendFrame126("I have lost " + Misc.formatNumbers(c.duelLoses)  + " Duel Arena fights", 6405);
		c.getPA().sendFrame126("I have teleported home " + Misc.formatNumbers(c.teleHome)  + " times", 6406);
		c.getPA().sendFrame126("I have used " + Misc.formatNumbers(c.specsUsed) + " special attacks", 6407);
		c.getPA().sendFrame126("I have eaten " + Misc.formatNumbers(c.foodEaten)  + " pieces of food", 6408);
		c.getPA().sendFrame126("I have siped " + Misc.formatNumbers(c.potsDrank) + " potions", 6409);
		c.getPA().sendFrame126("I have " + Misc.formatNumbers(c.pkPoints) + " PVP Points", 6410);
		c.getPA().sendFrame126("N/A", 6411);
		c.getPA().sendFrame126("I have killed Callisto " /*+ Misc.formatNumbers(TODO) */ + " times", 8578);
		c.getPA().sendFrame126("I have killed the Chaotic Fanatic " /*+ Misc.formatNumbers(TODO) */ + " times", 8579);
		c.getPA().sendFrame126("I have killed the Flock Leader Gerin " /*+ Misc.formatNumbers(TODO) */ + " times", 8580);
		c.getPA().sendFrame126("I have killed the General Graardor " /*+ Misc.formatNumbers(TODO) */ + " times", 8581);
		c.getPA().sendFrame126("I have killed Kree'arra " /*+ Misc.formatNumbers(TODO) */ + " times", 8582);
		c.getPA().sendFrame126("I have killed Vetion " /*+ Misc.formatNumbers(TODO) */ + " times", 8582);
		c.getPA().sendFrame126("I have killed Zulrah " /*+ Misc.formatNumbers(TODO) */ + " times", 8583);
		c.getPA().sendFrame126("I have killed Zamorak " /*+ Misc.formatNumbers(TODO) */ + " times", 8584);
		c.getPA().sendFrame126("I have killed Saradomin " /*+ Misc.formatNumbers(TODO) */ + " times", 8585);
		c.getPA().sendFrame126("I have casted Vengeance " /*+ Misc.formatNumbers(TODO) */ + " times", 8586);
		c.getPA().sendFrame126("I have performed " + Misc.formatNumbers(c.emotesPerformed)  + " emotes", 8587);
		c.getPA().sendFrame126("I have voted " + Misc.formatNumbers(c.timesVoted)  + " times", 8588);
		c.getPA().sendFrame126("I have claimed my vote " + Misc.formatNumbers(c.votesClaimed)  + " times", 8589);
		c.getPA().sendFrame126("I have been muted " + Misc.formatNumbers(c.timesMuted) + " times", 8590);
		c.getPA().sendFrame126("I have been banned " + Misc.formatNumbers(c.timesBanned)  + " times", 8591);
		c.getPA().sendFrame126("I have activated " + Misc.formatNumbers(c.prayersAcivated)  + " prayers", 8592);
		c.getPA().sendFrame126("I have completed barrows " /*+ Misc.formatNumbers(TODO) */ + " times", 8593);
		c.getPA().sendFrame126("I have completed Fight Caves " /*+ Misc.formatNumbers(TODO) */ + " times", 8594);
		c.getPA().sendFrame126("I have won " /*+ Misc.formatNumbers(TODO) */ + " Fight Pits fights", 8595);
		c.getPA().sendFrame126("I have completed " /*+ Misc.formatNumbers(TODO) */ + " Pest Control Matches", 8596);
		c.getPA().sendFrame126("I have " /*+ Misc.formatNumbers(TODO) */ + " OSRS-PVP Points", 8597);
		c.getPA().sendFrame126("I have spawned over " /*+ Misc.formatNumbers(TODO) */ + " items", 8598);
		c.getPA().sendFrame126("I have combined " + Misc.formatNumbers(c.itemsUpgraded) + " items together", 8599);
		c.getPA().sendFrame126("I have opened " + Misc.formatNumbers(c.mbOpened) + " Mystery Boxes", 8600); 
	}
	@Override
	public void appendNewInterface(Client c) {
		c.getPA().showInterface(getInterfaceID());
		setNewText(c);
	}

	/*
	 * private static int[] text = { 6402, 6403, 6404, 6405, 6406, 6407, 6408,
	 * 6409, 6410, 6411, 8578, 8579, 8580, 8581, 8582, 8583, 8584, 8585, 8586,
	 * 8587, 8588, 8589, 8590, 8591, 8592, 8593, 8594, 8595, 8596, 8597, 8598,
	 * 8599, 8600, 8601, 8602, 8603, 8604, 8605, 8606, 8607, 8608, 8609, 8610,
	 * 8611, 8612, 8613, 8614, 8615, 8616, 8617, 6718 };
	 */
}