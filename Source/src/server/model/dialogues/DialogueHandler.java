package server.model.dialogues;

import server.model.minigames.barrows.Dungeon;
import server.model.players.Player;

public class DialogueHandler {

	private Player player;
	
	public DialogueHandler(Player Player) {
		this.player = Player;
	}
	
	/**
	 * Handles all talking
	 * @param dialogue The dialogue you want to use
	 * @param npcId The npc id that the chat will focus on during the chat
	 */
	public void sendDialogues(int dialogue, int npcId) {
		player.talkingNpc = npcId;
		switch(dialogue) {
		case 0:
			player.talkingNpc = -1;
			player.getPA().removeAllWindows();
			player.nextChat = 0;
			break;
		case 1:
			sendStatement("You found a hidden tunnel! Do you want to enter it?");
			player.nextChat = 2;
			break;
		case 2:
			DialogueContainer.CreateDialogue(player, new DialogueAction() {
				@Override
				public void execute(DialogueContainer container) {
					switch (container.getOptionId()) {
					case 1: 
						Dungeon.enterDungeon(player);
						break;
					}
				}
			}, "I'm fearless, enter now", "Nevermind");
			break;
		case 483: //competition judge
			sendNpcChat4("Hello!", "I'm the competition judge of the Ranging Guild.", "You can buy shots from me and shoot the targets", "for points. You can exchange the points at me.", 693, "Judge");
			player.nextChat = 484;
			break;
			
		case 484: 
			sendNpcChat1("What would you like to do/ask?", 693, "Judge");
			player.nextChat = 485;
			break;
			
		case 485:
			sendOption4("I would like to buy shots.", "I would like to exchange my points.", "How am I doing right now?", "Never mind.");
			player.dialogueAction = 485;
			break;
			// training
		case 3:
			DialogueContainer.CreateDialogue(player, new DialogueAction() {
				@Override
				public void execute(DialogueContainer container) {
					switch (container.getOptionId()) {
					case 1: 
						if (container.getCurrentDialogueId() == 2)
						{
							player.getPA().spellTeleport(2895, 2727, 0);
						}
						player.getPA().spellTeleport(2676, 3715, 0);
						break;
					case 2:
						if (container.getCurrentDialogueId() == 2)
						{
							player.getPA().spellTeleport(2710, 9466, 0);
						}
						player.getPA().spellTeleport(2737, 3479, 0);
						break;
					case 3: 
						player.getPA().spellTeleport(3428, 3537, 0);
						break;
					case 4:
						player.getPA().spellTeleport(2884, 9798, 0);
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
			DialogueContainer.CreateDialogue(player, new DialogueAction() {
				@Override
				public void execute(DialogueContainer container) {
					switch (container.getOptionId()) {
					case 1:
						player.getPA().spellTeleport(3542, 3314, 0);
						break;
					case 2: // pc
						player.getPA().spellTeleport(2658, 2661, 0);
						break;
					case 3:
						player.getPA().spellTeleport(2447, 5171, 0);
						break;
					case 4:
						player.getPA().spellTeleport(2435, 5172, 0);
						player.getActionSender().sendMessage("Walk west from here.");
						break;
					case 5:
						player.getPA().spellTeleport(2457, 3090, 0);
						break;
					}
				}
			}, "Barrows","Pest Control", "Fight Caves", "Fight Pits", "Castle Wars");
			break;
		case 5:
			DialogueContainer.CreateDialogue(player, new DialogueAction() {
				@Override
				public void execute(DialogueContainer container) {
					switch (container.getOptionId()) {
					case 1: // barrows
						player.getPA().spellTeleport(3542, 3314, 0);
						break;
					case 2: // pc
						player.getPA().spellTeleport(2658, 2661, 0);
						break;
					case 3:
						player.getPA().spellTeleport(2447, 5171, 0);
						break;
					case 4:
						player.getPA().spellTeleport(2435, 5172, 0);
						player.getActionSender().sendMessage("Walk west from here.");
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
		player.getPA().sendFrame126(title, 6180);
		player.getPA().sendFrame126(text, 6181);
		player.getPA().sendFrame126(text1, 6182);
		player.getPA().sendFrame126(text2, 6183);
		player.getPA().sendFrame126(text3, 6184);
		player.getPA().sendFrame164(6179);
	}
	
	/*
	 * Options
	 */
	
	public void sendOption(String s) {
		player.getPA().sendFrame126("Select an Option", 2470);
	 	player.getPA().sendFrame126(s, 2471);
		player.getPA().sendFrame126("Click here to continue", 2473);
		player.getPA().sendFrame164(13758);
	}	
	
	public void sendOption2(String s, String s1) {
		player.getPA().sendFrame126("Select an Option", 2460);
		player.getPA().sendFrame126(s, 2461);
		player.getPA().sendFrame126(s1, 2462);
		player.getPA().sendFrame164(2459);
	}
	
	public void sendOption3(String s, String s1, String s2) {
		player.getPA().sendFrame126("Select an Option", 2470);
		player.getPA().sendFrame126(s, 2471);
		player.getPA().sendFrame126(s1, 2472);
		player.getPA().sendFrame126(s2, 2473);
		player.getPA().sendFrame164(2469);
	}
	
	public void sendOption4(String s, String s1, String s2, String s3) {
		player.getPA().sendFrame126("Select an Option", 2481);
		player.getPA().sendFrame126(s, 2482);
		player.getPA().sendFrame126(s1, 2483);
		player.getPA().sendFrame126(s2, 2484);
		player.getPA().sendFrame126(s3, 2485);
		player.getPA().sendFrame164(2480);
	}
	
	public void sendOption5(String s, String s1, String s2, String s3, String s4) {
		player.getPA().sendFrame126("Select an Option", 2493);
		player.getPA().sendFrame126(s, 2494);
		player.getPA().sendFrame126(s1, 2495);
		player.getPA().sendFrame126(s2, 2496);
		player.getPA().sendFrame126(s3, 2497);
		player.getPA().sendFrame126(s4, 2498);
		player.getPA().sendFrame164(2492);
	}

	/*
	 * Statements
	 */
	
	public void sendStatement(String s) { // 1 line click here to continue chat box interface
		player.getPA().sendFrame126(s, 357);
		player.getPA().sendFrame126("Click here to continue", 358);
		player.getPA().sendFrame164(356);
	}
	
	/*
	 * Npc Chatting
	 */
	
	public void sendNpcChat1(String s, int ChatNpc, String name) {
		player.getPA().sendFrame200(4883, 591);
		player.getPA().sendFrame126(name, 4884);
		player.getPA().sendFrame126(s, 4885);
		player.getPA().sendFrame75(ChatNpc, 4883);
		player.getPA().sendFrame164(4882);
	}
	
	public void sendNpcChat2(String s, String s1, int ChatNpc, String name) {
		player.getPA().sendFrame200(4888, 591);
		player.getPA().sendFrame126(name, 4889);
		player.getPA().sendFrame126(s, 4890);
		player.getPA().sendFrame126(s1, 4891);
		player.getPA().sendFrame75(ChatNpc, 4888);
		player.getPA().sendFrame164(4887);
	}

	public void sendNpcChat3(String s, String s1, String s2, int ChatNpc, String name) {
		player.getPA().sendFrame200(4894, 591);
		player.getPA().sendFrame126(name, 4895);
		player.getPA().sendFrame126(s, 4896);
		player.getPA().sendFrame126(s1, 4897);
		player.getPA().sendFrame126(s2, 4898);
		player.getPA().sendFrame75(ChatNpc, 4894);
		player.getPA().sendFrame164(4893);
	}
	
	public void sendNpcChat4(String s, String s1, String s2, String s3, int ChatNpc, String name) {
		player.getPA().sendFrame200(4901, 591);
		player.getPA().sendFrame126(name, 4902);
		player.getPA().sendFrame126(s, 4903);
		player.getPA().sendFrame126(s1, 4904);
		player.getPA().sendFrame126(s2, 4905);
		player.getPA().sendFrame126(s3, 4906);
		player.getPA().sendFrame75(ChatNpc, 4901);
		player.getPA().sendFrame164(4900);
	}
	
	/*
	 * Player Chating Back
	 */
	
	public void sendPlayerChat1(String s) {
		player.getPA().sendFrame200(969, 591);
		player.getPA().sendFrame126(player.playerName, 970);
		player.getPA().sendFrame126(s, 971);
		player.getPA().sendFrame185(969);
		player.getPA().sendFrame164(968);
	}
	
	public void sendPlayerChat2(String s, String s1) {
		player.getPA().sendFrame200(974, 591);
		player.getPA().sendFrame126(player.playerName, 975);
		player.getPA().sendFrame126(s, 976);
		player.getPA().sendFrame126(s1, 977);
		player.getPA().sendFrame185(974);
		player.getPA().sendFrame164(973);
	}
	
	public void sendPlayerChat3(String s, String s1, String s2) {
		player.getPA().sendFrame200(980, 591);
		player.getPA().sendFrame126(player.playerName, 981);
		player.getPA().sendFrame126(s, 982);
		player.getPA().sendFrame126(s1, 983);
		player.getPA().sendFrame126(s2, 984);
		player.getPA().sendFrame185(980);
		player.getPA().sendFrame164(979);
	}
	
	public void sendPlayerChat4(String s, String s1, String s2, String s3) {
		player.getPA().sendFrame200(987, 591);
		player.getPA().sendFrame126(player.playerName, 988);
		player.getPA().sendFrame126(s, 989);
		player.getPA().sendFrame126(s1, 990);
		player.getPA().sendFrame126(s2, 991);
		player.getPA().sendFrame126(s3, 992);
		player.getPA().sendFrame185(987);
		player.getPA().sendFrame164(986);
	}
	public void sendStatementTwo(String s) {
		player.getPA().sendFrame126(s, 357);
		player.getPA().sendFrame126("Click here to continue", 358);
		player.getPA().sendFrame164(356);
	}
}
