from server.util import Plugin

def bonues(player):

# Writes the item bonuses

		player.getItems().resetBonus()
		player.getItems().getBonus()
		player.getItems().writeBonus()