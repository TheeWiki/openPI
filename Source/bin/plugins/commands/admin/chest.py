from server.util import Plugin
from server.model.content import DailyChest

# Daily chest looting command for administrators
def admin_command_chest(c, playerCommand):
	DailyChest.checkChest(c)