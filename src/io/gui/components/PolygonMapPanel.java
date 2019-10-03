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
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;

import game.map.Country;
import game.map.CountryBorder;
import game.player.Player;
import game.player.PlayerStatus;
import game.resources.GameCreator;
import io.gui.frames.GameMapFrame;
import io.gui.frames.InitFrame;

@SuppressWarnings("serial")
public class PolygonMapPanel extends JPanel {

	GameMapFrame frame;
	JPopupMenu popup = new JPopupMenu();
	Map<Polygon,Country> cmap = GameCreator.getCMap();
	ActionListener popupItemListener;
	Set<Country> antiGreyCoties;
	Country cotyOld;
	Country coty;

	public PolygonMapPanel(GameMapFrame frame) {
		super();
		this.frame = frame;
		popup.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		
		popupItemListener = new ActionListener() {
			
			public void actionPerformed(ActionEvent evt) {
				int t = Integer.parseInt(((JMenuItem) evt.getSource()).getText());
				frame.placeTroops(t, coty);
				repaint();
			}
			
		};
		
		addMouseListener(new MouseAdapter() {
			
			Point mouseP;
			JMenuItem item;
			Player currentPlayer;
			int troops;
			
			public void getMouseMapInfo(MouseEvent e) {
				mouseP = e.getPoint();
				currentPlayer = GameCreator.getCurrentPlayer();
				coty = null;
				for(Polygon poly : cmap.keySet()) {
					if(poly.contains(mouseP)) {
						coty = cmap.get(poly); break;
					}
				}
			}			
			
			@Override
			public void mouseClicked(MouseEvent e) {
				getMouseMapInfo(e);
				if(coty == null) return;
				
				switch(GameCreator.getGameState()) {
				case INIT:
					if(coty.king() != null) break;
					currentPlayer.addCountry(coty);
					addAndRepaint();
					if(GameCreator.getCountries().stream().filter(c -> c.king() == null).count() == 0) {
						GameCreator.updateGameStatus();
					} break;
				case START:
					if(coty.king() != currentPlayer) break;
					addAndRepaint();
					break;
				case PLAY:
					if(currentPlayer.getStatus() != PlayerStatus.INIT) break;
					if(currentPlayer != coty.king()) break;
					
					int army = frame.getNewArmyCount();
					int i = 1; int n = 0;
					troops = (int) (i * Math.pow(10, n));
					popup.removeAll();
					while(troops < army){
						popup.add(item = new JMenuItem(String.valueOf(troops)));
						item.addActionListener(popupItemListener);
						// loop unit
						if(i == 2) i = 5;
						else if(i == 5) {
							i = 1; n++;
						} else i++;
						troops = (int) (i * Math.pow(10, n));
					} 
					popup.add(item = new JMenuItem(String.valueOf(army)));
					item.addActionListener(popupItemListener);
					popup.show(PolygonMapPanel.this, mouseP.x, mouseP.y);
				default:
					break;
				}
			}
			
			private void addAndRepaint() {
				coty.addSoldiers();
				repaint();
				frame.updateCurrentPlayer();
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
				getMouseMapInfo(e);
				if(coty == null) return;
				
				switch(currentPlayer.getStatus()) {
				case FIGHT:
					antiGreyCoties = coty.getNeighboringEnemyCountries();
					repaint();
					cotyOld = coty;
					break;
				case END:
					antiGreyCoties = coty.getNearFriendlyCountries();
					repaint();
					cotyOld = coty;
					break;
				default:
					break;
				}
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				getMouseMapInfo(e);
				if(coty == null) {
					cotyOld = null;
					return;
				}
				
				switch(currentPlayer.getStatus()) {
				case FIGHT:
					if(!antiGreyCoties.contains(coty)) break;
					fight();
					break;
				case END:
					if(!antiGreyCoties.contains(coty)) break;
					
					 moveOptionPane();
					// TODO
				default:
					break;
				}
				antiGreyCoties = null;
				repaint();
			}
			
			private void fight() {
				boolean again;
				do {
					switch(fightOptionPane()) {
					case 0:
						again = CountryBorder.fight(cotyOld, coty, false, frame);
						break;
					case 1:
						CountryBorder.fight(cotyOld, coty, true, frame);
					default:
						again = false;
						break;
					}
				} while(again);
			}
			
			private int fightOptionPane() {
				Object[] options = {"Fight", 
									"Fast-Forward", 
									"Cancel"};
				return JOptionPane.showOptionDialog(frame,
						cotyOld.name + " --> " + coty.name,
						"Attack?",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[2]);
			}
			
			private int moveOptionPane() {
				Object[] options = new Object[42];
				// TODO
				return JOptionPane.showOptionDialog(frame,
						cotyOld.name + " --> " + coty.name,
						"Move!",
						JOptionPane.YES_NO_CANCEL_OPTION,
						JOptionPane.QUESTION_MESSAGE,
						null,
						options,
						options[2]);
			}
			
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Set<Entry<Polygon, Country>> cmapES = cmap.entrySet();
		
		cmapES.stream()
			  .forEach(pc -> fillOnePolygon(g, pc.getKey(), pc.getValue()));
		
		g.setColor(Color.BLACK);
		cmapES.parallelStream()
			  .forEach(pc -> drawOnePolygon(g, pc.getKey(), pc.getValue()));
	}
	
	private void fillOnePolygon(Graphics g, Polygon poly, Country cotyP) {
		Player ply = cotyP.king();
		// draw Country with Player color
		if(ply != null) {
			g.setColor(ply.color);
			switch(ply.getStatus()) {
			case FIGHT:
			case END:
				if(!antiGreyCoties.contains(cotyP)) 
					g.setColor(Color.GRAY);
			default:
				break;
			} g.fillPolygon(poly);
		}
	}
	
	private void drawOnePolygon(Graphics g, Polygon poly, Country cotyP) {
		// draw Country borde
		g.drawPolygon(poly);
		// draw country name and soldier count
		Point polyP = middlePoint(poly);
		g.drawString(cotyP.name, polyP.x, polyP.y);
		g.drawString(String.valueOf(cotyP.getSoldiers()), polyP.x, polyP.y + 20);
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