package game.resources;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Map;

import base.collections.CircularArrayList;
import game.map.Country;
import game.player.Player;

class GameData {
	
	static GameStatus state = GameStatus.INIT;
	final CircularArrayList<Player> players = new CircularArrayList<Player>();
	final Map<Polygon, Country> countries;
	private int goldenCavalier;
	
	/**
	 * 
	 * @param players
	 * @param continents
	 * @param countries
	 * @param borders
	 * @param goldenCavelier
	 */
	GameData(ArrayList<Player> players, Map<Polygon, Country> countries, int goldenCavelier) {
		this.players.addAll(players);
		this.countries = countries;
		this.goldenCavalier = goldenCavelier;
	}
	
	/**
	 * @return the updated Status in which Programm currently is
	 */
	static void updateStatus() {
		switch(state) {
		case INIT:
			state = GameStatus.START; break;
		case START:
			state = GameStatus.PLAY; break;
		case PLAY:
		case END:
			state = GameStatus.END; break;
		}
	}
	
	/**
	 * raises the Strength of the golden Cavalier
	 * @return the current Strength of the golden Cavalier
	 */
	void updateGoldenCavalier() {
		if(goldenCavalier < 10) {
			goldenCavalier += 2;
		} else if(goldenCavalier < 60) {
			goldenCavalier += 5;
		}
	}	

	int getGoldenCavalier() {
		return goldenCavalier;
	}
	
	protected GameData clone() {
		// TODO
		return null;
	}
	
}
