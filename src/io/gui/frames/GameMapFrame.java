package io.gui.frames;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
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
import io.gui.GUImanager;
import io.gui.components.DicePanel;
import io.gui.components.PolygonMapPanel;

@SuppressWarnings("serial")
public class GameMapFrame extends JFrame {

	public final PolygonMapPanel mapPanel;
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
	 * 
	 * @param distributeRandomly true, if the countries should be distributed randomly
	 */
	public GameMapFrame(boolean distributeRandomly) {
		this();
		if (distributeRandomly) {
			distributeRandomly();
		}
	}

	public GameMapFrame() {
		currentPlayer = GameCreator.getCurrentPlayer();
		newArmy = currentPlayer.getNewTroops();
		Font risikoFont = new Font("Courier", Font.BOLD, 32);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 960, 30 + 42 + 10);
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
		useUlti.setEnabled(false);
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

		updateSize(0, 0);
		contentPane.add(currentPlayerTF);
		contentPane.add(newArmyCounter);
		contentPane.add(attDices);
		contentPane.add(defDices);
		contentPane.add(calvalierCounter);
		contentPane.add(useUlti);
		contentPane.add(nextPlayerStatus);
		contentPane.add(mapPanel);

		setVisible(true);
	}

	private void distributeRandomly() {
		// Collection<Country> countriesMap = GameCreator.getCPMap().values();
		MouseListener[] mouseListener = mapPanel.getMouseListeners();
		// countriesMap.forEach(action);
		List<Polygon> list = GameCreator.getCPMap().keySet().stream().collect(Collectors.toList());

		for (Polygon entry : list) {
			Point middlePoint = mapPanel.middlePoint(entry);
			mouseListener[0].mouseClicked(
					new MouseEvent(this, MouseEvent.MOUSE_CLICKED, Calendar.getInstance().getTimeInMillis(), 0,
							(int) middlePoint.getX(), (int) middlePoint.getY(), 1, false));
		}
	}

	public void updateSize(int width, int height) {
		width = Math.max(width, 960);
		int txtWidth = 42;
		int margin = 10;

		int playerTFwidth = 340;
		int nextPlayerStatusWidth = 220;
		int ultiWidth = 150;

		currentPlayerTF.setBounds(5, 5, playerTFwidth, txtWidth);
		newArmyCounter.setBounds(playerTFwidth + margin, 5, txtWidth, txtWidth);
		attDices.setBounds(playerTFwidth + txtWidth + 2 * margin, 5, 120, txtWidth);
		defDices.setBounds(playerTFwidth + txtWidth + 120 + 3 * margin, 5, 80, txtWidth);
		calvalierCounter.setBounds(width - nextPlayerStatusWidth - margin - ultiWidth - margin - txtWidth, 5, txtWidth,
				txtWidth);
		useUlti.setBounds(width - nextPlayerStatusWidth - margin - ultiWidth, 5, ultiWidth, txtWidth);
		nextPlayerStatus.setBounds(width - nextPlayerStatusWidth, 5, nextPlayerStatusWidth, txtWidth);
		mapPanel.setBounds(5, txtWidth + margin, width, height);

		setSize((int) (width + 1.5 * margin), 30 + Math.max(height + txtWidth + margin + 3, txtWidth + margin));
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
			useUlti.setBackground(Color.BLACK);
			useUlti.setForeground(Color.YELLOW);
			useUlti.setEnabled(false);
			if (0 < currentPlayer.ultiReady()) {
				useUlti.setBackground(Color.BLACK);
				useUlti.setForeground(Color.YELLOW);
				useUlti.setEnabled(true);
			} else if (newArmy < 0) {
				newArmy = Math.abs(newArmy);
				calvalierCounter.setText(String.valueOf(GameCreator.getGoldenCavalier()));
				JOptionPane.showMessageDialog(this,
						"Your ultimate ability has been used automatically, because you had 5 cards", "Ultimate used",
						JOptionPane.INFORMATION_MESSAGE);
			}
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
