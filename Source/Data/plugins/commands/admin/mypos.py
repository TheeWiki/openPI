from server.util import Plugin

# Gets the player X and Y position
def admin_command_mypos(player, playerCommand):
	player.sendMessage("X: " + player.absX);
	player.sendMessage("Y: " + player.absY);