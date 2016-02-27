from server.util import Plugin

# Handles all first object click options from the Location Controller
def locControl_o_1(player, objectType, obX, obY):
	LocationController.sendFirstClickObject(player, objectType);