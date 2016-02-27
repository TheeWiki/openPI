package server.model.dialogues;

import server.model.players.Player;

public class OptionDialogue {
	private String[] lines;

	public OptionDialogue(String[] lines) {
		this.lines = lines;
	}

	public void display(Player c) {
		c.getPA().showOptions(c, lines);
	}

	public String[] getLines() {
		return lines;
	}
}
