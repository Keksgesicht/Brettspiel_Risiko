package io.gui.frames;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class loadGameFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3740068696150772474L;

	public loadGameFrame() {
		super("load Game");
		this.add(new JFileChooser());
		this.pack();
		this.validate();

	}
}
