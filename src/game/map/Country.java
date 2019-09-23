package game.map;

import java.util.ArrayList;

import game.player.Player;
import game.resources.GameCreator;

public class Country {
	
	public final String name; 
	private int soldiers = 0;
	
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
	public Player player() {
		for(Player ply : GameCreator.getPlayers()) {
			if(ply.controlledCountries.contains(this))
				return ply; 
		} return null;
	}
	
	/**
	 * @return a list of countries which have a direct border with this Country and are in enemy hand
	 */
	public ArrayList<Country> getNeighboringEnemyCountries() {
		if(getSoldiers() < 2) return null;
		ArrayList<Country> countries = new ArrayList<Country>();
		for(CountryBorder bd : GameCreator.getBorders()) {
			Country bd_l = bd.left();
			Country bd_r = bd.right();
			
			if(bd_l == this && bd_r.player() != this.player())
				countries.add(bd_r);
			else if(bd_r == this && bd_l.player() != this.player())
				countries.add(bd_l);
		} return countries;
	}
	
	/**
	 * @return a list of countries which the Player, which controlls the country, can reach when crossing direct borders starting at this Country
	 */
	public ArrayList<Country> getNearFriendlyCountries() {
		ArrayList<Country> countries = new ArrayList<Country>();
		countries = getNearFriendlyCountries(countries, this);		
		return countries;
	}
	
	private ArrayList<Country> getNearFriendlyCountries(ArrayList<Country> contained, Country coty) {
		contained.add(coty);
		for(CountryBorder bd : GameCreator.getBorders()) {
			Country bd_l = bd.left();
			Country bd_r = bd.right();
			
			if(bd_l == coty && bd_r.player() == this.player() && !contained.contains(bd_r)) {
				contained = getNearFriendlyCountries(contained, bd_r);
				contained.add(bd_r);
			}
			else if(bd_r == coty && bd_l.player() == this.player() && !contained.contains(bd_l)) {
				contained = getNearFriendlyCountries(contained, bd_l);
				contained.add(bd_l);
			}
		} contained.remove(coty);
		return contained;
	}
	
}
