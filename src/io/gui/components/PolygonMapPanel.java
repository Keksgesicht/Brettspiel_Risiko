package io.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;

import game.map.Country;
import game.player.Player;
import game.resources.GameCreator;
import game.resources.GameStatus;

@SuppressWarnings("serial")
public class PolygonMapPanel extends JPanel {
	
	
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		JPopupMenu popup = new JPopupMenu();
	    ActionListener menuListener = new ActionListener() {
	      public void actionPerformed(ActionEvent event) {
	        System.out.println("Popup menu item ["
	            + event.getActionCommand() + "] was pressed.");
	      }
	    };
	    JMenuItem item;
	    popup.add(item = new JMenuItem("Left"));
	    item.setHorizontalTextPosition(JMenuItem.RIGHT);
	    item.addActionListener(menuListener);
	    JMenu submenu = new JMenu("A submenu");

	    JMenuItem menuItem = new JMenuItem("An item in the submenu");
	    submenu.add(menuItem);

	    menuItem = new JMenuItem("Another item");
	    submenu.add(menuItem);
	    popup.add(submenu);
		
	    popup.setLabel("Justification");
	    popup.setBorder(new BevelBorder(BevelBorder.RAISED));
	    
		Map<Polygon,Country> cmap = GameCreator.getCMap();		
		for(Entry<Polygon, Country> pc : cmap.entrySet()) {
			// draw Country with Player color
			if(GameCreator.getGameState() != GameStatus.INIT) {
				Player ply = pc.getValue().player();
				g.setColor(ply.color);
				g.fillPolygon(pc.getKey());
			}
			// add popup menu for specific polygon
			/*
			this.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent arg0) {
					// open popup menu only with right mouse button
					if(arg0.getButton() != MouseEvent.BUTTON3)
						return;
					
					// prepare popup menu
					switch(ply.getStatus()) {
					case INIT:
						
						break;
					case FIGHT:
						break;
					case END:
						break;
					default:
						return;
					
					}
					
					// show popup menu
					if(pc.getKey().contains(arg0.getPoint())) {
						popup.show(PolygonMapPanel.this, arg0.getX(), arg0.getY());
					}
				}
			});	*/
			// draw Country border
			g.setColor(Color.BLACK);
			g.drawPolygon(pc.getKey());
		}
	    
		
		
	}
	
}