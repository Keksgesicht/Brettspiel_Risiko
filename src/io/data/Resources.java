/**
 * 
 */
package io.data;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;

/**
 * @author Braun
 */
public abstract class Resources {

	/**
	 * Lädt ein Bild aus den Resourcen
	 * 
	 * @param name der Name der Datei
	 * @return das Bild als {@link BufferedImage}-Objekt
	 * @throws IOException Eine IOException wird geworfen, falls das Bild nicht
	 *                     gefunden wurde oder andere Probleme beim Laden auftreten
	 */
	public static BufferedImage loadImage(String name) throws IOException {
		URL res = Resources.class.getClassLoader().getResource(name);
		if (res == null)
			throw new IOException("Resource not found: " + name);
		return ImageIO.read(res);
	}

}
