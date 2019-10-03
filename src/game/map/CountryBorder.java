package game.map;

import java.util.PriorityQueue;
import java.util.Random;

import base.collections.IntegerComparator;
import game.resources.GameCreator;
import io.gui.frames.GameMapFrame;

public class CountryBorder {
	
	private Country left;
	private Country right;

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
	 */
	public static boolean fight(Country attacker, Country defender, boolean ff, GameMapFrame frame) {
		Random r = new Random(System.nanoTime());
		boolean again;
		do {
			int a = attacker.getSoldiers() -1;
			int d = defender.getSoldiers();
			if(3 < a) a = 3;
			if(2 < d) d = 2;
			PriorityQueue<Integer> qa = new PriorityQueue<Integer>(a , new IntegerComparator().reversed());
			PriorityQueue<Integer> qd = new PriorityQueue<Integer>(d , new IntegerComparator().reversed());
			for(int i = 0; i < a; i++) {
				qa.add( (int) r.nextDouble() * 6 + 1 );
			}
			for(int i = 0; i < d; i++) {
				qd.add( (int) r.nextDouble() * 6 + 1 );
			}
			frame.updateDices(qa.toArray(new Integer[3]), qd.toArray(new Integer[3]));
			do {
				if(qd.remove() < qa.remove())
					defender.subSoldiers();
				else
					attacker.subSoldiers();
			} while(!qa.isEmpty() && !qd.isEmpty());
			again = (1 < attacker.getSoldiers() && 0 < defender.getSoldiers());
		} while(ff && again);
		return again;
	}
	

}
