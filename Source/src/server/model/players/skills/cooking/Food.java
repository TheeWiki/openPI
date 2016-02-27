package server.model.players.skills.cooking;

import java.util.HashMap;

import server.model.minigames.duel_arena.Rules;
import server.model.players.Player;

/**
 * @author Sanity
 */

public class Food {
	
	public enum FoodToEat {		
		MANTA(391,22,"Manta Ray"),
		SHARK(385,20,"Shark"),
		LOBSTER(379,12,"Lobster"),
		TROUT(333,7,"Trout"),
		SALMON(329,9,"Salmon"),
		SWORDFISH(373,14,"Swordfish"),
		TUNA(361,10,"Tuna"),
		MONKFISH(7946,16,"Monkfish"),
		SEA_TURTLE(397,22,"Sea Turtle"),
		TUNA_POTATO(7060,22,"Tuna Potato");
		
		
		private int id; private int heal; private String name;
		
		private FoodToEat(int id, int heal, String name) {
			this.id = id;
			this.heal = heal;
			this.name = name;		
		}
		
		public int getId() {
			return id;
		}

		public int getHeal() {
			return heal;
		}
		
		public String getName() {
			return name;
		}
		public static HashMap <Integer,FoodToEat> food = new HashMap<Integer,FoodToEat>();
		
		public static FoodToEat forId(int id) {
			return food.get(id);
		}
		
	static {
		for (FoodToEat f : FoodToEat.values())
			food.put(f.getId(), f);
		}
	}
	
	public static void eat(Player player, int id, int slot) {
		if (player.duelRule[Rules.EAT_RULE.getRule()]) {
			player.getActionSender().sendMessage("You may not eat in this duel.");
			return;
		}
		if (System.currentTimeMillis() - player.foodDelay >= 1500 && player.playerLevel[3] > 0) {
			player.getCombat().resetPlayerAttack();
			player.attackTimer += 2;
			player.startAnimation(829);
			player.getItems().deleteItem(id,slot,1);
			FoodToEat f = FoodToEat.food.get(id);
			if (player.playerLevel[3] < player.getLevelForXP(player.playerXP[3])) {
				player.playerLevel[3] += f.getHeal();
				if (player.playerLevel[3] > player.getLevelForXP(player.playerXP[3]))
					player.playerLevel[3] = player.getLevelForXP(player.playerXP[3]);
			}
			player.foodDelay = System.currentTimeMillis();
			player.getPA().refreshSkill(3);
			player.getPA().sendSound(player.getSound().FOODEAT);
			player.getActionSender().sendMessage("You eat the " + f.getName() + ".");
		}		
	}
}