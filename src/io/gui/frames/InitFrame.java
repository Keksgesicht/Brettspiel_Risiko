package io.gui.frames;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class InitFrame extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					InitFrame frame = new InitFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public InitFrame() {
		setTitle("Risiko - Welcome");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 420, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnLoadLastGame = new JButton("load last Game");
		btnLoadLastGame.setBounds(10, 235, 190, 25);
		contentPane.add(btnLoadLastGame);
		
		JButton btnCreateNewGame = new JButton("create new Game");
		btnCreateNewGame.setBounds(210, 235, 190, 25);
		contentPane.add(btnCreateNewGame);
	}
}
