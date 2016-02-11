package server.model.content;

import java.util.ArrayList;
import java.util.Random;

import server.model.players.Client;

public class RandomRewards 
{
	private static ArrayList<Integer> items = new ArrayList<Integer>();
	
	public static void addItems(Client c)
	{
		for (int i = 0; i < z; i++) {
			c.getItems().addItem(items.get(i), 1);
		}
	}
	static Random r = new Random();
	
	private static int arrayItems()
	{
		items.add(4151);
		items.add(4720);
		return items.size();
	}
	
	private static int z = arrayItems();
}
