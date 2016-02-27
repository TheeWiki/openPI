from server.util import Plugin
from server.model.players.skills.herblore import Cleaning
from server.model.players.skills.prayer import BuryBones
from server.model.players.skills.cooking import Food

def click(c, itemId, itemSlot):
			Cleaning.handleCleaning(c, itemId, itemSlot)

# Eating food types like Sharks, etc..
def click(c, itemId, itemSlot):
		if (Food.FoodToEat.forId(itemId)):
			Food.eat(c, itemId, itemSlot)
def click(c, itemId, itemSlot):
		if (BuryBones.BonesData.forId(itemId)):
			BuryBones.boneOnGround(c, itemId, itemSlot)