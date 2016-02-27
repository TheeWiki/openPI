from server.util import Plugin
from server import Constants
from server.world import Location

# Command for teleporting home since there's no home teleport button in magic book
def command_home(player, playerCommand):
		Location(player, Constants.START_LOCATION_X, Constants.START_LOCATION_Y, 0);