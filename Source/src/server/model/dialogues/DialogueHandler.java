package server.model.dialogues;

import server.model.players.Player;

public class DialogueHandler {

	private Player c;
	
	public DialogueHandler(Player Player) {
		this.c = Player;
	}
	
	/**
	 * Handles all talking
	 * @param dialogue The dialogue you want to use
	 * @param npcId The npc id that the chat will focus on during the chat
	 */
	public void sendDialogues(int dialogue, int npcId) {
		c.talkingNpc = npcId;
		switch(dialogue) {
		case 0:
			c.talkingNpc = -1;
			c.getPA().removeAllWindows();
			c.nextChat = 0;
			break;
		case 1:
			sendStatement("You found a hidden tunnel! Do you want to enter it?");
			c.nextChat = 2;
			break;
		case 2:
			DialogueContainer.CreateDialogue(c, new DialogueAction() {
				@Override
				public void execute(DialogueContainer container) {
					switch (container.getOptionId()) {
					case 1: 
						c.getPA().spellTeleport(2676, 3715, 0);
						break;
					}
				}
			}, "I'm fearless, enter now", "Nevermind");
			break;
		case 483: //competition judge
			sendNpcChat4("Hello!", "I'm the competition judge of the Ranging Guild.", "You can buy shots from me and shoot the targets", "for points. You can exchange the points at me.", 693, "Judge");
			c.nextChat = 484;
			break;
			
		case 484: 
			sendNpcChat1("What would you like to do/ask?", 693, "Judge");
			c.nextChat = 485;
			break;
			
		case 485:
			sendOption4("I would like to buy shots.", "I would like to exchange my points.", "How am I doing right now?", "Never mind.");
			c.dialogueAction = 485;
			break;
			// training
		case 3:
			DialogueContainer.CreateDialogue(c, new DialogueAction() {
				@Override
				public void execute(DialogueContainer container) {
					switch (container.getOptionId()) {
					case 1: 
						if (container.getCurrentDialogueId() == 2)
						{
							c.getPA().spellTeleport(2895, 2727, 0);
						}
						c.getPA().spellTeleport(2676, 3715, 0);
						break;
					case 2:
						if (container.getCurrentDialogueId() == 2)
						{
							c.getPA().spellTeleport(2710, 9466, 0);
						}
						c.getPA().spellTeleport(2737, 3479, 0);
						break;
					case 3: 
						c.getPA().spellTeleport(3428, 3537, 0);
						break;
					case 4:
						c.getPA().spellTeleport(2884, 9798, 0);
						break;
					case 5:
						container.showOptions(2);
						break;
					}
					
				}
				@Override
				public void preExecution(DialogueContainer container) {
					container.addAdditionalOptions("Lost Island","Brimhaven Dungeon");
				}
			}, "Rock Crabs", "Moss Giants", "Slayer Tower", "Taverly Dungeon", "More");
			break;
		case 4:
			DialogueContainer.CreateDialogue(c, new DialogueAction() {
				@Override
				public void execute(DialogueContainer container) {
					switch (container.getOptionId()) {
					case 1:
						c.getPA().spellTeleport(3542, 3314, 0);
						break;
					case 2: // pc
						c.getPA().spellTeleport(2658, 2661, 0);
						break;
					case 3:
						c.getPA().spellTeleport(2447, 5171, 0);
						break;
					case 4:
						c.getPA().spellTeleport(2435, 5172, 0);
						c.sendMessage("Walk west from here.");
						break;
					case 5:
						c.getPA().spellTeleport(2457, 3090, 0);
						break;
					}
				}
			}, "Barrows","Pest Control", "Fight Caves", "Fight Pits", "Castle Wars");
			break;
		case 5:
			DialogueContainer.CreateDialogue(c, new DialogueAction() {
				@Override
				public void execute(DialogueContainer container) {
					switch (container.getOptionId()) {
					case 1: // barrows
						c.getPA().spellTeleport(3542, 3314, 0);
						break;
					case 2: // pc
						c.getPA().spellTeleport(2658, 2661, 0);
						break;
					case 3:
						c.getPA().spellTeleport(2447, 5171, 0);
						break;
					case 4:
						c.getPA().spellTeleport(2435, 5172, 0);
						c.sendMessage("Walk west from here.");
						break;
					case 5:
						container.showOptions(2);
						break;
					}
				}
				@Override
				public void preExecution(DialogueContainer container) {
					container.addAdditionalOptions("Farming", "Thieving" , "Runecrafting");
				}
			}, "Woodcutting","Fishing", "Mining", "Agility", "More");
			break;
	}
}

	
	/*
	 * Information Box
	 */
	
	public void sendStartInfo(String text, String text1, String text2, String text3, String title) {
		c.getPA().sendFrame126(title, 6180);
		c.getPA().sendFrame126(text, 6181);
		c.getPA().sendFrame126(text1, 6182);
		c.getPA().sendFrame126(text2, 6183);
		c.getPA().sendFrame126(text3, 6184);
		c.getPA().sendFrame164(6179);
	}
	
	/*
	 * Options
	 */
	
	public void sendOption(String s) {
		c.getPA().sendFrame126("Select an Option", 2470);
	 	c.getPA().sendFrame126(s, 2471);
		c.getPA().sendFrame126("Click here to continue", 2473);
		c.getPA().sendFrame164(13758);
	}	
	
