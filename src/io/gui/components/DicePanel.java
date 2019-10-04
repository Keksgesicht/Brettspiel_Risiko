package io.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

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
			JOptionPane.showMessageDialog(null, "Konnte Resourcen nicht laden: " + ex.getMessage(),
					"Fehler beim Laden der Resourcen", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
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
		// repaint(50);
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
		}
	}

	public BufferedImage getDice(int value) {
		return dices[value % dices.length];
	}

	/**
     * Lädt ein Bild aus den Resourcen
     * @param name der Name der Datei
     * @return das Bild als {@link BufferedImage}-Objekt
     * @throws IOException Eine IOException wird geworfen, falls das Bild nicht gefunden wurde oder andere Probleme beim Laden auftreten
     */
	public static BufferedImage loadImage(String name) throws IOException {
		URL res = DicePanel.class.getClassLoader().getResource(name);
        if(res == null)
            throw new IOException("Resource not found: " + name);
        return ImageIO.read(res);
    }

	private void loadDices() throws IOException {
		dices = new BufferedImage[6];
		for(int i = 1; i <= 6; i++) {
			dices[i - 1] = loadImage("dice" + i + ".jpg");
		}
	}

}
