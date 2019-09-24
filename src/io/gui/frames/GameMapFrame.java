package io.gui.frames;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import io.gui.components.PolygonMapPanel;

@SuppressWarnings("serial")
public class GameMapFrame extends JFrame {
	
	private JPanel mapPanel;
	private JPanel contentPane;

	/**
	 * Create the frame.
	 */
	public GameMapFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 350);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		setContentPane(contentPane);
		
		mapPanel = new PolygonMapPanel();
		mapPanel.setBounds(50, 50, 230, 230);
		contentPane.add(mapPanel);
		
		int nAtt = 3;
		int txtWidth = 47;
		Font risikoFont = new Font("Courier", Font.BOLD,42);
		
		// attacker dice TextFields
		JTextField[] attDices = new JTextField[nAtt];
		for(int i = 0; i < nAtt; i++) {
			attDices[i] = new JTextField("0");
			attDices[i].setEditable(false);
			attDices[i].setFont(risikoFont);
			attDices[i].setBackground(Color.RED);
			attDices[i].setForeground(Color.WHITE);
			attDices[i].setHorizontalAlignment(SwingConstants.CENTER);
			attDices[i].setBounds(300 + txtWidth * i, 60, txtWidth, txtWidth);
			contentPane.add(attDices[i]);
		}
		// defender dice TextFields
		int nDef = 2;
		JTextField[] defDices = new JTextField[nDef];
		for(int i = 0; i < nDef; i++) {
			defDices[i] = new JTextField("0");
			defDices[i].setEditable(false);
			defDices[i].setFont(risikoFont);
			defDices[i].setBackground(Color.BLUE);
			defDices[i].setForeground(Color.WHITE);
			defDices[i].setHorizontalAlignment(SwingConstants.CENTER);
			defDices[i].setBounds(300 + txtWidth * i, 120, txtWidth, txtWidth);
			contentPane.add(defDices[i]);
		}
		
		JTextField calvalierCounter = new JTextField("0");
		calvalierCounter.setEditable(false);
		calvalierCounter.setFont(risikoFont);
		calvalierCounter.setBackground(Color.BLACK);
		calvalierCounter.setForeground(Color.YELLOW);
		calvalierCounter.setHorizontalAlignment(SwingConstants.CENTER);
		calvalierCounter.setBounds(300, 180, txtWidth, txtWidth);
		contentPane.add(calvalierCounter);
		
		Font buttonFont = new Font("Courier", Font.BOLD,20);
		JButton useUlti = new JButton("use Cards!");
		useUlti.setFont(buttonFont);
		useUlti.setBackground(Color.BLACK);
		useUlti.setForeground(Color.YELLOW);
		useUlti.setHorizontalAlignment(SwingConstants.CENTER);
		useUlti.setBounds(350, 180, 140, txtWidth);
		contentPane.add(useUlti);
	}

}
