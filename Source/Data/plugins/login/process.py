from server.util import Plugin
from server.model.players.skills.prayer import Draining

def process(c):

# Handles the prayer draining effects
	Draining.handlePrayerDrain(c)

# Resets the inventory interface configs
def process(c):
	if (c.updateItems):
		c.getItems().resetItems(3214)