package server.model.players.combat.range;

import server.Server;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.event.Event;
import server.event.EventContainer;
import server.event.EventManager;
import server.model.npcs.NPC;
import server.model.npcs.NPCHandler;
import server.model.objects.Objects;
import server.model.players.Client;
import server.model.players.skills.SkillIndex;
import server.util.Misc;
import server.util.Direction;

public class DwarfMultiCannon {

    /**
     * To-Do: Exception when trying to set up a cannon within 3 coords of another one
     *            NPC distance checking
     *	       Projectiles(Not sure if it works)
     *
     * @author relex lawl && xsj
     */
    ;
    private Direction s = Direction.SOUTH_EAST;
    private Client player;
    public DwarfMultiCannon(Client client) {
        this.player = client;
    }
    private static final int CANNON_BASE = 7, CANNON_STAND = 8, CANNON_BARRELS = 9, CANNON = 6;
    private static final int CANNONBALL = 2, CANNON_BASE_ID = 6, CANNON_STAND_ID = 8, CANNON_BARRELS_ID = 10, CANNON_FURNACE_ID = 12;

    public void setUpCannon() {
    	if (!canSetUpCannon() && player.playerLevel[SkillIndex.RANGE.getSkillId()] < 60)
    	{
    		player.sendMessage("You don't have a Range level of 60 to set the cannon up");
    		return;
    	}
//        if (!canSetUpCannon())
//            return;
        EventManager.getSingleton().addEvent(new Event() {
            int time = 4;
            public void execute(EventContainer setup) {
                if (!canSetUpCannon())
                    setup.stop();
                player.sendMessage("@blu@You begin to assemble the cannon..");
                switch (time) {
                case 4: 
                    if (!player.getItems().playerHasItem(CANNON_BASE_ID))
                        setup.stop();
                    player.startAnimation(827);
                    player.getPA().walkTo(-1, 0);
                    player.turnPlayerTo(player.absX + 1, player.absY + 1);
                    player.hasCannon = true;
                    player.settingUpCannon = true;
                    player.setUpBase = true;
                    Objects base = new Objects(CANNON_BASE, player.absX, player.absY, 0, 0, 10, 0);
                    Server.objectHandler.addObject(base);
                    Server.objectHandler.placeObject(base);
                    player.oldCannon = base;
                    player.getItems().deleteItem(CANNON_BASE_ID, 1);
                    base.belongsTo = player.playerName;
                    break;

                case 3:
                    if (!player.getItems().playerHasItem(CANNON_STAND_ID)) {
                        player.settingUpCannon = false; 
                        setup.stop();
                    }
                    player.startAnimation(827);
                    player.setUpStand = true;
                    Objects stand = new Objects(CANNON_STAND, player.absX, player.absY, 0, 0, 10, 0);
                    Server.objectHandler.removeObject(player.oldCannon);
                    Server.objectHandler.addObject(stand);
                    Server.objectHandler.placeObject(stand);
                    player.oldCannon = stand;
                    player.getItems().deleteItem(CANNON_STAND_ID, 1);
                    stand.belongsTo = player.playerName;
                    break;

                case 2:
                    if (!player.getItems().playerHasItem(CANNON_BARRELS_ID)) {
                        player.settingUpCannon = false;
                        setup.stop();
                    }
                    player.startAnimation(827);
                    player.setUpBarrels = true;
                    Objects barrel = new Objects(CANNON_BARRELS, player.absX, player.absY, 0, 0, 10, 0);
                    Server.objectHandler.removeObject(player.oldCannon);
                    Server.objectHandler.addObject(barrel);
                    Server.objectHandler.placeObject(barrel);
                    player.oldCannon = barrel;
                    player.getItems().deleteItem(CANNON_BARRELS_ID, 1);
                    barrel.belongsTo = player.playerName;
                    break;

                case 1:
                    if (!player.getItems().playerHasItem(CANNON_FURNACE_ID)) {
                        player.settingUpCannon = false;
                        setup.stop();
                    }
                    player.startAnimation(827);
                    player.setUpFurnace = true;
                    Objects cannon = new Objects(CANNON, player.absX, player.absY, 0, 0, 10, 0);
                    player.cannonBaseX = player.absX;
                    player.cannonBaseY = player.absY;
                    player.cannonBaseH = player.heightLevel;
                    Server.objectHandler.removeObject(player.oldCannon);
                    Server.objectHandler.addObject(cannon);
                    Server.objectHandler.placeObject(cannon);
                    player.oldCannon = cannon;
                    player.getItems().deleteItem(CANNON_FURNACE_ID, 1);
                    cannon.belongsTo = player.playerName;
                    break;

                case 0:
                    player.settingUpCannon = false;
                    setup.stop();
                    break;
                }
                if (time > 0)
                    time--;
            }
        }, 2000);
    }

