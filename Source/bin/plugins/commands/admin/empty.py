from server.util import Plugin
from server.model.players import Client

# Empties the player inventory of items
def command_empty(c, playerCommand):
		c.getItems().removeAllItems()