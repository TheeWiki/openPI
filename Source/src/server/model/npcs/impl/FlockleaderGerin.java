package server.model.npcs.impl;

import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.npcs.NPC;
import server.model.npcs.SpecialNPC;
import server.model.players.Player;
import server.model.players.skills.SkillIndex;
import server.util.Misc;

public class FlockleaderGerin extends SpecialNPC {

	@Override
	public void execute(Player Player, NPC n) {
		n.startAnimation(6956);
		int offX = (n.getY() - Player.getY()) * -1;
		int offY = (n.getX() - Player.getX()) * -1;
		Player.getPA().createPlayersProjectile(n.getX() + 1, n.getY() + 1, offX, offY, 50, 106, 1192, 66, 31,
				-Player.getId() - 1, 76, 0);

		CycleEventHandler.getSingleton().addEvent(0, Player, new CycleEvent() {

			@Override
			public void execute(CycleEventContainer container) {
				int damage = Misc.random(n.maxHit);
				if (Player.prayerActive[17]) {
					damage = 0;
				}
				if (Misc.random(10 + Player.getCombat().calculateRangeDefence()) > Misc.random(n.attack + 10)) {
					damage = 0;
				}
				if (damage > Player.getLevel()[SkillIndex.HITPOINTS.getSkillId()]) {
					damage = Player.getLevel()[SkillIndex.HITPOINTS.getSkillId()];
				}
				Player.dealDamage(damage);
				Player.handleHitMask(damage);
				Player.getPA().refreshSkill(3);
				Player.updateRequired = true;
				container.stop();
			}

			@Override
			public void stop() {
			}

		}, 1_800);
	}
}