package io.gui.frames;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import game.player.Player;
import game.resources.GameCreator;
import io.gui.GUImanager;

@SuppressWarnings("serial")
public class PlayerCustomizationFrame extends JFrame {

	public PlayerCustomizationFrame(int players) {
		super("Player Customization");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 650, 475);
		setResizable(false);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e1) { // TODO Auto-generated catch block
			e1.printStackTrace();
		}

		JTabbedPane allPlayersPanel = new JTabbedPane();
		int borderwidth = 5;
		allPlayersPanel.setBorder(BorderFactory.createEmptyBorder(borderwidth, borderwidth, borderwidth, borderwidth));

		JLabel[] playerNameLabel = new JLabel[players];
		JTextField[] playerNameFields = new JTextField[players];
		JColorChooser[] colorChooser = new JColorChooser[players];
		JPanel[] playerPanels = new JPanel[players];
		JPanel[] playerNamePanels = new JPanel[players];
		JPanel colorPanel = new JPanel();
		colorPanel.setLayout(new BoxLayout(colorPanel, BoxLayout.Y_AXIS));

		int width = 100;
		int height = 25;

		for (int i = 0; i < players; i++) {

			playerNameLabel[i] = new JLabel("Name of Player " + String.valueOf(i + 1) + ":");

			playerNameFields[i] = new JTextField();
			playerNameFields[i].setPreferredSize(new Dimension(width, height));
			playerNameFields[i].setDocument(new JTextFieldLimit(15));
			playerNameFields[i].setText("Player" + String.valueOf(i + 1));
			playerNameFields[i].addFocusListener(new myTextFieldFocus(playerNameFields[i]));

			Color initialColor;
			switch (i) {
			case 0:
				initialColor = Color.GREEN;
				break;
			case 1:
				initialColor = Color.RED;
				break;
			case 2:
				initialColor = Color.BLUE;
				break;
			case 3:
				initialColor = Color.YELLOW;
				break;
			case 4:
				initialColor = Color.ORANGE;
				break;
			case 5:
				initialColor = Color.CYAN;
				break;
			default:
				initialColor = Color.BLACK;
				break;
			}
			colorChooser[i] = new JColorChooser(initialColor);
			// colorChooser[i].setSelectionModel(newModel);
			// colorChooser[i].getSelectionModel().addChangeListener(listener);

			// colorPanel.add(playerNamePanels[i]);

			playerNamePanels[i] = new JPanel();
			playerNamePanels[i].setLayout(new FlowLayout(FlowLayout.LEFT));
			playerNamePanels[i].add(playerNameLabel[i]);
			playerNamePanels[i].add(playerNameFields[i]);

			playerPanels[i] = new JPanel();
			playerPanels[i].setLayout(new BorderLayout(5, 5));
			playerPanels[i].add(playerNamePanels[i], BorderLayout.NORTH);
			playerPanels[i].add(colorChooser[i], BorderLayout.SOUTH);

			allPlayersPanel.addTab("Player" + String.valueOf(i + 1), null, playerPanels[i],
					"Customization of Player" + String.valueOf(i + 1));

			switch (i) {
			case 0:
				allPlayersPanel.setMnemonicAt(i, KeyEvent.VK_1);
				break;
			case 1:
				allPlayersPanel.setMnemonicAt(i, KeyEvent.VK_2);
				break;
			case 2:
				allPlayersPanel.setMnemonicAt(i, KeyEvent.VK_3);
				break;
			case 3:
				allPlayersPanel.setMnemonicAt(i, KeyEvent.VK_4);
				break;
			case 4:
				allPlayersPanel.setMnemonicAt(i, KeyEvent.VK_5);
				break;
			case 5:
				allPlayersPanel.setMnemonicAt(i, KeyEvent.VK_6);
				break;
			default:
				break;
			}
		}

		JButton startGameButton = new JButton("Start Game");
		startGameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				ArrayList<Player> playerList = new ArrayList<Player>();
				for (int j = 0; j < players; j++) {
					playerList.add(
							new Player(playerNameFields[j].getText(), colorChooser[j].getColor(), 50 - 5 * players));
				}
				GameCreator.createNewGame(playerList);
				GUImanager.setFrame(new GameMapFrame());
				dispose();
			}
		});

		JPanel advancedPanel = new JPanel();
		advancedPanel.setBackground(Color.GREEN);

		/*
		 * JCheckBox showAdvancedCheckBox = new JCheckBox("Show advanced options");
		 * showAdvancedCheckBox.addItemListener(new ItemListener() {
		 * 
		 * @Override public void itemStateChanged(ItemEvent e) { PlayerCustomizationFrame
		 * currentFrame = PlayerCustomizationFrame.this.; Rectangle tempRectangle =
		 * currentFrame.getBounds(); if (e.getStateChange() == ItemEvent.SELECTED) {
		 * tempRectangle.height += 100; currentFrame.add(advancedPanel, BorderLayout.SOUTH);
		 * currentFrame.setBounds(tempRectangle); } else { // disables the rest of the Frame
		 * tempRectangle.height -= 100; currentFrame.remove(advancedPanel);
		 * currentFrame.setBounds(tempRectangle); } PlayerCustomizationFrame.this.validate(); }
		 * }); bottomPanel.add(showAdvancedCheckBox);
		 */

		JPanel bottomPanel = new JPanel();

		bottomPanel.add(startGameButton);

		this.add(allPlayersPanel);
		this.add(bottomPanel, BorderLayout.SOUTH);

		allPlayersPanel.validate();
		this.validate();
		setVisible(true);
	}

}

class myTextFieldFocus implements FocusListener {

	private JTextField myJTextField;
	private String defaultString;

	public myTextFieldFocus(JTextField myJTextField) {
		this.myJTextField = myJTextField;
		defaultString = myJTextField.getText();
	}

	@Override
	public void focusGained(FocusEvent e) {
		if (myJTextField.getText().equals(defaultString)) {
			myJTextField.setText("");
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		if (myJTextField.getText().isEmpty()) {
			myJTextField.setText(defaultString);
		}
	}

}

@SuppressWarnings("serial")
class JTextFieldLimit extends PlainDocument {
	private int limit;

	JTextFieldLimit(int limit) {
		super();
		this.limit = limit;
	}

	@Override
	public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		if (str == null)
			return;
		if ((getLength() + str.length()) <= limit) {
			super.insertString(offset, str, attr);
		}
	}
}
