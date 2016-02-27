from server.util import Plugin
from server import Server

# adds the player to the Console that is shown upon server startup
def panelupdate(c):
	Server.panel.addEntity(c.playerName)