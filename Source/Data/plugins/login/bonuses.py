from server.util import Plugin

def bonues(c):

# Writes the item bonuses

		c.getItems().resetBonus()
		c.getItems().getBonus()
		c.getItems().writeBonus()