    public void shootCannon() {
        Objects cannon = null;
        for (server.model.objects.Objects o: Server.objectHandler.globalObjects) {
            if (o.objectX == player.cannonBaseX && o.objectY == player.cannonBaseY && o.objectHeight == player.cannonBaseH) {
                cannon = o;
            }
        }
        if (cannon == null) {
            player.sendMessage("This is not your cannon!");
            return;
        }
        if (player.cannonIsShooting) {
            if (player.getItems().playerHasItem(CANNONBALL)) {
                int amountOfCannonBalls = player.getItems().getItemAmount(CANNONBALL) > 30 ? 30 : player.getItems().getItemAmount(CANNONBALL);
                player.cannonBalls += amountOfCannonBalls;
            } else {
                player.sendMessage("Your cannon is already firing!");
                return;
            }
        }
        if (player.cannonBalls < 1) {
            int amountOfCannonBalls = player.getItems().getItemAmount(CANNONBALL) > 30 ? 30 : player.getItems().getItemAmount(CANNONBALL);
            if (amountOfCannonBalls < 1) {
                player.sendMessage("You need ammo to shoot this cannon!");
                return;
            }
            player.cannonBalls = amountOfCannonBalls;
            player.getItems().deleteItem(CANNONBALL, player.getItems().getItemSlot(CANNONBALL), amountOfCannonBalls);
        } else
            startFiringCannon(cannon);
    }

    private void startFiringCannon(final Objects cannon) {
        player.cannonIsShooting = true;

        EventManager.getSingleton().addEvent(new Event() {
            public void execute(EventContainer fire) {
                if (player.cannonBalls < 1) {
                   
                    player.cannonIsShooting = false;
                    fire.stop();
                    player.sendMessage("Your cannon has run out of ammo!");
                } else {
                    //player.rotation++;
                   // rotateCannon(cannon);
                    rotate(cannon);
                }
            }
        }, (player.inMulti() ? 800 : 800));
        
    }

