package server.util;

import java.util.logging.Logger;

public class ItemDef {

	private static final Logger logger = Logger.getLogger(ItemDef.class
			.getName());

	public static final ItemDef forId(int i) {
		try {
			for (int j = 0; j < 10; j++)
				if (cache[j].id == i)
					return cache[j];
			cacheIndex = (cacheIndex + 1) % 10;
			ItemDef itemDef = cache[cacheIndex] = new ItemDef();
			if (i < streamIndices.length)
				itemData.currentOffset = streamIndices[i];
			itemDef.id = i;
			if (i < totalItems)
				itemDef.readValues(itemData);
			if (i == 13205) {
				itemDef.name = "Abyssal dagger";
				itemDef.groundActions = new String[] { null, null, "Take",
						null, null };
				itemDef.actions = new String[] { null, "Wield", "Check",
						"Uncharge", "Drop" };
				itemDef.description = "Something sharp from the body of a defeated Abyssal Sire.";
			}
			switch (i) {
			case 13211:
				itemDef.name = "Pegasian boots";
				itemDef.groundActions = new String[] { null, null, "Take",
						null, null };
				itemDef.actions = new String[] { null, "Wield", "Check",
						"Uncharge", "Drop" };
				break;

			case 13213:
				itemDef.name = "Primordial Boots";
				itemDef.groundActions = new String[] { null, null, "Take",
						null, null };
				itemDef.actions = new String[] { null, "Wield", "Check",
						"Uncharge", "Drop" };
				break;

			case 13215:
				itemDef.name = "Eternal boots";
				itemDef.groundActions = new String[] { null, null, "Take",
						null, null };
				itemDef.actions = new String[] { null, "Wield", "Check",
						"Uncharge", "Drop" };
				break;
			}
			return itemDef;
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isStackable(int id) {
		if (forId(id) == null)
			return false;
		return forId(id).stackable;
	}

	public String[] getItemAction() {
		if (actions == null)
			return null;
		return this.actions;
	}

	public static void unpackConfig() {
		try {
			itemData = new Buffer(
					FileOperations.readFile("./Data/cache/obj.dat"));
			Buffer indexStream = new Buffer(
					FileOperations.readFile("./Data/cache/obj.idx"));
			totalItems = indexStream.readUnsignedWord() + 21;
			streamIndices = new int[totalItems];
			int i = 2;
			for (int j = 0; j < totalItems - 21; j++) {
				streamIndices[j] = i;
				i += indexStream.readUnsignedWord();
			}
			cache = new ItemDef[20];
			for (int k = 0; k < 20; k++)
				cache[k] = new ItemDef();
			logger.info(totalItems + " item definitions loadded...");
			logger.info(forId(544).name);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getName() {
		if (name != null)
			return name;
		else
			return "";
	}

	@SuppressWarnings("unused")
	public void readValues(Buffer stream) {
		do {
			int i = stream.readUnsignedByte();
			if (i == 0)
				return;
			int anInt188;
			int anInt164;
			int modelRotationX;
			int modelRotationY;
			int modelZoom;
			int modelID;
			int anInt185;
			int anInt162;
			int anInt175;
			int anInt197;
			int anInt166;
			int anInt173;
			int anInt204;
			int certID;
			int certTemplateID;
			int anInt167;
			int anInt192;
			int anInt191;
			byte anInt196;
			int anInt184;
			int team;
			boolean membersObject;
			if (i == 1)
				modelID = stream.readUnsignedWord();
			else if (i == 2)
				name = stream.readString();
			else if (i == 3)
				description = stream.readString();
			else if (i == 4)
				modelZoom = stream.readUnsignedWord();
			else if (i == 5)
				modelRotationY = stream.readUnsignedWord();
			else if (i == 6)
				modelRotationX = stream.readUnsignedWord();
			else if (i == 7) {
				int modelOffset1 = stream.readUnsignedWord();
				if (modelOffset1 > 32767)
					modelOffset1 -= 0x10000;
			} else if (i == 8) {
				int modelOffset2 = stream.readUnsignedWord();
				if (modelOffset2 > 32767)
					modelOffset2 -= 0x10000;
			} else if (i == 10)
				stream.readUnsignedWord();
			else if (i == 11)
				stackable = true;
			else if (i == 12)
				price = stream.readDWord();
			else if (i == 16)
				membersObject = true;
			else if (i == 23) {
				int anInt165 = stream.readUnsignedWord();
				byte aByte205 = stream.readSignedByte();
			} else if (i == 24)
				anInt188 = stream.readUnsignedWord();
			else if (i == 25) {
				int anInt200 = stream.readUnsignedWord();
				byte aByte154 = stream.readSignedByte();
			} else if (i == 26)
				anInt164 = stream.readUnsignedWord();
			else if (i >= 30 && i < 35) {
				if (groundActions == null)
					groundActions = new String[5];
				groundActions[i - 30] = stream.readString();
				if (groundActions[i - 30].equalsIgnoreCase("hidden"))
					groundActions[i - 30] = null;
			} else if (i >= 35 && i < 40) {
				if (actions == null)
					actions = new String[5];
				actions[i - 35] = stream.readString();
			} else if (i == 40) {
				int j = stream.readUnsignedByte();
				int[] originalModelColors = new int[j];
				int[] modifiedModelColors = new int[j];
				for (int k = 0; k < j; k++) {
					originalModelColors[k] = stream.readUnsignedWord();
					modifiedModelColors[k] = stream.readUnsignedWord();
				}

			} else if (i == 78)
				anInt185 = stream.readUnsignedWord();
			else if (i == 79)
				anInt162 = stream.readUnsignedWord();
			else if (i == 90)
				anInt175 = stream.readUnsignedWord();
			else if (i == 91)
				anInt197 = stream.readUnsignedWord();
			else if (i == 92)
				anInt166 = stream.readUnsignedWord();
			else if (i == 93)
				anInt173 = stream.readUnsignedWord();
			else if (i == 95)
				anInt204 = stream.readUnsignedWord();
			else if (i == 97)
				certID = stream.readUnsignedWord();
			else if (i == 98)
				certTemplateID = stream.readUnsignedWord();
			else if (i == 100) {
				int length = stream.readUnsignedByte();
				int[] stackIDs = new int[length];
				int[] stackAmounts = new int[length];
				for (int i2 = 0; i2 < length; i2++) {
					stackIDs[i2] = stream.readUnsignedWord();
					stackAmounts[i2] = stream.readUnsignedWord();
				}
			} else if (i == 110)
				anInt167 = stream.readUnsignedWord();
			else if (i == 111)
				anInt192 = stream.readUnsignedWord();
			else if (i == 112)
				anInt191 = stream.readUnsignedWord();
			else if (i == 113)
				anInt196 = stream.readSignedByte();
			else if (i == 114)
				anInt184 = stream.readSignedByte() * 5;
			else if (i == 115)
				team = stream.readUnsignedByte();
		} while (true);
	}

	public boolean isWieldable() {
		// if (actions == null) {
		// return false;
		// }
		// if (actions[1].equals(null)) {
		// return false;
		// }
		String menu = actions[1].toLowerCase();
		return (menu.equals("wear") || menu.equals("wield") || menu
				.equals("equip"));
	}

	public int getPrice() {
		return price;
	}

	public int id;
	public int price;
	public String name = "";
	public String description = "";
	public boolean stackable;
	public String actions[];
	public String groundActions[];
	public boolean memberItem;
	public static int totalItems;
	public static int cacheIndex;
	private static Buffer itemData;
	private static int streamIndices[];
	public static ItemDef[] cache;

}
