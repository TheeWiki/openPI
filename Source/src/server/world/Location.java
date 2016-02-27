package server.world;

import server.Constants;
import server.event.CycleEvent;
import server.event.CycleEventContainer;
import server.event.CycleEventHandler;
import server.model.players.Player;

public class Location {
	
	@SuppressWarnings("unused")
	private int NORMAL = 0, ANCIENT = 1, LUNAR = 2;
	
	/**
	 * Handles the teleport
	 * @param player player teleporting
	 * @param x x coordinate
	 * @param y y coordindate
	 * @param height heightlevel
	 */
	public Location(Player player, int x, int y, int height) {
		this.x = x;
		this.y = y;
		this.height = height;
		handleTeleport(player);
	}
	
	private int x, y, height;
	
	/*
	 * Gets the X coordinate
	 */
	public int getX() {
		return x;
	}
	
	/*
	 * gets the y coordinate
	 */
	public	int getY() {
		return y;
	}
	
	/*
	 * gets the heightLevel
	 */
	public	int getHeight() {
		return height;
	}
	
	/**
	 * Handles the teleport
	 * @param player player teleporting
	 */
	public void handleTeleport(final Player player) {
		if (!canTeleport(player))
			return;
		player.teleporting = true;
		player.startAnimation(getTeleportAnimation(player));
		player.gfx0(getTeleportGfx(player));
		CycleEventHandler.getSingleton().addEvent(player, new CycleEvent() {
			int timer = player.playerMagicBook == 1 ? 9 : 6;
			@Override
			public void execute(CycleEventContainer container) {
				timer--;
				if (timer == 3) {
					player.resetWalkingQueue();
					player.teleportToX = getX();
			        player.teleportToY = getY();
					player.heightLevel = getHeight();
					player.getPA().requestUpdates();
					if (player.playerMagicBook == 1)
						player.gfx0(getTeleportEndGfx(player));
					player.startAnimation(getTeleportEndAnimation(player));
				}
				
				if (timer == 0)
					container.stop();
			}
			@Override
			public void stop() {
				player.getPA().resetShaking();
				player.teleporting = false;
			}
		}, 1);
	}
	
	/**
	 * Gets the teleport gfx
	 * @param player player teleporting
	 * @return teleport gfx
	 */
	private int getTeleportGfx(Player player) {
		return player.playerMagicBook == 0 ? 308 : player.playerMagicBook == 1 ? 0 : 1685; 
	}
	
	/**
	 * Gets the teleport animation
	 * @param player player teleporting
	 * @return teleport animation
	 */
	private int getTeleportAnimation(Player player) {
		return player.playerMagicBook == 0 ? 714 : player.playerMagicBook == 1 ? 1979 : 9606; 
	}
	
	/**
	 * Gets the teleport end animation
	 * @param player player teleporting
	 * @return end animation for teleporting
	 */
	private int getTeleportEndAnimation(Player player) {
		return player.playerMagicBook == 0 ? 715 : 0; 
	}
	
	/**
	 * Gets the teleport end animation
	 * @param player player teleporting
	 * @return end animation for teleporting
	 */
	private int getTeleportEndGfx(Player player) {
		return /*player.playerMagicBook == 1 ? 8941 : player.playerMagicBook == 1 ? */1681/* : 9013*/; 
	}
	
	/**
	 * Checks if a player can teleport
	 * @param player player teleporting
	 * @return can teleport or not
	 */
	private boolean canTeleport(Player player) {
		if (player.inWild() && player.wildLevel > Constants.NO_TELEPORT_WILD_LEVEL) {
			player.getActionSender().sendMessage("You can't teleport above level "+ Constants.NO_TELEPORT_WILD_LEVEL + " in the wilderness.");
			return false;
		}
		if (System.currentTimeMillis() - player.teleBlockDelay < player.teleBlockLength) {
			player.getActionSender().sendMessage("You are teleblocked and cannot teleport.");
			return false;
		}
		if (player.teleporting)
			return false;
		player.stopMovement();
		player.getPA().sendFrame107();
		player.getPA().sendFrame99(0);
		player.getPA().removeAllWindows();
		player.npcIndex = 0;
		player.playerIndex = 0;
		player.dialogueAction = -1;
		player.faceUpdate(0);
		return true;
	}
}