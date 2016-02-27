from server.util import Plugin

# Handles Login frame text (with membership notice)
def logintext(player):
	player.getPA().handleLoginText()