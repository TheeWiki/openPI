package server.model.players.combat;

import java.util.HashMap;

import server.Server;
import server.model.npcs.NPCHandler;
import server.model.players.Client;
import server.model.players.PlayerHandler;
import server.util.Misc;

public class Specials {

	private Client c;
	public Specials(Client c) {
		this.c = c;
	}
		 	 		
	private enum specialAttack {
	
	//ItemName(ItemId, SpecDamage, SpecAccuracy, SpecAmount, Anim, GFX0, GFX100, DoubleHit, SsSpec, SpecEffect)
	
	ABYSSAL_WHIP(4151, 1, 1, 5, 1658, 341, -1, false, false, 0),
	DRAGON_DAGGER(1215, .95, 1.15, 2.5, 1062, -1, 252, true, false, 0),
	DRAGON_DAGGER_P(1231, .85, 1.15, 2.5, 1062, -1, 252, true, false, 0),
	DRAGON_DAGGER_PP(5698, .85, 1.15, 2.5, 1062, -1, 252, true, false, 0),
	DRAGON_DAGGER_PPP(5680, .85, 1.15, 2.5, 1062, -1, 252, true, false, 0),
	DRAGON_LONG(1305, 1.20, 1.10, 2.5, 1058, -1, 248, false, false, 0),
	DRAGON_MACE(1434, 1.55, .85, 2.5, 1060, -1, 251, false, false, 0),
	DRAGON_SCIMITAR(4587, 1, 1, 2.5, 1872, -1, 347, false, false, 1),
	DRAGON_HALBERD(3204, 1.25, .85, 3.3, 1203, -1, 282, true, false, 0),
	BARRELCHEST_ANCHOR(10887, 1.55, 1.05, 5, 5870, -1, 1027, false, false, 0),
	GRANITE_MAUL(4153, 1.10, .85, 5, 1667, -1, 337, false, false, 0),
	KORASIS_SWORD(18786, 1.55, 1.55, 5.5, 1872, -1, 1224, false, false, 0),
	ARMADYL_GODSWORD(11694, 1.55, 1.75, 5, 7074, 1222, -1, false, false, 0),
	BANDOS_GODSWORD(11696, 1.35, 1.75, 10, 7073, 1223, -1, false, false, 3),
	SARADOMIN_GODSWRD(11698, 1.35, 1.75, 5, 7071, 1220, -1, false, false, 4),
	ZAMORAK_GODSWORD(11700, 1.35, 1.75, 5, 7070, 1221, -1, false, false, 2),
	SARADOMIN_SWORD(11730, 1.10, 1.35, 10, 7072, -1, 1224, true, true, 0),
	VESTAS_LONGSWORD(13899, 1.55, 1.35, 2.5, 10502, -1, -1, false, false, 0),
	VESTAS_SPEAR(13905, 1.35, 1.45, 5, 10499, 1835, -1, false, false, 5),
	STATIUSS_WARHAMMER(13902, 1.55, 1.25, 3.3, 10505, 1840, -1, false, false, 0),
	MORRIGANS_JAVELIN(13879, 1.55, 1.25, 5, 10501, 1836, -1, false, false, 7),
	MORRIGANS_THROWNAXE(13883, 1.55, 1.25, 2.5, 10504, 1838, -1, false, false, 0),
	MAGIC_SHORTBOW(861, 1.05, .95, 5.5, 1074, -1, -1, true, false, 0),
	MAGIC_LONGBOW(859, 1.20, 1.05, 5.5, 426, -1, -1, false, false, 0),
	DARK_BOW(11235, 1.35, 1.35, 5.5, 426, -1, -1, true, false, 0),
	HAND_CANNON(15241, 1.45, 1.25, 5, 12175, 2138, -1, false, false, 0);
		
		private int weapon, anim, gfx1, gfx2, specEffect;
		private double specDamage, specAccuracy, specAmount;
		private boolean doubleHit, ssSpec;
		
		private specialAttack(int weapon, double specDamage, double specAccuracy, double specAmount, int anim, int gfx1, int gfx2, boolean doubleHit, boolean ssSpec, int specEffect) {
			this.weapon = weapon;
			this.specDamage = specDamage;
			this.specAccuracy = specAccuracy;
			this.specAmount = specAmount;
			this.anim = anim;
			this.gfx1 = gfx1;
			this.gfx2 = gfx2;			
			this.doubleHit = doubleHit;
			this.ssSpec = ssSpec;
			this.specEffect = specEffect;
		}
		
		private int getWeapon() {
			return weapon;
		}
		
		private double getSpecDamage() {
			return specDamage;
		}
		
		private double getSpecAccuracy() {
			return specAccuracy;
		}
		
		private double getSpecAmount() {
			return specAmount;
		}
		
		private int getAnim() {
			return anim;
		}
		
		private int getGfx1() {
			return gfx1;
		}
		
		private int getGfx2() {
			return gfx2;
		}
		
		private boolean getDoubleHit() {
			return doubleHit;
		}
		
		private boolean getSsSpec() {
			return ssSpec;
		}
		
		@SuppressWarnings("unused")
		private int getSpecEffect() {
			return specEffect;
		}
		public static HashMap <Integer,specialAttack> specialAttack = new HashMap<Integer,specialAttack>();

		@SuppressWarnings("unused")
		public static specialAttack getWeapon(int weapon) {
			return specialAttack.get(weapon);
		}
		static {
			for (specialAttack SA : specialAttack.values())
				specialAttack.put(SA.getWeapon(), SA);
		}
	}
		
