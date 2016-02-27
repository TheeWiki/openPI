from server.util import Plugin

# Digging for the barrows hills to break in if in right area
def itemClick_952(c, itemId, itemSlot):
		c.startAnimation(830)
		if(c.inArea(3553, 3301, 3561, 3294)):
			c.teleTimer = 3;
			c.newLocation = 1;
		elif(c.inArea(3550, 3287, 3557, 3278)):
			c.teleTimer = 3;
			c.newLocation = 2;
		elif(c.inArea(3561, 3292, 3568, 3285)):
			c.teleTimer = 3;
			c.newLocation = 3;
		elif(c.inArea(3570, 3302, 3579, 3293)):
			c.teleTimer = 3;
			c.newLocation = 4;
		elif(c.inArea(3571, 3285, 3582, 3278)):
			c.teleTimer = 3;
			c.newLocation = 5;
		elif(c.inArea(3562, 3279, 3569, 3273)):
			c.teleTimer = 3;
			c.newLocation = 6;
		else:
			c.sendMessage("You find nothing...");