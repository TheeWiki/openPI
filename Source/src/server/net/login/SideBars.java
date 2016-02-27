package server.net.login;

import java.util.HashMap;

/**
 * Alternative for loading side bar interfaces.
 * @author Dennis
 */
public enum SideBars 
{
		ATTACK_TAB(0, 2423),
		SKILL_TAB(1, 3917),
		QUEST_TAB(2, 638),
		INVENTORY_TAB(3, 3213),
		EQUIPMENT_TAB(4, 1644),
		PRAYER_TAB(5, 5608),
		REGULAR_MAGIC_TAB(6, 1151),
		ANCIENT_MAGIC_TAB(6, 12855),
		CLAN_CHAT_TAB(7, -1),
		ADD_FRIEND_TAB(8, 5065),
		ADD_IGNORE_TAB(9, 5715),
		LOGOUT_TAB(10, 2449),
		OPTIONS_TAB(11, 904),
		RUN_TAB(12, 147),
		MUSIC_TAB(13, 962);
	
	/**
	 * Integers that declare @enum SideBar's "sidebar" & "interfaceId"
	 */
	private int sideBar, interfaceId;

	/**
	 * Getter for the sidebar Id
	 * @return sideBar
	 */
	public int getSideBar() {
		return sideBar;
	}

	/**
	 * Getter for the interface ID
	 * @return interfaceID
	 */
	public int getInterfaceId() {
		return interfaceId;
	}

	/**
	 * Constructor for the @enum SideBars
	 * @param sideBar
	 * @param interfaceId
	 */
	SideBars(int sideBar, int interfaceId) {
		this.sideBar = sideBar;
		this.interfaceId = interfaceId;
	}
	private static HashMap<Integer, SideBars> sidebarsMap = new HashMap<Integer, SideBars>();
	
	static
	{
		for (SideBars sb : values()) {
			sidebarsMap.put(sb.getSideBar(), sb);
			sidebarsMap.put(sb.getInterfaceId(), sb);
		}
	}
}