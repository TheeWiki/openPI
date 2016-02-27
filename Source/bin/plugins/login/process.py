from server.util import Plugin
from server.model.players.skills.prayer import Draining

def process(player):

# Handles the prayer draining effects
	Draining.handlePrayerDrain(player)

# Resets the inventory interface configs
def process(player):
	if (player.updateItems):
		player.getItems().resetItems(3214)