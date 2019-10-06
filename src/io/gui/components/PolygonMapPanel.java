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
import javax.swing.JSlider;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import game.map.Country;
import game.map.CountryBorder;
import game.player.Player;
import game.player.PlayerStatus;
import game.resources.GameCreator;
import io.gui.frames.GameMapFrame;

@SuppressWarnings("serial")
public class PolygonMapPanel extends JPanel {

	GameMapFrame frame;
	JPopupMenu popup = new JPopupMenu();
	Map<Polygon, Country> cmap = GameCreator.getCMap();
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
			boolean inProgress = false;

			public void getMouseMapInfo(MouseEvent e) {
				mouseP = e.getPoint();
				currentPlayer = GameCreator.getCurrentPlayer();
				coty = null;
				for (Polygon poly : cmap.keySet()) {
					if (poly.contains(mouseP)) {
						coty = cmap.get(poly);
						break;
					}
				}
			}

			@Override
			public void mouseClicked(MouseEvent e) {
				getMouseMapInfo(e);
				if (coty == null)
					return;

				switch (GameCreator.getGameState()) {
				case INIT:
					if (coty.king() != null)
						break;
					currentPlayer.addCountry(coty);
					addAndRepaint();
					if (GameCreator.getCountries().stream().filter(c -> c.king() == null).count() == 0) {
						GameCreator.updateGameStatus();
					}
					break;
				case START:
					if (coty.king() != currentPlayer)
						break;
					addAndRepaint();
					break;
				case PLAY:
					if (currentPlayer.getStatus() != PlayerStatus.INIT)
						break;
					if (currentPlayer != coty.king())
						break;

					popup.removeAll();
					int army = frame.getNewArmyCount();
					if (army == 0)
						break;
					int i = 1;
					int n = 0;
					troops = (int) (i * Math.pow(10, n));

					while (troops < army) {
						popup.add(item = new JMenuItem(String.valueOf(troops)));
						item.addActionListener(popupItemListener);
						// loop unit
						if (i == 2)
							i = 5;
						else if (i == 5) {
							i = 1;
							n++;
						} else
							i++;
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
				if (inProgress)
					return;
				getMouseMapInfo(e);
				if (coty == null)
					return;

				switch (currentPlayer.getStatus()) {
				case FIGHT:
					antiGreyCoties = coty.getNeighboringEnemyCountries();
					repaint();
					cotyOld = coty;
					break;
				case END:
					if (coty.getSoldiers() < 2)
						break;
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
				if (inProgress)
					return;
				inProgress = true;
				getMouseMapInfo(e);
				if (coty == null) {
					cotyOld = null;
					return;
				}

				switch (currentPlayer.getStatus()) {
				case FIGHT:
					if (antiGreyCoties == null || !antiGreyCoties.contains(coty))
						break;
					fight();
					break;
				case END:
					if (antiGreyCoties == null || !antiGreyCoties.contains(coty))
						break;
					moveOptionPane(false);
				default:
					break;
				}
				antiGreyCoties = null;
				cotyOld = null;
				inProgress = false;
				repaint();
			}

			private void fight() {
				while (true) {
					switch (fightOptionPane()) {
					case 0:
						if (!CountryBorder.fight(cotyOld, coty, false, frame)) {
							victory();
							return;
						}
						break;
					case 1:
						CountryBorder.fight(cotyOld, coty, true, frame);
						victory();
						return;
					default:
						return;
					}
				}
			}

			private void victory() {
				if (coty.getSoldiers() == 0) {
					moveOptionPane(true);
					currentPlayer.fightWon();
				}
			}

			private int fightOptionPane() {
				Object[] options = { "Fight", "Fast-Forward", "Cancel" };
				return JOptionPane.showOptionDialog(frame, cotyOld.name + " --> " + coty.name, "Attack?",
						JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
			}

			private void moveOptionPane(boolean victory) {
				int army = cotyOld.getSoldiers() - 1;
				int min = victory ? 1 : 0;
				if (victory) {
					currentPlayer.addCountry(coty);
					if (GameCreator.getPlayers().size() < 2) {
						GameCreator.updateGameStatus();
						JOptionPane.showMessageDialog(frame,
								GameCreator.getCurrentPlayer().name + " has won the game!");
						cotyOld.subSoldiers();
						coty.addSoldiers();
						frame.nextPlayerStatus.setVisible(false);
						return;
					}
				}
				JOptionPane movePane = new JOptionPane();
				JSlider armyMoveSlider = new JSlider(min, army, army);

				int maxTick = army < 10 ? 1 : army < 50 ? 5 : 10;
				armyMoveSlider.setMajorTickSpacing(maxTick);
				armyMoveSlider.setMinorTickSpacing(1);
				armyMoveSlider.setPaintTicks(true);
				armyMoveSlider.setPaintLabels(true);
				armyMoveSlider.addChangeListener(new ChangeListener() {

					public void stateChanged(ChangeEvent changeEvent) {
						JSlider theSlider = (JSlider) changeEvent.getSource();
						if (!theSlider.getValueIsAdjusting()) {
							movePane.setInputValue(theSlider.getValue());
						}
					}

				});
				movePane.setInputValue(army);
				movePane.setMessage(new Object[] { cotyOld.name + " --> " + coty.name, armyMoveSlider });
				movePane.setMessageType(JOptionPane.QUESTION_MESSAGE);
				movePane.setOptionType(JOptionPane.DEFAULT_OPTION);
				movePane.createDialog(frame, "Move!").setVisible(true);

				int m = Integer.parseInt(movePane.getInputValue().toString());
				for (int j = 0; j < m; j++) {
					cotyOld.subSoldiers();
					coty.addSoldiers();
				}
			}

		});
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Set<Entry<Polygon, Country>> cmapES = cmap.entrySet();
		cmapES.stream().forEach(pc -> fillOnePolygon(g, pc.getKey(), pc.getValue()));

		g.setColor(Color.BLACK);
		cmapES.parallelStream().forEach(pc -> drawOnePolygon(g, pc.getKey(), pc.getValue()));
	}

	private void fillOnePolygon(Graphics g, Polygon poly, Country cotyP) {
		Player ply = cotyP.king();
		// draw Country with Player color
		if (ply != null) {
			g.setColor(ply.color);
			if (antiGreyCoties != null && !antiGreyCoties.contains(cotyP))
				g.setColor(Color.GRAY);
			g.fillPolygon(poly);
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
		for (int x : poly.xpoints) {
			sumX += x;
		}
		int sumY = 0;
		for (int y : poly.ypoints) {
			sumY += y;
		}
		return new Point(sumX / n, sumY / n);
	}

}