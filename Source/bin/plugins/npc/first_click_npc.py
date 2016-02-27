from server.util import Plugin

# Plugin test
def npcClick1_1(c, npcType):
 	c.sendMessage("Python works.")
 	
# Ranging Guild npc (tutor)
def npcClick1_693(c, npcType):
 	c.getDH().sendDialogues(483, npcType);
 
# Banker npc that opens the bank
def npcClick1_494(c, npcType):
 	c.getPA().openUpBank()
 
# Gnome glider npc opens glider interface
def npcClick1_2138(c, npcType):
 		c.getPA().showInterface(802);