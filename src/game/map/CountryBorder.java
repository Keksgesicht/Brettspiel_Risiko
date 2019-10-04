package game.map;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Random;

import base.collections.IntegerComparator;
import game.resources.GameCreator;
import io.gui.frames.GameMapFrame;

public class CountryBorder {
	
	private Country left;
	private Country right;
	private static boolean again;

	/**
	 * creates a representive of a connection between the countries c1 and c2
	 * @param c1 the one Country
	 * @param c2 the other Country
	 */
	public CountryBorder(Country c1, Country c2) {
		left = c1;
		right = c2;
	}
	
	public CountryBorder(String s1, String s2) {
		for(Country c : GameCreator.getCMap().values()) {
			if(c.name.equals(s1))
				left = c;
			else if(c.name.equals(s2))
				right = c;
		}
	}
	
	/**
	 * @param o the other Border which will be compared
	 * @return whether the other Border is between the same two countries
	 */
	public boolean equals(Object o) {
		if(o instanceof CountryBorder) {
			if((
					((CountryBorder) o).left() == left()
					|| 
					((CountryBorder) o).left() == right()
				) && (
					((CountryBorder) o).right() == left() 
					|| 
					((CountryBorder) o).right() == right()
			)) return true;
		} return false;
	}
	
	/**
	 * @return the one Country
	 */
	public Country left() {
		return left;
	}
	
	/**
	 * @return the other Country
	 */
	public Country right() {
		return right;
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
