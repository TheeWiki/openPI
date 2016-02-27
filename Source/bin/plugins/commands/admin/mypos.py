from server.util import Plugin

# Gets the player X and Y position
def admin_command_mypos(c, playerCommand):
	c.sendMessage("X: " + c.absX);
	c.sendMessage("Y: " + c.absY);