from server.util import Plugin

# Loads the weapon styles on login
def weaponstyles(c):
	c.getPA().handleWeaponStyle()