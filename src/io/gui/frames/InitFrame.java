package io.gui.frames;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import io.gui.GUImanager;

@SuppressWarnings("serial")
public class InitFrame extends JFrame {

	private JPanel contentPane;
	private JTextField txtRisiko;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					GUImanager.setFrame(new InitFrame());
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
		setForeground(new Color(0, 0, 0));
		setTitle("Risiko - Welcome");
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 420, 185);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		contentPane.setBackground(Color.BLACK);
		
		JButton btnLoadLastGame = new JButton("load last Game");
		btnLoadLastGame.setBounds(10, 120, 190, 25);
		btnLoadLastGame.setBackground(Color.DARK_GRAY);
		btnLoadLastGame.setForeground(Color.WHITE);
		contentPane.add(btnLoadLastGame);
		
		JButton btnCreateNewGame = new JButton("create new Game");
		btnCreateNewGame.setBounds(210, 120, 190, 25);
		btnCreateNewGame.setBackground(Color.DARK_GRAY);
		btnCreateNewGame.setForeground(Color.WHITE);
		btnCreateNewGame.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent evt) {
				int n = 5;
				Object[] options = new Object[n];
				for(int i=0; i<5; i++) {
					options[i] = String.valueOf(i+2);
				}
				int playerCount = JOptionPane.showOptionDialog(InitFrame.this,
				    "How many Players?",
				    "Player Count",
				    JOptionPane.YES_NO_CANCEL_OPTION,
				    JOptionPane.QUESTION_MESSAGE,
				    null,
				    options,
				    options[0]);
				GUImanager.setFrame(new GameMapFrame(playerCount));
				InitFrame.this.dispose();
			}
		});
		contentPane.add(btnCreateNewGame);
		
		txtRisiko = new JTextField();
		txtRisiko.setHorizontalAlignment(SwingConstants.CENTER);
		txtRisiko.setEditable(false);
		txtRisiko.setText("RISIKO");
		txtRisiko.setBounds(10, 10, 390, 100);
		contentPane.add(txtRisiko);
		txtRisiko.setColumns(10);
		Font risikoFont = new Font("Courier", Font.BOLD,100);
		txtRisiko.setFont(risikoFont);
		txtRisiko.setForeground(Color.RED);
		txtRisiko.setBackground(Color.BLACK);
		txtRisiko.setBorder(BorderFactory.createLineBorder(Color.black));
	}
}