   public void pickUpCannon() {
        Objects cannon = null;
        for (server.model.objects.Objects o: Server.objectHandler.globalObjects) {
            if (o.objectX == player.cannonBaseX && o.objectY == player.cannonBaseY && o.objectHeight == player.cannonBaseH) {
                cannon = o;
            }
        }
        if (cannon == null) {
            player.sendMessage("This is not your cannon!");
            return;
        }
        player.startAnimation(827);
        server.model.objects.Objects empty = new server.model.objects.Objects(100, cannon.objectX, cannon.objectY, 0, 0, 10, 0);
        Server.objectHandler.addObject(empty);
        Server.objectHandler.placeObject(empty);
        Server.objectHandler.removeObject(empty);
        if (player.setUpBase) {
            if (player.getItems().freeSlots() > 0)
                player.getItems().addItem(CANNON_BASE_ID, 1);
            else {
//                player.getItems().addItemToBank(CANNON_BASE_ID, 1);
                player.sendMessage("You did not have enough inventory space, so this cannon part was banked.");
            }
            player.setUpBase = false;
        }
        if (player.setUpStand) {
            if (player.getItems().freeSlots() > 0)
                player.getItems().addItem(CANNON_STAND_ID, 1);
            else {
//                player.getItems().addItemToBank(CANNON_STAND_ID, 1);
                player.sendMessage("You did not have enough inventory space, so this cannon part was banked.");
            }
            player.setUpStand = false;
        }
        if (player.setUpBarrels) {
            if (player.getItems().freeSlots() > 0)
                player.getItems().addItem(CANNON_BARRELS_ID, 1);
            else {
//                player.getItems().addItemToBank(CANNON_BARRELS_ID, 1);
                player.sendMessage("You did not have enough inventory space, so this cannon part was banked.");
            }
            player.setUpBarrels = false;
        }
        if (player.setUpFurnace) {
            if (player.getItems().freeSlots() > 0)
                player.getItems().addItem(CANNON_FURNACE_ID, 1);
            else {
//                player.getItems().addItemToBank(CANNON_FURNACE_ID, 1);
                player.sendMessage("You did not have enough inventory space, so this cannon part was banked.");
            }
            player.setUpFurnace = false;
        }
        if (player.cannonBalls > 0) {
            if (player.getItems().freeSlots() > 0)
                player.getItems().addItem(CANNONBALL, player.cannonBalls);
            else {
//                player.getItems().addItemToBank(CANNONBALL, player.cannonBalls);
                player.sendMessage("You did not have enough inventory space, so your cannonballs have been banked.");
            }
            player.cannonBalls = 0;
        }
    }
    boolean b;
    public void startCannon() {
        CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {

            @
            Override
            public void execute(CycleEventContainer container) {
                if (player.cannonBalls > 0) {	
                    for (int j = 0; j < NPCHandler.npcs.length; j++) {
                        if (NPCHandler.npcs[j] != null  && !(NPCHandler.npcs[j].MaxHP == 0)) {
                            if (!NPCHandler.npcs[j].isDead && player.goodDistance(NPCHandler.npcs[j].absX, NPCHandler.npcs[j].absY, player.cannonBaseX, player.cannonBaseY, 6)) {
                                // if(checkFire(player.cannon, NPCHandler.npcs[j])) {
                                if (!player.inMulti() && NPCHandler.npcs[j] != null) {
                                	 fire(player.cannon, NPCHandler.npcs[j]);           	 
                                    break;
                                }
                            }
                        }
                    }
                } else if (!b) {
                   
                    container.stop();
                     player.sendMessage("Your cannon has ran out of balls!");
                }
            }

            @
            Override
            public void stop() {
                b = false;
                player.shooting = false;
                s = Direction.SOUTH_EAST;
            }

        }, player.shooting ? Misc.random(6) : 2);
    }
    public void rotate(Objects cannon) {
    	startCannon();
        if (cannon == null) return;
        int x = cannon.getObjectX();
        int y = cannon.getObjectY();

        switch (s) {
        case NORTH:
            player.getPA().objectAnim(x, y, s.getId(), 10, -1);
            s = Direction.NORTH_EAST;
            break;
        case NORTH_EAST:
            player.getPA().objectAnim(x, y, s.getId(), 10, -1);
            s = Direction.EAST;
            break;
        case EAST:
            player.getPA().objectAnim(x, y, s.getId(), 10, -1);
            s = Direction.SOUTH_EAST;
            break;
        case SOUTH_EAST:
            player.getPA().objectAnim(x, y, s.getId(), 10, -1);
            s = Direction.SOUTH;
            break;
        case SOUTH:
            player.getPA().objectAnim(x, y, s.getId(), 10, -1);
            s = Direction.SOUTH_WEST;
            break;
        case SOUTH_WEST:
            player.getPA().objectAnim(x, y, s.getId(), 10, -1);
            s = Direction.WEST;
            break;
        case WEST:
            player.getPA().objectAnim(x, y, s.getId(), 10, -1);
            s = Direction.NORTH_WEST;
            break;
        case NORTH_WEST:
            player.getPA().objectAnim(x, y, s.getId(), 10, -1);
            s = Direction.NORTH;
            break;
        }
    }
    public boolean checkFire(Objects cannon, NPC npc) {
        int obX = cannon.getObjectX();
        int obY = cannon.getObjectY();
        int npcX = npc.absX;
        int npcY = npc.absY;
        if (!npc.isDead && npc.heightLevel == player.heightLevel) {
            player.getCannonCoords().fillUp(obX, obY);
            switch (s) {
            case WEST:
            case SOUTH:
            case NORTH:
            case EAST:
            case NORTH_EAST:
            case SOUTH_EAST:
            case SOUTH_WEST:
            case NORTH_WEST:
                return player.getCannonCoords().checkCoords(s, npcX, npcY);
            }
        }
        return false;
    }

    /**
     * TODO: Get cannon ball projectile ID (shoots blanks atm)
     * @param cannon2
     * @param npc
     */
    public void fire(DwarfMultiCannon cannon2, NPC npc) {
        if (npc.isDead) {
            return;
        }
       // if (npc.MaxHP != 0 && npc.goodDistance(player.absX, player.absY, npc.absX, npc.absY, 12)) {
        player.shooting = true;
        int oX = 1/*Objects.objectX + 1*/;
        int oY = 1/*Objects.objectY - 1*/;
        int offX = ((++oX - npc.getX()) * -1);
        int offY = ((++oY - npc.getY()) * -1);
        player.getPA().createPlayersProjectile(oX, oY, offY, offX, 50, 60, 53, 20, 20, - player.oldNpcIndex + 1, 30);
        int damage = Misc.random(20);
        player.getPA().addSkillXP(45.5 + damage + Misc.random(3), SkillIndex.RANGE.getSkillId());
        npc.dealDamage(damage);
        npc.killerId = player.getId();
        npc.facePlayer(1); //might need to get player id if it doesnt face automatically == Objects.belongsTo = player.playerName;
        npc.hitDiff = damage;
        npc.HP -= damage;
        npc.hitUpdateRequired = true;
        //npc.forceChat("test ryan xsj");
        player.cannonBalls--;
       // }
        if (npc.HP <= 0)
            npc.isDead = true;
    }
    public static int distanceToSquare(int x, int y, int tx, int ty) {
        return (int) Math.sqrt((Math.abs(x - tx) + Math.abs(y - ty)));
    }

    private final boolean canSetUpCannon() {
        return inGoodArea() || player.playerLevel[SkillIndex.HITPOINTS.getSkillId()] > 0 || !player.hasCannon || !player.settingUpCannon;
    }

    private final boolean inGoodArea() {
        /*if (ClanWars.inSafeFFA) {
			player.sendMessage("You are not allowed to set up a cannon in clan wars!");
			return false;
		}
		if (BountyHunter.inBH(player)) {
			player.sendMessage("You are not allowed to set up a cannon in bounty hunter!");
			return false;
		}*/
        return true;
    }
}