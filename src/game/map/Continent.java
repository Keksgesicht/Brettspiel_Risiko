package game.map;

import java.util.ArrayList;
import java.util.List;

import game.player.Player;

public class Continent {
	
	final List<Country> countries;
	private final int soldiers;
	
	/**
	 * @param coties all countries which are on this Continent
	 * @param sold he number of additional soldiers a Player can get at the beginning of a round
	 */
	public Continent(ArrayList<Country> coties, int sold) {
		countries = coties;
		soldiers = sold;
	}
	
	/**
	 * @param ply <br>the Player which possible controlls all countries of this Continent
	 * @return 
	 * if Player ply doesn't controll all countries of this Continent
	 * ==> 0 <p>
	 * if Player ply controlls all countries of this Continent
	 * ==> the number of additional soldiers Player ply can get at the beginning of a round
	 */
	public int isControlledBy(Player ply) {
		if(ply.containsCountries(countries))
			return soldiers;
		return 0;
	}
	
}
