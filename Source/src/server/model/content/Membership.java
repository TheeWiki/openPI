package server.model.content;

import java.util.Calendar;
import java.util.GregorianCalendar;

import server.model.players.Player;
import server.model.players.PlayerSave;

public class Membership {

	private int getTodayDate(Player c) {
		Calendar cal = new GregorianCalendar();
		@SuppressWarnings("unused")
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		@SuppressWarnings("unused")
		int year = cal.get(Calendar.YEAR);
		return (month);
	}

	public void giveMembership(Player c) {
		c.startDate = getTodayDate(c);
		c.membership = true;
		PlayerSave.saveGame(c);
		c.sendMessage("@yel@You have just recieved a month membership!");
	}
	/**
	 * TODO: Fix printout
	 * @param c
	 */
	public void checkDate(Player c) {
		if (c.membership = true)
		{
			c.sendMessage("@blu@You have "+getDaysLeft(c) + " days of membership left.");
		}
		if(c.membership && c.startDate <= 0) {
			c.startDate = getTodayDate(c);
			PlayerSave.saveGame(c);
		} else if(c.membership && getDaysLeft(c) <= 0) {
			c.membership = false;
			c.startDate = -1;
			PlayerSave.saveGame(c);
			c.sendMessage("@red@Your account isn't a member, type ::buymembership or visit keldagrim.com");
		}
	}

	public int getDaysLeft(Player c) {
		return (31 - (getTodayDate(c) - c.startDate));
	}
}