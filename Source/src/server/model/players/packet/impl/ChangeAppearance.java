package server.model.players.packet.impl;

import server.model.players.Player;
import server.model.players.packet.PacketType;

/**
 * Change appearance
 **/

public class ChangeAppearance implements PacketType {

	private static final int[][] MALE_VALUES = { { 0, 8 }, // head
			{ 10, 17 }, // jaw
			{ 18, 25 }, // torso
			{ 26, 31 }, // arms
			{ 33, 34 }, // hands
			{ 36, 40 }, // legs
			{ 42, 43 }, // feet
	};

	private static final int[][] FEMALE_VALUES = { { 45, 54 }, // head
			{ -1, -1 }, // jaw
			{ 56, 60 }, // torso
			{ 61, 65 }, // arms
			{ 67, 68 }, // hands
			{ 70, 77 }, // legs
			{ 79, 80 }, // feet
	};

	private static final int[][] ALLOWED_COLORS = { { 0, 11 }, // hair color
			{ 0, 15 }, // torso color
			{ 0, 15 }, // legs color
			{ 0, 5 }, // feet color
			{ 0, 7 } // skin color
	};

	@Override
	public void processPacket(final Player Player, final int packetType, final int packetSize) {
		final int gender = Player.getInStream().readSignedByte();

		if (gender != 0 && gender != 1)
			return;

		final int[] apperances = new int[MALE_VALUES.length]; // apperance's
																// value
		// check
		for (int i = 0; i < apperances.length; i++) {
			int value = Player.getInStream().readSignedByte();
			if (value < (gender == 0 ? MALE_VALUES[i][0] : FEMALE_VALUES[i][0]) || value > (gender == 0 ? MALE_VALUES[i][1] : FEMALE_VALUES[i][1]))
				value = (gender == 0 ? MALE_VALUES[i][0] : FEMALE_VALUES[i][0]);
			apperances[i] = value;
		}

		final int[] colors = new int[ALLOWED_COLORS.length]; // color value
																// check
		for (int i = 0; i < colors.length; i++) {
			int value = Player.getInStream().readSignedByte();
			if (value < ALLOWED_COLORS[i][0] || value > ALLOWED_COLORS[i][1])
				value = ALLOWED_COLORS[i][0];
			colors[i] = value;
		}

		if (Player.canChangeAppearance) {
			Player.playerAppearance[0] = gender; // gender
			Player.playerAppearance[1] = apperances[0]; // head
			Player.playerAppearance[2] = apperances[2]; // torso
			Player.playerAppearance[3] = apperances[3]; // arms
			Player.playerAppearance[4] = apperances[4]; // hands
			Player.playerAppearance[5] = apperances[5]; // legs
			Player.playerAppearance[6] = apperances[6]; // feet
			Player.playerAppearance[7] = apperances[1]; // beard
			Player.playerAppearance[8] = colors[0]; // hair colour
			Player.playerAppearance[9] = colors[1]; // torso colour
			Player.playerAppearance[10] = colors[2]; // legs colour
			Player.playerAppearance[11] = colors[3]; // feet colour
			Player.playerAppearance[12] = colors[4]; // skin colour

			Player.getPA().removeAllWindows();
			Player.getPA().requestUpdates();
			Player.canChangeAppearance = false;
		}
	}
}