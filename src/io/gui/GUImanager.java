/**
 * 
 */
package io.gui;

import java.awt.Color;

/**
 * @author Jan Braun
 *
 */
public abstract class GUImanager {

	/**
	 * 
	 */
	public GUImanager() {
		// TODO Auto-generated constructor stub
	}

	public static Color contrast(Color color) {
		// Counting the perceptive luminance - human eye favors green color...
		double luminance = (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue()) / 255;
		int d = luminance > 0.5 ? 
				0 : // bright colors - black font
				255; // dark colors - white font
		return new Color(d, d, d);
	}

}
