package game.map;

import java.util.HashSet;
import java.util.Set;

import game.player.Player;
import game.resources.GameCreator;

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
		for(CountryBorder bd : GameCreator.getBorders()) {
			Country bd_l = bd.left();
			Country bd_r = bd.right();
			
			if(bd_l == this && bd_r.king() != this.king())
				countries.add(bd_r);
			else if(bd_r == this && bd_l.king() != this.king())
				countries.add(bd_l);
		} return countries;
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
		for(CountryBorder bd : GameCreator.getBorders()) {
			Country bd_l = bd.left();
			Country bd_r = bd.right();
			
			if(bd_l == coty && bd_r.king() == this.king() && !contained.contains(bd_r)) {
				contained = getNearFriendlyCountries(contained, bd_r);
				contained.add(bd_r);
			}
			else if(bd_r == coty && bd_l.king() == this.king() && !contained.contains(bd_l)) {
				contained = getNearFriendlyCountries(contained, bd_l);
				contained.add(bd_l);
			}
		} contained.remove(coty);
		return contained;
	}

	/**
	 * @param player
	 */
	public void theKingIsDead(Player player) {
		king = player;
	}
	
}
