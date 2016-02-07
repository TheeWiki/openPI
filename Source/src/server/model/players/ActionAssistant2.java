package server.model.players;

import server.Server;
import server.model.npcs.*;
import server.model.players.Client;
import server.model.players.Player;

public class ActionAssistant2 {

	public Client client;
	
	public ActionAssistant2(Client client) {
		this.client = client;
	}
	
	public static void showInterface(Client client, int i) {
		client.getOutStream().createFrame(97);
		client.getOutStream().writeWord(i);
		client.flushOutStream();
	}
	
	public static void sendQuest(Client client, String s, int i) {
		client.getOutStream().createFrameVarSizeWord(126);
		client.getOutStream().writeString(s);
		client.getOutStream().writeWordA(i);
		client.getOutStream().endFrameVarSizeWord();
		client.flushOutStream();
	}

	public void GERTRUDE4(){
		/*int kitten = 1555+Misc.random(5);
		client.QuestStage11 = 15;
		client.sendQuestTab();
	  	client.getActionAssistant().addItem(kitten,1);
		client.getActionAssistant().addSkillXP(3250/7,7);*/
		client.getPA().QuestReward(client, "Gertrude's Cat", 2, "2 Quest Points", "3,250 Cooking Xp", "A kitten!", "", "", "", 4675);
	}
	
	public void DESERT(){
		client.questPoints++; client.questPoints++; client.questPoints++; client.questPoints++; client.questPoints++;
		client.getPA().QuestReward(client, "Desert Treasure", 5, "5 Quest Points", "20,000 Magic XP", "Ancient Magicks", "", "", "", 4675);
	}
	
	public void DEMONREWARD(){
		client.questPoints++; client.questPoints++; client.questPoints++;
		client.getPA().QuestReward(client, "Demon Slayer", 3, "3 Quest Points", "Silverlight", "", "", "", "", 2402);
	}
	
	public void HFTD(){
		client.questPoints++; client.questPoints++; client.questPoints++; client.questPoints++;
		client.getPA().QuestReward(client, "Horror From The Deep", 4, "4 Quest Points", "4662 in each of: Ranged,", "Magic, Strength", "", "", "", 3849);
	}
	
	public void RMQ(){
		client.getPA().QuestReward(client, "Rune Mysteries Quest", 3, "3 Quest Point", "Ability to mine Rune Essences.", "", "", "", "", 1436);
	}

	public void COOK2(){
		/*client.QuestStage9 = 15;
		client.sendQuestTab();
		client.getActionAssistant().addSkillXP(1000/7,7);*/
		client.getPA().addSkillXP((5000), 7);
		client.questPoints++; client.questPoints++; client.questPoints++; client.getPA().loadQuests();
		client.getPA().QuestReward(client, "Cook's Assistant", 3, "3 Quest Points", "5000 Cooking Xp", "", "", "", "", 1891);
	}
	
	public void DORIC(){
		/*client.QuestStage9 = 15;
		client.sendQuestTab();
		client.getActionAssistant().addSkillXP(1000/7,7);*/
		client.getPA().addSkillXP((1300), 14);
		client.questPoints++;
		client.getPA().loadQuests();
		client.doricQuest = 100;
		client.getPA().QuestReward(client, "Doric's Quest", 1, "1 Quest Points", "1300 Mining Xp", "180 Coins", "1 Bronze Pickaxe", "", "", 1265);
	}

	public void JULIET2(){
		/*client.QuestStage5 = 15;
		client.sendQuestTab();*/
		client.getPA().QuestReward(client, "Romeo and Juliet", 7, "7 Quest Points", "", "", "", "", "", 755);
	}

	public void startDwarfQuest7(){
		/*client.QuestStage3 = 15;
		client.sendQuestTab();
		client.getActionAssistant().addSkillXP(17500/7,12);*/
		client.getPA().QuestReward(client, "Dwarf Cannon", 4, "4 Quest Points", "17500 Crafting Xp", "Permisions to purchase and", "use Dwarf Multicannon", "", "", 4);
	}
	
	public void WITCHPOTION(){
		client.getPA().addSkillXP(3250,6);
		//client.getPA().sendQuestTab();
		client.getPA().QuestReward(client, "Witch's Potion", 3, "3 Quest Points", "3250 Cooking Xp", "", "", "", "", 221);
	}
	
	public void sendQuestTab(){
		if(client.rfdRound == 0) {
			sendFrame126("Recipe for Disaster", 7332);
		} else if(client.rfdRound > 0 && client.rfdRound < 4) {
			sendFrame126("@yel@Recipe for Disaster", 7332);
		} else if(client.rfdRound == 4) {
			sendFrame126("@gre@Recipe for Disaster", 7332);
		}
		if(client.horrorFromDeep == 0) {
			sendFrame126("Horror from the Deep", 7333);
		} else if(client.horrorFromDeep == 1) {
			sendFrame126("@yel@Horror from the Deep", 7333);
		} else if(client.horrorFromDeep == 2) {
			sendFrame126("@gre@Horror from the Deep", 7333);
		}
		if(client.desertT == 0) {
			sendFrame126("Desert Treasure", 7334);
		} else if(client.desertT == 1) {
			sendFrame126("@yel@Desert Treasure", 7334);
		} else if(client.desertT == 2) {
			sendFrame126("@gre@Desert Treasure", 7334);
		}
		if(client.cooksA == 0) {
			sendFrame126("Cook's Assistant", 7336);
		} else if(client.cooksA == 1) {
			sendFrame126("@yel@Cook's Assistant", 7336);
		} else if(client.cooksA == 3) {
			sendFrame126("@gre@Cook's Assistant", 7336);
		}
		if(client.doricQuest == 0) {
			sendFrame126("Doric's Quest", 7339);
		} else if(client.doricQuest < 100 && client.doricQuest > 0) {
			sendFrame126("@yel@Doric's Quest", 7339);
		} else if(client.doricQuest == 100) {
			sendFrame126("@gre@Doric's Quest", 7339);
		}
		sendFrame126("@or1@Quest Points: "+client.questPoints, 640);
	}
	
	public void sendFrame126(String s, int id) {
		//synchronized(c) {
			if(client.getOutStream() != null && client != null ) {
				client.getOutStream().createFrameVarSizeWord(126);
				client.getOutStream().writeString(s);
				client.getOutStream().writeWordA(id);
				client.getOutStream().endFrameVarSizeWord();
				client.flushOutStream();
			}
		//}
	}
}