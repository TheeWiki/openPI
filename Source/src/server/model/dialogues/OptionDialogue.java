package server.model.dialogues;

import server.model.players.Client;

public class OptionDialogue {
	private String[] lines;

	public OptionDialogue(String[] lines) {
		this.lines = lines;
	}

	public void display(Client c) {
		c.getPA().showOptions(c, lines);
	}

	public String[] getLines() {
		return lines;
	}
}
