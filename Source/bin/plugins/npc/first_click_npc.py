from server.util import Plugin

# Plugin test
def npcClick1_1(c, npcType):
 	player.sendMessage("Python works.")
 	
# Ranging Guild npc (tutor)
def npcClick1_693(player, npcType):
 	player.getDH().sendDialogues(483, npcType);
 
# Banker npc that opens the bank
def npcClick1_494(player, npcType):
 	player.getPA().openUpBank()
 
# Gnome glider npc opens glider interface
def npcClick1_2138(player, npcType):
 		player.getPA().showInterface(802);