from server.util import Plugin

# Reloads the Python scripts
def admin_command_reloadscripts(c, playerCommand):
	Plugin.load();