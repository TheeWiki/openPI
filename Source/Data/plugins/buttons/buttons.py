from server.util import Plugin
from server.model.minigames.pest_control import PestControlRewards
from server.world.sound import MusicTab
from server.model.content import GnomeGlider
from server.model.content import EmoteHandler

# Handles Statically called methods in java and renders the actions here
def buttons(c, actionButtonId):
		PestControlRewards.handlePestButtons(c, actionButtonId)
		MusicTab.handleClick(c, actionButtonId)
		EmoteHandler.startEmote(c, actionButtonId)
		GnomeGlider.flightButtons(c, actionButtonId)