	public void activateSpecial(int weapon, int i) {
		int equippedWeapon = c.playerEquipment[c.playerWeapon];	
		if(Server.npcHandler.npcs[i] == null && c.npcIndex > 0) {
			return;
		}
		if(Server.playerHandler.players[i] == null && c.playerIndex > 0) {
			return;
		}
		c.doubleHit = false;
		c.specEffect = 0;
		c.projectileStage = 0;
		c.specMaxHitIncrease = 2;
		if(c.npcIndex > 0) {
			c.oldNpcIndex = i;
		} else if (c.playerIndex > 0){
			c.oldPlayerIndex = i;
			Server.playerHandler.players[i].underAttackBy = c.playerId;
			Server.playerHandler.players[i].logoutDelay = System.currentTimeMillis();
			Server.playerHandler.players[i].singleCombatDelay = System.currentTimeMillis();
			Server.playerHandler.players[i].killerId = c.playerId;
		}
		c.specEffect = 0;
		c.projectileStage = 0;
		for (specialAttack SA : specialAttack.values()) {
			if (NPCHandler.npcs[c.npcIndex] == null && c.npcIndex > 0) {
				return;
			}
			if (PlayerHandler.players[c.playerIndex] == null && c.playerIndex > 0) {
				return;
			}
			if (equippedWeapon == SA.getWeapon()) {
				if (SA.getWeapon() == 11235) {
					c.usingBow = true;
					c.dbowSpec = true;
					c.rangeItemUsed = c.playerEquipment[c.playerArrows];
					c.getItems().deleteArrow();
					c.getItems().deleteArrow();
					c.lastWeaponUsed = weapon;
					c.hitDelay = 3;
					c.startAnimation(426);
					c.projectileStage = 1;
					c.gfx100(c.getCombat().getRangeStartGFX());
					c.hitDelay = c.getCombat().getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());					
					if (c.fightMode == 2)
						c.attackTimer--;
					if (c.playerIndex > 0)
						c.getCombat().fireProjectilePlayer();
					else if (c.npcIndex > 0)
						c.getCombat().fireProjectileNpc();
					c.specAccuracy = SA.getSpecAccuracy();
					c.specDamage = SA.getSpecDamage();
				}
				else if (SA.getWeapon() == 15241) {
					c.usingBow = true;
					c.rangeItemUsed = c.playerEquipment[c.playerArrows];
					c.getItems().deleteArrow();	
					c.lastWeaponUsed = weapon;
					c.startAnimation(12175);
					c.specAccuracy = SA.getSpecAccuracy();
					c.specDamage = SA.getSpecDamage();
					c.hitDelay = 5;
					c.attackTimer-= 7;
					c.hitDelay = c.getCombat().getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());					
					if (c.fightMode == 2)
					if (c.playerIndex > 0)
						c.getCombat().fireProjectilePlayer();
					else if (c.npcIndex > 0)
						c.getCombat().fireProjectileNpc();
				}
				else if (SA.getWeapon() == 13879 || SA.getWeapon() == 13883) {
					c.usingRangeWeapon = true;
					c.rangeItemUsed = c.playerEquipment[c.playerWeapon];
					c.getItems().deleteArrow();
					c.lastWeaponUsed = weapon;
					c.startAnimation(SA.getAnim());
					c.gfx0(SA.getGfx1());
					c.specAccuracy = SA.getSpecAccuracy();
					c.specDamage = SA.getSpecDamage();
					c.projectileStage = 1;					
					c.hitDelay = c.getCombat().getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					if (c.fightMode == 2)
						c.attackTimer--;
					if (c.playerIndex > 0)
						c.getCombat().fireProjectilePlayer();
					else if (c.npcIndex > 0)
						c.getCombat().fireProjectileNpc();
				}
				else if (SA.getWeapon() == 859 || SA.getWeapon() == 861) {
					c.usingBow = true;			
					c.bowSpecShot = 1;
					c.rangeItemUsed = c.playerEquipment[c.playerArrows];
					c.getItems().deleteArrow();	
					c.lastWeaponUsed = weapon;
					c.startAnimation(SA.getAnim());
					c.projectileStage = 1;
					c.hitDelay = c.getCombat().getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					if (c.fightMode == 2)
						c.attackTimer--;
					if (c.playerIndex > 0)
						c.getCombat().fireProjectilePlayer();
					else if (c.npcIndex > 0)
						c.getCombat().fireProjectileNpc();
				}
				else if (SA.getGfx1() == -1) {
					c.gfx100(SA.getGfx2());
					c.startAnimation(SA.getAnim());
					c.specDamage = SA.getSpecDamage();
					c.specAccuracy = SA.getSpecAccuracy();
					c.hitDelay = c.getCombat().getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.doubleHit = SA.getDoubleHit();
					c.ssSpec = SA.getSsSpec();
				} else {				
					c.gfx0(SA.getGfx1());
					c.startAnimation(SA.getAnim());
					c.specDamage = SA.getSpecDamage();
					c.specAccuracy = SA.getSpecAccuracy();
					c.hitDelay = c.getCombat().getHitDelay(c.getItems().getItemName(c.playerEquipment[c.playerWeapon]).toLowerCase());
					c.doubleHit = SA.getDoubleHit();
					c.ssSpec = SA.getSsSpec();
				}
			}
		c.delayedDamage = Misc.random(c.getCombat().calculateMeleeMaxHit());
		c.delayedDamage2 = Misc.random(c.getCombat().calculateMeleeMaxHit());
		c.usingSpecial = false;
		c.getItems().updateSpecialBar();
		}
	}
	
	public double specAmount() {
		for (specialAttack SA : specialAttack.values()) {
			if (c.playerEquipment[c.playerWeapon] == SA.getWeapon()) {
				return SA.getSpecAmount();
			}
		}
	return 0;
	}
	
}