from server.util import Plugin

# Resets the followers of any entity (Player, NPC)
def resetfollowers(player):
	player.getPA().resetFollow()