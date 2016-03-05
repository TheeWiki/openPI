from server.util import Plugin

# Gets the player X and Y position
def admin_command_mypos(player, playerCommand):
	player.getActionSender().sendMessage("X: " + player.absX);
	player.getActionSender().sendMessage("Y: " + player.absY);