package server.model.items;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

/**
 * TODO: Implement system (standalone class)
 * 
 * @author Dennis
 *
 */
public class Weight {

	private static double[] weights = new double[7948];

	public static void loadWeight() {
		try {
			Scanner s = new Scanner(new File("./data/content/weight.txt"));
			while (s.hasNextLine()) {
				String[] line = s.nextLine().split(" ");
				weights[Integer.parseInt(line[0])] = Double.parseDouble(line[1]);
			}
			System.out.println("Loaded " + weights.length + " item weights");
			s.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}