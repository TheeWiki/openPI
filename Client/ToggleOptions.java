import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.LinkedList;

import sign.signlink;

/**
 * The {@link #ToggleOptions(boolean, String...)} enumeration gives the user to
 * maximize their preferences by disabling and enabling client features that are
 * listed in the {@link #ToggleOptions(boolean, String...)} enumeration shown
 * below. then is saved to the cache directory for safe and easy management.
 * 
 * @author Dennis
 *
 */
public enum ToggleOptions {
	
	HD_MINIMAP(true),
	SMOOTH_MINIMAP(true),
	RIGHT_CLICK(true),
	HP_ABOVE_HEADS(true),
	ROOFS_ON(true),
	NAMES_ABOVE_HEADS(true),
	HD_CHARACTERS(true),
	LOW_MEM(false);

	/**
	 * The variable known as {@link #toggled} contains the core values of the
	 * {@link #ToggleOptions(boolean, String...)} enumeration, which decides if
	 * the selected toggle is true or false.
	 */
	private boolean toggled;
	/**
	 * Conflicting check
	 */
	protected final String[] conflicting;

	/**
	 * Gets the value of {@link #toggled} and returns it as {@link #isToggled()}.
	 * @return
	 */
	public boolean isToggled() {
		return toggled;
	}

	/**
	 * This is the constructor that builds the
	 * {@link #ToggleOptions(boolean, String...)} enumeration, and sets the core
	 * values accordingly.
	 * 
	 * @param toggled
	 * @param conflicting
	 */
	private ToggleOptions(boolean toggled, String... conflicting) {
		this.toggled = toggled;
		this.conflicting = conflicting;
	}

	/**
	 * The {@link #toggleOptionsMap} puts the values of
	 * {@link #ToggleOptions(boolean, String...)} into a HashMap for simple
	 * storage.
	 */
	private static HashMap<Boolean, ToggleOptions> toggleOptionsMap = new HashMap<Boolean, ToggleOptions>();
	
	/**
	 * Loops through the enumeration values and puts them inside the HashMap
	 */
	static
	{
		for (ToggleOptions to : values()) {
			toggleOptionsMap.put(to.isToggled(), to);
		}
	}

	/**
	 * Toggles the specific {@link #ToggleOptions(boolean, String...)} values to
	 * the users preference.
	 */
	public void toggle() {
		toggled = !toggled;
		if (toggled) {
			for (String conflict : conflicting) {
				ToggleOptions option = ToggleOptions.valueOf(conflict);
				System.out.println("Toggling off " + conflict + " " + option);
				if (option.isToggled())
					option.toggle();
			}
		}
		if (this == HD_MINIMAP) {
			Client.getInstance().method22();
		}
		save();
	}

	/**
	 * Capitalizes strings when needed.
	 */
	@Override
	public String toString() {
		return Client.capitalize(this.name().toLowerCase()).replaceAll("_", " ").replaceAll("Hd ", "HD ");
	}

	/**
	 * Stores all needed button ids into {@link #button_ids} LinkedList.
	 */
	@SuppressWarnings("unused")
	private LinkedList<Integer> button_ids = new LinkedList<Integer>();

	/**
	 * Loads the saved values from {@link #ToggleOptions(boolean, String...)}
	 * and sets the values as desired from the player.
	 */
	public static void load() {
		File file = new File(signlink.findcachedir() + "Settings.scf");
		if (!file.exists()) {
			ToggleOptions.save();
			return;
		}
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new FileReader(file));
			String string = "", setting = "", value = "";
			String[] args = null;
			while ((string = reader.readLine()) != null) {
				args = string.split("=");
				setting = args[0];
				if (args.length > 1)
					value = args[1];
				else
					value = "";
				switch (setting.toLowerCase()) {
				default:
					if (!setting.equals("")) {
						try {
							ToggleOptions to = ToggleOptions.valueOf(setting);
							boolean bool = value.equals("") ? true : Boolean.parseBoolean(value);
							to.toggled = bool;
							if (to.toggled)
								for (String conflict : to.conflicting) {
									ToggleOptions toValues = ToggleOptions.valueOf(conflict);
									toValues.toggled = false;
								}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					break;
				}
			}
		} catch (Exception e) {
			System.out.println("Failed to read settings to file " + file.getAbsolutePath());
			e.printStackTrace();
		} finally {
			try {
				if (reader != null)
					reader.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Saves the {@link #ToggleOptions(boolean, String...)} values that the
	 * users specifies and creates a file in the cache directory that holds the
	 * saved data externally from the client in a safe and easy to manage place.
	 */
	public static void save() {
		File file = new File(signlink.findcachedir() + "Settings.scf");
		BufferedWriter writter = null;
		try {
			writter = new BufferedWriter(new FileWriter(file));

			for (ToggleOptions to : ToggleOptions.values()) {
				writter.write(to.name() + "=" + to.toggled);
				writter.newLine();
			}
		} catch (Exception e) {
			System.out.println("Failed to save settings to file " + file.getAbsolutePath());
			e.printStackTrace();
		} finally {
			try {
				if (writter != null)
					writter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}