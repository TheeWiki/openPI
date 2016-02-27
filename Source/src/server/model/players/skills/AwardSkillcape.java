package server.model.players.skills;

import server.model.players.Player;

public class AwardSkillcape {

	/**
	 * Skill cape awarding
	 * @param c
	 * @param arg
	 */
	public static void executeAward(Player c) {
		for (SkillMasters sm : SkillMasters.values()) {
			if (c.getItems().freeSlots() < 1 ) {
				c.sendMessage("You don't have enough space");
				return;
			}
			if (c.playerLevel[SkillIndex.getSkills(c.getShops().get99Count())] < 99)
			{
				c.sendMessage("You need to master all skills first..");
				return;
			}
			c.getDH().sendNpcChat1("Here's you Skillcape & Hood " + c.playerName + ".", sm.getNpcId(), sm.getName());
			c.nextChat = 0;
		}
	}
}