from server.util import Plugin

# Reloads the Python scripts
def admin_command_reloadscripts(player, playerCommand):
	Plugin.load();