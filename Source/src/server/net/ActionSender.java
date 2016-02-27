package server.net;

import server.model.players.Player;

public class ActionSender {
	
	private Player player;
	
	public ActionSender(Player player) {
		this.player = player;
	}
	/**
	 * Sends a message to player.
	 * @param message	Message to send player.
	 * @return			The action sender instance, for chaining.
	 */
	public ActionSender sendMessage(String message) {
		player.outStream.createFrameVarSize(253);
		player.outStream.writeString(message);
		player.outStream.endFrameVarSize();
		return this;
	}
	/**
	 * Sends a string.
	 * 
	 * @param id
	 *            The interface id.
	 * @param string
	 *            The string.
	 * @return The action sender instance, for chaining.
	 */
	public ActionSender sendString(int id, String string) {
		player.getOutStream().createFrameVarSizeWord(126);
		player.getOutStream().writeString(string);
		player.getOutStream().writeWordA(id);
		player.getOutStream().endFrameVarSizeWord();
		player.flushOutStream();
		return this;
	}
	/**
	 * Sends a config toggle.
	 * @param id		The config id.
	 * @param state		On or off.
	 * @return			The action sender instance, for chaining.
	 */
	public ActionSender sendConfig(int id, int state) {
		player.getOutStream().createFrame(36);
		player.getOutStream().writeWordBigEndian(id);
		player.getOutStream().writeByte(state);
		player.flushOutStream();
		return this;
	}
	/**
	 * Sends a sidebar interface.
	 * @param sidebar		Sidebar id to change.
	 * @param interfaceId	Interface id to set sidebar to.
	 * @return				The action sender instance, for chaining.
	 */
	public ActionSender sendSidebarInterface(int sidebar, int interfaceId) {
		player.outStream.createFrame(71);
		player.outStream.writeWord(interfaceId);
		player.outStream.writeByteA(sidebar);
		return this;
	}
	public void attemptGroundGraphics(int i1, int i2) {
		player.outStream.createFrame(151);
		player.outStream.writeByteS(0);
		player.outStream.writeWordBigEndian(i1);
		player.outStream.writeByteS(i2);
	}

	public void cameraMovement(int i1, int i2, int i3, int i4, int i5) {
		player.outStream.createFrame(166);
		player.outStream.writeByte(i1);
		player.outStream.writeByte(i2);
		player.outStream.writeWord(i3);
		player.outStream.writeByte(i4);
		player.outStream.writeByte(i5);
	}

	public static void cancelAllAnimation(Player c) {
		c.outStream.createFrame(1);
	}

	public static void changeColourOnInterface(Player c, int i1, int i2) {
		c.outStream.createFrame(122);
		c.outStream.writeWordBigEndianA(i1);
		c.outStream.writeWordBigEndianA(i2);
	}

	public static void changeToSidebar(Player c, int i1) {
		c.outStream.createFrame(106);
		c.outStream.writeByteC(i1);
	}

	public static void controlMinimap(Player c, int i1) {
		c.outStream.createFrame(99);
		c.outStream.writeByte(i1);
	}

	public static void createGroundGraphics(Player c, int ID, int Y, int X) {
		sendCoordinates(c, Y - (c.mapRegionY * 8), X - (c.mapRegionX * 8));
		c.outStream.createFrame(4);
		c.outStream.writeByte(0);
		c.outStream.writeWord(ID);
		c.outStream.writeByte(0);
		c.outStream.writeWord(0);
	}

	public static void createGroundItem(Player c, int i1, int i2) {
		c.outStream.createFrame(44);
		c.outStream.writeWordBigEndianA(i1);
		c.outStream.writeWord(i2);
		c.outStream.writeByte(0);
	}

	public static void createInterfacePart(Player c, int offset, int interfaceId) {
		c.outStream.createFrame(70);
		c.outStream.writeWord(offset);
		c.outStream.writeWordBigEndian(0);
		c.outStream.writeWordBigEndian(interfaceId);
	}

	public static void displayItem(Player c, int frame, int item, int slot,
			int amount) {
		c.outStream.createFrameVarSizeWord(34);
		c.outStream.writeWord(frame);
		c.outStream.writeByte(slot);
		c.outStream.writeWord(item + 1);
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
		c.outStream.endFrameVarSizeWord();
	}

	public static void displayMutliIcon(Player c, int i1) {
		c.outStream.createFrame(61);
		c.outStream.writeByte(i1);
	}