	public void sendOption2(String s, String s1) {
		c.getPA().sendFrame126("Select an Option", 2460);
		c.getPA().sendFrame126(s, 2461);
		c.getPA().sendFrame126(s1, 2462);
		c.getPA().sendFrame164(2459);
	}
	
	public void sendOption3(String s, String s1, String s2) {
		c.getPA().sendFrame126("Select an Option", 2470);
		c.getPA().sendFrame126(s, 2471);
		c.getPA().sendFrame126(s1, 2472);
		c.getPA().sendFrame126(s2, 2473);
		c.getPA().sendFrame164(2469);
	}
	
	public void sendOption4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame126("Select an Option", 2481);
		c.getPA().sendFrame126(s, 2482);
		c.getPA().sendFrame126(s1, 2483);
		c.getPA().sendFrame126(s2, 2484);
		c.getPA().sendFrame126(s3, 2485);
		c.getPA().sendFrame164(2480);
	}
	
	public void sendOption5(String s, String s1, String s2, String s3, String s4) {
		c.getPA().sendFrame126("Select an Option", 2493);
		c.getPA().sendFrame126(s, 2494);
		c.getPA().sendFrame126(s1, 2495);
		c.getPA().sendFrame126(s2, 2496);
		c.getPA().sendFrame126(s3, 2497);
		c.getPA().sendFrame126(s4, 2498);
		c.getPA().sendFrame164(2492);
	}

	/*
	 * Statements
	 */
	
	public void sendStatement(String s) { // 1 line click here to continue chat box interface
		c.getPA().sendFrame126(s, 357);
		c.getPA().sendFrame126("Click here to continue", 358);
		c.getPA().sendFrame164(356);
	}
	
	/*
	 * Npc Chatting
	 */
	
	public void sendNpcChat1(String s, int ChatNpc, String name) {
		c.getPA().sendFrame200(4883, 591);
		c.getPA().sendFrame126(name, 4884);
		c.getPA().sendFrame126(s, 4885);
		c.getPA().sendFrame75(ChatNpc, 4883);
		c.getPA().sendFrame164(4882);
	}
	
	public void sendNpcChat2(String s, String s1, int ChatNpc, String name) {
		c.getPA().sendFrame200(4888, 591);
		c.getPA().sendFrame126(name, 4889);
		c.getPA().sendFrame126(s, 4890);
		c.getPA().sendFrame126(s1, 4891);
		c.getPA().sendFrame75(ChatNpc, 4888);
		c.getPA().sendFrame164(4887);
	}

	public void sendNpcChat3(String s, String s1, String s2, int ChatNpc, String name) {
		c.getPA().sendFrame200(4894, 591);
		c.getPA().sendFrame126(name, 4895);
		c.getPA().sendFrame126(s, 4896);
		c.getPA().sendFrame126(s1, 4897);
		c.getPA().sendFrame126(s2, 4898);
		c.getPA().sendFrame75(ChatNpc, 4894);
		c.getPA().sendFrame164(4893);
	}
	
	public void sendNpcChat4(String s, String s1, String s2, String s3, int ChatNpc, String name) {
		c.getPA().sendFrame200(4901, 591);
		c.getPA().sendFrame126(name, 4902);
		c.getPA().sendFrame126(s, 4903);
		c.getPA().sendFrame126(s1, 4904);
		c.getPA().sendFrame126(s2, 4905);
		c.getPA().sendFrame126(s3, 4906);
		c.getPA().sendFrame75(ChatNpc, 4901);
		c.getPA().sendFrame164(4900);
	}
	
	/*
	 * Player Chating Back
	 */
	
	public void sendPlayerChat1(String s) {
		c.getPA().sendFrame200(969, 591);
		c.getPA().sendFrame126(c.playerName, 970);
		c.getPA().sendFrame126(s, 971);
		c.getPA().sendFrame185(969);
		c.getPA().sendFrame164(968);
	}
	
	public void sendPlayerChat2(String s, String s1) {
		c.getPA().sendFrame200(974, 591);
		c.getPA().sendFrame126(c.playerName, 975);
		c.getPA().sendFrame126(s, 976);
		c.getPA().sendFrame126(s1, 977);
		c.getPA().sendFrame185(974);
		c.getPA().sendFrame164(973);
	}
	
	public void sendPlayerChat3(String s, String s1, String s2) {
		c.getPA().sendFrame200(980, 591);
		c.getPA().sendFrame126(c.playerName, 981);
		c.getPA().sendFrame126(s, 982);
		c.getPA().sendFrame126(s1, 983);
		c.getPA().sendFrame126(s2, 984);
		c.getPA().sendFrame185(980);
		c.getPA().sendFrame164(979);
	}
	
	public void sendPlayerChat4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame200(987, 591);
		c.getPA().sendFrame126(c.playerName, 988);
		c.getPA().sendFrame126(s, 989);
		c.getPA().sendFrame126(s1, 990);
		c.getPA().sendFrame126(s2, 991);
		c.getPA().sendFrame126(s3, 992);
		c.getPA().sendFrame185(987);
		c.getPA().sendFrame164(986);
	}
	public void sendStatementTwo(String s) {
		c.getPA().sendFrame126(s, 357);
		c.getPA().sendFrame126("Click here to continue", 358);
		c.getPA().sendFrame164(356);
	}
}
