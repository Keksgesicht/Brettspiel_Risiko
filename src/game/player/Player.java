package game.player;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;

import game.map.Country;
import game.resources.GameCreator;

public class Player implements Cloneable {

	private enum AreaCard {
		TANK, CAVALIER, SOLDIER
	}

	public final ArrayList<Country> controlledCountries;
	public final ArrayList<AreaCard> cards;
	public final String name;
	public final Color color;
	private PlayerStatus state;
	private boolean won1Fight;

	/**
	 * 
	 * @param name how the Player should be called
	 * @param col  the Color in which his countries should be drawn
	 */
	public Player(String name, Color col) {
		controlledCountries = new ArrayList<Country>();
		cards = new ArrayList<AreaCard>();
		state = PlayerStatus.WAIT;
		won1Fight = false;
		this.name = name;
		color = col;
	}

	private Player(String name, Color col, ArrayList<Country> controlled, ArrayList<AreaCard> cards) {
		controlledCountries = controlled;
		this.cards = cards;
		state = PlayerStatus.WAIT;
		won1Fight = false;
		this.name = name;
		color = col;
	}

	/**
	 * @return the state of his turn
	 */
	public PlayerStatus getStatus() {
		return state;
	}

	/**
	 * updates the state of his turn before returning
	 */
	public void updateStatus() {
		switch (state) {
		case INIT:
			state = PlayerStatus.FIGHT;
			won1Fight = false;
			break;
		case FIGHT:
			state = PlayerStatus.END;
			break;
		case END:
			state = PlayerStatus.WAIT;
			break;
		case WAIT:
			state = PlayerStatus.INIT;
			break;
		}
	}

	/**
	 * this method should be called after this Player won a fight gives the player
	 * only one AreaCard in one turn
	 */
	public void fightWon() {
		if (won1Fight)
			return;
		won1Fight = true;
		AreaCard newCard = AreaCard.TANK;
		Random r = new Random(System.nanoTime());
		switch ((int) r.nextDouble() * 3) {
		case 0:
			newCard = AreaCard.SOLDIER;
			break;
		case 1:
			newCard = AreaCard.CAVALIER;
			break;
		case 2:
			newCard = AreaCard.TANK;
			break;
		}
		cards.add(newCard);
	}

	/**
	 * @return if this Player has not the right cards on his hand ==> 0
	 *         <p>
	 *         if this Player has the right cards on his hand ==> 1
	 *         <p>
	 *         if this Player has to use the cards on his hand ==> 2
	 */
	public int ultiReady() {
		if (cards.size() < 3)
			return 0;
		if (cards.size() >= 5)
			return 2;
		byte t, c, s;
		t = c = s = 0;
		for (AreaCard ac : cards) {
			switch (ac) {
			case TANK:
				t++;
				break;
			case CAVALIER:
				c++;
				break;
			case SOLDIER:
				s++;
				break;
			}
		}
		if (t == 3 || c == 3 || s == 3)
			return 1;
		if (t != 0 && c != 0 && s != 0)
			return 1;
		return 0;
	}

	/**
	 * removes the right cards from his hand
	 */
	public int useUlti() {
		if (cards.size() == 3) {
			cards.clear();
		} else {
			ArrayList<AreaCard> t, c, s;
			t = c = s = new ArrayList<AreaCard>();
			for (AreaCard ac : cards) {
				cards.remove(ac);
				switch (ac) {
				case TANK:
					t.add(ac);
					break;
				case CAVALIER:
					c.add(ac);
					break;
				case SOLDIER:
					s.add(ac);
					break;
				}
			}
			if (t.size() == 0 || c.size() == 0 || s.size() == 0) {
				if (t.size() != 3)
					cards.addAll(t);
				if (c.size() != 3)
					cards.addAll(c);
				if (s.size() != 3)
					cards.addAll(s);
			} else {
				t.remove(0);
				c.remove(0);
				s.remove(0);
				cards.addAll(t);
				cards.addAll(c);
				cards.addAll(s);
			}
		}
		int gc = GameCreator.getGoldenCavalier();
		GameCreator.updateGoldenCavalier();
		return gc;
	}

	/**
	 * 
	 * @return The number of troops to be deployed
	 */
	public int addTroops() {
		int troops = 0;
		if (controlledCountries.size() < 12) {
			troops += 3;
		} else {
			troops = controlledCountries.size() / 4;
		}
		if (ultiReady() == 2) {
			troops += useUlti();
		}
		return troops;
	}

	/**
	 * @return the Player who has the next turn after this Player
	 */
	public Player getNext() {
		ArrayList<Player> please = GameCreator.getPlayers();
		int index = please.indexOf(this);
		return please.get(index + 1);
	}

	@Override
	public Player clone() {
		Player ply = new Player(this.name, this.color, this.controlledCountries, this.cards);
		ply.state = this.state;
		ply.won1Fight = this.won1Fight;
		return ply;
	}

}
