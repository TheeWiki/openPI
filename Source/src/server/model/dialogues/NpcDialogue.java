package server.model.dialogues;

import server.model.players.Client;

public abstract class NpcDialogue {
	private int nextDialogueId = 0;

	private int optionId;

	public abstract void sendDialogue(Client player, int dialogueId);

	public abstract void executeOption(Client player, int buttonId);

	public int getNextDialogueId() {
		return nextDialogueId;
	}

	public void setNextDialogueId(final int nextDialogueId) {
		this.nextDialogueId = nextDialogueId;
	}

	public int getOptionId() {
		return optionId;
	}

	public void setOptionId(final int optionId) {
		this.optionId = optionId;
	}
}
