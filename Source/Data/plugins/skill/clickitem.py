from server.util import Plugin
from server.model.players.skills.herblore import Cleaning
from server.model.players.skills.cooking import Food

def click(player, itemId, itemSlot):
			Cleaning.handleCleaning(player, itemId, itemSlot)

# Eating food types like Sharks, etc..
def click(player, itemId, itemSlot):
		if (Food.FoodToEat.forId(itemId)):
			Food.eat(player, itemId, itemSlot)