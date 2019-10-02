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
import javax.swing.border.BevelBorder;

import game.map.Country;
import game.player.Player;
import game.player.PlayerStatus;
import game.resources.GameCreator;
import io.gui.frames.GameMapFrame;

@SuppressWarnings("serial")
public class PolygonMapPanel extends JPanel {

	GameMapFrame frame;
	JPopupMenu popup = new JPopupMenu();
	Map<Polygon,Country> cmap = GameCreator.getCMap();

	public PolygonMapPanel(GameMapFrame frame) {
		super();
		this.frame = frame;
		popup.setBorder(new BevelBorder(BevelBorder.RAISED));
		addMouseListener(new MouseAdapter() {
			
			Point mouseP;
			JMenuItem item;
			Player currentPlayer;
			Country c;
			int troops;
			
			@Override
			public void mouseClicked(MouseEvent e) {
				mouseP = e.getPoint();
				currentPlayer = GameCreator.getCurrentPlayer();
				c = null;
				for(Polygon poly : cmap.keySet()) {
					if(poly.contains(mouseP)) {
						c = cmap.get(poly); break;
					}
				} if(c == null) return;
				
				switch(GameCreator.getGameState()) {
				case INIT:
					if(c.king() != null) break;
					currentPlayer.addCountry(c);
					addAndRepaint();
					if(GameCreator.getCountries().stream().filter(c -> c.king() == null).count() == 0) {
						GameCreator.updateGameStatus();
					} break;
				case START:
					if(c.king() != currentPlayer) break;
					addAndRepaint();
					break;
				case PLAY:
					if(currentPlayer.getStatus() != PlayerStatus.INIT) break;
					if(currentPlayer != c.king()) break;
					
					int army = PolygonMapPanel.this.frame.getNewArmyCount();
					int i = 1; int n = 0;
					troops = (int) (i * Math.pow(10, n));
					popup.removeAll();
					while(troops < army) {
						popup.add(item = new JMenuItem(String.valueOf(troops)));
						item.addActionListener(new ActionListener() {
							
							public void actionPerformed(ActionEvent evt) {
								int t = Integer.parseInt(((JMenuItem) evt.getSource()).getText());
								PolygonMapPanel.this.frame.placeTroops(t, c);
								repaintMap();
							}
							
						});
						// loop unit
						if(i == 2) i = 5;
						else if(i == 5) {
							i = 1; n++;
						} else i++;
						troops = (int) (i * Math.pow(10, n));
					} 
					popup.add(item = new JMenuItem(String.valueOf(army)));
					item.addActionListener(new ActionListener() {
						
						public void actionPerformed(ActionEvent evt) {
							int t = Integer.parseInt(((JMenuItem) evt.getSource()).getText());
							PolygonMapPanel.this.frame.placeTroops(t, c);
							repaintMap();
						}
						
					});
					popup.show(PolygonMapPanel.this, mouseP.x, mouseP.y);
				default:
					break;
				}
			}
			
			private void addAndRepaint() {
				c.addSoldiers();
				repaintMap();
				PolygonMapPanel.this.frame.updateCurrentPlayer();
			}
			
			@Override
			public void mouseDragged(MouseEvent e) {
				
			}
			
		});
	}
	
	void repaintMap() {
		new Thread() { 
			public void run() {
				PolygonMapPanel.this.repaint(); 
			} 
		}.start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
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