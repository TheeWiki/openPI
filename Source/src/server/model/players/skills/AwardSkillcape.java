package server.model.players.skills;

import server.model.players.Player;

public class AwardSkillcape {

	/**
	 * Skill cape awarding
	 * @param player
	 * @param arg
	 */
	public static void executeAward(Player player) {
		for (SkillMasters sm : SkillMasters.values()) {
			if (player.getItems().freeSlots() < 1 ) {
				player.getActionSender().sendMessage("You don't have enough space");
				return;
			}
			if (player.playerLevel[SkillIndex.getSkills(player.getShops().get99Count())] < 99)
			{
				player.getActionSender().sendMessage("You need to master all skills first..");
				return;
			}
			player.getDH().sendNpcChat1("Here's you Skillcape & Hood " + player.playerName + ".", sm.getNpcId(), sm.getName());
			player.nextChat = 0;
		}
	}
}