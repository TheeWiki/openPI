package server.model.npcs;

import java.util.HashMap;

import server.Server;
import server.model.npcs.impl.FlockleaderGerin;
import server.model.players.Player;

public abstract class SpecialNPC {

	public abstract void execute(Player Player, NPC n);

	private static HashMap<Integer, SpecialNPC> npcMap = new HashMap<Integer, SpecialNPC>();

	static {
		npcMap.put(3164, new FlockleaderGerin());
	}

	public static SpecialNPC forId(int npcType) {
		return npcMap.get(npcType);
	}

	public static void executeAttack(Player Player, int i) {
		SpecialNPC n = SpecialNPC.forId(NPCHandler.npcs[i].npcType);
		if (n == null || Player == null) {
			return;
		}
		if (Player.isDead || Player.getLevel()[3] <= 0 || NPCHandler.npcs[i].isDead || NPCHandler.npcs[i].HP <= 0) {
			return;
		}
		if (Player.distanceToPoint(NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY()) > Server.npcHandler
				.followDistance(i)) {
			return;
		}
		if (Player.playerIndex <= 0 && Player.npcIndex <= 0)
			if (Player.autoRet == 1)
				Player.npcIndex = i;
		Player.getPA().removeAllWindows();
		NPCHandler.npcs[i].facePlayer(Player.playerId);
		Player.logoutDelay = System.currentTimeMillis();
		Player.underAttackBy2 = NPCHandler.npcs[i].npcId;
		Player.singleCombatDelay = System.currentTimeMillis();
		NPCHandler.npcs[i].killerId = Player.playerId;
		n.execute(Player, NPCHandler.npcs[i]);
		Player.getPA().removeAllWindows();
	}
}
