from server.util import Plugin

# Logs the player into the world messaging system, then allows other users to Private Message the logged in user
def pmaccess(player):
	player.getPA().logIntoPM()