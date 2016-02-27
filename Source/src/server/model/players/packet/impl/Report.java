package server.model.players.packet.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import server.model.players.Player;
import server.model.players.PlayerHandler;
import server.model.players.packet.PacketType;
import server.util.Misc;

/**
 *
 * Handles player abuse reports via the 'Report Abuse' interface.
 * Moderators are by default given the ability to file as many
 * abuse reports they manage to, contrary to the ordinary Players.
 *
 * Credits to Sir Raxim because of his release. I had forgotten some
 * important parts in my initial release.
 *
 * @author Nouish
 *
 */
public final class Report implements PacketType {

	@Override
	public void processPacket(Player player, int packetType, int packetSize) {
		/*
		 * Reads the reported username from the stream. Because the 317 protocol
		 * sent this as {@link Long} we must convert it to {@link String}.
		 * To do this, we require assistance from the almighty {@link Misc} class.
		 */
		String reportedName = Misc.longToPlayerName2(player.getInStream().readQWord());

		/*
		 * Reads the ident of the rule that the reported user supposedly broke.
		 */
		int brokenRuleIdent = player.getInStream().readUnsignedWordBigEndian();

		/*
		 * Gets the last time we filed an abuse report. If the Player didn't file
		 * one this session, the default 0L will be returned (to allow them to file it).
		 */
		long lastReport = Long.valueOf(player.getAttributes().getAttribute("lastReport", 0L).toString());

		/*
		 * Make sure we received a valid {@link brokenRuleIdent}.
		 */
		if (brokenRuleIdent < 0 || brokenRuleIdent > 12) {
			/*
			 * Player modification, or not in sync with the server.
			 * Not sure what to reply in this situation, actually.
			 */
			return;
		}

		/*
		 * Make sure they haven't filed an abuse report for at least 60 seconds, except
		 * if they are moderator+.
		 */
		if ((System.currentTimeMillis() - lastReport) < 60000 && player.playerRights < 1) {
			player.getActionSender().sendMessage("You can only report a player once every 60 seconds.");
			return;
		}

		/*
		 * Also, make sure nobody can report themselves. Why the hell would anyone report
		 * themself anyway? >.<
		 */
		if (reportedName.equalsIgnoreCase(player.playerName)) {
			player.getActionSender().sendMessage("You cannot report yourself!");
			return;
		}

		/*
		 * Because offline players can't really do anything, let's just make sure the
		 * Player reported is online ;)
		 */
		if (!PlayerHandler.isPlayerOn(reportedName)) {
			player.getActionSender().sendMessage("Cannot find " + Misc.formatPlayerName(reportedName) + ".");
			return;
		}

		/*
		 * Build the most basic information of the report.
		 */
		StringBuilder sb = new StringBuilder()
				.append(Misc.formatPlayerName(player.playerName))
				.append(" reported ")
				.append(Misc.formatPlayerName(reportedName))
				.append(" for ")
				.append(RULES[brokenRuleIdent])
				.append(".");

		/*
		 * Could be helpful to know when someone have been reported, so we print it to
		 * our batch/terminal window.
		 */
		System.out.println(sb.toString());

		/*
		 * Because it seems like we have successfully made sure the Player reported is
		 * currently online, we would like to log it to a .txt document.
		 */
		try {

			/*
			 * Firstly we must make an helpful instance of our abuse report directory.
			 */
			File directory = new File("./data/abuse-reports/");

			/*
			 * Secondly we have to make sure the directory exists.
			 */
			if (!directory.exists()) {
				/*
				 * In this case, it doesn't seem to exist. Let's attempt to make it :)
				 */
				if (!directory.mkdirs() && !directory.mkdir()) {
					/*
					 * Awkward; for some odd reason we were unable to do so, so we'll have
					 * to throw an {@link IOException} to stop the method from going further.
					 */
					throw new IOException("failed to create our report directory.");
				}
			}

			/*
			 * Because the directory seems to exist, we can make an instance of our soon to
			 * be abuse report document. It will be created soon.
			 */
			File file = new File(directory + File.separator + reportedName + " - " + getTime(true) + ".txt");

			/*
			 * We make an {@link FileWriter} instance to write our {@link file}. This also
			 * creates the document.
			 */
			FileWriter fw = new FileWriter(file);

			/*
			 * To "introduce" the file, we start out with the basics (who reported who).
			 */
			fw.write(sb.toString());

			/*
			 * Eventually we've logged some further proof; MESSAGES. Let's check.
			 */
			for (int i = chatMessages.length - 1; i >= 0; i--) {
				/*
				 * Sort out nulls, messages older than 60 seconds and not related to the
				 * two Players in the scene (the reported player + the one reporting).
				 */
				if (chatMessages[i] == null ||
						System.currentTimeMillis() - chatMessages[i].millis >= 60000 ||
						!reportedName.equalsIgnoreCase(chatMessages[i].name) &&
						!player.playerName.equalsIgnoreCase(chatMessages[i].name)) {
					continue;
				}
				/*
				 * Success! We have something that could be essential. Let's include that
				 * in our document.
				 */
				fw.write("\n[" + chatMessages[i].time + "] " + chatMessages[i].name + ": " + chatMessages[i].message);
			}

			/*
			 * With or without logged messages, it's time to close the document.
			 */
			fw.close();

		} catch (IOException exception) {

			/*
			 * Print the stack trace. Otherwise it'll be harder to find the issue quickly ;).
			 */
			exception.printStackTrace();

			/*
			 * Because the abuse could be crucial, we must also inform the Player.
			 */
			player.getActionSender().sendMessage("Failed to write the abuse report. Please contact an administrator.");

			/*
			 * Stop the method from going any further. We'd rather not tell the {@link Player}
			 * their report was filed properly, after telling them it wasn't ;)
			 */
			return;
		}

		/*
		 * Let's thank the {@link Player} for helping us rid cheaters and abusers :)
		 */
		player.getActionSender().sendMessage("Thank-you, your abuse report has been received.");

		/*
		 * Required to prevent people from spamming this function, by default they have to wait
		 * at least 60 seconds.
		 */
		player.getAttributes().setAttribute("lastReport", System.currentTimeMillis());

	}

