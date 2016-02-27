from server.util import Plugin

# Handles all second object click options from the Location Controller
def locControl_o_2(c, objectType, obX, obY):
	LocationController.sendSecondClickObject(c, objectType);