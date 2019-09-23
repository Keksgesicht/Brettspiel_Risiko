/**
 * 
 */
package game.resources;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;

import game.map.Continent;
import game.map.Country;
import game.map.CountryBorder;

/**
 * @author janb
 *
 */
abstract class MapCreator {
	
	private static ArrayList<Continent> continents = null;
	private static HashMap<Polygon, Country> countries = null;
	private static ArrayList<CountryBorder> borders = null;

	static void createMap(MapList map) {
		switch(map) {
		case DEFAULT:
			mapDefault(); break;
		case TEST:
			mapTest(); break;
		}
	}

	public static ArrayList<Continent> getContinents() {
		return continents;
	}

	public static HashMap<Polygon, Country> getCountries() {
		return countries;
	}

	public static ArrayList<CountryBorder> getBorders() {
		return borders;
	}
	
	/* Map creation */
	
	private static void mapDefault() {
		
	}
	
	private static void mapTest() {
		
	}

}
