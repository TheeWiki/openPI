package server.model.players;

import server.Config;
import server.Server;

public class DialogueHandler {

	private Client c;
	
	public DialogueHandler(Client client) {
		this.c = client;
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
			c.dialogueAction = 1;
			c.nextChat = 2;
			break;
		case 2:
			sendOption2("Yea! I'm fearless!",  "No way! That looks scary!");
			c.dialogueAction = 1;
			c.nextChat = 0;
			break;
		case 3:
			sendNpcChat4("Hello!", "My name is Duradel and I am a master of the slayer skill.", "I can assign you a slayer task suitable to your combat level.", 
			"Would you like a slayer task?", c.talkingNpc, "Duradel");
			c.nextChat = 4;
		break;
		case 5:
			sendNpcChat4("Hello adventurer...", "My name is Kolodion, the master of this mage bank.", "Would you like to play a minigame in order ", 
						"to earn points towards recieving magic related prizes?", c.talkingNpc, "Kolodion");
			c.nextChat = 6;
		break;
		case 6:
			sendNpcChat4("The way the game works is as follows...", "You will be teleported to the wilderness,", 
			"You must kill mages to recieve points,","redeem points with the chamber guardian.", c.talkingNpc, "Kolodion");
			c.nextChat = 15;
		break;
		case 11:
			sendNpcChat4("Hello!", "My name is Duradel and I am a master of the slayer skill.", "I can assign you a slayer task suitable to your combat level.", 
			"Would you like a slayer task?", c.talkingNpc, "Duradel");
			c.nextChat = 12;
		break;
		case 12:
			sendOption2("Yes I would like a slayer task.", "No I would not like a slayer task.");
			c.dialogueAction = 5;
		break;
		case 13:
			sendNpcChat4("Hello!", "My name is Duradel and I am a master of the slayer skill.", "I see I have already assigned you a task to complete.", 
			"Would you like me to give you an easier task?", c.talkingNpc, "Duradel");
			c.nextChat = 14;
		break;
		case 14:
			sendOption2("Yes I would like an easier task.", "No I would like to keep my task.");
			c.dialogueAction = 6;
		break;
		case 15:
			sendOption2("Yes I would like to play", "No, sounds too dangerous for me.");
			c.dialogueAction = 7;
		break;
		case 16:
			sendOption2("I would like to reset my barrows brothers.", "I would like to fix all my barrows");
			c.dialogueAction = 8;
		break;
		case 17:
			sendOption5("Air", "Mind", "Water", "Earth", "More");
			c.dialogueAction = 10;
			c.dialogueId = 17;
			c.teleAction = -1;
		break;
		case 18:
			sendOption5("Fire", "Body", "Cosmic", "Astral", "More");
			c.dialogueAction = 11;
			c.dialogueId = 18;
			c.teleAction = -1;
		break;
		case 19:
			sendOption5("Nature", "Law", "Death", "Blood", "More");
			c.dialogueAction = 12;
			c.dialogueId = 19;
			c.teleAction = -1;
		break;
		case 20:
			sendNpcChat4("Haha, hello", "My name is Wizard Distentor! I am the master of clue scroll reading.", "I can read the magic signs of a clue scroll", 
			"You got to pay me 100K for reading the clue though!", c.talkingNpc, "Wizard Distentor");
			c.nextChat = 21;
		break;
		case 21:
			sendOption2("Yes I would like to pay 100K", "I don't think so sir");
			c.dialogueAction = 50;
		break;
		case 23:
			sendNpcChat4("Greetings, Adventure", "I'm the legendary Vesta seller", "With 120 noted Lime Stones, and 20 Million GP", 
			"I'll be selling you the Vesta's Spear", c.talkingNpc, "Legends Guard");
			c.nextChat = 24;
		break;
		case 54:
			sendOption2("Buy Vesta's Spear", "I can't afford that");
			c.dialogueAction = 51;
		break;
		case 56:
			sendStatement("Hello "+c.playerName+", you currently have "+c.pkPoints+" PK points.");
		break;
		
		case 57:
			c.getPA().sendFrame126("Teleport to shops?", 2460);
			c.getPA().sendFrame126("Yes.", 2461);
			c.getPA().sendFrame126("No.", 2462);
			c.getPA().sendFrame164(2459);
			c.dialogueAction = 27;
		break;
		
		/**
		* Recipe for disaster - Sir Amik Varze
		**/
		
		case 24:
			c.getAA2().JULIET2();
			break;
		case 25:
			sendOption2("Yes", "No");
			c.rfdOption = true;
			c.nextChat = 0;
			break;
		case 26:
			sendPlayerChat1("Yes");
			c.nextChat = 28;
			break;
		case 27:
			sendPlayerChat1("No");
			c.nextChat = 29;
			break;
		
		case 29:
			c.getPA().removeAllWindows();
			c.nextChat = 0;
			break;
		case 30:
			sendNpcChat4("Congratulations!", "You have defeated all Recipe for Disaster bosses", "and have now gained access to the Culinaromancer's chest", "and the Culinaromancer's item store.", c.talkingNpc, "Sir Amik Varze");
			c.nextChat = 0;
			PlayerSave.saveGame(c);
			break;
		case 31:
			sendNpcChat4("", "You have been defeated!", "You made it to round "+c.roundNpc, "", c.talkingNpc, "Sir Amik Varze");
			c.roundNpc = 0;
			c.nextChat = 0;
			break;
			
		/**
		* Horror from the deep
		**/
		case 32:
			sendNpcChat4("", "Would you like to start the quest", "Horror from the Deep?", "", c.talkingNpc, "Jossik");
			c.nextChat = 33;
			break;
		case 33:
			sendNpcChat4("", "You will have to be able to defeat a level-100 ", "Dagannoth mother with different styles of attacks.", "", c.talkingNpc, "Jossik");
			c.nextChat = 34;
			break;
		case 34:
			sendOption2("Yes I am willing to fight!", "No thanks, I am not strong enough.");
			c.horrorOption = true;
			break;
		case 35:
			sendPlayerChat1("Yes I am willing to fight!");
			c.nextChat = 37;
			break;
		case 36:
			sendPlayerChat1("No thanks, I am not strong enough.");
			c.nextChat = 0;
			break;
		case 37:
			c.horrorFromDeep = 1;
			c.height = (c.playerId * 4);
			c.getPA().movePlayer(2515, 10008, c.height);
			Server.npcHandler.spawnNpc(c, 1351, 2521, 10024, c.height, 0, 100, 16, 75, 75, true, true);
			c.getPA().removeAllWindows();
			c.getPA().loadQuests();
			c.inHfd = true;
			break;
			
		/**
		* Desert Treasure dialogue
		*/
		case 41:
			sendNpcChat4("", "Do you want to start the quest", "Desert treasure?","", c.talkingNpc, "Archaeologist");
			c.nextChat = 42;
			break;
		case 42:
			sendNpcChat4("", "You will have to fight four high level bosses,", "after each boss you will be brought back", "here to refill your supplies if it is needed.", c.talkingNpc, "Archaeologist");
			c.nextChat = 43;
			break;
		case 43:
			sendOption2("Yes I want to fight!", "No thanks, I am not ready.");
			c.dtOption = true;
			break;
		case 44:
			sendPlayerChat1("Yes I want to fight!");
			c.nextChat = 51;
			break;
		case 45:
			sendPlayerChat1("No thanks, I am not ready.");
			c.nextChat = 0;
			break;
	
		
		case 48:
			sendOption2("Yes, I am ready!", "No, I am not ready.");
			c.dtOption2 = true;
			break;
		case 49:
			sendPlayerChat1("Yes, I am ready!");
			c.nextChat = 52;
			break;
		case 50:
			sendPlayerChat1("No, I am not ready.");
			c.nextChat = 0;
			break;
		case 51:
			c.desertT++;
			c.height = (c.playerId * 4);
			c.getPA().movePlayer(3310, 9376, c.height);
			Server.npcHandler.spawnNpc(c, 1977, 3318, 9376, c.height, 0, 130, 40, 70, 90, true, true);
			c.getPA().removeAllWindows();
			c.getPA().loadQuests();
			c.inDt = true;
			break;
	
			
		/**
		* Cook's Assistant
		*/
		case 100:
			sendNpcChat1("What am I to do?", c.talkingNpc, "Cook");
			c.nextChat = 101;
			break;
		case 101:
			sendOption4("What`s wrong?", "Can you make me a cake?", "You don`t look very happy.", "Nice hat!");
			c.caOption4a = true;
			c.caPlayerTalk1 = true;
			break;
		case 102:
			sendPlayerChat1("What`s wrong?");
			c.nextChat = 103;
			break;
		case 103:
			sendNpcChat3("Oh dear, oh dear, oh dear, Im in a terrible terrible", "mess! It`s the Duke`s birthday today, and I should be", "making him a lovely big birthday cake.", c.talkingNpc, "Cook");
			c.nextChat = 104;
			break;
		case 104:
			sendNpcChat4("I`ve forgotten to buy the ingredients. I`ll never get", "them in time now. He`ll sack me! What will I do? I have", "four children and a goat to look after. Would you help", "me? Please?", c.talkingNpc, "Cook");
			c.nextChat = 105;
			break;
		case 105:
			sendOption2("Im always happy to help an cook in distress.", "I can`t right now, Maybe later.");
			c.caOption2 = true;
			break;
		case 106:
		c.cooksA++;
		c.getPA().loadQuests();
			sendPlayerChat1("Yes, I`ll help you.");
			c.nextChat = 107;
			break;
		case 107:
			sendNpcChat2("Oh thank you, thank you. I need milk, an egg and", "flour. I`d be very grateful if you can get them for me.", c.talkingNpc, "Cook");
			c.nextChat = 108;
			break;
		case 108:
			sendOption4("Where do I find some flour.", "How about some milk?", "And eggs? where are they found?", "Actually, I know where to find these stuff.");
			c.caOption4c = true;
			c.caOption4b = true;
			break;
		case 109:
			sendNpcChat1("How are you getting on with finding the ingredients?", c.talkingNpc, "Cook");
			c.nextChat = 110;
			break;
		case 110:
			sendPlayerChat1("Here's a bucket of milk.");
			c.getItems().deleteItem(1927, 1);
			c.nextChat = 111;
			break;
		case 111:
			sendPlayerChat1("Here's a pot of flour.");
			c.getItems().deleteItem(1933, 1);
			c.nextChat = 112;
			break;
		case 112:
		c.cooksA++;
		c.getPA().loadQuests();
			sendPlayerChat1("Here's a fresh egg.");
			c.getItems().deleteItem(1944, 1);
			c.nextChat = 113;
			break;
		case 113:
			sendNpcChat2("You've brough me everything I need! I am saved!",	"Thank you!", c.talkingNpc, "Cook");
		c.cooksA++;
		c.getPA().loadQuests();
		c.getAA2().COOK2();
			c.nextChat = 0;
			break;
/*		case 114:
			sendPlayerChat1("So do I get to go the Duke's Party?");
			c.nextChat = 115;
			break;
		case 115:
			sendNpcChat2("I'm afraid not, only the big cheeses get to dine with the", "Duke.", c.talkingNpc, "Cook");
			c.nextChat = 116;
			break;
		case 116:
			sendPlayerChat2("Well, maybe one day I'll be important enough to sit on", "the Duke's table.");
			c.nextChat = 117;
			break;
		case 117:
			sendNpcChat1("Maybe, but I won't be holding my breath.", c.talkingNpc, "Cook");
			c.cooksA++; c.cooksA++; c.getPA().loadQuests(); c.getAA2().COOK2();
			c.nextChat = 0;
			break;*/
			
		//** Getting Items - Cook's Assistant **//
		case 118:
			sendNpcChat3("There`s a mill fairly close, Go North then West.", "Mill Lane Mill is just off the road to Draynor. I", "usually get my flour from there.", c.talkingNpc, "Cook");
			c.nextChat = 119;
			break;
		case 119:
			sendNpcChat2("Talk to Millie, she`ll help, she`s a lovely girl and a fine", "Miller.", c.talkingNpc, "Cook");
			c.nextChat = 108;
			break;
		case 120:
			sendNpcChat2("There is a cattle field on the other side of the river,", "just across the road from the Groats` Farm.", c.talkingNpc, "Cook");
			c.nextChat = 121;
			break;
		case 121:
			sendNpcChat3("Talk to Gillie Groats, she looks after the Dairy Cows -", "She`ll tell you everything you need to know about", "milking cows!", c.talkingNpc, "Cook");
			c.nextChat = 108;
			break;
		case 122:
			sendNpcChat2("I normally get my eggs from the Groats` farm on the", "other side of the river.", c.talkingNpc, "Cook");
			c.nextChat = 123;
			break;
		case 123:
			sendNpcChat1("But any chicken should lay eggs.", c.talkingNpc, "Cook");
			c.nextChat = 108;
			break;
		case 124:
			sendPlayerChat1("Actually, I know where to find these stuff");
			c.nextChat = 0;
			break;
		case 125:
			sendPlayerChat1("You're a cook why, don't you bake me a cake?");
			c.nextChat = 126;
			break;
		case 126:
			sendNpcChat1("*sniff* Dont talk to me about cakes...", c.talkingNpc, "Cook");
			c.nextChat = 102;
			break;
		case 127:
			sendPlayerChat1("You don't look very happy.");
			c.nextChat = 128;
			break;
		case 128:
			sendNpcChat2("No, I`m not. The world is caving in around me - I am", "overcome by dark feelings of impending doom.", c.talkingNpc, "Cook");
			c.nextChat = 129;
			break;
		case 129:
			sendOption2("What's wrong?", "I'd take off the rest of the day if I were you.");
			c.caOption2a = true;
			break;
		case 130:
			sendPlayerChat1("Nice hat!");
			c.nextChat = 131;
			break;
		case 131:
			sendNpcChat1("Err thank you. It`s a pretty ordinary cook`s hat really.", c.talkingNpc, "Cook");
			c.nextChat = 132;
			break;
		case 132:
			sendPlayerChat1("Still, suits you. The trousers are pretty special too.");
			c.nextChat = 133;
			break;
		case 133:
			sendNpcChat1("It`s all standard cook`s issue uniform...", c.talkingNpc, "Cook");
			c.nextChat = 134;
			break;
		case 134:
			sendPlayerChat2("The whole hat, apron, stripey trousers ensemble -", "it works. It makes you look like a real cook.");
			c.nextChat = 135;
			break;
		case 135:
			sendNpcChat2("I am a real cook!, I haven`t got time to be chatting", "about Culinary Fashion. I`m in desperate need of help.", c.talkingNpc, "Cook");
			c.nextChat = 102;
			break;
		case 136:
			sendPlayerChat1("I'd take off the rest of the day if I were you.");
			c.nextChat = 137;
			break;
		case 137:
			sendNpcChat2("No, that`s the worst thing I could do. I`d get in terrible", "trouble.", c.talkingNpc, "Cook");
			c.nextChat = 138;
			break;
		case 138:
			sendPlayerChat1("Well maybe you need to take a holiday...");
			c.nextChat = 139;
			break;
		case 139:
			sendNpcChat2("That would be nice but the duke doesn`t allow holidays", "for core staff.", c.talkingNpc, "Cook");
			c.nextChat = 140;
			break;
		case 140:
			sendPlayerChat2("Hmm, why not run away to the sea and start a new", "life as a Pirate.");
			c.nextChat = 141;
			break;
		case 141:
			sendNpcChat2("My wife gets sea sick, and i have an irrational fear of", "eyepatches. I don`t see it working myself.", c.talkingNpc, "Cook");
			c.nextChat = 142;
			break;
		case 142:
			sendPlayerChat1("I`m afraid I've run out of ideas.");
			c.nextChat = 143;
			break;
		case 143:
			sendNpcChat1("I know I`m doomed.", c.talkingNpc, "Cook");
			c.nextChat = 102;
			break;
			
		//
		
		case 144:
			sendNpcChat1("Nice day, isn't it?", c.talkingNpc, "");
			c.nextChat = 0;
			break;
		
	/*
	 * Doric's Quest
	 */
	 
	    case 300:
            sendNpcChat1("Why hello there adventurer, how can I help you?", c.talkingNpc, "Doric");
            c.nextChat = 301;
        break;
		
		case 301:
			sendOption3("I'm looking for a quest.", "Nice place you got here.", "Just passing by.");
			c.doricOption = true;
		break;
		
	    case 299:
            sendPlayerChat1("I'm just passing by.");
            c.nextChat = 302;
        break;
		
	    case 302:
            sendNpcChat1("Very well, so long.", c.talkingNpc, "Doric");
            c.nextChat = 0;
        break;
		
	    case 303:
            sendPlayerChat1("Nice place you got here.");
            c.nextChat = 304;
        break;
		
	    case 304:
            sendNpcChat1("Why thank you kind sir.", c.talkingNpc, "Doric");
            c.nextChat = 305;
        break;
		
		case 305:
            sendPlayerChat1("My pleasure.");
            c.nextChat = 0;
        break;
		
	    case 306:
            sendPlayerChat1("I'm looking for a quest.");
            c.nextChat = 307;
        break;
		
	    case 307:
            sendNpcChat2("A quest you say? Hmm...", "Can you run me a quick errand?", c.talkingNpc, "Doric");
            c.nextChat = 308;
        break;
		
		case 308:
			sendOption2("Of course.", "I need to go.");
			c.doricOption2 = true;
		break;
		
	    case 309:
            sendPlayerChat1("I need to go.");
            c.nextChat = 0;
        break;
		
	    case 310:
            sendPlayerChat1("Of course!");
            c.nextChat = 311;
        break;
		
	    case 311:
            sendNpcChat3("Very good! I need some materials for a new ", "pickaxe I'm working on, is there any way you ", "could go get these?", c.talkingNpc, "Doric");
            c.nextChat = 312;
        break;
		
	    case 312:
            sendPlayerChat1("Sure, what materials?");
            c.nextChat = 313;
        break;
		
	    case 313:
            sendNpcChat3("6 lumps of clay,", "4 copper ores,", "and 2 iron ores.", c.talkingNpc, "Doric");
            c.nextChat = 314;
        break;
		
	    case 314:
            sendPlayerChat1("Sounds good, I will be back soon!");
            c.nextChat = 315;
			c.doricQuest = 5;
        break;
		
	    case 315:
            sendNpcChat1("Thank you adventurer, hurry back!", c.talkingNpc, "Doric");
            c.nextChat = 0;
        break;
		
	    case 316:
            sendNpcChat1("Have you got all the materials yet?", c.talkingNpc, "Doric");
            c.nextChat = 317;
        break;
		
	    case 317:
            sendPlayerChat1("Not all of them.");
            c.nextChat = 0;
        break;
		
	    case 318:
            sendNpcChat1("Have you got all the materials yet?", c.talkingNpc, "Doric");
            c.nextChat = 319;
        break;
		
	    case 319:
            sendPlayerChat1("Yep! Right here.");
            c.nextChat = 320;
			c.getItems().deleteItem(434, 6);
			c.getItems().deleteItem(436, 4);
			c.getItems().deleteItem(440, 2);
        break;
		
	    case 320:
            sendNpcChat2("Thank you so much adventurer, heres a reward", "for any hardships you may have encountered.", c.talkingNpc, "Doric");
            c.nextChat = 0;
			c.getAA2().DORIC();
			c.sendMessage("Congradulations, you have completed Doric's Quest!");
        break;
		
	    case 321:
            sendNpcChat1("Welcome to my home, feel free to use my anvils!", c.talkingNpc, "Doric");
            c.nextChat = 0;
        break;
		
	/*
	 * Demon Slayer
	 */
	 
	    case 325:
            sendNpcChat1("Hello young one.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 326;
        break;
		
	    case 326:
            sendNpcChat2("Cross my palm with silver and the fortune will be", "revealed to you.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 327;
        break;
		
		
		
	    case 328:
            sendPlayerChat1("Who are you calling 'young one'?");
            c.nextChat = 329;
        break;
		
	    case 329:
            sendNpcChat1("I do not have time with the likes of you questioning my ways.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 0;
        break;
		
	    case 330:
            sendPlayerChat1("No, I don't believe in that stuff.");
            c.nextChat = 0;
        break;
		
	    case 331:
            sendPlayerChat1("With silver?");
            c.nextChat = 332;
        break;
		
	    case 332:
            sendNpcChat1("With money from ones pocket.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 333;
        break;
		
		case 333:
			sendOption2("Ok, here you go.", "No I don't believe in that stuff.");
			c.DSOption2 = true;
		break;
		
	    case 334:
            sendNpcChat2("I sence you do not have enough! Return to me", "when you are able to pay...", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 0;
        break;
		
	    case 335:
            sendPlayerChat1("Ok, here you go.");
            c.nextChat = 336;
			c.getItems().deleteItem(995, 1);
        break;
		
	    case 336:
            sendNpcChat3("Come closer and listen carefully to what the future", "holds, as I peer into the swirling mists of the crystal", "ball.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 337;
        break;
		
	    case 337:
            sendNpcChat1("I can see images forming. I can see you.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 338;
        break;
		
	    case 338:
            sendNpcChat2("You are holding a very impressing looking sword. I'm", "sure I recognize it...", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 339;
        break;
		
	    case 339:
            sendNpcChat1("These is a big dark shadow appearing now.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 340;
        break;
		
	    case 340:
            sendNpcChat1("Aaargh!", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 341;
        break;
		
	    case 341:
            sendPlayerChat1("Are you all right?");
            c.nextChat = 342;
        break;
		
	    case 342:
            sendNpcChat1("It's Delrith! Delrith is coming!", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 343;
        break;
		
	    case 343:
            sendPlayerChat1("Who's Delrith?");
            c.nextChat = 344;
        break;
		
	    case 344:
            sendNpcChat1("Delrith...", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 345;
        break;
		
	    case 345:
            sendNpcChat1("Delrith is a powerful demon.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 346;
        break;
		
	    case 346:
            sendNpcChat2("Oh! I really hope he didn't see me looking at him", "through my crystal ball!", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 347;
        break;
		
	    case 347:
            sendNpcChat2("He tried to destroy this city 150 years ago. He was", "stopped just in time by the great hero Wally.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 348;
        break;
		
	    case 348:
            sendNpcChat3("Using his magic sword Silverlight. Wally managed to", "trap this demon ni the stone circle just south", "of this city.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 349;
        break;
		
	    case 349:
            sendNpcChat3("Ye gods! Silverlight was the sword you were holding in", "my vision! You are the one destined to stop the demon", "this time.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 350;
        break;
		
		case 350:
			sendOption3("How am I meant to fight a demon who can destroy cities?", "Okay, where is he? I'll kill him for you.", "Wally doesn't sound like a very heroic name.");
			c.DSOption3 = true;
		break;
		
	    case 351:
            sendPlayerChat2("How am I menat to fight a demon who can destroy", "cities?");
            c.nextChat = 352;
			c.demonSlayer = 5;
        break;
		
	    case 352:
            sendNpcChat3("If you face Delrith while he is still weak from being", "summoned and use the correct weapon, you will not", "find the task too hard.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 353;
        break;
		
	    case 353:
            sendNpcChat2("Do not fear. If you follow the path of the great hero", "Wally, then you are sure to defeat the demon.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 354;
        break;
		
		case 354:
			sendOption3("Okay, where is he? I'll kill him for you.", "Wally doesn't sound like a very heroic name.", "So how did wally kill Delright?");
			c.DSOption4 = true;
		break;
		
	    case 355:
            sendPlayerChat1("So, how did Wally kill Delrith?");
            c.nextChat = 356;
			c.demonSlayer = 6;
        break;
		
	    case 356:
            sendNpcChat2("Wally managed to arrive at the stone circle just as", "Delrith was summoned by a cult of chaos druids.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 357;
        break;
		
		case 357:
			sendStatement("You look into the crystal ball.");
			c.nextChat = 358;
		break;
		
	    case 358:
            sendNpcChat1("Die, foul demon!", c.talkingNpc, "Wally");
            c.nextChat = 359;
        break;
		
	    case 359:
            sendNpcChat1("Now, what was that incantation again?", c.talkingNpc, "Wally");
            c.nextChat = 360;
        break;
		
		
		
		case 361:
			sendStatement("You watch as the demon is sucked into the stone circle.");
			c.nextChat = 362;
		break;
		
	    case 362:
            sendNpcChat1("I am the greatest demon slayer EVER!", c.talkingNpc, "Wally");
            c.nextChat = 363;
        break;
		
	    case 363:
            sendNpcChat4("By reciting the correct magical incantation, and", "thrusting Silverlight into Delrith while he was newly", "summoned, Wally was able to imprision Delrith in the", "stone block in the centre of the circle.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 364;
        break;
		
		case 364:
			sendStatement("You stop looking into the crystal ball.");
			c.nextChat = 365;
			c.demonSlayer = 8;
		break;
		
	    case 365:
            sendPlayerChat1("Delrith will come forth from the stone circle again..");
            c.nextChat = 366;
        break;
		
	    case 366:
            sendPlayerChat2("I would imagine an evil sorcerer is already beginning", "the rituals to summon Delrith as we speak.");
            c.nextChat = 367;
        break;
		
		case 367:
			sendOption3("Okay where is he? I'll kill him for you.", "What is the magical incantation?", "Where can I find silverlight?");
			c.DSOption5 = true;
		break;
		
	    case 368:
            sendPlayerChat1("Wally doesn't sound like a very heroic name.");
            c.nextChat = 369;
        break;
		
	    case 369:
            sendNpcChat1("Oh but he was! He was the greatest hero this town has had!", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 354;
        break;
		
		case 370:
            sendPlayerChat1("Wally doesn't sound like a very heroic name.");
            c.nextChat = 371;
        break;
		
	    case 371:
            sendNpcChat1("Oh but he was! He was the greatest hero this town has had!", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 350;
        break;
		
	    case 372:
            sendPlayerChat1("Okay, where is he? I'll kill him for you.");
            c.nextChat = 373;
        break;
		
	    case 373:
            sendNpcChat2("You are foolish to try to take on Delrith with out knowing", "even a bit of knowledge about him!", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 350;
        break;
		
		case 374:
            sendPlayerChat1("Okay, where is he? I'll kill him for you.");
            c.nextChat = 375;
        break;
		
	    case 375:
            sendNpcChat2("You are foolish to try to take on Delrith with out knowing", "even a bit of knowledge about him!", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 354;
        break;

	    case 376:
            sendPlayerChat1("Okay, where is he? I'll kill him for you.");
            c.nextChat = 377;
        break;
		
	    case 377:
            sendNpcChat2("You have amazing courage to take on a task like this,", "but without Silverlight, you are doomed.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 367;
        break;
		
	    case 378:
            sendPlayerChat1("What is the magical incantation?");
            c.nextChat = 379;
        break;
		
		case 379:
		if(c.Incantation == 1) {
			sendNpcChat1("Carlem... Gabindo... Purchai... Zaree... Camerinthum!", c.talkingNpc, "Wally");
			c.nextChat = 367;
		} else if(c.Incantation == 2) {
			sendNpcChat1("Purchai... Zaree... Gabindo... Cariem... Camerinthum!", c.talkingNpc, "Wally");
			c.nextChat = 367;
		} else if(c.Incantation == 3) {
			sendNpcChat1("Purchai... Camerinthum... Aber... Gabindo... Carlem!", c.talkingNpc, "Wally");
			c.nextChat = 367;
		} else if(c.Incantation == 4) {
			sendNpcChat1("Carlem... Aber... Camerinthum... Purchai... Gabindo!", c.talkingNpc, "Wally");
			c.nextChat = 367;
		}
		break;
		
	    case 380:
            sendPlayerChat1("Where can I find Silverlight?");
            c.nextChat = 381;
        break;
		
	    case 381:
            sendPlayerChat3("Silverlight has been passed down by Wally's", "dessendants, I believe it is currently in the case of one", "of the king's knights called Sir Prysin.");
            c.nextChat = 382;
        break;
		
	    case 382:
            sendNpcChat2("He shouldn't be to hard to find. He lives in the royal", "palace in the city. Tell him Gypsy Aris sent you.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 383;
        break;
		
	    case 383:
            sendPlayerChat1("Okay, thanks. I'll do my best to stop the demon.");
            c.nextChat = 384;
        break;
		
	    case 384:
            sendNpcChat1("Good luck, and may Guthix be with you!", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 0;
			c.demonSlayer = 10;
        break;
		
		case 385:
			sendOption3("What is the incantation again?", "Where do I find Sir Prysin?", "Nevermind.");
			c.DSOption6 = true;
		break;
		
	    case 386:
            sendPlayerChat1("What is the incantation again?");
            c.nextChat = 387;
        break;
		
		case 387:
			if(c.Incantation == 1) {
				sendNpcChat1("Carlem... Gabindo... Purchai... Zaree... Camerinthum!", c.talkingNpc, "Wally");
				c.nextChat = 0;
			} else if(c.Incantation == 2) {
				sendNpcChat1("Purchai... Zaree... Gabindo... Cariem... Camerinthum!", c.talkingNpc, "Wally");
				c.nextChat = 0;
			} else if(c.Incantation == 3) {
				sendNpcChat1("Purchai... Camerinthum... Aber... Gabindo... Carlem!", c.talkingNpc, "Wally");
				c.nextChat = 0;
			} else if(c.Incantation == 4) {
				sendNpcChat1("Carlem... Aber... Camerinthum... Purchai... Gabindo!", c.talkingNpc, "Wally");
				c.nextChat = 0;
		}
		break;
		
	    case 388:
            sendPlayerChat1("Where do I find Sir Prysin?");
            c.nextChat = 389;
        break;
		
	    case 389:
            sendNpcChat2("Sir Pysin can be found in the kings royal palace", "just north of here.", c.talkingNpc, "Gypsy Aris");
            c.nextChat = 0;
        break;
		
	    case 390:
            sendPlayerChat1("Nevermind.");
            c.nextChat = 0;
        break;
		
	    case 391:
            sendNpcChat1("Hello, who are you?", c.talkingNpc, "Sir Prysin");
            c.nextChat = 392;
        break;
		
		case 392:
			sendOption3("I am a mighty adventurer. Who are you?", "I'm not sure. I was hoping you could tell me.", "Gypsy Aris said I should come and talk to you.");
			c.DSOption7 = true;
		break;
		
	    case 393:
            sendPlayerChat1("I am a mighty adventurer. Who are you?");
            c.nextChat = 394;
        break;
		
	    case 394:
            sendNpcChat2("I am Sir Prysin, one of the royal guards of the king.", "Now I ask you again, who are you?", c.talkingNpc, "Sir Prysin");
            c.nextChat = 392;
        break;
		
	    case 395:
            sendPlayerChat1("I'm not sure, I was hoping you could tell me.");
            c.nextChat = 396;
        break;
		
	    case 396:
            sendNpcChat1("I'm sorry sir, but I do not know who you are.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 392;
        break;
		
	    case 397:
            sendPlayerChat1("Gypsy Aris said I should come and talk to you.");
            c.nextChat = 398;
        break;
		
	    case 398:
            sendNpcChat3("Gypsy Aris? Is she still alive? I remember her from", "when I was pretty young. Well, what do you need to", "talk to me about?", c.talkingNpc, "Sir Prysin");
            c.nextChat = 399;
        break;
		
	    case 399:
            sendPlayerChat1("I need to find Silverlight.");
            c.nextChat = 400;
        break;
		
	    case 400:
            sendNpcChat1("What do you need to find that for?", c.talkingNpc, "Sir Prysin");
            c.nextChat = 401;
        break;
		
	    case 401:
            sendPlayerChat1("I need to fight Delrith.");
            c.nextChat = 402;
        break;
		
	    case 402:
            sendNpcChat2("Delrith? I thought the world was rid of him, thanks to ", "my great-grandfather.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 403;
        break;
		
	    case 403:
            sendPlayerChat1("Well the gypsy's crystal ball seems to think otherwise.");
            c.nextChat = 404;
        break;
		
	    case 404:
            sendNpcChat1("Well, if the ball says so, I'd better help you.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 405;
        break;
		
	    case 405:
            sendNpcChat1("The problem is getting Silverlight.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 406;
        break;
		
	    case 406:
            sendPlayerChat1("You mean you don't have it?");
            c.nextChat = 407;
        break;
		
	    case 407:
            sendNpcChat4("Oh, I do have it, but it is so powerful that the king", "made me put it in a special box requiring three", "different keys to open it. That way it wouldn't fall into", "the wrong hands.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 408;
        break;
		
		case 408:
			sendOption2("So give me the keys!", "And why is this a problem?");
			c.DSOption8 = true;
		break;
		
	    case 409:
            sendPlayerChat1("So give me the keys!");
            c.nextChat = 410;
        break;
		
	    case 410:
            sendNpcChat1("Uhm well, it's not so easy.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 412;
        break;
		
		case 411:
			sendPlayerChat1("And why is this a problem?");
			c.nextChat = 412;
		break;
		
	    case 412:
            sendNpcChat2("I kept one of the keys, I gave the other two to others", "for safekeeping.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 413;
        break;
		
	    case 413:
            sendNpcChat1("One I gave to Rovin the captain of the palace guard.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 414;
        break;
		
	    case 414:
            sendNpcChat1("The other I gave to the wizard Trailborn.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 415;
        break;
		
	    case 415:
            sendPlayerChat1("Can you give me your key?");
            c.nextChat = 416;
        break;
		
	    case 416:
            sendNpcChat1("Um....ah....", c.talkingNpc, "Sir Prysin");
            c.nextChat = 417;
        break;
		
	    case 417:
            sendNpcChat1("Well, there's a problem there as well.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 418;
        break;
		
	    case 418:
            sendNpcChat3("I managed to drop the key in the drain just outside the", "palace kitchen. I can see it in there, but I can't reach", "it.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 419;
        break;
		
	    case 419:
            sendPlayerChat1("So where does the drain lead?");
            c.nextChat = 420;
        break;
		
	    case 420:
            sendNpcChat2("It is the drain for the drainpipes running from the sink", "in the kitchen down to the palace sweres.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 421;
        break;
		
	    case 421:
            sendPlayerChat1("Where can I find Captain Rovin?");
            c.nextChat = 422;
        break;
		
	    case 422:
            sendNpcChat2("Captain Rovin lives at the top of the guards' questers in", "the north-west wing of this palace.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 423;
        break;
		
	    case 423:
            sendPlayerChat1("Where does the wizard live?");
            c.nextChat = 424;
        break;
		
	    case 424:
            sendNpcChat1("Wizard Trailborn?", c.talkingNpc, "Sir Prysin");
            c.nextChat = 425;
        break;
		
	    case 425:
            sendNpcChat3("He is one of the wizards in the tower, on the little", "island just off the south cost. I believe his quarters are", "on the second-floor of the tower.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 426;
        break;
		
	    case 426:
            sendPlayerChat1("Well, I'd better go key hunting.");
            c.nextChat = 427;
        break;
		
	    case 427:
            sendNpcChat1("Okay, goodbye.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 0;
			c.demonSlayer = 20;
        break;
		
	    case 428:
            sendNpcChat2("What are you doing up here? Only palace guards", "are allowed up here.", c.talkingNpc, "Captain Rovin");
            c.nextChat = 429;
        break;
		
	    case 429:
            sendPlayerChat1("Yea, I know, but this is important.");
            c.nextChat = 430;
        break;
		
	    case 430:
            sendNpcChat1("Ok, I'm listenting. Tell me what's so important.", c.talkingNpc, "Captain Rovin");
            c.nextChat = 431;
        break;
		
	    case 431:
            sendPlayerChat1("There's a demon who wants to invade the city.");
            c.nextChat = 432;
        break;
		
	    case 432:
            sendNpcChat1("Is it a powerful demon?", c.talkingNpc, "Captain Rovin");
            c.nextChat = 433;
        break;
		
	    case 433:
            sendPlayerChat1("Yes, very.");
            c.nextChat = 434;
        break;
		
	    case 434:
            sendNpcChat2("As good as the palace guards are, I don't know if", "they're up to taking on a very powerful demon.", c.talkingNpc, "Captain Rovin");
            c.nextChat = 435;
        break;
		
	    case 435:
            sendPlayerChat1("It's not them who are going to fight the demon, it's me.");
            c.nextChat = 436;
        break;
		
	    case 436:
            sendNpcChat1("What, all by yourself? How are you going to do that?", c.talkingNpc, "Captain Rovin");
            c.nextChat = 437;
        break;
		
	    case 437:
            sendPlayerChat2("I'm going to use the powerful sword Silverlight, which I", "believe you have one of the keys for?");
            c.nextChat = 438;
        break;
		
	    case 438:
            sendNpcChat1("Yes, I do. But why should I give it to you?", c.talkingNpc, "Captain Rovin");
            c.nextChat = 439;
        break;
		
	    case 439:
            sendPlayerChat1("Sir Prysin said you would give me the key.");
            c.nextChat = 440;
        break;
		
	    case 440:
            sendNpcChat2("Oh, he did, did he? Well I don't report to Sir Prysin, I", "report directly to the king!", c.talkingNpc, "Captain Rovin");
            c.nextChat = 441;
        break;
		
	    case 441:
            sendNpcChat4("I didn't work my way up through the ranks of the", "palace guards so I could take orders from an ill-bred", "moron who only has a job because his great-", "grandfather was a hero with a silly name!", c.talkingNpc, "Captain Rovin");
            c.nextChat = 442;
        break;
		
	    case 442:
            sendPlayerChat1("Why did he give you one of the keys then?");
            c.nextChat = 443;
        break;
		
	    case 443:
            sendNpcChat4("Only because the king ordered him to! The king", "couldn't get Sir Prysin to part with his precious", "ancestral sword, but he made him lock it up so he", "couldn't lose it.", c.talkingNpc, "Captain Rovin");
            c.nextChat = 444;
        break;
		
	    case 444:
            sendNpcChat2("I got one key and I think some wizard got another.", "Now what happend to the third one?", c.talkingNpc, "Captain Rovin");
            c.nextChat = 445;
        break;
		
	    case 445:
            sendPlayerChat1("Sir Prysin dropped it down a drain!");
            c.nextChat = 446;
        break;
		
	    case 446:
            sendNpcChat1("Ha ha ha! The idiot!", c.talkingNpc, "Captain Rovin");
            c.nextChat = 447;
        break;
		
	    case 447:
            sendNpcChat2("Okay, I'll give you the key, just so that it's you that", "kills the demon and not Sir Prysin!", c.talkingNpc, "Captain Rovin");
            c.nextChat = 448;
        break;
		
	
		
	    case 449:
            sendNpcChat1("Have a good day, and be safe.", c.talkingNpc, "Captain Rovin");
            c.nextChat = 0;
        break;
		
	    case 450:
            sendPlayerChat1("That must be the key Sir Prysin dropped.");
            c.nextChat = 451;
        break;
		
	    case 451:
            sendPlayerChat3("I don't seem to be able to reach it. I wonder if I can", "dislodge it somehow. That way it may go down into the", "sewers.");
            c.nextChat = 0;
        break;
		
	    case 452:
            sendPlayerChat2("OK, I think I've washed the key down into the sewer.", "I'd better go down and get it!");
            c.nextChat = 0;
        break;
		
	    case 453:
            sendNpcChat1("Ello young thingummywut.", c.talkingNpc, "Trailborn");
            c.nextChat = 454;
        break;
		
	    case 454:
            sendPlayerChat1("I need to get a key given to you by Sir Prysin.");
            c.nextChat = 455;
        break;
		
	    case 455:
            sendNpcChat2("Sir Prysin? Who's that? What would I want his key", "for?", c.talkingNpc, "Trailborn");
            c.nextChat = 456;
        break;
		
	    case 456:
            sendPlayerChat1("Well, have you got any keys knocking around?");
            c.nextChat = 457;
        break;
		
	    case 457:
            sendNpcChat3("Now you come to mention it, yes I do have a kay. It's", "in my special closet of valuable stuff. Now how do I get", "into that?", c.talkingNpc, "Trailborn");
            c.nextChat = 458;
        break;
		
	    case 458:
            sendNpcChat2("I sealed it using one of my magic rituals. So it would", "make sense that another ritual would open it again.", c.talkingNpc, "Trailborn");
            c.nextChat = 459;
        break;
		
	    case 459:
            sendPlayerChat1("So do you know what ritual to use?");
            c.nextChat = 460;
        break;
		
	    case 460:
            sendNpcChat1("Let me think a second.", c.talkingNpc, "Trailborn");
            c.nextChat = 461;
        break;
		
	    case 461:
            sendNpcChat4("Yes a simple drazier style ritual should suffice. Hmm,", "main problem with that is I'll need 25 sets of bones.", "Now where am I going to get hold of something like", "that?", c.talkingNpc, "Trailborn");
            c.nextChat = 462;
        break;
		
	    case 462:
            sendPlayerChat1("I'll get you the bones.");
            c.nextChat = 463;
        break;
		
	    case 463:
            sendNpcChat1("Ooh that would be very good of you.", c.talkingNpc, "Trailborn");
            c.nextChat = 464;
        break;
		
	    case 464:
            sendPlayerChat1("Ok, I'll speak to you when I've got the bones.");
            c.nextChat = 0;
			c.boneSearch = true;
        break;
		
	    case 465:
            sendNpcChat1("How are you doing finding bones?", c.talkingNpc, "Trailborn");
			if(!c.getItems().playerHasItem(526, 25)) {
            c.nextChat = 466;
			} else if(c.getItems().playerHasItem(526, 25)) {
			c.nextChat = 467;
		}
        break;
		
	    case 466:
            sendPlayerChat1("I don't have all 25 yet, I'll be back.");
            c.nextChat = 0;
        break;
		
	    case 467:
            sendPlayerChat1("I have the bones right here.");
            c.nextChat = 468;
        break;
		
	    case 468:
            sendNpcChat1("Give 'em here then.", c.talkingNpc, "Trailborn");
            c.nextChat = 469;
        break;
		
		case 469:
			sendStatement("You give Trailborn 25 sets of bones.");
			c.nextChat = 470;
		break;
		
	    case 470:
            sendNpcChat1("Hurrah! That's all 25 sets of bones.", c.talkingNpc, "Trailborn");
            c.nextChat = 471;
        break;
		
	    case 471:
            sendNpcChat4("Wings of dark and colour too,", "Spreading in the morning dew;", "Locked away I have a key;", "Return it now, please, unto me.", c.talkingNpc, "Trailborn");
            c.nextChat = 472;
        break;
		
	
		
	    case 473:
            sendPlayerChat1("Thank you very much.");
        break;
		
	    case 474:
            sendNpcChat1("So how are you doing with getting the keys?", c.talkingNpc, "Sir Prysin");
			if(!c.captainRovin || !c.trailborn || !c.prysin) {
            c.nextChat = 475;
			} else if(c.captainRovin && c.trailborn && c.prysin) {
			c.nextChat = 477;
		}
        break;
		
	    case 475:
            sendPlayerChat1("I don't have them all yet.");
            c.nextChat = 476;
        break;
		
	    case 476:
            sendNpcChat1("Well hurry up, I doubt we have much time!", c.talkingNpc, "Sir Prysin");
            c.nextChat = 0;
        break;
		
	    case 477:
            sendPlayerChat1("I've got them right here!");
            c.nextChat = 478;
        break;
		
	    case 478:
            sendNpcChat1("Excellent! Now I can give you Silverlight.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 479;
        break;
		
	
		
	    case 480:
            sendNpcChat1("Now, go kill that demon!", c.talkingNpc, "Sir Prysin");
            c.nextChat = 0;
        break;
		
	    case 481:
            sendNpcChat1("Have you slayed the demon yet?", c.talkingNpc, "Sir Prysin");
            c.nextChat = 482;
        break;
		
	    case 482:
            sendPlayerChat1("No not yet.");
            c.nextChat = 0;
        break;
		
		case 483:
            sendNpcChat1("Have you slayed the demon yet?", c.talkingNpc, "Sir Prysin");
            c.nextChat = 484;
        break;
		
	    case 484:
            sendPlayerChat1("Not yet. And I, um, lost Silverlight.");
            c.nextChat = 485;
        break;
		
	    case 485:
            sendNpcChat2("Yes, I know, someone returned it to me. Take better", "care of it this time.", c.talkingNpc, "Sir Prysin");
            c.nextChat = 0;
			c.getItems().addItem(2402, 1);
        break;
		
	    case 486:
            sendPlayerChat1("Now, what was the magical incantation again...");
            c.nextChat = 487;
        break;
		
		case 487:
				sendOption4("Carlem... Gabindo... Purchai... Zaree... Camerinthum!", "Purchai... Zaree... Gabindo... Cariem... Camerinthum!", "Purchai... Camerinthum... Aber... Gabindo... Carlem!", "Carlem... Aber... Camerinthum... Purchai... Gabindo!");
				c.incantationOption = true;
		break;
		
		case 488:
			sendStatement("Delrith is sucked into the vortex...");
			c.nextChat = 489;
		break;
		
		case 489:
			sendStatement("...back into the dark dimension from which he came.");
			c.nextChat = 0;
			c.getAA2().DEMONREWARD();
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
	
	private void sendStatement(String s) { // 1 line click here to continue chat box interface
		c.getPA().sendFrame126(s, 357);
		c.getPA().sendFrame126("Click here to continue", 358);
		c.getPA().sendFrame164(356);
	}
	
	/*
	 * Npc Chatting
	 */
	
	private void sendNpcChat1(String s, int ChatNpc, String name) {
		c.getPA().sendFrame200(4883, 591);
		c.getPA().sendFrame126(name, 4884);
		c.getPA().sendFrame126(s, 4885);
		c.getPA().sendFrame75(ChatNpc, 4883);
		c.getPA().sendFrame164(4882);
	}
	
	private void sendNpcChat2(String s, String s1, int ChatNpc, String name) {
		c.getPA().sendFrame200(4888, 591);
		c.getPA().sendFrame126(name, 4889);
		c.getPA().sendFrame126(s, 4890);
		c.getPA().sendFrame126(s1, 4891);
		c.getPA().sendFrame75(ChatNpc, 4888);
		c.getPA().sendFrame164(4887);
	}

	private void sendNpcChat3(String s, String s1, String s2, int ChatNpc, String name) {
		c.getPA().sendFrame200(4894, 591);
		c.getPA().sendFrame126(name, 4895);
		c.getPA().sendFrame126(s, 4896);
		c.getPA().sendFrame126(s1, 4897);
		c.getPA().sendFrame126(s2, 4898);
		c.getPA().sendFrame75(ChatNpc, 4894);
		c.getPA().sendFrame164(4893);
	}
	
	private void sendNpcChat4(String s, String s1, String s2, String s3, int ChatNpc, String name) {
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
	
	private void sendPlayerChat1(String s) {
		c.getPA().sendFrame200(969, 591);
		c.getPA().sendFrame126(c.playerName, 970);
		c.getPA().sendFrame126(s, 971);
		c.getPA().sendFrame185(969);
		c.getPA().sendFrame164(968);
	}
	
	private void sendPlayerChat2(String s, String s1) {
		c.getPA().sendFrame200(974, 591);
		c.getPA().sendFrame126(c.playerName, 975);
		c.getPA().sendFrame126(s, 976);
		c.getPA().sendFrame126(s1, 977);
		c.getPA().sendFrame185(974);
		c.getPA().sendFrame164(973);
	}
	
	private void sendPlayerChat3(String s, String s1, String s2) {
		c.getPA().sendFrame200(980, 591);
		c.getPA().sendFrame126(c.playerName, 981);
		c.getPA().sendFrame126(s, 982);
		c.getPA().sendFrame126(s1, 983);
		c.getPA().sendFrame126(s2, 984);
		c.getPA().sendFrame185(980);
		c.getPA().sendFrame164(979);
	}
	
	private void sendPlayerChat4(String s, String s1, String s2, String s3) {
		c.getPA().sendFrame200(987, 591);
		c.getPA().sendFrame126(c.playerName, 988);
		c.getPA().sendFrame126(s, 989);
		c.getPA().sendFrame126(s1, 990);
		c.getPA().sendFrame126(s2, 991);
		c.getPA().sendFrame126(s3, 992);
		c.getPA().sendFrame185(987);
		c.getPA().sendFrame164(986);
	}
}
