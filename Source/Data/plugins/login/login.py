from server.util import Plugin
from server.model.players import EquipmentListener

def login(player):

# Sets the Equipment tab & Special bar functionality

		player.getItems().sendWeapon(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()], player.getItems().getItemName(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]))
		player.getItems().setEquipment(player.playerEquipment[EquipmentListener.HAT_SLOT.getSlot()], 1, EquipmentListener.HAT_SLOT.getSlot())
		player.getItems().setEquipment(player.playerEquipment[EquipmentListener.CAPE_SLOT.getSlot()], 1, EquipmentListener.CAPE_SLOT.getSlot())
		player.getItems().setEquipment(player.playerEquipment[EquipmentListener.AMULET_SLOT.getSlot()], 1, EquipmentListener.AMULET_SLOT.getSlot())
		player.getItems().setEquipment(player.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()], player.playerEquipmentN[EquipmentListener.ARROWS_SLOT.getSlot()], EquipmentListener.ARROWS_SLOT.getSlot())
		player.getItems().setEquipment(player.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()], 1, EquipmentListener.CHEST_SLOT.getSlot())
		player.getItems().setEquipment(player.playerEquipment[EquipmentListener.SHIELD_SLOT.getSlot()], 1, EquipmentListener.SHIELD_SLOT.getSlot())
		player.getItems().setEquipment(player.playerEquipment[EquipmentListener.LEGS_SLOT.getSlot()], 1, EquipmentListener.LEGS_SLOT.getSlot())
		player.getItems().setEquipment(player.playerEquipment[EquipmentListener.GLOVES_SLOT.getSlot()], 1, EquipmentListener.GLOVES_SLOT.getSlot())
		player.getItems().setEquipment(player.playerEquipment[EquipmentListener.BOOTS_SLOT.getSlot()], 1, EquipmentListener.BOOTS_SLOT.getSlot())
		player.getItems().setEquipment(player.playerEquipment[EquipmentListener.RING_SLOT.getSlot()], 1, EquipmentListener.RING_SLOT.getSlot())
		player.getItems().setEquipment(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()], player.playerEquipmentN[EquipmentListener.WEAPON_SLOT.getSlot()], EquipmentListener.WEAPON_SLOT.getSlot())
		player.getItems().sendWeapon(player.getEquipment()[EquipmentListener.WEAPON_SLOT.getSlot()], player.getItems().getItemName(player.getEquipment()[EquipmentListener.WEAPON_SLOT.getSlot()]))
		player.getItems().addSpecialBar(player.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()])