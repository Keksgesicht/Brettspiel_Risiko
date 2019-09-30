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
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import game.map.Country;
import game.player.Player;
import game.player.PlayerStatus;
import game.resources.GameCreator;

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
				case INIT:
					if(c.king() != null) break;
					
					c.addSoldiers();
					currentPlayer.addCountry(c);
					currentPlayer.addTroops();
					currentPlayer.getNext();
					repaintMap();
					if(GameCreator.getCountries().stream().filter(c -> c.king() == null).count() == 0) {
						GameCreator.updateGameStatus();
					} break;
				case START:
					if(currentPlayer.troops == 0) {
						GameCreator.updateGameStatus();
					} else {
						if(c.king() != currentPlayer) break;
						currentPlayer.addTroops();
						c.addSoldiers();
						currentPlayer.getNext();
						repaintMap();
						break;
					}
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
				default:
					break;
				}
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				
			}
			
		});
	}
	
	void repaintMap() {
		new Thread() { public void run() { PolygonMapPanel.this.repaint(); } }.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Map<Polygon, Country> cmap = GameCreator.getCMap();
		for (Entry<Polygon, Country> pc : cmap.entrySet()) {
			Polygon poly = pc.getKey();
			Country coty = pc.getValue();
			Player ply = coty.king();
			// draw Country with Player color
			if(ply != null) {
				g.setColor(ply.color);
				g.fillPolygon(poly);
			}
			// draw Country border
			g.setColor(Color.BLACK);
			g.drawPolygon(poly);
			
			Point polyP = middlePoint(poly);
			g.drawString(coty.name, polyP.x, polyP.y);
			g.drawString(String.valueOf(coty.getSoldiers()), polyP.x, polyP.y + 20);
		}
	}
	
	private Point middlePoint(Polygon poly) {
		int n = poly.npoints;
		int sumX = 0;
		for(int x : poly.xpoints) {
			sumX += x;
		}
		int sumY = 0;
		for(int y : poly.ypoints) {
			sumY += y;
		}
		return new Point(sumX / n, sumY / n);
	}

}