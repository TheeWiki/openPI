from server.util import Plugin

# Handles the click item of the Dwarf multi cannon
def itemClick_6(player, itemId, itemSlot):
		player.getCannon().setUpCannon();