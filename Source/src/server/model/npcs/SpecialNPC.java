package server.model.npcs;

import java.util.HashMap;

import server.Server;
import server.model.npcs.impl.FlockleaderGerin;
import server.model.players.Client;

public abstract class SpecialNPC {

	public abstract void execute(Client client, NPC n);

	private static HashMap<Integer, SpecialNPC> npcMap = new HashMap<Integer, SpecialNPC>();

	static {
		npcMap.put(3164, new FlockleaderGerin());
	}

	public static SpecialNPC forId(int npcType) {
		return npcMap.get(npcType);
	}

	public static void executeAttack(Client client, int i) {
		SpecialNPC n = SpecialNPC.forId(NPCHandler.npcs[i].npcType);
		if (n == null || client == null) {
			return;
		}
		if (client.isDead || client.getLevel()[3] <= 0 || NPCHandler.npcs[i].isDead || NPCHandler.npcs[i].HP <= 0) {
			return;
		}
		if (client.distanceToPoint(NPCHandler.npcs[i].getX(), NPCHandler.npcs[i].getY()) > Server.npcHandler
				.followDistance(i)) {
			return;
		}
		if (client.playerIndex <= 0 && client.npcIndex <= 0)
			if (client.autoRet == 1)
				client.npcIndex = i;
		client.getPA().removeAllWindows();
		NPCHandler.npcs[i].facePlayer(client.playerId);
		client.logoutDelay = System.currentTimeMillis();
		client.underAttackBy2 = NPCHandler.npcs[i].npcId;
		client.singleCombatDelay = System.currentTimeMillis();
		NPCHandler.npcs[i].killerId = client.playerId;
		n.execute(client, NPCHandler.npcs[i]);
		client.getPA().removeAllWindows();
	}
}
