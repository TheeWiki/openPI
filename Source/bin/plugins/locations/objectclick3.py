from server.util import Plugin

# Handles all third object click options from the Location Controller
def locControl_o_3(c, objectType, obX, obY):
	LocationController.sendThirdClickObject(c, objectType);