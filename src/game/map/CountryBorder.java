package game.map;

import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

import base.collections.IntegerComparator;
import base.graphs.Graph;
import io.gui.frames.GameMapFrame;

public abstract class CountryBorder {
	
	private static boolean again;
	private static Graph<Boolean, Country> borders = new Graph<Boolean, Country>(false);
	private static String matrixName = "mapBorders";

	/**
	 * creates a representive of a connection between the countries c1 and c2
	 * 
	 * @param c1 the one Country
	 * @param c2 the other Country
	 */
	public static void addBorder(Country c1, Country c2) {
		borders.setWeight(matrixName, c1, c2, true);
		borders.setWeight(matrixName, c2, c1, true);
	}

	public static List<Country> getNeighboors(Country c) {
		return borders.getAdjazenzListe(matrixName, c);
	}
	
	/**
	 * @param attacker
	 * @param defender
	 * @param ff
	 * @param frame
	 * @return whether the fight can go on
	 */
	public static boolean fight(Country attacker, Country defender, boolean ff, GameMapFrame frame) {
		Random r = new Random(System.nanoTime());
		do {
			Thread t = new Thread() {

				@Override
				public void run() {
					int att = attacker.getSoldiers() - 1;
					int def = defender.getSoldiers();
					if (3 < att)
						att = 3;
					if (2 < def)
						def = 2;

					Comparator<Integer> intComp = new IntegerComparator().reversed();
					PriorityQueue<Integer> queueAtt = new PriorityQueue<Integer>(att, intComp);
					PriorityQueue<Integer> queueDef = new PriorityQueue<Integer>(def, intComp);
					for (int i = 0; i < att; i++)
						queueAtt.add((int) (r.nextDouble() * 6 + 1));
					for (int i = 0; i < def; i++)
						queueDef.add((int) (r.nextDouble() * 6 + 1));

					try {
						sleep(750);
						frame.attDices.drawDices(queueAtt.stream().sorted(intComp).toArray(Integer[]::new));
						sleep(750);
						frame.defDices.drawDices(queueDef.stream().sorted(intComp).toArray(Integer[]::new));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					do {
						if (queueDef.remove() < queueAtt.remove())
							defender.subSoldiers();
						else
							attacker.subSoldiers();
						frame.mapPanel.repaint(200);
					} while (!queueAtt.isEmpty() && !queueDef.isEmpty());
					again = (1 < attacker.getSoldiers() && 0 < defender.getSoldiers());
				}

			};
			try {
				t.start();
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		} while(ff && again);
		return again;
	}
	

}
