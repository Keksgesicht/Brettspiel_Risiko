package io.gui;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

/**
 * @author Braun
 */
public abstract class GUImanager {

	public static double SCALE = 1.0d;
	public static Dimension mapSpace;

	private static JFrame frame;

	public static void setFrame(JFrame frame) {
		GUImanager.frame = frame;
		frame.setVisible(true);
	}

	public static JFrame getFrame() {
		return frame;
	}

	public static Color contrast(Color color) {
		// Counting the perceptive luminance - human eye favors green color
		double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
		int d = luminance > 0.5 ? 
				0 : // bright colors - black font
				255; // dark colors - white font
		return new Color(d, d, d);
	}

}
