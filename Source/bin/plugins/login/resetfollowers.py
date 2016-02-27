from server.util import Plugin

# Resets the followers of any entity (Player, NPC)
def resetfollowers(c):
	c.getPA().resetFollow()