from server.util import Plugin
from server.world.locations import LocationController

# Handles all first npc click options from the Location Controller
def locControl_n_1(player, npcType):
		LocationController.sendFirstClickNpc(player, npcType);