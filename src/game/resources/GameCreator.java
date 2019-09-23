package game.resources;

import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Map;

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
	
	public static Map<Polygon, Country> getCMap() {
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
	
	public static int getGoldenCavalier() {
		return live.getGoldenCavalier();
	}
	
	public static void updateGoldenCavalier() {
		live.updateGoldenCavalier();
	}
	
	public static GameStatus getGameState() {
		return GameData.state;
	}
	
	public static void createNewGame(int players, MapList map) {
		ArrayList<Player> playerList = new ArrayList<Player>();
		for(int i = 1; i <= players; i++) {
			playerList.add(new Player("Testsubjekt" +  i, Color.GREEN));
		}
		createNewGame(playerList, map);
	}
	
	public static void createNewGame(ArrayList<Player> players, MapList map) {
		MapCreator.createMap(map);
		live = new GameData(players, 
				MapCreator.getContinents(), 
				MapCreator.getCountries(), 
				MapCreator.getBorders(), 4);
	}

}