	public static void doSomethingGroundGraphics(Player c, int i1) {
		c.outStream.createFrame(101);
		c.outStream.writeByteC(i1);
		c.outStream.writeByte(0);
	}

	public static void enterName(Player c) {
		c.outStream.createFrame(187);
	}

	public static void flashSelectedSidebar(Player c, int i1) {
		c.outStream.createFrame(24);
		c.outStream.writeByteA(i1);
	}

	public static void friendsListStatus(Player c, int i1) {
		c.outStream.createFrame(221);
		c.outStream.writeByte(i1);
	}

	public static void hidePrompt(Player c) {
		c.outStream.createFrame(27);
	}

	public static void hintIcons(Player c, int type, int id) {
		c.outStream.createFrame(254);
		c.outStream.writeByte(type);
		c.outStream.writeWord(id);
		c.outStream.write3Byte(0);
	}

	public static void interfaceAnimation(Player c, int MainFrame, int SubFrame) {
		c.outStream.createFrame(200); // What? - 2012
		c.outStream.writeWord(MainFrame);
		c.outStream.writeWord(SubFrame);
	}

	public static void interfaceMedia(Player c, int i1, int i2, int i3, int i4) {
		c.outStream.createFrame(230);
		c.outStream.writeWordA(i1);
		c.outStream.writeWord(i2);
		c.outStream.writeWord(i3);
		c.outStream.writeWordBigEndianA(i4);
	}

	public static void loadPrivateMessage(Player c, int i1, int i2) {
		c.outStream.createFrame(50);
		c.outStream.writeQWord(i1);
		c.outStream.writeByte(i2);
	}

	public static void logout(Player c) {
		c.outStream.createFrame(109);
	}

	public static void logout(Player c, int i1) {
		c.outStream.createFrame(72);
		c.outStream.writeWordBigEndian(i1);
	}

	public static void movingGraphics(Player c, int i1, int i2, int i3, int i4,
			int i5, int i6, int i7, int i8, int i9, int i10, int i11) {
		sendCoordinates(c, c.absX - (c.mapRegionY * 8), c.absY
				- (c.mapRegionX * 8));
		c.outStream.createFrame(117);
		c.outStream.writeByte(i1);
		c.outStream.writeByte(i2);
		c.outStream.writeByte(i3);
		c.outStream.writeWord(i4);
		c.outStream.writeWord(i5);
		c.outStream.writeByte(i6);
		c.outStream.writeByte(i7);
		c.outStream.writeWord(i8);
		c.outStream.writeWord(i9);
		c.outStream.writeByte(i10);
		c.outStream.writeByte(i11);
	}

	public void playerIsMember(int i1) {
		player.outStream.createFrame(249);
		player.outStream.writeByteA(i1);
		player.outStream.writeWordBigEndianA(player.getId());
	}

	public static void playerMedia(Player c, int Frame) {
		c.outStream.createFrame(185);
		c.outStream.writeWordBigEndianA(Frame);
	}

	public static void playMusic(Player c, int i1) {
		c.outStream.createFrame(74);
		c.outStream.writeWordBigEndian(i1);
	}

	public static void playSound(Player c, int i1, int i2, int i3) {
		c.outStream.createFrame(174);
		c.outStream.writeWord(i1);
		c.outStream.writeByte(i2);
		c.outStream.writeWord(i3);
	}

	public static void processRightClick(Player c, String s, int l, int k) {
		c.outStream.createFrameVarSize(104);
		c.outStream.writeByteC(l);
		c.outStream.writeByteA(k);
		c.outStream.writeString(s);
		c.outStream.endFrameVarSize();
	}

	public static void removeAllWindows(Player c) {
		c.outStream.createFrame(219);
	}

	public static void replaceAllnamesOnIgnore(Player c, long i1) {
		c.outStream.createFrame(214);
		c.outStream.writeQWord(i1);
	}

	public static void resetCamera(Player c) {
		c.outStream.createFrame(107);
	}

	public static void resetItems(Player c, int i1, int i2, int i3, int i4) {
		c.outStream.createFrame(53);
		c.outStream.writeWord(i1);
		c.outStream.writeWord(i2);
		c.outStream.writeByte(i3);
		c.outStream.writeWordBigEndianA(i4);
	}

	public static void resizeItem(Player c, int MainFrame, int SubFrame,
			int SubFrame2) {
		c.outStream.createFrame(246);// Not sure - 2012
		c.outStream.writeWordBigEndian(MainFrame);
		c.outStream.writeWord(SubFrame);
		c.outStream.writeWord(SubFrame2);
	}

