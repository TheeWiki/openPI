from server.util import Plugin

# Empties the player inventory of items
def command_empty(player, playerCommand):
		player.getItems().removeAllItems()