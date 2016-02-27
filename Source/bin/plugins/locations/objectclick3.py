from server.util import Plugin

# Handles all third object click options from the Location Controller
def locControl_o_3(player, objectType, obX, obY):
	LocationController.sendThirdClickObject(player, objectType);