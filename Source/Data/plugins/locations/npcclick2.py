from server.util import Plugin

# Handles all second npc click options from the Location Controller
def locControl_n_2(player, npcType):
		LocationController.sendSecondClickNpc(player, npcType);