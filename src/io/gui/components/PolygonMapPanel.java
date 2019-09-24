package io.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JPanel;
import game.map.Country;
import game.player.Player;
import game.resources.GameCreator;
import game.resources.GameStatus;

@SuppressWarnings("serial")
public class PolygonMapPanel extends JPanel {
	
	public PolygonMapPanel() {
		super();
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				Point mouseP = arg0.getPoint();
				Map<Polygon,Country> cmap = GameCreator.getCMap();
				Player currentPlayer = GameCreator.getCurrentPlayer();
				
				Country c = null;
				for(Polygon poly : cmap.keySet()) {
					if(poly.contains(mouseP)) {
						c = cmap.get(poly); break;
					}
				}
				if(c == null) return;
				
				switch(GameCreator.getGameState()) {
				case START:
					if(c.player() != null) break;
					
					currentPlayer.controlledCountries.add(c);
					c.addSoldiers(); break;
				case PLAY:
					switch(currentPlayer.getStatus()) {
					case INIT:
						break;
					case END:
						break;
					case FIGHT:
						break;
					default:
						break;
					}
				default:
					break;
				}
			}
		});
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Map<Polygon,Country> cmap = GameCreator.getCMap();		
		for(Entry<Polygon, Country> pc : cmap.entrySet()) {
			// draw Country with Player color
			if(GameCreator.getGameState() != GameStatus.INIT) {
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