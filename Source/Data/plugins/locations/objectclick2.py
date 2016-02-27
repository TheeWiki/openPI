from server.util import Plugin

# Handles all second object click options from the Location Controller
def locControl_o_2(player, objectType, obX, obY):
	LocationController.sendSecondClickObject(player, objectType);