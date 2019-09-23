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
		Country c1 = new Country("A");
		Polygon p1 = new Polygon();
		p1.addPoint(10, 10);
		p1.addPoint(10, 100);
		p1.addPoint(100, 100);
		p1.addPoint(100, 10);
		countries.put(p1, c1);
		
		Country c2 = new Country("BB");
		Polygon p2 = new Polygon();
		p2.addPoint(100, 10);
		p2.addPoint(100, 100);
		p2.addPoint(200, 100);
		p2.addPoint(200, 10);
		countries.put(p1, c1);
		
		Country c3 = new Country("CCC");
		Polygon p3 = new Polygon();
		p3.addPoint(10, 100);
		p3.addPoint(10, 200);
		p3.addPoint(200, 200);
		p3.addPoint(200, 100);
		countries.put(p1, c1);
		
		borders.add(new CountryBorder(c1,c2));
		borders.add(new CountryBorder(c1,c3));
		borders.add(new CountryBorder(c2,c3));
		
		ArrayList<Country> coties = new ArrayList<Country>();
		coties.add(c1);
		coties.add(c2);
		coties.add(c3);
		continents.add(new Continent(coties, 42));
	}

}
