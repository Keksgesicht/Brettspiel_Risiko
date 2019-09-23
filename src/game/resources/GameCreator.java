package game.resources;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;

import game.map.Continent;
import game.map.Country;
import game.map.CountryBorder;
import game.player.Player;

/**
 * @author Jan Braun
 *
 */
public abstract class GameCreator {
	
	static GameData live;
	
	public static ArrayList<Player> getPlayers() {
		return live.players;
	}
	
	public static ArrayList<Continent> getContinents() {
		return live.continents;
	}
	
	public static HashMap<Polygon, Country> getCMap() {
		return live.countries;
	}
	
	public static ArrayList<Country> getCountries() {
		ArrayList<Country> alc = new ArrayList<Country>();
		for(Country c : live.countries.values()) {
			alc.add(c);
		} return alc; 
	}
	
	public static ArrayList<Polygon> getPolygons() {
		ArrayList<Polygon> alp = new ArrayList<Polygon>();
		for(Polygon c : live.countries.keySet()) {
			alp.add(c);
		} return alp; 
	}
	
	public static ArrayList<CountryBorder> getBorders() {
		return live.borders;
	}
	
	public static int goldenCavalier() {
		return live.goldenCavalier();
	}
	
	public static GameStatus getGameState() {
		return GameData.state;
	}
	
	public static void createNewGame(int players, MapList map) {
		
		
	}
	
	public static void createNewGame(ArrayList<Player> players, MapList map) {
		MapCreator.createMap(MapList.DEFAULT);
		live = new GameData(players, 
				MapCreator.getContinents(), 
				MapCreator.getCountries(), 
				MapCreator.getBorders(), 4);
	}

}
