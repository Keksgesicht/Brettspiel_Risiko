/**
 * 
 */
package game.controller;

import java.util.ArrayList;
import game.player.Player;
import game.player.PlayerStatus;
import game.resources.GameCreator;

/**
 * @author Braun
 *
 */
public abstract class GameController {

	static Player currentPlayer;
	
	/**
	 * 
	 */		
	public static void play() {
		currentPlayer = getPlayerList().get(0);
		while(true) {
			playerTurn();
			currentPlayer = currentPlayer.getNext();
		}
	}

	
	public static ArrayList<Player> getPlayerList() {
		return GameCreator.getPlayers();
	}
	
	public static Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public static void playerTurn() {
		
		if(currentPlayer.getStatus() == PlayerStatus.WAIT) {
			currentPlayer.updateStatus();
			//currentPlayer.PlaceTroops
			currentPlayer.updateStatus();
			//currentPlayer.fightStuff
			currentPlayer.updateStatus();
			//currentPlayer.endRound
			currentPlayer.updateStatus();
		}	
	}
	

	
	/*
	nützliches Zeug:
		currentPlayer.getStatus();
		currentPlayer.updateStatus();
	*/

}
