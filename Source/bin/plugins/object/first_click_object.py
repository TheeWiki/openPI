from server.util import Plugin
from server.model.players.skills.thieving import WallSafes
from server.model.npcs.instance import InstanceController
from server.world.locations import LocationController
from server.model.minigames.castle_wars import CastleWars

# Global bank objects
def objectClick1_2213(c, objectType, obX, obY):
	c.getPA().openUpBank()
def objectClick1_14367(c, objectType, obX, obY):
	c.getPA().openUpBank()
def objectClick1_11758(c, objectType, obX, obY):
	c.getPA().openUpBank()
def objectClick1_3193(c, objectType, obX, obY):
	c.getPA().openUpBank()

# Shoot Dwaf multi cannon
def objectClick1_6(c, objectType, obX, obY):
	c.getCannon().shootCannon()

# Thieving wall safes
def objectClick1_7236(c, objectType, obX, obY):
	WallSafes.checkWallSafe(c)

# Instance system
def objectClick1_2(c, objectType, obX, obY):
	if InstanceController.isInstanceObject(objectType):
		InstanceController.executeInstance(c, objectType)

# Handles the Castle Wars objects to put the players in the selected teams
def objectClick1_4387(c, objectType, obX, obY):
	CastleWars.addToWaitRoom(c, 1);
def objectClick1_4388(c, objectType, obX, obY):
	CastleWars.addToWaitRoom(c, 2);
def objectClick1_4389(c, objectType, obX, obY):
	CastleWars.addToWaitRoom(c, 3)

# Handles Flax picking
def objectClick1_2646(c, objectType, obX, obY):
		if (Picking.isPickable(objectType)):
			Picking.pickup(c, objectType, obY, obY)