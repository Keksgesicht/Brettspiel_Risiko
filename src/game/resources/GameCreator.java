package game.resources;

import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import game.map.Continent;
import game.map.Country;
import game.player.Player;

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
		return getPlayers().get(plyN);
	}
	
	public static Player nextPlayer() {
		return getPlayers().get(++plyN);
	}
	
	public static List<Continent> getContinents() {
		return MapCreator.getContinents();
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
	
	public static void createNewGame(int players, MapList map) {
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
			playerList.add(new Player("Testsubjekt" +  i, col, 20 - 5 * players));
		}
		createNewGame(playerList, map);
	}
	
	public static void createNewGame(ArrayList<Player> players, MapList map) {
		MapCreator.createMap(map);
		live = new GameData(players, MapCreator.getCountries(), 4);
	}

}
