package io.data.text;

import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import game.map.Continent;
import game.map.Country;
import game.map.CountryBorder;

/**
 * @author Braun
 */
public abstract class MapReader {
	
	public static double MAPZOOM = 1.0d;
	public static Rectangle mapSpace;

	private static Map<String, File> mapFiles;
	private static List<Continent> continents;
	
	public static List<Continent> getContinents() {
		return continents;
	}

	public static Map<String, File> searchForMaps(File... mapFolder) {
		// TODO
		return mapFiles;
	}

	protected static Object[] parseMap(File mapXML) throws ParserConfigurationException, SAXException, IOException {
		Object[] mapContent = new Object[2];
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(mapXML);
		Element root = doc.getDocumentElement();
		root.normalize();
		mapContent[0] = root.getAttribute("zoom");
		List<Continent> alc = new ArrayList<Continent>();
		Map<Polygon, Country> poco = new HashMap<Polygon, Country>();
		
		NodeList continents = doc.getElementsByTagName("Continent");
		for (int i = 0; i < continents.getLength(); i++) {

			Element cont = (Element) continents.item(i);
			ArrayList<Country> contCoties = new ArrayList<Country>();
			NodeList countries = cont.getElementsByTagName("Country");
			for (int j=0; j < countries.getLength(); j++) {
				
				Element cotyE = (Element) countries.item(j);
				Country cotyC = new Country(cotyE.getAttribute("name"));
				NodeList points = cotyE.getElementsByTagName("Point");
				Polygon poly = new Polygon();
				for (int k = 0; k < points.getLength(); k++) {
					Element p = (Element) points.item(k);
					poly.addPoint(Integer.valueOf(p.getAttribute("x")), Integer.valueOf(p.getAttribute("y")));
				}
				contCoties.add(cotyC);
				poco.put(poly, cotyC);

			}
			alc.add(new Continent(contCoties, Integer.valueOf(cont.getAttribute("soldiers"))));
			
		}
		MapReader.continents = alc;
		mapContent[1] = poco;
		return mapContent;
	}
	
	@SuppressWarnings("unchecked")
	public static Map<Polygon, Country> loadMap(String mapName) throws ParserConfigurationException, SAXException, IOException {
		Object[] mapContent = parseMap(mapFiles.get(mapName));
		MAPZOOM = Double.parseDouble((String) mapContent[0]);
		
		Map<Polygon, Country> poco = (Map<Polygon, Country>) mapContent[1];
		int maxX = 0;
		int maxY = 0;
		for (Entry<Polygon, Country> pc1 : poco.entrySet()) {
			Polygon poly1 = pc1.getKey();
			for (Entry<Polygon, Country> pc2 : poco.entrySet()) {
				Polygon poly2 = pc2.getKey();
				if (poly1 == poly2)
					continue;
				for (int i = 0; i < poly1.npoints; i++) {
					int x = poly1.xpoints[i];
					int y = poly1.ypoints[i];
					if (poly2.contains(x, y))
						CountryBorder.addBorder(pc1.getValue(), pc2.getValue());
					maxX = Math.max(maxX, x);
					maxY = Math.max(maxY, y);
				}

			}
		}
		mapSpace = new Rectangle(0, 0, maxX, maxY);
		return poco;
	}

}
