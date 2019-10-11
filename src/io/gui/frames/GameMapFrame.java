package io.gui.frames;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import game.map.Country;
import game.player.Player;
import game.player.PlayerStatus;
import game.resources.GameCreator;
import io.gui.GUImanager;
import io.gui.components.DicePanel;
import io.gui.components.PolygonMapPanel;

@SuppressWarnings("serial")
public class GameMapFrame extends JFrame {

	public final JPanel mapPanel;
	public final DicePanel attDices;
	public final DicePanel defDices;
	public final JButton nextPlayerStatus;

	private JPanel contentPane;
	private JTextField currentPlayerTF;
	private JTextField newArmyCounter;
	private JTextField calvalierCounter;
	private JButton useUlti;
	private Player currentPlayer;
	private int newArmy;

	/**
	 * Create the frame.
	 */
	public GameMapFrame(int playerCount) {
		GameCreator.createNewGame(playerCount + 2);
		currentPlayer = GameCreator.getCurrentPlayer();
		newArmy = currentPlayer.getNewTroops();
		Font risikoFont = new Font("Courier", Font.BOLD, 32);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 950, 75);
		setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);

		currentPlayerTF = new JTextField();
		currentPlayerTF.setEditable(false);
		currentPlayerTF.setFont(risikoFont);
		currentPlayerTF.setForeground(currentPlayer.color);
		currentPlayerTF.setBackground(GUImanager.contrast(currentPlayer.color));
		currentPlayerTF.setHorizontalAlignment(JTextField.LEFT);
		currentPlayerTF.setText(currentPlayer.name);

		newArmyCounter = new JTextField();
		newArmyCounter.setEditable(false);
		newArmyCounter.setFont(risikoFont);
		newArmyCounter.setForeground(Color.BLACK);
		newArmyCounter.setHorizontalAlignment(SwingConstants.CENTER);
		newArmyCounter.setText(String.valueOf(newArmy));

		attDices = new DicePanel(3);
		defDices = new DicePanel(2);

		calvalierCounter = new JTextField(String.valueOf(GameCreator.getGoldenCavalier()));
		calvalierCounter.setEditable(false);
		calvalierCounter.setFont(risikoFont);
		calvalierCounter.setBackground(Color.BLACK);
		calvalierCounter.setForeground(Color.YELLOW);
		calvalierCounter.setHorizontalAlignment(SwingConstants.CENTER);

		Font buttonFont = new Font("Courier", Font.BOLD, 20);
		useUlti = new JButton("use Cards!");
		useUlti.setFont(buttonFont);
		useUlti.setVisible(false);
		useUlti.setBackground(Color.BLACK);
		useUlti.setForeground(Color.YELLOW);
		useUlti.setHorizontalAlignment(SwingConstants.CENTER);
		useUlti.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				if (currentPlayer.getStatus() == PlayerStatus.INIT && 0 < currentPlayer.ultiReady()) {
					newArmy += currentPlayer.useUlti();
					newArmyCounter.setText(String.valueOf(newArmy));
					calvalierCounter.setText(String.valueOf(GameCreator.getGoldenCavalier()));
					useUlti.setBackground(Color.BLUE);
					useUlti.setForeground(Color.WHITE);
					useUlti.setEnabled(false);
				}
			}

		});

		nextPlayerStatus = new JButton("Finish fighting");
		nextPlayerStatus.setFont(buttonFont);
		nextPlayerStatus.setForeground(Color.BLACK);
		nextPlayerStatus.setHorizontalAlignment(SwingConstants.CENTER);
		nextPlayerStatus.setVisible(false);
		nextPlayerStatus.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent evt) {
				switch (currentPlayer.getStatus()) {
				case FIGHT:
					currentPlayer.updateStatus();
					nextPlayerStatus.setText("Next Player");
					break;
				case END:
					currentPlayer.updateStatus();
					updateCurrentPlayer();
					nextPlayerStatus.setText("Finish fighting");
					nextPlayerStatus.setVisible(false);
				default:
					break;
				}
			}

		});

		mapPanel = new PolygonMapPanel(this);
		mapPanel.setBorder(BorderFactory.createLineBorder(Color.black));

		setSize(900, 100);
		contentPane.add(currentPlayerTF);
		contentPane.add(newArmyCounter);
		contentPane.add(attDices);
		contentPane.add(defDices);
		contentPane.add(calvalierCounter);
		contentPane.add(useUlti);
		contentPane.add(nextPlayerStatus);
		contentPane.add(mapPanel);
	}

	public void updateSize(int width, int height) {
		int txtWidth = 42;
		int margin = 10;
		width = Math.max(width, 950);
		height = Math.max(height, 75);
		currentPlayerTF.setBounds(5, 5, 280, txtWidth);
		newArmyCounter.setBounds(280 + margin, 5, txtWidth, txtWidth);
		attDices.setBounds(280 + txtWidth + 2 * margin, 5, 120, txtWidth);
		defDices.setBounds(280 + txtWidth + 120 + 3 * margin, 5, 80, txtWidth);
		calvalierCounter.setBounds(width - 190 - margin - 140 - margin - txtWidth, 5, txtWidth, txtWidth);
		useUlti.setBounds(width - 190 - margin - 140, 5, 140, txtWidth);
		nextPlayerStatus.setBounds(width - 190, 5, 190, txtWidth);
		mapPanel.setBounds(5, txtWidth + margin, width, height);
		setSize((int) (width + 1.5 * margin), height + 2 * txtWidth);
	}

	public void updateCurrentPlayer() {
		currentPlayer = GameCreator.nextPlayer();
		currentPlayerTF.setText(currentPlayer.name);
		currentPlayerTF.setForeground(currentPlayer.color);
		currentPlayerTF.setBackground(GUImanager.contrast(currentPlayer.color));
		currentPlayer.addTroops();
		newArmy = currentPlayer.getNewTroops();
		switch (GameCreator.getGameState()) {
		case START:
			if (newArmy == 0) {
				GameCreator.updateGameStatus();
				currentPlayer.updateStatus();
				currentPlayer.addTroops();
				newArmy = currentPlayer.getNewTroops();
			}
			break;
		case PLAY:
			useUlti.setVisible(true);
			if (0 < currentPlayer.ultiReady()) {
				useUlti.setBackground(Color.BLACK);
				useUlti.setForeground(Color.YELLOW);
				useUlti.setEnabled(true);
			} else if (newArmy < 0) {
				newArmy = Math.abs(newArmy);
				calvalierCounter.setText(String.valueOf(GameCreator.getGoldenCavalier()));
				useUlti.setBackground(Color.BLUE);
				useUlti.setForeground(Color.WHITE);
				useUlti.setEnabled(false);
			} else
				useUlti.setVisible(false);
			currentPlayer.updateStatus();
			newArmyCounter.setVisible(true);
		default:
			break;
		}
		newArmyCounter.setText(String.valueOf(newArmy));
	}

	public int getNewArmyCount() {
		return newArmy;
	}

	public void placeTroops(int t, Country c) {
		for (int i = 0; i < t; i++)
			c.addSoldiers();
		newArmy -= t;
		if (newArmy <= 0) {
			currentPlayer.updateStatus();
			nextPlayerStatus.setVisible(true);
			newArmyCounter.setVisible(false);
		} else
			newArmyCounter.setText(String.valueOf(newArmy));
	}

}
