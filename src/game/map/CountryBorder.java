package game.map;

import java.util.PriorityQueue;
import java.util.Random;

import base.collections.IntegerComparator;

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
	 * 
	 * @param attacker
	 * @param defender
	 * @param ff
	 */
	public static void fight(Country attacker, Country defender, boolean ff) {
		Random r = new Random(System.nanoTime());
		do {
			int a = attacker.getSoldiers() -1;
			int d = defender.getSoldiers();
			if(3 < a) a = 3;
			if(2 < d) d = 2;
			PriorityQueue<Integer> qa = new PriorityQueue<Integer>(a , new IntegerComparator().reversed());
			PriorityQueue<Integer> qd = new PriorityQueue<Integer>(d , new IntegerComparator().reversed());
			for(int i = 0; i < a; i++) {
				qa.add((int) r.nextDouble()*6+1);
			} for(int i = 0; i < d; i++) {
				qd.add((int) r.nextDouble()*6+1);
			} do {
				if(qd.remove() < qa.remove())
					defender.subSoldiers();
				else
					attacker.subSoldiers();
			} while(0 < qa.size() && 0 < qd.size());
		} while(ff && 1 < attacker.getSoldiers() && 0 < defender.getSoldiers());
	}
	

}
