from server.util import Plugin
from server.model.players import EquipmentListener

def login(c):

# Sets the Equipment tab & Special bar functionality

		c.getItems().sendWeapon(c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()], c.getItems().getItemName(c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()]))
		c.getItems().setEquipment(c.playerEquipment[EquipmentListener.HAT_SLOT.getSlot()], 1, EquipmentListener.HAT_SLOT.getSlot())
		c.getItems().setEquipment(c.playerEquipment[EquipmentListener.CAPE_SLOT.getSlot()], 1, EquipmentListener.CAPE_SLOT.getSlot())
		c.getItems().setEquipment(c.playerEquipment[EquipmentListener.AMULET_SLOT.getSlot()], 1, EquipmentListener.AMULET_SLOT.getSlot())
		c.getItems().setEquipment(c.playerEquipment[EquipmentListener.ARROWS_SLOT.getSlot()], c.playerEquipmentN[EquipmentListener.ARROWS_SLOT.getSlot()], EquipmentListener.ARROWS_SLOT.getSlot())
		c.getItems().setEquipment(c.playerEquipment[EquipmentListener.CHEST_SLOT.getSlot()], 1, EquipmentListener.CHEST_SLOT.getSlot())
		c.getItems().setEquipment(c.playerEquipment[EquipmentListener.SHIELD_SLOT.getSlot()], 1, EquipmentListener.SHIELD_SLOT.getSlot())
		c.getItems().setEquipment(c.playerEquipment[EquipmentListener.LEGS_SLOT.getSlot()], 1, EquipmentListener.LEGS_SLOT.getSlot())
		c.getItems().setEquipment(c.playerEquipment[EquipmentListener.GLOVES_SLOT.getSlot()], 1, EquipmentListener.GLOVES_SLOT.getSlot())
		c.getItems().setEquipment(c.playerEquipment[EquipmentListener.BOOTS_SLOT.getSlot()], 1, EquipmentListener.BOOTS_SLOT.getSlot())
		c.getItems().setEquipment(c.playerEquipment[EquipmentListener.RING_SLOT.getSlot()], 1, EquipmentListener.RING_SLOT.getSlot())
		c.getItems().setEquipment(c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()], c.playerEquipmentN[EquipmentListener.WEAPON_SLOT.getSlot()], EquipmentListener.WEAPON_SLOT.getSlot())
		c.getItems().sendWeapon(c.getEquipment()[EquipmentListener.WEAPON_SLOT.getSlot()], c.getItems().getItemName(c.getEquipment()[EquipmentListener.WEAPON_SLOT.getSlot()]))
		c.getItems().addSpecialBar(c.playerEquipment[EquipmentListener.WEAPON_SLOT.getSlot()])