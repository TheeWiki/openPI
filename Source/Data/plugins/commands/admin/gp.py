from server.util import Plugin

# Gives the player max cash
def admin_command_gp(player, playerCommand):
	player.getItems().addItem(995, 2147000000)