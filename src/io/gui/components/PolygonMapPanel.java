package io.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import game.map.Country;
import game.player.Player;
import game.player.PlayerStatus;
import game.resources.GameCreator;
import game.resources.GameStatus;
import game.resources.MapList;
import io.gui.frames.GameMapFrame;
import io.gui.frames.InitFrame;

@SuppressWarnings("serial")
public class PolygonMapPanel extends JPanel {

	JPopupMenu popup = new JPopupMenu();
	JMenuItem item;
	Country c;

	public PolygonMapPanel() {
		super();
		addMouseListener(new MouseAdapter() {
			
			@Override
			public void mouseClicked(MouseEvent e) {
				Point mouseP = e.getPoint();
				Map<Polygon,Country> cmap = GameCreator.getCMap();
				Player currentPlayer = GameCreator.getCurrentPlayer();
				
				c = null;
				for(Polygon poly : cmap.keySet()) {
					if(poly.contains(mouseP)) {
						c = cmap.get(poly); break;
					}
				}
				if(c == null) return;
				
				switch(GameCreator.getGameState()) {
				case START:
					if(c.player() != currentPlayer) break;
					
					c.addSoldiers();
					if(c.player() == null)
						currentPlayer.controlledCountries.add(c);
					break;
				case PLAY:
					if(currentPlayer.getStatus() != PlayerStatus.INIT) break;
					
					int army = 3;
					int i = 1; int n = 0;
					do {
						int troops = i * 10 ^ n;
						popup.add(item = new JMenuItem(String.valueOf(troops)));
						item.addActionListener(new ActionListener() { 
							public void actionPerformed(ActionEvent evt) {
								for(int i=0; i < troops; i++)
									c.addSoldiers();
							}
						});
						// loop unit
						if(i == 2) i = 5;
						if(i == 5) {
							i = 1; n++;
						} else i++;
					} while(i < army);
				}
			}
			
			public void mouseDragged(MouseEvent e) {
				
			}
			
		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Map<Polygon, Country> cmap = GameCreator.getCMap();
		for (Entry<Polygon, Country> pc : cmap.entrySet()) {
			// draw Country with Player color
			if (GameCreator.getGameState() != GameStatus.INIT) {
				Player ply = pc.getValue().player();
				g.setColor(ply.color);
				g.fillPolygon(pc.getKey());
			}
			// draw Country border
			g.setColor(Color.BLACK);
			g.drawPolygon(pc.getKey());
		}
	}

}