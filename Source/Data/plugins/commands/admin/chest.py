from server.util import Plugin
from server.model.content import DailyChest

# Daily chest looting command for administrators
def admin_command_chest(player, playerCommand):
	DailyChest.checkChest(player)