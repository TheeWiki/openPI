from server.util import Plugin

# Handles all third npc click options from the Location Controller
def locControl_n_3(player, npcType):
		LocationController.sendThirdClickNpc(player, npcType);