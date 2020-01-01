package io.data.text;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Polygon;
import java.io.IOException;
import java.io.InputStream;
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
import game.resources.GameCreator;
import io.gui.GUImanager;
import io.gui.frames.GameMapFrame;

/**
 * @author Braun
 */
public abstract class MapReader {

	private static List<Continent> continents;
	private static Map<String, InputStream> mapFiles = new HashMap<String, InputStream>();
	private static Map<Polygon, Point> stringPoints = new HashMap<Polygon, Point>();
	private static List<ArrayList<Point>> borderLines = new ArrayList<ArrayList<Point>>();
	
	static {
		ClassLoader cl = MapReader.class.getClassLoader();
		try {
			mapFiles.put("default", cl.getResourceAsStream("maps/default.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static List<Continent> getContinents() {
		return continents;
	}

	public static Map<Polygon, Point> getStringPoints() {
		return stringPoints;
	}

	public static List<ArrayList<Point>> getBorderLines() {
		return borderLines;
	}

	protected static Map<Polygon, Country> parseMap(InputStream mapXML)
			throws ParserConfigurationException, SAXException, IOException {
		Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(mapXML);
		Element root = doc.getDocumentElement();
		root.normalize();
		GUImanager.SCALE = Double.parseDouble(root.getAttribute("scale"));
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
				Element polyE = (Element) cotyE.getElementsByTagName("Polygon").item(0);
				NodeList points = polyE.getElementsByTagName("Point");
				Polygon poly = new Polygon();
				for (int k = 0; k < points.getLength(); k++) {
					Element p = (Element) points.item(k);
					poly.addPoint((int) (Integer.valueOf(p.getAttribute("x")) * GUImanager.SCALE),
							(int) (Integer.valueOf(p.getAttribute("y")) * GUImanager.SCALE));
				}
				Element pointN = (Element) cotyE.getElementsByTagName("Point").item(0);
				if (!pointN.getParentNode().getNodeName().equals("Polygon")) {
					stringPoints.put(poly,
							new Point((int) (Integer.valueOf(pointN.getAttribute("x")) * GUImanager.SCALE),
									(int) (Integer.valueOf(pointN.getAttribute("y")) * GUImanager.SCALE)));
				}
				contCoties.add(cotyC);
				poco.put(poly, cotyC);

			}
			alc.add(new Continent(contCoties, Integer.valueOf(cont.getAttribute("soldiers"))));
			
		}
		NodeList borders = doc.getElementsByTagName("Border");
		for (int i = 0; i < borders.getLength(); i++) {
			Element border = (Element) borders.item(i);
			NodeList points = border.getElementsByTagName("Point");
			ArrayList<Point> line = new ArrayList<Point>();
			for (int j = 0; j < points.getLength(); j++) {
				Element p = (Element) points.item(j);
				line.add(new Point((int) (Integer.valueOf(p.getAttribute("x")) * GUImanager.SCALE),
						(int) (Integer.valueOf(p.getAttribute("y")) * GUImanager.SCALE)));
			}
			borderLines.add(line);
		}
		MapReader.continents = alc;
		return poco;
	}
	
	public static Map<Polygon, Country> loadMap(String mapName) throws ParserConfigurationException, SAXException, IOException {
		Map<Polygon, Country> poco = parseMap(mapFiles.get(mapName));
		detectBorders(poco);
		return poco;
	}

	private static void detectBorders(Map<Polygon, Country> poco) {
		new Thread() {
			
			@Override
			public void run() {
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
				for (int i = 0; i < borderLines.size(); i++) {
					ArrayList<Point> line = borderLines.get(i);
					Country coty1 = getCotyWithPoint(getPolyWithPoint(line.get(0)));
					Country coty2 = getCotyWithPoint(getPolyWithPoint(line.get(line.size() - 1)));
					CountryBorder.addBorder(coty1, coty2);
				}
				maxX += 10;
				maxY += 10;
				GUImanager.mapSpace = new Dimension(maxX, maxY);
				while (!(GUImanager.getFrame() instanceof GameMapFrame)) {
					try {
						sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				((GameMapFrame) GUImanager.getFrame()).updateSize(maxX, maxY);
			}
			
		}.start();
	}

	public static Polygon getPolyWithPoint(Point p) {
		for (Polygon poly : GameCreator.getPolygons()) {
			if (poly.contains(p)) {
				return poly;
			}
		}
		return null;
	}

	public static Country getCotyWithPoint(Polygon p) {
		return GameCreator.getCPMap().get(p);
	}

}
