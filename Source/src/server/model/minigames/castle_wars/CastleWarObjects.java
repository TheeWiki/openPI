package server.model.minigames.castle_wars;

import server.model.players.Player;
import server.model.players.EquipmentListener;

public class CastleWarObjects {

    public static void handleObject(Player player, int id, int x, int y) {
        if (!CastleWars.isInCw(player)) {
//            c.sendMessage("You are not in Castlewars.");
            return;
        }
        switch (id) {
        case 4385:
        case 4386:
        	player.getPA().showInterface(11169);
        	break;
            case 4469:
                if (CastleWars.getTeamNumber(player) == 2) {
                    player.getActionSender().sendMessage("You are not allowed in the other teams spawn point.");
                    break;
                }
                if (x == 2426) {
                    if (player.getY() == 3080) {
                        player.getPA().movePlayer(2426, 3081, player.heightLevel);
                    } else if (player.getY() == 3081) {
                        player.getPA().movePlayer(2426, 3080, player.heightLevel);
                    }
                } else if (x == 2422) {
                    if (player.getX() == 2422) {
                        player.getPA().movePlayer(2423, 3076, player.heightLevel);
                    } else if (player.getX() == 2423) {
                        player.getPA().movePlayer(2422, 3076, player.heightLevel);
                    }
                }
                break;
            case 4470:
                if (CastleWars.getTeamNumber(player) == 1) {
                    player.getActionSender().sendMessage("You are not allowed in the other teams spawn point.");
                    break;
                }
                if (x == 2373 && y == 3126) {
                    if (player.getY() == 3126) {
                        player.getPA().movePlayer(2373, 3127, 1);
                    } else if (player.getY() == 3127) {
                        player.getPA().movePlayer(2373, 3126, 1);
                    }
                } else if (x == 2377 && y == 3131) {
                    if (player.getX() == 2376) {
                        player.getPA().movePlayer(2377, 3131, 1);
                    } else if (player.getX() == 2377) {
                        player.getPA().movePlayer(2376, 3131, 1);
                    }
                }
                break;
            case 4417:
                if (x == 2428 && y == 3081 && player.heightLevel == 1) {
                    player.getPA().movePlayer(2430, 3080, 2);
                }
                if (x == 2425 && y == 3074 && player.heightLevel == 2) {
                    player.getPA().movePlayer(2426, 3074, 3);
                }
                if (x == 2419 && y == 3078 && player.heightLevel == 0) {
                    player.getPA().movePlayer(2420, 3080, 1);
                }
                break;
            case 4415:
                if (x == 2419 && y == 3080 && player.heightLevel == 1) {
                    player.getPA().movePlayer(2419, 3077, 0);
                }
                if (x == 2430 && y == 3081 && player.heightLevel == 2) {
                    player.getPA().movePlayer(2427, 3081, 1);
                }
                if (x == 2425 && y == 3074 && player.heightLevel == 3) {
                    player.getPA().movePlayer(2425, 3077, 2);
                }
                if (x == 2374 && y == 3133 && player.heightLevel == 3) {
                    player.getPA().movePlayer(2374, 3130, 2);
                }
                if (x == 2369 && y == 3126 && player.heightLevel == 2) {
                    player.getPA().movePlayer(2372, 3126, 1);
                }
                if (x == 2380 && y == 3127 && player.heightLevel == 1) {
                    player.getPA().movePlayer(2380, 3130, 0);
                }
                break;
            case 4411:
                if (x == 2421 && y == 3073 && player.heightLevel == 1) {
                    player.getPA().movePlayer(player.getX(), player.getY(), 0);
                }
                break;
            case 4419:
                if (x == 2417 && y == 3074 && player.heightLevel == 0) {
                    player.getPA().movePlayer(2416, 3074, 0);
                }
                if (x == 2417 && y == 3074 && player.heightLevel == 1) {
                    player.getPA().movePlayer(2417, 3073, 0);
                }
                break;
            case 4911:
                if (x == 2421 && y == 3073 && player.heightLevel == 1) {
                    player.getPA().movePlayer(2421, 3074, 0);
                }
                if (x == 2378 && y == 3134 && player.heightLevel == 1) {
                    player.getPA().movePlayer(2378, 3133, 0);
                }
                break;
            case 1747:
                if (x == 2421 && y == 3073 && player.heightLevel == 0) {
                    player.getPA().movePlayer(2421, 3074, 1);
                }
                if (x == 2378 && y == 3134 && player.heightLevel == 0) {
                    player.getPA().movePlayer(2378, 3133, 1);
                }
                break;
            case 4912:
                if (x == 2430 && y == 3082 && player.heightLevel == 0) {
                    player.getPA().movePlayer(player.getX(), player.getY() + 6400, 0);
                }
                if (x == 2369 && y == 3125 && player.heightLevel == 0) {
                    player.getPA().movePlayer(player.getX(), player.getY() + 6400, 0);
                }
                break;
            case 1757:
                if (x == 2430 && y == 9482) {
                    player.getPA().movePlayer(2430, 3081, 0);
                } else {
                    player.getPA().movePlayer(2369, 3126, 0);
                }
                break;

            case 4418:
                if (x == 2380 && y == 3127 && player.heightLevel == 0) {
                    player.getPA().movePlayer(2379, 3127, 1);
                }
                if (x == 2369 && y == 3126 && player.heightLevel == 1) {
                    player.getPA().movePlayer(2369, 3127, 2);
                }
                if (x == 2374 && y == 3131 && player.heightLevel == 2) {
                    player.getPA().movePlayer(2373, 3133, 3);
                }
                break;
            case 4420:
                if (x == 2382 && y == 3131 && player.heightLevel == 0) {
                    if (player.getX() >= 2383 && player.getX() <= 2385) {
                        player.getPA().movePlayer(2382, 3130, 0);
                    } else {
                        player.getPA().movePlayer(2383, 3133, 0);
                    }
                }
                break;
            case 4437:
                if (x == 2400 && y == 9512) {
                    player.getPA().movePlayer(2400, 9514, 0);
                } else if (x == 2391 && y == 9501) {
                    player.getPA().movePlayer(2393, 9502, 0);
                } else if (x == 2409 && y == 9503) {
                    player.getPA().movePlayer(2411, 9503, 0);
                } else if (x == 2401 && y == 9494) {
                    player.getPA().movePlayer(2401, 9493, 0);
                }
                break;
            case 1568:
                if (x == 2399 && y == 3099) {
                    player.getPA().movePlayer(2399, 9500, 0);
                } else {
                    player.getPA().movePlayer(2400, 9507, 0);
                }
            case 6281:
                player.getPA().movePlayer(2370, 3132, 2);
                break;
            case 4472:
                player.getPA().movePlayer(2370, 3132, 1);
                break;
            case 6280:
                player.getPA().movePlayer(2429, 3075, 2);
                break;
            case 4471:
                player.getPA().movePlayer(2429, 3075, 1);
                break;
            case 4406:
                CastleWars.removePlayerFromCw(player);
                break;
            case 4407:
                CastleWars.removePlayerFromCw(player);
                break;
            case 4458:
                player.startAnimation(881);
                player.getItems().addItem(4049, 1);
                player.getActionSender().sendMessage("You get some bandages");
                break;
            case 4902: //sara flag
            case 4377:
                switch (CastleWars.getTeamNumber(player)) {
                    case 1:
                        CastleWars.returnFlag(player, player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]);
                        break;
                    case 2:
                        CastleWars.captureFlag(player);
                        break;
                }
                break;
            case 4903: //zammy flag
            case 4378:
                switch (CastleWars.getTeamNumber(player)) {
                    case 1:
                        CastleWars.captureFlag(player);
                        break;
                    case 2:
                        CastleWars.returnFlag(player, player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]);
                        break;
                }
                break;
            case 4461: //barricades
                player.getActionSender().sendMessage("You get a barricade!");
                player.getItems().addItem(4053, 1);
                break;
            case 4463: // explosive potion!
                player.getActionSender().sendMessage("You get an explosive potion!");
                player.getItems().addItem(4045, 1);
                break;
            case 4464: //pickaxe table
                player.getActionSender().sendMessage("You get a bronzen pickaxe for mining.");
                player.getItems().addItem(1265, 1);
                break;
            case 4900:
            case 4901:
                CastleWars.pickupFlag(player);
                break;
        }
    }
    public static void castlewarsObjects(Player player, int objectType, int obX, int obY)
    {
    	switch(objectType)
    	{
        case 4411:
        case 4415:
        case 4417:
        case 4418:
        case 4420:
        case 4469:
        case 4470:
        case 4419:
        case 4911:
        case 4912:
        case 1747:
        case 1757:
        case 4437:
        case 6281:
        case 6280:
        case 4472:
        case 4471:
        case 4406:
        case 4407:
        case 4458:
        case 4902:
        case 4903:
        case 4900:
        case 4901:
        case 4461:
        case 4463:
        case 4464:
        case 4377:
        case 4378:
        		handleObject(player, objectType, obX, obY);
        case 1568:
            if (obX == 3097 && obY == 3468)
                player.getPA().movePlayer(3097, 9868, 0);
            else
            	handleObject(player, obY, obY, obY);
            break;
    	}
    }
}