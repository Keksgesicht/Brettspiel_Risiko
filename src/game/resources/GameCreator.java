package game.resources;

import java.awt.Color;
import java.awt.Polygon;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import game.map.Continent;
import game.map.Country;
import game.player.Player;
import io.data.text.MapReader;

/**
 * @author Braun
 */
public abstract class GameCreator {
	
	static GameData live;
	private static int plyN = 0;
	
	public static List<Player> getPlayers() {
		return live.players;
	}
	
	public static Player getCurrentPlayer() {
		plyN %= getPlayers().size();
		return getPlayers().get(plyN);
	}
	
	public static Player nextPlayer() {
		do {
			plyN++;
		} while (getPlayers().get(plyN) == null);
		return getCurrentPlayer();
	}

	public static void removePlayer(Player ply) {
		for(int i=0; i < getPlayers().size(); i++) {
			if(getPlayers().get(i) == ply)
				getPlayers().set(i, null);
		}
	}

	public static boolean noEnemyPlayers() {
		return (getPlayers().stream().filter(ply -> ply != null).count() < 2);
	}

	public static List<Continent> getContinents() {
		return MapReader.getContinents();
	}
	
	public static Map<Polygon, Country> getCPMap() {
		return live.countries;
	}
	
	public static Collection<Country> getCountries() {
		return live.countries.values();
	}

	public static Set<Polygon> getPolygons() {
		return live.countries.keySet();
	}

	public static int getGoldenCavalier() {
		return live.getGoldenCavalier();
	}
	
	public static void updateGoldenCavalier() {
		live.updateGoldenCavalier();
	}
	
	public static void updateGameStatus() {
		GameData.updateStatus();
	}
	
	public static GameStatus getGameState() {
		return GameData.state;
	}
	
	public static void createNewGame(int players) {
		ArrayList<Player> playerList = new ArrayList<Player>();
		for(int i = 1; i <= players; i++) {
			Color col;
			switch(i) {
			case 1:
				col = Color.RED;
				break;
			case 2:
				col = Color.BLUE;
				break;
			case 3:
				col = Color.YELLOW;
				break;
			case 4:
				col = Color.ORANGE;
				break;
			case 5:
				col = Color.PINK;
				break;
			default:
				col = Color.GREEN;
				break;
			}
			playerList.add(new Player("Testsubjekt" + i, col, 50 - 5 * players));
		}
		createNewGame(playerList);
	}
	
	public static void createNewGame(ArrayList<Player> players) {
		try {
			live = new GameData(players, MapReader.loadMap("default"), 4);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
	}

}
