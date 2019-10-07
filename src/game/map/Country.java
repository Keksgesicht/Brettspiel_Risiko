package game.map;

import java.util.HashSet;
import java.util.Set;

import game.player.Player;

public class Country {
	
	public final String name;
	private int soldiers = 0;
	private Player king;
	
	/**
	 * creates a Country by giving it a name and 2D area
	 * @param name the name of Country
	 */
	public Country(String name) {
		this.name = name;
	}
	
	/**
	 * @return the number of soldiers which defend this Country for the Player which controlls the country
	 */
	public int getSoldiers() {
		return soldiers;
	}
	
	public int addSoldiers() {
		return soldiers++;
	}
	
	public int subSoldiers() {
		soldiers--;
		if(soldiers < 0) soldiers = 0;
		return soldiers;
	}
	
	/**	
	 * @return the Player which controlls this Country
	 */
	public Player king() {
		return king;
	}
	
	/**
	 * @return a list of countries which have a direct border with this Country and are in enemy hand
	 */
	public Set<Country> getNeighboringEnemyCountries() {
		if(getSoldiers() < 2) return null;
		Set<Country> countries = new HashSet<Country>();
		for (Country neighboor : CountryBorder.getNeighboors(this))
			if (neighboor.king() != this.king())
				countries.add(neighboor);
		return countries;
	}
	
	/**
	 * @return a list of countries which the Player, which controlls the country, can reach when crossing direct borders starting at this Country
	 */
	public Set<Country> getNearFriendlyCountries() {
		Set<Country> countries = new HashSet<Country>();
		countries = getNearFriendlyCountries(countries, this);		
		return countries;
	}
	
	private Set<Country> getNearFriendlyCountries(Set<Country> contained, Country coty) {
		contained.add(coty);
		for (Country neighboor : CountryBorder.getNeighboors(coty)) {
			if (neighboor.king() == this.king() && !contained.contains(neighboor)) {
				contained = getNearFriendlyCountries(contained, neighboor);
				contained.add(neighboor);
			}
		}
		contained.remove(coty);
		return contained;
	}

	/**
	 * @param player
	 */
	public void theKingIsDead(Player player) {
		king = player;
	}
	
}
