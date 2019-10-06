package io.gui.frames;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import game.map.Country;
import game.player.Player;
import game.player.PlayerStatus;
import game.resources.GameCreator;
import io.gui.components.DicePanel;
import io.gui.components.PolygonMapPanel;

@SuppressWarnings("serial")
public class GameMapFrame extends JFrame {

	public final JPanel mapPanel;
	public final DicePanel attDices;
	public final DicePanel defDices;

	private JPanel contentPane;
	private JTextField currentPlayerTF;
	private JTextField newArmyCounter;
	private JTextField calvalierCounter;
	private JButton useUlti;
	public JButton nextPlayerStatus;
	private Player currentPlayer = GameCreator.getCurrentPlayer();
	private int newArmy = currentPlayer.getNewTroops();

	/**
	 * Create the frame.
	 */
	public GameMapFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 750, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setResizable(false);
		setContentPane(contentPane);

		mapPanel = new PolygonMapPanel(this);
		mapPanel.setBounds(50, 65, 420, 420);
		contentPane.add(mapPanel);

		int txtWidth = 47;
		Font risikoFont = new Font("Courier", Font.BOLD, 42);

		attDices = new DicePanel(3);
		attDices.setBounds(500, 75, 120, txtWidth);
		contentPane.add(attDices);
		defDices = new DicePanel(2);
		defDices.setBounds(500, 115, 120, txtWidth);
		contentPane.add(defDices);

		calvalierCounter = new JTextField(String.valueOf(GameCreator.getGoldenCavalier()));
		calvalierCounter.setEditable(false);
		calvalierCounter.setFont(risikoFont);
		calvalierCounter.setBackground(Color.BLACK);
		calvalierCounter.setForeground(Color.YELLOW);
		calvalierCounter.setHorizontalAlignment(SwingConstants.CENTER);
		calvalierCounter.setBounds(500, 180, txtWidth, txtWidth);
		contentPane.add(calvalierCounter);

		Font buttonFont = new Font("Courier", Font.BOLD, 20);
		useUlti = new JButton("use Cards!");
		useUlti.setFont(buttonFont);
		useUlti.setVisible(false);
		useUlti.setBackground(Color.BLACK);
		useUlti.setForeground(Color.YELLOW);
		useUlti.setHorizontalAlignment(SwingConstants.CENTER);
		useUlti.setBounds(550, 180, 140, txtWidth);
		useUlti.addActionListener(new ActionListener() {

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
		contentPane.add(useUlti);

		nextPlayerStatus = new JButton("Start fighting");
		nextPlayerStatus.setFont(buttonFont);
		// nextPlayerStatus.setBackground(Color.WHITE);
		nextPlayerStatus.setForeground(Color.BLACK);
		nextPlayerStatus.setHorizontalAlignment(SwingConstants.CENTER);
		nextPlayerStatus.setBounds(500, 240, 190, txtWidth);
		nextPlayerStatus.setVisible(false);
		nextPlayerStatus.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent evt) {
				switch (currentPlayer.getStatus()) {
				case FIGHT:
					currentPlayer.updateStatus();
					nextPlayerStatus.setText("Next Player");
					break;
				case END:
					currentPlayer.updateStatus();
					updateCurrentPlayer();
					nextPlayerStatus.setText("Start fighting");
					break;
				case INIT:
					if (newArmy == 0) {
						currentPlayer.updateStatus();
						nextPlayerStatus.setText("Finish fighting");
					} else
						JOptionPane.showMessageDialog(GameMapFrame.this, "There are troops left to be placed");
				default:
					break;
				}
			}

		});
		contentPane.add(nextPlayerStatus);

		currentPlayerTF = new JTextField();
		currentPlayerTF.setEditable(false);
		currentPlayerTF.setFont(risikoFont);
		currentPlayerTF.setBackground(Color.WHITE);
		currentPlayerTF.setHorizontalAlignment(JTextField.LEFT);
		currentPlayerTF.setText(currentPlayer.name);
		currentPlayerTF.setForeground(currentPlayer.color);
		currentPlayerTF.setBounds(60, 5, 300, 55);
		contentPane.add(currentPlayerTF);

		newArmyCounter = new JTextField();
		newArmyCounter.setEditable(false);
		newArmyCounter.setFont(risikoFont);
		newArmyCounter.setBackground(Color.WHITE);
		newArmyCounter.setForeground(Color.BLACK);
		newArmyCounter.setHorizontalAlignment(SwingConstants.CENTER);
		newArmyCounter.setText(String.valueOf(newArmy));
		newArmyCounter.setBounds(400, 5, 55, 55);
		contentPane.add(newArmyCounter);
	}

	public void updateCurrentPlayer() {
		currentPlayer = GameCreator.nextPlayer();
		currentPlayerTF.setText(currentPlayer.name);
		currentPlayerTF.setForeground(currentPlayer.color);
		currentPlayer.addTroops();
		newArmy = currentPlayer.getNewTroops();
		switch (GameCreator.getGameState()) {
		case START:
			if (newArmy == 0) {
				GameCreator.updateGameStatus();
				currentPlayer.updateStatus();
				currentPlayer.addTroops();
				newArmy = currentPlayer.getNewTroops();
				nextPlayerStatus.setVisible(true);
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
		newArmyCounter.setText(String.valueOf(newArmy));
	}

}
