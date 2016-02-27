from server.util import Plugin

# Loads the weapon styles on login
def weaponstyles(player):
	player.getPA().handleWeaponStyle()