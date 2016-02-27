from server.util import Plugin

# Opens the kill log interface
def admin_command_killslog(player, playerCommand):
	KillLog.killInterface(player)