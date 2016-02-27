package server.model.content;

import java.util.Calendar;
import java.util.GregorianCalendar;

import server.model.players.Player;
import server.model.players.PlayerSave;

public class Membership {

	private int getTodayDate(Player player) {
		Calendar cal = new GregorianCalendar();
		@SuppressWarnings("unused")
		int day = cal.get(Calendar.DAY_OF_MONTH);
		int month = cal.get(Calendar.MONTH);
		@SuppressWarnings("unused")
		int year = cal.get(Calendar.YEAR);
		return (month);
	}

	public void giveMembership(Player player) {
		player.startDate = getTodayDate(player);
		player.membership = true;
		PlayerSave.saveGame(player);
		player.getActionSender().sendMessage("@yel@You have just recieved a month membership!");
	}
	/**
	 * TODO: Fix printout
	 * @param player
	 */
	public void checkDate(Player player) {
		if (player.membership = true)
		{
			player.getActionSender().sendMessage("@blu@You have "+getDaysLeft(player) + " days of membership left.");
		}
		if(player.membership && player.startDate <= 0) {
			player.startDate = getTodayDate(player);
			PlayerSave.saveGame(player);
		} else if(player.membership && getDaysLeft(player) <= 0) {
			player.membership = false;
			player.startDate = -1;
			PlayerSave.saveGame(player);
			player.getActionSender().sendMessage("@red@Your account isn't a member, type ::buymembership or visit keldagrim.com");
		}
	}

	public int getDaysLeft(Player player) {
		return (31 - (getTodayDate(player) - player.startDate));
	}
}