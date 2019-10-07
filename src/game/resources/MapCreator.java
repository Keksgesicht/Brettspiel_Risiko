package game.resources;

import java.awt.Polygon;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import game.map.Continent;
import game.map.Country;
import game.map.CountryBorder;

/**
 * @author janb
 *
 */
abstract class MapCreator {
	
	private static ArrayList<Continent> continents = null;
	private static Map<Polygon, Country> countries = null;

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

	public static Map<Polygon, Country> getCountries() {
		return countries;
	}
	
	/* Map creation */
	
	private static void mapDefault() {
		
	}
	
	private static void mapTest() {
		countries = new HashMap<Polygon, Country>();
		continents = new ArrayList<Continent>();
		
		int n = 2;
		int b = 200;
		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				String s1 = "A";
				for(int k=0; k < i + n * j; k++)
					s1 += "A";
				Country c1 = new Country(s1);
				Polygon p1 = new Polygon();
				p1.addPoint(10 + b * j, 10 + b * i);
				p1.addPoint(10 + b * j, 100 + b * i);
				p1.addPoint(100 + b * j, 100 + b * i);
				p1.addPoint(100 + b * j, 10 + b * i);
				countries.put(p1, c1);
				
				String s2 = "B";
				for(int k=0; k < i + n * j; k++)
					s2 += "B";
				Country c2 = new Country(s2);
				Polygon p2 = new Polygon();
				p2.addPoint(100 + b * j, 10 + b * i);
				p2.addPoint(100 + b * j, 100 + b * i);
				p2.addPoint(200 + b * j, 100 + b * i);
				p2.addPoint(200 + b * j, 10 + b * i);
				countries.put(p2, c2);
				
				String s3 = "C";
				for(int k=0; k < i + n * j; k++)
					s3 += "C";
				Country c3 = new Country(s3);
				Polygon p3 = new Polygon();
				p3.addPoint(10 + b * j, 100 + b * i);
				p3.addPoint(10 + b * j, 200 + b * i);
				p3.addPoint(200 + b * j, 200 + b * i);
				p3.addPoint(200 + b * j, 100 + b * i);
				countries.put(p3, c3);
				
				CountryBorder.addBorder(c1, c2);
				CountryBorder.addBorder(c1, c3);
				CountryBorder.addBorder(c2, c3);
				
				ArrayList<Country> coties = new ArrayList<Country>();
				coties.add(c1);
				coties.add(c2);
				coties.add(c3);
				continents.add(new Continent(coties, 13));
			}
		}
		CountryBorder.addBorder(getCountry("B"), getCountry("AAA"));
		CountryBorder.addBorder(getCountry("C"), getCountry("AA"));
		CountryBorder.addBorder(getCountry("C"), getCountry("BB"));
		CountryBorder.addBorder(getCountry("C"), getCountry("CCC"));
		CountryBorder.addBorder(getCountry("AAAA"), getCountry("CCC"));
		CountryBorder.addBorder(getCountry("BBBB"), getCountry("CCC"));
		CountryBorder.addBorder(getCountry("BB"), getCountry("AAAA"));
		CountryBorder.addBorder(getCountry("CC"), getCountry("CCCC"));
	}

	private static Country getCountry(String countryName) {
		for (Country c : countries.values())
			if (c.name.equals(countryName))
				return c;
		return null;
	}

}
