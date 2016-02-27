from server.util import Plugin
from server.model.players.skills.thieving import WallSafes
from server.model.npcs.instance import InstanceController
from server.world.locations import LocationController
from server.model.minigames.castle_wars import CastleWars

# Global bank objects
def objectClick1_2213(player, objectType, obX, obY):
	player.getPA().openUpBank()
def objectClick1_14367(player, objectType, obX, obY):
	player.getPA().openUpBank()
def objectClick1_11758(player, objectType, obX, obY):
	player.getPA().openUpBank()
def objectClick1_3193(player, objectType, obX, obY):
	player.getPA().openUpBank()

# Shoot Dwaf multi cannon
def objectClick1_6(player, objectType, obX, obY):
	player.getCannon().shootCannon()

# Thieving wall safes
def objectClick1_7236(player, objectType, obX, obY):
	WallSafes.checkWallSafe(c)

# Instance system
def objectClick1_2(player, objectType, obX, obY):
	if InstanceController.isInstanceObject(objectType):
		InstanceController.executeInstance(player, objectType)

# Handles the Castle Wars objects to put the players in the selected teams
def objectClick1_4387(player, objectType, obX, obY):
	CastleWars.addToWaitRoom(player, 1);
def objectClick1_4388(player, objectType, obX, obY):
	CastleWars.addToWaitRoom(player, 2);
def objectClick1_4389(player, objectType, obX, obY):
	CastleWars.addToWaitRoom(player, 3)

# Handles Flax picking
def objectClick1_2646(player, objectType, obX, obY):
		if (Picking.isPickable(objectType)):
			Picking.pickup(player, objectType, obY, obY)