package server.model.items;


/**
 * Items In The Game
 * @author Sanity
 * Revised by Shawn
 */
public class GameItem {
	public int id, amount;
	public boolean stackable = false;

	public GameItem(int id, int amount) {
		if (Item.itemStackable[id]) {
			stackable = true;
		}
	this.id = id;
	this.amount = amount;
	}
}