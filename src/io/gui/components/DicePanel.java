package io.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import io.data.Resources;

/**
 * @author Braun
 */
@SuppressWarnings("serial")
public class DicePanel extends JPanel {

	BufferedImage[] dices;

	private Integer[] diceValues;
	private int numDices;

	public DicePanel(int numDices) {
		try {
			loadDices();
		} catch (IOException ex) {
			ex.printStackTrace();
			JOptionPane.showMessageDialog(null, "Konnte Resourcen nicht laden: " + ex.getMessage(),
					"Fehler beim Laden der Resourcen", JOptionPane.ERROR_MESSAGE);
			System.exit(200);
		}
		this.numDices = numDices;
		diceValues = new Integer[numDices];
		Random random = new Random(System.currentTimeMillis());
		for (int i = 0; i < numDices; i++) {
            diceValues[i] = random.nextInt(6) + 1;
        }
		this.setBackground(Color.GRAY);
		repaint();
	}

	public void drawDices(Integer[] diceValues) {
		this.diceValues = diceValues;
		numDices = diceValues.length;
		paintComponent(getGraphics());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		int width = getWidth();
		int height = getHeight();
		int margin = 10;
		int diceCount = Math.min(diceValues.length, numDices);

		int diceSize = Math.min(height - 2 * margin, (width - (diceCount + 1) * margin) / diceCount);
		int offsetX = (width - 2 * margin - (diceCount * diceSize)) / 2;

		for (int i = 0; i < diceCount; i++) {
			int x = offsetX + i * (margin + diceSize);
			int y = margin;
			int value = (diceValues[i] - 1) % 6;
			g.drawImage(getDice(value), x, y, diceSize, diceSize, null);
			try {
				Thread.sleep(33);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public BufferedImage getDice(int value) {
		return dices[value % dices.length];
	}

	private void loadDices() throws IOException {
		dices = new BufferedImage[6];
		for(int i = 1; i <= 6; i++) {
			dices[i - 1] = Resources.loadImage("pictures/dice" + i + ".jpg");
		}
	}

}
