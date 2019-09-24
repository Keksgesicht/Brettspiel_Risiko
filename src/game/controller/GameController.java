/**
 * 
 */
package game.controller;

import java.util.ArrayList;

import game.map.Country;
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
			playerINIT(true);
			currentPlayer.updateStatus();
			//currentPlayer.fightStuff
			currentPlayer.updateStatus();
			//currentPlayer.endRound
			currentPlayer.updateStatus();
		}	
	}
	
	public static int playerINIT(boolean wantsUlti) {
		return currentPlayer.addTroops(wantsUlti);
	}
	
	public static ArrayList<Country> GetControlledCountries(){
		return currentPlayer.controlledCountries;
	}
	

	
	/*
	nützliches Zeug:
		currentPlayer.getStatus();
		currentPlayer.updateStatus();
	*/

}
