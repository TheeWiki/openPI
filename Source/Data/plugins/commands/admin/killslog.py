from server.util import Plugin

# Opens the kill log interface
def admin_command_killslog(c, playerCommand):
	KillLog.killInterface(c)