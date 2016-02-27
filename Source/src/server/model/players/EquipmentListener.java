package server.model.players;

import java.util.HashMap;

/**
 * The {@link #EquipmentListener(int)} enumeration represents the slot values
 * for when Equipping or Removing items from the {@link #slotId}. The
 * {@link #slotId} values are numerically accurate, but correct for viewing the
 * models when equipping the items, etc..
 * 
 * @author Dennis
 *
 */
public enum EquipmentListener 
{
	
	HAT_SLOT(0),
	CAPE_SLOT(1),
	AMULET_SLOT(2),
	WEAPON_SLOT(3),
	CHEST_SLOT(4),
	SHIELD_SLOT(5),
	LEGS_SLOT(7),
	GLOVES_SLOT(9),
	BOOTS_SLOT(10),
	RING_SLOT(12),
	ARROWS_SLOT(13);
	
	/**
	 * The variable known as {@link #slotId} holds the core values of
	 * {@link #EquipmentListener(int)} and contains the specific values needed
	 * for equipping the items in the correct slots.
	 */
	private int slotId;

	/**
	 * Gets the variable {@link #slotId} and returns its core value as
	 * {@link #getSlot()}.
	 * 
	 * @return slotId
	 */
	public int getSlot() {
		return slotId;
	}

	/**
	 * Constructs the {@link #EquipmentListener(int)} enumeration and sets the
	 * appropriate values to be used outside of the
	 * {@link #EquipmentListener(int)} enumeration.
	 * 
	 * @param slotId
	 */
	private EquipmentListener(int slotId) {
		this.slotId = slotId;
	}

	/**
	 * {@value #equipmentMap} is used to contain all of the data collected from
	 * {@link #getSlot()} and stores inside of the of the HashMap.
	 */
	private static HashMap<Integer, EquipmentListener> equipmentMap = new HashMap<Integer, EquipmentListener>();

	/**
	 * Loops through the values of the EquipmentListener enumeration and puts
	 * them inside of the equipmentMap.
	 */
	static {
		for (EquipmentListener el : values()) {
			equipmentMap.put(el.getSlot(), el);
		}
	}
}