package game.map;

import java.awt.Polygon;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.stream.Collectors;

import base.collections.IntegerComparator;
import base.graphs.Graph;
import io.data.text.MapReader;
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
	public static boolean fight(Polygon attackerPoly, Polygon defenderPoly, boolean ff, GameMapFrame frame) {
		Random r = new Random(System.nanoTime());
		Country attackerCoty = MapReader.getCotyWithPoint(attackerPoly);
		Country defenderCoty = MapReader.getCotyWithPoint(defenderPoly);
		do {
			Thread t = new Thread() {

				@Override
				public void run() {
					int att = attackerCoty.getSoldiers() - 1;
					int def = defenderCoty.getSoldiers();
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
						sleep(911);
						Integer[] attDicesValues = queueAtt.stream().sorted(intComp).toArray(Integer[]::new);
						frame.attDices.drawDices(attDicesValues);
						String attDiceString = Arrays.stream(attDicesValues).map(String::valueOf)
								.collect(Collectors.joining(", "));
						System.out.println("ATTACKER " + attackerCoty.name + ": " + attDiceString);

						sleep(911);
						Integer[] defDicesValues = queueDef.stream().sorted(intComp).toArray(Integer[]::new);
						frame.defDices.drawDices(defDicesValues);
						String defDiceString = Arrays.stream(defDicesValues).map(String::valueOf)
								.collect(Collectors.joining(", "));
						System.out.println("DEFENDER " + defenderCoty.name + ": " + defDiceString);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

					do {
						if (queueDef.remove() < queueAtt.remove())
							defenderCoty.subSoldiers();
						else
							attackerCoty.subSoldiers();
					} while (!queueAtt.isEmpty() && !queueDef.isEmpty());
					again = 1 < attackerCoty.getSoldiers() && 0 < defenderCoty.getSoldiers();

					frame.mapPanel.fillOnePolygon(frame.mapPanel.getGraphics(), attackerPoly, attackerCoty);
					frame.mapPanel.fillOnePolygon(frame.mapPanel.getGraphics(), defenderPoly, defenderCoty);
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