	/**
	 * Logs an message sent by Players via public chat.
	 *
	 * @param name
	 *		The name of the Player who spoke.
	 * @param text
	 *		The message (what they said) in {@link byte}-form.
	 * @param len
	 *		The length of the message.
	 */
	public static void appendChat(String name, byte[] text, int len) {
		/*
		 * Because the abuse reports would make no sense if we didn't "scroll down"
		 * the older messages, we have to do so:
		 */
		for (int i = chatMessages.length - 1; i > 0; i--) {
			chatMessages[i] = chatMessages[i - 1];
		}
		/*
		 * Let's set slot zero to the most recent message we've received.
		 */
		chatMessages[0] = new ChatMessage(name, Misc.textUnpack(text, len));
	}

	/**
	 * Returns the current time (HH:mm:ss) in {@link String} format. Because we
	 * also use this method in our abuse report document name, we have to remove
	 * the ':'s (hence the {@link Boolean} b).
	 *
	 * @param b
	 *		Whether it's for the document name or not (true = document).
	 * @return
	 *		Current time of the day in HH:mm:ss format.
	 */
	private static String getTime(boolean b) {
		if (!b) {
			return new SimpleDateFormat("HH:mm:ss").format(new Date());
		}
		return getTime(false).replaceAll(":", "");
	}

	/*
	 * Class which represents an message sent by an {@link Player}.
	 */
	private static final class ChatMessage {

		/**
		 * Construct a new instance of this object.
		 *
		 * @param name
		 *		The name of the {@link Player} that spoke.
		 * @param message
		 *		The message they sent.
		 */
		private ChatMessage(String name, String message) {
			this.name = name;
			this.message = message;
			this.time = getTime(false);
			this.millis = System.currentTimeMillis();
		}

		/*
		 * The name of the {@link Player} that spoke.
		 */
		private final String name;

		/*
		 * The message they sent.
		 */
		private final String message;

		/*
		 * When they sent it.
		 */
		private final String time;

		/*
		 * When they sent it (this one is used to sort out old messages).
		 */
		private final long millis;
	}

	/*
	 * Expand the array if you have a huge playerbase. Five hundred should be
	 * more than enough for the regular server (10-20 players).
	 */
	private static final ChatMessage[] chatMessages = new ChatMessage[500];

	/*
	 * An array holding proper descriptions for the rules (because we receive
	 * an integer telling us what rule was supposedly broken).
	 */
	private static final String[] RULES = {
		"Offensive language", "Item scamming", "Password scamming",
		"Bug abuse", "Jagex staff impersonation", "Account sharing/trading",
		"Macroing", "Multiple logging in", "Encouraging others to break rules",
		"Misuse of customer support", "Advertising / website", "Real world item trading"
	};

}