	public static void resizeModel(Player c, int MainFrame, int SubFrame) {
		c.outStream.createFrame(75);
		c.outStream.writeWordBigEndianA(MainFrame);
		c.outStream.writeWordBigEndianA(SubFrame);
	}

	public static void sendCoordinates(Player c, int X, int Y) {
		c.outStream.createFrame(85);
		c.outStream.writeByteC(X);
		c.outStream.writeByteC(Y);
	}

	public static void sendMessage(Player c, String s) {
		c.outStream.createFrameVarSize(253);
		c.outStream.writeString(s);
		c.outStream.endFrameVarSize();
	}

	public static void sendQuestSomething(Player c, int id) {
		c.outStream.createFrame(79);
		c.outStream.writeWordBigEndian(id);
		c.outStream.writeWordA(0);
	}

	public static void sendString(Player c, String s, int id) {
		c.outStream.createFrameVarSizeWord(126);
		c.outStream.writeString(s);
		c.outStream.writeWordA(id);
		c.outStream.endFrameVarSizeWord();
	}

	public static void setChatOptions(Player c, int publicChat,
			int privateChat, int tradeBlock) {
		c.outStream.createFrame(206);
		c.outStream.writeByte(publicChat);
		c.outStream.writeByte(privateChat);
		c.outStream.writeByte(tradeBlock);
	}

	public static void setPlayerConfig(Player c, int i1, int i2) {
		c.outStream.createFrame(36);
		c.outStream.writeWordBigEndian(i1);
		c.outStream.writeByte(i2);
	}

	public static void setInterfaceVisibility(Player c, int MainFrame,
			int SubFrame) {
		c.outStream.createFrame(171);
		c.outStream.writeByte(MainFrame);
		c.outStream.writeWord(SubFrame);
	}

	public void shakeScreen(Player c, int i1) {
		c.outStream.createFrame(35);
		c.outStream.writeByte(i1);
		c.outStream.writeByte(i1);
		c.outStream.writeByte(i1);
		c.outStream.writeByte(i1);
	}

	public static void showInterface(Player c, int Frame) {
		c.outStream.createFrame(164);
		c.outStream.writeWordBigEndian_dup(Frame);
	}

	public static void skillLevel(Player c, int i1, int i2, int i3) {
		c.outStream.createFrame(134);
		c.outStream.writeByte(i1);
		c.outStream.writeDWord_v1(i2);
		c.outStream.writeByte(i3);
	}

	public static void somethingSideInterface(Player c, int MainFrame,
			int SubFrame) {
		c.outStream.createFrame(248);
		c.outStream.writeWordA(MainFrame);
		c.outStream.writeWord(SubFrame);
	}

	public static void spinCamera(Player c, int i1, int i2, int i3, int i4,
			int i5) {
		c.outStream.createFrame(177);
		c.outStream.writeByte(i1);
		c.outStream.writeByte(i2);
		c.outStream.writeWord(i3);
		c.outStream.writeByte(i4);
		c.outStream.writeByte(i5);
	}

	public static void systemUpdate(Player c, int i1) {
		c.outStream.createFrame(114);
		c.outStream.writeWordBigEndian(i1);
	}

	public static void turnPrivateChatOff(Player c) {
		c.outStream.createFrame(68);
	}

	public static void updatePlayer(Player c) {
		c.outStream.createFrame(81);
	}

	public static void viewInterface(Player c, int interfaceid) {
		c.outStream.createFrame(97);
		c.outStream.writeWord(interfaceid);
	}

	public static void walkableInterface(Player c, int ID) {
		c.outStream.createFrame(208);
		c.outStream.writeWordBigEndian_dup(ID);
	}

	public static void welcomeInterface(Player c, int i1, int i2, int i3,
			int i4, int i5) {
		c.outStream.createFrame(176);
		c.outStream.writeByteC(i1);
		c.outStream.writeByte(i2);
		c.outStream.writeWordA(i3);
		c.outStream.writeDWord_v2(i4);
		c.outStream.writeWord(i5);
	}

	public static void writeAmount(Player c, int amount) {
		c.outStream.writeByte(255);
		c.outStream.writeDWord(amount);
	}

	public static void writeInterfaceOverChat(Player c, int i1) {
		c.outStream.createFrame(218);
		c.outStream.writeWordBigEndianA(i1);
	}
}
